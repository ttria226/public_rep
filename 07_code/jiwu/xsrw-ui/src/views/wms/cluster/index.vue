<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="140px">
      <el-form-item label="集群名称" prop="clusterName">
        <el-input
          v-model="queryParams.clusterName"
          placeholder="请输入集群名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="集群的注册中心ip地址" prop="clusterIp" label-width="220px">
        <el-input
          v-model="queryParams.clusterIp"
          placeholder="请输入集群的注册中心ip地址, 包含http或https,http://xxxxx"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务所在的分组" prop="groupName">
        <el-input
          v-model="queryParams.groupName"
          placeholder="请输入服务所在的分组,默认: DEFAULT_GROUP"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="网关服务名称" prop="serviceGatewayName">
        <el-input
          v-model="queryParams.serviceGatewayName"
          placeholder="请输入网关服务名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主数据系统服务名称" prop="serviceCimsName">
        <el-input
          v-model="queryParams.serviceCimsName"
          placeholder="请输入主数据系统服务名称"
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
          v-hasPermi="['wms:cluster:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleRefresh"
          v-hasPermi="['wms:cluster:detectall']"
        >刷新状态</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wms:cluster:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="clusterList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id"  :show-overflow-tooltip="true"/>
      <el-table-column label="所属组织" align="center" prop="factoryName" :show-overflow-tooltip="true" />
      <el-table-column label="集群名称" align="center" prop="clusterName" :show-overflow-tooltip="true" width="210" />
      <el-table-column label="集群的注册中心ip地址" align="center" prop="clusterIp" :show-overflow-tooltip="true" width="210" />
      <el-table-column label="服务所在的分组" align="center" prop="groupName" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="网关服务名称" align="center" prop="serviceGatewayName" :show-overflow-tooltip="true" />
      <el-table-column label="主数据系统服务名称" align="center" prop="serviceCimsName" :show-overflow-tooltip="true" />
      <el-table-column label="是否主集群" align="center" prop="mainCluster" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.mainCluster == '0'">否</span>
          <span v-if="scope.row.mainCluster == '1'">是</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" fixed="right" width="120px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wms:cluster:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wms:cluster:remove']"
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

    <!-- 添加或修改集群对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-form-item label="所属组织" prop="factory">
          <treeselect v-model="form.factory" :options="deptOptions" :normalizer="normalizer" placeholder="选择所属组织" />
        </el-form-item>
        <el-form-item label="集群名称" prop="clusterName">
          <el-input v-model="form.clusterName" placeholder="请输入集群名称" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="集群的注册中心ip地址" prop="clusterIp">
          <el-input v-model="form.clusterIp" placeholder="请输入集群的注册中心ip地址" maxlength="40" show-word-limit />
        </el-form-item>
        <el-form-item label="服务所在的分组" prop="groupName">
          <el-input v-model="form.groupName" placeholder="请输入服务所在的分组" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="网关服务名称" prop="serviceGatewayName">
          <el-input v-model="form.serviceGatewayName" placeholder="请输入网关服务名称" maxlength="40" show-word-limit />
        </el-form-item>
        <el-form-item label="主数据系统服务名称" prop="serviceCimsName">
          <el-input v-model="form.serviceCimsName" placeholder="请输入主数据系统服务名称" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" maxlength="100" show-word-limit type="textarea" :rows = "5" />
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
import { listCluster, getCluster, delCluster, addCluster, updateCluster, refreshCluster } from "@/api/wms/cluster";

import { Loading } from 'element-ui';
import { listDept } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Cluster",
  components: { Treeselect },
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
      // 集群表格数据
      clusterList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        clusterName: null,
        clusterIp: null,
        groupName: null,
        serviceGatewayName: null,
        serviceCimsName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        factory:[
          { required: true, message: "所属组织不能为空", trigger: "change" }
        ],
        clusterName: [
          { required: true, message: "集群名称不能为空", trigger: "blur" }
        ],
        clusterIp: [
          { required: true, message: "集群的注册中心ip地址不能为空", trigger: "blur" }
        ],
        groupName: [
          { required: true, message: "服务所在的分组不能为空", trigger: "blur" }
        ],
        serviceGatewayName: [
          { required: true, message: "网关服务名称不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "删除标记不能为空", trigger: "blur" }
        ],
      },
      // 部门树选项
      deptOptions: [],
      deptList:[],
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'Cluster') {
        this.getList();
      }
    }
  },
  created() {
    listDept().then(response => {
      this.deptOptions = this.handleTree(response.data, "deptId");
      this.deptList = response.data;
    });
    this.getList();
  },
  methods: {
    /** 查询集群列表 */
    getList() {
      this.loading = true;
      listCluster(this.queryParams).then(response => {
        this.clusterList = response.rows;
        this.total = response.total;
        this.loading = false;
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
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        clusterName: null,
        clusterIp: null,
        groupName: "DEFAULT_GROUP",
        serviceGatewayName: null,
        serviceCimsName: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null,
        remark: null
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
      this.title = "添加集群";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCluster(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改集群";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCluster(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCluster(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleRefresh() {
      const options = {
        text: '正在刷新',
        background: 'rgba(0, 0, 0, 0.8)'
      }
      let loadingInstance = Loading.service(options);
      refreshCluster().then(res => {
        this.getList()
        this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
          loadingInstance.close();
        });
        this.$modal.msgSuccess("刷新成功");
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const names = row.clusterName || this.clusterName;
      this.$modal.confirm('是否确认删除集群名称为"' + names + '"的数据项？').then(function() {
        return delCluster(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wms/cluster/export', {
        ...this.queryParams
      }, `cluster_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
