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
| `utils/` | Small framework-agnostic helpers (`cn` for joining class names). |

Import with the `@/` alias, e.g. `import { Button } from '@/components/common/Button'`.
Not created yet (added when needed): `services/` (API calls).

## Styling rules (Tailwind CSS v4)
- Style with Tailwind classes directly in components; no per-component CSS files.
- Palette, fonts, radii, glows and animations live in `@theme` in `styles/index.css` and become classes (`bg-purple`, `text-cyan`, `rounded-card`, `animate-float`, `shadow-glow-cyan`).
- Don't hardcode brand colors — use the theme names.
- A complex look used in 2+ places becomes a `@utility` in `styles/index.css`.
- Class names must be written in full (never built from string pieces) so Tailwind can detect them.
- Responsive: mobile-first; the sidebar becomes permanent at the custom `desktop:` breakpoint (900px).
