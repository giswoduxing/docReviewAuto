# CXWDSC-8 User Management Login Proposal

## Background

The current repository only provides framework scaffolding for backend security and frontend permissions.
Authentication is still implemented with a single in-memory HTTP Basic administrator, the database only stores basic user profile data, and the frontend exposes placeholder permission state instead of a real login flow.

The Linear ticket `CXWDSC-8` requires a usable user management login module with:

- a preset `admin` account with password `A123456!`
- organization, role, and user management after bootstrap login
- role-based menu visibility for different users
- account lockout after 3 consecutive failed passwords with a 10-minute unlock window
- reasonable attention to module completeness, security, and performance

## Scope

This change introduces a persisted authentication and authorization module across `backend/` and `frontend/`.
It includes:

- session-based login/logout/session APIs
- persisted users, roles, organizations, and role-permission assignments
- seeded bootstrap data for the built-in `admin` user and management role
- role-based menu authorization on backend and frontend
- failed-login tracking with 10-minute lockout behavior
- admin screens for organization, role, and user management
- automated backend validation and frontend build validation

This change does not include:

- password reset by SMS/email
- audit log screens
- fine-grained button permissions beyond the management/menu scope required by this ticket
- SSO or third-party identity provider integration

## Technical Plan

### Architecture

- Replace the in-memory `UserDetailsService` with persisted authentication backed by MyBatis-Plus entities and mappers.
- Use Spring Security with server-side session storage so credentials are not kept in browser persistent storage.
- Add authentication endpoints for login, logout, and current-session lookup.
- Extend the domain model with organizations, roles, user-role links, and role-permission links.
- Seed the default organization, administrator role, permissions, and `admin` user during application startup.
- Drive frontend menu visibility from the authenticated session payload rather than hard-coded placeholder permissions.
- Add dedicated views for organization, role, and user management that call the new protected APIs.

### Security Design

- Store user passwords with `BCryptPasswordEncoder`; never persist plaintext passwords.
- Keep authentication state in the server session and rely on `HttpOnly` session cookies instead of `localStorage`.
- Return structured JSON errors for unauthenticated, forbidden, invalid-credential, and lockout states.
- Enforce server-side authorization with `@PreAuthorize` on all non-public management APIs.
- Validate user creation, role creation, and login request payloads with JSR-303 annotations.
- Track failed password attempts per user and set a time-based lock when the configured threshold is reached.
- Reset failed-attempt counters after successful authentication or once a lockout window expires.

### Internationalization Design

- Add backend message keys for login success/failure, lockout, role validation, and management CRUD success responses.
- Add frontend locale keys for login form copy, validation prompts, menu labels, and management page content in both `zh-CN` and `en-US`.
- Keep backend response messages sourced from `MessageSource` and frontend UI text sourced from `vue-i18n`.
- Represent permission labels in the frontend through locale keys that map to backend permission codes.

### Performance Design

- Keep management list APIs paginated where the existing user module already uses `PageResult`.
- Add indexes for usernames, role codes, organization codes, and lookup tables used during login and authorization.
- Reuse a compact permission catalog instead of storing large menu payload blobs per session.
- Minimize frontend initial payload size by keeping routes lazy-loaded and only hydrating session state once per app bootstrap.

## File List

### Added

- `.openspec/specs/cxwdsc-8-user-management-login/spec.md`
- `backend/src/main/java/com/docreview/backend/auth/**`
- `backend/src/main/java/com/docreview/backend/organization/**`
- `backend/src/main/java/com/docreview/backend/role/**`
- `backend/src/test/java/com/docreview/backend/auth/**`
- `backend/src/test/resources/application.yml`
- `backend/src/main/resources/db/schema.sql`
- `frontend/src/views/auth/LoginView.vue`
- `frontend/src/views/organizations/OrganizationManagementView.vue`
- `frontend/src/views/roles/RoleManagementView.vue`
- `frontend/src/views/users/UserManagementView.vue`

### Updated

- `backend/src/main/java/com/docreview/backend/config/SecurityConfig.java`
- `backend/src/main/java/com/docreview/backend/config/FrameworkSecurityProperties.java`
- `backend/src/main/java/com/docreview/backend/DocReviewBackendApplication.java`
- `backend/src/main/java/com/docreview/backend/common/exception/*`
- `backend/src/main/java/com/docreview/backend/user/**`
- `backend/src/main/resources/application.yml`
- `backend/db/mysql/init.sql`
- `frontend/src/router/*`
- `frontend/src/stores/session.js`
- `frontend/src/components/navigation/*`
- `frontend/src/services/http.js`
- `frontend/src/locales/zh-CN.js`
- `frontend/src/locales/en-US.js`
- `frontend/src/assets/styles/main.css`

## Testing Plan

### Required Validation

- `git fetch origin --prune`
- `git pull --ff-only origin main`
- `mvn -q test`
- `npm install`
- `npm run build`

### Functional Validation

- Log in with the seeded `admin / A123456!` account and confirm organization, role, and user menus are visible.
- Create a role with a restricted menu set, create a user assigned to that role, and confirm the user only sees the permitted menus after login.
- Submit 3 consecutive wrong passwords for the created user and confirm the account is locked for 10 minutes.
- Confirm the same locked user cannot log in with the correct password until the lock expires.

## Risks and Follow-up Notes

- Existing placeholder review/settings pages may need later alignment with the new permission model if future tickets reactivate them.
- Active sessions will reflect the permissions captured at login time; immediate propagation of role changes to already-logged-in users can be addressed in a follow-up if required.
