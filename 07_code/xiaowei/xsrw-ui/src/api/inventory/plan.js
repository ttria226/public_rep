import request from '@/utils/request'
import { wms } from '@/utils/agent'

// 查询盘点计划列表
export function listInventoryPlan(query) {
  return request({
    url:  wms + '/checkDelivery/list',
    method: 'get',
    params: query
  })
}

// 新增盘点计划
export function addInventoryPlan(data) {
  return request({
    url: wms + '/checkDelivery/add',
    method: 'post',
    data: data
  })
}

// 删除盘点计划
export function delInventoryPlan(id) {
  return request({
    url: wms + '/checkDelivery/' + id,
    method: 'delete'
  })
}

// 生成盘点任务
export function addInventoryTask(data) {
  return request({
    url: wms + '/task/addCheckTask',
    method: 'post',
    data: data
  })
}