import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询库存物资月报列表
export function listStockMaterialMonthReport(query) {
  return request({
    url:  wms + '/inout/detail/materialDetailMonthlyCountList',
    method: 'get',
    params: query
  })
}