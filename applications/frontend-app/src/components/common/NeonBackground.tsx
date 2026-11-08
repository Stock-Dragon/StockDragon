/** Fixed full-screen cyberpunk backdrop: grid + drifting neon orbs. */
export function NeonBackground() {
  return (
    <div className="pointer-events-none fixed inset-0 z-0 overflow-hidden bg-void" aria-hidden="true">
      <div className="absolute -top-40 -left-30 size-130 animate-drift rounded-full bg-purple opacity-40 blur-[110px]" />
      <div className="absolute -right-35 -bottom-55 size-140 animate-drift rounded-full bg-blue opacity-40 blur-[110px] [animation-delay:-8s]" />
      <div className="absolute top-[38%] right-[22%] size-65 animate-drift rounded-full bg-cyan opacity-12 blur-[110px] [animation-delay:-14s]" />
      <div className="bg-neon-grid absolute inset-0" />
      <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_center,transparent_40%,var(--color-void)_100%)]" />
    </div>
  )
}
