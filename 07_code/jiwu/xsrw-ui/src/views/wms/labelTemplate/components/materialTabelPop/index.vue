<template>
  <el-dialog title="选择物料" :visible.sync="open" width="1200px" append-to-body>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input
          v-model="queryParams.materialCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规格型号" prop="specifications">
        <el-input
          v-model="queryParams.specifications"
          placeholder="请输入规格型号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="materialList" highlight-current-row @current-change="handleCurrentChange" @selection-change="handleSelectionChange">
      <!-- <el-table-column label="所属组织" align="center" :show-overflow-tooltip="true" prop="orgName" /> -->
      <el-table-column label="来源类型" align="center" :show-overflow-tooltip="true" prop="type" />
      <el-table-column label="物料编码" align="center" min-width="220px" :show-overflow-tooltip="true" prop="code" />
      <el-table-column label="物料名称" align="center" :show-overflow-tooltip="true" prop="name" />
      <el-table-column label="批次号" align="center" min-width="200px" :show-overflow-tooltip="true" prop="batchCode" />
      <el-table-column label="规格型号" align="center" :show-overflow-tooltip="true" prop="specifications" />
      <el-table-column label="单位" align="center" min-width="100px" :show-overflow-tooltip="true" prop="unitName" />
      <el-table-column label="物料类别" align="center" :show-overflow-tooltip="true" prop="categoryName" />
      <!-- <el-table-column label="是否启用批次" align="center" :show-overflow-tooltip="true" prop="batchFlag" /> -->
      <el-table-column label="批次属性" align="center" min-width="120px" :show-overflow-tooltip="true" prop="batchAttrName" />
      <el-table-column label="是否混物料" align="center" min-width="120px" :show-overflow-tooltip="true" prop="sameMaterialFlag" />
      <el-table-column label="是否混批次" align="center" min-width="120px" :show-overflow-tooltip="true" prop="sameBatchFlag" />
      <el-table-column label="备注" align="center" :show-overflow-tooltip="true" prop="remark" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" :disabled="!currentRow" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>

  </el-dialog>
</template>

<script>
  import { getMaterialList } from "@/api/wms/labelTemplate";
// import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/wms/material";
import { listUnit } from "@/api/wms/unit";
import { listCategory } from "@/api/wms/category";
import { listAttr } from "@/api/wms/attr";
import { listContactsUnit } from "@/api/wms/contactsUnit";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { listDept } from "@/api/system/dept";
import { getCode } from "@/api/wms/common";

export default {
  name: "MaterialLabelPop",
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      names:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 物料管理表格数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orgId: null,
        code: null,
        name: null,
        specifications: null,
      },
      // 单位
      unitList:[],
      // 物料类别
      categoryList:[],
      // 批次属性
      attrList:[],
      // 供应商
      contactsUnitList:[],
      // 部门树选项
      deptOptions: [],
      deptList:[],
      currentRow: null,
    };
  },
  created() {
    this.getList();
  },
  methods: {

    /** 转换部门数据结构 */
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

    /** 查询物料管理列表 */
    getList() {
      this.loading = true;
      getMaterialList(this.queryParams).then(response => {
        response.rows.forEach(data => {
          if (data.batchFlag == '0'){
              data.batchFlag = "否";
          }
          if (data.batchFlag == '1'){
             data.batchFlag = "是";
          }
          if (data.sameMaterialFlag == '0'){
            data.sameMaterialFlag = "否";
          }
          if (data.sameMaterialFlag == '1'){
            data.sameMaterialFlag = "是";
          }
          if (data.sameBatchFlag == '0'){
            data.sameBatchFlag = "否";
          }
          if (data.sameBatchFlag == '1'){
            data.sameBatchFlag = "是";
          }
        });
        this.materialList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
    /** 提交按钮 */
    submitForm() {
      this.$emit('setCurrentRow', this.currentRow)
      this.open = false
    },
    handleCurrentChange(val) {
      this.currentRow = val
    }
  }
};
</script>
