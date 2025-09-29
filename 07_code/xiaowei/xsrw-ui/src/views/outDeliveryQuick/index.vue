<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="出库单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入出库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="出库类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择出库类型">
          <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="出库单状态" prop="status" label-width="90px">
        <el-select v-model="queryParams.status" clearable placeholder="请选择出库单状态">
            <el-option v-for="dict in dict.type.inout_out_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inout:deliveryOut:add']">新增出库计划</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:deliveryOut:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:deliveryOut:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outDeliveryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" />
      <el-table-column label="出库单号" align="center" prop="code" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="出库类型" align="center" prop="type" :show-overflow-tooltip="true" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <el-table-column label="物料使用部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="申请人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100"/>
      <el-table-column label="来源" align="resource" prop="newLocal" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_local" :value="scope.row.newLocal" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="出库状态" align="center" prop="completeState" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.complete_state" :value="scope.row.completeState" />
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="auditor" :show-overflow-tooltip="true" min-width="100"/>
      <el-table-column label="创建日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="120"/>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" min-width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 2)" v-hasPermi="['inout:deliveryOut:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status == '1'" @click="handleUpdate(scope.row)" v-hasPermi="['inout:deliveryOut:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '1'" @click="handleDelete(scope.row)" v-hasPermi="['inout:deliveryOut:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.status == '1'" @click="handleDetailed(scope.row, 1)" v-hasPermi="['inout:deliveryOut:approve']">审核</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.status == '2'" @click="handleArriveTask(scope.row,1)">执行出库</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-box" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleFloorDiaplayTask(scope.row,1)">地堆拣货</el-button> -->
          <!-- <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.status == '2'" @click="handleExecute(scope.row,3)" v-hasPermi="['inout:task:executeOutTask']">强制执行</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

      <!-- 添加或修改出库单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="55%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20" class="mb8">
          <el-col :span="12">
            <el-form-item label="出库类型" prop="type">
              <el-select style="width: 100%;" v-model="form.type" placeholder="请选择出库类型" class="select-input-form">
                <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请部门">
              <el-input v-model="form.deptName" disabled placeholder="请输入申请部门" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请人">
              <el-input v-model="form.createBy" disabled placeholder="请输入申请人" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="待出库区">
              <el-select style="width: 100%;" v-model="form.reservoirId" filterable placeholder="请选择待出库区" clearable>
                <el-option v-for="dict in reservoirList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="5" placeholder="请输入内容" show-word-limit maxlength="250" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10" class="mb8" style="margin-left: 25px;">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addDetailed()" >新增行</el-button>
          </el-col>
        </el-row>
        <el-form-item prop="tOutDeliveryDetailList" label-width="30px">
          <el-table v-loading="loading" :data="form.tOutDeliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'tOutDeliveryDetailList.' + scope.$index + '.materialCode'" :rules="detailRules.materialCode">
                  <el-input v-model="scope.row.materialCode" placeholder="请选择物料编码" :disabled="title === '修改出库计划' && scope.row.id !== null" size="small" @focus="materialComOpen(scope.$index)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="预计出库数量" align="center" prop="predictCount" width="180">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'tOutDeliveryDetailList.' + scope.$index + '.predictCount'">
                  <el-input v-model="scope.row.predictCount" placeholder="请输入预计出库数量" v-intNumber maxlength="6" size="small" @blur="val => handleInputChange(val,'predictCount',scope.$index,scope.row)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="计量单位" align="center" prop="materialUnit" width="100"></el-table-column>
            <!-- <el-table-column label="批次号" align="center" prop="batchCode" width="200"></el-table-column> -->
<!--            <el-table-column label="小件领取数量" align="center" prop="smallPredictCount" width="180">-->
<!--              <template slot-scope="scope">-->
<!--                <el-form-item label-width="0px" :prop="'tOutDeliveryDetailList.' + scope.$index + '.smallPredictCount'" v-if="scope.row.minUnitName">-->
<!--                  <el-input v-model="scope.row.smallPredictCount" placeholder="请输入小件领取数量" v-intNumber maxlength="6" size="small" @blur="val => handleInputChange(val,'smallPredictCount',scope.$index,scope.row)">-->
<!--                    <template slot="append">{{ scope.row.minUnitName }}</template>-->
<!--                  </el-input>-->
<!--                </el-form-item>-->
<!--              </template>-->
<!--            </el-table-column>-->
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

    <!-- 执行出库 -->
    <ArriveTaskCom ref="arriveTaskCom" @setArriveTask="setArriveTask"></ArriveTaskCom>
    <!-- 地堆拣货 -->
    <floor-display-task ref="floorDisplayTaskCom" @setArriveTask="setArriveTask"></floor-display-task>
    <!-- 详情/执行/重新执行/强制执行 -->
    <ExecuteDetailCom ref="executeDetailCom" @setControl="setControl" @showMaterialRfid="showMaterialRfid"></ExecuteDetailCom>
    <!-- 选择物料rfid -->
    <material-rfid ref="outMaterialRfidCom" @setMaterialRfid="setMaterialRfid"></material-rfid>
    <DetailCom ref="detailCom" @setDetail="setDetail"></DetailCom>
    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>
  </div>
</template>

<script>
import { listOutDeliveryQuick, getOutDelivery, addOutDelivery, updateOutDelivery, delOutDelivery, approveOutDelivery } from "@/api/inoutDelivery/outDelivery";

import { listReservoir } from "@/api/wms/reservoir";

import { wms } from "@/utils/agent";
import store from "@/store";

import ArriveTaskCom from "./components/arriveTask";
import FloorDisplayTask from './components/floorDisplayTask';
import ExecuteDetailCom from "./components/executeDetail";
import MaterialRfid from '../outDeliveryControl/components/materialRfid';
import DetailCom from "./components/detail";
import MaterialCom from '../outDeliveryPlan/components/material';

export default {
  name: "outDeliveryQuick",
  dicts: ["inout_out_local", "inout_out_type", "inout_out_status","complete_state"],
  components: { ArriveTaskCom, FloorDisplayTask, ExecuteDetailCom, MaterialRfid, DetailCom, MaterialCom },
  data() {
    let validatePredictCount = (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("预计出库数量不能为空"));
        this.$message.error("预计出库数量不能为空")
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
      // 出库单表格数据
      outDeliveryList: [],
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
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        type: [{ required: true, message: "请选择出库类型", trigger: "blur" }],
      },
      detailRules: {
        materialCode: [
          { required: true, message: "请选择物料编码", trigger: "change" },
        ],
        materialName: [
          { required: true, message: "请选择物料名称", trigger: "change" },
        ],
        predictCount: [
          // { required: true, message: "请输入预计入库数量", trigger: "blur" },
          { trigger: "blur", validator: validatePredictCount },
        ],
      },

      detailedOpen: false, //详情标识
      detailedId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情
      detailIndex: 0, //操作详情角标

      reservoirList: [], //库区list
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "outDeliveryQuick") {
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
        return row.status == "1";
    },
    /** 查询预先发货清单列表 */
    getList() {
      this.loading = true;
      listOutDeliveryQuick(this.queryParams).then((response) => {
        this.outDeliveryList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询库区列表 */
    getReservoirList() {
      let qps = {
        pageNum: 1,
        pageSize: 5000,
      };
      listReservoir(qps).then(response => {
        this.reservoirList = response.rows;
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.getReservoirList()
      this.open = true;
      this.title = "添加出库计划";
      this.form = {
        id: null,
        type: null,
        deptName: null,
        createBy: null,
        reservoirId: null,
        remark: null,
        deliveryModule: '3',
        tOutDeliveryDetailList: []
      };
      this.$nextTick(() => {
        this.reset();
        this.form.deptId = store.getters.deptId;
        this.form.deptName = store.getters.deptName;
        this.form.createBy = store.getters.name;
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.getReservoirList();
      const id = row.id || this.ids;
      this.form = {
        id: null,
        type: null,
        deptName: null,
        createBy: null,
        reservoirId: null,
        remark: null,
        deliveryModule: '3',
        tOutDeliveryDetailList: []
      };
      getOutDelivery(id).then((response) => {
        this.open = true;
        this.title = "修改出库计划";
        this.$nextTick(() => {
          this.reset();
          this.form = response.data;
        })
      });
    },
    //输入框变化事件
    handleInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.form.tOutDeliveryDetailList,index,info)
    },
    /** 提交按钮 */
    submitForm() {
      this.$message.closeAll()
      if(this.form.tOutDeliveryDetailList.length == 0){
        this.$message.warning("出库物料明细不能为空！")
        return false
      }
      this.inputIndex = ''
      let errorMessage = []
      this.form.tOutDeliveryDetailList.map((item,index) => {
        if(item.materialCode === null || item.materialCode === ''){
          errorMessage.push(`第【${(index + 1)}】行的物料不能为空`)
        } else if((item.predictCount === null || item.predictCount === '') && (item.minUnitName && (item.smallPredictCount === null || item.smallPredictCount === ''))){
          errorMessage.push(`物料【${(item.materialName)}】行的预计出库数量和小件领取数量不能都为空`)
        } else if((item.predictCount === null || item.predictCount === '') && !item.minUnitName){
          errorMessage.push(`物料【${(item.materialName)}】行的预计出库数量不能为空`)
        }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateOutDelivery(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOutDelivery(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 新增明细操作 */
    addDetailed() {
      this.form.tOutDeliveryDetailList.push({
        id: null,
        materialCode: '',
        materialId: '',
        materialName: '',
        unitId: '',
        materialUnit: '',
        minUnitName: '',
        predictCount: null,
        smallPredictCount: null,
      });
    },
    /** 删除明细操作 */
    deleteDetailed(row,index) {
      this.$modal.confirm("是否确认删除此明细").then(() => {
        this.form.tOutDeliveryDetailList.splice(index,1);
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
    setMaterial(material) {
      let tOutDeliveryDetailList = JSON.parse(JSON.stringify(this.form.tOutDeliveryDetailList));
      let flag = tOutDeliveryDetailList.every((item,index) => {
        if(index != this.detailIndex) {
          return item.materialId != material.id
        } else {
          return true
        }
      })
      if(flag){
        tOutDeliveryDetailList[this.detailIndex].materialId = material.id ? material.id : "";
        tOutDeliveryDetailList[this.detailIndex].materialCode = material.code ? material.code : "";
        tOutDeliveryDetailList[this.detailIndex].materialName = material.name ? material.name : "";
        tOutDeliveryDetailList[this.detailIndex].materialUnit = material.unitName ? material.unitName : "";
        tOutDeliveryDetailList[this.detailIndex].unitId = material.unitId ? material.unitId : "";
        tOutDeliveryDetailList[this.detailIndex].minUnitName = material.minUnitName ? material.minUnitName : "";
        tOutDeliveryDetailList[this.detailIndex].predictCount = material.predictCount ? material.predictCount : "";
        tOutDeliveryDetailList[this.detailIndex].smallPredictCount = material.smallPredictCount ? material.smallPredictCount : "";
        this.form.tOutDeliveryDetailList = tOutDeliveryDetailList;
        this.$forceUpdate()
      } else {
        this.$message.error(`已有【${material.name}】物料在明细中，请重新选择！`)
      }
    },
    //详情提交
    setDetail(data){
      let params = {}
      switch(this.detailType){
        case 1:
          params.id = data.id;
          params.status = data.flag;
          params.checkRemark = data.checkRemark;
          approveOutDelivery(params).then(res => {
            if(res.code === 200){
              this.$modal.msgSuccess("审核成功");
              this.$refs.detailCom.open = false;
              this.$refs.detailForm = {}
              this.getList();
            }
          });
          break;
      }
    },
    /** 执行出库按钮操作 */
    handleArriveTask(row,type) {
      this.$refs.arriveTaskCom.open = true
      this.$refs.arriveTaskCom.type = type
      this.$refs.arriveTaskCom.materialList = [{...row}]
    },
    /** 地堆拣货按钮操作 */
    handleFloorDiaplayTask(row,type){
      this.$refs.floorDisplayTaskCom.open = true
      this.$refs.floorDisplayTaskCom.type = type
      this.$refs.floorDisplayTaskCom.materialList = [{...row}]
    },
    // 打开配置明细
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;

      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    //执行方法
    handleExecute(row,type){
      this.$refs.executeDetailCom.open = true;
      this.$refs.executeDetailCom.detailType = type;
      this.detailedId = row.id;
      this.$refs.executeDetailCom.detailId = row.id;
    },
    //选择物料rfid
    showMaterialRfid(item){
      this.$refs.outMaterialRfidCom.locationId = item.locationId
      this.$refs.outMaterialRfidCom.batchCode = item.batchCode
      this.$refs.outMaterialRfidCom.open = true
    },
    //选择物料rfid成功后的回调
    setMaterialRfid(materials){
      let list = JSON.parse(JSON.stringify(this.$refs.executeDetailCom.detailForm.taskWcsDetailVOList))
      list.map((item) => {
        if(item.batchCode == materials.batchCode){
          if(item.rfids){
            let rfidsList = [...item.rfids]
            materials.rfids.map((materialRfidInfo) => {
              let info = rfidsList.find((itemRfidInfo) => { return itemRfidInfo == materialRfidInfo })
              if(!info){
                rfidsList.push(materialRfidInfo)
              }
            })
            item.rfids = rfidsList
            item.rfidString = rfidsList.toString()
          } else {
            item.rfids = materials.rfids
            item.rfidString = materials.rfids.toString()
          }
        }
      })
      this.$refs.executeDetailCom.detailForm.taskWcsDetailVOList = list
      this.$refs.outMaterialRfidCom.open = false
      this.$forceUpdate()
    },
    //执行成功后的回调
    setControl(){
      this.getList();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let tips = "";
      if (row.id) {
        tips = '是否确认删除入库单号为"' + row.code + '"的数据项？';
      } else {
        let codes = [];
        this.outDeliveryList.forEach((item) => {
          if (this.ids.indexOf(item.id) > -1) {
            codes.push(item.code);
          }
        });
        tips = '是否确认删除入库单号为"' + codes.toString() + '"的数据项？';
      }
      this.$modal.confirm(tips).then(function () {
        return delOutDelivery(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/deliveryOut/export", {...this.queryParams,}, `deliveryOut_${new Date().getTime()}.xlsx`);
    },
    //上架成功后的操作
    setArriveTask(flag) {
      if(flag){
        this.getList()
      }
    },
  },
};
</script>
