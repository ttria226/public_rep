<template>
	<view class="app">
		<headTitle>
			<view slot="content">单据详情</view>
		</headTitle>
		<view class="content">
			<!-- <view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList"
				:key="index" @click="checked(index)"> -->
			<view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList"
				:key="index">
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
				<!-- <view v-if="item.tOutDeliveryDetailList[0].smallReceiveCount">
					小件领取数量：{{item.tOutDeliveryDetailList[0].smallReceiveCount}}
				</view> -->
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
		</view>
		<button class="button" type="primary" @click="showModal = true">回流</button>
		<u-modal v-model="showModal" content="确认回流？" :show-cancel-button="true" :mask-close-able="true" :show-title="false" @confirm="reflux"
			@cancel="showModal=false">
		</u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
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
				showModal: false,
				content: '东临碣石，以观沧海',
				formData: {
					status: 0,
					id: ""
				},
				rules: {}
			}
		},

		onLoad(option) {
			this.id = option.id || ''
			this.getApi()
		},
		// onReachBottom() {
		// 	if(this.dataList.length<this.total){
		// 		this.queryParams.pageNum++
		// 		this.getApi()
		// 	}
		// },
		methods: {
			checked(val) {
				this.indexCheck = val
			},
			getApi() {
				this.$http.getAction(`/wms/outApi/outDelivery/${this.id}`, this.queryParams)
					.then(res => {
						this.dataList = [res.data]
						// this.total = res.total
					})
			},
			reflux() {
				this.$http.postAction(`/wms/outApi/refluxOutDelivery`, {id: this.id})
					.then(res => {
						uni.showToast({
							icon: 'none',
							title: '回流成功'
						});
						setTimeout(() => {
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