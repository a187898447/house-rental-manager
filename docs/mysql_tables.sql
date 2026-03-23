-- =============================================
-- 租房小程序 MySQL 8 建表语句
-- =============================================

-- =============================================
-- 用户模块
-- =============================================
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar_url` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `openid` VARCHAR(100) DEFAULT NULL COMMENT '微信openid',
    `role` TINYINT DEFAULT 0 COMMENT '角色: 0-租客 1-房东',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_phone` (`phone`),
    KEY `idx_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 楼盘模块
-- =============================================
CREATE TABLE IF NOT EXISTS `building` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '楼盘ID',
    `owner_id` BIGINT NOT NULL COMMENT '房东ID',
    `name` VARCHAR(100) NOT NULL COMMENT '楼盘名称',
    `address` VARCHAR(500) DEFAULT NULL COMMENT '地址',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_owner_id` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼盘表';

-- =============================================
-- 房源模块
-- =============================================
CREATE TABLE IF NOT EXISTS `property` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '房源ID',
    `owner_id` BIGINT NOT NULL COMMENT '房东ID',
    `building_id` BIGINT DEFAULT NULL COMMENT '楼盘ID',
    `name` VARCHAR(100) DEFAULT NULL COMMENT '房源名称',
    `address` VARCHAR(500) DEFAULT NULL COMMENT '详细地址',
    `building` VARCHAR(50) DEFAULT NULL COMMENT '楼栋(例:1栋)',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '户型(例:2室1厅)',
    `unit` VARCHAR(20) DEFAULT NULL COMMENT '单元',
    `room_number` VARCHAR(20) NOT NULL COMMENT '房号',
    `area` DECIMAL(10,2) DEFAULT NULL COMMENT '面积(平方米)',
    `rent_amount` DECIMAL(10,2) NOT NULL COMMENT '租金(元/月)',
    `deposit_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '押金(元)',
    `daily_rate` DECIMAL(10,2) DEFAULT NULL COMMENT '日租金',
    `water_unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '水费单价(元/吨)',
    `electricity_unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '电费单价(元/度)',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-未出租 1-已出租',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_owner_id` (`owner_id`),
    KEY `idx_building_id` (`building_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房源表';

-- =============================================
-- 预约看房模块
-- =============================================
CREATE TABLE IF NOT EXISTS `appointment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `tenant_name` VARCHAR(50) NOT NULL COMMENT '租客姓名',
    `tenant_phone` VARCHAR(20) NOT NULL COMMENT '租客电话',
    `appointment_date` DATETIME NOT NULL COMMENT '预约看房时间',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待确认 1-已确认 2-已取消 3-已完成',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_property_id` (`property_id`),
    KEY `idx_tenant_phone` (`tenant_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约看房表';

-- =============================================
-- 租客模块
-- =============================================
CREATE TABLE IF NOT EXISTS `tenant` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '租客ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `name` VARCHAR(50) NOT NULL COMMENT '租客姓名',
    `phone` VARCHAR(20) NOT NULL COMMENT '租客电话',
    `id_card` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
    `lease_start_date` DATE DEFAULT NULL COMMENT '租赁开始日期',
    `lease_end_date` DATE DEFAULT NULL COMMENT '租赁结束日期',
    `emergency_contact` VARCHAR(50) DEFAULT NULL COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) DEFAULT NULL COMMENT '紧急联系电话',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待入住 1-已入住 2-已退租',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_property_id` (`property_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租客表';

-- =============================================
-- 合同模块
-- =============================================
CREATE TABLE IF NOT EXISTS `contract` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '合同ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `owner_id` BIGINT NOT NULL COMMENT '房东ID',
    `rent_amount` DECIMAL(10,2) NOT NULL COMMENT '租金(元/月)',
    `deposit_amount` DECIMAL(10,2) NOT NULL COMMENT '押金(元)',
    `water_fee` DECIMAL(10,2) DEFAULT 0 COMMENT '水费单价',
    `electricity_fee` DECIMAL(10,2) DEFAULT 0 COMMENT '电费单价',
    `other_fees` TEXT DEFAULT NULL COMMENT '其他费用说明',
    `start_date` DATE NOT NULL COMMENT '合同开始日期',
    `end_date` DATE NOT NULL COMMENT '合同结束日期',
    `sign_url` VARCHAR(500) DEFAULT NULL COMMENT '签署URL/图片',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待签署 1-已签署 2-已生效 3-已到期 4-已解除',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_property_id` (`property_id`),
    KEY `idx_owner_id` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同表';

-- =============================================
-- 租金账单模块
-- =============================================
CREATE TABLE IF NOT EXISTS `rent_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '账单ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '租金金额',
    `days` INT DEFAULT NULL COMMENT '租期天数',
    `daily_rate` DECIMAL(10,2) DEFAULT NULL COMMENT '日租金',
    `pay_month` VARCHAR(10) NOT NULL COMMENT '缴费月份(YYYY-MM)',
    `pay_date` DATE DEFAULT NULL COMMENT '实际支付日期',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待支付 1-已支付 2-已逾期 3-已取消',
    `remind_count` INT DEFAULT 0 COMMENT '提醒次数',
    `pay_method` VARCHAR(20) DEFAULT NULL COMMENT '支付方式: cash-现金, transfer-转账, wechat-微信, alipay-支付宝',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_property_id` (`property_id`),
    KEY `idx_pay_month` (`pay_month`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租金账单表';

-- =============================================
-- 押金模块
-- =============================================
CREATE TABLE IF NOT EXISTS `deposit` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '押金ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `contract_id` BIGINT DEFAULT NULL COMMENT '合同ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '押金金额',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待缴纳 1-已缴纳 2-待退还 3-已退还 4-已扣除',
    `pay_date` DATE DEFAULT NULL COMMENT '缴纳日期',
    `refund_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '退还金额',
    `refund_date` DATE DEFAULT NULL COMMENT '退还日期',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_property_id` (`property_id`),
    KEY `idx_contract_id` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='押金表';

-- =============================================
-- 水电账单模块
-- =============================================
CREATE TABLE IF NOT EXISTS `utility_bill` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '账单ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `bill_month` VARCHAR(10) NOT NULL COMMENT '账单月份(YYYY-MM)',
    `last_water_reading` DECIMAL(10,2) DEFAULT NULL COMMENT '上次水表读数',
    `water_reading` DECIMAL(10,2) DEFAULT NULL COMMENT '本次水表读数',
    `water_usage` DECIMAL(10,2) DEFAULT NULL COMMENT '用水量',
    `water_unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '水费单价',
    `water_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '水费金额',
    `last_electricity_reading` DECIMAL(10,2) DEFAULT NULL COMMENT '上次电表读数',
    `electricity_reading` DECIMAL(10,2) DEFAULT NULL COMMENT '本次电表读数',
    `electricity_usage` DECIMAL(10,2) DEFAULT NULL COMMENT '用电量',
    `electricity_unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '电费单价',
    `electricity_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '电费金额',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待支付 1-已支付 2-已逾期',
    `pay_date` DATE DEFAULT NULL COMMENT '支付日期',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_property_id` (`property_id`),
    KEY `idx_bill_month` (`bill_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='水电账单表';

-- =============================================
-- 其他费用模块
-- =============================================
CREATE TABLE IF NOT EXISTS `other_fee` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '费用ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `fee_type` VARCHAR(20) NOT NULL COMMENT '费用类型: parking-停车费, network-网络费, furniture-家具维修, cleaning-清洁费, other-其他',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `bill_month` VARCHAR(10) DEFAULT NULL COMMENT '账单月份',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待支付 1-已支付',
    `pay_date` DATE DEFAULT NULL COMMENT '支付日期',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_property_id` (`property_id`),
    KEY `idx_fee_type` (`fee_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='其他费用表';

-- =============================================
-- 报修模块
-- =============================================
CREATE TABLE IF NOT EXISTS `repair` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报修ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `title` VARCHAR(100) NOT NULL COMMENT '报修标题',
    `description` TEXT DEFAULT NULL COMMENT '问题描述',
    `images` TEXT DEFAULT NULL COMMENT '图片URL(JSON数组)',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待处理 1-处理中 2-已完成 3-已取消',
    `handler_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `handle_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_property_id` (`property_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修表';

-- =============================================
-- 通知消息模块
-- =============================================
CREATE TABLE IF NOT EXISTS `notify_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` VARCHAR(20) NOT NULL COMMENT '消息类型: appointment-预约, bill-账单, contract-合同, repair-报修, system-系统',
    `title` VARCHAR(100) NOT NULL COMMENT '消息标题',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `business_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
    `business_type` VARCHAR(20) DEFAULT NULL COMMENT '业务类型',
    `read_status` TINYINT DEFAULT 0 COMMENT '阅读状态: 0-未读 1-已读',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_read_status` (`read_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';

