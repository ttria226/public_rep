<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="波次单号" prop="code" label-width="80px">
        <el-input v-model="queryParams.code" placeholder="请输入波次单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.nextflag" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading="addLoading" @click="handleActivatePicking" v-hasPermi="['inout:delivery:add']">激活拣货</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-printer" size="mini" :disabled="single" @click="handleAutomaticAssign" v-hasPermi="['inout:delivery:labelprint']">自动分配</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:delivery:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:delivery:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="waveAllocationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="波次单号" align="center" prop="originCode" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="数量" align="center" prop="num" :show-overflow-tooltip="true" min-width="130" />
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <!-- <el-table-column label="单据类型" align="center" prop="type" :show-overflow-tooltip="true" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_type" :value="scope.row.type" />
        </template>
      </el-table-column> -->
      <el-table-column label="申请部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="申请人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="状态" align="center" prop="nextFlag" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <!-- <dict-tag :options="dict.type.nextflag" :value="scope.row.nextflag" /> -->
          <span :style="{ color: (scope.row.nextFlag == '0' || scope.row.nextFlag == '2') ? 'red' : 'green' }" v-for="item in dict.type.nextflag" :key="item.value" v-show="scope.row.nextFlag == item.value">{{ item.label }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" v-if="scope.row.nextFlag == '1'" @click="handleDetailed(scope.row, 3)" v-hasPermi="['inout:delivery:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.nextFlag == '0'" @click="handleDelete(scope.row)" v-hasPermi="['inout:delivery:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleArriveTask(scope.row,'2')" v-hasPermi="['inout:delivery:check']">分配</el-button>
          <el-button size="mini" type="text" icon="el-icon-box" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleFloorDiaplayTask(scope.row,'2')" v-hasPermi="['inout:delivery:check']">地堆拣货</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleArriveTask(scope.row,'4')" v-hasPermi="['inout:delivery:check']">自动分配</el-button>
          <el-button size="mini" type="text" icon="el-icon-box" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleFloorDiaplayTask(scope.row,'4')" v-hasPermi="['inout:delivery:check']">地堆自动拣货</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    
    <!-- 执行出库 -->
    <ArriveTaskCom ref="arriveTaskCom" @setArriveTask="setArriveTask"></ArriveTaskCom>
    <!-- 地堆拣货 -->
    <floor-display-task ref="floorDisplayTaskCom" @setArriveTask="setArriveTask"></floor-display-task>
    <DetailCom ref="detailCom"></DetailCom>
  </div>
</template>

<script>
import { listMergeDeliveryTask, enforcementMergeOutDelivery, delOutDeliveryTask, } from "@/api/inoutDelivery/outDelivery";

import { wms } from "@/utils/agent";

import ArriveTaskCom from "../outDeliveryTask/components/arriveTask";
import DetailCom from "./components/detail";
import FloorDisplayTask from '../outDeliveryTask/components/floorDisplayTask';

export default {
  name: "waveAllocation",
  dicts: ["inout_out_type", "nextflag"],
  components: { DetailCom, ArriveTaskCom, FloorDisplayTask },
  directives: {
    print,
  },
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
      // 波次分配表格数据
      waveAllocationList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        type: null,
        status: null,
      },

      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情

      detailIndex: 0, //操作详情角标
      addLoading: false, // 新增按钮loading

      dialogLoading: false, //弹窗loading

      isAuth: false, //是否生产日期必填
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "waveAllocation") {
        this.getList();
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询波次分配列表 */
    getList() {
      this.loading = true;
      listMergeDeliveryTask(this.queryParams).then((response) => {
        this.waveAllocationList = response.rows;
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 激活拣货按钮操作 */
    handleActivatePicking() {
      if(!this.ids || this.ids && this.ids.length == 0){
        this.$message.warning(`请至少选择一条记录`)
        return
      }
      const ids = this.ids;
      let tips = '该操作将激活拣货任务,确定要激活吗？';
      this.$modal.confirm(tips).then(function () {
        return enforcementMergeOutDelivery({ id: ids });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("激活拣货成功");
      }).catch(() => { });
    },
    /** 自动分配按钮操作 */
    handleAutomaticAssign() {
      if(!this.ids || this.ids && this.ids.length == 0){
        this.$message.warning(`请至少选择一条记录`)
        return
      }
      const ids = this.ids;
      let tips = '该操作将为出库单自动分配拣货,确定要分配吗？';
      this.$modal.confirm(tips).then(function () {
        return checkDelivery({ id: ids });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("自动分配成功");
      }).catch(() => { });
    },
    /** 执行出库按钮操作 */
    handleArriveTask(row,type) {
      this.$refs.arriveTaskCom.open = true
      this.$refs.arriveTaskCom.type = type
      this.$refs.arriveTaskCom.materialList = [{...row}]
    },
    /** 地堆拣货按钮操作 */
    handleFloorDiaplayTask(row,type){
      this.$refs.floorDisplayTaskCom.open = true
      this.$refs.floorDisplayTaskCom.type = type
      this.$refs.floorDisplayTaskCom.materialList = [{...row}]
    },
    // 打开配置明细
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;
      let title = "波次分配详情";

      this.$refs.detailCom.detailTitle = title;
      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let tips = "";
      if (row.id) {
        tips = '是否确认删除波次单号为"' + row.code + '"的数据项？';
      } else {
        let codes = [];
        this.deliveryDetailList.forEach((item) => {
          if (this.ids.indexOf(item.id) > -1) {
            codes.push(item.code);
          }
        });
        tips = '是否确认删除波次单号为"' + codes.toString() + '"的数据项？';
      }
      this.$modal.confirm(tips).then(function () {
        return delOutDeliveryTask(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/deliveryOut/outTasklist/export", {...this.queryParams,}, `WaveAllocationExport_${new Date().getTime()}.xlsx`);
    },
    //上架成功后的操作
    setArriveTask(flag) {
      if(flag){
        this.getList()
      }
    },
  },
};
</script>
