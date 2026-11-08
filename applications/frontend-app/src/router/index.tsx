import { createBrowserRouter, Navigate } from 'react-router'
import { GuestOnly, RequireAuth } from '@/components/auth/RouteGuards'
import { AdministrationPage } from '@/pages/AdministrationPage/AdministrationPage'
import { AppLayout } from '@/components/layout/AppLayout'
import { AuthLayout } from '@/components/layout/AuthLayout'
import { InstrumentsPage } from '@/pages/InstrumentsPage/InstrumentsPage'
import { LoginPage } from '@/pages/LoginPage/LoginPage'
import { MainPage } from '@/pages/MainPage/MainPage'
import { RegisterPage } from '@/pages/RegisterPage/RegisterPage'
import { paths } from '@/router/paths'
import { WalletPage } from '@/pages/WalletPage/WalletPage'

export const router = createBrowserRouter([
  {
    // Public screens, only for signed-out users (centered card on branded backdrop)
    element: <GuestOnly />,
    children: [
      {
        element: <AuthLayout />,
        children: [
          { path: paths.login, element: <LoginPage /> },
          { path: paths.register, element: <RegisterPage /> },
        ],
      },
    ],
  },
  {
    // Application, only for signed-in users (sidebar + content)
    element: <RequireAuth />,
    children: [
      {
        element: <AppLayout />,
        children: [
          { path: paths.main, element: <MainPage /> },
          { path: paths.wallet, element: <WalletPage /> },
          { path: paths.instruments, element: <InstrumentsPage /> },
          { path: paths.administration, element: <AdministrationPage /> },
        ],
      },
    ],
  },
  { path: '*', element: <Navigate to={paths.main} replace /> },
])
