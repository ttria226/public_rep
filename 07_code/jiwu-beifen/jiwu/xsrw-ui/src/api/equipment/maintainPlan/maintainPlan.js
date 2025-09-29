import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询设备维修计划列表
export function listMaintainPlan(query) {
  return request({
    url: wms + '/maintanenceplan/repairList',
    method: 'get',
    params: query
  })
}

// 查询设备维修计划详细
export function getMaintainPlan(id) {
  return request({
    url: wms + '/maintanenceplan/' + id,
    method: 'get'
  })
}

// 新增设备维修计划
export function addMaintainPlan(data) {
  return request({
    url: wms + '/maintanenceplan/repairAdd',
    method: 'post',
    data: data
  })
}

// 修改设备维修计划
export function updateMaintainPlan(data) {
  return request({
    url: wms + '/maintanenceplan',
    method: 'put',
    data: data
  })
}

// 启用设备维修计划
export function startMaintainPlan(id) {
  return request({
    url: wms + '/maintanenceplan/planStart/'+id,
    method: 'put'
  })
}

// 作废设备维修计划
export function cancelMaintainPlan(id) {
  return request({
    url: wms + '/maintanenceplan/planCancel/'+id,
    method: 'put'
  })
}