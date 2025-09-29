<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码">
        <el-input v-model="queryParams.materailCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="物料名称">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:StockMain:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="validityWarningList" @selection-change="handleSelectionChange">
      <el-table-column label="物料编码" align="center" prop="materialCode" min-width="130px" :show-overflow-tooltip="true" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200px" />
      <el-table-column label="物料描述" align="center" prop="description" :show-overflow-tooltip="true" min-width="230px" />
      <el-table-column label="物料类型" align="center" prop="categoryName" :show-overflow-tooltip="true" min-width="120px" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" min-width="100px" />
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" min-width="130px" />
      <el-table-column label="生产日期" align="center" prop="producedDate" :show-overflow-tooltip="true" min-width="150px" />
      <el-table-column label="有效期" align="center" prop="expiryDate" :show-overflow-tooltip="true" min-width="150px">
        <template slot-scope="scope">
          <span v-if="scope.row.isWarning" style="color: red;">{{ scope.row.expiryDate }}</span>
          <span v-else>{{ scope.row.expiryDate }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>

<script>
import { listStockWarningValidityWarning } from "@/api/wms/stockLimitsWarning";
import { wms } from '@/utils/agent';

export default {
  name: "validityWarning",
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
      // 有效期预警列表表格数据
      validityWarningList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materailCode: null,
        materialName: null,
        warehouseId: null,
      },

    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'validityWarning') {
        this.getList();
      }
    }
  },
  created() {
    // this.getQueryWareHousList();
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
      listStockWarningValidityWarning(this.queryParams).then(response => {
        response.rows.map((item) => {
          if(item.expiryDate){
            let date1 = new Date()
            let date2 = new Date(item.expiryDate)
            if(date2.getTime() - date1.getTime() <= 3 * 24 * 60 * 60 * 1000){
              item.isWarning = true
            } else {
              item.isWarning = false
            }
          } else {
            item.isWarning = false
          }
        })
        console.log(response.rows)
        this.validityWarningList = response.rows;
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
      this.queryParams={
        pageNum: 1,
        pageSize: 10,
        materailCode: null,
        materialName: null,
        warehouseId: null,
      }
      this.resetForm("queryForm");
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
      this.download(wms+'/report/center/stockWarning/validityWarning/export', {...this.queryParams}, `validityWarning_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
