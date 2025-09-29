<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="收货单号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入收货单号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="单据类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择单据类型">
          <el-option v-for="dict in dict.type.in_delivery_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.type" clearable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.wms_delivery_collection_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['inout:collection:add']">新增收货单</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['inout:collection:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="takeDeliveryReturnList">
      <el-table-column label="收货单号" align="center" prop="code" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="单据类型" align="center" prop="type" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="仓库" align="center" prop="currentWarehouseId" :formatter="convertWarehouse" :show-overflow-tooltip="true" width="150" /> -->
      <el-table-column label="物料使用部门" align="center" prop="deptName" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="制单人" align="center" prop="maker" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="来源" align="resource" prop="newLocal" :show-overflow-tooltip="true" min-width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.in_delivery_origin" :value="scope.row.newLocal" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_delivery_collection_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetailed(scope.row, 2)" v-hasPermi="['inout:collection:query']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '0' || scope.row.status == '2'" @click="handleDetailed(scope.row,1)" v-hasPermi="['inout:collection:remove']">退货</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.status == '0'" @click="handleDelete(scope.row)" v-hasPermi="['inout:collection:remove']">作废</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <choose-order ref="chooseOrderCom" @setOrder="setOrder"></choose-order>
    <DetailCom ref="detailCom" @setDetail="setDetail"></DetailCom>
  </div>
</template>

<script>
import { listCollection, addCollection, updateCollectionStatus, updateCollectionReturnStatus } from "@/api/inoutDelivery/inDelivery";

import { wms } from "@/utils/agent";

import ChooseOrder from '../components/inout/chooseOrder';
import DetailCom from "./components/detail";

export default {
  name: "takeDeliveryReturn",
  dicts: ["in_delivery_type", "wms_delivery_collection_status", "in_delivery_origin"],
  components: { DetailCom, ChooseOrder },
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
      // 收货退货表格数据
      takeDeliveryReturnList: [],
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
        status: null
      },

      detailId: '', //详情id
      detailType: 0, // 配置明细类型，0配置明细 1详情
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "takeDeliveryReturn") {
        this.getList();
      }
    },
  },
  created() {
    this.getList();
  },
  methods: {
    // 已收货禁止选择
    checkSelectable(row, index) {
      return row.status == '1' || row.status == '9';
    },
    /** 查询预先发货清单列表 */
    getList() {
      this.loading = true;
      listCollection(this.queryParams).then((response) => {
        this.takeDeliveryReturnList = response.rows;
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
    /** 新增按钮操作 */
    handleAdd() {
      this.$refs.chooseOrderCom.title = '新建收货单';
      this.$refs.chooseOrderCom.type = '3';
      this.$refs.chooseOrderCom.form = {};
      this.$refs.chooseOrderCom.open = true;
    },
    // 查看/退货按钮操作
    handleDetailed(row, type) {
      this.$refs.detailCom.detailId = row.id;
      this.detailId = row.id;
      this.detailType = type
      this.$refs.detailCom.detailType = type;
      let title = '收货单详情';

      this.$refs.detailCom.detailTitle = title;
      this.$refs.detailCom.detailForm = {};
      this.$refs.detailCom.open = true;
    },
    /** 作废按钮操作 */
    handleDelete(row) {
      const ids = row.id;
      let tips = '该操作将作废该订单所有数据,确定要作废吗？';
      this.$modal.confirm(tips).then(function () {
        return updateCollectionStatus({ id: ids, status: '2' });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("作废成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + "/inout/delivery/export", {...this.queryParams,}, `inDelivery_${new Date().getTime()}.xlsx`);
    },
    //选择要生成收货单的数据
    setOrder(order){
      let params = {
        originId: order.id
      }
      addCollection(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("新建收货单成功");
          this.$refs.chooseOrderCom.title = '';
          this.$refs.chooseOrderCom.type = '';
          this.$refs.chooseOrderCom.form = {};
          this.$refs.chooseOrderCom.open = false;
          this.getList();
        }
      });
    },
    //详情提交
    setDetail(data){
      let detailList = []
      data.deliveryDetailList.map((item) => {
        let info = {
          id: item.id,
          returnCount: item.returnCount
        }
        detailList.push(info)
      })
      let params = {
        id: data.id,
        remark: data.remark,
        detailList
      }
      updateCollectionReturnStatus(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess("退货成功");
          this.$refs.detailCom.open = false;
          this.$refs.detailForm = {}
          this.getList();
        }
      });
    },
  },
};
</script>
