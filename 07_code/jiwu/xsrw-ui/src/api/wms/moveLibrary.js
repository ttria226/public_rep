import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询库内移位列表
export function listMoveLibrary(query) {
  return request({
    url:  wms + '/moveLibraryNew/list',
    method: 'get',
    params: query
  })
}

// 查询库内移位详细
export function getMoveLibraryNew(code) {
  return request({
    url: wms + '/moveLibraryNew/details?code=' + code,
    method: 'get'
  })
}
// 查询库内移位详细
export function getMoveLibrary(id) {
  return request({
    url: wms + '/moveLibrary/' + id,
    method: 'get'
  })
}

// 新增库内移位
export function addMoveLibrary(data) {
  return request({
    url: wms + '/moveLibrary',
    method: 'post',
    data: data
  })
}

// 修改库内移位
export function updateMoveLibrary(data) {
  return request({
    url: wms + '/moveLibrary',
    method: 'put',
    data: data
  })
}

// 删除库内移位
export function delMoveLibrary(id) {
  return request({
    url: wms + '/moveLibrary/' + id,
    method: 'delete'
  })
}

// 库内移位审核
export function auditMoveLibrary(id) {
  return request({
    url: wms + '/moveLibrary/auditor/' + id,
    method: 'get'
  })
}

// 生成移库任务
export function moveLibrary(id) {
  return request({
    url: wms + '/moveLibrary/move/' + id,
    method: 'get'
  })
}
