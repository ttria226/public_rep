<template>
  <el-dialog title="查看wcs运行记录" :visible.sync="open" width="70%" append-to-body>
    <el-table :data="taskList">
      <el-table-column type="index" label="序号" width="50"/>
      <el-table-column label="任务类型" align="center" prop="taskType" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_task_type" :value="scope.row.taskType" />
        </template>
      </el-table-column>
      <!--          <el-table-column label="载具编号" align="center" prop="trayCode" min-width="110"></el-table-column>-->
      <el-table-column label="设备" align="center" prop="wcsType" width="100"></el-table-column>
<!--      <el-table-column label="任务编号" align="center" prop="taskNo" min-width="120"></el-table-column>-->
      <el-table-column label="开始位置" align="center" prop="startPosition">
        <template slot-scope="scope">
          <span v-if="scope.row.taskType=='1' || scope.row.taskType=='4'">输送线</span>
          <span v-else>{{scope.row.startPosition}}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束位置" align="center" prop="purposePosition">
        <template slot-scope="scope">
          <span v-if="scope.row.taskType=='2' || scope.row.taskType=='6'">输送线</span>
          <span v-else>{{scope.row.purposePosition}}</span>
        </template>
      </el-table-column>
      <el-table-column label="接口类型" align="center" prop="interfaceType" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.interfaceType=='1'">发送</span>
          <span v-if="scope.row.interfaceType=='2'">接收</span>
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="taskStatus" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
      <el-table-column label="故障原因" align="center" prop="acceptData" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if = "scope.row.status == '0'">{{scope.row.acceptData}}</span>
        </template>
      </el-table-column>
      <el-table-column label="时间" align="center" prop="createTime" min-width="110"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-s-tools" v-if="scope.row.taskStatus != '3' && (scope.row.taskType == '6' || scope.row.taskType == '4')" @click="executeTray(scope.row)">强制完成</el-button>
          <el-button size="mini" type="text" icon="el-icon-refresh" v-if="scope.row.taskStatus == '4' || scope.row.taskStatus == '1'"  @click="handleExecuteWcs(scope.row)">重新发送</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel">关 闭</el-button>
    </div>
  </el-dialog>
</template>
<script>
  import {
    getListByTray,executeTray
  } from "@/api/inoutDelivery/taskWcsRecord";
  import {
    enforceDelivery
  } from "@/api/inoutDelivery/inDelivery";


  export default {
    name: "wcsDetailCom",
    dicts: ["wcs_task_type", "wcs_excute_status"],
    data() {
      return {
        //弹窗标识
        open: false,
        trayId: null,
        //详情表单
        detailForm: {},
        // 表单校验
        rules: {},
        taskList: [],
        // 总条数
        total: 0,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          trayId: null,
        },
      }
    },
    watch: {
      open() {
        if (this.open) {
          this.getList();
        }
      },
    },
    methods: {
      // 载具出入库强制执行
      executeTray(row){
        let param = {
          id: row.taskWcsId
        }
        this.$modal.confirm("确定要重新发送该任务调用设备？").then(() => {
          return executeTray(param)
        }).then(() => {
          this.$modal.msgSuccess("发送成功");
          this.getList();
        }).catch(() => {});
      },

      /** 查询详情 */
      getList() {
        this.queryParams.trayId = this.trayId;
        getListByTray(this.queryParams).then(response => {
          this.taskList = response.rows;
          this.total = response.total;
        });
      },

      //重新发送命令
      handleExecuteWcs(row) {
        let param = {
          id: row.taskWcsId
        }
        this.$modal.confirm("确定要重新发送该任务调用设备？").then(() => {
          return enforceDelivery(param)
        }).then(() => {
          this.$modal.msgSuccess("发送成功");
          this.getList();
        }).catch(() => {});
      },
      cancel() {
        this.open = false
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
    }
  }
</script>
