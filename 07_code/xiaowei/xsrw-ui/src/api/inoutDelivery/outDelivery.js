import request from '@/utils/request'
import {wms} from '@/utils/agent'

// 查询出库计划列表
export function listOutDelivery(query) {
  return request({
    url:  wms + '/deliveryOut/list',
    method: 'get',
    params: query
  })
}

// 查询出库计划详细
export function getOutDelivery(id) {
  return request({
    url: wms + '/deliveryOut/' + id,
    method: 'get'
  })
}

// 新增出库计划
export function addOutDelivery(data) {
  return request({
    url: wms + '/deliveryOut',
    method: 'post',
    data: data
  })
}

// 修改出库计划
export function updateOutDelivery(data) {
  return request({
    url: wms + '/deliveryOut',
    method: 'put',
    data: data
  })
}

// 删除出库计划
export function delOutDelivery(id) {
  return request({
    url: wms + '/deliveryOut/' + id,
    method: 'delete'
  })
}

// 审核出库计划
export function approveOutDelivery(data) {
  return request({
    url: wms + '/deliveryOut/approve',
    method: 'post',
    data: data
  })
}

// 生成出库任务
export function getOutDeliveryTask(query) {
  return request({
    url: wms + '/deliveryOut/toOutTask',
    method: 'get',
    params: query
  })
}

//获取物料列表
export function getOutMaterialSelectList(query) {
  return request({
    url:  wms + '/deliveryOut/getMaterialSelectList',
    method: 'get',
    params: query
  })
}

// 查询波次计划列表
export function listMergeDelivery(query) {
  return request({
    url:  wms + '/mergeDelivery/list',
    method: 'get',
    params: query
  })
}

// 获取波次管理的出库计划列表
export function listMergeOutDelivery(query) {
  return request({
    url:  wms + '/deliveryOut/merge/list',
    method: 'get',
    params: query
  })
}

// 查询波次计划详情
export function getMergeDeliveryDetail(id) {
  return request({
    url: wms + '/mergeDelivery/'+id,
    method: 'get'
  })
}

// 创建波次计划
export function addMergeDelivery(query) {
  return request({
    url:  wms + '/mergeDelivery/create',
    method: 'get',
    params: query
  })
}

// 查询波次分配列表
export function listMergeDeliveryTask(query) {
  return request({
    url:  wms + '/mergeDeliveryDetail/list',
    method: 'get',
    params: query
  })
}

// 查询波次分配详情
export function getMergeDeliveryTaskDetail(query) {
  return request({
    url: wms + '/mergeTask/list',
    method: 'get',
    params: query
  })
}

// 查询波次拣货详情
export function getMergeDeliveryControlDetail(id) {
  return request({
    url: wms + '/mergeTask/'+id,
    method: 'get'
  })
}

//获取出库执行列表
export function listOutDeliveryTask(query) {
  return request({
    url:  wms + '/deliveryOut/outTasklist',
    method: 'get',
    params: query
  })
}

// 查询出库执行详细
export function getOutDeliveryTaskDetail(id,query) {
  return request({
    url: wms + '/out/' + id,
    method: 'get',
    params: query
  })
}

// 查询出库执行的查看
export function getOutDeliveryTaskShowDetail(id,query) {
  return request({
    url: wms + '/out/detail/' + id,
    method: 'get',
    params: query
  })
}

// 查询出库执行的选择载具列表
export function getOutDeliveryTraylist(query) {
  return request({
    url: wms + '/out/traylist',
    method: 'get',
    params: query
  })
}

// 查询出库执行的地堆载具列表
export function getOutDeliveryFloorDisplayTraylist(query) {
  return request({
    url: wms + '/out/groundPileTrayList',
    method: 'get',
    params: query
  })
}

// 删除出库执行
export function delInDeliveryTask(id) {
  return request({
    url: wms + '/deliveryOut/removeTasks/' + id,
    method: 'delete'
  })
}

// 执行出库
export function executeOutDelivery(data) {
  return request({
    url: wms + '/out',
    method: 'post',
    data: data
  })
}

// 波次分配-分配
export function executeMergeOutDelivery(data) {
  return request({
    url: wms + '/mergeDelivery/addTask',
    method: 'post',
    data: data
  })
}

// 波次分配-地堆拣货
export function executeMergeOutFloorDisplayDelivery(data) {
  return request({
    url: wms + '/mergeDelivery/addTaskPile',
    method: 'post',
    data: data
  })
}

// 地堆拣货
export function executeOutFloorDisplayDelivery(data) {
  return request({
    url: wms + '/out/groundPileOutbound',
    method: 'post',
    data: data
  })
}

// 强制执行出库
export function enforcementOutDelivery(data) {
  return request({
    url: wms + '/out/executeOutTask',
    method: 'post',
    data: data
  })
}


// 重新发送
export function enforcementDeliveryOut(data) {
  return request({
    url: wms + '/inout/task/enforcementDelivery/out',
    method: 'post',
    data: data
  })
}

// 强制回库
export function forcedBack(data) {
  return request({
    url: wms + '/tray/recycleOut',
    method: 'post',
    data: data
  })
}


// 查询出库物料rfid列表
export function getOutDeliveryMaterialRfidList(query) {
  return request({
    url: wms + '/out/materialRfidList',
    method: 'get',
    params: query
  })
}

// 波次拣货-强制执行
export function enforcementMergeOutDelivery(data) {
  return request({
    url: wms + '/mergeDelivery/executeOutTask',
    method: 'post',
    data: data
  })
}

// 获取小件领取记录列表
export function getOutDeliverySmallRecordList(query) {
  return request({
    url: wms + '/deliveryOut/smallRecordList',
    method: 'get',
    params: query
  })
}

// 获取移库监控的详情
export function getMoveDeliveryDetail(query) {
  return request({
    url: wms + '/inout/task/getShiftDetail',
    method: 'get',
    params: query
  })
}

// 移库监控强制执行
export function executeMoveDelivery(query) {
  return request({
    url: wms + '/inout/task/updateStock',
    method: 'get',
    params: query
  })
}

// 移库（双伸位）强制执行
export function executeMoveDoubleEx(data) {
  return request({
    url: wms + '/inout/task/executeMoveDoubleEx',
    method: 'post',
    data: data
  })
}

// 重新发送命令（双伸位）
export function enforcementMoveDelivery(data) {
  return request({
    url: wms + '/inout/task/enforcementMove',
    method: 'post',
    data: data
  })
}

// 移库监控作废
export function cancelMoveDelivery(query) {
  return request({
    url: wms + '/inout/task/move/cancellation',
    method: 'get',
    params: query
  })
}

// 获取应急出库的库位列表
export function getEmergencyOutStockList(query) {
  return request({
    url: wms + '/stock/getStockByMaterialList',
    method: 'get',
    params: query
  })
}

// 获取出库单（新增）列表
export function listOutDeliveryOrder(query) {
  return request({
    url: wms + '/inout/removal/list',
    method: 'get',
    params: query
  })
}

// 获取出库复核列表
export function listOutDeliveryCheck(query) {
  return request({
    url: wms + '/inout/recheck/list',
    method: 'get',
    params: query
  })
}

// 新增出库复核
export function addOutDeliveryCheck(data) {
  return request({
    url: wms + '/inout/recheck',
    method: 'post',
    data: data
  })
}

// 出库复核审核/作废
export function updateOutDeliveryCheckStatus(data) {
  return request({
    url: wms + '/inout/recheck/updateStatus',
    method: 'post',
    data: data
  })
}

// 查询出库复核详情
export function getOutDeliveryCheck(id) {
  return request({
    url: wms + '/inout/recheck/' + id,
    method: 'get'
  })
}

// 获取出库完成的出库单列表
export function getOutDeliverySelectList(query) {
  return request({
    url: wms + '/deliveryOut/selectList',
    method: 'get',
    params: query
  })
}

// 获取发货单列表
export function listOutDeliverySend(query) {
  return request({
    url: wms + '/inout/shipments/list',
    method: 'get',
    params: query
  })
}

// 新增发货单
export function addOutDeliverySend(data) {
  return request({
    url: wms + '/inout/shipments',
    method: 'post',
    data: data
  })
}

// 查询发货单详情
export function getOutDeliverySend(id) {
  return request({
    url: wms + '/inout/shipments/' + id,
    method: 'get'
  })
}

// 获取已审核通过的复核单列表
export function getOutDeliveryCheckSelectList(query) {
  return request({
    url: wms + '/inout/recheck/selectList',
    method: 'get',
    params: query
  })
}

// 发货审核/作废
export function updateOutDeliverySendStatus(data) {
  return request({
    url: wms + '/inout/shipments/updateStatus',
    method: 'post',
    data: data
  })
}

// 获取发货退货单列表
export function listOutDeliverySendReturn(query) {
  return request({
    url: wms + '/inout/removal/list',
    method: 'get',
    params: query
  })
}

// 新增发货退货单
export function addOutDeliverySendReturn(data) {
  return request({
    url: wms + '/inout/removal',
    method: 'post',
    data: data
  })
}

// 查询发货退货单详情
export function getOutDeliverySendReturn(id) {
  return request({
    url: wms + '/inout/removal/' + id,
    method: 'get'
  })
}

// 获取未发货的发货单列表
export function getOutDeliverySendSelectList(query) {
  return request({
    url: wms + '/inout/shipments/selectList',
    method: 'get',
    params: query
  })
}

// 发货退货的退货
export function updateOutDeliverySendReturnStatus(data) {
  return request({
    url: wms + '/inout/removal/returnStatus',
    method: 'post',
    data: data
  })
}

// 获取齐套出库列表
export function listOutDeliveryComplete(query) {
  return request({
    url: wms + '/deliveryOut/suit/list',
    method: 'get',
    params: query
  })
}

// 新增齐套出库计划
export function addOutDeliveryComplete(data) {
  return request({
    url: wms + '/deliveryOut/suit/add',
    method: 'post',
    data: data
  })
}

//齐套出库-获取物料库存列表
export function getOutMaterialStockSelectList(query) {
  return request({
    url:  wms + '/deliveryOut/suit/materialList',
    method: 'get',
    params: query
  })
}

//齐套出库-出库分配
export function executeOutDeliveryComplete(data) {
  return request({
    url:  wms + '/deliveryOut/suit/addTask',
    method: 'post',
    data: data
  })
}

//齐套出库-地堆拣货
export function executeOutFloorDisplayDeliveryComplete(data) {
  return request({
    url:  wms + '/deliveryOut/suit/addTaskPile',
    method: 'post',
    data: data
  })
}

//自动分配-获取载具列表
export function getOutDeliveryAutoTraylist(query) {
  return request({
    url:  wms + '/out/traylist/voluntarily',
    method: 'get',
    params: query
  })
}

//地堆自动拣货-获取载具列表
export function getOutDeliveryFloorDisplayAutoTraylist(query) {
  return request({
    url:  wms + '/out/groundPileTrayList/voluntarily',
    method: 'get',
    params: query
  })
}

// 波次分配-自动分配-获取载具列表
export function getMergeOutDeliveryAutoTraylist(query) {
  return request({
    url: wms + '/mergeTask/traylist/voluntarily',
    method: 'get',
    params: query
  })
}

// 波次分配-地堆自动拣货-获取载具列表
export function getMergeOutDeliveryFloorDisplayAutoTraylist(query) {
  return request({
    url: wms + '/mergeTask/groundPileTrayList/voluntarily',
    method: 'get',
    params: query
  })
}

// 拣货任务-作废
export function cancelTaskOutDelivery(query) {
  return request({
    url: wms + '/inout/task/out/cancellation',
    method: 'get',
    params: query
  })
}

// 查询快捷出库列表
export function listOutDeliveryQuick(query) {
  return request({
    url:  wms + '/deliveryOut/quick/list',
    method: 'get',
    params: query
  })
}

//快捷出库-执行自动出库-获取载具列表
export function getOutDeliveryQuickAutoTraylist(query) {
  return request({
    url:  wms + '/deliveryOut/quick/traylist/voluntarily/show',
    method: 'get',
    params: query
  })
}

//快捷出库-执行自动出库-获取执行出库提交数据
export function getOutDeliveryQuickAutoSubmitData(query) {
  return request({
    url:  wms + '/deliveryOut/quick/traylist/voluntarily/submit',
    method: 'get',
    params: query
  })
}

// 快捷出库-执行自动出库
export function executeOutDeliveryQuick(data) {
  return request({
    url: wms + '/deliveryOut/quick/execute',
    method: 'post',
    data: data
  })
}

// 删除出库单-根据编号
export function delOutDeliveryByCode(data) {
  return request({
    url: wms + '/deliveryOut/delAll/',
    method: 'post',
    data: data
  })
}
