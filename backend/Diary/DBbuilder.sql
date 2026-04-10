CREATE DATABASE IF NOT EXISTS AIDiarySys
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;


USE AIDiarySys;

CREATE TABLE  IF NOT EXISTS user
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
    username    VARCHAR(50) NOT NULL DEFAULT '未命名用户' COMMENT '用户昵称',
    account     VARCHAR(225) NOT NULL UNIQUE COMMENT '账号',
    password    VARCHAR(225) NOT NULL COMMENT '密码',
    email       VARCHAR(225) NOT NULL UNIQUE COMMENT '邮箱',
    phone       VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    motto       VARCHAR(100) DEFAULT '这个人很懒，没有座右铭' COMMENT '座右铭',
    avatar      VARCHAR(500) DEFAULT '/uploads/avatars/280.jpg',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_account(account)
)COMMENT '用户表';

-- 笔记表，有文本，标签，所属用户，长文本，可见性，逻辑删除
CREATE TABLE IF NOT EXISTS note
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '笔记id',
    user_id     BIGINT NOT NULL COMMENT '日记所有者',
    content     LONGTEXT COMMENT '文本', -- longtext最大存储量可大4gb！！
    tags        VARCHAR(500) COMMENT '标签', -- 用逗号分隔
    visibility  TINYINT DEFAULT 0, -- 0私有 1好友可见 2全部可见
    is_delete   TINYINT DEFAULT 0, -- 1删除 0未删除
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id(user_id) -- 加快基于用户id的笔记表查询
)COMMENT '笔记表';
-- 关系表，谁和谁是好友（from，to）
CREATE TABLE IF NOT EXISTS friendship
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT COMMENT '用户',
    friend_id   BIGINT COMMENT '好友',
    state       TINYINT DEFAULT 0, -- 0待同意 1已同意 2已拒绝 3已删除
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '结交时间',
    UNIQUE KEY (user_id,friend_id) --  同一对朋友关系只能出现一次
)COMMENT '关系表';

-- AI分析存储表

CREATE TABLE IF NOT EXISTS ai_anylize
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    note_id     BIGINT NOT NULL COMMENT '所在id',
    summary     MEDIUMTEXT COMMENT 'ai总结', -- 用medium节省性能开销
    tags_sgt    VARCHAR(500) COMMENT 'ai标签建议',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY (note_id)
)COMMENT 'ai分析结果';