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
  /** Should reject with an Error (message is shown to the user) when the request fails. */
  onSubmit: (values: AuthFormValues) => Promise<void>
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
  const [submitting, setSubmitting] = useState(false)
  const [formError, setFormError] = useState<string | null>(null)
  const [errors, setErrors] = useState<Partial<Record<keyof AuthFormValues, string>>>({})

  const handleSubmit = async (event: FormEvent) => {
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

    if (Object.keys(next).length > 0) return

    setFormError(null)
    setSubmitting(true)
    try {
      await onSubmit({ email: email.trim(), password })
    } catch (error) {
      setFormError(error instanceof Error ? error.message : 'Something went wrong. Please try again.')
    } finally {
      setSubmitting(false)
    }
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

      {formError && (
        <p
          role="alert"
          className="rounded-control border border-negative/45 bg-negative/10 px-3.5 py-2.5 text-[0.9rem] text-negative"
        >
          {formError}
        </p>
      )}

      <Button type="submit" fullWidth disabled={submitting}>
        <SubmitIcon size={17} />
        {submitting ? 'Please wait…' : submitLabel}
      </Button>

      <p className="text-center text-[0.9rem] text-silver [&_a]:ml-1.5 [&_a]:font-semibold [&_a]:transition-[text-shadow] [&_a:hover]:[text-shadow:0_0_12px_var(--color-cyan)]">{footer}</p>
    </form>
  )
}
