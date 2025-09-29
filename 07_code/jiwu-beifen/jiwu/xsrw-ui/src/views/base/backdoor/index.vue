<template>
  <div class="app-container">
    <el-divider></el-divider>
    <div class="item">
      <p>删除单据</p>
      <el-row :gutter="20" class="row-item mb8">
        <el-col :span="2">
          <span>入库:</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="form.codeIn" placeholder="请输入单据编号"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" size="mini" @click="handleDelete(1)">删除</el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="row-item mb8">
        <el-col :span="2">
          <span>出库:</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="form.codeOut" placeholder="请输入单据编号"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" size="mini" @click="handleDelete(2)">删除</el-button>
        </el-col>
      </el-row>
    </div>
    <el-divider></el-divider>
    <div class="item">
      <p>删除打印配置</p>
      <el-row :gutter="20" class="row-item mb8">
        <el-col :span="2">
          <span>入库单号:</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="printForm.advanceDeliveryCode" placeholder="请输入入库单号"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" size="mini" @click="handleQuery2()">搜索</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="printList">
        <el-table-column label="预约单据号" align="center" prop="advanceDeliveryCode" width="150" :show-overflow-tooltip="true"/>
        <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true"/>
        <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true"/>
        <el-table-column label="批次号" align="center" prop="batchCode" />
        <el-table-column label="打印数" align="center" width="100" prop="sumCount" :show-overflow-tooltip="true"/>
        <el-table-column label="RFID单位" align="center" width="80" prop="maxUnitName" />
        <el-table-column label="换算数量" align="center" width="80" prop="convertCount" />
        <el-table-column label="计量单位" align="center" width="80" prop="minUnitName" />
        <el-table-column label="打印次数" align="center" width="80" prop="printCount" />
        <el-table-column label="操作" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-sold-out"
                       @click="handleDelete2(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="printForm.total>0" :total="printForm.total" :page.sync="printForm.pageNum"
                  :limit.sync="printForm.pageSize" @pagination="handleQuery2"/>
    </div>
    <el-divider></el-divider>
    <div class="item">
      <p>载具表库位解绑</p>
      <el-row :gutter="20" class="row-item mb8">
        <el-col :span="2">
          <span>载具编号:</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="form.codeTray" placeholder="请输入载具编号"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" size="mini" @click="handleQuery()">搜索</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="trayList">
        <el-table-column label="载具编号" width="150" align="center" prop="code" :show-overflow-tooltip="true" />
        <el-table-column label="载具类型" width="80" align="center" prop="trayCategory" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wms_t_tray_category " :value="scope.row.trayCategory" />
          </template>
        </el-table-column>
        <el-table-column label="载具状态" align="center" prop="status" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wms_t_tray " :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="180" />
        <el-table-column label="库位状态" align="center" prop="goodsAllocationStatus" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wms_goods_allocation_status" :value="scope.row.goodsAllocationStatus" />
          </template>
        </el-table-column>
        <el-table-column label="自重（kg）" width="120" align="center" prop="weight" :show-overflow-tooltip="true" />
        <el-table-column label="创建人" align="center" prop="createBy" :show-overflow-tooltip="true" />
        <el-table-column label="创建日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-sold-out"
              v-if="scope.row.locationId && (scope.row.palletNum === null || scope.row.palletNum === '')"
              @click="handleRelieve(scope.row)">载具解绑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-divider></el-divider>
    <div class="item">
      <p>wcs任务执行记录</p>
      <el-row :gutter="20" class="row-item mb8">
        <el-col :span="2">
          <span>载具编号:</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="taskForm.trayCode" placeholder="请输入载具编号" clearable></el-input>
        </el-col>
        <el-col :span="2">
          <span>任务状态:</span>
        </el-col>
        <el-col :span="4">
          <el-select v-model="taskForm.taskStatus" clearable placeholder="请选择状态">
            <el-option v-for="dict in dict.type.wcs_excute_status" :key="dict.value" :label="dict.label" :value="dict.value" clearable/>
          </el-select>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" size="mini" @click="handleQuery3()">搜索</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="taskList">
        <el-table-column label="任务类型" align="center" prop="taskType" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wcs_task_type" :value="scope.row.taskType" />
          </template>
        </el-table-column>
        <el-table-column label="任务编号" align="center" prop="taskNo" :show-overflow-tooltip="true" width="250" />
        <el-table-column label="载具编号" align="center" prop="trayCode" :show-overflow-tooltip="true" width="200" />
        <el-table-column label="目标库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="200" />
        <el-table-column label="伸位" align="center" prop="extentionType">
          <template slot-scope="scope">
            <span v-if="scope.row.extentionType == 2">{{scope.row.extentionType}}伸位优先</span>
            <span v-else-if="scope.row.extentionType">{{scope.row.extentionType}}伸位</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="taskStatus" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.taskStatus" />
          </template>
        </el-table-column>
        <el-table-column label="执行人" align="center" prop="createBy" :show-overflow-tooltip="true" />
        <el-table-column label="执行时间" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-refresh"
                       v-if="scope.row.taskStatus == '4' || scope.row.taskStatus == '1'"
                       @click="handleExecuteWcs(scope.row)">重新发送</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="taskForm.total>0" :total="taskForm.total" :page.sync="taskForm.pageNum"
                  :limit.sync="taskForm.pageSize" @pagination="handleQuery3"/>
    </div>
    <el-divider></el-divider>
    <div class="item">
      <p>redis更新</p>
      <el-row :gutter="20" class="row-item mb8">
        <el-col :span="2">
          <span>批次号:</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="form.batchCode" placeholder="请输入批次号"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="warning" size="mini" @click="handleRfIdUpdate()">更新</el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="row-item mb8">
        <el-col :span="2">
          <span>批次号:</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="form.batchCode2" placeholder="请输入批次号"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" size="mini" @click="handleRfIdQuery()">搜索</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="loading" :data="rfIdList">
        <el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true"/>
        <el-table-column label="物料id" align="center" prop="materialId" :show-overflow-tooltip="true"/>
        <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true"/>
        <el-table-column label="rfid" align="center" prop="rfid" :show-overflow-tooltip="true"/>
        <el-table-column label="数量" align="center" prop="count" :show-overflow-tooltip="true"/>
        <el-table-column label="rfid组" align="center" prop="rfids" :show-overflow-tooltip="true"/>
      </el-table>
    </div>

    <el-divider></el-divider>

  </div>
</template>

<script>
  import {
    delInDeliveryByCode,
    listDeliveryExecuteA,
    enforceDelivery
  } from "@/api/inoutDelivery/inDelivery";
  import {
    delOutDeliveryByCode
  } from "@/api/inoutDelivery/outDelivery";
  import {
    listTray,
    relieveLocation
  } from "@/api/wms/Tray";
  import {
    listPrint,
    deleteByDeliveryId
  } from "@/api/base/print";

  import { listDetail2, listDetail3 } from "@/api/wms/detail";

  export default {
    name: "Backdoor",
    dicts: ['wms_t_tray', 'wms_t_tray_category', 'wms_goods_allocation_status','wcs_excute_status','wcs_task_type'],
    data() {
      return {
        // 遮罩层
        loading: false,
        // 选中数组
        ids: [],
        names: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 单位表格数据
        unitList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: null,
        },
        // 表单参数
        form: {
          codeIn: null,
          codeOut: null,
          codeTray: null,
          batchCode: null,
          batchCode2: null,
        },

        // 表单校验
        rules: {},
        trayList: [],//载具列表
        rfIdList:[],//物料详情列表
        printList:[],//打印配置列表
        taskList:[],//设备任务表
        printForm:{
          pageNum: 1,
          pageSize: 10,
          total:0,
          advanceDeliveryCode:null,
        },
        taskForm:{
          pageNum: 1,
          pageSize: 10,
          total:0,
          taskNo: null,
          trayCode: null,
          taskType: null,
          taskStatus: '2',
        }

      };
    },
    watch: {

    },
    created() {},
    methods: {

      //查询载具列表
      handleQuery() {
        if(!this.form.codeTray){
          this.$modal.msgError("请先输入载具编号");
          return;
        }
        listTray({
          code: this.form.codeTray
        }).then(response => {
          this.trayList = response.rows;
        });
      },
      //解绑库位-载具表
      handleRelieve(row) {
        this.$modal.confirm("是否确认要解除【" + row.code + "】的库位绑定关系？").then(() => {
          return relieveLocation({
            id: row.id
          });
        }).then(() => {
          this.$modal.msgSuccess("操作成功");
          this.handleQuery();
        }).catch(() => {});
      },
      //删除单据
      handleDelete(type) {
        if (type == 1) {
          if(!this.form.codeIn){
            this.$modal.msgError("请先填写入库单号");
            return;
          }
          this.$modal.confirm("是否确认删除单号【" + this.form.codeIn + "】的入库单？").then(() => {
            return delInDeliveryByCode({
              code: this.form.codeIn
            });
          }).then(() => {
            this.$modal.msgSuccess("删除成功");
            this.form.codeIn = null;
          }).catch(() => {});
        } else {
          if(!this.form.codeOut){
            this.$modal.msgError("请先填写出库单号");
            return;
          }
          this.$modal.confirm("是否确认删除单号【" + this.form.codeOut + "】的出库单？").then(() => {
            return delOutDeliveryByCode({
              code: this.form.codeOut
            });
          }).then(() => {
            this.$modal.msgSuccess("删除成功");
            this.form.codeOut = null;
          }).catch(() => {});
        }
      },
      //删除打印配置删除
      handleQuery2(){
        if(!this.printForm.advanceDeliveryCode){
          this.$modal.msgError("请先输入入库单号");
          return;
        }
        listPrint(this.printForm).then(response => {
          this.printList = response.rows;
          this.printForm.total = response.total;
        });
      },
      //打印配置删除
      handleDelete2(row){
        this.$modal.confirm("是否确认删除单据【"+row.advanceDeliveryCode+"】中物料[" + row.materialName + "]的所有打印配置信息，如该物料有多条打印配置信息，将同步删除？").then(() => {
          return deleteByDeliveryId({
            advanceRegistrationId: row.advanceRegistrationId
          });
        }).then(() => {
          this.$modal.msgSuccess("删除成功");
          this.handleQuery2();
        }).catch(() => {});
      },
      //rfId-更新数据
      handleRfIdUpdate() {
        if(!this.form.batchCode){
          this.$modal.msgError("请先输入批次号");
          return;
        }
        this.$modal.confirm("是否确认要更新【" + this.form.batchCode + "】的redis数据？").then(() => {
          return listDetail2({
            batchCode: this.form.batchCode
          });
        }).then(() => {
          this.$modal.msgSuccess("操作成功");
        }).catch(() => {});
      },

      //rfId-查询rfId-redis列表
      handleRfIdQuery() {
        if(!this.form.batchCode2){
          this.$modal.msgError("请先输入批次号");
          return;
        }
        listDetail3({
          batchCode: this.form.batchCode2
        }).then(response => {
          this.rfIdList = response.rows;
        });
      },

      //任务列表
      handleQuery3(){
        listDeliveryExecuteA(this.taskForm).then(response => {
          this.taskList = response.rows;
          this.taskForm.total = response.total;
        });
      },
      //重新发送命令
      handleExecuteWcs(row) {
        let param = {
          id: row.id
        }
        this.$modal.confirm("确定要重新发送该任务调用设备？").then(() => {
          return enforceDelivery(param)
        }).then(() => {
          this.$modal.msgSuccess("发送成功");
          this.getList();
        }).catch(() => {});
      },

    },
  };
</script>
<style>
  .item {
    text-align: center;
  }

  .row-item {
    display: flex;
    align-items: center;
  }
</style>
