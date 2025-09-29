<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="越库单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入越库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.wms_overstock_status" :key="dict.value" :label="dict.label" :value="dict.value" />
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
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:collection:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="crossDockingList">
      <el-table-column label="越库单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="单据类型" align="center" prop="type" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_overstock_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <el-table-column label="物料使用部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="来源" align="resource" prop="newLocal" :show-overflow-tooltip="true" min-width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_origin" :value="scope.row.newLocal" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_overstock_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="auditor" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 3)" v-hasPermi="['inout:collection:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '1' || scope.row.status == '2'" @click="handleDetailed(scope.row,2)" v-hasPermi="['inout:collection:remove']">收货</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status != '1' && scope.row.status != '5'" @click="handleDetailed(scope.row,1)" v-hasPermi="['inout:collection:remove']">出库</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改入库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="padding-right: 30px">
        <el-row :gutter="20" class="mb8">
          <el-col :span="12">
            <el-form-item label="入库类型" prop="type">
              <el-select style="width: 100%;" v-model="form.type" placeholder="请选择入库类型" class="select-input-form">
                <el-option v-for="dict in dict.type.wms_overstock_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料使用部门">
              <el-input v-model="form.deptName" disabled placeholder="请输入物料使用部门" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制单人">
              <el-input v-model="form.createBy" disabled placeholder="请输入制单人" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10" class="mb8" style="margin-left: 25px;">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addDetailed()">新增行</el-button>
          </el-col>
        </el-row>
        <el-form-item label-width="30px" prop="deliveryDetailList">
          <el-table v-loading="loading" :data="form.deliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.materialCode'" :rules="detailRules.materialCode">
                  <el-input v-model="scope.row.materialCode" cleab placeholder="请选择物料编码" size="small" @focus="materialComOpen(scope.$index)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="预计入库数量" align="center" prop="predictCount" width="210">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.predictCount'" :rules="detailRules.predictCount">
                  <el-input v-model="scope.row.predictCount" v-intNumber maxlength="6" placeholder="请输入预计入库数量" size="small"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" width="120"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" width="200">
              <!-- <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.batchCode'" :rules="detailRules.batchCode">
                  <el-input v-model="scope.row.batchCode" placeholder="请输入批次号" size="small"></el-input>
                </el-form-item>
              </template> -->
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteDetailed(scope.row,scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>
    <DetailCom ref="detailCom" @setDetail="setDetail"></DetailCom>
  </div>
</template>

<script>
import { listOverstock, addOverstock, registerOverstock, outOverstock } from "@/api/inoutDelivery/overstock";

import { wms } from "@/utils/agent";
import store from "@/store";

import MaterialCom from "../warehouseManage/components/material"
import DetailCom from "./components/detail";

export default {
  name: "crossDocking",
  dicts: ["wms_overstock_type", "wms_overstock_status", "in_delivery_origin"],
  components: { MaterialCom, DetailCom },
  data() {
    let validateMaterial = (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("物料编码不能为空"));
        this.$message.error("物料编码不能为空")
      } else {
        callback();
      }
    };
    let validatePredictCount = (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("预计入库数量不能为空"));
        this.$message.error("预计入库数量不能为空")
      } else {
        callback();
      }
    };
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
      // 越库表格数据
      crossDockingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        status: null
      },
      // 表单参数
      form: {
        deliveryDetailList: []
      },
      // 表单校验
      rules: {
        type: [{ required: true, message: "请选择入库类型", trigger: "blur" }],
        deptId: [
            { required: true, message: "请选择物料使用部门", trigger: "blur" },
        ],
      },
      detailRules: {
        materialCode: [
          // { required: true, message: "请选择物料编码", trigger: "change" },
          { trigger: "change", validator: validateMaterial },
        ],
        materialName: [
          { required: true, message: "请选择物料名称", trigger: "change" },
        ],
        batchCode: [
          { required: true, message: "请输入批次号", trigger: "blur" },
        ],
        predictCount: [
          // { required: true, message: "请输入预计入库数量", trigger: "blur" },
          { trigger: "blur", validator: validatePredictCount },
        ],
        producedDate: [
          // { trigger: "change", validator: validateProducedDate },
          { required: true, message: "请选择生产日期", trigger: "change" },
        ],
      },

      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情
      detailIndex: 0, //操作详情角标
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "crossDocking") {
        this.getList();
      }
    },
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询预先发货清单列表 */
    getList() {
      this.loading = true;
      listOverstock(this.queryParams).then((response) => {
        this.crossDockingList = response.rows;
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
    /** 新增按钮操作 */
    handleAdd() {
      this.open = true;
      this.title = "新增入库单";
      this.form = {
        id: null,
        type: null,
        deptName: null,
        createBy: null,
        deliveryDetailList: []
      };
      this.$nextTick(() => {
        this.reset();
        this.form.deptId = store.getters.deptId;
        this.form.deptName = store.getters.deptName;
        this.form.createBy = store.getters.name;
      })
    },
    // 查看/收货/出库按钮操作
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;
      let title = type == '1' ? '入库单详情' : '收货单详情';

      this.$refs.detailCom.detailTitle = title;
      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    /** 作废按钮操作 */
    handleDelete(row) {
      const ids = row.id;
      let tips = '该操作将作废该订单所有数据,确定要作废吗？';
      this.$modal.confirm(tips).then(function () {
        return updateCollectionStatus({ id: ids, status: '2' });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("作废成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/delivery/export", {...this.queryParams,}, `inDelivery_${new Date().getTime()}.xlsx`);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
    },
    // 表单重置
    reset() {
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      // this.dialogLoading = true
      if(this.form.deliveryDetailList.length == 0){
        this.$message.warning("入库物料明细不能为空！")
        return false
      }
      this.inputIndex = ''
      let errorMessage = []
      this.form.deliveryDetailList.map((item,index) => {
        if(item.materialCode === null || item.materialCode === ''){
          errorMessage.push(`第【${(index + 1)}】行的物料不能为空`)
        } else if(item.predictCount === null || item.predictCount === ''){
          errorMessage.push(`物料【${(item.materialName)}】行的预计入库数量不能为空`)
        }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          addOverstock(this.form).then((response) => {
            this.$modal.msgSuccess("新增成功");
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
    /** 新增明细操作 */
    addDetailed() {
      this.form.deliveryDetailList.push({
        materialCode: '',
        materialId: '',
        materialName: '',
        unitId: '',
        unitName: '',
        predictCount: null,
        // producedDate: null,
        batchCode: null,
      });
    },
    /** 删除明细操作 */
    deleteDetailed(row,index) {
      this.$modal.confirm("是否确认删除此明细").then(() => {
        this.form.deliveryDetailList.splice(index,1);
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    // 打开选择物料弹窗
    materialComOpen(index) {
      this.$refs.materialCom.open = true;
      this.$refs.materialCom.queryParams.contactsUnitId = this.form.unitCode;
      this.detailIndex = index
    },
    //选择物料回显
    setMaterial(material) {
      let deliveryDetailList = JSON.parse(JSON.stringify(this.form.deliveryDetailList));
      let flag = deliveryDetailList.every((item,index) => {
        if(index != this.detailIndex) {
          return item.materialId != material.id
        } else {
          return true
        }
      })
      if(flag){
        deliveryDetailList[this.detailIndex].materialId = material.id ? material.id : "";
        deliveryDetailList[this.detailIndex].materialCode = material.code ? material.code : "";
        deliveryDetailList[this.detailIndex].materialName = material.name ? material.name : "";
        deliveryDetailList[this.detailIndex].unitName = material.unitName ? material.unitName : "";
        deliveryDetailList[this.detailIndex].unitId = material.unitId ? material.unitId : "";
        deliveryDetailList[this.detailIndex].batchCode = material.batchCode ? material.batchCode : "";
        this.form.deliveryDetailList = deliveryDetailList;
        this.$forceUpdate()
      } else {
        this.$message.error(`已有【${material.name}】物料在明细中，请重新选择！`)
      }
    },
    //详情提交
    setDetail(data){
      let params = {}
      let detailList = []
      switch(this.detailType){
        case 1:
          data.deliveryDetailList.map((item) => {
            let info = {
              id: item.id,
              receiveCount: item.receiveCount,
              remark: item.remark,
            }
            detailList.push(info)
          })
          params.id = data.id;
          params.deliveryDetailList = detailList;
          outOverstock(params).then(res => {
            if(res.code === 200){
              this.$modal.msgSuccess("出库成功");
              this.$refs.detailCom.open = false;
              this.$refs.detailForm = {}
              this.getList();
            }
          });
          break;
        case 2:
          data.deliveryDetailList.map((item) => {
            let info = {
              id: item.id,
              registrationCount: item.registrationCount
            }
            detailList.push(info)
          })
          params.id = data.id;
          params.deliveryDetailList = detailList;
          registerOverstock(params).then(res => {
            if(res.code === 200){
              this.$modal.msgSuccess("收货成功");
              this.$refs.detailCom.open = false;
              this.$refs.detailForm = {}
              this.getList();
            }
          });
          break;
      }
    },
  },
};
</script>
