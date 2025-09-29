<template>
	<view class="scanBox">
		<view class="titleBox">
			<view class="line"></view>
			<view class="text">请扫描物料条码</view>
		</view>
		<view class="abilityBtn">
			<view style="position: relative;">
				<image v-if="type == 1" style="position: absolute;top: 0px;left: 0px;z-index: 2;" src="@/static/img/selectedbg.png" alt=""></image>
				<image src="@/static/img/rfidBtn.png" alt="" @click="selType(1)"></image>
			</view>
			<!-- <image src="@/static/img/rfidBtn.png" alt="" @click="selType(1)"></image> -->
			<!-- <image src="@/static/img/labelBtnIcon.png" alt="" @click="selType(2)"></image> -->
		</view>
		<view>
			<view class="startBtn" v-if="!starting" @click="startScan">开始扫描</view>
			<view class="startBtn" v-if="starting">进行中···</view>
			<!-- <view class="startBtn" @click="startScan">开始扫描</view>
			<view class="startBtn" @click="stop">停止</view> -->
		</view>
		<view class="tableBox">
			<span>扫描结果：</span><span>{{scannerResult.scannerdata}}</span>
		</view>
		<view class="tableBox">
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="20%">库位</u-th>
					<u-th width="25%">物料名称</u-th>
					<u-th width="20%">库存数量</u-th>
					<u-th width="25%">实盘数量</u-th>
					<u-th width="10%">操作</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in materialList">
					<u-td width="20%" class="u-tdsty">{{item.locationName}}</u-td>
					<u-td width="25%" class="u-tdsty">{{item.materialName}}</u-td>
					<u-td width="20%">{{item.stockNum}}</u-td>
					<u-td width="25%">
						<u-input class="input-style" v-model="item.realyNum" type="number" :border="true"  />
					</u-td>
					<u-td width="10%"><span style="color: #009BF4;" @click="select(item)">移库</span></u-td>
				</u-tr>
			</u-table>
		</view>
	</view>
</template>

<script>
	var jpushModuleRelocationMaterials = uni.requireNativePlugin("Uhf-Plugin")
	export default {
		name: "RelocationMaterials",
		props: {
			typeName: {
				type: String,
				default: "入库"
			},
			orderId: {
				type: Number,
				default: null
			},
		},
		data() {
			return {
				trayCode:null,
				type: null, //扫描类型
				materialList: [], //扫描结果
				materialCountList: {}, //预计数量
				scannerResult: {
					scannerdata: '',
				},
				current: {},
				starting:false,
			};
		},
		created() {
			if (this.typeName == '入库') {
				this.getMaterialCountList()
			}
				console.log(this.typeName)
			if (this.typeName == '收货' && this.orderId) {
				this.getOrderList()
			}
		},
		methods: {
			// getScanTray(trayCode) {
			// 	this.$http.getAction('/wms/outApi/scanTray', {
			// 		trayCode: trayCode
			// 	}).then(res => {
			// 		console.log(666666666, res)
			// 		this.materialCountList = res.data
			// 	})
			// },
			select(item) {
				this.current = item
			},
			getMaterialCountList() {
				this.$http.getAction('/wms/api/inout/delivery/getMaterialCountList', {}).then(res => {
					this.materialCountList = res.data
				})
			},
			getOrderList () {
				this.$http.getAction(`/wms/api/inout/delivery/getDetail/${this.orderId}`, {})
				.then(res => {
					let orderList = res.data.deliveryDetailList || []
					orderList.map(item => {
						item.expectedCount = item.predictCount
						item.count = item.registrationCount || 0
					})
					this.materialList = orderList
					// this.total = res.total
				})
			},
			close(){
				jpushModuleRelocationMaterials.closeDevice()
				plus.key.removeEventListener("keydown")
			},
			selType(e) {
				this.type = 1
				if(this.type ==1 && this.starting){
					return
				}
				this.starting = false
				this.$modal.msg(`已选择${e==1?'RFID':'标签'}！`);
				this.$emit('closeOtherScan','')
				this.$nextTick(() => {
					jpushModuleRelocationMaterials.openDevice()
					plus.key.addEventListener("keydown", function() {
						alert("BackButton Key pressed!");
					}, false);
				})
				if(e == 2){
					setTimeout(() => {
						this.register()
					},500)
				}
			},
			// 判断开始扫描的类型
			startScan() {
				if (!this.type) {
					this.$modal.msg("请先选择扫描类型！");
					return
				}
				if (this.type == 1) {
					this.starting = true
					this.setLoggerEnable()
				}else{
					this.$modal.msg("请选择扫描类型为RFID！");
					return
				}
				// else {
				// 	this.register()
				// }
			},
			register() {
				var _this = this;
				jpushModuleRelocationMaterials.scannerRegister({
					name: "com.uhf.scanner"
				}, function(result) {
					_this.scannerResult = result
					console.log(1111, result);
					let scannerdata = _this.scannerResult.scannerdata.trim()
					this.$http.getAction('/wms/api/stock/getStockListByMaterial', {
						rfid: scannerdata
					}).then(res => {
						console.log(666666,res)
						this.materialList = res.data
					})
				})
			},
			stop() {
				jpushModuleRelocationMaterials.cardSearchStop()
				uni.showToast({
					icon: 'none',
					title: '停止了',
					duration: 3000
				})
			},
			async setLoggerEnable() {
				let _this = this
				jpushModuleRelocationMaterials.inventoryStart();
				jpushModuleRelocationMaterials.cardSearchContinue((result) => {
					console.log(111111111, result)
					_this.starting = false
					_this.$http.getAction('/wms/api/stock/getStockListByMaterial', {
						rfid: result.readValues[0].id
					}).then(res => {
						console.log(666666,res)
						_this.materialList = res.data
					})

				})
			},
		}
	}
</script>

<style scoped lang="scss">
	/deep/.u-tdsty{
		word-wrap: break-all;
		word-break: break-all;
	}
	.scanBox {
		background-color: #fff;
		padding: 45rpx 20rpx;

		.titleBox {
			display: flex;
			align-items: center;

			.line {
				width: 6rpx;
				height: 36rpx;
				background: #1948B2;
				border-radius: 3rpx;
			}

			.text {
				font-size: 36rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #1948B2;
				margin-left: 10rpx;
			}
		}

		.abilityBtn {
			margin-top: 45rpx;
			display: flex;
			justify-content: space-around;

			image {
				width: 255rpx;
				height: 255rpx;
			}
		}

		.startBtn {
			width: 162rpx;
			height: 72rpx;
			background-size: 100% 100%;
			margin-left: 100rpx;
			margin-top: 20rpx;
			background-image: url(@/static/img/startScan.png);
			font-size: 33rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 72rpx;
			text-align: center;
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
	}
</style>