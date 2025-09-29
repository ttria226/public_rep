<template>
  <div class="app-container">
    <el-dialog title="选择物料库存" :visible.sync="open" width="60%" append-to-body>
      <!-- <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="载具编号" prop="code">
          <el-input v-model="queryParams.code" placeholder="请输入载具编号" :maxlength="40" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form> -->
      <el-table v-loading="loading" :data="materialList" highlight-current-row @current-change="handleCurrentChange">
        <el-table-column label="库位" align="center" prop="locationname"></el-table-column>
        <el-table-column label="库区" align="center" prop="reservoirname"></el-table-column>
        <el-table-column label="载具编号" align="center" prop="code" width="250"></el-table-column>
        <el-table-column label="载具类型" align="center" prop="trayCategory">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wms_t_tray_category" :value="scope.row.trayCategory" />
          </template>
        </el-table-column>
        <el-table-column label="库存数量" align="center" prop="availableCount"></el-table-column>
      </el-table>

      <!-- <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" /> -->

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="buttonLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel" :loading="buttonLoading">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOutMaterialStockSelectList } from "@/api/inoutDelivery/outDelivery";

export default {
  name: "MaterialStockCom",
  dicts: ['wms_t_tray_category'],
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
      // 物料库存表格数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        // pageNum: 1,
        // pageSize: 10,
        // code: null,
        // name: null,
        materialId: null,
        type: null
      },
      materialId: null, //物料id
      type: '',//类型
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
  created() {
    // this.getList();
    // this.getModeList();
  },
  methods: {
    /** 查询物料库存列表 */
    getList() {
      this.loading = true;
      this.queryParams.materialId = this.materialId
      this.queryParams.type = this.type
      getOutMaterialStockSelectList(this.queryParams).then(response => {
        this.materialList = response.rows;
        this.total = response.total;
        this.loading = false;
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 选中行
    handleCurrentChange(row) {
      this.current = row
    },
    /** 提交按钮 */
    submitForm() {
      if (this.current) {
        this.$emit('setMaterialStock', this.current)
      }
      this.open = false
    },
  }
};
</script>
