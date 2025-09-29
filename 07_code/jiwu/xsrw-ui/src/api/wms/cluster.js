import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询远程仓库集群列表
export function listCluster(query) {
  return request({
    url:  wms + '/cluster/list',
    method: 'get',
    params: query
  })
}

// 查询远程仓库集群详细
export function getCluster(id) {
  return request({
    url: wms + '/cluster/' + id,
    method: 'get'
  })
}

// 新增远程仓库集群
export function addCluster(data) {
  return request({
    url: wms + '/cluster',
    method: 'post',
    data: data
  })
}

// 修改远程仓库集群
export function updateCluster(data) {
  return request({
    url: wms + '/cluster',
    method: 'put',
    data: data
  })
}

// 删除远程仓库集群
export function delCluster(id) {
  return request({
    url: wms + '/cluster/' + id,
    method: 'delete'
  })
}

// 刷新状态
export function refreshCluster() {
  return request({
    url: wms + '/cluster/detectAll',
    method: 'get'
  })
}
