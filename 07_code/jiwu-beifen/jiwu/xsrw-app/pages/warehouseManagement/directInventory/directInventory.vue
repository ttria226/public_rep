<template>
	<view>
		<headTitle title="扫描出库"></headTitle>
		<LocationScanning ref="location" @registreOk="registreOk"></LocationScanning>
		<view style="width: 100%;height: 10rpx;"></view>
		<!-- <scanMaterials ref="scanMater"></scanMaterials> -->
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
		<view class="tableBox">
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="25%">库位</u-th>
					<u-th width="25%">物料名称</u-th>
					<u-th width="25%">库存数量</u-th>
					<u-th width="25%">实盘数量</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in fromData.materialData">
					<u-td width="25%" class="u-tdsty">{{item.locationName}}</u-td>
					<u-td width="25%" class="u-tdsty">{{item.materialName}}</u-td>
					<u-td width="25%">{{item.count}}</u-td>
					<u-td width="25%">
						<u-input class="input-style" v-model="item.actualCount" type="number" :border="true"  />
					</u-td>
				</u-tr>
			</u-table>
		</view>
		<view class="btnBox">
			<view class="cancel" @click="cancel">取消</view>
			<view class="submit" @click="submit">盘点</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
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
				fromData: {},
			}
		},
		methods: {
			registreOk(scannerdata) {
				if (scannerdata) {
					let data = {
						code: scannerdata.trim()
					}
					this.$http.getAction('/wms/api/checkDelivery/locationInfo', data).then(res => {
						console.log(56656, res)
						this.$refs.location.scannerResult.scannerdata = res.data.locationName
						this.fromData = res.data
					}).catch()
				}
			},
			submit() {
				if (this.fromData && this.fromData.materialData && this.fromData.materialData.length > 0) {
					console.log(this.fromData)
					this.$http.postAction('/wms/api/checkDelivery/checkData', this.fromData.materialData).then(res => {
						this.$modal.msgSuccess(res.msg)
						setTimeout(() => {
							// this.$tab.switchTab("/pages/warehouseManagement/warehouseManagement");
							this.cancel()
						}, 2000)
					})
				} else {
					this.$modal.msgError('请扫描库位')
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
	/deep/.u-tdsty{
		word-wrap: break-all;
		word-break: break-all;
	}
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
	.tableBox {
		margin-top: 55rpx;

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
</style>