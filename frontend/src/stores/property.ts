import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Property, PropertyInput } from '@/types'
import { getProperties, createProperty, updateProperty, deleteProperty } from '@/services/property'

export const usePropertyStore = defineStore('property', () => {
  const properties = ref<Property[]>([])
  const currentProperty = ref<Property | null>(null)
  const loading = ref(false)

  // 按状态筛选
  const vacantProperties = computed(() => 
    properties.value.filter(p => p.status === 'vacant')
  )
  
  const rentedUnpaidProperties = computed(() => 
    properties.value.filter(p => p.status === 'rented_unpaid')
  )
  
  const rentedPaidProperties = computed(() => 
    properties.value.filter(p => p.status === 'rented_paid')
  )

  // 获取房源列表
  const fetchProperties = async () => {
    loading.value = true
    try {
      const res = await getProperties()
      properties.value = res
    } finally {
      loading.value = false
    }
  }

  // 新增房源
  const addProperty = async (data: PropertyInput) => {
    const newProperty = await createProperty(data)
    properties.value.push(newProperty)
    return newProperty
  }

  // 编辑房源
  const editProperty = async (id: string, data: PropertyInput) => {
    const updated = await updateProperty(id, data)
    const index = properties.value.findIndex(p => p.id === id)
    if (index !== -1) {
      properties.value[index] = updated
    }
    return updated
  }

  // 删除房源
  const removeProperty = async (id: string) => {
    await deleteProperty(id)
    properties.value = properties.value.filter(p => p.id !== id)
  }

  // 设置当前编辑的房源
  const setCurrentProperty = (property: Property | null) => {
    currentProperty.value = property
  }

  return {
    properties,
    currentProperty,
    loading,
    vacantProperties,
    rentedUnpaidProperties,
    rentedPaidProperties,
    fetchProperties,
    addProperty,
    editProperty,
    removeProperty,
    setCurrentProperty
  }
})
