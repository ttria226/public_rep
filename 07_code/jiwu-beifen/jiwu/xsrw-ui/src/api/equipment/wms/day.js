import request from '@/utils/request'

// 查询巡检记录列表
export function listDay(query) {
  return request({
    url: '/wms/day/list',
    method: 'get',
    params: query
  })
}

// 查询巡检记录详细
export function getDay(id) {
  return request({
    url: '/wms/day/' + id,
    method: 'get'
  })
}

// 新增巡检记录
export function addDay(data) {
  return request({
    url: '/wms/day',
    method: 'post',
    data: data
  })
}

// 修改巡检记录
export function updateDay(data) {
  return request({
    url: '/wms/day',
    method: 'put',
    data: data
  })
}

// 删除巡检记录
export function delDay(id) {
  return request({
    url: '/wms/day/' + id,
    method: 'delete'
  })
}
