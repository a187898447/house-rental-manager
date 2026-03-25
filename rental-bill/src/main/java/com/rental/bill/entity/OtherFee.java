import com.baomidou.mybatisplus.annotation.TableLogic;
package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 其他费用实体
 */
@Data
@TableName("other_fee")
public class OtherFee {

    @TableLogic
@TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long propertyId;

    /**
     * 费用类型：management-管理费、network-网络费、garbage-垃圾费、other-其他
     */
    private String feeType;

    private BigDecimal amount;

    private String billMonth;

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
