CREATE TABLE announcement (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                              title VARCHAR(200) NOT NULL COMMENT '公告标题',
                              content TEXT COMMENT '公告内容',
                              status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-发布',
                              create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) COMMENT '系统公告表';