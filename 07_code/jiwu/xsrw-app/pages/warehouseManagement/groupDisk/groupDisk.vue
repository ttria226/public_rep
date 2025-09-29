<template>
	<view>
		<headTitle title="组盘"></headTitle>
		<VehicleList ref="vehicle" @closeOtherScan="closeOtherScanVehicle"></VehicleList>
		<view style="width: 100%;height: 10rpx;"></view>
		<groupMaterialsZP ref="scanMater" :typeName="'组盘'" :orderId="id" @closeOtherScan="closeOtherScanMaterials"></groupMaterialsZP>
		<view class="radioBox">
			<view class="title">请更新载具状态</view>
			<view>
				<u-radio-group v-model="queryParams.trayStatus">
					<u-radio v-for="(item, index) in list" :key="index" :name="item.name">
						{{item.label}}
					</u-radio>
				</u-radio-group>
			</view>
		</view>
		<view class="btnBox">
			<view class="cancel" @click="cancel">取消</view>
			<view class="submit" @click="submit">提交</view>
			<!-- <view class="submit" @click="submitt">提交3</view> -->
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				id: null,
				showflag : true,
				queryParams: {
					trayStatus: 1,
					trayCode: null,
				},
				list: [{
						name: 2,
						label: "全托",
					},
					{
						name: 1,
						label: "半托",
					},
				],
			}
		},
		onLoad(opt) {
			this.id = opt.id ? parseInt(opt.id) : null
		},
		methods: {
			closeOtherScanMaterials(){
				this.$refs.vehicle.close()
			},
			closeOtherScanVehicle(){
				this.$refs.scanMater.close()
			},
			submitt(){
				// this.$modal.loading("登录中，请耐心等待...")
				// uni.showLoading({
				// 	title: '组盘中...',
				// })
				// uni.hideLoading()
				if(this.showflag){
					this.showflag= false
					console.log("........")
					let $this = this;
					setTimeout(function(){
						$this.showflag = true
					},3000)
					
				}
				
			},
			submit() {
			// this.$tab.switchTab("/pages/warehouseManagement/warehouseManagement");
				if(this.showflag){
					this.showflag= false
					console.log("........")
					let $this = this;
					this.queryParams.trayCode = this.$refs.vehicle.scannerResult.scannerdata.trim()
					this.queryParams.materialList = this.$refs.scanMater.materialList || []
					if (this.queryParams.trayCode && this.queryParams.materialList && this.queryParams.materialList.length > 0 && this.queryParams.trayStatus) {
						let zeroNum = 0
						this.queryParams.materialList.map((item) => {
							if(Number(item.actualCount) == 0){
								zeroNum++
							}
						})
						// if(zeroNum !== 0){
						// 	this.$modal.msg('上架数量不能为空或者0')
						// 	return
						// }
						let data = {
							trayCode: this.queryParams.trayCode,
							taskInList: [],
						}
						this.queryParams.materialList.map(item => {
							let material = {
								advanceRegistrationId: item.id,
								materialId: item.materialId,
								actualCount: item.actualCount,
								trayStatus:this.queryParams.trayStatus,
								batchCode: item.batchCode,
								rfIds: item.rfids,
							}
							let str=[]
							if(material.rfIds){
								material.rfIds.map(res=>{
									str.push(res.rfids)
								})
								material.rfIds=str
							}
							if(item.actualCount){
								data.taskInList.push(material)
							}
						})
						console.log(data)
						this.$http.postAction('/wms/api/inout/delivery/putaway', data).then(res => {
							this.$modal.msgSuccess(res.msg)
							setTimeout(() => {
								// uni.navigateTo({
								// 	url: '../godownEntry/index?page=1&title=组盘'
								// });
								let pages = getCurrentPages(); // 当前页面
								let beforePage = pages[pages.length - 3]; // 上一页
								uni.navigateBack({
									delta:2,
								    success: function() {
								        // 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
								        beforePage.$vm.reFresh();
								    }
								})
								$this.showflag = true
							}, 2000)
						}).catch(ret=>{})
					} else {
						if (!this.queryParams.trayCode) {
							this.$modal.msgError('请扫描载具标签')
						} else if (!this.queryParams.materialList || this.queryParams.materialList.length == 0) {
							this.$modal.msgError('请扫描物料条码')
						} else if (!this.queryParams.trayStatus) {
							this.$modal.msgError('请更新载具状态')
						}
					}
					
				}
			},
			cancel() {
				uni.navigateBack();
			}
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
		padding: 30rpx;
		background-color: #fff;
		margin-top: 10rpx;
		display: flex;
		justify-content: space-between;

		.title {
			font-size: 36rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #0B0B0B;
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
</style>