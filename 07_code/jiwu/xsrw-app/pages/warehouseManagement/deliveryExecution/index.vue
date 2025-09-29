<template>
	<view>
		<headTitle>
			<view slot="content">{{title}}</view>
		</headTitle>
		<header v-if="page == '1'">
			<view class="header_tap">
				<text>物料编码</text>
				<u-search shape="square" placeholder="请输入物料编码" v-model="queryParams.materialCode" @custom="searchBank"
					@search="searchBank"></u-search>
			</view>
		</header>
		<view class="content" style="height: 100%;">
			<view :class="{ 'choose': page == '2', 'content_cart': true }" v-for="(item,index) in dataList"
				:key="index">
				<view v-if="page == '1'">出库单号：{{item.originCode}}</view>
				<view v-if="page == '1'">物料编码：{{item.materialCode}}</view>
				<view v-if="page == '1'">物料名称：{{item.materialName}}</view>
				<view v-if="page == '1'">预计拣货数量：{{item.predictCount}}</view>
				<!-- <view v-if="page == '1'">小件领取数量：{{item.smallPredictCount}}</view> -->
				<view v-if="page == '2'">任务编号：{{item.taskNo}}</view>
				<view v-if="page == '2'">载具编号：{{item.trayCode}}</view>
				<view v-if="page == '2'">目标库位：{{item.locationName}}</view>
				<view v-if="page == '2'">状态：{{transfrom1(item.taskStatus)}}</view>
				<button class="button" type="primary" v-if="page == '1'"
					@click="goexecuteDetail(item,'2')">地堆拣货</button>
				<button class="button" v-if="item.taskStatus != '3'" type="primary"
					@click="goexecuteDetail(item,'1')">{{btnText}}</button>
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
				dataList: [],
				total: 0,
				queryParams: {
					materialCode: null,
					pageSize: 10,
					pageNum: 1,
				},
				page: '',
				title: '出库执行',
				btnText: '执行',
				option1: [],
			}
		},
		async onLoad(opt) {
			if (opt.page) {
				if (opt.page == '1') {
					this.page = '1'
					this.title = '出库分配'
					this.btnText = '分配'
				}
				if (opt.page == '2') {
					this.page = '2'
					this.title = '出库拣选'
					this.btnText = '拣货'
				}
			}
			await this.getDicts("wcs_excute_status").then(res => {
				this.option1 = res.data
			})
			this.getApi()
		},
		onReachBottom() {
			console.log('tttt')
			if (this.dataList.length < this.total) {
				this.queryParams.pageNum++
				this.getApi()
			}
		},
		methods: {
			//刷新
			reFresh() {
				this.queryParams.pageNum = 1
				this.getApi('load')
			},
			//下拉框
			searchBank(val) {
				this.queryParams.materialCode = val
				this.queryParams.pageNum = 1
				this.total = 0
				this.dataList = []
				console.log(val, "111")
				this.getApi()
			},
			getApi(type) {
				let url = this.page == '2' ? '/wms/outApi/pdaTaskChooselist' : '/wms/outApi/pdaOutTasklist'
				if (this.page == '2') {
					this.queryParams.taskType = 2
				}
				console.log(url, "url")
				this.$http.getAction(url, this.queryParams).then(res => {
					console.log(66666, res)
					if (this.dataList.length < res.total) {
						this.dataList = [...this.dataList, ...res.rows]
					} else if (this.dataList.length > res.total || type == 'load') {
						this.dataList = res.rows
					}
					this.total = res.total
				})
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
			goexecuteDetail(item, type) {
				if (this.page == '2') {
					uni.navigateTo({
						url: '../outInStore/outInStore?id=' + item.id + '&trayCode=' + item.trayCode+'&taskNo='+item.taskNo+'&locationName='+item.locationName
					});
				} else {
					if(type==1){
						uni.navigateTo({
							url: '/pages/warehouseManagement/deliveryExecution/execute?info=' + encodeURIComponent(JSON
								.stringify(item)) + '&page=' + this.page + "&type=" + type
							// url: '/pages/warehouseManagement/deliveryExecution/detail?type='+type+'&&info=' + encodeURIComponent(JSON.stringify(item)) 
						});
					}else{
						uni.navigateTo({
							url: '/pages/warehouseManagement/deliveryExecution/detail?type='+type+'&&info=' + encodeURIComponent(JSON.stringify(item)) 
						});
					}
				
				}
			}
		}
	}
</script>

<style scoped lang="scss">
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

		.content_cart {
			position: relative;
			// width: 785rpx;
			height: 504rpx;
			background: #FFFFFF;
			border-radius: 15rpx;
			padding: 30rpx 30rpx;
			margin: 20rpx 0rpx;

			&.choose {
				height: 404rpx;
			}

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
				float: right;
				margin-top: 10rpx;
				margin-left: 10rpx;
				// right: 20rpx;
				// bottom: 20rpx;
				width: 220rpx !important;
			}
		}
	}
</style>