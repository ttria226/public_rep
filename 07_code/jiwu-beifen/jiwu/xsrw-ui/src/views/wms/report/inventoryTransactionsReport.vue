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
      <el-form-item label="库位" prop="locationName">
        <el-input v-model="queryParams.locationName" placeholder="请输入库位名称" clearable @keyup.enter.native="handleQuery" />
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

    <el-table v-loading="loading" :data="inventoryTransactionReportList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="140" />
      <el-table-column label="规格型号" align="center" prop="specifications" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true"  min-width="100" />
      <el-table-column label="库位" align="center" prop="locationName" :show-overflow-tooltip="true"  min-width="100" />
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true"  min-width="100" />
      <el-table-column label="类型" align="center" prop="type" :show-overflow-tooltip="true"  min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.stock_detail_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="操作前数量" align="center" prop="beforeCount" :show-overflow-tooltip="true"  min-width="100" />
      <el-table-column label="操作后数量" align="center" prop="currentCount" :show-overflow-tooltip="true"  min-width="100" />
      <el-table-column label="变动数量" align="center" prop="changeCount" :show-overflow-tooltip="true"  min-width="100" >
        <template slot-scope="scope">
          <span v-if="scope.row.changeCount > 0">{{ '+' + scope.row.changeCount }}</span>
          <span v-else>{{ scope.row.changeCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="日期" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listStockDeal } from "@/api/wms/stockDeal";
import {wms} from '@/utils/agent'
export default {
  name: "inventoryTransactionReport",
  dicts: ['stock_detail_type'],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库存交易表格数据
      inventoryTransactionReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        batchCode: null,
        locationName:null,
      },
      dataTime: [] //日期选择
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "inventoryTransactionReport") {
        this.getList();
      }
    },
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询拒收列表 */
    getList() {
      this.loading = true;
      listStockDeal(this.queryParams).then((response) => {
        this.inventoryTransactionReportList = response.rows;
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
      this.download(wms + "/stock/detail/stockDeal/export", {...this.queryParams,}, `inventoryTransactionReport_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>
