import { request } from './index'

/**
 * 获取操作日志列表
 */
export const getOperationLogs = async (params?: {
  type?: string
  page?: number
  size?: number
}) => {
  const res = await request({ url: '/api/operation-log/list', method: 'GET', data: params }) as any
  const data = res?.data
  return data?.records ?? data?.list ?? []
}
