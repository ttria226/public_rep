<template>
  <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
    <el-form ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="100px" style="padding-right: 30px">
			<el-form-item :label="(type === '4' || type === '5' || type === '6' || type === '7') ? '出库单号' : '入库单号'" prop="code">
				<el-input v-model="queryParams.code" :placeholder="(type === '4' || type === '5' || type === '6' || type === '7') ? '请输入出库单号' : '请输入入库单号'" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="orderList" highlight-current-row @current-change="handleCurrentChange">
			<el-table-column :label="(type === '4' || type === '5' || type === '6' || type === '7') ? '出库单号' : '入库单号'" align="center" prop="code" min-width="130" />
			<el-table-column label="入库类型" align="center" prop="type" v-if="type !== '3'" min-width="100">
        <template slot-scope="scope">
					<dict-tag :options="dict.type.in_delivery_type" :value="scope.row.type" />
				</template>
			</el-table-column>
			<el-table-column label="物料使用部门" align="center" prop="deptName" min-width="100" />
			<el-table-column label="制单人" align="center" prop="createBy" min-width="100" />
      <el-table-column label="来源" align="center" prop="newLocal" min-width="100">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.in_delivery_origin" :value="scope.row.newLocal" />
				</template>
			</el-table-column>
      <!-- <el-table-column label="状态" align="center" prop="status" min-width="100">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.in_delivery_status" :value="scope.row.status" />
				</template>
			</el-table-column> -->
      <el-table-column label="审核人" align="center" prop="auditor" min-width="100" />
      <el-table-column label="创建日期" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150" />
    </el-table>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { listEquipment } from "@/api/equipment/equipment";
import { selectListInDelivery } from "@/api/inoutDelivery/inDelivery";
import { getOutDeliverySelectList, getOutDeliveryCheckSelectList, getOutDeliverySendSelectList } from "@/api/inoutDelivery/outDelivery";

export default {
  name: "ChooseOrderCom",
  dicts: ["in_delivery_type","in_delivery_origin","in_delivery_status"],
  data(){
    return{
      // 遮罩层
      loading: true,
      //选中数组内容
      currentRows: null,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 单据表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 弹框类型
      type: '',
      // 查询参数
      queryParams: {
        // pageNum: 1,
        // pageSize: 10,
        code: null,
      },
    }
  },
  watch: {
    open(val){
      if(val){
        this.getList();
      }
    }
  },
  methods: {
    /** 查询单据列表 */
    getList() {
      this.loading = true;
      switch(this.type){
        case '1':
        case '2':
        case '3':
          let params = {...this.queryParams}
          if(this.type == '1'){
            params.status = '3'
          }
          if(this.type == '2'){
            params.status = '11'
          }
          if(this.type == '3'){
            params.status = '4'
          }
          selectListInDelivery(params).then(response => {
            this.orderList = response.data;
          }).finally(() => {
            this.loading = false;
          });
          break;
        case '4':
          listEquipment(this.queryParams).then(response => {
            this.orderList = response.data;
            // this.total = response.total;
          }).finally(() => {
            this.loading = false;
          });
          break;
        case '5':
          getOutDeliverySelectList({...this.queryParams, completeState: 3}).then(response => {
            this.orderList = response.data;
            // this.total = response.total;
          }).finally(() => {
            this.loading = false;
          });
          break;
        case '6':
          getOutDeliveryCheckSelectList(this.queryParams).then(response => {
            this.orderList = response.data;
            // this.total = response.total;
          }).finally(() => {
            this.loading = false;
          });
          break;
        case '7':
          getOutDeliverySendSelectList(this.queryParams).then(response => {
            this.orderList = response.data;
            // this.total = response.total;
          }).finally(() => {
            this.loading = false;
          });
          break;
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        code: null,
      }
      this.resetForm("queryForm");
      this.orderList = []
      this.total = 0
      this.currentRows = null
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
    // 选中数据
    handleCurrentChange(selection) {
      this.currentRows = selection
    },
    //选择提交
    submitForm(){
      if(!this.currentRows){
        this.$message.warning("请选择要新增的单据！")
        return false
      }
      if (this.currentRows) {
        this.$emit('setOrder', this.currentRows)
      }
      this.open = false
    }
  }
}
</script>