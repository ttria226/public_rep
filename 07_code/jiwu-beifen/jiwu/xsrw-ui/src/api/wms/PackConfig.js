import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询包装配置列表
export function listPackConfig(query) {
  return request({
    url:  wms + '/unit/config/list',
    method: 'get',
    params: query
  })
}

// 查询包装配置详细
export function getPackConfig(id) {
  return request({
    url: wms + '/unit/config/' + id,
    method: 'get'
  })
}

// 新增包装配置
export function addPackConfig(data) {
  return request({
    url: wms + '/unit/config',
    method: 'post',
    data: data
  })
}

// 修改包装配置
export function updatePackConfig(data) {
  return request({
    url: wms + '/unit/config',
    method: 'put',
    data: data
  })
}

// 删除包装配置
export function delPackConfig(id) {
  return request({
    url: wms + '/unit/config/' + id,
    method: 'delete'
  })
}
