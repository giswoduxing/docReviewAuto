# 前端开发框架

基于 `Vue 3 + JavaScript + Vite` 的前端开发框架，包含以下能力：

- Vue Router 路由框架
- Pinia 状态管理
- Vue I18n 国际化
- Axios 请求封装
- `v-permission` 权限指令
- 响应式仪表盘、审核队列、工作台设置示例页面

## 启动要求

- Node.js `>= 18.18.0`
- npm `>= 9`

## 本地开发

```bash
npm install
npm run dev
```

默认开发地址：`http://localhost:5173`

## 生产构建

```bash
npm run build
npm run preview
```

## 工具链升级说明

- 当前项目已升级到 `vite@^6.4.1`，用于引入 `esbuild@^0.25.0`，避开 `GHSA-67mh-4wv8-2f99` 涉及的 `esbuild <= 0.24.2` 风险范围
- 现有 `@vitejs/plugin-vue@5.2.4` 仍兼容 `vite ^6`，因此不需要额外升级 Vue 插件
- 本项目现有 `vite.config.js` 仅使用别名、开发服务器 host/port 与 `/api` 代理，未使用 Vite 6 迁移说明里需要调整的高级解析配置，因此升级后无需额外改动
- `/api` 代理目标仍保持为 `http://127.0.0.1:18080`，本地开发启动与生产构建命令保持不变

## 环境变量

| 变量 | 说明 | 默认值 |
| --- | --- | --- |
| `VITE_API_BASE_URL` | 后端 API 基础地址 | `/api` |
| `VITE_APP_TITLE` | 页面标题前缀 | `Doc Review Console` |

## 目录结构

```text
frontend
├─ src
│  ├─ assets/styles        # 全局样式和设计令牌
│  ├─ components           # 可复用组件
│  ├─ layouts              # 页面骨架
│  ├─ locales              # 国际化文案
│  ├─ plugins              # i18n、权限指令
│  ├─ router               # 路由与导航配置
│  ├─ services             # Axios 请求封装
│  ├─ stores               # Pinia 状态
│  └─ views                # 页面级模块
├─ .env.development
├─ .env.production
├─ index.html
├─ jsconfig.json
├─ package.json
└─ vite.config.js
```

## 关键约定

- 默认通过内存态保存会话信息，避免将 token 写入 `localStorage`
- 路由页面统一使用懒加载，便于后续模块拆分
- 文案集中在 `src/locales`，便于中英文切换
- `src/services/http.js` 预留了统一拦截器和错误归一化入口
