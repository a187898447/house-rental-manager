package com.rental.user.client;

import com.rental.common.result.Result;
import com.rental.common.vo.PropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 房源服务 Feign 降级处理
 *
 * @author rental-team
 * @date 2026-04-01
 */
@Slf4j
@Component
public class PropertyClientFallback implements PropertyClient {

    @Override
    public Result<PropertyVO> getPropertyDetail(Long id) {
        log.error("房源服务调用失败：propertyId={}", id);
        return Result.error(503, "房源服务暂时不可用");
    }
}
