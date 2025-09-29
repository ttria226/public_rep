import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询库存统计报表列表
export function listStoreStatistics(query) {
  return request({
    url: wms + '/report/center/storeStatistics/list',
    method: 'get',
    params: query
  })
}