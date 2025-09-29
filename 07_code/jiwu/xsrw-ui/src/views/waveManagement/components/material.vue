<template>
  <div class="app-container">
    <el-dialog title="新增波次计划" :visible.sync="open" width="1200px" append-to-body>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="出库单号" prop="code">
          <el-input v-model="queryParams.code" placeholder="请输入出库单号" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="出库类型" prop="type">
          <el-select v-model="queryParams.type" clearable placeholder="请选择出库类型">
            <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="outPlanList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="出库单号" align="center" prop="code" width="200" :show-overflow-tooltip="true" />
        <el-table-column label="出库类型" align="center" prop="type" :show-overflow-tooltip="true" width="120">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.inout_out_type" :value="scope.row.type" />
          </template>
        </el-table-column>
        <el-table-column label="物料使用部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
        <el-table-column label="申请人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100"/>
        <el-table-column label="来源" align="resource" prop="newLocal" :show-overflow-tooltip="true" min-width="100">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.inout_out_local" :value="scope.row.newLocal" />
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.inout_out_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="出库状态" align="center" prop="completeState" :show-overflow-tooltip="true" min-width="100">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.complete_state" :value="scope.row.completeState" />
          </template>
        </el-table-column>
        <el-table-column label="审核人" align="center" prop="auditor" :show-overflow-tooltip="true" min-width="100"/>
        <el-table-column label="创建日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="120"/>
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
import { listMergeOutDelivery } from "@/api/inoutDelivery/outDelivery";

export default {
  name: "outPlanCom",
  dicts: ["inout_out_local", "inout_out_type", "inout_out_status","complete_state"],
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
      // 出库计划表格数据
      outPlanList: [],
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
  methods: {
    /** 查询出库计划列表 */
    getList() {
      this.loading = true;
      listMergeOutDelivery(this.queryParams).then(response => {
        this.outPlanList = response.rows;
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
      this.ids = selection.map((item) => item.id);
    },
    /** 提交按钮 */
    submitForm() {
      if (this.ids && this.ids.length > 0) {
        this.$emit('setPlan', { ids: this.ids })
      } else {
        this.$modal.msgError('请选择需要添加的内容')
      }
    },
  }
};
</script>
