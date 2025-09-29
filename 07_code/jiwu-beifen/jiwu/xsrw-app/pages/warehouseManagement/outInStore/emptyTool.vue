<template>
	<view>
		<headTitle title="空载具取出"></headTitle>
		<u-sticky>
			<view class="searchBox">
				<u-search placeholder="请输入载具编码" shape="square" v-model="queryParams.keyword"></u-search>
			</view>
		</u-sticky>
		<u-sticky>
			<view style="display: flex;align-items: center;">
				<view class="typeSel">仓库 <u-icon name="arrow-down-fill" size="22" style="margin-left: 20rpx;"></u-icon></view>
				<view style="width: 3rpx ;height: 38rpx;background-color: #CFD1D2;"></view>
				<view class="typeSel">区域 <u-icon name="arrow-down-fill"  size="22" style="margin-left: 20rpx;"></u-icon></view>
				<view style="width: 3rpx ;height: 38rpx;background-color: #CFD1D2;"></view>
				<view class="typeSel">库区 <u-icon name="arrow-down-fill"  size="22" style="margin-left: 20rpx;"></u-icon></view>
			</view>
		</u-sticky>
		<view class="contentBox">
			<view v-for="(item,index) in 5" class="cardBox">
				<view class="top">
					<view class="line"></view>
					<view>托盘编码：LPN035</view>
				</view>
				<view class="botto"></view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				queryParams: {
					keyword: ''
				}
			}
		},
		methods: {

		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style scoped lang="scss">
	.searchBox {
		background-color: #fff;
		padding: 10rpx 20rpx;
		display: flex;
		align-items: center;
	}

	.typeSel {
		width: 33%;
		text-align: center;
		line-height: 70rpx;
	}
	.contentBox{
		padding: 30rpx;
	}
	.cardBox{
		padding: 30rpx;
		.top{
			width: 6rpx;
			height: 36rpx;
			background: #1948B2;
			border-radius: 3rpx;
		}
	}
</style>