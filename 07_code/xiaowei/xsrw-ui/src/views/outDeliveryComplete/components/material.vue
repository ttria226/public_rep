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

      <el-table v-loading="loading" :data="materialList" highlight-current-row @current-change="handleCurrentChange">
        <el-table-column label="物料编码" align="center" prop="code" width="200" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" align="center" prop="name" :show-overflow-tooltip="true" />
        <el-table-column label="价格（元）" align="center" prop="sumPrice" :show-overflow-tooltip="true" />
        <el-table-column label="重量（kg）" align="center" prop="sumWeight" :show-overflow-tooltip="true" />
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
  import { getBomSelectList, getMaterialListByBomId } from "@/api/wms/bom";

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
          // pageNum: 1,
          // pageSize: 10,
          code: null,
          name: null,
        },
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

      /** 查询物料管理列表 */
      getList() {
        this.loading = true;
        getBomSelectList(this.queryParams).then(response => {
          this.materialList = response.data;
          // this.total = response.total;
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
      /** 提交按钮 */
      submitForm() {
        if (this.current) {
          this.buttonLoading = true
          getMaterialListByBomId({ id: this.current.id }).then(response => {
            this.$emit('setMaterial', { bomDetail: response.data, bomId: this.current.id, bomName: this.current.name })
            this.open = false
          }).finally(() => {
            this.buttonLoading = false
          });
        } else {
          this.open = false
        }
      },
  }
};
</script>
