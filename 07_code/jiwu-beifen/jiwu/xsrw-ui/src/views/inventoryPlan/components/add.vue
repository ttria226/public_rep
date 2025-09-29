<template>
  <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="padding-right: 30px">
      <!-- <el-form-item label="仓库" prop="warehouseId" v-if="type == '2' || type == '5'">
        <el-select style="width: 100%;" v-model="form.warehouseId" placeholder="请选择仓库" @change="changeAreaList" class="select-input-form">
          <el-option v-for="item in warehouseList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item> -->
      <el-form-item label="区域" prop="areaId" v-if="type == '2' || type == '5'">
        <el-select style="width: 100%;" v-model="form.areaId" placeholder="请选择区域" @change="changeReservoirList" class="select-input-form">
          <el-option v-for="item in areaList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="库区" prop="reservoirId" v-if="type == '2' || type == '5'">
        <el-select style="width: 100%;" v-model="form.reservoirId" placeholder="请选择库区" class="select-input-form">
          <el-option v-for="item in reservoirList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点数量" prop="randomNum" v-if="type == '4'">
        <el-input v-model="form.randomNum" v-intNumber placeholder="请输入盘点数量" :maxlength="6" />
      </el-form-item>
      <el-form-item label="时间范围" prop="dataTime" v-if="type == '3'">
        <el-date-picker v-model="form.dataTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd" @change="handelDateChange"></el-date-picker>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";

export default {
  name: 'inventoryAddCom',
  data(){
    return{
      // 弹出层标题
      title: "",
      //弹窗类型
      type: "",
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        warehouseId: [{ required: true, message: "请选择仓库", trigger: "change" }],
        areaId: [{ required: true, message: "请选择区域", trigger: "change" }],
        reservoirId: [{ required: true, message: "请选择库区", trigger: "change" }],
        randomNum: [
          { required: true, message: "请输入盘点数量", trigger: "blur" },
        ],
        dataTime: [{ required: true, message: "请选择时间范围", trigger: "change" }],
      },

      warehouseList: [],//仓库下拉list
      areaList: [],//区域下拉list
      reservoirList: [],//库区下拉list
    }
  },
  watch: {
    open(val){
      if(val){
        if(this.type == '2' || this.type == '5'){
          listArea({pageSize:5000}).then(response => {
            this.areaList = response.rows;
          });
          this.form = {
            areaId: null,
            reservoirId: null,
          }
        } else if(this.type == '4'){
          this.form = {
            randomNum: null,
          }
        } else if(this.type == '3'){
          this.form = {
            dataTime: null,
            startTime: null,
            endTime: null,
          }
        }
      }
    }
  },
  methods: {
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let params = JSON.parse(JSON.stringify(this.form))
          delete params.dataTime
          this.$emit("setAdd",params)
        }
      });
    },
    /** 仓库变化事件 */
    changeAreaList(val){
      this.form.areaId = null;
      listArea({pageSize:5000}).then(response => {
        this.areaList = response.rows;
      });
    },
    /** 区域变化事件 */
    changeReservoirList(val){
      this.form.reservoirId = null;
      listReservoir({areaId:this.form.areaId,pageSize:5000}).then(response => {
        this.reservoirList = response.rows;
      });
    },
    //时间范围变化
    handelDateChange(val){
      if(val && val.length > 0){
        this.form.startTime = val[0]
        this.form.endTime = val[1]
      } else {
        this.form.startTime = null
        this.form.endTime = null
      }
    }
  }
}
</script>
