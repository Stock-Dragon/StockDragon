import type { HTMLAttributes, MouseEvent } from 'react'
import { cn } from '@/utils/cn'

/** Dark glass panel with a neon spotlight that follows the cursor. */
export function Card({ className, children, onMouseMove, ...rest }: HTMLAttributes<HTMLDivElement>) {
  const handleMouseMove = (event: MouseEvent<HTMLDivElement>) => {
    const rect = event.currentTarget.getBoundingClientRect()
    event.currentTarget.style.setProperty('--x', `${event.clientX - rect.left}px`)
    event.currentTarget.style.setProperty('--y', `${event.clientY - rect.top}px`)
    onMouseMove?.(event)
  }

  return (
    <div
      className={cn(
        'glass relative overflow-hidden rounded-card border border-line p-5.5',
        'transition duration-300 ease-snappy',
        'hover:-translate-y-0.75 hover:border-violet/55 hover:shadow-[0_12px_40px_rgb(0_0_0/50%),0_0_26px_rgb(123_44_255/22%)]',
        // cursor spotlight
        'before:pointer-events-none before:absolute before:inset-0 before:opacity-0 before:transition-opacity before:duration-300',
        'before:bg-[radial-gradient(260px_circle_at_var(--x,50%)_var(--y,0%),rgb(176_38_255/16%),transparent_70%)] hover:before:opacity-100',
        '*:relative',
        className,
      )}
      onMouseMove={handleMouseMove}
      {...rest}
    >
      {children}
    </div>
  )
}
