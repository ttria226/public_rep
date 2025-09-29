import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询供应商质量统计报表列表
export function listSupplierQualityReport(query) {
  return request({
    url: wms+'/report/center/qualityReport/list',
    method: 'get',
    params: query
  })
}

// 查询供应商交付统计报表列表
export function listSupplierQeliveryReport(query) {
  return request({
    url: wms+'/report/center/deliveryReport/list',
    method: 'get',
    params: query
  })
}