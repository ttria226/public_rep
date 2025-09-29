<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编码" prop="rfid">
        <el-input
          v-model="queryParams.rfid"
          placeholder="请输入编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料编码" prop="materialCode">
        <el-input
          v-model="queryParams.materialCode"
          placeholder="请输入物料编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="rfid" prop="rfid">-->
<!--        <el-input-->
<!--          v-model="queryParams.rfid"-->
<!--          placeholder="请输入rfid"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="批次号" prop="batchCode">
        <el-input
          v-model="queryParams.batchCode"
          placeholder="请输入批次号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="rfid编号" prop="rfidHead">
        <el-input
          v-model="queryParams.rfidHead"
          placeholder="请输入rfid编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item style="margin-right: 40%">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

      <template>
        <el-radio v-model="printFloor" label="1"><span style="color: red">一楼RFID打印机</span></el-radio>
        <el-radio v-model="printFloor" label="2"><span style="color: red">二楼RFID打印机</span></el-radio>
      </template>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['inout:detail:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['inout:detail:edit']"
        >修改</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['inout:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['inout:detail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="主键" align="center" prop="id" />-->
      <el-table-column label="编码" align="center" prop="rfid" />
<!--      <el-table-column label="入库单详情标识" align="center" prop="advanceRegistrationId" />-->
<!--      <el-table-column label="物料标识" align="center" prop="materialId" />-->
      <el-table-column label="物料名称" align="center" width="100" prop="materialName" />
      <el-table-column label="物料编码" align="center" width="100" prop="materialCode" />
      <el-table-column label="rfid" align="center" prop="rfidHead" />
      <el-table-column label="物料数量" align="center" prop="rfidCount" />
      <el-table-column label="批次号" align="center" prop="batchCode" />
      <el-table-column label="库位" align="center" prop="locationName" />
      <el-table-column label="物料重量（kg）" align="center" prop="weight" />
      <el-table-column label="物料价格（元）" align="center" prop="price" />
<!--      <el-table-column label="小单位数量" align="center" prop="smallUnitCount" />-->
<!--      <el-table-column label="小件已使用数量" align="center" prop="useCount" />-->
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '1'">已入库</span>
          <span v-else-if="scope.row.status == '2'">已出库未复核</span>
          <span v-else-if="scope.row.status == '3'">已出库已复核</span>
          <span v-else-if="scope.row.status == '4'">已组盘</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['inout:detail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            icon="el-icon-printer"
            type="text"
            @click="handlePrint(scope.row)"
          >RFID打印</el-button>
          <el-button
                      size="mini"
                      icon="el-icon-printer"
                      type="text"
                      @click="handlePrint2(scope.row)"
                    >普通打印</el-button>

<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['inout:detail:remove']"-->
<!--          >删除</el-button>-->
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
    <!-- 单个打印 <div ><vue-barcode width="470" height="120" value="'SL0000000000000000000'" :displayValue="false"></vue-barcode></div>-->
        <el-dialog title="单个打印" :visible.sync="batchPrintOpen" width="500px" append-to-body>
           <!--单个打印 -->
          <div style="widht:100%;height: 400px;overflow: hidden;">
            <div id="printArea">
              <div  class="code_box">
                <div style="width:200px;height:20px;margin-top:-15px;">
                  <vue-qr style="margin-left:20px;" :size="150" :text="currentData.qrcode" ></vue-qr>
                  <span style="text-align: center;font-weight:bold;font-size:16px;margin:0 auto;margin-top:-1px;margin-left:60px;" >
                    {{currentData.qrtext}}
                  </span>
                </div>
              </div>
            </div>
            <div style="width: 100%;margin-top: 300px;text-align: center;">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-printer"
                v-print="print"
              >临时打印</el-button>
            </div>

          </div>

        </el-dialog>

    <!-- 添加或修改物料详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
<!--        <el-form-item label="rfid" prop="rfid">-->
<!--          <el-input v-model="form.rfid" placeholder="请输入rfid" />-->
<!--        </el-form-item>-->
        <el-form-item label="物料重量（kg）" prop="weight">
          <el-input v-model="form.weight" placeholder="请输入物料重量" />
        </el-form-item>
        <el-form-item label="物料价格（元）" prop="price">
          <el-input v-model="form.price" placeholder="请输入物料价格" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { listDetail, getDetail, delDetail, addDetail, updateDetail,printjk } from "@/api/wms/detail";
import {wms} from '@/utils/agent';
import VueBarcode from 'vue-barcode'
import VueQr from 'vue-qr'
import print from 'vue-print-nb'
export default {
  name: "Detail",
   directives: { print, },
   components: {  VueBarcode, VueQr, },
  data() {
    return {
      printFloor: "1",
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
      // 物料详情表格数据
      detailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        materialName: null,
        materialCode: null,
        rfid: null,
        batchCode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      currentData:{qrcode:'RF10000001',qrtext:'10000001'},
      print: {
          id: 'printArea',
          //popTitle: '料箱编号', // 打印配置页上方的标题
          //extraHead: '', // 最上方的头部文字，附加在head标签上的额外标签，使用逗号分割
          preview: false, // 是否启动预览模式，默认是false
          previewTitle: '预览的标题', // 打印预览的标题
          previewPrintBtnLabel: '预览结束，开始打印', // 打印预览的标题下方的按钮文本，点击可进入打印
          zIndex: 20002, // 预览窗口的z-index，默认是20002，最好比默认值更高
          previewBeforeOpenCallback () { console.log('正在加载预览窗口！'); console.log(this.msg, this) }, // 预览窗口打开之前的callback
          previewOpenCallback () { console.log('已经加载完预览窗口，预览打开了！') }, // 预览窗口打开时的callback
          beforeOpenCallback () { console.log('开始打印之前！') }, // 开始打印之前的callback
          openCallback () { console.log('执行打印了！') }, // 调用打印时的callback
          closeCallback () { console.log('关闭了打印工具！') }, // 关闭打印的callback(无法区分确认or取消)
          clickMounted () { console.log('点击v-print绑定的按钮了！') },
          // url: 'http://localhost:8080/', // 打印指定的URL，确保同源策略相同
          // asyncUrl (reslove) {
          //   setTimeout(() => {
          //     reslove('http://localhost:8080/')
          //   }, 2000)
          // },
          standard: '',
          extarCss: ''
        },
        batchPrintOpen:false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
  // 点击批量打印
      handlePrint2(row) {
        console.log(row);
        this.currentData.qrcode = row.rfidHead
        this.currentData.qrtext = row.materialCode
console.log(this.currentData);
        this.batchPrintOpen = true
      },
    /** 查询物料详情列表 */
    getList() {
      this.loading = true;
      listDetail(this.queryParams).then(response => {
        this.detailList = response.rows;
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
        code: null,
        advanceRegistrationId: null,
        materialId: null,
        materialName: null,
        materialCode: null,
        rfid: null,
        batchCode: null,
        trayId: null,
        locationId: null,
        status: null,
        useCount: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deptId: null,
        deptName: null,
        delFlag: null,
        weight: null,
        price: null,
        smallUnitCount: null
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
      this.open = true;
      this.title = "添加物料详情";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDetail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物料详情";
      });
    },
    /** 打印按钮 */
    handlePrint(row){
      let params = {
        detailId: row.id,
        printFloor: this.printFloor
      }
      this.$modal.confirm('是否确认打印【'+ (row.rfid) +'】数据？').then(function() {
        return printjk(params);
      }).then(() => {
        if (res.code == 200) {
          this.$modal.msgSuccess("打印成功");
        } else {
          this.$modal.msgSuccess(res.msg);
        }
      }).catch(() => {});

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDetail(this.form).then(response => {
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
        return delDetail(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms+'/inout/detail/export', {
        ...this.queryParams
      }, `detail_${new Date().getTime()}.xlsx`)
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
	  display:flex;
	  flex-direction: column;
	  align-items: center;
    margin-top: -38px;
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

