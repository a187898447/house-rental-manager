import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Property } from '@/types'
import { getPropertyList } from '@/services/property'

export const usePropertyStore = defineStore('property', () => {
  const propertyList = ref<Property[]>([])
  const total = ref(0)

  /**
   * 加载房源列表
   */
  async function loadPropertyList(landlordId: number, status?: number) {
    try {
      const result = await getPropertyList(landlordId, status)
      propertyList.value = result.records
      total.value = result.total
    } catch (error) {
      console.error('加载房源列表失败:', error)
    }
  }

  /**
   * 添加房源
   */
  function addProperty(property: Property) {
    propertyList.value.unshift(property)
    total.value++
  }

  /**
   * 更新房源
   */
  function updateProperty(updated: Property) {
    const index = propertyList.value.findIndex(p => p.id === updated.id)
    if (index !== -1) {
      propertyList.value[index] = updated
    }
  }

  /**
   * 删除房源
   */
  function removeProperty(propertyId: number) {
    const index = propertyList.value.findIndex(p => p.id === propertyId)
    if (index !== -1) {
      propertyList.value.splice(index, 1)
      total.value--
    }
  }

  return {
    propertyList,
    total,
    loadPropertyList,
    addProperty,
    updateProperty,
    removeProperty,
  }
})
