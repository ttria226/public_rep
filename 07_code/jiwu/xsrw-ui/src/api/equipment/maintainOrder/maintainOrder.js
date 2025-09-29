import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询维修工单列表
export function listMaintainOrder(query) {
  return request({
    url:  wms + '/maintenanceDay/repairList',
    method: 'get',
    params: query
  })
}

// 查询维修工单详细
export function getMaintainOrder(id) {
  return request({
    url: wms + '/maintenanceDay/' + id,
    method: 'get'
  })
}

// 新增维修工单
export function addMaintainOrder(data) {
  return request({
    url: wms + '/maintenanceDay/repairAdd',
    method: 'post',
    data: data
  })
}

// 修改维修工单
export function updateMaintainOrder(data) {
  return request({
    url: wms + '/maintenanceDay',
    method: 'put',
    data: data
  })
}

// 撤销维修工单
export function cancelMaintainOrder(id) {
  return request({
    url: wms + '/maintenanceDay/dayCancel/' + id,
    method: 'put'
  })
}

// 分派/重新分派维修工单
export function sendMaintainOrder(data) {
  return request({
    url: wms + '/maintenanceDay/assign',
    method: 'put',
    data: data
  })
}