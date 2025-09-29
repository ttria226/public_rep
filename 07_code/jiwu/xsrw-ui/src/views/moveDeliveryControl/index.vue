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

    <el-table v-loading="loading" :data="moveDeliveryControlList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="任务编号" align="center" prop="taskNo" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="任务类型" align="center" prop="taskType" :show-overflow-tooltip="true" width="80" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_task_type" :value="scope.row.taskType" />
        </template>
      </el-table-column>
      <el-table-column label="载具编号" align="center" prop="trayCode" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="目标库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="起始位置" align="center" prop="startPosition" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="到达位置" align="center" prop="purposePosition" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="任务状态" align="center" prop="taskStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="故障原因" align="center" prop="errorMessage" :show-overflow-tooltip="true" width="250" /> -->
      <el-table-column label="执行人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="执行时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <!-- 详情/执行/重新执行/强制执行 -->
    <DetailCom ref="detailCom" @setControl="setControl"></DetailCom>
  </div>
</template>
<script>
import { listDeliveryExecute } from "@/api/inoutDelivery/inDelivery";
import { cancelMoveDelivery } from "@/api/inoutDelivery/outDelivery";

import { wms } from "@/utils/agent";

import DetailCom from "./components/detail";

export default {
  name: 'moveDeliveryControl',
  dicts: ["wcs_excute_status",'wcs_task_type'],
  components: { DetailCom },
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
      // 移库监控表格数据
      moveDeliveryControlList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskType: 5,
        taskNo: null,
        trayCode: null,
        taskStatus: null,
      },

      detailedId: null,
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "moveDeliveryControl") {
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
      listDeliveryExecute({...this.queryParams,}).then((response) => {
        this.moveDeliveryControlList = response.rows;
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
      this.download(wms + "/inout/task/export", {...this.queryParams,}, `moveDeliveryControl_${new Date().getTime()}.xlsx`);
    },
    //作废方法
    handleCancel(row){
      const ids = row.taskNo;
      let tips = '是否确认作废任务编号为"' + ids + '"的数据项？';
      this.$modal.confirm(tips).then(function () {
        return cancelMoveDelivery({ taskNo: ids });
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
      //     return enforcementOutDelivery({id: row.id})
      //   }).then(() => {
      //     this.$modal.msgSuccess("强制执行任务成功");
      //     this.getList();
      //   }).catch(() => { });
      // }
    },
    //执行成功后的回调
    setControl(){
      this.getList();
    }
  }
}
</script>
