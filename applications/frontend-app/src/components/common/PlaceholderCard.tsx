import type { LucideIcon } from 'lucide-react'
import { Card } from '@/components/common/Card'

interface PlaceholderCardProps {
  icon: LucideIcon
  title: string
  description: string
}

/** Temporary filler for sections that are not implemented yet. */
export function PlaceholderCard({ icon: Icon, title, description }: PlaceholderCardProps) {
  return (
    <Card className="flex min-h-72 flex-col items-center justify-center gap-4 border-dashed text-center">
      <span className="grid size-16 place-items-center rounded-card bg-violet/12 text-purple ring-1 shadow-glow-purple ring-purple/40 ring-inset">
        <Icon size={30} />
      </span>
      <h2 className="text-lg">{title}</h2>
      <p className="max-w-md text-silver">{description}</p>
      <span className="label-caps rounded-full border border-warning/45 bg-warning/10 px-3 py-1 text-warning">
        Under construction
      </span>
    </Card>
  )
}
