package com.rental.bill.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UtilityBillVO {
    private Long id;
    private Long tenantId;
    private String tenantName;
    private Long propertyId;
    private String propertyName;
    private String billMonth;
    private BigDecimal waterReading;
    private BigDecimal waterReadingCurrent;
    private BigDecimal waterAmount;
    private BigDecimal electricityReading;
    private BigDecimal electricityReadingCurrent;
    private Integer source;
    private Integer status;
    private String statusName;
    private LocalDate payDate;
    private String remark;
    private LocalDateTime createdAt;
}
