import request from '@/utils/request'
import {wms, cims} from '@/utils/agent'

// 查询库存列表
export function listStock(query) {
  return request({
    url:  wms + '/stock/list',
    method: 'get',
    params: query
  })
}

// 查询库存详细
export function getStock(id) {
  return request({
    url: wms + '/stock/' + id,
    method: 'get'
  })
}

// 新增库存
export function addStock(data) {
  return request({
    url: wms + '/stock',
    method: 'post',
    data: data
  })
}

// 修改库存
export function updateStock(data) {
  return request({
    url: wms + '/stock',
    method: 'put',
    data: data
  })
}

// 删除库存
export function delStock(id) {
  return request({
    url: wms + '/stock/' + id,
    method: 'delete'
  })
}

// 冻结解冻
export function updateFreeze(data) {
  return request({
    url: wms + '/stock/updateFreezeByIds',
    method: 'post',
    data: data
  })
}

// 查询批次总数量列表
export function listBatchSum(query) {
  return request({
    url:  wms + '/stock/listBatchSum',
    method: 'get',
    params: query
  })
}

// 根据id获取载具列表
export function stockShift(id) {
  return request({
    url: wms + '/stock/shift/' + id,
    method: 'get'
  })
}

// 移库
export function moveLibrary(data) {
  return request({
    url: wms + '/moveLibrary/shift',
    method: 'post',
    data: data
  })
}

// 移库根据id获取库位列表
export function getOtherLocation(query) {
  return request({
    url: wms + '/location/getOtherLocation',
    method: 'get',
    params: query
  })
}

export function stockMainList(query) {
  return request({
    url:  wms + '/stock/stockMain/list',
    method: 'get',
    params: query
  })
}

//盘点计划-选择物料
export function stockInventoryList(query) {
  return request({
    url:  wms + '/inout/detail/selectMaterialDetailList',
    method: 'get',
    params: query
  })
}
