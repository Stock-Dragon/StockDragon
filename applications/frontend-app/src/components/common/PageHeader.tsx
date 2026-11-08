import type { ReactNode } from 'react'

interface PageHeaderProps {
  title: string
  subtitle?: ReactNode
}

export function PageHeader({ title, subtitle }: PageHeaderProps) {
  return (
    <header className="flex flex-col gap-1.5">
      <h1 className="text-gradient-energy w-fit text-[clamp(1.8rem,4vw,2.6rem)] font-extrabold">{title}</h1>
      {subtitle && <p className="text-silver">{subtitle}</p>}
    </header>
  )
}
