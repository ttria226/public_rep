import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询拣货策略列表
export function listOutStrategy(query) {
  return request({
    url:  wms + '/outStrategy/list',
    method: 'get',
    params: query
  })
}

// 查询拣货策略详细
export function getOutStrategy(id) {
  return request({
    url: wms + '/outStrategy/' + id,
    method: 'get'
  })
}

// 新增拣货策略
export function addOutStrategy(data) {
  return request({
    url: wms + '/outStrategy',
    method: 'post',
    data: data
  })
}

// 修改拣货策略
export function updateOutStrategy(data) {
  return request({
    url: wms + '/outStrategy',
    method: 'put',
    data: data
  })
}

// 删除拣货策略
export function delOutStrategy(id) {
  return request({
    url: wms + '/outStrategy/' + id,
    method: 'delete'
  })
}
