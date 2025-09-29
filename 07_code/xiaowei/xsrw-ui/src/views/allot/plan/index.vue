<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="85px">
      <el-form-item label="调拨单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入调拨单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item> -->
      <el-form-item label="单据状态" prop="status" label-width="90px">
        <el-select v-model="queryParams.status" clearable placeholder="请选择单据状态">
          <el-option v-for="dict in dict.type.wms_allot_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" v-hasPermi="['cims:allot:generate']" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['cims:allot:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['cims:allot:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="allotPlanList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" />
      <el-table-column label="调拨单号" align="center" fixed min-width="180px" prop="code" :show-overflow-tooltip="true" />
      <el-table-column label="物料编码" align="center" fixed min-width="150px" prop="materialCode" :show-overflow-tooltip="true" />
      <el-table-column label="物料名称" align="center" fixed min-width="200px" prop="materialName" :show-overflow-tooltip="true" />
      <el-table-column label="批次" align="center" prop="batchCode" min-width="150px" :show-overflow-tooltip="true" />
      <el-table-column label="计量单位" align="center" prop="unitName" width="100px" :show-overflow-tooltip="true" />
      <el-table-column label="数量" align="center" prop="allotNum" :show-overflow-tooltip="true" />
      <el-table-column label="原仓库" align="center" prop="outWarehouseName" :show-overflow-tooltip="true" width="180" />
      <!-- <el-table-column label="原库位" align="center" prop="cuRemoteClusterName" :show-overflow-tooltip="true" width="180" /> -->
      <el-table-column label="新仓库" align="center" prop="inWarehouseName" :show-overflow-tooltip="true" width="180" />
      <!-- <el-table-column label="新库位" align="center" prop="cuRemoteClusterName" :show-overflow-tooltip="true" width="180" /> -->
      <!-- <el-table-column label="调拨类型" align="center" prop="type" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_type" :value="scope.row.type" />
        </template>
      </el-table-column> -->
      <el-table-column label="状态" align="center" prop="allotStatus" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_allot_status" :value="scope.row.allotStatus" />
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="auditor" :show-overflow-tooltip="true" width="100" />
      <el-table-column label="审核时间" align="center" prop="updateTime" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150" fixed="right">
        <template slot-scope="scope">
          <!-- <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 2)" v-hasPermi="['inout:delivery:query']">查看</el-button> -->
          <!-- <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status == '1'" @click="handleUpdate(scope.row)" v-hasPermi="['inout:delivery:edit']">修改</el-button> -->
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.allotStatus == '1'" @click="handleDelete(scope.row)" v-hasPermi="['inout:delivery:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-user" v-if="scope.row.allotStatus == '1'" @click="handleDetailed(scope.row, 1)" v-hasPermi="['inout:delivery:approve']">调拨</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加调拨单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="55%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" style="padding-right: 10px">
        <el-row :gutter="10" class="mb8">
          <el-col :span="12">
            <el-form-item label="原仓库" prop="outWarehouseId">
              <el-select style="width: 65%;margin-right: 10px;" v-model="form.outWarehouseId" placeholder="请选择原仓库" class="select-input-form" @change="handleDeptChange">
                <el-option v-for="item in outWarehouseList" :key="item.deptId" :label="item.deptName" :value="item.deptId" />
              </el-select>
              <el-button type="primary" @click="materialComOpen" style="width: 30%;padding: 10px;">选择物料</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="迁入仓库">
              <el-input v-model="form.inWarehouseName" disabled placeholder="请输入迁入仓库" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10" class="mb8">
          <el-col :span="12">
            <el-form-item label="调拨数量" prop="allotNum">
              <el-input v-model="form.allotNum" v-intNumber placeholder="请输入调拨数量" :maxlength="6" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="10px" prop="allotStockList">
          <el-table v-loading="loading" :data="form.allotStockList">
            <el-table-column label="区域" align="center" prop="areaName" min-width="180" />
            <el-table-column label="库区" align="center" prop="reservoirName" min-width="120" />
            <el-table-column label="库位" align="center" prop="locationName" min-width="120" />
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="150" />
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180" />
            <el-table-column label="计量单位" align="center" prop="unitName" width="100" />
            <el-table-column label="批次号" align="center" prop="batchCode" width="150" />
            <el-table-column label="库存数量" align="center" prop="availableCount" width="120" />
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
import { listAllotPlan, delAllotPlan, addAllotPlan, listAllotDept, executeAllot } from "@/api/inoutDelivery/allot";

import { listDept } from "@/api/system/dept";

import { wms } from "@/utils/agent";
import store from "@/store";

import MaterialCom from "./components/material";
import DetailCom from "./components/detail";

export default {
  name: "AllotPlan",
  dicts: ["wms_allot_status"],
  components: { MaterialCom, DetailCom },
  data() {
    let validateAllotNum = (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("调拨数量不能为空"));
      } else if (this.form.allotStockList && this.form.allotStockList.length > 0 && (Number(value) > this.form.allotStockList[0].availableCount)) {
        callback(new Error("调拨数量不能大于所选物料的库存数量"));
      } else {
        callback();
      }
    };
    let validateAllotStockList= (rule, value, callback) => {
      if (value === null || (value && value.length === 0)) {
        callback(new Error("请选择需要调拨的物料"));
      } else if (this.form.allotNum !== null && this.form.allotNum !== '' && (Number(this.form.allotNum) > value[0].availableCount)) {
        callback(new Error("调拨数量不能大于所选物料的库存数量"));
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
      // 盘点计划表格数据
      allotPlanList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code : null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        outWarehouseId: [{ required: true, message: "请选择原仓库", trigger: "change" }],
        allotNum: [{ required: true, trigger: "blur", validator: validateAllotNum }],
        allotStockList: [{ trigger: "change", validator: validateAllotStockList }],
      },
      outWarehouseList: [], //原仓库下拉list

      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'AllotPlan') {
        this.getList();
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    // 禁止选择
    checkSelectable(row, index) {
      return row.allotStatus == '1';
    },
    // 仓库列表
    getAllotDeptList(){
      listDept({ status: 0, delFlag: 0 }).then(res=>{
        this.outWarehouseList = res.data
      })
    },
    /** 查询调拨列表 */
    getList() {
      this.loading = true;
      listAllotPlan(this.queryParams).then(response => {
        this.allotPlanList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.open = true;
      this.title = "新增调拨单";
      this.getAllotDeptList();
      this.form = {
        materialId: null,
        stockId: null,
        batchCode: null,
        allotNum: null,
        outWarehouseId: null,
        outWarehouseName: null,
        inWarehouseId: null,
        inWarehouseName: null,
        allotStockList: []
      };
      this.$nextTick(() => {
        this.reset();
        this.form.inWarehouseId = store.getters.deptId;
        this.form.inWarehouseName = store.getters.deptName;
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.getAllotDeptList();
      const id = row.id || this.ids
      this.form = {
        materialId: null,
        stockId: null,
        batchCode: null,
        allotNum: null,
        outWarehouseId: null,
        outWarehouseName: null,
        inWarehouseId: null,
        inWarehouseName: null,
        allotStockList: []
      };
      getCheckDelivery(id).then(response => {
        this.open = true;
        this.title = "修改调拨单";
        this.$nextTick(() => {
          this.reset();
          this.form = response.data;
        })
      });
    },
    // 打开配置明细
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;
      let title = "调拨审核";

      this.$refs.detailCom.detailTitle = title;
      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    /** 原仓库改变事件 */
    handleDeptChange(val){
      let info = this.outWarehouseList.find((item) => { return item.deptId == val })
      this.form.outWarehouseName = info.deptName
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let params = JSON.parse(JSON.stringify(this.form))
          params.materialId = this.form.allotStockList[0].materialId
          params.stockId = this.form.allotStockList[0].id
          params.batchCode = this.form.allotStockList[0].batchCode
          delete params.allotStockList
          if (this.form.id != null) {
            updateAllotPlan(params).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            })
          } else {
            addAllotPlan(params).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            })
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let codes = []
      this.allotPlanList.forEach(item => {
        if (this.ids.indexOf(item.id) > -1) {
          codes.push(item.code)
        }
      })
      this.$modal.confirm('是否确认删除调拨单号为"' + codes.toString() + '"的数据项？').then(function() {
        return delAllotPlan(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/allot/export', {...this.queryParams}, `allotPlanExport_${new Date().getTime()}.xlsx`)
    },
    // 打开选择物料弹窗
    materialComOpen(index) {
      if(!this.form.outWarehouseId){
        this.$message.warning("请选择原仓库");
        return;
      }
      this.$refs.materialCom.open = true;
      this.$refs.materialCom.deptId = this.form.outWarehouseId;
    },
    //选择物料回调
    setMaterial(material) {
      let allotStockList = JSON.parse(JSON.stringify(this.form.allotStockList));
      let flag = allotStockList.length > 0 && allotStockList[0].materialId !== material.materialId || (allotStockList.length === 0)
      if(flag){
        let list = [{...material}]
        this.form.allotStockList = list;
        this.$forceUpdate()
      }
      this.$refs.materialCom.open = false;
    },
    //详情提交
    setDetail(data){
      let params = {
        id: data.id,
        allotStatus: data.flag,
        remark: data.remark
      }
      executeAllot(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("审核成功");
          this.$refs.detailCom.open = false;
          this.$refs.detailForm = {}
          this.getList();
        }
      });
    },
  }
};
</script>
