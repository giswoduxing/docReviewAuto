export default {
  app: {
    title: '文档审核控制台',
    subtitle: '用户、角色与组织引导中心'
  },
  shell: {
    brandEyebrow: '管理工作台',
    workspaceEyebrow: '当前模块',
    securityNote: '当前已启用服务端会话、BCrypt 密码存储与定时锁定策略。',
    toggleSidebar: '切换侧栏',
    logout: '退出登录',
    anonymous: '匿名用户',
    noRole: '未分配角色',
    theme: {
      sunrise: '日光',
      slate: '石墨'
    }
  },
  nav: {
    dashboard: '仪表盘',
    dashboardDescription: '查看引导状态与访问摘要',
    organizations: '组织架构',
    organizationsDescription: '创建并查看组织节点',
    roles: '角色管理',
    rolesDescription: '为角色配置菜单权限',
    users: '用户管理',
    usersDescription: '创建用户并绑定角色'
  },
  common: {
    loading: '加载中...',
    saving: '保存中...',
    reset: '重置'
  },
  request: {
    error: {
      unknown: '请求失败，请稍后重试。'
    }
  },
  auth: {
    permission: {
      organization: {
        title: '组织架构管理',
        description: '可访问组织架构管理菜单'
      },
      role: {
        title: '角色管理',
        description: '可访问角色管理菜单'
      },
      user: {
        title: '用户管理',
        description: '可访问用户管理菜单'
      }
    }
  },
  dashboard: {
    hero: {
      eyebrow: '初始化总控',
      title: '先完成平台引导，再安全地下发访问权限',
      description: '使用内置管理员登录，创建组织、定义带菜单权限的角色，再为新用户开通账号，让其仅看到角色允许的功能入口。',
      emptyOrganization: '未绑定组织'
    },
    metrics: {
      identity: {
        label: '当前操作人',
        hint: '来自已认证会话的个人信息。'
      },
      roles: {
        label: '已分配角色',
        hint: '角色摘要由后端会话接口返回。'
      },
      permissions: {
        label: '菜单权限数',
        hint: '导航菜单与路由守卫均依赖这些权限编码。'
      },
      lockout: {
        label: '锁定策略',
        value: '3 次 / 10 分钟',
        hint: '连续输错 3 次密码后，账号冻结 10 分钟。'
      }
    },
    quickStart: {
      title: '快速检查'
    },
    checklist: {
      first: '确认内置 admin 账号可进入组织架构、角色管理、用户管理。',
      second: '创建一个受限角色，只勾选该用户需要看到的菜单权限。',
      third: '创建新用户并重新登录，确认导航菜单只展示该角色允许的入口。'
    },
    session: {
      title: '当前会话快照',
      username: '用户名',
      roles: '角色',
      permissions: '权限'
    }
  },
  login: {
    eyebrow: '安全登录',
    title: '登录系统',
    subtitle: '使用预置管理员完成组织、角色与用户的初始化。',
    form: {
      username: '用户名',
      usernamePlaceholder: '请输入用户名',
      password: '密码',
      passwordPlaceholder: '请输入密码',
      submit: '登录'
    },
    defaultAccount: {
      title: '默认管理员',
      description: '用户名 `admin`，初始密码 `A123456!`。'
    },
    lockout: {
      title: '锁定规则',
      description: '连续输错 3 次密码后，账号将被冻结 10 分钟。'
    }
  },
  organizations: {
    form: {
      title: '创建组织',
      description: '新增部门或业务单元，供后续用户归属使用。',
      name: '组织名称',
      namePlaceholder: '例如：合规中心',
      code: '组织编码',
      codePlaceholder: '例如：COMPLIANCE',
      parent: '上级组织',
      parentPlaceholder: '无上级 / 根节点',
      leader: '负责人',
      leaderPlaceholder: '例如：陈晓',
      submit: '创建组织'
    },
    list: {
      title: '组织列表',
      emptyTitle: '暂无组织',
      emptyDescription: '请先在左侧表单创建第一个组织节点。'
    },
    table: {
      name: '名称',
      code: '编码',
      parent: '上级组织',
      leader: '负责人',
      status: '状态'
    }
  },
  roles: {
    form: {
      title: '创建角色',
      description: '为角色勾选登录后可见的菜单权限。',
      code: '角色编码',
      codePlaceholder: '例如：USER_MANAGER',
      name: '角色名称',
      namePlaceholder: '例如：用户管理员',
      descriptionLabel: '角色说明',
      descriptionPlaceholder: '描述该角色的职责边界。',
      permissions: '菜单权限',
      submit: '创建角色'
    },
    list: {
      title: '角色列表',
      emptyTitle: '暂无角色',
      emptyDescription: '创建角色并分配菜单权限。',
      builtIn: '内置',
      noDescription: '暂无说明'
    }
  },
  users: {
    form: {
      title: '创建用户',
      description: '为用户配置登录账号、所属组织与角色关系。',
      username: '用户名',
      usernamePlaceholder: '例如：jdoe',
      password: '初始密码',
      passwordPlaceholder: '请使用字母、数字与符号组合',
      displayName: '显示名称',
      displayNamePlaceholder: '例如：Jane Doe',
      email: '邮箱',
      emailPlaceholder: '例如：jane.doe@example.com',
      organization: '所属组织',
      organizationPlaceholder: '请选择组织',
      roles: '角色绑定',
      submit: '创建用户'
    },
    list: {
      title: '用户列表（{total}）',
      emptyTitle: '暂无用户',
      emptyDescription: '请先创建第一个非管理员账号。'
    },
    table: {
      username: '用户名',
      displayName: '显示名称',
      organization: '所属组织',
      roles: '角色',
      status: '状态'
    }
  },
  notFound: {
    title: '页面不存在',
    description: '当前地址未匹配到已配置的路由。',
    action: '返回仪表盘'
  }
}
