package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 押金记录实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_deposit_record")
public class DepositRecord {

    /**
     * 记录 ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 租客 ID
     */
    private Long tenantId;

    /**
     * 房源 ID
     */
    private Long propertyId;

    /**
     * 房东 ID
     */
    private Long landlordId;

    /**
     * 押金金额
     */
    private BigDecimal amount;

    /**
     * 支付日期
     */
    private LocalDate paidDate;

    /**
     * 退还日期
     */
    private LocalDate refundDate;

    /**
     * 状态：0-未支付，1-已支付，2-已退还
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
