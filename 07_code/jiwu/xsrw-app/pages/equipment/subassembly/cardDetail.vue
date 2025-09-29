<template>
	<view>
		<view class="wapper" v-for="item in dataList">
			<view :class="[item.status == 1 ? 'wapper_start1' : 'wapper_start']">
				<view class="wapper_start_fontSize">
					<text v-if="item.status == 1">已完成</text>
					<text v-else-if="item.status == 2">未开始</text>
					<text v-else>已报修</text>
				</view>
			</view>
			<view class="title">
				<view class="title_left"></view>
				<view class="title_right">
					{{item.name}}
				</view>
			</view>
			<view class="centre">
				<view>
					<text>设备编号：</text>
					<text class="centre_fontSize">{{item.equNo}}</text>
				</view>
				<view>
					<text>序列号：</text>
					<text class="centre_fontSize"> {{item.serialNo}}</text>
				</view>
				<view>
					<text>安装地点：</text>
					<text class="centre_fontSize">{{item.functionLocation}}</text>
				</view>
			</view>
			<view class="wapper_bottom" v-if="item.status == 2">
				<button class="button" type="primary" @click="goSubmit(item,1)">巡检登记</button>
			</view>
			<view class="wapper_bottom" v-else>
				<button class="button" style="width: 100%;" type="primary" @click="goSubmit(item,3)">查看详情</button>
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
			dayId: {
				type: Number,
				default: 0
			}
		},
		data() {
			return {

			}
		},
		methods: {
			goSubmit(item,type) {
				uni.navigateTo({
					url: '/pages/equipment/patrolSubmit?info=' + encodeURIComponent(JSON.stringify(item))+'&dayId='+this.dayId+'&type='+type
				});
			}
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style lang="scss" scoped>
	.content {
		width: 100%;
		height: 100%;
		padding: 18rpx 22rpx;
		background: #F5F5F5;
		box-sizing: border-box;

		.wapper {
			box-sizing: border-box;
			margin: 16rpx 0rpx;
			position: relative;
			background: #FFFFFF;
			border-radius: 14rpx;
			padding-bottom: 30rpx;
			
			.wapper_start {
				position: absolute;
				right: 0px;
				width: 138rpx;
				height: 74rpx;
				background: #FFECDD;
				border-radius: 0rpx 0rpx 0rpx 14rpx;
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
				border-radius: 0rpx 0rpx 0rpx 14rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				background: #EAEAEA;

				.wapper_start_fontSize1 {
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					background: #EAEAEA;
				}
			}

			.title {
				display: flex;
				align-items: center;
				padding:40rpx 0 20rpx 13rpx;
								
				.title_left {
					margin-left: 4rpx;
					width: 6rpx;
					height: 36rpx;
					background: #1948B2;
					border-radius: 4rpx;
				}

				.title_right {
					font-size: 33rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #1948B2;
					margin-left: 13rpx;
				}

			}

			.centre {
				font-size: 33rpx;
				line-height: 55rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #707072;
				padding: 0 23rpx 30rpx 23rpx;
				
				.centre_fontSize {
					color: black;
					font-weight: 700;
				}
			}

			.wapper_bottom {
				display: flex;
				justify-content: center;

				.button {
					width: 90%;
					background: #0075FF;
					border-radius: 8rpx;
					margin: 0px 24rpx !important;
				}

				.button1 {
					width: 90%;
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