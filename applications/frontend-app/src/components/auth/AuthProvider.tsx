import { useCallback, useEffect, useMemo, useState, type ReactNode } from 'react'
import { authService, type AuthResponse, type Credentials } from '@/services/authService'
import { setUnauthorizedHandler } from '@/services/httpClient'
import { tokenStorage, type StoredSession } from '@/services/tokenStorage'
import { getJwtExpiry, isJwtExpired } from '@/utils/jwt'
import { AuthContext, type AuthContextValue } from './AuthContext'

/** Restores the session from storage, dropping it if the token has already expired. */
function loadSession(): StoredSession | null {
  const session = tokenStorage.read()
  if (!session || isJwtExpired(session.token)) {
    tokenStorage.clear()
    return null
  }
  return session
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [session, setSession] = useState<StoredSession | null>(loadSession)

  const startSession = useCallback(({ token, email }: AuthResponse) => {
    const next = { token, email }
    tokenStorage.write(next)
    setSession(next)
  }, [])

  const logout = useCallback(() => {
    tokenStorage.clear()
    setSession(null)
  }, [])

  const login = useCallback(async (credentials: Credentials) => startSession(await authService.login(credentials)), [startSession])
  const register = useCallback(async (credentials: Credentials) => startSession(await authService.register(credentials)), [startSession])

  // Sign out automatically when the token expires, and when the API rejects it.
  useEffect(() => {
    setUnauthorizedHandler(logout)
    return () => setUnauthorizedHandler(null)
  }, [logout])

  useEffect(() => {
    if (!session) return
    const expiry = getJwtExpiry(session.token)
    if (expiry === null) return
    const timer = setTimeout(logout, Math.max(expiry - Date.now(), 0))
    return () => clearTimeout(timer)
  }, [session, logout])

  const value = useMemo<AuthContextValue>(
    () => ({ email: session?.email ?? null, isAuthenticated: session !== null, login, register, logout }),
    [session, login, register, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
