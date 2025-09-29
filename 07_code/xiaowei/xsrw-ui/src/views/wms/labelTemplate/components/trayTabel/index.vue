<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="载具编号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入载具编号" clearable/>
      </el-form-item>
      <el-form-item label="载具类型" prop="trayCategory">
        <el-select v-model="queryParams.trayCategory" clearable placeholder="请选择载具类型">
          <el-option v-for="dict in dict.type.wms_t_tray_category" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="载具状态" align="center" prop="status" >
        <el-select v-model="queryParams.status" clearable placeholder="请选择载具状态">
          <el-option v-for="dict in dict.type.wms_t_tray" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="getSerch">搜索</el-button>
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetTemplate">重置</el-button>-->
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" ref="queryParams" :data="TrayList" highlight-current-row @current-change="handleCurrentChange">
      <el-table-column label="载具编号" align="center" prop="code" :show-overflow-tooltip="true" />
      <el-table-column label="载具类型" align="center" prop="trayCategory" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_tray_category " :value="scope.row.trayCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="载具状态" align="center" prop="status" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_tray " :value="scope.row.status"/>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
  import { listBylabelTemplateId } from "@/api/wms/labelTemplate";
  export default {
    name: "TrayTabel",
    dicts: ['wms_t_tray','wms_t_tray_category'],
    props: ['labelTemplateId'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 载具管理表格数据
        TrayList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          code: '',
          trayCategory: '',
          labelTemplateId: this.labelTemplateId,
        },
        // 载具类型
        trayTypeList: [],
        // 载具状态
        statusList: [],
      };
    },
    created() {
      this.getDicts("wms_t_tray").then(response => {
        this.statusList = response.data
      });
      this.getDicts("wms_t_tray_state").then(response => {
        this.trayTypeList = response.data
      });
      this.getList();
    },
    methods: {
      statusFormat(row, column) {
        let courseType = '';
        for (var i = 0; i < this.statusList.length; i++) {
          if (this.statusList[i].dictValue == row.status) {
            courseType = this.statusList[i].dictLabel
          }
        }
        return courseType;
      },
      trayTypeFormat(row, column) {
        let courseType = '';
        for (var i = 0; i < this.trayTypeList.length; i++) {
          if (this.trayTypeList[i].dictValue == row.type) {
            courseType = this.trayTypeList[i].dictLabel
          }
        }
        return courseType;
      },
      /** 查询载具管理列表 */
      getList() {
        this.loading = true;
        listBylabelTemplateId(this.queryParams).then(response => {
          this.TrayList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      handleCurrentChange(val) {
        this.$emit('setCurrentRow', val)
      },
      getSerch(){
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 10;
        this.getList()
      },
      resetTemplate() {
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
          code: '',
          trayCategory: '',
          labelTemplateId: this.labelTemplateId,
        }
        this.getList()
      },
    }
  };
</script>
