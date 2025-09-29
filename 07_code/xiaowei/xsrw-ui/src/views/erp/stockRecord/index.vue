<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料号" prop="matnr">
        <el-input
          v-model="queryParams.matnr"
          placeholder="请输入物料号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="批次" prop="charg">
        <el-input
          v-model="queryParams.charg"
          placeholder="请输入批次"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓位" prop="lgpla">
        <el-input
          v-model="queryParams.lgpla"
          placeholder="请输入仓位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:task:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工厂" align="center" prop="werks" />
      <el-table-column label="库存地点" align="center" prop="lgort" />
      <el-table-column label="仓库号" align="center" prop="lgnum" />
      <el-table-column label="存储类型" align="center" prop="lgtyp" />
      <el-table-column label="物料号" align="center" prop="matnr" />
      <el-table-column label="物料描述" align="center" prop="makt" />
      <el-table-column label="批次" align="center" prop="charg" />
      <el-table-column label="仓位" align="center" prop="lgpla" />
      <el-table-column label="仓位名称" align="center" prop="locationName" />
      <el-table-column label="存储区" align="center" prop="lgber" />
      <el-table-column label="供应商" align="center" prop="lifnr" />
      <el-table-column label="供应商名称" align="center" prop="name1" />
      <el-table-column label="仓位存量" align="center" prop="gesme" />
      <el-table-column label="在库数量" align="center" prop="stockCount" />
      <el-table-column label="单价" align="center" prop="verpr" />
      <el-table-column label="总价" align="center" prop="salkv" />
      <el-table-column label="计量单位" align="center" prop="meins" />
      <el-table-column label="库存类别" align="center" prop="bestq" />
      <el-table-column label="特殊库存标识" align="center" prop="sobkz" />
      <el-table-column label="特殊库存编号" align="center" prop="posid" />
      <el-table-column label="预留字段1" align="center" prop="field1" />
      <el-table-column label="预留字段2" align="center" prop="field2" />
      <el-table-column label="预留字段3" align="center" prop="field3" />
      <el-table-column label="预留字段4" align="center" prop="field4" />
      <el-table-column label="预留字段5" align="center" prop="field5" />
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listErpStockRecord } from "@/api/erp/stockRecord";

import { wms } from "@/utils/agent";

export default {
  name: 'erpStockRecord',
  data(){
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
      //表格数据
      recordList: [],
      // 查询参数
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        werks: null,
        lgort: null,
        lgnum: null,
        lgtyp: null,
        matnr: null,
        makt: null,
        charg: null,
        lgpla: null,
        lgber: null,
        lifnr: null,
        name1: null,
        gesme: null,
        verpr: null,
        salkv: null,
        meins: null,
        bestq: null,
        sobkz: null,
        posid: null,
        field1: null,
        field2: null,
        field3: null,
        field4: null,
        field5: null,
      },
    }
  },
  watch: {
  },
  created(){
    this.getList();
  },
  methods: {
    /** 查询监控列表 */
    getList() {
      this.loading = true;
      listErpStockRecord({...this.queryParams,}).then((response) => {
        this.recordList = response.rows;
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
      this.download(wms + "/webservice/erp/stock/export", {...this.queryParams,}, `erp库存_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>
