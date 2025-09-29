<template>
	<view>
		<headTitle :isRight="true">
			<view slot="content">巡检详情</view>
		</headTitle>
		<view class="content">
			<view class="tap">
				<view class="tap_title">
					{{tapList.planName}}
				</view>
				<view class="tap_bottom">
					<view class="centre">
						<view class="centre-item">
							<text class="centre_dian"></text>
							<text>计划巡检路线：</text>
							<text class="centre_fontSize">{{tapList.inspectionLine}}</text>
						</view>
						<view class="centre-item">
							<text class="centre_dian"></text>
							<text>巡检人：</text>
							<text class="centre_fontSize"> {{tapList.inspectorName}}</text>
						</view>
						<view class="centre-item">
							<text class="centre_dian"></text>
							<text>巡检时间：</text>
							<text class="centre_fontSize">{{tapList.day}}</text>
						</view>
					</view>
				</view>
			</view>
			<view class="bottom">
				<view class="bottom_title">
					设备信息
				</view>
				<cardDetail :dataList="dataList" :state="state" :dayId="queryParams.dayid"></cardDetail>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		getPollingDetail
	} from "@/api/equipment"
	import cardDetail from "@/pages/equipment/subassembly/cardDetail";
	export default {
		components: {
			cardDetail
		},
		data() {
			return {
				//接受的数据
				tapList: [],
				queryParams: {
					pageSize: 10,
					pageNum: 1,
					planid: "",
					dayid: "",
				},
				total: 0,
				dataList: [],
				state: 2,
			}
		},
		onLoad(option) {
			if (option.info != undefined) {
				let userInfo = JSON.parse(decodeURIComponent(option.info));
				this.tapList = userInfo
				this.queryParams.planid = userInfo.planId
				this.queryParams.dayid = userInfo.id
			}
		},
		onReachBottom() {
			if (this.dataList.length < this.total) {
				this.queryParams.pageNum++
				this.getPollingDetailApi()
			}
		},
		created() {
			this.getPollingDetailApi()
		},
		methods: {
			//刷新
			reFresh(){
				this.queryParams.pageNum = 1
				this.getPollingDetailApi('load')
			},
			getPollingDetailApi(type) {
				getPollingDetail(this.queryParams).then(res => {
					if(this.dataList.length < res.total){
						this.dataList = [...this.dataList,...res.rows]
					} else if(this.dataList.length > res.total || type == 'load'){
						this.dataList = res.rows
					}
					let finishNum = 0
					this.dataList.map((info) => {
						if(info.status != 2){
							finishNum++
						}
					})
					this.total = res.total
					if(finishNum === res.total){
						let pages = getCurrentPages(); // 当前页面
						let beforePage = pages[pages.length - 2]; // 上一页
						uni.navigateBack({
						    success: function() {
						        // 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
						        beforePage.$vm.reFresh();
						    }
						})
					}
					this.$forceUpdate()
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
<style lang="scss" scoped>
	body {
		margin: 0;
		padding: 0;
	}

	.content {
		width: 100%;
		height: 100%;
		// padding: 9px 11px;
		background: #F5F5F5;

		.tap {
			width: 100%;
			min-height: 300rpx;
			background-size: 100% 100%;
			background-image: url("../../static/images/equipment/detailbackground.png");

			.tap_title {
				// width: 173px;
				padding: 30rpx 0px 0px 28rpx;
				height: 40rpx;
				font-size: 42rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #FFFFFF;
				line-height: 28rpx;
			}

			.tap_bottom {
				margin-top: 32rpx;
				display: flex;

				.centre {
					width: 100%;
					min-height: 144rpx;
					font-size: 32rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #FFFFFF;
					.centre-item{
						line-height: 1.5;
						padding: 0 30rpx;
					}

					> view {
						margin: 20rpx 0px;
					}

					.centre_dian {
						margin-right: 20rpx;
						display: inline-flex;
						width: 10rpx;
						height: 10rpx;
						background: #FFFFFF;
						border-radius: 50%;
					}
				}
			}
		}

		.bottom {
			width: 100%;
			height: 100%;
			padding: 18rpx 22rpx;
			background: #F5F5F5;
			box-sizing: border-box;

			.bottom_title {
				// width: 81px;
				font-size: 38rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #0B0B0B;
			}

			.wapper {
				margin: 16rpx 0;
				position: relative;
				width: 100%;
				min-height: 414rpx;
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
				}
			}

		}
	}
</style>