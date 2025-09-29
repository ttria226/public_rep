<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="批次号" prop="batchCode">
        <el-input v-model="queryParams.batchCode" placeholder="请输入批次号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5" style="padding-right: 70%">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['base:print:add']"
        >新增</el-button>
      </el-col>
        <template>
          <el-radio v-model="printFloor" label="1"><span style="color: red">一楼RFID打印机</span></el-radio>
          <el-radio v-model="printFloor" label="2"><span style="color: red">二楼RFID打印机</span></el-radio>
        </template>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['base:print:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
    </el-row>

    <el-table v-loading="loading" :data="printList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预约单据号" align="center" prop="advanceDeliveryCode" width="150" :show-overflow-tooltip="true"/>
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true"/>
      <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true"/>
      <el-table-column label="批次号" align="center" prop="batchCode" />
      <el-table-column label="打印数" align="center" width="100" prop="sumCount" :show-overflow-tooltip="true"/>
      <el-table-column label="RFID单位" align="center" width="80" prop="maxUnitName" />
      <el-table-column label="换算数量" align="center" width="80" prop="convertCount" />
      <el-table-column label="计量单位" align="center" width="80" prop="minUnitName" />
      <el-table-column label="打印次数" align="center" width="80" prop="printCount" />
      <el-table-column label="打印类型" align="center" width="80" prop="printType">
        <template slot-scope="scope">
          <span v-if="scope.row.printType == '1'">RFID打印</span>
          <span v-if="scope.row.printType == '2'">普通打印</span>
        </template>
      </el-table-column>
      <el-table-column label="打印时间" align="center" prop="printTime" width="150"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="scope.row.printCount == 0"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:print:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.printCount == 0"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:print:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-printer"
            @click="handlePrint(scope.row)"
          >RFID打印</el-button>
          <el-button
            size="mini"
            icon="el-icon-printer"
            type="text"
            @click="createRfid(scope.row)"
            v-if="scope.row.rfidHeads == null"
          >RFID生成</el-button>
          <el-button
            size="mini"
            icon="el-icon-printer"
            type="text"
            @click="handlePrint2(scope.row)"
          >普通打印</el-button>
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

    <!-- 添加或修改rfid打印记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="单据编号" prop="advanceDeliveryCode">
          <el-input v-model="form.advanceDeliveryCode" :disabled="showDisable" @focus="materialComOpen" placeholder="请输入单据编号"/>
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="form.materialName" disabled placeholder="请输入包装编码"/>
        </el-form-item>
        <el-form-item label="预计入库数量" prop="predictCount">
          <el-input v-model="form.predictCount"  disabled placeholder="请输入包装编码"/>
        </el-form-item>
        <el-form-item label="剩余未打印数量" prop="returnCount">
          <el-input v-model="form.returnCount" disabled placeholder="请输入包装编码"/>
        </el-form-item>
        <el-form-item label="换算关系" prop="unitConfig">
          <el-select style="width: 30%;" v-model="form.maxUnit" clearable filterable placeholder="请选择基本单位" @change="qualifyScoreInputmax">
            <el-option v-for="item in minUnitList" :key="item.id" :label="item.name" :value="item.id" :disabled="item.disabled"></el-option>
          </el-select>
          <el-input style="width: 30%;margin-left: 10px;" @input="qualifyScoreInput" oninput="value=value.replace(/[^\d]/g,'')" @blur="hsblur" v-model="form.convertCount" placeholder="请输入换算值"  maxlength="10"/>
          <el-select style="width: 30%;margin-left: 10px;" v-model="form.minUnit" disabled filterable placeholder="请选择包装单位" @change="qualifyScoreInput">
            <el-option v-for="item in unitList" :key="item.id" :label="item.name" :value="item.id" :disabled="item.disabled"></el-option>
          </el-select>
          <!-- <el-input style="width: 48.5%;margin-left: 10px;" disabled v-model="form.companyName" placeholder="请输入换算单位"/> -->
        </el-form-item>
        <el-form-item label="打印物料数量" prop="sumCount">
          <el-input v-model="form.sumCount" style="width: 40%;" placeholder="请输入本次打印数量" @input="qualifyScoreInput" oninput="value=value.replace(/[^\d]/g,'')" onblur="value=value>0?parseInt(value):''" maxlength="5">
            <i slot="suffix" style="margin-right: 10px;font-weight: bold;color: black;font-size: 16px;">{{form.maxUnitName}}</i>
          </el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 批量打印 <div ><vue-barcode width="470" height="120" value="'SL0000000000000000000'" :displayValue="false"></vue-barcode></div>-->
    <el-dialog title="单个打印" :visible.sync="batchPrintOpen" width="500px" append-to-body>
      <!--批量打印 -->
      <div style="widht:100%;height: 400px;overflow: hidden;">
        <div id="batchPrintArea">
          <div  class="code_box" style="margin-top: -38px;width: 200px;height: auto;">
            <div style="width:150px;height:170px;margin-top:-25px;display: block;text-align: center;"  v-for="(item, index) in rfids" :key="item + index">
              <vue-qr style="margin-left:0px;" :size="150" :text="item" ></vue-qr>
              <span style="font-weight:bold;font-size:16px;margin:0 auto;margin-top:-1px;margin-left:0px;text-align: center;" >
                    {{mcode}}
                  </span>
            </div>
          </div>
        </div>
        <div style="width: 100%;text-align: center;position: absolute;top: 440px;z-index: 1;right:20px;">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-printer"
            v-print="print"
          >打印</el-button>
        </div>

      </div>

    </el-dialog>
    <MaterialCom ref="materialCom" @setMaterial = "setMaterial"></MaterialCom>
  </div>
</template>

<script>
import { listPrint, getPrint, delPrint, addPrint, updatePrint ,printInfo,printInfoErcode} from "@/api/base/print";
import MaterialCom from './components/material'
import { listUnit } from "@/api/wms/unit";
import VueBarcode from "vue-barcode";
import VueQr from "vue-qr";
import print from 'vue-print-nb'


export default {
  name: "Print",
  directives: { print, },
  components:{
    VueBarcode, VueQr,MaterialCom
  },
  data() {
    let unitConfigRules = (rule, value, callback)=>{
      if(this.form.convertCount && this.form.maxUnit){
        callback();
      } else if(!this.form.convertCount && this.form.maxUnit){
        callback(new Error('请输入换算值'));
      } else if(!this.form.maxUnit && this.form.convertCount){
        callback(new Error('请选择换算单位'));
      } else if(!this.form.maxUnit && !this.form.convertCount){
        callback(new Error('请输入换算值和选择换算单位'));
      }
    }
    return {
      printErCode: false,
      printFloor: '1',
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
      // rfid打印记录表格数据
      printList: [],
      unitList:[],
      minUnitList: [],
      unitConfig : [
        {count:'',maxUnit:''}
      ],
      // 弹出层标题
      title: "",
      showDisable:false,
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        materialName: null,
        batchCode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        advanceDeliveryCode:[
          {required: true, message: "单据号不能为空", trigger: "blur"}
        ],
        sumCount:[
          {required: true, message: "打印总数不能为空", trigger: "blur"}
        ],
        unitConfig:[
          {required: true, validator: unitConfigRules,trigger:"blur"}
        ],
      },
      // 批量打印
      print: {
        id: 'batchPrintArea',
        popTitle: '', // 打印配置页上方的标题
        extraHead: '', // 最上方的头部文字，附加在head标签上的额外标签，使用逗号分割
        preview: false, // 是否启动预览模式，默认是false
        previewTitle: '预览的标题', // 打印预览的标题
        previewPrintBtnLabel: '预览结束，开始打印', // 打印预览的标题下方的按钮文本，点击可进入打印
        zIndex: 20002, // 预览窗口的z-index，默认是20002，最好比默认值更高
        previewBeforeOpenCallback () { console.log('正在加载预览窗口！'); console.log(this.msg, this)}, // 预览窗口打开之前的callback
        previewOpenCallback () { console.log('已经加载完预览窗口，预览打开了！') }, // 预览窗口打开时的callback
        beforeOpenCallback () { console.log('开始打印之前！') }, // 开始打印之前的callback
        openCallback () { console.log('执行打印了！') }, // 调用打印时的callback
        closeCallback () { console.log('关闭了打印工具！') }, // 关闭打印的callback(无法区分确认or取消)
        clickMounted () { console.log('点击v-print绑定的按钮了！'), this.batchPrintSure = false },
        // url: 'http://localhost:8080/', // 打印指定的URL，确保同源策略相同
        // asyncUrl (reslove) {
        //   setTimeout(() => {
        //     reslove('http://localhost:8080/')
        //   }, 2000)
        // },
        standard: '',
        extarCss: ''
      },
      mcode:'0001',
      rfids:['10001','10002','10002'],
      batchPrintOpen:false,
    };
  },
  created() {
    this.getList();
    this.listUnit();
  },
  watch: {
    $route(to, form) {
      if (to.name == 'unitConfig') {
        this.getList()
      }
    }
  },
  methods: {
    // 创建RFID
    createRfid(row){
      this.$modal.confirm('是否确认创建物料"' + row.materialName + '"的RFID,创建成功后将不可删除？').then(function() {
        return printInfoErcode({id: row.id})
      }).then(() => {
        this.$modal.msgSuccess("创建成功");
        this.getList();
      }).catch(() => {});
    },

    // 点击批量打印
    handlePrint2(row) {
      if (!row.rfidHeads){
        this.$modal.msgWarning("请先创建RFID");
      }else {
        this.rfids = row.rfidHeads.split(",");
        this.mcode = row.materialCode;
        // console.log(this.currentData);
        this.batchPrintOpen = true;
      }
    },
    hsblur(){
        let value = this.form.convertCount;
        if(value>0){
            this.form.convertCount=value>0?parseInt(value):''
        }
        let sycount = this.form.returnCount
        // console.log(sycount / value);
        if(sycount / value>0){
            this.form.sumCount = parseInt(sycount / value)
        }
    },
    /** 查询rfid打印记录列表 */
    getList() {
      this.loading = true;
      listPrint(this.queryParams).then(response => {
        this.printList = response.rows;
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
        advanceRegistrationId: null,
        advanceDeliveryCode: null,
        sumCount: null,
        printCount: null,
        printTime: null,
        minUnit: null,
        minUnitName: null,
        maxUnit: null,
        maxUnitName: null,
        convertCount: null,
        status: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deptId: null,
        deptName: null,
        delFlag: null
      };
      this.showDisable=false;
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
      this.open = true;
      this.title = "添加打印配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPrint(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.showDisable = true;
        this.title = "修改打印配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      console.log("1111",this.form)

      this.$refs["form"].validate(valid => {
        if (valid) {
          this.unitList.map((item) => {
            if(item.id == this.form.maxUnit){
              this.form.maxUnitName = item.name
            }
          })

          if(this.form.sumCount* this.form.convertCount > this.form.returnCount){
            this.$modal.msgError("打印总数不可超过剩余可打印数量");
            return;
          }
          if (this.form.id != null) {
            updatePrint(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPrint(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除rfid打印记录编号为"' + ids + '"的数据项？').then(function() {
        return delPrint(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handlePrint(row) {
      let printFloor = this.printFloor;
      this.$modal.confirm('是否确认打印物料编号为"' + row.materialName + '"的数据项,打印成功后将不可删除？').then(function() {
        let param = {
          id: row.id,
          printFloor: printFloor
        };
        return printInfo(param);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("打印需要时间，请稍后等待");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('base/print/export', {
        ...this.queryParams
      }, `print_${new Date().getTime()}.xlsx`)
    },
    // 单位列表
    listUnit(){
      listUnit({pageNum: 1, pageSize: 5000}).then(res=>{
        this.unitList = res.rows;
        this.minUnitList = res.rows;
      })
    },
    qualifyScoreInputmax(e){
        this.unitList.map((item) => {
            if(item.id == e){
                this.form.maxUnitName = item.name
            }
        })
        this.$forceUpdate()
    },
    /** 输入框输入值改变重绘*/
    qualifyScoreInput() {
      this.$forceUpdate()
    },
    // 打开选择物料弹窗
    materialComOpen() {
      this.$refs.materialCom.open = true
    },
    setMaterial(material) {
      console.log("111111",material)
      if (material) {
        this.form.advanceRegistrationId = material.id
        this.form.advanceDeliveryCode = material.advanceDeliveryCode
        this.form.materialName = material.materialName
        this.form.materialId = material.materialId
        this.form.maxUnit = material.unitId
        this.form.maxUnitName = material.unitName
        this.form.minUnit = material.unitId
        this.form.minUnitName = material.unitName
        this.form.predictCount = material.predictCount
        this.form.returnCount = material.predictCount - material.returnCount
      } else {
        this.form.materialName = ''
      }
    }
  }
};
</script>
<style lang="scss">
  .print-area{
    // margin: 20px;
    width: 600px;
    height: 600px;
    display: flex;
    justify-content: center;
    align-items: center;
    .code-box{
      width: 600px;
      height: 600px;
      border: 3px solid skyblue;
      display: flex;
      justify-content: center;
      align-items: center;
      .img-box{
        border: 1px solid skyblue;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }
    }
  }
  .code_box {
    //display:flex;
    //flex-direction: column;
    //align-items: center;
    //margin-top: -38px;
  }
  .batch-print-area{
    // margin: 20px;
    display: none;
    position: fixed;
    left: 0;
    top: 0;
    width: 600px;
    height: 600px;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: -999;
    .batch-code-box{
      width: 600px;
      // height: 600px;
      // background: #FFFFFF;
      margin-top: -50px;
      border: 3px solid skyblue;
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      align-items: center;
      .batch-img-box{
        margin-top: -60px;
        border: 1px solid skyblue;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }
    }
  }
</style>
<style>
  @page{
    size: auto;
    /* margin: 5mm; */
  }
</style>
<style media="print" lang="scss">
  @page {
    size: auto;
    margin: 3mm;
  }
  @media print {
    html {
      background-color: #ffffff;
      height: auto;
      margin: 0px;
    }

    body {
      border: solid 1px #ffffff;
      margin: 10mm 15mm 10mm 15mm;
    }
    table {
      table-layout: auto !important;
    }

    .el-table__header-wrapper .el-table__header {
      width: 100% !important;
      border: solid 1px #f2f2f2;
    }
    .el-table__body-wrapper .el-table__body {
      width: 100% !important;
    }
    #pagetable table {
      table-layout: fixed !important;
    }
    #printArea table {
      table-layout: auto !important;
    }

    #printArea .el-table__header-wrapper .el-table__header {
      width: 100% !important;
      border: solid 1px #f2f2f2;
    }
    #printArea .el-table__body-wrapper .el-table__body {
      width: 100% !important;
    }
    #printArea #pagetable table {
      table-layout: fixed !important;
    }
  }
</style>
