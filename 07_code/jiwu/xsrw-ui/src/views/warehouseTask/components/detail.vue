<template>
  <div class="app-container">
    <el-dialog title="入库任务详情" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20">
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="入库单号">
              <span>{{ detailForm.advanceDeliveryCode }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="入库单类型">
              <dict-tag :options="dict.type.in_delivery_type" :value="detailForm.advanceDeliveryType" />
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
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="备注">
              <span>{{ detailForm.advanceDeliveryRemark }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="detail-form-item">
            <el-form-item label="审核备注">
              <span>{{ detailForm.auditRemark }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label-width="0px">
          <el-table v-loading="loading" :data="detailForm.taskInList">
            <el-table-column label="载具编号" align="center" prop="trayCode"  min-width="200"></el-table-column>
            <!-- <el-table-column label="载具状态" align="center" prop="trayStatus"></el-table-column> -->
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="200"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName"  min-width="200"></el-table-column>
            <el-table-column label="上架数量" align="center" prop="actualCount"  min-width="100"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName"  min-width="100"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode"  min-width="200"></el-table-column>
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
import { getInDeliveryTask } from "@/api/inoutDelivery/inDelivery";
export default {
  name: "warehouseTaskDetailCom",
  dicts: ['in_delivery_type','in_delivery_status','in_delivery_origin'],
  data(){
    return {
      detailId: '', //详情id
      //弹窗显示标识
      open: false,
      //加载标识
      loading: false,
      //详情表单
      detailForm: {}
    }
  },
  watch: {
    open(){
      if (this.open) {
        this.getDetail();
      }
    },
  },
  methods: {
    /** 获取详情操作 */
    getDetail(){
      this.loading = true;
      getInDeliveryTask(this.detailId).then(response => {
        this.detailForm = response.data
        this.loading = false;
      });
    },
    /** 关闭按钮操作 */
    detailCancel(){
      this.open = false
    }
  }
}
</script>
