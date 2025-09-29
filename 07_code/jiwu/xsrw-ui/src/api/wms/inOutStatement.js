import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询入出库流水报表列表
export function listInOutStatement(query) {
  return request({
    url: wms+'/report/center/inOutStatement/list',
    method: 'get',
    params: query
  })
}

// 查询客户列表
export function getContactsUnitAllList(query) {
  return request({
    url: wms+'/contacts/unit/getAll',
    method: 'get',
    params: query
  })
}