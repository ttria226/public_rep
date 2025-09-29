<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="仓库名称" prop="warehouseId">
        <el-select v-model="queryParams.operatorId" filterable placeholder="请选择仓库名称" clearable>
          <el-option v-for="item in personQueryList" :key="item.userId" :label="item.userName" :value="item.userId"></el-option>
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

    <el-table v-loading="loading" :data="defectiveProductWarningReportList">
      <el-table-column label="物料编码" align="center" prop="materialCode" min-width="130px"/>
      <el-table-column label="物料名称" align="center" prop="materialName" min-width="200px" />
      <el-table-column label="物料描述" align="center" prop="description" min-width="300px" />
      <el-table-column label="物料类型" align="center" prop="categoryName" min-width="120px" />
      <el-table-column label="计量单位" align="center" prop="unitName" min-width="100px" />
      <el-table-column label="数量" align="center" prop="count" min-width="100px" />
      <!-- <el-table-column label="仓库名称" align="center" prop="warehouseName" min-width="150px" /> -->
      <el-table-column label="批次号" align="center" prop="batchCode" min-width="150px" />
      <el-table-column label="呆滞时间" align="center" prop="deadTime" min-width="180px" />
      <el-table-column label="呆滞期(天)" align="center" prop="deadDayCount" min-width="150px" >
        <template slot-scope="scope">
          <span style="color: red;">{{ scope.row.deadDayCount }}</span>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listDeadStockWarning } from "@/api/wms/deadStockWarning";
import { getAction } from "@/api/manage"
import {wms} from '@/utils/agent'

export default {
  name: "defectiveProductWarningReport",
  dicts: ['wms_base_classes_type'],
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
      // 呆滞品预警报表表格数据
      defectiveProductWarningReportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        warehouseId: null,
      },
      warehouseList: [], //操作人员下拉列表
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
      listDeadStockWarning(this.queryParams).then(response => {
        this.defectiveProductWarningReportList = response.rows;
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
      this.download(wms+'/report/center/deadStockWarning/export', {...this.queryParams}, `deadStockWarning_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
