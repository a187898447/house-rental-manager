import { post, get } from './index'

/**
 * 提交报修
 */
export function submitRepair(data: {
  tenantId: number
  propertyId: number
  description: string
  images?: string[]
}) {
  return post<number>('/repair', data)
}

/**
 * 处理报修
 */
export function processRepair(repairId: number, status: number, remark?: string) {
  return post('/repair/process', null, {
    params: { repairId, status, remark }
  })
}

/**
 * 查询报修列表
 */
export function getRepairList(
  propertyId?: number,
  status?: number,
  pageNum: number = 1,
  pageSize: number = 10
) {
  return get('/repair/list', {
    propertyId,
    status,
    pageNum,
    pageSize,
  })
}
