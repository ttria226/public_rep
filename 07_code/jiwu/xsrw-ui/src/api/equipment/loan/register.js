import request from '@/utils/request'
import { wms } from "@/utils/agent";
// 查询设备借还登记列表
export function listRegister(query) {
  return request({
    url: wms + '/loan/register/list',
    method: 'get',
    params: query
  })
}

// 查询设备借还登记详细
export function getRegister(id) {
  return request({
    url: wms + '/loan/register/' + id,
    method: 'get'
  })
}

// 新增设备借还登记
export function addRegister(data) {
  return request({
    url: wms + '/loan/register',
    method: 'post',
    data: data
  })
}

// 修改设备借还登记
export function updateRegister(data) {
  return request({
    url: wms + '/loan/register',
    method: 'put',
    data: data
  })
}

// 删除设备借还登记
export function delRegister(id) {
  return request({
    url: wms + '/loan/register/' + id,
    method: 'delete'
  })
}

// 查询设备借还登记列表
export function getRegisterEquipmentList(query) {
  return request({
    url: wms + '/loan/register/getEquipmentList',
    method: 'get',
    params: query
  })
}
