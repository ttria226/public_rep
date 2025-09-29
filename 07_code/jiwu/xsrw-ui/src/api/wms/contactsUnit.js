import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询供应商列表
export function listContactsUnit(query) {
  return request({
    url: wms+'/contacts/unit/list',
    method: 'get',
    params: query
  })
}

// 查询供应商详细
export function getContactsUnit(id) {
  return request({
    url: wms+'/contacts/unit/' + id,
    method: 'get'
  })
}

// 新增供应商
export function addContactsUnit(data) {
  return request({
    url: wms+'/contacts/unit',
    method: 'post',
    data: data
  })
}

// 修改供应商
export function updateContactsUnit(data) {
  return request({
    url: wms+'/contacts/unit',
    method: 'put',
    data: data
  })
}

// 删除供应商
export function delContactsUnit(id) {
  return request({
    url: wms+'/contacts/unit/' + id,
    method: 'delete'
  })
}
