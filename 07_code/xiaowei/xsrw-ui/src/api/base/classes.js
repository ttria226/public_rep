import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询班次管理列表
export function listClasses(query) {
  return request({
    url: wms + '/base/classes/list',
    method: 'get',
    params: query
  })
}

// 查询班次管理详细
export function getClasses(id) {
  return request({
    url: wms + '/base/classes/' + id,
    method: 'get'
  })
}

// 新增班次管理
export function addClasses(data) {
  return request({
    url: wms + '/base/classes',
    method: 'post',
    data: data
  })
}

// 修改班次管理
export function updateClasses(data) {
  return request({
    url: wms + '/base/classes',
    method: 'put',
    data: data
  })
}

// 删除班次管理
export function delClasses(id) {
  return request({
    url: wms + '/base/classes/' + id,
    method: 'delete'
  })
}
