import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询编码配置列表
export function listCodeConfig(query) {
  return request({
    url:  wms + '/codeConfig/list',
    method: 'get',
    params: query
  })
}

// 查询编码配置详细
export function getCodeConfig(id) {
  return request({
    url: wms + '/codeConfig/' + id,
    method: 'get'
  })
}

// 新增编码配置
export function addCodeConfig(data) {
  return request({
    url: wms + '/codeConfig',
    method: 'post',
    data: data
  })
}

// 修改编码配置
export function updateCodeConfig(data) {
  return request({
    url: wms + '/codeConfig',
    method: 'put',
    data: data
  })
}

// 删除编码配置
export function delCodeConfig(id) {
  return request({
    url: wms + '/codeConfig/' + id,
    method: 'delete'
  })
}
