import { useState } from 'react'
import { TaskResponse } from '../api/taskApi'

interface TaskItemProps {
  task: TaskResponse
  onToggle: (id: number) => Promise<void>
  onEdit: (task: TaskResponse) => void
  onDelete: (id: number) => Promise<void>
}

export default function TaskItem({
  task,
  onToggle,
  onEdit,
  onDelete,
}: TaskItemProps) {
  const [deleting, setDeleting] = useState(false)
  const [toggling, setToggling] = useState(false)

  const handleToggle = async () => {
    setToggling(true)
    try {
      await onToggle(task.id)
    } finally {
      setToggling(false)
    }
  }

  const handleDelete = async () => {
    setDeleting(true)
    try {
      await onDelete(task.id)
    } finally {
      setDeleting(false)
    }
  }

  const isCompleted = task.status === 'COMPLETED'

  return (
    <div
      className={`group rounded-2xl border bg-white/90 p-4 shadow-sm transition-all hover:shadow-md ${
        isCompleted ? 'border-emerald-100' : 'border-slate-100'
      }`}
    >
      <div className="flex items-start gap-4">
        <button
          type="button"
          onClick={handleToggle}
          disabled={toggling}
          className={`mt-0.5 flex h-6 w-6 shrink-0 items-center justify-center rounded-lg border-2 transition-all ${
            isCompleted
              ? 'border-emerald-500 bg-emerald-500 text-white'
              : 'border-slate-300 hover:border-indigo-400'
          }`}
        >
          {isCompleted && (
            <svg className="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
            </svg>
          )}
        </button>

        <div className="min-w-0 flex-1">
          <h3
            className={`text-base font-semibold ${
              isCompleted ? 'text-slate-400 line-through' : 'text-slate-900'
            }`}
          >
            {task.title}
          </h3>
          {task.description && (
            <p className={`mt-1 text-sm ${isCompleted ? 'text-slate-300' : 'text-slate-500'}`}>
              {task.description}
            </p>
          )}
          <div className="mt-2 flex items-center gap-2">
            <span
              className={`rounded-full px-2.5 py-0.5 text-xs font-medium ${
                isCompleted
                  ? 'bg-emerald-50 text-emerald-600'
                  : 'bg-amber-50 text-amber-600'
              }`}
            >
              {isCompleted ? 'Hoàn thành' : 'Đang chờ'}
            </span>
            <span className="text-xs text-slate-400">
              {new Date(task.createdAt).toLocaleDateString('vi-VN')}
            </span>
          </div>
        </div>

        <div className="flex gap-2 opacity-70 transition-opacity group-hover:opacity-100">
          <button
            onClick={() => onEdit(task)}
            className="rounded-lg bg-indigo-50 px-3 py-1.5 text-xs font-medium text-indigo-600 hover:bg-indigo-100"
          >
            Sửa
          </button>
          <button
            onClick={handleDelete}
            disabled={deleting}
            className="rounded-lg bg-rose-50 px-3 py-1.5 text-xs font-medium text-rose-600 hover:bg-rose-100 disabled:opacity-50"
          >
            {deleting ? '...' : 'Xóa'}
          </button>
        </div>
      </div>
    </div>
  )
}
