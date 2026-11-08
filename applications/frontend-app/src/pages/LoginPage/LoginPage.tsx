import { Link, useNavigate } from 'react-router'
import { AuthForm } from '@/components/auth/AuthForm'
import { paths } from '@/router/paths'

export function LoginPage() {
  const navigate = useNavigate()

  return (
    <AuthForm
      mode="login"
      title="Welcome back"
      subtitle="Sign in to access your terminal."
      submitLabel="Sign in"
      passwordMinLength={1}
      // TODO: call the auth API. For now we just enter the app.
      onSubmit={() => navigate(paths.main)}
      footer={
        <>
          New to Stock Dragon?<Link to={paths.register}>Create account</Link>
        </>
      }
    />
  )
}
