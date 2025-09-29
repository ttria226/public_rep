import request from '@/utils/request'


// 查询巡检标准列表
export function listItems(query) {
  return request({
    url: '/wms/items/list',
    method: 'get',
    params: query
  })
}

// 查询巡检标准详细
export function getItems(id) {
  return request({
    url: '/wms/items/' + id,
    method: 'get'
  })
}

// 新增巡检标准
export function addItems(data) {
  return request({
    url: '/wms/items',
    method: 'post',
    data: data
  })
}

// 修改巡检标准
export function updateItems(data) {
  return request({
    url: '/wms/items',
    method: 'put',
    data: data
  })
}

// 删除巡检标准
export function delItems(id) {
  return request({
    url: '/wms/items/' + id,
    method: 'delete'
  })
}
