<template>
  <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
    <!-- <el-form ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="100px" style="padding-right: 30px">
      <el-form-item label="库位" prop="locationName">
        <el-input v-model="queryParams.locationName" placeholder="请输入库位" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <div class="dialog-title"><span>物料信息</span></div>
      </el-col>
    </el-row>
    <el-table ref="trayTable" :data="materialList" highlight-current-row @current-change="handleCurrentChange" row-key="materialCode">
      <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
      <el-table-column label="物料名称" align="center" prop="materialName" min-width="200"></el-table-column>
      <el-table-column label="计量单位" align="code" prop="materialUnit" min-width="100"></el-table-column>
      <el-table-column label="批次号" align="code" prop="batchCode" min-width="180"></el-table-column>
      <el-table-column label="预计拣货数量" align="center" prop="predictCount" min-width="130" />
    </el-table>
    <el-row :gutter="10" class="mb8" v-if="current">
      <el-col :span="1.5">
        <div class="dialog-title"><span>已分配</span><span>(预计拣货数量：{{ materialAllNum }})</span></div>
      </el-col>
    </el-row>
    <el-row :gutter="10" class="mb8" v-if="current && (detailType == '1' || detailType == '2')">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addDetailed()" >新增行</el-button>
      </el-col>
    </el-row>
    <el-form ref="form" :model="form" label-width="100px" v-if="form.taskDetailList && form.taskDetailList.length > 0">
      <el-form-item prop="taskDetailList" label-width="0px">
        <el-table border :data="form.taskDetailList" :span-method="objectSpanMethod">
          <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180"></el-table-column>
          <el-table-column label="物料名称" align="center" prop="materialName"  min-width="180"></el-table-column>
          <el-table-column label="库位" align="center" prop="locationName"  min-width="180" v-if="detailType == '5' || detailType == '6'" />
          <el-table-column label="库位" align="center" prop="locationName"  min-width="180" v-if="detailType == '1' || detailType == '2'">
            <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskDetailList.' + scope.$index + '.locationName'" :rules="detailRules.locationName">
                <el-input v-model="scope.row.locationName" placeholder="请选择库位" size="small" @focus="materialStockComOpen(scope.$index)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <!-- <el-table-column label="载具" align="center" prop="trayId"  min-width="120">
            <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskDetailList.' + scope.$index + '.trayId'">
                <el-select v-model="scope.row.trayId" class="select-input-form" @change="val => handleTrayChange(val,scope.row)">
                  <el-option v-for="item in trayList" :key="item.id" :label="item.code" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column> -->
          <el-table-column label="载具编号" align="center" prop="trayCode"  min-width="120" />
          <el-table-column label="库存数量" align="center" prop="availableCount" width="120"></el-table-column>
          <el-table-column label="拣货数量" align="center" prop="predictCount" width="120" v-if="detailType == '5'" />
          <el-table-column label="拣货数量" align="center" prop="receiveCount" width="120" v-if="detailType == '6'" />
          <el-table-column label="拣货数量" align="center" prop="predictCount" width="120" v-if="detailType == '2'">
            <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskDetailList.' + scope.$index + '.predictCount'" :rules="detailRules.predictCount">
                <el-input v-model="scope.row.predictCount" placeholder="请输入拣货数量" v-intNumber maxlength="6" size="small" @blur="val => handleInputChange(val,'predictCount',scope.$index,scope.row)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="拣货数量" align="center" prop="receiveCount" width="120" v-if="detailType == '1'">
            <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskDetailList.' + scope.$index + '.receiveCount'" :rules="detailRules.receiveCount">
                <el-input v-model="scope.row.receiveCount" placeholder="请输入拣货数量" v-intNumber maxlength="6" size="small" @blur="val => handleInputChange(val,'receiveCount',scope.$index,scope.row)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="批次号" align="center" prop="batchCode" min-width="130"></el-table-column>
          <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" v-if="detailType == '1' || detailType == '2'">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteDetailed(scope.$index)">删除</el-button>
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
</template>
<script>
import { getInDeliveryTaskLocationList } from "@/api/inoutDelivery/inDelivery";
import { getOutDeliveryAutoTraylist, getOutDeliveryFloorDisplayAutoTraylist, executeOutDeliveryComplete, executeOutFloorDisplayDeliveryComplete } from "@/api/inoutDelivery/outDelivery";

export default {
  name: 'ArriveTaskOutDelivery',
  dicts: ['wms_t_tray_category'],
  data(){
    let validatePredictCount= (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("拣货数量不能为空"));
        this.$message.error("拣货数量不能为空")
      } else if (Number(value) <= 0) {
        callback(new Error("拣货数量应大于0"));
        this.$message.error("拣货数量应大于0")
      } else {
        callback();
      }
    };
    let validateLocationName= (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("库位信息不能为空"));
        this.$message.error("库位信息不能为空")
      } else {
        callback();
      }
    };
    return {
      // 遮罩层
      loading: true,
      //选中数组数据
      currentSelection: [],
      //选中行
      current: null,
      //选中行下标
      currentIndex: 0,
      // 总条数
      total: 0,
      // 载具表格数据
      vehicleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //推荐载具类型
      trayType: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        locationName: null,
        trayCategory: null
      },
      //物料信息
      materialList: [],
      //物料拣货总数量
      materialAllNum: 0,
      //出库id
      detailId: null,
      //类型
      detailType: null,
      //物料库存表下标
      detailIndex: null,
      // 表单参数
      form: {
        taskDetailList: []
      },
      //校验规则
      detailRules: {
        locationName: [
          { trigger: "change", validator: validateLocationName },
        ],
        receiveCount: [
          { trigger: "blur", validator: validatePredictCount },
        ],
        predictCount: [
          { trigger: "blur", validator: validatePredictCount },
        ],
      },

      spanArr:[], // 需要合并的行数
      pos:0,// 索引
    }
  },
  watch: {
    open(val){
      if(val){
        this.reset()
      }
    }
  },
  methods: {
    /** 获取载具列表 */
    getList(flag){
      this.loading = true;
      this.queryParams.goodsAllocationStatus = '1'
      getInDeliveryTaskLocationList(this.queryParams).then((response) => {
        this.vehicleList = response.data;
        this.total = response.data.length;
        this.loading = false;
      });
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
    // 打开选择物料弹窗
    materialStockComOpen(index) {
      this.detailIndex = index
      this.$emit("materialStockGet",{ materialId: this.current.materialId, stockType: this.detailType == '1' ? '1' : (this.detailType == '2' ? '0' : '') })
    },
    // 选中行
    handleCurrentChange(row) {
      this.current = row
      let index = this.materialList.findIndex((item) => { return item.materialId == row.materialId })
      this.currentIndex = index
      let taskDetailList = row.taskDetailList ? JSON.parse(JSON.stringify(row.taskDetailList)) : []
      this.materialAllNum = row.predictCount
      if(this.detailType == '1' || this.detailType == '2'){
        this.getSpanArr(taskDetailList)
        this.$nextTick(() => {
          this.form.taskDetailList = taskDetailList
          this.$forceUpdate()
        })
      } else if(this.detailType == '5' && taskDetailList.length === 0){
        getOutDeliveryAutoTraylist({ outDeliveryId: row.outDeliveryId, materialId: row.materialId }).then((response) => {
          if(response.data && response.data.length > 0){
            response.data.map((item) => {
              let info = {
                trayCode: item.code ? item.code : "",
                trayId: item.id ? item.id : "",
                stockId: item.stockid ? item.stockid : "",
                locationId: item.locationId ? item.locationId : "",
                locationName: item.locationname ? item.locationname : "",
                materialCode: this.current.materialCode,
                materialId: this.current.materialId,
                materialName: this.current.materialName,
                unitName: this.current.unitName,
                batchCode: this.current.batchCode,
                availableCount: item.availableCount ? item.availableCount : 0,
                predictCount: item.predictCount ? item.predictCount : 0,
              }
              taskDetailList.push(info)
            })
          }
          this.getSpanArr(taskDetailList)
          this.$nextTick(() => {
            this.form.taskDetailList = taskDetailList
            this.materialList[this.currentIndex].taskDetailList = taskDetailList
            this.$forceUpdate()
          })
        });
      } else if(this.detailType == '6' && taskDetailList.length === 0){
        getOutDeliveryFloorDisplayAutoTraylist({ outDeliveryId: row.outDeliveryId, materialId: row.materialId }).then((response) => {
          console.log(response.data)
          if(response.data && response.data.dataList.length > 0){
            response.data.dataList.map((item) => {
              let info = {
                trayCode: item.code ? item.code : "",
                trayId: item.id ? item.id : "",
                stockId: item.stockid ? item.stockid : "",
                locationId: item.locationId ? item.locationId : "",
                locationName: item.locationname ? item.locationname : "",
                materialCode: this.current.materialCode,
                materialId: this.current.materialId,
                materialName: this.current.materialName,
                unitName: this.current.unitName,
                batchCode: this.current.batchCode,
                availableCount: item.availableCount ? item.availableCount : 0,
                receiveCount: item.receiveCount ? item.receiveCount : 0,
              }
              taskDetailList.push(info)
            })
          }
          this.getSpanArr(taskDetailList)
          this.$nextTick(() => {
            this.form.taskDetailList = taskDetailList
            this.materialList[this.currentIndex].taskDetailList = taskDetailList
            this.$forceUpdate()
          })
        });
      } else if((this.detailType == '5' || this.detailType == '6') && taskDetailList.length > 0){
        this.getSpanArr(taskDetailList)
        this.$nextTick(() => {
          this.form.taskDetailList = taskDetailList
          this.materialList[this.currentIndex].taskDetailList = taskDetailList
          this.$forceUpdate()
        })
      }
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.currentSelection = JSON.parse(JSON.stringify(selection))
      let list = []
      for(let i = 0; i < this.currentSelection.length; i++){
        for(let j = 0; j < this.materialList.length; j++){
          let info = {
            advanceRegistrationId: this.materialList[j].id,
            locationName: this.currentSelection[i].name,
            locationId: this.currentSelection[i].id,
            trayId: this.currentSelection[i].trayId ? this.currentSelection[i].trayId : null,
            materialCode: this.materialList[j].materialCode,
            materialId: this.materialList[j].materialId,
            materialName: this.materialList[j].materialName,
            batchCode: this.materialList[j].batchCode,
            predictCount: this.materialList[j].predictCount,
            registrationCount: this.materialList[j].registrationCount,
            putawayCount: this.materialList[j].putawayCount,
            reActualCount: this.detailType == '1' ? (Number(this.materialList[j].registrationCount) - Number(this.materialList[j].putawayCount)) : (Number(this.materialList[j].predictCount) - Number(this.materialList[j].putawayCount)),
          }
          list.push(info)
        }
      }
      this.getSpanArr(list)
      this.$nextTick(() => {
        this.form.taskDetailList = list
        this.$forceUpdate()
      })
    },
    //获取合并索引数组
    getSpanArr(data) {
      this.spanArr = []
      this.pos = 0
      // 遍历数据
      for (let i = 0; i < data.length; i++) {
        // 如果是第一个数据，就将列表spanArr添加一个1，表示暂时只有一个名字相同的、且将索引pos赋值为0
        if (i === 0) {
          this.spanArr.push(1);
          this.pos = 0
        } else {
          // 判断当前元素与上一个元素是否相同
          if (data[i].materialCode === data[i - 1].materialCode) {
            // 如果相同就将索引为 pos 的值加一
            this.spanArr[this.pos] += 1;
            // 且将数组添加 0
            this.spanArr.push(0);
          } else {
            // 如果元素不同了，就可以通过索引为 pos 的值知晓应该需要合并的行数
            // 同时，我们再次添加一个值1，表示重新开始判断重复姓名的次数
            this.spanArr.push(1);
            // 同时 索引加一
            this.pos = i;
          }
        }
      }
      console.log("索引数组：")
      console.log(this.spanArr)
    },
    // 处理合并
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0 || columnIndex === 1 || columnIndex === 6) {
        /*
        将需要合并的行数赋值给 _row，注意这里由上一个方法的输出可以知道，这里的值可以是 3或者0
        当为 3 时，表示将当下的第 rowIndex+1 行与第 columnIndex+1 列所指向的单元格向下合并 _row 格
        当为 0 时，表示将当下的第 rowIndex+1 行与第 columnIndex+1 列所指向的单元格隐藏
        */
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col
        }
      }
    },
    //输入框变化事件
    handleInputChange(val,type,index,row){
      let info = JSON.parse(JSON.stringify(row))
      info[type] = val.target.value
      this.inputIndex = index
      this.$set(this.form.taskDetailList,index,info)
      this.$nextTick(() => {
        let list = JSON.parse(JSON.stringify(this.form.taskDetailList))
        this.materialList[this.currentIndex].taskDetailList = list
        console.log(this.materialList)
        this.$forceUpdate()
      })
    },
    /** 新增明细操作 */
    addDetailed() {
      if(!this.current){
        this.$message.warning('请选择要添加库存的物料')
        return
      }
      let list = JSON.parse(JSON.stringify(this.form.taskDetailList))
      let info = {
        // outDeliveryDetailId: this.materialList[j].id,
        trayCode: null,
        trayId: null,
        stockId: null,
        locationId: null,
        locationName: null,
        materialCode: this.current.materialCode,
        materialId: this.current.materialId,
        materialName: this.current.materialName,
        unitName: this.current.unitName,
        batchCode: this.current.batchCode,
        // num: this.materialList[j].num,
        availableCount: null,
      }
      if(this.detailType == '1'){
        info.receiveCount = null
      } else if(this.detailType == '2'){
        info.predictCount = null
      }
      list.push(info)
      this.getSpanArr(list)
      this.$nextTick(() => {
        this.materialList[this.currentIndex].taskDetailList = list
        this.form.taskDetailList = list
        this.$forceUpdate()
      })
    },
    /** 删除明细操作 */
    deleteDetailed(index) {
      this.$modal.confirm("是否确认删除此明细").then(() => {
        let taskDetailList = JSON.parse(JSON.stringify(this.form.taskDetailList))
        taskDetailList.splice(index,1)
        this.getSpanArr(taskDetailList)
        this.$nextTick(() => {
          this.form.taskDetailList = taskDetailList
          this.materialList[this.currentIndex].taskDetailList = taskDetailList
          this.$forceUpdate()
        })
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 提交按钮 */
    submitForm() {
      this.$message.closeAll()
      let errorMessageList = []
      this.materialList.map((item) => {
        if(item.taskDetailList){
          item.taskDetailList.map((detailItem,index) => {
            if(detailItem.stockId === null || detailItem.stockId === ''){
              errorMessageList.push(`物料【${item.materialName}】的第${(index+1)}行的库位信息不能为空`)
            } else if(!detailItem.receiveCount && this.detailType == '1'){
              errorMessageList.push(`物料【${item.materialName}】的库位【${detailItem.locationName}】的拣货数量不能为空或者0`)
            } else if(!detailItem.predictCount && this.detailType == '2'){
              errorMessageList.push(`物料【${item.materialName}】的库位【${detailItem.locationName}】的拣货数量不能为空或者0`)
            }
          })
        } else {
          errorMessageList.push(`请添加物料【${item.materialName}】的出库库存明细`)
        }
      })
      if(errorMessageList.length > 0){
        this.$message.error(errorMessageList.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let list = []
          this.materialList.map((item) => {
            let tTaskOutDetailListVOS = []
            this.form.taskDetailList.map((detailItem) => {
              let info = {
                stockId: detailItem.stockId,
              }
              if(this.detailType == '1' || this.detailType == '6'){
                info.receiveCount = detailItem.receiveCount
              } else if(this.detailType == '2' || this.detailType == '5'){
                info.trayId = detailItem.trayId
                info.locationId = detailItem.locationId
                info.predictCount = detailItem.predictCount
              }
              tTaskOutDetailListVOS.push(info)
            })
            let params = {
              outDeliveryDetailId: item.id,
              tTaskOutDetailListVOS,
            }
            list.push(params)
          })
          if(this.detailType == '2' || this.detailType == '5'){
            executeOutDeliveryComplete(list).then((response) => {
              this.$modal.msgSuccess("出库分配成功");
              this.open = false
              this.$emit("setArriveTask",true)
            });
          } else if(this.detailType == '1' || this.detailType == '6'){
            executeOutFloorDisplayDeliveryComplete(list).then((response) => {
              this.$modal.msgSuccess("地堆拣货成功");
              this.open = false
              this.$emit("setArriveTask",true)
            });
          }
        }
      });
    },
    //重置表单
    reset(){
      this.current = null
      this.currentIndex = 0
      this.materialAllNum = 0
      this.form = {
        taskDetailList: []
      }
    },
    /** 取消按钮 */
    cancel(){
      this.open = false
      this.form = {
        taskDetailList: []
      }
      this.current = null
      this.currentIndex = 0
      this.materialAllNum = 0
      this.materialList = []
    }
  }
}
</script>
