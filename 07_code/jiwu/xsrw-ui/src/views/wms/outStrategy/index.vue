<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:strategy:export']">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="outStrategyList">
      <el-table-column label="拣货策略名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="是否启用" align="center" prop="flag" :show-overflow-tooltip="true" >
        <template slot-scope="scope">
          <span>{{scope.row.flag == '0' ? '否' :'是'}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-unlock" @click="handleUpdate(scope.row)" v-hasPermi="['wms:strategy:edit']" v-if="scope.row.flag === '0'">启用</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { listOutStrategy, updateOutStrategy } from "@/api/wms/outStrategy";

import {wms} from '@/utils/agent'

export default {
  name: "OutStrategy",
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
      // 拣货策略表格数据
      outStrategyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  watch: {
    $route(to, form) {
      if (to.name == 'outStrategy') {
        this.getList()
      }
    }
  },
  methods: {
    /** 查询拣货策略列表 */
    getList() {
      this.loading = true;
      listOutStrategy(this.queryParams).then(response => {
        this.outStrategyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 启用按钮操作 */
    handleUpdate(row) {
      this.$modal.confirm('是否确认启用拣货策略为"' + row.name + '"的数据项？').then(function() {
        let date = {
          id:row.id
        }
        return updateOutStrategy(date);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("启用成功");
      }).catch(() => {});

    },

    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/outStrategy/export', {...this.queryParams}, `outStrategy_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
