<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="物料编码" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="计量单位" prop="unitId">
        <el-select v-model="queryParams.unitId" clearable placeholder="请选择计量单位">
          <el-option v-for="dict in unitList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="供应商" prop="contactsUnitId">
        <el-select v-model="queryParams.contactsUnitId" clearable placeholder="请选择供应商">
          <el-option v-for="dict in contactsUnitList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="物料类别" prop="categoryId">
        <el-select v-model="queryParams.categoryId" clearable placeholder="请选择物料类别">
          <el-option v-for="dict in categoryList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="批次属性" prop="batchAttrId">
        <el-select v-model="queryParams.batchAttrId" clearable placeholder="请选择批次属性">
          <el-option v-for="dict in attrList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否重点物料" prop="importantStatus">
        <el-select v-model="queryParams.importantStatus" clearable placeholder="请选择是否重点物料">
          <el-option v-for="dict in dict.type.wms_rule_status" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" :loading = "addLoading" v-hasPermi="['wms:material:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['wms:material:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
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
          <el-button plain icon="el-icon-upload"  size="mini" v-hasPermi="['wms:material:import']">导入</el-button>
        </el-upload>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:material:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExportDemo" v-hasPermi="['wms:material:exportdemo']">下载模板</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="batchType" v-hasPermi="['wms:material:bacthStock']" @click="batchUpdate">批量设置库存上下限</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="物料编码" fixed="left" align="center" prop="code" width="120px" :show-overflow-tooltip="true"/>
      <el-table-column label="物料名称" fixed="left" align="center" prop="name" min-width="240px" :show-overflow-tooltip="true"/>
      <el-table-column label="规格型号" align="center" prop="specifications" min-width="180px" :show-overflow-tooltip="true"/>
      <el-table-column label="计量单位" align="center" prop="unitName" />
      <el-table-column label="供应商" align="center" prop="contactsUnitName" />
      <el-table-column label="物料类别" align="center" prop="categoryName" width="100px" :show-overflow-tooltip="true"/>
      <!-- <el-table-column label="是否启用批次" align="center" prop="batchFlag" min-width="140px" /> -->
      <!-- <el-table-column label="批次属性" align="center" prop="batchAttrName" width="140px" /> -->
<!--      <el-table-column label="是否混物料" align="center" prop="sameMaterialFlag" />-->
<!--      <el-table-column label="是否混批次" align="center" prop="sameBatchFlag" />-->
      <!-- <el-table-column label="检验方式" align="center" prop="inspectionMethod" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_inspection_method" :value="scope.row.inspectionMethod"/>
        </template>
      </el-table-column> -->
      <el-table-column label="是否启用有效期管理" align="center" prop="expirationFlag" />
      <el-table-column label="有效期天数" align="center" prop="expirationDate" />
      <el-table-column label="单价(元)" align="center" prop="unitPrice" />
      <el-table-column label="重量(KG)" align="center" prop="roughWeight" />
      <el-table-column label="长度(cm)" align="center" prop="materialLength" />
      <el-table-column label="宽度(cm)" align="center" prop="materialWidth" />
      <el-table-column label="高度(cm)" align="center" prop="materialHeight" />
      <!-- <el-table-column label="净重(KG)" align="center" prop="roughWeight" /> -->
      <el-table-column label="库存上限" align="center" prop="stockMax" />
      <el-table-column label="库存下限" align="center" prop="stockMin" />
      <el-table-column label="财务凭证号" align="center" prop="financeVoucherNo" />
      <el-table-column label="是否重点物料" align="center" prop="importantStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_rule_status" :value="scope.row.importantStatus"/>
        </template>
      </el-table-column>
      <!-- <el-table-column label="单价" align="center" width="100" prop="unitPrice" /> -->
      <el-table-column label="物料描述" align="center" prop="description" min-width="180px"  :show-overflow-tooltip="true"/>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="200px" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-star-on" @click="handleImportantUpdate(scope.row)" v-if="!scope.row.importantStatus || scope.row.importantStatus == '0'">设为重点物料</el-button>
          <el-button size="mini" type="text" icon="el-icon-star-off" @click="handleImportantUpdate(scope.row)" v-if="scope.row.importantStatus == '1'">取消重点物料</el-button>
          <el-button size="mini" type="text" icon="el-icon-picture-outline" @click="handlePictureUpdate(scope.row)" v-hasPermi="['wms:material:edit']">图片管理</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:material:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:material:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改物料管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="40%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px" style="padding-right: 100px;">
        <el-form-item label="物料编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入物料编码" maxlength="8" show-word-limit />
        </el-form-item>
        <el-form-item label="物料名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入物料名称" maxlength="40" show-word-limit />
        </el-form-item>
        <el-form-item label="规格型号" prop="specifications">
          <el-input v-model="form.specifications" placeholder="请输入规格型号" maxlength="40" show-word-limit />
        </el-form-item>
        <el-form-item label="计量单位" prop="unitId">
          <el-select style="width: 100%;" v-model="form.unitId" class="select-input-form">
            <el-option v-for="item in unitList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="供应商" prop="contactsUnitId">-->
<!--          <el-select style="width: 100%;" v-model="form.contactsUnitId" class="select-input-form" clearable>-->
<!--            <el-option v-for="item in contactsUnitList" :key="item.id" :value="item.id" :label="item.name"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item label="物料类别" prop="categoryId">
<!--          <el-select style="width: 100%;" v-model="form.categoryId" class="select-input-form">-->
<!--            <el-option v-for="item in categoryList" :key="item.id" :value="item.id" :label="item.name"></el-option>-->
<!--          </el-select>-->
          <el-input v-model="form.categoryId" placeholder="物料组编号" maxlength="10" show-word-limit />
        </el-form-item>
        <!-- <el-form-item label="是否启用批次" prop="batchFlag">
          <el-select style="width: 100%;" v-model="form.batchFlag" class="select-input-form" >
            <el-option value="1" label="是">是</el-option>
            <el-option value="0" label="否">否</el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="批次属性" prop="batchAttrId" ><!--v-if="form.batchFlag == '1'"-->
          <el-select style="width: 100%;" v-model="form.batchAttrId" class="select-input-form" @change="handlebatchAttrIdChange" >
            <el-option v-for="item in attrList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单价(元)" prop="unitPrice">
          <el-input-number :min="0" :max="999999" :precision="2" :step="1" v-model="form.unitPrice" show-word-limit placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="物料描述" prop="description">
          <el-input maxlength="200" v-model="form.description" show-word-limit placeholder="请输入物料描述" />
        </el-form-item>
<!--        <el-form-item label="是否混物料" prop="sameMaterialFlag">-->
<!--          <el-select style="width: 100%;" v-model="form.sameMaterialFlag" class="select-input-form">-->
<!--            <el-option value="1" label="是">是</el-option>-->
<!--            <el-option value="0" label="否">否</el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="是否混批次" prop="sameBatchFlag">-->
<!--          <el-select style="width: 100%;" v-model="form.sameBatchFlag" class="select-input-form">-->
<!--            <el-option value="1" label="是">是</el-option>-->
<!--            <el-option value="0" label="否">否</el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <!-- <el-form-item label="检验方式" prop="inspectionMethod">
          <el-select style="width: 100%;" v-model="form.inspectionMethod" placeholder="请选择检验方式" class="select-input-form">
            <el-option v-for="dict in dict.type.wms_inspection_method" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="是否启用有效期管理" prop="expirationFlag" >
          <el-select style="width: 100%;" v-model="form.expirationFlag" class="select-input-form" @change="handleExpirationFlagChange" :disabled="form.batchAttrId == attrTermId ? true : false">
            <el-option value="1" label="是">是</el-option>
            <el-option value="0" label="否">否</el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="有效期天数" prop="expirationDate" :required="form.expirationFlag == '1' ? true : false">
          <el-input-number style="width: 100%;" v-model="form.expirationDate"  :min="0" :max="9999" placeholder="请输入有效期天数" maxlength="4" show-word-limit class="select-input-form"/>
        </el-form-item>
        <el-form-item label="库存上限" prop="stockMax">
          <el-input oninput="value=value.replace(/[^\d]/g,'')" onblur="value = (value ? parseInt(value) : '')" maxlength="8" v-model="form.stockMax" show-word-limit placeholder="请输入库存上限" />
        </el-form-item>
        <el-form-item label="库存下限" prop="stockMin">
          <el-input oninput="value=value.replace(/[^\d]/g,'')" onblur="value = (value ? parseInt(value) : '')" maxlength="8" v-model="form.stockMin" show-word-limit placeholder="请输入库存下限" />
        </el-form-item>
        <el-form-item label="重量(KG)" prop="roughWeight"><!--netWeight-->
          <el-input-number v-model="form.roughWeight"  :min="0" :max="99999" placeholder="请输入重量" show-word-limit class="select-input-form"/>
        </el-form-item>
        <el-form-item label="长度(cm)" prop="materialLength">
          <el-input-number v-model="form.materialLength"  :min="0" :max="99999" placeholder="请输入长度" show-word-limit class="select-input-form"/>
        </el-form-item>
        <el-form-item label="宽度(cm)" prop="materialWidth">
          <el-input-number v-model="form.materialWidth"  :min="0" :max="99999" placeholder="请输入宽度" show-word-limit class="select-input-form"/>
        </el-form-item>
        <el-form-item label="高度(cm)" prop="materialHeight">
          <el-input-number v-model="form.materialHeight"  :min="0" :max="99999" placeholder="请输入高度" show-word-limit class="select-input-form"/>
        </el-form-item>
        <!-- <el-form-item label="净重(KG)" prop="roughWeight">
          <el-input-number style="width: 100%;" v-model="form.roughWeight"  :min="0" :max="99999"  placeholder="请输入净重" show-word-limit  class="select-input-form"/>
        </el-form-item> -->
        <el-form-item label="财务凭证号" prop="financeVoucherNo">
          <el-input maxlength="100" v-model="form.financeVoucherNo" placeholder="请输入财务凭证号" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="5" placeholder="请输入内容" show-word-limit maxlength="250" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 图片管理 -->
    <el-dialog title="图片管理" :visible.sync="pictureVisible" width="40%" append-to-body v-if="pictureVisible">
      <el-form ref="formPicture" :model="formPicture" :rules="formPictureRule" label-width="100px">
        <el-form-item label="上传图片" prop="img">
					<image-upload v-model="formPicture.img" :imageDisabled="isShow"></image-upload>
				</el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormPicture">确 定</el-button>
        <el-button @click="pictureVisible=false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 批量设置库存上下限 -->
    <el-dialog title="批量设置" :visible.sync="Inventory" width="40%" append-to-body v-if="Inventory">
      <el-form ref="formNum" :model="formNum" :rules="rulesNum" label-width="150px">
        <el-form-item label="库存上限" prop="stockMax">
          <el-input oninput="value=value.replace(/[^\d]/g,'')" v-model="formNum.stockMax" placeholder="请输入库存上限" maxlength="8"/>
        </el-form-item>
        <el-form-item label="库存下限" prop="stockMin">
          <el-input oninput="value=value.replace(/[^\d]/g,'')" v-model="formNum.stockMin" placeholder="请输入库存下限" maxlength="8"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitformNum">确 定</el-button>
        <el-button @click="Inventory=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial,bacthStock } from "@/api/wms/material";
import { listUnit } from "@/api/wms/unit";
import { listCategory } from "@/api/wms/category";
import { listAttr } from "@/api/wms/attr";
import { listContactsUnit } from "@/api/wms/contactsUnit";
// import Treeselect from "@riophae/vue-treeselect";
// import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { wms } from '@/utils/agent';
import { getToken } from '@/utils/auth'

export default {
  name: "Material",
  dicts: ['wms_inspection_method','wms_rule_status'],
  // components: { Treeselect },
  data() {
    let validateExpirationDate = (rule, value, callback) => {
      if (!value && this.form.expirationFlag == '1') {
        callback(new Error("有效期天数不能为空"));
      } else {
        if(value.length>10){
            callback(new Error("有效期天数填写有问题"));
        }else{
            callback();
        }
      }
    };
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      names:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 物料管理表格数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orgId: null,
        code: null,
        name: null,
        specifications: null,
      },
      // 表单参数
      form: {
        code: ""
      },
      // 表单校验
      rules: {
        // stockMax:[
        //   { required: true, message: "库存上限不能为空", trigger: "blur" }
        // ],
        // stockMin:[
        //   { required: true, message: "库存下限不能为空", trigger: "blur" }
        // ],
        name: [
          { required: true, message: "物料名称不能为空", trigger: "blur" }
        ],
        // orgId: [
        //   { required: true, message: "所属组织不能为空", trigger: "blur" }
        // ],
        categoryId:[
          { required: true, message: "物料类别不能为空", trigger: "blur" }
        ],
        // batchFlag:[
        //   { required: true, message: "是否启用批次不能为空", trigger: "blur" }
        // ],
        batchAttrId:[
          { required: true, message: "批次属性不能为空", trigger: "blur" }
        ],
        // sameMaterialFlag:[
        //   { required: true, message: "是否混物料不能为空", trigger: "blur" }
        // ],
        // sameBatchFlag:[
        //   { required: true, message: "是否混批次不能为空", trigger: "blur" }
        // ],
        expirationFlag:[
          { required: true, message: "是否启用有效期管理不能为空", trigger: "blur" }
        ],
        expirationDate:[
          { trigger: "blur", validator: validateExpirationDate }
        ],
        // roughWeight:[
        //   { required: true, message: "重量不能为空", trigger: "blur" }
        // ],
        // materialLength:[
        //   { required: true, message: "长度不能为空", trigger: "blur" }
        // ],
        // materialWidth:[
        //   { required: true, message: "宽度不能为空", trigger: "blur" }
        // ],
        // materialHeight:[
        //   { required: true, message: "高度不能为空", trigger: "blur" }
        // ],
        unitPrice:[
          { required: true, message: "单价不能为空", trigger: "change" }
        ],
        unitId:[
          { required: true, message: "计量单位不能为空", trigger: "blur" }
        ],
        // contactsUnitId:[
        //   { required: true, message: "供应商不能为空", trigger: "blur" }
        // ],
        description:[
          { required: true, message: "物料描述不能为空", trigger: "blur" }
        ],
        specifications:[
          { required: true, message: "规格型号不能为空", trigger: "blur" }
        ],
        code:[
          { required: true, message: "物料编码不能为空", trigger: "blur" }
        ]
      },
      // 计量单位
      unitList:[],
      // 物料类别
      categoryList:[],
      // 批次属性
      attrList:[],
      // 供应商
      contactsUnitList:[],
      uploadUrl: process.env.VUE_APP_BASE_API +'/'+wms+'/material/importData', // 上传地址
      uploadHeaders: { Authorization: this.getToken() },
      fileList:[],
      Inventory:false, // 批量弹框
      formNum:{}, // 批量值
      rulesNum:{
        stockMax:[
          { required: true, message: "库存上限不能为空", trigger: "blur" }
        ],
        stockMin:[
          { required: true, message: "库存下限不能为空", trigger: "blur" }
        ],
      },
      batchType:true, //批量按钮
      attrTermId:0, // 批次属性 剩余有效期天数ID
      addLoading: false, // 新增按钮的loading

      pictureVisible:false, // 图片管理弹框
      formPicture:{}, // 图片管理表单
      formPictureRule:{
        img:[
          { required: true, message: "请选择图片", trigger: "change" }
        ],
      },
      isShow: false, //是否可以编辑图片
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'material') {
        this.getList();
      }
    }
  },
  created() {
    this.getList();
    this.getModeList();
  },
  methods: {
    getToken,
    /** 查询所有下拉列表 **/
    getModeList(){
      /** 计量单位下拉列表 **/
      listUnit({pageSize: 5000}).then(response =>{
        this.unitList = response.rows;
      })

      /** 供应商下拉列表 **/
      listContactsUnit({pageSize: 5000}).then(response =>{
        this.contactsUnitList = response.rows;
      })

      /** 物料类别下拉列表 **/
      listCategory({pageSize: 5000}).then(response =>{
        this.categoryList = response.rows;
      });

      /** 批次属性下拉列表 **/
      listAttr({pageSize: 5000}).then(response =>{
        this.attrList = response.rows;
        response.rows.forEach(data => {
          if("剩余有效期天数" == data.name){
            this.attrTermId = data.id;
            return;
          }
        })

      });
    },
    /** 查询物料管理列表 */
    getList() {
      this.loading = true;
      listMaterial(this.queryParams).then(response => {
        response.rows.forEach(data => {
          if (data.batchFlag == '0'){
              data.batchFlag = "否";
          }
          if (data.batchFlag == '1'){
             data.batchFlag = "是";
          }
          if (data.sameMaterialFlag == '0'){
            data.sameMaterialFlag = "否";
          }
          if (data.sameMaterialFlag == '1'){
            data.sameMaterialFlag = "是";
          }
          if (data.sameBatchFlag == '0'){
            data.sameBatchFlag = "否";
          }
          if (data.sameBatchFlag == '1'){
            data.sameBatchFlag = "是";
          }
          if (data.expirationFlag == '1'){
            data.expirationFlag = "是"
          }
          if (data.expirationFlag == '0'){
            data.expirationFlag = "否"
          }
        });
        this.materialList = response.rows;
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
        orgId: null,
        code: null,
        name: null,
        specifications: null,
        unitId: null,
        contactsUnitId: null,
        categoryId: null,
        batchFlag: null,
        batchAttrId: null,
        baseUnitId: null,
        sameMaterialFlag: null,
        sameBatchFlag: null,
        inspectionMethod: null,
        roughWeight: null,
        materialLength: null,
        materialWidth: null,
        materialHeight: null,
        expirationDate: null,
        expirationFlag: null,
        stockMax: null,
        stockMin: null,
        img: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null
      };
      this.resetForm("form");
    },
    // 批量表单重置
    batchreset() {
      this.formNum = {
        stockMax: null,
        stockMin: null,
      };
      this.resetForm("formNum");
    },
    /** 是否启用有效期管理改变操作 */
    handleExpirationFlagChange(value){
      this.$refs.form.clearValidate('expirationDate')
    },
    /** 批次属性启用剩余有效期天数时，必须启用有效期管理 */
    handlebatchAttrIdChange(value){
      if (this.form.batchAttrId == this.attrTermId){
        this.form.expirationFlag = '1';
      }
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
      if(this.ids.length > 0 ){
        this.batchType = false
      }else{
        this.batchType = true
      }
      this.names = selection.map(item => item.name)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加物料管理";
      // 是否启用批次新增时默认启用
      this.form.batchFlag = '1';
      this.form.batchAttrId = this.attrList[0].id
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMaterial(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物料管理";
      });
    },
    /** 图片管理操作 */
    handlePictureUpdate(row){
      const id = row.id
      this.formPicture = {
        id,
        img: row.img || null,
      };
      this.resetForm("formPicture");
      this.pictureVisible = true
    },
    // 批量设置库存
    batchUpdate(){
      this.batchreset();
      this.Inventory = true
    },
    // 图片管理提交
    submitFormPicture(){
      this.$refs["formPicture"].validate(valid => {
        if (valid) {
          updateMaterial(this.formPicture).then(response => {
            this.$modal.msgSuccess("图片编辑成功");
            this.pictureVisible = false;
            this.getList();
          });
        }
      });
    },
    // 批量提交
    submitformNum(){
      this.$refs["formNum"].validate(valid => {
        if (valid) {
          this.formNum.ids = this.ids.toString()
          bacthStock(this.formNum).then(response => {
            this.$modal.msgSuccess("设置成功");
            this.Inventory = false;
            this.getList();
          });
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMaterial(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMaterial(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    //重点物料设置
    handleImportantUpdate(row){
      let msg = "";
      let params = {
        id : row.id,
        importantStatus: '1'
      }
      if(row.importantStatus == '1'){
        msg = "是否取消物料" + row.name +"的重点物料设置?";
        params.importantStatus = '0';
      }else{
        msg = "是否确认设置物料" + row.name +"为重点物料?";
      }
      this.$modal.confirm(msg).then(function() {
        return updateMaterial(params);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("设置成功");
      }).catch(() => {});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const name = row.name || this.names;
      this.$modal.confirm('是否确认删除物料"' + name + '"的数据项？').then(function() {
        return delMaterial(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/material/export', {...this.queryParams}, `material_${new Date().getTime()}.xlsx`)
    },
    /** 下载模板按钮操作 */
    handleExportDemo() {
      this.download(wms+'/material/export/demo',{}, `material_${new Date().getTime()}.xlsx`)
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

  }
};
</script>
