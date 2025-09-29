import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询bom列表
export function listBom(query) {
  return request({
    url: wms + '/bom/list',
    method: 'get',
    params: query
  })
}

// 查询bom详细
export function getBom(id) {
  return request({
    url: wms + '/bom/' + id,
    method: 'get'
  })
}

// 新增bom
export function addBom(data) {
  return request({
    url: wms + '/bom',
    method: 'post',
    data: data
  })
}

// 修改bom
export function updateBom(data) {
  return request({
    url: wms + '/bom',
    method: 'put',
    data: data
  })
}

// 删除bom
export function delBom(id) {
  return request({
    url: wms + '/bom/' + id,
    method: 'delete'
  })
}

// 查询选择的bom列表
export function getBomSelectList(query) {
  return request({
    url: wms + '/bom/getBomList',
    method: 'get',
    params: query
  })
}

// 根据bomId获取物料信息
export function getMaterialListByBomId(query) {
  return request({
    url: wms + '/bom/getMaterialListByBomId',
    method: 'get',
    params: query
  })
}