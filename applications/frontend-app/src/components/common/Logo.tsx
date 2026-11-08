import logo from '@/assets/StockDragonLogoTransparentBackground.png'
import { cn } from '@/utils/cn'

interface LogoProps {
  /** Rendered size in px (the logo is square). */
  size?: number
  className?: string
}

export function Logo({ size = 48, className }: LogoProps) {
  return (
    <img
      src={logo}
      width={size}
      height={size}
      alt="Stock Dragon"
      draggable={false}
      className={cn('block select-none object-contain', className)}
    />
  )
}
