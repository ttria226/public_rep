<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="包装编码" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入包装编码" clearable />
      </el-form-item> -->
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading = 'addLoading' @click="handleAdd" v-hasPermi="['wms:unit/config:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:unit/config:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="PackConfigList" @selection-change="handleSelectionChange">
      <el-table-column label="包装单位" align="center" prop="maxUnitName" :show-overflow-tooltip="true" />
      <el-table-column label="物料" align="center" prop="materialName" :show-overflow-tooltip="true" />
      <el-table-column label="换算关系" align="center" prop="unitConfigName" :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_pack_config " :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:unit/config:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:unit/config:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <!-- 添加或修改包装配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body @close='packIndex'>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="form.materialName" :disabled="title == '修改包装配置' " @focus="materialComOpen" placeholder="请输入包装编码"/>
        </el-form-item>
        <el-form-item label="包装单位" prop="unitId">
          <el-select style="width: 100%;" v-model="form.unitId" clearable filterable placeholder="请选择包装单位" @change="qualifyScoreInput">
            <el-option v-for="item in unitList" :key="item.id" disabled :label="item.name" :value="item.id" :disabled="item.disabled"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="换算关系" prop="unitConfig">
          <el-input style="width: 48.5%;" @input="qualifyScoreInput" oninput="value=value.replace(/[^\d]/g,'')" onblur="value=value>0?parseInt(value):''" v-model="form.count" placeholder="请输入换算值"  maxlength="10"/>
          <!-- <el-input style="width: 48.5%;margin-left: 10px;" disabled v-model="form.companyName" placeholder="请输入换算单位"/> -->
          <el-select style="width: 48.5%;margin-left: 10px;" v-model="form.minUnitId" clearable filterable placeholder="请选择基本单位" @change="qualifyScoreInput">
            <el-option v-for="item in minUnitList" :key="item.id" :label="item.name" :value="item.id" :disabled="item.disabled"></el-option>
          </el-select>
        </el-form-item>
       <el-form-item label="状态" prop="status">
          <el-select style="width: 100%;" v-model="form.status" clearable placeholder="请选择状态">
            <el-option v-for="dict in dict.type.wms_t_pack_config" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" show-word-limit maxlength="200"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancels">取 消</el-button>
      </div>
    </el-dialog>
    <MaterialCom ref="materialCom" @setMaterial = "setMaterial"></MaterialCom>
  </div>
</template>

<script>
import { listPackConfig, getPackConfig, delPackConfig, addPackConfig, updatePackConfig } from "@/api/wms/PackConfig";
import { listUnit } from "@/api/wms/unit";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { mapGetters } from 'vuex';
import { wms } from '@/utils/agent';
import MaterialCom from './components/material'
export default {
  name: "PackConfig",
  dicts: ['wms_t_pack_config'],
  components:{
    Treeselect,
    MaterialCom
  },
  computed: {
    ...mapGetters([
      'deptId',
    ]),
  },
  data() {
    let unitConfigRules = (rule, value, callback)=>{
      if(this.form.count && this.form.minUnitId){
        callback();
      } else if(!this.form.count && this.form.minUnitId){
         callback(new Error('请输入换算值'));
      } else if(!this.form.minUnitId && this.form.count){
         callback(new Error('请选择换算单位'));
      } else if(!this.form.minUnitId && !this.form.count){
         callback(new Error('请输入换算值和选择换算单位'));
      }
    }
    let regulationTypeRules = (rule, value, callback) => {
      if(!this.form.status){
        callback(new Error('请选择状态'));
      }else{
        callback();
      }
		}
    return {
      defaultParams: {
        label: 'label',
        value: 'id',
        children: 'children',
        checkStrictly: true
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
      // 包装配置表格数据
      PackConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        unitId: null,
        // code: null,
        status: null,
        factory:null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        materialName:[
          {required: true, message: "物料名称不能为空", trigger: ["change","blur"]}
        ],
        remark:[
        	{required: true, message: "备注不能为空", trigger: "blur"}
        ],
        unitConfig:[
          {required: true, validator: unitConfigRules,trigger:"blur"}
        ],
        unitId:[
          {required: true, message: "包装单位不能为空", trigger: "blur"}
        ],
        status:[
        	{required: true, validator: regulationTypeRules,trigger: "change"}
        ],
      },
      unitConfig : [
        {count:'',minUnitId:''}
      ],
      unitList:[],
      minUnitList: [],
      unitConfigs : [
        {count:'',minUnitId:''}
      ],
      addLoading: false, // 新增按钮loading
    };
  },
  created() {
    this.getList();
    this.listUnit()
  },
  watch: {
    $route(to, form) {
      if (to.name == 'unitConfig') {
        this.getList()
      }
    }
  },
  methods: {
    setMaterial(material) {
      if(material){
        this.form.materialName = material.name
        this.form.materialId = material.id
        this.form.companyName = material.unitName
        this.form.minUnitId = material.unitId
        this.form.minUnitName = material.unitName
        this.form.unitId = material.unitId
        // this.form.unitConfig.push({
        //   count:'',
        //   minUnitId: material.unitId,
        // })
      }else{
        this.form.materialName = ''
      }
      // unitName   单位   name 名称
    },
    // 打开选择物料弹窗
    materialComOpen() {
      this.$refs.materialCom.open = true
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
    maxInput(value){
      this.$forceUpdate()
    },
    // 包装单位
    maxUnitFormat(row, column){
      let courseType = '';
      if(row.unitConfig){
        courseType = row.unitConfig[0].maxUnitId
      }
      return courseType;
    },
    // 单位列表
    listUnit(){
      listUnit({pageNum: 1, pageSize: 5000}).then(res=>{
        this.unitList = JSON.parse(JSON.stringify(res.rows))
        this.minUnitList = JSON.parse(JSON.stringify(res.rows))
      })
    },
    // 刪除換算單位
    conversionDelet(index){
      // if(this.unitConfig.length > 1){
      //   for (var i = 0; i < this.company.length; i++) {
      //     if(this.company[i].id == this.unitConfig[index].minUnitId){
      //       this.company[i].disabled = false
      //     }
      //   }
      //   this.unitConfig.splice(index,1)
      // }else{
      // }
      // this.unitConfigs = JSON.parse(JSON.stringify(this.unitConfig) )
    },
    // 添加换算单位
    conversionAdd(){
      this.unitConfig.push({count:'',minUnitId:''})
      this.unitConfigs = JSON.parse(JSON.stringify(this.unitConfig) )
    },
    /** 查询包装配置列表 */
    getList() {
      this.loading = true;
      listPackConfig(this.queryParams).then(response => {
        this.PackConfigList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancels() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        remark: null,
        code: null,
        status:null,
        unitId:null,
        materialId: null,
        materialName:null,
        maxUnitId: null,
        maxUnitName: null,
        minUnitId: null,
        minUnitName: null,
        companyName:null,
        // unitConfig:[],
      };
      this.unitConfig = [{count:'',minUnitId:''}]
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
      this.queryParams.factory=null
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
      // for (var i = 0; i <  this.minUnitList.length; i++) {
      //   this.company[i].disabled = false
      // }
      this.title = "添加包装配置";
    },
    packIndex(){
      this.unitConfigs = [
        {count:'',minUnitId:''}
      ]
      this.unitConfig = [
        {count:'',minUnitId:''}
      ]
    },
    /** 输入框输入值改变重绘*/
    qualifyScoreInput() {
      this.$forceUpdate()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPackConfig(id).then(response => {
        console.log(response.data)
        this.form = response.data;
        this.form.companyName = this.form.minUnitName
        this.form.unitId = response.data.maxUnitId
        this.form.minUnitId = response.data.minUnitId
        // this.form.count = this.form.unitConfig[0].count
        this.open = true;
        this.title = "修改包装配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      // this.form.unitConfig[0].count = this.form.count
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.maxUnitId = this.form.unitId
          this.unitList.map((item) => {
            if(item.id == this.form.unitId){
              this.form.maxUnitName = item.name
            }
          })
          if (this.form.id != null) {
            updatePackConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPackConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
        return delPackConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/unit/config/export', {...this.queryParams}, `unitConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
