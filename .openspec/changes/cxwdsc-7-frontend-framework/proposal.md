# CXWDSC-7 Frontend Framework Proposal

## Background

The repository started with an empty `frontend` directory that only contained a placeholder `README.md`.
The Linear ticket `CXWDSC-7` requires a frontend development framework based on Vue 3 and JavaScript.
To make follow-up feature tickets implementable, the repository needs a runnable scaffold with routing, state management, internationalization, request handling, and an example shell layout.

## Scope

This change introduces a Vue 3 + JavaScript application scaffold under `frontend/` using Vite.
It includes:

- application bootstrap and Vite configuration
- route structure with a shared shell layout
- Pinia state stores
- vue-i18n locale setup
- Axios request wrapper
- `v-permission` directive placeholder
- dashboard, review queue, settings, and not-found example pages
- frontend setup documentation

This change does not include:

- real backend integration
- authentication flow implementation
- UI component library integration
- production deployment pipeline

## Technical Plan

### Architecture

- Use `Vite` as the build and dev server baseline.
- Use `Vue 3` with the Composition API and `<script setup>`.
- Keep the application entry in `src/main.js`.
- Register `Pinia`, `vue-i18n`, router, and custom directives during bootstrap.
- Use `src/layouts/AppShell.vue` as the shared shell for authenticated application pages.
- Place route-level views under `src/views` and lazy load them via router config.
- Centralize HTTP client behavior in `src/services/http.js`.

### Security Design

- The scaffold does not store tokens in `localStorage`; session state remains in memory via the Pinia session store.
- The request layer is configured with `withCredentials: true` to align with cookie-based auth when backend support is added later.
- A `v-permission` directive is included to provide a standard entry point for button-level authorization checks.
- The scaffold does not use `v-html` and does not render untrusted raw HTML.
- No secrets or environment-specific credentials are committed; only non-sensitive example env vars are added.

### Internationalization Design

- Visible UI copy is centralized in `src/locales/zh-CN.js` and `src/locales/en-US.js`.
- `vue-i18n` is initialized in `src/plugins/i18n.js`.
- Route titles, navigation labels, page content, and session placeholder labels all resolve through translation keys.
- The locale switcher updates application state through the Pinia app store.

### Performance Design

- Route views are lazy loaded in `src/router/routes.js` to keep the initial bundle smaller.
- The scaffold keeps the reactive surface narrow by using small Pinia stores instead of a large nested global state object.
- The request layer is isolated so later caching, retry, timeout, and telemetry policies can be added without touching page components.
- Styling is implemented as a single global stylesheet with tokenized CSS variables to avoid unnecessary framework overhead in the initial scaffold.

## File List

### Added

- `frontend/.env.development`
- `frontend/.env.production`
- `frontend/.gitignore`
- `frontend/index.html`
- `frontend/jsconfig.json`
- `frontend/package.json`
- `frontend/vite.config.js`
- `frontend/src/main.js`
- `frontend/src/App.vue`
- `frontend/src/assets/styles/main.css`
- `frontend/src/components/dashboard/KpiCard.vue`
- `frontend/src/components/navigation/SideNav.vue`
- `frontend/src/components/navigation/TopBar.vue`
- `frontend/src/layouts/AppShell.vue`
- `frontend/src/locales/zh-CN.js`
- `frontend/src/locales/en-US.js`
- `frontend/src/plugins/directives.js`
- `frontend/src/plugins/i18n.js`
- `frontend/src/router/index.js`
- `frontend/src/router/routes.js`
- `frontend/src/services/http.js`
- `frontend/src/stores/app.js`
- `frontend/src/stores/session.js`
- `frontend/src/views/dashboard/DashboardView.vue`
- `frontend/src/views/error/NotFoundView.vue`
- `frontend/src/views/reviews/ReviewQueueView.vue`
- `frontend/src/views/settings/WorkspaceSettingsView.vue`

### Updated

- `frontend/README.md`

## Testing Plan

### Required Validation

- `git pull --ff-only origin main`
- `npm install`
- `npm run build`

### Current Session Evidence

- Repository sync against `origin/main` completed cleanly before implementation.
- Static verification was completed for route targets, imports, i18n key usage, and shell wiring.
- Runtime validation is currently blocked in-session because no JavaScript runtime or package manager is available on `PATH` (`node`, `npm`, `bun`, `pnpm`, and `yarn` were all probed and not found).

## Risks and Follow-up Notes

- The scaffold currently uses example page data and placeholder session permissions; real backend contracts still need a later ticket.
- No component library is wired yet, so future tickets should either standardize on a library or keep the current custom CSS direction.
- Because runtime tooling is unavailable in this session, build validation remains pending and must be rerun in a Node-enabled environment before final handoff is considered complete.
