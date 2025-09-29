<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="出库单号" prop="code" label-width="80px">
        <el-input v-model="queryParams.code" placeholder="请输入出库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择状态">
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading="addLoading" @click="handleAdd" v-hasPermi="['inout:delivery:add']">新增出库成品</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading="addLoading" @click="handleActivatePicking" v-hasPermi="['inout:delivery:add']">激活拣货</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-printer" size="mini" :disabled="single" @click="handleAutomaticAssign" v-hasPermi="['inout:delivery:labelprint']">自动分配</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:delivery:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:delivery:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outDeliveryCompleteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" />
      <el-table-column label="出库单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="单据类型" align="center" prop="type" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <el-table-column label="申请部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="申请人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="来源" align="resource" prop="newLocal" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_local" :value="scope.row.newLocal" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <!-- <dict-tag :options="dict.type.inout_out_status" :value="scope.row.status" /> -->
          <span :style="{ color: scope.row.status == '2' ? 'green' : 'red' }" v-for="item in dict.type.inout_out_status" :key="item.value" v-show="scope.row.status == item.value">{{ item.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出库状态" align="center" prop="completeState" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <!-- <dict-tag :options="dict.type.complete_state" :value="scope.row.completeState" /> -->
          <span :style="{ color: scope.row.completeState == '1' ? 'red' : 'green' }" v-for="item in dict.type.complete_state" :key="item.value" v-show="scope.row.completeState == item.value">{{ item.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 4)" v-hasPermi="['inout:delivery:query']">查看</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status == '1'" @click="handleUpdate(scope.row)" v-hasPermi="['inout:delivery:edit']">编辑</el-button> -->
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '1' && scope.row.completeState == '1'" @click="handleDelete(scope.row)" v-hasPermi="['inout:delivery:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-finished" v-if="scope.row.status == '1' && scope.row.completeState == '1'" @click="handleDetailed(scope.row, 3)" v-hasPermi="['inout:delivery:register']">审核</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.status == '2' && scope.row.completeState != '3'" @click="handleDetailed(scope.row, 2)" v-hasPermi="['inout:delivery:check']">分配</el-button>
          <el-button size="mini" type="text" icon="el-icon-box" v-if="scope.row.status == '2' && scope.row.completeState != '3'" @click="handleDetailed(scope.row, 1)" v-hasPermi="['inout:delivery:check']">地堆拣货</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.status == '2' && scope.row.completeState != '3'" @click="handleDetailed(scope.row, 5)" v-hasPermi="['inout:delivery:check']">自动分配</el-button>
          <el-button size="mini" type="text" icon="el-icon-box" v-if="scope.row.status == '2' && scope.row.completeState != '3'" @click="handleDetailed(scope.row, 6)" v-hasPermi="['inout:delivery:check']">地堆自动拣货</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改齐套出库计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="padding-right: 30px">
        <el-row :gutter="20" class="mb8">
          <el-col :span="8">
            <el-form-item label="单据类型" prop="type">
              <el-select style="width: 100%;" v-model="form.type" placeholder="请选择单据类型" class="select-input-form">
                <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请部门">
              <el-input v-model="form.deptName" disabled placeholder="请输入申请部门" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请人">
              <el-input v-model="form.createBy" disabled placeholder="请输入申请人" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb8">
          <el-col :span="12">
            <el-form-item label="物料BOM组合" prop="suitMaterialName">
              <el-input v-model="form.suitMaterialName" placeholder="请选择物料BOM组合" @focus="materialComOpen" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计出库数量" prop="suitNum">
              <el-input v-model="form.suitNum" v-intNumber placeholder="请输入预计出库数量" :maxlength="6" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="30px" prop="tOutDeliveryDetailList">
          <el-table v-loading="loading" :data="form.tOutDeliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="预计出库数量" align="center" prop="showPredictCount" width="150"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" width="120"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" width="200"></el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 选择物料Bom -->
    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>
    <!-- 分配任务 -->
    <ArriveTaskCom ref="arriveTaskCom" @materialStockGet="materialStockGet" @setArriveTask="setArriveTask"></ArriveTaskCom>
    <!-- 选择物料库存 -->
    <MaterialStockCom ref="materialStockCom" @setMaterialStock="setMaterialStock"></MaterialStockCom>
    <DetailCom ref="detailCom" @setDetail="setDetail"></DetailCom>
  </div>
</template>

<script>
import { listOutDeliveryComplete, getOutDelivery, delOutDelivery, addOutDeliveryComplete, updateOutDeliveryComplete, approveOutDelivery } from "@/api/inoutDelivery/outDelivery";

import { wms } from "@/utils/agent";
import store from "@/store";

import MaterialCom from "./components/material";
import ArriveTaskCom from "./components/arriveTask";
import MaterialStockCom from "./components/materialStock";
import DetailCom from "./components/detail";

export default {
  name: "outDeliveryComplete",
  dicts: ["inout_out_type", "inout_out_status", "inout_out_local","complete_state"],
  components: { MaterialCom, ArriveTaskCom, MaterialStockCom, DetailCom },
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
      // 齐套出库表格数据
      outDeliveryCompleteList: [],
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
        tOutDeliveryDetailList: []
      },
      // 表单校验
      rules: {
        type: [{ required: true, message: "请选择单据类型", trigger: "change" }],
        suitMaterialName: [
          { required: true, trigger: "change", validator: validateMaterial },
        ],
        suitNum: [
          { required: true, message: "请输入预计出库数量", trigger: "blur" },
        ],
      },
      detailRules: {
        materialCode: [
          { required: true, trigger: "change", validator: validateMaterial },
        ],
        materialName: [
          { required: true, message: "请选择物料名称", trigger: "change" },
        ],
        batchCode: [
          { required: true, message: "请输入批次号", trigger: "blur" },
        ],
        predictCount: [
          { required: true, trigger: "blur", validator: validatePredictCount },
        ],
        producedDate: [
          // { trigger: "change", validator: validateProducedDate },
          { required: true, message: "请选择生产日期", trigger: "change" },
        ],
      },

      detailedOpen: false, //详情标识
      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情

      detailIndex: 0, //操作详情角标
      addLoading: false, // 新增按钮loading

      dialogLoading: false, //弹窗loading
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "outDeliveryComplete") {
        this.getList();
      }
    },
    'form.suitNum': {
      handler(val){
        if(val){
          this.form.tOutDeliveryDetailList.map((item) => {
            item.showPredictCount = item.predictCount * Number(val)
          })
        } else {
          this.form.tOutDeliveryDetailList.map((item) => {
            item.showPredictCount = item.predictCount
          })
        }
        this.$forceUpdate()
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    // 已收货禁止选择
    checkSelectable(row, index) {
      return row.status == '1';
    },
    /** 查询齐套出库列表 */
    getList() {
      this.loading = true;
      this.queryParams.deliveryModule = '2'
      listOutDeliveryComplete(this.queryParams).then((response) => {
        this.outDeliveryCompleteList = response.rows;
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 激活拣货按钮操作 */
    // handleActivatePicking() {
    //   if(!this.ids || this.ids && this.ids.length == 0){
    //     this.$message.warning(`请至少选择一条记录`)
    //     return
    //   }
    //   const ids = this.ids;
    //   let tips = '该操作将激活拣货任务,确定要激活吗？';
    //   this.$modal.confirm(tips).then(function () {
    //     return checkDelivery({ id: ids });
    //   }).then(() => {
    //     this.getList();
    //     this.$modal.msgSuccess("激活拣货成功");
    //   }).catch(() => { });
    // },
    /** 自动分配按钮操作 */
    // handleAutomaticAssign() {
    //   if(!this.ids || this.ids && this.ids.length == 0){
    //     this.$message.warning(`请至少选择一条记录`)
    //     return
    //   }
    //   const ids = this.ids;
    //   let tips = '该操作将为出库单自动分配拣货,确定要分配吗？';
    //   this.$modal.confirm(tips).then(function () {
    //     return checkDelivery({ id: ids });
    //   }).then(() => {
    //     this.getList();
    //     this.$modal.msgSuccess("自动分配成功");
    //   }).catch(() => { });
    // },
    /** 新增按钮操作 */
    handleAdd() {
      this.open = true;
      this.title = "添加出库成品";
      this.form = {
        id: null,
        type: null,
        deptName: null,
        suitMaterialId: null,
        suitMaterialName: null,
        suitNum: null,
        createBy: null,
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
      const id = row.id || this.ids;
      this.form = {
        id: null,
        type: null,
        deptName: null,
        suitMaterialId: null,
        suitMaterialName: null,
        suitNum: null,
        createBy: null,
        tOutDeliveryDetailList: []
      };
      getOutDelivery(id).then((response) => {
        response.data.tOutDeliveryDetailList.map((item) => {
          if(response.data.suitNum){
            item.predictCount = item.predictCount / response.data.suitNum
          }
        })
        this.open = true;
        this.title = "编辑出库成品";
        this.$nextTick(() => {
          this.reset();
          this.form = response.data;
        })
      });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.form.tOutDeliveryDetailList.length == 0){
        this.$message.warning("出库物料明细不能为空！")
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let params = JSON.parse(JSON.stringify(this.form))
          // params.tOutDeliveryDetailList.map((item) => {
          //   item.predictCount = item.predictCount * Number(params.suitNum)
          // })
          // params.deliveryModule = '1'
          if (this.form.id != null) {
            updateOutDeliveryComplete(params).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            })
          } else {
            addOutDeliveryComplete(params).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            })
          }
        }
      });
    },
    // 打开配置明细
    handleDetailed(row, type) {
      if(type == 1 || type == 2 || type == 5 || type == 6){
        getOutDelivery(row.id).then((response) => {
          if(!response.data.tOutDeliveryDetailList){
            this.$message.error("查询物料信息异常！")
            return
          }
          response.data.tOutDeliveryDetailList.map((item) => {
            if(response.data.suitNum){
              item.predictCount = item.predictCount / response.data.suitNum
            }
          })
          this.$refs.arriveTaskCom.open = true
          this.$refs.arriveTaskCom.title = type == 1 ? '地堆拣货': '出库分配'
          this.$refs.arriveTaskCom.detailId = row.id
          this.$refs.arriveTaskCom.detailType = type
          // this.$refs.arriveTaskCom.trayType = res.data !== null ? res.data+"" : ""
          this.$refs.arriveTaskCom.materialList = []
          this.$refs.arriveTaskCom.materialList = [...response.data.tOutDeliveryDetailList]
        });
      } else {
        this.$refs.detailCom.detailId = row.id;
        this.detailId = row.id;
        this.detailType = type
        this.$refs.detailCom.detailType = type;
        this.$refs.detailCom.isFinish = row.completeState == 2 || row.completeState == 3
        let title = "出库单详情";

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
        tips = '是否确认删除出库单号为"' + row.code + '"的数据项？';
      } else {
        let codes = [];
        this.tOutDeliveryDetailList.forEach((item) => {
          if (this.ids.indexOf(item.id) > -1) {
            codes.push(item.code);
          }
        });
        tips = '是否确认删除出库单号为"' + codes.toString() + '"的数据项？';
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
      this.download(wms + "/inout/delivery/export", {...this.queryParams,}, `inDelivery_${new Date().getTime()}.xlsx`);
    },
    //打开选择物料库存弹窗
    materialStockGet(param){
      this.$refs.materialStockCom.materialId = param.materialId;
      this.$refs.materialStockCom.type = param.stockType;
      this.$refs.materialStockCom.open = true;
    },
    //选择物料库存回调
    setMaterialStock(materialStock){
      console.log(this.$refs.arriveTaskCom.detailIndex)
      let taskDetailList = JSON.parse(JSON.stringify(this.$refs.arriveTaskCom.form.taskDetailList));
      let flag = taskDetailList.every((item,index) => {
        if(index != this.$refs.arriveTaskCom.detailIndex) {
          return item.trayId != materialStock.id
        } else {
          return true
        }
      })
      if(flag){
        taskDetailList[this.$refs.arriveTaskCom.detailIndex].trayCode = materialStock.code ? materialStock.code : "";
        taskDetailList[this.$refs.arriveTaskCom.detailIndex].trayId = materialStock.id ? materialStock.id : "";
        taskDetailList[this.$refs.arriveTaskCom.detailIndex].stockId = materialStock.stockid ? materialStock.stockid : "";
        taskDetailList[this.$refs.arriveTaskCom.detailIndex].locationName = materialStock.locationname ? materialStock.locationname : "";
        taskDetailList[this.$refs.arriveTaskCom.detailIndex].locationId = materialStock.locationId ? materialStock.locationId : "";
        taskDetailList[this.$refs.arriveTaskCom.detailIndex].availableCount = materialStock.availableCount ? materialStock.availableCount : "";
        this.$refs.arriveTaskCom.getSpanArr(taskDetailList)
        this.$nextTick(() => {
          this.$refs.arriveTaskCom.form.taskDetailList = taskDetailList
          this.$refs.arriveTaskCom.materialList[this.$refs.arriveTaskCom.currentIndex].taskDetailList = taskDetailList
          this.$refs.arriveTaskCom.$forceUpdate()
        })
      } else {
        this.$message.error(`已有【${material.locationName}】库位在明细中，请重新选择！`)
      }
    },
    // 打开选择物料弹窗
    materialComOpen() {
      this.$refs.materialCom.open = true;
      this.$refs.materialCom.queryParams.contactsUnitId = this.form.unitCode;
      // this.detailIndex = index
    },
    //选择物料bom回调
    setMaterial(material) {
      let tOutDeliveryDetailList = JSON.parse(JSON.stringify(material.bomDetail));
      let resultList = []
      tOutDeliveryDetailList.map((item) => {
        let info = {
          materialId: item.id,
          materialCode: item.code,
          materialName: item.name,
          unitName: item.unitName,
          unitId: item.unitId,
          batchCode: item.batchCode,
          predictCount: item.predictCount,
          showPredictCount: this.form.suitNum ? (item.predictCount * Number(this.form.suitNum)) : item.predictCount,
        }
        resultList.push(info)
      })
      this.form.suitMaterialId = material.bomId
      this.form.suitMaterialName = material.bomName
      this.form.tOutDeliveryDetailList = resultList
    },
    //详情提交
    setDetail(data){
      let params = {}
      params.id = data.id;
      params.status = data.flag;
      params.remark = data.remark;
      approveOutDelivery(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("审核成功");
          this.$refs.detailCom.open = false;
          this.$refs.detailForm = {}
          this.getList();
        }
      });
    },
    //分配成功后的操作
    setArriveTask(flag) {
      if(flag){
        this.getList()
      }
    },
  },
};
</script>
