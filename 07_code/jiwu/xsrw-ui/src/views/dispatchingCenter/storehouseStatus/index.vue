<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属部门" prop="depId">
				<treeselect v-model="queryParams.depId" :options="deptOptions" :normalizer="normalizer" placeholder="选择所属部门" style="width: 215px;" />
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

    <el-table v-loading="loading" :data="storehouseStatusList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="所属部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="库存数量" align="center" prop="libraryCount" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="入库任务" align="center" prop="inTaskCount" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="出库任务" align="center" prop="outTaskCount" :show-overflow-tooltip="true" min-width="130" />
      <!-- <el-table-column label="调拨任务" align="center" prop="inventoryTaskCount" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="补货任务" align="center" prop="reservoirName" :show-overflow-tooltip="true" width="120" /> -->
      <el-table-column label="盘点任务" align="center" prop="inventoryTaskCount" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="库管员" align="center" prop="createBy" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="日期" align="center" prop="createTime" :show-overflow-tooltip="true" width="120" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listWareHouseStatus } from "@/api/dispatchCenter/wareHouseStatus";
import { listDept } from "@/api/system/dept";

import {wms} from '@/utils/agent'

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "storehouseStatus",
  dicts: ["in_delivery_type"],
  components: { Treeselect },
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 仓库状态表格数据
      storehouseStatusList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        depId: null,
      },
      deptOptions: [], //部门列表
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "storehouseStatus") {
        this.getList();
      }
    },
  },
  created() {
    this.getListDept()
    this.getList();
  },
  methods: {
    // 构造树形结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    // 获取部门数据
    getListDept() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      })
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      listWareHouseStatus(this.queryParams).then((response) => {
        this.storehouseStatusList = response.rows;
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
      this.download(wms + "/dispatchCenter/wareHouseStatus/export", {...this.queryParams,}, `wareHouseStatusExport_${new Date().getTime()}.xlsx`);
    },
  }
}
</script>