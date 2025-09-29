import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询收货列表
export function listTakeDelivery(query) {
  return request({
    url:  wms + '/takeDelivery/list',
    method: 'get',
    params: query
  })
}

// 查询收货详细
export function getTakeDelivery(id) {
  return request({
    url: wms + '/takeDelivery/' + id,
    method: 'get'
  })
}

// 查询收货详细
export function listByConfirmReceipt(takeDeliveryCode) {
  return request({
    url: wms + '/takeDeliveryDetail/listByConfirmReceipt?takeDeliveryCode=' + takeDeliveryCode,
    method: 'get'
  })
}

// 新增收货
export function addTakeDelivery(data) {
  return request({
    url: wms + '/takeDelivery',
    method: 'post',
    data: data
  })
}

// 修改收货
export function updateTakeDelivery(data) {
  return request({
    url: wms + '/takeDelivery',
    method: 'put',
    data: data
  })
}

// 删除收货
export function delTakeDelivery(id) {
  return request({
    url: wms + '/takeDelivery/' + id,
    method: 'delete'
  })
}

// 新增收货
export function addTakeDeliveryDetail(data) {
  return request({
    url: wms + '/takeDeliveryDetail/batchUpdate',
    method: 'post',
    data: data
  })
}

// 删除明细
export function delDetail(id) {
  return request({
    url: wms + '/takeDelivery/removeDetail?id=' + id,
    method: 'post'
  })
}

// 审核
export function auditorTakeDelivery(id) {
  return request({
    url: wms + '/takeDelivery/auditor/' + id,
    method: 'get'
  })
}

// 生成上架任务列表
export function shelfTakeDelivery(id) {
  return request({
    url: wms + '/takeDelivery/shelf/' + id,
    method: 'get'
  })
}

// 生成上架任务
export function shelfInfoSubmit(query) {
  return request({
    url: wms + '/takeDelivery/shelfInfoSubmit',
    method: 'get',
    params: query,
  })
}

// 获取载具列表
export function trayList(id) {
  return request({
    url: wms + '/takeDeliveryDetailRecord/trayList',
    method: 'get'
  })
}

// 生成上架任务的物料列表
export function recordList(id) {
  return request({
    url: wms + '/takeDeliveryDetailRecord/list?trayId=' + id,
    method: 'get'
  })
}

// 确认收货
export function confirmReceipt(data) {
  return request({
    url: wms + '/takeDeliveryDetail/confirmReceipt',
    method: 'post',
    data: data
  })
}
// 生成上架任务的物料列表
export function listByTakeDelivery(query) {
  return request({
    url: wms + '/Tray/listByTakeDelivery',
    method: 'get',
    params: query
  })
}
