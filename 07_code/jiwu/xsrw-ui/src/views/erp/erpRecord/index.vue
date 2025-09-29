<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="wcsId" prop="taskWcsId">
        <el-input v-model="queryParams.taskWcsId" placeholder="请输入taskWcsId" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="erpType">
        <el-select v-model="queryParams.erpType" clearable placeholder="类型">
          <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList">
      <el-table-column label="任务类型" align="center" prop="taskType" :show-overflow-tooltip="true" width="80" >
        <template slot-scope="scope">
          <span v-if="scope.row.erpType == '1'">入库</span>
          <span v-else-if="scope.row.erpType == '2'">出库</span>
          <span v-else-if="scope.row.erpType == '3'">盘点</span>
          <span v-else-if="scope.row.erpType == '5'">移库</span>
        </template>
      </el-table-column>
      <el-table-column label="任务id" align="center" prop="taskWcsId"/>
      <el-table-column label="关联批量id" align="center" prop="paramId" />
      <el-table-column label="单据编码" align="center" prop="zzdjbm" />
      <el-table-column label="单据行号" align="center" prop="zzdjhh" />
      <el-table-column label="请求参数" align="center" prop="sendData" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="返回数据" align="center" prop="acceptData" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="任务状态" align="center" prop="status" :show-overflow-tooltip="true">
		  <template slot-scope="scope">
			<dict-tag :style="{ 'color': scope.row.status == 0 ? 'red' : '#000000' }" :options="dict.type.erp_task_status" :value="scope.row.status" />
		  </template>
	  </el-table-column>
      <el-table-column label="执行时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-circle-check"
                     v-if="scope.row.status == '0'" @click="handleCancel(scope.row)">重新发送</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>
<script>
import { listErpRecord, sendErpRecord } from "@/api/erp/stockRecord";

import { wms } from "@/utils/agent";

export default {
  name: 'erpRecord',
  dicts: ["wcs_excute_status",'wcs_task_type','erp_task_status'],
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
      //表格数据
      recordList: [],
      typeList:[
        {"label":"入库","value":"1"},
        {"label":"出库","value":"2"},
        {"label":"盘点","value":"3"},
        {"label":"移库","value":"5"},
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskWcsId: null,
        erpType: null,
      },
    }
  },
  watch: {
    $route(to, form) {
      if (to.name == "moveDeliveryControl") {
        this.getList();
      }
    },
  },
  created(){
    this.getList();
  },
  methods: {
    /** 查询监控列表 */
    getList() {
      this.loading = true;
      listErpRecord({...this.queryParams,}).then((response) => {
        this.recordList = response.rows;
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
    //重新发送
    handleCancel(row){
      const id = row.id;
      let tips = '是否重新发送任务编号为"' + id + '"的数据项？';
      this.$modal.confirm(tips).then(function () {
        return sendErpRecord({ id: id });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("操作成功");
      }).catch(() => { });
    },
  }
}
</script>
<style scoped lang="sass">

</style>
