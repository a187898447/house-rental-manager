package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约看房实体
 */
@Data
@TableName("appointment")
public class Appointment {

    @TableLogic
@TableId(type = IdType.AUTO)
    private Long id;

    private Long propertyId;

    private String tenantName;

    private String tenantPhone;

    private LocalDateTime appointmentDate;

    /**
     * 0:待确认 1:已确认 2:已取消 3:已完成
     */
    private Integer status;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;


    @TableLogic
    private Integer deleted;
}
