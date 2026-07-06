import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { taskApi, TaskResponse } from '../api/taskApi'
import type { TaskStats } from '../api/taskApi'
import { authApi } from '../api/authApi'
import { getApiErrorMessage } from '../api/client'
import { clearTokens } from '../utils/auth'
import TaskList from '../components/TaskList'
import TaskForm from '../components/TaskForm'
import TaskStatsComponent from '../components/TaskStats'
import FilterTabs from '../components/FilterTabs'

interface TodoPageProps {
  setIsAuthenticated: (value: boolean) => void
}

export default function TodoPage({ setIsAuthenticated }: TodoPageProps) {
  const navigate = useNavigate()
  const [tasks, setTasks] = useState<TaskResponse[]>([])
  const [stats, setStats] = useState<TaskStats | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [filter, setFilter] = useState<'all' | 'pending' | 'completed'>('all')
  const [currentPage, setCurrentPage] = useState(1)
  const [totalPages, setTotalPages] = useState(1)
  const [editingTask, setEditingTask] = useState<TaskResponse | null>(null)

  useEffect(() => {
    loadTasks()
    loadStats()
  }, [filter, currentPage])

  const loadTasks = async () => {
    setLoading(true)
    setError('')
    try {
      let response
      if (filter === 'all') {
        response = await taskApi.getTasks(currentPage, 10)
      } else {
        const status = filter === 'pending' ? 'PENDING' : 'COMPLETED'
        response = await taskApi.getTasksByStatus(status, currentPage, 10)
      }
      setTasks(response.data.content)
      setTotalPages(response.data.totalPages)
    } catch (err: unknown) {
      setError(getApiErrorMessage(err, 'Không thể tải danh sách công việc'))
    } finally {
      setLoading(false)
    }
  }

  const loadStats = async () => {
    try {
      const response = await taskApi.getTaskStats()
      setStats(response.data)
    } catch {
      // Silent error
    }
  }

  const handleCreateTask = async (title: string, description: string) => {
    try {
      await taskApi.createTask({ title, description })
      setCurrentPage(1)
      loadTasks()
      loadStats()
    } catch (err: unknown) {
      setError(getApiErrorMessage(err, 'Không thể tạo công việc'))
    }
  }

  const handleUpdateTask = async (id: number, title: string, description: string) => {
    try {
      await taskApi.updateTask(id, { title, description })
      loadTasks()
      loadStats()
      setEditingTask(null)
    } catch (err: unknown) {
      setError(getApiErrorMessage(err, 'Không thể cập nhật công việc'))
    }
  }

  const handleToggleTask = async (id: number) => {
    try {
      await taskApi.toggleTaskStatus(id)
      loadTasks()
      loadStats()
    } catch (err: unknown) {
      setError(getApiErrorMessage(err, 'Không thể thay đổi trạng thái'))
    }
  }

  const handleDeleteTask = async (id: number) => {
    if (!confirm('Bạn có chắc muốn xóa công việc này?')) return
    try {
      await taskApi.deleteTask(id)
      loadTasks()
      loadStats()
    } catch (err: unknown) {
      setError(getApiErrorMessage(err, 'Không thể xóa công việc'))
    }
  }

  const handleLogout = async () => {
    try {
      await authApi.logout()
    } catch {
      // Silent error
    } finally {
      clearTokens()
      setIsAuthenticated(false)
      navigate('/login')
    }
  }

  const handleFilterChange = (newFilter: 'all' | 'pending' | 'completed') => {
    setFilter(newFilter)
    setCurrentPage(1)
  }

  return (
    <div className="page-bg">
      <header className="sticky top-0 z-10 border-b border-white/60 bg-white/80 backdrop-blur-md">
        <div className="mx-auto flex max-w-5xl items-center justify-between px-4 py-4">
          <div className="flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-indigo-500 to-purple-600 text-white shadow-md">
              ✓
            </div>
            <div>
              <h1 className="text-xl font-bold text-slate-900">My Tasks</h1>
              <p className="text-xs text-slate-500">Quản lý công việc hàng ngày</p>
            </div>
          </div>
          <button onClick={handleLogout} className="btn-danger text-sm">
            Đăng xuất
          </button>
        </div>
      </header>

      <main className="mx-auto max-w-5xl px-4 py-8">
        {error && (
          <div className="mb-6 rounded-xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">
            {error}
          </div>
        )}

        {stats && <TaskStatsComponent stats={stats} />}

        <FilterTabs filter={filter} onChange={handleFilterChange} />

        {editingTask ? (
          <div className="mb-8">
            <h2 className="mb-4 text-lg font-semibold text-slate-800">Chỉnh sửa công việc</h2>
            <TaskForm
              initialTitle={editingTask.title}
              initialDescription={editingTask.description || ''}
              onSubmit={(title, desc) => handleUpdateTask(editingTask.id, title, desc)}
              onCancel={() => setEditingTask(null)}
              isEditing
            />
          </div>
        ) : (
          <TaskForm onSubmit={handleCreateTask} />
        )}

        {loading ? (
          <div className="flex justify-center py-16">
            <div className="h-8 w-8 animate-spin rounded-full border-4 border-indigo-200 border-t-indigo-600" />
          </div>
        ) : tasks.length === 0 ? (
          <div className="rounded-2xl border border-dashed border-slate-200 bg-white/60 py-16 text-center">
            <p className="text-4xl">📝</p>
            <p className="mt-3 text-slate-500">
              {filter === 'all' ? 'Chưa có công việc nào' : `Không có công việc ${filter === 'pending' ? 'đang chờ' : 'đã hoàn thành'}`}
            </p>
          </div>
        ) : (
          <>
            <TaskList
              tasks={tasks}
              onToggle={handleToggleTask}
              onEdit={setEditingTask}
              onDelete={handleDeleteTask}
            />

            {totalPages > 1 && (
              <div className="mt-8 flex items-center justify-center gap-3">
                <button
                  onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                  disabled={currentPage === 1}
                  className="btn-secondary disabled:opacity-40"
                >
                  Trước
                </button>
                <span className="rounded-xl bg-white px-4 py-2 text-sm font-medium text-slate-600 shadow-sm">
                  Trang {currentPage} / {totalPages}
                </span>
                <button
                  onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                  disabled={currentPage === totalPages}
                  className="btn-secondary disabled:opacity-40"
                >
                  Sau
                </button>
              </div>
            )}
          </>
        )}
      </main>
    </div>
  )
}
