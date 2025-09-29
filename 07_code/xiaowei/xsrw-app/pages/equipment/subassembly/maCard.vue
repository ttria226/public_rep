<template>
	<view class="content">
		<view class="wapper" :style="{'min-height':item.status == 3 ? '302rpx' : '414rpx'}" v-for="item in dataList">
			<view :class="[item.status == 3 ? 'wapper_start1' : 'wapper_start']">
				<view class="wapper_start_fontSize">
					<text v-if="item.status == 3">已完成</text>
					<text v-else>未开始</text>

				</view>
			</view>

			<view class="title">
				<view class="title_left"></view>
				<view class="title_right">
					设备名称:{{item.equName}}
				</view>
			</view>
			<view class="centre">
				<view class="centre-item">
					<text>设备编号：</text>
					<text class="centre_fontSize">{{item.equNo}}</text>
				</view>
				<view class="centre-item">
					<text v-if="skip == 1">保养人：</text>
					<text v-if="skip == 2">维修人：</text>
					<text class="centre_fontSize"> {{item.userName}}</text>
				</view>
				<view class="centre-item">
					<text>保养时间：</text>
					<text class="centre_fontSize">{{item.planDay}}</text>
				</view>
			</view>
			<button class="button" v-if="item.status != 3 &&skip == 1" type="primary" @click="goDetail(item)">开始保养</button>
			<button class="button" v-if="item.status != 3 &&skip == 2" type="primary" @click="goDetail(item)">开始维修</button>
			<view v-else>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			//父组件传过来的数据
			dataList: {
				type: Array,
				default: ""
			},
			//父组件传过来的状态,来判断下面的按钮显示
			state: {
				type: Number,
				default: ""
			},
			skip: {
				type: Number,
				default: ""
			}
		},
		data() {
			return {}
		},
		methods: {
			goDetail(item) {
				if (this.$props.skip == 1) {
					uni.navigateTo({
						url: '/pages/equipment/maintainDetail?info=' + encodeURIComponent(JSON.stringify(item))
					});
				} else if (this.$props.skip == 2) {
					uni.navigateTo({
						url: '/pages/equipment/serviceDetail?info=' + encodeURIComponent(JSON.stringify(item))
					});
				}

			}
		}
	}
</script>

<style lang="scss" scoped>
	.content {
		width: 100%;
		height: 100%;
		// padding: 18rpx 22rpx;
		background: #F5F5F5;
		box-sizing: border-box;

		.wapper {
			box-sizing: border-box;
			margin: 16rpx 0rpx;
			position: relative;
			width: 100%;
			min-height: 414rpx;
			background: #FFFFFF;
			border-radius: 16rpx;
			padding-bottom: 20rpx;

			.wapper_start {
				position: absolute;
				right: 0px;
				width: 138rpx;
				height: 74rpx;
				background: #FFECDD;
				border-radius: 16rpx;
				display: flex;
				align-items: center;
				justify-content: center;

				.wapper_start_fontSize {
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #FF7D14;
				}
			}

			.wapper_start1 {
				position: absolute;
				right: 0px;
				width: 138rpx;
				height: 74rpx;
				background: #EAEAEA;
				border-radius: 16rpx;
				display: flex;
				align-items: center;
				justify-content: center;

				.wapper_start_fontSize1 {
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					background: #EAEAEA;
				}
			}

			.title {
				padding-top: 44rpx;
				display: flex;

				.title_left {
					margin-left: 4rpx;
					width: 6rpx;
					height: 40rpx;
					background: #1948B2;
					border-radius: 4rpx;
				}

				.title_right {
					margin-left: 24rpx;
					// width: 154px;
					height: 36rpx;
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #1948B2;
				}

			}

			.centre {
				// margin: 34rpx 0px 40rpx 26rpx;
				// width: 100%;
				// height: 144rpx;
				font-size: 36rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #707072;
				padding: 0 30rpx;
				// line-height: 28rpx;
				.centre-item{
					line-height: 1.5;
				}
				> view {
					margin: 20rpx 0px;
				}

				.centre_fontSize {
					color: black;
					font-weight: 700;
				}
			}

			.wapper_bottom {
				display: flex;
				justify-content: space-between;

				.button {
					width: 322rpx;
					background: #0075FF;
					border-radius: 8rpx;
					margin: 0px 24rpx !important;
				}

				.button1 {
					width: 322rpx;
					background: #29C013;
					border-radius: 8rpx;
					margin: 0px 24rpx !important;
				}
			}

			.button {
				margin: 0px 24rpx !important;
			}
		}
	}
</style>