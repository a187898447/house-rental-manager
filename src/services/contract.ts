import { post, get } from './index'

/**
 * 生成合同
 */
export function generateContract(tenantId: number, propertyId: number, terms: any) {
  return post<number>('/contract/generate', {
    tenantId,
    propertyId,
    ...terms
  })
}

/**
 * 签署合同
 */
export function signContract(contractId: number, signatureImage: string) {
  return post('/contract/sign', {
    contractId,
    signatureImage
  })
}

/**
 * 查询合同详情
 */
export function getContractDetail(contractId: number) {
  return get(`/contract/${contractId}`)
}
