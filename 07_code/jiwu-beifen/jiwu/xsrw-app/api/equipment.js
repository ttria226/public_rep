import request from '@/utils/request'

// 获取用户信息列表
export function getPollingList() {
  return request({
    'url': '/wms/day/listApp',
    'method': 'get'
  })
}
// 获取用户信息详情
export function getPollingDetail(data) {
  return request({
    'url': '/wms/day/getInfoApp',
    'method': 'get',
	 params: data
  })
}
//完成
export function getPollingSubmit(data) {
  return request({
    'url': '/day/addDayInfo',
    'method': 'post',
	 data: data
  })
}

//获取巡检详情
export function getPatrolEduDetail(data) {
  return request({
    'url': '/wms/day/getDayInfo',
    'method': 'get',
	 params: data
  })
}