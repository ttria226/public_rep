<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="110px">
      <el-form-item label="库位编码" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入库位编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="库位名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入库位名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="库位状态" prop="goodsAllocationStatus">
        <el-select v-model="queryParams.goodsAllocationStatus" @change="changeForceUpdate()" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.wms_goods_allocation_status" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所属区域" prop="areaId">
        <el-select v-model="queryParams.areaId"  @change="changeQueryReservoirList()" placeholder="请选择所属区域" clearable>
          <el-option v-for="dict in queryAreaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="所属库区" prop="reservoirId">
        <el-select v-model="queryParams.reservoirId" @change="changeQueryGoodShelfList()" placeholder="请选择所属库区" clearable>
          <el-option v-for="dict in kqQueryList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="托盘编码" prop="palletNum">
        <el-input v-model="queryParams.palletNum" placeholder="请输入载具编码" clearable/>
      </el-form-item>
<!--      <el-form-item label="所属货架" prop="goodShelfId">-->
<!--        <el-select v-model="queryParams.goodShelfId" @change="changeForceUpdate()" placeholder="请选择所属货架" clearable>-->
<!--          <el-option v-for="dict in goodShelfQueryList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="存放物料类别" prop="categoryId">
        <el-select v-model="queryParams.categoryId" @change="changeForceUpdate()" placeholder="请选择存放物料类别" class="select-input-form" clearable>
          <el-option v-for="dict in categoryList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="是否混物料存放" prop="sameMaterialFlag">
        <el-select v-model="queryParams.sameMaterialFlag" @change="changeForceUpdate()" placeholder="请选择是否混物料存放" clearable>
          <el-option value="1" label="是">是</el-option>
          <el-option value="0" label="否">否</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否混批次" prop="sameBatchFlag">
        <el-select v-model="queryParams.sameBatchFlag" @change="changeForceUpdate()" placeholder="请选择是否混批次" clearable>
          <el-option value="1" label="是">是</el-option>
          <el-option value="0" label="否">否</el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="排" prop="locationRow">
        <el-select v-model="queryParams.locationRow" @change="changeForceUpdate()" placeholder="请选择排" clearable>
          <el-option v-for="dict in maxRow" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="列" prop="locationColumn">
        <el-select v-model="queryParams.locationColumn" @change="changeForceUpdate()" placeholder="请选择列" clearable>
          <el-option v-for="dict in maxColumn" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="层" prop="locationPlies">
        <el-select v-model="queryParams.locationPlies" @change="changeForceUpdate()" placeholder="请选择层" clearable>
          <el-option v-for="dict in maxPlies" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option value="1" label="启用"></el-option>
          <el-option value="0" label="禁用"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="伸位类型" prop="extentionType">
        <el-input v-model="queryParams.extentionType" placeholder="请输入伸位类型" clearable/>
      </el-form-item>
      <el-form-item label="货架组" prop="extentionFristId">
        <el-input v-model="queryParams.extentionFristId" placeholder="请输入货架组" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :loading='addLoading' @click="handleAdd" v-hasPermi="['wms:location:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :loading='addLoading' @click="handlePAdd" v-hasPermi="['wms:location:padd']">库位生成</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handlePUpdate" v-hasPermi="['wms:location:plcUpdate']">批量修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['wms:location:remove']">删除</el-button>
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
          <el-button plain icon="el-icon-upload" v-hasPermi="['wms:location:importData']" size="mini">导入</el-button>
        </el-upload>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:location:export']">导出</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExportDemo" v-hasPermi="['wms:unit:exportdemo']">下载模板</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="locationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="库位编码" align="center" prop="erpCode" width="120px" :show-overflow-tooltip="true" fixed="left" />
      <el-table-column label="库位名称" align="center" prop="name" width="160px" :show-overflow-tooltip="true" fixed="left" />
      <el-table-column label="是否启用" align="center" prop="status" :show-overflow-tooltip="true" fixed="left">
        <template slot-scope="scope"> {{scope.row.status == '1' ? '启用':'禁用'}}</template>
      </el-table-column>
      <el-table-column label="库位类型" align="center" prop="locationType" :show-overflow-tooltip="true" fixed="left">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_location_type" :value="scope.row.locationType"/>
        </template>
      </el-table-column>
      <el-table-column label="库位状态" align="center" prop="goodsAllocationStatus" :show-overflow-tooltip="true" fixed="left">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_goods_allocation_status" :value="scope.row.goodsAllocationStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="货架组" align="center" prop="extentionFristId"/>
      <el-table-column label="伸位" align="center" prop="extentionType"/>
      <el-table-column label="1伸位库位状态" align="center" prop="firstGoodsAllocationStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_goods_allocation_status" :value="scope.row.firstGoodsAllocationStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="载具编号" align="center" prop="palletNum" width="150px" :show-overflow-tooltip="true"/>
      <el-table-column label="所属区域" align="center" prop="areaName" :show-overflow-tooltip="true" width="150"/>
      <el-table-column label="所属库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" width="150"/>
<!--      <el-table-column label="所属货架" align="center" prop="goodShelfName" :show-overflow-tooltip="true" width="200"/>-->
<!--      <el-table-column label="存放物料类别" align="center" prop="categoryName" min-width="120px" :show-overflow-tooltip="true"/>-->
<!--      <el-table-column label="存放物料的包装方式" align="center" prop="unitName" min-width="140px" :show-overflow-tooltip="true"/>-->
      <!-- <el-table-column label="是否混物料存放" align="center" prop="sameMaterialFlag" min-width="140px" :show-overflow-tooltip="true"/>
      <el-table-column label="是否混批次" align="center" prop="sameBatchFlag" min-width="120px" :show-overflow-tooltip="true"/> -->
      <el-table-column label="排" align="center" prop="locationRow" :show-overflow-tooltip="true"/>
      <el-table-column label="列" align="center" prop="locationColumn" :show-overflow-tooltip="true"/>
      <el-table-column label="层" align="center" prop="locationPlies" :show-overflow-tooltip="true"/>
<!--      <el-table-column label="载具编号" align="center" prop="palletNum" min-width="220px" :show-overflow-tooltip="true"/>-->
      <el-table-column label="创建人" align="center" prop="createBy" :show-overflow-tooltip="true"/>
<!--      <el-table-column label="制单日期" align="center" width="180" prop="createTime" :show-overflow-tooltip="true"/>-->
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="180px" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['wms:location:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['wms:location:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-open" v-if="scope.row.status == '0'" @click="handleUpdateStatus(scope.row, '1')" v-hasPermi="['wms:location:updateStatus']">启用</el-button>
          <el-button size="mini" type="text" icon="el-icon-turn-off" v-if="scope.row.status == '1'" @click="handleUpdateStatus(scope.row, '0')" v-hasPermi="['wms:location:updateStatus']">禁用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改库位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px" style="padding-right: 100px;">
        <el-form-item label="库位名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" maxlength="40" show-word-limit />
        </el-form-item>
        <el-form-item label="库位类型" prop="locationType">
          <el-select style="width: 100%;" v-model="form.locationType" placeholder="请选择库位类型" class="select-input-form">
            <el-option v-for="dict in dict.type.wms_location_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属区域" prop="areaId">
          <el-select style="width: 100%;" v-model="form.areaId"  @change="changeReservoirList()" placeholder="请选择区域" class="select-input-form">
            <el-option v-for="dict in areaList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属库区" prop="reservoirId">
          <el-select style="width: 100%;" v-model="form.reservoirId" @change="changeGoodShelfList()" placeholder="请选择库区" class="select-input-form">
            <el-option v-for="dict in kqList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属货架" prop="goodShelfId">
          <el-select style="width: 100%;" v-model="form.goodShelfId" placeholder="请选择货架" class="select-input-form">
            <el-option v-for="dict in goodShelfList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="存放物料类别" prop="categoryId">
          <el-select style="width: 100%;" v-model="form.categoryId" placeholder="请选择物料类别" class="select-input-form">
            <el-option v-for="dict in categoryList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排" prop="locationRow">
          <el-input-number style="width: 100%;" v-model="form.locationRow" placeholder="请输入排" :disabled="form.id ? true : false" :max="999" class="select-input-form" />
        </el-form-item>
        <el-form-item label="列" prop="locationColumn">
          <el-input-number style="width: 100%;" v-model="form.locationColumn" placeholder="请输入列" :disabled="form.id ? true : false" :max="999" class="select-input-form" />
        </el-form-item>
        <el-form-item label="层" prop="locationPlies">
          <el-input-number style="width: 100%;" v-model="form.locationPlies" placeholder="请输入层" :disabled="form.id ? true : false" :max="999" class="select-input-form" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="5" placeholder="请输入内容" maxlength="250" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 库位批量修改 -->
    <el-dialog title="库位批量修改" :visible.sync="openPupdate" width="1200px" append-to-body>
      <el-form ref="updateform" :model="updateform" :rules="updateRules" label-width="150px" style="overflow: hidden">
          <el-col :span="7">
            <el-form-item label="起始排" prop="startRow">
              <el-select v-model="updateform.startRow" placeholder="请选择起始排" clearable @change="handleplUpdateQuery">
                <el-option v-for="dict in maxRow" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="起始列" >
              <el-select v-model="updateform.startColumn" placeholder="请选择起始列" clearable @change="handleplUpdateQuery">
                <el-option v-for="dict in maxColumn" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="起始层" >
              <el-select v-model="updateform.startPlies" placeholder="请选择起始层" clearable @change="handleplUpdateQuery">
                <el-option v-for="dict in maxPlies" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        <el-col :span="7">
          <el-form-item label="结束排" prop="endRow">
            <el-select v-model="updateform.endRow" placeholder="请选择结束排" clearable @change="handleplUpdateQuery">
              <el-option v-for="dict in maxRow" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="结束列" >
            <el-select v-model="updateform.endColumn" placeholder="请选择结束列" clearable @change="handleplUpdateQuery">
              <el-option v-for="dict in maxColumn" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="结束层" >
            <el-select v-model="updateform.endPlies" placeholder="请选择结束层" clearable @change="handleplUpdateQuery">
              <el-option v-for="dict in maxPlies" :key="`plc-`+dict" :label="dict" :value="dict"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="3">
        </el-col>
        <el-col :span="24">
          <div style="text-align: center;line-height: 40px;font-size: 16px;padding-bottom: 20px;">一共有{{totalCount}}条数据</div>
        </el-col>
        <el-col :span="8">
          <el-form-item label="区域" prop="areaId">
            <el-select v-model="updateform.areaId"  @change="changeReservoirListByUpdateform()" placeholder="请选择区域">
              <el-option v-for="dict in areaList" :key="`qy`+dict.id" :label="dict.name" :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="库区" prop="reservoirId">
            <el-select v-model="updateform.reservoirId" @change="changeGoodShelfListByUpdateform()" placeholder="请选择库区">
              <el-option v-for="dict in kqList" :key="`kq`+dict.id" :label="dict.name" :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="货架" prop="goodShelfId">
            <el-select v-model="updateform.goodShelfId" placeholder="请选择货架">
              <el-option v-for="dict in goodShelfList" :key="`goodShelf`+dict.id" :label="dict.name" :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="存放物料类别" prop="categoryId">
            <el-select v-model="updateform.categoryId" placeholder="请选择物料类别" clearable>
              <el-option v-for="dict in categoryList" :key="`lb`+dict.id" :label="dict.name" :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-button style="margin-top:5px;margin-left: 20px;" type="primary" :loading="plupdateFlag" size="mini" @click="submitPUpdateForm">保存</el-button>
        </el-col>
      </el-form>
    </el-dialog>


    <!-- 库位生成 -->
    <el-dialog title="库位生成" :visible.sync="openPadd" width="800px" append-to-body>
      <el-form ref="allForm" :model="allForm" :rules="rules" label-width="80px">
        <el-col :span="8">
          <el-form-item label="起始排" prop="startRow">
            <el-input v-model="allForm.startRow" type="number" placeholder="请输入起始排" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="起始列" prop="startColumn">
            <el-input v-model="allForm.startColumn" type="number" placeholder="请输入起始列" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="起始层" prop="startPlies">
            <el-input v-model="allForm.startPlies" type="number" placeholder="请输入起始层" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="结束排" prop="endRow">
            <el-input v-model="allForm.endRow" type="number" placeholder="请输入结束排" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="结束列" prop="endColumn">
            <el-input v-model="allForm.endColumn" type="number" placeholder="请输入结束列" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="结束层" prop="endPlies">
            <el-input v-model="allForm.endPlies" type="number" placeholder="请输入结束层" />
          </el-form-item>
        </el-col>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="plcreateFlag" @click="submitPAddForm">确 定</el-button>
        <el-button @click="paddcancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLocation, getLocation, delLocation, addLocation, updateLocation,paddLocation,plcCount,plupdateLocation,updateStatus } from "@/api/wms/location";
import { listReservoir } from "@/api/wms/reservoir";
import { listArea } from "@/api/wms/area";
import { listGoodShelf } from "@/api/wms/shelf";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { listCategory } from "@/api/wms/category";
import { listUnit } from "@/api/wms/unit";
import { listContactsUnit } from "@/api/wms/contactsUnit";
import {wms} from '@/utils/agent';
import { getToken } from '@/utils/auth';
export default {
  name: "Location",
  dicts: ['wms_goods_allocation_status','wms_location_type'],
  components: { Treeselect },
  data() {
    return {
      plupdateFlag:false,
      totalCount:0,
      updateform:{},
      openPupdate:false,
      maxColumn: 5,
      maxPlies: 5,
      maxRow: 5,
      openPadd:false,
      plcreateFlag:false,

      queryAreaList: [], // 筛选的区域列表
      kqQueryList: [], // 筛选的库区列表
      goodShelfQueryList: [], //筛选的货架列表

      areaList:[],//区域列表
      kqList:[],//库区列表
      goodShelfList: [], //货架列表

      categoryList:[],//物料类别
      unitList:[],//单位
      contactsUnitList:[],//供应商
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
      // 库位表格数据
      locationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        name: null,
        status: null,
        palletNum: '',
      },
      // 表单参数
      form: {},
      // 表单参数
      allForm: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        locationType: [
          { required: true, message: "请选择库位类型", trigger: "change" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
        reservoirId: [
          { required: true, message: "库区不能为空", trigger: "blur" }
        ],
        // goodShelfId: [
        //   { required: true, message: "货架不能为空", trigger: "blur" }
        // ],
        areaId: [
          { required: true, message: "区域不能为空", trigger: "blur" }
        ],
        upperLimit: [
          { required: true, message: "存放上限不能为空", trigger: "blur" }
        ],
        locationRow: [
          { required: true, message: "排不能为空", trigger: "blur" }
        ],
        locationColumn: [
          { required: true, message: "列不能为空", trigger: "blur" }
        ],
        locationPlies: [
          { required: true, message: "层不能为空", trigger: "blur" }
        ],
        goodsAllocationType: [
          { required: true, message: "货位类型不能为空", trigger: "blur" }
        ],
        goodsAllocationStatus: [
          { required: true, message: "货位状态不能为空", trigger: "blur" }
        ],
        startRow: [
          { required: true, message: "起始排不能为空", trigger: "blur" },
          { pattern: /^\+?[1-9]\d*$/, message: '请输入大于0的正整数'}
        ],
        startColumn: [
          { required: true, message: "起始列不能为空", trigger: "blur" },
          { pattern: /^\+?[1-9]\d*$/, message: '请输入大于0的正整数'}
        ],
        startPlies: [
          { required: true, message: "起始层不能为空", trigger: "blur" },
          { pattern: /^\+?[1-9]\d*$/, message: '请输入大于0的正整数'}
        ],
        endRow: [
          { required: true, message: "结束排不能为空", trigger: "blur" },
          { pattern: /^\+?[1-9]\d*$/, message: '请输入大于0的正整数'}
        ],
        endColumn: [
          { required: true, message: "结束列不能为空", trigger: "blur" },
          { pattern: /^\+?[1-9]\d*$/, message: '请输入大于0的正整数'}
        ],
        endPlies: [
          { required: true, message: "结束层不能为空", trigger: "blur" },
          { pattern: /^\+?[1-9]\d*$/, message: '请输入大于0的正整数'}
        ],
      },
      updateRules: {
        startRow: [
          { required: true, message: "请选择起始排", trigger: "change" }
        ],
        endRow: [
          { required: true, message: "请选择结束排", trigger: "change" }
        ],
        areaId: [
          { required: true, message: "请选择区域", trigger: "change" }
        ],
        reservoirId: [
          { required: true, message: "请选择库区", trigger: "change" }
        ],
      },
      uploadUrl: process.env.VUE_APP_BASE_API +'/'+wms+'/location/importData', // 上传地址
      uploadHeaders: { Authorization: this.getToken() },
      fileList:[],
      addLoading: false, // 新增按钮loading
    };
  },
  watch:{
    '$route'(to,form){
      if (to.name == 'location') {
        this.getList();
      }
    }
  },
  created() {
    listArea({pageSize:5000}).then(response => {
      this.queryAreaList = response.rows;
    });
    this.getWarehouseList()
    this.getList();
    this.getMaxPLC();
  },
  methods: {
    getToken,

    pupdatecancel(){
      this.openPupdate = false;
    },
    submitPUpdateForm(){
      console.log(this.updateform)
      this.$refs["updateform"].validate(valid => {
        if (valid) {
          this.plupdateFlag = true
          let updateParam = {
            startRow :this.updateform.startRow,
            startColumn :this.updateform.startColumn,
            startPlies :this.updateform.startPlies,
            endRow :this.updateform.endRow,
            endColumn :this.updateform.endColumn,
            endPlies :this.updateform.endPlies,
            areaId: this.updateform.areaId,
            reservoirId: this.updateform.reservoirId,
            goodShelfId: this.updateform.goodShelfId,
            categoryId: this.updateform.categoryId,
            unitId: this.updateform.unitId,
            sameMaterialFlag: this.updateform.sameMaterialFlag,
            sameBatchFlag: this.updateform.sameBatchFlag,
            upperLimit: this.updateform.upperLimit,
            locationType: this.updateform.locationType,
          }
          plupdateLocation(updateParam).then(response => {
            this.plupdateFlag = false
            this.openPupdate = false;
            this.getList();
            this.$modal.msgSuccess("更新成功");
          }).catch(error => {
            this.plupdateFlag = false
          });
        }
      })
    },
    handleplUpdateQuery(){
      let updateParam = {
        startRow :this.updateform.startRow,
        startColumn :this.updateform.startColumn,
        startPlies :this.updateform.startPlies,
        endRow :this.updateform.endRow,
        endColumn :this.updateform.endColumn,
        endPlies :this.updateform.endPlies,
      }
      listLocation(updateParam).then(response => {
        this.totalCount= response.total
      })
    },
    submitPAddForm(){
      this.$refs["allForm"].validate(valid => {
        if (valid) {
          this.plcreateFlag = true
          paddLocation(this.allForm).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.openPadd = false;
            this.getList();
            this.getMaxPLC();
            this.plcreateFlag = false
          }).catch(error => {
            this.plcreateFlag = false
          });
        }
      })
    },
    paddcancel(){
      this.openPadd = false;
    },
    getMaxPLC(){
      plcCount({}).then(response => {
        this.maxColumn= response.maxColumn
        this.maxPlies= response.maxPlies
        this.maxRow= response.maxRow
      });
    },
    handlePUpdate(){
      this.plupdateFlag = false;
      this.openPupdate = true;
      this.updateFormReset();
      this.getWarehouseList()
      this.handleplUpdateQuery();
    },
    handlePAdd(){
      this.getWarehouseList()
      this.openPadd = true;
      this.allFormReset();
    },
    changeAreaList(){
      this.form.areaId = null;
      listArea({pageSize:5000}).then(response => {
          this.areaList = response.rows;
      });
    },
    changeAreaListByUpdateform(){
      if (this.updateform.areaId) {
        this.updateform.areaId = null;
      }
      if (this.updateform.reservoirId) {
        this.updateform.reservoirId = null;
      }
      listArea({warehouseId:this.updateform.warehouseId,pageSize:5000}).then(response => {
          this.areaList = response.rows;
      });
    },
    changeQueryReservoirList(){
      this.queryParams.reservoirId = null;
      listReservoir({areaId:this.queryParams.areaId,pageSize:5000}).then(response => {
        this.kqQueryList = response.rows;
      });
    },
    changeReservoirList(){
      this.form.reservoirId = null;
      listReservoir({areaId:this.form.areaId,pageSize:5000}).then(response => {
        this.kqList = response.rows;
      });
    },
    changeQueryGoodShelfList(){
      this.queryParams.goodShelfId = null;
      listGoodShelf({areaId:this.queryParams.areaId, reservoirId:this.queryParams.reservoirId}).then(response => {
        this.goodShelfQueryList = response.data;
      });
    },
    changeGoodShelfList(){
      this.form.goodShelfId = null;
      listGoodShelf({areaId:this.form.areaId, reservoirId:this.form.reservoirId}).then(response => {
        this.goodShelfList = response.data;
      });
    },
    changeReservoirListByUpdateform(){
      if (this.updateform.reservoirId) {
        this.updateform.reservoirId = null;
      }
      listReservoir({areaId:this.updateform.areaId,pageSize:5000}).then(response => {
        this.kqList = response.rows;
      });
    },
    changeGoodShelfListByUpdateform(){
      if (this.updateform.goodShelfId) {
        this.updateform.goodShelfId = null;
      }
      listGoodShelf({areaId:this.updateform.areaId, reservoirId:this.updateform.reservoirId}).then(response => {
        this.goodShelfList = response.data;
      });
    },
    /** 查询仓库列表 */
    getWarehouseList() {
      let qps = {
        pageNum: 1,
        pageSize: 10,
      };
      qps.pageSize = 5000,
      qps.type = 1
      listArea({pageSize:5000}).then(response => {
        this.areaList = response.rows;
      });

    },
    changeForceUpdate(){
      this.$forceUpdate();
    },
    /** 查询库位列表 */
    getList() {
      this.loading = true;
      listLocation(this.queryParams).then(response => {

        response.rows.forEach(data =>{
          if (data.sameMaterialFlag == '0'){
            data.sameMaterialFlag = '否'
          }
          if (data.sameMaterialFlag == '1'){
            data.sameMaterialFlag = '是'
          }
          if (data.sameBatchFlag == '0'){
            data.sameBatchFlag = '否'
          }
          if (data.sameBatchFlag == '1'){
            data.sameBatchFlag = '是'
          }
        });

        this.locationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });

      // 查询物料类别
      if(this.categoryList.length == 0){
        listCategory().then(response => {
          this.categoryList = response.rows;
        });
      }


      // 查询单位
      if(this.unitList.length == 0){
        listUnit().then(response => {
          this.unitList = response.rows;
        });
      }


      // 供应商
      if(this.contactsUnitList.length == 0){
        listContactsUnit().then(response => {
          this.contactsUnitList = response.rows;
        });
      }
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
        areaId: null,
        reservoirId: null,
        goodShelfId: null,
        orgId: null,
        code: null,
        name: null,
        status: null,
        startRow:null,
        startColumn: null,
        startPlies: null,
        endRow: null,
        endColumn: null,
        endPlies:null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null,
        locationType: null
      };
      this.resetForm("form");
    },
    // 表单重置
    updateFormReset() {
      this.updateform = {
        id: null,
        areaId: null,
        reservoirId: null,
        goodShelfId: null,
        orgId: null,
        code: null,
        name: null,
        status: null,
        startRow:null,
        startColumn: null,
        startPlies: null,
        endRow: null,
        endColumn: null,
        endPlies:null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null,
        locationType: null
      };
      this.resetForm("updateform");
    },
    // 表单重置
    allFormReset() {
      this.allForm = {
        id: null,
        areaId: null,
        reservoirId: null,
        goodShelfId: null,
        orgId: null,
        code: null,
        name: null,
        status: null,
        startRow:null,
        startColumn: null,
        startPlies: null,
        endRow: null,
        endColumn: null,
        endPlies:null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null,
        locationType: null
      };
      this.resetForm("allForm");
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
      this.names = selection.map(item => item.name)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加库位";
      this.getWarehouseList()
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getWarehouseList()
      const id = row.id || this.ids
      getLocation(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改库位";
        listReservoir({areaId:this.form.areaId, pageSize:5000}).then(response => {
          this.kqList = response.rows;
        });
        listGoodShelf({areaId:this.form.areaId, reservoirId:this.form.reservoirId}).then(response => {
          this.goodShelfList = response.data;
        });
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLocation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLocation(this.form).then(response => {
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
      const names = row.name || this.names;
      this.$modal.confirm('是否确认删除库位"' + names + '"的数据项？').then(function() {
        return delLocation(ids);
      }).then(() => {
        this.getList();
        this.getMaxPLC();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 启用禁用 */
    handleUpdateStatus(row, status){
      const data = {
        id: row.id,
        status: status
      }
      const msg = status == "1" ? row.name + '"启用？' : row.name + '"禁用？';

      this.$modal.confirm('是否确认将库位"' + msg).then(function() {
        return updateStatus(data);
      }).then(() => {
        this.getList();
        if(status == "1"){
          this.$modal.msgSuccess("启用成功");
        } else {
          this.$modal.msgSuccess("禁用成功");
        }

      }).catch(() => {});

    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/location/export', {...this.queryParams}, `location_${new Date().getTime()}.xlsx`)
    },
    /** 下载模板按钮操作 */
    handleExportDemo() {
      this.download(wms+'/location/export/demo',{}, `location_${new Date().getTime()}.xlsx`)
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
