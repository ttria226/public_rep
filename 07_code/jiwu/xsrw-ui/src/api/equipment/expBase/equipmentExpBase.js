import request from '@/utils/request'

// 查询保养/维修经验库列表
export function listEquipmentExpBase(query) {
  return request({
    url: '/wms/equipmentExpBase/list',
    method: 'get',
    params: query
  })
}

// 查询保养/维修经验库详细
export function getEquipmentExpBase(id) {
  return request({
    url: '/wms/equipmentExpBase/' + id,
    method: 'get'
  })
}

// 新增保养/维修经验库
export function addEquipmentExpBase(data) {
  return request({
    url: '/wms/equipmentExpBase',
    method: 'post',
    data: data
  })
}

// 修改保养/维修经验库
export function updateEquipmentExpBase(data) {
  return request({
    url: '/wms/equipmentExpBase',
    method: 'put',
    data: data
  })
}

// 删除保养/维修经验库
export function delEquipmentExpBase(id) {
  return request({
    url: '/wms/equipmentExpBase/' + id,
    method: 'delete'
  })
}
