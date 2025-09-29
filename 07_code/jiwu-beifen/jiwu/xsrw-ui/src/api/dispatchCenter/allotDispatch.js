import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询调拨调度列表
export function listAllotDispatch(query) {
  return request({
    url:  wms + '/dispatchCenter/allotDispatch/list',
    method: 'get',
    params: query
  })
}