import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询任务列表
export function listTask(query) {
  return request({
    url:  wms + '/task/list',
    method: 'get',
    params: query
  })
}

// 查询任务详细
export function getTask(id) {
  return request({
    url: wms + '/task/' + id,
    method: 'get'
  })
}
// 查询任务详细列表
export function getListByTaskId(query) {
  return request({
    // url: wms + '/taskDetail/getListByTaskId/' + id,
    // method: 'get',
    url:  wms + '/taskDetail/getListByTaskId',
    method: 'get',
    params: query
  })
}
// 查询任务详细历史记录列表
export function getCheckAreaHistory(query) {
  return request({
    url:  wms + '/taskDetail/getCheckAreaHistory',
    method: 'get',
    params: query
  })
}
// 新增任务
export function addTask(data) {
  return request({
    url: wms + '/task',
    method: 'post',
    data: data
  })
}

// 修改任务
export function updateTask(data) {
  return request({
    url: wms + '/task',
    method: 'put',
    data: data
  })
}

// 删除任务
export function delTask(id) {
  return request({
    url: wms + '/task/' + id,
    method: 'delete'
  })
}
// 批量审核
export function approveTask(id) {
  return request({
    url: wms + '/task/approve/'+id,
    method: 'post',
  })
}
// 批量审核
export function approve(id,data) {
  return request({
    // url: wms + '/task/approve/'+id,
    url: wms + '/taskDetail/approve/'+id+"?status="+data.status,
    method: 'post',
    // data: data
  })
}

// 平库库区盘点批量审核
export function approveAreaCheck(data) {
  return request({
    url:  wms + '/taskDetail/approveAreaCheck',
    method: 'post',
    data: data
  })
}

// 生成盘点报表
export function addTaskResult(id) {
  return request({
    url: wms + '/checkResult/createCheckResult/'+id,
    method: 'post'
  })
}
