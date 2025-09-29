<template>
  <div class="app-container">
    <el-dialog :title="detailTitle" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="120px" style="padding-right: 30px">
        <el-row :gutter="20">
          <el-col :span="8" class="detail-form-item">
            <el-form-item :label="detailType == '2' ? '收货单号' : '入库单号'">
              <span>{{ detailForm.code }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item :label="detailType == '2' ? '单据类型' : '入库类型'">
              <dict-tag :options="dict.type.in_delivery_type" :value="detailForm.type" />
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item :label="detailType == '2' ? '状态' : '入库状态'">
              <!-- <dict-tag :options="dict.type.in_delivery_status" :value="detailForm.status" /> -->
              <span v-for="item in dict.type.in_delivery_status" :key="item.value" v-show="detailForm.status == item.value">{{ detailForm.status == '2' ? '待收货' : item.label }}</span>
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
            <el-form-item label="物料BOM组合">
              <span>{{ detailForm.bomName }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="0px" v-if="detailForm.deliveryDetailList && detailForm.deliveryDetailList.length > 0">
          <el-table v-loading="loading" :data="detailForm.deliveryDetailList">
            <!-- <el-table-column label="物料BOM组合" align="center" prop="bomName" width="200"></el-table-column> -->
            <el-table-column label="物料编码" align="center" prop="materialCode" width="200"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="150"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" min-width="200"></el-table-column>
            <!-- <el-table-column label="生产日期" align="center" prop="producedDate" min-width="150"></el-table-column> -->
            <el-table-column label="预计入库数量" align="center" prop="predictCount" width="120"></el-table-column>
            <el-table-column label="收货数量" align="center" prop="registrationCount" width="120" v-if="detailType == 2">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'deliveryDetailList.' + scope.$index + '.registrationCount'" :rules="detailRules.registrationCount">
                  <el-input v-model="scope.row.registrationCount" placeholder="请输入收货数量" v-intNumber size="small" maxlength="6" @blur="val => handleInputChange(val,'registrationCount',scope.$index,scope.row)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="已入库数量" align="center" :prop="detailType == 2 ? 'registrationInCount' : 'registrationCount'" min-width="120" v-if="detailType == 2"></el-table-column>
            <el-table-column label="单价（元）" align="center" prop="price" width="120" v-if="detailType == 3"></el-table-column>
            <el-table-column label="财务凭证号" align="center" prop="financeVoucherNo" width="120" v-if="detailType == 3"></el-table-column>
<!--            <el-table-column label="登记时间" align="center" prop="producedDate" width="210" v-if="detailType == 4 && detailForm.status >= 2 && detailForm.status != 9"></el-table-column>-->
          </el-table>
        </el-form-item>
        <!-- <el-form-item label="审核备注" v-if="detailType == 1">
          <el-input v-model="detailForm.remark" type="textarea" :rows="5" placeholder="请输入内容" show-word-limit maxlength="250" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="submitDetailExamineForm('2')" v-if="detailType == 1">审核通过</el-button>
        <el-button type="primary" @click="submitDetailExamineForm('8')" v-if="detailType == 1">作废</el-button> -->
        <el-button type="primary" @click="submitDetailRegistrationForm" v-if="detailType == 2">确定收货</el-button><!--submitDetailCheckForm-->
        <!-- <el-button type="primary" @click="submitDetailRegistrationForm" v-if="detailType == 3">登记入库</el-button> -->
        <el-button type="primary" @click="submitDetailTemplateForm" v-if="detailType == 3 && detailForm.status >= 2 && detailForm.status != 9">标签打印</el-button>
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { getInDelivery } from "@/api/inoutDelivery/inDelivery";

  import { getRuleStatus } from "@/api/base/rule";

  export default {
    name: "warehouseDetailCom",
    dicts: ['in_delivery_type','in_delivery_status','in_delivery_origin','in_delivery_detection_fail_type'],
    data() {
      let validateRegistrationCount= (rule, value, callback) => {
        if (value === null || value === '') {
          callback(new Error("收货数量不能为空"));
          this.$message.error("收货数量不能为空")
        } else if (Number(value) < 0) {
          callback(new Error("收货数量应大于等于0"));
          this.$message.error("收货数量应大于等于0")
        } else if (!this.isNoLimit && this.inputIndex !== '' && Number(value) > 0 && (Number(value) > (Number(this.detailForm.deliveryDetailList[this.inputIndex].predictCount) - Number(this.detailForm.deliveryDetailList[this.inputIndex].receiveCount)))) {
          callback(new Error("收货数量应小于预计入库数量"));
          this.$message.error("收货数量应小于预计入库数量")
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
          registrationCount: [
            { trigger: "blur", validator: validateRegistrationCount },
          ],
          // detectionCount: [
          //   { trigger: "blur", validator: validateDetectionCount },
          // ],
        },

        isNoLimit: false, //不限制登记数量
      };
    },
    watch: {
      open(){
        if (this.open) {
          this.getAuth()
          this.getDetail();
        }
      },
    },
    created() {
      // this.getList();
      // this.getModeList();
    },
    methods: {
      /** 查询详情 */
      getDetail() {
        this.loading = true;
        getInDelivery(this.detailId).then(response => {
          this.detailForm = response.data
          if(this.detailType == '2'){
            this.detailForm.deliveryDetailList.map((item) => {
              item.registrationInCount = item.registrationCount
            })
          }
          this.loading = false;
        });
      },
      /** 获取是否不限制登记数量 */
      getAuth(){
        getRuleStatus(6).then(res => {
          this.isNoLimit = res.data == '1' ? true : false
        })
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
      /** 检测提交按钮 */
      submitDetailCheckForm() {
        this.inputIndex = ''
        let errorMessage = []
        this.detailForm.deliveryDetailList.map((item) => {
          if(Number(item.detectionCount) > Number(item.predictCount)){
            errorMessage.push(`物料【${(item.materialName)}】的检测不通过数量应小于预计入库数量`)
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
      /** 审核按钮 */
      submitDetailExamineForm(flag){
        this.$refs["detailForm"].validate((valid) => {
          if (valid) {
            this.$emit('setDetail', {...this.detailForm, flag})
          }
        });
      },
      /** 登记按钮 */
      submitDetailRegistrationForm(){
        this.inputIndex = ''
        let errorMessage = []
        this.detailForm.deliveryDetailList.map((item) => {
          if(Number(item.registrationCount) > (Number(item.predictCount) - Number(item.receiveCount))){
            errorMessage.push(`物料【${(item.materialName)}】的收货数量应小于预计入库数量`)
          }
        })
        if(errorMessage.length > 0){
          this.$message.error(errorMessage.join("，"))
          return false
        }
        this.$refs["detailForm"].validate((valid) => {
          if (valid) {
            console.log(this.detailForm)
            this.$emit('setDetail', this.detailForm)
          }
        });
      },
      /** 标签打印 */
      submitDetailTemplateForm(){
        this.$emit('templateGet')
        this.open = false
      }
  }
};
</script>
