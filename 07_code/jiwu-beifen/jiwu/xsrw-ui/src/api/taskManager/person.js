import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询人员管理列表
export function listPerson(query) {
  return request({
    url:  wms + '/person/list',
    method: 'get',
    params: query
  })
}

// 查询人员管理详情
export function getPerson(id) {
  return request({
    url: wms + '/person/' + id,
    method: 'get'
  })
}

// 新增人员管理
export function addPerson(data) {
  return request({
    url: wms + '/person',
    method: 'post',
    data: data
  })
}

// 修改人员管理
export function updatePerson(data) {
  return request({
    url: wms + '/person',
    method: 'put',
    data: data
  })
}

// 删除人员管理
export function delPerson(id) {
  return request({
    url: wms + '/person/' + id,
    method: 'delete'
  })
}