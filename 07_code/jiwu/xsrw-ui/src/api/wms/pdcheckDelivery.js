import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询盘点计划列表
export function listCheckDelivery(query) {
  return request({
    url:  wms + '/checkDelivery/list',
    method: 'get',
    params: query
  })
}

// 查询盘点计划详细
export function getCheckDelivery(id) {
  return request({
    url: wms + '/checkDelivery/' + id,
    method: 'get'
  })
}

// 新增盘点计划
export function addCheckDelivery(data) {
  return request({
    url: wms + '/checkDelivery',
    method: 'post',
    data: data
  })
}

// 修改盘点计划
export function updateCheckDelivery(data) {
  return request({
    url: wms + '/checkDelivery',
    method: 'put',
    data: data
  })
}

// 删除盘点计划
export function delCheckDelivery(id) {
  return request({
    url: wms + '/checkDelivery/' + id,
    method: 'delete'
  })
}

// 新增盘点计划
export function checkDeliveryAdd(data) {
  return request({
    url: wms + '/checkDelivery/add',
    method: 'post',
    data: data
  })
}
// 新增盘点任务
export function addCheckTask(data) {
  return request({
    url: wms + '/task/addCheckTask',
    method: 'post',
    data: data
  })
}