<template>
  <div class="app-container">
    <el-dialog title="选择物料" :visible.sync="open" width="1200px" append-to-body>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="物料编码" prop="code">
          <el-input v-model="queryParams.code" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="物料名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <span style="color: red;">备注：出库方式只能是一种（货架/地堆），且出库数量必须在可用数量内！</span>
        </el-col>
      </el-row>

      <el-table v-loading="loading" :data="materialList" highlight-current-row @current-change="handleCurrentChange">
        <el-table-column label="物料编码" align="center" prop="code" width="200" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" align="center" prop="name" :show-overflow-tooltip="true" />
        <el-table-column label="可用数量" align="center" prop="stockInfo" :show-overflow-tooltip="true" >
          <template slot-scope="scope">
            <span>地堆：</span><span style="color: red;">{{ scope.row.materiaLocal }}</span><span style="margin: 0 10px;">|</span><span>货架：</span><span style="color: red;">{{ scope.row.materialTray }}</span>
          </template>
        </el-table-column>
        <el-table-column label="单位" align="center" prop="unitName" :show-overflow-tooltip="true" />
        <!-- <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true" /> -->
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getOutMaterialSelectList } from "@/api/inoutDelivery/outDelivery";

  export default {
    name: "MaterialCom",
    dicts: ['cims_inspection_method'],
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
          code: null,
          name: null,
        },
        current: null, // 当前选中的物料
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

      /** 查询物料管理列表 */
      getList() {
        this.loading = true;
        getOutMaterialSelectList(this.queryParams).then(response => {
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
          this.$emit('setMaterial', this.current)
        }
        this.open = false
      },
  }
};
</script>
