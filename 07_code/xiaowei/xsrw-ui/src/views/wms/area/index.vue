<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="区域名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入区域名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
           <el-option value="0" label="启用"></el-option>
           <el-option value="1" label="禁用"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading='addLoading' @click="handleAdd" v-hasPermi="['wms:area:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['wms:area:remove']">删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-upload
          class="upload-demo"
          :action="uploadUrl"
          :headers="uploadHeaders"
          :on-success="handleAfterUpload"
          :limit="1"
          :on-exceed="handleExceed"
          :before-upload="handleBeforeUpload"
          :show-file-list="false"
          :file-list="fileList"
        >
          <el-button plain icon="el-icon-upload" v-hasPermi="['wms:area:importData']" size="mini">导入</el-button>
        </el-upload>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:area:export']">导出</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExportDemo" v-hasPermi="['wms:unit:exportdemo']">下载模板</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="areaList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="区域名称" align="center" prop="name" :show-overflow-tooltip="true"  width="180px" />
      <el-table-column label="区域编码" align="center" prop="code" :show-overflow-tooltip="true"  width="100px" />
      <!-- <el-table-column label="仓库名称" align="center" prop="warehouse.name" width="220px" :show-overflow-tooltip="true"/> -->
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '0' ">启用</span>
          <span v-if="scope.row.status == '1' ">禁用</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"  width="400px"/>
      <el-table-column label="操作" align="center" fixed="right" width="160px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:area:edit']">修改</el-button>
          <el-button v-if="scope.row.status == '0'" size="mini" type="text" icon="el-icon-turn-off" @click="handleUpdateStatus(scope.row,'1')" v-hasPermi="['wms:area:updateStatus']">禁用</el-button>
          <el-button v-if="scope.row.status == '1'" size="mini" type="text" icon="el-icon-open" @click="handleUpdateStatus(scope.row,'0')" v-hasPermi="['wms:area:updateStatus']">启用</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:area:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改区域对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="区域名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" maxlength="40" show-word-limit />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select style="width: 100%;" v-model="form.status" placeholder="请选择状态" @change="inputUpdate" class="select-input-form">
           <el-option value="0" label="启用"></el-option>
           <el-option value="1" label="禁用"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="5" placeholder="请输入内容" maxlength="250" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listArea, getArea, delArea, addArea, updateArea } from "@/api/wms/area";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
// import { listWarehouse } from "@/api/wms/warehouse";
import { listUser } from "@/api/system/user";
import { wms } from '@/utils/agent';
import { getToken } from '@/utils/auth'
import store from "@/store";

export default {
  name: "Area",
  // dicts: ['cims_unit_type'],
    components: { Treeselect },
  data() {
    return {
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
      // 区域表格数据
      areaList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orgName: null,
        code: null,
        name: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "区域名称不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
      },
      // 编号
      codeValue:null,
      uploadUrl: process.env.VUE_APP_BASE_API +'/'+wms+'/area/importData', // 上传地址
      uploadHeaders: { Authorization: this.getToken() },
      fileList:[],
      addLoading: false, // 新增按钮loading
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'area') {
        this.getList();
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getToken,
    // 更新输入框，部分输入框改变后视图不会同步更新时使用
    inputUpdate () {
      this.$forceUpdate()
    },
    getList() {
      this.loading = true;
      // 查询区域列表
      listArea(this.queryParams).then(response => {
        this.areaList = response.rows;
        this.total = response.total;
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
        name: null,
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
      this.queryParams.orgName = null
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
      this.reset();
      this.open = true;
      this.form.status = "0";
      this.title = "添加区域";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getArea(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改区域";
      });
    },
    handleUpdateStatus(row,status){
      const ids = row.id || this.ids;
      let strname = '禁用'
      if(status == '1'){
        strname = '禁用'
      }else{
        strname = '启用'
      }
      this.$modal.confirm('是否'+strname+'该区域？').then(function() {
        row.status = status;
        return updateArea(row);
      }).then(()=>{
        this.$modal.msgSuccess("修改成功");
        this.getList();
      }).catch(()=>{
        if (status == '1'){
          row.status = '0'
        }else {
          row.status = '1'
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateArea(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addArea(this.form).then(response => {
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
      const names = row.name || this.names;
      this.$modal.confirm('是否确认删除区域"' + names + '"的数据项？').then(function() {
        return delArea(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/area/export', {...this.queryParams}, `area_${new Date().getTime()}.xlsx`)
    },
    /** 下载模板按钮操作 */
    handleExportDemo() {
      this.download(wms+'/area/export/demo',{}, `area_${new Date().getTime()}.xlsx`)
    },
    /** 导入超出限制提示 */
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    /** 导入前的校验 */
    handleBeforeUpload(file) {
      // const isJPG = file.type === 'image/jpeg';
      // const isLt2M = file.size / 1024 / 1024 < 2;

      // if (!isJPG) {
      //   this.$message.error('上传头像图片只能是 JPG 格式!');
      // }
      // if (!isLt2M) {
      //   this.$message.error('上传头像图片大小不能超过 2MB!');
      // }
      return true;
    },
    /** 文件上传后回调 */
    handleAfterUpload(response){
      this.fileList = [];
      if (response.code == '200'){
        this.$message.success("导入成功")
        this.resetQuery();
      }else {
        this.$message.error(response.msg)
      }
    },
  }
};
</script>
