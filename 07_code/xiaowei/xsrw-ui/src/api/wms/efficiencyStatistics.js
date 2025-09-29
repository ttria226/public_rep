import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询效率统计报表列表
export function listEfficiencyStatistics(query) {
  return request({
    url: wms+'/report/center/efficiencyStatistics/list',
    method: 'get',
    params: query
  })
}