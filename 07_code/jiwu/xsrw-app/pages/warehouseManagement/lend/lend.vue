<template>
	<view>
		<headTitle>
			<view slot="content">借出</view>
		</headTitle>
		<view class="content">
			<view class="content_cart" v-for="(item,index) in dataList" :key="index">
				<view>
					名称：{{item.name}}
				</view>
				<view>
					部门：{{item.deptName}}
				</view>
				<view>
					位置：{{item.functionLocation}}
				</view>
				<view>
					库存数量：{{item.availableCount}}
				</view>
				<button class="button" type="primary" @click="lend(item)">借出</button>
			</view>
			<u-empty text="数据为空" mode="list" v-show="dataList.length <= 0 "></u-empty>
			<u-modal v-model="show" title="借出" width="90%" :show-confirm-button="false">
				<view class="from-box">
					<uni-forms label-position="left" label-width='150' ref="form" :modelValue="formData" :rules="rules">
						<uni-forms-item label="借出数量" name="loanCount">
							<uni-easyinput type="number" v-model="formData.loanCount" @change="numChange" placeholder="请输入借出数量" />
						</uni-forms-item>
					</uni-forms>
				</view>
				<view style="display: flex;padding: 30rpx 0rpx;">
					<button style="width: 250rpx;height: 90rpx;" type="primary" @click="submit">借出</button>
					<button style="width: 200rpx;height: 90rpx;" @click="show = false">取消</button>
				</view>
			</u-modal>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				queryParams: {
					pageSize: 10,
					pageNum: 1,
				},
				dataList: [],
				total: 0,
				show: false,
				current: {},
				formData: {
					loanCount: '',
				},
				rules: {
					loanCount: {
						required: true,
						errorMessage: '请输入借出数量',
					},
				},
			}
		},
		async onLoad() {
			this.getApi()
		},
		onReachBottom() {
			if (this.dataList.length < this.total) {
				this.queryParams.pageNum++
				this.getApi()
			}
		},
		methods: {
			//刷新
			reFresh(){
				this.queryParams.pageNum = 1
				this.getApi('load')
			},
			getApi(type) {
				this.$http.getAction("/wms/outApi/loan/getEquipmentList", this.queryParams).then(res => {
					if(this.dataList.length < res.total){
						this.dataList = [...this.dataList,...res.rows]
					} else if(this.dataList.length > res.total || type == 'load'){
						this.dataList = res.rows
					}
					this.total = res.total
				})
			},
			lend(item) {
				this.current = item
				this.formData.loanRegisterId = item.registerId
				this.show = true
			},
			numChange() {
				this.formData.loanCount = parseInt(this.formData.loanCount)
				if (this.formData.loanCount > this.current.availableCount) {
					this.formData.loanCount = parseInt(this.current.availableCount)
				}
			},
			submit() {
				this.$http.postAction('/wms/outApi/loan/addReturn', this.formData).then(res => {
					this.show = false
					this.$modal.msgSuccess(res.msg);
					this.reFresh()
				})
				
			},
		}
	}
</script>

<style lang="scss">
	header {
		display: flex;
		align-items: center;
		width: 100%;
		height: 104rpx;
		background: #FFFFFF;

		.header_tap {
			display: flex;
			align-items: center;
			margin-left: 24rpx;
			width: 688rpx;
			height: 92rpx;
			background: #F4F6F8;
			border-radius: 4rpx;

			text {
				// width: 72rpx;
				// height: 36rpx;
				margin: 0px 20rpx;
				font-size: 36rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #0B0B0B;
			}
		}
	}

	.content {
		padding: 30rpx 30rpx;
		width: 100%;
		height: 100%;
		background: #F5F5F5;
		box-sizing: border-box;
		overflow: scroll;

		.active {
			color: #0075FF;
		}

		.navTitle_All {
			display: flex;
			align-items: center;
			justify-content: center;
			height: 115rpx;
			background: #FFFFFF;

			.navTitle {
				width: 50%;
				text-align: center;
			}
		}

		.content_cart {
			position: relative;
			// width: 785rpx;
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

			.button {
				// position: absolute;
				// right: 20rpx;
				// bottom: 20rpx;
				width: 200rpx !important;
				margin: 0;
				margin-left: auto;
			}
		}
	}

	.from-box {
		margin-top: 30rpx;
		padding: 45rpx 20rpx;
		background: #fff;
	}
</style>