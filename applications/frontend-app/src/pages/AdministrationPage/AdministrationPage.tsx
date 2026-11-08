import { Settings2 } from 'lucide-react'
import { PageHeader } from '@/components/common/PageHeader'
import { PlaceholderCard } from '@/components/common/PlaceholderCard'

export function AdministrationPage() {
  return (
    <div className="flex flex-col gap-7">
      <PageHeader title="Administration" subtitle="Users, roles and platform configuration." />
      <PlaceholderCard
        icon={Settings2}
        title="Administration is coming soon"
        description="This is where user management and system settings will appear."
      />
    </div>
  )
}
