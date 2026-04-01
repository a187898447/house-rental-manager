import { post, get } from './index'
import type { RentRecord, RentRecordDTO, PageResult } from '@/types'

/**
 * 创建租金记录
 */
export function createRentRecord(data: RentRecordDTO) {
  return post<number>('/rent', data)
}

/**
 * 确认收款
 */
export function confirmPayment(recordId: number, paidDate: string) {
  return post(`/rent/${recordId}/payment`, null, {
    params: { paidDate }
  })
}

/**
 * 查询租金记录列表
 */
export function getRentRecordList(
  landlordId: number,
  status?: number,
  pageNum: number = 1,
  pageSize: number = 10
) {
  return get<PageResult<RentRecord>>('/rent/list', {
    landlordId,
    status,
    pageNum,
    pageSize,
  })
}

/**
 * 查询今日待收租（D+0 提醒）
 */
export function getDueTodayList(
  landlordId: number,
  pageNum: number = 1,
  pageSize: number = 10
) {
  return get<PageResult<RentRecord>>('/rent/due-today', {
    landlordId,
    pageNum,
    pageSize,
  })
}

/**
 * 查询催租提醒（D+2/D+3）
 */
export function getOverdueList(
  landlordId: number,
  days: number,
  pageNum: number = 1,
  pageSize: number = 10
) {
  return get<PageResult<RentRecord>>('/rent/overdue', {
    landlordId,
    days,
    pageNum,
    pageSize,
  })
}
