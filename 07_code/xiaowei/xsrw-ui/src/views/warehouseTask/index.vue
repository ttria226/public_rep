<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入库单号" prop="advanceDeliveryCode">
        <el-input v-model="queryParams.advanceDeliveryCode" placeholder="请输入入库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="入库类型" prop="advanceDeliveryType">
        <el-select v-model="queryParams.advanceDeliveryType" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="nextFlag" label-width="90px">
        <el-select v-model="queryParams.nextFlag" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.wms_delivery_detail_next_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="物料编码" prop="advanceDeliveryCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="advanceDeliveryCode">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="批次号" prop="advanceDeliveryCode">
        <el-input v-model="queryParams.batchCode" placeholder="请输入批次号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :disabled="multiple" @click="handleArriveTask" v-hasPermi="['inout:registration:add']">批量组盘上架</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:registration:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:registration:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" ref="multipleTable" :data="inDeliveryTaskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" />
      <el-table-column label="入库单号" align="center" prop="advanceDeliveryCode" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="规格型号" align="center" prop="specifications" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" width="100" />
<!--      <el-table-column label="登记数量" align="center" prop="predictCount" :show-overflow-tooltip="true" width="100" />-->
      <el-table-column label="入库数量" align="center" prop="detectionCount" :show-overflow-tooltip="true" width="100" />
      <el-table-column label="打印数量" align="center" prop="returnCount" :show-overflow-tooltip="true" width="80" />
      <el-table-column label="入库类型" align="center" prop="advanceDeliveryType" :show-overflow-tooltip="true" min-width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_type" :value="scope.row.advanceDeliveryType" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <el-table-column label="制单人" align="center" prop="maker" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="nextFlag" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.nextFlag != 4 ? 'red' : 'green' }" v-for="item in dict.type.wms_delivery_detail_next_status" :key="item.value" v-show="scope.row.nextFlag == item.value">{{ item.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="执行时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row)" v-hasPermi="['inout:registration:query']">查看</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['inout:registration:remove']">删除</el-button> -->
          <el-button size="mini" type="text" icon="el-icon-receiving" v-if="(scope.row.nextFlag == 1 || scope.row.nextFlag == 2 || scope.row.nextFlag == 3) && (scope.row.detectionCount > scope.row.putawayCount)" @click="handlePutwayDisplay(scope.row)">组盘上架</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="(scope.row.nextFlag == 1 || scope.row.nextFlag == 2 || scope.row.nextFlag == 3) && (scope.row.detectionCount > scope.row.putawayCount)" @click="handleFloorDisplay(scope.row)">地堆上架</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 地堆上架 -->
    <el-dialog title="地堆上架" :visible.sync="floorDisplayOpen" width="50%" append-to-body>
      <el-form ref="floorDisplayForm" :model="floorDisplayForm" :rules="rules" label-width="120px" style="padding-right: 30px">
        <el-form-item label="选择上架区域" prop="areaId">
          <el-select style="width: 100%;" v-model="floorDisplayForm.areaId" @change="changeReservoirList" clearable placeholder="请选择上架区域">
            <el-option v-for="item in areaList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择上架库区" prop="reservoirId">
          <el-select style="width: 100%;" v-model="floorDisplayForm.reservoirId" @change="changeLocationList" clearable placeholder="请选择上架库区">
            <el-option v-for="item in reservoirList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择上架库位" prop="locationId">
          <el-select style="width: 100%;" v-model="floorDisplayForm.locationId" filterable clearable placeholder="请选择上架库位">
            <el-option v-for="item in locationList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="载具编码">
          <!-- <el-select style="width: 100%;" v-model="floorDisplayAllForm.trayCode" filterable remote reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod" :loading="codeLoading">
            <el-option v-for="item in trayCodeList" :key="item.id" :label="item.code" :value="item.id">
            </el-option>
          </el-select> -->
          <el-input v-model="floorDisplayForm.trayCode" placeholder="请输入载具编码" :maxlength="40"  />
        </el-form-item>
        <el-form-item label="是否全部上架" prop="floorStatus">
          <el-radio-group v-model="floorDisplayForm.floorStatus" @change="handleFloorStatusChange">
            <el-radio :label="1">全部</el-radio>
            <el-radio :label="0">部分</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label-width="30px" prop="deliveryDetailList" v-if="floorDisplayForm.floorStatus === 0">
          <el-form :model="floorDisplayPartParams" ref="floorDisplayPartQueryForm" size="small" :inline="true" label-width="80px">
            <el-form-item label="RFID标识" prop="rfid">
              <el-input v-model="floorDisplayPartParams.rfid" placeholder="请输入RFID标识" clearable @keyup.enter.native="handleFloorDisplayPartQuery" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFloorDisplayPartQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetFloorDisplayPartQuery">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="floorDisplayPartloading" :data="floorDisplayForm.deliveryDetailList" @selection-change="handleFloorDisplaySelectionChange">
            <el-table-column type="selection" :selectable="checkFloorDisplaySelectable" width="55" align="center" />
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="130"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" min-width="90"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" min-width="120"></el-table-column>
            <el-table-column label="RFID数量" align="center" prop="rfidCount" min-width="100"></el-table-column>
            <el-table-column label="RFID标识" align="center" prop="rfid" min-width="240"></el-table-column>
            <el-table-column label="库位" align="center" prop="locationName" min-width="120"></el-table-column>
          </el-table>

          <pagination v-show="floorDisplayPartTotal > 0" :total="floorDisplayPartTotal" :page.sync="floorDisplayPartPageNum" :limit.sync="floorDisplayPartPageSize" @pagination="floorDisplayPartPagination" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFloorDisplayForm">确 定</el-button>
        <el-button @click="floorDisplayCancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 生成上架任务 -->
    <arriveTaskCopy ref="arriveTaskCom" @setArriveTask="setArriveTask" @showMaterialRfid="showMaterialRfid" @setControl="setControl"></arriveTaskCopy>
    <DetailCom ref="detailCom" ></DetailCom>
    <!-- 选择物料rfid -->
    <material-rfid ref="materialRfidCom" @setMaterialRfid="setMaterialRfid"></material-rfid>
  </div>
</template>

<script>
import { listInDeliveryTask, getInDeliveryTask, floorStockingInDeliveryTask, getInDeliveryTaskTrayPageList, getInDeliveryTaskTrayType, getTestRegistrationList } from "@/api/inoutDelivery/inDelivery";

import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";
import { listLocation } from "@/api/wms/location";

import { getToken } from "@/utils/auth";
import { wms } from "@/utils/agent";

import arriveTaskCopy from "./components/arriveTaskCopy";
import DetailCom from "./components/detail";

import MaterialRfid from '../warehouseTask/components/materiaRfidCopy';

export default {
  name: "warehouseTask",
  dicts: ["in_delivery_type", "in_delivery_complete_status","wms_delivery_detail_next_status"],
  components: { arriveTaskCopy, DetailCom, MaterialRfid },
  data() {
    let validateDeliveryDetailList = (rule, value, callback) => {
      if (this.floorDisplaySelection && this.floorDisplaySelection.length == 0) {
        callback(new Error("请选择至少一条地堆物料"));
      } else {
        callback();
      }
    };
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //选中数组数据
      currentSelection: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 入库单表格数据
      inDeliveryTaskList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      floorDisplayOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        advanceDeliveryCode: null,
        advanceDeliveryType: null,
        status: null,
        printStatus: '1'
      },
      // 表单参数
      floorDisplayForm: {},
      // 表单校验
      rules: {
        areaId: [{ required: true, message: "请选择上架区域", trigger: "change" }],
        reservoirId: [{ required: true, message: "请选择上架库区", trigger: "change" }],
        locationId: [{ required: true, message: "请选择上架库位", trigger: "change" },],
        deliveryDetailList: [{ trigger: "change", validator: validateDeliveryDetailList },],
      },

      areaList: [], //区域list
      reservoirList: [], //库区list
      locationList: [], //库位list
      trayCodeList: [],//载具list

      codeLoading: false, //载具loading

      floorDisplayPartloading: false, //部分上架物料loading
      // 部分地堆上架弹窗总条数
      floorDisplayPartTotal: 0,
      // 部分地堆上架弹窗查询参数
      floorDisplayPartParams: {
        pageNum: 1,
        pageSize: 10,
        rfid: null,
      },
      floorDisplayPartPageNum: 1,
      floorDisplayPartPageSize: 10,

      floorDisplaySelection: [], //当前选择的物料信息
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "warehouseTask") {
        this.getList();
      }
    },
  },
  created() {
    this.getAreaList()
    this.getList();
  },
  methods: {
    getToken,
    // 已收货禁止选择
    checkSelectable(row, index) {
        return row.status != "3";
    },
    /** 查询预先发货清单列表 */
    getList() {
      this.loading = true;
      listInDeliveryTask(this.queryParams).then((response) => {
        this.inDeliveryTaskList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询区域列表 */
    getAreaList() {
      let qps = {
        pageNum: 1,
        pageSize: 5000,
      };
      listArea(qps).then(response => {
        this.areaList = [...response.rows];
        this.areaTableList = [...response.rows];
      });
    },
    /** 查询库区列表 */
    getReservoirList() {
      let qps = {
        areaId: this.floorDisplayForm.areaId,
        pageNum: 1,
        pageSize: 5000,
      };
      listReservoir(qps).then(response => {
        this.reservoirList = response.rows;
      });
    },
    /** 查询库位列表 */
    getLocationList() {
      let qps = {
        areaId: this.floorDisplayForm.areaId,
        reservoirId: this.floorDisplayForm.reservoirId,
        locationType: 1,
        pageNum: 1,
        pageSize: 5000,
      };
      listLocation(qps).then(response => {
        this.locationList = response.rows;
      });
    },
    /** 查询载具列表 */
    getTrayList(code){
      let qps = {
        notStatus: 2,
        code: code,
        pageNum: 1,
        pageSize: 10,
      };
      this.codeLoading = true
      getInDeliveryTaskTrayPageList(qps).then(res => {
        if(res.code == 200){
          this.trayCodeList = res.data
        }
      }).finally(() => {
        this.codeLoading = false
      })
    },
    //地推上架的载具远程查询
    remoteMethod(query) {
      if (query !== '') {
        this.getTrayList(query)
      } else {
        this.getTrayList('')
      }
    },
    //区域变化事件
    changeReservoirList(val){
      if(val){
        this.floorDisplayForm.reservoirId = null
        this.floorDisplayForm.locationId = null
        this.reservoirList = []
        this.locationList = []
        this.getReservoirList()
      }
    },
    //库区变化事件
    changeLocationList(val){
      if(val){
        this.floorDisplayForm.locationId = null
        this.locationList = []
        this.getLocationList()
      }
    },
    /** 处理地堆上架状态变化事件 */
    handleFloorStatusChange(val){
      this.resetPart();
      if(val === 0){
        this.getFloorDisplayPartList()
      }
    },
    /** 部分上架弹窗搜索按钮操作 */
    handleFloorDisplayPartQuery() {
      this.floorDisplayPartParams.pageNum = 1;
      this.getFloorDisplayPartList();
    },
    /** 部分上架弹窗重置按钮操作 */
    resetFloorDisplayPartQuery() {
      this.resetForm("floorDisplayPartQueryForm");
      this.handleFloorDisplayPartQuery();
    },
    /** 检测列表分页事件 */
    floorDisplayPartPagination(info){
      this.$modal.confirm("只能提交上架当前页的物料，更换页码会导致之前输入内容消失，确定要继续吗？").then(() => {
        this.floorDisplayPartParams.pageNum = this.floorDisplayPartPageNum
        this.floorDisplayPartParams.pageSize = this.floorDisplayPartPageSize
        this.getFloorDisplayPartList();
      }).catch(() => {
        this.floorDisplayPartPageNum = this.floorDisplayPartParams.pageNum
        this.floorDisplayPartPageSize = this.floorDisplayPartParams.pageSize
      });
    },
    // 多选框选中数据
    handleFloorDisplaySelectionChange(selection) {
      this.floorDisplaySelection = JSON.parse(JSON.stringify(selection))
    },
    // 有库位的物料禁止选择
    checkFloorDisplaySelectable(row, index) {
        return (row.locationName === null || row.locationName === undefined);
    },
    //获取部分地堆的物料列表
    getFloorDisplayPartList() {
      this.floorDisplayPartloading = true;
      getTestRegistrationList(this.floorDisplayForm.id,{...this.floorDisplayPartParams, detectionFailStatus: 1}).then((response) => {
        this.floorDisplayForm.deliveryDetailList = response.rows;
        this.floorDisplayPartTotal = response.total;
        this.floorDisplayPartloading = false;
      });
    },
    // 取消按钮
    floorDisplayCancel() {
      this.floorDisplayOpen = false;
      this.reset();
      this.resetPart();
    },
    // 提交地堆上架
    submitFloorDisplayForm(){
      this.$refs["floorDisplayForm"].validate((valid) => {
        if (valid) {
          let materialDetailIds = []
          this.floorDisplaySelection.map((item) => {
            materialDetailIds.push(item.id)
          })
          let params = {
            id: this.floorDisplayForm.id,
            locationId: this.floorDisplayForm.locationId,
            trayCode: this.floorDisplayForm.trayCode,
            floorStatus: this.floorDisplayForm.floorStatus,
            materialDetailIds
          }
          floorStockingInDeliveryTask(params).then((response) => {
            if(response.code === 200){
              this.$modal.msgSuccess("地推上架成功");
              this.floorDisplayOpen = false;
              this.getList();
            }
          });
        }
      });
    },
    //地堆上架
    handleFloorDisplay(row){
      this.reset()
      this.$nextTick(() => {
        this.floorDisplayOpen = true;
        this.floorDisplayForm.id = row.id
      })
    },
    //添加组盘上架
    handlePutwayDisplay(row){
      console.log(row,99);
      this.$refs.multipleTable.clearSelection();
      let ids = []
      ids.push(row.materialId)
      this.currentSelection = [];
      this.currentSelection.push(row);
      getInDeliveryTaskTrayType(ids.toString()).then(res => {
        this.$refs.arriveTaskCom.open = true
        this.$refs.arriveTaskCom.trayType = res.data !== null ? res.data+"" : ""
        this.$refs.arriveTaskCom.materialList = [...this.currentSelection]
      })
    },
    // 表单重置
    reset() {
      this.floorDisplayForm = {
        id: null,
        areaId: null,
        reservoirId: null,
        locationId: null,
        trayCode: null,
        floorStatus: 1,
        deliveryDetailList: []
      };
      this.resetForm("floorDisplayForm");
    },
    /** 重置表格 */
    resetPart(){
      this.floorDisplayPartTotal = 0
      this.floorDisplayPartParams = {
        pageNum: 1,
        pageSize: 10,
        rfid: null,
      }
      this.floorDisplayPartPageNum = 1
      this.floorDisplayPartPageSize = 10
      this.floorDisplaySelection = []
      this.resetForm("floorDisplayPartQueryForm");
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
      this.currentSelection = JSON.parse(JSON.stringify(selection))
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 生成上架任务按钮操作 */
    handleArriveTask() {
      let ids = []
      this.currentSelection.map((item) => {
        ids.push(item.materialId)
      })
      getInDeliveryTaskTrayType(ids.toString()).then(res => {
        this.$refs.arriveTaskCom.open = true
        this.$refs.arriveTaskCom.trayType = res.data !== null ? res.data+"" : ""
        this.$refs.arriveTaskCom.materialList = [...this.currentSelection]
      })
    },
    // 打开配置明细
    handleDetailed(row) {
      this.$refs.detailCom.detailId = row.id;
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
        this.inDeliveryTaskList.forEach((item) => {
            if (this.ids.indexOf(item.id) > -1) {
                codes.push(item.code);
            }
        });
        tips = '是否确认删除入库单号为"' + codes.toString() + '"的数据项？';
      }
      this.$modal.confirm(tips).then(function () {
        return delAdvanceDelivery(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/delivery/detail/export", {...this.queryParams,}, `warehouseTaskExport_${new Date().getTime()}.xlsx`);
    },
    //上架成功后的操作
    setArriveTask(flag) {
      if(flag){
        this.getList()
        if(this.$refs.materialRfidCom.$refs.tabSelect){
          this.$refs.materialRfidCom.$refs.tabSelect.clearSelection()
          this.$refs.materialRfidCom.selection=[]
        }
      }
    },
    // 新的物料选择 李元辉 2023.10.30
    //执行成功后的回调
    setControl(){
      this.getList();
    },
    //选择物料rfid
    showMaterialRfid(item){
        console.log(item);
      this.$refs.materialRfidCom.id = item.id
      this.$refs.materialRfidCom.trayId = item.trayId
      this.$refs.materialRfidCom.materialId = item.materialId
      this.$refs.materialRfidCom.reActualCount = item.detectionCount
	  this.$refs.materialRfidCom.otherSelectList=this.$refs.arriveTaskCom.form.taskInList
      this.$refs.materialRfidCom.open = true
      if(!item.rfids||item.rfids.length == 0){
          if(this.$refs.materialRfidCom.$refs.tabSelect){
              this.$refs.materialRfidCom.$refs.tabSelect.clearSelection()
              this.$refs.materialRfidCom.selection=[]
          }
      }
    },
    //选择物料rfid成功后的回调
    setMaterialRfid(materials){
      let list = JSON.parse(JSON.stringify(this.$refs.arriveTaskCom.form.taskInList))
        console.log(123456,list);
        console.log(materials);
        list.map((item) => {
          if(item.trayId == materials.trayId && item.materialId == materials.materialId && materials.id == item.advanceRegistrationId){
          if(item.rfids){
            let rfidsList = [...item.rfids]
            materials.rfids.map((materialRfidInfo) => {
              let info = rfidsList.find((itemRfidInfo) => { return itemRfidInfo == materialRfidInfo })
              if(!info){
                rfidsList.push(materialRfidInfo)
              }
            })
			item.reActualCount=materials.rfidCount
            item.rfids = rfidsList
            item.rfidString = rfidsList.toString()
          } else {
            item.rfids = materials.rfids
			item.reActualCount=materials.rfidCount
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
