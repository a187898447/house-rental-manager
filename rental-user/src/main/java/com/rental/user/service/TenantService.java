package com.rental.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.user.dto.TenantDTO;
import com.rental.user.entity.Tenant;
import com.rental.user.vo.TenantVO;

/**
 * 租客服务接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 入住登记
     *
     * @param dto 租客信息
     * @return 租客 ID
     */
    Long checkIn(TenantDTO dto);

    /**
     * 退租办理
     *
     * @param tenantId 租客 ID
     * @param checkOutDate 退租日期
     */
    void checkOut(Long tenantId, java.time.LocalDate checkOutDate);

    /**
     * 查询租客列表
     *
     * @param landlordId 房东 ID
     * @param status     状态（可选）
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 租客列表
     */
    Page<TenantVO> getTenantList(Long landlordId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 查询租客详情
     *
     * @param tenantId 租客 ID
     * @return 租客信息
     */
    TenantVO getTenantDetail(Long tenantId);
}
