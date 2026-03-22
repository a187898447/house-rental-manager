package com.rental.notify.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知消息VO
 */
@Data
public class NotifyMessageVO {
    
    private Long id;
    
    private Long userId;
    
    private String type;
    
    private String typeName;
    
    private String title;
    
    private String content;
    
    private Long businessId;
    
    private String businessType;
    
    private Integer readStatus;
    
    private String readStatusName;
    
    private LocalDateTime createdAt;
}
