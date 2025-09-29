<template>
	<view>
		<headTitle :title="getTitle()"></headTitle>
		<VehicleList ref="vehicle" @closeOtherScan="closeOtherScanVehicle"></VehicleList>
		<view style="width: 100%;height: 10rpx;"></view>
		<scanMaterials ref="scanMater" @closeOtherScan="closeOtherScanMaterials"></scanMaterials>
		<view class="radioBox">
			<view class="title">请更新载具状态</view>
			<view>
				<u-radio-group v-model="queryParams.trayStatus">
					<u-radio v-for="(item, index) in list" :key="index" :name="item.name">
						{{item.label}}
					</u-radio>
				</u-radio-group>
			</view>
		</view>
		<view class="from-box" v-if="page == '2'">
			<uni-forms ref="form" :modelValue="from" :rules="rules" label-position="left" label-width='100'>
				<uni-forms-item required label="区域" name="areaId">
					<uni-data-select
					  v-model="from.areaId"
					  :localdata="area"
					  @change="areaChange"
					></uni-data-select>
				</uni-forms-item>
				<uni-forms-item required name="reservoirId" label="库区">
					<uni-data-select
					  v-model="from.reservoirId"
					  :localdata="reservoir"
					  @change="reservoirChange"
					></uni-data-select>
				</uni-forms-item>
				<uni-forms-item required name="locationId" label="库位">
					<uni-data-select
					  v-model="from.locationId"
					  :localdata="location"
					></uni-data-select>
				</uni-forms-item>
			</uni-forms>
		</view>
		<view class="btnBox">
			<view class="cancel" @click="cancel">取消</view>
			<view class="submit" @click="submit">上架</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				page: '1', // 1:收货组盘， 2:扫描入库
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
			this.page = opt.page || '1'
			this.getArea()
		},
		methods: {
			getTitle() {
				let title = '组盘'
				switch(this.page) {
				     case '1':
				        title = '收货组盘'
				        break;
				     case '2':
				        title = '扫描入库'
				        break;
				     default:
				        title = '组盘'
				}
				return title
			},
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
				this.$http.getAction('/wms/reservoir/list?pageNum=1&pageSize=5000&areaId=' + this.from.areaId, {}).then(res => {
					this.reservoir = res.rows
					this.reservoir.map(item => {
						item.text = item.name
						item.value = item.id
					})
				})
			},
			getLocation() {
				this.$http.getAction('/wms/location/list?pageNum=1&pageSize=5000&locationType=1&areaId=' + this.from.areaId + '&reservoirId=' + this.from.reservoirId, {}).then(res => {
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
			closeOtherScanMaterials(){
				this.$refs.vehicle.close()
			},
			closeOtherScanVehicle(){
				this.$refs.scanMater.close()
			},
			submit() {
				this.queryParams.trayCode = this.$refs.vehicle.scannerResult.scannerdata.trim()
				this.queryParams.materialList = this.$refs.scanMater.materialList
				if (this.queryParams.trayCode && this.queryParams.materialList && this.queryParams.materialList.length > 0 && this.queryParams.trayStatus) {
					this.$refs.form.validate().then(v => {
						this.$http.postAction('/wms/api/inout/delivery/putawayTask', this.queryParams).then(res => {
							this.$modal.msgSuccess(res.msg)
							setTimeout(() => {
								// this.$tab.switchTab("/pages/warehouseManagement/warehouseManagement");
								this.cancel()
							}, 2000)
						})
					})
				} else {
					if (!this.queryParams.trayCode) {
						this.$modal.msgError('请扫描载具标签')
					} else if (!this.queryParams.materialList || this.queryParams.materialList.length == 0) {
						this.$modal.msgError('请扫描物料条码')
					} else if (!this.queryParams.trayStatus) {
						this.$modal.msgError('请更新载具状态')
					}
				}
			},
			cancel() {
				this.$tab.navigateBack();
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