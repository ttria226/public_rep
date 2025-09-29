<template>
  <el-dialog title="上架" :visible.sync="open" width="60%" append-to-body>
    <el-form ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="100px" style="padding-right: 30px">
      <el-form-item label="库位" prop="locationName">
        <el-input v-model="queryParams.locationName" placeholder="请输入库位" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="库区" prop="reservoirId">
        <el-select v-model="queryParams.reservoirId" placeholder="请选择库区" class="select-input-form">
          <el-option v-for="item in reservoirList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="trayTable" v-loading="loading" :data="vehicleList" @selection-change="handleSelectionChange" height="350">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="库位" align="center" prop="name" min-width="200"></el-table-column>
      <el-table-column label="库区" align="center" prop="reservoirName" min-width="200"></el-table-column>
      <el-table-column label="载具编号" align="code" prop="palletNum" min-width="120"></el-table-column>
      <el-table-column label="状态" align="center" prop="goodsAllocationStatus" width="210">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_goods_allocation_status" :value="scope.row.goodsAllocationStatus" />
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="pageNum" :limit.sync="pageSize" @pagination="pagination" />

    <el-row :gutter="10" class="mb8" v-if="form.taskInList.length > 0">
      <el-col :span="1.5">
        <div class="dialog-title">已选载具</div>
      </el-col>
    </el-row>
    <el-form ref="form" :model="form" label-width="100px" v-if="form.taskInList.length > 0" >
      <el-form-item prop="taskInList" label-width="0px">
        <el-table v-loading="loading" border :data="form.taskInList" :span-method="objectSpanMethod">
          <el-table-column label="库位" align="center" prop="locationName"  min-width="200"></el-table-column>
          <el-table-column label="载具" align="center" prop="trayId"  min-width="120">
            <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskInList.' + scope.$index + '.trayId'">
                <el-select v-model="scope.row.trayId" filterable class="select-input-form" @change="val => handleTrayChange(val,scope.row)">
                  <el-option v-for="item in trayList" :key="item.id" :label="item.code" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="物料编码" align="center" prop="materialCode" min-width="200"></el-table-column>
          <el-table-column label="物料名称" align="center" prop="materialName"  min-width="200"></el-table-column>
          <el-table-column label="预计上架数量" align="center" prop="registrationCount" v-if="detailType == '1'" min-width="120"></el-table-column>
          <el-table-column label="预计上架数量" align="center" prop="predictCount" v-if="detailType == '2'" min-width="120"></el-table-column>
          <el-table-column label="已上架数量" align="center" prop="putawayCount"  min-width="100"></el-table-column>
          <el-table-column label="上架数量" align="center" prop="reActualCount" min-width="120">
            <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskInList.' + scope.$index + '.reActualCount'" :rules="detailRules.reActualCount">
                <el-input v-model="scope.row.reActualCount" placeholder="请输入上架数量" v-intNumber maxlength="6" size="small" @blur="val => handleInputChange(val,'reActualCount',scope.$index,scope.row)"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="rfid" align="center" prop="rfidString" min-width="180" ></el-table-column>
          <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" min-width="280">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteDetailed(scope.$index)">删除</el-button>
              <el-button size="mini" type="text" icon="el-icon-setting" @click="handleChooseRfid(scope.row)">选择物料详细</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.rfidString" @click="handleRfidDelete(scope.row,scope.$index)">清空物料详细</el-button>
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
import { putawayInDeliveryTaskComplete, putawayInDeliveryTaskFast, getInPageLocationList, getInDeliveryTaskTrayList } from "@/api/inoutDelivery/inDelivery";

// import { listTray } from "@/api/wms/Tray";
import { takeOutTray, recycleTray } from "@/api/wms/Tray";
import { listReservoir } from "@/api/wms/reservoir";
export default {
  name: 'ArriveTask',
  dicts: ['wms_goods_allocation_status'],
  data(){
    let validateReActualCount= (rule, value, callback) => {
      if (value === null || value === '') {
        callback(new Error("上架数量不能为空"));
        this.$message.error("上架数量不能为空")
      } else if (Number(value) < 0) {
        callback(new Error("上架数量应大于等于0"));
        this.$message.error("上架数量应大于等于0")
      } else if (this.inputIndex !== '' && Number(value) > 0 && Number(value) > (Number(this.form.taskInList[this.inputIndex].detectionCount) - Number(this.form.taskInList[this.inputIndex].putawayCount))) {
        callback(new Error("上架数量应小于预计上架数量"));
        this.$message.error("上架数量应小于预计上架数量")
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
      //推荐载具类型
      trayType: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        locationName: null,
        reservoirId: null,
        goodsAllocationStatus: null
      },
      pageNum: 1,
      pageSize: 10,
      //物料信息
      materialList: [],
      //入库id
      detailId: null,
      //类型
      detailType: null,
      // 表单参数
      form: {
        taskInList: []
      },
      detailRules: {
        reActualCount: [
          { trigger: "blur", validator: validateReActualCount },
        ],
        trayId: [
          { required: true, message: "请选择载具", trigger: "change" },
        ],
      },

      spanArr:[], // 需要合并的行数
      pos:0,// 索引

      trayList: [], //载具下拉list
      newTrayList:[], //载具下拉new
      newTrayList2:[], //载具下拉new
      reservoirList: [], //库区下拉list
    }
  },
  watch: {
    open(val){
      if(val){
        this.getList(true)
        this.getTrayList()
        this.getReservoirList()
      }
    }
  },
  methods: {
    /** 获取载具列表 */
    getList(flag){
      this.loading = true;
      this.queryParams.goodsAllocationStatus = '1'
      getInPageLocationList(this.queryParams).then((response) => {
        this.vehicleList = response.rows;
        this.total = response.total;
        this.loading = false;
        if(flag && response.data && response.data.length > 0){
          this.$nextTick(() => {
            this.$refs.trayTable.toggleRowSelection(response.data[0],true)
          })
        }
      });
    },
    /** 获取载具下拉 */
    getTrayList(){
      getInDeliveryTaskTrayList({ notStatus: 2 }).then((response) => {
        this.newTrayList = response.data;
        for(var i=0;i<response.data.length;i++){
          if(i<10){
            this.newTrayList2.push(response.data[i])
          }
        }
        this.trayList=this.newTrayList2
      });
    },
    /** 查询库区列表 */
    getReservoirList() {
      let qps = {
        pageNum: 1,
        pageSize: 5000,
      };
      listReservoir(qps).then(response => {
        this.reservoirList = response.rows;
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
    /** 列表分页事件 */
    pagination(info){
      this.$modal.confirm("只能提交上架当前页的库位，更换页码会导致之前选中内容消失，确定要继续吗？").then(() => {
        this.queryParams.pageNum = this.pageNum
        this.queryParams.pageSize = this.pageSize
        this.getList();
      }).catch(() => {
        this.pageNum = this.queryParams.pageNum
        this.pageSize = this.queryParams.pageSize
      });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      // this.trayList=this.newTrayList2
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
        this.form.taskInList = list
        this.$forceUpdate()
      })
      setTimeout(()=>{
        this.trayList = this.newTrayList
      },3000)
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
          if (data[i].locationName === data[i - 1].locationName) {
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
      if (columnIndex === 0 || columnIndex === 1) {
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
      this.$set(this.form.taskInList,index,info)
    },
    /** 托盘出库/回库 */
    handleMoveStatus(row,type){
      this.$modal.confirm('是否确认将该载具'+(type == 1 ? '出库':'回库')+'？').then(function() {
        return type == 1 ? takeOutTray({ id: row.id }) : recycleTray({ id: row.id });
      }).then(() => {
        this.getList(true);
        this.$modal.msgSuccess((type == 1 ? '出库':'回库') + "成功");
      }).catch(() => {});
    },
    //载具状态修改
    handleTrayChange(val,row){
      let taskInList = JSON.parse(JSON.stringify(this.form.taskInList))
      taskInList.map((item) => {
        if(item.locationId == row.locationId){
          item.trayId = val
        }
        return item
      })
      this.form.taskInList = taskInList
      this.$forceUpdate()
    },
    /** 删除明细操作 */
    deleteDetailed(index) {
      this.$modal.confirm("是否确认删除此明细").then(() => {
        let taskInList = JSON.parse(JSON.stringify(this.form.taskInList))
        taskInList.splice(index,1)
        this.getSpanArr(taskInList)
        this.$nextTick(() => {
          this.form.taskInList = taskInList
        })
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 提交按钮 */
    submitForm() {
      this.$message.closeAll()
      if(this.form.taskInList.length === 0){
        this.$message.warning("请选择库位！")
        return false
      }
      this.inputIndex = ''
      let errorMessage = []
      this.form.taskInList.map((item) => {
        let num = Number(item.detectionCount);
        let jNum=Number(item.putawayCount);
        let iNum=Number(item.reActualCount);
        if(jNum==0 && iNum > num){
          errorMessage.push(`物料【${(item.materialName)}】的上架数量应小于或等于预计上架数量`)
        }
        if(jNum==0 && iNum == num){
          if(item.rfids){
            if(item.rfids.length>iNum||item.rfids.length<iNum){
              errorMessage.push(`物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
            }
          }
        }
        if(jNum==0 && iNum < num){
          if(item.rfids){
            if(item.rfids.length>iNum||item.rfids.length<iNum){
              errorMessage.push(`物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
            }
          }else{
            errorMessage.push(`请选择物料【${(item.materialName)}】详细信息`)
          }
        }
        if(jNum!=0 && iNum>num-jNum){
          errorMessage.push(`物料【${(item.materialName)}】的上架数量应小于预计上架数量`)
        }
        if(jNum!=0 && iNum<=num-jNum){
          if(item.rfids){
            if(item.rfids.length>iNum||item.rfids.length<iNum){
              errorMessage.push(`物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
            }
          }else{
            errorMessage.push(`请选择物料【${(item.materialName)}】详细信息`)
          }
        }
        // if(Number(item.reActualCount) > (Number(item.detectionCount) - Number(item.putawayCount))){
        //   errorMessage.push(`物料【${(item.materialName)}】的上架数量应小于预计上架数量`)
        // }
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let taskInList = []
          this.form.taskInList.map((item) => {
            let info = {
              advanceRegistrationId: item.advanceRegistrationId,
              materialId: item.materialId,
              actualCount: item.reActualCount,
              trayId: item.trayId,
              // trayStatus: item.trayStatus,
              locationId: item.locationId,
              batchCode: item.batchCode,
            }
            taskInList.push(info)
          })
          if(this.detailType == '2'){
            putawayInDeliveryTaskComplete({taskInList, id: this.detailId}).then((response) => {
              if(response.code === 200){
                this.form.taskInList = []
                this.$modal.msgSuccess("上架成功");
                this.open = false
                this.$emit("setArriveTask",true)
              }
            });
          } else {
            putawayInDeliveryTaskComplete({taskInList, id: this.detailId}).then((response) => {
              if(response.code === 200){
                this.form.taskInList = []
                this.$modal.msgSuccess("上架成功");
                this.open = false
                this.$emit("setArriveTask",true)
              }
            });
          }
        }
      });
    },
    /** 取消按钮 */
    cancel(){
      this.open = false
      this.form = {
        taskInList: []
      }
    },
    // 新增的物料什么的
    handleChooseRfid(item){
        console.log(item);
        this.$emit("showMaterialRfid",{ id: item.advanceRegistrationId,reActualCount:item.reActualCount })
    },
    //清空物料rfid信息
    handleRfidDelete(row,index){
      let info = JSON.parse(JSON.stringify(row))
      info.rfids = null
      info.rfidString = null
      this.$set(this.form.taskInList,index,info)
    },
  }
}
</script>
