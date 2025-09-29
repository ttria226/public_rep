import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询供应商来料合格率年度分析报表列表
export function listMaterialQualificationRate(query) {
  return request({
    url:  wms + '/report/center/materialQualificationRate/list',
    method: 'get',
    params: query
  })
}