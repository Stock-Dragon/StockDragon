import { ChartCandlestick, LayoutDashboard, Settings2, Wallet, type LucideIcon } from 'lucide-react'
import { paths } from '@/router/paths'

export interface NavItem {
  label: string
  to: string
  icon: LucideIcon
}

/** Tabs shown in the sidebar. Add a page here to make it appear in the navigation. */
export const navItems: NavItem[] = [
  { label: 'Main Page', to: paths.main, icon: LayoutDashboard },
  { label: 'Wallet', to: paths.wallet, icon: Wallet },
  { label: 'Instruments', to: paths.instruments, icon: ChartCandlestick },
  { label: 'Administration', to: paths.administration, icon: Settings2 },
]
