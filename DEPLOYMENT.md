# Ubuntu 服务器部署文档

## 系统要求

- Ubuntu 20.04 LTS 或更高版本
- 至少 2GB RAM
- 至少 20GB 磁盘空间
- root 或 sudo 权限

---

## 第一部分：环境准备

### 1. 更新系统

```bash
sudo apt update
sudo apt upgrade -y
```

### 2. 安装基础工具

```bash
sudo apt install -y curl wget git vim unzip
```

---

## 第二部分：安装 MySQL 数据库

### 1. 安装 MySQL

```bash
sudo apt install -y mysql-server
```

### 2. 启动 MySQL 服务

```bash
sudo systemctl start mysql
sudo systemctl enable mysql
```

### 3. 安全配置 MySQL

```bash
sudo mysql_secure_installation
```

按提示操作：
- 设置 root 密码（建议设置强密码）
- 移除匿名用户：Y
- 禁止 root 远程登录：N（如果需要远程管理选 N）
- 移除测试数据库：Y
- 重新加载权限表：Y

### 4. 创建数据库和用户

```bash
sudo mysql -u root -p
```

在 MySQL 命令行中执行：

```sql
-- 创建数据库
CREATE DATABASE chartboard CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（修改密码为你的密码）
CREATE USER 'chartboard'@'localhost' IDENTIFIED BY 'your_password_here';

-- 授权
GRANT ALL PRIVILEGES ON chartboard.* TO 'chartboard'@'localhost';
FLUSH PRIVILEGES;

-- 退出
EXIT;
```

### 5. 导入数据库结构和数据

```bash
# 假设项目已上传到 /opt/chartboard
cd /opt/chartboard

# 导入初始数据
mysql -u chartboard -p chartboard < backend/init_bigscreen_data.sql
mysql -u chartboard -p chartboard < backend/update_traffic_source.sql
```

---

## 第三部分：安装 Java 环境

### 1. 安装 OpenJDK 17

```bash
sudo apt install -y openjdk-17-jdk
```

### 2. 验证安装

```bash
java -version
```

应该显示类似：`openjdk version "17.0.x"`

### 3. 配置环境变量（可选）

```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

---

## 第四部分：安装 Maven

### 1. 安装 Maven

```bash
sudo apt install -y maven
```

### 2. 验证安装

```bash
mvn -version
```

---

## 第五部分：部署后端

### 1. 上传项目到服务器

```bash
# 方式1：使用 git clone
cd /opt
sudo git clone <your-git-repository-url> chartboard

# 方式2：使用 scp 上传
# 在本地执行：
# scp -r /path/to/chartboard user@server_ip:/opt/

# 设置权限
sudo chown -R $USER:$USER /opt/chartboard
```

### 2. 修改数据库配置

```bash
cd /opt/chartboard
vim backend/src/main/resources/application.properties
```

修改以下配置：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/chartboard?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=chartboard
spring.datasource.password=your_password_here

# 服务器端口（如果需要修改）
server.port=8080
```

### 3. 编译后端项目

```bash
cd /opt/chartboard/backend
mvn clean package -DskipTests
```

编译成功后，会在 `target` 目录生成 `chartboard-backend-0.0.1-SNAPSHOT.jar`

### 4. 创建后端服务（systemd）

```bash
sudo vim /etc/systemd/system/chartboard-backend.service
```

写入以下内容：

```ini
[Unit]
Description=ChartBoard Backend Service
After=mysql.service

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/opt/chartboard/backend
ExecStart=/usr/bin/java -jar /opt/chartboard/backend/target/chartboard-backend-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
```

### 5. 启动后端服务

```bash
# 重新加载 systemd
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start chartboard-backend

# 设置开机自启
sudo systemctl enable chartboard-backend

# 查看服务状态
sudo systemctl status chartboard-backend

# 查看日志
sudo journalctl -u chartboard-backend -f
```

### 6. 验证后端

```bash
curl http://localhost:8080/api-docs
```

如果返回 JSON 数据，说明后端启动成功。

---

## 第六部分：安装 Node.js 和 npm

### 1. 安装 Node.js 20.x

```bash
# 添加 NodeSource 仓库
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -

# 安装 Node.js
sudo apt install -y nodejs

# 验证安装
node -v
npm -v
```

---

## 第七部分：部署前端

### 1. 安装依赖

```bash
cd /opt/chartboard
npm install
```

### 2. 修改前端配置（如果需要）

```bash
vim vite.config.ts
```

确保代理配置正确：

```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
},
```

### 3. 构建前端

```bash
npm run build
```

构建完成后，会在 `dist` 目录生成静态文件。

---

## 第八部分：安装和配置 Nginx

### 1. 安装 Nginx

```bash
sudo apt install -y nginx
```

### 2. 创建 Nginx 配置

```bash
sudo vim /etc/nginx/sites-available/chartboard
```

写入以下内容：

```nginx
server {
    listen 80;
    server_name your_domain.com;  # 修改为你的域名或服务器IP

    # 前端静态文件
    root /opt/chartboard/dist;
    index index.html;

    # 前端路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Swagger文档代理
    location /swagger-ui.html {
        proxy_pass http://localhost:8080;
    }

    location /api-docs {
        proxy_pass http://localhost:8080;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### 3. 启用配置

```bash
# 创建软链接
sudo ln -s /etc/nginx/sites-available/chartboard /etc/nginx/sites-enabled/

# 删除默认配置（可选）
sudo rm /etc/nginx/sites-enabled/default

# 测试配置
sudo nginx -t

# 重启 Nginx
sudo systemctl restart nginx

# 设置开机自启
sudo systemctl enable nginx
```

---

## 第九部分：配置防火墙

### 1. 安装 UFW（如果未安装）

```bash
sudo apt install -y ufw
```

### 2. 配置防火墙规则

```bash
# 允许 SSH
sudo ufw allow 22/tcp

# 允许 HTTP
sudo ufw allow 80/tcp

# 允许 HTTPS（如果配置了SSL）
sudo ufw allow 443/tcp

# 启用防火墙
sudo ufw enable

# 查看状态
sudo ufw status
```

---

## 第十部分：配置 SSL（可选，推荐）

### 1. 安装 Certbot

```bash
sudo apt install -y certbot python3-certbot-nginx
```

### 2. 获取 SSL 证书

```bash
sudo certbot --nginx -d your_domain.com
```

按提示操作，Certbot 会自动配置 Nginx 的 SSL。

### 3. 自动续期

```bash
# 测试自动续期
sudo certbot renew --dry-run

# Certbot 会自动添加 cron 任务
```

---

## 第十一部分：验证部署

### 1. 检查服务状态

```bash
# 检查后端
sudo systemctl status chartboard-backend

# 检查 Nginx
sudo systemctl status nginx

# 检查 MySQL
sudo systemctl status mysql
```

### 2. 访问应用

打开浏览器访问：
- `http://your_server_ip` 或 `http://your_domain.com`
- 使用账号：`admin` / `admin123` 登录

### 3. 测试功能

- 登录功能
- 数据概览页面
- 用户管理
- 大屏展示
- 数据刷新

---

## 第十二部分：日常维护

### 1. 查看日志

```bash
# 后端日志
sudo journalctl -u chartboard-backend -f

# Nginx 访问日志
sudo tail -f /var/log/nginx/access.log

# Nginx 错误日志
sudo tail -f /var/log/nginx/error.log

# MySQL 日志
sudo tail -f /var/log/mysql/error.log
```

### 2. 重启服务

```bash
# 重启后端
sudo systemctl restart chartboard-backend

# 重启 Nginx
sudo systemctl restart nginx

# 重启 MySQL
sudo systemctl restart mysql
```

### 3. 更新应用

```bash
# 更新代码
cd /opt/chartboard
git pull

# 重新构建后端
cd backend
mvn clean package -DskipTests

# 重新构建前端
cd ..
npm install
npm run build

# 重启服务
sudo systemctl restart chartboard-backend
sudo systemctl reload nginx
```

### 4. 数据库备份

```bash
# 创建备份目录
sudo mkdir -p /opt/backups

# 备份数据库
sudo mysqldump -u chartboard -p chartboard > /opt/backups/chartboard_$(date +%Y%m%d_%H%M%S).sql

# 设置定时备份（每天凌晨2点）
sudo crontab -e
# 添加：
# 0 2 * * * mysqldump -u chartboard -p'your_password' chartboard > /opt/backups/chartboard_$(date +\%Y\%m\%d_\%H\%M\%S).sql
```

---

## 故障排查

### 后端无法启动

```bash
# 查看详细日志
sudo journalctl -u chartboard-backend -n 100

# 检查端口占用
sudo netstat -tlnp | grep 8080

# 检查数据库连接
mysql -u chartboard -p -e "SELECT 1"
```

### 前端无法访问

```bash
# 检查 Nginx 配置
sudo nginx -t

# 检查文件权限
ls -la /opt/chartboard/dist

# 查看 Nginx 错误日志
sudo tail -f /var/log/nginx/error.log
```

### 数据库连接失败

```bash
# 检查 MySQL 状态
sudo systemctl status mysql

# 检查用户权限
mysql -u root -p -e "SELECT user, host FROM mysql.user WHERE user='chartboard';"

# 测试连接
mysql -u chartboard -p -h localhost chartboard
```

---

## 性能优化建议

### 1. MySQL 优化

编辑 `/etc/mysql/mysql.conf.d/mysqld.cnf`：

```ini
[mysqld]
max_connections = 200
innodb_buffer_pool_size = 512M
innodb_log_file_size = 128M
query_cache_size = 32M
```

### 2. Nginx 优化

编辑 `/etc/nginx/nginx.conf`：

```nginx
worker_processes auto;
worker_connections 1024;

gzip on;
gzip_vary on;
gzip_min_length 1024;
gzip_types text/plain text/css text/xml text/javascript application/json application/javascript application/xml+rss;
```

### 3. JVM 优化

修改 `/etc/systemd/system/chartboard-backend.service`：

```ini
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /opt/chartboard/backend/target/chartboard-backend-0.0.1-SNAPSHOT.jar
```

---

## 安全建议

1. **修改默认密码**：登录后立即修改 admin 账户密码
2. **定期更新**：定期更新系统和依赖包
3. **配置 SSL**：生产环境必须使用 HTTPS
4. **限制访问**：使用防火墙限制不必要的端口
5. **定期备份**：设置自动备份数据库
6. **监控日志**：定期检查应用和系统日志

---

## 快速部署脚本

创建 `deploy.sh`：

```bash
#!/bin/bash

echo "开始部署 ChartBoard..."

# 更新代码
cd /opt/chartboard
git pull

# 构建后端
cd backend
mvn clean package -DskipTests

# 构建前端
cd ..
npm install
npm run build

# 重启服务
sudo systemctl restart chartboard-backend
sudo systemctl reload nginx

echo "部署完成！"
echo "后端状态："
sudo systemctl status chartboard-backend --no-pager
echo "Nginx状态："
sudo systemctl status nginx --no-pager
```

使用：

```bash
chmod +x deploy.sh
./deploy.sh
```

---

## 联系支持

如遇到问题，请检查：
1. 服务日志
2. 网络连接
3. 防火墙配置
4. 数据库连接

祝部署顺利！🚀
