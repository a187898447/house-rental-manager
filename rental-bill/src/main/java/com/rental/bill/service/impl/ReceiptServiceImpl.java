package com.rental.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rental.bill.entity.Receipt;
import com.rental.bill.mapper.ReceiptMapper;
import com.rental.bill.service.ReceiptService;
import com.rental.bill.vo.ReceiptVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl extends ServiceImpl<ReceiptMapper, Receipt> implements ReceiptService {

    @Override
    public Page<ReceiptVO> getOwnerReceipts(Long ownerId, String type, String month, Integer page, Integer size) {
        Page<Receipt> p = new Page<>(page, size);
        LambdaQueryWrapper<Receipt> w = new LambdaQueryWrapper<Receipt>()
                .eq(Receipt::getOwnerId, ownerId)
                .eq(type != null && !type.isEmpty(), Receipt::getType, type)
                .eq(month != null && !month.isEmpty(), Receipt::getMonth, month)
                .orderByDesc(Receipt::getCreatedAt);
        
        Page<Receipt> result = this.page(p, w);
        Page<ReceiptVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    public ReceiptVO getDetail(Long id) {
        Receipt receipt = this.getById(id);
        if (receipt == null) {
            log.warn("收据不存在: id={}", id);
            return null;
        }
        return convertToVO(receipt);
    }

    private ReceiptVO convertToVO(Receipt receipt) {
        ReceiptVO vo = new ReceiptVO();
        BeanUtils.copyProperties(receipt, vo);
        // TODO: 关联查询 propertyName, tenantName
        return vo;
    }
}