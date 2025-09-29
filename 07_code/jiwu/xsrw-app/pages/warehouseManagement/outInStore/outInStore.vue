<template>
	<view>
		<headTitle title="执行拣货"></headTitle>
		<!-- <VehicleList ref="vehicle" :isChuku="false" @registreOk="registreOks" @closeOtherScan="closeOtherScanVehicle"></VehicleList> -->
		<view style="width: 100%;height: 10rpx;"></view>
		<scanMaterialsNoRFID ref="scanMater" :orderId="detailId" typeName="拣货" :trayCode="trayCode"
			@showSmallChoose="showSmallChoose"></scanMaterialsNoRFID>
		<!--@closeOtherScan="closeOtherScanMaterials"-->
		<!-- <view class="radioBox">
			<view class="btn" v-if="isSmallChoose" @click="receive">小件领取</view>
		</view> -->
		<view class="btnBox">
			<view class="cancel" @click="cancel">取消</view>
			<view class="submit" @click="submit">提交</view>
		</view>
		<view>
			<u-modal v-model="show" title="小件领取" width="90%" @confirm="submitXj">
				<view slot="default">
					<view style="height: 900rpx;padding: 30rpx;word-wrap: break-word;">
						<view>
							<view class="tdSty">
								<view class="boxText">区域：{{xiaojianInfo.areaName}}</view>
								<view class="boxText">载具：{{xiaojianInfo.trayName}}</view>
							</view>
							<view class="tdSty">
								<view class="boxText">库区：{{xiaojianInfo.reservoirName}}</view>
								<view class="boxText">库位：{{xiaojianInfo.locationName}}</view>
							</view>
							<view class="tdSty">
								<view class="boxText">
									转换单位：1{{xiaojianInfo.unitName}}={{xiaojianInfo.count||0}}{{xiaojianInfo.smallUnitName}}
								</view>
								<view class="boxText"></view>
							</view>
						</view>
						<view class="titleBox">
							<view style="display: flex;align-items: center;">
								<view class="line"></view>
								<view class="text">请扫描物料标签</view>
							</view>
						</view>
						<view class="abilityBtn">
							<image src="@/static/img/labelBtnIcon.png" alt="" @click="registers()"></image>
						</view>
						<!-- <view style="display: flex;align-items: center;margin-top: 30rpx;">
							<span style="margin-right: 20rpx;font-size: 36rpx;font-family: PingFang SC;font-weight: 500;color: #0B0B0B;">小件领取数量:</span>
							<u-input v-model="numCout" type="number" :border="true"  style="width: 200rpx;"/>
						</view> -->
					</view>

				</view>
			</u-modal>
			<u-modal v-model="equation" content="拣货数量与出库任务预计数量不一致， 确定要提交吗？" :show-title="false" show-cancel-button
				@confirm="confirmSub" @cancel="equation=false"></u-modal>
			<u-modal v-model="showHK" content="是否将该载具回库？" :show-title="false" @confirm="confirmHkSub" show-cancel-button
				@cancel="cancelHkSub"></u-modal>
			<u-modal v-model="showHK" content="是否将该载具回库？" :show-title="false" @confirm="confirmHkSub" show-cancel-button
				@cancel="cancelHkSub"></u-modal>

			<u-popup v-model="showTaskHk" mode="center" :mask-close-able="false" :closeable="false">
				<view style="padding: 30rpx;border-radius: 20rpx;">
					<view style="font-size: 28rpx;font-weight: bold;margin-bottom: 20rpx;">请选择类型</view>
					<u-radio-group v-model="sort">
						<u-radio :name="1">物料整托出库</u-radio>
						<u-radio :name="2">需半料回库</u-radio>
					</u-radio-group>

					<u-button @click="submitTaskType" style="margin-top: 20rpx;">提交</u-button>
				</view>

			</u-popup>
		</view>
	</view>
</template>

<script>
	var jpushModule = uni.requireNativePlugin("Uhf-Plugin")
	export default {
		data() {
			return {
				sort: 1,
				isshowHk: false,
				showTaskHk: false,
				showHK: false,
				equation: false,
				numCout: 0,
				show: false,
				queryParams: {},
				xiaojianInfo: {}, //小件领取信息
				scannerResult: {
					scannerdata: ""
				},
				detailId: null,
				trayCode: null,
				isSmallChoose: true,
				taskNo: '',
				locationName: null,

			}
		},
		onLoad(opt) {
			if (opt.id) {
				this.detailId = Number(opt.id)
				this.trayCode = opt.trayCode
				this.taskNo = opt.taskNo
				this.locationName = opt.locationName
				console.log(opt.trayCode)
			}
		},
		methods: {
			submitXj() {
				// const materialCountList=this.$refs.scanMater.materialCountList
				let materIndex = 0
				let materSmallCount = 0
				let materSmallrfid = null
				this.$refs.scanMater.materialList.map((ret, index) => {
					console.log(index)
					if (this.xiaojianInfo.batchCode == ret.batchCode) {
						ret.smallReceiveCount ? materSmallCount = Number(ret.smallReceiveCount) + Number(this
							.numCout) : materSmallCount = Number(0) + Number(this.numCout)
						materSmallrfid = this.xiaojianInfo.rfid
					}
				})
				console.log('我是扫码结果', materIndex, materSmallCount, this.$refs.scanMater.materialList)
				this.$refs.scanMater.materialList[materIndex].smallReceiveCount = materSmallCount
				this.$refs.scanMater.materialList[materIndex].rfid = materSmallrfid
				this.$refs.scanMater.$forceUpdate()
				console.log('我是被改变的值', this.$refs.scanMater.materialList)
			},
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
				let predictCount = this.$refs.scanMater.materialList && this.$refs.scanMater.materialList.length > 0 ? this
					.$refs.scanMater.materialList[0].predictCount : null
				let actualCount = this.$refs.scanMater.materialList && this.$refs.scanMater.materialList.length > 0 ? this
					.$refs.scanMater.materialList[0].actualCount : 0
				this.queryParams.trayCode = this.trayCode
				this.queryParams.id = this.detailId
				this.queryParams.receiveCount = this.$refs.scanMater.materialList && this.$refs.scanMater.materialList
					.length > 0 ? this.$refs.scanMater.materialList[0].rfidsAllCount : null
				this.queryParams.rfidListPda = this.$refs.scanMater.materialList[0].rfids
				console.log(7777, actualCount, this.queryParams.receiveCount, predictCount)
				if (predictCount == actualCount + this.queryParams.receiveCount) {
					if (this.locationName.substr(0, 2) == '一楼')
						this.isshowHk = true
				}
				console.log(this.queryParams)
				// return
				// if(!this.queryParams.trayCode){
				// 	this.$modal.msgError("请选择载具！");
				// 	return
				// }
				// if(this.queryParams.receiveCount && this.queryParams.receiveCount <= 0){
				// 	this.$modal.msgError("请扫描物料！");
				// 	return
				// }
				if (!this.queryParams.receiveCount) {
					this.$modal.msgError("预计拣货数量为0，不可提交！");
					return
				}

				if (predictCount < this.queryParams.receiveCount) {
					this.$modal.msgError("扣除数量太大！");
					return
				}

				// if(smallPredictCount && smallPredictCount != this.queryParams.smallReceiveCount || (this.queryParams.receiveCount && this.queryParams.receiveCount > 0 && this.queryParams.receiveCount != rfidCount)){
				// 	this.$modal.msgError("拣货数量与出库任务预计数量不一致，不可提交！");
				// 	return
				// }
				this.confirmSub()
			},
			submitTaskType() {
				let that = this
				that.queryParams.taskNo=that.taskNo
				that.queryParams.sort=that.sort
				uni.showLoading({
					title: '加载中'
				})
				that.httpOut()
			},
			confirmSub() {
				console.log(this.queryParams)
				let that = this
			
				if (that.isshowHk) {
					that.showTaskHk = true
				} else {
					uni.showLoading({
						title: '加载中'
					})
					this.httpOut()
				}
			},
			httpOut(){
				this.$http.postAction('/wms/out/executeOutTaskPDA', this.queryParams).then(
					res => { // /wms/outApi/executeOutTaskPDA
						// if(res.data){
						// this.showHK=true
						// }else{
						console.log(res)
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
					}).finally(()=>{
						uni.hideLoading()
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