# spring-cloud-demo

企业级 Spring Cloud 脚手架（JDK 21、Spring Boot 3.x、Spring Cloud Alibaba、无 Lombok、无 MapStruct、五层架构 + 四层泛型基类）。

## 模块一览

| 模块 | 说明 |
|------|------|
| `cloud-common` | `BaseController` / `BaseService` / `BaseConverter` / `BaseHandler`、`Result`、`UserContext`、`GlobalException`、`JwtUtil`、`RedisUtils`、异步线程池、`logback`/MDC 约定、Boot 自动配置 |
| `cloud-gateway` | 路由、`JWT` 校验、`X-Login-UserId` / `X-Trace-Id` 透传、跨域、文档与健康检查白名单 |
| `cloud-auth` | 演示登录签发 JWT（密码固定 `123456`） |
| `cloud-system` | 内存用户表 + SKU 商品价格（Feign Demo 数据源） |
| `cloud-business` | 下单完整链路：**Controller → Service → Converter → Handler → Mapper**，含 Spock + Mockito 单测示例 |

数据库脚本：`sql/schema.sql`（接入 MySQL 时参考，当前演示默认不使用）。

## 环境约定

1. JDK **21**，Maven **3.9+**。
2. **Nacos（可选）**：仅在使用 `spring.profiles.active=nacos` 或 `NACOS_ENABLED=true` 做服务发现时需要。
3. 本 Demo **不要求 MySQL / Redis**：订单内存占位；`RedisUtils` 为未接 Redis 的占位实现。
4. **单机默认**：不写任何 profile 时 **不连 Nacos**；网关与各服务 **直连本机固定端口**——auth `9101`、system `9102`、business `9103`、gateway `9000`。需要注册中心时对**各模块**追加 `--spring.profiles.active=nacos` 并启动 Nacos（会加载各自的 `application-nacos.yml`）。

## JWT 密钥

各服务（网关、认证、接入统一返回体的业务服务）YAML 中的 `cloud.security.jwt.secret` **必须完全一致**。当前示例值为 UTF-8 明文（长度满足 HS256 建议）；生产务必改为安全配置源注入。

## 启动顺序建议（单机默认）

任选其一：

1. **默认（不写 profile）**：不启动 Nacos → 按需启动：`cloud-auth` → `cloud-system` → `cloud-business` → `cloud-gateway`。  
   - `cloud-business` 已通过 `spring.cloud.openfeign.client.config.cloud-system.url` 默认指向 `http://127.0.0.1:9102`。  
   - 网关路由默认指向 `http://127.0.0.1:9101|9102|9103`，可用 `GATEWAY_ROUTE_*` / `FEIGN_CLOUD_SYSTEM_URL` 覆盖。

2. **注册中心模式**：先启动 Nacos，各服务与网关均加 **`--spring.profiles.active=nacos`**（或 `SPRING_PROFILES_ACTIVE=nacos`），路由与 Feign 走 `lb://`。

3. **profile `local`**：与默认单机行为一致，并**强制**关闭 Nacos（即使误设 `NACOS_ENABLED=true` 也不会去连注册中心）。旧习惯可继续写 `spring.profiles.active=local`。

## 演示调用

### 登录拿 Token（经网关）

```http
POST http://localhost:9000/cloud-auth/api/auth/login
Content-Type: application/json

{"userId":1,"password":"123456"}
```

响应 `data.accessToken` 用作 `Authorization: Bearer <token>`。

### 提交订单（经网关）

```http
POST http://localhost:9000/cloud-business/api/orders/submit
Authorization: Bearer <token>
Content-Type: application/json
X-Trace-Id: optional-custom-trace

{"buyerUserId":1,"productCode":"SKU-DEMO","quantity":2}
```

`buyerUserId` **必须与 JWT 解析出的 UID 一致**（网关已将 UID 写入 `X-Login-UserId`，业务侧也可用 `UserContext` 校验）。

接口文档：`/doc.html`、`/swagger-ui.html`（在各服务端口或经网关前缀访问，注意 Knife4j 路径已列入网关白名单）。

## 编译与测试

```bash
mvn clean install
mvn test -pl cloud-business
```

- `cloud-business` 内含 **Spock/Groovy + JUnit Mockito** 示例。  
- `OrderServiceImpl#page` 当前为占位实现；若需分页，请在父 POM 管理 `mybatis-plus-jsqlparser` 依赖并启用 `PaginationInnerInterceptor`（见 MyBatis-Plus 官方文档）。

## 架构约束回顾

- 禁止 Lombok、禁止 MapStruct；实体/DTO/VO 均手写构造器与访问器。  
- 业务代码必须继承四大基类并走 **Controller → Service → Converter → Handler → Mapper**。  
- Feign、异步与第三方调用收口在 **Handler**；对象转换收口在 **Converter**。  
- 异步线程中需**手动**设置/清理 `UserContext` 与 MDC（示例见 `OrderAsyncNotifyService`）。
