<template>
  <div class="app-container">
    <el-dialog title="波次单详情" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="波次单号">
              <span>{{ detailForm.code }}</span>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8" class="detail-form-item">
            <el-form-item label="单据类型">
              <dict-tag :options="dict.type.inout_out_type" :value="detailForm.type" />
            </el-form-item>
          </el-col> -->
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="状态">
              <dict-tag :options="dict.type.complete_state" :value="detailForm.status" />
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="物料使用部门">
              <span>{{ detailForm.deptName }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
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
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="审核人">
              <span>{{ detailForm.auditor }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb8">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="审核原因">
              <span>{{ detailForm.remark }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="30px">
          <el-table v-loading="loading" :data="detailForm.detail">
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" min-width="100"></el-table-column>
            <el-table-column label="预计出库数量" align="center" prop="num" min-width="120"></el-table-column>
            <!-- <el-table-column label="小件领取数量" align="center" prop="smallPredictCount" width="150"></el-table-column> -->
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { getMergeDeliveryDetail, } from "@/api/inoutDelivery/outDelivery";

  export default {
    name: "waveManagementDetailCom",
    dicts: ['inout_out_type','complete_state'],
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
        getMergeDeliveryDetail(this.detailId).then(response => {
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
  }
};
</script>