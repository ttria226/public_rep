<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="物料规格" prop="specifications">
        <el-input
          v-model="queryParams.specifications"
          placeholder="请输入物料规格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="移库编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入移库编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="所属部门" prop="factory">
        <el-input
          v-model="queryParams.factory"
          placeholder="请输入所属部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="转出库位ID" prop="locationOutId">
        <el-input
          v-model="queryParams.locationOutId"
          placeholder="请输入转出库位ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="转入库位ID" prop="locationInId">
        <el-input
          v-model="queryParams.locationInId"
          placeholder="请输入转入库位ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="确认状态 0未确认 1已确认" prop="auditorStatus">
        <el-select v-model="queryParams.auditorStatus" placeholder="请选择确认状态 0未确认 1已确认" clearable>
          <el-option
            v-for="dict in dict.type.auditor_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item> -->
      <!-- <el-form-item label="确认人" prop="auditor">
        <el-input
          v-model="queryParams.auditor"
          placeholder="请输入确认人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <!-- <el-form-item label="确认人名称" prop="auditorName">
        <el-input
          v-model="queryParams.auditorName"
          placeholder="请输入确认人名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['stock:moveLibrary:add']"
        >新增</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['stock:moveLibrary:edit']"
        >修改</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          :disabled="single"
          @click="handelMoveLibrary"
          v-hasPermi="['stock:moveLibrary:shift']"
        >生成移库任务</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['stock:moveLibrary:remove']"
        >删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:moveLibrary:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="moveLibraryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="调拨类型" align="center" :show-overflow-tooltip="true" >
        <template slot-scope>
          <div>移库</div>
        </template>
      </el-table-column>
      <!-- <el-table-column label="主键" align="center" prop="id"  :show-overflow-tooltip="true"/> -->
      <el-table-column label="单据编码" align="center" prop="code" :show-overflow-tooltip="true" width="230" />
      <!-- <el-table-column label="所属部门" align="center" prop="factory" :show-overflow-tooltip="true" width="200" /> -->
      <!-- <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="确认时间" align="center" prop="auditor" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="转出仓库" align="center" prop="warehouseName" :show-overflow-tooltip="true" width="200" /> -->
      <el-table-column label="转出区域" align="center" prop="areaName" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="转出库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="转出库位" align="center" prop="locationOutName" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="转入库位" align="center" prop="locationInName" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="确认状态" align="center" prop="auditorStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.auditor_status" :value="scope.row.auditorStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="确认人" align="center" prop="auditorName" :show-overflow-tooltip="true" />
      <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="制单日期" align="center" prop="createTime" :show-overflow-tooltip="true" width="180" />
      <!-- <el-table-column label="确认人名称" align="center" prop="auditorName" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" /> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220" fixed="right">
        <template slot-scope="scope">
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['stock:moveLibrary:edit']"
          >修改</el-button> -->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-checked"
            v-if="scope.row.auditorStatus == '0'"
            @click="handleAudit(scope.row)"
            v-hasPermi="['stock:moveLibrary:auditor']"
          >确认</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDetail(scope.row)"
            v-hasPermi="['stock:moveLibrary:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.auditorStatus == '0'"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:moveLibrary:remove']"
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

    <!-- 详情 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" disabled :inline="true" :rules="rules" label-width="80px">
        <el-form-item label="调拨类型" prop="code">
          <el-input value = "移库" placeholder="" />
        </el-form-item>
        <el-form-item label="单据编码" prop="code">
          <el-input v-model="form.code" placeholder="" />
        </el-form-item>
        <el-form-item label="所属组织" prop="factory">
          <el-input v-model="form.deptName" placeholder="" />
        </el-form-item>
        <!-- <el-form-item label="转出仓库" prop="warehouseName">
          <el-input v-model="form.warehouseName" placeholder="" />
        </el-form-item> -->
        <el-form-item label="转出区域" prop="areaName">
          <el-input v-model="form.areaName" placeholder="" />
        </el-form-item>
        <el-form-item label="转出库区" prop="reservoirName">
          <el-input v-model="form.reservoirName" placeholder="" />
        </el-form-item>
        <el-form-item label="转出库位" prop="locationOutName">
          <el-input v-model="form.locationOutName" placeholder="" />
        </el-form-item>
        <el-form-item label="转入库位" prop="locationInName">
          <el-input v-model="form.locationInName" placeholder="" />
        </el-form-item>
        <el-form-item label="确认状态" prop="auditorStatus">
          <el-select v-model="form.auditorStatus" placeholder="">
            <el-option
              v-for="dict in dict.type.auditor_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="确认人" prop="auditorName">
          <el-input v-model="form.auditorName" placeholder="" />
        </el-form-item>
        <el-form-item label="制单人" prop="createBy">
          <el-input v-model="form.createBy" placeholder="" />
        </el-form-item>
        <el-form-item label="制单日期" prop="createTime">
          <el-input v-model="form.createTime" placeholder="" />
        </el-form-item>
        <!-- <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item> -->
        <!-- <el-form-item label="删除(0:未删除 1:删除)" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除(0:未删除 1:删除)" />
        </el-form-item> -->
      </el-form>

      <el-table :data="form.moveLibraryDetailVoList">
        <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" width="220" />
        <el-table-column label="所属组织" align="center" prop="deptName" :show-overflow-tooltip="true" width="200" />
        <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" />
        <el-table-column label="规格型号" align="center" prop="specifications" :show-overflow-tooltip="true" />
        <el-table-column label="数量" align="center" prop="count" :show-overflow-tooltip="true" />
        <el-table-column label="单位" align="center" prop="unitName" :show-overflow-tooltip="true" />
        <el-table-column label="批号" align="center" prop="batchCode" :show-overflow-tooltip="true" width="260" />
        <!-- <el-table-column label="条码号" align="center" prop="createBy" :show-overflow-tooltip="true" /> -->
      </el-table>

      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="submitForm">确 定</el-button> -->
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMoveLibrary, getMoveLibrary, delMoveLibrary, addMoveLibrary, updateMoveLibrary, auditMoveLibrary, moveLibrary } from "@/api/wms/moveLibrary";

export default {
  name: "MoveLibrary",
  dicts: ['auditor_status'],
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
      // 库内移位表格数据
      moveLibraryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        factory: null,
        locationOutId: null,
        locationInId: null,
        status: null,
        auditorStatus: null,
        auditor: null,
        auditorName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'MoveLibrary') {
        this.getList();
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询库内移位列表 */
    getList() {
      this.loading = true;
      listMoveLibrary(this.queryParams).then(response => {
        this.moveLibraryList = response.rows;
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
        factory: null,
        locationOutId: null,
        locationInId: null,
        status: "0",
        auditorStatus: null,
        auditor: null,
        auditorName: null,
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
      this.title = "添加库内移位";
    },
    /** 修改按钮操作 */
    handleDetail(row) {
      this.reset();
      const id = row.id || this.ids
      getMoveLibrary(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMoveLibrary(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMoveLibrary(this.form).then(response => {
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
      let tips = ''
      if (row.id) {
        tips = '是否确认删除该条数据？'
      } else {
        let codes = []
        this.moveLibraryList.forEach(item => {
          if (this.ids.indexOf(item.id) > -1) {
            codes.push(item.code)
          }
        })
        tips = '是否确认删除这些数据？'
      }
      this.$modal.confirm(tips).then(function() {
        return delMoveLibrary(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wms/moveLibrary/export', {
        ...this.queryParams
      }, `moveLibrary_${new Date().getTime()}.xlsx`)
    },
    // 确认按钮操作
    handleAudit(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认通过'+ (row.id ? '该条' : '这些') +'数据的确认？').then(function() {
        return auditMoveLibrary(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("确认成功");
      }).catch(() => {});
    },
    // 生成移库任务
    handelMoveLibrary() {
      const ids = this.ids[0];
      const item = this.moveLibraryList.find(item => item.id == ids)
      if (item.auditorStatus != '1') {
        this.$modal.msgError("未确认的任务无法移库");
      } else {
        this.$modal.confirm('是否确认生成库内移位编号为"' + item.code + '"的移库任务？').then(function() {
          return moveLibrary(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("操作成功");
        }).catch(() => {});
      }
    }
  }
};
</script>
