package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 其他费用实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_other_fee")
public class OtherFee {

    /**
     * 费用 ID
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
     * 费用类型（管理费、网络费、垃圾费等）
     */
    private String feeType;

    /**
     * 费用金额
     */
    private BigDecimal amount;

    /**
     * 账单月份
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
