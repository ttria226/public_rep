<template>
  <div class="app-container" v-loading.fullscreen="loading">
    <el-row :gutter="50">
      <el-col :span="14" class="body-left">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
          <el-form-item label="切换库区" prop="reservoirId">
            <el-select v-model="queryParams.reservoirId" clearable placeholder="请选择库区">
              <el-option v-for="dict in reservoirList" :key="dict.id" :label="dict.name" :value="dict.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="切换排" prop="locationRow" label-width="90px">
            <el-select v-model="queryParams.locationRow" clearable placeholder="请选择排">
              <el-option v-for="dict in lineList" :key="dict.locationRow" :label="dict.locationRowName" :value="dict.locationRow" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">返回</el-button>
          </el-form-item>
        </el-form>

        <div class="show-body">
          <div class="left">
            <div class="text-dom" v-for="(item,index) in directionList" :key="'left-text-'+index">{{ item+"层" }}</div>
          </div>
          <div class="right">
            <div class="out-line" v-for="(item,index) in showList" :key="'row-'+index">
              <template v-for="(info,i) in item">
                <div class="in-block" :style="{ 'background-color': info.backNewColor }" v-if="info" :key="'row-'+index+'-col-'+i" @click="handleShowInfo(info,index,i)"></div>
                <div class="in-block" :style="{ 'background-color': legendList[5].color }" v-else :key="'row-'+index+'-col-'+i+'-1'"></div>
              </template>
            </div>
            <div class="right-down">
              <div class="text-dom" v-for="(item,index) in transverseList" :key="'right-text-'+index">{{ item+'列' }}</div>
            </div>
          </div>
        </div>

        <div class="one-detail" v-if="detailForm">
          <div class="title-info">
            <div class="title">当前选中库位</div>
            <div class="buttons">
              <el-button type="primary" size="mini" v-if="detailForm.status === '1'"  @click="handleFreeze(detailForm.isFreeze === '0' ? '1' : '0')">{{ detailForm.isFreeze === '0' ? '冻结' : '解冻' }}</el-button>
              <el-button type="primary" size="mini"  @click="handleEnableStatus(detailForm.status === '1' ? '0' : '1')">{{ detailForm.status === '1' ? '禁用' : '启用' }}</el-button>
              <el-button type="primary" size="mini" v-if="detailForm.status === '1' && detailForm.isFreeze === '0'"  @click="handleOutDelivery">出库</el-button>
              <el-button type="primary" size="mini" v-if="detailForm.status === '1' && detailForm.isFreeze === '0'" @click="handleMoveDelivery">移库</el-button>
              <el-button type="primary" size="mini" v-if="detailForm.status === '1' && detailForm.isFreeze === '0'" @click="handleStatusChange">修改库位状态</el-button>
            </div>
          </div>
          <div class="detail-info">
            <el-form :model="detailForm" ref="detailForm" size="small" label-width="68px">
              <!-- <el-form-item label="库位编号">
                <div class="form-text">{{ detailForm.code }}</div>
              </el-form-item> -->
              <el-form-item label="库位名称">
                <div class="form-text">{{ detailForm.name}}</div>
              </el-form-item>
              <el-form-item label="载具编号">
                <div class="form-text">{{ detailForm.palletNum}}</div>
              </el-form-item>
              <el-table :data="detailForm.materialList">
                <el-table-column label="物料编码" align="center" prop="code" min-width="180px" :show-overflow-tooltip="true"/>
                <el-table-column label="物料名称" align="center" prop="name" min-width="180px" :show-overflow-tooltip="true"/>
                <el-table-column label="库存数量" align="center" prop="availableCount" min-width="120px" :show-overflow-tooltip="true"/>
              </el-table>
            </el-form>
          </div>
        </div>
      </el-col>
      <el-col :span="10" class="body-right">
        <div class="legend">
          <div class="title">图例</div>
          <el-row :gutter="20">
            <el-col class="legend-item" :span="8" v-for="(item,index) in legendList" :key="item.name+index">
              <div class="block" :style="{ 'background-color': item.color }"></div>
              <div class="text">{{ item.name }}</div>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>

    <!-- 出库对话框 -->
    <el-dialog title="出库" :visible.sync="open" width="55%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="padding-right: 30px">
        <el-row :gutter="20" class="mb8">
          <el-col :span="12">
            <el-form-item label="出库类型" prop="type">
              <el-select style="width: 100%;" v-model="form.type" placeholder="请选择出库类型" class="select-input-form">
                <el-option v-for="dict in dict.type.inout_out_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请部门">
              <el-input v-model="form.deptName" disabled placeholder="请输入申请部门" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请人">
              <el-input v-model="form.createBy" disabled placeholder="请输入申请人" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库位">
              <el-input v-model="form.locationName" disabled placeholder="请输入库位" :maxlength="40" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="tOutDeliveryDetailList" label-width="30px">
          <el-table v-loading="loading" :data="form.tOutDeliveryDetailList">
            <el-table-column label="物料编码" align="center" prop="code" min-width="180"></el-table-column>
            <el-table-column label="物料名称" align="center" prop="name" min-width="180"></el-table-column>
            <el-table-column label="计量单位" align="center" prop="unitName" width="100"></el-table-column>
            <el-table-column label="库存数量" align="center" prop="availableCount" width="200"></el-table-column>
            <el-table-column label="预计出库数量" align="center" prop="predictCount" width="180">
              <template slot-scope="scope">
                <el-form-item label-width="0px" :prop="'tOutDeliveryDetailList.' + scope.$index + '.predictCount'" :rules="detailRules.predictCount">
                  <el-input v-model="scope.row.predictCount" placeholder="请输入预计出库数量" v-intNumber maxlength="6" size="small" @blur="val => handleInputChange(val,'predictCount',scope.$index,scope.row)"></el-input>
                </el-form-item>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改库存状态 -->
    <el-dialog title="修改库位状态详情" :visible.sync="statusVisible" width="40%" append-to-body>
      <el-form ref="statusForm" :model="statusForm" :rules="statusRules" label-width="150px">
        <el-form-item label="库位状态" prop="goodsAllocationStatus">
          <el-select style="width: 100%;" v-model="statusForm.goodsAllocationStatus" placeholder="请选择库位状态" class="select-input-form">
            <el-option v-for="dict in dict.type.wms_goods_allocation_status" :disabled="dict.value === '0'" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitStatusForm">确 定</el-button>
        <el-button @click="cancelStatusForm">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 移库 -->
    <el-dialog title="移库" :visible.sync="moveVisible" width="40%" append-to-body>
      <el-form ref="moveForm" :model="moveForm" :rules="moveRules" label-width="100px">
        <el-form-item label="目标库位" prop="locationId">
          <el-select style="width: 100%;" v-model="moveForm.locationId" placeholder="请选择库位" class="select-input-form">
            <el-option v-for="dict in locationList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMoveForm">确 定</el-button>
        <el-button @click="cancelMoveForm">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getVisualizationLocationDetail, getVisualizationReservoirSelect, getVisualizationLocationSelect, getVisualizationOtherLocation, updateVisualizationLocationStatus, updateVisualizationLocationFreeze, visualizationLocationOutbound } from "@/api/stockLedger/visualization";
import store from "@/store";

import { moveLibrary } from "@/api/wms/stock";
export default {
  name: 'visualizationDetail',
  dicts: ["wms_goods_allocation_status", "inout_out_type", "inout_out_status"],
  data(){
    let validatePredictCount= (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("预计出库数量不能为空"));
        this.$message.error("预计出库数量不能为空")
      } else if (Number(value) <= 0) {
        callback(new Error("预计出库数量应大于0"));
        this.$message.error("预计出库数量应大于0")
      } else if (this.inputIndex !== '' && Number(value) > 0 && Number(value) > Number(this.form.tOutDeliveryDetailList[this.inputIndex].availableCount)) {
        callback(new Error("预计出库数量应小于库存数量"));
        this.$message.error("预计出库数量应小于库存数量")
      } else {
        callback();
      }
    };
    return{
      //加载
      loading: false,
      //传参
      reservoirId: '',
      locationRow: '',
      //查询条件
      queryParams: {
        reservoirId: '',
        locationRow: ''
      },
      //库区list
      reservoirList: [],
      //排list
      lineList: [],
      //图例list
      legendList: [
        {
          name: '空闲',
          color: '#4BD863',
          text: 'goodsAllocationStatus',
          value: '1'
        },
        {
          name: '标记入库',
          color: '#70B603',
          text: 'goodsAllocationStatus',
          value: '4'
        },
        {
          name: '禁用',
          color: '#D90011',
          text: 'isFreeze',
          value: '0'
        },
        {
          name: '占用',
          color: '#0079FE',
          text: 'goodsAllocationStatus',
          value: '2'
        },
        {
          name: '标记出库',
          color: '#81D3F8',
          text: 'goodsAllocationStatus',
          value: '3'
        },
        {
          name: '非库位',
          color: '#333333',
          text: 'noType',
          value: '6'
        },
        {
          name: '当前选中',
          color: '#D7D7D7',
          text: 'choose',
          value: '7'
        },
        {
          name: '冻结',
          color: '#005478',
          text: 'isFreeze',
          value: '1'
        }
      ],
      //纵向list
      directionList:[],
      //横向list
      transverseList:[],
      //展示list
      showList: [],
      //详情表单
      detailForm: null,

      //出库弹窗标识
      open: false,
      form: {},
      rules: {
        type: [{ required: true, message: "请选择出库类型", trigger: "blur" }],
      },
      detailRules: {
        predictCount: [
          { trigger: "blur", validator: validatePredictCount },
        ],
      },

      inputIndex: '',

      //修改库位状态弹窗标识
      statusVisible: false,
      //修改库位状态表单
      statusForm: {
        goodsAllocationStatus: ''
      },
      //修改库位状态规则
      statusRules: {
        status:[
          { required: true, message: "请选择库位状态", trigger: "change" }
        ]
      },

      //移库弹窗标识
      moveVisible: false,
      //移库表单
      moveForm: {
        locationId: ''
      },
      //修改库位状态规则
      moveRules: {
        locationId:[
          { required: true, message: "请选择目标库位", trigger: "change" }
        ]
      },
      //库位信息
      locationList: []
    }
  },
  created(){
    this.reservoirId = this.$route.query.reservoirId
    this.locationRow = this.$route.query.locationRow
    this.queryParams.reservoirId = this.reservoirId
    this.queryParams.locationRow = this.locationRow
    this.getSelectList()
    this.getLocationList()
  },
  methods: {
    /** 初始化下拉 */
    getSelectList(){
      getVisualizationReservoirSelect({}).then(res => {
        res.data.map((item) => {
          item.id = item.id+""
        })
        this.reservoirList = res.data
      })
      this.getLocationSelectList()
    },
    /** 获取库区排 */
    getLocationSelectList(){
      getVisualizationLocationSelect({ reservoirId: this.queryParams.reservoirId }).then(res => {
        res.data.map((item) => {
          item.id = item.id+""
        })
        this.lineList = res.data
      })
    },
    /** 获取库位详情 */
    handleShowInfo(info,index,i){
      let resultList = JSON.parse(JSON.stringify(this.showList))
      let resultForm = {}
      for(let j = 0; j < resultList.length; j++){
        for(let k = 0; k < resultList[j].length; k++){
          if(resultList[j][k] && j === index && k === i){
            resultList[j][k].backNewColor = this.legendList[6].color
            resultForm = JSON.parse(JSON.stringify(resultList[j][k]))
          } else if(resultList[j][k]){
            resultList[j][k].backNewColor = resultList[j][k].backOldColor
          }
        }
      }
      this.showList = resultList
      this.detailForm = resultForm
      this.$forceUpdate()
    },
    /** 获取库位 */
    getLocationList(){
      this.loading = true
      getVisualizationLocationDetail(this.queryParams).then(res => {
        let list = res.data
        let directionList = []
        let transverseList = []
        list.map((item) => {
          directionList.push(item.locationPlies)
          transverseList.push(item.locationColumn)
        })
        let directionNoRepeatList = [...new Set(directionList)];
        let transverseNoRepeatList = [...new Set(transverseList)];
        let directionResultList = []
        let transverseResultList = []
        let directionMaxNum = Math.max(...directionNoRepeatList)
        let transverseMaxNum = Math.max(...transverseNoRepeatList)
        let resultList = new Array(directionMaxNum)
        for(let i = 0; i < resultList.length; i++){
          resultList[i] = new Array(transverseMaxNum)
        }
        for(let l = 0; l < directionMaxNum; l++){
          directionResultList.push(l+1)
        }
        for(let m = 0; m < transverseMaxNum; m++){
          transverseResultList.push(m+1)
        }
        for(let j = 0; j < resultList.length; j++){
          for(let k = 0; k < resultList[j].length; k++){
            let info = list.find((item) => { return item.locationPlies == (j + 1) && item.locationColumn == (k + 1) })
            if(info){
              let backColor = this.legendList[5].color
              if(info.status === '0'){
                backColor = this.legendList[2].color
              } else if(info.status === '1' && info.isFreeze === '1'){
                backColor = this.legendList[7].color
              } else if(info.status === '1' && info.isFreeze === '0'){
                let flagIndex = this.legendList.findIndex((legendInfo) => { return info.goodsAllocationStatus === legendInfo.value && legendInfo.text === 'goodsAllocationStatus' })
                backColor = this.legendList[flagIndex].color
              }
              info.backOldColor = backColor
              info.backNewColor = backColor
              resultList[j][k] = JSON.parse(JSON.stringify(info))
            }
          }
        }
        this.directionList = directionResultList
        this.transverseList = transverseResultList
        this.showList = resultList
        console.log("resultList",resultList)
      }).finally(() => {
        this.loading = false
      })

    },
    /** 获取目标库位下拉 */
    getDialogLocationSelectList(){
      getVisualizationOtherLocation({ locationId: this.detailForm.id }).then(res => {
        this.locationList = res.data
      })
    },
    /** 查询 */
    handleQuery(){
      this.getLocationList()
    },
    /** 返回 */
    resetQuery(){
      const obj = { path: "/stockLedger/visualization" };
      this.$tab.closeOpenPage(obj);
    },
    /** 冻结按钮 */
    handleFreeze(type){
      const ids = this.detailForm.id+"";
      let msg = type =='1' ? '是否确认将此库位冻结？':'是否确认将此库位解冻？';
      const data = {
        locationId: ids,
        type: type
      }
      this.$modal.confirm(msg).then(function() {
        return updateVisualizationLocationFreeze(data);
      }).then(() => {
        this.getLocationList();
        this.detailForm = null
        this.$modal.msgSuccess(isFreeze == "1" ? "冻结成功" : "解冻成功");
      }).catch(() => {});
    },
    /** 禁用/启用按钮 */
    handleEnableStatus(status) {
      const ids = this.detailForm.id;
      let strname = '禁用'
      if(status == '0'){
        strname = '禁用'
      }else{
        strname = '启用'
      }
      const data = {
        id: ids,
        status: status,
      }
      this.$modal.confirm('是否'+strname+'此库区下的库位？').then(function() {
        return updateVisualizationLocationStatus(data);
      }).then(() => {
        this.getLocationList();
        this.detailForm = null
        this.$modal.msgSuccess(strname+"成功");
      }).catch(() => {});
    },
    /** 出库按钮 */
    handleOutDelivery(){
      this.open = true
      this.form.deptId = store.getters.deptId;
      this.form.deptName = store.getters.deptName;
      this.form.createBy = store.getters.name;
      this.form.locationName = this.detailForm.name
      this.form.tOutDeliveryDetailList = [...this.detailForm.materialList]
    },
    /** 移库按钮 */
    handleMoveDelivery(){
      this.getDialogLocationSelectList()
      this.moveVisible = true
    },
    /** 修改库位状态按钮 */
    handleStatusChange(){
      this.statusVisible = true
      this.statusForm = { goodsAllocationStatus: this.detailForm.goodsAllocationStatus }
    },
    //输入框变化事件
    handleInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.form.tOutDeliveryDetailList,index,info)
    },
    /** 出库提交 */
    submitForm(){
      this.inputIndex = ''
      let errorMessage = []
      this.form.tOutDeliveryDetailList.map((item) => {
        if(Number(item.predictCount) > Number(item.availableCount)){
          errorMessage.push(`物料【${(item.name)}】的预计出库数量应小于库存数量`)
        }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          let list = []
          this.form.tOutDeliveryDetailList.map(item => {
            let info = {
              materialId: item.materialId,
              stockId: item.stockId,
              predictCount: item.predictCount
            }
            list.push(info)
          })
          const data = {
            type: this.form.type,
            tOutDeliveryDetailList: list,
          }
          visualizationLocationOutbound(data).then(res => {
            this.getLocationList();
            this.detailForm = null
            this.$modal.msgSuccess("出库成功");
            this.open = false
            this.form = {
              id: null,
              type: null,
              deptId: null,
              deptName: null,
              createBy: null,
              locationName: null,
              tOutDeliveryDetailList: []
            }
          })
        }
      });
    },
    /** 关闭出库 */
    cancel(){
      this.open = false
      this.form = {
        id: null,
        type: null,
        deptId: null,
        deptName: null,
        createBy: null,
        locationName: null,
        tOutDeliveryDetailList: []
      }
    },
    /** 提交库位状态方法 */
    submitStatusForm(){
      this.$refs["statusForm"].validate(valid => {
        if (valid) {
          const data = {
            id: this.detailForm.id,
            goodsAllocationStatus: this.statusForm.goodsAllocationStatus,
          }
          updateVisualizationLocationStatus(data).then(res => {
            this.getLocationList();
            this.detailForm = null
            this.$modal.msgSuccess("修改库位状态成功");
            this.statusVisible = false
            this.statusForm = {
              goodsAllocationStatus: ''
            }
          })
        }
      });
    },
    /** 关闭提交库位状态 */
    cancelStatusForm(){
      this.statusVisible = false
      this.statusForm = {
        goodsAllocationStatus: ''
      }
    },
    /** 移库方法 */
    submitMoveForm(){
      this.$refs["moveForm"].validate(valid => {
        if (valid) {
          let stockIds = []
          this.detailForm.materialList.map((item) => {
            stockIds.push(item.stockId)
          })
          // let params = {
          //   stockId: stockIds[0],
          //   locationInId: this.moveForm.locationId
          // }
          const data = new FormData()
          data.append("stockId",stockIds[0])
          data.append("locationInId",this.moveForm.locationId)
          moveLibrary(data).then(res => {
            this.getLocationList();
            this.detailForm = null
            this.$modal.msgSuccess("移库成功");
            this.moveVisible = false;
            this.moveForm = {
              locationId: ''
            }
          })
        }
      });
    },
    /** 关闭移库 */
    cancelMoveForm(){
      this.moveVisible = false
      this.moveForm = {
        locationId: ''
      }
    },
  }
}
</script>
<style lang="scss" scoped>
.title-info{
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  .title{
    font-size: 18px;
    font-weight: bold;
  }
}
.detail-info{
  ::v-deep .el-form-item{
    margin-bottom: 4px;
  }
}
.show-body{
  display: flex;
  align-items: flex-start;
  .text-dom{
    width: 30px;
    height: 30px;
    font-size: 16px;
    font-weight: bold;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .left{
    .text-dom{
      font-size: 18px;
      margin-bottom: 5px;
    }
  }
  .right{
    min-height: 200px;
    margin-left: 20px;
    .out-line{
      display: flex;
      .in-block{
        width: 30px;
        height: 30px;
        margin-right: 5px;
        margin-bottom: 5px;
      }
    }
    .right-down{
      margin-top: 10px;
      display: flex;
      .text-dom{
        margin-right: 5px;
      }
    }
  }
}
.legend{
  padding: 10px 20px 0 20px;
  border: 1px solid #222222;
  .title{
    margin-bottom: 20px;
    font-size: 16px;
    font-weight: bold;
  }
  .legend-item{
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    .block{
      width: 20px;
      height: 20px;
      margin-right: 10px;
    }
    &.noMargin{
      margin-bottom: 0;
    }
  }
}
</style>
