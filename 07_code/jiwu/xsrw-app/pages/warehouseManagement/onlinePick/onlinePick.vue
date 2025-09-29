<template>
	<view>
		<headTitle title="在线拣选"></headTitle>
		<OnlineVehicle ref="jianxuan" @type="type"></OnlineVehicle>
		<view style="display: flex;justify-content: center;" v-if="type">
			<view class="subBtn" @click="showModal=false">{{type == 1 ? '确认入库' : '确认出库'}}</view>
		</view>
		<u-modal v-model="showModal" :content="type == 1 ? '确认入库' : '确认出库'" :show-cancel-button="true" :mask-close-able="true" :show-title="false" @confirm="submitForm"
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
				content: "",
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
				if (type == 1) {
					const data = {
						trayId: this.$refs.jianxuan.trayId,
					}
					this.$http.postAction('/wms/api/inout/delivery/executeInByTray', data).then(res => {
						console.log(res)
						this.$modal.msgSuccess(res.msg);
						setTimeout(() => {
							this.$tab.navigateBack();
						}, 1000)
					})
				} else {
					let data = {
						trayId: this.$refs.jianxuan.trayId,
						tTaskOutDetailListVOS: [],
					}
					this.$refs.jianxuan.materialList.map(item => {
						let material = {
							stockId: item.id,
							receiveCount: parseInt(item.count),
						}
						data.tTaskOutDetailListVOS.push(material)
					})
					this.$http.postAction('/wms/outApi/executeOutByTray', data).then(res => {
						console.log(res)
						this.$modal.msgSuccess(res.msg);
						setTimeout(() => {
							this.$tab.navigateBack();
						}, 1000)
					})
				}
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