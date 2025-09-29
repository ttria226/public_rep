<template>
	<view>
		<view class="tap">
			<view class="tap_title">
				<text>
					设备名称：
				</text>
				<text>
					{{dataList.name}}
				</text>
			</view>
			<view class="tap_bottom" v-if="state == 1">
				<view class="bottom_item">
					<text>设备编号：</text>
					<text>{{dataList.equNo}}</text>
				</view>
				<view class="bottom_item">
					<text>序列号：</text>
					<text>{{dataList.serialNo}}</text>
				</view>
				<view class="bottom_item">
					<text>安装地点：</text>
					<text>{{dataList.functionLocation}}</text>
				</view>
				<view class="bottom_item">
					<text>巡检标准：</text>
					<text>{{dataList.content}}</text>
				</view>
			</view>
			<view class="tap_bottom1" v-else>
				<view class="bottom_item">
					<text>设备编号：</text>
					<text>{{dataList.equNo}}</text>
				</view>
				<view class="bottom_item">
					<text>序列号：</text>
					<text>{{dataList.serialNo}}</text>
				</view>
				<view class="bottom_item">
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
			state:{
				type:Number,
				default:""
			}
		},
		data() {
			return {
				
			}
		},
		methods: {}
	}
</script>

<style lang="scss" scoped>
	.tap {
		box-sizing: border-box;
		padding:30rpx;
		background-size: 100% 100%;
		background-image: url("../../../static/images/equipment/updatebackground.png");
	
		.tap_title {
			// width: 205px;
			font-size: 38rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #FFFFFF;
			height: 70rpx;
			line-height: 70rpx;
		}
	
		.tap_bottom {
			font-size: 32rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
		}
		.tap_bottom1 {
			padding-top: 30rpx;
			font-size: 34rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			> view {
				margin: 16rpx 0rpx 4rpx 0rpx;
			}
		}
	}
</style>