<template>
  <el-dialog title="移库任务详情" :visible.sync="open" width="60%" append-to-body>
    <el-form ref="detailForm" :model="detailForm" label-width="30px" style="padding-right: 30px">
      <el-form-item>
        <el-table :data="detailForm.taskWcsDetailVOList" label-width="30px">
          <el-table-column label="物料编码" align="center" prop="materialCode" width="180"></el-table-column>
          <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
          <el-table-column label="计量单位" align="center" prop="unitName" width="100"></el-table-column>
          <el-table-column label="批次号" align="center" prop="batchCode" min-width="200"></el-table-column>
          <el-table-column label="预计移库数量" align="center" prop="predictCount" width="120"></el-table-column>
          <el-table-column label="实际移库数量" align="center" prop="actualCount" width="120"></el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getDetail" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitDetailForm" v-if="detailType == 3">执行</el-button>
      <el-button @click="cancel">关 闭</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { getMoveDeliveryDetail, executeMoveDelivery } from "@/api/inoutDelivery/outDelivery";
export default {
  name: "moveDeliveryControlDetailCom",
  dicts: ["wms_asn_origin_type", "wms_take_delivery_type", "wms_asn_status"],
  data(){
    return {
      // 总条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null
      },
      //弹窗标识
      open: false,
      //详情表单
      detailForm: {
        taskWcsDetailVOList: []
      },
      //详情类型
      detailType: '',
      //详情Id
      detailId: '',
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
    /** 查询详情 */
    getDetail() {
      this.loading = true;
      this.queryParams.id = this.detailId
      getMoveDeliveryDetail(this.queryParams).then(response => {
        this.detailForm.taskWcsDetailVOList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    //强制执行
    submitDetailForm(){
      this.$refs["detailForm"].validate((valid) => {
        if (valid) {
          let params = {
            id: this.detailId
          }
          executeMoveDelivery(params).then((response) => {
            this.$modal.msgSuccess("强制执行成功");
            this.open = false
            this.detailForm = {
              taskWcsDetailVOList: []
            }
            this.$emit("setControl",true)
          });
        }
      });
    },
    cancel(){
      this.open = false
      this.detailForm = {
        taskWcsDetailVOList: []
      }
    }
  }
}
</script>
