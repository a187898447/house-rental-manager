package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 水电费账单实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_utility_bill")
public class UtilityBill {

    /**
     * 账单 ID
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
     * 类型：1-水费，2-电费
     */
    private Integer type;

    /**
     * 单价（元/度或元/吨）
     */
    private BigDecimal unitPrice;

    /**
     * 上期读数
     */
    private BigDecimal previousReading;

    /**
     * 本期读数
     */
    private BigDecimal currentReading;

    /**
     * 使用量
     */
    private BigDecimal usage;

    /**
     * 账单金额
     */
    private BigDecimal amount;

    /**
     * 账单月份（如：2026-03）
     */
    private String billMonth;

    /**
     * 状态：0-未支付，1-已支付
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
