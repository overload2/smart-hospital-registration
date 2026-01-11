**智能医院挂号系统（Smart Hospital Registration）**



**项目简介**



这是一个基于 Java + Spring Boot + Maven 开发的 **智能医院挂号系统**，旨在为医院提供患者挂号、医生管理、科室管理、排班管理、病历管理等核心功能的数字化解决方案。



系统采用前后端分离架构设计（当前仓库为主要后端服务，如包含前端会在后续补充），支持患者在线挂号、医生信息查询、科室导航、挂号记录管理等功能，为医院信息化建设提供基础支撑。



------



**📁 项目结构**



```
smart-hospital-registration/
├── src/
│   ├── main/
│   │   ├── java/com/hospital/registration/
│   │   │   ├── config/                # 配置类
│   │   │   ├── controller/            # 控制层（暂未展示，可根据需求添加）
│   │   │   ├── entity/                # 数据库实体类（如 User, Doctor, Department 等）
│   │   │   ├── mapper/                # 数据访问层（如 Repository 接口）
│   │   │   ├── common/                # 通用工具类、枚举、异常、统一返回等
│   │   │   ├── service/               # 业务逻辑层（可后续扩展）
│   │   │   └── SmartHospitalRegistrationApplication.java  # 启动类
│   │   ├── resources/
│   │   │   ├── application.yml        # 主配置文件
│   │   │   ├── application-dev.yml    # 开发环境配置
│   │   │   ├── application-test.yml   # 测试环境配置
│   │   │   └── sql/                   # SQL脚本（如建库脚本）
│   └── test/                          # 测试代码
├── pom.xml                            # Maven 项目配置
├── .gitignore                         # Git 忽略规则
├── .gitattributes                     # Git 属性配置
├── .mvn/wrapper/                      # Maven Wrapper 相关文件
├── mvnw, mvnw.cmd                     # Maven Wrapper 脚本
└── logs/                              # 日志文件目录（如 hospital-registration.log）
```



------



**🔧 技术栈**



**后端框架**: Spring Boot

**构建工具**: Maven

**语言**: Java

**数据库**（待集成）: MySQL / PostgreSQL（可扩展）

**配置管理**: application.yml + 多环境配置（dev/test）

**日志**: 默认日志框架（可扩展为 Logback / SLF4J）

**其他**:

统一异常处理（GlobalExceptionHandler）

通用返回封装（Result）

自定义业务异常（BusinessException）

枚举管理（如 Gender, PaymentStatus, RegistrationStatus 等）

数据初始化组件（DataInitializer）



------



**📦 功能模块（规划/已实现部分）**



\> 当前项目处于初始化阶段，已包含基础框架与核心实体，后续功能模块将持续开发，包括：



**1. 用户管理**

患者用户注册 / 登录（待开发）

用户角色管理（如：患者、医生、管理员）

用户信息维护



**2. 科室管理**

科室信息增删改查

科室列表展示



**3. 医生管理**

医生信息管理

医生所属科室、职称、擅长领域等



**4. 挂号管理**

患者挂号（选择科室/医生/时间）

挂号记录查询

挂号状态管理（已挂号、已就诊、已取消等）



**5. 排班管理**

医生排班计划

时间段管理（TimeSlot）

可预约时段展示



**6. 病历管理**

就诊记录

病历信息存储



**7. 支付管理（预留）**

挂号费支付状态

支付流水记录



------



**🚀 快速开始（开发环境）**



**1. 克隆项目**



```
git clone https://github.com/overload2/smart-hospital-registration.git
cd smart-hospital-registration
```



**2. 导入 IDE**

使用 IntelliJ IDEA 或 Eclipse 导入为 Maven 项目，等待依赖下载完成。



**3. 配置文件（可选）**

根据需要修改 `src/main/resources/application.yml` 或 `application-dev.yml` 中的配置，如：



服务器端口

数据库连接（待接入）

日志级别等



**4. 编译 & 启动**



使用以下命令启动 Spring Boot 应用：



```
mvn spring-boot:run
```



或者直接运行主类：



```
com.hospital.registration.SmartHospitalRegistrationApplication
```



默认情况下，项目启动后可在 `http://localhost:8080` 访问（如果后续添加了 Web 层 / Controller）。



------



**🗃️ 数据库相关**



项目目前包含 SQL 建库脚本：`src/main/resources/sql/create-database.sql`（如有）

实体类已定义（如 User, Doctor, Department, Registration 等），可配合 JPA / MyBatis 使用

数据访问层使用 `Repository` 接口（如 `DepartmentRepository.java`）



\> ⚠️ 数据库连接配置尚未完全启用，需在 `application.yml` 中补充数据源配置后，才可连接真实数据库。



------



**🧪 测试**



项目包含基础的测试类：



`SmartHospitalRegistrationApplicationTests.java`（Spring Boot 默认测试类，可扩展）



你可在此基础上添加单元测试、接口测试等。



------



**📄 项目配置文件**



`application.yml`：主配置文件

`application-dev.yml`：开发环境专用配置

`application-test.yml`：测试环境配置



支持多环境配置切换，可通过 `spring.profiles.active` 指定。



------



**🤝 后续开发计划**



[ ] 集成 MySQL / PostgreSQL 数据库

[ ] 实现用户登录 / 注册 / 权限控制（JWT / Spring Security）

[ ] 开发 RESTful API 接口层

[ ] 前端 Vue / React 页面（可单独仓库或集成）

[ ] 挂号流程完整实现

[ ] 医生排班与可预约时段管理

[ ] 病历与就诊记录管理

[ ] 支持支付接口对接

[ ] 系统日志与操作审计

[ ] Docker 容器化部署

[ ] CI/CD 自动化部署流程



------



**📞 联系方式 / 开发者**



**项目作者**: overload2（GitHub）

**技术交流 / 协作欢迎提交 Issue 或 Pull Request**

如你参与开发或使用本系统，欢迎贡献代码或提出建议！



------



**📄 License**



本项目目前为 **内部开发阶段**，暂未指定明确开源协议，后续如开源将补充 LICENSE 文件。



------



**📌 备注**



项目当前处于 **初始化阶段**，已搭建基础框架、核心包结构、通用组件、实体类等

后续将逐步完善各个功能模块，欢迎持续关注本仓库的更新！