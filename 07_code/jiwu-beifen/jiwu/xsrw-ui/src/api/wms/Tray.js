import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询载具管理列表
export function listTray(query) {
  return request({
    url:  wms + '/tray/list',
    method: 'get',
    params: query
  })
}

// 查询载具管理详细
export function getTray(id) {
  return request({
    url: wms + '/tray/' + id,
    method: 'get'
  })
}

// 新增载具管理
export function addTray(data) {
  return request({
    url: wms + '/tray',
    method: 'post',
    data: data
  })
}

// 修改载具管理
export function updateTray(data) {
  return request({
    url: wms + '/tray',
    method: 'put',
    data: data
  })
}

// 删除载具管理
export function delTray(id) {
  return request({
    url: wms + '/tray/' + id,
    method: 'delete'
  })
}

// 载具出库
export function takeOutTray(data) {
  return request({
    url: wms + '/tray/takeOut',
    method: 'post',
    data: data
  })
}


// 载具出库 -- 盘点
export function takeOutTrayCheck(data) {
  return request({
    url: wms + '/tray/takeOut/check',
    method: 'post',
    data: data
  })
}

// 载具回库
export function recycleTray(data) {
  return request({
    url: wms + '/tray/recycle',
    method: 'post',
    data: data
  })
}
// 解除库位绑定
export function relieveLocation(data) {
  return request({
    url: wms + '/tray/relieveLocation',
    method: 'post',
    data: data
  })
}
