import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询仓库状态列表
export function listWareHouseStatus(query) {
  return request({
    url:  wms + '/dispatchCenter/wareHouseStatus/list',
    method: 'get',
    params: query
  })
}