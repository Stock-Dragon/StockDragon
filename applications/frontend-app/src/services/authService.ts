import { request } from '@/services/httpClient'

export interface Credentials {
  email: string
  password: string
}

/** Mirrors the backend `AuthResponse` record. */
export interface AuthResponse {
  token: string
  email: string
}

export const authService = {
  login: (credentials: Credentials) => request<AuthResponse>('/auth/login', { method: 'POST', body: credentials }),
  register: (credentials: Credentials) => request<AuthResponse>('/auth/register', { method: 'POST', body: credentials }),
}
