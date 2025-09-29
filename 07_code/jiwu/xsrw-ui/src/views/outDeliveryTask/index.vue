<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="出库单号" prop="originCode">
        <el-input v-model="queryParams.originCode" placeholder="请输入出库单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="出库单类型" prop="type" label-width="90px">
        <el-select v-model="queryParams.type" clearable placeholder="请选择出库单类型">
          <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="出库单状态" prop="status" label-width="90px">
        <el-select v-model="queryParams.status" clearable placeholder="请选择出库单状态">
          <el-option v-for="dict in dict.type.nextflag" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
			<el-form-item label="物料编码查询" prop="materialCode"  label-width="120px">
			  <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['inout:detail:remove']">删除</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['out:outTasklist:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="outDeliveryTaskList" @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" align="center" /> -->
      <el-table-column label="出库单号" align="center" prop="originCode" :show-overflow-tooltip="true" min-width="160" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="批次" align="center" prop="batchCode" :show-overflow-tooltip="true" width="100" />
      <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" width="80" />
      <el-table-column label="需出库数量" align="center" prop="num" :show-overflow-tooltip="true" width="100" />
<!--      <el-table-column label="小件数量" align="center" prop="smallPredictCount" :show-overflow-tooltip="true" width="150" />-->
      <el-table-column label="出库类型" align="center" prop="type" :show-overflow-tooltip="true" width="80" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.inout_out_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="申请部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="申请人" align="center" prop="createBy" :show-overflow-tooltip="true" width="100" />
      <el-table-column label="状态" align="center" prop="nextFlag" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.nextflag" :value="scope.row.nextFlag" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
	  <el-table-column label="备注" align="center" prop="remark" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" v-if="scope.row.nextFlag == '1'"  @click="handleDetailed(scope.row)">查看</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-delete"  v-if="scope.row.nextFlag == '0'" @click="handleDelete(scope.row)" v-hasPermi="['inout:detail:remove']">删除</el-button> -->
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleArriveTask(scope.row,1)">执行出库</el-button>
          <el-button size="mini" type="text" icon="el-icon-box" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleFloorDiaplayTask(scope.row,1)">地堆拣货</el-button>
          <el-button size="mini" type="text" icon="el-icon-setting" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleArriveTask(scope.row,3)">执行自动出库</el-button>
         <!--  <el-button size="mini" type="text" icon="el-icon-box" v-if="scope.row.nextFlag == '0' || scope.row.nextFlag == '2'" @click="handleFloorDiaplayTask(scope.row,3)">地堆自动拣货</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 执行出库 -->
    <ArriveTaskCom ref="arriveTaskCom" @setArriveTask="setArriveTask"></ArriveTaskCom>
    <!-- 地堆拣货 -->
    <floor-display-task ref="floorDisplayTaskCom" @setArriveTask="setArriveTask"></floor-display-task>
    <DetailCom ref="detailCom" ></DetailCom>
  </div>
</template>

<script>
import { listOutDeliveryTask, getAdvanceDelivery, delOutDeliveryTask, } from "@/api/inoutDelivery/outDelivery";
import { getToken } from "@/utils/auth";
import { wms } from "@/utils/agent";

import ArriveTaskCom from "./components/arriveTask";
import DetailCom from "./components/detail";
import FloorDisplayTask from './components/floorDisplayTask';

export default {
  name: "outDeliveryTask",
  dicts: ["wms_asn_origin_type", "inout_out_type", "nextflag"],
  components: { ArriveTaskCom, DetailCom, FloorDisplayTask },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //选中数组数据
      currentSelection: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 出库单表格数据
      outDeliveryTaskList: [],
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        originCode: null,
        type: null,
        status: null,
		materialCode:null,
      },
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "outDeliveryTask") {
        this.getList();
      }
    },
  },
  created() {
    this.getList();
  },
  methods: {
      getToken,
      // 已收货禁止选择
      checkSelectable(row, index) {
          return row.nextFlag == "0";
      },
      /** 查询预先发货清单列表 */
      getList() {
        this.loading = true;
        listOutDeliveryTask(this.queryParams).then((response) => {
          this.outDeliveryTaskList = response.rows;
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
        this.currentSelection = JSON.parse(JSON.stringify(selection))
        this.ids = selection.map((item) => item.id);
        this.single = selection.length !== 1;
        this.multiple = !selection.length;
      },
      /** 执行出库按钮操作 */
      handleArriveTask(row,type) {
        this.$refs.arriveTaskCom.open = true;
        this.$refs.arriveTaskCom.type = type;
        this.$refs.arriveTaskCom.materialList = [{...row}]
      },
      /** 地堆拣货按钮操作 */
      handleFloorDiaplayTask(row,type){
        this.$refs.floorDisplayTaskCom.open = true
        this.$refs.floorDisplayTaskCom.type = type
        this.$refs.floorDisplayTaskCom.materialList = [{...row}]
      },
      // 打开配置明细
      handleDetailed(row) {
        this.$refs.detailCom.id = row.id;
        this.$refs.detailCom.open = true;
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        let tips = "";
        if (row.id) {
          tips = '是否确认删除出库单号为"' + row.originCode + '"的数据项？';
        } else {
          let codes = [];
          this.outDeliveryTaskList.forEach((item) => {
            if (this.ids.indexOf(item.id) > -1) {
                codes.push(item.originCode);
            }
          });
          tips = '是否确认删除出库单号为"' + codes.toString() + '"的数据项？';
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
        this.download(wms + "/deliveryOut/outTasklist/export", {...this.queryParams,}, `outTasklist_${new Date().getTime()}.xlsx`);
      },
      //上架成功后的操作
      setArriveTask(flag) {
        if(flag){
          this.getList()
        }
      },
      //详情提交
      setDetail(data){

      },
      /** 导入超出限制提示 */
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
  },
};
</script>
