import { request } from './index'

/**
 * 获取操作日志列表
 */
export const getOperationLogs = async (params?: { 
  type?: string
  page?: number
  size?: number
}) => {
  const res = await request<any>({
    url: '/api/operation-log/list',
    method: 'GET',
    data: params
  })
  return res?.records || res?.list || []
}
