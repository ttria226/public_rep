<template>
  <el-dialog title="查看wcs运行记录" :visible.sync="open" width="60%" append-to-body>
    <!--    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">-->
    <!--      <el-form-item label="任务编号" prop="taskNo">-->
    <!--        <el-input v-model="queryParams.taskNo" placeholder="请输入任务编号" clearable />-->
    <!--      </el-form-item>-->
    <!--      <el-form-item>-->
    <!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
    <!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
    <!--      </el-form-item>-->
    <!--    </el-form>-->
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
      <el-table-column label="开始位置" align="center" prop="startPosition"></el-table-column>
      <el-table-column label="结束位置" align="center" prop="purposePosition"></el-table-column>
      <el-table-column label="接口类型" align="center" prop="interfaceType" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.interfaceType=='1'">发送</span>
          <span v-if="scope.row.interfaceType=='2'">接收</span>
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.status=='0'">失败</span>
          <span v-if="scope.row.status=='1'">成功</span>
        </template>
      </el-table-column>
      <el-table-column label="故障原因" align="center" prop="acceptData" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if = "scope.row.status == '0'">{{scope.row.acceptData}}</span>
        </template>
      </el-table-column>
      <el-table-column label="时间" align="center" prop="createTime" min-width="110"></el-table-column>
      <!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">-->
      <!--        <template slot-scope="scope">-->
      <!--        </template>-->
      <!--      </el-table-column>-->
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
    listTaskWcsRecord
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
        taskWcsId: null,
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
          taskWcsId: null,
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
        this.queryParams.taskWcsId = this.taskWcsId;
        listTaskWcsRecord(this.queryParams).then(response => {
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
