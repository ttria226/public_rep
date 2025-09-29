import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询复检管理列表
export function listRecheck(query) {
  return request({
    url: wms + '/stock/recheck/list',
    method: 'get',
    params: query
  })
}

// 新增复检管理
export function addRecheck(data) {
  return request({
    url: wms + '/stock/recheck',
    method: 'post',
    data: data
  })
}

// 复检管理-检测失败
export function recheckCheckFail(data) {
  return request({
    url: wms + '/inout/detail/checkStockMaDetail',
    method: 'post',
    data: data
  })
}

// 复检管理-检测完成
export function recheckCheckFinish(data) {
  return request({
    url: wms + '/stock/recheck/checkEnd',
    method: 'post',
    data: data
  })
}

// 查询复检库存列表
export function getRecheckStockInfoList(query) {
  return request({
    url: wms + '/stock/getStockInfoList',
    method: 'get',
    params: query
  })
}

// 查询复检库存的物料详情列表
export function getRecheckMaterialDetailList(query) {
  return request({
    url: wms + '/stock/recheck/getMaterialDetailList',
    method: 'get',
    params: query
  })
}