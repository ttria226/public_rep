<template>
	<view class="reservation">
		<headTitle>
			<view slot="content">波次分拨</view>
		</headTitle>
		<header>
			<view class="header_tap">
				<text>波次单号</text>
				<u-search shape="square" placeholder="请输入波次单号" v-model="queryParams.code" @custom="searchBank"
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
					波次单号：{{item.code}}
				</view>
				<view>
					部门：{{item.deptName}}
				</view>
				<button class="button" type="primary" @click="allocation(item.id)">分拨</button>
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
				this.$http.getAction("/wms/mergeApi/list", this.queryParams).then(res => {
					if (this.dataList.length < res.total) {
						this.dataList = [...this.dataList, ...res.rows]
					} else if (this.dataList.length > res.total || type == 'load'){
						this.dataList = res.rows
					}
					this.total = res.total
				})
			},
			goexecuteDetail(item) {
				uni.navigateTo({
					url: './detail?info=' + encodeURIComponent(JSON.stringify(item))
				});
				// uni.navigateTo({
				// 	url: `/pages/warehouseManagement/godownEntry/detail?id=${item.id}`
				// });
			},
			allocation(id) {
				this.$http.getAction("/wms/mergeApi/allocate?id=" + id, {}).then(res => {
					uni.showToast({
						icon: 'success',
						title: '分拨完成'
					});
					this.reFresh()
				})
			},
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
				width: 200rpx !important;
			}
		}
	}
</style>