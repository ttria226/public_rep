import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询存储策略列表
export function listPutaway(query) {
  return request({
    url: wms + '/base/putaway/list',
    method: 'get',
    params: query
  })
}

// 查询存储策略详情
export function getPutaway(id) {
  return request({
    url: wms + '/base/putaway/' + id,
    method: 'get'
  })
}

// 新增存储策略
export function addPutaway(data) {
  return request({
    url: wms + '/base/putaway',
    method: 'post',
    data: data
  })
}

// 修改存储策略
export function updatePutaway(data) {
  return request({
    url: wms + '/base/putaway',
    method: 'put',
    data: data
  })
}

// 删除存储策略
export function delPutaway(id) {
  return request({
    url: wms + '/base/putaway/' + id,
    method: 'delete'
  })
}

// 更新存储策略状态
export function updatePutawayStatus(data) {
  return request({
    url: wms + '/base/putaway/updateStatus',
    method: 'put',
    data: data
  })
}

// 查询存储策略的选择物料列表
export function getPutawayMaterialSelectList(query) {
  return request({
    url: wms + '/material/getMaterialSelectList',
    method: 'get',
    params: query
  })
}