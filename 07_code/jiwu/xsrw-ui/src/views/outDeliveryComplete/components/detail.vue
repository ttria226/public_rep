<template>
  <div class="app-container">
    <el-dialog title="出库单详情" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="110px" style="padding-right: 30px" v-if="!isFinish">
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
          <el-col :span="8" class="detail-form-item" v-if="detailType == 4">
            <el-form-item label="审核原因">
              <span>{{ detailForm.remark }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item" v-if="detailType == 4">
            <el-form-item label="审核人">
              <span>{{ detailForm.auditor }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item" v-if="detailType == 4">
            <el-form-item label="物料BOM组合">
              <span>{{ detailForm.bomName }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="审核备注" v-if="detailType == 3">
          <el-input v-model="detailForm.remark" type="textarea" :rows="5" placeholder="请输入内容" show-word-limit maxlength="250" />
        </el-form-item>
        <el-form-item label-width="30px">
          <el-table v-loading="loading" :data="detailForm.tOutDeliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="预计出库数量" align="center" prop="predictCount" min-width="120"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="materialUnit" width="120"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" min-width="180"></el-table-column>
            <!-- <el-table-column label="财务凭证号" align="center" prop="specifications"></el-table-column> -->
            <!-- <el-table-column label="小件领取数量" align="center" prop="smallPredictCount" width="150"></el-table-column> -->
          </el-table>
        </el-form-item>
      </el-form>
      <el-form ref="detailForm" :model="detailForm" label-width="0px" style="padding-right: 30px" v-if="isFinish">
        <el-form-item>
          <el-table v-loading="loading" :data="detailForm.outDeliveryDetailList">
            <el-table-column label="载具编号" align="center" prop="trayCode" min-width="180"></el-table-column>
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="库存数量" align="center" prop="predictCount" width="120"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" min-width="120"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" min-width="200"></el-table-column>
            <el-table-column label="库位" align="center" prop="locationName" width="150"></el-table-column>
            <el-table-column label="库区" align="center" prop="reservoirName" width="150"></el-table-column>
            <!-- <el-table-column label="状态" align="center" prop="status" width="100">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.nextflag" :value="scope.row.status" />
              </template>
            </el-table-column> -->
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDetailExamineForm('2')" v-if="detailType == 3">审核通过</el-button>
        <el-button type="primary" @click="submitDetailExamineForm('9')" v-if="detailType == 3">审核不通过</el-button>
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getOutDelivery, getOutDeliveryTaskShowDetail } from "@/api/inoutDelivery/outDelivery";

export default {
  name: "outDeliveryCompleteDetailCom",
  dicts: ['inout_out_type','inout_out_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 是否显示弹出层
      open: false,
      current: null, // 当前选中的物料

      detailForm: {}, //详情
      detailType: '', //详情类型
      isFinish: false,//详情状态
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
      if(this.isFinish){
        getOutDeliveryTaskShowDetail(this.id).then(response => {
          this.detailForm.outDeliveryDetailList = response.data
          this.loading = false;
        });
      } else {
        getOutDelivery(this.detailId).then(response => {
          this.detailForm = response.data
          this.loading = false;
        });
      }
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
      this.$emit('setDetail', {...this.detailForm, flag})
    },
  }
};
</script>