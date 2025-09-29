import request from '@/utils/request'
import {wms} from '@/utils/agent'
// 查询库区列表
export function listReservoir(query) {
  return request({
    url: wms + '/reservoir/list',
    method: 'get',
    params: query
  })
}

// 查询库区详细
export function getReservoir(id) {
  return request({
    url: wms + '/reservoir/' + id,
    method: 'get'
  })
}

// 新增库区
export function addReservoir(data) {
  return request({
    url: wms + '/reservoir',
    method: 'post',
    data: data
  })
}

// 修改库区
export function updateReservoir(data) {
  return request({
    url: wms + '/reservoir',
    method: 'put',
    data: data
  })
}

// 删除库区
export function delReservoir(id) {
  return request({
    url: wms + '/reservoir/' + id,
    method: 'delete'
  })
}

// 启用/禁用库区
export function delReservoirStatus(query) {
  return request({
    url: wms + '/reservoir/status',
    method: 'get',
    params: query
  })
}
