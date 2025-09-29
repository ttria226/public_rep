<template>
  <el-dialog title="查看任务记录" :visible.sync="open" width="70%" append-to-body>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="任务编号" prop="taskNo">
        <el-input v-model="queryParams.taskNo" placeholder="请输入任务编号" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="taskList">
      <el-table-column type="index" label="序号" width="50"/>
      <el-table-column label="任务类型" align="center" prop="taskType" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_task_type" :value="scope.row.taskType" />
        </template>
      </el-table-column>
      <el-table-column label="载具编号" align="center" prop="trayCode" width="140"></el-table-column>
      <el-table-column label="任务编号" align="center" prop="taskNo" width="160"></el-table-column>
      <el-table-column label="开始位置" align="center" prop="startPosition">
        <template slot-scope="scope">
          <span v-if="scope.row.taskType=='1'">输送线</span>
          <span v-else>{{scope.row.startPosition}}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束位置" align="center" prop="purposePosition">
        <template slot-scope="scope">
          <span v-if="scope.row.taskType=='2'">输送线</span>
          <span v-else>{{scope.row.purposePosition}}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行状态" align="center" prop="taskStatus" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
<!--      <el-table-column label="接口类型" align="center" prop="interfaceType" width="60">-->
<!--        <template slot-scope="scope">-->
<!--          <span v-if="scope.row.interfaceType=='1'">发送</span>-->
<!--          <span v-if="scope.row.interfaceType=='2'">接收</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="时间" align="center" prop="createTime" width="150"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-s-order" @click="showDetail(scope.row)">运行记录</el-button>
          <el-button size="mini" type="text" icon="el-icon-refresh"
            v-if="scope.row.taskStatus == '4' || scope.row.taskStatus == '1'"
            @click="handleExecuteWcs(scope.row)">重新发送</el-button>
          <el-button size="mini" type="text" icon="el-icon-circle-check"
                     v-if="(scope.row.taskType == '1' || scope.row.taskType == '5') && (scope.row.taskStatus != '3' && scope.row.taskStatus != null)" @click="handleExecute(scope.row,3)">强制完成</el-button>
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
    listTaskRecord
  } from "@/api/inoutDelivery/taskWcsRecord";

  import { enforceDelivery, enforcementDelivery } from "@/api/inoutDelivery/inDelivery";
  import { executeMoveDoubleEx } from "@/api/inoutDelivery/outDelivery";



  export default {
    name: "runDetailCom",
    dicts: ["wcs_task_type", "wcs_excute_status"],
    data() {
      return {
        //弹窗标识
        open: false,
        mainTaskNo: null,
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
          mainTaskNo: null,
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
      /** 查询详情 */
      getList() {
        this.queryParams.mainTaskNo = this.mainTaskNo;
        listTaskRecord(this.queryParams).then(response => {
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
      //执行方法
      handleExecute(row){
        let param = {
          id: row.taskWcsId,
        }
        let taskType = row.taskType;
        this.$modal.confirm("确定要强制执行该任务？").then(() => {
          if(taskType == '1'){
            return enforcementDelivery(param);
          }else if(taskType == '2'){

          }else if(taskType == '5'){
            return executeMoveDoubleEx(param);
          }
        }).then(() => {
          this.$modal.msgSuccess("执行成功");
          this.getList();
        }).catch(() => { });
      },
      cancel() {
        this.open = false;
        this.$emit("closeTask",{})
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

      // 查看记录详情
      showDetail(item){
        console.log(item,23)
        this.$emit("showRunTask",{ taskWcsId: item.taskWcsId})
      },
    }
  }
</script>
