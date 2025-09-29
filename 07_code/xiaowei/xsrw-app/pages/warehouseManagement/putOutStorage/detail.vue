<template>
	<view class="app">
		<headTitle>
			<view slot="content">单据详情</view>
		</headTitle>
		<view class="content">
			<view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList"
				:key="index" @click="checked(index)">
				<view>
					出库单号：{{item.code}}
				</view>
				<view>
					物料编码：{{item.tOutDeliveryDetailList[0].materialCode}}
				</view>
				<view>
					物料名称：{{item.tOutDeliveryDetailList[0].materialName}}
				</view>
				<view>
					预计出库数量：{{item.tOutDeliveryDetailList[0].predictCount}}
				</view>
				<!-- <view>
					小件领取数量：{{item.tOutDeliveryDetailList[0].smallPredictCount}}
				</view> -->
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
		</view>
		<button class="button" type="primary" @click="goSubmit" v-if="statusIcon==1">审核</button>
		<button class="button" type="primary" @click="goSubmit" v-if="statusIcon==4">检测</button>
		<u-modal v-model="show" title="审核" width="90%" :show-confirm-button="false">
			<view class="slot-content">
				<uni-forms label-position="top" ref="form" :modelValue="formData" :rules="rules">
					<uni-forms-item label="审核备注">
						<uni-easyinput type="textarea" v-model="formData.remark" placeholder="请输入..." />
					</uni-forms-item>
				</uni-forms>
			</view>
			<view style="display: flex;justify-content: space-between;padding: 0 20rpx 30rpx 20rpx;">
				<button style="width: 260rpx;height: 90rpx;" type="primary" @click="gonopass">审核不通过</button>
				<button style="width: 210rpx;height: 90rpx;" type="primary" @click="gopass">审核通过</button>
				<button style="width: 140rpx;height: 90rpx;" @click="guanbi">取消</button>
			</view>

		</u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				indexCheck: null,
				dataList: [],
				tapList: [],
				total: 0,
				statusIcon: 0,
				queryParams: {
					id: "",
					materialCode: null,
					// pageSize: 10,
					// pageNum: 1,
				},
				//弹出框
				show: false,
				content: '东临碣石，以观沧海',
				formData: {
					status: 0,
					id: ""
				},
				rules: {}
			}
		},

		onLoad(option) {
			if (option.info != undefined) {
				let userInfo = JSON.parse(decodeURIComponent(option.info));
				this.tapList = userInfo
				this.statusIcon = userInfo.status
				this.queryParams.id = userInfo.id
				this.formData.id = userInfo.id
				this.getApi()
			}

		},
		// onReachBottom() {
		// 	if(this.dataList.length<this.total){
		// 		this.queryParams.pageNum++
		// 		this.getApi()
		// 	}
		// },
		methods: {
			//刷新
			reFresh(){
				this.getApi()
			},
			checked(val) {
				this.indexCheck = val
			},
			getApi() {
				console.log(this.queryParams)
				this.$http.getAction(`/wms/outApi/outDelivery/${this.queryParams.id}`, this.queryParams)
					.then(res => {
						this.dataList = [res.data]
						// this.total = res.total
						if(this.statusIcon !== res.data.status){
							this.statusIcon = ''
							setTimeout(() => {
								this.jumpPage()
							}, 1000)
						}
					})
			},
			jumpPage(){
				let pages = getCurrentPages(); // 当前页面
				let beforePage = pages[pages.length - 2]; // 上一页
				uni.navigateBack({
				    success: function() {
				        // 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
				        beforePage.$vm.reFresh();
				    }
				})
			},
			goSubmit(item) {
				if (this.indexCheck != null) {
					if (this.statusIcon == 1) {
						this.show = true;
					} else {
						uni.navigateTo({
							url: '/pages/warehouseManagement/stockStore/storeList'
						});
					}
				} else {
					uni.showToast({
						icon: 'none',
						title: '请先选中数据!'
					})
				}
			},
			gopass() {
				this.formData.status = 2
				this.$http.postAction(`/wms/outApi/outDelivery/approve`, this.formData).then(res => {
					uni.showToast({
						icon: 'success',
						title: '审核成功'
					});
					this.show = false;
					setTimeout(() => {
						this.jumpPage()
					}, 1000)
				})
			},
			gonopass() {
				this.formData.status = 9
				this.$http.postAction(`/wms/outApi/outDelivery/approve`, this.formData).then(res => {
					uni.showToast({
						icon: 'none',
						title: '审核成功'
					});
					this.show = false;
					setTimeout(() => {
						this.jumpPage()
					}, 1000)
				})
			},
			guanbi() {
				this.show = false;
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
			height: 94%;
			padding: 30rpx 30rpx;
			background: #F5F5F5;
			box-sizing: border-box;
			overflow: scroll;

			.content_cart {
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

				.cart_wapper {
					display: flex;

					view {
						width: 50%;
					}
				}
			}

			.content_cart1 {
				height: 414rpx;
				background: gray;
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

				.cart_wapper {
					display: flex;

					view {
						width: 50%;
					}
				}
			}
		}

		.button {
			margin: 30rpx 30rpx;
			position: absolute;
			width: 92%;
			bottom: 10rpx;
		}

		.uni-forms-item.is-direction-top {
			flex-direction: inherit;
			display: flex;
		}
	}
</style>