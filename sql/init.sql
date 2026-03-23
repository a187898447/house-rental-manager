-- =====================================================
-- 租房小程序数据库建表语句 (MySQL 8)
-- 数据库: house_rental
-- 版本: 1.0
-- 创建日期: 2026-03-23
-- =====================================================

CREATE DATABASE IF NOT EXISTS house_rental DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE house_rental;

-- ----------------------------------------
-- 1. 用户表 (t_user)
-- ----------------------------------------
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    openid VARCHAR(100) COMMENT '微信openid',
    phone VARCHAR(20) COMMENT '手机号',
    nickname VARCHAR(100) COMMENT '昵称',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    role VARCHAR(20) DEFAULT 'tenant' COMMENT '角色: landlord-房东, tenant-租客, admin-管理员',
    status INT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_openid (openid),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------------------
-- 2. 楼栋表 (building)
-- ----------------------------------------
DROP TABLE IF EXISTS building;
CREATE TABLE building (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼栋ID',
    owner_id BIGINT NOT NULL COMMENT '房东ID',
    name VARCHAR(100) NOT NULL COMMENT '楼栋名称',
    address VARCHAR(255) COMMENT '地址',
    water_price DECIMAL(10,2) DEFAULT 3.5 COMMENT '水费单价(元/吨)',
    electricity_price DECIMAL(10,2) DEFAULT 0.6 COMMENT '电费单价(元/度)',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_owner_id (owner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼栋表';

-- ----------------------------------------
-- 3. 房源表 (property)
-- ----------------------------------------
DROP TABLE IF EXISTS property;
CREATE TABLE property (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '房源ID',
    owner_id BIGINT NOT NULL COMMENT '房东ID',
    building_id BIGINT COMMENT '楼栋ID',
    name VARCHAR(200) COMMENT '房源名称',
    address VARCHAR(500) COMMENT '详细地址',
    building VARCHAR(50) COMMENT '楼栋(例:1栋)',
    unit VARCHAR(50) COMMENT '单元',
    room_number VARCHAR(50) NOT NULL COMMENT '房号',
    type VARCHAR(50) COMMENT '户型(例:2室1厅)',
    area DECIMAL(10,2) COMMENT '面积(㎡)',
    rent_amount DECIMAL(10,2) NOT NULL COMMENT '月租金',
    deposit_amount DECIMAL(10,2) COMMENT '押金',
    status INT DEFAULT 0 COMMENT '状态: 0-未出租, 1-已出租',
    daily_rate DECIMAL(10,2) COMMENT '日租金(按天出租)',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_owner_id (owner_id),
    INDEX idx_building_id (building_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房源表';

-- ----------------------------------------
-- 4. 租客表 (tenant)
-- ----------------------------------------
DROP TABLE IF EXISTS tenant;
CREATE TABLE tenant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '租客ID',
    user_id BIGINT COMMENT '租客用户ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    name VARCHAR(100) COMMENT '租客姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    id_card VARCHAR(20) COMMENT '身份证号',
    lease_start_date DATE COMMENT '租约开始日期',
    lease_end_date DATE COMMENT '租约结束日期',
    rent_pay_day TINYINT DEFAULT 1 COMMENT '租金支付日(每月几号)',
    emergency_contact VARCHAR(100) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    status INT DEFAULT 0 COMMENT '状态: 0-待入住, 1-已入住, 2-已退租',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_property_id (property_id),
    INDEX idx_user_id (user_id),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租客表';

-- ----------------------------------------
-- 5. 租金记录表 (rent_record)
-- ----------------------------------------
DROP TABLE IF EXISTS rent_record;
CREATE TABLE rent_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账单ID',
    tenant_id BIGINT NOT NULL COMMENT '租客ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '租金金额',
    days INT COMMENT '出租天数(按天计费)',
    daily_rate DECIMAL(10,2) COMMENT '日租金',
    pay_month VARCHAR(10) NOT NULL COMMENT '所属月份(YYYY-MM)',
    pay_date DATE COMMENT '实际支付日期',
    status INT DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付, 2-已逾期, 3-已取消',
    remind_count INT DEFAULT 0 COMMENT '催租提醒次数',
    pay_method VARCHAR(20) COMMENT '支付方式: cash-现金, transfer-转账, wechat-微信, alipay-支付宝',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_pay_month (pay_month),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租金记录表';

-- ----------------------------------------
-- 6. 押金表 (deposit)
-- ----------------------------------------
DROP TABLE IF EXISTS deposit;
CREATE TABLE deposit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '押金ID',
    tenant_id BIGINT NOT NULL COMMENT '租客ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '押金金额',
    status INT DEFAULT 0 COMMENT '状态: 0-待退还, 1-已退还, 2-部分退还',
    refund_date DATETIME COMMENT '退还日期',
    refund_amount DECIMAL(10,2) COMMENT '实际退还金额',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='押金表';

-- ----------------------------------------
-- 7. 水电账单表 (utility_bill)
-- ----------------------------------------
DROP TABLE IF EXISTS utility_bill;
CREATE TABLE utility_bill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账单ID',
    tenant_id BIGINT NOT NULL COMMENT '租客ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    bill_month VARCHAR(10) NOT NULL COMMENT '账单月份(YYYY-MM)',
    water_reading DECIMAL(10,2) COMMENT '水表读数',
    water_amount DECIMAL(10,2) DEFAULT 0 COMMENT '水费',
    electricity_reading DECIMAL(10,2) COMMENT '电表读数',
    electricity_amount DECIMAL(10,2) DEFAULT 0 COMMENT '电费',
    source INT DEFAULT 0 COMMENT '来源: 0-手动, 1-API获取',
    status INT DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付',
    pay_date DATETIME COMMENT '支付日期',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_bill_month (bill_month),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='水电账单表';

-- ----------------------------------------
-- 8. 其他费用表 (other_fee)
-- ----------------------------------------
DROP TABLE IF EXISTS other_fee;
CREATE TABLE other_fee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '费用ID',
    tenant_id BIGINT NOT NULL COMMENT '租客ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    fee_type VARCHAR(50) NOT NULL COMMENT '费用类型: management-管理费, network-网络费, garbage-垃圾费, other-其他',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    bill_month VARCHAR(10) NOT NULL COMMENT '账单月份(YYYY-MM)',
    status INT DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付',
    pay_date DATETIME COMMENT '支付日期',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_fee_type (fee_type),
    INDEX idx_bill_month (bill_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='其他费用表';

-- ----------------------------------------
-- 9. 合同表 (contract)
-- ----------------------------------------
DROP TABLE IF EXISTS contract;
CREATE TABLE contract (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '合同ID',
    tenant_id BIGINT NOT NULL COMMENT '租客ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    owner_id BIGINT NOT NULL COMMENT '房东ID',
    rent_amount DECIMAL(10,2) NOT NULL COMMENT '月租金',
    deposit_amount DECIMAL(10,2) NOT NULL COMMENT '押金',
    water_fee DECIMAL(10,2) COMMENT '水费单价',
    electricity_fee DECIMAL(10,2) COMMENT '电费单价',
    other_fees TEXT COMMENT '其他费用(JSON)',
    start_date DATE NOT NULL COMMENT '合同开始日期',
    end_date DATE NOT NULL COMMENT '合同结束日期',
    sign_url VARCHAR(500) COMMENT '签名图片URL',
    status INT DEFAULT 0 COMMENT '状态: 0-待签署, 1-已签署, 2-已生效, 3-已到期, 4-已解除',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_owner_id (owner_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同表';

-- ----------------------------------------
-- 10. 租客邀请表 (tenant_invite)
-- ----------------------------------------
DROP TABLE IF EXISTS tenant_invite;
CREATE TABLE tenant_invite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '邀请ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    invite_code VARCHAR(32) NOT NULL COMMENT '邀请码',
    expired_at DATETIME NOT NULL COMMENT '过期时间',
    max_use_count INT DEFAULT 1 COMMENT '最大使用次数',
    used_count INT DEFAULT 0 COMMENT '已使用次数',
    status INT DEFAULT 1 COMMENT '状态: 1-有效, 2-已使用, 3-已过期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_property_id (property_id),
    INDEX idx_invite_code (invite_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租客邀请表';

-- ----------------------------------------
-- 11. 收据表 (receipt)
-- ----------------------------------------
DROP TABLE IF EXISTS receipt;
CREATE TABLE receipt (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收据ID',
    tenant_id BIGINT NOT NULL COMMENT '租客ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    type VARCHAR(50) NOT NULL COMMENT '收据类型: rent-租金, deposit-押金, utility-水电, other-其他',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    file_url VARCHAR(500) COMMENT '文件URL',
    file_type VARCHAR(20) COMMENT '文件类型: PDF, IMG',
    bill_month VARCHAR(10) COMMENT '账单月份',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收据表';

-- ----------------------------------------
-- 12. 报修表 (repair)
-- ----------------------------------------
DROP TABLE IF EXISTS repair;
CREATE TABLE repair (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报修ID',
    tenant_id BIGINT NOT NULL COMMENT '租客ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    title VARCHAR(200) NOT NULL COMMENT '报修标题',
    description TEXT COMMENT '问题描述',
    images VARCHAR(1000) COMMENT '图片URL(JSON数组)',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    status INT DEFAULT 0 COMMENT '状态: 0-待处理, 1-处理中, 2-已完成, 3-已取消',
    handler_id BIGINT COMMENT '处理人ID(房东)',
    handle_remark VARCHAR(500) COMMENT '处理备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修表';

-- ----------------------------------------
-- 13. 预约看房表 (appointment)
-- ----------------------------------------
DROP TABLE IF EXISTS appointment;
CREATE TABLE appointment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '预约ID',
    property_id BIGINT NOT NULL COMMENT '房源ID',
    user_id BIGINT COMMENT '用户ID(租客)',
    tenant_name VARCHAR(100) COMMENT '租客姓名',
    tenant_phone VARCHAR(20) COMMENT '租客电话',
    appointment_date DATETIME NOT NULL COMMENT '预约看房时间',
    status INT DEFAULT 0 COMMENT '状态: 0-待确认, 1-已确认, 2-已取消, 3-已完成',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_property_id (property_id),
    INDEX idx_user_id (user_id),
    INDEX idx_tenant_phone (tenant_phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约看房表';

-- ----------------------------------------
-- 14. 通知消息表 (notify_message)
-- ----------------------------------------
DROP TABLE IF EXISTS notify_message;
CREATE TABLE notify_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    sender_id BIGINT COMMENT '发送者ID',
    type VARCHAR(30) NOT NULL COMMENT '消息类型: rent-租金, appointment-预约, contract-合同, repair-报修, system-系统',
    title VARCHAR(100) NOT NULL COMMENT '消息标题',
    content TEXT NOT NULL COMMENT '消息内容',
    related_id BIGINT COMMENT '关联业务ID',
    related_type VARCHAR(30) COMMENT '关联业务类型',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读: 0-未读, 1-已读',
    read_at DATETIME COMMENT '阅读时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';

-- =====================================================
-- 建表完成
-- =====================================================
