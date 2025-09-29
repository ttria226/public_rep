<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading='addLoading' @click="handleAdd" v-hasPermi="['wms:rule:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:classes:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['base:classes:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="storagePolicyList" @selection-change="handleSelectionChange">
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="绑定库位" align="center" prop="locationName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="是否可用" align="center" prop="status" :show-overflow-tooltip="true" min-width="120px" >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="1" inactive-value="0" @change="value => handleOpenClose(value,scope.row,scope.$index)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="120" />
      <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:rule:edit']">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:rule:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改存储策略对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料编码" prop="materialCode">
          <el-input v-model="form.materialCode" placeholder="请选择物料" size="small" @focus="materialComOpen"></el-input>
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="form.materialName" disabled size="small"></el-input>
        </el-form-item>
        <el-form-item label="绑定区域" prop="areaId">
          <el-select style="width: 100%;" v-model="form.areaId"  @change="changeReservoirList" placeholder="请选择绑定区域" clearable>
            <el-option v-for="dict in areaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="绑定库区" prop="reservoirId">
          <el-select style="width: 100%;" v-model="form.reservoirId" @change="changeLocationList" placeholder="请选择绑定库区" clearable>
            <el-option v-for="dict in reservoirList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="绑定库位" prop="detailList">
          <el-select style="width: 100%;" v-model="form.detailList" multiple placeholder="请选择绑定库位" clearable>
            <el-option v-for="dict in locationList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
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
import { listPutaway, getPutaway, delPutaway, addPutaway, updatePutaway, updatePutawayStatus } from "@/api/base/putaway";

import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";
import { listLocation } from "@/api/wms/location";

import { wms } from "@/utils/agent";

import MaterialCom from "./components/material";

export default {
  name: "storagePolicy",
  components: { MaterialCom },
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
      // 存储策略表格数据
      storagePolicyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
      },
      // 表单参数
      form: {
        detailList: []
      },
      // 表单校验
      rules: {
        materialCode: [{ required: true, message: "请选择物料", trigger: "change" }],
        areaId: [{ required: true, message: "请选择绑定区域", trigger: "change" }],
        reservoirId: [{ required: true, message: "请选择绑定库区", trigger: "change" }],
        detailList: [{ required: true, type: 'array', message: "请选择绑定库位", trigger: "change" },],
      },

      areaList: [], //区域list
      reservoirList: [], //库区list
      locationList: [], //库位list

      addLoading: false, // 新增按钮loading
    };
  },
  created() {
    this.getAreaList()
    this.getList();
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'storagePolicy') {
        this.getList();
      }
    }
  },
  methods: {
    /** 查询上架策略列表 */
    getList() {
      this.loading = true;
      listPutaway(this.queryParams).then(response => {
        this.storagePolicyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询区域列表 */
    getAreaList() {
      let qps = {
        pageNum: 1,
        pageSize: 5000,
      };
      listArea(qps).then(response => {
        this.areaList = [...response.rows];
      });
    },
    /** 查询库区列表 */
    getReservoirList() {
      let qps = {
        areaId: this.form.areaId,
        pageNum: 1,
        pageSize: 5000,
      };
      listReservoir(qps).then(response => {
        this.reservoirList = response.rows;
      });
    },
    /** 查询库位列表 */
    getLocationList() {
      let qps = {
        areaId: this.form.areaId,
        reservoirId: this.form.reservoirId,
        // locationType: 1,
        pageNum: 1,
        pageSize: 5000,
      };
      listLocation(qps).then(response => {
        this.locationList = response.rows;
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
        materialId: null,
        materialCode: null,
        materialName: null,
        areaId: null,
        reservoirId: null,
        detailList: [],
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
      this.title = "添加存储策略";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPutaway(id).then(response => {
        this.changeReservoirList(response.data.areaId)
        this.changeLocationList(response.data.reservoirId)
        // listLocation({ pageNum: 1, pageSize: 5000, }).then(response => {
        //   this.locationList = response.rows;
        // });
        this.form = Object.assign({id: null, materialId: null, materialCode: null, materialName: null, areaId: null, reservoirId: null, detailList: [],},{...response.data});
        this.open = true;
        this.title = "修改存储策略";
      });
    },
    /** 处理是否可用事件 */
    handleOpenClose(value,row,index){
      let params = {
        id: row.id,
        status: value
      }
      updatePutawayStatus(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess(value === '1' ? "启用成功" : "禁用成功");
          this.getList();
        } else {
          row.status = value === '1' ? '0' : '1'
          this.$set(this.storagePolicyList,index,row)
        }
      }).catch(() => {
        row.status = value === '1' ? '0' : '1'
        this.$set(this.storagePolicyList,index,row)
      })
    },
    // 打开选择物料弹窗
    materialComOpen() {
      this.$refs.materialCom.open = true;
    },
    //选择物料回调
    setMaterial(material) {
      let flag = this.form.materialCode !== material.code
      if(flag){
        this.form.materialId = material.id ? material.id : "";
        this.form.materialCode = material.code ? material.code : "";
        this.form.materialName = material.name ? material.name : "";
      } else {
        this.$message.error(`已有【${material.name}】物料在明细中，请重新选择！`)
      }
    },
    //区域变化事件
    changeReservoirList(val){
      this.form.reservoirId = null
      this.form.detailList = []
      this.reservoirList = []
      this.locationList = []
      if(val){
        this.getReservoirList()
      }
    },
    //库区变化事件
    changeLocationList(val){
      this.form.detailList = []
      this.locationList = []
      if(val){
        this.getLocationList()
      }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePutaway(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPutaway(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除"' + (row.id ? '该条' : '这些') + '"数据项？').then(function() {
        return delPutaway(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms +'/base/putaway/export', {...this.queryParams}, `putaway_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
