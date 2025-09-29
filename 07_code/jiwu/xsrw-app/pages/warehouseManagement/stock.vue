<template>
	<view>
		<headTitle title="库存管理"></headTitle>
		<view class="content" v-if="isMenuShow">
	<!-- 		<view class="ability">
				<view class="imgBox">
					<image src="@/static/img/dan.png" alt=""></image>
				</view>
				<view class="title">盘点单</view>
			</view> -->
			<view class="ability" @click="goPage('./inventory')" v-if="checkPermi(['pda:stock:query'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">库存查询</view>
			</view>
			<!-- <view class="ability" @click="goPage('./stockStore/index')" v-if="checkPermi(['pda:stock:stock'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">盘点任务</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./directInventory/directInventory')" v-if="checkPermi(['pda:stock:directInventory'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">直接盘点</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./relocation/index')" v-if="checkPermi(['pda:stock:relocation'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">移库任务</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./directRelocation/directRelocation')" v-if="checkPermi(['pda:stock:directRelocation'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">直接移库</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./pickRelocation/detail')" v-if="checkPermi(['pda:stock:relocationWarehousing'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">移库入库</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./pickRelocation/detail')" v-if="checkPermi(['pda:stock:selectRelocation'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">拣选移库</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./lend/lend')" v-if="checkPermi(['pda:stock:equipmentLend'])">
				<view class="imgBox">
					<image src="@/static/img/chukuTask.png" alt=""></image>
				</view>
				<view class="title">设备借出</view>
			</view> -->
			<!-- <view class="ability" @click="goPage('./replenishment/index')" v-if="checkPermi(['pda:stock:replenishment'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">（BOM）补料</view>
			</view> -->
			<view class="ability" @click="goPage('./locationInventory/index')" v-if="checkPermi(['pda:stock:directInventory'])">
				<view class="imgBox">
					<image src="@/static/img/pandianIcon.png" alt=""></image>
				</view>
				<view class="title">盘点任务</view>
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
			if(this.checkPermi(['pda:stock:query']) || this.checkPermi(['pda:stock:stock']) || this.checkPermi(['pda:stock:directInventory']) || this.checkPermi(['pda:stock:relocation']) || this.checkPermi(['pda:stock:directRelocation']) || this.checkPermi(['pda:stock:relocationWarehousing']) || this.checkPermi(['pda:stock:selectRelocation']) || this.checkPermi(['pda:stock:equipmentLend']) || this.checkPermi(['pda:stock:replenishment'])){// || this.checkPermi(['pda:stock:directInventory'])
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