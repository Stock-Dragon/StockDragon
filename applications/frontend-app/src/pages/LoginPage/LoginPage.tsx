import { Link } from 'react-router'
import { AuthForm } from '@/components/auth/AuthForm'
import { useAuth } from '@/components/auth/useAuth'
import { paths } from '@/router/paths'

export function LoginPage() {
  const { login } = useAuth()

  // On success the auth state changes and <GuestOnly> redirects into the app.
  return (
    <AuthForm
      mode="login"
      title="Welcome back"
      subtitle="Sign in to access your terminal."
      submitLabel="Sign in"
      passwordMinLength={1}
      onSubmit={login}
      footer={
        <>
          New to Stock Dragon?<Link to={paths.register}>Create account</Link>
        </>
      }
    />
  )
}
