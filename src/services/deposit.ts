import { post } from './index'

/**
 * 收取押金
 */
export function collectDeposit(tenantId: number, propertyId: number, amount: number) {
  return post<number>('/deposit', null, {
    params: { tenantId, propertyId, amount }
  })
}

/**
 * 退还押金
 */
export function refundDeposit(recordId: number, refundDate: string) {
  return post(`/deposit/${recordId}/refund`, null, {
    params: { refundDate }
  })
}
