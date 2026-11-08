import { ChartCandlestick } from 'lucide-react'
import { PageHeader } from '@/components/common/PageHeader'
import { PlaceholderCard } from '@/components/common/PlaceholderCard'

export function InstrumentsPage() {
  return (
    <div className="flex flex-col gap-7">
      <PageHeader title="Instruments" subtitle="Browse and analyse tradable instruments." />
      <PlaceholderCard
        icon={ChartCandlestick}
        title="Instruments are coming soon"
        description="This is where the instrument list, charts and AI analysis will appear."
      />
    </div>
  )
}
