<template>
	<view class="scanBox">
		<view class="titleBox" v-if="isScan">
			<view class="line"></view>
			<view class="text">请扫描物料条码</view>
		</view>
		<view class="abilityBtn" v-if="isScan">
			<view style="position: relative;">
				<image v-if="type == 2" style="position: absolute;top: 0px;left: 0px;z-index: 2;"
					src="@/static/img/selectedbg.png" alt=""></image>
				<image src="@/static/img/labelBtnIcon.png" alt="" @click="selType(2)"></image>
			</view>
		</view>
		<view :class="{ 'tableBox': true, 'noTop': !isScan }">
			<u-table border-color="#595959" :key="keyValue">
				<u-tr>
					<u-th width="40%">物料名称</u-th>
					<u-th width="20%">预计</u-th>
					<u-th width="20%">已拣</u-th>
					<u-th width="20%">本次</u-th>
				</u-tr>
				<u-tr>
					<u-td width="40%" class="u-tdsty">{{materialName}}</u-td>
					<u-td width="20%">{{predictCount}}</u-td>
					<u-td width="20%">{{receiveCount}}</u-td>
					<u-td width='20%'>{{rfidsAllCount}}</u-td>
				</u-tr>
			</u-table>
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="60%">批次号/RFID编码</u-th>
					<u-th width="40%">本RFID扣除数量</u-th>
				</u-tr>

				<u-tr v-if="rfidsAll !== null && rfidsAll.length > 0" v-for="(item,index) in rfidsAll">
					<u-td width="60%">
						<view style="display: flex; align-items: center;align-self: center;" @click="del(index)">
							<image src="@/static/img/del.png" style="width: 30rpx;height: 30rpx; align-items: center; ">
							</image>
							<view style="display: flex; flex-direction: column;">
								<view>{{item.batchCode}}</view>
								<view>{{item.rfidHead}}</view>
							</view>

						</view>
					</u-td>
					<u-td width="40%">
						<u-input class="input-style" v-model="item.rfidCount" type="number" :border="true"
							@input="onInput(index, $event)" />
					</u-td>
				</u-tr>
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
			materialName: {
				type: String,
				default: null
			},
			predictCount: {
				type: Number,
				default: null
			},
			receiveCount: { //已拣数量
				type: Number,
				default: ""
			},
		},
		data() {
			return {
				keyValue: 10086,
				starting: false,
				type: null, //扫描类型
				materialList: [], //扫描结果
				materialCountList: {}, //预计数量
				rfidsAll: [],
				rfids: [],
				isSmallChoose: true,
				isScan: true,
				rfidsAllCount: 0
			};
		},
		created() {
			console.log(111, this.materialName)
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
					let scannerdata = _this.scannerResult.scannerdata.trim();
					scannerdata = scannerdata.replace("\n", "")
					_this.$http.getAction('/wms/inout/detail/getRfidInfo?rfid=' + scannerdata, {}).then(
						res => {
							let materialList = res.data
							if (_this.rfidsAll) {
								let isHas = false
								for (let i = 0; i < _this.rfidsAll.length; i++) {
									if (_this.rfidsAll[i].rfidHead === materialList.rfidHead) {
										isHas = true;
									}
								}
								if (!isHas) {
									_this.rfidsAll.push(materialList)
									_this.rfidsAllCount = (parseFloat(_this.rfidsAllCount) +
										materialList.rfidCount).toFixed(3);
								}
							} else {
								let rfids = []
								rfids.push(materialList)
								_this.rfidsAll = rfids
								_this.rfidsAllCount = materialList.rfidCount || 0
							}
							console.log("rfidsAll=", _this.rfidsAll)
							console.log("rfidsAllCount=", _this.rfidsAllCount)
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
					this.materialCountList = respone.data
				}
				let _this = this;
				jpushModuleScanMaterials.inventoryStart();
				jpushModuleScanMaterials.cardSearchContinue((result) => {
					_this.$http.postAction('/wms/inout/detail/alllist', {
						tMaterialDetailDTOS: result.readValues
					}).then(res => {
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
			del(index) {
				var _this = this;
				let item = _this.rfidsAll[index]
				_this.rfidsAll.splice(index, 1)
				this.rfidsAllCount = 0
				if (_this.rfidsAll !== null && _this.rfidsAll.length > 0) {
					_this.rfidsAll.map(item2 => {
						this.rfidsAllCount = (item.rfidCount + item2.rfidCount || 0)
							.toFixed(3);
					})
				}
				_this.$forceUpdate()
			},
			onInput(index, event) {
				console.log('event', event)
				var _this = this;
				let item = _this.rfidsAll[index]
				item.rfidCount = event;

				_this.rfidsAllCount = 0
				if (_this.rfidsAll !== null && _this.rfidsAll.length > 0) {
					_this.rfidsAll.map(item2 => {
						_this.rfidsAllCount = (parseFloat(item2.rfidCount || 0)).toFixed(3);
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