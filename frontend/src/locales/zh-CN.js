export default {
  app: {
    title: '文档审核控制台',
    subtitle: 'Vue 3 + JavaScript 前端开发基座'
  },
  shell: {
    brandEyebrow: '流程基座',
    workspaceEyebrow: '当前模块',
    securityNote: '默认启用内存态会话与权限指令占位',
    operatorName: '控制台值班员',
    role: '审核运营',
    toggleSidebar: '切换侧栏',
    locales: {
      zhCN: '中文',
      enUS: '英文'
    },
    theme: {
      sunrise: '日光',
      slate: '石墨'
    }
  },
  nav: {
    dashboard: '仪表盘',
    dashboardDescription: '查看交付节奏与运行面板',
    reviews: '审核队列',
    reviewsDescription: '浏览待处理样例并触发动作',
    settings: '工作台设置',
    settingsDescription: '查看框架配置与开发约定'
  },
  common: {
    active: '进行中',
    stable: '稳定',
    planned: '规划中',
    enabled: '已启用',
    disabled: '未启用',
    compact: '紧凑',
    expanded: '展开',
    approve: '通过',
    reject: '驳回',
    escalate: '升级',
    backHome: '返回仪表盘'
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
      unknown: '请求失败，请稍后重试'
    }
  },
  dashboard: {
    hero: {
      eyebrow: '前端基座',
      title: '统一路由、状态与页面骨架',
      description: '当前脚手架覆盖布局、国际化、权限占位和请求层封装，后续业务模块可直接沿用目录约定扩展。',
      primaryAction: '查看审核队列',
      secondaryAction: '工作台设置'
    },
    metrics: {
      pending: {
        label: '待处理审核',
        hint: '覆盖当前样例流程'
      },
      velocity: {
        label: '页面装配速度',
        hint: '使用懒加载与统一骨架'
      },
      coverage: {
        label: '基础能力覆盖',
        hint: '包含 Router / Pinia / i18n / Axios'
      },
      latency: {
        label: '请求层延展点',
        hint: '已保留拦截器与错误归一化'
      }
    },
    stages: {
      title: '模块分层',
      intake: {
        title: '入口层',
        description: '由 Vite、环境变量和路由定义组成启动面。'
      },
      workspace: {
        title: '工作台层',
        description: '统一提供侧栏、页头和响应式内容区。'
      },
      service: {
        title: '服务层',
        description: '请求拦截、权限指令和状态管理作为可复用基础设施。'
      }
    },
    activity: {
      title: '样例交付面',
      first: {
        title: '仪表盘展示交付状态',
        meta: '用于汇总脚手架当前能力与扩展方向。'
      },
      second: {
        title: '审核队列承接后续业务模块',
        meta: '可继续接入真实接口和审批流程。'
      },
      third: {
        title: '工作台设置暴露框架级开关',
        meta: '语言、导航和主题都通过状态管理统一驱动。'
      }
    },
    checklist: {
      title: '交付检查',
      first: '保留 Axios 拦截器和错误归一化入口',
      second: '将可见文案集中在 locales 目录',
      third: '通过 v-permission 演示按钮级权限控制'
    }
  },
  reviews: {
    title: '审核队列示例',
    summary: '以卡片式队列模拟文档审核场景，后续可直接替换为后端接口返回值。',
    stats: {
      total: '待审项目',
      urgent: '即将超时',
      permissions: '可用权限'
    },
    filters: {
      all: '全部',
      highRisk: '高风险',
      expiring: '即将超时'
    },
    card: {
      submitted: '提交时间',
      type: '文档类型',
      owner: '责任人',
      sla: '剩余 SLA',
      status: '状态'
    },
    status: {
      pending: '待处理',
      approved: '已通过',
      rejected: '已驳回',
      escalated: '已升级'
    },
    risk: {
      high: '高风险',
      medium: '中风险',
      low: '低风险'
    },
    feedback: {
      approved: '已将文档标记为通过',
      rejected: '已将文档标记为驳回',
      escalated: '已升级到人工复核'
    },
    items: {
      first: {
        title: '供应商准入流程说明书',
        owner: '合规组'
      },
      second: {
        title: '外部合作协议模板',
        owner: '法务组'
      },
      third: {
        title: '隐私条款修订记录',
        owner: '数据治理组'
      }
    },
    tags: {
      policy: '制度',
      contract: '合同',
      privacy: '隐私',
      urgent: '加急',
      external: '外部提交'
    },
    empty: '当前筛选条件下暂无项目'
  },
  settings: {
    title: '工作台设置',
    description: '在不引入业务依赖的前提下展示框架级配置面板。',
    runtime: {
      title: '运行时配置',
      apiBase: 'API 基地址',
      appTitle: '应用标题',
      locale: '默认语言',
      sidebar: '侧栏状态',
      theme: '视觉主题'
    },
    modules: {
      title: '内置模块',
      router: '路由骨架',
      state: '状态管理',
      request: '请求层',
      i18n: '国际化',
      permission: '权限指令'
    },
    conventions: {
      title: '开发约定',
      first: '页面模块统一使用路由懒加载。',
      second: '会话信息默认保留在内存态，避免写入 localStorage。',
      third: '所有可见文案都从 locales 中读取。'
    },
    controls: {
      title: '交互开关',
      compact: '切换紧凑侧栏',
      locale: '切换语言',
      theme: '切换主题'
    },
    permissions: {
      title: '当前会话权限'
    }
  },
  notFound: {
    title: '页面不存在',
    description: '当前地址未映射到已配置路由。',
    action: '返回仪表盘'
  }
}
