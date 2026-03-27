package com.rental.bill.feign;

import com.rental.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 房源服务Feign客户端
 */
@FeignClient(name = "rental-property", path = "/property")
public interface PropertyFeignClient {

    @GetMapping("/statistics/{ownerId}")
    Result<Map<String, Object>> getStatistics(@PathVariable("ownerId") Long ownerId);
}
