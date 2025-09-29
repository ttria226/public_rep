import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询库位列表
export function listLocation(query) {
  return request({
    url:wms + '/location/list',
    method: 'get',
    params: query
  })
}

// 查询库位详细
export function getLocation(id) {
  return request({
    url:wms + '/location/' + id,
    method: 'get'
  })
}



// 批量新增库位
export function paddLocation(data) {
  return request({
    url:wms + '/location/padd',
    method: 'post',
    data: data
  })
}

// 新增库位
export function addLocation(data) {
  return request({
    url:wms + '/location',
    method: 'post',
    data: data
  })
}

// 修改库位
export function updateLocation(data) {
  return request({
    url:wms + '/location',
    method: 'put',
    data: data
  })
}

// 删除库位
export function delLocation(id) {
  return request({
    url: wms + '/location/' + id,
    method: 'delete'
  })
}


// 获取最大的排列层
export function plcCount(data) {
  return request({
    url: wms + '/location/plcCount' ,
    method: 'post',
    data: data
  })
}


// 获取最大的排列层  批量更新仓库、库区、区域
export function plupdateLocation(data) {
  return request({
    url: wms + '/location/plcUpdate' ,
    method: 'post',
    data: data
  })
}


// 启用禁用
export function updateStatus(query) {
  return request({
    url: wms + '/location/updateStatus',
    method: 'get',
    params: query,
  })
}

// 查询需盘点管理列表
export function listDemandCheckLocation(query) {
  return request({
    url:wms + '/location/demandCheck/list',
    method: 'get',
    params: query
  })
}

// 需盘点管理-标记有货/无货
export function markerDemandCheckLocation(data) {
  return request({
    url: wms + '/location/updateGoodsAllocationStatus' ,
    method: 'post',
    data: data
  })
}
