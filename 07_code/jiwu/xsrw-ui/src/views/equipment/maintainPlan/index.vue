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
      <el-form-item label="设备编号" prop="equNo">
        <el-input
          v-model="queryParams.equNo"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择计划状态"
          clearable
        >
          <el-option
            v-for="dict in dict.type.maintenance_plan_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:maintanenceplan:repairexport']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="maintainPlanList">
      <el-table-column label="序号" type="index" align="center" prop="id" />
      <el-table-column
        label="设备名称"
        align="center"
        prop="equName"
        min-width="200"
      />
      <el-table-column
        label="设备编号"
        align="center"
        prop="equNo"
        min-width="200"
      />
      <el-table-column
        label="计划名称"
        align="center"
        prop="planName"
        min-width="200"
      />
      <el-table-column
        label="计划开始时间"
        align="center"
        prop="planStartTime"
        width="120"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planStartTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划结束时间"
        align="center"
        prop="planEndTime"
        width="120"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planEndTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划说明"
        align="center"
        prop="content"
        min-width="300"
      />
      <el-table-column
        label="计划状态"
        align="center"
        prop="status"
        width="100"
      >
        <template slot-scope="scope">
          <div
            class="status-block"
            v-for="item in dict.type.maintenance_plan_status"
            :key="item.value"
            :style="{ 'background-color': statusColor[scope.row.status] }"
            v-show="item.value == scope.row.status"
          >
            {{ item.label }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="250">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.status === 1"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:maintanenceplan:edit']"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetailed(scope.row)"
            v-hasPermi="['wms:maintanenceplan:query']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-position"
            v-if="scope.row.status === 1"
            @click="handleStatus(scope.row, '2')"
            v-hasPermi="['wms:maintanenceplan:edit']"
            >启用</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.status === 1 || scope.row.status === 2"
            @click="handleStatus(scope.row, '0')"
            v-hasPermi="['wms:maintanenceplan:planCancel']"
            >作废</el-button
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

    <!-- 添加或修改维修计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        :disabled="isShow"
      >
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
        <el-form-item label="故障零配件名称">
          <el-input
            v-model="form.partName"
            placeholder="请输入故障零配件名称"
          />
        </el-form-item>
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="计划说明" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            placeholder="请输入计划说明"
          />
        </el-form-item>
        <el-row :gutter="10" class="mb8" v-if="!isShow">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="addDetailed()"
              >新增行</el-button
            >
          </el-col>
        </el-row>
        <el-form-item label-width="0px" prop="planVOList">
          <el-table v-loading="loading" :data="form.planVOList">
            <el-table-column
              label="计划维修日期"
              align="center"
              prop="planDay"
              min-width="180"
            >
              <template slot-scope="scope">
                <el-form-item
                  label-width="0px"
                  :prop="'planVOList.' + scope.$index + '.planDay'"
                  :rules="detailRules.planDay"
                >
                  <el-date-picker
                    v-model="scope.row.planDay"
                    type="date"
                    placeholder="请选择计划维修日期"
                    value-format="yyyy-MM-dd"
                  ></el-date-picker>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="维修类型"
              align="center"
              prop="repairType"
              min-width="180"
            >
              <template slot-scope="scope">
                <el-form-item
                  label-width="0px"
                  :prop="'planVOList.' + scope.$index + '.repairType'"
                >
                  <el-select
                    v-model="scope.row.repairType"
                    placeholder="请选择维修类型"
                    clearable
                  >
                    <el-option
                      v-for="dict in dict.type.repair_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="故障描述"
              align="center"
              prop="content"
              min-width="180"
            >
              <template slot-scope="scope">
                <el-form-item label-width="0px">
                  <el-input
                    v-model="scope.row.content"
                    type="textarea"
                    placeholder="请输入故障描述"
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              fixed="right"
              class-name="small-padding fixed-width"
              v-if="!isShow"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="deleteDetailed(scope.row, scope.$index)"
                  v-hasPermi="['']"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="!isShow" @click="submitForm"
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择设备 -->
    <choose-equipment
      ref="chooseEquipment"
      @setEquipment="setEquipment"
    ></choose-equipment>
  </div>
</template>
<script>
import { listMaintainPlan, getMaintainPlan, addMaintainPlan, updateMaintainPlan, startMaintainPlan, cancelMaintainPlan } from "@/api/equipment/maintainPlan/maintainPlan";

import { wms } from "@/utils/agent";

import ChooseEquipment from '../components/chooseEquipment';
export default {
  components: { ChooseEquipment },
  name: 'maintainPlan',
  dicts: ["maintenance_plan_status", "repair_type"],
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
      // 维修计划表格数据
      maintainPlanList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equName: null,
        equNo: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        equName: [{ required: true, message: '请选择设备', trigger: 'change' }],
        planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
        content: [{ required: true, message: '请输入计划说明', trigger: 'blur' }],
      },
      detailRules: {
        planDay: [
          { required: true, message: "请选择计划维修日期", trigger: "change" },
        ],
        repairType: [
          { required: true, message: "请选择维修类型", trigger: "change" },
        ],
        content: [
          { required: true, message: "请输入故障描述", trigger: "blur" },
        ],
      },

      isShow: false, //是否查看
      currentInfo: {}, //当前选中行

      statusColor: ['#CC8566', '#FF3B30', '#33CCB0']
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询维修计划列表 */
    getList() {
      this.loading = true;
      listMaintainPlan(this.queryParams).then(response => {
        this.maintainPlanList = response.rows;
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
        equipmentId: null,
        equNo: null,
        equName: null,
        partName: null,
        planName: null,
        content: null,
        planVOList: []
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
      this.title = "添加维修计划";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getMaintainPlan(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改维修计划";
      });
    },
    /** 选择设备 */
    handleChooseEquipment() {
      this.$refs.chooseEquipment.open = true
    },
    /** 新增明细操作 */
    addDetailed() {
      this.form.planVOList.push({
        planDay: '',
        repairType: '',
        content: '',
      });
    },
    /** 删除明细操作 */
    deleteDetailed(row, index) {
      this.$modal.confirm("是否确认删除此明细").then(() => {
        this.form.planVOList.splice(index, 1);
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateMaintainPlan(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMaintainPlan(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    // 查看计划详情
    handleDetailed(row) {
      this.reset();
      const id = row.id
      getMaintainPlan(id).then(response => {
        response.data.planVOList.map((item) => {
          item.repairType = item.repairType+""
          return item
        })
        this.form = response.data;
        this.open = true;
        this.isShow = true
        this.title = "查看维修计划";
      });
    },
    /** 作废/启用按钮操作 */
    handleStatus(row, status) {
      const ids = row.id || this.ids;
      let msg = ""
      if (status === '0') {
        msg = "温馨提示：作废操作后不可撤回，是否确定作废该维修计划？"
      } else {
        msg = '是否确认启用该条数据？'
      }
      this.$modal.confirm(msg).then(function () {
        if (status === '0') {
          return cancelMaintainPlan(ids)
        } else {
          return startMaintainPlan(ids);
        }
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(status === '0' ? "作废成功" : '启用成功');
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/maintanenceplan/repairexport', { ...this.queryParams }, `maintainPlan_${new Date().getTime()}.xlsx`)
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
