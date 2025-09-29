import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询库龄分析报表列表
export function listWareHouseAgeAnalyse(query) {
  return request({
    url: wms + '/report/center/wareHouseAgeAnalyse/list',
    method: 'get',
    params: query
  })
}