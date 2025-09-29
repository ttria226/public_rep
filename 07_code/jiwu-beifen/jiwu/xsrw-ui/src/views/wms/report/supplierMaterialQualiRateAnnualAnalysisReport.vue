<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="供应商名称" prop="contactsUnitName" label-width="90px">
        <el-input v-model="queryParams.contactsUnitName" placeholder="请输入供应商名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="联系人" prop="contactsUnitContact">
        <el-input v-model="queryParams.contactsUnitContact" placeholder="请输入联系人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="仓库名称" prop="warehouseId">
        <el-select v-model="queryParams.operatorId" filterable placeholder="请选择仓库名称" clearable>
          <el-option v-for="item in personQueryList" :key="item.userId" :label="item.warehouseId" :value="item.userId"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['base:classes:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="supplierMaterialQualiRateAnnualAnalysisReportList">
      <el-table-column label="供应商名称" align="center" prop="contactsUnitName" min-width="200px"/>
      <el-table-column label="联系人" align="center" prop="contactsUnitContact" min-width="200px" />
      <!-- <el-table-column label="仓库名称" align="center" prop="warehouseName" min-width="200px" /> -->
      <el-table-column label="收货数量" align="center" prop="registrationCount" min-width="120px" />
      <el-table-column label="质检通过数量" align="center" prop="detectionCount" min-width="120px" />
      <el-table-column label="合格率" align="center" prop="passRate" min-width="120px" >
        <template slot-scope="scope">
          <span v-if="scope.row.passRateNum < 60" style="color: red;">{{ scope.row.passRate }}</span>
          <span v-else>{{ scope.row.passRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="月份" align="center" prop="month" :show-overflow-tooltip="true" width="120" />
    </el-table>
    
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listMaterialQualificationRate } from "@/api/wms/materialQualificationRate";
import { getAction } from "@/api/manage"

import {wms} from '@/utils/agent'

export default {
  name: "supplierMaterialQualiRateAnnualAnalysisReport",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 供应商来料合格率年度分析报表表格数据
      supplierMaterialQualiRateAnnualAnalysisReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contactsUnitName: null,
        contactsUnitContact: null,
        warehouseId: null,
      },
      warehouseList: [], //仓库下拉列表
    };
  },
  created() {
    // this.getPerson()
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
    /** 查询班次管理列表 */
    getList() {
      this.loading = true;
      listMaterialQualificationRate(this.queryParams).then(response => {
        response.rows.map((item) => {
          let num = Number(item.passRate.split("%")[0])
          item.passRateNum = num
        })
        this.supplierMaterialQualiRateAnnualAnalysisReportList = response.rows;
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
      this.download(wms + '/report/center/materialQualificationRate/export', {...this.queryParams}, `materialQualificationRate_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
