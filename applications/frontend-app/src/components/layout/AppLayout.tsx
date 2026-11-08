import { useState } from 'react'
import { Outlet } from 'react-router'
import { Menu } from 'lucide-react'
import { Logo } from '@/components/common/Logo'
import { NeonBackground } from '@/components/common/NeonBackground'
import { Sidebar } from './Sidebar'

export function AppLayout() {
  const [menuOpen, setMenuOpen] = useState(false)

  return (
    <div className="relative min-h-screen">
      <NeonBackground />
      <Sidebar open={menuOpen} onClose={() => setMenuOpen(false)} />

      {/* Top bar — mobile only */}
      <header className="sticky top-0 z-20 flex items-center gap-2.5 border-b border-line bg-void/85 px-4 py-2.5 backdrop-blur-md desktop:hidden">
        <button
          type="button"
          className="grid size-10 cursor-pointer place-items-center rounded-control border border-line-strong text-ice"
          onClick={() => setMenuOpen(true)}
          aria-label="Open menu"
        >
          <Menu size={22} />
        </button>
        <Logo size={36} className="drop-shadow-neon" />
        <span className="font-display text-[0.85rem] tracking-[0.2em]">STOCK DRAGON</span>
      </header>

      <main className="relative z-1 max-w-350 px-4 pt-6 pb-10 desktop:ml-66 desktop:px-11 desktop:pt-10 desktop:pb-14">
        <Outlet />
      </main>
    </div>
  )
}
