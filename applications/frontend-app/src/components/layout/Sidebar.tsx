import { NavLink, useNavigate } from 'react-router'
import { LogOut, X } from 'lucide-react'
import { Logo } from '@/components/common/Logo'
import { navItems } from '@/router/navigation'
import { paths } from '@/router/paths'
import { cn } from '@/utils/cn'

interface SidebarProps {
  open: boolean
  onClose: () => void
}

export function Sidebar({ open, onClose }: SidebarProps) {
  const navigate = useNavigate()

  return (
    <>
      {/* Dimmed backdrop behind the mobile drawer */}
      <div
        className={cn(
          'fixed inset-0 z-25 bg-void/70 transition-opacity duration-300 desktop:hidden',
          open ? 'opacity-100' : 'pointer-events-none opacity-0',
        )}
        onClick={onClose}
      />

      <aside
        className={cn(
          'fixed inset-y-0 left-0 z-30 flex w-66 flex-col border-r border-line px-4 py-5',
          'bg-linear-to-b from-obsidian/95 to-void/95 backdrop-blur-xl',
          'transition-transform duration-300 ease-snappy desktop:translate-x-0',
          open ? 'translate-x-0' : '-translate-x-full',
          // neon edge
          'after:pointer-events-none after:absolute after:inset-y-0 after:-right-px after:w-px after:opacity-60',
          'after:bg-linear-to-b after:from-purple after:via-transparent after:via-40% after:to-cyan',
        )}
      >
        <div className="flex items-center justify-between px-2 pt-1 pb-5.5">
          <div className="flex items-center gap-2.5">
            <Logo size={60} className="drop-shadow-neon" />
            <span className="font-display text-[0.95rem] leading-[1.15] tracking-[0.2em]">
              STOCK
              <br />
              <b className="text-gradient-ice font-extrabold">DRAGON</b>
            </span>
          </div>
          <button
            type="button"
            className="cursor-pointer text-silver desktop:hidden"
            onClick={onClose}
            aria-label="Close menu"
          >
            <X size={20} />
          </button>
        </div>

        <nav className="flex flex-1 flex-col gap-1.5" aria-label="Main navigation">
          <span className="label-caps px-3 pb-2">Navigation</span>
          {navItems.map(({ label, to, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              end
              onClick={onClose}
              className={({ isActive }) =>
                cn(
                  'relative flex items-center gap-3 rounded-control px-3.5 py-3 font-medium transition duration-300 ease-snappy',
                  isActive
                    ? 'bg-linear-to-r from-purple/30 to-blue/10 text-white shadow-[inset_0_0_0_1px_rgb(176_38_255/40%),0_0_22px_rgb(176_38_255/18%)]'
                    : 'text-silver hover:translate-x-0.75 hover:bg-violet/12 hover:text-ice',
                  // active marker on the left edge
                  isActive &&
                    'before:absolute before:top-[20%] before:bottom-[20%] before:-left-4 before:w-0.75 before:rounded-r-sm before:bg-cyan before:shadow-glow-cyan',
                )
              }
            >
              {({ isActive }) => (
                <>
                  <Icon size={19} className={cn(isActive && 'text-cyan drop-shadow-[0_0_6px_var(--color-cyan)]')} />
                  {label}
                </>
              )}
            </NavLink>
          ))}
        </nav>

        <div className="flex flex-col gap-3 border-t border-line pt-4">
          <div className="flex items-center gap-3 px-2">
            <span className="grid size-9.5 flex-none place-items-center rounded-full bg-gradient-pulse font-display text-xs font-bold shadow-glow-purple">
              SD
            </span>
            <div>
              <p className="max-w-37.5 truncate text-[0.82rem]">demo@stockdragon.io</p>
              <p className="text-[0.72rem] text-silver">Analyst</p>
            </div>
          </div>
          <button
            type="button"
            className="flex cursor-pointer items-center gap-2.5 rounded-control border border-line px-3.5 py-2.5 text-silver transition duration-300 hover:border-negative/55 hover:text-negative hover:shadow-[0_0_20px_rgb(255_51_102/18%)]"
            onClick={() => navigate(paths.login)}
          >
            <LogOut size={17} />
            Log out
          </button>
        </div>
      </aside>
    </>
  )
}
