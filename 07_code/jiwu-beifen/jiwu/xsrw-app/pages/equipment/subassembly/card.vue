<template>
	<view class="content">
		<view class="wapper" :style="{'min-height':item.status == 1 ? '302rpx' : '414rpx'}" v-for="item in dataList">
			<view class="wapper-body" @click="goDetail(item)">
				<view :class="[item.status == 1 ? 'wapper_start1' : (item.status == 2 ? 'wapper_start2' : 'wapper_start')]">
					<view class="wapper_start_fontSize">
						<text v-if="item.status == 1">已完成</text>
						<text v-else-if="item.status == 2">进行中</text>
						<text v-else>未开始</text>
					</view>
				</view>
				<view class="title">
					<view class="title_left"></view>
					<view class="title_right">
						{{item.planName}}
					</view>
				</view>
				<view class="centre">
					<view class="centre-item">
						<text>计划巡检路线：</text>
						<text class="centre_fontSize">{{item.inspectionLine}}</text>
					</view>
					<view class="centre-item">
						<text>巡检人：</text>
						<text class="centre_fontSize"> {{item.inspectorName}}</text>
					</view>
					<view class="centre-item">
						<text>巡检时间：</text>
						<text class="centre_fontSize">{{item.day}}</text>
					</view>
				</view>
			</view>
			<button class="button" v-if="item.status != 1" type="primary" @click="goDetail(item)">{{ item.status == 2 ? '继续巡查' : '开始巡查' }}</button>
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
			total:{
				type: Number,
				default: ""
			},
			//父组件传过来的状态,来判断下面的按钮显示
			state: {
				type: Number,
				default: ""
			},
		
		},
		onLoad() {
		},
		data() {
			return {}
		},
		methods: {
			goDetail(item) {
				uni.navigateTo({
					url: '/pages/equipment/patrolDetail?info=' + encodeURIComponent(JSON.stringify(item))
				});
			},
			goUpdate(item){
				uni.navigateTo({
					url: '/pages/equipment/patrolSubmit?info=' + encodeURIComponent(JSON.stringify(item))
				});
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
			margin: 16rpx 0;
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
				border-radius:0rpx 0rpx 0rpx 14rpx;
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
				border-radius:0rpx 0rpx 0rpx 14rpx;
				display: flex;
				align-items: center;
				justify-content: center;

				.wapper_start_fontSize {
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #333333;
				}
			}
			
			.wapper_start2 {
				position: absolute;
				right: 0px;
				width: 138rpx;
				height: 74rpx;
				background: #E1F5FA;
				border-radius:0rpx 0rpx 0rpx 14rpx;
				display: flex;
				align-items: center;
				justify-content: center;
			
				.wapper_start_fontSize {
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #0098E6;
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
					margin-left: 13rpx;
					// width: 154px;
					font-size: 33rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #1948B2;
				}

			}

			.centre {
				font-size: 33rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #707072;
				// line-height: 40rpx;
				padding: 0 30rpx;
				// margin: 17px 0px 20px 13px;
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
					width: 161px;
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