<template>
  <el-dialog title="生成上架任务" :visible.sync="open" width="60%" append-to-body>
    <el-form ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="100px" style="padding-right: 30px">
      <el-form-item label="载具编号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入载具编号,多个以,分割"/>
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
    <el-table ref="trayTable" v-loading="loading" :data="vehicleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="载具编号" align="center" prop="code" width="250"></el-table-column>
      <el-table-column label="载具类型" align="center" prop="trayCategory">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_tray_category" :value="scope.row.trayCategory" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"></el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="210">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wms_t_tray" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-sell" v-if="scope.row.palletNum !== null && scope.row.palletNum !== ''" @click="handleMoveStatus(scope.row,1)">载具出库</el-button>
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
          <el-table-column label="载具编号" align="center" prop="trayCode"  min-width="120"></el-table-column>
          <el-table-column label="载具状态" align="center" prop="trayStatus"  min-width="100">
            <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskInList.' + scope.$index + '.trayStatus'" :rules="detailRules.trayStatus">
                <el-select v-model="scope.row.trayStatus" class="select-input-form" @change="val => handleTrayStatusChange(val,scope.row)">
                  <el-option v-for="dict in dict.type.wms_t_tray" :key="dict.value" :disabled="dict.label == '空闲'" :label="dict.label" :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="物料编码" align="center" prop="materialCode" min-width="100"></el-table-column>
          <el-table-column label="物料名称" align="center" prop="materialName"  min-width="170"></el-table-column>
          <el-table-column label="预计上架数量" align="center" prop="detectionCount"  min-width="120"></el-table-column>
          <el-table-column label="已上架数量" align="center" prop="putawayCount"  min-width="100" v-if="!again"></el-table-column>
          <el-table-column label="上架数量" align="center" prop="reActualCount" min-width="80">
            <!-- <template slot-scope="scope">
              <el-form-item label-width="0px" :prop="'taskInList.' + scope.$index + '.reActualCount'" :rules="detailRules.reActualCount">
                <el-input v-model="scope.row.reActualCount" placeholder="请输入上架数量" maxlength="6" size="small" @blur="val => handleInputChange(val,'reActualCount',scope.$index,scope.row)"></el-input>
              </el-form-item>
            </template> -->
          </el-table-column>
          <el-table-column label="rfid" align="center" prop="rfidString" min-width="180" ></el-table-column>
          <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" min-width="280">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteDetailed(scope.$index,scope.row)">删除</el-button>
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
import { putawayInDeliveryTask, putawayInAfreshTask, getInDeliveryTaskTrayPageList } from "@/api/inoutDelivery/inDelivery";

// import { listTray } from "@/api/wms/Tray";
import { takeOutTray, recycleTray } from "@/api/wms/Tray";
export default {
  name: 'ArriveTask',
  dicts: ['wms_t_tray','wms_t_tray_category'],
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
        code: null,
        trayCategory: null,
      },
      pageNum: 1,
      pageSize: 10,
      //物料信息
      materialList: [],
      //载具状态
      trayStatusList: [],
      // 表单参数
      form: {
        taskInList: []
      },
      detailRules: {
        reActualCount: [
          { trigger: "blur", validator: validateReActualCount },
        ],
        trayStatus: [
          { required: true, message: "请选择载具状态", trigger: "change" },
        ],
      },
      again:false,// 是否是从 重新组盘 进入的
      taskId:null,
      originTrayId:null,
      spanArr:[], // 需要合并的行数
      pos:0,// 索引
    }
  },
  watch: {
    open(val){
      if(val){
        this.getList(true)
      }
    }
  },
  methods: {
    /** 获取载具列表 */
    getList(flag){
      this.loading = true;
      if(!this.queryParams.trayCategory){
        this.queryParams.trayCategory = this.trayType
      }
      if(this.originTrayId){
        this.queryParams.id = this.originTrayId;
      }
      getInDeliveryTaskTrayPageList({...this.queryParams, notStatus: 2}).then((response) => {
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
    /** 列表分页事件 */
    pagination(info){
        if(this.currentSelection.length>0){
            this.$modal.confirm("只能提交上架当前页的载具，更换页码会导致之前选中内容消失，确定要继续吗？").then(() => {
                this.queryParams.pageNum = this.pageNum
                this.queryParams.pageSize = this.pageSize
                this.getList();
            }).catch(() => {
                this.pageNum = this.queryParams.pageNum
                this.pageSize = this.queryParams.pageSize
            });
        }else{
            this.queryParams.pageNum = this.pageNum
            this.queryParams.pageSize = this.pageSize
            this.getList();
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
      this.currentSelection = JSON.parse(JSON.stringify(selection))
			console.log(this.currentSelection,1111,selection);
      let list = []
      if(this.again){
        for(let i = 0; i < this.currentSelection.length; i++){
          for(let j = 0; j < this.materialList.length; j++){
            let info = {
              originId: this.materialList[j].originId,
              advanceRegistrationId: this.materialList[j].advanceRegistrationId,
              trayCode: this.currentSelection[i].code,
              trayId: this.currentSelection[i].id,
              trayStatus: '1',
              materialCode: this.materialList[j].materialCode,
              materialId: this.materialList[j].materialId,
              locationId: this.materialList[j].locationId,
              locationName: this.materialList[j].locationName,
              materialName: this.materialList[j].materialName,
              batchCode: this.materialList[j].batchCode,
              predictCount: this.materialList[j].actualCount,
              detectionCount: this.materialList[j].actualCount,
              // putawayCount: this.materialList[j].actualCount,
              putawayCount: 0,
              // reActualCount: this.materialList[j].actualCount,
			  reActualCount: 0,
            }
            list.push(info)
          }
        }
      }else{
        for(let i = 0; i < this.currentSelection.length; i++){
          for(let j = 0; j < this.materialList.length; j++){
            let info = {
              advanceRegistrationId: this.materialList[j].id,
              trayCode: this.currentSelection[i].code,
              trayId: this.currentSelection[i].id,
              trayStatus: '1',
              materialCode: this.materialList[j].materialCode,
              materialId: this.materialList[j].materialId,
              materialName: this.materialList[j].materialName,
              batchCode: this.materialList[j].batchCode,
              predictCount: this.materialList[j].predictCount,
              detectionCount: this.materialList[j].detectionCount,
              putawayCount: this.materialList[j].putawayCount,
              // reActualCount: Number(this.materialList[j].detectionCount) - Number(this.materialList[j].putawayCount),
							reActualCount:0
            }
            list.push(info)
          }
        }
      }
      this.getSpanArr(list)
      this.$nextTick(() => {
        this.form.taskInList = list
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
          if (data[i].trayCode === data[i - 1].trayCode) {
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
    handleTrayStatusChange(val,row){
      let taskInList = JSON.parse(JSON.stringify(this.form.taskInList))
      taskInList.map((item) => {
        if(item.trayCode == row.trayCode){
          item.trayStatus = val
        }
        return item
      })
      this.form.taskInList = taskInList
      this.$forceUpdate()
    },
    /** 删除明细操作 */
    deleteDetailed(index,row) {
      this.$modal.confirm("是否确认删除此明细").then(() => {
        let taskInList = JSON.parse(JSON.stringify(this.form.taskInList))
        taskInList.splice(index,1)
        this.getSpanArr(taskInList)
		let rowInx=null
		this.vehicleList.map((res,inx)=>{
			res.id==row.trayId?rowInx=inx:''
		})
		console.log()
        this.$nextTick(() => {
          this.form.taskInList = taskInList
		  this.$refs.trayTable.toggleRowSelection(this.vehicleList[rowInx],false)
        })
	  }).then(() => {
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 提交按钮 */
    submitForm() {
      if(this.again){
        // 这里应该是重新组合的提交
        this.submitRefresh();
      }else{
        //正常组盘提交
        this.submiteCommon();
      }


    },
    submiteCommon(){
      // 正常的提交
      this.$message.closeAll()
      if(this.form.taskInList.length === 0){
        this.$message.warning("请选择载具！")
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
        // if(jNum==0 && iNum == num){
        //   if(item.rfids){
        //     if(item.rfids.length>iNum||item.rfids.length<iNum){
        //       errorMessage.push(`物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
        //     }
        //   }
        // }
        // if(jNum==0 && iNum < num){
        //   if(item.rfids){
        //     if(item.rfids.length>iNum||item.rfids.length<iNum){
        //       errorMessage.push(`物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
        //     }
        //   }else{
        //     errorMessage.push(`请选择物料【${(item.materialName)}】详细信息`)
        //   }
        // }
        if(jNum!=0 && iNum>num-jNum){
          errorMessage.push(`物料【${(item.materialName)}】的上架数量应小于预计上架数量`)
        }
        // if(jNum!=0 && iNum<=num-jNum){
        //   if(item.rfids){
        //     if(item.rfids.length>iNum||item.rfids.length<iNum){
        //       errorMessage.push(`物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
        //     }
        //   }else{
        //     errorMessage.push(`请选择物料【${(item.materialName)}】详细信息`)
        //   }
        // }
				if(iNum==0){
					errorMessage.push(`载具【${(item.trayCode)}】的上架数量为0，请选择物料详细信息或删除载具`)
				}
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
              trayStatus: item.trayStatus,
              batchCode: item.batchCode,
              rfIds:item.rfids
            }
            taskInList.push(info)
          })
          this.$modal.confirm("组盘上架后将直接调用设备执行，是否确认组盘？").then(() => {
            return putawayInDeliveryTask({taskInList});
          }).then((response) => {
            if(response.code === 200){
              this.$modal.msgSuccess("生成上架成功");
              this.open = false
              this.form = {
                taskInList: []
              }
              this.queryParams = {
                pageNum: 1,
                pageSize: 10,
                code: null,
                trayCategory: null,
              }
              this.$emit("setArriveTask",true)
              this.$refs.buyerTable.clearSelection()
            }
          }).catch(() => { });
        }
      });
      this.pageNum=1;
      this.pageSize=10;
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        code: null,
        trayCategory: null,
      }

    },

    submitRefresh(){
      // 正常的提交
      this.$message.closeAll()
      if(this.form.taskInList.length === 0){
        this.$message.warning("请选择载具！")
        return false
      }
      this.inputIndex = ''
      let errorMessage = []
	  console.log(this.form)
      this.form.taskInList.map((item) => {
        let num = Number(item.detectionCount);//预计上架数量 
        let jNum=Number(item.putawayCount);//已上架数量 0
        let iNum=Number(item.reActualCount);//上架数量 0
		console.log(num,jNum,iNum)
        if(jNum==0 && iNum > num){
          errorMessage.push(`1物料【${(item.materialName)}】的上架数量应小于或等于预计上架数量`)
        }
     //    if(jNum==0 && iNum == num){
     //      if(item.rfids){
			  // console.log(item,item.rfids,iNum)
     //        if(item.rfids.length>iNum||item.rfids.length<iNum){
     //          errorMessage.push(`2物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
     //        }
     //      }
     //    }
        if(jNum==0 && iNum < num){
          if(item.rfids){
            if(item.rfids.length>iNum||item.rfids.length<iNum){
              errorMessage.push(`3物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
            }
          }else{
            errorMessage.push(`请选择物料【${(item.materialName)}】详细信息`)
          } 
        }
        if(jNum!=0 && iNum>num-jNum){
          errorMessage.push(`4物料【${(item.materialName)}】的上架数量应小于预计上架数量`)
        }
        if(jNum!=0 && iNum<=num-jNum){
          if(item.rfids){
            if(item.rfids.length>iNum||item.rfids.length<iNum){
              errorMessage.push(`5物料【${(item.materialName)}】的上架数量应等于Rfid的数量`)
            }
          }else{
            errorMessage.push(`请选择物料【${(item.materialName)}】详细信息`)
          }
        }
		if(iNum==0){
			errorMessage.push(`载具【${(item.trayCode)}】的上架数量为0，请选择物料详细信息或删除载具`)
		}
      })
      if(errorMessage.length > 0){
        this.$message.error(errorMessage.join("，"))
        return false
      }
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let taskInList = [];
          this.form.taskInList.map((item) => {
            let info = {
              originId: item.originId,
              advanceRegistrationId: item.advanceRegistrationId,
              materialId: item.materialId,
              actualCount: item.reActualCount,
              trayId: item.trayId,
              trayStatus: item.trayStatus,
              batchCode: item.batchCode,
              rfIds:item.rfids
            }
            taskInList.push(info)
          })
          let param = {
            id:this.taskId,
            taskInList:taskInList
          }
          this.$modal.confirm("组盘上架后将直接调用设备执行，是否确认组盘？").then(() => {
            return putawayInAfreshTask(param);
          }).then((response) => {
            if(response.code === 200){
              this.$modal.msgSuccess("生成上架成功");
              this.open = false
              this.form = {
                taskInList: []
              }
              this.queryParams = {
                pageNum: 1,
                pageSize: 10,
                code: null,
                trayCategory: null,
              }
              this.$emit("setArriveTask",true)
              this.$refs.buyerTable.clearSelection()
            }
          }).catch(() => { });
        }
      });
      this.pageNum=1;
      this.pageSize=10;
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        code: null,
        trayCategory: null,
      }


    },
    /** 取消按钮 */
    cancel(){
      this.open = false
      this.form = {
        taskInList: []
      }
      this.pageNum=1,
      this.pageSize=10
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        code: null,
        trayCategory: null,
      }
    },
    // 新增的物料什么的
    handleChooseRfid(item){
      console.log(item,456);
      if(this.again){
        this.$emit("showMaterialRfid",{ id: item.advanceRegistrationId,originId: item.originId,reActualCount:item.reActualCount,trayId:item.trayId,again:this.again,locationId:item.locationId,materialId:item.materialId,rfids:item.rfids})
      }else{
        this.$emit("showMaterialRfid",{ id: item.advanceRegistrationId,reActualCount:item.reActualCount,trayId:item.trayId,materialId:item.materialId,rfids:item.rfids})
      }

    },
    //清空物料rfid信息
    handleRfidDelete(row,index){
      let info = JSON.parse(JSON.stringify(row))
      info.rfids = null
      info.rfidString = null
			info.reActualCount=0
      this.$set(this.form.taskInList,index,info)
      if(this.$refs.materialRfidCom.$refs.tabSelect){
          this.$refs.materialRfidCom.$refs.tabSelect.clearSelection()
          this.$refs.materialRfidCom.selection=[]
      }
    },

  }
}
</script>
