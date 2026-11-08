import { createContext } from 'react'
import type { Credentials } from '@/services/authService'

export interface AuthContextValue {
  /** Email of the signed-in user, or null when signed out. */
  email: string | null
  isAuthenticated: boolean
  login: (credentials: Credentials) => Promise<void>
  register: (credentials: Credentials) => Promise<void>
  logout: () => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)
