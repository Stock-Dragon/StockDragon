import { Card } from '@/components/common/Card'
import { Sparkline } from '@/components/common/Sparkline'
import { StatusBadge, type ServiceStatus } from '@/components/common/StatusBadge'
import type { ServiceInfo } from './mockServices'

const SPARK_COLORS: Record<ServiceStatus, string> = {
  operational: 'var(--color-cyan)',
  degraded: 'var(--color-warning)',
  outage: 'var(--color-negative)',
}

export function ServiceCard({ service }: { service: ServiceInfo }) {
  const { name, description, status, uptime, latencyMs, history } = service

  return (
    <Card className="flex flex-col">
      <div className="flex items-start justify-between gap-3">
        <div>
          <h3 className="text-base">{name}</h3>
          <p className="mt-0.5 text-[0.85rem] text-silver">{description}</p>
        </div>
        <StatusBadge status={status} />
      </div>

      <div className="mt-auto flex items-end gap-5.5 pt-6.5">
        <Metric label="Uptime 30d" value={uptime} />
        <Metric label="Latency" value={status === 'outage' ? '—' : `${latencyMs} ms`} />
        <div className="ml-auto">
          <Sparkline values={history} color={SPARK_COLORS[status]} width={96} />
        </div>
      </div>
    </Card>
  )
}

function Metric({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <span className="label-caps block">{label}</span>
      <span className="block font-display text-[1.15rem] font-semibold whitespace-nowrap">{value}</span>
    </div>
  )
}
