-- =====================================================
-- 租房小程序数据库建表语句 (MySQL 8.0)
-- =====================================================

-- ----------------------------------------
-- 1. 用户表
-- ----------------------------------------
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `openid` VARCHAR(100) COMMENT '微信openid',
    `phone` VARCHAR(20) COMMENT '手机号',
    `nickname` VARCHAR(100) COMMENT '昵称',
    `avatar_url` VARCHAR(500) COMMENT '头像URL',
    `role` VARCHAR(20) DEFAULT 'tenant' COMMENT '角色: landlord-房东, tenant-租客',
    `wx_unionid` VARCHAR(100) COMMENT '微信unionid',
    `status` INT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_user_openid` (`openid`),
    INDEX `idx_user_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------------------
-- 2. 楼栋表
-- ----------------------------------------
CREATE TABLE `building` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `owner_id` BIGINT NOT NULL COMMENT '房东ID',
    `name` VARCHAR(100) NOT NULL COMMENT '楼栋名称',
    `address` VARCHAR(255) COMMENT '楼栋地址',
    `water_price` DECIMAL(10,2) DEFAULT 3.5 COMMENT '水费单价（元/吨）',
    `electricity_price` DECIMAL(10,2) DEFAULT 0.6 COMMENT '电费单价（元/度）',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_building_owner` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼栋表';

-- ----------------------------------------
-- 3. 房源表
-- ----------------------------------------
CREATE TABLE `property` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `owner_id` BIGINT NOT NULL COMMENT '房东ID',
    `building_id` BIGINT COMMENT '楼栋ID',
    `name` VARCHAR(200) COMMENT '房源名称',
    `address` VARCHAR(500) COMMENT '详细地址',
    `building` VARCHAR(50) COMMENT '楼栋（例:1栋）',
    `unit` VARCHAR(50) COMMENT '单元',
    `room_number` VARCHAR(50) NOT NULL COMMENT '房号',
    `type` VARCHAR(50) COMMENT '户型（例:2室1厅）',
    `area` DECIMAL(10,2) COMMENT '面积（㎡）',
    `rent_amount` DECIMAL(10,2) NOT NULL COMMENT '月租金',
    `deposit_amount` DECIMAL(10,2) COMMENT '押金',
    `water_price` DECIMAL(10,2) DEFAULT 3.5 COMMENT '水费单价（元/吨）',
    `electricity_price` DECIMAL(10,2) DEFAULT 0.6 COMMENT '电费单价（元/度）',
    `status` INT DEFAULT 0 COMMENT '状态: 0-未出租, 1-已出租',
    `daily_rate` DECIMAL(10,2) COMMENT '日租金（按天出租）',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_property_owner` (`owner_id`),
    INDEX `idx_property_building` (`building_id`),
    INDEX `idx_property_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源表';

-- ----------------------------------------
-- 4. 租客表
-- ----------------------------------------
CREATE TABLE `tenant` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `user_id` BIGINT COMMENT '关联用户ID',
    `name` VARCHAR(100) COMMENT '租客姓名',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `lease_start_date` DATE COMMENT '租约开始日期',
    `lease_end_date` DATE COMMENT '租约结束日期',
    `id_card` VARCHAR(20) COMMENT '身份证号',
    `emergency_contact` VARCHAR(100) COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) COMMENT '紧急联系电话',
    `status` INT DEFAULT 1 COMMENT '状态: 1-租住中, 2-已退租',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_tenant_property` (`property_id`),
    INDEX `idx_tenant_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租客表';

-- ----------------------------------------
-- 5. 租金记录表
-- ----------------------------------------
CREATE TABLE `rent_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '租金金额',
    `days` INT COMMENT '出租天数（按天计费）',
    `daily_rate` DECIMAL(10,2) COMMENT '日租金',
    `pay_month` VARCHAR(10) NOT NULL COMMENT '所属月份（YYYY-MM）',
    `pay_date` DATE COMMENT '实际支付日期',
    `status` INT DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付, 2-已逾期, 3-已取消',
    `remind_count` INT DEFAULT 0 COMMENT '催租提醒次数',
    `last_remind_date` DATE COMMENT '上次提醒日期',
    `remind_status` INT DEFAULT 0 COMMENT '提醒状态: 0-未提醒, 1-D+0已提醒, 2-D+2已提醒, 3-D+3已提醒',
    `pay_method` VARCHAR(20) COMMENT '支付方式: cash-现金, transfer-转账, wechat-微信, alipay-支付宝',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_rent_tenant` (`tenant_id`),
    INDEX `idx_rent_property` (`property_id`),
    INDEX `idx_rent_month` (`pay_month`),
    INDEX `idx_rent_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租金记录表';

-- ----------------------------------------
-- 6. 押金表
-- ----------------------------------------
CREATE TABLE `deposit` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `contract_id` BIGINT COMMENT '合同ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '押金金额',
    `status` INT DEFAULT 0 COMMENT '状态: 0-待退还, 1-已退还, 2-部分退还',
    `refund_date` DATE COMMENT '退还日期',
    `refund_amount` DECIMAL(10,2) COMMENT '实退金额',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_deposit_tenant` (`tenant_id`),
    INDEX `idx_deposit_property` (`property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='押金表';

-- ----------------------------------------
-- 7. 水电账单表
-- ----------------------------------------
CREATE TABLE `utility_bill` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `bill_month` VARCHAR(10) NOT NULL COMMENT '账单月份（YYYY-MM）',
    `water_reading` DECIMAL(10,2) COMMENT '上期水表读数',
    `water_reading_current` DECIMAL(10,2) COMMENT '本期水表读数',
    `water_amount` DECIMAL(10,2) COMMENT '水费',
    `electricity_reading` DECIMAL(10,2) COMMENT '上期电表读数',
    `electricity_reading_current` DECIMAL(10,2) COMMENT '本期电表读数',
    `electricity_amount` DECIMAL(10,2) COMMENT '电费',
    `source` INT DEFAULT 0 COMMENT '来源: 0-手动, 1-API获取',
    `status` INT DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付',
    `pay_date` DATE COMMENT '支付日期',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_utility_tenant` (`tenant_id`),
    INDEX `idx_utility_property` (`property_id`),
    INDEX `idx_utility_month` (`bill_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水电账单表';

-- ----------------------------------------
-- 8. 其他费用表
-- ----------------------------------------
CREATE TABLE `other_fee` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `fee_type` VARCHAR(50) NOT NULL COMMENT '费用类型: management-管理费, network-网络费, garbage-垃圾费, other-其他',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `bill_month` VARCHAR(10) NOT NULL COMMENT '账单月份（YYYY-MM）',
    `status` INT DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付',
    `pay_date` DATE COMMENT '支付日期',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_otherfee_tenant` (`tenant_id`),
    INDEX `idx_otherfee_property` (`property_id`),
    INDEX `idx_otherfee_month` (`bill_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='其他费用表';

-- ----------------------------------------
-- 9. 合同表
-- ----------------------------------------
CREATE TABLE `contract` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `owner_id` BIGINT NOT NULL COMMENT '房东ID',
    `rent_amount` DECIMAL(10,2) NOT NULL COMMENT '月租金',
    `deposit_amount` DECIMAL(10,2) NOT NULL COMMENT '押金',
    `water_fee` DECIMAL(10,2) COMMENT '水费押金',
    `electricity_fee` DECIMAL(10,2) COMMENT '电费押金',
    `other_fees` TEXT COMMENT '其他费用（JSON）',
    `start_date` DATE NOT NULL COMMENT '合同开始日期',
    `end_date` DATE NOT NULL COMMENT '合同结束日期',
    `sign_url` VARCHAR(500) COMMENT '签名图片URL',
    `status` INT DEFAULT 0 COMMENT '状态: 0-草稿, 1-已签署, 2-已解除',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_contract_tenant` (`tenant_id`),
    INDEX `idx_contract_property` (`property_id`),
    INDEX `idx_contract_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';

-- ----------------------------------------
-- 10. 租客邀请表
-- ----------------------------------------
CREATE TABLE `tenant_invite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `invite_code` VARCHAR(32) NOT NULL COMMENT '邀请码',
    `expired_at` TIMESTAMP NOT NULL COMMENT '过期时间',
    `max_use_count` INT DEFAULT 1 COMMENT '最大使用次数',
    `used_count` INT DEFAULT 0 COMMENT '已使用次数',
    `status` INT DEFAULT 1 COMMENT '状态: 1-有效, 2-已使用, 3-已过期',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_invite_code` (`invite_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租客邀请表';

-- ----------------------------------------
-- 11. 收据表
-- ----------------------------------------
CREATE TABLE `receipt` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `type` VARCHAR(50) NOT NULL COMMENT '收据类型: rent-租金, deposit-押金, utility-水电, other-其他',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `file_url` VARCHAR(500) COMMENT '文件URL',
    `file_type` VARCHAR(20) COMMENT '文件类型: PDF, IMG',
    `bill_month` VARCHAR(10) COMMENT '账单月份',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_receipt_tenant` (`tenant_id`),
    INDEX `idx_receipt_property` (`property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收据表';

-- ----------------------------------------
-- 12. 报修表
-- ----------------------------------------
CREATE TABLE `repair_request` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `title` VARCHAR(200) NOT NULL COMMENT '报修标题',
    `description` TEXT COMMENT '问题描述',
    `images` VARCHAR(1000) COMMENT '图片URL（JSON数组）',
    `status` INT DEFAULT 0 COMMENT '状态: 0-待处理, 1-处理中, 2-已完成, 3-已取消',
    `handler_id` BIGINT COMMENT '处理人ID（房东）',
    `handle_remark` VARCHAR(500) COMMENT '处理备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_repair_tenant` (`tenant_id`),
    INDEX `idx_repair_property` (`property_id`),
    INDEX `idx_repair_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修表';

-- ----------------------------------------
-- 13. 消息表
-- ----------------------------------------
CREATE TABLE `message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tenant_id` BIGINT NOT NULL COMMENT '发送者ID（租客）',
    `owner_id` BIGINT NOT NULL COMMENT '接收者ID（房东）',
    `property_id` BIGINT COMMENT '房源ID',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `type` INT DEFAULT 1 COMMENT '类型: 1-租客发, 2-房东回复',
    `is_read` INT DEFAULT 0 COMMENT '状态: 0-未读, 1-已读',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_message_tenant` (`tenant_id`),
    INDEX `idx_message_owner` (`owner_id`),
    INDEX `idx_message_property` (`property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- ----------------------------------------
-- 14. 预约看房表
-- ----------------------------------------
CREATE TABLE `appointment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `tenant_name` VARCHAR(100) COMMENT '访客姓名',
    `tenant_phone` VARCHAR(20) COMMENT '访客电话',
    `appointment_date` TIMESTAMP COMMENT '预约日期',
    `status` INT DEFAULT 0 COMMENT '状态: 0-待确认, 1-已确认, 2-已取消, 3-已完成',
    `remark` VARCHAR(500) COMMENT '备注',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_appointment_property` (`property_id`),
    INDEX `idx_appointment_phone` (`tenant_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约看房表';

-- =====================================================
-- 建表完成
-- =====================================================

-- ----------------------------------------
-- 收据表
-- ----------------------------------------
CREATE TABLE IF NOT EXISTS `receipt` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type` VARCHAR(20) NOT NULL COMMENT '关联类型: rent-租金, deposit-押金, utility-水电, other-其他',
    `business_id` BIGINT COMMENT '关联的业务ID',
    `property_id` BIGINT NOT NULL COMMENT '房源ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租客ID',
    `owner_id` BIGINT NOT NULL COMMENT '房东ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `pay_method` VARCHAR(20) COMMENT '支付方式: cash-现金, alipay-支付宝, wechat-微信, bank-银行',
    `month` VARCHAR(10) COMMENT '收据月份',
    `remark` VARCHAR(500) COMMENT '备注',
    `file_url` VARCHAR(500) COMMENT '收据文件URL',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-否, 1-是',
    PRIMARY KEY (`id`),
    INDEX `idx_receipt_owner` (`owner_id`),
    INDEX `idx_receipt_property` (`property_id`),
    INDEX `idx_receipt_type` (`type`),
    INDEX `idx_receipt_month` (`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收据表';
