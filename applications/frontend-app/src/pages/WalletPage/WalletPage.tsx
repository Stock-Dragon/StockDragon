import { Wallet } from 'lucide-react'
import { PageHeader } from '@/components/common/PageHeader'
import { PlaceholderCard } from '@/components/common/PlaceholderCard'

export function WalletPage() {
  return (
    <div className="flex flex-col gap-7">
      <PageHeader title="Wallet" subtitle="Your balances, positions and transaction history." />
      <PlaceholderCard
        icon={Wallet}
        title="Wallet is coming soon"
        description="This is where your portfolio value, open positions and history will appear."
      />
    </div>
  )
}
