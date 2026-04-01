package com.rental.user.client;

import com.rental.common.result.Result;
import com.rental.common.vo.PropertyVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 房源服务 Feign 客户端
 *
 * @author rental-team
 * @date 2026-04-01
 */
@FeignClient(name = "rental-property", fallback = PropertyClientFallback.class)
public interface PropertyClient {

    @GetMapping("/api/property/{id}")
    Result<PropertyVO> getPropertyDetail(@PathVariable("id") Long id);
}
