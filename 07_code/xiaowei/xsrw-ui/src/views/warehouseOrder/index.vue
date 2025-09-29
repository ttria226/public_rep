<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入库单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入入库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inout:put:add']">新增入库单</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:put:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:put:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deliveryDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="入库单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="类型" align="center" prop="type" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <el-table-column label="物料使用部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="制单人" align="center" prop="maker" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="来源" align="resource" prop="newLocal" :show-overflow-tooltip="true" min-width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_origin" :value="scope.row.newLocal" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_delivery_put_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="创建日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 2)" v-hasPermi="['inout:put:query']">查看</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['inout:put:remove']">删除</el-button> -->
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '0'" @click="handleDetailed(scope.row, 1)" v-hasPermi="['inout:put:remove']">审核</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '0'" @click="handleCancel(scope.row)" v-hasPermi="['inout:put:remove']">作废</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 确认标签打印对话框 -->
    <el-dialog title="确认提示" :visible.sync="templateOpen" width="50%" append-to-body>
      <el-form ref="templateForm" :model="templateForm" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20" class="mb8">
          <el-col :span="24" class="">
            <i class="el-icon-warning"></i><span>该操作将打印以下物料标签，请确认标签模板及打印数量。</span>
          </el-col>
          <el-col :span="24">
            <el-form-item label="标签模版">
              <el-radio-group v-model="templateForm.templateType">
                <el-radio v-for="item in templateList" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
              </el-radio-group>
              <div class="lable-img">
                <img :src="templateForm.templateImg" alt="" />
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="deliveryDetailList" label-width="0px">
          <el-table v-loading="loading" :data="templateForm.deliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" width="160"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" width="90"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" width="200"></el-table-column>
            <el-table-column label="预计入库数量" align="center" prop="predictCount" width="100"></el-table-column>
            <el-table-column label="打印数量" align="center" prop="sn" width="150">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.sn'" :rules="detailRules.sn">
                  <el-input v-model="scope.row.sn" placeholder="请输入打印数量" size="small"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTemplateForm">确 定</el-button>
        <el-button @click="templateCancel">取 消</el-button>
      </div>
    </el-dialog>

    <choose-order ref="chooseOrderCom" @setOrder="setOrder"></choose-order>
    <DetailCom ref="detailCom" @templateGet="templateGet" @setDetail="setDetail"></DetailCom>
  </div>
</template>

<script>
import { listPut, addPut, updatePutStatus, getTemplateSelectList, getTemplateMaterialList } from "@/api/inoutDelivery/inDelivery";

import { wms } from "@/utils/agent";

import DetailCom from "./components/detail";
import ChooseOrder from '../components/inout/chooseOrder';

import print from 'vue-print-nb'

export default {
  name: "warehouseOrder",
  dicts: ["in_delivery_type", "wms_delivery_put_status", "in_delivery_origin"],
  components: { DetailCom, ChooseOrder },
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
      // 入库单表格数据
      deliveryDetailList: [],
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

      templateList: [], //标签打印-模版列表

      templateOpen: false,//标签打印弹窗标识
      templateForm: {
        templateType: 1,
        templateImg: '',
        deliveryDetailList: []
      },

      dialogLoading: false, //弹窗loading
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "qualityInspectionOrder") {
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
      listPut(this.queryParams).then((response) => {
        this.deliveryDetailList = response.rows;
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
      this.$refs.chooseOrderCom.title = '新建入库单';
      this.$refs.chooseOrderCom.type = '2';
      this.$refs.chooseOrderCom.form = {};
      this.$refs.chooseOrderCom.open = true;
    },
    // 打开配置明细
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;
      let title = '入库单详情';

      this.$refs.detailCom.detailTitle = title;
      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let tips = "";
      if (row.id) {
        tips = '是否确认删除入库单号为"' + row.code + '"的数据项？';
      } else {
        let codes = [];
        this.deliveryDetailList.forEach((item) => {
          if (this.ids.indexOf(item.id) > -1) {
            codes.push(item.code);
          }
        });
        tips = '是否确认删除入库单号为"' + codes.toString() + '"的数据项？';
      }
      this.$modal.confirm(tips).then(function () {
        return delInDelivery(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
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
    /** 确认按钮操作 */
    handleConfirm(){
      const ids = row.id;
      let tips = '该操作将确认该单质检结果,确定要确认吗？';
      this.$modal.confirm(tips).then(function () {
        return updatePutStatus({ id: ids, status: '1' });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("确认成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/delivery/export", {...this.queryParams,}, `inDelivery_${new Date().getTime()}.xlsx`);
    },
    //选择要生成入库单的数据
    setOrder(order){
      let params = {
        originId: order.id
      }
      addPut(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("新建入库单成功");
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
      params.id = this.detailId;
      params.status = data.flag;
      params.remark = data.remark;
      updatePutStatus(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess(data.flag == '1' ? "审核成功" : '作废成功');
          this.$refs.detailCom.open = false;
          this.$refs.detailForm = {}
          this.getList();
        }
      });
    },
    //详情跳标签打印
    templateGet(){
      this.handleLabelPrint()
    },
    // 表单重置
    resetTemplate() {
      this.templateForm = {
        templateType: 1,
        templateImg: '',
        deliveryDetailList: []
      };
      this.resetForm("templateForm");
    },
    //标签打印
    handleLabelPrint(){
      let ids = this.detailId || this.ids
      this.resetTemplate()
      getTemplateSelectList({}).then(res => {
        if(res.code === 200){
          this.templateList = res.rows
        }
      })
      getTemplateMaterialList(ids.toString()).then(res => {
        if(res.code === 200){
          this.templateForm.deliveryDetailList = res.data
          this.templateOpen = true
        }
      })
    },
    // 提交标签打印
    submitTemplateForm(){
      this.$refs["templateForm"].validate((valid) => {
        if (valid) {
          this.$modal.msgSuccess("打印成功");
          this.open = false;
          this.getList();
        }
      });
    },
    //标签打印取消
    templateCancel(){
      this.templateOpen = false
    },
  },
};
</script>
