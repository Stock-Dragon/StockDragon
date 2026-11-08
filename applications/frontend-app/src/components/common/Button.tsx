import type { ButtonHTMLAttributes } from 'react'
import { cn } from '@/utils/cn'

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'ghost'
  fullWidth?: boolean
}

const base =
  'relative inline-flex cursor-pointer items-center justify-center gap-2.5 overflow-hidden rounded-control px-6.5 py-3.25 ' +
  'font-display text-[0.85rem] font-semibold tracking-[0.12em] uppercase ' +
  'transition-[transform,box-shadow,background-position,border-color,color] duration-300 ease-snappy ' +
  'active:translate-y-0 active:scale-[0.98] disabled:cursor-not-allowed disabled:opacity-50'

const variants = {
  // gradient with a light sweep on hover
  primary:
    'bg-gradient-pulse bg-[length:200%_100%] text-white shadow-[0_0_22px_rgb(176_38_255/35%)] ' +
    'hover:-translate-y-0.5 hover:bg-[position:100%_0] hover:shadow-[0_0_32px_rgb(176_38_255/55%),0_0_60px_rgb(41_121_255/30%)] ' +
    'after:absolute after:inset-0 after:-translate-x-[120%] after:bg-[linear-gradient(105deg,transparent_30%,rgb(255_255_255/35%)_50%,transparent_70%)] ' +
    'after:transition-transform after:duration-700 after:ease-snappy hover:after:translate-x-[120%]',
  ghost:
    'border border-line-strong bg-transparent text-silver hover:border-cyan hover:text-ice hover:shadow-glow-cyan',
}

export function Button({
  variant = 'primary',
  fullWidth = false,
  className,
  type = 'button',
  children,
  ...rest
}: ButtonProps) {
  return (
    <button type={type} className={cn(base, variants[variant], fullWidth && 'w-full', className)} {...rest}>
      <span className="relative z-1 inline-flex items-center gap-2">{children}</span>
    </button>
  )
}
