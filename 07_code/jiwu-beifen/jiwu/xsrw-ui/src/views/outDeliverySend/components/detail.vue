<template>
  <div class="app-container">
    <el-dialog :title="detailTitle" :visible.sync="open" width="60%" append-to-body>
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
          <!-- <el-col :span="8" class="detail-form-item">
            <el-form-item label="出库状态">
              <dict-tag :options="dict.type.wms_out_recheck_status" :value="detailForm.status" />
            </el-form-item>
          </el-col> -->
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="物料使用部门">
              <span>{{ detailForm.deptName }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
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
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="来源">
              <dict-tag :options="dict.type.inout_out_local" :value="detailForm.newLocal" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="10" class="detail-form-item" v-if="detailType && detailType != 1">
            <el-form-item label="审核备注">
              <span>{{ detailForm.remark }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="0px" v-if="detailForm.tOutDeliveryDetailList && detailForm.tOutDeliveryDetailList.length > 0">
          <el-table v-loading="loading" :data="detailForm.tOutDeliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" width="200"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="150"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="materialUnit"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" min-width="200"></el-table-column>
            <el-table-column label="预计出库数量" align="center" prop="predictCount" width="120"></el-table-column>
            <el-table-column label="已出库数量" align="center" prop="receiveCount" width="210"></el-table-column>
            <!-- <el-table-column label="状态" align="center" prop="detectionFailType" width="210">
              <template slot-scope="scope">
                <span style="color: red;" v-if="scope.row.registrationCount == 0">{{ scope.row.detectionCount }}个通过</span>
                <span style="color: green;" v-else-if="scope.row.predictCount == scope.row.detectionCount">全部通过</span>
              </template>
            </el-table-column> -->
            <el-table-column label="出库时间" align="center" prop="createTime" width="210"></el-table-column>
            <el-table-column label="财务凭证号" align="center" prop="financeVoucherNo" min-width="150"></el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="submitDetailTemplateForm">标签打印</el-button> -->
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getOutDeliverySend } from "@/api/inoutDelivery/outDelivery";

export default {
  name: "outDeliverySendDetailCom",
  dicts: ['inout_out_type','wms_out_recheck_status','inout_out_local'],
  data() {
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
      getOutDeliverySend(this.detailId).then(response => {
        this.detailForm = response.data
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
    },
    /** 标签打印 */
    submitDetailTemplateForm(){
      this.$emit('templateGet')
      this.open = false
    }
  }
};
</script>
