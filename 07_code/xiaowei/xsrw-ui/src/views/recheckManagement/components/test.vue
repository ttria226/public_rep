<template>
  <el-dialog title="复检" :visible.sync="testOpen" width="60%" append-to-body>
    <el-form :model="dialogQueryParams" ref="dialogQueryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="物料编码" prop="materialCode"><!-- prop="materialCode" prop="materialId" -->
        <el-input v-model="dialogQueryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleDialogQuery" />
        <!-- <el-select v-model="dialogQueryParams.materialId" clearable placeholder="请选择物料">
          <el-option v-for="item in testRegistrationList" :key="item.id" :label="item.materialName" :value="item.id" />
        </el-select> -->
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="dialogQueryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleDialogQuery" />
      </el-form-item>
      <el-form-item label="RFID标识" prop="rfid">
        <el-input v-model="dialogQueryParams.rfid" placeholder="请输入RFID标识" clearable @keyup.enter.native="handleDialogQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleDialogQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetDialogQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-form ref="testForm" :model="testForm" label-width="00px" style="padding-right: 30px">
      <el-form-item prop="deliveryDetailList">
        <el-table v-loading="loading" :data="testForm.deliveryDetailList">
          <el-table-column label="物料编码" align="center" prop="materialCode" min-width="120"></el-table-column>
          <el-table-column label="物料名称" align="center" prop="materialName" min-width="160"></el-table-column>
          <el-table-column label="计量单位" align="center" prop="unitName" min-width="90"></el-table-column>
          <el-table-column label="批次号" align="center" prop="batchCode" min-width="120"></el-table-column>
          <el-table-column label="RFID标识" align="center" prop="rfid" min-width="120"></el-table-column>
          <!-- <el-table-column label="检测状态" align="center" prop="detectionFailStatus" min-width="120">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.wms_material_detail_check" :value="scope.row.detectionFailStatus" />
            </template>
          </el-table-column> -->
          <el-table-column label="未通过原因" align="center" prop="detectionFailType" width="180">
            <template slot-scope="scope">
              <el-form-item label-width="0px">
                <el-select style="width: 100%;" v-model="scope.row.detectionFailType" placeholder="请选择未通过原因" @change="val => handleDialogSelectChange(val,'detectionFailType',scope.$index,scope.row)">
                  <el-option v-for="dict in dict.type.in_delivery_detection_fail_type" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
                <!-- <el-input v-model="scope.row.detectionFailType" placeholder="请输入未通过原因" size="small" ></el-input> -->
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="detectionFailRemark" width="180">
            <template slot-scope="scope">
              <el-form-item label-width="0px">
                <el-input v-model="scope.row.detectionFailRemark" placeholder="请输入备注" size="small" maxlength="250" @blur="val => handleDialogInputChange(val,'detectionFailRemark',scope.$index,scope.row)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="dialogTotal > 0" :total="dialogTotal" :page.sync="testPageNum" :limit.sync="testPageSize" @pagination="dialogPagination" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitTestForm">确 定</el-button>
      <el-button @click="testCancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { getRecheckMaterialDetailList, recheckCheckFail } from "@/api/wms/recheck";

export default {
  name: 'recheckTestCom',
  dicts: ["in_delivery_detection_fail_type"],
  data(){
    return{
      // 遮罩层
      loading: true,
      detailId: null, //详情id
      testOpen: false, //检测弹窗标识
      // 检测弹窗总条数
      dialogTotal: 0,
      // 检测弹窗查询参数
      dialogQueryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        // materialId: null,
        materialName: null,
        rfid: null,
      },
      testPageNum: 1,
      testPageSize: 10,
      // 检测弹窗表单参数
      testForm: {
        deliveryDetailList: []
      },

      testRegistrationList: [], //检测的物料下拉
    }
  },
  watch: {
    testOpen(){
      if (this.testOpen) {
        this.getList();
      }
    },
  },
  methods: {
    /** 获取检测的物料下拉列表 */
    getTestRegistrationSelectList(){
      console.log(this.detailId)
      getRecheckMaterialDetailList(this.detailId).then((response) => {
        this.testRegistrationList = response.data;
        this.dialogQueryParams.materialId = response.data && response.data.length > 0 ? response.data[0].id : ""
        this.getList()
      });
    },
    /** 查询复检入库的列表 */
    getList() {
      this.loading = true;
      let params = {...this.dialogQueryParams}
      params.id = this.detailId
      delete params.materialId
      getRecheckMaterialDetailList(params).then((response) => {
        this.testForm.deliveryDetailList = response.rows;
        this.dialogTotal = response.total;
        this.loading = false;
      });
    },
    //输入框变化事件
    handleDialogInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.testForm.deliveryDetailList,index,info)
    },
    //选择变化事件
    handleDialogSelectChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val
      this.$set(this.testForm.deliveryDetailList,index,info)
    },
    /** 检测弹窗搜索按钮操作 */
    handleDialogQuery() {
      this.dialogQueryParams.pageNum = 1;
      this.getList();
    },
    /** 检测弹窗重置按钮操作 */
    resetDialogQuery() {
      this.resetForm("dialogQueryForm");
      // this.dialogQueryParams.materialId = this.testRegistrationList && this.testRegistrationList.length > 0 ? this.testRegistrationList[0].id : ""
      this.handleDialogQuery();
    },
    /** 检测列表分页事件 */
    dialogPagination(info){
      this.$modal.confirm("只能提交检测当前页的物料，更换页码会导致之前输入内容消失，确定要继续吗？").then(() => {
        this.dialogQueryParams.pageNum = this.testPageNum
        this.dialogQueryParams.pageSize = this.testPageSize
        this.getList();
      }).catch(() => {
        this.testPageNum = this.dialogQueryParams.pageNum
        this.testPageSize = this.dialogQueryParams.pageSize
      });
    },
    // 提交检测
    submitTestForm(){
      let list = []
      this.testForm.deliveryDetailList.map((item) => {
        if(item.detectionFailType && item.detectionFailStatus == '0'){
          let info = {
            id: item.id,
            detectionFailType: item.detectionFailType,
            detectionFailRemark: item.detectionFailRemark
          }
          list.push(info)
        }
      })
      if(list.length === 0){
        this.$message.warning("请至少输入一条物料的检测信息！")
        return false
      }
      this.$refs["testForm"].validate((valid) => {
        if (valid) {
          recheckCheckFail(list).then((response) => {
            this.$modal.msgSuccess("复检入库成功");
            this.testOpen = false
            this.$emit('setTest', true)
          });
        }
      });
    },
    //检测取消
    testCancel(){
      this.testOpen = false
      this.resetForm("dialogQueryForm");
      this.testForm = {
        deliveryDetailList: []
      }
      this.testPageNum = 1
      this.testPageSize = 10
    },
  }
}
</script>