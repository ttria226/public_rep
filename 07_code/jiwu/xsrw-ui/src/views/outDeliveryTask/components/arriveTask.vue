<template>
  <el-dialog :title="(type == '2' || type == '4') ? '波次分配': '执行出库'" :visible.sync="open" width="50%" append-to-body>
    <el-form ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="100px" style="padding-right: 30px" v-if="type == '1' || type == '2'">
      <el-form-item label="载具编号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入载具编号" :maxlength="40" />
      </el-form-item>
      <el-form-item label="载具类型" prop="trayCategory">
        <el-select v-model="queryParams.trayCategory" placeholder="请选择载具类型" class="select-input-form">
          <el-option v-for="dict in dict.type.wms_t_tray_category" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="trayTable" v-loading="loading" :data="vehicleList" @selection-change="handleSelectionChange" v-if="type == '1' || type == '2'">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="库位" align="center" prop="locationname" v-if="type == '2'"></el-table-column>
      <el-table-column label="库区" align="center" prop="reservoirname" v-if="type == '2'"></el-table-column>
      <el-table-column label="载具编号" align="center" prop="code" width="250"></el-table-column>
      <el-table-column label="载具类型" align="center" prop="trayCategory">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_tray_category" :value="scope.row.trayCategory" />
        </template>
      </el-table-column>
      <el-table-column label="库存数量" align="center" prop="availableCount"></el-table-column>
      <el-table-column label="库位" align="center" prop="locationname" v-if="type == '1'"></el-table-column>
      <el-table-column label="库区" align="center" prop="reservoirname" v-if="type == '1'"></el-table-column>
    </el-table>
    <el-row :gutter="10" class="mb8" :style="{ 'margin-top': (type == '3' || type == '4') ? '-30px' : '0px' }">
      <el-col :span="1.5">
        <div class="dialog-title"><span>{{ (type == "2" || type == "4") ? "已分配" : "已选载具" }}</span><span>(预计拣货数量：{{ materialList[0] ? materialList[0].num : 0 }})</span></div>
      </el-col>
    </el-row>
    <el-form ref="form" :model="form" label-width="0px" style="padding-right: 30px">
      <el-form-item prop="taskDetailList">
        <el-table v-loading="loading" :data="form.taskDetailList" ><!--:span-method="objectSpanMethod"-->
          <el-table-column label="库位" align="center" prop="locationName" v-if="type == '2' || type == '4'" min-width="130"></el-table-column>
          <el-table-column label="载具编号" align="center" prop="trayCode" min-width="150" v-if="type == '1' || type == '3'"></el-table-column>
          <el-table-column label="物料编码" align="center" prop="materialCode" min-width="130"></el-table-column>
          <el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
          <el-table-column label="库存数量" align="center" prop="availableCount" width="120"></el-table-column>
          <el-table-column label="拣货数量" align="center" prop="predictCount" width="120" v-if="type == '3' || type == '4'"></el-table-column>
          <el-table-column label="拣货数量" align="center" prop="predictCount" width="120" v-if="type == '1' || type == '2'">
            <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskDetailList.' + scope.$index + '.predictCount'" :rules="detailRules.predictCount">
                <el-input-number :precision="3" :step="0.001" v-model="scope.row.predictCount"  placeholder="请输入拣货数量"  maxlength="6" size="small" @blur="val => handleInputChange(val,'predictCount',scope.$index,scope.row)"></el-input-number>
              </el-form-item>
            </template>
          </el-table-column>
          <!-- <el-table-column label="计量单位" align="center" prop="unitName" min-width="120"></el-table-column> -->
          <el-table-column label="批次号" align="center" prop="batchCode" min-width="130"></el-table-column>
          <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" v-if="type == '1' || type == '2'">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteDetailed(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">{{ type == '2' || type == '4' ? '分配': '执行出库' }}</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { executeOutDelivery, executeMergeOutDelivery, getOutDeliveryTraylist, getOutDeliveryAutoTraylist, getMergeOutDeliveryAutoTraylist } from "@/api/inoutDelivery/outDelivery";

export default {
  name: 'outDeliveryTaskArriveTask',
  dicts: ["wms_t_tray_category"],
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
    return {
      // 遮罩层
      loading: true,
      //选中数组数据
      currentSelection: [],
      // 总条数
      total: 0,
      // 载具表格数据
      vehicleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //执行出库类型
      type: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        trayCode: null,
        trayType: null,
      },
      //物料信息
      materialList: [],
      // 表单参数
      form: {
        taskDetailList: []
      },
      detailRules: {
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
        if(this.type == '1' || this.type == '2'){
          this.getList(true)
        } else if(this.type == '3'){
          this.getAutoList()
        } else if(this.type == '4'){
          this.getMergeAutoList()
        }
      }
    }
  },
  methods: {
    /** 获取载具列表 */
    getList(flag){
      this.loading = true;
      getOutDeliveryTraylist({ ...this.queryParams, id: this.materialList[0].materialId, batchCode: this.materialList[0].batchCode}).then((response) => {
        this.vehicleList = response.rows;
        this.total = response.total;
        this.loading = false;
        if(flag && response.rows.length > 0){
          this.$nextTick(() => {
            this.$refs.trayTable.toggleRowSelection(response.rows[0],true)
          })
        }
      });
    },
    /** 获取自动分配载具列表 */
    getAutoList(){
      this.loading = true;
      getOutDeliveryAutoTraylist({ outDeliveryId: this.materialList[0].outDeliveryId, materialId: this.materialList[0].materialId }).then((response) => {
        let list = []
        if(response.data && response.data.length > 0){
          response.data.map((item) => {
            let info = {
              outDeliveryDetailId: this.materialList[0].id,
              trayCode: item.code ? item.code : "",
              trayId: item.id ? item.id : "",
              stockId: item.stockid ? item.stockid : "",
              locationId: item.locationId ? item.locationId : "",
              locationName: item.locationname ? item.locationname : "",
              materialCode: this.materialList[0].materialCode,
              materialId: this.materialList[0].materialId,
              materialName: this.materialList[0].materialName,
              unitName: this.materialList[0].unitName,
              batchCode: item.batchCode ? item.batchCode : "",
              availableCount: item.availableCount ? item.availableCount : 0,
              predictCount: item.predictCount ? item.predictCount : 0,
            }
            list.push(info)
          })
        }
        this.getSpanArr(list)
        this.$nextTick(() => {
          this.form.taskDetailList = list
          this.$forceUpdate()
        })
        this.loading = false;
      });
    },
    /** 获取波次分配自动分配载具列表 */
    getMergeAutoList(){
      this.loading = true;
      getMergeOutDeliveryAutoTraylist({ mergeDeliveryId: this.materialList[0].mergeDeliveryId, materialId: this.materialList[0].materialId }).then((response) => {
        let list = []
        if(response.data && response.data.length > 0){
          response.data.map((item) => {
            let info = {
              outDeliveryDetailId: this.materialList[0].id,
              trayCode: item.code ? item.code : "",
              trayId: item.id ? item.id : "",
              stockId: item.stockid ? item.stockid : "",
              locationId: item.locationId ? item.locationId : "",
              locationName: item.locationname ? item.locationname : "",
              materialCode: this.materialList[0].materialCode,
              materialId: this.materialList[0].materialId,
              materialName: this.materialList[0].materialName,
              unitName: this.materialList[0].unitName,
              batchCode: item.batchCode ? item.batchCode : "",
              availableCount: item.availableCount ? item.availableCount : 0,
              predictCount: item.predictCount ? item.predictCount : 0,
            }
            list.push(info)
          })
        }
        this.getSpanArr(list)
        this.$nextTick(() => {
          this.form.taskDetailList = list
          this.$forceUpdate()
        })
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.currentSelection = JSON.parse(JSON.stringify(selection))
      let list = []
      for(let i = 0; i < this.currentSelection.length; i++){
        for(let j = 0; j < this.materialList.length; j++){

          let info = {
            outDeliveryDetailId: this.materialList[j].id,
            trayCode: this.currentSelection[i].code,
            trayId: this.currentSelection[i].id,
            stockId: this.currentSelection[i].stockid,
            locationId: this.currentSelection[i].locationId,
            locationName: this.currentSelection[i].locationname,
            materialCode: this.materialList[j].materialCode,
            materialId: this.materialList[j].materialId,
            materialName: this.materialList[j].materialName,
            unitName: this.materialList[j].unitName,
            batchCode: this.currentSelection[i].batchCode,
            // num: this.materialList[j].num,
            availableCount: this.currentSelection[i].availableCount,
            predictCount: 0
          }
          list.push(info)
        }
      }
      let leastNum = this.materialList[0] ? this.materialList[0].num : 0
      list.map((item) => {
        if(leastNum > 0){
          if(leastNum <= item.availableCount){
            item.predictCount = leastNum
            leastNum = 0
          } else {
            item.predictCount = item.availableCount
            leastNum = item.availableCount
          }
        }
      })
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
          if (data[i].trayCode === data[i - 1].trayCode && (this.type == '1' || this.type == '3') || (data[i].locationId === data[i - 1].locationId && (this.type == '2' || this.type == '4'))) {
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
      if (columnIndex === 0) {
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
    },
    /** 删除明细操作 */
    deleteDetailed(index) {
      this.$modal.confirm("是否确认删除此明细").then(() => {
        let taskDetailList = JSON.parse(JSON.stringify(this.form.taskDetailList))
        taskDetailList.splice(index,1)
        this.getSpanArr(taskDetailList)
        this.$nextTick(() => {
          this.form.taskDetailList = taskDetailList
        })
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 提交按钮 */
    submitForm() {
      this.$message.closeAll()
      if(this.form.taskDetailList.length == 0){
        this.$message.warning("请选择载具！")
        return false
      }
      let errorMessage = []
      this.form.taskDetailList.map((item) => {
        if(!item.predictCount){
          errorMessage.push("物料【"+item.materialCode+"】在载具【"+item.trayCode+"】上的拣货数量不能为空或者0")
        }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let tTaskOutDetailListVOS = []
          this.form.taskDetailList.map((item) => {
            let info = {
              stockId: item.stockId,
              trayId: item.trayId,
              locationId: item.locationId,
              predictCount: item.predictCount,
            }
            tTaskOutDetailListVOS.push(info)
          })
          let params = {
            outDeliveryDetailId: this.materialList[0].id,
            tTaskOutDetailListVOS,
          }
          if(this.type == '2' || this.type == '4'){
            executeMergeOutDelivery(params).then((response) => {
              this.$modal.msgSuccess("分配成功");
              this.open = false
              this.form = {
                taskDetailList: []
              }
              this.type = null
              this.$emit("setArriveTask",true)
            });
          } else {
            this.$modal.confirm("提交后将直接调用设备，是否确认执行？").then(() => {
              return executeOutDelivery(params);
            }).then(() => {
              this.$modal.msgSuccess("执行出库成功");
              this.open = false
              this.form = {
                taskDetailList: []
              }
              this.type = null
              this.$emit("setArriveTask",true)
            }).catch(() => { });
          }
        }
      });
    },
    /** 取消按钮 */
    cancel(){
      this.open = false
      this.form = {
        taskDetailList: []
      }
      this.type = null
    }
  }
}
</script>
