import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询采购订单准时交付率列表
export function listDeliveryTimeStatistics(query) {
  return request({
    url:  wms + '/report/center/deliveryTimeStatistics/list',
    method: 'get',
    params: query
  })
}