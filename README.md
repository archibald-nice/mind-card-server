# 思维卡片管理系统 (Mind Card Server)

一个基于Spring Boot 3的思维卡片管理系统，提供完整的CRUD功能和RESTful API接口。支持卡片的创建、编辑、分类、标签管理、状态控制等功能。

## 🚀 技术栈

- **框架**: Spring Boot 3.4.7
- **数据库**: PostgreSQL (生产环境) / H2 (测试环境)
- **ORM**: Spring Data JPA
- **安全**: Spring Security
- **构建工具**: Maven
- **Java版本**: 17

## 📁 项目结构

```
src/main/java/com/archie/mind_card_server/
├── config/                 # 配置类
│   ├── DatabaseConfig.java    # 数据库配置
│   ├── SecurityConfig.java    # 安全配置
│   ├── WebConfig.java         # Web配置
│   └── DataInitializer.java   # 数据初始化
├── controller/             # 控制器层
│   ├── MindCardController.java # 思维卡片控制器
│   └── HealthController.java   # 健康检查控制器
├── dto/                    # 数据传输对象
│   ├── MindCardDTO.java       # 思维卡片DTO
│   └── ApiResponse.java       # 统一响应格式
├── entity/                 # 实体类
│   └── MindCard.java          # 思维卡片实体
├── exception/              # 异常处理
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
├── repository/             # 数据访问层
│   └── MindCardRepository.java
├── service/                # 服务层
│   ├── MindCardService.java
│   └── impl/
│       └── MindCardServiceImpl.java
└── MindCardServerApplication.java # 主启动类
```

## 🛠️ 快速开始

### 1. 环境准备

- JDK 17+
- Maven 3.6+
- PostgreSQL 12+ (可选，默认配置)

### 2. 数据库配置

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database
    username: your_username
    password: your_password
```

### 3. 运行应用

```bash
# 克隆项目
git clone <repository-url>
cd mind-card-server

# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run
```

应用将在 `http://localhost:8080/api` 启动

### 4. 健康检查

访问 `http://localhost:8080/api/health` 检查应用状态

## 📚 API 文档

### 基础路径
所有API的基础路径为：`/api/mind-cards`

### 主要接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/mind-cards` | 创建思维卡片 |
| GET | `/mind-cards/{id}` | 根据ID获取卡片 |
| PUT | `/mind-cards/{id}` | 更新思维卡片 |
| DELETE | `/mind-cards/{id}` | 删除思维卡片(软删除) |
| GET | `/mind-cards` | 获取所有激活的卡片 |
| GET | `/mind-cards/page` | 分页获取卡片 |
| GET | `/mind-cards/search` | 关键词搜索 |
| GET | `/mind-cards/category/{category}` | 按分类获取 |
| GET | `/mind-cards/statistics` | 获取统计信息 |

### 请求示例

#### 创建思维卡片
```bash
curl -X POST http://localhost:8080/api/mind-cards \
  -H "Content-Type: application/json" \
  -d '{
    "title": "学习笔记",
    "content": "这是一个学习笔记的内容",
    "category": "学习",
    "tags": "笔记,学习",
    "priorityLevel": 1,
    "createdBy": "user1"
  }'
```

#### 搜索思维卡片
```bash
curl "http://localhost:8080/api/mind-cards/search?keyword=学习"
```

#### 分页获取
```bash
curl "http://localhost:8080/api/mind-cards/page?page=0&size=10&sortBy=createdAt&sortDir=desc"
```

## 🗄️ 数据模型

### MindCard 实体

| 字段 | 类型 | 描述 |
|------|------|------|
| id | Long | 主键ID |
| title | String | 标题 (必填，最大200字符) |
| content | String | 内容 (最大2000字符) |
| category | String | 分类 (最大100字符) |
| tags | String | 标签 (最大500字符) |
| isActive | Boolean | 是否激活 (默认true) |
| priorityLevel | Integer | 优先级 (默认0) |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |
| createdBy | String | 创建者 |
| updatedBy | String | 更新者 |

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=MindCardServerApplicationTests
```

## 📊 监控和管理

应用集成了Spring Boot Actuator，提供以下监控端点：

- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 应用指标

## 🔧 配置说明

### 主要配置项

- **服务器端口**: `server.port=8080`
- **上下文路径**: `server.servlet.context-path=/api`
- **数据库连接池**: 使用HikariCP，最大连接数20
- **JPA配置**: 自动更新表结构，显示SQL语句
- **日志配置**: DEBUG级别，输出到控制台和文件

### 安全配置

- 默认禁用CSRF保护
- 允许跨域访问
- 基础HTTP认证 (用户名: admin, 密码: admin123)

## 🚀 部署

### Docker部署 (可选)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/mind-card-server-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 生产环境配置

1. 修改数据库连接信息
2. 配置生产环境的日志级别
3. 设置适当的安全配置
4. 配置外部配置文件

## 🤝 贡献

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 邮箱: your-email@example.com
- 项目地址: https://github.com/your-username/mind-card-server

---

**注意**: 这是一个演示项目，生产环境使用前请根据实际需求调整配置和安全设置。
