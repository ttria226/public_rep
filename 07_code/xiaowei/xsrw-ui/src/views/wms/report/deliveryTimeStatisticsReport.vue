<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入单号" clearable @keyup.enter.native="handleQuery" />
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

    <el-table v-loading="loading" :data="deliveryTimeStatisticsReportList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="预计数量" align="center" prop="predictCount" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="收货数量" align="center" prop="receiveCount" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="创建日期" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="预计交货日期" align="center" prop="planDate" :show-overflow-tooltip="true" min-width="150" />>
      <el-table-column label="实际交货日期" align="center" prop="predictDate" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="时长（h）" align="center" prop="time" :show-overflow-tooltip="true" min-width="100" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listDeliveryTimeStatistics } from "@/api/wms/deliveryTimeStatistics";
import {wms} from '@/utils/agent'
export default {
  name: "deliveryTimeStatisticsReport",
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 效率统计表格数据
      deliveryTimeStatisticsReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
      },
      dataTime: [] //日期选择
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "deliveryTimeStatisticsReport") {
        this.getList();
      }
    },
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询采购订单准时交付率列表 */
    getList() {
      this.loading = true;
      listDeliveryTimeStatistics(this.queryParams).then((response) => {
        this.deliveryTimeStatisticsReportList = response.rows;
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
      this.download(wms + "/report/center/deliveryTimeStatistics/export", {...this.queryParams,}, `deliveryTimeStatisticsReport_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>