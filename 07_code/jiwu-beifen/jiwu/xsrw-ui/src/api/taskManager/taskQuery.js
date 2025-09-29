import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询任务查询列表
export function listTaskQuery(query) {
  return request({
    url:  wms + '/taskManager/taskQuery/list',
    method: 'get',
    params: query
  })
}