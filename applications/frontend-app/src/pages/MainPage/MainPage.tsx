import { Activity, Gauge, ShieldCheck, TriangleAlert } from 'lucide-react'
import { Card } from '@/components/common/Card'
import { PageHeader } from '@/components/common/PageHeader'
import { mockServices } from './mockServices'
import { ServiceCard } from './ServiceCard'

const online = mockServices.filter((s) => s.status === 'operational').length
const incidents = mockServices.filter((s) => s.status !== 'operational').length
const measured = mockServices.filter((s) => s.status !== 'outage')
const avgLatency = Math.round(measured.reduce((sum, s) => sum + s.latencyMs, 0) / measured.length)

// Full class names (not built dynamically) so Tailwind can detect them.
const TONES = {
  cyan: 'bg-cyan/12 text-cyan ring-cyan/40 shadow-cyan/25',
  blue: 'bg-blue/12 text-blue ring-blue/40 shadow-blue/25',
  purple: 'bg-purple/12 text-purple ring-purple/40 shadow-purple/25',
  red: 'bg-negative/12 text-negative ring-negative/40 shadow-negative/25',
}

const summary = [
  { label: 'Services online', value: `${online}/${mockServices.length}`, icon: Activity, tone: 'cyan' },
  { label: 'Avg latency', value: `${avgLatency} ms`, icon: Gauge, tone: 'blue' },
  { label: 'Overall uptime', value: '98.2%', icon: ShieldCheck, tone: 'purple' },
  { label: 'Active incidents', value: String(incidents), icon: TriangleAlert, tone: 'red' },
] as const

export function MainPage() {
  return (
    <div className="flex flex-col gap-7">
      <PageHeader
        title="Main Page"
        subtitle={
          <>
            Status of the Stock Dragon platform services.{' '}
            <em className="text-warning not-italic">Mock data — live feed coming soon.</em>
          </>
        }
      />

      <section className="grid grid-cols-[repeat(auto-fit,minmax(220px,1fr))] gap-4" aria-label="Summary">
        {summary.map(({ label, value, icon: Icon, tone }) => (
          <Card key={label} className="flex items-center gap-4 px-5 py-4.5">
            <span
              className={`grid size-11 flex-none place-items-center rounded-control ring-1 shadow-[0_0_18px] ring-inset ${TONES[tone]}`}
            >
              <Icon size={20} />
            </span>
            <div>
              <span className="block font-display text-2xl font-bold">{value}</span>
              <span className="label-caps block">{label}</span>
            </div>
          </Card>
        ))}
      </section>

      <div className="flex flex-col gap-4">
        <h2 className="text-[0.85rem] tracking-[0.25em] text-silver uppercase">Services</h2>
        <section className="grid grid-cols-[repeat(auto-fill,minmax(min(340px,100%),1fr))] gap-4.5">
          {mockServices.map((service) => (
            <ServiceCard key={service.id} service={service} />
          ))}
        </section>
      </div>
    </div>
  )
}
