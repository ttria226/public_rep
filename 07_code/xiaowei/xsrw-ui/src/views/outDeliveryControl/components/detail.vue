<template>
	<el-dialog title="出库任务详情" :visible.sync="open" width="60%" append-to-body :modal-append-to-body="false">
		<el-form ref="detailForm" :model="detailForm" label-width="30px" style="padding-right: 30px">
			<!--      <el-row :gutter="10" class="mb8">-->
			<!--        <el-col :span="1.5">-->
			<!--          <div class="dialog-title" style="margin-left: 30px"><span>预计拣货数量：{{ detailForm.taskWcsDetailVOList[0] ? detailForm.taskWcsDetailVOList[0].predictCount : 0 }}</span></div>-->
			<!--        </el-col>-->
			<!--      </el-row>-->
			<el-form-item>
				<el-table :data="detailForm.taskWcsDetailVOList" label-width="30px">
					<el-table-column label="载具编号" align="center" prop="trayCode" width="160"></el-table-column>
					<!-- <el-table-column label="载具状态" align="center" prop="trayStatus"></el-table-column> -->
					<el-table-column label="物料编码" align="center" prop="materialCode" width="180"></el-table-column>
					<el-table-column label="物料名称" align="center" prop="materialName" min-width="180"></el-table-column>
					<el-table-column label="在库数量" align="center" prop="availableCount" width="120"></el-table-column>
					<el-table-column label="预计数量" align="center" prop="predictCount" width="120"></el-table-column>
					<el-table-column label="已拣数量" align="center" prop="actualCount" width="120"></el-table-column>
					<!--          v-if="detailType == 3"-->
					<el-table-column label="拣货数量" align="center" prop="receiveCount" width="200">
						<template slot-scope="scope">
							<el-form-item label-width="0px"
								:prop="'taskWcsDetailVOList.' + scope.$index + '.receiveCount'"
								:rules="detailRules.receiveCount">
								<el-input-number v-model="scope.row.receiveCount" :precision="3" :step="0.001"
									:max="999999" placeholder="请输入实际拣货数量" size="large"
									@blur="val => handleInputChange(val,'receiveCount',scope.$index,scope.row)"></el-input-number>
							</el-form-item>
						</template>
					</el-table-column>
					<el-table-column label="计量单位" align="center" prop="unitName" width="100"></el-table-column>
					<el-table-column label="拣货rfid" align="center" prop="rfidString" min-width="180"
						v-if="detailType == 3"></el-table-column>
					<!--          <el-table-column label="预计小件领取数量" align="center" prop="smallPredictCount" width="130"></el-table-column>-->
					<!--          <el-table-column label="小件领取最大数量" align="center" prop="count" width="130"></el-table-column>-->
					<!--          <el-table-column label="小件领取数量" align="center" prop="smallReceiveCount" min-width="180" v-if="detailType == 3">-->
					<!--            <template slot-scope="scope">-->
					<!--              <el-form-item label-width="0px" :prop="'taskWcsDetailVOList.' + scope.$index + '.smallReceiveCount'" :rules="detailRules.smallReceiveCount" v-if="scope.row.smallUnitName">-->
					<!--                <el-input :disabled="detailType == 3" v-model="scope.row.smallReceiveCount" placeholder="请输入小件领取数量" v-intNumber maxlength="6" size="small" @blur="val => handleInputChange(val,'smallReceiveCount',scope.$index,scope.row)">-->
					<!--                  <template slot="append">{{ scope.row.smallUnitName }}</template>-->
					<!--                </el-input>-->
					<!--              </el-form-item>-->
					<!--            </template>-->
					<!--          </el-table-column>-->
					<!--          <el-table-column label="rfid" align="center" prop="rfid" width="150" v-if="detailType == 3">-->
					<!--            <template slot-scope="scope">-->
					<!--              <el-form-item label-width="0px" :prop="'taskWcsDetailVOList.' + scope.$index + '.rfid'" :rules="detailRules.rfid" v-if="scope.row.smallUnitName">-->
					<!--                 <el-input v-model="scope.row.rfid" placeholder="请输入rfid" size="small" @blur="val => handleInputChange(val,'rfid',scope.$index,scope.row)" /> -->
					<!--                <el-select v-model="scope.row.rfid" filterable remote reserve-keyword :remote-method="remoteMethod" allow-create default-first-option placeholder="请选择rfid" :loading="rfidLoading">-->
					<!--                  <el-option v-for="item in rfidOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>-->
					<!--                </el-select>-->
					<!--              </el-form-item>-->
					<!--            </template>-->
					<!--          </el-table-column>-->
					<el-table-column label="批次号" align="center" prop="batchCode" min-width="200"></el-table-column>
					<el-table-column label="库区" align="center" prop="reservoirName" width="120"></el-table-column>
					<el-table-column label="库位" align="center" prop="locationName" width="120"></el-table-column>
					<el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220"
						fixed="right" v-if="detailType == 3">
						<template slot-scope="scope">
							<el-button size="mini" type="text" icon="el-icon-setting"
								v-if="scope.row.receiveCount && scope.row.receiveCount > 0"
								@click="handleChooseRfid(scope.row)">选择物料详细</el-button>
							<el-button size="mini" type="text" icon="el-icon-delete" v-if="scope.row.rfidString"
								@click="handleRfidDelete(scope.row,scope.$index)">清空物料详细</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button type="primary" @click="submitDetailForm"
				v-if="detailType == 1 || detailType == 2 || detailType == 3" :loading="execLoading">执行</el-button>
			<el-button @click="cancel">关 闭</el-button>
		</div>
		<el-dialog :title="`请选择任务${taskNo}的完成类型`" :visible.sync="dialogVisibleTansk" width="30%" :show-close="false"
			:close-on-click-modal="false" :close-on-press-escape="false" @close="dialogVisibleTansk=false"
			:modal-append-to-body="false" append-to-body>
			<el-radio-group v-model="sort">
				<el-radio :label="1">物料整托出库</el-radio>
				<el-radio :label="2">需半料回库</el-radio>
			</el-radio-group>
			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitTanskType">确 定</el-button>
			</span>
		</el-dialog>
	</el-dialog>
</template>
<script>
	import {
		updateLocationDeliveryExecute
	} from "@/api/inoutDelivery/inDelivery";
	import {
		getOutDeliveryTaskDetail,
		enforcementOutDelivery
	} from "@/api/inoutDelivery/outDelivery";
	import {
		listDetail
	} from "@/api/wms/detail";
	export default {
		name: "outDeliveryControlDetailCom",
		dicts: ["wms_asn_origin_type", "wms_take_delivery_type", "wms_asn_status"],
		data() {
			let validateReceiveCount = (rule, value, callback) => {
				if (this.inputIndex !== '' && (value === null || value === '') && (this.detailForm.taskWcsDetailVOList[
						this.inputIndex].smallReceiveCount === null || this.detailForm.taskWcsDetailVOList[this
						.inputIndex].smallReceiveCount === '')) {
					callback(new Error("实际拣货数量和小件领取数量至少一个不能为空"));
					this.$message.error("实际拣货数量和小件领取数量至少一个不能为空")
				} else if (Number(value) <= 0 && value !== null && value !== '') {
					callback(new Error("实际拣货数量应大于0"));
					this.$message.error("实际拣货数量应大于0")
				} else if (this.inputIndex !== '' && Number(value) > 0 && (Number(value) > Number(this.detailForm
						.taskWcsDetailVOList[this.inputIndex].predictCount))) {
					callback(new Error("实际拣货数量应小于预计拣货数量"));
					this.$message.error("实际拣货数量应小于预计拣货数量")
				} else {
					callback();
				}
			};
			let validateSmallReceiveCount = (rule, value, callback) => {
				if (this.inputIndex !== '' && (value === null || value === '') && (this.detailForm.taskWcsDetailVOList[
						this.inputIndex].receiveCount === null || this.detailForm.taskWcsDetailVOList[this
						.inputIndex].receiveCount === '')) {
					callback(new Error("实际拣货数量和小件领取数量至少一个不能为空"));
					this.$message.error("实际拣货数量和小件领取数量至少一个不能为空")
				} else if (Number(value) <= 0 && value !== null && value !== '') {
					callback(new Error("小件领取数量应大于0"));
					this.$message.error("小件领取数量应大于0")
				} else if (this.inputIndex !== '' && Number(value) > 0 && (Number(value) > Number(this.detailForm
						.taskWcsDetailVOList[this.inputIndex].count))) {
					callback(new Error("小件领取数量应小于小件领取最大数量"));
					this.$message.error("小件领取数量应小于小件领取最大数量")
				} else {
					callback();
				}
			};
			let validateRfid = (rule, value, callback) => {
				if (this.inputIndex !== '' && this.detailForm.taskWcsDetailVOList[this.inputIndex]
					.smallReceiveCount !== null && this.detailForm.taskWcsDetailVOList[this.inputIndex]
					.smallReceiveCount !== '' && (value === null || value === '')) {
					callback(new Error("rfid不能为空"));
					this.$message.error("rfid不能为空")
				} else {
					callback();
				}
			};
			return {
				sort: 1,
				taskNo: '',
				dialogVisibleTansk: false,
				//弹窗标识
				open: false,
				//详情表单
				detailForm: {
					taskWcsDetailVOList: []
				},
				//详情类型
				detailType: '',
				//详情Id
				detailId: '',
				//输入index
				inputIndex: null,
				//详情校验规则
				detailRules: {
					receiveCount: [{
						trigger: "blur",
						validator: validateReceiveCount
					}, ],
					smallReceiveCount: [{
						trigger: "blur",
						validator: validateSmallReceiveCount
					}, ],
					rfid: [{
						trigger: "blur",
						validator: validateRfid
					}, ]
				},
				rfidOptions: [], //获取rfid下拉
				rfidLoading: false,
				execLoading: false, //执行按钮loading
			}
		},
		watch: {
			open() {
				if (this.open) {
					this.getRfidOptions()
					this.getDetail();
				}
			},
		},
		methods: {
			submitTanskType() {
				let params = {
					id: this.detailId,
					receiveCount: this.detailForm.taskWcsDetailVOList[0].receiveCount,
					smallReceiveCount: this.detailForm.taskWcsDetailVOList[0].smallReceiveCount,
					rfid: this.detailForm.taskWcsDetailVOList[0].rfid,
					rfidList: this.detailForm.taskWcsDetailVOList[0].rfids,
					taskNo: this.taskNo,
					sort: this.sort
				}
				enforcementOutDelivery(params).then((response) => {
					this.$modal.msgSuccess("强制执行成功");
					this.open = false
					// this.execLoading = false;
					this.detailForm = {
						taskWcsDetailVOList: []
					}
					this.$emit("setControl", true)
				}).finally(() => {
					this.dialogVisibleTansk = false
				});
			},
			/** 查询详情 */
			getDetail() {
				this.loading = true;
				getOutDeliveryTaskDetail(this.detailId, {
					type: 2
				}).then(response => {
					if (response.data.smallPredictCount && response.data.smallPredictCount > 0) {
						response.data.smallReceiveCount = response.data.smallPredictCount
					}
					this.detailForm.taskWcsDetailVOList = [{
						...response.data
					}]
					this.loading = false;
				});
			},
			/** 获取rfid选项 */
			getRfidOptions(query) {
				this.rfidLoading = true
				listDetail({
					pageNum: 1,
					pageSize: 10,
					rfid: query
				}).then(res => {
					this.rfidOptions = res.data
				}).finally(() => {
					this.rfidLoading = false
				});
			},
			remoteMethod(query) {
				if (query !== '') {
					this.getRfidOptions(query)
				}
			},
			//选择物料rifd
			handleChooseRfid(item) {
				this.$emit("showMaterialRfid", {
					locationId: item.locationId,
					batchCode: item.batchCode,
					materialId: item.materialId
				})
			},
			//清空物料rfid信息
			handleRfidDelete(row, index) {
				let info = JSON.parse(JSON.stringify(row))
				info.rfids = null
				info.rfidString = null
				this.$set(this.detailForm.taskWcsDetailVOList, index, info)
			},
			//输入框变化事件
			handleInputChange(val, type, index, row) {
				let info = JSON.parse(JSON.stringify(row))
				info[type] = val.target.value
				this.inputIndex = index
				this.$set(this.detailForm.taskWcsDetailVOList, index, info)
			},
			//执行上架
			submitDetailForm() {
				this.$message.closeAll()
				this.inputIndex = ''
				let errorMessage = []
				if (this.detailType == '3') {
					this.detailForm.taskWcsDetailVOList.map((item) => {
						if ((item.smallReceiveCount == '' || item.smallReceiveCount == null) && (item
								.receiveCount == '' || item.receiveCount == null)) {
							errorMessage.push(`物料【${(item.materialName)}】的实际拣货数量和小件领取数量至少一个不能为空`)
						} else if (Number(item.receiveCount) > 0 && (Number(item.receiveCount) > Number(item
								.predictCount))) {
							errorMessage.push(`物料【${(item.materialName)}】的实际拣货数量应小于预计拣货数量`)
						} else if (Number(item.smallReceiveCount) > 0 && (Number(item.smallReceiveCount) > Number(
								item.count))) {
							errorMessage.push(`物料【${(item.materialName)}】的小件领取数量应小于小件领取最大数量`)
						} else if (item.smallReceiveCount !== null && item.smallReceiveCount !== '' && (item
								.rfid === '' || item.rfid === null)) {
							errorMessage.push(`物料【${(item.materialName)}】的rfid不能为空`)
						} else if (item.receiveCount !== '' && item.receiveCount !== null && !item.rfids) {
							errorMessage.push(`物料【${(item.materialName)}】的拣货rfid信息不能为空`)
						}
						// else if(item.receiveCount !== '' && item.receiveCount !== null){
						//   errorMessage.push(`物料【${(item.materialName)}】的拣货rfid数量必须等于上架数量`)
						// }
					})
				}
				if (errorMessage.length > 0) {
					this.$message.error(errorMessage.join("，"))
					return false
				}
				this.$refs["detailForm"].validate((valid) => {
					if (valid) {
						// this.execLoading = true;
						let params = {}
						if (this.detailType === 3) {
							params = {
								id: this.detailId,
								receiveCount: this.detailForm.taskWcsDetailVOList[0].receiveCount,
								smallReceiveCount: this.detailForm.taskWcsDetailVOList[0].smallReceiveCount,
								rfid: this.detailForm.taskWcsDetailVOList[0].rfid,
								rfidList: this.detailForm.taskWcsDetailVOList[0].rfids
							}
							if (this.detailForm.taskWcsDetailVOList[0].receiveCount + this.detailForm
								.taskWcsDetailVOList[0].actualCount == this.detailForm.taskWcsDetailVOList[0]
								.predictCount&&this.detailForm.taskWcsDetailVOList[0].reservoirId == 33) {
								this.dialogVisibleTansk = true
							} else {
								enforcementOutDelivery(params).then((response) => {
									this.$modal.msgSuccess("强制执行成功");
									this.open = false
									// this.execLoading = false;
									this.detailForm = {
										taskWcsDetailVOList: []
									}
									this.$emit("setControl", true)
								});
							}

						} else {
							params = {
								id: this.detailId,
								locationId: this.detailForm.taskWcsDetailVOList[0].locationId,
							}
							updateLocationDeliveryExecute(params).then((response) => {
								this.$modal.msgSuccess("执行成功");
								this.open = false
								// this.execLoading = false;
								this.detailForm = {
									taskWcsDetailVOList: []
								}
								this.$emit("setControl", true)
							});
						}
					}
				});
			},
			cancel() {
				this.open = false
				this.detailForm = {
					taskWcsDetailVOList: []
				}
			}
		}
	}
</script>