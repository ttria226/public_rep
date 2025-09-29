<template>
	<view>
		<view class="tap">
			<view class="tap_title">
				<text>
					设备名称：
				</text>
				<text>
					{{dataList.equName}}
				</text>
			</view>
			<view class="tap_bottom" v-if="state == 1">
				<view>
					<text>设备编号：</text>
					<text>{{dataList.equNo}}</text>
				</view>
				<view>
					<text v-if="skip == 1">保养人：</text>
					<text v-if="skip == 2">维修人：</text>
					<text>{{dataList.userName}}</text>
				</view>
				<view>
					<text v-if="skip == 1">保养时间：</text>
					<text v-if="skip == 2">维修时间</text>
					<text>{{dataList.planDay}}</text>
				</view>
			</view>
			<view class="tap_bottom1" v-else>
				<view>
					<text>设备编号：</text>
					<text>{{dataList.equNo}}</text>
				</view>
				<view>
					<text>序列号：</text>
					<text>{{dataList.serialNo}}</text>
				</view>
				<view>
					<text>安装地点：</text>
					<text>{{dataList.functionLocation}}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			//父组件传过来的数据
			dataList: {
				type: Object,
				default: ""
			},
			//父组件传过来的状态
			state: {
				type: Number,
				default: ""
			},
			skip: {
				type: Number,
				default: ""
			},
		},
		data() {
			return {

			}
		},
		methods: {}
	}
</script>

<style lang="scss">
	.tap {
		box-sizing: border-box;
		padding: 0px 26rpx;
		background-size: 100% 100%;
		background-image: url("../../../static/images/equipment/updatebackground.png");
		width: 100%;
		height: 312rpx;

		.tap_title {
			// width: 205px;
			padding-top: 28rpx;
			height: 40rpx;
			font-size: 42rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #FFFFFF;
		}

		.tap_bottom {
			padding-top: 44rpx;
			font-size: 34rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;

			view {
				margin: 4rpx 0px 4rpx 0px;
			}
		}

		.tap_bottom1 {
			padding-top: 44rpx;
			font-size: 34rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;

			view {
				margin: 16rpx 0rpx 4rpx 0rpx;
			}
		}
	}
</style>