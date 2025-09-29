import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询盘点差异报表列表
export function listCheckResult(query) {
  return request({
    url:  wms + '/checkResult/list',
    method: 'get',
    params: query
  })
}

// 查询盘点差异报表详细
export function getCheckResult(id) {
  return request({
    url: wms + '/checkResult/' + id,
    method: 'get'
  })
}

// 新增盘点差异报表
export function addCheckResult(data) {
  return request({
    url: wms + '/checkResult',
    method: 'post',
    data: data
  })
}

// 修改盘点差异报表
export function updateCheckResult(data) {
  return request({
    url: wms + '/checkResult',
    method: 'put',
    data: data
  })
}

// 删除盘点差异报表
export function delCheckResult(id) {
  return request({
    url: wms + '/checkResult/' + id,
    method: 'delete'
  })
}

// 查询盘差分析明细
export function getResultInfoList(query) {
  return request({
    url: wms + '/checkResult/resultDetailList',
    method: 'get',
    params: query
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