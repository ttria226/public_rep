import request from '@/utils/request'
import { wms } from '@/utils/agent'
// 查询rfid打印记录列表
export function listPrint(query) {
  return request({
    url: wms + '/base/material/print/list',
    method: 'get',
    params: query
  })
}

// 查询rfid打印记录详细
export function getPrint(id) {
  return request({
    url: wms + '/base/material/print/' + id,
    method: 'get'
  })
}

// 新增rfid打印记录
export function addPrint(data) {
  return request({
    url: wms + '/base/material/print',
    method: 'post',
    data: data
  })
}

// 修改rfid打印记录
export function updatePrint(data) {
  return request({
    url: wms + '/base/material/print',
    method: 'put',
    data: data
  })
}

// 删除rfid打印记录
export function delPrint(id) {
  return request({
    url: wms + '/base/material/print/' + id,
    method: 'delete'
  })
}

// RFID打印
export function printInfo(data) {
  return request({
    url: wms + '/base/material/print/printInfo',
    method: 'post',
    data: data
  })
}


// 普通打印
export function printInfoErcode(data) {
  return request({
    url: wms + '/base/material/print/printInfo/erCode',
    method: 'post',
    data: data
  })
}


//获取可打印入库单详情列表
export function listAdvanceDetail(query) {
  return request({
    url:  wms + '/inout/delivery/detail/getPrintList',
    method: 'get',
    params: query
  })
}


// 根据入库单据id删除打印信息
export function deleteByDeliveryId(data) {
  return request({
    url: wms + '/base/material/print/deleteByDeliveryId',
    method: 'post',
    data: data
  })
}
