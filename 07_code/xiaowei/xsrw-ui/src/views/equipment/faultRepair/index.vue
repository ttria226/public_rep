<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="equName">
        <el-input v-model="queryParams.equName" placeholder="请输入设备名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="报障人" prop="createBy">
        <el-input v-model="queryParams.createBy" placeholder="请输入报障人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['wms:repairReport:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:repairReport:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="faultRepairList">
      <el-table-column label="序号" type="index" align="center" prop="id" />
      <el-table-column label="设备编号" align="center" prop="equNo" min-width="200"/>
      <el-table-column label="设备名称" align="center" prop="equName" min-width="200" />
      <el-table-column label="申请人" align="center" prop="createBy" width="120" />
      <el-table-column label="来源" align="center" prop="source" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.source == 2">新建</span>
          <span v-if="scope.row.source == 1">设备巡检</span>
        </template>
      </el-table-column>
      <el-table-column label="报障时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="设备状态" align="center" prop="equipmentStatus" width="100">
        <template slot-scope="scope">
          <div class="status-block" v-for="item in dict.type.equipment_type" :key="item.value" :style="{ 'background-color': statusColor[scope.row.equipmentStatus] }" v-show="item.value == scope.row.equipmentStatus">{{ item.label }}</div>
        </template>
      </el-table-column>
      <el-table-column label="生成工单状态" align="center" prop="status" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 1">未生成</span>
          <span v-if="scope.row.status === 2">已生成</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status === 1" @click="handleUpdate(scope.row)" v-hasPermi="['wms:repairReport:edit']">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)" v-hasPermi="['wms:repairReport:edit']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status === 1" @click="handleDelete(scope.row)" v-hasPermi="['wms:repairReport:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-position" v-if="scope.row.status === 1" @click="getOrder(scope.row)" v-hasPermi="['wms:repairReport:edit']">生成工单</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改故障设备信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" :disabled="isShow">
        <!-- <el-form-item label="所属部门" prop="depId">
          <treeselect v-model="form.depId" :options="deptOptions" :normalizer="normalizer" placeholder="选择所属部门" @select="getPerson" />
        </el-form-item> -->
        <el-form-item label="申请人" prop="createBy">
          <!-- <el-select v-model="form.createBy" filterable placeholder="请选择申请人" clearable style="width: 100%;">
            <el-option v-for="item in personList" :key="item.userId" :label="item.userName" :value="item.userId"></el-option>
          </el-select> -->
          <el-input v-model="form.createBy" disabled placeholder="请选择设备名称" />
        </el-form-item>
        <el-form-item label="设备名称" prop="equName">
          <el-input v-model="form.equName" placeholder="请选择设备名称" @focus="handleChooseEquipment" />
        </el-form-item>
        <el-form-item label="设备编号" prop="equNo">
          <el-input v-model="form.equNo" disabled placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="故障零配件名称">
          <el-input v-model="form.faultyAccessoryName" placeholder="请输入故障零配件名称" />
        </el-form-item>
        <el-form-item label="故障描述" prop="faultMessage">
          <el-input v-model="form.faultMessage" type="textarea" placeholder="请输入故障描述" />
        </el-form-item>
        <el-form-item label="故障等级" prop="faultLv">
          <el-radio-group v-model="form.faultLv">
            <el-radio v-for="dict in dict.type.equ_fault_lv" :key="dict.value" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item label="设备状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.maintenance_status" :key="dict.value" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="是否停机" prop="isShutdown">
          <el-radio-group v-model="form.isShutdown">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传图片" prop="img">
					<image-upload v-model="form.img" :imageDisabled="isShow"></image-upload>
				</el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="!isShow" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择设备 -->
    <choose-equipment ref="chooseEquipment" @setEquipment="setEquipment"></choose-equipment>
  </div>
</template>
<script>
import { listDept } from "@/api/system/dept";
import { listFaultRepairOrder, getFaultRepairOrder, addFaultRepairOrder, updateFaultRepairOrder, delFaultRepairOrder, createMaintainOrder } from "@/api/equipment/faultRepair/faultRepair";
import { getAction } from "@/api/manage"
import { wms } from "@/utils/agent";

import store from "@/store";
import ChooseEquipment from '../components/chooseEquipment';
export default {
  components: { ChooseEquipment },
  name: 'faultRepairOrder',
  dicts: ["equ_fault_lv","maintenance_status","equipment_type"],
  data(){
    return{
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
      // 故障报修表格数据
      faultRepairList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equName: null,
        createBy: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // deptId: [{ required: true, message: '请选择所属部门', trigger: 'change' }],
        createBy: [{ required: true, message: '请选择申请人', trigger: 'change' }],
        equName: [{ required: true, message: '请选择设备', trigger: 'change' }],
        faultMessage: [{ required: true, message: '请输入故障描述', trigger: 'blur' }],
        // status: [{ required: true,  message: '请选择设备状态', trigger: 'change' }],
        faultLv: [{ required: true,  message: '请选择故障等级', trigger: 'change' }],
        isShutdown: [{ required: true, message: '请选择是否停机',  trigger: 'change' }],
        img: [{ required: true,  message: '请上传图片', trigger: 'change' }],
      },
      deptOptions: [], //部门列表
      personList: [], //申请人列表

      isShow: false, //是否查看
      currentInfo: {}, //当前选中行

      statusColor: ['','#3399FF','#CC3370','']
    }
  },
  created(){
    this.getList()
    this.getListDept()
  },
  methods: {
    // 获取申请人列表
    getPerson(node) {
      this.form.depName = node.deptName
      getAction('system/user/list', {
        deptId: node.deptId
      }).then(res => {
        console.log(res)
        if (res.code == 200) {
          this.personList = res.rows
        } else {
          this.$modal.msgError(res.msg);
        }
      })
    },
    // 构造树形结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    // 获取部门数据
    getListDept() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      })
    },
    /** 查询维修工单列表 */
    getList() {
      this.loading = true;
      listFaultRepairOrder(this.queryParams).then(response => {
        this.faultRepairList = response.rows;
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
        createBy: null,
        equipmentId: null,
        equNo: null,
        equName: null,
        faultyAccessoryName: null,
        faultMessage: null,
        faultLv: null,
        isShutdown: null,
        img: null,
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
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.form.createBy = store.getters.name;
      this.title = "添加设备故障信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getFaultRepairOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备故障信息";
      });
    },
    /** 选择设备 */
    handleChooseEquipment(){
      this.$refs.chooseEquipment.open = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateFaultRepairOrder(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFaultRepairOrder(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    // 查看工单详情
    handleDetail(row) {
      this.reset();
      const id = row.id
      getFaultRepairOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.isShow = true
        this.title = "查看设备故障信息";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
        return delFaultRepairOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("撤销成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/repairReport/export', {...this.queryParams}, `EquipmentFaultRepair_${new Date().getTime()}.xlsx`)
    },
    /** 生成工单操作 */
    getOrder(row) {
      const ids = row.id;
      this.$modal.confirm('是否确认该条数据生成工单？').then(function() {
        return createMaintainOrder({ id: ids });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("生成工单成功");
      }).catch(() => {});
    },
    /** 选择设备返回操作 */
    setEquipment(row){
      this.form.equName = row.name ? row.name : ''
      this.form.equNo = row.equNo ? row.equNo : ''
      this.form.equipmentId = row.id ? row.id : ''
    },
  }
}
</script>