package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 押金实体
 */
@Data
@TableName("deposit")
public class Deposit {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long propertyId;

    private Long contractId;

    private BigDecimal amount;

    /**
     * 0:待退还 1:已退还 2:部分退还
     */
    private Integer status;

    private LocalDateTime payDate;

    private LocalDateTime refundDate;

    private BigDecimal refundAmount;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
