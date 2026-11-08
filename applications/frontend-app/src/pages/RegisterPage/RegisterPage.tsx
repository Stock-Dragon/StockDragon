import { Link } from 'react-router'
import { AuthForm } from '@/components/auth/AuthForm'
import { useAuth } from '@/components/auth/useAuth'
import { paths } from '@/router/paths'

export function RegisterPage() {
  const { register } = useAuth()

  // The backend returns a token on registration, so the user is signed in right away.
  return (
    <AuthForm
      mode="register"
      title="Join the hunt"
      subtitle="Create your Stock Dragon account."
      submitLabel="Create account"
      passwordMinLength={8}
      onSubmit={register}
      footer={
        <>
          Already have an account?<Link to={paths.login}>Sign in</Link>
        </>
      }
    />
  )
}
