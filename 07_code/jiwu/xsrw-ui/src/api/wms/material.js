import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询物料管理列表
export function listMaterial(query) {
  return request({
    url: wms +'/material/list',
    method: 'get',
    params: query
  })
}

// 查询物料管理详细
export function getMaterial(id) {
  return request({
    url: wms +'/material/' + id,
    method: 'get'
  })
}

// 新增物料管理
export function addMaterial(data) {
  return request({
    url: wms +'/material',
    method: 'post',
    data: data
  })
}

// 修改物料管理
export function updateMaterial(data) {
  return request({
    url: wms +'/material',
    method: 'put',
    data: data
  })
}

// 删除物料管理
export function delMaterial(id) {
  return request({
    url: wms +'/material/' + id,
    method: 'delete'
  })
}
// 批量设置库存
export function bacthStock(data) {
  return request({
    url: wms +'/material/bacthStock',
    method: 'post',
    data: data
  })
}