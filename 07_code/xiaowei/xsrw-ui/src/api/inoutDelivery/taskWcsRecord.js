import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询列表
export function listTaskWcsRecord(query) {
  return request({
    url:  wms + '/inout/wcsRecord/list',
    method: 'get',
    params: query
  })
}
// 查询任务列表
export function listTaskRecord(query) {
  return request({
    url:  wms + '/inout/wcsRecord/getTaskNoList',
    method: 'get',
    params: query
  })
}
// 查询载具的出库/回库任务执行记录
export function getListByTray(query) {
  return request({
    url:  wms + '/inout/wcsRecord/getListByTray',
    method: 'get',
    params: query
  })
}



// 载具出入库强制执行
export function executeTray(data) {
  return request({
    url:  wms + '/inout/task/executeTray',
    method: 'post',
    data: data
  })
}


