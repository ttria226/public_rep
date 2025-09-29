<template>
	<view>
		<headTitle title="载具回库"></headTitle>
		<VehicleList ref="vehicle" :isChuku="false"></VehicleList>
		<view class="btnBox">
			<view class="submit" @click="submit">回库</view>
		</view>
		<u-modal v-model="showModal" content="确定要将该载具回库吗？" :show-title="false" @confirm="submitForm"
			@cancel="showModal=false">
		</u-modal>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
	export default {
		data() {
			return {
				showModal:false,
				queryParams: {
					trayCode: null,
				},
			}
		},
		methods: {
			submit() {
				if (this.$refs.vehicle.scannerResult.scannerdata.trim()) {
					this.showModal=true
				} else {
					this.$refs.uToast.show({
						title: '请扫描载具标签',
						type: 'error',
					})
				}
			},
			submitForm() {
				this.queryParams.trayCode = this.$refs.vehicle.scannerResult.scannerdata.trim()
				this.$http.getAction('/wms/outApi/trayBack', this.queryParams).then(res => {
					this.$modal.msgSuccess(res.msg);
					setTimeout(()=>{
						this.$tab.switchTab("/pages/warehouseManagement/warehouseManagement");
					}, 1000)
				})
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
		display: flex;
		justify-content: end;
		margin-right:30rpx;
		margin-top:30rpx;
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
</style>