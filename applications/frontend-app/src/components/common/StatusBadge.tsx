import { cn } from '@/utils/cn'

export type ServiceStatus = 'operational' | 'degraded' | 'outage'

const LABELS: Record<ServiceStatus, string> = {
  operational: 'Operational',
  degraded: 'Degraded',
  outage: 'Outage',
}

// Full class names (not built dynamically) so Tailwind can detect them.
const TONES: Record<ServiceStatus, { badge: string; dot: string }> = {
  operational: { badge: 'border-positive/45 bg-positive/10 text-positive', dot: 'bg-positive shadow-[0_0_10px_var(--color-positive)]' },
  degraded: { badge: 'border-warning/45 bg-warning/10 text-warning', dot: 'bg-warning shadow-[0_0_10px_var(--color-warning)]' },
  outage: { badge: 'border-negative/45 bg-negative/10 text-negative', dot: 'bg-negative shadow-[0_0_10px_var(--color-negative)]' },
}

export function StatusBadge({ status }: { status: ServiceStatus }) {
  const tone = TONES[status]

  return (
    <span
      className={cn(
        'inline-flex items-center gap-2 rounded-full border py-1 pr-3 pl-2.5 text-[0.72rem] font-semibold tracking-widest uppercase',
        tone.badge,
      )}
    >
      <span className={cn('relative size-2 rounded-full', tone.dot)}>
        <span className={cn('absolute inset-0 animate-ping rounded-full', tone.dot)} />
      </span>
      {LABELS[status]}
    </span>
  )
}
