<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="tab-item" :class="{'active': currentTab == index}" v-for="(item, index) in tabs" @click="switchTab(index)" :key="index">{{item}}</div>
      </el-col>

      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <!-- <el-form-item label="仓库" prop="warehouseId">
            <el-select v-model="queryParams.warehouseId" @change="changeQueryAreaList()" clearable placeholder="请选择仓库">
              <el-option
                v-for="item in wareList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item> -->

           <el-form-item label="区域" prop="areaId" v-if="currentTab == 1">
               <el-select v-model="queryParams.areaId"  @change="changeQueryReservoirList()" placeholder="请选择区域" clearable>
                 <el-option
                   v-for="dict in queryAreaList"
                   :key="dict.id"
                   :label="dict.name"
                   :value="dict.id"
                 ></el-option>
               </el-select>
           </el-form-item>

           <el-form-item label="库区" prop="reservoirId" v-if="currentTab == 1">
               <el-select v-model="queryParams.reservoirId" @change="inputChange" placeholder="请选择库区" clearable>
                 <el-option
                   v-for="dict in kqQueryList"
                   :key="dict.id"
                   :label="dict.name"
                   :value="dict.id"
                 ></el-option>
               </el-select>
           </el-form-item>
          <!-- <el-form-item label="仓库类型" prop="warehouseType" v-if="currentTab == 1">
            <el-select v-model="queryParams.warehouseType" clearable placeholder="请选择仓库类型">
              <el-option
                v-for="dict in dict.type.cims_warehouse_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value">
              </el-option>
            </el-select>
          </el-form-item> -->

           <!-- <el-form-item label="库位" prop="locationId">
            <el-select v-model="queryParams.locationId" clearable filterable placeholder="请选择库位">
                <el-option
                  v-for="item in spaceList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
          </el-form-item> -->
          <!-- <el-form-item label="物料编码" prop="materialCode">
            <el-input
              v-model="queryParams.materialCode"
              placeholder="请输入物料编码"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item> -->
          <el-form-item label="物料名称" prop="materialName" v-if="currentTab == 0">
            <el-input
              v-model="queryParams.materialName"
              placeholder="请输入物料名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="制单日期" prop="createTimes">
            <el-date-picker
              v-model="queryParams.createTimes"
              type="daterange"
              clearable
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <!-- <el-form-item label="物料规格">
            <el-input
              v-model="queryParams.specifications"
              placeholder="请输入物料规格"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item> -->
          <!-- <el-form-item label="批号" prop="batchCode">
            <el-input
              v-model="queryParams.batchCode"
              placeholder="请输入批号"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="载具" prop="trayCode">
            <el-input
              v-model="queryParams.trayCode"
              placeholder="请输入载具编号"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item> -->
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['check:checkDelivery:add']"
            >新增</el-button>
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['wms:checkDelivery:edit']"
            >修改</el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['check:checkDelivery:remove']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              v-hasPermi="['check:task:addCheckTask']"
              @click="generate"
            >生成盘点任务</el-button>
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="ImportBill"
            >单据导入</el-button>
          </el-col> -->
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['check:checkDelivery:export']"
            >导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 盘库策略物料tab -->
        <el-table v-loading="loading" ref="multipleTablem" :data="checkDeliveryList" v-show="currentTab == 0" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <!-- <el-table-column label="主键" align="center" prop="id"  :show-overflow-tooltip="true"/> -->
<!--          <el-table-column label="所属组织" fixed align="center" prop="factoryCode" :show-overflow-tooltip="true" />-->
<!--          <el-table-column label="仓库名称" fixed width="200" align="center" prop="wareHouseName" :show-overflow-tooltip="true" />-->
<!--          <el-table-column label="仓库类型" fixed align="center" prop="wareHouseType" :show-overflow-tooltip="true">-->
<!--            <template slot-scope="scope">-->
<!--              <dict-tag :options="dict.type.cims_warehouse_type" :value="scope.row.wareHouseType"/>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="物料编码" fixed min-width="220" align="center" prop="materialCode" :show-overflow-tooltip="true" />
          <el-table-column label="物料名称" fixed min-width="200" align="center" prop="materialName" :show-overflow-tooltip="true" />
         <!-- <el-table-column label="区域" width="200" align="center" prop="areaName" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="库区" width="200" align="center" prop="reservoirName" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="状态" width="200" align="center" prop="reservoirName" :show-overflow-tooltip="true">启用</el-table-column> -->
          <el-table-column label="规格型号"  width="120" align="center" prop="specifications" :show-overflow-tooltip="true" />
          <el-table-column label="单位" width="100" align="center" prop="unitName" :show-overflow-tooltip="true" />
          <el-table-column label="包装方式" width="120" align="center" prop="packUnitName" :show-overflow-tooltip="true" />
          <el-table-column label="在库数量" width="120" align="center" prop="libraryCount" :show-overflow-tooltip="true" />
          <el-table-column label="可用数量" width="120" align="center" prop="availableCount" :show-overflow-tooltip="true" />
          <el-table-column label="制单人" width="100" align="center" prop="createBy" :show-overflow-tooltip="true" />
          <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="库位" width="200" align="center" prop="locationName" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="位置" width="200" align="center" prop="position" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="批号" width="260" align="center" prop="batchCode" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="载具" width="200" align="center" prop="trayCode" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="冻结标识" align="center" prop="isfreeze" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.wms_stock_isfreeze" :value="scope.row.isfreeze"/>
            </template>
          </el-table-column> -->
          <!-- <el-table-column label="入库日期" width="130" align="center" prop="beginDate" :show-overflow-tooltip="true"/> -->
    <!--      <el-table-column label="结束日期" width="180" align="center" prop="endDate" :show-overflow-tooltip="true"/>-->
          <!-- <el-table-column label="生产日期" width="130" align="center" prop="producedDate" :show-overflow-tooltip="true"/> -->
          <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['wms:checkDelivery:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['wms:checkDelivery:remove']"
              >删除</el-button>
            </template>
          </el-table-column> -->
        </el-table>

        <!-- 盘库策略库区tab -->
        <el-table v-loading="loading" ref="multipleTable" :data="checkDeliveryList" v-show="currentTab == 1" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <!-- <el-table-column label="主键" align="center" prop="id"  :show-overflow-tooltip="true"/> -->
<!--          <el-table-column label="所属组织" fixed align="center" prop="factoryCode" :show-overflow-tooltip="true" />-->
<!--          <el-table-column label="仓库名称" fixed width="200" align="center" prop="wareHouseName" :show-overflow-tooltip="true" />-->
<!--          <el-table-column label="仓库类型" fixed align="center" prop="wareHouseType" :show-overflow-tooltip="true">-->
<!--            <template slot-scope="scope">-->
<!--              <dict-tag :options="dict.type.cims_warehouse_type" :value="scope.row.wareHouseType"/>-->
<!--            </template>-->
<!--          </el-table-column>-->
          <!-- <el-table-column label="物料编码" fixed width="220" align="center" prop="materialCode" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="物料名称" fixed width="100" align="center" prop="materialName" :show-overflow-tooltip="true" /> -->
         <el-table-column label="区域" min-width="200" align="center" prop="areaName" :show-overflow-tooltip="true" />
          <el-table-column label="库区" min-width="200" align="center" prop="reservoirName" :show-overflow-tooltip="true" />
          <el-table-column label="状态" width="100" align="center" prop="reservoirStatus" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span v-if="scope.row.reservoirStatus == '1' ">禁用</span>
              <span v-if="scope.row.reservoirStatus == '0' ">启用</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="规格型号"  width="100" align="center" prop="specifications" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="单位"  align="center" prop="unitName" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="包装方式"  align="center" prop="packUnitName" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="在库数量" align="center" prop="count" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="可用数量" align="center" prop="availableCount" :show-overflow-tooltip="true" /> -->
          <el-table-column label="制单人" width="100" align="center" prop="createBy" :show-overflow-tooltip="true" />
          <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="库位" width="200" align="center" prop="locationName" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="位置" width="200" align="center" prop="position" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="批号" width="260" align="center" prop="batchCode" :show-overflow-tooltip="true" /> -->
          <!-- <el-table-column label="载具" width="200" align="center" prop="trayCode" :show-overflow-tooltip="true" /> -->
         <!-- <el-table-column label="冻结标识" align="center" prop="isfreeze" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.wms_stock_isfreeze" :value="scope.row.isfreeze"/>
            </template>
          </el-table-column> -->
          <!-- <el-table-column label="入库日期" width="130" align="center" prop="beginDate" :show-overflow-tooltip="true"/> -->
    <!--      <el-table-column label="结束日期" width="180" align="center" prop="endDate" :show-overflow-tooltip="true"/>-->
          <!-- <el-table-column label="生产日期" width="130" align="center" prop="producedDate" :show-overflow-tooltip="true"/> -->
          <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['wms:checkDelivery:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['wms:checkDelivery:remove']"
              >删除</el-button>
            </template>
          </el-table-column> -->
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <!-- 添加盘点计划-物料对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body @selection-change="stockOnChange">
      <el-table :data="stockList" @selection-change="stockChange">
        <el-table-column type="selection" width="55" align="center" />
        <!-- <el-table-column label="主键" align="center" prop="id"  :show-overflow-tooltip="true"/> -->
<!--        <el-table-column label="所属组织" align="center" prop="factoryCode" :show-overflow-tooltip="true" />-->
        <el-table-column label="往来单位" align="center" min-width="180" prop="contactsUnitName" :show-overflow-tooltip="true" />
        <!-- <el-table-column label="仓库" width="180" align="center" prop="wareHouseName" :show-overflow-tooltip="true" /> -->
        <el-table-column label="物料编码" min-width="200" align="center" prop="materialCode" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" min-width="200" align="center" prop="materialName" :show-overflow-tooltip="true" />
        <el-table-column label="规格型号" width="100" align="center" prop="specifications" :show-overflow-tooltip="true" />
        <el-table-column label="单位" width="100" align="center" prop="unitName" :show-overflow-tooltip="true" />
        <el-table-column label="包装方式" width="100" align="center" prop="packUnitName" :show-overflow-tooltip="true" />
        <el-table-column label="在库数量" width="100" align="center" prop="libraryCount" :show-overflow-tooltip="true" />
        <el-table-column label="可用数量" width="100" align="center" prop="availableCount" :show-overflow-tooltip="true" />
      </el-table>
     <pagination
        v-show="stockTotal>0"
        :total="stockTotal"
        :page.sync="stockParams.pageNum"
        :limit.sync="stockParams.pageSize"
        @pagination="stockTap"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="isSubmitForm" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

    </el-dialog>



    <!-- 添加盘点计划-库区对话框 -->
    <el-dialog :title="title" :visible.sync="reservoirOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="padding-right: 30px;">
        <!-- <el-form-item label="仓库" prop="warehouseId">
          <el-select v-model="form.warehouseId" @change="changeAreaList()" placeholder="请选择仓库">
            <el-option
              v-for="item in wareList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item> -->

         <el-form-item label="区域" prop="areaId">
             <el-select v-model="form.areaId"  @change="changeReservoirList()" placeholder="请选择区域" clearable>
               <el-option
                 v-for="dict in areaList"
                 :key="dict.id"
                 :label="dict.name"
                 :value="dict.id"
               ></el-option>
             </el-select>
         </el-form-item>

         <el-form-item label="库区" prop="reservoirId">
             <el-select v-model="form.reservoirId" @change="inputChange" placeholder="请选择库区" clearable>
               <el-option
                 v-for="dict in kqList"
                 :key="dict.id"
                 :label="dict.name"
                 :value="dict.id"
               ></el-option>
             </el-select>
         </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="isSubmitForm" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 上传弹框 -->
    <el-dialog title="导入单据" :visible.sync="upload.open" width="400px" append-to-body @close="cancel">
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                 :action="upload.url"
    			       :file-list="fileListd"
                 :disabled="upload.isUploading"
    						 :on-remove="onRemove"
    			       :on-change="onChanc"
                 :on-progress="handleFileUploadProgress"
                 :on-success="handleFileSuccess"
                 :auto-upload="false"
                 drag :before-upload="handleBeforeUpload">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！最大不超过50M</div>
      </el-upload>
      <!-- <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm" :loading="upload.isUploading">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div> -->
    </el-dialog>
  </div>
</template>

<script>

import { listCheckDelivery, getCheckDelivery, delCheckDelivery, addCheckDelivery, updateCheckDelivery, checkDeliveryAdd,addCheckTask} from "@/api/wms/pdcheckDelivery";
import { listReservoir } from "@/api/wms/reservoir";
import { stockMainList } from "@/api/wms/stock";
import { mapGetters } from 'vuex'
import { listLocation } from "@/api/wms/location";
import { listArea } from "@/api/wms/area";
import { wms } from '@/utils/agent';
export default {
  name: "CheckCheckDelivery",
  dicts: ['wms_stock_isfreeze', 'cims_warehouse_type', 'cims_reservoir_type'],
  computed: {
    ...mapGetters([
      'token'
    ]),
  },
  data() {
    return {
      tabs: ['盘点策略-物料', '盘点策略-库区'], // 左侧tab
      currentTab: 0, // 当前选中的tab
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
      checkDeliveryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否弹窗库区盘点弹出层
      reservoirOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stockId: null,
        status: null,
        factory: null,
        auditor: null,
        materialCode: null,
        materialName: null,
        specifications: null,
        batchCode: null,
        trayCode: null,
        warehouseId: null,
        locationId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        warehouseId: [
          { required: true, message: '请选择仓库', trigger: 'blur' }
        ],
        areaId: [
          { required: true, message: '请选择区域', trigger: 'blur' }
        ],
        reservoirId: [
          { required: true, message: '请选择库区', trigger: 'blur' }
        ],
      },
      // 仓库列表
      wareList:[],
      // 所属区域
      queryAreaList:[],
      // 库区列表
      kqQueryList: [],
      // form所属区域
      areaList:[],
      // form库区列表
      kqList: [],
      // 货位列表
      spaceList:[],
      fileListd:[],
      // 上传列表
      upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      stockList:[],
      stockSelection: [], // 新增物料盘点时选中的数据
      // 库存查询参数
      stockParams: {
        pageNum: 1,
        pageSize: 10,
        // readConverterExp:0,
        // isfreeze:0,
      },
      stockTotal:0,
      idStock:[],
      isSubmitForm: false, // 是否正在提交
    };
  },
  created() {
    this.upload.headers = {Authorization: "Bearer " + this.token}
    this.getList();
    this.listArea();
    // this.listLocation();
  },
  watch: {
    $route(to, form) {
      if (to.name == 'CheckCheckDelivery') {
        this.getList()
      }
    }
  },
  methods: {
    // 切换tab
    switchTab (index) {
      if (this.currentTab != index) {
        this.currentTab = index
        this.resetQuery()
        this.$refs.multipleTablem.clearSelection();
        this.$refs.multipleTable.clearSelection();
        this.getList()
      }
    },
    listArea(){
      // 区域
      listArea({pageNum: 1,pageSize: 5000}).then(response => {
        this.queryAreaList = response.rows;
        console.log(this.queryAreaList)
      });
    },
    listDialogArea(){
      listArea({pageNum: 1,pageSize: 5000}).then(response => {
        this.areaList = response.rows;
      });
    },
    // 货位列表
    listLocation(){
      listLocation({pageNum: 1,pageSize: 1000,}).then(res=>{
        this.spaceList = res.rows
      })
    },
    /** 查询仓库下区域 **/
    changeQueryAreaList(){
      this.queryParams.areaId = null;
      this.queryParams.reservoirId = null;
      this.queryParams.locationId = null;
      this.queryAreaList = [];
      this.kqQueryList = [];
      listArea({warehouseId:this.queryParams.warehouseId,pageSize:5000}).then(response => {
        this.queryAreaList = response.rows;
      });
    },
    // 查询区域下库区
    changeQueryReservoirList(){
      this.queryParams.reservoirId = null;
      if (this.queryParams.areaId) {
        listReservoir({areaId:this.queryParams.areaId,pageSize:5000}).then(response => {
          this.kqQueryList = response.rows;
        });
      } else {
        this.kqQueryList = [];
      }
    },


    /** form查询仓库下区域 **/
    changeAreaList(){
        this.form.areaId = null;
        this.form.reservoirId = null;
        this.areaList = [];
        this.kqList = [];
        if(this.form.warehouseId){
          const item = this.wareList.find(item => item.id == this.form.warehouseId)
          this.form.warehouseType = item.type

          listArea({warehouseId:this.form.warehouseId,pageSize:5000}).then(response => {
              this.areaList = response.rows;
          });
          this.$forceUpdate()
        } else{
          this.areaList = []
          // listArea({pageSize:5000}).then(response => {
          //     this.queryAreaList = response.rows;
          // });
          // // 获取库位源数据
          // listLocation({pageNum:1,pageSize:1000}).then(response => {
          //   this.spaceList = response.rows;
          // });
        }
    },
    // form查询区域下库区
    changeReservoirList(){
      this.form.reservoirId = null;
      if (this.form.areaId) {
        listReservoir({areaId:this.form.areaId,pageSize:5000}).then(response => {
          this.kqList = response.rows;
        });
      } else {
        this.kqList = [];
      }
    },

    // 强制更新输入框，在部分输入框值改变后页面显示数据不改变情况下使用
    inputChange() {
      this.$forceUpdate()
    },

    /** 查询库区下库位 **/
    // changeQueryReservoirList(){
    //     this.queryParams.locationId = null;
    //     if(this.queryParams.areaId){
    //       listLocation({areaId:this.queryParams.areaId,pageNum:1,pageSize:5000}).then(response => {
    //           this.spaceList = response.rows;
    //       });
    //     } else{
    //       this.spaceList = []
    //       // 获取库位源数据
    //       // listLocation({pageNum:1,pageSize:1000}).then(response => {
    //       //   this.spaceList = response.rows;
    //       // });
    //     }
    // },
    /** 查询盘点计划列表 */
    getList() {
      this.loading = true;
      let queryParams = JSON.parse(JSON.stringify(this.queryParams))
      if (queryParams.createTimes && queryParams.createTimes.length > 0) {
        queryParams.startTime = queryParams.createTimes[0]
        queryParams.endTime = queryParams.createTimes[1]
      }
      queryParams.checkType = this.currentTab + 1
      listCheckDelivery(queryParams).then(response => {
        this.checkDeliveryList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reservoirOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        warehouseId: null,
        areaId: null,
        reservoirId: null,
        warehouseType: null,
      };
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
      // this.queryParams.materialCode= null
      // this.queryParams.materialName= null
      // this.queryParams.specifications= null
      // this.queryParams.batchCode= null
      // this.queryParams.trayCode= null
      // this.queryParams.warehouseId= null
      // this.queryParams.locationId= null
      this.handleQuery();
      this.changeQueryAreaList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      // this.reset();
      if (this.currentTab == 0) {
        this.stockList = []
        this.idStock = []
        this.stockParams.pageNum = 1
        this.stockTap()
        this.open = true;
        this.title = "添加盘点计划";
      } else {
        this.reset()
        this.reservoirOpen = true;
        this.title = "添加盘点计划";
        this.listDialogArea()
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCheckDelivery(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改盘点计划";
      });
    },
    // 库存列表
    stockTap(){
      stockMainList(this.stockParams).then(res=>{
        this.stockList = res.rows
        this.stockTotal = res.total
      })
    },
    // 库存多选框选中数据
    stockChange(selection) {
      this.stockSelection = selection
      this.idStock = selection.map(item => item.id)
      // this.ids = selection.map(item => item.id)
      // this.single = selection.length!==1
      // this.multiple = !selection.length
    },
    stockOnChange(selection){
    },
    /** 提交按钮 */
    submitForm() {
      if (this.currentTab == 0) {
        if(this.stockSelection.length>0){
          let datas = []
          this.stockSelection.forEach(item => {
            const data = {
              checkType: this.currentTab + 1,
              materialId: item.materialId,
              warehouseId: item.warehouseId,
              warehouseType: item.wareHouseType,
            }
            datas.push(data)
          })
          this.isSubmitForm = true
          checkDeliveryAdd(datas).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.open = false;
            this.getList();
            this.isSubmitForm = false
          }).catch(err => {
            this.isSubmitForm = false
          });
        }else{
          this.$modal.msgError('请选择需要添加的内容')
        }
      } else {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.checkType = this.currentTab + 1
            this.isSubmitForm = true
            checkDeliveryAdd([this.form]).then(response => {
              this.isSubmitForm = false
              this.$modal.msgSuccess("新增成功");
              this.getList();
              this.reservoirOpen = false;
            }).catch(err => {
              this.isSubmitForm = false
            });
          }
        });
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除选中的数据项？').then(function() {
        return delCheckDelivery(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 生成任务按钮操作 */
    generate(row) {
      if(this.ids.length > 0 || row.id){
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认对该数据生成盘点任务？').then(function() {
          return addCheckTask(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("生成成功");
        }).catch(() => {});
      }else{
        this.$modal.msgError('请选择需要生成任务的计划')
      }

    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/checkDelivery/export', {
        ...this.queryParams
      }, `checkDelivery_${new Date().getTime()}.xlsx`)
    },
    // 单据导入
    ImportBill(){
      this.fileListd=[]
      this.upload.isUploading = false
      this.upload.open = true
    },
    onRemove(){
    },
    onChanc(){
    	// if(this.fileNumber != 'x'){
    	// 	this.fileNumber++
    	// }else{
    	// 	this.fileNumber = 0
    	// }
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.isUploading = false
    	if(response.code == 200){
    		this.upload.open = false
    	   this.$modal.msgSuccess(response.msg)
    		this.getList();
    	}else{
        this.upload.isUploading = false
    	   this.$modal.msgError(response.msg)
    	}
    },
    // 上传前 校验大小
    handleBeforeUpload(file) {
      let fileSize = 50
      const isLt = file.size / 1024 / 1024 < fileSize
      if (!isLt) {
        this.msgWarn(`上传文件大小不能超过 ${fileSize} MB!`)
        return false
      }
    },
    // 提交上传文件
    submitFileForm() {
      if(this.fileListd.length >0){
        this.$refs.upload.submit()
        this.upload.isUploading = true
      }else{
        this.$modal.msgError('请选择需要上传文件')
      }
    },
  }
};
</script>

<style lang="scss">
  .tab-item{
    padding: 5px 10px;
    font-size: 14px;
    color: #606266;
    cursor: pointer;
    &.active{
      background: #ECF5FF;
    }
  }
</style>
