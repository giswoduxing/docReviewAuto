# Frontend Framework Spec

## Overview

The repository shall provide a Vue 3 + JavaScript frontend foundation under `frontend/` so that later business modules can be built on a consistent application shell.

## Requirements

### 1. Application Bootstrap

- The frontend shall use `Vite` as the build baseline.
- The frontend shall bootstrap from `frontend/src/main.js`.
- The application shall register `Pinia`, `vue-i18n`, router, and custom directives during startup.

### 2. Routing and Layout

- The frontend shall expose a shared application shell layout for primary routes.
- The default route shall resolve to `/dashboard`.
- The application shall provide at least these routes:
  - `/dashboard`
  - `/reviews`
  - `/settings`
- The application shall provide a not-found route for unmatched paths.
- Route-level views shall be lazy loaded.

### 3. State and Session

- The frontend shall provide a Pinia app store for locale and shell UI state.
- The frontend shall provide a Pinia session store for in-memory session information.
- Session data shall not require `localStorage` for the default scaffold.

### 4. Internationalization

- Visible UI copy shall be centralized in locale files.
- The scaffold shall provide at least `zh-CN` and `en-US` locales.
- Switching locale shall update page-level UI copy.
- Switching locale shall keep the browser title aligned with the current page title.

### 5. Request Layer

- The frontend shall expose a shared Axios client wrapper.
- The request layer shall support credentialed requests.
- The request layer shall support an authorization header when an access token exists in memory.
- The request layer shall normalize error payloads into a shared object shape.

### 6. Permissions

- The frontend shall include a reusable `v-permission` directive.
- The scaffold shall use the directive to demonstrate button-level access control on at least one example page.

### 7. Example Screens

- The scaffold shall include a dashboard example page.
- The scaffold shall include a review queue example page.
- The scaffold shall include a settings example page.
- The example pages shall render inside the shared application shell.

### 8. Documentation

- The frontend shall include a README that documents installation, development startup, production build, environment variables, and directory structure.

## Validation

- `git pull --ff-only origin main`
- `npm install`
- `npm run build`

## Current Status

- Repository-local scaffold implementation is present.
- Repository-local proposal documentation exists under `.openspec/changes/cxwdsc-7-frontend-framework/proposal.md`.
- Runtime validation remains pending because the current session does not provide a usable JavaScript runtime or package manager.
