<template>
	<view class="scanBox">
		<view class="titleBox">
			<view style="display: flex;align-items: center;">
				<view class="line"></view>
				<view class="text">请扫描载具标签</view>
			</view>
			<view class="chukuBtn"  v-if="isChuku" @click="goPage('/pages/warehouseManagement/putInStorage/VehicleOutbound')">载具出库</view>
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
			<span style="color:red;" v-if="trayMessage">（{{trayMessage}}）</span>
		</view>
	</view>
</template>

<script>
	var jpushModuleVehicle = uni.requireNativePlugin("Uhf-Plugin")
	export default {
		name: "scanMaterials",
		props: {
			isChuku: {
				type: Boolean,
				default:true
			},
		},
		data() {
			return {
				scannerResult:{
					scannerdata:''
				},
				materialList: {}, //扫描结果
				materialCountList:{},//预计数量
				flagType:false,
				trayMessage:'',//托盘错误信息
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
				jpushModuleVehicle.closeDevice()
				plus.key.removeEventListener("keydown")
			},
			registerBefore(){
				this.flagType = true
				jpushModuleVehicle.openDevice()
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
				jpushModuleVehicle.scannerRegister({
					name: "com.uhf.scanner"
				}, function(result) {
					console.log(888888888888)
					_this.scannerResult = result
					_this.$emit('registreOk','')
					console.log(result.scannerdata)
					_this.$http.getAction('/wms/api/base/tray/getStatusByCode?trayCode='+result.scannerdata,{}).then(res => {
						console.log(res)
						if(res.msg){
							_this.trayMessage = res.msg
						}else{
							_this.trayMessage =''
						}
					})
				})
			},
		}
	}
</script>

<style scoped lang="scss">
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