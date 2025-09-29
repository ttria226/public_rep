<template>
  <div class="app-container">
    <div class="content_tree">
      <el-tree ref="treeRef" :data="equipmentTreeList" node-key="id" check-on-click-node :highlight-current="true" :default-expanded-keys="[treeId]" :default-checked-keys="[treeId]" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
    </div>
    <div class="content_left">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery" >重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['wms:equipmentExpBase:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['wms:equipmentExpBase:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:equipmentExpBase:export']">导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="equList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <!-- <el-table-column label="主键" align="center" prop="id" /> -->
        <el-table-column label="标题" align="center" prop="title" />
        <!-- <el-table-column label="分类" align="center" prop="type" /> -->
        <el-table-column label="发布人" align="center" prop="createBy" />
        <el-table-column label="发布时间" align="center" prop="createTime" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:equipmentExpBase:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-document" @click="handleDetail(scope.row)" v-hasPermi="['wms:equipmentExpBase:edit']">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:equipmentExpBase:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>

    <!-- 添加或修改保养经验库对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" :disabled="isShow">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="详情" prop="content">
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
        <el-form-item label="上传文件" prop="fileLink">
          <wenjianUpdate v-model="form.fileLink" @ok="ok" :fileDisabled="isShow"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-if="!isShow">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import wenjianUpdate from "@/components/ImageUpload/wenjianUpdate";
import { wms } from "@/utils/agent";

import { listEquipmentExpBase, getEquipmentExpBase, delEquipmentExpBase, addEquipmentExpBase, updateEquipmentExpBase } from "@/api/equipment/expBase/equipmentExpBase";
import { listEquipmentTree } from "@/api/equipment/equipmentTree/equipmentTree";
export default {
  name: "EquipmentExpBase",
  components: { wenjianUpdate },
  data() {
    return {
      defaultProps: {
        children: 'childList',
        label: 'name'
      },
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
      // 经验库分类树表格数据
      equipmentTreeList: [],
      equList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        // type: null,
        treeId: ""
      },
      //上传
      fileList: [],
      uploadUrl: '/system/user/importData',
      // 表单参数
      form: {
        fileLink: "",
      },
      treeId: "", //选中的tree信息
      // 表单校验
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入详情', trigger: 'change' }],
        fileLink: [{ required: true, message: '请上传文件', trigger: 'change' }],
      },
      isShow: false, //是否查看
    };
  },
  created() {
    this.getListTree()
  },
  methods: {
    ok(val) {
      console.log(val, "11")
      this.form.fileLink = val
    },
    //树形点击事件
    handleNodeClick(data) {
      console.log(data);
      this.queryParams.treeId = data.id
      this.getList()
      this.treeId = data.id
      this.treeInfo = {...data}
      console.log(this.form.treeId, "111")
    },
    /** 查询经验库分类树列表 */
    async getListTree() {
      let resultInfo = await listEquipmentTree({ type: 2, pageSize: 5000 })
      this.equipmentTreeList = resultInfo.rows;
      this.queryParams.treeId = resultInfo.rows[0].id
      this.treeId = resultInfo.rows[0].id
      this.treeInfo = {...resultInfo.rows[0]}
      this.$nextTick(() => {
        this.$refs['treeRef'].setCurrentKey(this.treeId)
      })
      this.getList();
    },
    /** 查询右侧列表 */
    getList() {
      this.loading = true;
      // this.$http.getAction("/wms/equipmentExpBase/list", this.queryParams).then(res => {
      //   this.equList = res.rows
      //   this.total = res.total;
      //   this.loading = false;
      // })
      listEquipmentExpBase({...this.queryParams, baseType: 1}).then(res => {
        console.log(res)
        this.equList = res.rows
        this.total = res.total;
        this.loading = false;
      })
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
        title: null,
        baseType: null,
        type: null,
        content: null,
        fileLink: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deptId: null,
        deptName: null,
        delFlag: null
      };
      this.isShow = false
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
      this.queryParams.treeId = this.treeId
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加保养经验库";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log(row)
      this.reset();
      const id = row.id || this.ids
      getEquipmentExpBase(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改保养经验库";
      });
    },
    /** 查看按钮操作 */
    handleDetail(row) {
      console.log(row)
      this.reset();
      const id = row.id || this.ids
      getEquipmentExpBase(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.isShow = true;
        this.title = "查看保养经验库";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEquipmentExpBase(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            console.log(this.form)
            this.form.treeId = this.treeId
            addEquipmentExpBase(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function () {
        return delEquipmentExpBase(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/equipmentExpBase/export', {...this.queryParams}, `equipmentExpBase_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  display: flex;
  .content_tree {
    width: 150px;
  }
  .content_left {
    width: calc(100vw - 150px);
  }
}
</style>
