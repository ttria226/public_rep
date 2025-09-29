<template>
	<view>
		<headTitle title="快捷功能"></headTitle>
		<view class="content" v-if="isMenuShow">
			<view class="ability" @click="goPage('./putInStorage/OrderList')" v-if="checkPermi(['pda:shortcuts:scanTake'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">扫码收货</view>
			</view>
			<view class="ability" @click="goPage('./putInStorage/VehicleList?page=1')" v-if="checkPermi(['pda:shortcuts:takeGroup'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">收货组盘</view>
			</view>
			<view class="ability" @click="goPage('./putInStorage/VehicleList?page=2')" v-if="checkPermi(['pda:shortcuts:sacnWarehousing'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">扫描入库</view>
			</view>
			<view class="ability" @click="goPage('./godownEntry/index?page=2&title=快捷入库')" v-if="checkPermi(['pda:shortcuts:quickWarehousing'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">快捷入库</view>
			</view>
			<view class="ability" @click="goPage('./shortcuts/scanOutbound/index')" v-if="checkPermi(['pda:shortcuts:scanOutbound'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">扫描出库</view>
			</view>
			<view class="ability" @click="goPage('./quickOutbound/index')" v-if="checkPermi(['pda:shortcuts:quickOutbound'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">快捷出库</view>
			</view>
			<view class="ability" @click="goPage('./qualityPick/index')" v-if="checkPermi(['pda:shortcuts:inspectionPick'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">质检拣选</view>
			</view>
			<view class="ability" @click="goPage('./onlinePick/onlinePick')" v-if="checkPermi(['pda:shortcuts:onlinePick'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">在线拣选</view>
			</view>
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
			if(this.checkPermi(['pda:shortcuts:scanTake']) || this.checkPermi(['pda:shortcuts:takeGroup']) || this.checkPermi(['pda:shortcuts:sacnWarehousing']) || this.checkPermi(['pda:shortcuts:quickWarehousing']) || this.checkPermi(['pda:shortcuts:scanOutbound']) || this.checkPermi(['pda:shortcuts:quickOutbound']) || this.checkPermi(['pda:shortcuts:inspectionPick']) || this.checkPermi(['pda:shortcuts:onlinePick'])){
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