package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.bill.entity.DepositRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 押金记录服务接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
public interface DepositRecordService extends IService<DepositRecord> {

    /**
     * 收取押金
     *
     * @param tenantId   租客 ID
     * @param propertyId 房源 ID
     * @param amount     押金金额
     * @return 记录 ID
     */
    Long collectDeposit(Long tenantId, Long propertyId, BigDecimal amount);

    /**
     * 退还押金
     *
     * @param recordId   记录 ID
     * @param refundDate 退还日期
     */
    void refundDeposit(Long recordId, LocalDate refundDate);

    /**
     * 查询押金记录
     *
     * @param tenantId 租客 ID
     * @return 押金记录
     */
    DepositRecord getByTenantId(Long tenantId);
}
