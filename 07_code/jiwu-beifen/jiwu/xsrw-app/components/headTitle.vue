<template>
	<view >
		<view class="titleContent" :style="[{height:CustomBar + 'px'}]">
			<view :style="style" class="left"  @click="goBack" v-if="!isBack">
				<slot name="backText">
					<u-icon name="arrow-left" style="color: #fff;" size="36"></u-icon>
				</slot>
			</view>
			<view :style="style"  class="left" v-else></view>
			<view class="content-hederTitle">
				<slot name="content">
					<view>{{title}}</view>
				</slot>
			</view>
			<view class="action" @click="goPage" v-if="!isRight">
				<slot name="right">
					<view>首页</view>
				</slot>
			</view>
			<view v-else style="width: 80rpx;"></view>
		</view>
		<view class="zhanwei" :style="[{height:CustomBar+'px'}]"></view>
	</view>
</template>

<script>
	export default {
		props: {
			title:{
				type:String,
				default:"页面标题"
			},
			isBack: {
				type: Boolean,
				default: false
			},
			isRight: {
				type: Boolean,
				default:false
			},
		},
		name: "headTitle",
		data() {
			return {
				StatusBar: this.StatusBar,
				CustomBar: this.CustomBar
			};
		},
		computed: {
			style() {
				var StatusBar= this.StatusBar;
				var CustomBar= this.CustomBar;
				var bgImage = this.bgImage;
				var style = `height:${CustomBar}px;padding-top:${StatusBar}px;`;
				return style
			}
		},
		methods: {
			goBack(){
				uni.navigateBack({
					delta:1
				})
			},
			goPage(){
				uni.reLaunch({
					url:'/pages/inlet'
				})
			},
		}
	}
</script>

<style scoped lang="scss">
	.zhanwei{
		width: 100%;
	}
	.titleContent {
		padding:0 30rpx;
		position: fixed;
		top: 0;
		background-color: #0075FF;
		width: 100%;
		height: 100rpx;
		display: flex;
		align-items: center;
		z-index: 99999999 !important;
		
		.left{
			width: 80rpx;
		}
		.content-hederTitle {
			flex: 1;
			text-align: center;
			font-size: 42rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
		}
		.action{
			width: 80rpx;
			font-size: 36rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 42rpx;
		}
	}
</style>