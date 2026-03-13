export default {
  app: {
    title: 'Doc Review Console',
    subtitle: 'User, role, and organization bootstrap center'
  },
  shell: {
    brandEyebrow: 'Admin Workspace',
    workspaceEyebrow: 'Current module',
    securityNote: 'Server-side sessions, BCrypt passwords, and timed lockout are active.',
    toggleSidebar: 'Toggle sidebar',
    logout: 'Logout',
    anonymous: 'Anonymous',
    noRole: 'No role assigned',
    theme: {
      sunrise: 'Sunrise',
      slate: 'Slate'
    }
  },
  nav: {
    dashboard: 'Dashboard',
    dashboardDescription: 'Bootstrap status and access summary',
    organizations: 'Organizations',
    organizationsDescription: 'Create and view organization nodes',
    roles: 'Roles',
    rolesDescription: 'Assign menu permissions to roles',
    users: 'Users',
    usersDescription: 'Create user accounts and role bindings'
  },
  common: {
    loading: 'Loading...',
    saving: 'Saving...',
    reset: 'Reset'
  },
  request: {
    error: {
      unknown: 'Request failed. Please try again later.'
    }
  },
  auth: {
    permission: {
      organization: {
        title: 'Organization management',
        description: 'Access the organization management menu'
      },
      role: {
        title: 'Role management',
        description: 'Access the role management menu'
      },
      user: {
        title: 'User management',
        description: 'Access the user management menu'
      }
    }
  },
  dashboard: {
    hero: {
      eyebrow: 'Bootstrap control',
      title: 'Seed the platform, then hand off access safely',
      description: 'Log in with the built-in administrator, create organizations, define menu-bearing roles, and provision new users who only see the routes their role allows.',
      emptyOrganization: 'No organization'
    },
    metrics: {
      identity: {
        label: 'Current operator',
        hint: 'Loaded from the authenticated session.'
      },
      roles: {
        label: 'Assigned roles',
        hint: 'Role summaries returned by the backend session API.'
      },
      permissions: {
        label: 'Menu permissions',
        hint: 'Navigation and guards use these permission codes.'
      },
      lockout: {
        label: 'Lockout policy',
        value: '3 / 10m',
        hint: 'Three consecutive failures lock the account for ten minutes.'
      }
    },
    quickStart: {
      title: 'Quick start'
    },
    checklist: {
      first: 'Confirm the built-in admin account can reach organization, role, and user management.',
      second: 'Create a restricted role and bind only the menu permissions that user should see.',
      third: 'Create a new user, sign in, and verify the navigation is limited to that role.'
    },
    session: {
      title: 'Session snapshot',
      username: 'Username',
      roles: 'Roles',
      permissions: 'Permissions'
    }
  },
  login: {
    eyebrow: 'Secure sign-in',
    title: 'Login',
    subtitle: 'Use the seeded administrator to bootstrap organizations, roles, and users.',
    form: {
      username: 'Username',
      usernamePlaceholder: 'Enter your username',
      password: 'Password',
      passwordPlaceholder: 'Enter your password',
      submit: 'Sign in'
    },
    defaultAccount: {
      title: 'Default administrator',
      description: 'Username `admin`, initial password `A123456!`.'
    },
    lockout: {
      title: 'Lockout policy',
      description: 'After 3 wrong passwords, the account is frozen for 10 minutes.'
    }
  },
  organizations: {
    form: {
      title: 'Create organization',
      description: 'Add departments or business units that new users can belong to.',
      name: 'Organization name',
      namePlaceholder: 'Example: Compliance Center',
      code: 'Organization code',
      codePlaceholder: 'Example: COMPLIANCE',
      parent: 'Parent organization',
      parentPlaceholder: 'No parent / root level',
      leader: 'Leader',
      leaderPlaceholder: 'Example: Alice Chen',
      submit: 'Create organization'
    },
    list: {
      title: 'Organization list',
      emptyTitle: 'No organizations yet',
      emptyDescription: 'Create the first organization node from the form.'
    },
    table: {
      name: 'Name',
      code: 'Code',
      parent: 'Parent',
      leader: 'Leader',
      status: 'Status'
    }
  },
  roles: {
    form: {
      title: 'Create role',
      description: 'Choose which menus the role should expose after login.',
      code: 'Role code',
      codePlaceholder: 'Example: USER_MANAGER',
      name: 'Role name',
      namePlaceholder: 'Example: User Manager',
      descriptionLabel: 'Role description',
      descriptionPlaceholder: 'Describe the operational boundary for this role.',
      permissions: 'Menu permissions',
      submit: 'Create role'
    },
    list: {
      title: 'Role list',
      emptyTitle: 'No roles yet',
      emptyDescription: 'Create a role and assign menu permissions.',
      builtIn: 'Built-in',
      noDescription: 'No description'
    }
  },
  users: {
    form: {
      title: 'Create user',
      description: 'Provision a login account, assign its organization, and bind one or more roles.',
      username: 'Username',
      usernamePlaceholder: 'Example: jdoe',
      password: 'Initial password',
      passwordPlaceholder: 'Use letters, numbers, and symbols',
      displayName: 'Display name',
      displayNamePlaceholder: 'Example: Jane Doe',
      email: 'Email',
      emailPlaceholder: 'Example: jane.doe@example.com',
      organization: 'Organization',
      organizationPlaceholder: 'Select an organization',
      roles: 'Role bindings',
      submit: 'Create user'
    },
    list: {
      title: 'User list ({total})',
      emptyTitle: 'No users yet',
      emptyDescription: 'Use the form to create the first non-admin account.'
    },
    table: {
      username: 'Username',
      displayName: 'Display name',
      organization: 'Organization',
      roles: 'Roles',
      status: 'Status'
    }
  },
  notFound: {
    title: 'Page not found',
    description: 'The current URL does not match a configured route.',
    action: 'Back to dashboard'
  }
}
