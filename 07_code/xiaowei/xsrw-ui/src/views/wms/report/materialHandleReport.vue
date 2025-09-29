<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="物料名称">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="起始日期" prop="beginDate">
        <el-date-picker clearable v-model="queryParams.beginDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择起始日期"></el-date-picker>
      </el-form-item>
      <el-form-item label="终止日期" prop="endDate">
        <el-date-picker clearable v-model="queryParams.endDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择终止日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:MaterialSummary:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="materialList">
      <!-- <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="物料编码" align="center" prop="code" width="250px" :show-overflow-tooltip="true" />
      <el-table-column label="物料名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <!-- <el-table-column label="仓库名称" align="center" prop="warehouseName" :show-overflow-tooltip="true" /> -->
      <el-table-column label="累计入库数量" align="center" prop="inCount"  :show-overflow-tooltip="true" />
      <el-table-column label="累计出库数量" align="center" prop="outCount" :show-overflow-tooltip="true" />
      <el-table-column label="库存结余数量" align="center" prop="stockCount" :show-overflow-tooltip="true" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>

<script>
import { listReportyCenter } from "@/api/wms/materialHandleReport";
import { wms } from '@/utils/agent';

export default {
  name: "MaterialHandleReport",
  dicts: [],
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
      // 物料收发汇总表表格数据
      materialList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        // warehouseId: null,
        beginDate: null,
        endDate: null,
      },
      queryWareHouseList: [], //仓库list

    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'MaterialHandleReport') {
        this.getList();
      }
    }
  },
  created() {
    this.getQueryWareHousList();
    this.getList();
  },
  methods: {
    /** 查询仓库列表 */
    getQueryWareHousList() {
      let qps = {
        pageNum: 1,
        pageSize: 10,
      };
      qps.pageSize = 5000
    },
    /** 查询出库计划列表 */
    getList() {
      this.loading = true;
      listReportyCenter(this.queryParams).then(response => {
        this.materialList = response.rows;
        this.total = response.total;
      }).finally(() => {
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
      this.queryParams= {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        // warehouseId: null,
        beginDate: null,
        endDate: null,
      }
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/report/center/MaterialSummary/export', {
        ...this.queryParams
      }, `MaterialSummary${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
