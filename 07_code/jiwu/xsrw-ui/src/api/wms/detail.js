import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询物料详情列表
export function listDetail(query) {
  return request({
    url:  wms + '/inout/detail/list',
    method: 'get',
    params: query
  })
}

// 查询物料详情详细
export function getDetail(id) {
  return request({
    url:  wms + '/inout/detail/' + id,
    method: 'get'
  })
}

// 新增物料详情
export function addDetail(data) {
  return request({
    url:  wms + '/inout/detail',
    method: 'post',
    data: data
  })
}

// 修改物料详情
export function updateDetail(data) {
  return request({
    url:  wms + '/inout/detail',
    method: 'put',
    data: data
  })
}

// 删除物料详情
export function delDetail(id) {
  return request({
    url:  wms + '/inout/detail/' + id,
    method: 'delete'
  })
}

// 单个打印rfid
export function printjk(data) {
  return request({
    url:  wms + '/inout/detail/printRfidById',
    method: 'post',
    data: data
  })
}

// 根据入库单物料打印rfid
export function printByAdvanceId(data) {
  return request({
    url:  wms + '/inout/detail/printRfidByAdvanceId',
    method: 'post',
    data: data
  })
}

// 更新redsi
export function listDetail2(query) {
  return request({
    url:  wms + '/inout/detail/alllist2',
    method: 'get',
    params: query
  })
}
// 查询物料详情redis列表
export function listDetail3(query) {
  return request({
    url:  wms + '/inout/detail/alllist3',
    method: 'get',
    params: query
  })
}
