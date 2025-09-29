import request from '@/utils/request'
import {wms} from '@/utils/agent'
// 查询列表
export function listErpRecord(query) {
  return request({
    url: wms + '/webservice/erp/record/list',
    method: 'get',
    params: query
  })
}

//
export function sendErpRecord(data) {
  return request({
    url: wms + '/webservice/erp/record/sendRecord',
    method: 'post',
    data: data
  })
}

// 查询列表
export function listErpStockRecord(query) {
  return request({
    url: wms + '/webservice/erp/stock/list',
    method: 'get',
    params: query
  })
}
