package com.rental.bill.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 报修VO
 */
@Data
public class RepairVO {
    
    private Long id;
    
    private Long tenantId;
    
    private String tenantName;
    
    private String tenantPhone;
    
    private Long propertyId;
    
    private String propertyName;
    
    private String roomNumber;
    
    private String title;
    
    private String description;
    
    private String images;
    
    private String contactPhone;
    
    private Integer status;
    
    private String statusName;
    
    private Long handlerId;
    
    private String handlerName;
    
    private String handleRemark;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
