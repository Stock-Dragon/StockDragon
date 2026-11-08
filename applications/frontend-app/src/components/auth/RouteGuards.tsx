import { Navigate, Outlet } from 'react-router'
import { paths } from '@/router/paths'
import { useAuth } from './useAuth'

/** Renders nested routes only for signed-in users; everyone else goes to the login screen. */
export function RequireAuth() {
  const { isAuthenticated } = useAuth()
  return isAuthenticated ? <Outlet /> : <Navigate to={paths.login} replace />
}

/** Login / register are for signed-out users; signed-in users are sent to the app. */
export function GuestOnly() {
  const { isAuthenticated } = useAuth()
  return isAuthenticated ? <Navigate to={paths.main} replace /> : <Outlet />
}
