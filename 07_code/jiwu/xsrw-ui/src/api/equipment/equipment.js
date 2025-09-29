import request from '@/utils/request'

// 查询设备台账列表
export function listEquipment(query) {
  return request({
    url: '/wms/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询设备台账详细
export function getEquipment(id) {
  return request({
    url: '/wms/equipment/' + id,
    method: 'get'
  })
}

// 新增设备台账
export function addEquipment(data) {
  return request({
    url: '/wms/equipment',
    method: 'post',
    data: data
  })
}

// 修改设备台账
export function updateEquipment(data) {
  return request({
    url: '/wms/equipment',
    method: 'put',
    data: data
  })
}

// 删除设备台账
export function delEquipment(id) {
  return request({
    url: '/wms/equipment/' + id,
    method: 'delete'
  })
}
