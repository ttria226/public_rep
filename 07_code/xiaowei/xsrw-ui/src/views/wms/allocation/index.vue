<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['base:allocation:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['base:allocation:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['base:allocation:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['base:allocation:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-s-cooperation"
          size="mini"
          @click="handleMove"
        >载具搬运</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="allocationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="编码" align="center" prop="code" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="备注" align="center" prop="remark" />
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['base:allocation:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['base:allocation:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改接货位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 载具搬运功能 -->
    <el-dialog :title="title" :visible.sync="openCarry" width="700px" append-to-body>
      <el-form ref="carryForm" :model="carryForm" :rules="rules" label-width="100px">
        <el-form-item label="搬运类型：" prop="type">
          <el-radio-group v-model="carryForm.type" @change="radioTypeChange">
            <el-radio :label="'1'">入库</el-radio>
            <el-radio :label="'2'">出库</el-radio>
            <el-radio :label="'3'">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="开始位置：" prop="startPoint">
          <el-radio-group v-model="carryForm.startPoint" @change="radioStartChange">
            <el-radio v-for="item in allocationList1" :key="item.label" :label="item.label">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="结束位置：" prop="endPoint">
          <el-radio-group v-model="carryForm.endPoint">
            <el-radio v-for="item in allocationList2" :key="item.label" :label="item.label">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="载具编号：" prop="trayCode">
          <el-input v-model="carryForm.trayCode" placeholder="请输入载具编号" style="width:50%" maxlength="50" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCarryForm" :loading="carryLoading">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAllocation, getAllocation, delAllocation, addAllocation, updateAllocation, carryAllocation } from "@/api/wms/allocation";

import { wms } from '@/utils/agent';
import store from "../../../store";
import {Message} from "element-ui";

export default {
  name: "Allocation",
  data() {
    return {
      // 遮罩层
      loading: true,
      carryLoading:false,
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
      // 接货位表格数据
      allocationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openCarry: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        name: null,
      },
      // 表单参数
      form: {},
      carryForm:{
        type: '1',
        startPoint:"HC01",
        endPoint:"HC05",
        trayCode:""
      },
      // 表单校验
      rules: {
        type: [{ required: true, message: "请选择搬运类型", trigger: "change" }],
        startPoint: [{ required: true, message: "请选择开始位置", trigger: "change" }],
        endPoint: [{ required: true, message: "请选择结束位置", trigger: "change" }],
      },
      allocationList1:[
        {
          label : "HC01",
          name : "HC01",
        },
        {
          label : "HC02",
          name : "HC02",
        },
        {
          label : "HC03",
          name : "HC03",
        },
        {
          label : "HC04",
          name : "HC04",
        }
      ],//入口
      allocationList2:[
        {
          label : "HC05",
          name : "HC05",
        },{
          label : "HC06",
          name : "HC06",
        },{
          label : "HC07",
          name : "HC07",
        },{
          label : "HC08",
          name : "HC08",
        },{
          label : "HC09",
          name : "HC09",
        },{
          label : "HC10",
          name : "HC10",
        },
      ],//出口
      wcsStartPoint:"HC01",//wcs开始位置
      wcsEndPoint:"HC05",//wcs结束位置
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询接货位列表 */
    getList() {
      this.loading = true;
      listAllocation(this.queryParams).then(response => {
        this.allocationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openCarry = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        code: null,
        name: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deptId: null,
        deptName: null,
        delFlag: null
      };
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加接货位";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAllocation(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改接货位";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAllocation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAllocation(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 载具搬运提交按钮 */
    submitCarryForm() {
      this.$refs["carryForm"].validate(valid => {
        if (valid) {
          if(this.carryForm.startPoint == this.carryForm.endPoint){
            this.$modal.msgError("开始位置和结束位置不能一致!");
            return;
          }
          this.$modal.confirm('是否确认调用设备从【' + this.carryForm.startPoint+'】-【'+  this.carryForm.endPoint+ '】？').then(()=>{
            this.carryLoading = true;
            return carryAllocation(this.carryForm);
          }).then(response => {
            if(response.code == 200){
              this.carryLoading = false;
              this.$modal.msgSuccess("操作成功");
              this.openCarry = false;
            }else{
              this.carryLoading = false;
              this.$modal.msgError(response.msg);
            }
          }).catch(() => {
            this.carryLoading = false;
          });
        }
      });
    },
    /** 设备搬运 */
    handleMove(row){
      this.title = "载具搬运";
      this.openCarry = true;
      this.form = row;
      this.radioStartChange();
    },
    radioTypeChange(){
      let typeval = this.carryForm.type
      if(typeval == 1){
        // this.allocationList2 = [{
        //   name: 'HC05',
        //   label: 'HC05',
        // },{
        //   name: 'HC06',
        //   label: 'HC06',
        // }]
        this.allocationList2 = [{
          name: 'SSX1',
          label: 'SSX1',
        },]
        this.carryForm.endPoint = 'SSX1'
        this.allocationList1 = [{
          name: 'HC01',
          label: 'HC01',
        },
          {
            name: 'HC02',
            label: 'HC02',
          },
          {
            name: 'HC03',
            label: 'HC03',
          },
          {
            name: 'HC04',
            label: 'HC04',
          },]
        this.carryForm.startPoint = 'HC01'
      }else if(typeval == 2){
        this.allocationList2 = [{
          name: 'HC07',
          label: 'HC07',
        },{
          name: 'HC08',
          label: 'HC08',
        },{
          name: 'HC09',
          label: 'HC09',
        },{
          name: 'HC10',
          label: 'HC10',
        }]
        this.carryForm.endPoint = 'HC07'
        this.allocationList1 = [{
          name: 'SSX1',
          label: 'SSX1',
        },]
        this.carryForm.startPoint = 'SSX1'
      }else{
        this.allocationList2 = [{
          name: 'HC01',
          label: 'HC01',
        },
          {
            name: 'HC02',
            label: 'HC02',
          },
          {
            name: 'HC03',
            label: 'HC03',
          },
          {
            name: 'HC04',
            label: 'HC04',
          },{
            name: 'HC05',
            label: 'HC05',
          },
          {
            name: 'HC06',
            label: 'HC06',
          },
          {
            name: 'HC07',
            label: 'HC07',
          },
          {
            name: 'HC08',
            label: 'HC08',
          },
          {
            name: 'HC09',
            label: 'HC09',
          },
          {
            name: 'HC10',
            label: 'HC10',
          },{
            name: 'SSX1',
            label: 'SSX1',//输送线口
          }]
        this.carryForm.endPoint = 'HC05'
        this.allocationList1 = [{
          name: 'HC01',
          label: 'HC01',
        },
          {
            name: 'HC02',
            label: 'HC02',
          },
          {
            name: 'HC03',
            label: 'HC03',
          },
          {
            name: 'HC04',
            label: 'HC04',
          },{
            name: 'HC05',
            label: 'HC05',
          },
          {
            name: 'HC06',
            label: 'HC06',
          },
          {
            name: 'HC07',
            label: 'HC07',
          },
          {
            name: 'HC08',
            label: 'HC08',
          },
          {
            name: 'HC09',
            label: 'HC09',
          },
          {
            name: 'HC10',
            label: 'HC10',
          },{
            name: 'SSX1',
            label: 'SSX1',//输送线口
          }]
        this.carryForm.startPoint = 'HC01'
      }
    },
    radioStartChange(){
      if(this.carryForm.type == 3){
        return
      }
      let typeval = this.carryForm.startPoint
      if(typeval == 'HC01' || typeval == 'HC02'){
        this.carryForm.endPoint = 'SSX1'
        this.allocationList2 = [{
          name: 'SSX1',
          label: 'SSX1',//输送线口
        }]
      }else if(typeval == 'HC03' || typeval == 'HC04'){
        this.carryForm.endPoint = 'HC05'
        this.allocationList2 = [{
          name: 'HC05',
          label: 'HC05',
        },{
          name: 'HC06',
          label: 'HC06',
        }]
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除接货位编号为"' + ids + '"的数据项？').then(function() {
        return delAllocation(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/allocation/export', {
        ...this.queryParams
      }, `allocation_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
