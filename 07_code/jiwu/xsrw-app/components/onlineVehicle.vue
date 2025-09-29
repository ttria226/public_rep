<template>
	<view class="scanBox">
		<view class="titleBox">
			<view style="display: flex;align-items: center;">
				<view class="line"></view>
				<view class="text">请扫描载具标签</view>
			</view>
		</view>
		<view class="abilityBtn">
			<view style="position: relative;">
				<image v-if="flagType" style="position: absolute;top: 0px;left: 0px;z-index: 2;" src="@/static/img/selectedbg.png" alt=""></image>
				<image src="@/static/img/labelBtnIcon.png" alt="" @click="registerBefore()"></image>
			</view>
			<!-- <image src="@/static/img/labelBtnIcon.png" alt="" @click="registerBefore()"></image> -->
		</view>
<!-- 		<view>
			<view class="startBtn" @click="startScan">开始扫描</view>
			<view class="startBtn" @click="stop">停止</view>
		</view> -->
		<view class="tableBox">
			<span>扫描结果：</span><span>{{scannerResult.scannerdata}}</span>
		</view>
		<view class="tableBox" v-if="materialList && materialList.length > 0">
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="34%">物料名称</u-th>
					<u-th width="33%">批次号</u-th>
					<u-th width="33%" v-if="info.type == 1">预计数量</u-th>
					<u-th width="33%" v-if="info.type == 2">实际拣货数量</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in materialList">
					<u-td width="34%" class="u-tdsty">{{item.materialName}}</u-td>
					<u-td width="33%" class="u-tdsty">{{item.batchCode}}</u-td>
					<u-td width="33%" v-if="info.type == 1">{{item.availableCount}}</u-td>
					<u-td width="33%" v-if="info.type == 2">
						<u-input class="input-style" v-model="item.count" type="number" :border="true"  />
					</u-td>
				</u-tr>
			</u-table>
		</view>
	</view>
</template>

<script>
	var jpushModuleOnlineVehicle = uni.requireNativePlugin("Uhf-Plugin")
	export default {
		name: "OnlineVehicle",
		data() {
			return {
				scannerResult:{
					scannerdata:''
				},
				materialList: {}, //扫描结果
				materialCountList:{},//预计数量
				info: {}, // 载具信息
				flagType:false,
			};
		},
		created() {
			
		},
		methods: {
			goPage(url){
				uni.navigateTo({
					url:url
				})
			},
			close(){
				jpushModuleOnlineVehicle.closeDevice()
				plus.key.removeEventListener("keydown")
			},
			registerBefore(){
				this.flagType = true
				jpushModuleOnlineVehicle.openDevice()
				plus.key.addEventListener("keydown", ()=> {
					console.log('哈哈哈哈')
				}, false);
				this.$emit('closeOtherScan','')
				setTimeout(() => {
					this.register()
				},500)
			},
			register() {
				var _this = this;
				jpushModuleOnlineVehicle.scannerRegister({
					name: "com.uhf.scanner"
				}, (result) => {
					console.log(888888888888)
					_this.scannerResult = result
					this.$http.getAction('/wms/api/base/tray/getDeliveryByTrayCode', {
						trayCode: result.scannerdata
					}).then(res => {
						console.log(666666,res)
						this.materialList = res.data.data
						this.info = res.data
						this.$emit('type', res.data.type)
					})
					_this.$emit('registreOk','')
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
			justify-content: space-between;
			.chukuBtn{
				text-align: center;
				width: 170rpx;
				height: 82rpx;
				background: #0075FF;
				font-size: 36rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #FFFFFF;
				border-radius: 7rpx;
				line-height: 82rpx;
			}
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
			}
		}
	}
</style>