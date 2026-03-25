import com.baomidou.mybatisplus.annotation.TableLogic;
package com.rental.notify.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知消息实体
 */
@Data
@TableName("notify_message")
public class NotifyMessage {

    @TableLogic
@TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 消息类型: appointment-预约, bill-账单, contract-合同, repair-报修, system-系统
     */
    private String type;

    private String title;

    private String content;

    /**
     * 关联业务ID
     */
    private Long businessId;

    /**
     * 业务类型: appointment/bill/contract/repair
     */
    private String businessType;

    /**
     * 0:未读 1:已读
     */
    private Integer readStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;


    @TableLogic
    private Integer deleted;
}
