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
      <el-form-item label="创建者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入创建者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门" prop="deptId">
        <el-input
          v-model="queryParams.deptName"
          placeholder="请输入部门"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['base:bom:add']"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['base:bom:edit']"
        >修改</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['base:bom:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['base:bom:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bomList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主键" align="center" prop="id" /> -->
      <el-table-column label="编码" align="center" prop="code" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="价格（元）" align="center" prop="sumPrice" />
      <el-table-column label="重量（kg）" align="center" prop="sumWeight" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="部门" align="center" prop="deptName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:bom:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:bom:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改bom对话框 -->
    <el-dialog :title="title" :visible.sync="open"  width="55%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20" class="mb8">
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" :maxlength="40" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请部门">
              <el-input v-model="form.deptName" disabled placeholder="请输入申请部门" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请人">
              <el-input v-model="form.createBy" disabled placeholder="请输入申请人" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10" class="mb8" style="margin-left: 25px;">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addDetailed()" >新增行</el-button>
          </el-col>
        </el-row>
        <el-form-item prop="bomDetails" label-width="30px">
          <el-table v-loading="loading" :data="form.bomDetails">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'bomDetails.' + scope.$index + '.materialCode'" :rules="detailRules.materialCode">
                  <el-input v-model="scope.row.materialCode" placeholder="请选择物料编码" :disabled="title === '修改出库计划' && scope.row.id !== null" size="small" @focus="materialComOpen(scope.$index)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="数量" align="center" prop="predictCount" width="180">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'bomDetails.' + scope.$index + '.count'">
                  <el-input v-model="scope.row.count" placeholder="请输入数量" size="small"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" width="100"></el-table-column>
            <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteDetailed(scope.row,scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>
  </div>
</template>

<script>
import { listBom, getBom, delBom, addBom, updateBom } from "@/api/wms/bom";
import store from "@/store";
import { wms } from '@/utils/agent';
import MaterialCom from "../../base/rule/storagePolicy/components/material";
export default {
  name: "Bom",
  components: { MaterialCom },
  data() {
    return {
        // 入库单表格数据
      outDeliveryList: [],
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
      // bom表格数据
      bomList: [],
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
        createBy: null,
        deptId: null,
      },
      detailRules: {
          materialCode: [
              { required: true, message: "请选择物料编码", trigger: "change" },
          ],
          materialName: [
              { required: true, message: "请选择物料名称", trigger: "change" },
          ],
      },
      // 表单参数
      form: {
          bomDetails: []
      },
      // 表单校验
      rules: {
          name: [{ required: true, message: "请输入名称", trigger: "blur" }],
      }
    };
  },
  created() {
    this.getList();
  },
  watch: {
      $route(to, form) {
          if (to.name == "bom") {
              this.getList();
          }
      },
  },
  methods: {
      // 打开选择物料弹窗
      materialComOpen(index) {
          this.$refs.materialCom.open = true;
          this.$refs.materialCom.queryParams.contactsUnitId = this.form.unitCode;
          this.detailIndex = index
      },
      /** 删除明细操作 */
      deleteDetailed(row,index) {
          this.$modal.confirm("是否确认删除此明细").then(() => {
              this.form.bomDetails.splice(index,1);
          }).then(() => {
              this.$modal.msgSuccess("删除成功");
          }).catch(() => { });
      },
      setMaterial(material) {
          let bomDetails = JSON.parse(JSON.stringify(this.form.bomDetails));
          let flag = bomDetails.every((item,index) => {
              if(index != this.detailIndex) {
                  return item.materialId != material.id
              } else {
                  return true
              }
          })
          if(flag){
              bomDetails[this.detailIndex].materialId = material.id ? material.id : "";
              bomDetails[this.detailIndex].materialCode = material.code ? material.code : "";
              bomDetails[this.detailIndex].materialName = material.name ? material.name : "";
              bomDetails[this.detailIndex].unitName = material.unitName ? material.unitName : "";
              this.form.bomDetails = bomDetails;
              this.$forceUpdate()
          } else {
              this.$message.error(`已有【${material.name}】物料在明细中，请重新选择！`)
          }
      },
      /** 新增明细操作 */
      addDetailed() {
          this.form.bomDetails.push({
              materialCode: '',
              materialId: '',
              materialName: '',
              unitId: '',
              unitName: '',
              minUnitName: '',
              predictCount: null,
              // batchCode: null,
              smallPredictCount: null,
          });
      },
    /** 查询bom列表 */
    getList() {
      this.loading = true;
      listBom(this.queryParams).then(response => {
        this.bomList = response.rows;
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
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deptId: null,
        deptName: null,
        delFlag: null,
        bomDetails: []
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
      this.form.deptId = store.getters.deptId;
      this.form.deptName = store.getters.deptName;
      this.form.createBy = store.getters.name;
      this.open = true;
      this.title = "添加bom";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBom(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改bom";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBom(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBom(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除bom编号为"' + ids + '"的数据项？').then(function() {
        return delBom(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/bom/export', {
        ...this.queryParams
      }, `bom_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
