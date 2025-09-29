<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="公司" prop="companyName">
        <el-input v-model="queryParams.companyName" placeholder="请输入公司" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="仓库" prop="warehouseId">
        <el-select v-model="queryParams.warehouseId"  placeholder="请选择仓库" clearable>
          <el-option v-for="dict in queryWarehouseList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading='addLoading' @click="handleAdd" v-hasPermi="['wms:reservoir:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['wms:reservoir:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:reservoir:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="personManageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="姓名" align="center" prop="name" min-width="120px" :show-overflow-tooltip="true"/>
      <el-table-column label="邮箱" align="center" prop="email" min-width="150px" :show-overflow-tooltip="true"/>
      <el-table-column label="地址" align="center" prop="address" min-width="150px" :show-overflow-tooltip="true"/>
      <el-table-column label="手机号" align="center" prop="phone" min-width="120px" :show-overflow-tooltip="true"/>
      <el-table-column label="公司" align="center" prop="companyName" min-width="220px" :show-overflow-tooltip="true"/>
      <el-table-column label="性别" align="center" prop="sex" min-width="100px" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.sex" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="仓库" align="center" prop="status" :show-overflow-tooltip="true" />
      <el-table-column label="是否禁用" align="center" prop="status" :show-overflow-tooltip="true" min-width="120px" >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="1" inactive-value="0" @change="value => handleOpenClose(value,scope.row,scope.$index)"></el-switch>
        </template>
      </el-table-column> -->
      <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" width="180" />
      <!-- <el-table-column label="备注" align="center" prop="remark" width="220px" :show-overflow-tooltip="true"/> -->
      <el-table-column label="操作" align="center" fixed="right" width="180px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:reservoir:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:reservoir:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改库区对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <!-- <el-form-item label="仓库" prop="warehouseId">
          <el-select style="width: 100%;" v-model="form.warehouseId" placeholder="请选择仓库" class="select-input-form">
            <el-option v-for="dict in warehouseList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="公司" prop="companyName">
          <!-- <el-select style="width: 100%;" v-model="form.companyName" placeholder="请选择所属公司" class="select-input-form">
            <el-option v-for="dict in companyList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select> -->
          <el-input v-model="form.companyName" placeholder="请输入所属公司" maxlength="40" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.sex">
            <el-radio v-for="dict in dict.type.sys_user_sex" :key="dict.id" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <!-- <el-form-item label="是否禁用" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="5" placeholder="请输入内容" maxlength="250" show-word-limit />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPerson, getPerson, addPerson, updatePerson, delPerson } from "@/api/taskManager/person";

import { listArea } from "@/api/wms/area";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

import { wms } from '@/utils/agent'

export default {
  name: "personManage",
  components: { Treeselect },
  dicts: ["sys_user_sex"],
  data() {
    return {
      queryWarehouseList: [], // 筛选的仓库列表
      warehouseList:[],//仓库列表
      companyList:[],//所属公司
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      names: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 人员管理表格数据
      personManageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        companyName: null,
        // warehouseId: null,
        phone: null,
      },
      // 表单参数
      form: {
        code: ""
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        warehouseId: [
          { required: true, message: "仓库不能为空", trigger: "change" }
        ],
        companyName: [
          { required: true, message: "所属公司不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "手机号不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "是否禁用不能为空", trigger: "change" }
        ],
      },
      addLoading: false, // 新增按钮loading
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'personManage') {
        this.getList();
      }
    }
  },
  created() {
    // listArea({pageSize:5000}).then(response => {
    //     this.queryWarehouseList = response.rows;
    // });
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      // 查询人员管理列表
      listPerson(this.queryParams).then(response => {
        this.personManageList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
    },
    // 表单重置
    reset() {
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.names = selection.map(item => item.name)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      // listArea({pageSize:5000}).then(response => {
      //   this.warehouseList = response.rows;
      // });
      this.open = true;
      this.title = "添加人员";
      this.form = {
        id: null,
        // warehouseId: null,
        name: null,
        companyName: null,
        phone: null,
        sex: null,
        email: null,
        address: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null
      };
      this.$nextTick(() => {
        this.reset();
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // listArea({pageSize:5000}).then(response => {
      //    this.areaList = response.rows;
      // });
      const id = row.id || this.ids
      this.form = {
        id: null,
        // warehouseId: null,
        name: null,
        companyName: null,
        phone: null,
        sex: null,
        email: null,
        address: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null
      };
      getPerson(id).then(response => {
        this.open = true;
        this.title = "修改人员";
        this.$nextTick(() => {
          this.reset();
          this.form = response.data;
        })
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePerson(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPerson(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 处理是否可用事件 */
    handleOpenClose(value,row,index){
      let params = {
        id: row.id,
        status: value
      }
      updatePutawayStatus(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess(value === '1' ? "启用成功" : "禁用成功");
          this.getList();
        } else {
          row.status = value === '1' ? '0' : '1'
          this.$set(this.storagePolicyList,index,row)
        }
      }).catch(() => {
        row.status = value === '1' ? '0' : '1'
        this.$set(this.storagePolicyList,index,row)
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
        return delPerson(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/person/export', {...this.queryParams}, `personManage_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
