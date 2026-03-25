package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 水电账单实体
 */
@Data
@TableName("utility_bill")
public class UtilityBill {

    @TableLogic
@TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long propertyId;

    private String billMonth;

    private BigDecimal waterReading;

    private BigDecimal waterReadingCurrent;

    private BigDecimal waterAmount;

    private BigDecimal electricityReading;

    private BigDecimal electricityReadingCurrent;

    private BigDecimal electricityAmount;

    /**
     * 0:手动 1:API获取
     */
    private Integer source;

    /**
     * 0:待支付 1:已支付
     */
    private Integer status;

    private LocalDate payDate;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;


    @TableLogic
    private Integer deleted;
}
