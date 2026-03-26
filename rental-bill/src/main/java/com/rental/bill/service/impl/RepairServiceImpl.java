package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.RepairCreateDTO;
import com.rental.bill.entity.Repair;
import com.rental.bill.mapper.RepairMapper;
import com.rental.bill.service.RepairService;
import com.rental.bill.vo.RepairVO;
import com.rental.common.exception.BusinessException;
import com.rental.common.notify.NotifyClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 报修服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepairServiceImpl implements RepairService {

    private final RepairMapper repairMapper;

    @Autowired(required = false)
    private NotifyClient notifyClient;

    private static final List<Integer> CANCELABLE_STATUSES = Arrays.asList(0);
    private static final List<Integer> PROCESSABLE_STATUSES = Arrays.asList(0);
    private static final List<Integer> COMPLETABLE_STATUSES = Arrays.asList(1);

    @Override
    @Transactional
    public Long create(RepairCreateDTO dto) {
        Repair repair = new Repair();
        repair.setTenantId(dto.getTenantId());
        repair.setPropertyId(dto.getPropertyId());
        repair.setTitle(dto.getTitle());
        repair.setDescription(dto.getDescription());
        repair.setImages(dto.getImages());
        repair.setContactPhone(dto.getContactPhone());
        repair.setStatus(0); // 待处理
        
        repairMapper.insert(repair);
        
        // TODO: 获取房东ID后发送通知
        // if (notifyClient != null) {
        //     notifyClient.notifyRepairCreated(ownerId, propertyName, title);
        // }
        
        log.info("报修创建成功: id={}, propertyId={}, title={}", repair.getId(), dto.getPropertyId(), dto.getTitle());
        return repair.getId();
    }

    @Override
    public RepairVO getDetail(Long id) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            log.warn("报修不存在: id={}", id);
            throw new BusinessException("报修不存在");
        }
        return convertToVO(repair);
    }

    @Override
    public Page<RepairVO> getTenantRepairs(Long tenantId, Integer status, Integer page, Integer size) {
        Page<Repair> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<Repair>()
                .eq(Repair::getTenantId, tenantId)
                .eq(status != null, Repair::getStatus, status)
                .orderByDesc(Repair::getCreatedAt);
        
        Page<Repair> result = repairMapper.selectPage(pageParam, wrapper);
        
        Page<RepairVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    public Page<RepairVO> getOwnerRepairs(Long ownerId, Long propertyId, Integer status, Integer page, Integer size) {
        // 通过 property 表关联过滤 ownerId
        Page<Repair> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<Repair>()
                .inSql(Repair::getPropertyId, 
                    "SELECT id FROM property WHERE owner_id = " + ownerId + " AND deleted IS NULL")
                .eq(propertyId != null, Repair::getPropertyId, propertyId)
                .eq(status != null, Repair::getStatus, status)
                .orderByDesc(Repair::getCreatedAt);
        
        Page<Repair> result = repairMapper.selectPage(pageParam, wrapper);
        
        Page<RepairVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    @Transactional
    public boolean startProcess(Long id, Long handlerId) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            log.warn("报修不存在: id={}", id);
            throw new BusinessException("报修不存在");
        }
        if (!PROCESSABLE_STATUSES.contains(repair.getStatus())) {
            log.warn("报修状态不正确: id={}, status={}", id, repair.getStatus());
            throw new BusinessException("报修状态不正确");
        }
        
        repair.setStatus(1); // 处理中
        repair.setHandlerId(handlerId);
        
        // TODO: 通知租客开始处理
        // if (notifyClient != null) {
        //     notifyClient.sendNotify(tenantId, "repair", "报修已开始处理", ...);
        // }
        
        boolean result = repairMapper.updateById(repair) > 0;
        log.info("报修开始处理: id={}, handlerId={}", id, handlerId);
        return result;
    }

    @Override
    @Transactional
    public boolean complete(Long id, String remark) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            log.warn("报修不存在: id={}", id);
            throw new BusinessException("报修不存在");
        }
        if (!COMPLETABLE_STATUSES.contains(repair.getStatus())) {
            log.warn("报修状态不正确，无法完成: id={}, status={}", id, repair.getStatus());
            throw new BusinessException("只有处理中的报修可以完成");
        }
        
        repair.setStatus(2); // 已完成
        repair.setHandleRemark(remark);
        
        // TODO: 通知租客已完成
        // if (notifyClient != null) {
        //     notifyClient.sendNotify(tenantId, "repair", "报修已完成", ...);
        // }
        
        boolean result = repairMapper.updateById(repair) > 0;
        log.info("报修完成: id={}", id);
        return result;
    }

    @Override
    @Transactional
    public boolean cancel(Long id) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            log.warn("报修不存在: id={}", id);
            throw new BusinessException("报修不存在");
        }
        if (!CANCELABLE_STATUSES.contains(repair.getStatus())) {
            log.warn("报修状态不正确，无法取消: id={}, status={}", id, repair.getStatus());
            throw new BusinessException("只有待处理的报修可以取消");
        }
        
        repair.setStatus(3); // 已取消
        
        boolean result = repairMapper.updateById(repair) > 0;
        log.info("报修取消: id={}", id);
        return result;
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status, String remark) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            log.warn("报修不存在: id={}", id);
            throw new BusinessException("报修不存在");
        }
        repair.setStatus(status);
        if (remark != null) {
            repair.setHandleRemark(remark);
        }
        boolean result = repairMapper.updateById(repair) > 0;
        log.info("报修状态更新: id={}, status={}", id, status);
        return result;
    }

    private RepairVO convertToVO(Repair repair) {
        RepairVO vo = new RepairVO();
        vo.setId(repair.getId());
        vo.setTenantId(repair.getTenantId());
        vo.setPropertyId(repair.getPropertyId());
        vo.setTitle(repair.getTitle());
        vo.setDescription(repair.getDescription());
        vo.setImages(repair.getImages());
        vo.setContactPhone(repair.getContactPhone());
        vo.setStatus(repair.getStatus());
        vo.setStatusName(getStatusName(repair.getStatus()));
        vo.setHandlerId(repair.getHandlerId());
        vo.setHandleRemark(repair.getHandleRemark());
        vo.setCreatedAt(repair.getCreatedAt());
        vo.setUpdatedAt(repair.getUpdatedAt());
        return vo;
    }

    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待处理";
            case 1 -> "处理中";
            case 2 -> "已完成";
            case 3 -> "已取消";
            default -> "未知";
        };
    }
}
