import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询调拨单列表
export function listAllotPlan(query) {
  return request({
    url:  wms + '/allot/list',
    method: 'get',
    params: query
  })
}

// 新增调拨单
export function addAllotPlan(data) {
  return request({
    url: wms + '/allot/add',
    method: 'post',
    data: data
  })
}

// 删除调拨单
export function delAllotPlan(id) {
  return request({
    url: wms + '/allot/' + id,
    method: 'delete'
  })
}

// 执行调拨
export function executeAllot(query) {
  return request({
    url:  wms + '/allot/createDelivery',
    method: 'get',
    params: query
  })
}