<template>
  <div class="app-container">
    <el-dialog :title="detailTitle" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="收货单号">
              <span>{{ detailForm.code }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="单据类型">
              <dict-tag :options="dict.type.in_delivery_type" :value="detailForm.type" />
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="状态">
              <dict-tag :options="dict.type.in_delivery_status" :value="detailForm.status" />
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
          <el-col :span="10" class="detail-form-item" v-if="detailType == 2">
            <el-form-item label="退货备注">
              <span>{{ detailForm.remark }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="0px" v-if="detailForm.deliveryDetailList && detailForm.deliveryDetailList.length > 0">
          <el-table v-loading="loading" :data="detailForm.deliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" width="200"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="150"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName"></el-table-column>
            <el-table-column label="已收货数量" align="center" prop="registrationCount" min-width="120"></el-table-column>
            <el-table-column label="退货数量" align="center" prop="returnCount" width="120" v-if="detailType == 1">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.returnCount'" :rules="detailRules.returnCount">
                  <el-input v-model="scope.row.returnCount" placeholder="请输入退货数量" v-intNumber size="small" maxlength="6" @blur="val => handleInputChange(val,'returnCount',scope.$index,scope.row)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="已退货数量" align="center" prop="returnCount" min-width="120" v-if="detailType == 2"></el-table-column>
            <el-table-column label="单价（元）" align="center" prop="price" min-width="100" v-if="detailType == 2"></el-table-column>
            <el-table-column label="财务凭证号" align="center" prop="financeVoucherNo" min-width="150"></el-table-column>
            <el-table-column label="批次" align="center" prop="batchCode" min-width="200"></el-table-column>
<!--            <el-table-column label="登记时间" align="center" prop="producedDate" width="210" v-if="detailType == 4 && detailForm.status >= 2 && detailForm.status != 9"></el-table-column>-->
          </el-table>
        </el-form-item>
        <el-form-item label="退货备注" v-if="detailType == 1">
          <el-input v-model="detailForm.remark" type="textarea" :rows="5" placeholder="请输入内容" show-word-limit maxlength="250" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDetailForm" v-if="detailType == 1">确 定</el-button>
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getCollection } from "@/api/inoutDelivery/inDelivery";

export default {
  name: "takeDeliveryReturnDetailCom",
  dicts: ['in_delivery_type','in_delivery_status','in_delivery_origin','in_delivery_detection_fail_type'],
  data() {
    let validateReturnCount= (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("退货数量不能为空"));
        this.$message.error("退货数量不能为空")
      } else if (Number(value) < 0) {
        callback(new Error("退货数量应大于等于0"));
        this.$message.error("退货数量应大于等于0")
      } else if (this.inputIndex !== '' && Number(value) > 0 && (Number(value) > Number(this.detailForm.deliveryDetailList[this.inputIndex].registrationCount))) {
        callback(new Error("退货数量应小于等于已收货数量"));
        this.$message.error("退货数量应小于等于已收货数量")
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
          { trigger: "blur", validator: validateReturnCount },
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
      getCollection(this.detailId).then(response => {
        this.detailForm = response.data
        if(this.detailType == '3'){
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
    submitDetailForm() {
      this.inputIndex = ''
      let errorMessage = []
      this.detailForm.deliveryDetailList.map((item) => {
        if(Number(item.returnCount) > Number(item.registrationCount)){
          errorMessage.push(`物料【${(item.materialName)}】的退货数量应小于已收货数量`)
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
