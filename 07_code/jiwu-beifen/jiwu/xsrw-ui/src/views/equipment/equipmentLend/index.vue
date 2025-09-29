<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="单据编号" prop="equipmentCode">-->
<!--        <el-input v-model="queryParams.equipmentCode" placeholder="请输入单据编号" clearable @keyup.enter.native="handleQuery" />-->
<!--      </el-form-item>-->
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input v-model="queryParams.equipmentName" placeholder="请输入设备名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery" >搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增借出单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="equipmentLendList">
<!--      <el-table-column label="单据编号" align="center" prop="code" />-->
      <el-table-column label="设备编号" align="center" prop="equipmentCode" />
      <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="借出数量" align="center" prop="loanCount" />
      <el-table-column label="可用数量" align="center" prop="availableCount" />
      <el-table-column label="借出人" align="center" prop="loanBy" />
      <el-table-column label="借出时间" align="center" prop="loanTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loanTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="操作" align="center" fixed="right" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetailed(scope.row)" v-hasPermi="['wms:maintenanceDay:query']" >查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-sold-out" @click="handleUpdate(scope.row)" v-hasPermi="['wms:maintenanceDay:dayCancel']">还入</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 借出对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" :disabled="isShow">
        <el-form-item label="设备名称" prop="equipmentName">
          <el-input v-model="form.equipmentName" placeholder="请选择设备名称" @focus="handleChooseEquipment" />
        </el-form-item>
        <el-form-item label="设备编号">
          <el-input v-model="form.equipmentCode" disabled placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="可用数量">
          <el-input v-model="form.availableCount" disabled placeholder="请输入可用数量" v-intNumber maxlength="6" />
        </el-form-item>
        <el-form-item label="借出数量" prop="loanCount">
          <el-input v-model="form.loanCount" placeholder="请输入借出数量" v-intNumber maxlength="6" />
        </el-form-item>
        <el-form-item label="借出人" prop="loanBy">
          <el-input v-model="form.loanBy" placeholder="请输入借出人" />
        </el-form-item>
        <el-form-item label="借出时间" prop="loanTime">
          <el-date-picker style="width: 100%" v-model="form.loanTime" type="date" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="!isShow" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择设备 -->
    <choose-equipmentR ref="chooseEquipmentR" @setEquipment="setEquipment"></choose-equipmentR>
  </div>
</template>
<script>
import { listRecord, getRecord, addRecord, returnRecord,  } from "@/api/equipment/loan/record";

import { wms } from "@/utils/agent";

import store from "@/store";

import ChooseEquipmentR from './components/chooseEquipmentR';
export default {
  name: 'equipmentLend',
  dicts: ["equ_fault_lv", "maintenance_status"],
  components: { ChooseEquipmentR },
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
      // 设备借出表格数据
      equipmentLendList: [],
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
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        equipmentName: [{ required: true, message: '请选择设备', trigger: 'change' }],
        registerId: [{ required: true, message: '请输入借出人', trigger: 'blur' }],
        loanCount: [{ required: true, message: '请输入借出数量', trigger: 'blur' }],
        loanTime: [{ required: true, message: '请选择借出时间', trigger: 'change' }],
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
    /** 查询设备借出列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.equipmentLendList = response.rows;
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
      this.title = "新增借出单";
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
    /** 选择设备 */
    handleChooseEquipment() {
      this.$refs.chooseEquipmentR.open = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          addRecord(this.form).then((response) => {
            this.$modal.msgSuccess("新增成功");
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
        this.title = "查看设备借出详情";
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/loan/record/export?exportType=1', { ...this.queryParams }, `record_${new Date().getTime()}.xlsx`)
    },
    /** 选择设备返回操作 */
    setEquipment(row) {
      this.form.loanRegisterId = row.registerId ? row.registerId : ''
      this.form.equipmentName = row.name ? row.name : ''
      this.form.equipmentCode = row.equNo ? row.equNo : ''
      this.form.equipmentId = row.id ? row.id : ''
      this.form.availableCount = row.availableCount ? row.availableCount : ''
    },
  }
}
</script>
