import { Outlet } from 'react-router'
import { Logo } from '@/components/common/Logo'
import { NeonBackground } from '@/components/common/NeonBackground'

export function AuthLayout() {
  return (
    <div className="relative grid min-h-screen place-items-center px-4 py-6">
      <NeonBackground />
      <div className="relative z-1 grid w-full max-w-115 grid-cols-1 items-center gap-12 desktop:max-w-270 desktop:grid-cols-[1.1fr_0.9fr]">
        <section className="hidden flex-col items-center gap-2 text-center desktop:flex">
          <Logo size={340} className="animate-float drop-shadow-neon-lg" />
          <h1 className="-mt-9 text-[clamp(2rem,4vw,3rem)] font-extrabold tracking-[0.18em]">
            STOCK <span className="text-gradient-energy">DRAGON</span>
          </h1>
          <p className="tracking-[0.08em] text-silver">AI-powered terminal for market analysis.</p>
        </section>

        <main className="glass gradient-border relative rounded-3xl px-6 py-8 shadow-[0_30px_80px_rgb(0_0_0/60%),0_0_60px_rgb(123_44_255/18%)] desktop:px-9 desktop:py-10">
          <Logo size={84} className="mx-auto -mt-3 mb-2 drop-shadow-neon desktop:hidden" />
          <Outlet />
        </main>
      </div>
    </div>
  )
}
