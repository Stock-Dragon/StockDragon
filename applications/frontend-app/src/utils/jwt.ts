/** Returns the token expiry as a timestamp in ms, or null when the token can't be decoded. */
export function getJwtExpiry(token: string): number | null {
  try {
    const payload = token.split('.')[1]
    const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'))
    const exp = (JSON.parse(json) as { exp?: number }).exp
    return typeof exp === 'number' ? exp * 1000 : null
  } catch {
    return null
  }
}

export function isJwtExpired(token: string): boolean {
  const expiry = getJwtExpiry(token)
  return expiry === null || expiry <= Date.now()
}
