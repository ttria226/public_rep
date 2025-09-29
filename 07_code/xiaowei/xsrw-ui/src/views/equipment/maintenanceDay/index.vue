<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="设备名称" prop="equName">
        <el-input
          v-model="queryParams.equName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="保养类型" prop="maintenanceType">
        <el-select
          v-model="queryParams.maintenanceType"
          placeholder="请选择保养类型"
          clearable
        >
          <el-option
            v-for="dict in dict.type.maintenance_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="执行人" prop="executorName">
        <el-input
          v-model="queryParams.executorName"
          placeholder="请输入执行人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="保养单号" prop="dayNo">
        <el-input
          v-model="queryParams.dayNo"
          placeholder="请输入保养单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工单时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          type="date"
          placeholder="请选择工单时间"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['wms:maintanenceplan:add']"
          >新增工单</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:maintanenceplan:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="maintainOrderList">
      <el-table-column label="序号" type="index" align="center" prop="id" />
      <el-table-column label="保养单号" align="center" prop="dayNo" />
      <el-table-column label="设备名称" align="center" prop="equName" />
      <el-table-column label="设备编号" align="center" prop="equNo" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="来源" align="center" prop="source">
        <template slot-scope="scope">
          <span v-if="scope.row.source == 1">计划生成</span>
          <span v-if="scope.row.source == 2">新建工单</span>
        </template>
      </el-table-column>
      <el-table-column label="申请人" align="center" prop="createBy" />
      <el-table-column label="保养类别" align="center" prop="maintenanceType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.maintenance_type"
            :value="scope.row.maintenanceType"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="保养完成时间"
        align="center"
        prop="planDay"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planDay, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="执行人"
        align="center"
        prop="executorName"
        min-width="100"
      />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <div
            class="status-block"
            v-for="item in dict.type.maintenance_status"
            :key="item.value"
            :style="{ 'background-color': statusColor[scope.row.status] }"
            v-show="item.value == scope.row.status"
          >
            {{ scope.row.status === 0 ? "作废" : item.label }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="250">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.status === 1 || scope.row.status === 2"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:maintanenceplan:edit']"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetailed(scope.row)"
            v-hasPermi="['wms:maintanenceplan:edit']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-position"
            v-if="scope.row.status === 1 || scope.row.status === 2"
            @click="sendOrder(scope.row)"
            v-hasPermi="['wms:maintanenceplan:edit']"
            >{{ scope.row.status === 2 ? "重新分派" : "分派" }}</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.status === 1"
            @click="handleCancel(scope.row)"
            v-hasPermi="['wms:maintanenceplan:remove']"
            >撤销</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改保养工单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        :disabled="isShow"
      >
        <!-- <el-form-item label="所属部门" prop="depId">
          <treeselect v-model="form.depId" :options="deptOptions" :normalizer="normalizer" placeholder="选择所属部门" @select="getPerson" />
        </el-form-item> -->
        <el-form-item label="申请人" prop="createBy">
          <!-- <el-select v-model="form.createBy" filterable placeholder="请选择申请人" clearable style="width: 100%;">
            <el-option v-for="item in personList" :key="item.userId" :label="item.userName" :value="item.userId"></el-option>
          </el-select> -->
          <el-input
            v-model="form.createBy"
            disabled
            placeholder="请选择设备名称"
          />
        </el-form-item>
        <el-form-item label="主设备名称" prop="equName">
          <el-input
            v-model="form.equName"
            placeholder="请选择设备名称"
            @focus="handleChooseEquipment"
          />
        </el-form-item>
        <el-form-item label="设备编号" prop="equNo">
          <el-input
            v-model="form.equNo"
            disabled
            placeholder="请输入设备编号"
          />
        </el-form-item>
        <el-form-item label="保养零配件名称">
          <el-input
            v-model="form.partName"
            placeholder="请输入保养零配件名称"
          />
        </el-form-item>
        <el-form-item label="保养内容" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入保养内容"
          />
        </el-form-item>
        <!-- <el-form-item label="保养类型" prop="equFaultLv">
          <el-radio-group v-model="form.equFaultLv">
            <el-radio
              v-for="dict in dict.type.equ_fault_lv"
              :key="dict.value"
              :label="parseInt(dict.value)"
              >{{ dict.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item> -->
        <!-- <el-form-item label="设备状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.maintenance_status" :key="dict.value" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <!-- <el-form-item label="是否停机" prop="isShutdown">
          <el-radio-group v-model="form.isShutdown">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <!-- <el-form-item label="上传图片" prop="beforeImg">
          <image-upload
            v-model="form.beforeImg"
            :imageDisabled="isShow"
          ></image-upload>
        </el-form-item> -->
        <el-form-item label="保养时间范围" prop="timeValue">
          <el-date-picker
            style="width: 100%;"
            v-model="form.planDay"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="保养类型" prop="maintenanceType">
          <el-select
            style="width: 100%;"
            v-model="form.maintenanceType"
            placeholder="请选择保养类型"
            clearable
          >
            <el-option
              v-for="dict in dict.type.maintenance_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="!isShow" @click="submitForm"
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择分派人 -->
    <choose-send-person
      ref="chooseSendPerson"
      @setSendPerson="setSendPerson"
    ></choose-send-person>
    <!-- 选择设备 -->
    <choose-equipment
      ref="chooseEquipment"
      @setEquipment="setEquipment"
    ></choose-equipment>
  </div>
</template>
<script>
import { listDept } from "@/api/system/dept";
import { listMaintainOrder, getMaintainOrder, addMaintainOrder, updateMaintainOrder, cancelMaintainOrder, sendMaintainOrder } from "@/api/equipment/maintainOrder/maintainOrder";
import { getAction } from "@/api/manage"
import { wms } from "@/utils/agent";

import store from "@/store";
import chooseSendPerson from '../components/chooseSendPerson';
import ChooseEquipment from '../components/chooseEquipment';
export default {
  components: { chooseSendPerson, ChooseEquipment },
  name: 'maintainOrder',
  dicts: ["equ_fault_lv", "maintenance_status", "maintenance_type"],
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
      // 保养工单表格数据
      maintainOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equName: null,
        equFaultLv: null,
        executorName: null,
        dayNo: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      value1: "",
      // 表单校验
      rules: {
        deptId: [{ required: true, message: '请选择所属部门', trigger: 'change' }],
        createBy: [{ required: true, message: '请选择申请人', trigger: 'change' }],
        equName: [{ required: true, message: '请选择设备', trigger: 'change' }],
        remark: [{ required: true, message: '请输入保养内容', trigger: 'blur' }],
        // status: [{ required: true,  message: '请选择设备状态', trigger: 'change' }],
        maintenanceType: [{ required: true, message: '请选择保养类型', trigger: 'change' }],
        // isShutdown: [{ required: true, message: '请选择是否停机', trigger: 'change' }],
        beforeImg: [{ required: true, message: '请上传图片', trigger: 'change' }],
      },
      deptOptions: [], //部门列表
      personList: [], //申请人列表

      isShow: false, //是否查看
      currentInfo: {}, //当前选中行

      statusColor: ['#FE1E00', '#CBAB00', '#33CCB0', '#269932']
    }
  },
  created() {
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
    // 选择时间范围
    inspectionStartTime(e) {
      console.log(e)
      this.form.inspectionStartTime = e[0]
      this.form.inspectionEndTime = e[1]
    },
    // 获取部门数据
    getListDept() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      })
    },
    /** 查询保养工单列表 */
    getList() {
      this.loading = true;
      this.$http.getAction("/wms/maintenanceDay/list", this.queryParams).then(response => {
        this.maintainOrderList = response.rows;
        this.total = response.total;
      }).finally(() => {
        this.loading = false;
      })
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
        partName: null,
        remark: null,
        equFaultLv: null,
        isShutdown: null,
        beforeImg: null,
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
      this.title = "添加保养工单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getMaintainOrder(id).then(response => {
        this.form = response.data;
        this.form.maintenanceType = this.form.maintenanceType.toString()
        this.open = true;
        this.title = "修改保养工单";
      });
    },
    /** 选择设备 */
    handleChooseEquipment() {
      this.$refs.chooseEquipment.open = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateMaintainOrder(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.$http.postAction('/wms/maintenanceDay', this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            })
          }
        }
      });
    },
    // 查看工单详情
    handleDetailed(row) {
      console.log(row.maintenanceType)

      this.reset();
      const id = row.id
      getMaintainOrder(id).then(response => {
        this.form = response.data;
        this.form.maintenanceType = this.form.maintenanceType.toString()
        this.open = true;
        this.isShow = true
        this.title = "查看保养工单";
      });
    },
    /** 撤销按钮操作 */
    handleCancel(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认撤销'+ (row.id ? '该条' : '这些') +'数据？').then(function () {
        return cancelMaintainOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("撤销成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/maintenanceDay/export', { ...this.queryParams }, `maintainOrder_${new Date().getTime()}.xlsx`)
    },
    /** 分派操作 */
    sendOrder(row) {
      this.currentInfo = { ...row }
      this.$refs.chooseSendPerson.open = true
    },
    /** 分派返回操作 */
    setSendPerson(form) {
      let resultInfo = { ...form }
      resultInfo.id = this.currentInfo.id
      sendMaintainOrder(resultInfo).then(res => {
        this.$refs.chooseSendPerson.open = false
        this.$modal.msgSuccess("分派成功");
        this.getList();
      })
    },
    /** 选择设备返回操作 */
    setEquipment(row) {
      this.form.equName = row.name ? row.name : ''
      this.form.equNo = row.equNo ? row.equNo : ''
      this.form.equipmentId = row.id ? row.id : ''
    },
  }
}
</script>