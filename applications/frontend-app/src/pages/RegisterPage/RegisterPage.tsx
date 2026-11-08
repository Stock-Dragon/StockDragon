import { Link, useNavigate } from 'react-router'
import { AuthForm } from '@/components/auth/AuthForm'
import { paths } from '@/router/paths'

export function RegisterPage() {
  const navigate = useNavigate()

  return (
    <AuthForm
      mode="register"
      title="Join the hunt"
      subtitle="Create your Stock Dragon account."
      submitLabel="Create account"
      passwordMinLength={8}
      // TODO: call the registration API. For now we go to the login screen.
      onSubmit={() => navigate(paths.login)}
      footer={
        <>
          Already have an account?<Link to={paths.login}>Sign in</Link>
        </>
      }
    />
  )
}
