# User Management Login Spec

## Overview

The repository shall provide a persisted login and user-management module that supports bootstrap administration, role-based menu permissions, and timed account lockout.

## Requirements

### 1. Bootstrap Administrator

- The system shall seed a built-in `admin` account during initialization.
- The seeded administrator shall use the configured bootstrap password, defaulting to `A123456!`.
- The seeded administrator shall be assigned a management role that can access organization, role, and user administration.
- The seeded administrator password shall be stored as a BCrypt hash.

### 2. Authentication Flow

- The backend shall expose a public login API that accepts username and password.
- The backend shall expose protected logout and current-session APIs.
- Authentication state shall be maintained with a server-side session and an `HttpOnly` cookie.
- The frontend shall provide a login screen and redirect unauthenticated users away from protected routes.
- The frontend shall hydrate the current session on application bootstrap.

### 3. User, Role, and Organization Management

- The backend shall persist organizations, roles, users, user-role links, and role-permission links.
- The backend shall provide protected APIs to list and create organizations.
- The backend shall provide protected APIs to list and create roles with assigned menu permissions.
- The backend shall provide protected APIs to list and create users with assigned roles and organization membership.
- Newly created users shall be able to authenticate with their assigned username and password.

### 4. Role-Based Menu Permissions

- The backend shall define a catalog of supported menu permissions.
- Roles shall grant menu permissions through persisted role-permission assignments.
- The current-session API shall return the authenticated user profile, role summaries, and granted permission codes.
- The frontend navigation and route guards shall use the granted permission codes to control menu visibility and route access.
- Unauthorized access to protected management APIs shall be rejected server-side.

### 5. Failed Login Lockout

- The system shall track consecutive failed password attempts per user account.
- After 3 consecutive failed login attempts, the account shall be locked for 10 minutes.
- Successful login shall reset the failed-attempt counter and clear any expired lock metadata.
- Login attempts during the lock window shall be rejected with a lockout response.

### 6. Internationalization

- Backend success and error messages introduced by this module shall be sourced from message bundles.
- Frontend UI text introduced by this module shall be sourced from locale files.
- The module shall provide at least `zh-CN` and `en-US` copy for new visible strings.

### 7. Validation and Buildability

- Backend automated tests shall cover successful login, lockout behavior, and authorization-sensitive management flows.
- Frontend production build validation shall pass after the new routes and views are added.

## Validation

- `git pull --ff-only origin main`
- `mvn -q test`
- `npm install`
- `npm run build`
- Manual app walkthrough: `admin login -> create role/user -> verify restricted menus -> verify 3-failure lockout`
