<template>
  <div class="app-container">
    <el-dialog :title="detailTitle" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="越库单号">
              <span>{{ detailForm.code }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="类型">
              <dict-tag :options="dict.type.wms_overstock_type" :value="detailForm.type" />
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="状态">
              <dict-tag :options="dict.type.wms_overstock_status" :value="detailForm.status" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="物料使用部门">
              <span>{{ detailForm.deptName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="制单人">
              <span>{{ detailForm.createBy }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="制单日期">
              <span>{{ detailForm.createTime }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="来源">
              <dict-tag :options="dict.type.in_delivery_origin" :value="detailForm.newLocal" />
            </el-form-item>
          </el-col>
          <el-col :span="10" class="detail-form-item">
            <el-form-item label="审核备注">
              <span>{{ detailForm.remark }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="0px" v-if="detailForm.deliveryDetailList && detailForm.deliveryDetailList.length > 0">
          <el-table v-loading="loading" :data="detailForm.deliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="130"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName"></el-table-column>
            <el-table-column label="入库数量" align="center" prop="registrationCount" min-width="120" v-if="detailType == 1 || (detailType == 3 && detailForm.status >= 4)"></el-table-column>
            <el-table-column label="预计入库数量" align="center" prop="predictCount" min-width="120" v-if="detailType == 2 || (detailType == 3 && detailForm.status <= 3)"></el-table-column>
            <el-table-column label="收货数量" align="center" prop="registrationCount" width="120" v-if="detailType == 2">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.registrationCount'" :rules="detailRules.registrationCount">
                  <el-input v-model="scope.row.registrationCount" placeholder="请输入收货数量" v-intNumber size="small" maxlength="6" @blur="val => handleInputChange(val,'registrationCount',scope.$index,scope.row)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="已入库数量" align="center" :prop="detailType == 2 ? 'registrationInCount' : 'registrationCount'" min-width="120" v-if="detailType == 2 || (detailType == 3 && detailForm.status <= 3)"></el-table-column>
            <el-table-column label="出库数量" align="center" prop="receiveCount" min-width="120" v-if="detailType == 1">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.receiveCount'" :rules="detailRules.receiveCount">
                  <el-input v-model="scope.row.receiveCount" placeholder="请输入出库数量" v-intNumber size="small" maxlength="6" @blur="val => handleInputChange(val,'receiveCount',scope.$index,scope.row)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark" min-width="120" v-if="detailType == 1">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.remark'">
                  <el-input v-model="scope.row.remark" placeholder="请输入备注" size="small" maxlength="250" @blur="val => handleInputChange(val,'remark',scope.$index,scope.row)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <!-- <el-table-column label="出库仓库" align="center" prop="warehouseId"  min-width="120" v-if="detailType == 1">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.warehouseId'">
                  <el-select v-model="scope.row.warehouseId" class="select-input-form" @change="val => handleSelectChange(val,'warehouseId',scope.$index,scope.row)">
                    <el-option v-for="item in warehouseList" :key="item.id" :label="item.code" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column> -->
            <el-table-column label="出库数量" align="center" prop="receiveCount" min-width="120" v-if="detailType == 3 && detailForm.status >= 4" />
            <el-table-column label="备注" align="center" prop="remark" min-width="120" v-if="detailType == 3 && detailForm.status >= 4" />
            <el-table-column label="批次" align="center" prop="batchCode" min-width="130"></el-table-column>
            <el-table-column label="单价（元）" align="center" prop="price" min-width="100" v-if="detailType == 3"></el-table-column>
            <el-table-column label="财务凭证号" align="center" prop="financeVoucherNo" min-width="150" v-if="detailType == 2 || detailType == 3"></el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitOutDetailForm" v-if="detailType == 1">确 定</el-button>
        <el-button type="primary" @click="submitInDetailForm" v-if="detailType == 2">确认收货</el-button>
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getOverstock } from "@/api/inoutDelivery/overstock";

export default {
  name: "crossDockingDetailCom",
  dicts: ['wms_overstock_type','wms_overstock_status','in_delivery_origin'],
  data() {
    let validateRegistrationCount= (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("收货数量不能为空"));
        this.$message.error("收货数量不能为空")
      } else if (Number(value) < 0) {
        callback(new Error("收货数量应大于等于0"));
        this.$message.error("收货数量应大于等于0")
      } else if (this.inputIndex !== '' && Number(value) > 0 && (Number(value) > Number(this.detailForm.deliveryDetailList[this.inputIndex].predictCount))) {
        callback(new Error("收货数量应小于等于预计入库数量"));
        this.$message.error("收货数量应小于等于预计入库数量")
      } else {
        callback();
      }
    };
    let validateReceiveCount= (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("出库数量不能为空"));
        this.$message.error("出库数量不能为空")
      } else if (Number(value) < 0) {
        callback(new Error("出库数量应大于等于0"));
        this.$message.error("出库数量应大于等于0")
      } else if (this.inputIndex !== '' && Number(value) > 0 && (Number(value) > Number(this.detailForm.deliveryDetailList[this.inputIndex].registrationCount))) {
        callback(new Error("出库数量应小于等于入库数量"));
        this.$message.error("出库数量应小于等于入库数量")
      } else {
        callback();
      }
    };
    let validateWarehouseId= (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("出库仓库不能为空"));
        this.$message.error("出库仓库不能为空")
      } else {
        callback();
      }
    };
    return {
      // 遮罩层
      loading: true,
      // 弹出层标题
      detailTitle: "",
      // 是否显示弹出层
      open: false,
      current: null, // 当前选中的物料

      detailForm: {}, //详情
      detailType: '', //详情类型
      detailId: '', //详情id

      //详情校验规则
      detailRules: {
        returnCount: [
          { trigger: "blur", validator: validateRegistrationCount },
        ],
        receiveCount: [
          { trigger: "blur", validator: validateReceiveCount },
        ],
        warehouseId: [
          { trigger: "change", validator: validateWarehouseId },
        ],
      },
    };
  },
  watch: {
    open(){
      if (this.open) {
        this.getDetail();
      }
    },
  },
  methods: {
    /** 查询详情 */
    getDetail() {
      this.loading = true;
      getOverstock(this.detailId).then(response => {
        this.detailForm = response.data
        if(this.detailType == '2'){
          this.detailForm.deliveryDetailList.map((item) => {
            item.registrationInCount = item.registrationCount
          })
        }
        this.loading = false;
      });
    },
    // 取消按钮
    detailCancel() {
      this.open = false;
      this.detailForm = {}
      this.detailTitle = ''
      this.detailType = ''
      this.detailId = ''
      // this.reset();
    },
    // 选中行
    handleCurrentChange(row) {
      this.current = row
    },
    //输入框变化事件
    handleInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.detailForm.deliveryDetailList,index,info)
    },
    //选择变化事件
    handleSelectChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val
      this.$set(this.detailForm.deliveryDetailList,index,info)
    },
    /** 确定按钮 */
    submitInDetailForm() {
      this.inputIndex = ''
      let errorMessage = []
      this.detailForm.deliveryDetailList.map((item) => {
        if(Number(item.receiveCount) > Number(item.registrationCount)){
          errorMessage.push(`物料【${(item.materialName)}】的出库数量应小于入库数量`)
        }
        // else if(item.warehouseId === null || item.warehouseId === ''){
        //   errorMessage.push(`物料【${(item.materialName)}】的出库仓库不能为空`)
        // }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["detailForm"].validate((valid) => {
        if (valid) {
          this.$emit('setDetail', this.detailForm)
        }
      });
    },
    /** 确定收货按钮 */
    submitOutDetailForm() {
      this.inputIndex = ''
      let errorMessage = []
      this.detailForm.deliveryDetailList.map((item) => {
        if(Number(item.registrationCount) > Number(item.predictCount)){
          errorMessage.push(`物料【${(item.materialName)}】的收货数量应小于等于预计入库数量`)
        }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["detailForm"].validate((valid) => {
        if (valid) {
          this.$emit('setDetail', this.detailForm)
        }
      });
    },
  }
};
</script>
