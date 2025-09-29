<template>
	<view>
		<headTitle title="入库管理"></headTitle>
		<view class="content" v-if="isMenuShow">
			<view class="ability" @click="goPage('./reservation/index?state=1')" v-if="checkPermi(['pda:delivery:delivery'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">预约管理</view>
			</view>
			<view class="ability" @click="goPage('./reservation/index?state=2,5')" v-if="checkPermi(['pda:delivery:take'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">收货</view>
			</view>
			<view class="ability" @click="goPage('./reservation/index?state=4')" v-if="checkPermi(['pda:delivery:inspection'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">质检</view>
			</view>
			<view class="ability" @click="goPage('./godownEntry/index?page=1&title=组盘')" v-if="checkPermi(['pda:delivery:putawayTask'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">组盘</view>
			</view>
			<view class="ability" @click="goPage('./executeIn/index?page=1')" v-if="checkPermi(['pda:delivery:grounding'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">上架记录</view>
			</view>
			<view class="ability" @click="goPage('./groundShelf/index')" v-if="checkPermi(['pda:delivery:floorList'])">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">地堆上架</view>
			</view>
			<!-- <view class="ability" @click="goPage('./putInStorage/OrderList')">
				<view class="imgBox">
					<image src="@/static/img/ScanCode.png" alt=""></image>
				</view>
				<view class="title">扫码登记</view>
			</view> -->
			<!-- <view class="ability">
				<view class="imgBox">
					<img src="@/static/img/groundingIcon.png" alt="">
				</view>
				<view class="title">上架</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./godownEntry/index')">
				<view class="imgBox">
					<image src="@/static/img/stockIcon.png" alt=""></image>
				</view>
				<view class="title">入库单列表</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./putInStorage/VehicleList?page=1')" v-if="checkPermi(['pda:shortcuts:takeGroup'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">收货组盘</view>
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
			if(this.checkPermi(['pda:delivery:delivery']) || this.checkPermi(['pda:delivery:take']) || this.checkPermi(['pda:delivery:inspection']) || this.checkPermi(['pda:delivery:putawayTask']) || this.checkPermi(['pda:delivery:grounding']) || this.checkPermi(['pda:delivery:floorList'])){
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