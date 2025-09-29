<template>
  <el-dialog title="地堆拣货" :visible.sync="open" width="50%" append-to-body>
    <el-form ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="100px" style="padding-right: 30px" v-if="type == '1' || type == '2'">
      <el-form-item label="载具编号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入载具编号" :maxlength="40" />
      </el-form-item>
      <el-form-item label="载具类型" prop="trayCategory">
        <el-select v-model="queryParams.trayCategory" placeholder="请选择载具类型" class="select-input-form">
          <el-option v-for="dict in dict.type.wms_t_tray_category" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <div class="show-test" :style="{ 'margin-top': (type == '3' || type == '4') ? '-30px' : '0px' }">
      <span>预计拣货数量：{{ predictReceiveCount }}</span>
      <span>已拣货数量：{{ totalCount }}</span>
    </div>
    <el-form ref="form" :model="form" label-width="0px" style="padding-right: 30px">
      <el-form-item prop="taskDetailList">
        <el-table ref="trayTable" v-loading="loading" :data="form.taskDetailList">
          <el-table-column label="载具编号" align="center" prop="code" width="250"></el-table-column>
          <el-table-column label="载具类型" align="center" prop="trayCategory">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.wms_t_tray_category" :value="scope.row.trayCategory" />
            </template>
          </el-table-column>
          <el-table-column label="库位" align="center" prop="locationname"></el-table-column>
          <el-table-column label="库区" align="center" prop="reservoirname"></el-table-column>
          <el-table-column label="库存数量" align="center" prop="availableCount"></el-table-column>
          <el-table-column label="实际拣货数量" align="center" prop="receiveCount" min-width="120" v-if="type == '3' || type == '4'" />
          <el-table-column label="实际拣货数量" align="center" prop="receiveCount" min-width="120" v-if="type == '1' || type == '2'">
            <template slot-scope="scope">
              <el-form-item label-width="0px">
                <el-input v-model="scope.row.receiveCount" placeholder="请输入实际拣货数量" v-intNumber maxlength="6" size="small" @blur="val => handleInputChange(val,'receiveCount',scope.$index,scope.row)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { executeOutFloorDisplayDelivery, executeMergeOutFloorDisplayDelivery, getOutDeliveryFloorDisplayTraylist, getOutDeliveryFloorDisplayAutoTraylist, getMergeOutDeliveryFloorDisplayAutoTraylist } from "@/api/inoutDelivery/outDelivery";

export default {
  name: "floorDisplayTask",
  dicts: ["wms_t_tray_category"],
  data(){
    return {
      // 遮罩层
      loading: true,
      //选中数组数据
      currentSelection: [],
      // 总条数
      total: 0,
      // 预计拣货数量
      predictReceiveCount: 0,
      // 已拣货数量
      totalCount: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //地堆拣货类型
      type: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        trayCode: null,
        trayType: null,
      },
      //物料信息
      materialList: [],
      // 表单参数
      form: {
        taskDetailList: []
      },
      detailRules: {
        receiveCount: [
          { required: true, message: "请输入实际拣货数量", trigger: "blur" },
        ],
      },

      spanArr:[], // 需要合并的行数
      pos:0,// 索引
    }
  },
  watch: {
    open(val){
      if(val){
        this.form = {
          taskDetailList: []
        }
        if(this.type == '1' || this.type == '2'){
          this.getList()
        } else if(this.type == '3'){
          this.getAutoList()
        } else if(this.type == '4'){
          this.getMergeAutoList()
        }
      }
    }
  },
  methods: {
    /** 获取载具列表 */
    getList(){
      this.loading = true;
      getOutDeliveryFloorDisplayTraylist({ ...this.queryParams, id: this.materialList[0].id, materialId: this.materialList[0].materialId}).then((response) => {
        this.form.taskDetailList = response.data.data.rows;
        this.total = response.data.data.total;
        this.predictReceiveCount = response.data.predictReceiveCount
        this.totalCount = response.data.totalCount;
        this.loading = false;
      });
    },
    /** 获取自动分配载具列表 */
    getAutoList(){
      this.loading = true;
      getOutDeliveryFloorDisplayAutoTraylist({ outDeliveryId: this.materialList[0].outDeliveryId, materialId: this.materialList[0].materialId }).then((response) => {
        this.form.taskDetailList = response.data.dataList;
        this.predictReceiveCount = response.data.predictReceiveCount
        this.totalCount = response.data.totalCount;
        this.loading = false;
      });
    },
    /** 获取波次分配自动分配载具列表 */
    getMergeAutoList(){
      this.loading = true;
      getMergeOutDeliveryFloorDisplayAutoTraylist({ mergeDeliveryId: this.materialList[0].mergeDeliveryId, materialId: this.materialList[0].materialId }).then((response) => {
        this.form.taskDetailList = response.data.dataList;
        this.predictReceiveCount = response.data.predictReceiveCount
        this.totalCount = response.data.totalCount;
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
    //输入框变化事件
    handleInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.form.taskDetailList,index,info)
    },
    /** 提交按钮 */
    submitForm() {
      let tTaskOutDetailListVOS = []
      this.form.taskDetailList.map((item) => {
        if(item.receiveCount){
          let info = {
            stockId: item.stockid,
            receiveCount: item.receiveCount,
          }
          tTaskOutDetailListVOS.push(info)
        }
      })
      if(tTaskOutDetailListVOS.length == 0){
        this.$message.warning("请至少输入一条载具的实际拣货数量！")
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let params = {
            outDeliveryDetailId: this.materialList[0].id,
            tTaskOutDetailListVOS,
          }
          if(this.type == '2' || this.type == '4'){
            executeMergeOutFloorDisplayDelivery(params).then((response) => {
              this.$modal.msgSuccess("地堆拣货成功");
              this.open = false
              this.form = {
                taskDetailList: []
              }
              this.type = null
              this.$emit("setArriveTask",true)
            });
          } else {
            executeOutFloorDisplayDelivery(params).then((response) => {
              this.$modal.msgSuccess("地堆拣货成功");
              this.open = false
              this.form = {
                taskDetailList: []
              }
              this.type = null
              this.$emit("setArriveTask",true)
            });
          }
        }
      });
    },
    /** 取消按钮 */
    cancel(){
      this.open = false
      this.form = {
        taskDetailList: []
      }
      this.type = null
    }
  }
}
</script>
<style lang="scss" scoped>
.show-test{
  font-size: 18px;
  color: #222222;
  font-weight: bold;
  span{
    margin-right: 50px;
  }
  margin-bottom: 10px;
}
</style>
