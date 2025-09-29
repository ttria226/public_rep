import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 物料收发汇总列表
export function listReportyCenter(query) {
  return request({
    url:  wms + '/report/center/list',
    method: 'get',
    params: query
  })
}

// 查询出库计划详细
export function getOutDelivery(id) {
  return request({
    url: wms + '/outDelivery/' + id,
    method: 'get'
  })
}

// 新增出库计划
export function addOutDelivery(data) {
  return request({
    url: wms + '/outDelivery',
    method: 'post',
    data: data
  })
}

