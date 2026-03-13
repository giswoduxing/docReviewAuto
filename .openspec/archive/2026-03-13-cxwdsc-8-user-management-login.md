# CXWDSC-8 User Management Login Archive

## Status

- Completed on: 2026-03-13
- Proposal: `.openspec/changes/cxwdsc-8-user-management-login/proposal.md`
- Spec: `.openspec/specs/cxwdsc-8-user-management-login/spec.md`

## Implementation Summary

- Replaced the placeholder in-memory administrator with persisted session-based authentication, logout, and current-session APIs.
- Seeded the built-in `admin` account, root organization, administrator role, and menu permissions during application startup.
- Added persisted organization, role, role-permission, user, and user-role data models with protected management APIs.
- Added frontend login, session bootstrap, permission-aware routing, and organization/role/user management views.
- Implemented 3-attempt failed-login lockout with a 10-minute recovery window and localized backend/frontend messaging.

## Validation Results

- `mvn -q test` passed, including `AuthFlowIntegrationTest` coverage for seeded admin login, restricted-user permissions, and lockout recovery.
- `npm install` completed successfully.
- `npm run build` passed.
- Frontend route visibility proof confirmed admin users see `dashboard`, `organizations`, `roles`, and `users`, while restricted users only see the routes allowed by their permission codes.

## Notes

- Follow-up Linear issue `CXWDSC-12` tracks the out-of-scope Vite/esbuild advisory remediation discovered during `npm audit`.
- Session state uses `HttpOnly` cookies; no browser persistent token storage was introduced.
