-- =====================================================
-- 租房小程序数据库表结构
-- 数据库：MySQL 8.0
-- 创建时间：2026-04-01
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS rental_db 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE rental_db;

-- =====================================================
-- 1. 用户表
-- =====================================================
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` BIGINT NOT NULL COMMENT '用户 ID',
  `openid` VARCHAR(64) DEFAULT NULL COMMENT '微信 openid',
  `unionid` VARCHAR(64) DEFAULT NULL COMMENT '微信 unionid',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像 URL',
  `role` VARCHAR(20) DEFAULT NULL COMMENT '角色：landlord-房东，tenant-住户',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  UNIQUE KEY `uk_unionid` (`unionid`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =====================================================
-- 2. 楼栋表
-- =====================================================
DROP TABLE IF EXISTS `t_building`;
CREATE TABLE `t_building` (
  `id` BIGINT NOT NULL COMMENT '楼栋 ID',
  `name` VARCHAR(100) NOT NULL COMMENT '楼栋名称',
  `address` VARCHAR(255) NOT NULL COMMENT '地址',
  `total_floors` INT DEFAULT NULL COMMENT '总层数',
  `total_rooms` INT DEFAULT NULL COMMENT '总房间数',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建人 ID（房东 ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_by` (`create_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼栋表';

-- =====================================================
-- 3. 房源表
-- =====================================================
DROP TABLE IF EXISTS `t_property`;
CREATE TABLE `t_property` (
  `id` BIGINT NOT NULL COMMENT '房源 ID',
  `name` VARCHAR(100) NOT NULL COMMENT '房源名称',
  `address` VARCHAR(255) NOT NULL COMMENT '地址',
  `building_id` BIGINT DEFAULT NULL COMMENT '楼栋 ID',
  `layout` VARCHAR(50) DEFAULT NULL COMMENT '户型（如：3 室 2 厅 1 卫）',
  `area` DECIMAL(10,2) DEFAULT NULL COMMENT '面积（平方米）',
  `rent_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '租金（元/月）',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-未出租，1-已出租',
  `deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建人 ID（房东 ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_create_by` (`create_by`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源表';

-- =====================================================
-- 4. 租客表
-- =====================================================
DROP TABLE IF EXISTS `t_tenant`;
CREATE TABLE `t_tenant` (
  `id` BIGINT NOT NULL COMMENT '租客 ID',
  `user_id` BIGINT NOT NULL COMMENT '用户 ID',
  `property_id` BIGINT NOT NULL COMMENT '房源 ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `id_card` VARCHAR(20) NOT NULL COMMENT '身份证号',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `check_in_date` DATE NOT NULL COMMENT '入住日期',
  `check_out_date` DATE DEFAULT NULL COMMENT '退租日期',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-在住，1-已退租',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_property_id` (`property_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租客表';

-- =====================================================
-- 5. 租金记录表
-- =====================================================
DROP TABLE IF EXISTS `t_rent_record`;
CREATE TABLE `t_rent_record` (
  `id` BIGINT NOT NULL COMMENT '记录 ID',
  `tenant_id` BIGINT NOT NULL COMMENT '租客 ID',
  `property_id` BIGINT NOT NULL COMMENT '房源 ID',
  `landlord_id` BIGINT NOT NULL COMMENT '房东 ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '租金金额',
  `due_date` DATE NOT NULL COMMENT '应收日期',
  `paid_date` DATE DEFAULT NULL COMMENT '实收日期',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-未支付，1-已支付，2-逾期',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_property_id` (`property_id`),
  KEY `idx_landlord_id` (`landlord_id`),
  KEY `idx_status` (`status`),
  KEY `idx_due_date` (`due_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租金记录表';

-- =====================================================
-- 6. 押金记录表
-- =====================================================
DROP TABLE IF EXISTS `t_deposit_record`;
CREATE TABLE `t_deposit_record` (
  `id` BIGINT NOT NULL COMMENT '记录 ID',
  `tenant_id` BIGINT NOT NULL COMMENT '租客 ID',
  `property_id` BIGINT NOT NULL COMMENT '房源 ID',
  `landlord_id` BIGINT NOT NULL COMMENT '房东 ID',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '押金金额',
  `paid_date` DATE DEFAULT NULL COMMENT '支付日期',
  `refund_date` DATE DEFAULT NULL COMMENT '退还日期',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-未支付，1-已支付，2-已退还',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_property_id` (`property_id`),
  KEY `idx_landlord_id` (`landlord_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='押金记录表';

-- =====================================================
-- 7. 水电单价配置表
-- =====================================================
DROP TABLE IF EXISTS `t_utility_price`;
CREATE TABLE `t_utility_price` (
  `id` BIGINT NOT NULL COMMENT '配置 ID',
  `property_id` BIGINT NOT NULL COMMENT '房源 ID',
  `type` TINYINT NOT NULL COMMENT '类型：1-水费，2-电费',
  `unit_price` DECIMAL(10,4) NOT NULL COMMENT '单价（元/度或元/吨）',
  `effective_date` DATE NOT NULL COMMENT '生效日期',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_property_id` (`property_id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水电单价配置表';

-- =====================================================
-- 8. 水电费账单表
-- =====================================================
DROP TABLE IF EXISTS `t_utility_bill`;
CREATE TABLE `t_utility_bill` (
  `id` BIGINT NOT NULL COMMENT '账单 ID',
  `tenant_id` BIGINT NOT NULL COMMENT '租客 ID',
  `property_id` BIGINT NOT NULL COMMENT '房源 ID',
  `landlord_id` BIGINT NOT NULL COMMENT '房东 ID',
  `type` TINYINT NOT NULL COMMENT '类型：1-水费，2-电费',
  `unit_price` DECIMAL(10,4) DEFAULT NULL COMMENT '单价（元/度或元/吨）',
  `previous_reading` DECIMAL(10,2) DEFAULT NULL COMMENT '上期读数',
  `current_reading` DECIMAL(10,2) DEFAULT NULL COMMENT '本期读数',
  `usage` DECIMAL(10,2) DEFAULT NULL COMMENT '使用量',
  `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '账单金额',
  `bill_month` VARCHAR(7) DEFAULT NULL COMMENT '账单月份（如：2026-03）',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-未支付，1-已支付',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_property_id` (`property_id`),
  KEY `idx_landlord_id` (`landlord_id`),
  KEY `idx_type` (`type`),
  KEY `idx_bill_month` (`bill_month`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水电费账单表';

-- =====================================================
-- 9. 其他费用表
-- =====================================================
DROP TABLE IF EXISTS `t_other_fee`;
CREATE TABLE `t_other_fee` (
  `id` BIGINT NOT NULL COMMENT '费用 ID',
  `tenant_id` BIGINT NOT NULL COMMENT '租客 ID',
  `property_id` BIGINT NOT NULL COMMENT '房源 ID',
  `landlord_id` BIGINT NOT NULL COMMENT '房东 ID',
  `fee_type` VARCHAR(50) NOT NULL COMMENT '费用类型（管理费、网络费、垃圾费等）',
  `amount` DECIMAL(10,2) NOT NULL COMMENT '费用金额',
  `bill_month` VARCHAR(7) DEFAULT NULL COMMENT '账单月份',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-未支付，1-已支付',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_property_id` (`property_id`),
  KEY `idx_landlord_id` (`landlord_id`),
  KEY `idx_bill_month` (`bill_month`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='其他费用表';

-- =====================================================
-- 10. 报修记录表
-- =====================================================
DROP TABLE IF EXISTS `t_repair`;
CREATE TABLE `t_repair` (
  `id` BIGINT NOT NULL COMMENT '报修 ID',
  `tenant_id` BIGINT NOT NULL COMMENT '租客 ID',
  `property_id` BIGINT NOT NULL COMMENT '房源 ID',
  `landlord_id` BIGINT NOT NULL COMMENT '房东 ID',
  `description` TEXT NOT NULL COMMENT '问题描述',
  `images` VARCHAR(1000) DEFAULT NULL COMMENT '图片 URL（多张逗号分隔）',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-待处理，1-处理中，2-已完成',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '处理备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_property_id` (`property_id`),
  KEY `idx_landlord_id` (`landlord_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修记录表';

-- =====================================================
-- 11. 电子合同表
-- =====================================================
DROP TABLE IF EXISTS `t_contract`;
CREATE TABLE `t_contract` (
  `id` BIGINT NOT NULL COMMENT '合同 ID',
  `tenant_id` BIGINT NOT NULL COMMENT '租客 ID',
  `property_id` BIGINT NOT NULL COMMENT '房源 ID',
  `landlord_id` BIGINT NOT NULL COMMENT '房东 ID',
  `contract_no` VARCHAR(50) DEFAULT NULL COMMENT '合同编号',
  `start_date` DATE NOT NULL COMMENT '租赁开始日期',
  `end_date` DATE NOT NULL COMMENT '租赁结束日期',
  `rent_amount` DECIMAL(10,2) NOT NULL COMMENT '租金金额',
  `deposit_amount` DECIMAL(10,2) NOT NULL COMMENT '押金金额',
  `terms` TEXT DEFAULT NULL COMMENT '合同条款（JSON）',
  `landlord_signature` VARCHAR(255) DEFAULT NULL COMMENT '房东签名 URL',
  `tenant_signature` VARCHAR(255) DEFAULT NULL COMMENT '租客签名 URL',
  `landlord_sign_time` DATETIME DEFAULT NULL COMMENT '房东签署时间',
  `tenant_sign_time` DATETIME DEFAULT NULL COMMENT '租客签署时间',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-待签署，1-房东已签，2-双方已签',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_property_id` (`property_id`),
  KEY `idx_landlord_id` (`landlord_id`),
  KEY `idx_contract_no` (`contract_no`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电子合同表';

-- =====================================================
-- 12. 消息通知表
-- =====================================================
DROP TABLE IF EXISTS `t_notification`;
CREATE TABLE `t_notification` (
  `id` BIGINT NOT NULL COMMENT '通知 ID',
  `user_id` BIGINT NOT NULL COMMENT '用户 ID',
  `title` VARCHAR(100) NOT NULL COMMENT '通知标题',
  `content` TEXT NOT NULL COMMENT '通知内容',
  `type` TINYINT DEFAULT 0 COMMENT '类型：0-系统通知，1-催租提醒，2-账单提醒',
  `is_read` TINYINT DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `related_id` BIGINT DEFAULT NULL COMMENT '关联 ID（账单 ID、合同 ID 等）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- =====================================================
-- 初始化数据
-- =====================================================

-- 插入测试用户（房东）
INSERT INTO `t_user` (`id`, `openid`, `phone`, `nickname`, `role`) VALUES
(1000000001, 'wx_landlord_001', '13800138001', '测试房东', 'landlord');

-- 插入测试用户（租客）
INSERT INTO `t_user` (`id`, `openid`, `phone`, `nickname`, `role`) VALUES
(1000000002, 'wx_tenant_001', '13800138002', '测试租客', 'tenant');

-- 插入测试房源
INSERT INTO `t_property` (`id`, `name`, `address`, `layout`, `area`, `rent_amount`, `status`, `create_by`) VALUES
(2000000001, '阳光公寓 101', '北京市朝阳区阳光小区 1 栋 101 室', '2 室 1 厅 1 卫', 65.00, 3500.00, 0, 1000000001),
(2000000002, '阳光公寓 102', '北京市朝阳区阳光小区 1 栋 102 室', '3 室 2 厅 1 卫', 85.00, 4500.00, 0, 1000000001);

-- 插入测试租客
INSERT INTO `t_tenant` (`id`, `user_id`, `property_id`, `name`, `id_card`, `phone`, `check_in_date`, `status`) VALUES
(3000000001, 1000000002, 2000000001, '张三', '110101199001011234', '13800138002', '2026-03-01', 0);

-- 插入测试租金记录
INSERT INTO `t_rent_record` (`id`, `tenant_id`, `property_id`, `landlord_id`, `amount`, `due_date`, `status`) VALUES
(4000000001, 3000000001, 2000000001, 1000000001, 3500.00, '2026-04-01', 0);

-- 插入测试押金记录
INSERT INTO `t_deposit_record` (`id`, `tenant_id`, `property_id`, `landlord_id`, `amount`, `paid_date`, `status`) VALUES
(5000000001, 3000000001, 2000000001, 1000000001, 7000.00, '2026-03-01', 1);

-- =====================================================
-- 视图和存储过程（可选）
-- =====================================================

-- 创建房东收入统计视图
CREATE OR REPLACE VIEW `v_landlord_income` AS
SELECT 
  r.landlord_id,
  DATE_FORMAT(r.paid_date, '%Y-%m') AS month,
  SUM(r.amount) AS rent_income,
  COUNT(DISTINCT r.tenant_id) AS tenant_count
FROM t_rent_record r
WHERE r.status = 1
GROUP BY r.landlord_id, DATE_FORMAT(r.paid_date, '%Y-%m');

-- =====================================================
-- 完成提示
-- =====================================================
SELECT '数据库表结构创建完成！' AS message;
SELECT '共创建 12 张表：t_user, t_building, t_property, t_tenant, t_rent_record, t_deposit_record, t_utility_price, t_utility_bill, t_other_fee, t_repair, t_contract, t_notification' AS tables;
SELECT '已插入测试数据' AS data;
