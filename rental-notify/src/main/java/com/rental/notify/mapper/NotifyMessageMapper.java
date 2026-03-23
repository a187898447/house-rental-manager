package com.rental.notify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rental.notify.entity.NotifyMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知消息Mapper
 */
@Mapper
public interface NotifyMessageMapper extends BaseMapper<NotifyMessage> {
}
