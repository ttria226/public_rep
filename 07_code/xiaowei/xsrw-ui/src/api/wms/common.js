import request from '@/utils/request'
import {wms} from '@/utils/agent'


// 获取系统编码
export function getCode(query) {
  return request({
    url: wms + '/code/create',
    method: 'get',
    params: {modelName: query}
  })
}
