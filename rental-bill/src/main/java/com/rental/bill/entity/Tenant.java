package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租客实体
 */
@Data
@TableName("tenant")
public class Tenant {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long propertyId;

    private String name;

    private String phone;

    private String idCard;

    private LocalDate leaseStartDate;

    private LocalDate leaseEndDate;

    private String emergencyContact;

    private String emergencyPhone;

    /**
     * 0:待入住 1:已入住 2:已退租
     */
    private Integer status;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
