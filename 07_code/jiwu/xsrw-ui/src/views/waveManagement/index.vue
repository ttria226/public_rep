<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="波次单号" prop="code" label-width="80px">
        <el-input v-model="queryParams.code" placeholder="请输入波次单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.complete_state" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inout:deliveryOut:add']">新增波次计划</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:delivery:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:delivery:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="waveManagementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="波次单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <!-- <el-table-column label="源单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" /> -->
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <el-table-column label="申请部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="申请人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <!-- <el-table-column label="来源" align="resource" prop="newLocal" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_local" :value="scope.row.newLocal" />
        </template>
      </el-table-column> -->
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <!-- <dict-tag :options="dict.type.complete_state" :value="scope.row.status" /> -->
          <span :style="{ color: scope.row.status == '0' ? 'red' : 'green' }" v-for="item in dict.type.complete_state" :key="item.value" v-show="scope.row.status == item.value">{{ item.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 3)" v-hasPermi="['inout:delivery:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '1'" @click="handleDelete(scope.row)" v-hasPermi="['inout:delivery:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-checked" v-if="scope.row.status == '1'" @click="handleCancel(scope.row)" v-hasPermi="['inout:delivery:check']">作废</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 选择出库计划 -->
    <MaterialCom ref="choosePlanCom" @setPlan="setPlan"></MaterialCom>
    <DetailCom ref="detailCom"></DetailCom>
  </div>
</template>

<script>
import { listMergeDelivery, delInDeliveryTask, addMergeDelivery } from "@/api/inoutDelivery/outDelivery";

import { wms } from "@/utils/agent";

import MaterialCom from "./components/material";
import DetailCom from "./components/detail";

export default {
  name: "waveManagement",
  dicts: ["inout_out_type", "complete_state","inout_out_local"],
  components: { MaterialCom, DetailCom },
  directives: {
    print,
  },
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
      // 波次管理表格数据
      waveManagementList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        type: null,
        status: null,
      },

      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情

      detailIndex: 0, //操作详情角标
      addLoading: false, // 新增按钮loading

      dialogLoading: false, //弹窗loading

      isAuth: false, //是否生产日期必填
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "waveManagement") {
        this.getList();
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询波次计划列表 */
    getList() {
      this.loading = true;
      listMergeDelivery(this.queryParams).then((response) => {
        this.waveManagementList = response.rows;
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$refs.choosePlanCom.title = '新建波次计划';
      this.$refs.choosePlanCom.form = {};
      this.$refs.choosePlanCom.open = true;
    },
    // 打开配置明细
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;
      let title = "波次单详情";

      this.$refs.detailCom.detailTitle = title;
      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    /** 作废按钮操作 */
    handleCancel(row) {
      const ids = row.id || this.ids;
      let tips = '该操作将作废该订单所有数据,确定要作废吗？';
      this.$modal.confirm(tips).then(function () {
        return updatePutStatus({ id: ids, status: '2' });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("作废成功");
      }).catch(() => { });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let tips = "";
      if (row.id) {
        tips = '是否确认删除波次单号为"' + row.code + '"的数据项？';
      } else {
        let codes = [];
        this.deliveryDetailList.forEach((item) => {
          if (this.ids.indexOf(item.id) > -1) {
            codes.push(item.code);
          }
        });
        tips = '是否确认删除波次单号为"' + codes.toString() + '"的数据项？';
      }
      this.$modal.confirm(tips).then(function () {
        return delInDeliveryTask(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/deliveryOut/outTasklist/export", {...this.queryParams,}, `WaveManagementExport_${new Date().getTime()}.xlsx`);
    },
    /** 选择出库计划回调 */
    setPlan(material) {
      let list = []
      material.ids.map((item) => {
        list.push(item)
      })
      addMergeDelivery({ids: list.toString()}).then((response) => {
        this.getList();
        this.$refs.choosePlanCom.open = false;
        this.$modal.msgSuccess("新增波次计划成功");
      });
    },
  },
};
</script>
