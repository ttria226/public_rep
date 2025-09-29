<template>
  <div class="app-container">
    <el-dialog :title="detailTitle" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="100px" style="padding-right: 30px">
        <!-- <el-row :gutter="20"> -->
          <!-- <el-col :span="8" class="detail-form-item">
            <el-form-item label="调拨单号">
              <span>{{ detailForm.code }}</span>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="8" class="detail-form-item">
            <el-form-item label="移库类型">
              <dict-tag :options="dict.type.in_delivery_type" :value="detailForm.type" />
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="8" class="detail-form-item">
            <el-form-item label="单据状态">
              <dict-tag :options="dict.type.wms_allot_status" :value="detailForm.status" />
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="关联单号">
              <span>{{ detailForm.deptName }}</span>
            </el-form-item>
          </el-col> -->
        <!-- </el-row> -->
        <!-- <el-row :gutter="20">
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
            <el-form-item label="备注">
              <span>{{ detailForm.remark }}</span>
            </el-form-item>
          </el-col>
        </el-row> -->
        <!-- <el-row :gutter="20">
          <el-col :span="16" class="detail-form-item" v-if="detailType && detailType != 1">
            <el-form-item label="审核原因">
              <span>{{ detailForm.remark }}</span>
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-form-item label="审核备注" v-if="detailType == 1">
          <el-input v-model="detailForm.remark" type="textarea" :rows="5" placeholder="请输入内容" show-word-limit maxlength="250" />
        </el-form-item>
        <!-- <el-form-item label-width="0px" v-if="detailForm.deliveryDetailList && detailForm.deliveryDetailList.length > 0">
          <el-table v-loading="loading" :data="detailForm.deliveryDetailList">
            <el-table-column label="产品编码" align="center" prop="materialCode" width="200"></el-table-column>
            <el-table-column label="产品名称" align="center" prop="materialName" min-width="150"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" min-width="150"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode" min-width="200"></el-table-column>
            <el-table-column label="数量" align="center" prop="allotNum" width="120"></el-table-column>
            <el-table-column label="当前仓库" align="center" prop="outWarehouseName"></el-table-column>
            <el-table-column label="移入仓库" align="center" prop="inWarehouseName" min-width="150"></el-table-column>
          </el-table>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDetailExamineForm('1')" v-if="detailType == 1">审核通过</el-button>
        <el-button type="primary" @click="submitDetailExamineForm('5')" v-if="detailType == 1">审核不通过</el-button>
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { getInDelivery } from "@/api/inoutDelivery/allot";

  export default {
    name: "allotPlanDetailCom",
    dicts: ['in_delivery_type','wms_allot_status'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 弹出层标题
        detailTitle: "",
        // 是否显示弹出层
        open: false,

        detailForm: {}, //详情
        detailType: '', //详情类型
        detailId: '', //详情id
      };
    },
    watch: {
      open(){
        if (this.open) {
          // this.getDetail();
          this.reset()
        }
      },
    },
    methods: {
      /** 查询详情 */
      getDetail() {
        this.loading = true;
        getInDelivery(this.detailId).then(response => {
          this.detailForm = response.data
          this.loading = false;
        });
      },
      //重置表单
      reset(){
        this.detailForm = {
          id: null,
          remark: null,
        }
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
      /** 审核按钮 */
      submitDetailExamineForm(flag){
        this.$refs["detailForm"].validate((valid) => {
          if (valid) {
            this.detailForm.id = this.detailId
            this.$emit('setDetail', {...this.detailForm, flag})
          }
        });
      },
  }
};
</script>
