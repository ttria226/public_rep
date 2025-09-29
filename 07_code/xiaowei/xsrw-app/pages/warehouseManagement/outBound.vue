<template>
	<view>
		<headTitle title="出库管理"></headTitle>
		<view class="content" v-if="isMenuShow">
			<view class="ability" @click="goPage('./putOutStorage/index')" v-if="checkPermi(['pda:outbound:outbound'])">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">出库计划</view>
			</view>
			<view class="ability" @click="goPage('./deliveryExecution/index?page=1')" v-if="checkPermi(['pda:outbound:allocation'])">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">出库分配</view>
			</view>
			<view class="ability" @click="goPage('./deliveryExecution/index?page=2')" v-if="checkPermi(['pda:outbound:select'])">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">出库拣选</view>
			</view>
			<!-- <view class="ability" @click="goPage('./checkPackage/index')" v-if="checkPermi(['pda:outbound:check'])">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">复核打包</view>
			</view>
			<view class="ability" @click="goPage('./waveDivision/waveDivision')" v-if="checkPermi(['pda:outbound:waveAllocation'])">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">波次分配</view>
			</view>
			<view class="ability" @click="goPage('./waveDivision/waveDivision')" v-if="checkPermi(['pda:outbound:waveSelect'])">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">波次拣选</view>
			</view>
			<view class="ability" @click="goPage('./waveDivision/waveDivision')" v-if="checkPermi(['pda:outbound:waveDivision'])">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">波次分拨</view>
			</view>
			<view class="ability" @click="goPage('./reflux/index')" v-if="checkPermi(['pda:outbound:reflux'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">出库回流</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./outInStore/outInStore')">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">出库任务</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./putInStorage/VehicleOutbound')">
				<view class="imgBox">
					<image src="@/static/img/emptyIcon.png" alt=""></image>
				</view>
				<view class="title">空载具取出</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./outInStore/returnVehicle')">
				<view class="imgBox">
					<image src="@/static/img/zaijuPutIn.png" alt=""></image>
				</view>
				<view class="title">载具回库</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./deliveryExecution/index')">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">出库执行列表</view>
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
			if(this.checkPermi(['pda:outbound:outbound']) || this.checkPermi(['pda:outbound:allocation']) || this.checkPermi(['pda:outbound:select']) || this.checkPermi(['pda:outbound:check']) || this.checkPermi(['pda:outbound:waveAllocation']) || this.checkPermi(['pda:outbound:waveSelect']) || this.checkPermi(['pda:outbound:waveDivision']) || this.checkPermi(['pda:outbound:reflux'])){
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