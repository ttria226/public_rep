<template>
  <el-dialog title="地堆拣货" :visible.sync="open" width="50%" append-to-body>
    <div class="show-test" style="margin-top: -30px;">
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
          <el-table-column label="实际拣货数量" align="center" prop="receiveCount" min-width="120" />
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
import { executeOutFloorDisplayDelivery, getOutDeliveryFloorDisplayAutoTraylist } from "@/api/inoutDelivery/outDelivery";

export default {
  name: "outDeliveryQuickFloorDisplayTask",
  dicts: ["wms_t_tray_category"],
  data(){
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 预计拣货数量
      predictReceiveCount: 0,
      // 已拣货数量
      totalCount: 0,
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
    }
  },
  watch: {
    open(val){
      if(val){
        this.form = {
          taskDetailList: []
        }
        this.getAutoList()
      }
    }
  },
  methods: {
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