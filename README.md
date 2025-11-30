# **🌍 AI Traveler Backend (AI 旅游助手后端)**

这是一个基于 Spring Boot 3 和 Spring AI 构建的智能旅游规划助手后端系统。它利用大语言模型（LLM）、向量模型、RAG（检索增强生成）、MCP（模型上下文协议）和地理空间数据，为用户提供从灵感激发到行程落地的全流程智能化服务。

## **📖 项目概述**

**AI Traveler**旨在解决传统旅游规划中信息繁杂、决策困难的痛点。

本项目作为系统的**云端大脑**，不仅提供基础的 CRUD 业务支持，更核心的是集成了先进的 AI 能力。通过整合 **ZhipuAI/OpenAI** 大模型、**高德地图 API、MCP** 和 **实时网络搜索**，实现了基于自然语言的智能推荐、行程自动生成、网页攻略提取以及多轮对话式规划。

## **✨ 核心功能**

### **🧠 AI 核心能力**

* **智能多轮对话**：基于用户输入的需求或问题（如“长沙有什么好玩的？从橘子洲到五一广场怎么走？”），LLM 使用内置工具进行网络搜索、POI搜索、路径规划、天气查询等，为用户进行推荐，回答用户问题。  
* **网页攻略提取**：利用 Jsoup \+ Boilerpipe \+ AI，抓取并清洗旅游攻略网页内容，从中提取结构化数据（美食、活动、特色文化等）保存入库。  
* **智能旅程生成**：AI根据心愿单，结合地理位置、天气信息智能规划整个行程，支持以日程为单位重新规划。  
* **多维路径规划**：根据起点和终点，从步行、骑行、驾车和公共交通多个维度给出路径规划，并提供一键唤起高德地图URI。

### **🗺️ 业务功能模块**

* **POI & 地点管理**：支持语义搜索（PGVector）、数据库精确搜索及全网 API 混合搜索。  
* **非结构化资源 (Non-POI)**：管理“吃烤鸭”、“逛夜市”等无固定坐标的泛活动项目。  
* **心愿单系统**：将感兴趣的地点暂存，作为 AI 规划的输入源。  
* **旅程全生命周期**：创建、编辑、状态流转、拖拽排序日程项。  
* **用户管理**：支持通过Vector搜索匹配兴趣相似的用户。  
* **团队协作**：支持查看公开旅程，申请/邀请好友 加入旅程，设置 OWNER/EDITOR/VIEWER 权限，共同编辑。  
* **资产管理**：  
  * **多媒体日志**：发布图文游记（集成阿里云 OSS）。  
  * **智能记账**：多币种支出记录与分类统计。

## **🏗️ 技术架构**

本项目采用**前后端分离**架构，后端采用**分层架构**设计。

### **技术栈**

| 类别 | 技术组件 | 说明 |
| :---- | :---- | :---- |
| **开发语言** | Java 21 | 利用新特性提升开发效率与性能 |
| **核心框架** | Spring Boot 3.4.7 | 及其生态组件 (Web, AOP, Test) |
| **AI 框架** | **Spring AI 1.1.0** | 核心 AI 编排，集成 ChatClient, Embedding, VectorStore |
| **模型服务** | ZhipuAI / OpenAI | 接入智谱 GLM-4 等大模型 |
| **ORM 框架** | MyBatis Plus 3.5.7 | 简化数据库操作，配合自定义 TypeHandler 处理 JSONB/Enum |
| **数据库** | **PostgreSQL 16** | 关系型数据存储 |
| **向量数据库** | **PGVector** | 存储文本嵌入向量，支持语义检索 |
| **工具协议** | **MCP Client** | 集成高德地图 MCP Server，实现标准化的工具调用 |
| **对象存储** | Aliyun OSS | 图片等非结构化数据存储 |
| **爬虫工具** | Jsoup \+ Boilerpipe | 网页内容抓取与正文降噪提取 |
| **API 文档** | SpringDoc / Knife4j | OpenAPI 3 规范文档自动生成 |

### **核心机制**

1. **向量模型+数据库**：利用 PGVector 存储用户偏好和 POI 描述的高维向量，实现用户兴趣匹配、语义搜索POI。  
2. **Function Calling (工具调用)**：LLM 可自主决定调用高德地图 API 查路线、查天气，或调用网络搜索工具。  
3. **编程式事务**：在多线程异步任务（如 AI 批量解析网页）中使用 TransactionTemplate 保证数据一致性。

## **🚀 快速开始**

### **1\. 环境准备**

* **JDK**: 21+  
* **Maven**: 3.8+  
* **Docker**: 用于启动数据库

### **2\. 启动基础设施**

项目根目录下提供了 docker-compose.yml，用于快速启动带有 pgvector 插件的 PostgreSQL。

```bash
docker-compose up \-d
```

### **3\. 配置文件设置**

项目配置文件位于 src/main/resources/application.yaml。  
注意：本项目使用了环境变量占位符，请在 IDE 的运行配置或系统环境变量中设置以下 Key：

| 环境变量名 | 说明 | 示例 |
| :---- | :---- | :---- |
| postgres-user | 数据库用户名 | postgres |
| postgres-password | 数据库密码 | pg123456 (需与 docker-compose 一致) |
| ZHIPUAI\_API\_KEY | 智谱 AI Key | See your dashboard |
| OPENAI\_API\_KEY | OpenAI Key | sk-... |
| OPENAI\_CHAT\_BASE\_URL | OpenAI 代理地址 | https://api.openai.com |
| GAODE\_API\_KEY | 高德地图 Web 服务 Key | See amap console |
| WEB\_SEARCH\_API\_KEY | 网络搜索 API Key | See bocha.cn |
| ALIYUN\_OSS\_ENDPOINT | 阿里云 OSS 节点 | oss-cn-hangzhou.aliyuncs.com |
| ALIYUN\_OSS\_BUCKET\_NAME | Bucket 名称 | my-travel-bucket |
| ALIYUN\_OSS\_REGION | OSS 区域 | cn-hangzhou |
| ALIYUN\_ACCESS\_KEY\_ID | 阿里云 AK (SDK自动读取， 需配置环境变量) | \- |
| ALIYUN\_ACCESS\_KEY\_SECRET | 阿里云 SK (SDK自动读取， 需配置环境变量) | \- |

### **4\. 数据库初始化**

项目启动后，Spring AI 会自动初始化 spring\_ai\_chat\_memory 表。  
业务表结构请手动执行 SQL 脚本（位于 src/main/resources/sql/）：

1. type-ddl.sql (创建枚举类型)  
2. table-ddl.sql (创建业务表)

### **5\. 编译与运行**

```bash
# 编译项目
mvn clean package -DskipTests

# 运行
java -jar target/AI-Traveler-0.0.1-SNAPSHOT.jar
```

启动成功后，访问 API 文档：

* **Knife4j**: http://localhost:8080/api/doc.html

## **📂 项目结构**

```
com.bread.traveler  
├── annotation      # 自定义注解 (权限校验等)  
├── aspect          # AOP 切面 (处理 @TripAccessValidate 等)  
├── config          # 全局配置 (AI, Web, Knife4j, Mybatis)  
├── controller      # REST 接口层  
├── dto             # 数据传输对象  
├── entity          # 数据库实体  
├── enums           # 枚举定义 (TripStatus, RoleType 等)  
├── exception       # 全局异常处理  
├── mapper          # DAO 层 (MyBatis Plus)  
├── service         # 业务逻辑层 (含 AI 编排逻辑)  
├── tools           # AI Function Tools (WebSearch, POI Search)  
├── typehandler     # MyBatis 类型处理器 (JSONB, Vector, UUID)  
├── utils           # 工具类 (GaoDe, OSS, Crawler)  
└── vo              # 统一响应对象  
```

