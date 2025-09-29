import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询可视化列表
export function listVisualization(query) {
  return request({
    url:  wms + '/reservoir/getReservoirList',
    method: 'get',
    params: query
  })
}

// 查询可视化的区域列表
export function getVisualizationAreaSelect(query) {
  return request({
    url: wms + '/area/chooseListArea',
    method: 'get',
    params: query
  })
}

// 查询可视化的库区列表
export function getVisualizationReservoirSelect(query) {
  return request({
    url: wms + '/reservoir/reservoirList',
    method: 'get',
    params: query
  })
}

// 查询可视化的库区的排列表
export function getVisualizationLocationSelect(query) {
  return request({
    url: wms + '/location/getLocationListByReservoirId',
    method: 'get',
    params: query
  })
}

// 获取可视化详情
export function getVisualizationLocationDetail(query) {
  return request({
    url: wms + '/location/getLocationCurrentDetail',
    method: 'get',
    params: query
  })
}

// 修改库位状态
export function updateVisualizationLocationStatus(data) {
  return request({
    url: wms +  '/location/updateLocationStatus',
    method: 'put',
    data: data
  })
}

// 查询可视化的空闲库位列表
export function getVisualizationOtherLocation(query) {
  return request({
    url: wms + '/location/getOtherLocation',
    method: 'get',
    params: query
  })
}

// 可视化的库位冻结解冻
export function updateVisualizationLocationFreeze(data) {
  return request({
    url: wms + '/stock/locationFreeze',
    method: 'get',
    params: data
  })
}

// 可视化的库位出库
export function visualizationLocationOutbound(data) {
  return request({
    url: wms + '/deliveryOut/visualOutbound',
    method: 'post',
    data: data
  })
}