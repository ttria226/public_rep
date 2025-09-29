import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询入出调度列表
export function listInOutDispatch(query) {
  return request({
    url:  wms + '/dispatchCenter/inOutDispatch/list',
    method: 'get',
    params: query
  })
}