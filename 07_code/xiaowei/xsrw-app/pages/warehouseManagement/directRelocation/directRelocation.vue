<template>
	<view>
		<headTitle title="直接移库"></headTitle>
		<RelocationMaterials ref="original"></RelocationMaterials>
		<LocationScanning ref="target"></LocationScanning>
		<view style="display: flex;justify-content: center;">
			<view class="subBtn" @click="showModal=false">直接移库</view>
		</view>
		<u-modal v-model="showModal" :content="content" :show-cancel-button="true" :mask-close-able="true" :show-title="false" @confirm="submitForm"
			@cancel="showModal=false">
		</u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				id: null,
				type: null,
				content: "确认移库？",
				showModal: false,
				queryParams: {
					pageNum: 1,
					pageSize: 10,
				}, //查询条件
				total: null, //总条数
				orderList: [], //入库单列表
			}
		},
		onLoad(option) {
			this.id = option.id ? parseInt(option.id) : null
		},
		methods: {
			type(type) {
				this.type = type
			},
			submitForm() {
				let data = {
					stockId: this.$refs.original.current.id,
					rfid: this.$refs.original.current.rfid,
					locationCode: this.$refs.target.scannerResult.scannerdata.trim()
				}
				this.$http.postAction('/wms/api/stock/directTransfer', data).then(res => {
					console.log(res)
					this.$modal.msgSuccess(res.msg);
					setTimeout(() => {
						this.$tab.navigateBack();
					}, 1000)
				})
				// if (type == 1) {
				// 	const data = {
				// 		trayId: this.$refs.jianxuan.trayId,
				// 	}
				// 	this.$http.postAction('/wms/api/inout/delivery/executeInByTray', data).then(res => {
				// 		console.log(res)
				// 		this.$modal.msgSuccess(res.msg);
				// 		setTimeout(() => {
				// 			this.$tab.navigateBack();
				// 		}, 1000)
				// 	})
				// } else {
				// 	let data = {
				// 		trayId: this.$refs.jianxuan.trayId,
				// 		tTaskOutDetailListVOS: [],
				// 	}
				// 	this.$refs.jianxuan.materialList.map(item => {
				// 		let material = {
				// 			stockId: item.id,
				// 			receiveCount: parseInt(item.count),
				// 		}
				// 		data.tTaskOutDetailListVOS.push(material)
				// 	})
				// 	this.$http.postAction('/wms/outApi/executeOutByTray', data).then(res => {
				// 		console.log(res)
				// 		this.$modal.msgSuccess(res.msg);
				// 		setTimeout(() => {
				// 			this.$tab.navigateBack();
				// 		}, 1000)
				// 	})
				// }
			},
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style scoped lang="scss">
	.subBtn {
		width: 225rpx;
		height: 90rpx;
		background: #0075FF;
		border-radius: 7rpx;
		font-size: 39rpx;
		font-family: PingFang SC;
		font-weight: bold;
		color: #FFFFFF;
		text-align: center;
		line-height: 90rpx;
		margin-top: 100rpx;
	}

	.cancelBtn {
		width: 222rpx;
		height: 90rpx;
		line-height: 90rpx;
		background: #F5F5F5;
		border-radius: 45rpx;
		font-size: 39rpx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #0B0B0B;
	}

	.sureBtn {
		width: 222rpx;
		height: 90rpx;
		line-height: 90rpx;
		background: #00BDFF;
		border-radius: 45rpx;
		font-size: 39rpx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #FFFFFF;
	}
</style>