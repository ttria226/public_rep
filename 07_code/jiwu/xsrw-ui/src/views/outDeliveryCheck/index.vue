<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="出库单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入出库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inout:delivery:add']">新增出库单</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:delivery:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:delivery:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outDeliveryCheckList">
      <el-table-column label="出库单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="类型" align="center" prop="type" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="物料使用部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="来源" align="resource" prop="newLocal" :show-overflow-tooltip="true" min-width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_local" :value="scope.row.newLocal" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_out_recheck_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="创建日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 2)" v-hasPermi="['inout:delivery:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '0'" @click="handleDetailed(scope.row, 1)" v-hasPermi="['inout:delivery:remove']">审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    
    <choose-order ref="chooseOrderCom" @setOrder="setOrder"></choose-order>
    <DetailCom ref="detailCom" @setDetail="setDetail"></DetailCom>
  </div>
</template>

<script>
import { listOutDeliveryCheck, addOutDeliveryCheck, updateOutDeliveryCheckStatus } from "@/api/inoutDelivery/outDelivery";

import { wms } from "@/utils/agent";

import ChooseOrder from '../components/inout/chooseOrder';
import DetailCom from "./components/detail";

export default {
  name: "outDeliveryCheck",
  dicts: ["inout_out_type", "wms_out_recheck_status", "inout_out_local"],
  components: { DetailCom, ChooseOrder },
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
      // 出库复核表格数据
      outDeliveryCheckList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        type: null,
      },

      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "outDeliveryCheck") {
        this.getList();
      }
    },
  },
  created() {
    this.getList();
  },
  methods: {
    // 已收货禁止选择
    checkSelectable(row, index) {
      return row.status == '1' || row.status == '9';
    },
    /** 查询预先发货清单列表 */
    getList() {
      this.loading = true;
      listOutDeliveryCheck(this.queryParams).then((response) => {
        this.outDeliveryCheckList = response.rows;
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
      this.$refs.chooseOrderCom.title = '新建出库单';
      this.$refs.chooseOrderCom.type = '5';
      this.$refs.chooseOrderCom.form = {};
      this.$refs.chooseOrderCom.open = true;
    },
    // 打开配置明细
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;
      let title = "出库单详情";

      this.$refs.detailCom.detailTitle = title;
      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    /** 复核按钮操作 */
    handleCheck(row) {
      const ids = row.id || this.ids;
      let tips = '请复核出库物料数量和库位信息是否正确。';
      this.$modal.confirm(tips).then(function () {
        return delOutDelivery(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("复核成功");
      }).catch(() => { });
    },
    //选择要出库复核的数据
    setOrder(order){
      let params = {
        originId: order.id
      }
      addOutDeliveryCheck(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("新建出库单成功");
          this.$refs.chooseOrderCom.title = '';
          this.$refs.chooseOrderCom.type = '';
          this.$refs.chooseOrderCom.form = {};
          this.$refs.chooseOrderCom.open = false;
          this.getList();
        }
      });
    },
    //详情提交
    setDetail(data){
      let params = {}
      params.id = data.id;
      params.status = data.flag;
      params.remark = data.remark;
      updateOutDeliveryCheckStatus(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess(data.flag == 1 ? "审核成功" : "作废成功");
          this.$refs.detailCom.open = false;
          this.$refs.detailForm = {}
          this.getList();
        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/recheck/export", {...this.queryParams,}, `outDeliveryCheck_${new Date().getTime()}.xlsx`);
    },
  },
};
</script>
