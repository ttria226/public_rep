import request from '@/utils/request'

// 查询设备保养计划列表
export function listMaintanenceplan(query) {
  return request({
    url: '/wms/maintanenceplan/list',
    method: 'get',
    params: query
  })
}

// 查询设备保养计划详细
export function getMaintanenceplan(id) {
  return request({
    url: '/wms/maintanenceplan/' + id,
    method: 'get'
  })
}

// 新增设备保养计划
export function addMaintanenceplan(data) {
  return request({
    url: '/wms/maintanenceplan',
    method: 'post',
    data: data
  })
}

// 修改设备保养计划
export function updateMaintanenceplan(data) {
  return request({
    url: '/wms/maintanenceplan',
    method: 'put',
    data: data
  })
}

// 删除设备保养计划
export function delMaintanenceplan(id) {
  return request({
    url: '/wms/maintanenceplan/' + id,
    method: 'delete'
  })
}
