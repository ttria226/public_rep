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
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:task:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" ref="multipleTable" :data="warehouseControlList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="任务编号" align="center" prop="taskNo" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="载具编号" align="center" prop="trayCode" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="目标库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="伸位" align="center" prop="extentionType">
        <template slot-scope="scope">
          <span v-if="scope.row.extentionType == 2">{{scope.row.extentionType}}伸位优先</span>
          <span v-else-if="scope.row.extentionType">{{scope.row.extentionType}}伸位</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="taskStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
<!--      <el-table-column label="故障原因" align="center" prop="errorMessage" :show-overflow-tooltip="true" />-->
      <el-table-column label="执行人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="执行时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleExecute(scope.row,4)" v-hasPermi="['inout:task:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-postcard" v-if="scope.row.taskStatus != '3'" @click="handleRunDetail(scope.row)">运行记录</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.taskStatus != '3' && scope.row.taskStatus != '5'" @click="onceAgain(scope.row)">重新组盘</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.taskStatus == '1'" @click="handleExecute(scope.row,1)" v-hasPermi="['inout:task:remove']">执行</el-button> -->
<!--          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.taskStatus == '4'" @click="handleExecuteWcs(scope.row)">重新执行</el-button>-->
<!--          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.taskStatus != '3' && scope.row.taskStatus != null" @click="handleExecute(scope.row,3)">强制完成</el-button>-->
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.taskStatus == '1' || scope.row.taskStatus == '4'" @click="handleCancel(scope.row)">作废</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 详情/执行/重新执行/强制执行 -->
    <DetailCom ref="detailCom" @setControl="setControl" @showMaterialRfid="showMaterialRfid"></DetailCom>
    <!-- 选择物料rfid -->
    <material-rfid ref="materialRfidCom" @setMaterialRfid="setMaterialRfid"></material-rfid>
    <!-- 重新组盘 -->
    <ArriveTaskCom ref="arriveTaskCom" @setArriveTask="setArriveTask" @showMaterialRfid="showMaterialRfid" @setControl="setControl"></ArriveTaskCom>
    <!-- 查看任务运行记录 -->
    <RunTaskCom ref="runTaskCom" @setControl="setControl" @showRunTask="showRunTask" @closeTask="closeTask"></RunTaskCom>
    <!-- 查看wcs运行记录 -->
    <WcsRecordCom ref="wcsRecordCom" @setControl="setControl"></WcsRecordCom>

  </div>
</template>
<script>
import { listDeliveryExecute, cancelTaskDelivery, enforceDelivery,getInDeliveryTaskTrayType,getDeliveryExecute } from "@/api/inoutDelivery/inDelivery";

import { getToken } from "@/utils/auth";

import DetailCom from "./components/detail";
import RunTaskCom from "./components/runDetail";
import WcsRecordCom from "./components/wcsRecordDetail";
import MaterialRfid from '../warehouseTask/components/materiaRfidCopy';
import ArriveTaskCom from "../warehouseTask/components/arriveTaskCopy";
import { wms } from "@/utils/agent";

export default {
  name: 'warehouseControl',
  dicts: ["wcs_excute_status"],
  components: { DetailCom, MaterialRfid,ArriveTaskCom,RunTaskCom,WcsRecordCom},
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
      // 入库监控表格数据
      warehouseControlList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskNo: null,
        trayCode: null,
        taskStatus: null,
        taskType: 1
      },

      detailedId: null,
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "warehouseControl") {
        this.getList();
      }
    },
  },
  created(){
    this.getList();
  },
  methods: {
    getToken,
    // 已收货禁止选择
    checkSelectable(row, index) {
      return row.isTakeType != "1";
    },
    /** 查询监控列表 */
    getList() {
      this.loading = true;
      listDeliveryExecute({...this.queryParams}).then((response) => {
        this.warehouseControlList = response.rows;
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
      this.download(wms + "/inout/task/export", {...this.queryParams}, `task_${new Date().getTime()}.xlsx`);
    },
    //作废方法
    handleCancel(row){
      const id = row.id;
      let tips = '是否确认作废入库单号为"' + row.taskNo + '"的数据项？';
      this.$modal.confirm(tips).then(function () {
        return cancelTaskDelivery({id:id});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("作废成功");
      }).catch(() => { });
    },
    //执行方法
    handleExecute(row,type){
      // if(type != 3){
        this.$refs.detailCom.open = true;
        this.$refs.detailCom.detailType = type;
        this.detailedId = row.id;
        this.$refs.detailCom.detailId = row.id;
      // } else {
      //   this.$modal.confirm("确定要强制执行该任务？").then(() => {
      //     return enforcementDelivery({id: row.id})
      //   }).then(() => {
      //     this.$modal.msgSuccess("强制执行任务成功");
      //     this.getList();
      //   }).catch(() => { });
      // }
    },
    //重新发送命令
    handleExecuteWcs(row){
      let param = {
        id: row.id
      }
      this.$modal.confirm("确定要重新发送该任务调用设备？").then(() => {
        return enforceDelivery(param)
      }).then(() => {
        this.$modal.msgSuccess("发送成功");
        this.getList();
      }).catch(() => { });
    },
    //上架成功后的操作
    setArriveTask(flag) {
      if(flag){
        this.getList()
        if(this.$refs.materialRfidCom.$refs.tabSelect){
          this.$refs.materialRfidCom.$refs.tabSelect.clearSelection()
          this.$refs.materialRfidCom.selection=[]
        }
      }
    },
    //选择物料rfid
    showMaterialRfid(item){
      if(item.again){
        this.$refs.materialRfidCom.again = item.again
        this.$refs.materialRfidCom.locationId = item.locationId
      }
      this.$refs.materialRfidCom.id = item.id
      this.$refs.materialRfidCom.trayId = item.trayId
      this.$refs.materialRfidCom.materialId = item.materialId
      this.$refs.materialRfidCom.reActualCount = item.reActualCount
	  this.$refs.materialRfidCom.otherSelectList=this.$refs.arriveTaskCom.form.taskInList
      this.$refs.materialRfidCom.open = true
      if(!item.rfids||item.rfids.length == 0){
          if(this.$refs.materialRfidCom.$refs.tabSelect){
              this.$refs.materialRfidCom.$refs.tabSelect.clearSelection()
              this.$refs.materialRfidCom.selection=[]
          }
      }
    },
    //选择物料rfid成功后的回调
    setMaterialRfid(materials){
      console.log(2222,materials);
      if(materials.again){
        let list = JSON.parse(JSON.stringify(this.$refs.arriveTaskCom.form.taskInList))
        list.map((item) => {
          if(item.trayId == materials.trayId && item.materialId == materials.materialId){
            if(item.rfids){
              let rfidsList = [...item.rfids]
              materials.rfids.map((materialRfidInfo) => {
                let info = rfidsList.find((itemRfidInfo) => { return itemRfidInfo == materialRfidInfo })
                if(!info){
                  rfidsList.push(materialRfidInfo)
                }
              })
							item.reActualCount=materials.rfidCount
              item.rfids = rfidsList
              item.rfidString = rfidsList.toString()
            } else {
							item.reActualCount=materials.rfidCount
              item.rfids = materials.rfids
              item.rfidString = materials.rfids.toString()
            }
          }
        })
        this.$refs.arriveTaskCom.form.taskInList = list
        this.$refs.materialRfidCom.open = false
        this.$forceUpdate()
      }else{
        let list = JSON.parse(JSON.stringify(this.$refs.detailCom.detailForm.taskWcsDetailVOList))
        list.map((item) => {
            if(item.trayId == materials.trayId && item.materialId == materials.materialId){
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
        this.$refs.materialRfidCom.open = false
        this.$forceUpdate()
      }

    },
    //执行成功后的回调
    setControl(){
      this.getList();
    },
    // 重新组盘
    onceAgain(item){
      this.$refs.multipleTable.clearSelection();
      getDeliveryExecute(item.id).then((response) => {
        this.$refs.arriveTaskCom.materialList = [...response.data.taskWcsDetailVOList]
        this.$refs.arriveTaskCom.taskId = response.data.id
        this.$refs.arriveTaskCom.originTrayId = response.data.trayId
        this.$refs.arriveTaskCom.open = true
        this.$refs.arriveTaskCom.again = true
      });
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
