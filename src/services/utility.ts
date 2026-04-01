import { post } from './index'

/**
 * 配置水电单价
 */
export function setUtilityPrice(propertyId: number, type: number, unitPrice: number) {
  return post('/utility/price', null, {
    params: { propertyId, type, unitPrice }
  })
}

/**
 * 抄表录入
 */
export function submitReading(
  tenantId: number,
  type: number,
  currentReading: number,
  billMonth: string
) {
  return post('/utility/reading', null, {
    params: { tenantId, type, currentReading, billMonth }
  })
}

/**
 * 生成水电账单
 */
export function generateBill(tenantId: number, type: number, billMonth: string) {
  return post('/utility/bill', null, {
    params: { tenantId, type, billMonth }
  })
}
