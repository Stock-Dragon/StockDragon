import { useId, useState, type InputHTMLAttributes, type ReactNode } from 'react'
import { Eye, EyeOff } from 'lucide-react'
import { cn } from '@/utils/cn'

interface TextFieldProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'size'> {
  label: string
  icon?: ReactNode
  error?: string
}

export function TextField({ label, icon, error, type = 'text', id, ...rest }: TextFieldProps) {
  const generatedId = useId()
  const inputId = id ?? generatedId
  const isPassword = type === 'password'
  const [revealed, setRevealed] = useState(false)

  return (
    <div className="flex flex-col gap-2">
      <label htmlFor={inputId} className="label-caps">
        {label}
      </label>
      <div
        className={cn(
          'group flex items-center gap-2.5 rounded-control border bg-void/70 px-3.5 transition duration-300',
          error
            ? 'border-negative shadow-[0_0_0_3px_rgb(255_51_102/12%)]'
            : 'border-line-strong hover:border-violet/60 focus-within:border-cyan focus-within:bg-void focus-within:shadow-[0_0_0_3px_rgb(0_239_255/12%),0_0_22px_rgb(0_239_255/22%)]',
        )}
      >
        {icon && <span className="inline-flex text-silver transition-colors group-focus-within:text-cyan">{icon}</span>}
        <input
          id={inputId}
          className="min-w-0 flex-1 bg-transparent py-3.5 text-ice outline-0 placeholder:text-silver/45"
          type={isPassword && revealed ? 'text' : type}
          aria-invalid={error ? true : undefined}
          aria-describedby={error ? `${inputId}-error` : undefined}
          {...rest}
        />
        {isPassword && (
          <button
            type="button"
            className="inline-flex cursor-pointer rounded-md p-1 text-silver transition-colors hover:text-cyan"
            onClick={() => setRevealed((v) => !v)}
            aria-label={revealed ? 'Hide password' : 'Show password'}
          >
            {revealed ? <EyeOff size={18} /> : <Eye size={18} />}
          </button>
        )}
      </div>
      {error && (
        <p id={`${inputId}-error`} className="text-[0.8rem] text-negative" role="alert">
          {error}
        </p>
      )}
    </div>
  )
}
