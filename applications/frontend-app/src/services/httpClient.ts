import { tokenStorage } from '@/services/tokenStorage'

const API_BASE = '/api/v1'

export class ApiError extends Error {
  readonly status: number

  constructor(status: number, message: string) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

/** Called when an authenticated request is rejected with 401 (expired / invalid token). */
let onUnauthorized: (() => void) | null = null
export function setUnauthorizedHandler(handler: (() => void) | null) {
  onUnauthorized = handler
}

interface RequestOptions {
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
  body?: unknown
}

export async function request<T>(path: string, { method = 'GET', body }: RequestOptions = {}): Promise<T> {
  const token = tokenStorage.read()?.token

  let response: Response
  try {
    response = await fetch(`${API_BASE}${path}`, {
      method,
      headers: {
        ...(body !== undefined && { 'Content-Type': 'application/json' }),
        ...(token && { Authorization: `Bearer ${token}` }),
      },
      body: body !== undefined ? JSON.stringify(body) : undefined,
    })
  } catch {
    throw new ApiError(0, 'Cannot reach the server. Please try again later.')
  }

  if (!response.ok) {
    // A 401 on a request that carried a token means the session is no longer valid.
    if (response.status === 401 && token) onUnauthorized?.()
    throw new ApiError(response.status, await readErrorMessage(response))
  }

  return (response.status === 204 ? undefined : await response.json()) as T
}

/** Backend errors look like { "error": "message" }; anything else gets a generic text. */
async function readErrorMessage(response: Response): Promise<string> {
  try {
    const data = (await response.json()) as { error?: string }
    if (typeof data.error === 'string' && data.error.length < 200) return data.error
  } catch {
    /* body is not JSON */
  }
  return response.status >= 500 ? 'Server error. Please try again later.' : 'Request failed. Check your data and try again.'
}
