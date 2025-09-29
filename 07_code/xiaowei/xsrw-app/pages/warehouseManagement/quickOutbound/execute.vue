<template>
	<view class="app">
		<headTitle>
			<view slot="content">{{title}}</view>
		</headTitle>
		<view class="content">
			<view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList" :key="index"><!-- @click="checked(index,item)"-->
				<view class="cart_wapper">
					<view style="width: 400rpx;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">载具编号：{{item.trayCode}}</view>
					<view>物料编码：{{item.materialCode}}</view>
				</view>
				<view class="cart_wapper">
					<view style="width: 400rpx;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">物料名称：{{item.materialName}}</view>
					<view>库存数量：{{item.availableCount}}</view>
				</view>
				<view class="cart_wapper">
					<view style="width: 400rpx;">实际拣货数量：{{item.predictCount}}</view>
					<view>批次号：{{item.batchCode}}</view>
				</view>
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
		</view>
		<button class="button" type="primary" v-if="dataList.length > 0" @click="goSubmit(dataList)">提交</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				indexCheck: null,
				dataList: [],
				total: 0,
				queryParams: {
					outDeliveryId: null,
					// materialId: null,
					// pageSize: 10,
					// pageNum: 1,
				},
				from: {
					outDeliveryDetailId: "",
					taskDetailList: [], //{ stockId: "", trayId: "", locationId: "" }
				},
				detailInfo: {},
				title: '执行出库',
			}
		},
		onLoad(option) {
			if (option.info) {
				let userInfo = JSON.parse(decodeURIComponent(option.info));
				this.detailInfo = userInfo
				this.queryParams.outDeliveryId = userInfo.id
				this.getApi()
			}
		},
		// onReachBottom() {
		// 	if (this.dataList.length < this.total) {
		// 		this.queryParams.pageNum++
		// 		this.getApi()
		// 	}
		// },
		methods: {
			checked(val, item) {
				console.log(item)
				this.from.tTaskOutDetailListVOS[0].stockId = item.stockid
				this.from.tTaskOutDetailListVOS[0].trayId = item.id
				this.from.tTaskOutDetailListVOS[0].locationId = item.locationId
				this.from.tTaskOutDetailListVOS[0].predictCount = item.predictCount
				this.indexCheck = val
			},
			getApi() {
				this.$http.getAction('/wms/deliveryOut/quick/traylist/voluntarily/show', this.queryParams).then(res => {
					console.log(res)
					let list = []
					if(res.data && res.data.length > 0){
					  res.data.map((item) => {
						let info = {
						  outDeliveryDetailId: this.detailInfo.id,
						  trayCode: item.code ? item.code : "",
						  trayId: item.id ? item.id : "",
						  stockId: item.stockid ? item.stockid : "",
						  locationId: item.locationId ? item.locationId : "",
						  locationName: item.locationname ? item.locationname : "",
						  materialCode: item.materialCode,
						  materialId: item.materialId,
						  materialName: item.materialName,
						  batchCode: item.batchCode,
						  availableCount: item.availableCount ? item.availableCount : 0,
						  predictCount: item.predictCount ? item.predictCount : 0,
						}
						list.push(info)
					  })
					}
					this.$nextTick(() => {
					  this.dataList = list
					  this.$forceUpdate()
					})
				})
			},
			goSubmit(item) {
				if(this.dataList.length == 0){
					this.$modal.msgError('请选择载具！')
					return
				}
				this.$http.getAction('/wms/deliveryOut/quick/traylist/voluntarily/submit', this.queryParams).then(res => {
					if(res.data){
						this.$http.postAction('/wms/deliveryOut/quick/execute', res.data).then(response => {
							uni.showToast({
								icon: 'success',
								title: '提交成功'
							});
							setTimeout(() => {
								// this.$tab.switchTab("/pages/warehouseManagement/warehouseManagement");
								let pages = getCurrentPages(); // 当前页面  
								let beforePage = pages[pages.length - 2]; // 上一页
								uni.navigateBack({
								    success: function() {
								        // 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
								        beforePage.$vm.reFresh();
								    }
								})
							}, 2000)
						})
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	.app {
		width: 100%;
		height: 100vh;
		position: relative;
		overflow: scroll;

		.content {
			width: 100%;
			height: 90%;
			padding: 30rpx 30rpx;
			background: #F5F5F5;
			box-sizing: border-box;
			overflow: scroll;

			.content_cart {
				position: relative;
				// height: 380rpx;
				background: #FFFFFF;
				border-radius: 15rpx;
				padding: 30rpx 30rpx;
				margin: 20rpx 0rpx;

				.cart_wapper {
					margin: 30rpx 0rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;

					view {
						width: 50%;
					}
				}
				.bottom1 {
					margin: 0;
					margin-left: auto;
					width: 210rpx;
					// position: absolute;
					// right: 30rpx;
				}
			}

			.content_cart1 {
				position: relative;
				// height: 380rpx;
				background: gray;
				border-radius: 15rpx;
				padding: 30rpx 30rpx;
				margin: 20rpx 0rpx;

				.cart_wapper {
					margin: 30rpx 0rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;

					view {
						width: 50%;
					}
				}

				.bottom1 {
					width: 210rpx;
					// position: absolute;
					// right: 30rpx;
				}
			}
		}

		.button {
			margin: 30rpx 30rpx;
			position: absolute;
			width: 92%;
			bottom: 10rpx;
		}
	}
</style>