import { useState, type FormEvent, type ReactNode } from 'react'
import { Lock, LogIn, Mail, UserPlus } from 'lucide-react'
import { Button } from '@/components/common/Button'
import { TextField } from '@/components/common/TextField'

export interface AuthFormValues {
  email: string
  password: string
}

interface AuthFormProps {
  mode: 'login' | 'register'
  title: string
  subtitle: string
  submitLabel: string
  /** Minimal password length enforced on the client. */
  passwordMinLength: number
  onSubmit: (values: AuthFormValues) => void
  /** Link to the opposite screen, rendered under the form. */
  footer: ReactNode
}

const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

export function AuthForm({
  mode,
  title,
  subtitle,
  submitLabel,
  passwordMinLength,
  onSubmit,
  footer,
}: AuthFormProps) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [errors, setErrors] = useState<Partial<Record<keyof AuthFormValues, string>>>({})

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()

    const next: typeof errors = {}
    if (!EMAIL_PATTERN.test(email.trim())) next.email = 'Enter a valid email address.'
    if (password.length < passwordMinLength) {
      next.password =
        passwordMinLength > 1
          ? `Password must be at least ${passwordMinLength} characters.`
          : 'Enter your password.'
    }
    setErrors(next)

    if (Object.keys(next).length === 0) onSubmit({ email: email.trim(), password })
  }

  const SubmitIcon = mode === 'login' ? LogIn : UserPlus

  return (
    <form className="flex flex-col gap-5" onSubmit={handleSubmit} noValidate>
      <header className="mb-1 flex flex-col gap-1.5">
        <h2 className="text-[1.7rem] font-bold">{title}</h2>
        <p className="text-silver">{subtitle}</p>
      </header>

      <TextField
        label="Email"
        type="email"
        name="email"
        autoComplete="email"
        placeholder="you@stockdragon.io"
        icon={<Mail size={18} />}
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        error={errors.email}
      />
      <TextField
        label="Password"
        type="password"
        name="password"
        autoComplete={mode === 'login' ? 'current-password' : 'new-password'}
        placeholder="••••••••"
        icon={<Lock size={18} />}
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        error={errors.password}
      />

      <Button type="submit" fullWidth>
        <SubmitIcon size={17} />
        {submitLabel}
      </Button>

      <p className="text-center text-[0.9rem] text-silver [&_a]:ml-1.5 [&_a]:font-semibold [&_a]:transition-[text-shadow] [&_a:hover]:[text-shadow:0_0_12px_var(--color-cyan)]">{footer}</p>
    </form>
  )
}
