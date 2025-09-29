import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询上架策略列表
export function listRule(query) {
  return request({
    url:  wms + '/rule/list',
    method: 'get',
    params: query
  })
}

// 查询上架策略详细
export function getRule(id) {
  return request({
    url: wms + '/rule/' + id,
    method: 'get'
  })
}

// 新增上架策略
export function addRule(data) {
  return request({
    url: wms + '/rule',
    method: 'post',
    data: data
  })
}

// 修改上架策略
export function updateRule(data) {
  return request({
    url: wms + '/rule',
    method: 'put',
    data: data
  })
}

// 删除上架策略
export function delRule(id) {
  return request({
    url: wms + '/rule/' + id,
    method: 'delete'
  })
}
// 查询已生成策略仓库列表
export function added(query) {
  return request({
    url:  wms + '/rule/added',
    method: 'get',
    params: query
  })
}
