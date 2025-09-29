<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId" filterable placeholder="请选择仓库名称" clearable>
          <el-option v-for="item in personQueryList" :key="item.userId" :label="item.userName" :value="item.userId"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="区域" prop="areaId">
        <el-select v-model="queryParams.areaId" @change="changeQueryReservoirList()" placeholder="请选择区域" class="select-input-form">
          <el-option v-for="item in areaQueryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="库区" prop="reservoirId">
        <el-select v-model="queryParams.reservoirId" placeholder="请选择库区" class="select-input-form">
          <el-option v-for="item in reservoirQueryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="时间范围" prop="dataTime">
        <el-date-picker v-model="dataTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item> -->
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

    <el-table v-loading="loading" :data="storageSpaceUsageFrequencyReportList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <!-- <el-table-column label="仓库" align="center" prop="warehouseName" :show-overflow-tooltip="true" min-width="150" /> -->
      <el-table-column label="区域" align="center" prop="areaName" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="货架" align="center" prop="goodShelfName" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="货位" align="center" prop="locationName" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="入库次数" align="center" prop="inCount" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="出库次数" align="center" prop="outCount" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="使用频率" align="center" prop="frequency" :show-overflow-tooltip="true" min-width="120" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listFrequencyOfLocation } from "@/api/wms/frequencyOfLocation";
import { getAction } from "@/api/manage"

import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";

import {wms} from '@/utils/agent'
export default {
  name: "storageSpaceUsageFrequencyReport",
  dicts: ["in_delivery_type"],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 货位使用频率报表表格数据
      storageSpaceUsageFrequencyReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        // warehouseId: null,
        areaId: null,
        reservoirId: null,
      },
      dataTime: [], //日期选择

      warehouseList: [], //仓库下拉列表
      areaQueryList: [], //区域下拉列表
      reservoirQueryList: [], //库区下拉列表
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "storageSpaceUsageFrequencyReport") {
        this.getList();
      }
    },
  },
  created() {
    // this.getNowDate()
    this.getAreaList()
    this.getList();
  },
  methods: {
    // 获取操作人员列表
    getPerson() {
      getAction('system/user/list', {pageSize: 5000}).then(res => {
        if (res.code == 200) {
          this.warehouseList = res.rows
        } else {
          this.$modal.msgError(res.msg);
        }
      })
    },
    // 获取区域下拉
    getAreaList() {
      listArea({pageSize:5000}).then(response => {
        this.areaQueryList = response.rows;
      });
    },
    // 获取库区下拉
    changeQueryReservoirList(){
      this.queryParams.reservoirId = null;
      this.reservoirQueryList = []
      listReservoir({areaId:this.queryParams.areaId,pageSize:5000}).then(response => {
        this.reservoirQueryList = response.rows;
      });
    },
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
    /** 查询拒收列表 */
    getList() {
      this.loading = true;
      // if(this.dataTime && this.dataTime.length > 0){
      //   this.queryParams.startDate = this.dataTime[0] + ' 00:00:00'
      //   this.queryParams.endDate = this.dataTime[1] + ' 23:59:59'
      // }
      listFrequencyOfLocation(this.queryParams).then((response) => {
        this.storageSpaceUsageFrequencyReportList = response.rows;
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
      this.download(wms + "/report/center/frequencyOfLocation/export", {...this.queryParams,}, `frequencyOfLocation_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>