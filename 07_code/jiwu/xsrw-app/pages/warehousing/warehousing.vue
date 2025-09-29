<template>
	<view class="pageContent">
		<view class="head">
			<view class="top" @click="handleLogout">退出登录</view>
			<view>智慧化仓库管理系统</view>
			<view>欢迎您！</view>
			<view style="text-align: right;font-size: 30rpx;" @click="goEQ">设备部</view>
		</view>
		<view class="ability">
			<view class="cardBox" @click="goPage('./warehouseManagement/warehouseManagement')">
				<view class="title">入库管理</view>
				<view style="margin-left: 150rpx;">
					<image src="@/static/img/ruku.png" alt=""></image>
				</view>
			</view>
			<view class="cardBox" style="background-color: #EEF2FE;" @click="goPage('./warehouseManagement/outBound')">
				<view class="title" style="color: #5A2DEE;">出库管理</view>
				<view style="margin-left: 150rpx;">
					<image src="@/static/img/chuku.png" alt=""></image>
				</view>
			</view>
			<view class="cardBox" style="background-color: #EEF2FE;" @click="goPage('./warehouseManagement/vehicle')">
				<view class="title" style="color: #5A2DEE;">载具管理</view>
				<view style="margin-left: 150rpx;">
					<image src="@/static/img/chuku.png" alt=""></image>
				</view>
			</view>
			<view class="cardBox" style="background-color: #EAF7ED;" @click="goPage('./warehouseManagement/stock')">
				<view class="title" style="color: #04B58D;">库存管理</view>
				<view style="margin-left: 150rpx;">
					<image src="@/static/img/pandian.png" alt=""></image>
				</view>
			</view>
<!--			<view class="cardBox" style="background-color: #FEF4F2;" @click="goPage('./warehouseManagement/shortcuts')">-->
<!--				<view class="title" style="color: #ED3142;">快捷功能</view>-->
<!--				<view style="margin-left: 150rpx;">-->
<!--					<image src="@/static/img/chaxun.png" alt=""></image>-->
<!--				</view>-->
<!--			</view>-->
			<!-- <view class="cardBox" style="background-color: #FEF4F2;" @click="goPage('./warehouseManagement/inventory')">
				<view class="title" style="color: #ED3142;">库存查询</view>
				<view style="margin-left: 150rpx;">
					<image src="@/static/img/chaxun.png" alt=""></image>
				</view>
			</view> -->
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {
			goEQ(){
				this.$tab.navigateTo("/pages/equipment/index")
			},
			goPage(url){
				uni.switchTab({
					url:url
				})
			},
			// 退出登录
			handleLogout() {
				this.$modal.confirm('确定注销并退出系统吗？').then(() => {
					this.$store.dispatch('LogOut').then(() => {
						this.$tab.reLaunch('/pages/login')
					})
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.head {
		padding: 30rpx;
		width: 100%;
		height: 540rpx;
		background-image: url(@/static/img/warehousingBg.png);
		background-size: 100% 100%;
		font-size: 47rpx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #FFFFFF;
		text-align: center;
		line-height: 70rpx;

		.top {
			text-align: right;
			font-size: 36rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			padding-bottom: 95rpx;
		}
	}

	.ability {
		border-radius: 42rpx 42rpx 0 0;
		padding: 57rpx 30rpx;
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		background-color: #FFFFFF;
		position: relative;
		top: -30rpx;

		.cardBox {
			width: 333rpx;
			height: 333rpx;
			background: #FFF8E9;
			border-radius: 28rpx;
			margin-bottom: 30rpx;

			.title {
				padding: 70rpx 30rpx;
				font-size: 44rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #FB8000;
				line-height: 10rpx;
			}

			image {
				width: 158rpx;
				height: 158rpx;
			}
		}
	}
</style>