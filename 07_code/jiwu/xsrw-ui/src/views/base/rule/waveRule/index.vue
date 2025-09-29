<template>
  <div class="app-container">
    <el-table v-loading="loading" :data="ruleList">
      <el-table-column label="规则名称" align="center" prop="name" :show-overflow-tooltip="true" min-width="200px" />
      <!-- <el-table-column label="仓库" align="center" prop="remark" :show-overflow-tooltip="true" min-width="200px" /> -->
      <el-table-column label="是否启用" align="center" prop="flag" :show-overflow-tooltip="true" min-width="120px" >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.flag" active-value="1" inactive-value="0" @change="value => handleOpenClose(value,scope.row,scope.$index)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" min-width="150px" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" min-width="300px" />
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listRule, updateRuleStatus } from "@/api/base/rule";


export default {
  name: "waveRule",
  dicts: ['wms_rule'],
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
      // 有效期规则表格数据
      ruleList: [],
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleModule: null,
      },
      status: [], //是否启用列表
    };
  },
  created() {
    this.getList();
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'waveRule') {
        this.getList();
      }
    }
  },
  methods: {
    /** 查询上架策略列表 */
    getList() {
      this.loading = true;
      this.queryParams.ruleModule = '5'
      listRule(this.queryParams).then(response => {
        response.rows.map((item) => {
          if(item.flag === null){
            item.flag = '0'
          }
        })
        this.ruleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    /** 处理是否启用事件 */
    handleOpenClose(value,row,index){
      let params = {
        id: row.id,
        flag: value
      }
      updateRuleStatus(params).then(res => {
        if(res.code === 200){
          this.$modal.msgSuccess(value === '1' ? "启用成功" : "禁用成功");
          this.getList();
        } else {
          row.flag = value === '1' ? '0' : '1'
          this.$set(this.ruleList,index,row)
        }
      }).catch(() => {
        row.flag = value === '1' ? '0' : '1'
        this.$set(this.ruleList,index,row)
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wms/rule/export', {...this.queryParams}, `rule_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
