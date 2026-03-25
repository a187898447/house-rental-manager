import com.baomidou.mybatisplus.annotation.TableLogic;
package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同实体
 */
@Data
@TableName("contract")
public class Contract {

    @TableLogic
@TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long propertyId;

    private Long ownerId;

    private BigDecimal rentAmount;

    private BigDecimal depositAmount;

    private BigDecimal waterFee;

    private BigDecimal electricityFee;

    private String otherFees;

    private LocalDate startDate;

    private LocalDate endDate;

    private String signUrl;

    /**
     * 0:待签署 1:已签署 2:已生效 3:已到期 4:已解除
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;


    @TableLogic
    private Integer deleted;
}
