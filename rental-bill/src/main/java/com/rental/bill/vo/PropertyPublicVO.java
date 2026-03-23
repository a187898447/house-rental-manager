package com.rental.bill.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 公开房源信息（无需登录）
 */
@Data
public class PropertyPublicVO {
    
    private Long id;
    
    private String roomNumber;
    
    private String unit;
    
    private BigDecimal area;
    
    private BigDecimal rentAmount;
    
    private String statusName;
    
    private String buildingName;
    
    private String buildingAddress;
}
