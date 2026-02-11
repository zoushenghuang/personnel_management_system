# Docker 容器化部署文档

## 系统要求

- Ubuntu 20.04 LTS 或更高版本
- 至少 4GB RAM
- 至少 20GB 磁盘空间
- root 或 sudo 权限

---

## 第一部分：安装 Docker 和 Docker Compose

### 1. 更新系统

```bash
sudo apt update
sudo apt upgrade -y
```

### 2. 安装 Docker

```bash
# 安装依赖
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

# 添加 Docker 官方 GPG 密钥
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# 添加 Docker 仓库
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# 安装 Docker
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# 启动 Docker
sudo systemctl start docker
sudo systemctl enable docker

# 验证安装
sudo docker --version
```

### 3. 安装 Docker Compose

```bash
# 下载 Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# 添加执行权限
sudo chmod +x /usr/local/bin/docker-compose

# 验证安装
docker-compose --version
```

### 4. 配置 Docker 用户权限（可选）

```bash
# 将当前用户添加到 docker 组
sudo usermod -aG docker $USER

# 重新登录或执行
newgrp docker

# 测试（无需 sudo）
docker ps
```

---

## 第二部分：准备项目文件

### 1. 上传项目到服务器

```bash
# 方式1：使用 git clone
cd /opt
sudo git clone <your-git-repository-url> chartboard
sudo chown -R $USER:$USER /opt/chartboard

# 方式2：使用 scp 上传
# 在本地执行：
# scp -r /path/to/chartboard user@server_ip:/opt/
```

### 2. 进入项目目录

```bash
cd /opt/chartboard
```

### 3. 配置环境变量（可选）

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑环境变量（如需修改密码等）
vim .env
```

---

## 第三部分：构建和启动容器

### 1. 构建镜像

```bash
# 构建所有服务的镜像
docker-compose build

# 或者分别构建
docker-compose build backend
docker-compose build frontend
```

### 2. 启动所有服务

```bash
# 后台启动
docker-compose up -d

# 查看启动日志
docker-compose logs -f
```

### 3. 查看容器状态

```bash
docker-compose ps
```

应该看到3个容器都在运行：
- chartboard-mysql
- chartboard-backend
- chartboard-frontend

---

## 第四部分：验证部署

### 1. 检查容器健康状态

```bash
# 查看所有容器
docker ps

# 查看容器日志
docker-compose logs mysql
docker-compose logs backend
docker-compose logs frontend
```

### 2. 测试后端API

```bash
# 测试健康检查
curl http://localhost:8080/actuator/health

# 测试API文档
curl http://localhost:8080/api-docs
```

### 3. 访问前端

打开浏览器访问：
- `http://your_server_ip`
- 使用账号：`admin` / `admin123` 登录

---

## 第五部分：常用命令

### 容器管理

```bash
# 启动所有服务
docker-compose up -d

# 停止所有服务
docker-compose stop

# 重启所有服务
docker-compose restart

# 停止并删除容器
docker-compose down

# 停止并删除容器、网络、卷
docker-compose down -v
```

### 查看日志

```bash
# 查看所有服务日志
docker-compose logs

# 实时查看日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs backend
docker-compose logs -f frontend

# 查看最近100行日志
docker-compose logs --tail=100 backend
```

### 进入容器

```bash
# 进入后端容器
docker-compose exec backend sh

# 进入MySQL容器
docker-compose exec mysql bash

# 进入前端容器
docker-compose exec frontend sh
```

### 重新构建

```bash
# 重新构建并启动
docker-compose up -d --build

# 强制重新构建（不使用缓存）
docker-compose build --no-cache
docker-compose up -d
```

---

## 第六部分：数据库管理

### 1. 连接数据库

```bash
# 方式1：通过容器
docker-compose exec mysql mysql -u chartboard -pchartboard123 chartboard

# 方式2：从宿主机（如果开放了3306端口）
mysql -h localhost -P 3306 -u chartboard -pchartboard123 chartboard
```

### 2. 备份数据库

```bash
# 创建备份目录
mkdir -p /opt/backups

# 备份数据库
docker-compose exec mysql mysqldump -u chartboard -pchartboard123 chartboard > /opt/backups/chartboard_$(date +%Y%m%d_%H%M%S).sql

# 或使用 docker exec
docker exec chartboard-mysql mysqldump -u chartboard -pchartboard123 chartboard > /opt/backups/chartboard_$(date +%Y%m%d_%H%M%S).sql
```

### 3. 恢复数据库

```bash
# 从备份恢复
docker-compose exec -T mysql mysql -u chartboard -pchartboard123 chartboard < /opt/backups/chartboard_backup.sql
```

### 4. 查看数据库日志

```bash
docker-compose logs mysql
```

---

## 第七部分：更新应用

### 1. 更新代码

```bash
cd /opt/chartboard
git pull
```

### 2. 重新构建和部署

```bash
# 停止服务
docker-compose down

# 重新构建
docker-compose build

# 启动服务
docker-compose up -d

# 查看日志确认启动成功
docker-compose logs -f
```

### 3. 快速更新脚本

创建 `update.sh`：

```bash
#!/bin/bash

echo "开始更新 ChartBoard..."

# 进入项目目录
cd /opt/chartboard

# 拉取最新代码
git pull

# 停止服务
docker-compose down

# 重新构建
docker-compose build --no-cache

# 启动服务
docker-compose up -d

# 等待服务启动
sleep 10

# 查看状态
docker-compose ps

echo "更新完成！"
```

使用：

```bash
chmod +x update.sh
./update.sh
```

---

## 第八部分：监控和维护

### 1. 查看资源使用

```bash
# 查看容器资源使用
docker stats

# 查看特定容器
docker stats chartboard-backend
```

### 2. 清理Docker资源

```bash
# 清理未使用的镜像
docker image prune -a

# 清理未使用的容器
docker container prune

# 清理未使用的卷
docker volume prune

# 清理所有未使用的资源
docker system prune -a
```

### 3. 查看磁盘使用

```bash
docker system df
```

---

## 第九部分：配置反向代理（可选）

如果需要配置域名和SSL，可以在宿主机安装Nginx作为反向代理。

### 1. 安装Nginx

```bash
sudo apt install -y nginx
```

### 2. 配置Nginx

```bash
sudo vim /etc/nginx/sites-available/chartboard
```

写入：

```nginx
server {
    listen 80;
    server_name your_domain.com;

    location / {
        proxy_pass http://localhost:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 3. 启用配置

```bash
sudo ln -s /etc/nginx/sites-available/chartboard /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 4. 配置SSL（使用Certbot）

```bash
sudo apt install -y certbot python3-certbot-nginx
sudo certbot --nginx -d your_domain.com
```

---

## 第十部分：故障排查

### 容器无法启动

```bash
# 查看详细日志
docker-compose logs backend

# 查看容器状态
docker-compose ps

# 检查端口占用
sudo netstat -tlnp | grep 8080
```

### 数据库连接失败

```bash
# 检查MySQL容器状态
docker-compose ps mysql

# 查看MySQL日志
docker-compose logs mysql

# 测试数据库连接
docker-compose exec mysql mysql -u chartboard -pchartboard123 -e "SELECT 1"
```

### 前端无法访问

```bash
# 检查前端容器
docker-compose logs frontend

# 检查Nginx配置
docker-compose exec frontend cat /etc/nginx/conf.d/default.conf

# 测试前端
curl http://localhost/
```

### 网络问题

```bash
# 查看Docker网络
docker network ls

# 检查容器网络
docker network inspect chartboard_chartboard-network

# 测试容器间连接
docker-compose exec backend ping mysql
```

---

## 第十一部分：生产环境优化

### 1. 修改docker-compose.yml

```yaml
# 添加资源限制
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

### 2. 配置日志轮转

```yaml
services:
  backend:
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

### 3. 使用Docker Secrets（敏感信息）

```bash
# 创建密码文件
echo "chartboard123" | docker secret create mysql_password -

# 在docker-compose.yml中使用
secrets:
  mysql_password:
    external: true
```

---

## 第十二部分：自动化部署

### 使用Docker Compose自动重启

```yaml
services:
  backend:
    restart: always  # 或 unless-stopped
```

### 设置开机自启

```bash
# Docker服务已设置开机自启
sudo systemctl enable docker

# 容器会随Docker自动启动（如果设置了restart: always）
```

---

## 快速参考

### 一键启动

```bash
cd /opt/chartboard && docker-compose up -d
```

### 一键停止

```bash
cd /opt/chartboard && docker-compose down
```

### 查看状态

```bash
cd /opt/chartboard && docker-compose ps
```

### 查看日志

```bash
cd /opt/chartboard && docker-compose logs -f
```

---

## 性能建议

1. **使用SSD存储**：提升数据库性能
2. **配置资源限制**：防止容器占用过多资源
3. **启用日志轮转**：防止日志文件过大
4. **定期清理**：清理未使用的镜像和容器
5. **监控资源**：使用 `docker stats` 监控

---

## 安全建议

1. **修改默认密码**：修改 `.env` 中的数据库密码
2. **限制端口暴露**：生产环境不要暴露MySQL端口
3. **使用HTTPS**：配置SSL证书
4. **定期更新**：更新Docker和镜像
5. **备份数据**：定期备份数据库

---

## 项目结构

```
chartboard/
├── docker-compose.yml          # Docker Compose配置
├── .env.example                # 环境变量模板
├── Dockerfile                  # 前端Dockerfile
├── nginx.conf                  # Nginx配置
├── .dockerignore              # Docker忽略文件
├── backend/
│   ├── Dockerfile             # 后端Dockerfile
│   ├── .dockerignore          # 后端忽略文件
│   └── ...
└── ...
```

---

## 常见问题

**Q: 容器启动后立即退出？**
A: 查看日志 `docker-compose logs <service_name>` 找出原因

**Q: 数据库数据丢失？**
A: 检查是否使用了 `docker-compose down -v`，这会删除卷

**Q: 端口被占用？**
A: 修改 `docker-compose.yml` 中的端口映射

**Q: 构建很慢？**
A: 使用国内镜像源，配置Docker镜像加速

---

## 联系支持

如遇到问题，请：
1. 查看容器日志
2. 检查网络连接
3. 验证配置文件
4. 查看Docker状态

祝部署顺利！🐳
