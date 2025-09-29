<template>
	<view class="app">
		<headTitle>
			<view slot="content">{{title}}</view>
		</headTitle>
		<view class="content">
			<view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList" :key="index"><!-- @click="checked(index,item)"-->
				<view class="cart_wapper">
					<view style="width: 300rpx;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">载具编号：{{item.code}}</view>
					<view>载具类型：<text v-if="item.trayCategory == 1">托盘</text><text v-else-if="item.trayCategory == 2">料箱 </text><text v-else-if="item.trayCategory == 3">货笼</text></view>
				</view>
				<view class="cart_wapper">
					<view>区域：{{item.areaname}}</view>
					<view>库区：{{item.reservoirname}}</view>
				</view>
				<view class="cart_wapper">
					<view>库位：{{item.locationname}}</view>
					<view>库存数量：{{item.availableCount}}</view>
				</view>
				<view class="cart_wapper">
					<view>实际拣货数量：{{type == '1' ? item.predictCount : (type == '2' ? item.receiveCount : '')}}</view>
					<view></view>
				</view>
				<button class="bottom1" v-if="!page" type="primary" @click="goexecuteDetail(item)">地堆拣货</button>
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
		</view>
		<u-modal v-model="show" :title="showList.locationType== 0 ?'其他':'地堆'" width="90%" :show-confirm-button="false">
			<view class="slot-content">
				<uni-forms label-width="200" ref="form" :modelValue="formData" :rules="rules">
					<uni-forms-item label="预计拣货数量:">
						<view style="height: 30rpx;">{{formData.predictCount}}</view>
					</uni-forms-item>
					<!-- <uni-forms-item label="实际拣货数量">
						<uni-easyinput type="text" v-model="formData.tTaskOutDetailListVOS[0] ? formData.tTaskOutDetailListVOS[0].receiveCount : 0"
							placeholder="请输入姓名" />
					</uni-forms-item> -->
				</uni-forms>
			</view>
			<view style="display: flex;justify-content: space-between;padding: 0 20rpx 30rpx 20rpx;">
				<button style="width: 250rpx;height: 90rpx;" type="primary" @click="gonopass">确定</button>
				<button style="width: 200rpx;height: 90rpx;" @click="guanbi">取消</button>
			</view>

		</u-modal>
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
					materialId: null,
					// pageSize: 10,
					// pageNum: 1,
				},
				from: {
					outDeliveryDetailId: "",
					tTaskOutDetailListVOS: [], //{ stockId: "", trayId: "", locationId: "" }
				},
				//弹出框
				show: false,
				showList: [],
				formData: {
					outDeliveryDetailId: "",
					predictCount: "",
					tTaskOutDetailListVOS: []//{ receiveCount: "", stockId: "" }
				},
				rules: {},
				detailInfo: {},
				page: '',
				type: '',
				title: '执行',
			}
		},
		onLoad(option) {
			if (option.page && option.page == '1') {
				this.page = '1'
				this.title = '分配'
			}
			if(option.type){
				this.type = option.type
			}
			if (option.info != undefined) {
				let userInfo = JSON.parse(decodeURIComponent(option.info));
				this.detailInfo = userInfo
				// this.formData.predictCount = userInfo.predictCount
				this.queryParams.outDeliveryId = userInfo.outDeliveryId
				this.queryParams.materialId = userInfo.materialId
				this.queryParams.materialId = userInfo.materialId
				this.from.outDeliveryDetailId = userInfo.id
				// this.formData.outDeliveryDetailId = userInfo.id
				// this.from.tTaskOutDetailListVOS[0].predictCount = userInfo.predictCount
				this.getApi()
			}
		},
		onReachBottom() {
			if (this.dataList.length < this.total) {
				this.queryParams.pageNum++
				this.getApi()
			}
		},
		methods: {
			checked(val, item) {
				console.log(item)
				// this.from.tTaskOutDetailListVOS.forEach(i=>{
				// 	console.log(item,"111")
				// 	this.from.tTaskOutDetailListVOS[0].stockId.push(item.stockid)
				// 	this.from.tTaskOutDetailListVOS.trayId.push(item.id)
				// 	this.from.tTaskOutDetailListVOS.locationId.push(item.locationId)
				// })
				this.from.tTaskOutDetailListVOS[0].stockId = item.stockid
				this.from.tTaskOutDetailListVOS[0].trayId = item.id
				this.from.tTaskOutDetailListVOS[0].locationId = item.locationId
				this.from.tTaskOutDetailListVOS[0].predictCount = item.predictCount
				this.indexCheck = val
			},
			getApi() {
				let url = this.type == '1' ? "/wms/out/traylist/voluntarily" : "/wms/out/groundPileTrayList/voluntarily"
				this.$http.getAction(url, this.queryParams).then(res => {
					console.log(res)
					if(this.type == '1'){
						this.dataList = [...this.dataList, ...res.data]
					} else {
						this.dataList = [...this.dataList, ...res.data.dataList]
					}
					// this.total = res.total
				})
			},
			goexecuteDetail(item) {
				console.log(item, "1111")
				this.formData.tTaskOutDetailListVOS[0].stockId = item.stockid
				this.show = true
			},
			guanbi() {
				this.show = false
			},
			gonopass() {
				this.$http.postAction(`/wms/out/groundPileOutbound`, this.formData).then(res => {
					uni.showToast({
						icon: 'success',
						title: '保存成功'
					})
					uni.navigateBack();
				})

			},
			goSubmit(item) {
				if (this.page == '1') {
					let url = this.type == '1' ? '/wms/outApi/padadd' : '/wms/out/groundPileOutbound'
					let tTaskOutDetailListVOS = []
					this.dataList.map((item) => {
						let info = {
							stockId: item.stockid,
						}
						if(this.type == '1'){
							info.trayId = item.id
							info.locationId = item.locationId
							info.predictCount = item.predictCount
						} else {
							info.receiveCount = item.receiveCount
						}
						tTaskOutDetailListVOS.push(info)
					})
					this.from.tTaskOutDetailListVOS = tTaskOutDetailListVOS
					this.$http.postAction(url, this.from).then(res => {
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
				} else {

				}
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
