<template>
  <el-dialog title="选择载具" :visible.sync="open" v-if="open" width="1200px" append-to-body>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="载具编号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入载具编号" clearable />
      </el-form-item>
      <el-form-item label="载具类型" prop="trayCategory">
        <el-select v-model="queryParams.trayCategory" clearable placeholder="请选择载具类型">
          <el-option v-for="dict in dict.type.wms_t_tray_category" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="TrayList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="载具编号" align="center" prop="code" :show-overflow-tooltip="true" width="150" />
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
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" :disabled="multiple" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listTray } from "@/api/wms/Tray";
import { updateLabelByIds } from "@/api/wms/labelTemplate"
import { treeselect } from "@/api/system/dept";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
// import { listWarehouse } from "@/api/wms/warehouse";
export default {
  name: "TrayTabelPop",
  components:{Treeselect},
  dicts: ['wms_t_tray','wms_t_tray_category'],
  props: ['labelTemplateId'],
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
      // 载具管理表格数据
      TrayList: [],
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        factory:null,
      },
      // 是否满载具
      flagList:[],
      // 载具类型
      trayTypeList:[],
      // 载具状态
      statusList:[],
    };
  },
  watch: {
    open(){
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        factory:null,
        code:'',
        trayCategory:null,
        warehouseId:null
      };
      this.getList();
    }
  },
  created() {

    this.getList();
    // this.listWarehouse()
  },
  methods: {
    defaultParamsChange(value){
      if(value.length >0){
        this.queryParams.factory = value[value.length-1]
        this.$refs.refHandle.dropDownVisible = false;
      }
    },
    formidChange(value){
      if(value.length >0){
        this.form.factory = value[value.length-1]
        this.$refs.refForm.dropDownVisible = false;
      }
    },
    listWarehouse(){
      // listWarehouse({pageNum: 1,pageSize: 1000,}).then(res=>{
      //   this.warehouseIdList = res.rows
      // })
    },
    /** 查询载具管理列表 */
    getList() {
      this.loading = true;
      listTray(this.queryParams).then(response => {
        this.TrayList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.factory = null
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 提交按钮 */
    submitForm() {
      let data = new FormData()
      data.append('ids', this.ids)
      data.append('labelTemplateId', this.labelTemplateId)
      updateLabelByIds(data).then(res => {
        this.resetQuery()
        this.$emit('trayTabelRefresh')
        this.$modal.msgSuccess("修改成功");
        this.open = false
      })
    },
  }
};
</script>
