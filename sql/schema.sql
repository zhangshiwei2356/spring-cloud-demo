CREATE DATABASE IF NOT EXISTS cloud_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cloud_demo;

DROP TABLE IF EXISTS biz_order;

CREATE TABLE biz_order (
    order_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    order_no VARCHAR(64) NOT NULL COMMENT '业务单号',
    buyer_user_id BIGINT NOT NULL COMMENT '购买人',
    product_code VARCHAR(64) NOT NULL COMMENT '商品编码',
    quantity INT NOT NULL COMMENT '数量',
    total_amount DECIMAL(18,2) NOT NULL COMMENT '总金额',
    status INT NOT NULL DEFAULT 10 COMMENT '状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_buyer (buyer_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
