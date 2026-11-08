import type { ServiceStatus } from '@/components/common/StatusBadge'

export interface ServiceInfo {
  id: string
  name: string
  description: string
  status: ServiceStatus
  uptime: string
  latencyMs: number
  /** Recent latency samples (ms) for the sparkline. */
  history: number[]
}

/** MOCK DATA — replaced by the real health API later. */
export const mockServices: ServiceInfo[] = [
  {
    id: 'backend-api',
    name: 'Backend API',
    description: 'Core REST gateway',
    status: 'operational',
    uptime: '99.98%',
    latencyMs: 42,
    history: [48, 44, 45, 41, 43, 39, 42, 40, 44, 42],
  },
  {
    id: 'auth',
    name: 'Auth Service',
    description: 'Sessions & user accounts',
    status: 'operational',
    uptime: '99.99%',
    latencyMs: 28,
    history: [30, 29, 31, 27, 28, 26, 29, 28, 27, 28],
  },
  {
    id: 'database',
    name: 'PostgreSQL',
    description: 'Primary data store',
    status: 'operational',
    uptime: '99.95%',
    latencyMs: 9,
    history: [10, 9, 11, 9, 8, 9, 10, 9, 8, 9],
  },
  {
    id: 'market-feed',
    name: 'Market Data Feed',
    description: 'Quotes & order book stream',
    status: 'degraded',
    uptime: '98.40%',
    latencyMs: 310,
    history: [120, 140, 180, 160, 220, 260, 240, 290, 330, 310],
  },
  {
    id: 'statements',
    name: 'Financial Statements',
    description: 'Filings ingestion pipeline',
    status: 'operational',
    uptime: '99.70%',
    latencyMs: 85,
    history: [90, 88, 84, 92, 86, 83, 85, 87, 84, 85],
  },
  {
    id: 'ai-engine',
    name: 'AI Analysis Engine',
    description: 'Signal & sentiment models',
    status: 'outage',
    uptime: '91.20%',
    latencyMs: 0,
    history: [210, 230, 260, 300, 380, 520, 700, 900, 0, 0],
  },
]
