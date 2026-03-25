package com.rental.property.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 房源创建DTO
 */
@Data
public class PropertyCreateDTO {
    
    private Long ownerId;
    
    private Long buildingId;
    
    private String unit;
    
    private String roomNumber;
    
    private BigDecimal area;
    
    private BigDecimal rentAmount;
    
    private BigDecimal depositAmount;
    
    private BigDecimal dailyRate;
    
    private String remark;
}
