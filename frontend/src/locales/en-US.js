export default {
  app: {
    title: 'Doc Review Console',
    subtitle: 'Vue 3 + JavaScript frontend starter'
  },
  shell: {
    brandEyebrow: 'Workflow Seed',
    workspaceEyebrow: 'Current module',
    securityNote: 'In-memory session and permission directive are enabled by default.',
    operatorName: 'Console Operator',
    role: 'Review Operations',
    toggleSidebar: 'Toggle sidebar',
    locales: {
      zhCN: 'Chinese',
      enUS: 'English'
    },
    theme: {
      sunrise: 'Sunrise',
      slate: 'Slate'
    }
  },
  nav: {
    dashboard: 'Dashboard',
    dashboardDescription: 'Delivery pulse and runtime snapshot',
    reviews: 'Review queue',
    reviewsDescription: 'Browse sample work items and actions',
    settings: 'Workspace settings',
    settingsDescription: 'Framework configuration and conventions'
  },
  common: {
    active: 'Active',
    stable: 'Stable',
    planned: 'Planned',
    enabled: 'Enabled',
    disabled: 'Disabled',
    compact: 'Compact',
    expanded: 'Expanded',
    approve: 'Approve',
    reject: 'Reject',
    escalate: 'Escalate',
    backHome: 'Back to dashboard'
  },
  tech: {
    vue3: 'Vue 3',
    vueRouter: 'Vue Router',
    viteJs: 'Vite + JS',
    pinia: 'Pinia',
    router: 'Router',
    axios: 'Axios',
    i18n: 'i18n',
    vueI18n: 'vue-i18n',
    permissionDirective: 'v-permission'
  },
  request: {
    error: {
      unknown: 'Request failed. Please try again later.'
    }
  },
  dashboard: {
    hero: {
      eyebrow: 'Frontend foundation',
      title: 'One shell for routing, state, and page scaffolding',
      description: 'This starter provides layout, i18n, permission placeholders, and an HTTP layer so follow-up modules can expand on a consistent structure.',
      primaryAction: 'Open review queue',
      secondaryAction: 'Open workspace settings'
    },
    metrics: {
      pending: {
        label: 'Pending reviews',
        hint: 'Covers the sample review flow'
      },
      velocity: {
        label: 'Assembly speed',
        hint: 'Lazy-loaded pages with a shared shell'
      },
      coverage: {
        label: 'Capability coverage',
        hint: 'Router / Pinia / i18n / Axios included'
      },
      latency: {
        label: 'Request extension point',
        hint: 'Interceptors and error normalization are ready'
      }
    },
    stages: {
      title: 'Layering',
      intake: {
        title: 'Entry layer',
        description: 'Bootstrapped by Vite, env variables, and route definitions.'
      },
      workspace: {
        title: 'Workspace layer',
        description: 'Provides shared navigation, page headers, and responsive content.'
      },
      service: {
        title: 'Service layer',
        description: 'Request handling, permissions, and stores are reusable infrastructure.'
      }
    },
    activity: {
      title: 'Delivered sample surface',
      first: {
        title: 'Dashboard summarizes scaffold status',
        meta: 'Designed to expose current framework capabilities and expansion paths.'
      },
      second: {
        title: 'Review queue can absorb future workflows',
        meta: 'Ready to be swapped from mock state to real APIs.'
      },
      third: {
        title: 'Workspace settings expose framework toggles',
        meta: 'Locale, navigation, and theme are driven by central state.'
      }
    },
    checklist: {
      title: 'Delivery checks',
      first: 'Axios interceptors and normalized request errors are wired in.',
      second: 'Visible copy is centralized in the locales directory.',
      third: 'v-permission demonstrates button-level access control.'
    }
  },
  reviews: {
    title: 'Review queue example',
    summary: 'A card queue simulates document review work items and can later be backed by the real API.',
    stats: {
      total: 'Open items',
      urgent: 'Expiring soon',
      permissions: 'Granted permissions'
    },
    filters: {
      all: 'All',
      highRisk: 'High risk',
      expiring: 'Expiring soon'
    },
    card: {
      submitted: 'Submitted',
      type: 'Type',
      owner: 'Owner',
      sla: 'Remaining SLA',
      status: 'Status'
    },
    status: {
      pending: 'Pending',
      approved: 'Approved',
      rejected: 'Rejected',
      escalated: 'Escalated'
    },
    risk: {
      high: 'High risk',
      medium: 'Medium risk',
      low: 'Low risk'
    },
    feedback: {
      approved: 'The document was marked as approved.',
      rejected: 'The document was marked as rejected.',
      escalated: 'The item was escalated for manual follow-up.'
    },
    items: {
      first: {
        title: 'Vendor onboarding playbook',
        owner: 'Compliance team'
      },
      second: {
        title: 'External partnership contract',
        owner: 'Legal team'
      },
      third: {
        title: 'Privacy policy revision log',
        owner: 'Data governance'
      }
    },
    tags: {
      policy: 'Policy',
      contract: 'Contract',
      privacy: 'Privacy',
      urgent: 'Urgent',
      external: 'External'
    },
    empty: 'No items match the current filter.'
  },
  settings: {
    title: 'Workspace settings',
    description: 'Framework-level controls without pulling in business dependencies.',
    runtime: {
      title: 'Runtime configuration',
      apiBase: 'API base URL',
      appTitle: 'Application title',
      locale: 'Default locale',
      sidebar: 'Sidebar mode',
      theme: 'Visual theme'
    },
    modules: {
      title: 'Built-in modules',
      router: 'Routing shell',
      state: 'State management',
      request: 'Request layer',
      i18n: 'Internationalization',
      permission: 'Permission directive'
    },
    conventions: {
      title: 'Engineering conventions',
      first: 'Page modules use route-level lazy loading.',
      second: 'Session state remains in memory instead of localStorage.',
      third: 'All visible copy is sourced from locales.'
    },
    controls: {
      title: 'Interactive toggles',
      compact: 'Toggle compact sidebar',
      locale: 'Switch locale',
      theme: 'Switch theme'
    },
    permissions: {
      title: 'Current session permissions'
    }
  },
  notFound: {
    title: 'Page not found',
    description: 'The current URL does not match a configured route.',
    action: 'Back to dashboard'
  }
}
