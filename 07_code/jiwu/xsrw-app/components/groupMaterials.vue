<template>
	<view class="scanBox">
		<view class="titleBox">
			<view class="line"></view>
			<view class="text">请扫描物料条码</view>
		</view>
		<view class="abilityBtn">
			<view style="position: relative;">
				<image v-if="type == 1" style="position: absolute;top: 0px;left: 0px;z-index: 2;"
					src="@/static/img/selectedbg.png" alt=""></image>
				<image src="@/static/img/rfidBtn.png" alt="" @click="selType(1)"></image>
			</view>
			<view style="position: relative;">
				<image v-if="type == 2" style="position: absolute;top: 0px;left: 0px;z-index: 2;"
					src="@/static/img/selectedbg.png" alt=""></image>
				<image src="@/static/img/labelBtnIcon.png" alt="" @click="selType(2)"></image>
			</view>
		</view>
		<view>
			<view class="startBtn" v-if="!starting" @click="startScan">开始扫描</view>
			<view class="startBtn" v-if="starting">进行中···</view>
			<!-- <view class="startBtn" v-if="starting" @click="stop">停止</view>
			<view class="startBtn"  @click="stop">停止</view> -->
		</view>
		<view class="tableBox">
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="34%">物料名称</u-th>
					<u-th width="33%">批次号</u-th>
					<u-th width="33%">上架数量</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in materialList">
					<u-td width="34%" class="u-tdsty">{{item.materialName}}</u-td>
					<u-td width="33%" class="u-tdsty">{{item.batchCode}}</u-td>
					<u-td width="33%">
						<u-input class="input-style" v-model="item.actualCount" disabled type="number" :border="true" />
					</u-td>
				</u-tr>
			</u-table>
		</view>
	</view>
</template>

<script>
	var jpushModuleMaterial = uni.requireNativePlugin("Uhf-Plugin")
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
		},
		data() {
			return {
				trayCode: null,
				type: null, //扫描类型
				materialList: [], //扫描结果
				materialCountList: {}, //预计数量
				retult: [], //扫描结果
				starting: false,
			};
		},
		created() {
			if (this.typeName == '组盘' && this.orderId) {
				this.getApi()
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
			getApi() {
				this.$http.getAction(`/wms/api/inout/delivery/getDetail/${this.orderId}`, this.queryParams)
					.then(res => {
						// let orderList = res.data.deliveryDetailList || []
						// orderList.map(item => {
						// 	item.expectedCount = item.predictCount
						// 	item.count = item.registrationCount || 0
						// })
						console.log(res)
						this.materialList = res.data.deliveryDetailList
						this.materialList.map((item) => {
							item.actualCount = 0
						})
						console.log(this.materialList)
						// this.total = res.total
					})
			},
			getMaterialCountList() {
				this.$http.getAction('/wms/api/inout/delivery/getMaterialCountList', {}).then(res => {
					this.materialCountList = res.data
				})
			},
			getOrderList() {
				this.$http.getAction(`/wms/api/inout/delivery/getDetail/${this.orderId}`, {})
					.then(res => {
						let orderList = res.data.deliveryDetailList || []
						orderList.map(item => {
							item.expectedCount = item.predictCount
							item.count = item.registrationCount || 0
						})
						this.materialList = orderList
						// this.total = res.total
					})
			},
			selType(e) {
				this.type = e
				if (this.type == 1 && this.starting) {
					return
				}
				this.starting = false
				this.$modal.msg(`已选择${e==1?'RFID':'标签'}！`);
				this.$emit('closeOtherScan', '')
				this.retult = []
				this.$nextTick(() => {
					jpushModuleMaterial.openDevice()
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
			close() {
				jpushModuleMaterial.closeDevice()
				plus.key.removeEventListener("keydown")
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
				jpushModuleMaterial.scannerRegister({
					name: "com.uhf.scanner"
				}, async function(result) {
					_this.scannerResult = result
					let scannerdata = _this.scannerResult.scannerdata.trim()
					if (_this.typeName == '拣货') {
						let respone = await _this.$http.getAction('/wms/outApi/scanTray', {
							trayCode: _this.trayCode
						})
						_this.materialCountList = respone.data
					}
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
					_this.retult = []
					if (index === -1) {
						_this.retult.push(resultInfo)
					}
					console.log('retult', _this.retult);
					_this.$http.postAction('/wms/inout/detail/alllist', {
						tMaterialDetailDTOS: _this.retult
					}).then(res => {
						if (_this.typeName == '组盘' && _this.orderId) {
							let materialList = res.data
							_this.materialList.map((item) => {
								let info = materialList.find((materialInfo) => {
									return materialInfo.materialId == item
										.materialId && materialInfo.batchCode == item
										.batchCode
								})
								if (info) {
									if (item.rfids) {
										let isHas = false
										for (let i = 0; i < info.rfidHeads.length; i++) {
											if (item.rfids.includes(info.rfidHeads[i])) {
												isHas = true;
											}
										}
										if (info && !isHas) {
											item.rfids = [...item.rfids, ...info.rfidHeads]
											item.actualCount = (parseFloat(item.actualCount) +
													info.count || 0)
												.toFixed(3);
										}
									} else {
										if (info) {
											item.rfids = info.rfidHeads
											item.actualCount = info.count || 0
										}
									}
								}
							})
						} else {
							_this.materialList = res.data
							if (_this.materialCountList && _this.materialCountList.length > 0) {
								for (let a in _this.materialCountList) {
									_this.materialList.map(ret => {
										ret.batchCode == a
										ret.expectedCount = this.materialCountList[a]
										ret.actualCount = info.count || 0
									})
								}
							} else {
								for (let a in _this.materialList) {
									console.log('a',_this.materialList[a])
									_this.materialList[a].actualCount = _this.materialList[a].count || 0
								}
							}
						}
						_this.$forceUpdate()
					})
				})
			},
			stop() {
				jpushModuleMaterial.cardSearchStop()
				uni.showToast({
					icon: 'none',
					title: '停止了',
					duration: 3000
				})
			},
			async setLoggerEnable() {
				if (this.typeName == '拣货') {
					let respone = await this.$http.getAction('/wms/outApi/scanTray', {
						trayCode: this.trayCode
					})
					this.materialCountList = respone.data
				}
				let _this = this
				jpushModuleMaterial.inventoryStart();
				setTimeout(function() {
					jpushModuleMaterial.cardSearchContinue((result) => {
						_this.retult = result
						_this.$http.postAction('/wms/inout/detail/alllist', {
							tMaterialDetailDTOS: result.readValues
						}).then(res => {
							if (_this.typeName == '组盘' && _this.orderId) {
								let materialList = res.data
								_this.materialList.map((item) => {
									let info = materialList.find((materialInfo) => {
										return materialInfo.materialId == item
											.materialId && materialInfo
											.batchCode == item.batchCode
									})
									if (item.rfids) {
										let isHas = false
										for (let i = 0; i < info.rfidHeads
											.length; i++) {
											if (item.rfids.includes(info.rfidHeads[
													i])) {
												isHas = true;
											}
										}
										if (info && !isHas) {
											item.rfids = [...item.rfids, ...info
												.rfidHeads
											]
											item.actualCount = (parseFloat(item
														.actualCount) + info.count ||
													0)
												.toFixed(3);
										}
									} else {
										if (info) {
											item.rfids = info.rfidHeads
											item.actualCount = info.count || 0
										}
									}
								})
							} else {
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
				}, 300)

			},
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