import request from '@/utils/request'
import { wms } from "@/utils/agent";
// 查询设备借还借出换入记录列表
export function listRecord(query) {
  return request({
    url: wms + '/loan/record/list',
    method: 'get',
    params: query
  })
}

// 查询设备借还借出换入记录详细
export function getRecord(id) {
  return request({
    url: wms + '/loan/record/' + id,
    method: 'get'
  })
}

// 新增设备借还借出换入记录
export function addRecord(data) {
  return request({
    url: wms + '/loan/record',
    method: 'post',
    data: data
  })
}

// 设备还入
export function returnRecord(data) {
  return request({
    url: wms + '/loan/record/return',
    method: 'post',
    data: data
  })
}

// 删除设备借还借出换入记录
export function delRecord(id) {
  return request({
    url: wms + '/loan/record/' + id,
    method: 'delete'
  })
}
