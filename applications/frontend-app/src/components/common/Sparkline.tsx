import { useId } from 'react'

interface SparklineProps {
  values: number[]
  /** CSS color of the line. */
  color?: string
  width?: number
  height?: number
}

/** Tiny dependency-free area chart, used for at-a-glance trends. */
export function Sparkline({
  values,
  color = 'var(--color-cyan)',
  width = 120,
  height = 36,
}: SparklineProps) {
  const gradientId = useId()
  const min = Math.min(...values)
  const max = Math.max(...values)
  const range = max - min || 1
  const step = width / (values.length - 1)

  const points = values.map((v, i) => [i * step, height - 3 - ((v - min) / range) * (height - 6)])
  const line = points.map(([x, y], i) => `${i === 0 ? 'M' : 'L'}${x.toFixed(1)},${y.toFixed(1)}`).join(' ')
  const area = `${line} L${width},${height} L0,${height} Z`

  return (
    <svg width={width} height={height} viewBox={`0 0 ${width} ${height}`} aria-hidden="true">
      <defs>
        <linearGradient id={gradientId} x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor={color} stopOpacity="0.35" />
          <stop offset="100%" stopColor={color} stopOpacity="0" />
        </linearGradient>
      </defs>
      <path d={area} fill={`url(#${gradientId})`} />
      <path d={line} fill="none" stroke={color} strokeWidth="1.8" strokeLinejoin="round" strokeLinecap="round" />
    </svg>
  )
}
