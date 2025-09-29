<template>
  <div class="app-container">
    <el-dialog :title="detailTitle" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="120px" style="padding-right: 30px">
        <el-form-item label-width="0px" v-if="detailForm.deliveryDetailList && detailForm.deliveryDetailList.length > 0">
          <el-table v-loading="loading" :data="detailForm.deliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="materialCode" width="200"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="150"></el-table-column>
            <!-- <el-table-column label="计量单位" align="center" prop="unitName"></el-table-column> -->
            <el-table-column label="批次号" align="center" prop="batchCode" min-width="200"></el-table-column>
            <el-table-column label="库存数量" align="center" prop="predictCount" width="120"></el-table-column>
            <el-table-column label="库位" align="center" prop="locationName" min-width="120"></el-table-column>
            <el-table-column label="库区" align="center" prop="reservoirName" width="120"></el-table-column>
            <el-table-column label="载具编号" align="center" prop="trayCode" width="120"></el-table-column>
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
  import { getMergeDeliveryTaskDetail } from "@/api/inoutDelivery/outDelivery";

  export default {
    name: "waveAllocationDetailCom",
    dicts: [],
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
        getMergeDeliveryTaskDetail({ mergeDeliveryId: this.detailId }).then(response => {
          this.detailForm.outDeliveryDetailList = response.data
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
  }
};
</script>
