<template>
	<view class="app">
		<headTitle>
			<view slot="content">单据详情</view>
		</headTitle>
		<view class="content">
			<view :class="[statusIcon==4 ? 'content_cart2' : 'content_cart']" v-for="(item,index) in dataList"
				:key="index">
				<view style="height: 50rpx;">
					预约单号：{{tapList.code}}
				</view>
				<view style="height: 50rpx;">
					物料编码：{{item.materialCode}}
				</view>
				<view style="height: 50rpx;">
					物料名称：{{item.materialName}}
				</view>
				<view style="display: flex; height: 50rpx;">
					<view style="width:50%;height: 100%;">
						计量单位：{{item.unitName}}
					</view>
					<view style="width:50%;height: 100%;">
						单价：{{item.price}}
					</view>
				</view>
				<view style="display: flex; height: 50rpx;">
					<view style="width:50%;height: 100%;">
						批号：{{item.batchCode}}
					</view>
					<view style="width:50%;height: 100%;">
						预计数量：{{item.predictCount}}
					</view>
				</view>
				<view style="height: 50rpx;" v-if="(statusIcon==2 || statusIcon==5)">
					登记数量：{{item.predictCount-item.registrationCount}}
				</view>

				<view v-if="statusIcon==4" style="width:100%; height: 50rpx; display: flex">
					<view style="width:50%;height: 100%;">
						<input style="height: 100%;" class="input" type="number" v-model="item.failCount"
							placeholder="请输入未通过数量" />
					</view>
					<view style="width:50%;height: 100%;">
						<uni-data-select style="height: 100%;" v-model="item.detectionFailType" :clear="false"
							:localdata="fileOption"></uni-data-select>
					</view>
				</view>

			</view>
			<u-empty text="数据为空" mode="list" v-show="!dataList || dataList.length <= 0 "></u-empty>
		</view>
		<button class="button" type="primary" @click="examine" v-if="statusIcon==1">审核</button>
		<button class="button" type="primary" @click="goReceive" v-if="(statusIcon==2 || statusIcon==5)">收货</button>
		<view class="btn-detection" v-if="statusIcon==4">
			<button class="detection" type="primary" @click="goDetection">检测</button>
			<button class="detection" type="primary" @click="detection">检测完成</button>
		</view>
		<u-modal v-model="show" title="审核" width="90%" :show-confirm-button="false">
			<view class="slot-content">
				<uni-forms label-position="top" ref="form" :modelValue="formData" :rules="rules">
					<uni-forms-item label="审核备注">
						<uni-easyinput type="textarea" v-model="formData.auditRemark" placeholder="请输入..." />
					</uni-forms-item>
				</uni-forms>
			</view>
			<view style="display: flex;justify-content: space-between;padding: 0 20rpx 30rpx 20rpx;">
				<button style="width: 260rpx;height: 90rpx;" type="primary" @click="gonopass">审核不通过</button>
				<button style="width: 210rpx;height: 90rpx;" type="primary" @click="gopass">审核通过</button>
				<button style="width: 140rpx;height: 90rpx;" @click="guanbi">取消</button>
			</view>
		</u-modal>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
	import {
		getDicts
	} from "@/api/system/data";
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
				rules: {},
				fileOption: [], // 未通过原因选项列表
			}
		},
		created() {
			getDicts("in_delivery_detection_fail_type").then(response => {
				this.fileOption = response.data;
				this.fileOption.map(item => {
					item.text = item.dictLabel
					item.value = item.dictValue
				})
			});

		},
		onLoad(option) {
			if (option.info != undefined) {
				let info = JSON.parse(decodeURIComponent(option.info));
				this.tapList = info
				this.statusIcon = info.status
				this.queryParams.id = info.id
				this.formData.id = info.id
				this.getApi()
			}

		},
		// onShow(){
		// 	uni.$on('refresh', (data) => {
		// 		if (data.refresh) {
		// 			// this.queryParams.pageNum = 1
		// 			this.getApi()
		// 		}
		// 	});
		// },
		// onUnload(){
		// 	uni.$off('refresh');
		// },
		// onReachBottom() {
		// 	if(this.dataList.length<this.total){
		// 		this.queryParams.pageNum++
		// 		this.getApi()
		// 	}
		// },
		methods: {
			//刷新
			reFresh() {
				this.getApi()
			},
			checked(val) {
				this.indexCheck = val
			},
			getApi() {
				this.$http.getAction(`/wms/api/inout/delivery/getDetail/${this.queryParams.id}`, this.queryParams)
					.then(res => {
						console.log("res", res)
						this.dataList = res.data.deliveryDetailList
						if (this.statusIcon !== res.data.status) {
							this.statusIcon = ''
							setTimeout(() => {
								this.jumpPage()
							}, 1000)
						}
						// this.total = res.total
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
			goReceive() {
				// uni.navigateTo({
				// 	url: '/pages/warehouseManagement/receive/receive?id=' + this.queryParams.id
				// });
				let data = {
					id: this.queryParams.id,
					deliveryDetailList: [],
				}
				this.dataList.map(item => {
					let material = {
						id: item.id,
						registrationCount: item.predictCount - item.registrationCount,
					}
					data.deliveryDetailList.push(material)
				})
				console.log("data", data)
				this.$http.postAction('/wms/api/inout/delivery/registerDelivery', data).then(res => {
					console.log("res", res)
					this.$modal.msgSuccess(res.msg);
					setTimeout(() => {
						this.jumpPage()
					}, 1000)
				})
			},
			goDetection() {
				// uni.navigateTo({
				// 	url: '/pages/warehouseManagement/detection/detection?id=' + this.queryParams.id
				// });
				let hasNull = false

				for (let i = 0; i < this.dataList.length; i++) {
					if (!this.dataList[i].failCount) {
						this.$refs.uToast.show({
							title: '未通过数量不能为空',
							type: 'error',
						})
						hasNull = true
						return
					}
					if (this.dataList[i].failCount > this.dataList[i].predictCount) {
						this.$refs.uToast.show({
							title: '未通过数量不能大于预计数量',
							type: 'error',
						})
						hasNull = true
						return
					}
					if (!this.dataList[i].detectionFailType) {
						this.$refs.uToast.show({
							title: '未通过原因不能为空',
							type: 'error',
						})
						hasNull = true
						return
					}
				}
				if (hasNull) {
					return
				}

				let materRfidList = []
				this.dataList.map((item) => {
					let info = {
						id: item.id,
						detectionFailType: item.detectionFailType,
						detectionFailRemark: "",
						failCount: item.failCount
					}
					materRfidList.push(info)
				})
				console.log("1111", materRfidList)
				this.$http.postAction('/wms/inout/detail/checkMaterial', materRfidList).then(res => {
					this.$modal.msgSuccess(res.msg);
					setTimeout(() => {
						// this.$tab.navigateBack(1);
						this.jumpPage()
					}, 1000)
				})
			},
			jumpPage() {
				let pages = getCurrentPages(); // 当前页面
				let beforePage = pages[pages.length - 2]; // 上一页
				uni.navigateBack({
					success: function() {
						// 触发列表页面的reFresh()方法,成功之后,刷新列表页面的数据
						beforePage.$vm.reFresh();
					}
				})
			},
			examine() {
				this.show = true
			},
			detection() {
				this.$http.postAction(`/wms/api/inout/delivery/checkDelivery`, {
						id: this.queryParams.id
					})
					.then(res => {
						uni.showToast({
							icon: 'success',
							title: '检测完成'
						});
						setTimeout(() => {
							this.jumpPage()
						}, 1000)
					})
			},
			gopass() {
				this.formData.status = 2
				this.$http.postAction(`/wms/api/inout/delivery/approve`, this.formData).then(res => {
					uni.showToast({
						icon: 'success',
						title: '审核通过'
					});
					this.show = false;
					setTimeout(() => {
						this.jumpPage()
					}, 1000)
				})
			},
			gonopass() {
				this.formData.status = 9
				this.$http.postAction(`/wms/api/inout/delivery/approve`, this.formData).then(res => {
					uni.showToast({
						icon: 'none',
						title: '审核不通过'
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

			.content_cart2 {
				height: 450rpx;
				background: #FFFFFF;
				border-radius: 15rpx;
				padding: 30rpx 30rpx;
				margin: 10rpx 0rpx 0rpx 0rpx;

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

				.input {
					width: 90%;
					background: #EEEEEE;
					border-radius: 15rpx;
					padding: 15rpx 7rpx;
				}
			}
		}

		.button {
			margin: 30rpx 30rpx;
			position: absolute;
			width: 92%;
			bottom: 10rpx;
		}

		.btn-detection {
			margin: 30rpx 30rpx;
			position: absolute;
			width: 92%;
			bottom: 10rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			.detection {
				width: 45%;
			}
		}

		.uni-forms-item.is-direction-top {
			flex-direction: inherit;
			display: flex;
		}
	}
</style>