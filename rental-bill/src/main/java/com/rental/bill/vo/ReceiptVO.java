package com.rental.bill.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收据视图对象
 */
@Data
public class ReceiptVO {
    
    private Long id;
    
    private String type;
    
    private Long businessId;
    
    private Long propertyId;
    
    private String propertyName;
    
    private Long tenantId;
    
    private String tenantName;
    
    private BigDecimal amount;
    
    private String payMethod;
    
    private String month;
    
    private String remark;
    
    private String fileUrl;
    
    private LocalDateTime createdAt;
}