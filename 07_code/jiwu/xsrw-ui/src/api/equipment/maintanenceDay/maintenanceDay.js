import request from '@/utils/request'

// 查询保养工单列表
export function listMaintenanceDay(query) {
  return request({
    url: '/wms/maintenanceDay/list',
    method: 'get',
    params: query
  })
}

// 查询保养工单详细
export function getMaintenanceDay(id) {
  return request({
    url: '/wms/maintenanceDay/' + id,
    method: 'get'
  })
}

// 新增保养工单
export function addMaintenanceDay(data) {
  return request({
    url: '/wms/maintenanceDay',
    method: 'post',
    data: data
  })
}

// 修改保养工单
export function updateMaintenanceDay(data) {
  return request({
    url: '/wms/maintenanceDay',
    method: 'put',
    data: data
  })
}

// 删除保养工单
export function delMaintenanceDay(id) {
  return request({
    url: '/wms/maintenanceDay/' + id,
    method: 'delete'
  })
}
