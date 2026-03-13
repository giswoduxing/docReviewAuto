# CXWDSC-6 后台开发框架归档

## 状态

- 完成日期: 2026-03-13
- 对应提案: `.openspec/changes/cxwdsc-6-backend-framework/proposal.md`

## 实现摘要

- 在 `backend/` 下生成了基于 Spring Boot 3.3.6、JDK 17 的 Maven 工程。
- 集成了 Spring Security、MessageSource i18n、MyBatis-Plus、MySQL 8+、Redis 和 Spring AI Alibaba DashScope 的基础骨架。
- 默认关闭 DashScope 自动装配，确保在未提供 API key 的本地环境中仍可启动；提供配置入口以便后续直接启用。
- 提供公开探活接口、受保护运行态接口、AI 请求入口和 MyBatis-Plus 分页示例接口。
- 补充了 MySQL 初始化脚本、项目级 Maven 设置和本地启动文档。

## 验证结果

- `mvn test` 通过。
- `mvn -DskipTests package` 通过。
- `java -jar target/doc-review-backend-0.0.1-SNAPSHOT.jar --server.port=18080` 成功启动，日志确认 Tomcat 在 `18080` 端口完成启动。

## 备注

- 运行态集成测试已覆盖公开接口和受保护接口的访问路径。
- MySQL、Redis、DashScope 默认采用“可配置但不阻塞启动”的骨架模式，真实连通性需在接入环境中配置对应凭据和实例。
