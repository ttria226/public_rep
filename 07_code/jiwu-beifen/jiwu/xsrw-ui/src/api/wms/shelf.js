import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询货架列表
export function listShelf(query) {
  return request({
    url: wms + '/base/shelf/list',
    method: 'get',
    params: query
  })
}

// 查询货架详细
export function getShelf(id) {
  return request({
    url: wms + '/base/shelf/' + id,
    method: 'get'
  })
}

// 新增货架
export function addShelf(data) {
  return request({
    url: wms + '/base/shelf',
    method: 'post',
    data: data
  })
}

// 修改货架
export function updateShelf(data) {
  return request({
    url: wms + '/base/shelf',
    method: 'put',
    data: data
  })
}

// 删除货架
export function delShelf(id) {
  return request({
    url: wms + '/base/shelf/' + id,
    method: 'delete'
  })
}

// 查询货架下拉列表
export function listGoodShelf(query) {
  return request({
    url: wms + '/base/shelf/getSelectList',
    method: 'get',
    params: query
  })
}