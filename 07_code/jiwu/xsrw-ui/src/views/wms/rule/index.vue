<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading='addLoading' @click="handleAdd" v-hasPermi="['wms:rule:add']">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ruleList" @selection-change="handleSelectionChange">
      <el-table-column label="上架策略描述" align="center" prop="ruleDesc" :show-overflow-tooltip="true" />
      <el-table-column label="上架策略备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="是否启用" align="center" prop="status" :show-overflow-tooltip="true" :formatter="convertStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:rule:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleDetailed(scope.row)" v-hasPermi="['wms:rule:query']">配置明细</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:rule:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改上架策略对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="是否启用" prop="status">
          <el-select style="width: 100%;" v-model="form.status" placeholder="请选择是否启用" class="select-input-form">
            <el-option
              v-for="dict in status"
              :key="parseInt(dict.dictValue)"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="ruleDesc">
          <el-input v-model="form.ruleDesc" type="textarea" :rows="3" :maxlength="100" show-word-limit placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="5" :maxlength="250" show-word-limit placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 配置明细  -->
    <el-dialog :title="title" :visible.sync="detailedOpen" v-if="detailedOpen" width="1200px" append-to-body>
      <el-form ref="detailForm" :model="form" :rules="rules" disabled :inline="true" label-width="80px">
        <el-form-item label="描述" prop="ruleDesc">
          <el-input v-model="form.ruleDesc" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="是否启用" prop="status">
          <el-select v-model="form.status" placeholder="请选择是否启用">
            <el-option
              v-for="dict in status"
              :key="parseInt(dict.dictValue)"
              :label="dict.dictLabel"
              :value="parseInt(dict.dictValue)">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" :disabled="form && form.putAwayRuleDetailList && form.putAwayRuleDetailList.length > 2" @click="addDetailed" v-hasPermi="['wms:rule:query']">新增行</el-button>
        </el-col>
      </el-row>
      <el-form ref="detailForm" :model="form" :rules="detailRules" lable-width="0">
        <el-form-item prop="putAwayRuleDetailList">
          <el-table v-loading="loading" :data="form.putAwayRuleDetailList">
            <el-table-column label="行号" align="center" prop="ruleOrder" :show-overflow-tooltip="true" width="210">
              <template slot-scope="scope">
                  <el-form-item label-width="0px" :prop="'putAwayRuleDetailList.'+scope.$index+'.ruleOrder'" :rules="detailRules.ruleOrder">
                    <el-input v-model="scope.row.ruleOrder" oninput="value=value.replace(/[^\d]/g,'')" maxlength="6" show-word-limit placeholder="请输入行号" size="small"></el-input>
                  </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="上架规则" align="center" prop="rule" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'putAwayRuleDetailList.'+scope.$index+'.rule'" :rules="detailRules.rule">
                  <el-select v-model="scope.row.rule" placeholder="请选择上架规则">
                    <el-option v-for="dict in dict.type.wms_rule" :key="parseInt(dict.value)" :label="dict.label" :value="parseInt(dict.value)"></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="是否启用" align="center" prop="status" :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <el-select v-model="scope.row.status" placeholder="请选择是否启用">
                  <el-option v-for="dict in status" :key="parseInt(dict.dictValue)" :label="dict.dictLabel" :value="parseInt(dict.dictValue)"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteDetailed(scope.row, scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDetailed">确 定</el-button>
        <el-button @click="detailedCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRule, getRule, delRule, addRule, updateRule, added } from "@/api/wms/rule";
import { treeselect } from "@/api/system/dept";
import { getCode } from "@/api/wms/common";


import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

import { CSJCL } from '@/utils/codeType'

export default {
  name: "Rule",
  dicts: ['wms_rule'],
  components: {
    Treeselect,
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
      // 上架策略表格数据
      ruleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      detailedOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleNo: null,
        ruleDesc: null,
        status: null,
        orgId: null,
        orgName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [
          { required: true, message: "请选择是否启用", trigger: "change" }
        ],
      },
      detailRules: {
        ruleOrder: [
          { required: true, message: "请输入行号", trigger: "blur" }
        ],
        rule: [
          { required: true, message: "请选择上架规则", trigger: "change" }
        ],
      },
      status: [], // 状态源数据
      putAwayRuleDetailList: [], // 配置明细
      addLoading: false, // 新增按钮loading
    };
  },
  created() {
    // 获取状态源数据
    this.getDicts('wms_rule_status').then(response => {
      this.status = response.data
    })
    this.getList();
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'rule') {
        this.getList();
      }
    }
  },
  methods: {
    // 状态翻译
    convertStatus(row, column) {
      const item = this.status.find(item => item.dictValue == row.status)
      return item && item.dictLabel ? item.dictLabel : '--'
    },
    /** 查询上架策略列表 */
    getList() {
      this.loading = true;
      listRule(this.queryParams).then(response => {
        this.ruleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 配置明细取消按钮
    detailedCancel() {
      this.detailedOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        ruleNo: null,
        ruleDesc: null,
        status: 1,
        orgId: null,
        orgName: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null
      };
      this.resetForm("form");
      this.resetForm("detailForm")
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
      this.title = "添加上架策略";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRule(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改上架策略";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRule(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除上架策略为"' + row.ruleDesc + '"的数据项？').then(function() {
        return delRule(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wms/rule/export', {...this.queryParams}, `rule_${new Date().getTime()}.xlsx`)
    },
    // 配置明细提交
    submitDetailed() {
      this.$refs["detailForm"].validate(valid => {
        if (valid) {
          let ruleOrderRepeat = false
          let ruleRepeat = false
          this.form.putAwayRuleDetailList.forEach((item, index) => {
            if (index > 0) {
              if (item.ruleOrder == this.form.putAwayRuleDetailList[0].ruleOrder) {
                ruleOrderRepeat = true
              }
              if (item.rule == this.form.putAwayRuleDetailList[0].rule) {
                ruleRepeat = true
              }
            }
          })
          if (ruleOrderRepeat && ruleRepeat) {
            this.$modal.msgWarning("行号和上架规则不能重复");
          } else if (ruleOrderRepeat) {
              this.$modal.msgWarning("行号不能重复");
          } else if (ruleRepeat) {
              this.$modal.msgWarning("上架规则不能重复");
          } else {
            updateRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.detailedOpen = false;
              this.getList();
            });
          }
        }
      });
    },
    // 打开配置明细
    handleDetailed(row) {
      this.reset();
      const id = row.id || this.ids
      getRule(id).then(response => {
        this.form = response.data;
        this.detailedOpen = true
        this.detailedId = row.id
        this.title = "配置明细";
      });
    },
    /** 新增明细操作 */
    addDetailed(row) {
      this.form.putAwayRuleDetailList.push({
        ruleOrder: null,
        receiptType: null,
        rule: null,
        status: 0,
      })
    },
    /** 删除明细操作 */
    deleteDetailed(row, index) {
      this.$modal.confirm('是否确认删除此明细').then(() => {
        this.form.putAwayRuleDetailList.splice(index, 1)
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
};
</script>
