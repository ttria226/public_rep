import request from '@/utils/request'


// 查询巡检计划列表
export function listPlan(query) {
  return request({
    url: '/wms/plan/list',
    method: 'get',
    params: query
  })
}

// 查询巡检计划详细
export function getPlan(id) {
  return request({
    url:'/wms/plan/' + id,
    method: 'get'
  })
}

// 新增巡检计划
export function addPlan(data) {
  return request({
    url:'/wms/plan',
    method: 'post',
    data: data
  })
}

// 修改巡检计划
export function updatePlan(data) {
  return request({
    url:'/wms/plan',
    method: 'put',
    data: data
  })
}

// 删除巡检计划
export function delPlan(id) {
  return request({
    url: '/wms/plan/' + id,
    method: 'delete'
  })
}
