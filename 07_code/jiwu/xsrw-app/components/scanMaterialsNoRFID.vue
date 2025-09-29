<template>
	<view class="scanBox">
		<view class="titleBox" v-if="isScan">
			<view class="line"></view>
			<view class="text">请扫描物料条码</view>
		</view>
		<view class="abilityBtn" v-if="isScan">
			<!-- <view style="position: relative;">
				<image v-if="type == 1" style="position: absolute;top: 0px;left: 0px;z-index: 2;" src="@/static/img/selectedbg.png" alt=""></image>
				<image src="@/static/img/rfidBtn.png" alt="" @click="selType(1)"></image>
			</view> -->
			<view style="position: relative;">
				<image v-if="type == 2" style="position: absolute;top: 0px;left: 0px;z-index: 2;"
					src="@/static/img/selectedbg.png" alt=""></image>
				<image src="@/static/img/labelBtnIcon.png" alt="" @click="selType(2)"></image>
			</view>
			<!-- <image src="@/static/img/rfidBtn.png" alt="" @click="selType(1)"></image> -->
			<!-- <image src="@/static/img/selectedbg.png" alt="" @click="selType(1)"></image> -->
			<!-- <image src="@/static/img/labelBtnIcon.png" alt="" @click="selType(2)"></image> -->
		</view>
		<!-- <view v-if="isScan">
			<view class="startBtn" v-if="!starting" @click="startScan">开始扫描</view>
			<view class="startBtn" v-if="starting">进行中···</view>
		</view> -->
		<view :class="{ 'tableBox': true, 'noTop': !isScan }">
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="20%">物料名称</u-th>
					<u-th width="20%">批次号</u-th>
					<u-th width="20%">预计</u-th>
					<u-th width="20%">已拣</u-th>
					<u-th width="20%">本次</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in materialList">
					<u-td width="20%" class="u-tdsty">{{item.materialName}}</u-td>
					<u-td width="20%" class="u-tdsty">{{item.batchCode}}</u-td>
					<u-td width="20%">{{typeName=='拣货' ? item.predictCount : item.expectedCount}}</u-td>
					<u-td width="20%">{{item.actualCount?item.actualCount:0}}</u-td>
					<u-td width="20%">{{item.rfidsAllCount?item.rfidsAllCount:0}}</u-td>
					<!-- <u-td width="20%" v-if="typeName=='拣货' && isSmallChoose">{{item.smallReceiveCount}}</u-td> -->
				</u-tr>
			</u-table>
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="60%">RFID编码</u-th>
					<u-th width="40%">本RFID扣除数量</u-th>
				</u-tr>
				<view v-for="(item,index) in materialList">
					<u-tr v-if="item.rfids !== undefined && item.rfids.length > 0" v-for="(item2,index1) in item.rfids">
						<u-td width="60%">
							<view style="display: flex; align-items: center;align-self: center;"
								@click="del(index,index1)">
								<image src="@/static/img/del.png"
									style="width: 30rpx;height: 30rpx; align-items: center; "></image>
								<view>{{item2.rfid?item2.rfid:''}}</view>
							</view>
						</u-td>
						<u-td width="40%">
							<u-input class="input-style" v-model="item2.count" type="number" :border="true"
								@input="onInput(index,index1, $event)" />
						</u-td>
						<!-- <u-td width="20%" v-if="typeName=='拣货' && isSmallChoose">{{item.smallReceiveCount}}</u-td> -->
					</u-tr>
				</view>
			</u-table>
		</view>
	</view>
</template>

<script>
	var jpushModuleScanMaterials = uni.requireNativePlugin("Uhf-Plugin")
	export default {
		name: "scanMaterials",
		props: {
			typeName: {
				type: String,
				default: "入库"
			},
			orderId: {
				type: Number,
				default: null
			},
			trayCode: {
				type: String,
				default: ""
			},
		},
		data() {
			return {
				starting: false,
				type: null, //扫描类型
				materialList: [], //扫描结果
				materialCountList: {}, //预计数量
				retult: [], //扫描结果
				isSmallChoose: true,
				isScan: true
			};
		},
		created() {
			if (this.typeName == '入库') {
				this.getMaterialCountList()
			}
			if (this.typeName == '收货' && this.orderId) {
				this.getOrderList()
			}
			if (this.typeName == '拣货' && this.orderId) {
				this.getTaskMaterialsList()
			}
		},
		methods: {
			// getScanTray(trayCode) {
			// 	this.$http.getAction('/wms/outApi/scanTray', {
			// 		trayCode: trayCode
			// 	}).then(res => {
			// 		console.log(666666666, res)
			// 		this.materialCountList = res.data
			// 	})
			// },
			getTaskMaterialsList() {
				let _this = this;
				this.$http.getAction('/wms/out/' + this.orderId, {
					type: 2
				}).then(res => {
					console.log(res.data)
					res.data.rfidsCount = null
					_this.materialList = [{
						...res.data
					}]
					_this.isSmallChoose = res.data.smallPredictCount > 0 ? true : false
					_this.isScan = res.data.predictCount ? true : false
					_this.$emit('showSmallChoose', _this.isSmallChoose)
				})
			},
			getMaterialCountList() {
				let _this = this;
				this.$http.getAction('/wms/api/inout/delivery/getMaterialCountList', {}).then(res => {
					_this.materialCountList = res.data
				})
			},
			getOrderList() {
				let _this = this;
				this.$http.getAction(`/wms/api/inout/delivery/getDetail/${this.orderId}`, {})
					.then(res => {
						let orderList = res.data.deliveryDetailList || []
						orderList.map(item => {
							item.expectedCount = item.predictCount
							item.count = item.registrationCount || 0
						})
						_this.materialList = orderList
						// this.total = res.total
					})
			},
			handleInput(e, type, item, index) {
				console.log(e)
				let info = JSON.parse(JSON.stringify(item))
				let value = ''
				if (e.length === 1) {
					value = e.replace(/[^1-9]/g, '');
				} else {
					value = e.replace(/[^\d]/g, '');
				}
				info[type] = value
				console.log(value)
				this.materialList.splice(index, 1, info)
			},
			close() {
				jpushModuleScanMaterials.closeDevice()
				plus.key.removeEventListener("keydown")
			},
			selType(e) {
				this.type = e
				if (this.type == 1 && this.starting) {
					return
				}
				this.starting = false
				this.$modal.msg(`已选择${e==1?'RFID':'标签'}！`);
				this.$emit('closeOtherScan', '')
				this.$nextTick(() => {
					jpushModuleScanMaterials.openDevice()
					plus.key.addEventListener("keydown", function() {
						alert("BackButton Key pressed!");
					}, false);
				})
				if (e == 2) {
					setTimeout(() => {
						this.register()
					}, 500)
				}
			},
			// 判断开始扫描的类型
			startScan() {
				if (!this.type) {
					this.$modal.msg("请先选择扫描类型！");
					return
				}
				if (this.type == 1) {
					this.starting = true
					this.setLoggerEnable()
				} else {
					this.$modal.msg("请选择扫描类型为RFID！");
					return
				}
				// else {
				// 	this.register()
				// }
			},
			register() {
				var _this = this;
				jpushModuleScanMaterials.scannerRegister({
					name: "com.uhf.scanner"
				}, async function(result) {
					_this.scannerResult = result
					console.log(1111, result);
					let scannerdata = _this.scannerResult.scannerdata.trim();
					scannerdata = scannerdata.replace("\n", "")
					// if(_this.typeName=='拣货'){
					// 	let respone = await _this.$http.getAction('/wms/outApi/scanTray', {trayCode: _this.trayCode})
					// 	console.log(333333,respone.data)
					// 	_this.materialCountList = respone.data
					// }
					let resultInfo = {
						id: scannerdata,
						count: 1,
						len: 12,
						rssi: -42,
						embeded: ""
					}

					let index = _this.retult.findIndex((item) => {
						return item.id === resultInfo.id
					})
					if (index === -1) {
						if (_this.retult != null && _this.retult.length > 0) {
							_this.retult.splice(0)
						}
						_this.retult.push(resultInfo)
					}
					_this.$http.postAction('/wms/inout/detail/alllist', {
						tMaterialDetailDTOS: _this.retult
					}).then(res => {
						console.log(666666, res)
						if (_this.typeName == '收货' || _this.typeName == '拣货') {
							let materialList = res.data
							_this.materialList.map((item) => {
								let info = materialList.find((materialInfo) => {
									return materialInfo.batchCode == item.batchCode &&
										materialInfo.materialId == item.materialId
								})
								if (item.rfidsAll) {
									let isHas = false
									for (let i = 0; i < info.rfidHeads.length; i++) {
										if (item.rfidsAll.includes(info.rfidHeads[i])) {
											isHas = true;
										}
									}
									if (info && !isHas) {
										let rfid = {}
										rfid.rfid = info.rfidHeads[0]
										rfid.count = info.count || 0
										item.rfids.push(rfid)
										item.rfidsAll = [...item.rfidsAll, ...info.rfidHeads]
										item.rfidsAllCount = (parseFloat(item.rfidsAllCount) +
											info.count).toFixed(3);
									}
								} else {
									if (info) {
										let rfid = {}
										rfid.rfid = info.rfidHeads[0]
										rfid.count = info.count || 0
										let rfids = []
										rfids.push(rfid)
										item.rfids = rfids
										item.rfidsAll = info.rfidHeads
										item.rfidsAllCount = info.count || 0
									}
								}
							})
						} else if (_this.typeName == '入库') {
							_this.materialList = res.data
							for (let a in _this.materialCountList) {
								_this.materialList.map(ret => {
									ret.batchCode == a
									ret.expectedCount = _this.materialCountList[a]
								})
							}
						}
						_this.retult = []
						_this.$forceUpdate()
					})
				})
			},
			stop() {
				jpushModuleScanMaterials.cardSearchStop()
				uni.showToast({
					icon: 'none',
					title: '停止了',
					duration: 3000
				})
			},
			async setLoggerEnable() {
				console.log('trayCode', this.trayCode)
				if (this.typeName == '拣货') {
					let respone = await this.$http.getAction('/wms/outApi/scanTray', {
						trayCode: this.trayCode
					})
					console.log(333333, respone.data)
					this.materialCountList = respone.data
				}
				let _this = this;
				jpushModuleScanMaterials.inventoryStart();
				jpushModuleScanMaterials.cardSearchContinue((result) => {
					console.log(111111111, result)
					_this.retult = result
					_this.$http.postAction('/wms/inout/detail/alllist', {
						tMaterialDetailDTOS: result.readValues
					}).then(res => {
						console.log(666666, res)
						if (_this.typeName == '收货' || _this.typeName == '拣货') {
							let materialList = res.data
							_this.materialList.map((item) => {
								let info = materialList.find((materialInfo) => {
									return materialInfo.batchCode == item.batchCode
								})
								if (info) {
									if (_this.typeName == '拣货') {
										item.rfidsCount = info.count || 0
									} else {
										item.count = info.count || 0
									}
									item.rfids = info.rfids
								}
							})
						} else if (_this.typeName == '入库') {
							_this.materialList = res.data
							for (let a in _this.materialCountList) {
								_this.materialList.map(ret => {
									ret.batchCode == a
									ret.expectedCount = _this.materialCountList[a]
								})
							}
						}
						_this.starting = false
						_this.$forceUpdate()
					})

				})
			},
			del(index, index1) {
				var _this = this;
				let item = _this.materialList[index]
				item.rfids.splice(index1, 1)
				item.rfidsAll.splice(index1, 1)
				item.rfidsAllCount = 0
				if (item.rfids !== null && item.rfids.length > 0) {
					item.rfids.map(item2 => {
						console.log(item2)
						if(item2.count)item.rfidsAllCount += item2.count
					})
				}
				item.rfidsAllCount.toFixed(3)
				_this.$forceUpdate()
			},
			onInput(index, index1, event) {
				console.log('event', event)
				var _this = this;
				let item = _this.materialList[index]
				item.rfids[index1].count = event;

				item.rfidsAllCount = 0
				if (item.rfids !== null && item.rfids.length > 0) {
					item.rfids.map(item2 => {
						item.rfidsAllCount = (parseFloat(item.rfidsAllCount) + parseFloat(item2.count || 0))
							.toFixed(3);
					})
				}
				_this.$forceUpdate()
			}
		}
	}
</script>

<style scoped lang="scss">
	/deep/.u-tdsty {
		word-wrap: break-all;
		word-break: break-all;
	}

	.scanBox {
		background-color: #fff;
		padding: 45rpx 20rpx;

		.titleBox {
			display: flex;
			align-items: center;

			.line {
				width: 6rpx;
				height: 36rpx;
				background: #1948B2;
				border-radius: 3rpx;
			}

			.text {
				font-size: 36rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #1948B2;
				margin-left: 10rpx;
			}
		}

		.abilityBtn {
			margin-top: 45rpx;
			display: flex;
			justify-content: space-around;

			image {
				width: 255rpx;
				height: 255rpx;
			}
		}

		.startBtn {
			width: 162rpx;
			height: 72rpx;
			background-size: 100% 100%;
			margin-left: 100rpx;
			margin-top: 20rpx;
			background-image: url(@/static/img/startScan.png);
			font-size: 33rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 72rpx;
			text-align: center;
		}

		.tableBox {
			margin-top: 55rpx;

			&.noTop {
				margin-top: 0px;
			}

			/deep/.u-th {
				background: linear-gradient(180deg, rgba(10, 133, 213, 0.85), rgba(1, 87, 174, 0.85));
				opacity: 0.98;
				font-size: 31rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #FFFFFF;
				text-shadow: 0rpx 2rpx 5rpx rgba(0, 57, 113, 0.35);
			}

			/deep/ .u-td {
				background: #F4F6F7;
				font-size: 33rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #0B0B0B;
				height: auto;
			}
		}
	}
</style>