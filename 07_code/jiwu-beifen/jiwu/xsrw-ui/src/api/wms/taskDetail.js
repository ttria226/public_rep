import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询任务详情列表
export function listTaskDetail(query) {
  return request({
    url:  wms + '/taskDetail/list',
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

// 批量新增任务详情
export function batchAddTaskDetail(data) {
  return request({
    url: wms + '/taskDetail/batchAddOrUpdate',
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
