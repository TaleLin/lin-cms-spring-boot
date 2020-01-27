# lin-cms-java

**打包**

```bash
mvn clean package  -Dmaven.test.skip=true
```

**启动**

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8888 --spring.profiles.active=prod
```

## 设计哲学

**简单，易用**

## 特性

- [x] 优雅的权限系统
- [x] 双令牌
- [x] 骚气的banner
- [x] 国际化
- [x] 全局异常处理
- [x] 优雅的分层结构
- [x] 行为日志
- [x] 请求日志
- [x] 校验类库
- [x] 文件上传


```
DROP TABLE IF EXISTS book;
CREATE TABLE book
(
    id          int(11)     NOT NULL AUTO_INCREMENT,
    title       varchar(50) NOT NULL,
    author      varchar(30)          DEFAULT NULL,
    summary     varchar(1000)        DEFAULT NULL,
    image       varchar(100)         DEFAULT NULL,
    create_time datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    update_time datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    delete_time datetime(3)          DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
```

