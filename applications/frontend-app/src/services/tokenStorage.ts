const STORAGE_KEY = 'stockdragon.session'

export interface StoredSession {
  token: string
  email: string
}

// Storage can throw (private mode, blocked site data) — fail soft, the user just logs in again.
export const tokenStorage = {
  read(): StoredSession | null {
    try {
      const raw = localStorage.getItem(STORAGE_KEY)
      return raw ? (JSON.parse(raw) as StoredSession) : null
    } catch {
      return null
    }
  },
  write(session: StoredSession): void {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(session))
    } catch {
      /* ignore */
    }
  },
  clear(): void {
    try {
      localStorage.removeItem(STORAGE_KEY)
    } catch {
      /* ignore */
    }
  },
}
