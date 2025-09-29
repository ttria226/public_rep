<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模板名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入模板名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="模板类型" prop="labelType">
        <el-select v-model="queryParams.labelType" placeholder="请选择模板类型">
          <el-option v-for="dict in types" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="detailOpen(null, 1)" v-hasPermi="['wms:template:add']">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="labelTemplateList" @selection-change="handleSelectionChange">
      <el-table-column label="模板名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="模板类型" align="center" prop="labelType" :show-overflow-tooltip="true" :formatter="convertType" />
      <el-table-column label="标签宽度" align="center" prop="labelWidth" :show-overflow-tooltip="true" />
      <el-table-column label="标签高度" align="center" prop="labelHeight" :show-overflow-tooltip="true" />
      <el-table-column label="图片宽度" align="center" prop="imageWidth" :show-overflow-tooltip="true" />
      <el-table-column label="图片高度" align="center" prop="imageHeight" :show-overflow-tooltip="true" />
      <el-table-column label="对象宽度" align="center" prop="objectWidth" :show-overflow-tooltip="true" />
      <el-table-column label="对象高度" align="center" prop="objectHeight" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="detailOpen(scope.row.id, 2)" v-hasPermi="['wms:template:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="detailOpen(scope.row.id, 3)" v-hasPermi="['wms:template:query']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:template:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改标签模板对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="标签宽度" prop="labelWidth">
          <el-input v-model="form.labelWidth" placeholder="请输入标签宽度" />
        </el-form-item>
        <el-form-item label="标签高度" prop="labelHeight">
          <el-input v-model="form.labelHeight" placeholder="请输入标签高度" />
        </el-form-item>
        <el-form-item label="图片宽度" prop="imageWidth">
          <el-input v-model="form.imageWidth" placeholder="请输入图片宽度" />
        </el-form-item>
        <el-form-item label="图片高度" prop="imageHeight">
          <el-input v-model="form.imageHeight" placeholder="请输入图片高度" />
        </el-form-item>
        <el-form-item label="对象宽度" prop="objectWidth">
          <el-input v-model="form.objectWidth" placeholder="请输入对象宽度" />
        </el-form-item>
        <el-form-item label="对象高度" prop="objectHeight">
          <el-input v-model="form.objectHeight" placeholder="请输入对象高度" />
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
    <LabelTemplateDetail ref="templateDetail" @submitOK="handleDetailSubmit"></LabelTemplateDetail>
  </div>
</template>

<script>
import { listLabelTemplate, getLabelTemplate, delLabelTemplate, addLabelTemplate, updateLabelTemplate } from "@/api/wms/labelTemplate";
import { listMaterial } from "@/api/wms/material";

import LabelTemplateDetail from "./labelTemplateDetail"

export default {
  name: "LabelTemplate",
    components: {
      LabelTemplateDetail,
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
      // 标签模板表格数据
      labelTemplateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        labelWidth: null,
        labelHeight: null,
        imageWidth: null,
        imageHeight: null,
        objectWidth: null,
        objectHeight: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      types: [], // 类型源数据
      materialList: [], // 物料列表
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'template') {
        this.getList();
      }
    }
  },
  created() {
    // 获取类型源数据
    this.getDicts('wms_label_template_type').then(response => {
      this.types = response.data
    })
    this.getList();
  },
  methods: {
    // 标签类型翻译
    convertType(row, column) {
      const item = this.types.find(item => item.dictValue == row.labelType)
      return item && item.dictLabel ? item.dictLabel : '--'
    },
    /** 查询标签模板列表 */
    getList() {
      this.loading = true;
      listLabelTemplate(this.queryParams).then(response => {
        this.labelTemplateList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询物料列表 */
    getMaterialList() {
      this.loading = true;
      listLabelTemplate(this.queryParams).then(response => {
        this.labelTemplateList = response.rows;
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
        labelWidth: null,
        labelHeight: null,
        imageWidth: null,
        imageHeight: null,
        objectWidth: null,
        objectHeight: null,
        status: "0",
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
      this.title = "添加标签模板";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLabelTemplate(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改标签模板";
      });
    },
    /** 更新完刷新列表 */
    handleDetailSubmit(flag){
      this.$refs.templateDetail.open = false
      if(flag){
        this.getList();
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLabelTemplate(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLabelTemplate(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除标签模板名称为"' + row.name  + '"的数据项？').then(function() {
        return delLabelTemplate(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wms/labelTemplate/export', {...this.queryParams}, `labelTemplate_${new Date().getTime()}.xlsx`)
    },
    // 打开配置明细弹窗
    detailOpen(id, type) {
      let title = type == 1 ? '添加标签模板' : (type == 2 ? '修改标签模板' : '标签模板详情')
      this.$refs.templateDetail.open = true
      this.$refs.templateDetail.id = id
      this.$refs.templateDetail.type = type
      this.$refs.templateDetail.title = title
    }
  }
};
</script>
