package com.rental.bill.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rental.bill.dto.OtherFeeCreateDTO;
import com.rental.bill.service.OtherFeeService;
import com.rental.bill.vo.OtherFeeVO;
import com.rental.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fee")
@RequiredArgsConstructor
@Tag(name = "其他费用", description = "其他费用管理相关接口")
public class OtherFeeController {

    private final OtherFeeService otherFeeService;

    @GetMapping("/{id}")
    @Operation(summary = "费用详情")
    public Result<OtherFeeVO> getDetail(@PathVariable Long id) {
        return Result.success(otherFeeService.getDetail(id));
    }

    @GetMapping("/tenant")
    @Operation(summary = "租客费用列表")
    public Result<Page<OtherFeeVO>> getTenantFees(
            @RequestParam Long tenantId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(otherFeeService.getTenantFees(tenantId, status, page, size));
    }

    @GetMapping("/owner")
    @Operation(summary = "房东费用列表")
    public Result<Page<OtherFeeVO>> getOwnerFees(
            HttpServletRequest request,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long ownerId = Long.valueOf(request.getHeader("X-User-Id"));
        return Result.success(otherFeeService.getOwnerFees(ownerId, status, page, size));
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "支付费用")
    public Result<Boolean> pay(@PathVariable Long id) {
        return Result.success(otherFeeService.pay(id));
    }

    @PostMapping
    @Operation(summary = "创建费用")
    public Result<Long> create(@RequestBody OtherFeeCreateDTO dto) {
        Long id = otherFeeService.create(dto);
        return Result.success("费用创建成功", id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除费用")
    @Parameter(name = "id", description = "费用ID")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(otherFeeService.delete(id));
    }
}
