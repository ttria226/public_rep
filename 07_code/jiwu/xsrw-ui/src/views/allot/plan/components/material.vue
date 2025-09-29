<template>
  <div class="app-container">
    <el-dialog title="选择调拨物料" :visible.sync="open" width="60%" append-to-body>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="物料编码" prop="materialCode">
          <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="allotStockList" highlight-current-row @current-change="handleCurrentChange">
        <el-table-column label="区域" align="center" prop="areaName" min-width="180" />
        <el-table-column label="库区" align="center" prop="reservoirName" min-width="120" />
        <el-table-column label="库位" align="center" prop="locationName" min-width="120" />
        <el-table-column label="物料编码" align="center" prop="materialCode" min-width="150" />
        <el-table-column label="物料名称" align="center" prop="materialName" min-width="180" />
        <el-table-column label="计量单位" align="center" prop="unitName" width="100" />
        <el-table-column label="批次号" align="center" prop="batchCode" width="150" />
        <el-table-column label="库存数量" align="center" prop="availableCount" width="120" />
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
import { listStock } from "@/api/wms/stock";

export default {
  name: "allotStockMaterialCom",
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
      //调拨库存表格数据
      allotStockList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        deptId: null,
        materialCode: null,
        materialName: null,
        isFreeze: 0,
      },
      deptId: null,
      current: null, // 当前选中的物料

      buttonLoading: false, //按钮loading
    };
  },
  watch: {
    open(){
      if (this.open) {
        this.getList();
      }
    }
  },
  methods: {
    /** 查询调拨库存列表 */
    getList() {
      this.loading = true;
      this.queryParams.deptId = this.deptId
      listStock(this.queryParams).then(response => {
        this.allotStockList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.queryParams = {
        deptId: null,
        materialCode: null,
        materialName: null,
        isFreeze: 0,
      }
      // this.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      // this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
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
      if (this.current) {
        this.$emit('setMaterial', this.current)
      } else {
        this.$modal.msgError('请选择需要添加的内容')
      }
    },
  }
};
</script>
