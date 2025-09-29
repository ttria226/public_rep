import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询库存台账列表
export function listStockLedger(query) {
  return request({
    url:  wms + '/stock/detail/stockDetailLedgerList',
    method: 'get',
    params: query
  })
}