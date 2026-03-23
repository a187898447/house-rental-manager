package com.rental.bill.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DepositVO {
    private Long id;
    private Long tenantId;
    private String tenantName;
    private Long propertyId;
    private String propertyName;
    private Long contractId;
    private BigDecimal amount;
    private Integer status;
    private String statusName;
    private LocalDate payDate;
    private BigDecimal refundAmount;
    private LocalDate refundDate;
    private String remark;
    private LocalDateTime createdAt;
}
