<template>
  <div class="app-container" ><!--v-loading.fullscreen="dialogLoading"-->
    <el-tabs v-model="planType" @tab-click="handleClick">
      <el-tab-pane label="托盘" name="1">
        <list-com ref="listCom" v-if="planType == '1'" @getList="getList" @setButtonHandle="setButtonHandle"></list-com>
      </el-tab-pane>
      <el-tab-pane label="料箱" name="2">
        <list-com ref="listCom" v-if="planType == '2'" @getList="getList" @setButtonHandle="setButtonHandle"></list-com>
      </el-tab-pane>
      <el-tab-pane label="地堆" name="3">
        <list-com ref="listCom" v-if="planType == '3'" @getList="getList" @setButtonHandle="setButtonHandle"></list-com>
      </el-tab-pane>
    </el-tabs>
    <!-- 选择物料 -->
    <MaterialCom ref="materialCom" @setMaterial="setMaterial"></MaterialCom>
    <AddCom ref="addCom" @setAdd="setAdd"></AddCom>
  </div>
</template>

<script>
import { listInventoryPlan, addInventoryPlan, delInventoryPlan, addInventoryTask } from "@/api/inventory/plan";

import { wms } from "@/utils/agent";

import ListCom from './components/list';

import AddCom from "./components/add";

import MaterialCom from "./components/material";

export default {
  name: "inventoryPlan",
  dicts: ["inout_out_type", "wms_out_recheck_status", "inout_out_local"],
  components: { ListCom, AddCom, MaterialCom },
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
      // 盘点计划表格数据
      inventoryPlanList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        type: null,
      },

      planType: '1',//盘点计划类型值
      checkType: '1', //盘点计划的维度类型值
    };
  },
  watch: {
    $route(to, form) {
      if (to.name == "inventoryPlan") {
        this.getList();
      }
    },
  },
  mounted() {
    this.getList();
  },
  methods: {
    /** 切换事件 */
    handleClick(val,event){
      this.planType = val.name
      this.checkType = '1'
      this.getList()
    },
    /** 查询盘点计划列表 */
    getList(val) {
      let params = {
        ...this.$refs.listCom.queryParams,
        trayType: this.planType,
        checkType: val ? val.checkType : this.checkType
      }
      if (params.dateTime && params.dateTime.length > 0) {
        params.startTime = params.dateTime[0]
        params.endTime = params.dateTime[1]
      }
      delete params.dateTime
      if(val){
        this.checkType = val.checkType
      }
      this.$refs.listCom.loading = true;
      listInventoryPlan(params).then((response) => {
        this.$refs.listCom.inventoryPlanList = response.rows;
        this.$refs.listCom.total = response.total;
        this.$refs.listCom.loading = false;
      });
    },
    /** 生成盘点任务/新增/删除、导出按钮操作 */
    setButtonHandle(handleData) {
      switch(handleData.type){
        case 1:
          this.$modal.confirm('该操作将生成盘点任务,确定要生成吗？').then(function () {
            return addInventoryTask(handleData.ids);
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("生成盘点任务成功");
          }).catch(() => { });
          break;
        case 2:
          if(handleData.checkType == '1'){
            this.$refs.materialCom.open = true;
            this.$refs.materialCom.trayType = this.planType;
          } else {
            this.$refs.addCom.title = '新增盘点计划';
            this.$refs.addCom.type = handleData.checkType;
            this.$refs.addCom.form = {};
            this.$refs.addCom.open = true;
          }
          this.checkType = handleData.checkType
          break;
        case 3:
          this.$modal.confirm('该操作将删除该数据,确定要删除吗？').then(function () {
            return delInventoryPlan(handleData.ids.toString());
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("删除成功");
          }).catch(() => { });
          break;
        case 4:
          this.download(wms + "/checkDelivery/export", {...handleData.vals,checkType: handleData.checkType, trayType: this.planType}, `inventoryPlanExport_${new Date().getTime()}.xlsx`);
          break;
      }
    },
    /** 新增确定回调 */
    setAdd(form){
      let params = {
        data: [{...form}],
        trayType: this.planType,
        checkType: this.checkType
      }
      addInventoryPlan(params).then((response) => {
        this.getList();
        this.$refs.addCom.open = false;
        this.$modal.msgSuccess("新增盘点计划成功");
      });
    },
    /** 选择物料回调 */
    setMaterial(material) {
      let list = []
      material.ids.map((item) => {
        let info = {
          materialId: item
        }
        list.push(info)
      })
      let params = {
        data: list,
        trayType: this.planType,
        checkType: this.checkType
      }
      addInventoryPlan(params).then((response) => {
        this.getList();
        this.$refs.materialCom.open = false;
        this.$refs.materialCom.trayType = null;
        this.$modal.msgSuccess("新增盘点计划成功");
      });
    },
  },
};
</script>
