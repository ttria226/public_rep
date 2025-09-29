<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务编号" prop="taskNo">
        <el-input v-model="queryParams.taskNo" placeholder="请输入任务编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="载具编号" prop="trayCode">
        <el-input v-model="queryParams.trayCode" placeholder="请输入载具编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.wcs_excute_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleEmergencyOut" v-hasPermi="['inout:task:export']">新增应急出库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:task:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outDeliveryControlList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="任务编号" align="center" prop="taskNo" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="载具编号" align="center" prop="trayCode" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="目标库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="状态" align="center" prop="taskStatus" :show-overflow-tooltip="true" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
<!--      <el-table-column label="优先级" align="center" prop="priority" min-width="120" :show-overflow-tooltip="true">-->
<!--        <template slot-scope="scope">-->
<!--          <el-select style="width: 100%;" v-model="scope.row.priority" placeholder="请选择优先级" @change="val => handleShowLevelChange(val,scope.row,scope.$index)" class="select-input-form">-->
<!--            <el-option v-for="dict in dict.type.wcs_task_priority" :key="dict.value" :label="dict.label" :value="dict.value" />-->
<!--          </el-select>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="故障原因" align="center" prop="errorMessage" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="执行人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="执行时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleExecute(scope.row,4)">查看</el-button>
<!--           <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.taskStatus == '1'" @click="handleExecute(scope.row,1)" v-hasPermi="['inout:task:remove']">执行</el-button>-->
          <el-button size="mini" type="text" icon="el-icon-postcard" v-if="scope.row.taskStatus != '3'" @click="handleRunDetail(scope.row)">运行记录</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.taskStatus == '1' || scope.row.taskStatus == '4'" @click="handleExecute(scope.row,2)" v-hasPermi="['inout:task:checkDetail']">重新发送</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.taskStatus != '3' && scope.row.taskStatus != '5' && scope.row.taskStatus != null" @click="handleExecute(scope.row,3)" v-hasPermi="['inout:task:executeOutTask']">强制完成</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.taskStatus != '3' && scope.row.taskStatus != '5' && scope.row.taskStatus != null" @click="handleCancel(scope.row)">强制作废</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.taskStatus == '2' && scope.row.taskStatus != null" @click="forcedBack(scope.row)">强制回库</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 详情/执行/重新执行/强制执行 -->
    <DetailCom ref="detailCom" @setControl="setControl" @showMaterialRfid="showMaterialRfid"></DetailCom>
    <!-- 选择物料rfid -->
    <material-rfid ref="outMaterialRfidCom" @setMaterialRfid="setMaterialRfid"></material-rfid>
    <!-- 应急出库 -->
    <emergency-out ref="emergencyOutCom" @showMaterial="showMaterial" @setEmergencyOut="setEmergencyOut"></emergency-out>
    <!-- 应急出库-选择物料 -->
    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>

    <!-- 查看任务运行记录 -->
    <RunTaskCom ref="runTaskCom" @setControl="setControl" @showRunTask="showRunTask" @closeTask="closeTask"></RunTaskCom>
    <!-- 查看wcs运行记录 -->
    <WcsRecordCom ref="wcsRecordCom" @setControl="setControl"></WcsRecordCom>

  </div>
</template>
<script>
import { listDeliveryExecute, updateDeliveryExecutePriority } from "@/api/inoutDelivery/inDelivery";
import { cancelTaskOutDelivery,enforcementDeliveryOut,forcedBack } from "@/api/inoutDelivery/outDelivery";

import { wms } from "@/utils/agent";

import DetailCom from "./components/detail";
import MaterialRfid from './components/materialRfid';
import EmergencyOut from './components/emergencyOut';
import MaterialCom from "../outDeliveryPlan/components/material";
import RunTaskCom from "../warehouseControl/components/runDetail";
import WcsRecordCom from "../warehouseControl/components/wcsRecordDetail";

export default {
  name: 'outDeliveryControl',
  dicts: ["wcs_excute_status","wcs_task_priority"],
  components: { DetailCom, MaterialRfid, EmergencyOut, MaterialCom, RunTaskCom, WcsRecordCom },
  data(){
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 出库监控表格数据
      outDeliveryControlList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskType: 2,
        taskNo: null,
        trayCode: null,
        taskStatus: null,
      },

      detailedId: null,
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "outDeliveryControl") {
        this.getList();
      }
    },
  },
  created(){
    this.getList();
  },
  methods: {
    // 已收货禁止选择
    checkSelectable(row, index) {
      return row.isTakeType != "1";
    },
    /** 查询监控列表 */
    getList() {
      this.loading = true;
      listDeliveryExecute({...this.queryParams}).then((response) => {
        this.outDeliveryControlList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/task/export", {...this.queryParams,}, `task_${new Date().getTime()}.xlsx`);
    },
    /** 处理优先级变化 */
    handleShowLevelChange(val,row,index){
      let oldValue = this.outDeliveryControlList[index].showLevel
      let info = JSON.parse(JSON.stringify(row))
      updateDeliveryExecutePriority({ id: row.id, priority: val }).then((response) => {
        this.$modal.msgSuccess("更新优先级成功");
        this.getList();
      }).catch(() => {
        info.priority = oldValue
        this.$set(this.outDeliveryControlList,index,info)
      });
    },
    //作废方法
    handleCancel(row){
      const ids = row.taskNo;
      let tips = '是否确认作废任务编号为"' + ids + '"的数据项？';
      this.$modal.confirm(tips).then(function () {
        return cancelTaskOutDelivery({ taskNo: ids });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("作废成功");
      }).catch(() => { });
    },
    // 强制回库
    forcedBack(row){
      let trayCode = row.trayCode;

      this.$modal.confirm("此功能为作废出库计划使用，请确保该任务除载具出库外没有进行任何其它操作，请谨慎使用该功能！！！").then(function () {
        return forcedBack({ trayCode: trayCode });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("执行成功");
      }).catch(() => { });
    },
    /** 新增应急出库 */
    handleEmergencyOut(){
      this.$refs.emergencyOutCom.open = true;
    },
    //执行方法
    handleExecute(row,type){
      // if(type != 3){
        if(type ==2){
            this.$modal.confirm("确定要重新发送该任务给WCS吗？").then(() => {
                    let param = {
                      id: row.id
                    }
                    return enforcementDeliveryOut(param)
                  }).then(() => {
                    this.$modal.msgSuccess("重新发送任务成功");
                    this.getList();
                  }).catch(() => { });
        }else{
            this.$refs.detailCom.open = true;
            this.$refs.detailCom.detailType = type;
            this.detailedId = row.id;
            this.$refs.detailCom.detailId = row.id;
        }
      // } else {
      //   this.$modal.confirm("确定要强制执行该任务？").then(() => {
      //     return enforcementOutDelivery({id: row.id})
      //   }).then(() => {
      //     this.$modal.msgSuccess("强制执行任务成功");
      //     this.getList();
      //   }).catch(() => { });
      // }
    },
    //选择物料rfid
    showMaterialRfid(item){
      this.$refs.outMaterialRfidCom.locationId = item.locationId
      this.$refs.outMaterialRfidCom.batchCode = item.batchCode
      this.$refs.outMaterialRfidCom.materialId = item.materialId
      this.$refs.outMaterialRfidCom.open = true
    },
    //选择物料rfid成功后的回调
    setMaterialRfid(materials){
      let list = JSON.parse(JSON.stringify(this.$refs.detailCom.detailForm.taskWcsDetailVOList))
      list.map((item) => {
        if(item.batchCode == materials.batchCode){
          if(item.rfids){
            let rfidsList = [...item.rfids]
            materials.rfids.map((materialRfidInfo) => {
              let info = rfidsList.find((itemRfidInfo) => { return itemRfidInfo == materialRfidInfo })
              if(!info){
                rfidsList.push(materialRfidInfo)
              }
            })
            item.rfids = rfidsList
            item.rfidString = rfidsList.toString()
          } else {
            item.rfids = materials.rfids
            item.rfidString = materials.rfids.toString()
          }
        }
      })
      this.$refs.detailCom.detailForm.taskWcsDetailVOList = list
      this.$refs.outMaterialRfidCom.open = false
      this.$forceUpdate()
    },
    //应急出库展示物料列表
    showMaterial(){
      this.$refs.materialCom.open = true;
    },
    //选择物料的回调
    setMaterial(material) {
      let flag = material ? this.$refs.emergencyOutCom.form.materialId !== material.id : false
      if(flag){
        this.$refs.emergencyOutCom.form.materialId = material.id ? material.id : "";
        this.$refs.emergencyOutCom.form.materialCode = material.code ? material.code : "";
        this.$refs.emergencyOutCom.form.materialName = material.name ? material.name : "";
        this.$refs.emergencyOutCom.form.unitName = material.unitName ? material.unitName : "";
        this.$refs.emergencyOutCom.form.unitId = material.unitId ? material.unitId : "";
        this.$refs.emergencyOutCom.form.minUnitName = material.minUnitName ? material.minUnitName : "";
        this.$refs.emergencyOutCom.form.predictCount = material.stock ? material.stock : "";
        this.$refs.emergencyOutCom.form.smallLimitCount = material.count ? material.count : "";
        this.$refs.emergencyOutCom.form.smallPredictCount = material.smallPredictCount ? material.smallPredictCount : "";
        this.$refs.emergencyOutCom.form.tOutDeliveryDetailList = []
        this.$refs.emergencyOutCom.currentSelection = []
      }
    },
    //应急出库成功后的回调
    setEmergencyOut(){
      this.getList();
    },
    //执行成功后的回调
    setControl(){
      this.getList();
    },
    //查看记录
    handleRunDetail(row){
      this.$refs.runTaskCom.open = true;
      this.$refs.runTaskCom.mainTaskNo = row.taskNo;
    },
    showRunTask(item) {
      this.$refs.wcsRecordCom.open = true;
      this.$refs.wcsRecordCom.taskWcsId = item.taskWcsId;
    },
    closeTask(){
      this.$refs.wcsRecordCom.open = false;
      this.$refs.wcsRecordCom.taskWcsId = null;
      this.getList();
    }
  }
}
</script>
