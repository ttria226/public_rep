<template>
  <div class="app-container">
    <el-dialog title="新增盘点计划" :visible.sync="open" width="1200px" append-to-body>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="物料编码" prop="materialCode">
          <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="批次" prop="batchCode">
          <el-input v-model="queryParams.batchCode" placeholder="请输入批次" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="库位" prop="locationId">
          <el-select v-model="queryParams.locationId" placeholder="请选择库位" clearable>
            <el-option v-for="dict in queryLocationList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入库时间" prop="dataTime">
          <el-date-picker v-model="dataTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="物料编码" align="center" prop="materialCode" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" align="center" prop="materialName" min-width="180" :show-overflow-tooltip="true" />
        <el-table-column label="规格" align="center" prop="specifications" min-width="120" :show-overflow-tooltip="true" />
        <el-table-column label="批次" align="center" prop="batchCode" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column label="库位" align="center" prop="locationName" min-width="120" :show-overflow-tooltip="true" />
        <el-table-column label="库存数量" align="center" prop="count" min-width="120" :show-overflow-tooltip="true" />
        <el-table-column label="入库时间" align="center" prop="createTime" min-width="150" :show-overflow-tooltip="true" />
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="buttonLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel" :loading="buttonLoading">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { stockInventoryList } from "@/api/wms/stock";
import { listLocation } from "@/api/wms/location";

export default {
  name: "MaterialCom",
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
      // 物料管理表格数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        beginDate: null,
        endDate: null,
        batchCode: null,
        locationId: null,
      },
      trayType: null, //大类型
      dataTime: [], //日期选择
      current: null, // 当前选中的物料
      queryLocationList: [], // 筛选的库位列表

      buttonLoading: false, //按钮loading
    };
  },
  watch: {
    open(){
      if (this.open) {
        this.getLocationList()
        this.getList();
      }
    }
  },
  methods: {
    /** 查询物料管理列表 */
    getList() {
      this.loading = true;
      if(this.dataTime && this.dataTime.length > 0){
        this.queryParams.beginDate = this.dataTime[0] + ' 00:00:00'
        this.queryParams.endDate = this.dataTime[1] + ' 23:59:59'
      } else {
        this.queryParams.beginDate = null
        this.queryParams.endDate = null
      }
      stockInventoryList({...this.queryParams, trayType: this.trayType}).then(response => {
        this.materialList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取库位 */
    getLocationList(){
      listLocation({pageNum: 1,pageSize:5000}).then(response => {
        this.queryLocationList = response.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dataTime = []
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 选中行
    handleCurrentChange(row) {
      this.current = row
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.materialId);
    },
    /** 提交按钮 */
    submitForm() {
      if (this.ids && this.ids.length > 0) {
        this.$emit('setMaterial', { ids: this.ids })
      } else {
        this.$modal.msgError('请选择需要添加的内容')
      }
    },
  }
};
</script>
