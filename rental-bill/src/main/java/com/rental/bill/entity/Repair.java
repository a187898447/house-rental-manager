package com.rental.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 报修实体
 */
@Data
@TableName("repair")
public class Repair {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long propertyId;

    private String title;

    private String description;

    private String images;

    private String contactPhone;

    /**
     * 0:待处理 1:处理中 2:已完成 3:已取消
     */
    private Integer status;

    private Long handlerId;

    private String handleRemark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
