import apiClient from './client'

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
}

export interface AuthResponse {
  accessToken: string
  refreshToken: string
}

export const authApi = {
  login: (data: LoginRequest) =>
    apiClient.post<AuthResponse>('/auth', {
      username: data.username.trim(),
      password: data.password,
    }),

  register: (data: RegisterRequest) => {
    const payload = {
      username: data.username.trim(),
      email: data.email.trim(),
      password: data.password,
      // Backward compat: older backend DTO used passwordHash
      passwordHash: data.password,
    }
    return apiClient.post<AuthResponse>('/auth/register', payload)
  },

  logout: () => apiClient.post('/auth/logout'),

  refresh: () => apiClient.post<AuthResponse>('/auth/refresh'),
}
