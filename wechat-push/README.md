# 系统通知管理平台

一个基于 Spring Boot 3.5.9 和 Java 17 的系统通知管理平台，集成了 MySQL、Redis、DingTalk 等功能。

## 项目结构

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── Application.java      # 主应用类
│   │   │           ├── controller/           # 控制器层
│   │   │           ├── service/              # 服务层接口
│   │   │           ├── service/impl/         # 服务层实现
│   │   │           ├── dto/                  # 数据传输对象
│   │   │           ├── entity/               # 实体类
│   │   │           ├── repository/           # 数据访问层
│   │   │           ├── config/               # 配置类
│   │   │           ├── common/               # 通用类
│   │   │           ├── exception/            # 异常处理
│   │   │           └── aspect/               # AOP 切面
│   │   └── resources/
│   │       ├── application.yml              # 主配置文件
│   │       ├── application-dev.yml          # 开发环境配置
│   │       ├── application-prod.yml         # 生产环境配置
│   │       ├── logback.xml                  # 日志配置
│   │       └── static/                       # 静态资源
│   │           ├── login.html               # 登录页面
│   │           └── todo.html                # 待办页面
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── ApplicationTest.java  # 单元测试
├── pom.xml                                   # Maven 配置
└── README.md                                 # 项目说明
```

## 功能特性

- **Spring Boot 3.5.9** - 现代 Java 框架
- **Java 17** - 现代 Java 版本
- **MySQL** - 关系型数据库
- **Redis** - 缓存数据库
- **DingTalk SDK** - 钉钉集成
- **JWT** - 无状态认证
- **Spring Security** - 权限管理
- **Swagger/OpenAPI** - API 文档
- **AOP 日志** - 请求/响应参数和执行时间记录
- **全局异常处理** - 统一异常处理
- **分层架构** - 清晰的代码结构
- **多环境配置** - 开发/生产环境分离

## 快速开始

### 前置条件

- Java 17 或更高版本
- Maven 3.6.0 或更高版本
- MySQL 8.0 或更高版本
- Redis 6.0 或更高版本

### 构建项目

```bash
mvn clean compile
```

### 运行测试

```bash
mvn test
```

### 打包应用

```bash
mvn clean package
```

这将在 `target` 目录中创建可执行 JAR 文件：
- `java-scaffold-1.0.0-SNAPSHOT.jar` - 基本 JAR
- `java-scaffold-1.0.0-SNAPSHOT-jar-with-dependencies.jar` - 包含所有依赖的 JAR

### 运行应用

```bash
java -jar target/java-scaffold-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

或使用 Maven：

```bash
mvn spring-boot:run
```

## 配置说明

### 数据库配置

在 `application-dev.yml` 和 `application-prod.yml` 文件中配置数据库连接信息。

### Redis 配置

在 `application-dev.yml` 和 `application-prod.yml` 文件中配置 Redis 连接信息。

### 钉钉配置

在 `application-dev.yml` 和 `application-prod.yml` 文件中配置钉钉应用信息。

### 系统通知配置

在 `application-dev.yml` 和 `application-prod.yml` 文件中配置系统通知 API 信息。

## 主要接口

### 认证接口
- **登录** - POST /api/auth/login

### 系统通知接口
- **获取通知列表** - POST /api/sys-notify/sysNotifyTodoRestService/getTodo

### 待办事项接口
- **获取待办列表** - GET /api/todo
- **创建待办事项** - POST /api/todo
- **更新待办事项** - PUT /api/todo/{id}
- **删除待办事项** - DELETE /api/todo/{id}

## 前端页面

- **登录页面** - http://localhost:8080/login.html
- **待办页面** - http://localhost:8080/todo.html

## API 文档

- **Swagger UI** - http://localhost:8080/swagger-ui/index.html
- **API 文档** - http://localhost:8080/v3/api-docs

## 定制化

1. **修改包名** - 更新 `pom.xml` 中的 `groupId` 和包结构
2. **添加依赖** - 在 `pom.xml` 中添加新的依赖
3. **扩展业务逻辑** - 修改相应的服务类添加业务逻辑
4. **添加新类** - 在 `com.example` 包或子包中创建新类

## 许可证
