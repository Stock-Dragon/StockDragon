import { createBrowserRouter, Navigate } from 'react-router'
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
    // Public screens (centered card on branded backdrop)
    element: <AuthLayout />,
    children: [
      { path: paths.login, element: <LoginPage /> },
      { path: paths.register, element: <RegisterPage /> },
    ],
  },
  {
    // Application shell (sidebar + content). Auth guard will be added with the API.
    element: <AppLayout />,
    children: [
      { path: paths.main, element: <MainPage /> },
      { path: paths.wallet, element: <WalletPage /> },
      { path: paths.instruments, element: <InstrumentsPage /> },
      { path: paths.administration, element: <AdministrationPage /> },
    ],
  },
  { path: '*', element: <Navigate to={paths.main} replace /> },
])
