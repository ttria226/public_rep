<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务编号" prop="taskNo">
        <el-input v-model="queryParams.taskNo" placeholder="请输入任务编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="载具编号" prop="trayCode">
        <el-input v-model="queryParams.trayCode" placeholder="请输入载具编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.wcs_excute_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:task:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outDeliveryControlList">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="任务编号" align="center" prop="taskNo" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="载具编号" align="center" prop="trayCode" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="目标库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="状态" align="center" prop="taskStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
      <el-table-column label="故障原因" align="center" prop="errorMessage" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="执行人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="执行时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleExecute(scope.row,4)">查看</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.taskStatus == '1'" @click="handleExecute(scope.row,1)" v-hasPermi="['inout:task:remove']">执行</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.taskStatus == '4'" @click="handleExecute(scope.row,2)" v-hasPermi="['inout:task:checkDetail']">重新执行</el-button> -->
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.taskStatus != '3' && scope.row.taskStatus != null" @click="handleExecute(scope.row,3)" v-hasPermi="['inout:task:executeOutTask']">强制完成</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 详情/执行/重新执行/强制执行 -->
    <DetailCom ref="detailCom" @setControl="setControl"></DetailCom>
  </div>
</template>
<script>
import { listDeliveryExecute } from "@/api/inoutDelivery/inDelivery";

import { wms } from "@/utils/agent";

import DetailCom from "./components/detail";

export default {
  name: 'waveControl',
  dicts: ["wcs_excute_status"],
  components: { DetailCom },
  data(){
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
      // 出库监控表格数据
      outDeliveryControlList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskType: 7,
        taskNo: null,
        trayCode: null,
        taskStatus: null,
      },

      detailedId: null,
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "outDeliveryControl") {
        this.getList();
      }
    },
  },
  created(){
    this.getList();
  },
  methods: {
    // 已收货禁止选择
    checkSelectable(row, index) {
      return row.isTakeType != "1";
    },
    /** 查询监控列表 */
    getList() {
      this.loading = true;
      listDeliveryExecute({...this.queryParams}).then((response) => {
        this.outDeliveryControlList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/task/export", {...this.queryParams,}, `task_${new Date().getTime()}.xlsx`);
    },
    //执行方法
    handleExecute(row,type){
      // if(type != 3){
        this.$refs.detailCom.open = true;
        this.$refs.detailCom.detailType = type;
        this.detailedId = row.id;
        this.$refs.detailCom.detailId = row.id;
      // } else {
      //   this.$modal.confirm("确定要强制执行该任务？").then(() => {
      //     return enforcementOutDelivery({id: row.id})
      //   }).then(() => {
      //     this.$modal.msgSuccess("强制执行任务成功");
      //     this.getList();
      //   }).catch(() => { });
      // }
    },
    //执行成功后的回调
    setControl(){
      this.getList();
    }
  }
}
</script>
