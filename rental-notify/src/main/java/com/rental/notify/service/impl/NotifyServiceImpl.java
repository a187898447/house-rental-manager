package com.rental.notify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.notify.entity.NotifyMessage;
import com.rental.notify.mapper.NotifyMessageMapper;
import com.rental.notify.service.NotifyService;
import com.rental.notify.vo.NotifyMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通知服务实现
 */
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final NotifyMessageMapper notifyMessageMapper;

    private static final Map<String, String> TYPE_NAMES = Map.of(
            "appointment", "预约通知",
            "bill", "账单通知",
            "contract", "合同通知",
            "repair", "报修通知",
            "system", "系统通知"
    );

    @Override
    @Transactional
    public Long send(Long userId, String type, String title, String content, Long businessId, String businessType) {
        NotifyMessage message = new NotifyMessage();
        message.setUserId(userId);
        message.setType(type);
        message.setTitle(title);
        message.setContent(content);
        message.setBusinessId(businessId);
        message.setBusinessType(businessType);
        message.setReadStatus(0);
        
        notifyMessageMapper.insert(message);
        return message.getId();
    }

    @Override
    public Page<NotifyMessageVO> getList(Long userId, String type, Integer readStatus, Integer page, Integer size) {
        Page<NotifyMessage> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<NotifyMessage> wrapper = new LambdaQueryWrapper<NotifyMessage>()
                .eq(NotifyMessage::getUserId, userId)
                .eq(type != null, NotifyMessage::getType, type)
                .eq(readStatus != null, NotifyMessage::getReadStatus, readStatus)
                .orderByDesc(NotifyMessage::getCreatedAt);
        
        Page<NotifyMessage> result = notifyMessageMapper.selectPage(pageParam, wrapper);
        
        Page<NotifyMessageVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        
        return voPage;
    }

    @Override
    @Transactional
    public boolean markRead(Long id) {
        NotifyMessage message = notifyMessageMapper.selectById(id);
        if (message != null) {
            message.setReadStatus(1);
            return notifyMessageMapper.updateById(message) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean markAllRead(Long userId) {
        List<NotifyMessage> messages = notifyMessageMapper.selectList(
                new LambdaQueryWrapper<NotifyMessage>()
                        .eq(NotifyMessage::getUserId, userId)
                        .eq(NotifyMessage::getReadStatus, 0)
        );
        
        for (NotifyMessage message : messages) {
            message.setReadStatus(1);
            notifyMessageMapper.updateById(message);
        }
        return true;
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notifyMessageMapper.selectCount(
                new LambdaQueryWrapper<NotifyMessage>()
                        .eq(NotifyMessage::getUserId, userId)
                        .eq(NotifyMessage::getReadStatus, 0)
        );
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return notifyMessageMapper.deleteById(id) > 0;
    }

    private NotifyMessageVO convertToVO(NotifyMessage message) {
        NotifyMessageVO vo = new NotifyMessageVO();
        vo.setId(message.getId());
        vo.setUserId(message.getUserId());
        vo.setType(message.getType());
        vo.setTypeName(TYPE_NAMES.getOrDefault(message.getType(), "未知"));
        vo.setTitle(message.getTitle());
        vo.setContent(message.getContent());
        vo.setBusinessId(message.getBusinessId());
        vo.setBusinessType(message.getBusinessType());
        vo.setReadStatus(message.getReadStatus());
        vo.setReadStatusName(message.getReadStatus() == 0 ? "未读" : "已读");
        vo.setCreatedAt(message.getCreatedAt());
        return vo;
    }
}
