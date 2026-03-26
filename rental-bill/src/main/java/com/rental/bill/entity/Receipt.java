package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收据实体
 */
@Data
@TableName("receipt")
public class Receipt {

    @TableLogic
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联类型: rent-租金, deposit-押金, utility-水电, other-其他
     */
    private String type;

    /**
     * 关联的业务ID（租金ID/押金ID等）
     */
    private Long businessId;

    private Long propertyId;

    private Long tenantId;

    private Long ownerId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 支付方式: cash-现金, alipay-支付宝, wechat-微信, bank-银行
     */
    private String payMethod;

    /**
     * 收据月份
     */
    private String month;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收据文件URL
     */
    private String fileUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}