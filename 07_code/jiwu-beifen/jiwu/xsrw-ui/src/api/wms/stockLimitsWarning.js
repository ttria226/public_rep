import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 列表
export function listStockWarning(query) {
  return request({
    url:  wms + '/report/center/stockWarning',
    method: 'get',
    params: query
  })
}

// 补货列表
export function listStockWarningReplenishment(query) {
  return request({
    url:  wms + '/report/center/stockWarning/replenishment',
    method: 'get',
    params: query
  })
}

// 有效期预警列表
export function listStockWarningValidityWarning(query) {
  return request({
    url:  wms + '/report/center/stockWarning/validityWarning',
    method: 'get',
    params: query
  })
}