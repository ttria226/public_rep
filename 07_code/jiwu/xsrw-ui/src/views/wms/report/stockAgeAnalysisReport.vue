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
      <el-form-item label="日期" prop="dataTime">
        <el-date-picker v-model="dataTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
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

    <el-table v-loading="loading" :data="stockAgeAnalysisReportList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="单价（元）" align="center" prop="unitPrice" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="库存数量" align="center" prop="count" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="库存重量（kg）" align="center" prop="weight" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="库存总金额（元）" align="center" prop="totalPrice" :show-overflow-tooltip="true" min-width="120" />
      <!-- <el-table-column label="月份" align="center" prop="monthly" :show-overflow-tooltip="true" min-width="120" /> -->
      <el-table-column label="入库时间" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="库龄（天）" align="center" prop="stockAge" :show-overflow-tooltip="true" min-width="100" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listWareHouseAgeAnalyse } from "@/api/wms/wareHouseAgeAnalyse";
import {wms} from '@/utils/agent'
export default {
  name: "stockAgeAnalysisReport",
  dicts: ["in_delivery_type"],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库龄分析表格数据
      stockAgeAnalysisReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        batchCode: null,
        beginDate: null,
        endDate: null
      },
      dataTime: [] //日期选择
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "stockAgeAnalysisReport") {
        this.getList();
      }
    },
  },
  created() {
    // this.getNowDate()
    this.getList();
  },
  methods: {
    /** 获取当前日期 */
    getNowDate(){
      let date = new Date()
      let month = date.getMonth()
      let year = date.getFullYear()
      var preDate = new Date(year, month, 0)
      var lastDay = preDate.getDate()
      let firstDate = year + "-" + (month + 1 < 10 ? '0'+(month + 1) : (month + 1)) + "-" + '01'
      let lastDate = year + "-" + (month + 1 < 10 ? '0'+(month + 1) : (month + 1)) + "-" + lastDay
      this.dataTime = [firstDate,lastDate]
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      if(this.dataTime && this.dataTime.length > 0){
        this.queryParams.startDate = this.dataTime[0]
        this.queryParams.endDate = this.dataTime[1]
      } else {
        this.queryParams.startDate = null
        this.queryParams.endDate = null
      }
      listWareHouseAgeAnalyse(this.queryParams).then((response) => {
        this.stockAgeAnalysisReportList = response.rows;
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
      // this.getNowDate()
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/report/center/wareHouseAgeAnalyse/export", {...this.queryParams,}, `wareHouseAgeAnalyse_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>