<template>
  <div class="app-container">
    <el-dialog title="选择物料rfid" :visible.sync="open" width="1200px" append-to-body>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
        <el-form-item label="RFID标识" prop="rfidHead">
          <el-input v-model="queryParams.rfidHead" placeholder="请输入RFID标识" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: center;margin-top: 10px;margin-bottom: 10px;">请选择{{reActualCount}}个RFID物料</div>
      <el-table :row-key="getRowKeys" v-loading="loading" :data="materialRfidList" @selection-change="handleSelectionChange" ref="tabSelect">
        <el-table-column type="selection" :reserve-selection="true" :selectable="checkSelectable" width="55" align="center" />
        <el-table-column label="RFID" align="center" prop="rfidHead" min-width="200"></el-table-column>
        <el-table-column label="物料编码" align="center" prop="materialCode" min-width="100"></el-table-column>
        <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
        <el-table-column label="计量单位" align="center" prop="unitName" min-width="90"></el-table-column>
        <el-table-column label="批次号" align="center" prop="batchCode" min-width="120"></el-table-column>
        <el-table-column label="对应物料数量" align="center" prop="rfidCount" min-width="200"></el-table-column>
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
  import { getTestRegistrationList } from "@/api/inoutDelivery/inDelivery";

  export default {
    name: "MaterialRfidCom",
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
          rfid: null,
        },
        pageNum: 1,
        pageSize: 10,

        selection: [], //当前选择的物料信息

        buttonLoading: false, //按钮loading

        id: null, //id参数
        reActualCount:null,
        trayId:'',// 多选的数据id
        materialId:'',// 多选的物料数据id
        again:false, // 是否从重新组盘进入的
        locationId:'' // 重新组盘之后得到locationId
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
        getRowKeys(row){
            return row.id;
        },
      /** 查询物料管理列表 */
      getList() {
        this.loading = true;
        console.log(this.again,"重新组盘进来的");
        if(this.again){
          getTestRegistrationList(this.id,{...this.queryParams, detectionFailStatus: 1, locationStatus: 3,locationId:this.locationId}).then(response => {
            this.materialRfidList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        }else{
          getTestRegistrationList(this.id,{...this.queryParams, detectionFailStatus: 1, locationStatus: 1}).then(response => {
            this.materialRfidList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        }

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
          this.queryParams.pageNum = this.pageNum
          this.queryParams.pageSize = this.pageSize
          this.getList();
        // this.$modal.confirm("只能提交上架当前页的物料，更换页码会导致之前输入内容消失，确定要继续吗？").then(() => {
        //   this.queryParams.pageNum = this.pageNum
        //   this.queryParams.pageSize = this.pageSize
        //   this.getList();
        // }).catch(() => {
        //   this.pageNum = this.queryParams.pageNum
        //   this.pageSize = this.queryParams.pageSize
        // });
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selection = JSON.parse(JSON.stringify(selection))

      },
      // 有库位的物料禁止选择
      checkSelectable(row, index) {
        if(this.again){
          return true;
        }else{
          return (row.locationName === null || row.locationName === undefined);
        }
      },
      // 选中行
      handleCurrentChange(row) {
        this.current = row
      },
      /** 提交按钮 */
      submitForm() {
        console.log(this.selection,333);
        let rfidCount = 0;
        this.selection.map((item) => {
          rfidCount = rfidCount + item.rfidCount
        })

        if(rfidCount != this.reActualCount ){
          this.$message.error('选择信息数量要等于“上架数量”')
        }else{
          if (this.selection && this.selection.length > 0) {
            let rfids = []
            this.selection.map((item) => {
              rfids.push(item.rfid)
            })
            console.log(11111);
            this.$emit('setMaterialRfid', { rfids, id: this.id ,trayId:this.trayId,again:this.again,materialId:this.materialId })
          } else {
            this.open = false
          }
        }

      },
  }
};
</script>
