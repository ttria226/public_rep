<template>
	<view>
		<headTitle :isRight="true">
			<view slot="content">巡检任务</view>
		</headTitle>
		<view class="content">
			<card :dataList="dataList" :total="total" :state="state" v-if="dataList && dataList.length > 0"></card>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0"></u-empty>
		</view>
	</view>
</template>

<script>
	import { getPollingList } from "@/api/equipment"
	import card from "@/pages/equipment/subassembly/card.vue";
	export default {
		components: {
			card
		},
		data() {
			return {
				dataList: [],
				total:0,
				state: 1,
				queryParams:{
					pageSize:10,
					pageNum:1,
				},
			}
		},
		onShow() {
			this.getPollingListApi()
		},
		onReachBottom(){
			if(this.dataList.length<this.total){
				this.queryParams.pageNum++
				this.getPollingListApi()
			}
		},
		methods: {
			//刷新
			reFresh(){
				this.queryParams.pageNum = 1
				this.getPollingListApi('load')
			},
			getPollingListApi(type) {
				getPollingList(this.queryParams).then(res => {
					console.log(res)
					if(this.dataList.length < res.total){
						this.dataList = [...this.dataList,...res.rows]
					} else if(this.dataList.length > res.total || type == 'load'){
						this.dataList = res.rows
					}
					this.total=res.total
				})
			}
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style scoped lang="scss">
	/deep/ .u-empty{
		height: 90% !important;
	}
	.content {
		width: 100%;
		height: calc(100vh - 104rpx - 50rpx);
		padding: 18rpx 22rpx;
		background: #F5F5F5;
		box-sizing: border-box;

		.wapper {
			margin: 16rpx 0;
			position: relative;
			width: 100%;
			height: 414rpx;
			background: #FFFFFF;
			border-radius: 16rpx;

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
					width: 308rpx;
					height: 36rpx;
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #1948B2;
				}

			}

			.centre {
				margin: 34rpx 0px 40rpx 26rpx;
				width: 100%;
				height: 144rpx;
				font-size: 36rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #707072;
				line-height: 28rpx;

				view {
					margin: 20rpx 0;
				}

				.centre_fontSize {
					color: black;
					font-weight: 700;
				}
			}

			.button {
				margin: 0px 24rpx !important;
			}
		}
	}
</style>