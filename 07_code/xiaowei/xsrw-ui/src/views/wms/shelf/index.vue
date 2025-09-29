<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编码" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['base:shelf:add']">新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['base:shelf:edit']">修改</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:shelf:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['base:shelf:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shelfList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主键" align="center" prop="id" /> -->
      <el-table-column label="编码" align="center" prop="code" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="是否启用" align="center" prop="status" :show-overflow-tooltip="true">
        <template slot-scope="scope"> {{scope.row.status == '0' ? '启用':'禁用'}}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['base:shelf:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-open" v-if="scope.row.status == '1'" @click="handleUpdateStatus(scope.row, '0')" v-hasPermi="['base:shelf:updateStatus']">启用</el-button>
          <el-button size="mini" type="text" icon="el-icon-turn-off" v-if="scope.row.status == '0'" @click="handleUpdateStatus(scope.row, '1')" v-hasPermi="['base:shelf:updateStatus']">禁用</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['base:shelf:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改货架对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20" v-if="form.id">
          <el-col :span="12">
            <el-form-item label="编码">
              <el-input v-model="form.code" disabled placeholder="请输入编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区域" prop="areaId">
              <el-select style="width: 100%;" v-model="form.areaId"  @change="val => changeReservoirList(val)" placeholder="请选择所属区域" clearable>
                <el-option v-for="dict in areaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.id">
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库区" prop="reservoirId">
              <el-select style="width: 100%;" v-model="form.reservoirId" placeholder="请选择所属库区" clearable>
                <el-option v-for="dict in reservoirList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.id">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select style="width: 100%;" v-model="form.status" placeholder="请选择状态">
              <el-option value="0" label="启用"></el-option>
              <el-option value="1" label="禁用"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="!form.id">
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区域" prop="areaId">
              <el-select style="width: 100%;" v-model="form.areaId"  @change="val => changeReservoirList(val)" placeholder="请选择所属区域" clearable>
                <el-option v-for="dict in areaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="!form.id">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select style="width: 100%;" v-model="form.status" placeholder="请选择状态">
                <el-option value="0" label="启用"></el-option>
                <el-option value="1" label="禁用"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库区" prop="reservoirId">
              <el-select style="width: 100%;" v-model="form.reservoirId" placeholder="请选择所属库区" clearable>
                <el-option v-for="dict in reservoirList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listShelf, getShelf, delShelf, addShelf, updateShelf } from "@/api/wms/shelf";
import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";

import {wms} from '@/utils/agent';

export default {
  name: "Shelf",
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
      // 货架表格数据
      shelfList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        name: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ],
        areaId: [
          { required: true, message: '请选择区域', trigger: 'change' }
        ],
        reservoirId: [
          { required: true, message: '请选择库区', trigger: 'change' }
        ],
      },

      areaList: [], //区域list
      reservoirList: [],//库区list
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询货架列表 */
    getList() {
      this.loading = true;
      listShelf(this.queryParams).then(response => {
        this.shelfList = response.rows;
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
        code: null,
        name: null,
        status: '0',
        areaId: null,
        reservoirId: null,
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
      this.title = "添加货架";
      this.getAreaList()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.getAreaList()
      getShelf(id).then(response => {
        this.changeReservoirList(response.data.areaId,() => {
          this.form = response.data;
          this.open = true;
          this.title = "修改货架";
        })
      });
    },
    /** 改变区域 */
    changeReservoirList(val,callback){
      this.form.reservoirId = null;
      listReservoir({areaId:val,pageSize:5000}).then(response => {
        this.reservoirList = response.rows;
        callback && callback()
      });
    },
    /** 获取区域list */
    getAreaList(){
      listArea({pageSize:5000}).then(response => {
        this.areaList = response.rows;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateShelf(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addShelf(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 启用禁用 */
    handleUpdateStatus(row, status){
      const data = {
        id: row.id,
        status: status
      }
      const msg = status == "0" ? row.name + '"启用？' : row.name + '"禁用？';

      this.$modal.confirm('是否确认将货架"' + msg).then(function() {
        return updateShelf(data);
      }).then(() => {
        this.getList();
        if(status == "1"){
          this.$modal.msgSuccess("启用成功");
        } else {
          this.$modal.msgSuccess("禁用成功");
        }

      }).catch(() => {});

    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
        return delShelf(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/base/shelf/export', {...this.queryParams}, `shelf_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
