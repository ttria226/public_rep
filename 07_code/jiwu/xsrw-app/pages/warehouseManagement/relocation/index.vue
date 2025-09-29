<template>
	<view>
		<headTitle>
			<view slot="content">{{title}}</view>
		</headTitle>
		<header>
			<view class="header_tap">
				<text>物料编码</text>
				<u-search shape="square" placeholder="请输入物料编码" v-model="queryParams.code" @custom="searchBank"
					@search="searchBank"></u-search>
			</view>
		</header>
		<view class="content">
			<view class="navTitle_All">
				<view class="navTitle" v-for="(item,index) in navList" :key="index">
					<view :class="{'active':isActive === item.index}" @click="checked(item.index)">
						{{item.title}}
					</view>
				</view>
			</view>
			<view class="content_cart" v-for="(item,index) in dataList" :key="index">
				<view>
					任务编号：{{item.taskNo}}
				</view>
				<view>
					载具编号：{{item.trayCode}}
				</view>
				<view>
					原库位：{{item.startPosition}}
				</view>
				<view>
					目标库位：{{item.purposePosition}}
				</view>
				<view>
					状态：{{transfrom1(item.taskStatus)}}
				</view>
				<button class="button" type="primary" @click="goDetail(item.id)">详情</button>
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
				title: '移库单',
				page: '1', // 1：组盘， 2：快捷入库
				getDicts,
				navList: [{
						index: 1,
						title: '未执行'
					}, {
						index: 3,
						title: "已完成"
					}
				],
				isActive: 1,
				dataList: [],
				total: 0,
				queryParams: {
					taskStatus: 1,
					taskType: 5,
					code: null,
					pageSize: 10,
					pageNum: 1,
				},
				option: [],
				option1: [],
			}
		},
		async onLoad(opt) {
			this.getApi()
			await this.getDicts("in_delivery_type").then(res => {
				this.option = res.data
			})
			await this.getDicts("wcs_excute_status").then(res => {
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
				if (index != this.isActive) {
					this.isActive = index
					this.queryParams.taskStatus = index
					this.dataList = []
					this.queryParams.pageNum = 1
					this.getApi()
				}
			},
			getApi(type) {
				this.$http.getAction("/wms/api/task/wcs/getTaskList", this.queryParams).then(res => {
					if(this.dataList.length < res.total){
						this.dataList = [...this.dataList,...res.rows]
					} else if(this.dataList.length > res.total || type == 'load'){
						this.dataList = res.rows
					}
					this.total = res.total
				})
			},
			goDetail(id) {
				// uni.navigateTo({
				// 	url: './detail?info=' + encodeURIComponent(JSON.stringify(item))
				// });
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
			// height: 414rpx;
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
				// position: absolute;
				// right: 20rpx;
				// bottom: 20rpx;
				margin: 0 0 0 auto;
				width: 200rpx !important;
			}
		}
	}
</style>