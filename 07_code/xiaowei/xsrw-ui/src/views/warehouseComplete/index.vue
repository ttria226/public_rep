<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="ASN单号" prop="code" label-width="80px">
        <el-input v-model="queryParams.code" placeholder="请输入ASN单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.in_delivery_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading="addLoading" @click="handleAdd" v-hasPermi="['inout:delivery:add']">新增ASN单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-printer" size="mini" :disabled="single" @click="handleLabelPrint" v-hasPermi="['inout:delivery:labelprint']">标签打印</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:delivery:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:delivery:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deliveryDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ASN单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="单据类型" align="center" prop="type" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_type" :value="scope.row.type" />
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
          <!-- <dict-tag :options="dict.type.in_delivery_status" :value="scope.row.status" /> -->
          <span :style="{ color: scope.row.status == '2' ? 'red' : 'green' }" v-for="item in dict.type.in_delivery_status" :key="item.value" v-show="scope.row.status == item.value">{{ item.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="auditor" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="创建日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 3)" v-hasPermi="['inout:delivery:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status == '1' || scope.row.status == '2'" @click="handleUpdate(scope.row)" v-hasPermi="['inout:delivery:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '1' || scope.row.status == '2'" @click="handleDelete(scope.row)" v-hasPermi="['inout:delivery:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-finished" v-if="scope.row.status == '2' || scope.row.status == '5'" @click="handleDetailed(scope.row, 2)" v-hasPermi="['inout:delivery:register']">收货</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-checked" v-if="scope.row.status == '4'" @click="handleDetailed(scope.row, 1)" v-hasPermi="['inout:delivery:check']">上架</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改ASN单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="padding-right: 30px">
        <el-row :gutter="20" class="mb8">
          <el-col :span="8">
            <el-form-item label="单据类型" prop="type">
              <el-select style="width: 100%;" v-model="form.type" placeholder="请选择单据类型" class="select-input-form">
                <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="物料使用部门">
              <el-input v-model="form.deptName" disabled placeholder="请输入物料使用部门" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="制单人">
              <el-input v-model="form.createBy" disabled placeholder="请输入制单人" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb8">
          <el-col :span="12">
            <el-form-item label="物料BOM组合" prop="bomName">
              <el-input v-model="form.bomName" placeholder="请选择物料BOM组合" @focus="materialComOpen" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计入库数量" prop="bomCount">
              <el-input v-model="form.bomCount" v-intNumber placeholder="请输入预计入库数量" :maxlength="6" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="30px" prop="deliveryDetailList">
          <el-table v-loading="loading" :data="form.deliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="预计入库数量" align="center" prop="showPredictCount" width="150"></el-table-column>
            <el-table-column label="生产日期" align="center" prop="producedDate" min-width="200">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.producedDate'" v-if="isAuth" :rules="detailRules.producedDate">
                  <el-date-picker style="width: 100%;" clearable v-model="scope.row.producedDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择生产日期" @change="val => handleDateChange(val,scope.$index,scope.row)"></el-date-picker>
                </el-form-item>
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.producedDate'" v-else>
                  <el-date-picker style="width: 100%;" clearable v-model="scope.row.producedDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择生产日期" @change="val => handleDateChange(val,scope.$index,scope.row)"></el-date-picker>
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
            <!-- <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteDetailed(scope.row,scope.$index)">删除</el-button>
              </template>
            </el-table-column> -->
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
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
    <!-- 选择物料Bom -->
    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>
    <!-- 生成上架任务 -->
    <ArriveTaskCom ref="arriveTaskCom" @setArriveTask="setArriveTask" @showMaterialRfid="showMaterialRfid" @setControl="setControl"></ArriveTaskCom>
    <DetailCom ref="detailCom" @templateGet="templateGet" @setDetail="setDetail"></DetailCom>
    <!-- 选择物料rfid -->
    <material-rfid ref="materialRfidCom" @setMaterialRfid="setMaterialRfid"></material-rfid>
  </div>
</template>

<script>
import { listInDelivery, getInDelivery, delInDelivery, addInDelivery, updateInDelivery, registerInDelivery, getTemplateSelectList, getTemplateMaterialList } from "@/api/inoutDelivery/inDelivery";

import { getRuleStatus } from "@/api/base/rule";

import { wms } from "@/utils/agent";
import store from "@/store";

import MaterialCom from "./components/material";
import DetailCom from "./components/detail";
import ArriveTaskCom from "./components/arriveTask";
import MaterialRfid from '../warehouseControl/components/materialRfid';

import print from 'vue-print-nb'

export default {
  name: "warehouseComplete",
  dicts: ["in_delivery_type", "in_delivery_status","in_delivery_complete_status", "in_delivery_origin","in_delivery_detection_fail_type","wms_material_detail_check"],
  components: { MaterialCom, DetailCom, ArriveTaskCom,MaterialRfid },
  directives: {
    print,
  },
  data() {
    let validateMaterial = (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("请选择物料BOM组合"));
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
    // let validateProducedDate = (rule, value, callback) => {
    //   if (value === null || value === '') {
    //     callback(new Error("生产日期不能为空"));
    //     this.$message.error("生产日期不能为空")
    //   } else {
    //     callback();
    //   }
    // };
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
      // 齐套入库表格数据
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
        status: null,
        deliveryModule: null
      },
      // 表单参数
      form: {
        deliveryDetailList: []
      },
      // 表单校验
      rules: {
        type: [{ required: true, message: "请选择单据类型", trigger: "change" }],
        bomName: [
            // { required: true, message: "请选择物料BOM组合", trigger: "change" },
            { required: true, trigger: "change", validator: validateMaterial },
        ],
        bomCount: [
          { required: true, message: "请输入物料bom入库数量", trigger: "blur" },
        ],
      },
      detailRules: {
        materialCode: [
          // { required: true, message: "请选择物料编码", trigger: "change" },
          { required: true, trigger: "change", validator: validateMaterial },
        ],
        materialName: [
          { required: true, message: "请选择物料名称", trigger: "change" },
        ],
        batchCode: [
          { required: true, message: "请输入批次号", trigger: "blur" },
        ],
        predictCount: [
          // { required: true, message: "请输入预计入库数量", trigger: "blur" },
          { required: true, trigger: "blur", validator: validatePredictCount },
        ],
        producedDate: [
          // { trigger: "change", validator: validateProducedDate },
          { required: true, message: "请选择生产日期", trigger: "change" },
        ],
      },
      deliveryDetailList: [], // 配置明细

      detailedOpen: false, //详情标识
      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情

      detailIndex: 0, //操作详情角标
      addLoading: false, // 新增按钮loading

      templateList: [], //标签打印-模版列表

      templateOpen: false,//标签打印弹窗标识
      templateForm: {
        templateType: 1,
        templateImg: '',
        deliveryDetailList: []
      },

      dialogLoading: false, //弹窗loading

      isAuth: false, //是否生产日期必填
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "warehouseComplete") {
        this.getList();
      }
    },
    'form.bomCount': {
      handler(val){
        if(val){
          this.form.deliveryDetailList.map((item) => {
            item.showPredictCount = item.predictCount * Number(val)
          })
        } else {
          this.form.deliveryDetailList.map((item) => {
            item.showPredictCount = item.predictCount
          })
        }
        this.$forceUpdate()
      }
    }
  },
  created() {
    this.getAuth();
    this.getList();
  },
  methods: {
    // 已收货禁止选择
    checkSelectable(row, index) {
      return row.status == '1' || row.status == '9';
    },
    /** 查询齐套入库列表 */
    getList() {
      this.loading = true;
      this.queryParams.deliveryModule = '1'
      listInDelivery(this.queryParams).then((response) => {
        this.deliveryDetailList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取是否校验生产日期 */
    getAuth(){
      getRuleStatus(1).then(res => {
        this.isAuth = res.data == '1' ? true : false
      })
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
      this.open = true;
      this.title = "添加ASN单";
      this.form = {
        id: null,
        type: null,
        deptName: null,
        bomId: null,
        bomName: null,
        bomCount: null,
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
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id || this.ids;
      this.form = {
        id: null,
        type: null,
        deptName: null,
        bomId: null,
        bomName: null,
        bomCount: null,
        createBy: null,
        deliveryDetailList: []
      };
      getInDelivery(id).then((response) => {
        response.data.deliveryDetailList.map((item) => {
          if(response.data.bomCount){
            item.predictCount = item.predictCount / response.data.bomCount
          }
        })
        this.open = true;
        this.title = "修改ASN单";
        this.$nextTick(() => {
          this.reset();
          this.form = response.data;
        })
      });
    },
    /** 提交按钮 */
    submitForm() {
      // this.dialogLoading = true
      this.$message.closeAll()
      if(this.form.deliveryDetailList.length == 0){
        this.$message.warning("入库物料明细不能为空！")
        return false
      }
      this.inputIndex = ''
      let errorMessage = []
      this.form.deliveryDetailList.map((item,index) => {
        if((item.producedDate === null || item.producedDate === '') && this.isAuth){
          errorMessage.push(`物料【${(item.materialName)}】行的生产日期不能为空`)
        }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let params = JSON.parse(JSON.stringify(this.form))
          params.deliveryDetailList.map((item) => {
            item.predictCount = item.predictCount * Number(params.bomCount)
          })
          params.deliveryModule = '1'
          if (this.form.id != null) {
            updateInDelivery(params).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              // this.dialogLoading = false
            });
          } else {
            addInDelivery(params).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              // this.dialogLoading = false
            });
          }
        } else {
          // this.dialogLoading = false
        }
      });
    },
    // 打开配置明细
    handleDetailed(row, type) {
      if(type == 1){
        getInDelivery(row.id).then((response) => {
          response.data.deliveryDetailList.map((item) => {
            if(response.data.bomCount){
              item.predictCount = item.predictCount / response.data.bomCount
            }
          })
          this.$refs.arriveTaskCom.open = true
          this.$refs.arriveTaskCom.detailId = row.id
          this.$refs.arriveTaskCom.detailType = '1'
          // this.$refs.arriveTaskCom.trayType = res.data !== null ? res.data+"" : ""
          this.$refs.arriveTaskCom.materialList = [...response.data.deliveryDetailList]
        });
      } else {
        this.$refs.detailCom.detailId = row.id;
        this.detailId = row.id;
        this.detailType = type
        this.$refs.detailCom.detailType = type;
        let title = "入库单详情";

        this.$refs.detailCom.detailTitle = title;
        this.$refs.detailCom.detailForm = {};
        this.$refs.detailCom.open = true;
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let tips = "";
      if (row.id) {
        tips = '是否确认删除ASN单号为"' + row.code + '"的数据项？';
      } else {
        let codes = [];
        this.deliveryDetailList.forEach((item) => {
          if (this.ids.indexOf(item.id) > -1) {
            codes.push(item.code);
          }
        });
        tips = '是否确认删除ASN单号为"' + codes.toString() + '"的数据项？';
      }
      this.$modal.confirm(tips).then(function () {
        return delInDelivery(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/delivery/export", {...this.queryParams,}, `inDelivery_${new Date().getTime()}.xlsx`);
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
        producedDate: null,
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
    materialComOpen() {
      this.$refs.materialCom.open = true;
      this.$refs.materialCom.queryParams.contactsUnitId = this.form.unitCode;
      // this.detailIndex = index
    },
    setMaterial(material) {
      let deliveryDetailList = JSON.parse(JSON.stringify(material.bomDetail));
      let resultList = []
      deliveryDetailList.map((item) => {
        let info = {
          materialId: item.id,
          materialCode: item.code,
          materialName: item.name,
          unitName: item.unitName,
          unitId: item.unitId,
          batchCode: item.batchCode,
          predictCount: item.predictCount,
          showPredictCount: this.form.bomCount ? (item.predictCount * Number(this.form.bomCount)) : item.predictCount,
          producedDate: null,
        }
        resultList.push(info)
      })
      this.form.bomId = material.bomId
      this.form.bomName = material.bomName
      this.form.deliveryDetailList = resultList
    },
    //详情提交
    setDetail(data){
      let params = {}
      let deliveryDetailList = []
      let formDeliveryDetailList = []
      params.id = data.id;
      deliveryDetailList = JSON.parse(JSON.stringify(data.deliveryDetailList))
      deliveryDetailList.map((item) => {
        let info = {
          id: item.id,
          registrationCount: item.registrationCount
        }
        formDeliveryDetailList.push(info)
      })
      params.deliveryDetailList = formDeliveryDetailList;
      registerInDelivery(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("收货成功");
          this.$refs.detailCom.open = false;
          this.$refs.detailForm = {}
          this.getList();
        }
      });
    },
    //上架成功后的操作
    setArriveTask(flag) {
      if(flag){
        this.getList()
      }
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
    //日期选择变化事件
    handleDateChange(val,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info.producedDate = val
      this.inputIndex = index
      this.$set(this.form.deliveryDetailList,index,info)
    },
    //输入框变化事件
    handleDialogInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.testForm.deliveryDetailList,index,info)
    },
    //选择变化事件
    handleDialogSelectChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val
      this.$set(this.testForm.deliveryDetailList,index,info)
    },
    // 新的物料选择 李元辉 2023.10.30
    //执行成功后的回调
    setControl(){
      this.getList();
    },
    //选择物料rfid
    showMaterialRfid(item){
      this.$refs.materialRfidCom.id = item.id
      this.$refs.materialRfidCom.reActualCount = item.reActualCount
      this.$refs.materialRfidCom.open = true
    },
    //选择物料rfid成功后的回调
    setMaterialRfid(materials){
      let list = JSON.parse(JSON.stringify(this.$refs.arriveTaskCom.form.taskInList))
      list.map((item) => {
        if(item.advanceRegistrationId == materials.id){
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
      this.$refs.arriveTaskCom.form.taskInList = list
      this.$refs.materialRfidCom.open = false
      this.$forceUpdate()
    },
  },
};
</script>
