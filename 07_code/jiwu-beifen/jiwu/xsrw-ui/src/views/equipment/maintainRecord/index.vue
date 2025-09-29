<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="equName">
        <el-input v-model="queryParams.equName" placeholder="请输入设备名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="维修类型" prop="equFaultLv">
        <el-select v-model="queryParams.equFaultLv" placeholder="请选择维修类型" clearable>
          <el-option v-for="dict in dict.type.equ_fault_lv" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="执行人" prop="executorName">
        <el-input v-model="queryParams.executorName" placeholder="请输入执行人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="保养时间" prop="planDay">
        <el-date-picker v-model="queryParams.planDay" type="date" placeholder="请选择保养时间" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:repairReport:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="maintainRecordList">
      <el-table-column label="序号" type="index" align="center" prop="id" />
      <el-table-column label="维修单号" align="center" prop="dayNo" min-width="200">
        <template slot-scope="scope">
          <el-button type="text" @click="handleDetail(scope.row)">{{ scope.row.dayNo }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="设备编号" align="center" prop="equNo" min-width="200"/>
      <el-table-column label="设备名称" align="center" prop="equName" min-width="200" />
      <el-table-column label="来源" align="center" prop="source">
        <template slot-scope="scope">
          <span v-if="scope.row.source == 1">计划生成</span>
          <span v-if="scope.row.source == 2">新建工单</span>
        </template>
      </el-table-column>
      <el-table-column label="维修类型" align="center" prop="equFaultLv">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.equ_fault_lv" :value="scope.row.equFaultLv"/>
        </template>
      </el-table-column>
      <el-table-column label="维修费用" align="center" prop="price" width="120" />
      <el-table-column label="执行人" align="center" prop="createBy" width="120" />
      <el-table-column label="计划维修日期" align="center" prop="planDay" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际维修日期" align="center" prop="trueDay" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.trueDay, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope>
          <span>已完成</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)" v-hasPermi="['wms:maintenanceDay:query']">维修详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 查看维修记录信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" label-width="120px" :disabled="isShow">
        <el-form-item label="设备名称">
          <el-input v-model="form.equName" placeholder="请选择设备名称" />
        </el-form-item>
        <el-form-item label="设备编号">
          <el-input v-model="form.equNo" placeholder="请输入设备编号" />
        </el-form-item>
        <el-form-item label="维修零配件名称">
          <el-input v-model="form.partName" placeholder="请输入故障零配件名称" />
        </el-form-item>
        <el-form-item label="故障描述">
          <el-input v-model="form.content" type="textarea" placeholder="请输入故障描述" />
        </el-form-item>
        <el-form-item label="计划维修时间">
          <el-input v-model="form.planDay" placeholder="请输入计划维修时间" />
        </el-form-item>
        <el-form-item label="实际维修时间">
          <el-input v-model="form.trueDay" placeholder="请输入实际维修时间" />
        </el-form-item>
        <el-form-item label="维修类型">
          <el-radio-group v-model="form.faultLv">
            <el-radio v-for="dict in dict.type.equ_fault_lv" :key="dict.value" :label="parseInt(dict.value)">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="维修前图片" prop="beforeImg">
					<image-upload :imageUploadRef="'imageUploadRef1'" v-model="form.beforeImg" :imageDisabled="isShow"></image-upload>
				</el-form-item>
        <el-form-item label="维修后图片" prop="afterImg">
					<image-upload :imageUploadRef="'imageUploadRef2'" v-model="form.beforeImg" :imageDisabled="isShow"></image-upload>
				</el-form-item>
        <el-form-item label="维修费用">
          <el-input v-model="form.price" placeholder="请输入维修费用" >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="消耗物料">
          <el-input v-model="form.material" placeholder="请输入消耗物料" />
        </el-form-item>
        <el-form-item label="其他">
          <el-input v-model="form.remark" placeholder="请输入其他" />
        </el-form-item>
        <el-form-item label="是否外协">
          <el-radio-group v-model="form.isExternal">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="外协单位">
          <el-input v-model="form.externalCompany" placeholder="请输入外协单位" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listMaintainRecord } from "@/api/equipment/faultRepair/faultRepair";
import { getMaintainOrder } from "@/api/equipment/maintainOrder/maintainOrder";

import { wms } from "@/utils/agent";

export default {
  name: 'maintainRecord',
  dicts: ["equ_fault_lv","maintenance_status"],
  data(){
    return{
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 维修记录表格数据
      maintainRecordList: [],
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
        planDay: null,
      },
      // 表单参数
      form: {},

      isShow: false, //是否查看
    }
  },
  created(){
    this.getList()
  },
  methods: {
    /** 查询维修记录列表 */
    getList() {
      this.loading = true;
      listMaintainRecord(this.queryParams).then(response => {
        this.maintainRecordList = response.rows;
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
        content: null,
        planDay: null,
        trueDay: null,
        faultLv: null,
        beforeImg: null,
        afterImg: null,
        price: null,
        material: null,
        remark: null,
        isExternal: null,
        externalCompany: null,
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
    // 查看维修记录详情
    handleDetail(row) {
      this.reset();
      const id = row.id
      getMaintainOrder(id).then(response => {
        this.form = response.data;
        this.form.planDay = this.parseTime(response.data.planDay, '{y}-{m}-{d}')
        this.form.trueDay = this.parseTime(response.data.trueDay, '{y}-{m}-{d}')
        this.open = true;
        this.isShow = true
        this.title = "查看维修记录详情";
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/repairReport/export', {...this.queryParams}, `EquipmentRecord_${new Date().getTime()}.xlsx`)
    },
  }
}
</script>
