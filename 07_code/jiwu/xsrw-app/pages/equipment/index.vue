<template>
	<view class="work-container">
		<headTitle :isRight="true">
			<view slot="content">智慧仓储</view>
		</headTitle>
		<!-- 宫格组件 -->
		<view class="grid-body">
			<uni-grid :column="3" :highlight="true" @change="change" :showBorder="false"  :square="false">
				<uni-grid-item v-for="(item, index) in list" :index="index" :key="index">
					<view class="grid-item-box" style="background-color: #fff;">
						<image style="width: 119rpx;height: 119rpx;" :src="item.image"></image>
						<text class="text">{{item.name}}</text>
					</view>
				</uni-grid-item>
			</uni-grid>
			<!-- <uni-grid :column="3" :showBorder="false">
				<uni-grid-item v-for="(item,index) in list" :index="index" :key="index" @click="goClick(item)">
					<view class="grid-item-box">
						<image style="width: 66px;height: 66px;" :src="item.image"></image>
						<text class="text">{{item.name}}</text>
					</view>
				</uni-grid-item>
			</uni-grid> -->
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [{
						image: '../../static/images/equipment/index1.png',
						name: "巡检任务",
						id: '1'
					},
					{
						image: '../../static/images/equipment/index2.png',
						name: "保养任务",
						id: '2'
					},
					{
						image: '../../static/images/equipment/index3.png',
						name: "维修任务",
						id: '3'
					}
				]
			}
		},
		methods: {
			change(item){
				if(item.detail.index == 0){
					console.log("1111")
					uni.navigateTo({
						url: '/pages/equipment/patrol'
					});
				}else if (item.detail.index == 1){
					uni.navigateTo({
						url: '/pages/equipment/maintain'
					});
				}else{
					uni.navigateTo({
						url: '/pages/equipment/service'
					});
				}
			}
		}
	}
</script>

<style lang="scss">
	/* #ifndef APP-NVUE */
	page {
		display: flex;
		flex-direction: column;
		box-sizing: border-box;
		background-color: #fff;
		min-height: 100%;
		height: auto;
	}

	view {
		font-size: 28rpx;
		line-height: inherit;
	}

	/* #endif */

	.text {
		font-size: 39rpx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #0B0B0B;
		text-align: center;
		margin-top: 20rpx;
	}

	.grid-item-box {
		flex: 1;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 30rpx 0;
	}

	.uni-margin-wrap {
		width: 1380rpx;
		width: 100%;
		;
	}

	.swiper {
		height: 600rpx;
	}

	.swiper-box {
		height: 300rpx;
	}

	.swiper-item {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		justify-content: center;
		align-items: center;
		color: #fff;
		height: 600rpx;
		line-height: 600rpx;
	}

	@media screen and (min-width: 500px) {
		.uni-swiper-dot-box {
			width: 400px;
			/* #ifndef APP-NVUE */
			margin: 0 auto;
			/* #endif */
			margin-top: 8px;
		}

		.image {
			width: 100%;
		}
	}
</style>