<template>
	<view>
		<headTitle title="载具管理"></headTitle>
		<view class="content" v-if="isMenuShow">
			<view class="ability" @click="goPage('./putInStorage/VehicleOutbound')" v-if="checkPermi(['pda:vehicle:vehicle'])">
				<view class="imgBox">
					<image src="@/static/img/emptyIcon.png" alt=""></image>
				</view>
				<view class="title">载具列表</view>
			</view>
			<view class="ability" @click="goPage('./vehicleReturn/vehicleReturn')" v-if="checkPermi(['pda:vehicle:return'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">载具回库</view>
			</view>
			<view class="ability" @click="goPage('./containerStorage/containerStorage')" v-if="checkPermi(['pda:vehicle:outbound'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">载具出库</view>
			</view>
			<view class="ability" @click="goPage('./palletHand/palletHand')" v-if="checkPermi(['pda:vehicle:transport'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">托盘搬运</view>
			</view>
			<!-- <view class="ability" @click="goPage('./godownEntry/index?page=1&title=箱托绑定')" v-if="checkPermi(['pda:vehicle:binding'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">箱托绑定</view>
			</view> -->
		</view>
		<view class="content-empty" v-else>
			<u-empty class="emptyClass" text="暂无权限" mode="list"></u-empty>
		</view>
	</view>
</template>

<script>
	import { checkPermi, checkRole } from "@/utils/permission";
	export default {
		data() {
			return {
				checkPermi,
				isMenuShow: false
			}
		},
		onShow(){
			if(this.checkPermi(['pda:vehicle:vehicle']) || this.checkPermi(['pda:vehicle:return']) || this.checkPermi(['pda:vehicle:outbound']) || this.checkPermi(['pda:vehicle:transport']) || this.checkPermi(['pda:vehicle:binding'])){
				this.isMenuShow = true
			} else {
				this.isMenuShow = false
			}
		},
		methods: {
			goPage(url){
				uni.navigateTo({
					url:url
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
	.content-empty{
		width: 100%;
		padding: 30rpx;
		height: calc(100vh - 60rpx - 104rpx - 100rpx);
	}
	.content {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		padding: 30rpx;
		.ability {
			width: 333rpx;
			height: 333rpx;
			background: #FFFFFF;
			border-radius: 28rpx;
			text-align: center;
			margin-bottom: 30rpx;
			.imgBox {
				padding-top: 75rpx;
				padding-bottom: 45rpx;
				image {
					width: 95rpx;
					height: 95rpx;
				}
			}
			.title{
				font-size: 44rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #0B0B0B;
			}
		}
	}
</style>