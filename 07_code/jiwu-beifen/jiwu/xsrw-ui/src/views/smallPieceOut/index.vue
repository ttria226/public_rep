<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="载具编号" prop="trayCode">
        <el-input v-model="queryParams.batchCode" placeholder="请输入载具编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:inout:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="smallPieceOutList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true"  width="120" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true"  />
      <el-table-column label="领取数量" align="center" prop="smallReceiveCount" :show-overflow-tooltip="true" width="80" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" width="80" />
      <el-table-column label="库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="区域" align="center" prop="areaName" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="载具编号" align="center" prop="trayCode" :show-overflow-tooltip="true" width="170" />
      <el-table-column label="日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="120" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { getOutDeliverySmallRecordList } from "@/api/inoutDelivery/outDelivery";
import {wms} from '@/utils/agent'
export default {
  name: "smallPieceOutList",
  dicts: ["stock_detail_type"],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 小件领取数量表格数据
      smallPieceOutList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        trayCode: null,
      },
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "smallPieceOutList") {
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
      getOutDeliverySmallRecordList(this.queryParams).then((response) => {
        this.smallPieceOutList = response.rows;
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
      this.download(wms + "/deliveryOut/smallRecordexport", {...this.queryParams,}, `smallRecordExport_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>
