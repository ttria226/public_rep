import request from '@/utils/request'
import {wms} from '@/utils/agent'


// 查询批次属性列表
export function listAttr(query) {
  return request({
    url: wms +'/attr/list',
    method: 'get',
    params: query
  })
}

// 查询批次属性详细
export function getAttr(id) {
  return request({
    url: wms +'/attr/' + id,
    method: 'get'
  })
}

// 新增批次属性
export function addAttr(data) {
  return request({
    url: wms +'/attr',
    method: 'post',
    data: data
  })
}

// 修改批次属性
export function updateAttr(data) {
  return request({
    url: wms +'/attr',
    method: 'put',
    data: data
  })
}

// 删除批次属性
export function delAttr(id) {
  return request({
    url: wms +'/attr/' + id,
    method: 'delete'
  })
}
