import request from '@/utils/request'
import {wms} from '@/utils/agent'
// 查询预警邮件配置列表
export function listForewarning(query) {
  return request({
    url: wms + '/stock/forewarning/list',
    method: 'get',
    params: query
  })
}

// 查询预警邮件配置详细
export function getForewarning(id) {
  return request({
    url: wms + '/stock/forewarning/' + id,
    method: 'get'
  })
}

// 新增预警邮件配置
export function addForewarning(data) {
  return request({
    url: wms + '/stock/forewarning',
    method: 'post',
    data: data
  })
}

// 修改预警邮件配置
export function updateForewarning(data) {
  return request({
    url: wms + '/stock/forewarning',
    method: 'put',
    data: data
  })
}

// 删除预警邮件配置
export function delForewarning(id) {
  return request({
    url: wms + '/stock/forewarning/' + id,
    method: 'delete'
  })
}
// 发送邮件
export function sendEmail(data) {
  return request({
    url: wms + '/stock/forewarning/sendEmail',
    method: 'post',
    data: data
  })
}
