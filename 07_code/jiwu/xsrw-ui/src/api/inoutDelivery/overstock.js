import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询越库列表
export function listOverstock(query) {
  return request({
    url:  wms + '/inout/overstock/list',
    method: 'get',
    params: query
  })
}

// 查询越库详情
export function getOverstock(id) {
  return request({
    url: wms + '/inout/overstock/' + id,
    method: 'get'
  })
}

// 新增越库
export function addOverstock(data) {
  return request({
    url: wms + '/inout/overstock',
    method: 'post',
    data: data
  })
}

// 越库-收货
export function registerOverstock(data) {
  return request({
    url: wms + '/inout/overstock/registerDelivery',
    method: 'post',
    data: data
  })
}

// 越库-出库
export function outOverstock(data) {
  return request({
    url: wms + '/inout/overstock/outDelivery',
    method: 'post',
    data: data
  })
}