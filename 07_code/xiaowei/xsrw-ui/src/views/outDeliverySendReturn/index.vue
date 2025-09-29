<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="发货单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入发货单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.wms_out_removal_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inout:delivery:add']">新增发货单</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:delivery:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:delivery:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outDeliverySendReturnList">
      <el-table-column label="发货单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
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
          <dict-tag :options="dict.type.wms_out_removal_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="创建日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row,2)" v-hasPermi="['inout:delivery:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '0' || scope.row.status == '2'" @click="handleDetailed(scope.row,1)" v-hasPermi="['inout:delivery:remove']">退货</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

      <!-- 退货对话框 -->
    <el-dialog title="退货" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" style="padding-right: 30px">
        <el-form-item label="退货数量">
          <el-input v-model="form.carNum" placeholder="请输入退货数量" v-intNumber maxlength="5" />
        </el-form-item>
        <el-form-item label="退货原因">
          <el-input type="textarea" :rows="2" placeholder="请输入退货原因" v-model="form.returnReason"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <choose-order ref="chooseOrderCom" @setOrder="setOrder"></choose-order>
    <DetailCom ref="detailCom" @setDetail="setDetail"></DetailCom><!--@setDetail="setDetail"-->
  </div>
</template>

<script>
import { listOutDeliverySendReturn, addOutDeliverySendReturn, updateOutDeliverySendReturnStatus } from "@/api/inoutDelivery/outDelivery";

import { wms } from "@/utils/agent";


import ChooseOrder from '../components/inout/chooseOrder';
import DetailCom from "./components/detail";

export default {
  name: "outDeliverySendReturn",
  dicts: ["inout_out_type","wms_out_removal_status","inout_out_local"],
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
      // 发货退货表格数据
      outDeliverySendReturnList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        materialCode: null,
        materialName: null,
      },

      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情

      form: {}
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "outDeliverySend") {
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
      listOutDeliverySendReturn(this.queryParams).then((response) => {
        this.outDeliverySendReturnList = response.rows;
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
      this.$refs.chooseOrderCom.type = '7';
      this.$refs.chooseOrderCom.form = {};
      this.$refs.chooseOrderCom.open = true;
    },
    // 打开配置明细
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;
      let title = "发货单详情";

      this.$refs.detailCom.detailTitle = title;
      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    /** 发货按钮操作 */
    handleSend(row) {
      this.reset()
      this.open = true
      this.detailId = row.id;
    },
    /** 重置表单 */
    reset(){
      this.form = {
        carNum: null,
        returnReason: null
      }
      this.$nextTick(() => {
        this.resetForm("form");
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let params = {
            ...this.form,
            id: this.detailId,
            status: '1'
          }
          updateOutDeliverySendReturnStatus(params).then((response) => {
            this.$modal.msgSuccess("退货成功");
            this.open = false;
            this.getList();
          }).finally(() => {
            // this.dialogLoading = false
          });
        } else {
          // this.dialogLoading = false
        }
      });
    },
    //详情提交
    setDetail(data){
      let tOutDeliveryDetailList = []
      data.tOutDeliveryDetailList.map((item) => {
        let info = {
          id: item.id,
          returnCount: item.returnCount
        }
        tOutDeliveryDetailList.push(info)
      })
      let params = {
        id: data.id,
        remark: data.remark,
        tOutDeliveryDetailList
      }
      updateOutDeliverySendReturnStatus(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("退货成功");
          this.$refs.detailCom.open = false;
          this.$refs.detailForm = {}
          this.getList();
        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/delivery/export", {...this.queryParams,}, `inDelivery_${new Date().getTime()}.xlsx`);
    },
    //选择要生成发货单的数据
    setOrder(order){
      let params = {
        originId: order.originId
      }
      addOutDeliverySendReturn(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("新建发货单成功");
          this.$refs.chooseOrderCom.title = '';
          this.$refs.chooseOrderCom.type = '';
          this.$refs.chooseOrderCom.form = {};
          this.$refs.chooseOrderCom.open = false;
          this.getList();
        }
      });
    },
  },
};
</script>
