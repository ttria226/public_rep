<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input v-model="queryParams.equipmentName" placeholder="请输入设备名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.d_loan_return_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery" >搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['wms:maintenanceDay:add']">新增工单</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="equipmentReturnList">
<!--      <el-table-column label="单据编号" align="center" prop="dayNo" />-->
      <el-table-column label="设备编号" align="center" prop="equipmentCode" />
      <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="借出数量" align="center" prop="loanCount" />
      <el-table-column label="还入数量" align="center" prop="returnCount" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <div class="status-block" v-for="item in dict.type.d_loan_return_status" :key="item.value" :style="{ 'background-color': statusColor[scope.row.status] }" v-show="item.value == scope.row.status">
            {{ scope.row.status === 0 ? "待还入" : item.label }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="还入时间" align="center" prop="returnTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-sold-out" v-if="scope.row.status !== '3'" @click="handleUpdate(scope.row)">还入</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 还入对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" :disabled="isShow">
        <el-form-item label="还入数量" prop="returnCount">
          <el-input v-model="form.returnCount" placeholder="请输入还入数量" v-intNumber maxlength="6" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="还入时间" prop="returnTime">
          <el-date-picker style="width: 100%" v-model="form.returnTime" type="date" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="!isShow" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listRecord, getRecord, addRecord, returnRecord,  } from "@/api/equipment/loan/record";

import { wms } from "@/utils/agent";

import store from "@/store";
export default {
  name: 'equipmentReturn',
  dicts: ["d_loan_return_status", "maintenance_status"],
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
      // 设备还入表格数据
      equipmentReturnList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentCode: null,
        equipmentName: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        returnCount: [{ required: true, message: '请输入还入数量', trigger: 'blur' }],
        // remark: [{ required: true, message: '请输入故障描述', trigger: 'blur' }],
        returnTime: [{ required: true, message: '请选择还入时间', trigger: 'change' }],
      },

      isShow: false, //是否查看
      currentInfo: {}, //当前选中行

      statusColor: ['#FE1E00', '#CBAB00', '#33CCB0', '#269932']
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询设备还入列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.equipmentReturnList = response.rows;
        this.total = response.total;
      }).finally(() => {
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.isShow = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        loanRegisterId:null,
        equipmentId: null,
        equipmentCode: null,
        equipmentName: null,
        loanCount: null,
        loanBy: null,
        loanTime: null,
      };
      this.$nextTick(() => {
        this.resetForm("form");
      })
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.form.createBy = store.getters.name;
      this.title = "还入";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "还入";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let param = {
            id:this.form.id,
            remark:this.form.remark,
            returnTime:this.form.returnTime,
            returnCount:this.form.returnCount,
          }
          returnRecord(param).then((response) => {
            this.$modal.msgSuccess("还入成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    // 查看工单详情
    handleDetailed(row) {
      this.reset();
      const id = row.id
      getRecord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.isShow = true
        this.title = "查看设备还入详情";
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/loan/record/export?exportType=2', { ...this.queryParams }, `return_${new Date().getTime()}.xlsx`)
    },
  }
}
</script>
