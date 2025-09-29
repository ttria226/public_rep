<template>
  <div class="app-container">
    <el-dialog title="选择物料rfid" :visible.sync="open" width="1200px" append-to-body>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
        <el-form-item label="RFID标识" prop="rfid">
          <el-input v-model="queryParams.rfidHead" placeholder="请输入RFID标识" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="materialRfidList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="物料编码" align="center" prop="materialCode" min-width="130"></el-table-column>
        <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
        <el-table-column label="计量单位" align="center" prop="unitName" min-width="90"></el-table-column>
        <el-table-column label="批次号" align="center" prop="batchCode" min-width="120"></el-table-column>
        <el-table-column label="RFID标识" align="center" prop="rfidHead" min-width="240"></el-table-column>
        <!-- <el-table-column label="库位" align="center" prop="locationName" min-width="120"></el-table-column> -->
      </el-table>

      <pagination v-show="total > 0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" @pagination="pagination" />

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="buttonLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel" :loading="buttonLoading">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getOutDeliveryMaterialRfidList } from "@/api/inoutDelivery/outDelivery";

  export default {
    name: "OutMaterialRfidCom",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 物料rfid表格数据
        materialRfidList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          rfidHead: null,
        },
        pageNum: 1,
        pageSize: 10,

        selection: [], //当前选择的物料信息

        buttonLoading: false, //按钮loading

        locationId: null, //库位参数
        batchCode: null, //批次号参数
      };
    },
    watch: {
      open(){
        if (this.open) {
          this.getList();
        }
      }
    },
    created() {
      // this.getList();
      // this.getModeList();
    },
    methods: {

      /** 查询物料管理列表 */
      getList() {
        this.loading = true;
        getOutDeliveryMaterialRfidList({...this.queryParams, locationId: this.locationId,
          batchCode: this.batchCode, materialId:this.materialId }).then(response => {
          this.materialRfidList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        // this.reset();
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 列表分页事件 */
      pagination(info){
        this.$modal.confirm("只能提交上架当前页的物料，更换页码会导致之前输入内容消失，确定要继续吗？").then(() => {
          this.queryParams.pageNum = this.pageNum
          this.queryParams.pageSize = this.pageSize
          this.getList();
        }).catch(() => {
          this.pageNum = this.queryParams.pageNum
          this.pageSize = this.queryParams.pageSize
        });
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selection = JSON.parse(JSON.stringify(selection))
      },
      // 有库位的物料禁止选择
      checkSelectable(row, index) {
        return (row.locationName === null || row.locationName === undefined);
      },
      // 选中行
      handleCurrentChange(row) {
        this.current = row
      },
      /** 提交按钮 */
      submitForm() {
        if (this.selection && this.selection.length > 0) {
          let rfids = []
          this.selection.map((item) => {
            rfids.push(item.rfid)
          })
          this.$emit('setMaterialRfid', { rfids, batchCode: this.batchCode })
        } else {
          this.open = false
        }
      },
  }
};
</script>
