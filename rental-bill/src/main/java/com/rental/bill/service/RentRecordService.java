package com.rental.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rental.bill.dto.RentRecordDTO;
import com.rental.bill.entity.RentRecord;
import com.rental.bill.vo.RentRecordVO;

import java.time.LocalDate;

/**
 * 租金记录服务接口
 *
 * @author rental-team
 * @date 2026-04-01
 */
public interface RentRecordService extends IService<RentRecord> {

    /**
     * 创建租金记录
     *
     * @param dto 租金记录 DTO
     * @return 记录 ID
     */
    Long createRentRecord(RentRecordDTO dto);

    /**
     * 确认收款
     *
     * @param recordId 记录 ID
     * @param paidDate 实收日期
     */
    void confirmPayment(Long recordId, LocalDate paidDate);

    /**
     * 查询租金记录列表
     *
     * @param landlordId 房东 ID
     * @param status     状态（可选）
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 租金记录列表
     */
    Page<RentRecordVO> getRentRecordList(Long landlordId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 获取待收租提醒（D+0）
     *
     * @param landlordId 房东 ID
     * @param today      今天日期
     * @return 待收租列表
     */
    Page<RentRecordVO> getDueTodayList(Long landlordId, LocalDate today, Integer pageNum, Integer pageSize);

    /**
     * 获取催租提醒（D+2/D+3）
     *
     * @param landlordId 房东 ID
     * @param days       逾期天数（2 或 3）
     * @return 催租列表
     */
    Page<RentRecordVO> getOverdueList(Long landlordId, Integer days, Integer pageNum, Integer pageSize);
}
