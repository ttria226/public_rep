import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询工作统计列表
export function listWorkStatistics(query) {
  return request({
    url:  wms + '/taskManager/workStatistics/list',
    method: 'get',
    params: query
  })
}