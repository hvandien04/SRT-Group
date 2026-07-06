import apiClient from './client'

export interface TaskRequest {
  title: string
  description?: string
}

export interface UpdateTaskRequest {
  title: string
  description?: string
  status?: string
}

export interface TaskResponse {
  id: number
  title: string
  description?: string
  status: string
  createdAt: string
  updatedAt: string
}

export interface PagedResponse<T> {
  content: T[]
  pageNumber: number
  pageSize: number
  totalElements: number
  totalPages: number
  isLastPage: boolean
}

export interface TaskStats {
  totalTasks: number
  completedTasks: number
  pendingTasks: number
}

export const taskApi = {
  createTask: (data: TaskRequest) =>
    apiClient.post<TaskResponse>('/tasks', data),

  getTasks: (page: number = 1, size: number = 10) =>
    apiClient.get<PagedResponse<TaskResponse>>('/tasks', {
      params: { page, size },
    }),

  getTasksByStatus: (status: string, page: number = 1, size: number = 10) =>
    apiClient.get<PagedResponse<TaskResponse>>('/tasks/filter', {
      params: { status, page, size },
    }),

  getTask: (id: number) =>
    apiClient.get<TaskResponse>(`/tasks/${id}`),

  updateTask: (id: number, data: UpdateTaskRequest) =>
    apiClient.put<TaskResponse>(`/tasks/${id}`, data),

  deleteTask: (id: number) =>
    apiClient.delete(`/tasks/${id}`),

  toggleTaskStatus: (id: number) =>
    apiClient.patch<TaskResponse>(`/tasks/${id}/toggle`),

  getTaskStats: () =>
    apiClient.get<TaskStats>('/tasks/stats/overview'),
}
