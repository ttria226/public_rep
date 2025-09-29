import request from '@/utils/request'
import {wms} from '@/utils/agent'
import {cims} from '@/utils/agent'

// 查询任务详情列表
export function listTaskDetail(query) {
  return request({
    url:  wms + '/task/list',
    method: 'get',
    params: query
  })
}

// 查询任务详情详细
export function getTaskDetail(id) {
  return request({
    url: wms + '/taskDetail/' + id,
    method: 'get'
  })
}

// 新增任务详情
export function addTaskDetail(data) {
  return request({
    url: wms + '/taskDetail',
    method: 'post',
    data: data
  })
}

// 修改任务详情
export function updateTaskDetail(data) {
  return request({
    url: wms + '/taskDetail',
    method: 'put',
    data: data
  })
}

// 删除任务详情
export function delTaskDetail(id) {
  return request({
    url: wms + '/taskDetail/' + id,
    method: 'delete'
  })
}

// 任务详情
export function detailList(query) {
  return request({
    url:  wms + '/api/task/detail/list',
    method: 'get',
    params: query
  })
}

// 执行任务
export function execute(query) {
  return request({
    url: wms + '/api/task/execute',
    method: 'get',
    params: query
  })
}

// 执行盘点
export function performCheck(id,data) {
  return request({
    url: wms + '/taskDetail/performCheck/'+id,
    method: 'post',
    data: data
  })
}

// 查询任务详情列表
export function trayDetail(query) {
  return request({
    url:  wms + '/checkDelivery/checkdelivery/detail',
    method: 'get',
    params: query
  })
}

// 执行盘点提交
export function checkdeliverySubmit(data) {
  return request({
    url: wms + '/checkDelivery/checkdelivery/submit',
    method: 'post',
    data: data
  })
}

// 平库盘点获取列表
export function checkAreaList(query) {
  return request({
    url:  wms + '/api/task/checkAreaDelivery/list',
    method: 'get',
    params: query
  })
}

// 平库盘点新增数据
export function checkAreaSave(data) {
  return request({
    url: wms + '/api/task/checkAreaDelivery/save',
    method: 'post',
    data: data
  })
}
// 平库盘点修改数据
export function checkAreaDeliveryUpdate(data) {
  return request({
    url: wms + '/api/task/checkAreaDelivery/update',
    method: 'post',
    data: data
  })
}
// 平库盘点删除数据
export function checkAreaDel(query) {
  return request({
    url:  wms + '/api/task/checkAreaDelivery/del',
    method: 'get',
    params: query
  })
}

// 平库盘点提交数据
export function checkAreaSubmit(query) {
  return request({
    url:  wms + '/api/task/checkAreaDelivery/submit',
    method: 'get',
    params: query
  })
}

// 校验托盘是否存在
export function codeQuery(query) {
  return request({
    url:  cims + '/material/feign/codeQuery',
    method: 'get',
    params: query
  })
}

// 获取执行盘点的载具和物料下拉列表
export function getInventorySelectLists(query) {
  return request({
    url:  wms + '/checkDelivery/getDropdownData',
    method: 'get',
    params: query
  })
}
