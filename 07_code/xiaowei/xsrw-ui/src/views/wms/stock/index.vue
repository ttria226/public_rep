<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="库位标识" prop="locationId">
        <el-input v-model="queryParams.locationId" placeholder="请输入库位标识" clearable @keyup.enter.native="handleQuery" />
      </el-form-item> -->
      <el-form-item label="物料编码" prop="materialCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="批次号" prop="batchCode">
        <el-input v-model="queryParams.batchCode" placeholder="请输入批次号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item> -->

      <!-- <el-form-item label="所属区域" prop="areaId">
          <el-select v-model="queryParams.areaId"  @change="changeQueryReservoirList()" placeholder="请选择所属区域" clearable>
            <el-option v-for="dict in queryAreaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
      </el-form-item> -->
      <!-- <el-form-item label="所属库区" prop="reservoirId">
        <el-select v-model="queryParams.reservoirId" @change="changeQueryLocationList()" placeholder="请选择所属库区" clearable>
          <el-option v-for="dict in kqQueryList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item> -->
      <!-- <el-form-item label="库位" prop="locationId">
        <el-select v-model="queryParams.locationId" clearable placeholder="请选择库位">
          <el-option v-for="item in locations" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="位置" prop="position">
        <el-input v-model="queryParams.position" placeholder="请输入位置" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="载具" prop="trayId">
         <el-select v-model="queryParams.trayId" clearable placeholder="请选择载具">
           <el-option v-for="item in trays" :key="item.id" :label="item.code" :value="item.id"></el-option>
         </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['wms:stock:add']" >新增</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['wms:stock:edit']">修改</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['wms:stock:remove']">删除</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain size="mini" :disabled="multiple" @click="generatePlan" v-hasPermi="['check:checkDelivery:add']">生成盘点计划</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="primary" plain size="mini" @click="handleBatch" v-hasPermi="['stock:stock:listbatchsum']">批次总数量</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain size="mini" :disabled="single" @click="handleMoveLibrary" v-hasPermi="['']">移库</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain size="mini" :disabled="multiple" @click="" v-hasPermi="['']">单据导入</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:stock:export']">导出到excel</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getMainList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockMainList" @selection-change="handleSelectionChange">
     <!-- <el-table-column type="selection" width="55" align="center" /> -->
     <!-- <el-table-column label="主键" align="center" prop="id"  :show-overflow-tooltip="true"/> -->
     <el-table-column fixed label="所属组织" align="center" min-width="120px" prop="deptName" :show-overflow-tooltip="true" />
<!--     <el-table-column fixed label="往来单位" align="center" min-width="200px" prop="contactsUnitName" :show-overflow-tooltip="true" />-->
     <el-table-column fixed label="物料编码" align="center" min-width="100px" prop="materialCode" :show-overflow-tooltip="true" />
     <el-table-column fixed label="物料名称" align="center" min-width="100px" prop="materialName" :show-overflow-tooltip="true" />
     <!-- <el-table-column label="仓库" align="center" prop="wareHouseName" :show-overflow-tooltip="true" width="220" /> -->
      <el-table-column label="规格型号" align="center" min-width="200px" prop="specifications" :show-overflow-tooltip="true" />
     <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" width="120px" />
<!--     <el-table-column label="包装方式" align="center" prop="packUnitName" :show-overflow-tooltip="true" width="120px" />-->

     <el-table-column label="在库数量" align="center" prop="libraryCount" :show-overflow-tooltip="true" width="120px" />
     <el-table-column label="可用数量" align="center" prop="availableCount" :show-overflow-tooltip="true" width="120px" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row)" v-hasPermi="['stock:stockMain:query']">详情</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:stock:remove']">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getMainList" />


    <!-- 批次总数量 -->
    <el-dialog :title="title" :visible.sync="batchOpen" width="50%" append-to-body>
      <el-table v-loading="batchLoading" :data="batchList">
        <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
        <el-table-column label="在库数量" align="center" prop="count" :show-overflow-tooltip="true" />
        <el-table-column label="可用数量" align="center" prop="availableCount" :show-overflow-tooltip="true" />
      </el-table>
      <pagination v-show="batchTotal>0" :total="batchTotal" :page.sync="batchParams.pageNum" :limit.sync="batchParams.pageSize" @pagination="getBatch" />

      <div slot="footer" class="dialog-footer">
        <el-button @click="batchCancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 移库 -->
    <el-dialog title="库内移位" :visible.sync="moveOpen" width="600px" append-to-body>
      <el-form ref="moveForm" :model="moveForm" :rules="moveRules" label-width="80px">
        <el-form-item label="目标库位" prop="locationInId">
          <el-select v-model="moveForm.locationInId" clearable placeholder="请选择库位">
            <template v-for="item in otherLocations">
              <el-option v-if="item.id != currentRow.locationId" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </template>
          </el-select>
        </el-form-item>
      </el-form>

      <div style="color: red; padding: 1.25rem 0; margin-top: 0.625rem;">生成移位任务后将把整个载具上的物料都移动到目标库位上</div>
      <el-table :data="trayList">
        <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" />
        <el-table-column label="批次号" align="center" width="260px" prop="batchCode" :show-overflow-tooltip="true" />
        <el-table-column label="在库数量" align="center" prop="count" :show-overflow-tooltip="true" />
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMoveLibrary">确 定</el-button>
        <el-button @click="moveCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情 -->
    <el-dialog title="详情" :visible.sync="stockDetailOpen" width="1000px" append-to-body>
      <el-form :model="stockDetailQueryParams" ref="stockDetailForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="批次号" prop="batchCode">
          <el-input v-model="stockDetailQueryParams.batchCode" placeholder="请输入批次号" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="区域" prop="areaId">
          <el-select v-model="stockDetailQueryParams.areaId"  @change="changeQueryReservoirList()" placeholder="请选择区域" clearable>
            <el-option v-for="dict in queryAreaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="库位" prop="locationId" v-if="wareHouseType == '1'">
          <el-select v-model="stockDetailQueryParams.locationId" @change="changeForceUpdate()" clearable placeholder="请选择库位">
            <el-option v-for="item in locations" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="位置" prop="position" v-if="wareHouseType == '2'">
          <el-input v-model="stockDetailQueryParams.position" placeholder="请输入位置" clearable @change="changeForceUpdate()" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="载具" prop="trayCode">
            <el-input v-model="stockDetailQueryParams.trayCode" placeholder="请输入载具编号" @keyup.enter.native="handleQuery" />
           <!-- <el-select v-model="stockDetailQueryParams.trayId" @change="changeForceUpdate()" clearable placeholder="请选择载具">
             <el-option v-for="item in trays" :key="item.id" :label="item.code" :value="item.id"></el-option>
           </el-select> -->
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleDetailQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetDetailQuery">重置</el-button>
        </el-form-item>
      </el-form>
<!--      <el-row :gutter="10" class="mb8">-->
<!--        <el-col :span="1.5">-->
<!--          <el-button type="primary" plain size="mini" :disabled="singleDetail" @click="handleMoveLibrary" v-hasPermi="['stock:stock:shift']">生成库内移位</el-button>-->
<!--        </el-col>-->
<!--      </el-row>-->
      <el-table v-loading="loading" :data="stockList" @selection-change="handleDetailSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主键" align="center" prop="id"  :show-overflow-tooltip="true"/> -->
      <el-table-column fixed label="所属组织" align="center" min-width="120px" prop="deptName" :show-overflow-tooltip="true" />
      <!-- <el-table-column fixed label="物料编码" align="center" min-width="240px" prop="materialCode" :show-overflow-tooltip="true" /> -->
      <el-table-column fixed label="物料名称" align="center" min-width="200px" prop="materialName" :show-overflow-tooltip="true" />
<!--      <el-table-column label="规格型号" align="center" prop="specifications" :show-overflow-tooltip="true" />-->
<!--      <el-table-column label="单位" align="center" prop="unitName" :show-overflow-tooltip="true" />-->
<!--      <el-table-column label="包装方式" align="center" prop="packUnitName" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="仓库" align="center" prop="wareHouseName" :show-overflow-tooltip="true" width="220" /> -->
      <el-table-column label="区域" align="center" prop="areaName" :show-overflow-tooltip="true" min-width="150px" />
      <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="220px" v-if="wareHouseType == '1'"/>
      <el-table-column label="库位" align="center" min-width="180px" prop="locationName" :show-overflow-tooltip="true"/>
      <el-table-column label="位置" align="center" min-width="220px" prop="position" :show-overflow-tooltip="true" v-if="wareHouseType == '2'"/>
      <el-table-column label="批次号" align="center" min-width="200px" prop="batchCode" :show-overflow-tooltip="true" />
      <el-table-column label="载具" align="center" min-width="200px" prop="trayCode" :show-overflow-tooltip="true" />
      <el-table-column label="在库数量" align="center" prop="count" :show-overflow-tooltip="true" width="100px" />
      <!-- <el-table-column label="可用数量" align="center" prop="availableCount" :show-overflow-tooltip="true" width="100px" /> -->
      <el-table-column label="是否冻结" align="center" prop="isFreeze" :show-overflow-tooltip="true" width="100px" >
        <template slot-scope="scope">
           <dict-tag :options="dict.type.stock_is_freeze" :value="scope.row.isFreeze"/>
         </template>
      </el-table-column>
       <el-table-column label="冻结类型" align="center" prop="originType" :show-overflow-tooltip="true" width="100px">
         <template slot-scope="scope">
           <dict-tag :options="dict.type.stock_origin_type" :value="scope.row.originType"/>
         </template>
       </el-table-column>
       <el-table-column label="入库日期" align="center" min-width="130px" prop="beginDate" :show-overflow-tooltip="true" />
       <!-- <el-table-column label="结束日期" align="center" min-width="130px" prop="endDate" :show-overflow-tooltip="true" /> -->
       <!-- <el-table-column label="生产日期" align="center" min-width="130px" prop="producedDate" :show-overflow-tooltip="true" /> -->
       <!-- <el-table-column label="剩余有效期天数" align="center" min-width="130px" prop="remainingValidDays" :show-overflow-tooltip="true" /> -->
        <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:stock:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:stock:remove']">删除</el-button>
          </template>
        </el-table-column> -->
      </el-table>

      <pagination v-show="stockDetailTotal>0" :total="stockDetailTotal" :page.sync="stockDetailQueryParams.pageNum" :limit.sync="stockDetailQueryParams.pageSize" @pagination="getList" />

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStock, getStock, delStock, addStock, updateStock, listBatchSum, stockShift, moveLibrary, getOtherLocation,stockMainList } from "@/api/wms/stock";
import { checkDeliveryAdd } from "@/api/wms/pdcheckDelivery";
import { listTray } from "@/api/wms/Tray";
import { listUnit } from "@/api/wms/unit";
import { listLocation } from "@/api/wms/location";
import { listArea } from "@/api/wms/area";
import { listReservoir } from "@/api/wms/reservoir";

import {wms} from '@/utils/agent'

export default {
  name: "Stock",
  dicts: ['stock_is_freeze','stock_origin_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      batchLoading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 选中数组
      detailIds: [],
      // 非单个禁用
      singleDetail: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      batchTotal: 0,
      // 库存表格数据
      stockList: [],
      stockMainList:[],
      batchList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      batchOpen: false,
      moveOpen: false,
      stockDetailOpen:false,
      wareHouseType:'0',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        locationId: null,
        materialId: null,
        batchCode: null,
        unitId: null,
        count: null,
        status: null,
        isfreeze: null,
        originType: null,
        code: null,
      },
      batchParams: {
        pageNum: 1,
        pageSize: 10,
        locationId: null,
        materialId: null,
        batchCode: null,
        unitId: null,
        count: null,
        status: null,
        isfreeze: null,
        originType: null,
        code: null,
      },
      stockDetailQueryParams:{
          pageSize:20,
      },
      stockDetailTotal:0,
      // 表单参数
      form: {},
      // 表单校验
      rules: {

      },
      warehouses: [], //仓库源数据
      queryAreaList:[],// 区域源数据
      // kqQueryList:[],// 库区源数据
      trays: [], // 载具源数据
      units: [], // 规格源数据
      locations: [], // 库位源数据
      freezeStatus: [], // 冻结状态源数据
      trayList: [], // 移库时的载具列表
      moveForm: {
        locationInId: null,
      },
      otherLocations: [], // 移库的目标库位源数据
      moveRules: {
        locationInId: [
          { required: true, message: '请输入目标库位', trigger: 'blur' }
        ],
      },
      currentRow: {}, // 当前选中数据的库位
      stockSelection: [],
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'Stock') {
        this.getMainList();
      }
    }
  },
  created() {
    // 获取载具源数据
    listTray({pageNum:1,pageSize:1000}).then(response => {
      this.trays = response.rows;
    });
    // 获取规格源数据
    listUnit({pageNum:1,pageSize:1000}).then(response => {
      this.units = response.rows;
    });
    // 区域
    listArea({pageSize:5000}).then(response => {
      this.queryAreaList = response.rows;
    });
    // 库区
    // listReservoir({pageSize:5000}).then(response => {
    //   this.kqQueryList = response.rows;
    // });
    // 获取库位源数据
    listLocation({pageNum:1,pageSize:1000}).then(response => {
      this.locations = response.rows;
    });
    // 获取冻结状态数据
    this.getDicts('wms_stock_isfreeze').then(response => {
      this.freezeStatus = response.data
			console.log('我是冻结状态',this.freezeStatus)
    })
    this.getMainList();
  },
  methods: {
    // 冻结状态翻译
    convertFreeze(row, column) {
      const item = this.freezeStatus.find(item => item.dictValue == row.isfreeze)
      return item && item.dictLabel ? item.dictLabel : '--'
    },
    /** 查询库存列表 */
    getMainList() {
      this.loading = true;
      stockMainList(this.queryParams).then(response => {
        this.stockMainList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询库存列表 */
    getList() {
      this.loading = true;
      listStock(this.stockDetailQueryParams).then(response => {
        this.stockList = response.rows;
        this.stockDetailTotal = response.total;
          console.log(this.stockDetailTotal);
          this.loading = false;
      });
    },
    handleDetailed(row){
      this.stockDetailQueryParams.warehouseId = row.warehouseId;
      this.stockDetailQueryParams.materialId = row.materialId;
      this.stockDetailOpen = true;
      this.wareHouseType = row.wareHouseType;
      this.changeQueryAreaList(row.warehouseId);
      this.getList();
    },
    /** 查询批次总数量列表 */
    getBatch() {
      this.batchLoading = true;
      listBatchSum(this.batchParams).then(response => {
        this.batchList = response.rows;
        this.batchTotal = response.total;
        this.batchLoading = false;
      });
    },
    /** 查询仓库下区域 **/
    changeQueryAreaList(warehouseId){
        this.stockDetailQueryParams.areaId = null;
        // this.stockDetailQueryParams.reservoirId = null;
        this.stockDetailQueryParams.locationId = null;
        if(warehouseId){
          listArea({warehouseId:warehouseId,pageSize:5000}).then(response => {
              this.queryAreaList = response.rows;
          });
          // 获取库位源数据
          listLocation({warehouseId:warehouseId,pageNum:1,pageSize:1000}).then(response => {
            this.locations = response.rows;
          });
        }
    },
    // /** 查询区域下库区 **/
    // changeQueryReservoirList(){
    //     this.queryParams.reservoirId = null;
    //     this.queryParams.locationId = null;
    //     if(this.queryParams.areaId){
    //       listReservoir({areaId:this.queryParams.areaId,pageSize:5000}).then(response => {
    //           this.kqQueryList = response.rows;
    //       });
    //     } else{
    //       listReservoir({pageSize:5000}).then(response => {
    //           this.kqQueryList = response.rows;
    //       });
    //     }
    // },
    /** 查询库区下库位 **/
    changeQueryReservoirList(){
        this.stockDetailQueryParams.locationId = null;
        listLocation({areaId:this.stockDetailQueryParams.areaId,pageSize:5000}).then(response => {
            this.locations = response.rows;
        });
        this.$forceUpdate()
    },
    changeForceUpdate(){
      this.$forceUpdate();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 批次总数量取消按钮
    batchCancel() {
      this.batchOpen = false;
    },
    //移库取消按钮
    moveCancel() {
      this.moveOpen = false;
    },
    // 详情取消按钮
    detailCancel() {
      this.stockDetailOpen = false;
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        locationId: null,
        materialId: null,
        batchCode: null,
        unitId: null,
        count: null,
        status: "0",
        isfreeze: null,
        originType: null,
        code: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getMainList();
    },
    handleDetailQuery(){
      this.stockDetailQueryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
      // this.changeQueryAreaList();
    },
    /** 重置按钮操作 */
    resetDetailQuery() {
      this.resetForm("stockDetailForm");
      this.changeQueryReservoirList();
      this.handleDetailQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.stockSelection = selection
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 多选框选中数据
    handleDetailSelectionChange(selection) {
      this.detailIds = selection.map(item => item.id);
      this.singleDetail = selection.length!==1;
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/stock/export', {
        ...this.queryParams
      }, `stock_${new Date().getTime()}.xlsx`)
    },
    // 点击批次总数量
    handleBatch() {
      this.batchLoading = true;
      this.batchParams = JSON.parse(JSON.stringify(this.queryParams))
      this.batchParams.pageNum = 1
      listBatchSum(this.queryParams).then(response => {
        this.batchList = response.rows;
        this.batchOpen = true
        this.batchTotal = response.total;
        this.batchLoading = false;
        this.title = "批次总数量"
      });
    },
    /** 生成盘点计划按钮操作 */
    generatePlan(row) {
      if(this.ids.length>0){
        const ids = this.ids
        this.$modal.confirm('是否确认生成盘点任务？').then(() => {
          let datas = []
          this.stockSelection.forEach(item => {
            const data = {
              checkType: 1,
              materialId: item.materialId,
              warehouseId: item.warehouseId,
              warehouseType: item.wareHouseType,
            }
            datas.push(data)
          })
          return checkDeliveryAdd(datas);
        }).then(() => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getMainList();
        }).catch(() => {});
      }else{
        this.$modal.msgError('请选择需要添加的内容')
      }
    },
    // 移库按钮操作
    handleMoveLibrary() {
      this.currentRow = this.stockList.find(item => item.id == this.detailIds[0])

      stockShift(this.detailIds[0]).then(res => {
        this.trayList = res.data;
        getOtherLocation({ locationId: this.currentRow.locationId }).then(res => {
          this.otherLocations = res.data

          if (res.data.length > 0 && this.trayList.length > 0) {
            this.moveOpen = true
          } else {
            if (res.data.length == 0){
              this.$modal.msgError("暂无合适库位移库");
            }
          }
        })
      })

    },
    // 移库提交
    submitMoveLibrary() {
      this.$refs["moveForm"].validate(valid => {
        if (valid) {
          const data = new FormData()
          data.append("stockId",this.detailIds[0])
          data.append("locationInId",this.moveForm.locationInId)
          moveLibrary(data).then(res => {
            this.getList();
            this.$modal.msgSuccess("操作成功");
            this.moveOpen = false;
          })
        }
      });
    }
  }
};
</script>
