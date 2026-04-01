import { post, get } from './index'
import type { Tenant, TenantDTO, PageResult } from '@/types'

/**
 * 入住登记
 */
export function checkIn(data: TenantDTO) {
  return post<number>('/tenant/check-in', data)
}

/**
 * 退租办理
 */
export function checkOut(tenantId: number, checkOutDate: string) {
  return post('/tenant/check-out', null, {
    params: { tenantId, checkOutDate }
  })
}

/**
 * 查询租客列表
 */
export function getTenantList(
  landlordId: number,
  status?: number,
  pageNum: number = 1,
  pageSize: number = 10
) {
  return get<PageResult<Tenant>>('/tenant/list', {
    landlordId,
    status,
    pageNum,
    pageSize,
  })
}

/**
 * 查询租客详情
 */
export function getTenantDetail(tenantId: number) {
  return get<Tenant>(`/tenant/${tenantId}`)
}
