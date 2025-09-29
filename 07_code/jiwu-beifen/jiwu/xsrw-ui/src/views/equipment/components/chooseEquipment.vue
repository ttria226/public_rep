<template>
  <el-dialog title="选择设备" :visible.sync="open" width="60%" append-to-body>
    <el-form ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="100px" style="padding-right: 30px">
      <el-form-item label="设备名称" prop="name">
				<el-input v-model="queryParams.name" placeholder="请输入设备名称" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="设备编号" prop="equNo">
				<el-input v-model="queryParams.equNo" placeholder="请输入设备编号" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
      <el-form-item label="所属部门" prop="depId">
				<treeselect v-model="queryParams.depId" :options="deptOptions" :normalizer="normalizer" placeholder="选择所属部门" style="width: 215px;" />
			</el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="equipmentList" highlight-current-row @current-change="handleCurrentChange">
      <el-table-column label="序号" type="index" align="center" />
			<el-table-column label="设备编号" align="center" prop="equNo" />
			<el-table-column label="设备名称" align="center" prop="name" />
			<el-table-column label="资产编号" align="center" prop="assetNo" />
			<el-table-column label="规格型号" align="center" prop="model" />
			<el-table-column label="功能位置" align="center" prop="functionLocation" />
      <el-table-column label="设备状态" align="center" prop="useStatus">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.equipment_type" :value="scope.row.useStatus" />
				</template>
			</el-table-column>
      <el-table-column label="所属部门" align="center" prop="depName" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { listDept } from "@/api/system/dept";
import { listEquipment } from "@/api/equipment/equipment";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "ChooseEquipment",
  dicts: ["equipment_type"],
  components: { Treeselect },
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
      // 设备台账表格数据
      equipmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        equNo: null,
        depId: null,
        depName: null,
      },

      deptOptions: [], //部门列表
    }
  },
  watch: {
    open(val){
      if(val){
        this.getList();
        this.getListDept()
      }
    }
  },
  methods: {
    // 构造树形结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    // 获取部门数据
    getListDept() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      })
    },
    /** 查询设备台账列表 */
    getList() {
      this.loading = true;
      listEquipment(this.queryParams).then(response => {
        this.equipmentList = response.rows;
        this.total = response.total;
      }).finally(() => {
        this.loading = false;
      });
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
        name: null,
        equNo: null,
        depId: null,
        depName: null,
      }
      this.resetForm("queryForm");
      this.equipmentList = []
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
        this.$message.warning("请选择设备！")
        return false
      }
      if (this.currentRows) {
        this.$emit('setEquipment', this.currentRows)
      }
      this.open = false
    }
  }
}
</script>