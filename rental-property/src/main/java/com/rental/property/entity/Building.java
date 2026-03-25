package com.rental.property.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 楼栋实体
 */
@Data
@TableName("building")
public class Building {

    @TableLogic
@TableId(type = IdType.AUTO)
    private Long id;

    private Long ownerId;

    private String name;

    private String address;

    private BigDecimal waterPrice;

    private BigDecimal electricityPrice;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;


    @TableLogic
    private Integer deleted;
}
