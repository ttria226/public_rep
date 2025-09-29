<template>
  <div class="app-container" style="height: 100%;">

    <div class="cluster" v-if="clusterList && clusterList.length > 0">
      <div class="cluster-item" v-for="(item, index) in clusterList" :key="index">
        <span class="cluster-name">{{item.clusterName}}</span>
        <img class="cluster-img" alt="正常" v-if="item.state.status && item.state.status == 'ON'" src="../../../assets/wms/wms-network-normal.png" />
        <img class="cluster-img" v-else alt="中断" src="../../../assets/wms/wms-network-interruption.png" />
        <div class="cluster-state">
          <span class="cluster-state-tit">网络通信状态：</span>
          <span class="cluster-state-cont" :class="{active: item.state.status == 'ON'}">{{item.state.status == "ON" ? "正常" : "中断"}}</span>
        </div>
      </div>
    </div>

    <div v-else class="no-data">
      <div class="no-data-img">
        <div class="no-data-tit">暂无数据</div>
        <div class="no-data-cont">请在仓库管理中添加集群信息</div>
      </div>
    </div>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改远程仓库集群对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="远程仓库集群名称" prop="clusterName">
          <el-input v-model="form.clusterName" placeholder="请输入远程仓库集群名称" />
        </el-form-item>
        <el-form-item label="远程仓库集群的注册中心ip地址, 包含http或https,http://xxxxx" prop="clusterIp">
          <el-input v-model="form.clusterIp" placeholder="请输入远程仓库集群的注册中心ip地址, 包含http或https,http://xxxxx" />
        </el-form-item>
        <el-form-item label="服务所在的分组,默认: DEFAULT_GROUP" prop="groupName">
          <el-input v-model="form.groupName" placeholder="请输入服务所在的分组,默认: DEFAULT_GROUP" />
        </el-form-item>
        <el-form-item label="网关服务名称" prop="serviceGatewayName">
          <el-input v-model="form.serviceGatewayName" placeholder="请输入网关服务名称" />
        </el-form-item>
        <el-form-item label="主数据系统服务名称" prop="serviceCimsName">
          <el-input v-model="form.serviceCimsName" placeholder="请输入主数据系统服务名称" />
        </el-form-item>
        <el-form-item label="删除标记" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标记" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listCluster, getCluster, delCluster, addCluster, updateCluster } from "@/api/wms/cluster";

export default {
  name: "WmsIndex",
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
      // 远程仓库集群表格数据
      clusterList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 8,
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
        clusterName: [
          { required: true, message: "远程仓库集群名称不能为空", trigger: "blur" }
        ],
        clusterIp: [
          { required: true, message: "远程仓库集群的注册中心ip地址, 包含http或https,http://xxxxx不能为空", trigger: "blur" }
        ],
        groupName: [
          { required: true, message: "服务所在的分组,默认: DEFAULT_GROUP不能为空", trigger: "blur" }
        ],
        serviceGatewayName: [
          { required: true, message: "网关服务名称不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "删除标记不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询远程仓库集群列表 */
    getList() {
      this.loading = true;
      listCluster(this.queryParams).then(response => {
        this.clusterList = response.rows;
        this.clusterList.forEach(data =>{
            if(!data.state){
                let param = {
                    // refreshTime: "2022-06-16 17:41:56",
                    status: "OFF",
                }
                data.state = param
            }
        })
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
        clusterName: null,
        clusterIp: null,
        groupName: null,
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
      this.title = "添加远程仓库集群";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCluster(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改远程仓库集群";
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
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

<style lang="scss">
  .cluster{
    display: flex;
    .cluster-item{
      margin-top: 1.25rem;
      margin-right: 3%;
      width: 20%;
      display: flex;
      flex-direction: column;
      align-items: center;
      .cluster-img{
        margin-top: 1.875rem;
        width: 6.25rem;
        // height: 6.25rem;
        object-fit: fill;
      }
      .cluster-state{
        margin-top: 1.875rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        .cluster-state-cont{
          margin-top: 0.625rem;
          font-size: 1.25rem;
          font-weight: bold;
          color: crimson;
          &.active{
            color: aquamarine;
          }
        }
      }
    }
  }
  .no-data{
    padding-top: 8rem;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    .no-data-img{
      width: 379px;
      height: 344px;
      background: url("../../../assets/wms/no-data.png");
      background-size: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      .no-data-tit{
        margin-top: 180px;
        font-size: 14px;
        font-weight: 400;
        color: #18191F;
      }
      .no-data-cont{
        margin-top: 10px;
        font-size: 12px;
        font-weight: 400;
        color: #969BAB;
      }
    }
  }
</style>
