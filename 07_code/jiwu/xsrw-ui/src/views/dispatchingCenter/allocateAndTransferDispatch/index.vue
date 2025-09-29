<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="调拨单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入调拨单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="allotStatus">
        <el-select v-model="queryParams.allotStatus" placeholder="请选择状态" class="select-input-form">
          <el-option v-for="dict in dict.type.wms_allot_status" :key="dict.value" :label="dict.label" :value="dict.value" />
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

    <el-table v-loading="loading" :data="allocateAndTransferDispatchList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="调拨单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="调拨数量" align="center" prop="allotNum" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="原仓库" align="center" prop="outWarehouseName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="目标仓库" align="center" prop="inWarehouseName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="状态" align="center" prop="allotStatus" :show-overflow-tooltip="true" min-width="120" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_allot_status" :value="scope.row.allotStatus" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="执行人" align="center" prop="createBy" :show-overflow-tooltip="true" width="120" /> -->
      <el-table-column label="执行时间" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listAllotDispatch } from "@/api/dispatchCenter/allotDispatch";
import {wms} from '@/utils/agent'
export default {
  name: "allocateAndTransferDispatch",
  dicts: ["wms_allot_status"],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 调拨调度表格数据
      allocateAndTransferDispatchList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        materialName: null,
        allotStatus: null,
      },
      dataTime: [] //日期选择
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "allocateAndTransferDispatch") {
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
      listAllotDispatch(this.queryParams).then((response) => {
        this.allocateAndTransferDispatchList = response.rows;
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
      this.download(wms + "/dispatchCenter/allotDispatch/export", {...this.queryParams,}, `allotDispatchExport_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>