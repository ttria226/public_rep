import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 库存调整列表
export function liststockResult(query) {
  return request({
    url:  wms + '/stock/changelog/stocklist',
    method: 'get',
    params: query
  })
}

// 库存调整记录
export function liststockLog(query) {
  return request({
    url:  wms + '/stock/changelog/list',
    method: 'get',
    params: query
  })
}

//添加调整记录
export function addCheckChange(data){
  return request({
    url: wms + '/stock/changelog/amendStockInfo',
    method: 'post',
    data: data
  })
}