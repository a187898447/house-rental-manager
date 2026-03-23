package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租金账单实体
 */
@Data
@TableName("rent_record")
public class RentRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long propertyId;

    private BigDecimal amount;

    private Integer days;

    private BigDecimal dailyRate;

    private String payMonth;

    private LocalDate payDate;

    /**
     * 0:待支付 1:已支付 2:已逾期 3:已取消
     */
    private Integer status;

    private Integer remindCount;

    /**
     * 支付方式：cash-现金、transfer-转账、wechat-微信、alipay-支付宝
     */
    private String payMethod;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
