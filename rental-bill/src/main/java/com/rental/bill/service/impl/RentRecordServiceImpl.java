package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.bill.dto.RentRecordDTO;
import com.rental.bill.entity.RentRecord;
import com.rental.bill.mapper.RentRecordMapper;
import com.rental.bill.service.RentRecordService;
import com.rental.bill.vo.RentRecordVO;
import com.rental.property.entity.Property;
import com.rental.property.mapper.PropertyMapper;
import com.rental.user.entity.Tenant;
import com.rental.user.mapper.TenantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 租金记录服务实现类
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentRecordServiceImpl extends ServiceImpl<RentRecordMapper, RentRecord> implements RentRecordService {

    private final TenantMapper tenantMapper;
    private final PropertyMapper propertyMapper;

    /**
     * 未支付状态
     */
    private static final Integer STATUS_UNPAID = 0;

    /**
     * 已支付状态
     */
    private static final Integer STATUS_PAID = 1;

    /**
     * 逾期状态
     */
    private static final Integer STATUS_OVERDUE = 2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRentRecord(RentRecordDTO dto) {
        log.info("创建租金记录：tenantId={}, amount={}", dto.getTenantId(), dto.getAmount());

        // 1. 检查租客是否存在
        Tenant tenant = tenantMapper.selectById(dto.getTenantId());
        if (tenant == null) {
            throw new RuntimeException("租客不存在");
        }

        // 2. 检查房源是否存在
        Property property = propertyMapper.selectById(dto.getPropertyId());
        if (property == null || property.getDeleted() == 1) {
            throw new RuntimeException("房源不存在");
        }

        // 3. 创建租金记录
        RentRecord record = new RentRecord();
        BeanUtils.copyProperties(dto, record);
        record.setLandlordId(property.getCreateBy());
        record.setStatus(STATUS_UNPAID);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        baseMapper.insert(record);
        log.info("租金记录创建成功：recordId={}", record.getId());
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPayment(Long recordId, LocalDate paidDate) {
        log.info("确认收款：recordId={}, paidDate={}", recordId, paidDate);

        RentRecord record = baseMapper.selectById(recordId);
        if (record == null) {
            throw new RuntimeException("租金记录不存在");
        }

        record.setStatus(STATUS_PAID);
        record.setPaidDate(paidDate);
        record.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(record);

        log.info("收款确认成功：recordId={}", recordId);
    }

    @Override
    public Page<RentRecordVO> getRentRecordList(Long landlordId, Integer status, Integer pageNum, Integer pageSize) {
        log.info("查询租金记录列表：landlordId={}, status={}", landlordId, status);

        Page<RentRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RentRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentRecord::getLandlordId, landlordId);
        if (status != null) {
            queryWrapper.eq(RentRecord::getStatus, status);
        }
        queryWrapper.orderByDesc(RentRecord::getCreateTime);

        Page<RentRecord> recordPage = baseMapper.selectPage(page, queryWrapper);

        // 转换为 VO
        List<RentRecordVO> voList = recordPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<RentRecordVO> voPage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public Page<RentRecordVO> getDueTodayList(Long landlordId, LocalDate today, Integer pageNum, Integer pageSize) {
        log.info("查询今日待收租：landlordId={}, today={}", landlordId, today);

        Page<RentRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RentRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentRecord::getLandlordId, landlordId)
                .eq(RentRecord::getStatus, STATUS_UNPAID)
                .eq(RentRecord::getDueDate, today);
        queryWrapper.orderByAsc(RentRecord::getDueDate);

        Page<RentRecord> recordPage = baseMapper.selectPage(page, queryWrapper);

        Page<RentRecordVO> voPage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        voPage.setRecords(recordPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public Page<RentRecordVO> getOverdueList(Long landlordId, Integer days, Integer pageNum, Integer pageSize) {
        log.info("查询催租提醒：landlordId={}, days={}", landlordId, days);

        Page<RentRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RentRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RentRecord::getLandlordId, landlordId)
                .eq(RentRecord::getStatus, STATUS_UNPAID)
                .lt(RentRecord::getDueDate, LocalDate.now());
        queryWrapper.orderByAsc(RentRecord::getDueDate);

        Page<RentRecord> recordPage = baseMapper.selectPage(page, queryWrapper);

        // 过滤出指定逾期天数的记录
        List<RentRecordVO> voList = recordPage.getRecords().stream()
                .map(record -> {
                    long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
                    if (overdueDays == days) {
                        return convertToVO(record);
                    }
                    return null;
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());

        Page<RentRecordVO> voPage = new Page<>(recordPage.getCurrent(), voList.size(), voList.size());
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 转换为 VO
     */
    private RentRecordVO convertToVO(RentRecord record) {
        RentRecordVO vo = new RentRecordVO();
        BeanUtils.copyProperties(record, vo);

        // 填充租客姓名
        if (record.getTenantId() != null) {
            Tenant tenant = tenantMapper.selectById(record.getTenantId());
            if (tenant != null) {
                vo.setTenantName(tenant.getName());
            }
        }

        // 填充房源名称
        if (record.getPropertyId() != null) {
            Property property = propertyMapper.selectById(record.getPropertyId());
            if (property != null) {
                vo.setPropertyName(property.getName());
            }
        }

        // 计算逾期天数
        if (record.getStatus() == STATUS_UNPAID && record.getDueDate() != null) {
            long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
            if (overdueDays > 0) {
                vo.setOverdueDays(overdueDays);
                vo.setStatus(STATUS_OVERDUE);
            }
        }

        return vo;
    }
}
