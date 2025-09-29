import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询入库单列表
export function listInDelivery(query) {
  return request({
    url:  wms + '/inout/delivery/list',
    method: 'get',
    params: query
  })
}

// 查询入库单详情
export function getInDelivery(id) {
  return request({
    url: wms + '/inout/delivery/' + id,
    method: 'get'
  })
}

// 新增入库单
export function addInDelivery(data) {
  return request({
    url: wms + '/inout/delivery',
    method: 'post',
    data: data
  })
}

// 修改入库单
export function updateInDelivery(data) {
  return request({
    url: wms + '/inout/delivery',
    method: 'put',
    data: data
  })
}

// 删除入库单
export function delInDelivery(id) {
  return request({
    url: wms + '/inout/delivery/' + id,
    method: 'delete'
  })
}

// 审核入库单
export function approveInDelivery(data) {
  return request({
    url: wms + '/inout/delivery/approve',
    method: 'post',
    data: data
  })
}

// 查询检测的物料筛选下拉列表
export function getTestRegistrationSelect(id) {
  return request({
    url:  wms + '/inout/delivery/getRegistrationInfo/'+id,
    method: 'get'
  })
}

// 查询检测的物料列表
export function getTestRegistrationList(id,query) {
  return request({
    url:  wms + '/inout/detail/getRegistrationList/'+id,
    method: 'get',
    params: query
  })
}

// 物料检测失败
export function checkMaterialFail(data) {
  return request({
    url: wms + '/inout/detail/checkMaterial',
    method: 'post',
    data: data
  })
}

// 物料检测完成
export function checkDelivery(data) {
  return request({
    url: wms + '/inout/delivery/checkDelivery',
    method: 'post',
    data: data
  })
}

// 检测入库单
export function checkInDelivery(data) {
  return request({
    url: wms + '/inout/delivery/check',
    method: 'post',
    data: data
  })
}

// 登记入库单
export function registerInDelivery(data) {
  return request({
    url: wms + '/inout/delivery/registerDelivery', //  /inout/delivery/register
    method: 'post',
    data: data
  })
}

//获取物料列表
export function getInMaterialSelectList(query) {
  return request({
    url:  wms + '/inout/delivery/getMaterialSelectList',
    method: 'get',
    params: query
  })
}

// 获取标签模板列表
export function getTemplateSelectList(query) {
  return request({
    url:  wms + '/labelTemplate/selectList',
    method: 'get',
    params: query
  })
}

// 获取标签打印中的物料列表
export function getTemplateMaterialList(id) {
  return request({
    url: wms + '/inout/delivery/getDeatilList/' + id,
    method: 'get'
  })
}

//获取拒收列表
export function listRejection(query) {
  return request({
    url:  wms + '/inout/rejection/list',
    method: 'get',
    params: query
  })
}

//获取入库任务列表
export function listInDeliveryTask(query) {
  return request({
    url:  wms + '/inout/delivery/detail/list',
    method: 'get',
    params: query
  })
}

// 查询入库任务详情
export function getInDeliveryTask(id) {
  return request({
    url: wms + '/inout/delivery/detail/' + id,
    method: 'get'
  })
}

// 获取入库任务的载具列表
export function getInDeliveryTaskTrayList(query) {
  return request({
    url: wms + '/tray/selectList',
    method: 'get',
    params: query
  })
}

// 获取入库任务的载具列表
export function getInDeliveryTaskTrayPageList(query) {
  return request({
    url: wms + '/tray/selectPutWayList',
    method: 'get',
    params: query
  })
}

// 根据id查询推荐载具
export function getInDeliveryTaskTrayType(id) {
  return request({
    url: wms + '/material/getTrayTypeByMaterials/' + id,
    method: 'get'
  })
}

// 生成上架任务
export function putawayInDeliveryTask(data) {
  return request({
    url: wms + '/inout/delivery/detail/putaway',
    method: 'post',
    data: data
  })
}

// 入库重新组盘
export function putawayInAfreshTask(data) {
  return request({
    url: wms + '/inout/delivery/detail/afreshPutaway',
    method: 'post',
    data: data
  })
}

// 地堆上架
export function floorStockingInDeliveryTask(data) {
  return request({
    url: wms + '/inout/delivery/detail/floorStocking',
    method: 'post',
    data: data
  })
}

// 获取齐套入库的库位列表
export function getInDeliveryTaskLocationList(query) {
  return request({
    url: wms + '/location/selectList',
    method: 'get',
    params: query
  })
}

/**
 * 获取库位列表-分页
 * @param query
 */
export function getInPageLocationList(query) {
  return request({
    url: wms + '/location/selectPageList',
    method: 'get',
    params: query
  })
}

// 快捷入库-上架
export function putawayInDeliveryTaskFast(data) {
  return request({
    url: wms + '/inout/delivery/detail/putawayComplete',
    method: 'post',
    data: data
  })
}

// 齐套入库-上架
export function putawayInDeliveryTaskComplete(data) {
  return request({
    url: wms + '/inout/delivery/detail/putawayBom',
    method: 'post',
    data: data
  })
}

//获取执行列表
export function listDeliveryExecute(query) {
  return request({
    url:  wms + '/inout/task/list',
    method: 'get',
    params: query
  })
}

//获取执行列表
export function listDeliveryExecuteA(query) {
  return request({
    url:  wms + '/inout/task/listAll',
    method: 'get',
    params: query
  })
}

// 查询执行详情
export function getDeliveryExecute(id) {
  return request({
    url: wms + '/inout/task/' + id,
    method: 'get'
  })
}

// 执行任务
export function updateLocationDeliveryExecute(data) {
  return request({
    url: wms + '/inout/task/updateLocation',
    method: 'post',
    data: data
  })
}

// 强制执行
export function enforcementDelivery(data) {
  return request({
    url: wms + '/inout/task/execute',
    method: 'post',
    data: data
  })
}

//更新执行任务优先级
export function updateDeliveryExecutePriority(query) {
  return request({
    url:  wms + '/inout/task/updatePriority',
    method: 'get',
    params: query
  })
}

// 入库单/质检单作废
export function cancelInDelivery(data) {
  return request({
    url: wms + '/inout/delivery/cancellation',
    method: 'post',
    data: data
  })
}

// 入库监控作废
export function cancelTaskDelivery(data) {
  return request({
    url: wms + '/inout/task/delivery/cancellation',
    method: 'post',
    data: data
  })
}

//获取选择新增入库单和质检单的单据列表
export function selectListInDelivery(query) {
  return request({
    url:  wms + '/inout/delivery/selectList',
    method: 'get',
    params: query
  })
}

// 查询质检单列表
export function listQuality(query) {
  return request({
    url:  wms + '/inout/quality/list',
    method: 'get',
    params: query
  })
}

// 新增质检单
export function addQuality(data) {
  return request({
    url: wms + '/inout/quality',
    method: 'post',
    data: data
  })
}

// 查询质检单详情
export function getQuality(id) {
  return request({
    url: wms + '/inout/quality/' + id,
    method: 'get'
  })
}

// 质检单确认/作废
export function updateQualityStatus(data) {
  return request({
    url: wms + '/inout/quality/updateStatus',
    method: 'post',
    data: data
  })
}

// 查询入库单列表
export function listPut(query) {
  return request({
    url:  wms + '/inout/put/list',
    method: 'get',
    params: query
  })
}

// 新增入库单
export function addPut(data) {
  return request({
    url: wms + '/inout/put',
    method: 'post',
    data: data
  })
}

// 查询入库单详情
export function getPut(id) {
  return request({
    url: wms + '/inout/put/' + id,
    method: 'get'
  })
}

// 入库单确认/作废
export function updatePutStatus(data) {
  return request({
    url: wms + '/inout/put/updateStatus',
    method: 'post',
    data: data
  })
}

// 查询收货单列表
export function listCollection(query) {
  return request({
    url:  wms + '/inout/collection/list',
    method: 'get',
    params: query
  })
}

// 新增收货单
export function addCollection(data) {
  return request({
    url: wms + '/inout/collection',
    method: 'post',
    data: data
  })
}

// 查询收货单详情
export function getCollection(id) {
  return request({
    url: wms + '/inout/collection/' + id,
    method: 'get'
  })
}

// 收货单确认/作废
export function updateCollectionStatus(data) {
  return request({
    url: wms + '/inout/collection/updateStatus',
    method: 'put',
    data: data
  })
}

// 收货单退货
export function updateCollectionReturnStatus(data) {
  return request({
    url: wms + '/inout/collection/returnStatus',
    method: 'post',
    data: data
  })
}

// 重新发送命令
export function enforceDelivery(data) {
  return request({
    url: wms + '/inout/task/enforcementDelivery',
    method: 'post',
    data: data
  })
}


// 删除入库单-根据编号
export function delInDeliveryByCode(data) {
  return request({
    url: wms + '/inout/delivery/deleteByCode',
    method: 'post',
    data: data
  })
}
