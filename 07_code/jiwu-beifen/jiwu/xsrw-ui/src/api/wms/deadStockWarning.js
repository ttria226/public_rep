import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询呆滞品预警报表列表
export function listDeadStockWarning(query) {
  return request({
    url: wms+'/report/center/deadStockWarning/list',
    method: 'get',
    params: query
  })
}