<template>
	<view class="pallet-hand">
		<headTitle title="载具回库"></headTitle>
		<VehicleList ref="vehicle" :isChuku="false"></VehicleList>
		<view class="cont">
			<button class="button" type="primary" @click="goSubmit">回库</button>
			<u-modal v-model="warehousing" content="确定要将该载具回库吗？" :show-title="false" show-cancel-button @confirm="confirm" @cancel="warehousing=false"></u-modal>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				warehousing: false,
			};
		},
		methods: {
			goSubmit() {
				if (!this.$refs.vehicle.scannerResult.scannerdata) {
					uni.showToast({
						icon: 'none',
						title: '请扫描载具标签'
					});
				} else {
					this.warehousing = true
				}
			},
			confirm() {
				const data = {
					code: this.$refs.vehicle.scannerResult.scannerdata.slice(0, 15)
				}
				this.$http.postAction('/wms/api/base/tray/recycleStock', data).then(res=>{
					this.$modal.msgSuccess(res.msg);
					setTimeout(() => {
						this.$tab.navigateBack(1);
					}, 1000)
					console.log(res)
				})
			},
		}
	}
</script>

<style lang="scss">
	.pallet-hand {
		background: #F5F5F5;

		.cont {

			.titleBox {
				background: #FFFFFF;
				padding: 45rpx 20rpx;
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
				padding-top: 45rpx;
				background: #FFFFFF;
				display: flex;
				justify-content: space-around;

				image {
					width: 255rpx;
					height: 255rpx;
				}
			}

			.tableBox {
				padding: 16rpx;
				background: #FFFFFF;

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
				}
			}

			.button {
				margin: 30rpx 30rpx;
				position: absolute;
				width: 92%;
				bottom: 10rpx;
			}
		}
	}
</style>