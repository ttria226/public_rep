import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询规则列表
export function listRule(query) {
  return request({
    url: wms + '/base/rule/list',
    method: 'get',
    params: query
  })
}

// 修改规则状态
export function updateRuleStatus(data) {
  return request({
    url: wms + '/base/rule',
    method: 'put',
    data: data
  })
}

//获取是否校验生产日期
export function getRuleStatus(id) {
  return request({
    url: wms + '/base/rule/getStatus/'+id,
    method: 'get',
  })
}