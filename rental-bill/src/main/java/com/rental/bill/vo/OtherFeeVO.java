package com.rental.bill.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OtherFeeVO {
    private Long id;
    private Long tenantId;
    private String tenantName;
    private Long propertyId;
    private String propertyName;
    private String feeType;
    private String feeTypeName;
    private BigDecimal amount;
    private String billMonth;
    private Integer status;
    private String statusName;
    private LocalDate payDate;
    private String remark;
    private LocalDateTime createdAt;
}
