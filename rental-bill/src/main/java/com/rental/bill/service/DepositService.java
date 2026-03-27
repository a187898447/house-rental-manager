package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.vo.DepositVO;

/**
 * 押金服务接口
 */
public interface DepositService {
    Long create(Long tenantId, Long propertyId, Long contractId, java.math.BigDecimal amount);
    Long createDeposit(Long propertyId, Long tenantId, java.math.BigDecimal amount);
    DepositVO getDetail(Long id);
    Page<DepositVO> getTenantDeposits(Long tenantId, Integer status, Integer page, Integer size);
    Page<DepositVO> getOwnerDeposits(Long ownerId, Integer status, Integer page, Integer size);
    boolean pay(Long id, String payMethod);
    boolean refund(Long id, java.math.BigDecimal refundAmount);
}
