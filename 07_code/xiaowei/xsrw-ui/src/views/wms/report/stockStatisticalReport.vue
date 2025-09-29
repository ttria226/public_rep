<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="批次号" prop="batchCode">
        <el-input v-model="queryParams.batchCode" placeholder="请输入批次号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="区域" prop="areaId">
        <el-select v-model="queryParams.areaId"  @change="changeQueryReservoirList()" placeholder="请选择所属区域" clearable>
          <el-option v-for="dict in queryAreaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="库区" prop="reservoirId">
        <el-select v-model="queryParams.reservoirId" placeholder="请选择所属库区" clearable>
          <el-option v-for="dict in kqQueryList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
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

    <el-table v-loading="loading" :data="stockStatisticalReportList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="类别" align="center" prop="categoryName" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="库位" align="center" prop="locationName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="库存" align="center" prop="count" :show-overflow-tooltip="true" min-width="120" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listStoreStatistics } from "@/api/wms/storeStatistics";
import {wms} from '@/utils/agent'

import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";

export default {
  name: "stockStatisticalReport",
  dicts: ["in_delivery_type"],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库存统计表格数据
      stockStatisticalReportList: [],
      queryAreaList: [], // 筛选的区域列表
      kqQueryList: [], // 筛选的库区列表
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        batchCode: null,
        areaId: null,
        reservoirId: null,
      },
      dataTime: [] //日期选择
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "stockStatisticalReport") {
        this.getList();
      }
    },
  },
  created() {
    listArea({pageSize:5000}).then(response => {
      this.queryAreaList = response.rows;
    });
    this.getList();
  },
  methods: {
    /** 查询拒收列表 */
    getList() {
      this.loading = true;
      listStoreStatistics(this.queryParams).then((response) => {
        this.stockStatisticalReportList = response.rows;
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
    changeQueryReservoirList(){
      this.queryParams.reservoirId = null;
      this.kqQueryList = []
      listReservoir({areaId:this.queryParams.areaId,pageSize:5000}).then(response => {
        this.kqQueryList = response.rows;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/report/center/storeStatistics/export", {...this.queryParams,}, `storeStatistics_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>