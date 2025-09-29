<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="载具编号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入载具编号" clearable />
      </el-form-item>
      <el-form-item label="载具状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择载具状态">
          <el-option v-for="dict in dict.type.wms_t_tray" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="载具类型" prop="trayCategory">
        <el-select v-model="queryParams.trayCategory" clearable placeholder="请选择载具类型">
          <el-option v-for="dict in dict.type.wms_t_tray_category" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="库位名称" prop="locationName">
        <el-input v-model="queryParams.locationName" placeholder="请输入库位名称" clearable />
      </el-form-item>
<!--      <el-form-item label="是否打印条码" prop="labelTemplateType">-->
<!--        <el-select v-model="queryParams.labelTemplateType" clearable placeholder="请选择">-->
<!--          <el-option v-for="dict in dict.type.wms_t_tray_label" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading = 'addLoading' @click="handleAdd" v-hasPermi="['wms:tray:add']">新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-upload
          class="upload-demo"
          :action="uploadUrl"
          :headers="uploadHeaders"
          :on-success="handleAfterUpload"
          :limit="1"
          :on-exceed="handleExceed"
          :before-upload="handleBeforeUpload"
          :show-file-list="false"
          :file-list="fileList"
        >
          <el-button plain icon="el-icon-upload" v-hasPermi="['wms:tray:import']" size="mini">导入</el-button>
        </el-upload>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:tray:export']">导出</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExportDemo" v-hasPermi="['wms:unit:exportdemo']">下载模板</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="TrayList" @selection-change="handleSelectionChange">
      <el-table-column label="载具编号" width="150" align="center" prop="code" :show-overflow-tooltip="true" />
      <el-table-column label="载具类型" width="80"  align="center" prop="trayCategory" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_tray_category " :value="scope.row.trayCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="载具状态" align="center" prop="status" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_tray " :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="库位" align="center" prop="locationName" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="库位状态" align="center" prop="goodsAllocationStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_goods_allocation_status" :value="scope.row.goodsAllocationStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="自重（kg）" width="120" align="center" prop="weight" :show-overflow-tooltip="true" />
      <el-table-column label="最大承载重量（kg）" width="140" align="center" prop="maxWeight" :show-overflow-tooltip="true" />
<!--      <el-table-column label="固定绑定库位" align="center" prop="locationType" :show-overflow-tooltip="true">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.tray_location_type" :value="scope.row.locationType"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="是否打印条码" align="center" prop="labelTemplateType" :show-overflow-tooltip="true">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.wms_t_tray_label" :value="scope.row.labelTemplateType"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="创建类型" align="center" prop="type" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_tray_state " :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" />
      <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" fixed="right" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:tray:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:tray:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-postcard" @click="handleRunDetail(scope.row)">运行记录</el-button>
          <el-button size="mini" type="text" icon="el-icon-sell" v-if="scope.row.palletNum !== null && scope.row.palletNum !== ''" @click="handleMoveStatus(scope.row,1)" v-hasPermi="['wms:tray:edit']">载具出库</el-button>
          <el-button size="mini" type="text" icon="el-icon-sold-out" v-if="scope.row.palletNum === null || scope.row.palletNum === ''" @click="handleMoveStatus(scope.row,2)" v-hasPermi="['wms:tray:edit']">载具回库</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改载具管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body @close="closeDestroy">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="载具类型" prop="trayCategory">
          <el-select style="width: 100%;" v-model="form.trayCategory" clearable placeholder="请选择载具类型" class="select-input-form">
            <el-option v-for="dict in dict.type.wms_t_tray_category" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="自重(kg)" prop="weight">
          <el-input-number style="width: 100%;" v-model="form.weight"  :min="0" :max="99999"  placeholder="请输入自重" show-word-limit class="select-input-form"/>
        </el-form-item>
        <el-form-item label="最大承载重量(kg)" prop="maxWeight">
          <el-input-number style="width: 100%;" v-model="form.maxWeight"  :min="0" :max="99999"  placeholder="请输入最大承载重量" show-word-limit class="select-input-form"/>
        </el-form-item>
        <el-form-item label="载具状态" prop="status">
          <el-select style="width: 100%;" v-model="form.status" clearable placeholder="请选择载具状态" class="select-input-form">
            <el-option v-for="dict in dict.type.wms_t_tray" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建类型" prop="type">
          <el-select style="width: 100%;" v-model="form.type" disabled  placeholder="请选择创建类型" class="select-input-form">
            <el-option v-for="dict in dict.type.wms_t_tray_state" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="5" placeholder="请输入内容" maxlength="250" show-word-limit/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看wcs运行记录 -->
    <WcsRecordCom ref="wcsRecordCom" @setControl="setControl"></WcsRecordCom>
  </div>
</template>

<script>
import { listTray, getTray, delTray, addTray, updateTray, takeOutTray, recycleTray } from "@/api/wms/Tray";
import { getCode } from "@/api/wms/common";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { mapGetters } from 'vuex';
import { MTP } from "@/utils/codeType";
import { wms } from '@/utils/agent';
import { getToken } from '@/utils/auth';
import { listArea} from "@/api/wms/area";
import { listReservoir } from "@/api/wms/reservoir";
import { listLocation } from "@/api/wms/location";
import WcsRecordCom from "./components/wcsRecordDetail";
export default {
  name: "Tray",
  dicts: ['wms_t_tray','wms_t_tray_category','wms_t_tray_state','wms_t_tray_mentor','tray_location_type','wms_t_tray_label','wms_goods_allocation_status'],
  computed: {
    ...mapGetters([
      'deptId','token'
    ]),
  },
  components:{Treeselect,WcsRecordCom},
  data() {
    let regulationTypeRules = (rule, value, callback) =>{
      if(!this.form.status){
      	callback(new Error('请选择状态'));
      }else{
        callback();
      }
    }
    let formchonde = (rule, value, callback) =>{
      if(!this.form.factory){
      	callback(new Error('请选择组织'));
      }else{
        callback();
      }
    }
    let trayTypechonde = (rule, value, callback) =>{
      if(!this.form.type){
      	callback(new Error('请选择类型'));
      }else{
        callback();
      }
    }
    let flagchonde = (rule, value, callback) =>{
      if(!this.form.flag){
      	callback(new Error('请选择是否满载具'));
      }else{
        callback();
      }
    }
    let trayCategoryChonde = (rule, value, callback) =>{
      if(!this.form.trayCategory){
      	callback(new Error('请选择载具类型'));
      }else{
        callback();
      }
    }
    let areaIdChonde = (rule, value, callback) =>{
      if(this.form.locationType == 0){
        if(!this.form.areaId){
        	callback(new Error('请选择区域'));
        }else{
          callback();
        }
      }
    }
    let reservoirIdChonde = (rule, value, callback) =>{
      if(this.form.locationType == 0){
        if(!this.form.reservoirId){
        	callback(new Error('请选择库区'));
        }else{
          callback();
        }
      }
    }
    let locationChonde = (rule, value, callback) =>{
      if(this.form.locationType == 0){
        if(!this.form.locationId){
        	callback(new Error('请选择库位'));
        }else{
          callback();
        }
      }
    }
    return {
      defaultParams: {
        label: 'label',
        value: 'id',
        children: 'children',
        checkStrictly: true
      },
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
      // 载具管理表格数据
      TrayList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        factory:null,
        status:null,
        trayCategory:null,
        labelTemplateType:null,
        locationName:null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code:[
        	{required: true, message: "载具编码不能为空", trigger: "blur"}
        ],
        remark:[
        	{required: true, message: "备注不能为空", trigger: "blur"}
        ],
        status:[
        	{required: true, validator: regulationTypeRules,trigger: "change"}
        ],
        factory:[{required: true, validator: formchonde,trigger: "change"}],
        type:[{required: true, validator: trayTypechonde,trigger: "change"}],
        trayCategory:[{required: true, validator: trayCategoryChonde,trigger: "change"}],
        areaId:[{required: true, validator: areaIdChonde,trigger: "change"}],
        reservoirId:[{required: true, validator: reservoirIdChonde,trigger: "change"}],
        locationId:[{required: true, validator: locationChonde,trigger: "change"}],
        weight:[
        	{required: true, message: "自重不能为空", trigger: "blur"}
        ],
        maxWeight:[
        	{required: true, message: "最大承载重量不能为空", trigger: "blur"}
        ],
      },
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
      fileNumber:0,
      uploadUrl: process.env.VUE_APP_BASE_API +'/'+wms+'/Tray/importData', // 上传地址
      uploadHeaders: { Authorization: this.getToken() },
      fileList:[],
      // 区域列表
      areaIdList:[],
      // 库区列表
      reservoirIdList:[],
      // 库位列表
      locationIdList:[],
      addLoading: false, // 新增按钮loading
    };
  },
  created() {
    this.upload.headers = {Authorization: "Bearer " + this.token}
    this.getList();
  },
  watch: {
    $route(to, form) {
      if (to.name == 'tray') {
        this.getList()
      }
    }
  },
  methods: {
    getToken,
    changeAreaList(){
      listArea({warehouseId:this.form.warehouseId,pageSize:5000}).then(response => {
        this.areaIdList = response.rows;
      });
    },
    changeReservoirList(){
        listReservoir({areaId:this.form.areaId,pageSize:5000}).then(response => {
            this.reservoirIdList = response.rows;
        });
    },
    changeLocationList(){
      listLocation({warehouseId:this.form.reservoirId,pageSize:5000}).then(response => {
          this.locationIdList = response.rows;
      });
    },
    defaultParamsChange(value){
      if(value.length >0){
        this.queryParams.factory = value[value.length-1]
        this.$refs.refHandle.dropDownVisible = false;
      }
    },
    formidChange(value){
      if(value.length >0){
        this.form.factory = value[value.length-1]
        this.$refs.refForm.dropDownVisible = false;
      }
    },
    listWarehouse(){
      // listWarehouse({pageNum: 1,pageSize: 1000,}).then(res=>{
      //   this.warehouseIdList = res.rows
      // })
    },
    closeDestroy(){
      this.areaIdList=[]
      this.reservoirIdList=[]
      this.locationIdList=[]
    },
    /** 查询载具管理列表 */
    getList() {
      this.loading = true;
      listTray(this.queryParams).then(response => {
        this.TrayList = response.rows;
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
        factory:null,
        code: null,
        status: null,
        trayCategory:null,
        locationType:'1',
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
      this.form.factory =this.deptId;
      this.form.type = '5';
      this.open = true;
      this.title = "添加载具管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTray(id).then(response => {
        this.form = response.data;
        this.changeAreaList()
        this.changeReservoirList()
        this.changeLocationList ()
        this.open = true;
        this.title = "修改载具管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTray(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTray(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
        return delTray(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 托盘出库/回库 */
    handleMoveStatus(row,type){
      this.$modal.confirm('是否确认将该载具'+(type == 1 ? '出库':'回库')+'？').then(function() {
        return type == 1 ? takeOutTray({ id: row.id }) : recycleTray({ id: row.id });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess((type == 1 ? '出库':'回库') + "成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/tray/export', {...this.queryParams}, `Tray_${new Date().getTime()}.xlsx`)
    },

    onRemove(){
      this.fileNumber = 0
    },
    onChanc(file, fileList){
      this.fileNumber++
    },
    /** 下载模板按钮操作 */
    handleExportDemo() {
      this.download(wms+'/Tray/export/demo',{}, `category_${new Date().getTime()}.xlsx`)
    },
    /** 导入超出限制提示 */
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    /** 导入前的校验 */
    handleBeforeUpload(file) {
      // const isJPG = file.type === 'image/jpeg';
      // const isLt2M = file.size / 1024 / 1024 < 2;

      // if (!isJPG) {
      //   this.$message.error('上传头像图片只能是 JPG 格式!');
      // }
      // if (!isLt2M) {
      //   this.$message.error('上传头像图片大小不能超过 2MB!');
      // }
      return true;
    },
    /** 文件上传后回调 */
    handleAfterUpload(response){
      this.fileList = [];
      if (response.code == '200'){
        this.$message.success("导入成功")
        this.resetQuery();
      }else {
        this.$message.error(response.msg)
      }
    },
    //查看记录
    handleRunDetail(item){
      this.$refs.wcsRecordCom.open = true;
      this.$refs.wcsRecordCom.trayId = item.id;
    },
  },

};
</script>
