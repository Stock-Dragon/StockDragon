# Stock Dragon — frontend

React + Vite + TypeScript. Run: `npm install && npm run dev` (or the "Frontend - Start Dev Server" IntelliJ configuration).

## Structure (`src/`)

| Path | Purpose |
|---|---|
| `pages/` | One folder per screen (login, register, main page, wallet, instruments, administration). Page-specific parts live next to the page. |
| `components/common/` | Reusable, domain-agnostic building blocks (Button, TextField, Card, ...). |
| `components/layout/` | App shells: `AppLayout` (sidebar) and `AuthLayout`. |
| `components/auth/` | Feature components shared by login/register. |
| `router/` | Route table (`index.tsx`), URL constants (`paths.ts`), sidebar tabs (`navigation.ts`). |
| `assets/` | Images (logo). |
| `styles/index.css` | Tailwind entry: design tokens (`@theme`), element defaults, and the few custom utilities (`glass`, `label-caps`, gradients). |
| `services/` | Backend communication: `httpClient` (fetch wrapper, bearer token, `ApiError`), `authService`, `tokenStorage`. |
| `utils/` | Small framework-agnostic helpers (`cn` for joining class names, `jwt` token expiry helpers). |

Import with the `@/` alias, e.g. `import { Button } from '@/components/common/Button'`.

## Styling rules (Tailwind CSS v4)
- Style with Tailwind classes directly in components; no per-component CSS files.
- Palette, fonts, radii, glows and animations live in `@theme` in `styles/index.css` and become classes (`bg-purple`, `text-cyan`, `rounded-card`, `animate-float`, `shadow-glow-cyan`).
- Don't hardcode brand colors — use the theme names.
- A complex look used in 2+ places becomes a `@utility` in `styles/index.css`.
- Class names must be written in full (never built from string pieces) so Tailwind can detect them.
- Responsive: mobile-first; the sidebar becomes permanent at the custom `desktop:` breakpoint (900px).

## Authentication
- Backend: `POST /api/v1/auth/login` and `/register` return `{ token, email }` (JWT, sent back as `Authorization: Bearer <token>`).
- `components/auth/AuthProvider` keeps the session (localStorage), signs the user out when the token expires or the API answers 401. Use `useAuth()` for `email`, `login`, `register`, `logout`.
- Routes: `RequireAuth` protects the app, `GuestOnly` protects login/register (see `router/index.tsx`).
- Dev: the backend has no CORS config, so Vite proxies `/api` to `http://localhost:8080` (override with `VITE_API_PROXY_TARGET`).
