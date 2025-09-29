import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询单位列表
export function listUnit(query) {
  return request({
    url:  wms +'/unit/list',
    method: 'get',
    params: query
  })
}

// 查询单位详细
export function getUnit(id) {
  return request({
    url: wms + '/unit/' + id,
    method: 'get'
  })
}

// 新增单位
export function addUnit(data) {
  return request({
    url:  wms +'/unit',
    method: 'post',
    data: data
  })
}

// 修改单位
export function updateUnit(data) {
  return request({
    url:  wms +'/unit',
    method: 'put',
    data: data
  })
}

// 删除单位
export function delUnit(id) {
  return request({
    url:  wms +'/unit/' + id,
    method: 'delete'
  })
}
