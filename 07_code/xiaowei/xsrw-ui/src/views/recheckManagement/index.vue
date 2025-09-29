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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inout:collection:add']">新增复检出库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:detail:materialDetailMonthlyExport']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recheckManagementList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" min-width="120" />
      <!-- <el-table-column label="类别" align="center" prop="categoryName" :show-overflow-tooltip="true" min-width="100" /> -->
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="库位" align="center" prop="locationName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="库存" align="center" prop="count" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.status == '1' ? 'red' : 'green' }" v-for="item in dict.type.wms_stock_recheck_status" :key="item.value" v-show="scope.row.status == item.value">{{ item.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="复检结果" align="center" prop="recheckResult" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <span :style="{ color: (scope.row.recheckResult == '全部通过' || scope.row.recheckResult === null || scope.row.recheckResult === '') ? 'green' : 'red' }">{{ scope.row.recheckResult }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document-checked" v-if="scope.row.status !== '3'" @click="handleDetailed(scope.row, 1)" v-hasPermi="['inout:delivery:check']">复检入库</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-checked" v-if="scope.row.status !== '3'" @click="handleFinishTest(scope.row)" v-hasPermi="['inout:delivery:check']">复检完成</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>
    <test-com ref="testCom" @setTest="setTest"></test-com>
  </div>
</template>
<script>
import { listRecheck, addRecheck, recheckCheckFinish } from "@/api/wms/recheck";
import {wms} from '@/utils/agent'

import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";

import MaterialCom from "./components/material";
import TestCom from "./components/test";

export default {
  name: "recheckManagement",
  dicts: ["wms_stock_recheck_status"],
  components: { MaterialCom, TestCom },
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 库存统计表格数据
      recheckManagementList: [],
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
      if (to.name == "recheckManagement") {
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
      listRecheck(this.queryParams).then((response) => {
        this.recheckManagementList = response.rows;
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
    /** 新增复检出库按钮操作 */
    handleAdd() {
      this.$refs.materialCom.open = true;
    },
    // 打开配置明细
    handleDetailed(row, type) {
      if(type == '1'){
        this.$refs.testCom.testOpen = true
        this.$refs.testCom.detailId = row.id;
        this.detailId = row.id;
        // this.$refs.testCom.dialogQueryParams.materialId = this.testRegistrationList && this.testRegistrationList.length > 0 ? this.testRegistrationList[0].id : ""
        this.$refs.testCom.testPageNum = 1
        this.$refs.testCom.testPageSize = 10
        this.$nextTick(() => {
          this.$refs.testCom.resetForm("dialogQueryForm");
        })
      } else {
        this.$refs.detailCom.detailId = row.id;
        this.detailId = row.id;
        this.detailType = type
        this.$refs.detailCom.detailType = type;
        let title = "复检单详情";

        this.$refs.detailCom.detailTitle = title;
        this.$refs.detailCom.detailForm = {};
        this.$refs.detailCom.open = true;
      }
    },
    //选择物料库位信息
    setMaterial(material) {
      let params = {
        stockId: material.id
      }
      addRecheck(params).then(res => {
        this.getList();
        this.$modal.msgSuccess("新增复检出库成功");
      })
    },
    //复检入库成功后的操作
    setTest(flag) {
      if(flag){
        this.getList()
      }
    },
    /** 复检完成按钮操作 */
    handleFinishTest(row){
      const ids = row.id;
      let tips = '该操作将标记物料编号为' + row.materialCode + '已完成复检,确定要标记吗？';
      this.$modal.confirm(tips).then(function () {
        return recheckCheckFinish({ id: ids });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("复检完成成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/stock/recheck/export", {...this.queryParams,}, `recheckManagement_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>