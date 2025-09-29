<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input
          v-model="queryParams.materialCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

   <el-row :gutter="10" class="mb8">
     <el-col :span="1.5">
       <el-button
         type="warning"
         plain
         icon="el-icon-download"
         size="mini"
         @click="handleExport"
         v-hasPermi="['wms:checkResult:export']"
       >导出</el-button>
     </el-col>
     <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
   </el-row>

    <el-table v-loading="loading" :data="stockAmendList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="stockId" fixed min-width="100" align="center" prop="stockId" :show-overflow-tooltip="true" v-if="1==2"/>
      <el-table-column label="批次号" fixed min-width="100" align="center" prop="batchCode" :show-overflow-tooltip="true" v-if="1==2"/>
      <el-table-column label="RFID标签ID" fixed min-width="100" align="center" prop="rfidHead" :show-overflow-tooltip="true"/>
      <el-table-column label="物料编码" fixed min-width="100" align="center" prop="materialCode" :show-overflow-tooltip="true"/>
      <el-table-column label="物料名称" fixed min-width="100" align="center" prop="materialName" :show-overflow-tooltip="true"/>
      <el-table-column label="库位" fixed min-width="100" align="center" prop="locationName" :show-overflow-tooltip="true"/>
      <el-table-column label="当前库存数量" fixed align="center"  width="220px" prop="stockCount" :show-overflow-tooltip="true" />
      <el-table-column label="调整次数" fixed align="center" prop="tzcs" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleResultTiaoZheng(scope.row)" v-hasPermi="['wms:checkResult:edit']">库存调整</el-button>
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleResultDetailList(scope.row)" v-hasPermi="['wms:checkResult:edit']">调整记录</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 库存调整 -->
    <el-dialog :title="title" :visible.sync="open" width="30%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="原库存数量" prop="beforeCount">
          <span>{{form.beforeCount}}</span>
        </el-form-item>
        <!-- <el-form-item label="新库存数量" prop="currCount">
          <el-input v-model="form.currCount" placeholder="请输入新库存数量" />
        </el-form-item> -->
        <el-form-item label="调整类型" prop="changeType">
          <el-radio-group v-model="form.changeType">
            <el-radio
              v-for="dict in dict.type.stock_change_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="调整数量" prop="changeNum">
          <el-input-number v-model="form.changeNum" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="调整原因" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 调整记录 -->
    <el-dialog title="调整记录" :visible.sync="isResultOpen" v-if="isResultOpen" width="60%" append-to-body>
        <el-table :data="formResultDetailList" @selection-change="handlePanChaChange">
          <el-table-column label="调整时间" fixed min-width="150" align="center" prop="createTime" :show-overflow-tooltip="true" />
          <el-table-column label="原库存数量" fixed min-width="150" align="center" prop="beforeCount" :show-overflow-tooltip="true" />
          <el-table-column label="调整数量" fixed min-width="150" align="center" prop="changeNum" :show-overflow-tooltip="true" >
            <template slot-scope="scope">
              <p v-if="scope.row.changeType==0"><span style="color:red">+</span>{{scope.row.changeNum}}</p>
              <p v-if="scope.row.changeType==1"><span style="color:red">-</span>{{scope.row.changeNum}}</p>
            </template>
          </el-table-column>
          <el-table-column label="新库存数量" fixed min-width="150" align="center" prop="changeType" :show-overflow-tooltip="true" >
            <template slot-scope="scope">
                <p v-if="scope.row.changeType==0">{{scope.row.beforeCount + scope.row.changeNum}}</p>
                <p v-if="scope.row.changeType==1">{{scope.row.beforeCount - scope.row.changeNum}}</p>
            </template>
          </el-table-column>
          <el-table-column label="调整原因" fixed min-width="150" align="center" prop="remark" :show-overflow-tooltip="true" />
          <el-table-column label="调整人" fixed min-width="150" align="center" prop="createBy" :show-overflow-tooltip="true" />
        </el-table>
        <pagination v-show="totalSee>0" :total="totalSee" :page.sync="SeeResultParams.pageNum"
				:limit.sync="SeeResultParams.pageSize" @pagination="getSeeList" />
    </el-dialog>


  </div>
</template>

<script>
import { liststockResult,addCheckChange,liststockLog } from "@/api/wms/pdstockAmend";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { mapGetters } from 'vuex';
import { treeselect } from "@/api/system/dept";
import { listMaterial } from "@/api/wms/material";
import { wms } from '@/utils/agent';
export default {
  name: "stockAmend",
  components:{Treeselect},
  computed: {
    ...mapGetters([
      'deptId','token'
    ]),
  },
  dicts:['stock_change_type'],
  data() {
    let regulationTypeRules = (rule, value, callback) => {
				if (!this.formStatus.status) {
					callback(new Error('请选择状态'));
				} else {
					callback();
				}
			}
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
      // 盘点差异报表表格数据
      stockAmendList: [],
      formResultDetailList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        factory:null,
        materialId:null,
        startDate:null,
        endDate:null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // newStockNum:[
        //   { required: true, message: '请输入新库存数量', trigger: 'blur' }
        // ],
        changeType: [{
						required: true,
						message: '请选择调整类型',
						trigger: 'change'
					}],
        changeNum: [
          { required: true, message: "调整数量不能为空", trigger: "blur" }
        ],
      },
      codeList:[
        {dictLabel:'箱',dictValue:1},
        {dictLabel:'个',dictValue:2},
      ],
      // 上传列表
      upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {  },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      fileListd:[],
      // 部门列表
      department:[],
      // 仓库列表
      currentList:[],
      isResultOpen:false,
      totalSee: 10,
      SeeResultParams: {
					pageNum: 1,
					pageSize: 10,
					planId: null,
					materialId: null,
				},
        formSeeIds: [],
        statusOpen:false,
          rulesStatus: {
					status: [{
						required: true,
						validator: regulationTypeRules,
						trigger: "change"
					}],
				},
         formStatus: {
					status: null,
				},
        formStatusList: [{
						dictLabel: '通过',
						dictValue: 4
					},
					{
						dictLabel: '驳回',
						dictValue: 5
					}
				],
    };
  },
  created() {
    this.upload.headers = {Authorization: "Bearer " + this.token}
    this.getList();
    // this.treeselect()
    // this.listMaterial()
  },
  watch: {
    $route(to, form) {
      if (to.name == 'CheckCheckResult') {
        this.getList()
      }
    }
  },
  methods: {
    // 部门下拉列表
    treeselect(){
      treeselect().then(res=>{
        this.department = res.data
      })
    },
    // 物料下拉列表
    listMaterial(){
      listMaterial({pageNum: 1,pageSize: 1000,}).then(res=>{
        this.codeList = res.rows;
      })
    },
    // 盘差正负号处理符号
    checkFormat(row,column){
      let courseType = '';
      if(row.checkDifferenceCount > 0){
        courseType = '+' + row.checkDifferenceCount
      }else{
        courseType = row.checkDifferenceCount
      }
      return courseType;
    },
    /** 查询盘点差异报表列表 */
    getList() {
      this.loading = true;
      liststockResult(this.queryParams).then(response => {
        this.stockAmendList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        stockId:null,
        beforeCount:0,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.factory = null
      this.queryParams.materialId = null
      this.queryParams.startDate = null
      this.queryParams.endDate = null
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加盘点差异报表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCheckResult(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改盘点差异报表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addCheckChange(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
        return delCheckResult(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/checkResult/export', {
        ...this.queryParams
      }, `checkResult_${new Date().getTime()}.xlsx`)
    },
    // 单据导入
    ImportBill(){
      this.fileListd=[]
      this.upload.isUploading = false
      this.upload.open = true
    },
    onRemove(){
    },
    onChanc(){
    	// if(this.fileNumber != 'x'){
    	// 	this.fileNumber++
    	// }else{
    	// 	this.fileNumber = 0
    	// }
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.isUploading = false
    	if(response.code == 200){
    		this.upload.open = false
    	   this.$modal.msgSuccess(response.msg)
    		this.getList();
    	}else{
        this.upload.isUploading = false
    	   this.$modal.msgError(response.msg)
    	}
    },
    // 上传前 校验大小
    handleBeforeUpload(file) {
      let fileSize = 50
      const isLt = file.size / 1024 / 1024 < fileSize
      if (!isLt) {
        this.msgWarn(`上传文件大小不能超过 ${fileSize} MB!`)
        return false
      }
    },
    // 提交上传文件
    submitFileForm() {
      if(this.fileListd.length >0){
        this.$refs.upload.submit()
        this.upload.isUploading = true
      }else{
        this.$modal.msgError('请选择需要上传文件')
      }
    },
    //库存调整
    handleResultTiaoZheng(row){
       this.reset();

      this.open=true
			this.SeeResultParams.pageNum = 1
			this.SeeResultParams.planId = row.planId
      this.SeeResultParams.materialDetailId = row.materialDetailId
			this.formResultDetailList = []
      this.getSeeList()
     
      this.form.beforeCount=row.stockCount; 
      this.form.materialDetailId=row.materialDetailId
    },
    //调整记录
   handleResultDetailList(row){
      this.isResultOpen=true
			this.SeeResultParams.pageNum = 1
      this.SeeResultParams.materialDetailId = row.materialDetailId
			this.formResultDetailList = []
      liststockLog(this.SeeResultParams).then(response => {
        console.log(response.rows)
        this.formResultDetailList = response.rows;
        this.totalSee = response.total;
      });
    },
    getSeeList() {
      console.log(this.SeeResultParams)
    //   getResultInfoList(this.SeeResultParams).then(response => {
    //     console.log(response.rows)
    //     this.formResultDetailList = response.rows;
    //     this.totalSee = response.total;
    //   });
    },
    // 审核
		handleStatus() {
			if (this.formSeeIds && this.formSeeIds.length > 0) {
				this.statusOpen = !this.statusOpen
			} else {
				this.$modal.msgError('请选择需要审核的任务')
			}
		},
    // 审核提交
			submitStatus() {
				this.$refs["formStatus"].validate(valid => {
					if (valid) {
						let ids = this.formSeeIds.toString()
						if (this.areaTaskDetailId) {
							const data = {
								taskDetailId: this.areaTaskDetailId,
								ids: this.formSeeIds,
								status: this.formStatus.status,
							}
							approveAreaCheck(data).then(res => {
								this.$modal.msgSuccess("确认成功");
								this.statusOpen = false;
								this.getSeeList();
								this.getList()
							})
						} else {
							const formStatus = this.formStatus
							if (this.formSeeIds[0].checkType == '2' && this.formSeeIds[0].wareHouseType == '1') {
								this.$modal.confirm('所选载具下的所有物料都将' + formStatus == '4' ? '通过' : '驳回').then(
									function() {
										return approve(ids, formStatus);
									}).then(() => {
									this.getList();
									this.$modal.msgSuccess("审核成功");
									this.statusOpen = false;
									this.getSeeList();
									this.getList()
								}).catch(() => {});
							} else {
								approve(ids, this.formStatus).then(response => {
									this.$modal.msgSuccess("审核成功");
									this.statusOpen = false;
									this.getSeeList();
									this.getList()
								});
							}
						}
					}
				})
			},
      handlePanChaChange(selection) {
         this.formSeeIds = selection.map(item => item.taskDetailId)
      },
  }
};
</script>
