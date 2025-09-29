import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询库存交易列表
export function listStockDeal(query) {
  return request({
    url:  wms + '/stock/detail/stockDeal/list',
    method: 'get',
    params: query
  })
}