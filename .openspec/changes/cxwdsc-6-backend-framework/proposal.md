# CXWDSC-6 后台开发框架提案

## 背景

当前仓库仅包含前后端占位 README，`backend/` 目录下不存在可构建的 Spring Boot 工程。为了支撑后续企业级业务开发，需要先生成一个基于 Spring Boot 3.x、JDK 17、Spring AI Alibaba、MyBatis-Plus、MySQL 8+、Redis 的后台脚手架，并补齐最小可运行的安全、国际化、数据访问与 AI 集成骨架。

## 目标

1. 在 `backend/` 下生成一个可构建的 Maven 后台工程。
2. 预置 Spring AI Alibaba、MyBatis-Plus、MySQL 8+、Redis 的依赖与配置入口。
3. 提供安全、i18n、分页查询、统一响应、统一异常处理等基础设施。
4. 提供一组示例接口，分别覆盖公开探活、受保护运行态信息、AI 调用入口、MyBatis-Plus 分页查询入口。
5. 提供初始化 SQL 与本地启动说明。

## 技术方案

### 工程结构

- `backend/pom.xml`
  - 采用 Spring Boot 3.x 父 POM，JDK 17。
  - 引入 `spring-boot-starter-web`、`spring-boot-starter-security`、`spring-boot-starter-validation`、`spring-boot-starter-data-redis`、`spring-boot-starter-actuator`、`spring-ai-alibaba-starter-dashscope`、`mybatis-plus-spring-boot3-starter`、`mysql-connector-j`。
- `backend/.mvn/`
  - 提供项目级 Maven 设置，避免依赖宿主机不可写的默认本地仓库。
- `backend/src/main/java`
  - 划分 `config`、`common`、`framework`、`assistant`、`user` 等包，方便后续增量扩展。
- `backend/src/main/resources`
  - 提供 `application.yml` 与 `i18n/messages*.properties`。
- `backend/db/mysql/init.sql`
  - 提供最小初始化表结构，满足 `created_at`、`updated_at`、`is_deleted` 和索引约束。

### 安全设计

- 采用 Spring Security + HTTP Basic 作为启动期最小鉴权方案。
- 公开接口仅开放 `/actuator/health` 和 `/api/public/**`。
- 非公开接口统一要求认证，并在控制器层通过 `@PreAuthorize("hasRole('ADMIN')")` 进行方法级保护。
- Controller 入参统一使用 `@Valid` 和 JSR-303 校验。
- MyBatis-Plus 查询示例仅选择所需字段，避免 `SELECT *`。
- 不在代码和配置中写入真实密钥；AI、MySQL、Redis 都通过环境变量覆盖。
- 统一异常处理不回显敏感配置，仅返回国际化后的错误信息。

### 国际化设计

- 用户可见消息统一收敛到 `src/main/resources/i18n/messages*.properties`。
- 统一使用 `MessageSource` + `Accept-Language` 解析 Locale。
- 键命名采用 `模块.功能.动作`，例如 `framework.public.ping.success`、`assistant.completion.success`。
- AI 系统提示模板中显式注入当前 Locale，满足多语言输出上下文要求。

### 性能设计

- DTO 优先使用 JDK 17 `record`。
- MyBatis-Plus 启用分页拦截器，示例查询显式分页。
- Redis 作为统一缓存实现，预设 TTL 与 key prefix，避免空值缓存。
- MySQL 默认连接配置使用 Hikari，设置 `initialization-fail-timeout=0`，保证本地未启动 MySQL 时框架仍可完成启动与非数据库路径验证。
- AI、MySQL、Redis 集成均采用“配置即接入”的骨架模式，启动时尽量避免对外部依赖做强耦合阻塞。

## 文件清单

- `.openspec/changes/cxwdsc-6-backend-framework/proposal.md`
- `backend/.gitignore`
- `backend/.mvn/maven.config`
- `backend/.mvn/settings.xml`
- `backend/pom.xml`
- `backend/README.md`
- `backend/db/mysql/init.sql`
- `backend/src/main/java/com/docreview/backend/**`
- `backend/src/main/resources/application.yml`
- `backend/src/main/resources/i18n/messages.properties`
- `backend/src/main/resources/i18n/messages_zh_CN.properties`
- `backend/src/main/resources/i18n/messages_en_US.properties`
- `backend/src/test/java/com/docreview/backend/framework/api/FrameworkEndpointTest.java`

## 测试计划

1. 运行 `mvn clean test`，验证上下文可启动、公开接口可访问、受保护接口需要认证。
2. 运行 `mvn -DskipTests package`，验证脚手架可打包。
3. 使用本地启动方式访问：
   - `GET /api/public/framework/ping`
   - `GET /api/internal/framework/runtime`（Basic Auth）
4. 检查 MyBatis-Plus、Redis、Spring AI Alibaba 的配置入口是否存在且不会阻塞基础启动链路。
