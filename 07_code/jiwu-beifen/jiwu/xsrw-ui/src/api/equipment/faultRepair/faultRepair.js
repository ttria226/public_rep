import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询故障报修单列表
export function listFaultRepairOrder(query) {
  return request({
    url:  wms + '/repairReport/list',
    method: 'get',
    params: query
  })
}

// 查询故障报修单详细
export function getFaultRepairOrder(id) {
  return request({
    url: wms + '/repairReport/' + id,
    method: 'get'
  })
}

// 新增故障报修单
export function addFaultRepairOrder(data) {
  return request({
    url: wms + '/repairReport',
    method: 'post',
    data: data
  })
}

// 修改故障报修单
export function updateFaultRepairOrder(data) {
  return request({
    url: wms + '/repairReport',
    method: 'put',
    data: data
  })
}

// 删除故障报修单
export function delFaultRepairOrder(id) {
  return request({
    url: wms + '/repairReport/' + id,
    method: 'delete'
  })
}

// 生成工单
export function createMaintainOrder(data) {
  return request({
    url: wms + '/repairReport/createOrder',
    method: 'post',
    data: data
  })
}

// 查询维修记录列表
export function listMaintainRecord(query) {
  return request({
    url:  wms + '/maintenanceDay/repairedList',
    method: 'get',
    params: query
  })
}