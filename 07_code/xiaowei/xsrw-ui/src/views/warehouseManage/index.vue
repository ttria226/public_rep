<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入库单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入入库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="入库类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择入库类型">
          <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="入库单状态" prop="status" label-width="90px">
        <el-select v-model="queryParams.status" clearable placeholder="请选择入库单状态">
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading="addLoading" @click="handleAdd" v-hasPermi="['inout:delivery:add']">新增入库单</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="primary" plain icon="el-icon-printer" size="mini" :disabled="single" @click="handleLabelPrint" v-hasPermi="['inout:delivery:labelprint']">标签打印</el-button>-->
<!--      </el-col>-->
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
      <el-table-column label="入库单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="入库类型" align="center" prop="type" :show-overflow-tooltip="true" min-width="100">
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
          <dict-tag :options="dict.type.in_delivery_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="完成状态" align="center" prop="completeState" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_complete_status" :value="scope.row.completeState" />
        </template>
      </el-table-column> -->
      <el-table-column label="审核人" align="center" prop="auditor" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="创建日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" min-width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 4)" v-hasPermi="['inout:delivery:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status == '1'" @click="handleUpdate(scope.row)" v-hasPermi="['inout:delivery:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '1'" @click="handleDelete(scope.row)" v-hasPermi="['inout:delivery:remove']">删除</el-button>
<!--          <el-button size="mini" type="text" icon="el-icon-printer" v-if="scope.row.status == '2' || scope.row.status == '5'" @click="handlePrint(scope.row)" v-hasPermi="['inout:delivery:labelprint']">打印</el-button>-->
          <el-button size="mini" type="text" icon="el-icon-finished" v-if="scope.row.status == '2' || scope.row.status == '5'" @click="handleDetailed(scope.row, 3)" v-hasPermi="['inout:delivery:register']">登记</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-checked" v-if="scope.row.status == '4'" @click="handleDetailed(scope.row, 2)" v-hasPermi="['inout:delivery:check']">检测</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-checked" v-if="scope.row.status == '4'" @click="handleFinishTest(scope.row)" v-hasPermi="['inout:delivery:check']">快捷检验</el-button>
          <el-button size="mini" type="text" icon="el-icon-user" v-if="scope.row.status == '1'" @click="handleDetailed(scope.row, 1)" v-hasPermi="['inout:delivery:approve']">审核</el-button>
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
                <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
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
          <el-col :span="12">
            <el-form-item label="预计收货日期">
              <el-date-picker style="width: 100%;" clearable v-model="form.planDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择预计收货日期"></el-date-picker>
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
                  <el-input-number v-model="scope.row.predictCount" :precision="3" :step="0.001" placeholder="请输入预计入库数量" :max="999999" :min="0.001" size="large" @blur="val => handleInputChange(val,'predictCount',scope.$index,scope.row)"></el-input-number>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="到期日期" align="center" prop="expireDate" min-width="200">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.expireDate'">
                  <el-date-picker style="width: 100%;" clearable v-model="scope.row.expireDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择到期日期" @change="val => handleExpireDateChange(val,scope.$index,scope.row)"></el-date-picker>
                </el-form-item>
              </template>
            </el-table-column>
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
               <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.batchCode'" :rules="detailRules.batchCode">
                  <el-input v-model="scope.row.batchCode" placeholder="请输入批次号" size="small" maxlength="10"></el-input>
                </el-form-item>
              </template>
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
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 入库检测对话框 -->
    <el-dialog title="入库检测" :visible.sync="testOpen" width="60%" append-to-body>
<!--      <el-form :model="dialogQueryParams" ref="dialogQueryForm" size="small" :inline="true" label-width="80px">-->
<!--        <el-form-item label="物料" prop="materialId">&lt;!&ndash; prop="materialCode" &ndash;&gt;-->
<!--          &lt;!&ndash; <el-input v-model="dialogQueryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleDialogQuery" /> &ndash;&gt;-->
<!--          <el-select v-model="dialogQueryParams.materialId" placeholder="请选择物料">-->
<!--            <el-option v-for="item in testRegistrationList" :key="item.id" :label="item.materialName" :value="item.id" />-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        &lt;!&ndash; <el-form-item label="物料名称" prop="materialName">-->
<!--          <el-input v-model="dialogQueryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleDialogQuery" />-->
<!--        </el-form-item> &ndash;&gt;-->
<!--        <el-form-item label="RFID标识" prop="rfid">-->
<!--          <el-input v-model="dialogQueryParams.rfid" placeholder="请输入RFID标识" clearable @keyup.enter.native="handleDialogQuery" />-->
<!--        </el-form-item>-->
<!--        <el-form-item>-->
<!--          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleDialogQuery">搜索</el-button>-->
<!--          <el-button icon="el-icon-refresh" size="mini" @click="resetDialogQuery">重置</el-button>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
      <el-form ref="testForm" :model="testForm" label-width="00px" style="padding-right: 30px">
        <el-form-item prop="deliveryDetailList">
          <el-table v-loading="dialogLoading" :data="testForm.deliveryDetailList">
<!--            <el-table-column label="编码" align="center" prop="rfid" min-width="150"></el-table-column>-->
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="100"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="100"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" min-width="90"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" min-width="100"></el-table-column>
<!--            <el-table-column label="RFID" align="center" prop="rfidHead" min-width="150"></el-table-column>-->
            <el-table-column label="检测状态" align="center" prop="detectionFailStatus" min-width="120">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.wms_material_detail_check" :value="scope.row.detectionFailStatus" />
              </template>
            </el-table-column>
            <el-table-column label="登记数量" align="center" prop="registrationCount" width="210"></el-table-column>
            <el-table-column label="未通过数量" align="center" prop="failCount" width="210">
            <template slot-scope="scope">
              <el-form-item label-width="0px">
<!--                <el-input v-model="scope.row.detectionCount" placeholder="请输入备注" size="small" maxlength="250" @blur="val => handleDialogInputChange(val,'detectionFailRemark',scope.$index,scope.row)"></el-input>-->
                <el-input-number v-model="scope.row.failCount" :precision="3" :step="0.001" placeholder="请输入未通过数量" :max="999999" size="large" @blur="val => handleDialogInputChange(val,'failCount',scope.$index,scope.row)"></el-input-number>
              </el-form-item>
            </template>
            </el-table-column>
            <el-table-column label="未通过原因" align="center" prop="detectionFailType" width="210">
              <template slot-scope="scope">
                <el-form-item label-width="0px">
                  <el-select style="width: 100%;" v-model="scope.row.detectionFailType" placeholder="请选择未通过原因" @change="val => handleDialogSelectChange(val,'detectionFailType',scope.$index,scope.row)">
                    <el-option v-for="dict in dict.type.in_delivery_detection_fail_type" :key="dict.value" :label="dict.label" :value="dict.value" />
                  </el-select>
                  <!-- <el-input v-model="scope.row.detectionFailType" placeholder="请输入未通过原因" size="small" ></el-input> -->
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="detectionFailRemark" width="210">
              <template slot-scope="scope">
                <el-form-item label-width="0px">
                  <el-input v-model="scope.row.detectionFailRemark" placeholder="请输入备注" size="small" maxlength="250" @blur="val => handleDialogInputChange(val,'detectionFailRemark',scope.$index,scope.row)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="dialogTotal > 0" :total="dialogTotal" :page.sync="testPageNum" :limit.sync="testPageSize" @pagination="dialogPagination" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitTestForm">确 定</el-button>
        <el-button @click="testCancel">取 消</el-button>
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
    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>
    <DetailCom ref="detailCom" @templateGet="templateGet" @setDetail="setDetail"></DetailCom>
  </div>
</template>

<script>
import { listInDelivery, getInDelivery, delInDelivery, addInDelivery, updateInDelivery, approveInDelivery, checkInDelivery, registerInDelivery, getTemplateSelectList, getTemplateMaterialList, getTestRegistrationSelect, getTestRegistrationList, checkMaterialFail, checkDelivery } from "@/api/inoutDelivery/inDelivery";
import { printByAdvanceId } from "@/api/wms/detail";
import { getRuleStatus } from "@/api/base/rule";

import { wms } from "@/utils/agent";
import store from "@/store";

import MaterialCom from "./components/material";
import DetailCom from "./components/detail";

import print from 'vue-print-nb'

export default {
  name: "warehouseManage",
  dicts: ["in_delivery_type", "in_delivery_status","in_delivery_complete_status", "in_delivery_origin","in_delivery_detection_fail_type","wms_material_detail_check"],
  components: { MaterialCom, DetailCom },
  directives: {
    print,
  },
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
    let validateBatchCode = (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("批次号不能为空"));
        this.$message.error("批次号不能为空")
      } else {
        if (value.length != 10){
          callback(new Error("批次号长度为10位"));
          this.$message.error("批次号长度为10位")
        }else {
          let pattern = '^\\d{10}$';
          let reg = new RegExp(pattern);
          if (!reg.test(value)) {
            callback(new Error("批次号为10位数字"));
            this.$message.error("批次号为10位数字");
          }else {
            callback();
          }
        }
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
        buttonLoading:false,
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
        status: null,
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
          { trigger: "blur",validator: validateBatchCode },
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

      inputIndex: '',

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

      testOpen: false, //检测弹窗标识
      // 检测弹窗总条数
      dialogTotal: 0,
      // 检测弹窗查询参数
      dialogQueryParams: {
        pageNum: 1,
        pageSize: 10,
        materialId: null,
        // materialName: null,
        rfid: null,
      },
      testPageNum: 1,
      testPageSize: 10,
      // 检测弹窗表单参数
      testForm: {
        deliveryDetailList: []
      },

      testRegistrationList: [], //检测的物料下拉

      isAuth: false, //是否生产日期必填
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "warehouseManage") {
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
      return row.status == '1';
    },
    /** 查询预先发货清单列表 */
    getList() {
      this.loading = true;
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
        this.buttonLoading = false
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
      this.buttonLoading = false
      this.title = "添加入库单";
      this.getAuth();
      this.form = {
        id: null,
        type: null,
        deptName: null,
        createBy: null,
        planDate: null,
        remark: null,
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
      this.getAuth();
      const id = row.id || this.ids;
      this.form = {
        id: null,
        type: null,
        deptName: null,
        createBy: null,
        planDate: null,
        remark: null,
        deliveryDetailList: []
      };
      getInDelivery(id).then((response) => {
        this.open = true;
        this.title = "修改入库单";
        this.$nextTick(() => {
          this.reset();
          this.form = response.data;
        })
      });
    },
    /** 提交按钮 */
    submitForm() {
      // this.dialogLoading = true
        this.buttonLoading = true
      this.$message.closeAll()
      if(this.form.deliveryDetailList.length == 0){
        this.$message.warning("入库物料明细不能为空！")
        return false
      }
      this.inputIndex = ''
      let errorMessage = []
      let materialObjs = []
      this.form.deliveryDetailList.map((item,index) => {
        materialObjs.map((item2,index2) => {
          if(item.materialCode == item2.materialCode && item.batchCode == item2.batchCode){
            errorMessage.push(`物料【${(item.materialName)}】有相同批次！`)
            this.buttonLoading = false
          }
        })
        materialObjs.push(item)
        if(item.materialCode === null || item.materialCode === ''){
          errorMessage.push(`第【${(index + 1)}】行的物料不能为空`)
        } else if(item.predictCount === null || item.predictCount === ''){
          errorMessage.push(`物料【${(item.materialName)}】行的预计入库数量不能为空`)
        } else if((item.producedDate === null || item.producedDate === '') && this.isAuth){
          errorMessage.push(`物料【${(item.materialName)}】行的生产日期不能为空`)
        }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateInDelivery(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
                this.buttonLoading = false
              // this.dialogLoading = false
            });
          } else {
            addInDelivery(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
                this.buttonLoading = false
              // this.dialogLoading = false
            });
          }
        } else {
            this.buttonLoading = false
          // this.dialogLoading = false
        }
      });
    },
    // 打开配置明细
    handleDetailed(row, type) {
      if(type == '2'){
        this.testOpen = true
        this.detailId = row.id;
        this.resetForm("dialogQueryForm");
        this.dialogQueryParams.materialId = this.testRegistrationList && this.testRegistrationList.length > 0 ? this.testRegistrationList[0].id : ""
        this.testPageNum = 1
        this.testPageSize = 10
        this.getTestRegistrationSelectList(row)
      } else {
        this.$refs.detailCom.detailId = row.id;
        this.detailId = row.id;
        this.detailType = type
        this.$refs.detailCom.detailType = type;
        let title = type == 1 ? "入库单详情" : (type == 2 ? "入库单检测" : (type == 3 ? "入库单登记" : '入库单详情'));

        this.$refs.detailCom.detailTitle = title;
        this.$refs.detailCom.detailForm = {};
        this.$refs.detailCom.open = true;
      }
    },
    /** 获取检测的物料下拉列表 */
    getTestRegistrationSelectList(row){
      // console.log(this.detailId)
      // getTestRegistrationSelect(this.detailId).then((response) => {
      //   this.testRegistrationList = response.data;
      //   this.dialogQueryParams.materialId = response.data && response.data.length > 0 ? response.data[0].id : ""
      //   this.getDialogList()
      // });

      this.getDialogList(row)
    },
    /** 检测完成按钮操作 */
    handleFinishTest(row){
      const ids = row.id;
      let tips = '是否确认将该入库单标记为已检测状态？';
      this.$modal.confirm(tips).then(function () {
        return checkDelivery({ id: ids });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("快捷检验成功");
      }).catch(() => { });
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
    materialComOpen(index) {
      this.$refs.materialCom.open = true;
      this.$refs.materialCom.queryParams.contactsUnitId = this.form.unitCode;
      this.detailIndex = index
    },
    setMaterial(material) {
      let deliveryDetailList = JSON.parse(JSON.stringify(this.form.deliveryDetailList));
      let flag = deliveryDetailList.every((item,index) => {
        if(index != this.detailIndex) {
          return item.materialId != material.id
        } else {
          return true
        }
      })
      if(true){
        deliveryDetailList[this.detailIndex].materialId = material.id ? material.id : "";
        deliveryDetailList[this.detailIndex].materialCode = material.code ? material.code : "";
        deliveryDetailList[this.detailIndex].materialName = material.name ? material.name : "";
        deliveryDetailList[this.detailIndex].unitName = material.unitName ? material.unitName : "";
        deliveryDetailList[this.detailIndex].unitId = material.unitId ? material.unitId : "";
        // deliveryDetailList[this.detailIndex].batchCode = material.batchCode ? material.batchCode : "";
        this.form.deliveryDetailList = deliveryDetailList;
        this.$forceUpdate()
      } else {
        this.$message.error(`已有【${material.name}】物料在明细中，请重新选择！`)
      }
    },
    //详情提交
    setDetail(data){
      let params = {}
      let deliveryDetailList = []
      let formDeliveryDetailList = []
      switch(this.detailType){
        case 1:
          params.id = data.id;
          params.status = data.flag;
          params.auditRemark = data.auditRemark;
          approveInDelivery(params).then(res => {
            if(res.code === 200){
              this.$modal.msgSuccess("审核成功");
              this.$refs.detailCom.open = false;
              this.$refs.detailForm = {}
              this.getList();
            }
          });
          break;
        case 2:
          params.id = data.id;
          deliveryDetailList = JSON.parse(JSON.stringify(data.deliveryDetailList))
          deliveryDetailList.map((item) => {
            let info = {
              id: item.id,
              detectionFailType: item.detectionFailType,
              detectionFailRemark: item.detectionFailRemark,
              detectionCount: item.detectionCount
            }
            formDeliveryDetailList.push(info)
          })
          params.deliveryDetailList = formDeliveryDetailList;
          checkInDelivery(params).then(res => {
            if(res.code === 200){
              this.$modal.msgSuccess("检测成功");
              this.$refs.detailCom.open = false;
              this.$refs.detailForm = {}
              this.getList();
            }
          });
          break;
        case 3:
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
              this.$modal.msgSuccess("登记成功");
              this.$refs.detailCom.open = false;
              this.$refs.detailForm = {}
              this.getList();
            }
          });
          break;
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
    //rfid标签打印
    handlePrint(row){
      let params = {
        id: row.id
      }
      this.$modal.confirm('是否确认打印【'+ (row.code) +'】数据？').then(function() {
        return printByAdvanceId(params);
      }).then(() => {
        if (res.code == 200) {
          this.$modal.msgSuccess("打印成功");
        } else {
          this.$modal.msgSuccess(res.msg);
        }
      }).catch(() => {});
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
    /** 检测弹窗搜索按钮操作 */
    handleDialogQuery() {
      this.dialogQueryParams.pageNum = 1;
      this.getDialogList();
    },
    /** 检测弹窗重置按钮操作 */
    resetDialogQuery() {
      this.resetForm("dialogQueryForm");
      this.dialogQueryParams.materialId = this.testRegistrationList && this.testRegistrationList.length > 0 ? this.testRegistrationList[0].id : ""
      this.handleDialogQuery();
    },
    //输入框变化事件
    handleInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.form.deliveryDetailList,index,info)
    },
    //日期选择变化事件
    handleDateChange(val,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info.producedDate = val
      this.inputIndex = index
      this.$set(this.form.deliveryDetailList,index,info)
    },
    handleExpireDateChange(val,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info.expireDate = val
      this.inputIndex = index
      this.$set(this.form.deliveryDetailList,index,info)
    },
    //输入框变化事件
    handleDialogInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.form.deliveryDetailList,index,info)
    },
    //选择变化事件
    handleDialogSelectChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val
      this.$set(this.testForm.deliveryDetailList,index,info)
    },
    /** 检测列表分页事件 */
    dialogPagination(info){
      this.$modal.confirm("只能提交检测当前页的物料，更换页码会导致之前输入内容消失，确定要继续吗？").then(() => {
        this.dialogQueryParams.pageNum = this.testPageNum
        this.dialogQueryParams.pageSize = this.testPageSize
        this.getDialogList();
      }).catch(() => {
        this.testPageNum = this.dialogQueryParams.pageNum
        this.testPageSize = this.dialogQueryParams.pageSize
      });
    },
    // 提交检测
    submitTestForm(){
      let list = []
      this.testForm.deliveryDetailList.map((item) => {
        if(item.detectionFailType && item.detectionFailStatus == '0'){
          let info = {
            id: item.id,
            detectionFailType: item.detectionFailType,
            detectionFailRemark: item.detectionFailRemark,
            failCount: item.failCount,
            detectionFailStatus: item.detectionFailStatus
          }
          list.push(info)
        }
      })
      if(list.length === 0){
        this.$message.warning("请至少输入一条物料的检测信息！")
        return false
      }
      this.$refs["testForm"].validate((valid) => {
        if (valid) {
          checkMaterialFail(list).then((response) => {
            this.$modal.msgSuccess("检测成功");
            this.testOpen = false;
            this.getDialogList();
          });
        }
      });
    },
    //检测取消
    testCancel(){
      this.testOpen = false
      this.resetForm("dialogQueryForm");
      this.testForm = {
        deliveryDetailList: []
      }
      this.testPageNum = 1
      this.testPageSize = 10
    },
    /** 查询入库检测列表 */
    getDialogList(row) {
      this.dialogLoading = true;
      let params = {...this.dialogQueryParams}
      delete params.materialId
      getTestRegistrationList(row.id,params).then((response) => {
        this.testForm.deliveryDetailList = response.rows;
        this.dialogTotal = response.total;
        this.dialogLoading = false;
      });
    },
  },
};
</script>
