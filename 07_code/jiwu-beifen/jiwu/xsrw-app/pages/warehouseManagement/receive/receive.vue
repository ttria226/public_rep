<template>
	<view>
		<headTitle title="扫码收货"></headTitle>
		<scanMaterials ref="wuliao" typeName='收货' :orderId="id"></scanMaterials>
		<view style="display: flex;justify-content: center;">
			<view class="subBtn" @click="submit">提交</view>
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
			submit() {
				let that = this
				const list = this.$refs.wuliao.materialList
				let content = []
				let zeroNum = 0
				list.map((item) => {
					if(Number(item.count) == 0){
						zeroNum++
					}
				})
				if(zeroNum !== 0){
					this.$modal.msg('实际收货数量不能为空或者0')
					return
				}
				list.map(ret => {
					if (ret.count != ret.expectedCount) {
						content.push(ret.batchCode)
					}
				})
				if (content && content.length > 0) {
					this.content = `批次号为 ${content.toString()} 的实际收货数量与预计收货数量不一致，确定要提交吗？`
					this.showModal = true
				} else {
					this.submitForm()
				}
			},
			submitForm() {
				let data = {
					id: this.id,
					deliveryDetailList: [],
				}
				this.$refs.wuliao.materialList.map(item => {
					let material = {
						id: item.id,
						registrationCount: parseInt(item.count),
					}
					data.deliveryDetailList.push(material)
				})
				this.$http.postAction('/wms/api/inout/delivery/registerDelivery', data).then(res => {
					console.log(res)
					this.$modal.msgSuccess(res.msg);
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