import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询标签模板列表
export function listLabelTemplate(query) {
  return request({
    url:  wms + '/labelTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询标签模板详细
export function getLabelTemplate(id) {
  return request({
    url: wms + '/labelTemplate/' + id,
    method: 'get'
  })
}

// 新增标签模板
export function addLabelTemplate(data) {
  return request({
    url: wms + '/labelTemplate',
    method: 'post',
    data: data
  })
}

// 修改标签模板
export function updateLabelTemplate(data) {
  return request({
    url: wms + '/labelTemplate',
    method: 'put',
    data: data
  })
}

// 删除标签模板
export function delLabelTemplate(id) {
  return request({
    url: wms + '/labelTemplate/' + id,
    method: 'delete'
  })
}

// 保存模板对应载具
export function updateLabelByIds(data) {
  return request({
    url: wms + '/tray/updateLabelByIds',
    method: 'post',
    data: data
  })
}

// 获取当前模板绑定的载具
export function listBylabelTemplateId(query) {
  return request({
    url: wms + '/tray/listBylabelTemplateId',
    method: 'get',
    params: query
  })
}

// 标签选择物料列表
export function getMaterialList(query) {
  return request({
    url:  wms + '/labelTemplate/getMaterialList',
    method: 'get',
    params: query
  })
}

// 批量打印生成模板信息
export function batchPrint(data) {
  return request({
    url: wms + '/tray/batchPrint',
    method: 'post',
    data: data
  })
}
// 批量打印生成模板信息
export function batchPrintList(data) {
  return request({
    url: wms + '/tray/batchPrintList',
    method: 'post',
    data: data
  })
}
