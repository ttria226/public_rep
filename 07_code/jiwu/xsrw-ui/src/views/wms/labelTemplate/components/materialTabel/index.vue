<template>
  <div class="app-container">

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
      <el-table-column label="批次号" min-width="220px" align="center" :show-overflow-tooltip="true" prop="batchCode" />
      <el-table-column label="物料名称" align="center" :show-overflow-tooltip="true" prop="name" />
      <el-table-column label="物料类别" align="center" :show-overflow-tooltip="true" prop="categoryName" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
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
import { listDept } from "@/api/system/dept";
import { getCode } from "@/api/wms/common";
import { IWL } from '@/utils/codeType'

export default {
  name: "MaterialTabel",
  dicts: ['cims_inspection_method'],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // 表单参数
      form: {
        code: ""
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "物料名称不能为空", trigger: "blur" }
        ],
        orgId: [
          { required: true, message: "所属组织不能为空", trigger: "blur" }
        ],
        categoryId:[
          { required: true, message: "物料类别不能为空", trigger: "blur" }
        ],
        batchFlag:[
          { required: true, message: "是否启用批次不能为空", trigger: "blur" }
        ],
        batchAttrId:[
          { required: true, message: "批次属性不能为空", trigger: "blur" }
        ],
        sameMaterialFlag:[
          { required: true, message: "是否混物料不能为空", trigger: "blur" }
        ],
        sameBatchFlag:[
          { required: true, message: "是否混批次不能为空", trigger: "blur" }
        ],
        expirationFlag:[
          { required: true, message: "是否启用有效期管理不能为空", trigger: "blur" }
        ],
        inspectionMethod:[
          { required: true, message: "检验方式不能为空", trigger: "blur" }
        ],
        unitId:[
          { required: true, message: "单位不能为空", trigger: "blur" }
        ],
        contactsUnitId:[
          { required: true, message: "供应商不能为空", trigger: "blur" }
        ]
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
    };
  },
  created() {
    this.getModeList();
  },
  methods: {
    /** 查询所有下拉列表 **/
    getModeList(){
      /** 单位下拉列表 **/
      listUnit().then(response =>{
        this.unitList = response.rows;
      })

      /** 供应商下拉列表 **/
      listContactsUnit().then(response =>{
        this.contactsUnitList = response.rows;
      })

      /** 物料类别下拉列表 **/
      listCategory().then(response =>{
        this.categoryList = response.rows;
      });

      /** 批次属性下拉列表 **/
      listAttr().then(response =>{
        this.attrList = response.rows;
      });

      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
        this.deptList = response.data;
      });
    },

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
      listMaterial(this.queryParams).then(response => {
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
    // 表单重置
    reset() {
      this.form = {
        id: null,
        orgId: null,
        code: null,
        name: null,
        specifications: null,
        unitId: null,
        contactsUnitId: null,
        categoryId: null,
        batchFlag: null,
        batchAttrId: null,
        baseUnitId: null,
        sameMaterialFlag: null,
        sameBatchFlag: null,
        inspectionMethod: null,
        expirationDate: null,
        expirationFlag: null,
        stockMax: null,
        stockMin: null,
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
      this.names = selection.map(item => item.name)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加物料管理";

      // 获取编码
      getCode(IWL).then(response => {
        this.form.code = response.msg;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMaterial(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物料管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.deptList.forEach(e=> {
            if(e.deptId == this.form.orgId){
              this.form.orgName = e.deptName
            }
          });

          if (this.form.id != null) {
            updateMaterial(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMaterial(this.form).then(response => {
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
      const name = row.name || this.names;
      this.$modal.confirm('是否确认删除物料"' + name + '"的数据项？').then(function() {
        return delMaterial(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wms/material/export', {
        ...this.queryParams
      }, `material_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
