import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询效率统计列表
export function listEfficiencyStatistics(query) {
  return request({
    url:  wms + '/taskManager/efficiencyStatistics/list',
    method: 'get',
    params: query
  })
}