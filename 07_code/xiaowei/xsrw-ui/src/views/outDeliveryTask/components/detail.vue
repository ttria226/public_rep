<template>
  <div class="app-container">
    <el-dialog title="出库任务详情" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="detailForm" :model="detailForm" label-width="0px" style="padding-right: 30px">
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
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getOutDeliveryTaskShowDetail, } from "@/api/inoutDelivery/outDelivery";
export default {
  name: "warehouseTaskDetailCom",
  dicts: ["nextflag"],
  data(){
    return {
      //详情id
      id: null,
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
      getOutDeliveryTaskShowDetail(this.id).then(response => {
        this.detailForm.outDeliveryDetailList = response.data
        this.loading = false;
      });
    },
    /** 关闭按钮操作 */
    detailCancel(){
      this.open = false
      this.id = null
    }
  }
}
</script>