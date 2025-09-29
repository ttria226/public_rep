<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input
          v-model="queryParams.materialCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="批次号" prop="batchCode">
        <el-input
          v-model="queryParams.batchCode"
          placeholder="请输入批次号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="仓库" prop="warehouseId">
         <el-select v-model="queryParams.warehouseId" @change="changeQueryAreaList()" placeholder="请选择仓库" clearable>
           <el-option
             v-for="item in warehouses"
             :key="item.id"
             :label="item.name"
             :value="item.id">
           </el-option>
         </el-select>
      </el-form-item> -->
      <el-form-item label="区域" prop="areaId">
          <el-select v-model="queryParams.areaId"  @change="changeQueryReservoirList()" placeholder="请选择区域" clearable>
            <el-option
              v-for="dict in queryAreaList"
              :key="dict.id"
              :label="dict.name"
              :value="dict.id"
            ></el-option>
          </el-select>
      </el-form-item>
      <el-form-item label="库位" prop="locationId">
      <el-select v-model="queryParams.locationId" placeholder="请选择库位" clearable>
        <el-option
          v-for="item in locations"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      </el-form-item>
      <el-form-item label="载具类型" prop="locationType">
        <el-select v-model="queryParams.locationType" clearable placeholder="请选择载具类型">
          <el-option v-for="dict in dict.type.wms_location_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="载具" prop="trayCode">
        <el-input
          v-model="queryParams.trayCode"
          placeholder="请输入载具编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
         <!-- <el-select v-model="queryParams.trayId" placeholder="请选择载具" clearable>
           <el-option
             v-for="item in trays"
             :key="item.id"
             :label="item.code"
             :value="item.id">
           </el-option>
         </el-select> -->
      </el-form-item>
      <el-form-item label="状态" prop="isFreeze">
        <el-select v-model="queryParams.isFreeze" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.stock_is_freeze"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="冻结类型" prop="originType">
        <el-select v-model="queryParams.originType" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.stock_origin_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="物料规格" prop="unitId">
       <el-select v-model="queryParams.unitId" placeholder="请选择物料规格">
         <el-option
           v-for="item in units"
           :key="item.id"
           :label="item.name"
           :value="item.id">
         </el-option>
       </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['wms:stock:add']"
        >新增</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['wms:stock:edit']"
        >修改</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['wms:stock:remove']"
        >删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="stockUpdateFreeze('1')"
          v-hasPermi="['stock:stock:updateFreeze']"
        >冻结</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click="stockUpdateFreeze('0')"
          v-hasPermi="['stock:stock:Freezeupdate']"
        >解冻</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click=""
          v-hasPermi="['']"
        >移库</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          size="mini"
          :disabled="multiple"
          @click=""
          v-hasPermi="['']"
        >单据导入</el-button>
      </el-col> -->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['wms:stock:export']"-->
<!--        >导出到excel</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" :selectable='selectEve' />
      <!-- <el-table-column label="主键" align="center" prop="id"  :show-overflow-tooltip="true"/> -->
      <el-table-column label="状态" min-width="100px" align="center" prop="isFreeze" :show-overflow-tooltip="true" fixed="left">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.stock_is_freeze" :value="scope.row.isFreeze"/>
        </template>
      </el-table-column>
      <el-table-column label="冻结类型" min-width="100px" align="center" prop="originType" :show-overflow-tooltip="true" fixed="left">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.stock_origin_type" :value="scope.row.originType"/>
        </template>
      </el-table-column>
      <el-table-column label="所属组织" min-width="150px" align="center" prop="deptName" :show-overflow-tooltip="true" />
      <el-table-column label="物料编码" min-width="200px" align="center" prop="materialCode" :show-overflow-tooltip="true" />
      <el-table-column label="物料名称" min-width="200px" align="center" prop="materialName" :show-overflow-tooltip="true" />
      <el-table-column label="批次号" align="center" min-width="200px" prop="batchCode" :show-overflow-tooltip="true" />
      <el-table-column label="规格型号" align="center" min-width="100px" prop="specifications" :show-overflow-tooltip="true" />
      <el-table-column label="单位" align="center" min-width="100px" prop="unitName" :show-overflow-tooltip="true" />
      <!-- <el-table-column label="仓库" align="center" prop="wareHouseName" :show-overflow-tooltip="true" width="220" /> -->
      <el-table-column label="区域" align="center" prop="areaName" :show-overflow-tooltip="true" min-width="180px"/>
      <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="180px"/>
      <el-table-column label="库位名称" align="center" min-width="180px" prop="locationName" :show-overflow-tooltip="true" />
      <el-table-column label="库位类型" align="center" prop="locationType" :show-overflow-tooltip="true" min-width="100px">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_location_type" :value="scope.row.locationType"/>
        </template>
      </el-table-column>
       <el-table-column label="载具" align="center" min-width="200px" prop="trayCode" :show-overflow-tooltip="true" />
      <el-table-column label="在库数量" align="center" prop="count" :show-overflow-tooltip="true"  min-width="100px"/>
      <el-table-column label="可用数量" align="center" prop="availableCount" :show-overflow-tooltip="true" min-width="100px" />
      <!-- <el-table-column label="载具" align="center" prop="trayCode" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="可用数量" align="center" prop="availableCount" :show-overflow-tooltip="true" /> -->
     <!-- <el-table-column label="开始日期" align="center" prop="beginDate" :show-overflow-tooltip="true" />
      <el-table-column label="结束日期" align="center" prop="endDate" :show-overflow-tooltip="true" />
      <el-table-column label="生产日期" align="center" prop="producedDate" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:stock:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:stock:remove']"
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
  </div>
</template>

<script>
import { listStock, getStock, delStock, addStock, updateStock, updateFreeze } from "@/api/wms/stock";
import { listTray } from "@/api/wms/Tray";
import { listArea } from "@/api/wms/area";
import { listUnit } from "@/api/wms/unit";
import { listLocation } from "@/api/wms/location";

export default {
  name: "FrozenThaw",
  dicts: ['stock_origin_type','stock_is_freeze','wms_location_type'],
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
      // 库存表格数据
      stockList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
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
        isFreeze: null,
        originType: null,
        code: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      warehouses: [], //仓库源数据
      queryAreaList:[], //区域源数据
      trays: [], // 载具源数据
      units: [], // 规格源数据
      locations: [], // 库位源数据
      // originTypes: [], // 库存冻结来源类型源数据
      // freeze: [], // 库存冻结状态源数据
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'FrozenThaw') {
        this.getList();
      }
    }
  },
  created() {
    listArea({pageNum:1,pageSize:1000,status:'0'}).then(response => {
        this.queryAreaList = response.rows;
    });
    // 获取载具源数据
    listTray({pageNum:1,pageSize:1000}).then(response => {
      this.trays = response.rows;
    });
    // 获取规格源数据
    listUnit({pageNum:1,pageSize:1000}).then(response => {
      this.units = response.rows;
    });
    // 获取库位源数据
    listLocation({pageNum:1,pageSize:1000}).then(response => {
      this.locations = response.rows;
    });
    // // 获取库存冻结来源类型
    // this.getDicts('stock_origin_type').then(response => {
    //   this.originTypes = response.data
    // }),
    // // 获取库存冻结状态
    // this.getDicts('stock_is_freeze').then(response => {
    //   this.freeze = response.data
    // }),
    this.getList();
  },
  methods: {
    // // 类型翻译
    // convertOriginType(row, column) {
    //   const item = this.originTypes.find(item => item.dictValue == row.originType)
    //   return item && item.dictLabel ? item.dictLabel : '--'
    // },
    // // 库存冻结来源类型翻译
    // convertFreeze(row, column) {
    //   const item = this.freeze.find(item => item.dictValue == row.isFreeze)
    //   const freeze = item && item.dictLabel ? item.dictLabel : '--'
    //   return freeze
    // },
    // table表多选禁用
    selectEve(row, rowIndex) {
      if (row.isFreeze == "0" || !row.originType || row.originType == 1) {
        return true
      } else {
        return false
      }
    },
    /** 查询库存列表 */
    getList() {
      this.loading = true;
      listStock(this.queryParams).then(response => {
        this.stockList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
        isFreeze: null,
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
    /** 查询仓库下区域 **/
    changeQueryAreaList(){
        this.queryParams.areaId = null;
        this.queryParams.locationId = null;
        listArea({warehouseId:this.queryParams.warehouseId,pageSize:5000,status:'0'}).then(response => {
            this.queryAreaList = response.rows;
        });
        // 获取库位源数据
        listLocation({warehouseId:this.queryParams.warehouseId,pageNum:1,pageSize:1000}).then(response => {
          this.locations = response.rows;
        });
    },
    /** 查询库区下库位 **/
    changeQueryReservoirList(){
        this.queryParams.locationId = null;
        listLocation({areaId:this.queryParams.areaId,pageSize:5000}).then(response => {
            this.locations = response.rows;
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
      // 重置区域，库位下拉选项
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
      this.reset();
      this.open = true;
      this.title = "添加库存";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStock(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改库存";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStock(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStock(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
        return delStock(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    // handleExport() {
    //   this.download('wms/stock/export', {
    //     ...this.queryParams
    //   }, `stock_${new Date().getTime()}.xlsx`)
    // },
    // 冻结解冻
    stockUpdateFreeze(isFreeze) {

      let msg = isFreeze =='1' ? '是否确认将选中数据冻结？':'是否确认将选中数据解冻？';
      const data = new FormData()
        data.append("ids",this.ids)
        data.append("isFreeze",isFreeze)
        data.append("originType",'1')
      this.$modal.confirm(msg).then(function() {

        return updateFreeze(data);
      }).then(() => {

        this.$modal.msgSuccess(isFreeze == "1" ? "冻结成功" : "解冻成功");
        this.getList();
      }).catch(() => {});


      // data = {
      //   ids: this.ids,
      //   isFreeze: isFreeze,
      //   originType: '1',
      // }
      // const data = new FormData()
      // data.append("ids",this.ids)
      // data.append("isFreeze",isFreeze)
      // data.append("originType",'1')
      // updateFreeze(data).then(res => {
      //   this.getList();
      //   this.$modal.msgSuccess(isFreeze == "1" ? "冻结成功" : "解冻成功");
      // })
    }
  }
};
</script>
