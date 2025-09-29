<template>
	<view>
		<headTitle title="扫描入库"></headTitle>
		<VehicleList ref="vehicle" @closeOtherScan="closeOtherScanVehicle"></VehicleList>
		<view style="width: 100%;height: 10rpx;"></view>
		<scanMaterials ref="scanMater" @closeOtherScan="closeOtherScanMaterials"></scanMaterials>
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
			<view class="submit" @click="submit">入库</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				queryParams: {
					trayStatus: null,
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
		methods: {
			closeOtherScanMaterials(){
				this.$refs.vehicle.close()
			},
			closeOtherScanVehicle(){
				this.$refs.scanMater.close()
			},
			submit() {
				this.queryParams.trayCode = this.$refs.vehicle.scannerResult.scannerdata.trim()
				this.queryParams.materialList = this.$refs.scanMater.materialList
				if (this.queryParams.trayCode && this.queryParams.materialList && this.queryParams.materialList.length > 0 && this.queryParams.trayStatus) {
					this.$http.postAction('/wms/api/inout/delivery/putawayTask', this.queryParams).then(res => {
						this.$modal.msgSuccess(res.msg)
						setTimeout(() => {
							// this.$tab.switchTab("/pages/warehouseManagement/warehouseManagement");
							this.cancel()
						}, 2000)
					})
				} else {
					if (!this.queryParams.trayCode) {
						this.$modal.msgError('请扫描载具标签')
					} else if (!this.queryParams.materialList || this.queryParams.materialList.length == 0) {
						this.$modal.msgError('请扫描物料条码')
					} else if (!this.queryParams.trayStatus) {
						this.$modal.msgError('请更新载具状态')
					}
				}
			},
			cancel() {
				this.$tab.navigateBack();
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