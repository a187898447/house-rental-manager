package com.rental.property.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房源视图对象
 */
@Data
public class PropertyVO {
    
    private Long id;
    
    private Long ownerId;
    
    private Long buildingId;
    
    private String buildingName;
    
    private String unit;
    
    private String roomNumber;
    
    private BigDecimal area;
    
    private BigDecimal rentAmount;
    
    private BigDecimal depositAmount;
    
    private Integer status;
    
    private String statusName;
    
    private BigDecimal dailyRate;
    
    private String remark;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
