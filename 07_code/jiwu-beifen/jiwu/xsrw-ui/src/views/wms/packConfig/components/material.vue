<template>
  <div class="app-container">
    <el-dialog title="选择物料" :visible.sync="open" width="1200px" append-to-body>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="物料编码" prop="code">
          <el-input v-model="queryParams.code" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="物料名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="物料类别" prop="categoryId">
          <el-select v-model="queryParams.categoryId" class="select-input-form">
            <el-option v-for="item in categoryList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="materialList" highlight-current-row @current-change="handleCurrentChange">
        <el-table-column label="物料编码" align="center" prop="code" width="200" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" align="center" prop="name" :show-overflow-tooltip="true" />
        <el-table-column label="规格型号" align="center" prop="specifications" :show-overflow-tooltip="true" />
        <el-table-column label="单位" align="center" prop="unitName" :show-overflow-tooltip="true" />
        <el-table-column label="物料类别" align="center" prop="categoryName" :show-overflow-tooltip="true" />
        <!-- <el-table-column label="是否启用批次" align="center" prop="batchFlag" :show-overflow-tooltip="true" /> -->
        <el-table-column label="批次属性" align="center" prop="batchAttrName" :show-overflow-tooltip="true" />
        <!-- <el-table-column label="是否混物料" align="center" prop="sameMaterialFlag" :show-overflow-tooltip="true" />
        <el-table-column label="是否混批次" align="center" prop="sameBatchFlag" :show-overflow-tooltip="true" /> -->
        <el-table-column label="备注" align="center" prop="remark" />
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/wms/material";
  import { listUnit } from "@/api/wms/unit";
  import { listCategory } from "@/api/wms/category";
  import { listAttr } from "@/api/wms/attr";
  import { listContactsUnit } from "@/api/wms/contactsUnit";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "MaterialCom",
    dicts: ['cims_inspection_method'],
    components: {
      Treeselect
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
          contactsUnitId: null,
        },
        // 单位
        unitList: [],
        // 物料类别
        categoryList: [],
        // 批次属性
        attrList: [],
        // 供应商
        contactsUnitList: [],
        current: null, // 当前选中的物料
      };
    },
    watch: {
      open(){
        if (this.open) {
          this.getList();

          /** 物料类别下拉列表 **/
          listCategory().then(response => {
            this.categoryList = response.rows;
          });
        }
      }
    },
    created() {
      // this.getList();
      // this.getModeList();
    },
    methods: {
      /** 查询所有下拉列表 **/
      getModeList() {
        /** 单位下拉列表 **/
        listUnit().then(response => {
          this.unitList = response.rows;
        })

        /** 供应商下拉列表 **/
        listContactsUnit().then(response => {
          this.contactsUnitList = response.rows;
        })

        /** 批次属性下拉列表 **/
        listAttr().then(response => {
          this.attrList = response.rows;
        });
      },

      /** 查询物料管理列表 */
      getList() {
        this.loading = true;
        listMaterial(this.queryParams).then(response => {
          response.rows.forEach(data => {
            if (data.batchFlag == '0') {
              data.batchFlag = "否";
            }
            if (data.batchFlag == '1') {
              data.batchFlag = "是";
            }
            if (data.sameMaterialFlag == '0') {
              data.sameMaterialFlag = "否";
            }
            if (data.sameMaterialFlag == '1') {
              data.sameMaterialFlag = "是";
            }
            if (data.sameBatchFlag == '0') {
              data.sameBatchFlag = "否";
            }
            if (data.sameBatchFlag == '1') {
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
        // this.reset();
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
      // 选中行
      handleCurrentChange(row) {
        this.current = row
      },
      /** 提交按钮 */
      submitForm() {
        if (this.current) {
          this.$emit('setMaterial', this.current)
        }
        this.open = false
      },
  }
};
</script>
