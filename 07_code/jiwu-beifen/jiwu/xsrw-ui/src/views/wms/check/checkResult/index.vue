<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="盘点计划详情标识" prop="taskDetailId">
        <el-input
          v-model="queryParams.taskDetailId"
          placeholder="请输入盘点计划详情标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核人" prop="auditor">
        <el-input
          v-model="queryParams.auditor"
          placeholder="请输入审核人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="盘点差异数量" prop="checkDifferenceCount">
        <el-input
          v-model="queryParams.checkDifferenceCount"
          placeholder="请输入盘点差异数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="物料编码" prop="materialCode">
        <el-input
          v-model="queryParams.materialCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
        <!-- <el-select v-model="queryParams.materialId" clearable filterable  placeholder="请选择物料号">
            <el-option
              v-for="item in codeList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
        </el-select> -->
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="起始时间">
        <el-date-picker
          v-model="queryParams.startDate"
          value-format="yyyy-MM-dd"
          type="date"
          placeholder="选择起始时间">
        </el-date-picker>
      </el-form-item> -->
      <!-- <el-form-item label="结束时间">
        <el-date-picker
          v-model="queryParams.endDate"
          value-format="yyyy-MM-dd"
          type="date"
          placeholder="选择结束时间">
        </el-date-picker>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

   <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['wms:checkResult:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['wms:checkResult:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['wms:checkResult:remove']"
        >删除</el-button>
      </el-col> -->
      <!-- <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="ImportBill"
        >单据导入</el-button>
      </el-col> -->
     <el-col :span="1.5">
       <el-button
         type="warning"
         plain
         icon="el-icon-download"
         size="mini"
         @click="handleExport"
         v-hasPermi="['wms:checkResult:export']"
       >导出</el-button>
     </el-col>
     <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
   </el-row>

    <el-table v-loading="loading" :data="checkResultList" @selection-change="handleSelectionChange">
<!--      <el-table-column label="所属组织" align="center" prop="factoryName" :show-overflow-tooltip="true" />-->
<!--      <el-table-column label="仓库" align="center" prop="currentWarehouseName" :show-overflow-tooltip="true" />-->
      <el-table-column label="物料编码" align="center"  width="220px" prop="materialCode" :show-overflow-tooltip="true" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" />
      <!-- <el-table-column label="载具" width="220px" align="center" prop="trayCode" :show-overflow-tooltip="true" /> -->
      <el-table-column label="盘差" align="center" prop="checkDifferenceCount" :show-overflow-tooltip="true" :formatter="checkFormat" />
      <!-- <el-table-column label="仓库号" width="220px" align="center" prop="currentWarehouseCode" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:checkResult:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:checkResult:remove']"
          >删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改盘点差异报表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="盘点计划详情标识" prop="taskDetailId">
          <el-input v-model="form.taskDetailId" placeholder="请输入盘点计划详情标识" />
        </el-form-item>
        <el-form-item label="审核人" prop="auditor">
          <el-input v-model="form.auditor" placeholder="请输入审核人" />
        </el-form-item>
        <el-form-item label="盘点差异数量" prop="checkDifferenceCount">
          <el-input v-model="form.checkDifferenceCount" placeholder="请输入盘点差异数量" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="删除(0:未删除 1:删除)" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除(0:未删除 1:删除)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入 -->
    <el-dialog title="导入单据" :visible.sync="upload.open" width="400px" append-to-body @close="cancel">
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                 :action="upload.url"
    			       :file-list="fileListd"
                 :disabled="upload.isUploading"
    						 :on-remove="onRemove"
    			       :on-change="onChanc"
                 :on-progress="handleFileUploadProgress"
                 :on-success="handleFileSuccess"
                 :auto-upload="false"
                 drag :before-upload="handleBeforeUpload">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！最大不超过50M</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm" :loading="upload.isUploading">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCheckResult, getCheckResult, delCheckResult, addCheckResult, updateCheckResult } from "@/api/wms/pdcheckResult";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { mapGetters } from 'vuex';
import { treeselect } from "@/api/system/dept";
import { listMaterial } from "@/api/wms/material";
import { wms } from '@/utils/agent';
export default {
  name: "CheckCheckResult",
  components:{Treeselect},
  computed: {
    ...mapGetters([
      'deptId','token'
    ]),
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
      // 盘点差异报表表格数据
      checkResultList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        factory:null,
        materialId:null,
        startDate:null,
        endDate:null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      codeList:[
        {dictLabel:'箱',dictValue:1},
        {dictLabel:'个',dictValue:2},
      ],
      // 上传列表
      upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {  },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      fileListd:[],
      // 部门列表
      department:[],
      // 仓库列表
      currentList:[],
    };
  },
  created() {
    this.upload.headers = {Authorization: "Bearer " + this.token}
    this.getList();
    // this.treeselect()
    // this.listMaterial()
  },
  watch: {
    $route(to, form) {
      if (to.name == 'CheckCheckResult') {
        this.getList()
      }
    }
  },
  methods: {
    // 部门下拉列表
    treeselect(){
      treeselect().then(res=>{
        this.department = res.data
      })
    },
    // 物料下拉列表
    listMaterial(){
      listMaterial({pageNum: 1,pageSize: 1000,}).then(res=>{
        this.codeList = res.rows;
      })
    },
    // 盘差正负号处理符号
    checkFormat(row,column){
      let courseType = '';
      if(row.checkDifferenceCount > 0){
        courseType = '+' + row.checkDifferenceCount
      }else{
        courseType = row.checkDifferenceCount
      }
      return courseType;
    },
    /** 查询盘点差异报表列表 */
    getList() {
      this.loading = true;
      listCheckResult(this.queryParams).then(response => {
        this.checkResultList = response.rows;
        // this.checkResultList = [{
        //   material:'TASK202205012135324001',
        //   tray:'载具',
        //   checkDifferenceCount:10,
        //   factory:'跟部门',
        //   currentWarehouse:'3-4'
        // }];
        this.total = response.total;
        // this.total = 1;
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
      this.form = {
        id: null,
        taskDetailId: null,
        status: "0",
        auditor: null,
        checkDifferenceCount: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
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
      this.queryParams.factory = null
      this.queryParams.materialId = null
      this.queryParams.startDate = null
      this.queryParams.endDate = null
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
      this.title = "添加盘点差异报表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCheckResult(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改盘点差异报表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCheckResult(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCheckResult(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
        return delCheckResult(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/checkResult/export', {
        ...this.queryParams
      }, `checkResult_${new Date().getTime()}.xlsx`)
    },
    // 单据导入
    ImportBill(){
      this.fileListd=[]
      this.upload.isUploading = false
      this.upload.open = true
    },
    onRemove(){
    },
    onChanc(){
    	// if(this.fileNumber != 'x'){
    	// 	this.fileNumber++
    	// }else{
    	// 	this.fileNumber = 0
    	// }
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.isUploading = false
    	if(response.code == 200){
    		this.upload.open = false
    	   this.$modal.msgSuccess(response.msg)
    		this.getList();
    	}else{
        this.upload.isUploading = false
    	   this.$modal.msgError(response.msg)
    	}
    },
    // 上传前 校验大小
    handleBeforeUpload(file) {
      let fileSize = 50
      const isLt = file.size / 1024 / 1024 < fileSize
      if (!isLt) {
        this.msgWarn(`上传文件大小不能超过 ${fileSize} MB!`)
        return false
      }
    },
    // 提交上传文件
    submitFileForm() {
      if(this.fileListd.length >0){
        this.$refs.upload.submit()
        this.upload.isUploading = true
      }else{
        this.$modal.msgError('请选择需要上传文件')
      }
    },
  }
};
</script>
