-- =============================================
-- 租房小程序 MySQL 8 建表语句
-- 数据库: house_rental
-- 版本: 1.0
-- 创建日期: 2026-03-23
-- =============================================

CREATE DATABASE IF NOT EXISTS house_rental DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE house_rental;

-- =============================================
-- 1. 用户表 (user)
-- =============================================
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    openid          VARCHAR(64) UNIQUE COMMENT '微信OpenID',
    phone           VARCHAR(20) COMMENT '手机号',
    nickname        VARCHAR(50) COMMENT '昵称',
    avatar          VARCHAR(255) COMMENT '头像URL',
    role            TINYINT NOT NULL DEFAULT 0 COMMENT '角色: 0-房东, 1-租客',
    status          TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_openid (openid),
    INDEX idx_phone (phone),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 2. 楼栋表 (building)
-- =============================================
DROP TABLE IF EXISTS building;
CREATE TABLE building (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id         BIGINT NOT NULL COMMENT '房东ID',
    name            VARCHAR(50) NOT NULL COMMENT '楼栋名称(例:1栋、2栋)',
    address         VARCHAR(255) COMMENT '楼栋地址',
    water_price     DECIMAL(10,2) DEFAULT 0 COMMENT '水费单价(元/吨)',
    electricity_price DECIMAL(10,2) DEFAULT 0 COMMENT '电费单价(元/度)',
    remark          VARCHAR(500) COMMENT '备注',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼栋表';

-- =============================================
-- 3. 房源表 (property)
-- =============================================
DROP TABLE IF EXISTS property;
CREATE TABLE property (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id         BIGINT NOT NULL COMMENT '房东ID',
    building_id     BIGINT COMMENT '楼栋ID',
    name            VARCHAR(100) COMMENT '房源名称',
    address         VARCHAR(255) COMMENT '详细地址',
    building        VARCHAR(50) COMMENT '楼栋(例:1栋)',
    unit            VARCHAR(20) COMMENT '单元(例:101)',
    room_number     VARCHAR(20) NOT NULL COMMENT '房号',
    type            VARCHAR(50) COMMENT '户型(例:2室1厅)',
    area            DECIMAL(10,2) COMMENT '面积(㎡)',
    rent_amount     DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '月租金',
    deposit_amount  DECIMAL(10,2) DEFAULT 0 COMMENT '押金',
    daily_rate     DECIMAL(10,2) DEFAULT 0 COMMENT '日租金',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-未出租, 1-已出租',
    remark          VARCHAR(500) COMMENT '备注',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_user_id (user_id),
    INDEX idx_building_id (building_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房源表';

-- =============================================
-- 4. 租客表 (tenant)
-- =============================================
DROP TABLE IF EXISTS tenant;
CREATE TABLE tenant (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id         BIGINT NOT NULL COMMENT '房东ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    name            VARCHAR(50) NOT NULL COMMENT '姓名',
    phone           VARCHAR(20) NOT NULL COMMENT '电话',
    id_card         VARCHAR(20) COMMENT '身份证',
    lease_start_date DATE COMMENT '租期开始日期',
    lease_end_date   DATE COMMENT '租期结束日期',
    rent_pay_day    TINYINT DEFAULT 1 COMMENT '租金支付日(每月几号)',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系人电话',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-租住中, 1-已退租',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_user_id (user_id),
    INDEX idx_property_id (property_id),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租客表';

-- =============================================
-- 5. 租金记录表 (rent_record)
-- =============================================
DROP TABLE IF EXISTS rent_record;
CREATE TABLE rent_record (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    tenant_id       BIGINT NOT NULL COMMENT '租客ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    amount          DECIMAL(10,2) NOT NULL COMMENT '租金金额',
    days            INT COMMENT '计费天数',
    daily_rate      DECIMAL(10,2) COMMENT '日租金',
    pay_month       VARCHAR(10) NOT NULL COMMENT '账单月份(例:2026-03)',
    pay_date        DATE COMMENT '实际支付日期',
    pay_method      VARCHAR(20) COMMENT '支付方式: cash-现金, transfer-转账, wechat-微信',
    remind_count    INT DEFAULT 0 COMMENT '催租提醒次数',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付, 2-已逾期, 3-已取消',
    remark          VARCHAR(500) COMMENT '备注',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_pay_month (pay_month),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租金记录表';

-- =============================================
-- 6. 押金表 (deposit)
-- =============================================
DROP TABLE IF EXISTS deposit;
CREATE TABLE deposit (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    tenant_id       BIGINT NOT NULL COMMENT '租客ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    amount          DECIMAL(10,2) NOT NULL COMMENT '押金金额',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待退还, 1-已退还',
    refund_date     DATE COMMENT '退还日期',
    refund_amount   DECIMAL(10,2) COMMENT '实际退还金额',
    remark          VARCHAR(500) COMMENT '备注',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='押金表';

-- =============================================
-- 7. 水电账单表 (utility_bill)
-- =============================================
DROP TABLE IF EXISTS utility_bill;
CREATE TABLE utility_bill (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    tenant_id       BIGINT NOT NULL COMMENT '租客ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    bill_month      VARCHAR(10) NOT NULL COMMENT '账单月份(例:2026-03)',
    water_reading   DECIMAL(10,2) COMMENT '水表读数',
    water_amount    DECIMAL(10,2) DEFAULT 0 COMMENT '水费金额',
    electricity_reading DECIMAL(10,2) COMMENT '电表读数',
    electricity_amount DECIMAL(10,2) DEFAULT 0 COMMENT '电费金额',
    previous_water_reading DECIMAL(10,2) COMMENT '上期水表读数',
    previous_electricity_reading DECIMAL(10,2) COMMENT '上期电表读数',
    source          TINYINT DEFAULT 0 COMMENT '数据来源: 0-手动, 1-官方API',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付',
    pay_date        DATE COMMENT '支付日期',
    remark          VARCHAR(500) COMMENT '备注',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_bill_month (bill_month),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='水电账单表';

-- =============================================
-- 8. 其他费用表 (other_fee)
-- =============================================
DROP TABLE IF EXISTS other_fee;
CREATE TABLE other_fee (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    tenant_id       BIGINT NOT NULL COMMENT '租客ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    fee_type        VARCHAR(50) NOT NULL COMMENT '费用类型: 管理费,网络费,垃圾费,维修费等',
    amount          DECIMAL(10,2) NOT NULL COMMENT '金额',
    bill_month      VARCHAR(10) NOT NULL COMMENT '账单月份',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付',
    pay_date        DATE COMMENT '支付日期',
    remark          VARCHAR(500) COMMENT '备注',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_fee_type (fee_type),
    INDEX idx_bill_month (bill_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='其他费用表';

-- =============================================
-- 9. 合同表 (contract)
-- =============================================
DROP TABLE IF EXISTS contract;
CREATE TABLE contract (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    tenant_id       BIGINT NOT NULL COMMENT '租客ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    title           VARCHAR(100) COMMENT '合同标题',
    content         TEXT COMMENT '合同内容',
    rent_amount     DECIMAL(10,2) NOT NULL COMMENT '月租金',
    deposit_amount  DECIMAL(10,2) NOT NULL COMMENT '押金金额',
    water_fee       DECIMAL(10,2) COMMENT '水费单价(吨/元)',
    electricity_fee DECIMAL(10,2) COMMENT '电费单价(度/元)',
    other_fees      JSON COMMENT '其他费用配置(JSON)',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待签署, 1-已签署, 2-已生效, 3-已终止, 4-已解除',
    landlord_sign   VARCHAR(255) COMMENT '房东签名图片URL',
    tenant_sign    VARCHAR(255) COMMENT '租客签名图片URL',
    sign_date      DATE COMMENT '签署日期',
    start_date     DATE NOT NULL COMMENT '合同生效日期',
    end_date       DATE NOT NULL COMMENT '合同结束日期',
    termination_date DATE COMMENT '终止日期',
    termination_reason VARCHAR(500) COMMENT '终止原因',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_property_id (property_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同表';

-- =============================================
-- 10. 预约看房表 (appointment)
-- =============================================
DROP TABLE IF EXISTS appointment;
CREATE TABLE appointment (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    user_id         BIGINT COMMENT '用户ID(租客)',
    name            VARCHAR(50) NOT NULL COMMENT '姓名',
    phone           VARCHAR(20) NOT NULL COMMENT '电话',
    appointment_date DATE NOT NULL COMMENT '预约日期',
    appointment_time VARCHAR(20) NOT NULL COMMENT '预约时间段',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待确认, 1-已确认, 2-已完成, 3-已取消, 4-已拒绝',
    remark          VARCHAR(500) COMMENT '备注',
    reject_reason   VARCHAR(500) COMMENT '拒绝原因',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_property_id (property_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约看房表';

-- =============================================
-- 11. 报修表 (repair)
-- =============================================
DROP TABLE IF EXISTS repair;
CREATE TABLE repair (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    tenant_id       BIGINT COMMENT '租客ID',
    user_id         BIGINT COMMENT '报修人ID',
    title           VARCHAR(100) NOT NULL COMMENT '报修标题',
    description     TEXT COMMENT '问题描述',
    images          JSON COMMENT '报修图片(数组)',
    contact_phone   VARCHAR(20) COMMENT '联系电话',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待处理, 1-处理中, 2-已完成, 3-已取消',
    handler_id      BIGINT COMMENT '处理人ID',
    handle_remark   TEXT COMMENT '处理备注',
    handle_images   JSON COMMENT '处理图片',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted_at      DATETIME DEFAULT NULL COMMENT '删除时间',
    INDEX idx_property_id (property_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修表';

-- =============================================
-- 12. 通知消息表 (notify_message)
-- =============================================
DROP TABLE IF EXISTS notify_message;
CREATE TABLE notify_message (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id         BIGINT NOT NULL COMMENT '接收用户ID',
    sender_id       BIGINT COMMENT '发送用户ID',
    type            VARCHAR(30) NOT NULL COMMENT '消息类型: rent-租金, appointment-预约, contract-合同, repair-报修, system-系统',
    title           VARCHAR(100) NOT NULL COMMENT '标题',
    content         TEXT NOT NULL COMMENT '内容',
    related_id      BIGINT COMMENT '关联业务ID',
    related_type   VARCHAR(30) COMMENT '关联业务类型',
    is_read         TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读: 0-未读, 1-已读',
    read_at         DATETIME COMMENT '阅读时间',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';

-- =============================================
-- 13. 电子收据表 (receipt)
-- =============================================
DROP TABLE IF EXISTS receipt;
CREATE TABLE receipt (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    type            VARCHAR(30) NOT NULL COMMENT '收据类型: rent-租金, deposit-押金, utility-水电, other-其他',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    tenant_id       BIGINT NOT NULL COMMENT '租客ID',
    amount          DECIMAL(10,2) NOT NULL COMMENT '金额',
    receipt_date    DATE NOT NULL COMMENT '收据日期',
    file_url        VARCHAR(255) COMMENT '导出文件URL',
    file_type       VARCHAR(20) DEFAULT 'pdf' COMMENT '文件类型: pdf, png',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-生成中, 1-已生成, 2-生成失败',
    remark          VARCHAR(500) COMMENT '备注',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_property_id (property_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电子收据表';

-- =============================================
-- 14. 租客邀请表 (tenant_invite)
-- =============================================
DROP TABLE IF EXISTS tenant_invite;
CREATE TABLE tenant_invite (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    property_id     BIGINT NOT NULL COMMENT '房源ID',
    invite_code    VARCHAR(20) NOT NULL UNIQUE COMMENT '邀请码',
    status          TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-未使用, 1-已使用, 2-已过期',
    expired_at     DATETIME NOT NULL COMMENT '过期时间',
    used_at        DATETIME COMMENT '使用时间',
    used_user_id   BIGINT COMMENT '使用者ID',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_property_id (property_id),
    INDEX idx_invite_code (invite_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租客邀请表';

-- =============================================
-- 完成
-- =============================================
