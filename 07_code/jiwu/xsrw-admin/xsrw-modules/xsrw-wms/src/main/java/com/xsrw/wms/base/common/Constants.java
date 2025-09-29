package com.xsrw.wms.base.common;

import com.xsrw.common.core.constant.CacheConstants;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/5 11:23
 */
public class Constants {

    /** 0禁用  **/
    public static final String LOCATION_STATUS_NO = "0";
    /** 1启用 **/
    public static final String LOCATION_STATUS_YES = "1";


    /** 是否标识 **/
    /** 1是  **/
    public static final String YES = "1";
    /** 0否  **/
    public static final String NO = "0";


    /** 删除标识 **/
    /** 1已删除  **/
    public static final String DEL_FLAG_YES = "1";
    /** 0未删除  **/
    public static final String DEL_FLAG_NO = "0";

    /** 编码日期规则 **/
    /** 0 无 **/
    public static final String DATE_EMPTY = "0";
    /** 1 年月日 **/
    public static final String DATE_DAY = "1";
    /** 2 年月日时分秒 **/
    public static final String DATE_SECOND = "2";
    /** 3 时间戳 **/
    public static final String DATE_MILLISECOND = "3";

    /** 编码最后拼接规则 **/
    /** 0 无 **/
    public static final String RULE_EMPTY = "0";
    /** 1 自增长 **/
    public static final String RULE_AUTO = "1";
    /** 2 随机数 **/
    public static final String RULE_ROUND = "2";


    //货位状态(0:禁用,1:无货,2:有货,3:标记出库,4:标记入库)
    public static final String LOCATION_GOODS_ALLOCATION_STATUS_0 = "0";
    public static final String LOCATION_GOODS_ALLOCATION_STATUS_1 = "1";
    public static final String LOCATION_GOODS_ALLOCATION_STATUS_2 = "2";
    public static final String LOCATION_GOODS_ALLOCATION_STATUS_3 = "3";
    public static final String LOCATION_GOODS_ALLOCATION_STATUS_4 = "4";

    /** 拣货策略类型类型 **/
    /** 先进先出  **/
    public static final String FIFO = "1";
    /** 后进先出  **/
    public static final String LIFO = "2";
    /** 随机  **/
    public static final String RANDOM = "3";
    /** 批次  **/
    public static final String BATCH = "4";


    /**批次属性-生产日期**/
    public static final String MATERIAL_BATCH_TYPE_PRODUCT_DATE = "生产日期";
    /**批次属性-入库日期**/
    public static final String MATERIAL_BATCH_TYPE_IN_DATE = "入库日期";
    /**批次属性-剩余有效期天数**/
    public static final String MATERIAL_BATCH_TYPE_DAY_COUNT = "剩余有效期天数";
    /**批次属性-制单日期**/
    public static final String MATERIAL_BATCH_TYPE_MAKE_DATE = "制单日期";


    /** 载具类型 **/
    /** 1 托盘 */
    public static final String TRAY_CATEGORY_TRAY = "1";
    /** 2 料箱 */
    public static final String TRAY_CATEGORY_WORKBIN = "2";
    /** 3 货笼 */
    public static final String TRAY_CATEGORY_CAGE = "3";


    /** 是否打印条码 **/
    /** 0 否  **/
    public static final String LOCATION_LABELTEMPLATETYPE_NO = "0";
    /** 1 是  **/
    public static final String LOCATION_LABELTEMPLATETYPE_YES = "1";


    /** 载具状态 **/
    /** 空闲 **/
    public static final String TRAY_STATUS_LEISURE = "0";
    /** 半托 **/
    public static final String TRAY_STATUS_HALF = "1";
    /** 全托 **/
    public static final String TRAY_STATUS_FULL = "2";

    /** 载具创建类型 **/
    /** 手动创建 **/
    public static final String TRAY_CATEGORY_MANUAL = "5";
    /** 系统导入 **/
    public static final String TRAY_CATEGORY_SYSTEM = "6";
    /** 系统生成 **/
    public static final String TRAY_CATEGORY_CREATE = "7";

    /** 冻结状态标识 **/
    /** 0解冻  **/
    public static final String STOCK_IS_FREEZE_NO = "0";
    /** 1冻结  **/
    public static final String STOCK_IS_FREEZE_YES = "1";


   /** 库存是否可用 **/
    /** 库存不可用  **/
    public static final String STOCK_USE_NO = "1";
    /** 库存可用  **/
    public static final String STOCK_USE_YES = "0";


    /** 冻结类型标识 **/
    /** 1手动冻结  **/
    public static final String STOCK_ORIGIN_TYPE_MANUAL = "1";
    /** 2盘点计划  **/
    public static final String STOCK_ORIGIN_TYPE_CHECK = "2";
    /** 3出库计划  **/
    public static final String STOCK_ORIGIN_TYPE_OUT = "3";
    /** 4库内移位  **/
    public static final String STOCK_ORIGIN_TYPE_MOVE = "4";



    /** 入库单来源类型 **/
    /** 0 预约单  **/
    public static final String INOUT_DELIVERY_MODULE_ORDER = "0";
    /** 1 齐套入库  **/
    public static final String INOUT_DELIVERY_MODULE_COMPLETE = "1";
    /** 2 快捷入库  **/
    public static final String INOUT_DELIVERY_MODULE_FAST = "2";
    /** 3 调拨单  **/
    public static final String INOUT_DELIVERY_MODULE_ALLOT = "3";


    /** 出入库状态 **/
    /** 1 待审核  **/
    public static final String INOUT_STATUS_WAITING = "1";
    /** 2 审核通过  **/
    public static final String INOUT_STATUS_PASS = "2";
    /** 3 已检测  **/
    public static final String INOUT_STATUS_CHECKED = "3";
    /** 4 已登记  **/
    public static final String INOUT_STATUS_REGISTER = "4";
    /** 5 部分登记  **/
    public static final String INOUT_STATUS_REGISTER_PART = "5";
    /** 6 部分退货  **/
    public static final String INOUT_STATUS_RETURN_PART = "6";
    /** 7 全部退货  **/
    public static final String INOUT_STATUS_RETURN = "7";
    /** 8 已作废（出库计划使用）  **/
    public static final String INOUT_STATUS_REGISTER_ABOLISH = "8";
    /** 9 审核不通过(已作废)  **/
    public static final String INOUT_STATUS_FAILED = "9";
    /** 10部分上架  **/
    public static final String INOUT_STATUS_COMPLETE_PART = "10";
    /** 11已上架  **/
    public static final String INOUT_STATUS_COMPLETE_END = "11";

    /** 出入库完成状态 **/
    /** 1 未完成  **/
    public static final String INOUT_STATUS_NOT = "1";
    /** 2 部分完成 **/
    public static final String INOUT_STATUS_PART = "2";
    /** 3 已完成  **/
    public static final String INOUT_STATUS_END = "3";

    /** 入库单详情表转化状态 **/
    /** 0 未完成  **/
    public static final String INOUT_NEXTFLAG_NOT = "0";
    /** 1 已检测  **/
    public static final String INOUT_NEXTFLAG_CHECKED = "1";
    /** 2 已上架  **/
    public static final String INOUT_NEXTFLAG_PUT = "2";
    /** 3 部分执行  **/
    public static final String INOUT_NEXTFLAG_EXE_PART = "3";
    /** 4 全部执行  **/
    public static final String INOUT_NEXTFLAG_EXE_END = "4";
    /** 5 已作废  **/
    public static final String INOUT_NEXTFLAG_ABOLISH = "5";


    /** 1 已转为出库任务  **/
    public static final String INOUT_NEXT_FLAG_YES = "1";
    /** 0 待转为出库任务  **/
    public static final String INOUT_NEXT_FLAG_WAIT = "0";
    /** 任务 状态 **/
    /** 0未执行  **/
    public static final String TASK_STATUS_NO = "0";
    /** 1执行中(部分完成)  **/
    public static final String TASK_STATUS_ING = "1";
    /** 2执行完成/待审核 **/
    public static final String TASK_STATUS_END = "2";
    /** 3审核中 **/
    public static final String TASK_STATUS_APPROVE_ING = "3";
    /** 4已审核 **/
    public static final String TASK_STATUS_APPROVED = "4";
    /** 7 执行中**/
    public static final String TASK_STATUS_EXECUTING = "7";
    /** 8 已作废 **/
    public static final String TASK_STATUS_CANCELLATION = "8";

    /** 越库单状态 **/
    /** 1 待收货  **/
    public static final String OVERSTOCK_STATUS_WAITING = "1";
    /** 2 部分收货  **/
    public static final String OVERSTOCK_STATUS_REGISTER_PART = "2";
    /** 3 已收货  **/
    public static final String OVERSTOCK_STATUS_REGISTER = "3";
    /** 4 部分出库  **/
    public static final String OVERSTOCK_STATUS_OUT_PART = "4";
    /** 5 已出库  **/
    public static final String OVERSTOCK_STATUS_OUT = "5";


    /** 入库物料检测状态 **/
    /** 0未检测  **/
    public static final String MATERIAL_DETAIL_CHECK_NO = "0";
    /** 1检测成功  **/
    public static final String MATERIAL_DETAIL_CHECK_SUCESS = "1";
    /** 2检测失败  **/
    public static final String MATERIAL_DETAIL_CHECK_FAIL = "2";

    /** WCS类型标识 **/
    /** 1入库  **/
    public static final String WCS_TASK_TYPE_IN = "1";
    /** 2出库  **/
    public static final String WCS_TASK_TYPE_OUT = "2";
    /** 4移库  **/
    public static final String WCS_TASK_TYPE_MOVE = "5";
    /** 7波次出库  **/
    public static final String WCS_TASK_TYPE_MERGE = "7";

    /** WCS入库单来源 **/
    /** 1本地  **/
    public static final String DELIVERY_IN_TYPE_LOCAL = "1";
    /** 2ERP  **/
    public static final String DELIVERY_IN_TYPE_ERP = "2";
    /** 3调拨  **/
    public static final String DELIVERY_IN_TYPE_ALLOT = "3";
    /** 4 BOM  **/
    public static final String DELIVERY_IN_TYPE_BOM = "4";

    /** wcs任务执行状态 **/
    /** 1 未执行  **/
    public static final String WCS_EXECUTE_STATUS_NOT = "1";
    /** 2 执行中 **/
    public static final String WCS_EXECUTE_STATUS_ING = "2";
    /** 3 已完成  **/
    public static final String WCS_EXECUTE_STATUS_END = "3";
    /** 4 执行失败  **/
    public static final String WCS_EXECUTE_STATUS_FAIL = "4";
    /** 5 已作废 **/
    public static final String WCS_EXECUTE_STATUS_CANCELLATION = "5";


    /** 任务类型  **/
    /** 1 上架任务  **/
    public static final String TASK_TYPE_PUT = "1";
    /** 2 拣货任务 **/
    public static final String TASK_TYPE_PICK = "2";
    /** 3 盘点任务 **/
    public static final String TASK_TYPE_CHECK = "3";
    /** 4 回库任务 **/
    public static final String TASK_TYPE_BACK = "4";
    /** 5 移库任务 **/
    public static final String TASK_TYPE_MOVE = "5";
    /** 6 托盘取出 **/
    public static final String TASK_TYPE_OUT = "6";

    /** 任务子表 执行状态 **/
    /** 0未执行（未拣货）  **/
    public static final String TASK_DETAIL_STATUS_NO = "0";
    /** 1执行中（部分拣货）  **/
    public static final String TASK_DETAIL_STATUS_ING = "1";
    /** 2已完成  **/
    public static final String TASK_DETAIL_STATUS_END = "2";
    /** 3审核中  **/
    public static final String TASK_DETAIL_STATUS_APPROVE_ING = "3";
    /** 4已审核  **/
    public static final String TASK_DETAIL_STATUS_APPROVED = "4";
    /** 5已驳回  **/
    public static final String TASK_DETAIL_STATUS_REJECT = "5";
    /** 6任务失败 **/
    public static final String TASK_DETAIL_STATUS_FAILD = "6";
    /** 7 已作废 **/
    public static final String TASK_DETAIL_STATUS_CANCELLATION = "7";

    /** 盘点计划维度 **/
    /** 1物料 **/
    public static final String CHECK_DELIVERY_MATERIAL = "1";
    /** 2库区 **/
    public static final String CHECK_DELIVERY_LOCATION = "2";
    /** 3动碰 **/
    public static final String CHECK_DELIVERY_HISTORY = "3";
    /** 4随机 **/
    public static final String CHECK_DELIVERY_RNADOM = "4";
    /** 5空库位 **/
    public static final String CHECK_DELIVERY_EMPTY = "5";
    /** 6直接盘点 **/
    public static final String CHECK_DELIVERY_DIRECT = "6";

    /** 盘点载具类型 **/
    /** 1托盘 **/
    public static final String CHECK_TRAY_TYPE_TRAY = "1";
    /** 2料箱 **/
    public static final String CHECK_TRAY_TYPE_BOX = "2";
    /** 3地堆 **/
    public static final String CHECK_TRAY_TYPE_LAND = "3";

    /** 回库类型 **/
    /** 1 普通回库 **/
    public static final String BACK_TYPE_OTHER = "1";
    /** 2 盘点回库 **/
    public static final String BACK_TYPE_CHECK = "2";

    /** 拣货任务类型  出库计划、波次计划 **/
    /** 1出库计划  **/
    public static final Integer PICK_TASK_TYPE_DELIVERY = 1;
    /** 2波次计划 **/
    public static final Integer PICK_TASK_TYPE_MERGE = 2;
    /** 3 空盘上架 **/
    public static final Integer PICK_TASK_TYPE_EMPTY = 3;
    /** 4 物料盘点 **/
    public static final Integer PICK_TASK_TYPE_CHECKDELIVERY_MATERIAL = 4;
    /** 5 库区盘点 **/
    public static final Integer PICK_TASK_TYPE_CHECKDELIVERY_LOCATION = 5;
    /** 6 区域盘点 **/
    public static final Integer PICK_TASK_TYPE_CHECKDELIVERY_AREA = 6;

    /** 平库盘点记录审核状态 **/
    /** 1 未确认 **/
    public static final String CHECK_AREA_NO = "2";
    /** 2 已驳回 **/
    public static final String CHECK_AREA_REJECT = "5";
    /** 3 已确认 **/
    public static final String CHECK_AREA_CONFIRM = "4";

    /** 出库计划 审核状态 **/
    /** 0未审核  **/
    public static final String OUTDELIVERY_AUDITOR_STATUS_NO = "0";
    /** 1已审核 **/
    public static final String OUTDELIVERY_AUDITOR_STATUS_YES = "1";

    /**出入库单据完成状态*/
    /**0 未完成*/
    public static final String INOUT_FORM_STATUS_NOT = "0";
    /**1 已完成*/
    public static final String INOUT_FORM_STATUS_PART = "1";
    /**2 已作废*/
    public static final String INOUT_FORM_STATUS_END = "2";

    /**出库单完成状态*/
    /**未完成*/
    public  static  final String OUT_DELIVERY_COMPLETE_STATE_NOT="1";
    /**部分完成*/
    public  static  final String OUT_DELIVERY_COMPLETE_STATE_PART="2";
    /**已完成*/
    public  static  final String OUT_DELIVERY_COMPLETE_STATE_COMPLETED="3";

    /**已出库*/
    public  static  final String OUT_REMOVAL_STATUS_OUTBOUND="1";
    /**已作废*/
    public  static  final String OUT_REMOVAL_STATUS_CANCEL="2";
    /**已确认*/
    public  static  final String OUT_REMOVAL_STATUS_CONFIRM="3";

    /**规则管理类型*/
    /**1有效期*/
    public static final String RULE_MODULE_VALIDITY = "1";
    /**2分配规则*/
    public static final String RULE_MODULE_ALLOCATION = "2";
    /**3拣选规则*/
    public static final String RULE_MODULE_SELECT = "3";
    /**4补货规则*/
    public static final String RULE_MODULE_REPLENISHMENT = "4";
    /**5波次规则*/
    public static final String RULE_MODULE_WAV = "5";
    /**6超收规则*/
    public static final String RULE_MODULE_OVERCHARGE = "6";

    /**
     * 呆滞品预定时间  cache key
     */
    public static final String DZP_DATE = CacheConstants.SYS_CONFIG_KEY + "wms.dzpdate";
    /**
     * 系统邮箱地址  cache key
     */
    public static final String EMAIL_ADDRESS = CacheConstants.SYS_CONFIG_KEY + "wms.email.address";
    /**
     * 系统邮箱密码  cache key
     */
    public static final String EMAIL_PWD = CacheConstants.SYS_CONFIG_KEY + "wms.email.pwd";

    /**
     * 标签打印机ip地址  cache key
     */
    // 一层
    public static final String PRINT_IP_ONE_FLOOR = CacheConstants.SYS_CONFIG_KEY + "wms.print.onefloor.ip";
    // 二层
    public static final String PRINT_IP_TWO_FLOOR = CacheConstants.SYS_CONFIG_KEY + "wms.print.twofloor.ip";
    /**
     * 标签打印机端口  cache key
     */
    public static final String PRINT_PORT = CacheConstants.SYS_CONFIG_KEY + "wms.print.port";


    /** 调拨单状态 1待审核、2等待出库、3等待入库、4已完成、5审核驳回**/
    public static final String ALLOT_STATUS_CHECK = "1";
    public static final String ALLOT_STATUS_OUT = "2";
    public static final String ALLOT_STATUS_IN = "3";
    public static final String ALLOT_STATUS_SUCCESS = "4";
    public static final String ALLOT_STATUS_FAIL = "5";

    /** 物料详情状态**/
    /**0新增*/
    public static final String MATERIAL_DETAIL_STATUS_ADD = "0";
    /**4已组盘未入库*/
    public static final String MATERIAL_DETAIL_STATUS_IN_NO = "4";
    /**1入库*/
    public static final String MATERIAL_DETAIL_STATUS_IN = "1";
    /**2已出库未复核*/;
    public static final String MATERIAL_DETAIL_STATUS_OUT_NO = "2";
    /**3已出库已复核*/
    public static final String MATERIAL_DETAIL_STATUS_OUT = "3";

    /**
     * 看板时间范围类型 日 月 年
     */
    public static final String KANBAN_TIME_SCOPE_DAY = "日";
    public static final String KANBAN_TIME_SCOPE_MONTH = "月";
    public static final String KANBAN_TIME_SCOPE_YEAR = "年";

    /** 库位类型 0其他 1地堆**/
    /**0其他*/
    public static final String LOCATION_TYPE_DEFAULT = "0";
    /**1地堆*/
    public static final String LOCATION_TYPE_FLOOR = "1";

    /** 库位楼层 1一楼 2二楼**/
    /**1一楼*/
    public static final String LOCATION_FLOOR_FIRST = "1";
    /**2二楼*/
    public static final String LOCATION_FLOOR_SECOND = "2";


    /** AGV ts名称 一楼叉车slim 二楼叉车picking**/
    /**一楼叉车slim*/
    public static final String AGV_TS_NAME_SLIM = "slim";
    /**二楼叉车picking*/
    public static final String AGV_TS_NAME_PICKING = "picking";

    /** 接货位  **/
    /** 二楼输送线位置-下  入库使用**/
    public static final String SHELF_POINT_SECOND_LINE_IN = "SSX-1-1";
    /** 二楼输送线位置-上 出库使用 **/
    public static final String SHELF_POINT_SECOND_LINE_OUT = "SSX-1-2";

    /** 硬件执行任务记录  硬件类型 **/
    public static final String TASK_HARDWARE_WCS = "wcs";
    public static final String TASK_HARDWARE_AGV_PICKING = "agv:picking";
    public static final String TASK_HARDWARE_AGV_SLIM = "agv:slim";


    /** 硬件执行任务记录  接口类型 1发送任务  2接受上报信息 **/
    public static final String TASK_HARDWARE_INTERFACE_SEND = "1";
    public static final String TASK_HARDWARE_INTERFACE_ROLLBACK = "2";

    /**
     * 出库单状态
     *
     */
    // 1 待审核
    // 2 审核通过
    // 3 审核不通过
    // 6 部分退货
    // 7 全部退货
    // 10 已分配
    // 11 部分出库
    // 12 已出库
    // 8 已作废

    /**
     * 保存角色rpc参数  cache key
     */
    public static final String ROLE_SAVE_PARAM_KEY = CacheConstants.SYS_CONFIG_KEY + "roleSaveParam:";
    /**
     * 编辑角色rpc参数  cache key
     */
    public static final String ROLE_UPDATE_PARAM_KEY = CacheConstants.SYS_CONFIG_KEY + "roleUpdateParam:";

}
