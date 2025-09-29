import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询货位使用频率报表列表
export function listFrequencyOfLocation(query) {
  return request({
    url: wms+'/report/center/frequencyOfLocation/list',
    method: 'get',
    params: query
  })
}