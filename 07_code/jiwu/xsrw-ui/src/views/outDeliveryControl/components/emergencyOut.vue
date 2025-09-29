<template>
  <!-- 应急出库对话框 -->
  <el-dialog title="应急出库" :visible.sync="open" width="55%" append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="padding-right: 30px">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="出库类型" prop="type">
            <el-select style="width: 100%;" v-model="form.type" placeholder="请选择出库类型" class="select-input-form">
              <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="物料编码" prop="materialCode">
            <el-input v-model="form.materialCode" placeholder="请选择物料" size="small" @focus="materialComOpen"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="物料名称">
            <el-input v-model="form.materialName" disabled :maxlength="40" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <el-form-item label="预计出库数量" prop="predictCount">
            <el-input v-model="form.predictCount" placeholder="请输入预计出库数量" v-intNumber :maxlength="6" show-word-limit />
          </el-form-item>
        </el-col>
<!--        <el-col :span="12" v-if="form.minUnitName">-->
<!--          <el-form-item label="小件领取数量">-->
<!--            <el-input style="width: 78%;" v-model="form.smallPredictCount" v-intNumber placeholder="请输入小件领取数量" :maxlength="6" show-word-limit >-->
<!--              <template slot="append">{{ form.minUnitName }}</template>-->
<!--            </el-input>-->
<!--            <span style="margin-left: 10px;">{{ '1'+ form.unitName + form.smallLimitCount + form.minUnitName }}</span>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
      </el-row>
      <el-row :gutter="10" class="mb8" style="margin-left: 25px;">
        <el-col :span="1.5" style="float: right;">
          <el-button icon="el-icon-refresh" size="mini" @click="reset">重置</el-button>
        </el-col>
        <el-col :span="1.5" style="float: right;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click.stop="handleQuery">获取所在库位</el-button>
        </el-col>
      </el-row>
      <el-form-item prop="tOutDeliveryDetailList" label-width="30px">
        <el-table v-loading="loading" :data="form.tOutDeliveryDetailList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="库位" align="center" prop="locationName" min-width="150"></el-table-column>
          <el-table-column label="库区" align="center" prop="reservoirName" min-width="150"></el-table-column>
          <el-table-column label="载具编号" align="center" prop="code" width="180"></el-table-column>
          <el-table-column label="载具类型" align="center" prop="trayCategory" width="150">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.wms_t_tray_category" :value="scope.row.trayCategory" />
            </template>
          </el-table-column>
          <el-table-column label="库存数量" align="center" prop="availableCount" width="180"></el-table-column>
        </el-table>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { getEmergencyOutStockList } from "@/api/inoutDelivery/outDelivery";
import { visualizationLocationOutbound } from "@/api/stockLedger/visualization";

export default {
  name: "EmergencyOut",
  dicts: ["wms_t_tray_category","inout_out_type"],
  data(){
    let validatetOutDeliveryDetailList= (rule, value, callback) => {
      if (this.currentSelection && this.currentSelection.length == 0) {
        callback(new Error("请选择库位信息"));
      } else {
        callback();
      }
    };
    return{
      // 遮罩层
      loading: false,
      //选中数组数据
      currentSelection: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {
        tOutDeliveryDetailList: []
      },
      //表单规则
      rules: {
        type: [
          { required: true, message: "请选择出库类型", trigger: "change" },
        ],
        materialCode: [
          { required: true, message: "请选择物料", trigger: "change" },
        ],
        predictCount: [
          { required: true, message: "请输入预计出库数量", trigger: "blur" },
        ],
        smallPredictCount: [
          { required: true, message: "请输入小件领取数量", trigger: "blur" },
        ],
        tOutDeliveryDetailList: [
          { trigger: "change", validator: validatetOutDeliveryDetailList },
        ]
      },
    }
  },
  watch: {
    open(val){
      if(val){
        this.reset()
      }
    }
  },
  methods: {
    /** 展示物料弹窗 */
    materialComOpen(){
      this.$emit("showMaterial")
    },
    /** 查询库位列表 */
    handleQuery(){
      this.currentSelection = []
      this.getEmergencyOutStockTable()
    },
    /** 重置 */
    reset(){
      this.currentSelection = []
      this.form = {
        type: null,
        materialId: null,
        materialCode: null,
        materialName: null,
        unitName: null,
        unitId: null,
        minUnitName: null,
        predictCount: null,
        smallLimitCount: null,
        smallPredictCount: null,
        tOutDeliveryDetailList: []
      }
    },
    /** 获取应急出库的库位列表 */
    getEmergencyOutStockTable(){
      this.loading = true;
      getEmergencyOutStockList({ materialId: this.form.materialId }).then((response) => {
        this.form.tOutDeliveryDetailList = response.data;
        this.loading = false;
      });
    },
    /** 列表选择事件 */
    handleSelectionChange(selection){
      this.currentSelection = selection ? JSON.parse(JSON.stringify(selection)) : []
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let tOutDeliveryDetailList = []
          this.currentSelection.map((item) => {
            let info = {
              stockId: item.id,
            }
            tOutDeliveryDetailList.push(info)
          })
          let params = {
            type: this.form.type,
            materialId: this.form.materialId,
            predictCount: this.form.predictCount,
            smallPredictCount: this.form.smallPredictCount,
            deliveryType: '1',
            tOutDeliveryDetailList,
          }
          visualizationLocationOutbound(params).then((response) => {
            this.$modal.msgSuccess("应急出库成功");
            this.open = false
            this.$emit("setEmergencyOut",true)
          });
        }
      });
    },
    /** 取消按钮 */
    cancel(){
      this.open = false
      this.form = {
        tOutDeliveryDetailList: []
      }
    }
  }
}
</script>
