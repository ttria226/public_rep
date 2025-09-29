<template>
	<view>
		<headTitle>
			<view slot="content">库内移位</view>
		</headTitle>
		<view class="tableBox">
			<u-table border-color="#595959">
				<u-tr>
					<u-th>物料名称</u-th>
					<u-th>批次号</u-th>
					<u-th>在库数量</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in dataList">
					<u-td>{{item.materialName}}</u-td>
					<u-td>{{item.batchCode}}</u-td>
					<u-td>{{item.count}}</u-td>
				</u-tr>
			</u-table>
		</view>
		<view class="from-box">
			<uni-forms ref="form" :modelValue="from" :rules="rules" label-position="left" label-width='100'>
				<uni-forms-item required name="locationId" label="目标库位">
					<uni-data-select
					  v-model="from.locationId"
					  :localdata="locationList"
					></uni-data-select>
				</uni-forms-item>
			</uni-forms>
		</view>
		<view class="btnBox">
			<view class="cancel" @click="cancel">取消</view>
			<view class="submit" @click="submit">提交</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
				locationId: '',
				dataList: [],
				locationList: [],
				from: {
					locationId: null,
				},
				rules: {
					locationId: {
						rules: [{
							required: true,
							errorMessage: '请选择库位',
						}]
					},
				},
			};
		},
		onLoad(opt) {
			this.id = opt.id || ''
			this.locationId = opt.locationId || ''
			this.from.stockId = this.id
			this.shift()
			this.getOtherLocation()
		},
		methods: {
			shift() {
				this.$http.getAction(`/wms/api/stock/shift/${this.id}`, {})
					.then(res => {
						this.dataList = res.data
					})
			},
			getOtherLocation() {
				this.$http.getAction(`/wms/api/base/getOtherLocation`, {locationId: this.locationId})
					.then(res => {
						this.locationList = res.data
						this.locationList.map(item => {
							item.text = item.name
							item.value = item.id
						})
					})
			},
			submit() {
				this.$refs.form.validate().then(v => {
					this.$http.postAction('/wms/api/stock/moveLibrary/shiftMoveLibrary', this.form).then(res => {
						this.$modal.msgSuccess(res.msg)
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
				})
			},
			cancel() {
				this.$tab.navigateBack();
			}
		}
	}
</script>

<style scoped lang="scss">
	.tableBox {
		margin-top: 30rpx;
	
		/deep/.u-th {
			background: linear-gradient(180deg, rgba(10, 133, 213, 0.85), rgba(1, 87, 174, 0.85));
			opacity: 0.98;
			font-size: 31rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			text-shadow: 0rpx 2rpx 5rpx rgba(0, 57, 113, 0.35);
		}
	
		/deep/ .u-td {
			background: #F4F6F7;
			font-size: 33rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #0B0B0B;
			height: auto;
		}
	}
	.from-box {
		margin-top: 30rpx;
		padding: 45rpx 20rpx;
		background: #fff;
	}
	.btnBox {
		text-align: center;
		display: flex;
		justify-content: space-around;
		margin: 40rpx 0;

		.cancel {
			width: 187rpx;
			height: 91rpx;
			line-height: 91rpx;
			background: #FFFFFF;
			border: 1px solid #0B0B0B;
			border-radius: 7rpx;
			font-size: 39rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #0B0B0B;
		}

		.submit {
			width: 186rpx;
			height: 90rpx;
			line-height: 90rpx;
			background: #0075FF;
			border-radius: 7rpx;
			font-size: 39rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #FFFFFF;
		}
	}
</style>