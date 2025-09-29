<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入库单号" prop="advanceDeliveryCode">
        <el-input v-model="queryParams.advanceDeliveryCode" placeholder="请输入入库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="入库单类型" prop="advanceDeliveryType" label-width="90px">
        <el-select v-model="queryParams.advanceDeliveryType" clearable placeholder="请选择入库单类型">
          <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:rejection:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:rejection:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rejectionList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="入库单号" align="center" prop="advanceDeliveryCode" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" width="100" />
      <el-table-column label="数量" align="center" prop="rejectionCount" :show-overflow-tooltip="true" width="100" />
      <el-table-column label="入库类型" align="center" prop="advanceDeliveryType" :show-overflow-tooltip="true" min-width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_type" :value="scope.row.advanceDeliveryType" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <el-table-column label="原因" align="center" prop="rejectionFailType" :show-overflow-tooltip="true" min-width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_detection_fail_type" :value="scope.row.rejectionFailType" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="rejectionFailRemark" :show-overflow-tooltip="true" width="200"/>
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope>
          <!-- <dict-tag :options="dict.type.in_delivery_status" :value="scope.row.status" /> -->
          <span style="color: red;">检测不通过</span>
        </template>
      </el-table-column>
      <el-table-column label="检测人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="检测时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listRejection } from "@/api/inoutDelivery/inDelivery";
import { wms } from "@/utils/agent";
export default {
  name: "rejectionManage",
  dicts: ["in_delivery_type", "in_delivery_status", "in_delivery_detection_fail_type"],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 拒收表格数据
      rejectionList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        advanceDeliveryCode: null,
        advanceDeliveryType: null,
        materialName: null,
      },
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "rejectionManage") {
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
      listRejection(this.queryParams).then((response) => {
        this.rejectionList = response.rows;
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
      this.download(wms + "/inout/rejection/export", {...this.queryParams,}, `rejection_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>
