<template>
  <div class="app-container">
    <el-dialog title="出库单详情" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="出库单号">
              <span>{{ detailForm.code }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="出库单类型">
              <dict-tag :options="dict.type.inout_out_type" :value="detailForm.type" />
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="出库状态">
              <dict-tag :options="dict.type.inout_out_status" :value="detailForm.status" />
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
            <el-form-item label="申请人">
              <span>{{ detailForm.createBy }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="制单日期">
              <span>{{ detailForm.createTime }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb8">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="待出库区">
              <span>{{ detailForm.reservoirName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item" v-if="detailType == 2">
            <el-form-item label="审核原因">
              <span>{{ detailForm.checkRemark }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item" v-if="detailType == 2">
            <el-form-item label="审核人">
              <span>{{ detailForm.auditor }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb8">
          <el-col :span="8" class="detail-form-item" v-if="detailType == 2">
            <el-form-item label="备注">
              <span>{{ detailForm.remark }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="审核备注" v-if="detailType == 1">
          <el-input v-model="detailForm.checkRemark" type="textarea" :rows="5" placeholder="请输入内容" show-word-limit maxlength="250" />
        </el-form-item>
        <el-form-item label-width="30px">
          <el-table v-loading="loading" :data="detailForm.tOutDeliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="预计出库数量" align="center" prop="predictCount" width="150"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="materialUnit" width="100"></el-table-column>
            <!-- <el-table-column label="财务凭证号" align="center" prop="specifications"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" width="210"></el-table-column> -->
<!--            <el-table-column label="小件领取数量" align="center" prop="smallPredictCount" width="150"></el-table-column>-->
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDetailExamineForm('2')" v-if="detailType == 1">审核通过</el-button>
        <el-button type="danger" @click="submitDetailExamineForm('8')" v-if="detailType == 1">作废</el-button>
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { getOutDelivery, } from "@/api/inoutDelivery/outDelivery";

  export default {
    name: "outDeliveryPlanDetailCom",
    dicts: ['cims_inspection_method','inout_out_type','inout_out_status'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 是否显示弹出层
        open: false,
        current: null, // 当前选中的物料

        detailForm: {}, //详情
        detailType: '', //详情类型
        detailId: '',// 详情id
      };
    },
    watch: {
      open(){
        if (this.open) {
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
        getOutDelivery(this.detailId).then(response => {
          this.detailForm = response.data
          this.loading = false;
        });
      },
      // 取消按钮
      detailCancel() {
        this.open = false;
        this.detailForm = {}
        this.detailType = ''
        this.detailId = ''
        // this.reset();
      },
      /** 审核按钮 */
      submitDetailExamineForm(flag){
		  console.log('11111',flag)
		  this.$emit('setDetail', {...this.detailForm, flag})
     //    this.$refs["detailForm"].validate((valid) => {
     //      if (valid) {
			  // console.Console(vaild)
     //        this.$emit('setDetail', {...this.detailForm, flag})
     //      }
     //    });
      },
  }
};
</script>
