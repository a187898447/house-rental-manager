package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RentRecordCreateDTO;
import com.rental.bill.entity.RentRecord;
import com.rental.bill.mapper.RentRecordMapper;
import com.rental.bill.service.RentRecordService;
import com.rental.bill.vo.RentRecordVO;
import com.rental.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 租金账单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentRecordServiceImpl implements RentRecordService {

    private final RentRecordMapper rentRecordMapper;

    private static final List<Integer> PAYABLE_STATUSES = Arrays.asList(0, 2);

    @Override
    @Transactional
    public Long create(RentRecordCreateDTO dto) {
        RentRecord record = new RentRecord();
        record.setTenantId(dto.getTenantId());
        record.setPropertyId(dto.getPropertyId());
        record.setAmount(dto.getAmount());
        record.setDays(dto.getDays());
        record.setDailyRate(dto.getDailyRate());
        record.setPayMonth(dto.getPayMonth());
        record.setRemark(dto.getRemark());
        record.setStatus(0); // 待支付
        record.setRemindCount(0);
        
        rentRecordMapper.insert(record);
        log.info("租金账单创建成功: id={}, tenantId={}, amount={}", record.getId(), dto.getTenantId(), dto.getAmount());
        return record.getId();
    }

    @Override
    public RentRecordVO getDetail(Long id) {
        RentRecord record = rentRecordMapper.selectById(id);
        if (record == null) {
            log.warn("账单不存在: id={}", id);
            throw new BusinessException("账单不存在");
        }
        return convertToVO(record);
    }

    @Override
    public Page<RentRecordVO> getOwnerBills(Long ownerId, Long propertyId, Integer status, String month, Integer page, Integer size) {
        Page<RentRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<RentRecord>()
                .eq(propertyId != null, RentRecord::getPropertyId, propertyId)
                .eq(status != null, RentRecord::getStatus, status)
                .eq(month != null, RentRecord::getPayMonth, month)
                .orderByDesc(RentRecord::getCreatedAt);
        
        Page<RentRecord> result = rentRecordMapper.selectPage(pageParam, wrapper);
        
        Page<RentRecordVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    public Page<RentRecordVO> getTenantBills(Long tenantId, Integer status, Integer page, Integer size) {
        Page<RentRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<RentRecord> wrapper = new LambdaQueryWrapper<RentRecord>()
                .eq(RentRecord::getTenantId, tenantId)
                .eq(status != null, RentRecord::getStatus, status)
                .orderByDesc(RentRecord::getCreatedAt);
        
        Page<RentRecord> result = rentRecordMapper.selectPage(pageParam, wrapper);
        
        Page<RentRecordVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    @Transactional
    public boolean markPaid(Long id) {
        RentRecord record = rentRecordMapper.selectById(id);
        if (record == null) {
            log.warn("账单不存在: id={}", id);
            throw new BusinessException("账单不存在");
        }
        if (!PAYABLE_STATUSES.contains(record.getStatus())) {
            log.warn("账单状态不正确: id={}, status={}", id, record.getStatus());
            throw new BusinessException("账单状态不正确");
        }
        
        record.setStatus(1); // 已支付
        record.setPayDate(LocalDate.now());
        
        boolean result = rentRecordMapper.updateById(record) > 0;
        log.info("租金账单支付成功: id={}", id);
        return result;
    }

    @Override
    @Transactional
    public boolean sendReminder(Long id) {
        RentRecord record = rentRecordMapper.selectById(id);
        if (record == null) {
            log.warn("账单不存在: id={}", id);
            throw new BusinessException("账单不存在");
        }
        
        record.setRemindCount(record.getRemindCount() + 1);
        
        // TODO: 调用通知服务发送提醒
        log.info("租金账单催缴成功: id={}, remindCount={}", id, record.getRemindCount());
        
        return rentRecordMapper.updateById(record) > 0;
    }

    @Override
    @Transactional
    public boolean cancel(Long id) {
        RentRecord record = rentRecordMapper.selectById(id);
        if (record == null) {
            log.warn("账单不存在: id={}", id);
            throw new BusinessException("账单不存在");
        }
        if (record.getStatus() != 0) {
            log.warn("账单状态不正确，无法取消: id={}, status={}", id, record.getStatus());
            throw new BusinessException("只有待支付的账单可以取消");
        }
        
        record.setStatus(3); // 已取消
        
        boolean result = rentRecordMapper.updateById(record) > 0;
        log.info("租金账单取消成功: id={}", id);
        return result;
    }

    private RentRecordVO convertToVO(RentRecord record) {
        RentRecordVO vo = new RentRecordVO();
        vo.setId(record.getId());
        vo.setTenantId(record.getTenantId());
        vo.setPropertyId(record.getPropertyId());
        vo.setAmount(record.getAmount());
        vo.setDays(record.getDays());
        vo.setDailyRate(record.getDailyRate());
        vo.setPayMonth(record.getPayMonth());
        vo.setPayDate(record.getPayDate());
        vo.setStatus(record.getStatus());
        vo.setStatusName(getStatusName(record.getStatus()));
        vo.setRemindCount(record.getRemindCount());
        vo.setRemark(record.getRemark());
        vo.setCreatedAt(record.getCreatedAt());
        vo.setUpdatedAt(record.getUpdatedAt());
        return vo;
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已逾期";
            case 3 -> "已取消";
            default -> "未知";
        };
    }
}
