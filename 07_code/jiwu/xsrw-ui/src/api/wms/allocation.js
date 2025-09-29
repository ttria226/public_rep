import request from '@/utils/request'
import {wms} from '@/utils/agent'
// 查询接货位列表
export function listAllocation(query) {
  return request({
    url: wms + '/allocation/list',
    method: 'get',
    params: query
  })
}

// 查询接货位详细
export function getAllocation(id) {
  return request({
    url: wms + '/allocation/' + id,
    method: 'get'
  })
}

// 新增接货位
export function addAllocation(data) {
  return request({
    url: wms + '/allocation',
    method: 'post',
    data: data
  })
}

// 修改接货位
export function updateAllocation(data) {
  return request({
    url: wms + '/allocation',
    method: 'put',
    data: data
  })
}

// 删除接货位
export function delAllocation(id) {
  return request({
    url: wms + '/allocation/' + id,
    method: 'delete'
  })
}

// 载具搬运
export function carryAllocation(data) {
  return request({
    url: wms + '/wcs/agvSlim',
    method: 'post',
    data: data
  })
}
