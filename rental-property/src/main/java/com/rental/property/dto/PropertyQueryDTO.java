package com.rental.property.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 房源查询DTO
 */
@Data
public class PropertyQueryDTO {
    
    private Long ownerId;
    
    private Long buildingId;
    
    private Integer status;
    
    private String keyword;
    
    private Integer page = 1;
    
    private Integer size = 10;
}
