import { request } from './index'

/**
 * 获取操作日志列表
 */
export const getOperationLogs = async (params?: {
  type?: string
  startDate?: string
  endDate?: string
  page?: number
  size?: number
}) => {
  return request<any[]>({
    url: '/api/operation-log/list',
    method: 'GET',
    data: params
  })
}

/**
 * 获取操作日志详情
 */
export const getOperationLogDetail = async (id: string) => {
  return request<any>({
    url: `/api/operation-log/${id}`,
    method: 'GET'
  })
}