import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询业务监控列表
export function listBusinessMonitors(query) {
  return request({
    url:  wms + '/dispatchCenter/businessMonitors/list',
    method: 'get',
    params: query
  })
}