<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务编号" prop="taskNo">
        <el-input v-model="queryParams.taskNo" placeholder="请输入任务编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" placeholder="请选择状态" class="select-input-form">
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
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:detail:materialDetailMonthlyExport']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="businessMonitoringList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="任务编号" align="center" prop="taskNo" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" width="130" />
      <el-table-column label="计划数量" align="center" prop="predictCount" :show-overflow-tooltip="true" width="100" />
      <el-table-column label="实际数量" align="center" prop="actualCount" :show-overflow-tooltip="true" width="100" />
      <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="状态" align="center" prop="taskStatus" :show-overflow-tooltip="true" width="120" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
      <el-table-column label="任务类型" align="center" prop="taskType" :show-overflow-tooltip="true" width="120" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_task_type" :value="scope.row.taskType" />
        </template>
      </el-table-column>
      <el-table-column label="执行人" align="center" prop="createBy" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="执行时间" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listBusinessMonitors } from "@/api/dispatchCenter/businessMonitors";
import {wms} from '@/utils/agent'
export default {
  name: "businessMonitoring",
  dicts: ["wcs_excute_status",'wcs_task_type'],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 业务监控表格数据
      businessMonitoringList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskNo: null,
        materialName: null,
        taskStatus: null,
        // reservoirName: null,
      },
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "businessMonitoring") {
        this.getList();
      }
    },
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listBusinessMonitors(this.queryParams).then((response) => {
        this.businessMonitoringList = response.rows;
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
      this.download(wms + "/dispatchCenter/businessMonitors/export", {...this.queryParams,}, `businessMonitorsExport_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>