<template>
	<view class="reservation">
		<headTitle>
			<view slot="content">地堆上架</view>
		</headTitle>
		<!-- <header>
			<view class="header_tap">
				<text>预约单号</text>
				<u-search shape="square" placeholder="请输入预约单号" v-model="queryParams.code" @custom="searchBank"
					@search="searchBank"></u-search>
			</view>
		</header> -->
		<view class="content">
			<!-- <view class="navTitle_All">
				<view class="navTitle" v-for="(item,index) in navList" :key="index">
					<view :class="{'active':isActive === index}" @click="checked(index)">
						{{item.title}}
					</view>
				</view>
			</view> -->
			<view class="content_cart" v-for="(item,index) in dataList" :key="index">
				<view>
					预约单号：{{item.advanceDeliveryCode}}
				</view>
				<view>
					物料编码：{{item.materialCode}}
				</view>
				<view>
					物料名称：{{item.materialName}}
				</view>
				<view class="cart_wapper">
					<view>
						计量单位：{{item.unitName}}
					</view>
					<view>
						单价：{{item.price}}
					</view>
				</view>
				<view class="cart_wapper">
					<view>
						批号：{{item.batchCode}}
					</view>
					<view>
						预计数量：{{item.detectionCount}}
					</view>
				</view>
				<button class="button" type="primary" @click="goexecuteDetail(item.id)">详情</button>
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
		</view>
	</view>
</template>

<script>
	import {
		getDicts
	} from "@/api/system/data";
	export default {
		data() {
			return {
				getDicts,
				navList: [{
						index: 0,
						title: '未完成'
					}, {
						index: 1,
						title: "部分完成"
					},
					{
						index: 2,
						title: "已完成"
					}
				],
				isActive: 0,
				dataList: [],
				total: 0,
				queryParams: {
					// completeState: 1,
					code: null,
					pageSize: 10,
					pageNum: 1,
				},
				option: [],
				option1: [],
			}
		},
		async onLoad(opt) {
			this.queryParams.inStatus = opt.state
			this.getApi()
			await this.getDicts("in_delivery_type").then(res => {
				this.option = res.data
			})
			await this.getDicts("in_delivery_status").then(res => {
				this.option1 = res.data
			})
		},
		// onShow(){
		// 	uni.$on('refresh', (data) => {
		// 		if (data.refresh) {
		// 			this.queryParams.pageNum = 1
		// 			this.getApi()
		// 		}
		// 	});
		// },
		// onUnload(){
		// 	uni.$off('refresh');
		// },
		onReachBottom() {
			if (this.dataList.length < this.total) {
				this.queryParams.pageNum++
				this.getApi()
			}
		},
		methods: {
			//刷新
			reFresh(){
				this.queryParams.pageNum = 1
				this.getApi('load')
			},
			//下拉框
			searchBank(val) {
				this.dataList = []
				this.queryParams.pageNum = 1
				this.queryParams.code = val
				this.getApi()
			},
			transfrom(e) {
				let dictLabel = ''
				this.option.map(res => {
					if (res.dictValue == e) {
						dictLabel = res.dictLabel
						return
					}
				})
				return dictLabel
			},
			transfrom1(e) {
				let dictLabel = ''
				this.option1.map(res => {
					if (res.dictValue == e) {
						dictLabel = res.dictLabel
						return
					}
				})
				return dictLabel
			},
			checked(index) {
				this.isActive = index
				this.queryParams.completeState = index + 1
				this.dataList = []
				this.queryParams.pageNum = 1
				this.getApi()
			},
			getApi(type) {
				this.$http.getAction("/wms/api/inout/delivery/floorList", this.queryParams).then(res => {
					if(this.dataList.length < res.total){
						this.dataList = [...this.dataList,...res.rows]
					} else if(this.dataList.length > res.total || type == 'load'){
						this.dataList = res.rows
					}
					this.total = res.total
				})
			},
			goexecuteDetail(id) {
				uni.navigateTo({
					url: './detail?id=' + id
				});
				// uni.navigateTo({
				// 	url: `/pages/warehouseManagement/godownEntry/detail?id=${item.id}`
				// });
			}
		}
	}
</script>

<style lang="scss">
	page,
	body {
		height: 100%;
	}
	.reservation{
		min-height: 100%;
	}
	header {
		display: flex;
		align-items: center;
		width: 100%;
		height: 104rpx;
		background: #FFFFFF;

		.header_tap {
			display: flex;
			align-items: center;
			margin-left: 24rpx;
			width: 688rpx;
			height: 92rpx;
			background: #F4F6F8;
			border-radius: 4rpx;

			text {
				// width: 72rpx;
				// height: 36rpx;
				margin: 0px 20rpx;
				font-size: 36rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #0B0B0B;
			}
		}
	}

	.content {
		padding: 30rpx 30rpx;
		width: 100%;
		height: calc(100vh - 60rpx - 104rpx - 100rpx);
		background: #F5F5F5;
		box-sizing: border-box;
		overflow: scroll;

		.active {
			color: #0075FF;
		}

		.navTitle_All {
			display: flex;
			align-items: center;
			justify-content: center;
			height: 115rpx;
			background: #FFFFFF;

			.navTitle {
				width: 50%;
				text-align: center;
			}
		}

		.content_cart {
			position: relative;
			// width: 785rpx;
			min-height: 414rpx;
			background: #FFFFFF;
			border-radius: 15rpx;
			padding: 30rpx 30rpx;
			margin: 20rpx 0rpx;

			view {
				// width: 729rpx;
				// height: 94rpx;
				margin: 10rpx 0rpx;
				font-size: 35rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #0B0B0B;
				// line-height: 30rpx;
			}

			.button {
				position: absolute;
				right: 20rpx;
				bottom: 20rpx;
				width: 200rpx !important;
			}
		}
	}
</style>