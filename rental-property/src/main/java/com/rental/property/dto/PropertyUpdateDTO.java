package com.rental.property.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 房源更新DTO
 */
@Data
public class PropertyUpdateDTO {
    
    private Long buildingId;
    
    private String unit;
    
    private String roomNumber;
    
    private BigDecimal area;
    
    private BigDecimal rentAmount;
    
    private BigDecimal depositAmount;
    
    private Integer status;
    
    private BigDecimal dailyRate;
    
    private String remark;
}
