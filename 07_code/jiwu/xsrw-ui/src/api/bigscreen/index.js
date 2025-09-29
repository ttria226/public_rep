import request from '@/utils/request'
import {wms,wcs,cims} from '@/utils/agent'
/**
 * @param {Function} fn 防抖函数
 * @param {Number} delay 延迟时间
 */
export function debounce(fn, delay) {
  var timer;
  return function () {
    var context = this;
    var args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
      fn.apply(context, args);
    }, delay);
  };
}

/**
 * @param {date} time 需要转换的时间
 * @param {String} fmt 需要转换的格式 如 yyyy-MM-dd、yyyy-MM-dd HH:mm:ss
 */
export function formatTime(time, fmt) {
  if (!time) return '';//没传时间返回空
  else {
    const date = new Date(time);
    const o = {
      'M+': date.getMonth() + 1,//月
      'd+': date.getDate(),//日
      'H+': date.getHours(),//时
      'm+': date.getMinutes(),//分
      's+': date.getSeconds(),//秒
      'q+': Math.floor((date.getMonth() + 3) / 3),//月+3/3
      S: date.getMilliseconds(),//返回时间的毫秒
    };
    if (/(y+)/.test(fmt))//匹配1个到多个y
    //这一步把年转换完毕
      fmt = fmt.replace(
        RegExp.$1,//拿到y+匹配到的第一个分组
        (date.getFullYear() + '').substr(4 - RegExp.$1.length)
      );
    //这一步把生下的格式继续匹配转换
    for (const k in o) {
      if (new RegExp('(' + k + ')').test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          RegExp.$1.length === 1
            ? o[k]
            : ('00' + o[k]).substr(('' + o[k]).length)
        );
      }
    }
    return fmt;
  }
}

//获取任务看板的任务数量
export function countTaskBoard(query) {
  return request({
    url:  wms + '/systemKanban/taskKanban',
    method: 'get',
    params: query
  })
}

//获取设备运行的各种数量
export function countEquipmentRunning(query) {
  return request({
    url:  wms + '/systemKanban/equipmentStatistics',
    method: 'get',
    params: query
  })
}

//获取设备信息列表
export function listEquipmentInfo(query) {
  return request({
    url:  wms + '/systemKanban/equipmentList',
    method: 'get',
    params: query
  })
}

//获取任务信息列表
export function listTaskInfo(query) {
  return request({
    url:  wms + '/systemKanban/taskList',
    method: 'get',
    params: query
  })
}

//获取任务执行情况
export function getTaskExecuteInfo(query) {
  return request({
    url:  wms + '/systemKanban/taskExecutionStatistics',
    method: 'get',
    params: query
  })
}

//获取任务状态
export function getTaskStatusInfo(query) {
  return request({
    url:  wms + '/systemKanban/taskStatusStatistics',
    method: 'get',
    params: query
  })
}

//获取物料库存TOP
export function getMaterialStockTopInfo(query) {
  return request({
    url:  wms + '/systemKanban/stockList',
    method: 'get',
    params: query
  })
}

//获取仓库使用情况
export function getWarehouseUseInfo(query) {
  return request({
    url:  wms + '/systemKanban/warehouseUseStatistics',
    method: 'get',
    params: query
  })
}

//=======================================new==============================
//获取当日数量统计
export function numStatisticDay(query) {
  return request({
    url:  wms + '/bigscreen/day/numStatistic',
    method: 'get',
    params: query
  })
}
//库存类别比例
export function categoryRatioDay(query) {
  return request({
    url:  wms + '/bigscreen/day/categoryRatio',
    method: 'get',
    params: query
  })
}
//库存位置比例
export function locationTypeRatioDay(query) {
  return request({
    url:  wms + '/bigscreen/day/locationTypeRatio',
    method: 'get',
    params: query
  })
}
//当月出入库总额
export function inOutMoneyMonth(query) {
  return request({
    url:  wms + '/bigscreen/month/inOutMoney',
    method: 'get',
    params: query
  })
}
//重点物资月入出情况
export function keyPointMaterialMonth(query) {
  return request({
    url:  wms + '/bigscreen/month/keyPointMaterial',
    method: 'get',
    params: query
  })
}
//入库数量
export function inNumberMonth(query) {
  return request({
    url:  wms + '/bigscreen/month/inNumber',
    method: 'get',
    params: query
  })
}
//出库数量
export function outNumberMonth(query) {
  return request({
    url:  wms + '/bigscreen/month/outNumber',
    method: 'get',
    params: query
  })
}
//库龄情况
export function storageAgeInfoWarn(query) {
  return request({
    url:  wms + '/bigscreen/warn/storageAgeInfo',
    method: 'get',
    params: query
  })
}
//库龄分析
export function storageAgeWarn(query) {
  return request({
    url:  wms + '/bigscreen/warn/storageAge',
    method: 'get',
    params: query
  })
}
//最低库存预警
export function minimumStock(query) {
  return request({
    url:  wms + '/bigscreen/warn/minimumStock',
    method: 'get',
    params: query
  })
}
//最高库存预警
export function maximumStock(query) {
  return request({
    url:  wms + '/bigscreen/warn/maximumStock',
    method: 'get',
    params: query
  })
}

export function getNowTime(query) {
  return request({
    url:  wms + '/bigscreen/day/getNowTime',
    method: 'get',
    params: query
  })
}
