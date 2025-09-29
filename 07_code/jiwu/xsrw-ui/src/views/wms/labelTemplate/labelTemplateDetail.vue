<template>
  <div class="app-container" v-if="open">
    <el-dialog :title="title" :visible.sync="open" width="1400px" append-to-body>
      <el-form ref="form" :model="templateInfo[0]" :rules="rules" label-width="100px">
        <el-table :data="templateInfo">
          <el-table-column label="模板名称" align="center" prop="name" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-form-item prop="name" label-width="0" style="margin: 0px;">
                <el-input v-model="templateInfo[0].name" placeholder="请输入模板名称" size="small" maxlength="40" show-word-limit :disabled = "type == 3"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="模板类型" align="center" prop="labelType" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-form-item prop="labelType" label-width="0" style="margin: 0px;">
                <el-select v-model="templateInfo[0].labelType" :disabled="id > 0" @change="isThereData" placeholder="请选择模板类型">
                  <el-option v-for="dict in types" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"></el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column :label="templateInfo[0].labelType == 2 ? '二维码' : '条码'" align="center" prop="code">
            <template slot-scope="scope">
              <el-form-item label="宽" prop="objectWidth" style="margin: 0px;">
                <el-input v-model="templateInfo[0].objectWidth" :placeholder="templateInfo[0].labelType == 2 ? '请输入二维码宽度' : '请输入条码宽度'" @blur="isThereData" @input="objectWidthInput" maxlength="6" show-word-limit size="small"  :disabled = "type == 3"></el-input>
              </el-form-item>
              <el-form-item label="高" prop="objectHeight" style="margin: 0px;">
                <el-input v-model="templateInfo[0].objectHeight" :placeholder="templateInfo[0].labelType == 2 ? '请输入二维码高度' : '请输入条码高度'" @blur="isThereData" @input="objectHeightInput" maxlength="6" show-word-limit size="small"  :disabled = "type == 3 || templateInfo[0].labelType == 2"></el-input>
              </el-form-item>
              <!-- <el-form-item label="字体大小" prop="objectSize" style="margin: 0px;">
                <el-input v-model="templateInfo[0].objectSize" :placeholder="templateInfo[0].labelType == 2 ? '请输入二维码字体大小' : '请输入条码字体大小'" @blur="isThereData" @input="objectSizeInput" maxlength="2" show-word-limit size="small"  :disabled = "type == 3"></el-input>
              </el-form-item> -->
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <el-row :gutter="10" class="mb8" style="margin-top: 30px;">
        <el-col :span="1.5" v-if="type != 3">
          <el-button type="primary" plain size="mini" @click="submitForm" >保存</el-button>
        </el-col>
        <el-col :span="1.5" v-if="type != 1">
          <el-button type="primary" plain size="mini" v-print="print" v-hasPermi="['wms:labelTemplate:print']">打印</el-button>
        </el-col>
<!--        <el-col :span="1.5" v-if="id && templateInfo[0].labelType == 1">-->
<!--          <el-button type="primary" plain size="mini" @click="selectTemplate" v-hasPermi="['wms:labelTemplate:selectTray']">选择载具</el-button>-->
<!--        </el-col>-->
        <el-col :span="1.5" v-if="id && templateInfo[0].labelType == 2">
          <el-button type="primary" plain size="mini" @click="selectMaterial" v-hasPermi="['wms:labelTemplate:selectMaterial']">选择物料</el-button>
        </el-col>
        <el-col :span="1.5" v-if="id && templateInfo[0].labelType == 1">
          <el-button type="primary" plain size="mini" @click="handleBatchPrint" v-hasPermi="['wms:labelTemplate:batchPrint']">批量打印</el-button>
        </el-col>
      </el-row>

      <el-row :gutter="10" class="mb8">
        <el-col :span='13'>
          <div v-if="id && templateInfo[0].labelType == 1">
            <TrayTabel ref="trayTabel" @setCurrentRow="setCurrentRow" :labelTemplateId="id"></TrayTabel>
          </div>
          <div v-if="id && templateInfo[0].labelType == 2">
            <MaterialTabel ref="materialTabel"></MaterialTabel>
          </div>
        </el-col>

        <el-col :span='11'>
          <!--单个打印 -->
          <div id="printArea">
            <div  v-if="isData" class="code_box">
              <div v-if="templateInfo && templateInfo[0] && templateInfo[0].labelType == 1"><vue-barcode :width="templateInfo[0].objectWidth / 220" :height="templateInfo[0].objectHeight" :value="currentRow &&currentRow.code ? currentRow.code : 'SL0000000000000000000'" :displayValue="false"></vue-barcode></div>
              <div v-if="templateInfo && templateInfo[0] && templateInfo[0].labelType == 2"><vue-qr :size="templateInfo[0].objectWidth" :text="currentRow && currentRow.batchCode ? currentRow.batchCode : 'SL0000000000000000000'"></vue-qr></div>

              <span v-if="templateInfo && templateInfo[0] && templateInfo[0].labelType == 1" style="text-align: center;" >{{currentRow && currentRow.code ? currentRow.code : 'SL0000000000000000000'}}<span style="color: red;">{{currentRow && currentRow.code ? '' : '(示例)'}}</span></span>
              <span v-if="templateInfo && templateInfo[0] && templateInfo[0].labelType == 2" style="text-align: center;" >{{currentRow && currentRow.batchCode ? currentRow.batchCode : 'SL0000000000000000000'}}
                <span style="color: red;">{{currentRow && currentRow.batchCode ? '' : '(示例)'}}</span>
                <p>{{currentRow && currentRow.name ? currentRow.name : ''}}</P>
              </span>
            </div>
            <div v-else>
              <span>模板信息缺失</span>
            </div>

          </div>
        </el-col>
      </el-row>

      <!-- 批量打印 -->
      <el-dialog title="批量打印" :visible.sync="batchPrintOpen" width="500px" append-to-body>
        <el-form ref="batchPrintForm" :model="batchPrintForm" :rules="batchPrintRules" label-width="80px">
          <el-form-item label="载具类型" prop="trayCategory">
            <el-select v-model="batchPrintForm.trayCategory" placeholder="请选择载具类型" class="select-input-form">
                <el-option v-for="dict in dict.type.wms_t_tray_category" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
<!--          <el-form-item label="生成数量" prop="count">-->
<!--            <el-input v-model="batchPrintForm.count" oninput="value=value.replace(/[^\d]/g,'')" onblur="value=(value ? parseInt(value) : '')" maxlength="6" show-word-limit  placeholder="请输入生成数量" />-->
<!--          </el-form-item>-->
          <el-form-item label="编号-起" prop="startNo">
            <el-input v-model="batchPrintForm.startNo" maxlength="20"  placeholder="例如T20230101000001" />
          </el-form-item>
          <el-form-item label="编号-止" prop="endNo">
            <el-input v-model="batchPrintForm.endNo" maxlength="20" placeholder="例如T20230101000001" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" :loading="batchPrintSubmit" @click="submitBatchPrint">确 定</el-button>
          <el-button @click="batchPrintCancel">取 消</el-button>
        </div>
      </el-dialog>

      <MaterialTabelPop ref="materialTabelPop" :labelTemplateId="id" @setCurrentRow="setCurrentRow"></MaterialTabelPop>
      <TrayTabelPop ref="trayTabelPop" :labelTemplateId="id" @trayTabelRefresh="trayTabelRefresh"></TrayTabelPop>

    </el-dialog>

    <!-- 批量打印确认 -->
    <el-dialog title="" :visible.sync="batchPrintSure" width="30%">
      <span>共查询到{{this.batchPrintList.length}}条数据，是否开始打印标签？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="batchPrintSure = false">取 消</el-button>
        <el-button type="primary"  v-print="batchPrint" @click="batchPrintSure = false">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 批量打印区域 -->
    <div class="batch-print-area">
      <div id="batchPrintArea" class="batch-code-box">
        <div v-for="(item, index) in batchPrintList" :key="item + index">
            <div class="code_box">
              <div ><vue-barcode :width="templateInfo[0].objectWidth / 220" :height="templateInfo[0].objectHeight" :value="item" :displayValue="false"></vue-barcode></div>
              <span style="text-align: center;" >{{item}}
<!--                <span style="color: red;">{{currentRow && currentRow.batchCode ? '' : '(示例)'}}</span>-->
<!--                <p>{{item}}</P>-->
              </span>
            </div>
<!--          <div class="batch-img-box" :style="{width: templateInfo[0].imageWidth + 'px',height: templateInfo[0].imageHeight + 'px',}">-->
<!--            <div><vue-barcode :width="templateInfo[0].objectWidth / 220" :height="templateInfo[0].objectHeight" :value="item" :displayValue="false"></vue-barcode></div>-->
<!--            <span style="text-align: center;font-size: 10px;width: 100%">{{item}}</span>-->
<!--          </div>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { listLabelTemplate, getLabelTemplate, delLabelTemplate, addLabelTemplate, updateLabelTemplate, batchPrintList, } from "@/api/wms/labelTemplate";
  import { listMaterial } from "@/api/wms/material";

  import MaterialTabel from './components/materialTabel/index'
  import MaterialTabelPop from './components/materialTabelPop/index'
  import TrayTabel from './components/trayTabel/index'
  import TrayTabelPop from './components/trayTabelPop/index'
  import VueBarcode from 'vue-barcode'
  import VueQr from 'vue-qr'

  import print from 'vue-print-nb'

  export default {
    name: "LabelTemplateDetail",
    directives: { print, },
    components: { MaterialTabel, MaterialTabelPop, TrayTabel, TrayTabelPop, VueBarcode, VueQr, },
    dicts: ['wms_t_tray','wms_t_tray_category'],
    data() {
      return {
        // 遮罩层
        loading: false,
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
        // 标签模板表格数据
        labelTemplateList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        addOpen: false,
        batchPrintOpen: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: null,
          labelType: null,
          labelWidth: null,
          labelHeight: null,
          imageWidth: null,
          imageHeight: null,
          objectWidth: null,
          objectHeight: null,
          status: null,
        },
        templateInfo: [{
          name: null,
          labelWidth: null,
          labelHeight: null,
          imageWidth: null,
          imageHeight: null,
          objectWidth: null,
          objectHeight: null,
          // objectSize: null,
          status: null,
        }],
        // 表单参数
        form: {},
        batchPrintForm: {
          trayCategory: null,
          startNo: null,
          endNo: null,
        },
        // 表单校验
        rules: {
          name: [
            { required: true, message: '请输入模板名称', trigger: 'blur' }
          ],
          labelType: [
            { required: true, message: '请选择模板类型', trigger: 'change' }
          ],
          labelWidth: [
            { required: true, message: '请输入标签宽度', trigger: 'blur' }
          ],
          labelHeight: [
            { required: true, message: '请输入标签高度', trigger: 'blur' }
          ],
          imageWidth: [
            { required: true, message: '请输入图片宽度', trigger: 'blur' }
          ],
          imageHeight: [
            { required: true, message: '请输入图片高度', trigger: 'blur' }
          ],
          objectWidth: [
            { required: true, message: '请输入条码宽度', trigger: 'blur' }
          ],
          objectHeight: [
            { required: true, message: '请输入条码高度', trigger: 'blur' }
          ],
          // objectSize: [
          //   { required: true, message: '请输入条码字体大小', trigger: 'blur' }
          // ],
        },
        batchPrintRules:{
          trayCategory: [
            { required: true, message: '请选择载具类型', trigger: 'change' }
          ],
          startNo: [
            { required: true, message: '请输入载具编号', trigger: 'blur' }
          ],
          endNo: [
            { required: true, message: '请输入载具编号', trigger: 'blur' }
          ],
        },
        id: null,
        type: 1, // 页面类型
        types: [], // 模板类型源数据
        materialList: [], // 物料列表
        currentRow: null, // 当前选中行
        isData: false, // 是否有数据
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
        // 批量打印
        batchPrint: {
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
        batchPrintList: [],
        batchPrintSure: false,

        batchPrintSubmit: false, // 批量打印是否正在提交
      };
    },
    watch: {
      open(){
        this.isData = false
        this.initData()
      }
    },
    created() {
      // 获取类型源数据
      this.getDicts('wms_label_template_type').then(response => {
        this.types = response.data
      })
      this.initData()
      // this.getList();
    },
    methods: {
      // 获取模板详情并初始化数据
      initData() {
        if (this.id) {
          getLabelTemplate(this.id).then(response => {
            this.templateInfo = []
            this.templateInfo.push(response.data)
            this.isThereData()
          });
        } else {
          this.templateInfo = [{
            name: null,
            labelWidth: null,
            labelHeight: null,
            imageWidth: null,
            imageHeight: null,
            objectWidth: null,
            objectHeight: null,
            status: null,
          }]
        }
      },
      isThereData() {
        const templateInfo = this.templateInfo[0]
        if(templateInfo && templateInfo.labelType && templateInfo.labelWidth && templateInfo.labelHeight && templateInfo.imageWidth && templateInfo.imageHeight && templateInfo.objectWidth && templateInfo.objectHeight) {
          this.isData = true
        } else {
          this.isData = false
        }
      },
      labelWidthInput() {
        const val = this.templateInfo[0].labelWidth.replace(/[^\d]/g,'')
        this.templateInfo[0].labelWidth = val ? (val > 600 ? 600 : val) : ''
      },
      labelHeightInput() {
        const val = this.templateInfo[0].labelHeight.replace(/[^\d]/g,'')
        this.templateInfo[0].labelHeight = val ? (val > 600 ? 600 : val) : ''
      },
      imageWidthInput() {
        const val = this.templateInfo[0].imageWidth.replace(/[^\d]/g,'')
        this.templateInfo[0].imageWidth = val ? (val > 600 ? 600 : val) : ''
      },
      imageHeightInput() {
        const val = this.templateInfo[0].imageHeight.replace(/[^\d]/g,'')
        this.templateInfo[0].imageHeight = val ? (val > 600 ? 600 : val) : ''
      },
      objectWidthInput() {
        const val = this.templateInfo[0].objectWidth.replace(/[^\d]/g,'')
        this.templateInfo[0].objectWidth = val ? (val > 600 ? 600 : val) : ''
        if (this.templateInfo[0].labelType == 2) {
          this.templateInfo[0].objectHeight = this.templateInfo[0].objectWidth
        }
      },
      objectHeightInput() {
        const val = this.templateInfo[0].objectHeight.replace(/[^\d]/g,'')
        this.templateInfo[0].objectHeight = val ? (val > 600 ? 600 : val) : ''
      },
      /** 查询标签模板列表 */
      getList() {
        this.loading = true;
        listLabelTemplate(this.queryParams).then(response => {
          this.materialList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 查询物料列表 */
      getMaterialList() {
        this.loading = true;
        listLabelTemplate(this.queryParams).then(response => {
          this.labelTemplateList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.reset();
      },
      batchPrintCancel () {
        this.batchPrintOpen = false;
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          name: null,
          labelWidth: null,
          labelHeight: null,
          imageWidth: null,
          imageHeight: null,
          objectWidth: null,
          objectHeight: null,
          status: "0",
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          remark: null,
          delFlag: null
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
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加标签模板";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getLabelTemplate(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改标签模板";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.templateInfo[0].id != null) {
              updateLabelTemplate(this.templateInfo[0]).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.initData();
              });
            } else {
              addLabelTemplate(this.templateInfo[0]).then(response => {
                this.$modal.msgSuccess("新增成功");
                // this.id = response.data.id
                // this.templateInfo[0] = response.datatemplateInfo[0].labelWidth && this.templateInfo[0].labelHeight && this
                this.$emit("submitOK",true)
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
          return delLabelTemplate(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('wms/labelTemplate/export', {
          ...this.queryParams
        }, `labelTemplate_${new Date().getTime()}.xlsx`)
      },
      selectTemplate() {
        this.$refs.trayTabelPop.open = true
      },
      selectMaterial() {
        this.$refs.materialTabelPop.open = true
      },
      addTemplate() {
        this.addOpen = true
      },
      setCurrentRow(row) {
        this.currentRow = row
        this.$refs.materialTabel.materialList = [row]
      },
      trayTabelRefresh() {
        this.$refs.trayTabel.resetTemplate()
      },
      // 点击批量打印
      handleBatchPrint() {
        // this.batchPrintList.push("T20230817000001")
        // this.batchPrintList.push("T20230817000002")
        // this.batchPrintSure = true
        this.resetForm('batchPrintForm')
        if (this.templateInfo[0].labelWidth && this.templateInfo[0].labelHeight && this.templateInfo[0].imageWidth && this.templateInfo[0].imageHeight&& this.templateInfo[0].objectWidth && this.templateInfo[0].objectHeight) {
          this.batchPrintForm.trayCategory = null;
          this.batchPrintForm.startNo = null;
          this.batchPrintForm.endNo = null;
          this.batchPrintOpen = true
        } else {
          this.$modal.msgError("模板信息缺失");
        }
      },
      // 批量打印生成载具信息
      submitBatchPrint() {
        this.$refs["batchPrintForm"].validate(valid => {
          if (valid) {
            this.batchPrintSubmit = true
            this.batchPrintForm.labelTemplateId = this.templateInfo[0].id
            batchPrintList(this.batchPrintForm).then(res => {
              this.batchPrintList = res.data
              if(this.batchPrintList.length > 0){
                this.batchPrintOpen = false
                this.batchPrintSure = true
                this.batchPrintSubmit = false
              }else{
                this.$modal.msgError("未查询到标签数据");
                this.batchPrintSubmit = false
              }
            }).catch(err => {
              this.batchPrintSubmit = false
            })
          }
        });
      },
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
