<template>
	<view class="app">
		<headTitle>
			<view slot="content">{{title}}</view>
		</headTitle>
		<view class="content">
			<view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList" :key="index"><!--@click="checked(index)"-->
				<view>
					预约单号：{{item.code}}
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
						预计数量：{{item.predictCount}}
					</view>
					<view></view>
				</view>
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
		</view>
		<view v-if="tapList.status == '3' || tapList.status == '10'">
			<button class="button" type="primary" v-if="page == '2'" @click="toGrounding">收货</button>
			<button class="button" type="primary" v-else @click="toGroup">组盘</button>
		</view>
		<!-- <button class="button" type="primary" @click="toVehicleList">收货</button> -->
		<!-- <button class="button" type="primary" @click="goSubmit" v-if="statusIcon==1">审核</button>
		<button class="button" type="primary" @click="goSubmit" v-if="statusIcon==4">检测</button> -->
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
				title: '单据详情',
				page: '1',
				indexCheck: null,
				dataList: [],
				tapList:[],
				total: 0,
				statusIcon:0,
				queryParams: {
					id: "",
					materialCode: null,
					pageSize: 10,
					pageNum: 1,
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
			if(option.info != undefined){
				let userInfo = JSON.parse(decodeURIComponent(option.info));
				this.tapList = userInfo
				this.statusIcon=userInfo.status
				this.queryParams.id = userInfo.id
				this.formData.id = userInfo.id
				this.getApi()
			}
			this.page = option.page || '1'
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
				this.$http.getAction(`/wms/api/inout/delivery/getDetail/${this.queryParams.id}`, this.queryParams)
					.then(res => {
						console.log(res.data)
						let dataList = []
						res.data.deliveryDetailList.map((item) => {
							let dataInfo = {
								id: res.data.id,
								code: res.data.code,
								status: res.data.status,
								detailId: item.id,
								materialId: item.materialId,
								materialCode: item.materialCode,
								materialName: item.materialName,
								unitName: item.unitName,
								batchCode: item.batchCode,
								predictCount: item.predictCount,
							}
							dataList.push(dataInfo)
						})
						this.dataList = dataList
						if((this.tapList.status == '3' || this.tapList.status == '10') && (res.data.status !== '3' && res.data.status !== '10') || (this.tapList.status !== '3' && this.tapList.status !== '10' && this.tapList.status != res.data.status)){
							this.tapList.status = ''
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
						}
						// this.total = res.total
					})
			},
			goSubmit(item) {
				// if (this.indexCheck != null) {
					if(this.statusIcon == 1){
						this.show = true;
					}else{
						uni.navigateTo({
							url: '/pages/warehouseManagement/stockStore/storeList'
						});
					}
				// } else {
				// 	uni.showToast({
				// 		icon: 'none',
				// 		title: '请先选中数据!'
				// 	})
				// }
			},
			toVehicleList() {
				uni.navigateTo({
					url:'../putInStorage/VehicleList'
				})
			},
			toGroup() {
				uni.navigateTo({
					url:'../groupDisk/groupDisk?id=' + this.queryParams.id
				})
			},
			toGrounding() {
				uni.navigateTo({
					url:'../quickWarehousing/quickWarehousing?id=' + this.queryParams.id
				})
			},
			gopass() {
				this.formData.status = 2
				this.$http.postAction(`/wms/api/inout/delivery/approve`, this.formData).then(res => {
					uni.showToast({
						icon: 'success',
						title: '审核成功'
					});
					this.show = false;
				})
			},
			gonopass() {
				this.formData.status = 9
				this.$http.postAction(`/wms/api/inout/delivery/approve`, this.formData).then(res => {
					uni.showToast({
						icon: 'none',
						title: '审核不成功'
					});
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