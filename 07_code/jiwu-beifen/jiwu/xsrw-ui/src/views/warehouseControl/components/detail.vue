<template>
  <el-dialog title="任务详情" :visible.sync="open" width="60%" append-to-body>
    <el-form ref="detailForm" :model="detailForm" :rules="detailType == 4 ? null : rules" label-width="120px" style="padding-right: 30px">
      <el-form-item label-width="30px">
        <el-table :data="detailForm.taskWcsDetailVOList">
          <el-table-column label="载具编号" align="center" prop="trayCode" min-width="180"></el-table-column>
          <!-- <el-table-column label="载具状态" align="center" prop="trayStatus"></el-table-column> -->
          <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
          <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
          <el-table-column label="计量单位" align="center" prop="unitName" width="100"></el-table-column>
          <el-table-column label="批次号" align="center" prop="batchCode" width="200"></el-table-column>
          <el-table-column label="上架数量" align="center" prop="actualCount" width="100"></el-table-column>
          <el-table-column label="rfid" align="center" prop="rfidString" min-width="180" v-if="detailType == 3"></el-table-column>
          <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220" fixed="right" v-if="detailType == 3">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-setting" @click="handleChooseRfid(scope.row)">选择物料详细</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.rfidString" @click="handleRfidDelete(scope.row,scope.$index)">清空物料详细</el-button>
            </template>
          </el-table-column> -->
        </el-table>
      </el-form-item>
<!--      <el-form-item label="选择上架区域" prop="areaId" v-if="detailType != 4">-->
<!--        <el-select style="width: 100%;" v-model="detailForm.areaId" @change="changeReservoirList" clearable placeholder="请选择上架区域">-->
<!--          <el-option v-for="item in areaList" :key="item.id" :label="item.name" :value="item.id" />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="选择上架库区" prop="reservoirId" v-if="detailType != 4">-->
<!--        <el-select style="width: 100%;" v-model="detailForm.reservoirId" @change="changeLocationList" clearable placeholder="请选择上架库区">-->
<!--          <el-option v-for="item in reservoirList" :key="item.id" :label="item.name" :value="item.id" />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="选择上架库位" prop="locationId" v-if="detailType != 4">-->
<!--        <el-select style="width: 100%;" v-model="detailForm.locationId" clearable placeholder="请选择上架库位">-->
<!--          <el-option v-for="item in locationList" :key="item.id" :label="item.name" :value="item.id" />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitDetailForm(detailType)" v-if="detailType == 1 || detailType == 2">执行上架</el-button>
      <el-button type="primary" @click="submitDetailForm(detailType)" v-else-if="detailType == 3">强制完成</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { getDeliveryExecute, updateLocationDeliveryExecute, enforcementDelivery } from "@/api/inoutDelivery/inDelivery";
import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";
import { listLocation } from "@/api/wms/location";
export default {
  name: "warehouseControlDetailCom",
  dicts: ["wms_asn_origin_type", "wms_take_delivery_type", "wms_asn_status"],
  data(){
    return {
      //弹窗标识
      open: false,
      //详情表单
      detailForm: {},
      // 表单校验
      rules: {
        areaId: [{ required: true, message: "请选择上架区域", trigger: "change" }],
        reservoirId: [{ required: true, message: "请选择上架库区", trigger: "change" }],
        locationId: [{ required: true, message: "请选择上架库位", trigger: "change" },],
      },
      //详情类型
      detailType: '',
      //详情Id
      detailId: '',

      areaList: [], //区域list
      reservoirList: [], //库区list
      locationList: [], //库位list
    }
  },
  watch: {
    open(){
      if (this.open) {
        this.getAreaList()
        this.getDetail();
      }
    },
  },
  methods: {
    /** 查询详情 */
    getDetail() {
      this.loading = true;
      getDeliveryExecute(this.detailId).then(response => {
        this.detailForm = {...response.data, areaId: null, reservoirId: null, locationId: null}
        this.loading = false;
      });
    },
    /** 查询区域列表 */
    getAreaList() {
      let qps = {
        pageNum: 1,
        pageSize: 5000,
      };
      listArea(qps).then(response => {
        this.areaList = response.rows;
      });
    },
    /** 查询库区列表 */
    getReservoirList() {
      let qps = {
        areaId: this.detailForm.areaId,
        pageNum: 1,
        pageSize: 5000,
      };
      listReservoir(qps).then(response => {
        this.reservoirList = response.rows;
      });
    },
    /** 查询库位列表 */
    getLocationList() {
      let qps = {
        areaId: this.detailForm.areaId,
        reservoirId: this.detailForm.reservoirId,
        locationType: 0,
        pageNum: 1,
        pageSize: 5000,
      };
      listLocation(qps).then(response => {
        this.locationList = response.rows;
      });
    },
    //区域变化事件
    changeReservoirList(val){
      if(val){
        this.detailForm.reservoirId = null
        this.detailForm.locationId = null
        this.reservoirList = []
        this.locationList = []
        this.getReservoirList()
      }
    },
    //库区变化事件
    changeLocationList(val){
      if(val){
        this.detailForm.locationId = null
        this.locationList = []
        this.getLocationList()
      }
    },
    //选择物料rifd
    handleChooseRfid(item){
      this.$emit("showMaterialRfid",{ id: item.advanceRegistrationId })
    },
    //清空物料rfid信息
    handleRfidDelete(row,index){
      let info = JSON.parse(JSON.stringify(row))
      info.rfids = null
      info.rfidString = null
      this.$set(this.detailForm.taskWcsDetailVOList,index,info)
    },
    //执行上架
    submitDetailForm(detailType){
      // let errorMessage = []
      // this.detailForm.taskWcsDetailVOList.map((item) => {
      //   if(!item.rfids){
      //     errorMessage.push("物料【"+item.materialName+"】的物料rfid信息不能为空")
      //   } else if(item.rfids.length != item.actualCount){
      //     errorMessage.push("物料【"+item.materialName+"】的物料rfid数量必须等于上架数量")
      //   }
      // })
      // if(errorMessage.length > 0){
      //   this.$message.error(errorMessage.join("，"))
      //   return false
      // }
      this.$refs["detailForm"].validate((valid) => {
        if (valid) {
          let params = {
            id: this.detailForm.id,
            // locationId: this.detailForm.locationId
          }
          if(detailType == 3){
            // let taskWcsDetailVOList = []
            // this.detailForm.taskWcsDetailVOList.map((item) => {
            //   let info = {
            //     advanceRegistrationId: item.advanceRegistrationId,
            //     rfids: item.rfids
            //   }
            //   taskWcsDetailVOList.push(info)
            // })
            // params.taskWcsDetailVOList = taskWcsDetailVOList
            enforcementDelivery(params).then((response) => {
              this.$modal.msgSuccess("强制执行成功");
              this.open = false
              this.$emit("setControl",true)
            });
          }else{
            updateLocationDeliveryExecute(params).then((response) => {
              this.$modal.msgSuccess("执行成功");
              this.open = false
              this.$emit("setControl",true)
            });
          }
        }
      });
    },
    cancel(){
      this.open = false
    }
  }
}
</script>
