<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="区域" prop="areaId">
        <el-select v-model="queryParams.areaId"  @change="changeQueryReservoirList()" placeholder="请选择区域" clearable>
          <el-option v-for="dict in queryAreaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="库区" prop="reservoirId">
        <el-select v-model="queryParams.reservoirId" @change="changeQueryLocationList()" placeholder="请选择库区" clearable>
          <el-option v-for="dict in queryReservoirList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="库位" prop="locationId">
        <el-select v-model="queryParams.locationId" placeholder="请选择库位" clearable>
          <el-option v-for="dict in queryLocationList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
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

    <el-table v-loading="loading" :data="neededInventoryManagementList">
      <el-table-column label="库位" align="center" prop="locationName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="货架" align="center" prop="goodShelfName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="区域" align="center" prop="areaName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="状态" align="center" prop="goodsAllocationStatus" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.goodsAllocationStatus == '2' ? 'red' : 'green' }" v-for="item in dict.type.wms_goods_allocation_status" :key="item.value" v-show="scope.row.goodsAllocationStatus == item.value">{{ item.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="库存条数" align="center" prop="stockCount" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="操作人" align="center" prop="updateBy" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="操作时间" align="center" prop="updateTime" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document-checked" @click="handleMakerStatus(scope.row, '2')" v-hasPermi="['inout:delivery:check']">标记有货</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-checked" @click="handleMakerStatus(scope.row, '1')" v-hasPermi="['inout:delivery:check']">标记无货</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listDemandCheckLocation, markerDemandCheckLocation } from "@/api/wms/location";
import {wms} from '@/utils/agent'

import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";
import { listGoodShelf } from "@/api/wms/shelf";
import { listLocation } from "@/api/wms/location";

export default {
  name: "neededInventoryManagement",
  dicts: ["wms_goods_allocation_status"],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库存统计表格数据
      neededInventoryManagementList: [],
      queryAreaList: [], // 筛选的区域列表
      queryReservoirList: [], // 筛选的库区列表
      queryLocationList: [], // 筛选的库位列表
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        areaId: null,
        reservoirId: null,
        locationId: null,
      },
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "neededInventoryManagement") {
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
    /** 查询需盘点列表 */
    getList() {
      this.loading = true;
      listDemandCheckLocation(this.queryParams).then((response) => {
        this.neededInventoryManagementList = response.rows;
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
    //切换区域查库区事件
    changeQueryReservoirList(){
      this.queryParams.reservoirId = null;
      this.queryParams.locationId = null
      this.queryReservoirList = []
      listReservoir({areaId:this.queryParams.areaId,pageSize:5000}).then(response => {
        this.queryReservoirList = response.rows;
      });
    },
    //切换库区查库位事件
    changeQueryLocationList(){
      this.queryParams.locationId = null;
      this.queryLocationList = []
      listLocation({areaId:this.queryParams.areaId,reservoirId:this.queryParams.reservoirId,pageSize:5000}).then(response => {
        this.queryLocationList = response.rows;
      });
    },
    /** 标记无货/标记有货按钮操作 */
    handleMakerStatus(row, type){
      const ids = row.id;
      let tips = '该操作将库位为' + row.locationName + (type == '1' ? '标记无货' : '标记有货') +',确定要标记吗？';
      this.$modal.confirm(tips).then(function () {
        return markerDemandCheckLocation({ id: ids, goodsAllocationStatus: type });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(type == '1' ? '标记无货成功' : '标记有货成功');
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/location/demandCheck/export", {...this.queryParams,}, `demandCheckLocation_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>