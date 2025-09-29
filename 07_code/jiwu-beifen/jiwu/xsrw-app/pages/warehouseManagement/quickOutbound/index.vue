<template>
	<view>
		<headTitle>
			<view slot="content">出库单</view>
		</headTitle>
		<header>
			<view class="header_tap">
				<text>出库单号</text>
				<u-search shape="square" placeholder="请输入出库单号" v-model="queryParams.code" @custom="searchBank"
					@search="searchBank"></u-search>
			</view>
		</header>
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
					出库单号：{{item.code}}
				</view>
				<view>
					类型：
					{{transfrom(item.type)}}
				</view>
				<view>
					申请部门：{{item.deptName}}
				</view>
				<view>
					状态：{{transfrom1(item.status)}}
				</view>
				<button class="button" type="primary" @click="goDetail(item)">执行出库</button>
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
		</view>
	</view>
</template>

<script>
	import { getDicts } from "@/api/system/data";
	export default {
		data() {
			return {
				getDicts,
				navList: [
					{
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
					status: '2',
					code: null,
					pageSize: 10,
					pageNum: 1,
				},
				option: [],
				option1: [],
			}
		},
		async onLoad() {
			this.getApi()
			await this.getDicts("inout_out_type").then(res => {
				this.option = res.data
			})
			await this.getDicts("inout_out_status").then(res => {
				this.option1 = res.data
			})
		},
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
				this.queryParams.code = val
				this.queryParams.pageNum = 1
				this.dataList = []
				this.$nextTick(() => {
					this.getApi()
				});
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
				// this.queryParams.completeState = index + 1
				this.dataList = []
				this.queryParams.pageNum = 1
				this.getApi()
			},
			getApi(type) {
				this.$http.getAction("/wms/deliveryOut/quick/list", this.queryParams).then(res => {
					console.log(res)
					if (this.dataList.length < res.total) {
						this.dataList = [...this.dataList, ...res.rows]
					} else if (this.dataList.length > res.total || type == 'load'){
						this.dataList = res.rows
					}
					this.total = res.total
				})
			},
			goDetail(item) {
				uni.navigateTo({
					url: './execute?info=' + encodeURIComponent(JSON.stringify(item))
				});
				// uni.navigateTo({
				// 	url: `/pages/warehouseManagement/godownEntry/detail?id=${item.id}`
				// });
			}
		}
	}
</script>

<style lang="scss">
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
		height: calc(100vh - 40rpx - 104rpx - 100rpx);
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
			height: 414rpx;
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
				width: 220rpx !important;
			}
		}
	}
</style>