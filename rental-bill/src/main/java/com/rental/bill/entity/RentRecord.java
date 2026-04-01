package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租金记录实体
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Data
@TableName("t_rent_record")
public class RentRecord {

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
     * 租金金额
     */
    private BigDecimal amount;

    /**
     * 应收日期
     */
    private LocalDate dueDate;

    /**
     * 实收日期
     */
    private LocalDate paidDate;

    /**
     * 状态：0-未支付，1-已支付，2-逾期
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
