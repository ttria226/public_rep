import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询经验库分类树列表
export function listEquipmentTree(query) {
  return request({
    url: wms + '/equipmentTree/list',
    method: 'get',
    params: query
  })
}

// 查询经验库分类树详细
export function getEquipmentTree(id) {
  return request({
    url: wms + '/equipmentTree/' + id,
    method: 'get'
  })
}

// 新增经验库分类树
export function addEquipmentTree(data) {
  return request({
    url: wms + '/equipmentTree',
    method: 'post',
    data: data
  })
}

// 修改经验库分类树
export function updateEquipmentTree(data) {
  return request({
    url: wms + '/equipmentTree',
    method: 'put',
    data: data
  })
}

// 删除经验库分类树
export function delEquipmentTree(id) {
  return request({
    url: wms + '/equipmentTree/' + id,
    method: 'delete'
  })
}
