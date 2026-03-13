# 后台开发框架

## 技术栈

- Spring Boot 3.x
- JDK 17
- Spring AI Alibaba (DashScope)
- MyBatis-Plus
- MySQL 8+
- Redis

## 目录说明

- `src/main/java`: 后台源码
- `src/main/resources`: 配置和国际化资源
- `db/mysql/init.sql`: MySQL 初始化脚本
- `.mvn/`: 项目级 Maven 配置，规避宿主机默认仓库权限问题

## 本地验证

在 `backend/` 目录执行：

```bash
mvn clean test
mvn -DskipTests package
```

## 启动

```bash
mvn spring-boot:run
```

默认提供一个启动期管理员账号，用于验证受保护接口：

- username: `framework-admin`
- password: `ChangeMe_123456`

建议通过环境变量覆盖以下配置：

- `BOOTSTRAP_ADMIN_USERNAME`
- `BOOTSTRAP_ADMIN_PASSWORD`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `REDIS_HOST`
- `REDIS_PORT`
- `REDIS_PASSWORD`
- `DASHSCOPE_API_KEY`
- `DASHSCOPE_MODEL`

## 示例接口

- `GET /api/public/framework/ping`
- `GET /api/internal/framework/runtime`
- `POST /api/internal/assistant/completions`
- `GET /api/internal/users`
