import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询仓库列表
export function listWarehouse(query) {
  return request({
    url:  wms + '/warehouse/list',
    method: 'get',
    params: query
  })
}

// 查询仓库详情
export function getWarehouse(id) {
  return request({
    url: wms + '/warehouse/' + id,
    method: 'get'
  })
}

// 新增仓库
export function addWarehouse(data) {
  return request({
    url: wms + '/warehouse',
    method: 'post',
    data: data
  })
}

// 修改仓库
export function updateWarehouse(data) {
  return request({
    url: wms + '/warehouse',
    method: 'put',
    data: data
  })
}

// 删除仓库
export function delWarehouse(id) {
  return request({
    url: wms + '/warehouse/' + id,
    method: 'delete'
  })
}
// 修改仓库状态
export function changeWarehouseStatus(data) {
  return request({
    url: wms + '/warehouse/changeStatus',
    method: 'put',
    data: data
  })
}
