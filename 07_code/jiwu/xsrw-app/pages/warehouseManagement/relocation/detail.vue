<template>
	<view class="app">
		<headTitle>
			<view slot="content">{{title}}</view>
		</headTitle>
		<view class="content" :style="{ 'margin-bottom': info.taskStatus == '1' ? '100rpx' : '0rpx' }">
			<view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList"
				:key="index" @click="checked(index)">
				<view>
					预约单号：{{info.taskNo}}
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
						批号：{{item.batchCode}}
					</view>
				</view>
				<view class="cart_wapper">
					<view>
						预计移库数量：{{item.predictCount}}
					</view>
					<view>
						实际移库数量：{{item.actualCount}}
					</view>
				</view>
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
		</view>
		<view class="botton-content" v-if="info.taskStatus == '1'">
			<button class="button" type="primary" @click="show = true">执行</button>
		</view>
		<u-modal v-model="show" content="确认移库？" show-cancel-button :show-title="false" @confirm="confirm" @cancel="show=false"></u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				show: false,
				title: '移库详情',
				page: '1',
				indexCheck: null,
				dataList: [],
				tapList:[],
				total: 0,
				statusIcon:0,
				id: "",
				queryParams: {
					materialCode: null,
					pageSize: 10,
					pageNum: 1,
				},
				info: {},
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
			this.id = option.id || ''
			if (this.id) {
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
			checked(val) {
				this.indexCheck = val
			},
			getApi() {
				this.$http.getAction(`/wms/api/task/wcs/getDetailByTaskId/${this.id}`, {})
					.then(res => {
						console.log(res.data)
						this.dataList = res.data.taskWcsDetailVOList
						this.info = res.data
						// this.total = res.total
					})
			},
			confirm() {
				this.$http.postAction(`/wms/api/stock/updateStock?id=` + this.id, {}).then(res => {
					uni.showToast({
						icon: 'success',
						title: '移库成功'
					});
					this.show = false;
					setTimeout(() => {
						let pages = getCurrentPages(); // 当前页面
						let beforePage = pages[pages.length - 2]; // 上一页
						uni.navigateBack({
						    success: function() {
						        // 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
						        beforePage.$vm.reFresh();
						    }
						})
					}, 1000)
				})
			},
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
			height: calc(100% - 80rpx);
			padding: 30rpx 30rpx;
			// margin-bottom: 100rpx;
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
		
		.botton-content{
			position: fixed;
			width: 100%;
			height: 50rpx;
			bottom: 0rpx;
			background-color: #FFFFFF;
			.button {
				margin: 30rpx 30rpx;
				position: absolute;
				width: 92%;
				bottom: 10rpx;
			}
		}

		.uni-forms-item.is-direction-top {
			flex-direction: inherit;
			display: flex;
		}
	}
</style>