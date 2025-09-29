<template>
	<view>
		<headTitle title="地堆上架"></headTitle>
		<VehicleList ref="vehicle" :isChuku="false" @closeOtherScan="closeOtherScanVehicle"></VehicleList>
		<view style="width: 100%;height: 10rpx;"></view>
		<GroupMaterials ref="scanMater" typeName="地堆上架" :orderId="id" @closeOtherScan="closeOtherScanMaterials">
		</GroupMaterials>
		<!-- <view class="radioBox">
			<view class="title">请更新载具状态</view>
			<view>
				<u-radio-group v-model="queryParams.trayStatus">
					<u-radio v-for="(item, index) in list" :key="index" :name="item.name">
						{{item.label}}
					</u-radio>
				</u-radio-group>
			</view>
		</view> -->
		<view class="from-box">
			<uni-forms ref="form" :modelValue="from" :rules="rules" label-position="left" label-width='100'>
				<uni-forms-item required label="区域" name="areaId">
					<uni-data-select v-model="from.areaId" :localdata="area" @change="areaChange"></uni-data-select>
				</uni-forms-item>
				<uni-forms-item required name="reservoirId" label="库区">
					<uni-data-select v-model="from.reservoirId" :localdata="reservoir"
						@change="reservoirChange"></uni-data-select>
				</uni-forms-item>
				<uni-forms-item required name="locationId" label="库位">
					<uni-data-select v-model="from.locationId" :localdata="location"></uni-data-select>
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
				id: null,
				queryParams: {
					trayStatus: null,
					trayCode: null,
				},
				list: [{
						name: 2,
						label: "全托",
					},
					{
						name: 1,
						label: "半托",
					},
				],
				area: [],
				reservoir: [],
				location: [],
				from: {
					areaId: null,
					reservoirId: null,
					locationId: null,
				},
				rules: {
					areaId: {
						rules: [{
							required: true,
							errorMessage: '请选择区域',
						}, ]
					},
					reservoirId: {
						rules: [{
							required: true,
							errorMessage: '请选择库区',
						}]
					},
					locationId: {
						rules: [{
							required: true,
							errorMessage: '请选择库位',
						}]
					},
				},
			}
		},
		onLoad(opt) {
			this.id = opt.id ? parseInt(opt.id) : null
			this.getArea()
		},
		methods: {
			getArea() {
				this.$http.getAction('/wms/area/list?pageNum=1&pageSize=5000', {}).then(res => {
					this.area = res.rows
					this.area.map(item => {
						item.text = item.name
						item.value = item.id
					})
				})
			},
			getReservoir() {
				this.$http.getAction('/wms/reservoir/list?pageNum=1&pageSize=5000&areaId=' + this.from.areaId, {}).then(
					res => {
						this.reservoir = res.rows
						this.reservoir.map(item => {
							item.text = item.name
							item.value = item.id
						})
					})
			},
			getLocation() {
				this.$http.getAction('/wms/location/list?pageNum=1&pageSize=5000&locationType=1&areaId=' + this.from
					.areaId + '&reservoirId=' + this.from.reservoirId, {}).then(res => {
					this.location = res.rows
					this.location.map(item => {
						item.text = item.name
						item.value = item.id
					})
				})
			},
			areaChange(e) {
				this.$nextTick(() => {
					if (this.from.areaId) {
						this.from.reservoirId = null
						this.locationId = null
						this.getReservoir()
					}
				})
			},
			reservoirChange() {
				this.$nextTick(() => {
					if (this.from.areaId && this.from.reservoirId) {
						this.locationId = null
						this.getLocation()
					}
				})
			},
			closeOtherScanMaterials() {
				this.$refs.vehicle.close()
			},
			closeOtherScanVehicle() {
				this.$refs.scanMater.close()
			},
			submit() {
				this.queryParams.trayCode = this.$refs.vehicle.scannerResult.scannerdata.trim()
				this.queryParams.materialList = this.$refs.scanMater.materialList || []
				let data = {
					id: this.id,
					trayCode: this.queryParams.trayCode,
					locationId: null,
					rfIds: [],
				}
				if (this.queryParams.materialList && this.queryParams.materialList.length > 0) {
					this.queryParams.materialList.map(item => {
						let materialRfidsList = [...item.rfidHeads]
						data.rfIds = [...materialRfidsList]
					})
				}
				this.$refs.form.validate().then(v => {
					data.locationId = this.from.locationId
					console.log(data)
					this.$http.postAction('/wms/api/inout/delivery/floorTask', data).then(res => {
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
				}).catch(err => {
					console.log('表单错误信息：', err);
				})
			},
			cancel() {
				uni.navigateBack();
			}
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style scoped lang="scss">
	.radioBox {
		padding: 30rpx;
		background-color: #fff;
		margin-top: 10rpx;
		display: flex;
		justify-content: space-between;

		.title {
			font-size: 36rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #0B0B0B;
		}
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

	.from-box {
		margin-top: 30rpx;
		padding: 45rpx 20rpx;
		background: #fff;
	}
</style>