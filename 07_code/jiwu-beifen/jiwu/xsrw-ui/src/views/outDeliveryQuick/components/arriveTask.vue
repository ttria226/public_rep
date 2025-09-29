<template>
  <el-dialog title="执行出库" :visible.sync="open" width="50%" append-to-body>
    <el-row :gutter="10" class="mb8" style="margin-top: -30px;">
      <el-col :span="1.5">
        <div class="dialog-title"><span>已选载具</span></div><!--<span>(预计拣货数量：{{ materialList[0] ? materialList[0].num : 0 }})</span>-->
      </el-col>
    </el-row>
    <el-form ref="form" :model="form" label-width="0px" style="padding-right: 30px">
      <el-form-item prop="taskDetailList">
        <el-table v-loading="loading" :data="form.taskDetailList" ><!--:span-method="objectSpanMethod"-->
          <el-table-column label="载具编号" align="center" prop="trayCode" min-width="150"></el-table-column>
          <el-table-column label="物料编码" align="center" prop="materialCode" min-width="130"></el-table-column>
          <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
          <el-table-column label="库存数量" align="center" prop="availableCount" width="120"></el-table-column>
          <el-table-column label="拣货数量" align="center" prop="predictCount" width="120"></el-table-column>
          <!-- <el-table-column label="计量单位" align="center" prop="unitName" min-width="120"></el-table-column> -->
          <el-table-column label="批次号" align="center" prop="batchCode" min-width="130"></el-table-column>
        </el-table>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">执行出库</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { executeOutDeliveryQuick, getOutDeliveryQuickAutoTraylist, getOutDeliveryQuickAutoSubmitData } from "@/api/inoutDelivery/outDelivery";

export default {
  name: 'outDeliveryQuickArriveTask',
  dicts: ["wms_t_tray_category"],
  data(){
    return {
      // 遮罩层
      loading: true,
      //选中数组数据
      currentSelection: [],
      // 总条数
      total: 0,
      // 是否显示弹出层
      open: false,
      //执行出库类型
      type: null,
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
        this.getAutoList()
      }
    }
  },
  methods: {
    /** 获取自动分配载具列表 */
    getAutoList(){
      this.loading = true;
      getOutDeliveryQuickAutoTraylist({ outDeliveryId: this.materialList[0].id }).then((response) => {
        let list = []
        if(response.data && response.data.length > 0){
          response.data.map((item) => {
            let info = {
              outDeliveryDetailId: this.materialList[0].id,
              trayCode: item.code ? item.code : "",
              trayId: item.id ? item.id : "",
              stockId: item.stockid ? item.stockid : "",
              locationId: item.locationId ? item.locationId : "",
              locationName: item.locationname ? item.locationname : "",
              materialCode: item.materialCode,
              materialId: item.materialId,
              materialName: item.materialName,
              // unitName: this.materialList[0].unitName,
              batchCode: item.batchCode,
              availableCount: item.availableCount ? item.availableCount : 0,
              predictCount: item.predictCount ? item.predictCount : 0,
            }
            list.push(info)
          })
        }
        this.$nextTick(() => {
          this.form.taskDetailList = list
          this.$forceUpdate()
        })
        this.loading = false;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$message.closeAll()
      if(this.form.taskDetailList.length == 0){
        this.$message.warning("请选择载具！")
        return false
      }
      let errorMessage = []
      this.form.taskDetailList.map((item) => {
        if(!item.predictCount){
          errorMessage.push("物料【"+item.materialCode+"】在载具【"+item.trayCode+"】上的拣货数量不能为空或者0")
        }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          getOutDeliveryQuickAutoSubmitData({ outDeliveryId: this.materialList[0].id }).then((res) => {
            if(res.data){
              executeOutDeliveryQuick(res.data).then((response) => {
                this.$modal.msgSuccess("执行出库成功");
                this.open = false
                this.form = {
                  taskDetailList: []
                }
                this.type = null
                this.$emit("setArriveTask",true)
              });
            }
          })
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
