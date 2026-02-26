# RedPacket - Minecraft 红包插件（二次开发版）

[English](#english-version) | 中文

一个功能丰富的 Minecraft 红包插件，支持多种红包类型和经济系统集成。本项目基于原版进行二次开发和优化。

## 📋 项目简介

RedPacket 是一个为 Minecraft 服务器设计的红包系统插件，玩家可以发送和领取虚拟红包，增加服务器的互动性和趣味性。

### 主要特性

- 🧧 **多种红包类型**
  - 普通红包：随机金额分配
  - 接龙红包：成语接龙玩法
  - 口令红包：需要密码才能领取

- 💰 **经济系统集成**
  - 支持 Vault 经济系统
  - 支持 PlayerPoints 点券系统
  - 灵活的货币类型选择

- 🗄️ **数据库支持**
  - SQLite（本地数据库）
  - MySQL（远程数据库）
  - 自动数据持久化

- 🎮 **用户友好**
  - GUI 菜单界面
  - 聊天交互式创建
  - 会话管理系统
  - 颜色代码支持

- ⚙️ **高度可配置**
  - 红包金额限制
  - 数量限制
  - 过期时间设置
  - 数据库配置

## 🎯 红包类型说明

### 1. 普通红包 (Common RedPacket)
- 发送者设置总金额和红包数量
- 系统随机分配每个红包的金额
- 先到先得，抢完为止

### 2. 接龙红包 (JieLong RedPacket)
- 基于成语接龙的互动红包
- 玩家需要正确接龙才能领取
- 增加游戏趣味性和挑战性

### 3. 口令红包 (Password RedPacket)
- 需要输入正确口令才能领取
- 适合特定群体或活动使用
- 增加红包的专属性

## 📦 安装部署

### 前置要求

- **服务器**: Spigot/Paper 1.12.2 - 1.20+
- **Java**: Java 8 或更高版本
- **依赖插件**（可选）:
  - Vault（经济系统支持）
  - PlayerPoints（点券系统支持）

### 安装步骤

1. **下载插件**
   ```bash
   # 从 Releases 下载最新版本的 JAR 文件
   # 或使用 Maven 编译
   mvn clean package
   ```

2. **安装插件**
   ```bash
   # 将 JAR 文件复制到服务器 plugins 目录
   cp target/RedPacket-3.5.0.jar /path/to/server/plugins/
   ```

3. **安装依赖**（可选）
   - 下载并安装 Vault 插件
   - 下载并安装 PlayerPoints 插件
   - 安装任意经济插件（如 EssentialsX）

4. **启动服务器**
   - 首次启动会自动生成配置文件
   - 配置文件位于 `plugins/RedPacket/config.yml`

5. **配置插件**
   - 编辑 `config.yml` 配置数据库和红包设置
   - 重载插件：`/rp reload`

## ⚙️ 配置说明

### 数据库配置

```yaml
Database:
  # 数据库类型：sqlite 或 mysql
  Type: sqlite

  # SQLite 配置
  FileName: database.db

  # MySQL 配置（使用 MySQL 时需要配置）
  IP: localhost
  Port: 3306
  UserName: root
  Password: password
  DatabaseName: minecraft
  MySQLArgument: "?useUnicode=true&characterEncoding=utf8"

  # 数据表名称
  TableName: redpacket
```

### 红包设置

```yaml
RedPacket:
  # 单个红包最大数量
  MaxAmount: 10000

  # 红包最大总额
  MaxMoney: 10000

  # 红包最小总额
  MinMoney: 1

  # 是否自动过期
  Expired: false

  # 过期时间（毫秒）
  ExpiredTime: 4800000  # 80分钟
```

## 🎮 使用指南

### 基本命令

```bash
# 主命令（别名：/fhb, /rp）
/redpacket [new|get|help]

# 创建红包
/rp new

# 领取红包
/rp get <红包ID>

# 查看帮助
/rp help

# 重载配置（管理员）
/rp reload
```

### 创建红包流程

1. **输入命令**
   ```bash
   /rp new
   ```

2. **选择红包类型**
   - 在聊天中输入数字选择类型
   - 1: 普通红包
   - 2: 接龙红包
   - 3: 口令红包

3. **设置参数**
   - 输入总金额
   - 输入红包数量
   - 输入祝福语（可选）
   - 口令红包需要设置口令

4. **确认发送**
   - 系统扣除相应金额
   - 生成红包ID
   - 在聊天中显示红包信息

### 领取红包

```bash
# 点击聊天中的红包链接
# 或使用命令
/rp get <红包ID>

# 口令红包需要输入口令
/rp get <红包ID> <口令>
```

## 🔐 权限节点

### 基础权限

```yaml
# 用户权限（默认所有玩家）
redpacket.user: true

# 管理员权限（默认OP）
redpacket.admin: true
```

### 详细权限

```yaml
# 命令权限
redpacket.command.new: true        # 创建红包
redpacket.command.get: true        # 领取红包
redpacket.command.session: true    # 会话管理
redpacket.command.reload: op       # 重载配置

# 红包类型权限
redpacket.set.commonredpacket: true      # 发送普通红包
redpacket.set.jielongredpacket: true     # 发送接龙红包
redpacket.set.passwordredpacket: true    # 发送口令红包

redpacket.get.commonredpacket: true      # 领取普通红包
redpacket.get.jielongredpacket: true     # 领取接龙红包
redpacket.get.passwordredpacket: true    # 领取口令红包

# 特殊权限
redpacket.chat.color: op           # 聊天颜色代码
```

## 📁 项目结构

```
RedPacket-master/
├── src/main/java/sandtechnology/redpacket/
│   ├── RedPacketPlugin.java           # 主插件类
│   ├── command/
│   │   └── CommandHandler.java        # 命令处理器
│   ├── database/
│   │   ├── AbstractDatabaseManager.java  # 数据库抽象类
│   │   ├── MysqlManager.java          # MySQL 管理器
│   │   └── SqliteManager.java         # SQLite 管理器
│   ├── listener/
│   │   ├── ChatListener.java          # 聊天监听器
│   │   └── MessageSender.java         # 消息发送器
│   ├── redpacket/
│   │   └── RedPacket.java             # 红包核心类
│   ├── session/
│   │   ├── CreateSession.java         # 创建会话
│   │   ├── QuerySession.java          # 查询会话
│   │   └── SessionManager.java        # 会话管理器
│   ├── ui/
│   │   ├── GuiMenu.java               # GUI 菜单
│   │   └── MenuListener.java          # 菜单监听器
│   └── util/
│       ├── ColorHelper.java           # 颜色工具
│       ├── CommonHelper.java          # 通用工具
│       ├── CompatibilityHelper.java   # 兼容性工具
│       ├── EcoAndPermissionHelper.java # 经济权限工具
│       ├── IdiomManager.java          # 成语管理器
│       ├── JsonHelper.java            # JSON 工具
│       ├── MessageHelper.java         # 消息工具
│       ├── OperatorHelper.java        # 操作工具
│       └── RedPacketManager.java      # 红包管理器
│
├── src/main/resources/
│   ├── config.yml                     # 配置文件
│   ├── plugin.yml                     # 插件元数据
│   ├── idiom.json                     # 成语数据
│   └── LICENSE-*                      # 许可证文件
│
├── pom.xml                            # Maven 配置
├── LICENSE                            # 项目许可证
└── README.md                          # 项目说明
```

## 🔧 开发说明

### 编译项目

```bash
# 克隆项目
git clone https://github.com/xiaoxin-zk/RedPacket.git
cd RedPacket

# 使用 Maven 编译
mvn clean package

# 编译后的文件位于
# target/RedPacket-3.5.0.jar
```

### 依赖库

- **Spigot API 1.19.3** - Minecraft 服务器 API
- **Vault API 1.7** - 经济系统接口
- **PlayerPoints 3.2.5** - 点券系统
- **MySQL Connector 8.0.30** - MySQL 数据库驱动
- **SQLite JDBC 3.40.0.0** - SQLite 数据库驱动

### 技术栈

- Java 8
- Maven 构建工具
- Spigot/Bukkit API
- JDBC 数据库连接
- JSON 数据处理

## 🎨 特色功能

### 成语接龙系统

- 内置成语数据库（idiom.json）
- 智能匹配算法
- 支持多音字处理
- 防作弊机制

### 会话管理

- 交互式创建流程
- 状态机管理
- 超时自动取消
- 错误处理和提示

### GUI 菜单

- 可视化操作界面
- 物品图标展示
- 点击交互
- 动态更新

## 📊 数据库结构

### 红包表 (redpacket)

```sql
CREATE TABLE redpacket (
    id VARCHAR(36) PRIMARY KEY,      -- 红包ID
    type VARCHAR(20),                 -- 红包类型
    creator VARCHAR(36),              -- 创建者UUID
    total_amount DECIMAL(10,2),       -- 总金额
    count INT,                        -- 红包数量
    remaining INT,                    -- 剩余数量
    message TEXT,                     -- 祝福语
    password VARCHAR(50),             -- 口令（口令红包）
    create_time BIGINT,               -- 创建时间
    expire_time BIGINT,               -- 过期时间
    currency_type VARCHAR(20),        -- 货币类型
    data TEXT                         -- 额外数据（JSON）
);
```

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

### 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目基于原项目进行二次开发。

部分代码来自：https://github.com/SeraphJACK/JieLong

## 🙏 致谢

- 原作者：sandtechnology
- JieLong 项目：SeraphJACK
- Spigot 社区
- 所有贡献者

## 📮 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 GitHub Issue
- 在服务器论坛发帖

## ⚠️ 免责声明

本插件仅供学习和娱乐使用，请勿用于商业用途。使用本插件造成的任何问题，开发者不承担责任。

---

**版本**: 3.5.0
**API 版本**: 1.20
**最后更新**: 2024

**注意**: 这是一个二次开发版本，在原版基础上进行了优化和改进。

---

<a name="english-version"></a>

# RedPacket - Minecraft Red Packet Plugin (Modified Version)

[中文](#redpacket---minecraft-红包插件二次开发版) | English

A feature-rich Minecraft red packet plugin supporting multiple packet types and economy system integration. This project is a modified version based on the original.

## 📋 Overview

RedPacket is a red packet system plugin designed for Minecraft servers, allowing players to send and receive virtual red packets, enhancing server interactivity and fun.

### Key Features

- 🧧 **Multiple Packet Types**
  - Common Red Packet: Random amount distribution
  - JieLong Red Packet: Chinese idiom chain game
  - Password Red Packet: Requires password to claim

- 💰 **Economy System Integration**
  - Vault economy system support
  - PlayerPoints points system support
  - Flexible currency type selection

- 🗄️ **Database Support**
  - SQLite (local database)
  - MySQL (remote database)
  - Automatic data persistence

- 🎮 **User Friendly**
  - GUI menu interface
  - Chat interactive creation
  - Session management system
  - Color code support

- ⚙️ **Highly Configurable**
  - Red packet amount limits
  - Quantity limits
  - Expiration time settings
  - Database configuration

## 🎯 Packet Types

### 1. Common Red Packet
- Sender sets total amount and packet count
- System randomly distributes amount for each packet
- First come first served

### 2. JieLong Red Packet
- Interactive red packet based on Chinese idiom chain
- Players need to correctly continue the idiom chain to claim
- Increases game fun and challenge

### 3. Password Red Packet
- Requires correct password to claim
- Suitable for specific groups or events
- Adds exclusivity to red packets

## 📦 Installation

### Requirements

- **Server**: Spigot/Paper 1.12.2 - 1.20+
- **Java**: Java 8 or higher
- **Dependencies** (optional):
  - Vault (economy system support)
  - PlayerPoints (points system support)

### Installation Steps

1. **Download Plugin**
   ```bash
   # Download latest JAR from Releases
   # Or compile with Maven
   mvn clean package
   ```

2. **Install Plugin**
   ```bash
   # Copy JAR file to server plugins directory
   cp target/RedPacket-3.5.0.jar /path/to/server/plugins/
   ```

3. **Install Dependencies** (optional)
   - Download and install Vault plugin
   - Download and install PlayerPoints plugin
   - Install any economy plugin (e.g., EssentialsX)

4. **Start Server**
   - Configuration file will be auto-generated on first start
   - Config file located at `plugins/RedPacket/config.yml`

5. **Configure Plugin**
   - Edit `config.yml` to configure database and red packet settings
   - Reload plugin: `/rp reload`

## ⚙️ Configuration

### Database Configuration

```yaml
Database:
  # Database type: sqlite or mysql
  Type: sqlite

  # SQLite configuration
  FileName: database.db

  # MySQL configuration (required when using MySQL)
  IP: localhost
  Port: 3306
  UserName: root
  Password: password
  DatabaseName: minecraft
  MySQLArgument: "?useUnicode=true&characterEncoding=utf8"

  # Table name
  TableName: redpacket
```

### Red Packet Settings

```yaml
RedPacket:
  # Maximum packet count
  MaxAmount: 10000

  # Maximum total amount
  MaxMoney: 10000

  # Minimum total amount
  MinMoney: 1

  # Auto expiration
  Expired: false

  # Expiration time (milliseconds)
  ExpiredTime: 4800000  # 80 minutes
```

## 🎮 Usage Guide

### Basic Commands

```bash
# Main command (aliases: /fhb, /rp)
/redpacket [new|get|help]

# Create red packet
/rp new

# Claim red packet
/rp get <packet_id>

# View help
/rp help

# Reload config (admin)
/rp reload
```

### Creating Red Packet

1. **Enter Command**
   ```bash
   /rp new
   ```

2. **Select Packet Type**
   - Enter number in chat to select type
   - 1: Common red packet
   - 2: JieLong red packet
   - 3: Password red packet

3. **Set Parameters**
   - Enter total amount
   - Enter packet count
   - Enter blessing message (optional)
   - Password red packet requires password

4. **Confirm Send**
   - System deducts corresponding amount
   - Generates packet ID
   - Displays packet info in chat

### Claiming Red Packet

```bash
# Click red packet link in chat
# Or use command
/rp get <packet_id>

# Password red packet requires password
/rp get <packet_id> <password>
```

## 🔐 Permissions

### Basic Permissions

```yaml
# User permission (default all players)
redpacket.user: true

# Admin permission (default OP)
redpacket.admin: true
```

### Detailed Permissions

```yaml
# Command permissions
redpacket.command.new: true        # Create red packet
redpacket.command.get: true        # Claim red packet
redpacket.command.session: true    # Session management
redpacket.command.reload: op       # Reload config

# Packet type permissions
redpacket.set.commonredpacket: true      # Send common red packet
redpacket.set.jielongredpacket: true     # Send JieLong red packet
redpacket.set.passwordredpacket: true    # Send password red packet

redpacket.get.commonredpacket: true      # Claim common red packet
redpacket.get.jielongredpacket: true     # Claim JieLong red packet
redpacket.get.passwordredpacket: true    # Claim password red packet

# Special permissions
redpacket.chat.color: op           # Chat color codes
```

## 🔧 Development

### Build Project

```bash
# Clone project
git clone https://github.com/xiaoxin-zk/RedPacket.git
cd RedPacket

# Build with Maven
mvn clean package

# Compiled file located at
# target/RedPacket-3.5.0.jar
```

### Dependencies

- **Spigot API 1.19.3** - Minecraft server API
- **Vault API 1.7** - Economy system interface
- **PlayerPoints 3.2.5** - Points system
- **MySQL Connector 8.0.30** - MySQL database driver
- **SQLite JDBC 3.40.0.0** - SQLite database driver

### Tech Stack

- Java 8
- Maven build tool
- Spigot/Bukkit API
- JDBC database connection
- JSON data processing

## 🤝 Contributing

Issues and Pull Requests are welcome!

### Contribution Guidelines

1. Fork this project
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is a modified version based on the original project.

Part of the code is from: https://github.com/SeraphJACK/JieLong

## 🙏 Acknowledgments

- Original author: sandtechnology
- JieLong project: SeraphJACK
- Spigot community
- All contributors

## 📮 Contact

For questions or suggestions, please contact via:

- Submit GitHub Issue
- Post on server forums

## ⚠️ Disclaimer

This plugin is for learning and entertainment purposes only. Do not use for commercial purposes. The developer is not responsible for any issues caused by using this plugin.

---

**Version**: 3.5.0
**API Version**: 1.20
**Last Updated**: 2024

**Note**: This is a modified version with optimizations and improvements based on the original.
