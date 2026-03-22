package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.vo.UtilityBillVO;
import java.math.BigDecimal;

public interface UtilityBillService {
    Long create(Long tenantId, Long propertyId, String billMonth, BigDecimal waterAmount, BigDecimal electricityAmount);
    UtilityBillVO getDetail(Long id);
    Page<UtilityBillVO> getTenantBills(Long tenantId, Integer status, Integer page, Integer size);
    Page<UtilityBillVO> getOwnerBills(Long ownerId, Integer status, Integer page, Integer size);
    boolean pay(Long id);
}
