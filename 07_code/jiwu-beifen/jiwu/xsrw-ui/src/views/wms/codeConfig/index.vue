<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="86px">
      <el-form-item label="编码类型" prop="typeCode">
        <el-select v-model="queryParams.typeCode" placeholder="请选择编码类型" clearable>
          <el-option v-for="dict in dict.type.wms_code_type_name" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['wms:config:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:config:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="codeConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="编号" align="center" prop="id" :show-overflow-tooltip="true"/>
      <el-table-column label="编码类型" align="center" prop="type" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_code_type_name" :value="scope.row.typeCode"/>
        </template>
      </el-table-column>
      <el-table-column label="前缀编码" align="center" prop="beforeCode" :show-overflow-tooltip="true"/>
      <el-table-column label="中间日期" align="center" prop="middleDate" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.middleDate == '0' ">无</span>
          <span v-if="scope.row.middleDate == '1' ">日期（年月日）</span>
          <span v-if="scope.row.middleDate == '2' ">日期（年月日时分秒）</span>
          <span v-if="scope.row.middleDate == '3' ">时间戳</span>
        </template>
      </el-table-column>
      <el-table-column label="后缀" align="center" prop="afterNumberType" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.afterNumberType == '0' ">无</span>
          <span v-if="scope.row.afterNumberType == '1' ">自增长</span>
          <span v-if="scope.row.afterNumberType == '2' ">随机数</span>
        </template>
      </el-table-column>
      <el-table-column label="后缀位数" align="center" prop="ruleValue" :show-overflow-tooltip="true"/>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:config:edit']">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改编码配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="padding-right: 20px;">
        <el-form-item label="编码类型" prop="typeCode">
          <el-select style="width: 100%;" v-model="form.typeCode" class="select-input-form" placeholder="请选择编码类型">
            <el-option v-for="dict in dict.type.wms_code_type_name" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="前缀编码" prop="beforeCode">
          <el-input v-model="form.beforeCode" maxlength="25" show-word-limit  placeholder="请输入前缀的规则"/>
        </el-form-item>
        <el-form-item label="中间日期" prop="middleDate" placeholder="请选择中间日期">
          <el-select style="width: 100%;" v-model="form.middleDate" class="select-input-form">
            <el-option v-for="item in middleDateOption" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="后缀" prop="afterNumberType" placeholder="请选择后缀">
          <el-select style="width: 100%;" v-model="form.afterNumberType" class="select-input-form" clearable>
            <el-option v-for="item in afterNumberTypeOption" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="后缀位数" v-if="form.afterNumberType == 1 || form.afterNumberType == 2" prop="ruleValue">
          <el-input-number type="number" class="select-input-form" v-model="form.ruleValue" :min="1" :max="8"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="5" maxlength="250" show-word-limit placeholder="请输入备注"/>
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
  import {listCodeConfig, getCodeConfig, delCodeConfig, addCodeConfig, updateCodeConfig} from "@/api/wms/codeConfig";

  export default {
    name: "CodeConfig",
    dicts: ['wms_code_type_name'],
    data() {
      return {
        middleDateOption:[
          {
            value: '0',
            label: '无'
          },
          {
            value: '1',
            label: '日期（年月日）'
          },
          {
            value: '2',
            label: '日期（年月日时分秒）'
          },
          {
            value: '3',
            label: '时间戳'
          }
        ],
        afterNumberTypeOption:[
          {
            value: '0',
            label: '无'
          },
          {
            value: '1',
            label: '自增长'
          },
          {
            value: '2',
            label: '随机数'
          }
        ],
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        types:[],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 编码配置表格数据
        codeConfigList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          type: null,
          typeCode: null,
          beforeCode: null,
          middleDate: null,
          afterNumberType: null,
          ruleValue: null,
        },
        // 表单参数
        form: {
          middleDate: '0',
          afterNumberType: '0',
        },
        // 表单校验
        rules: {
          typeCode: [
            {required: true, message: "模块名称为空", trigger: "blur"}
          ],
          beforeCode: [
            {required: true, message: "前缀编码不能为空", trigger: "blur"}
          ],
          middleDate: [
            {required: true, message: "中间日期不能为空", trigger: "blur"}
          ],
          afterNumberType: [
            {required: true, message: "后缀不能为空", trigger: "blur"}
          ],
          ruleValue: [
            {required: true, message: "后缀位数不能为空", trigger: "blur"}
          ],
        }
      };
    },
    watch:{
      '$route'(to,form){
        if (to.name == 'codeConfig') {
          this.getList();
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询编码配置列表 */
      getList() {
        this.loading = true;
        listCodeConfig(this.queryParams).then(response => {
          this.codeConfigList = response.rows;
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
          type: null,
          typeCode: null,
          beforeCode: null,
          middleDate: "0",
          afterNumberType: null,
          ruleValue: null,
          currentIndex: null,
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
        this.types = selection.map(item => item.type)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加编码配置";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getCodeConfig(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改编码配置";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateCodeConfig(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addCodeConfig(this.form).then(response => {
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
        const types = row.type || this.types;
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除编码类型为"' + types + '"的数据项？').then(function () {
          return delCodeConfig(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('wms/codeConfig/export', {...this.queryParams}, `codeConfig_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
