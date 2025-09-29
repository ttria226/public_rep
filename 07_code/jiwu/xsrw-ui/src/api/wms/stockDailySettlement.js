import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询库存日结查询列表
export function listStockDailySettlement(query) {
  return request({
    url:  wms + '/stock/detail/stockDailySettlement/list',
    method: 'get',
    params: query
  })
}