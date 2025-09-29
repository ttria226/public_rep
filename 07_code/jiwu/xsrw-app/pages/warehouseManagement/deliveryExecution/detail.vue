<template>
	<view>
		<headTitle title="执行拣货"></headTitle>
		<VehicleList v-if="type==1" ref="vehicle" :isChuku="false" @registreOk="registreOks" @closeOtherScan="closeOtherScanVehicle"></VehicleList>
		<view style="width: 100%;height: 10rpx;"></view>
		<diduijianhuo ref="scanMater" :materialName="materialName" typeName="执行拣货" :predictCount="predictCount"
			:receiveCount="receiveCount" @showSmallChoose="showSmallChoose"></diduijianhuo>
		<view class="btnBox">
			<view class="cancel" @click="cancel">取消</view>
			<view class="submit" @click="submit">提交</view>
		</view>

	</view>
</template>

<script>
	var jpushModule = uni.requireNativePlugin("Uhf-Plugin")
	export default {
		data() {
			return {
				trayCode: null,
				type:null,
				showHK: false,
				equation: false,
				numCout: 0,
				show: false,
				queryParams: {},
				xiaojianInfo: {}, //小件领取信息
				rfids: [],
				scannerResult: {
					scannerdata: ""
				},
				materialName: null,
				predictCount: null,
				receiveCount: null,
				detailId: null,
				predictReceiveCount: null,
				detailData:null,
				isSmallChoose: true
			}
		},
		onLoad(opt) {
			if (opt.info != undefined) {
				let detailData = JSON.parse(decodeURIComponent(opt.info));
				console.log('kill',detailData)
				this.materialName = detailData.materialName
				this.predictCount = detailData.predictCount
				this.receiveCount = detailData.receiveCount
				this.detailId = detailData.outDeliveryId
				this.detailData=detailData
			}
		},
		methods: {
			receive() {
				this.show = true
			},
			registers() {
				var _this = this;
				jpushModule.scannerRegister({
					name: "com.uhf.scanner"
				}, function(result) {
					console.log(888888888888, result)
					_this.scannerResult = result
					_this.getSmallOut(result.scannerdata.trim())
				})
			},
			getSmallOut(e) {
				this.$http.getAction('/wms/outApi/smallOut', {
					trayCode: this.trayCode,
					rfid: e
				}).then(res => {
					console.log('我是获取扫描标签的信息')
					this.xiaojianInfo = res.data
					this.xiaojianInfo.rfid = e
				})
			},
			registreOks() {
				console.log(7777777777)
				const code = this.$refs.vehicle.scannerResult.scannerdata.trim()
				this.$refs.scanMater.trayCode = code
			},
			showSmallChoose(flag) {
				this.isSmallChoose = flag
			},
			closeOtherScanMaterials() {
				this.$refs.vehicle.close()
			},
			closeOtherScanVehicle() {
				this.$refs.scanMater.close()
			},
			submit() {
				let predictCount = this.$refs.scanMater.predictCount
				let rfidsAllCount = this.$refs.scanMater.rfidsAllCount

				let mTaskOutDetailListVOSs = []
				for (let index = 0; index < this.$refs.scanMater.rfidsAll.length; index++) {
					let mTaskOutDetailListVOS = {}
					mTaskOutDetailListVOS.stockId = this.$refs.scanMater.rfidsAll[index].stockId
					mTaskOutDetailListVOS.rfidHead = this.$refs.scanMater.rfidsAll[index].rfidHead
					mTaskOutDetailListVOS.receiveCount = this.$refs.scanMater.rfidsAll[index].rfidCount
					mTaskOutDetailListVOSs.push(mTaskOutDetailListVOS)
				}
				this.queryParams.outDeliveryDetailId = this.detailData.id
				this.queryParams.tTaskOutDetailListVOS = mTaskOutDetailListVOSs
				console.log("111", predictCount)
				console.log("111", rfidsAllCount)
				console.log("222", this.queryParams)
				console.log("222", this.detailId)
				// return
				// if(!this.queryParams.trayCode){
				// 	this.$modal.msgError("请选择载具！");
				// 	return
				// }
				// if(this.queryParams.receiveCount && this.queryParams.receiveCount <= 0){
				// 	this.$modal.msgError("请扫描物料！");
				// 	return
				// }
				if (!rfidsAllCount) {
					this.$modal.msgError("预计拣货数量为0，不可提交！");
					return
				}

				if (predictCount < rfidsAllCount) {
					this.$modal.msgError("扣除数量太大！");
					return
				}

				// if(smallPredictCount && smallPredictCount != this.queryParams.smallReceiveCount || (this.queryParams.receiveCount && this.queryParams.receiveCount > 0 && this.queryParams.receiveCount != rfidCount)){
				// 	this.$modal.msgError("拣货数量与出库任务预计数量不一致，不可提交！");
				// 	return
				// }

				this.confirmSub()
			},
			confirmSub() {
				console.log(this.queryParams)
				this.$http.postAction('/wms/outApi/groundPileOutbound', this.queryParams).then(res => {
					console.log(999999,res)
						this.$modal.msgSuccess("出库成功！");
						setTimeout(() => {
							// this.$tab.switchTab("/pages/warehouseManagement/warehouseManagement");
							let pages = getCurrentPages(); // 当前页面  
							let beforePage = pages[pages.length - 2]; // 上一页
							uni.navigateBack({
								success: function() {
									// 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
									beforePage.$vm.reFresh();
								}
							})
						}, 2000)
						// }
					})
			},
			confirmHkSub() {
				this.$http.getAction('/wms/outApi/trayBack', {
					trayCode: this.trayCode
				}).then(res => {
					this.$modal.msgSuccess(res.msg);
					setTimeout(() => {
						// this.$tab.switchTab("/pages/warehouseManagement/warehouseManagement");
						let pages = getCurrentPages(); // 当前页面  
						let beforePage = pages[pages.length - 2]; // 上一页
						uni.navigateBack({
							success: function() {
								// 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
								beforePage.$vm.reFresh();
							}
						})
					}, 2000)
				})
			},
			cancelHkSub() {
				let pages = getCurrentPages(); // 当前页面
				let beforePage = pages[pages.length - 2]; // 上一页
				uni.navigateBack({
					success: function() {
						// 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
						beforePage.$vm.reFresh();
					}
				})
			},
			cancel() {
				this.$tab.navigateBack();
			},
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style scoped lang="scss">
	.radioBox {
		width: 100%;
		text-align: right;

		// position: relative;
		// left: 560rpx;
		// top: 20rpx;
		// display: flex;
		// justify-content: end;
		// margin-right:30rpx;
		// margin-top:30rpx;
		.btn {
			background-color: #3AC60E;
			width: 161rpx;
			height: 72rpx;
			line-height: 72rpx;
			font-size: 33rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 72rpx;
			border-radius: 10rpx;
			text-shadow: 0rpx 2rpx 5rpx rgba(0, 113, 19, 0.35);
			text-align: center;
		}
	}

	.btnBox {
		text-align: center;
		display: flex;
		justify-content: space-around;
		margin: 40rpx 0;

		.cancel {
			width: 187rpx;
			height: 91rpx;
			line-height: 91rpx;
			background: #FFFFFF;
			border: 1px solid #0B0B0B;
			border-radius: 7rpx;
			font-size: 39rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #0B0B0B;
		}

		.submit {
			width: 186rpx;
			height: 90rpx;
			line-height: 90rpx;
			background: #0075FF;
			border-radius: 7rpx;
			font-size: 39rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #FFFFFF;
		}
	}

	.titleBox {
		margin-top: 20rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.chukuBtn {
			text-align: center;
			width: 170rpx;
			height: 82rpx;
			background: #0075FF;
			font-size: 36rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			border-radius: 7rpx;
			line-height: 82rpx;
		}

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

	.tdSty {
		display: flex;
		background: #F6FDFF;
		border-radius: 3rpx;
		line-height: 60rpx;
		font-size: 31rpx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #0B0B0B;

		.boxText {
			width: 50%;
			padding: 0rpx 20rpx;
			border: 1rpx solid #C4C4C4;
		}
	}
</style>