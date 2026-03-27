import { request } from './index'

/**
 * 获取房东数据统计
 */
export const getStatistics = async () => {
  const res = await request<any>({
    url: '/api/statistics/dashboard',
    method: 'GET'
  })
  return res?.data ?? res
}
