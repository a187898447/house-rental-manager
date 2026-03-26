package com.rental.bill.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租金账单VO
 */
@Data
public class RentRecordVO {
    
    private Long id;
    
    private Long tenantId;
    
    private String tenantName;
    
    private String tenantPhone;
    
    private Long propertyId;
    
    private String propertyName;
    
    private String roomNumber;
    
    private BigDecimal amount;
    
    private Integer days;
    
    private BigDecimal dailyRate;
    
    private String payMonth;
    
    private LocalDate payDate;
    
    private Integer status;
    
    private String statusName;
    
    private Integer remindCount;
    
    private LocalDate lastRemindDate;
    
    private Integer remindStatus;
    
    private String remindStatusName;
    
    private String remark;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
