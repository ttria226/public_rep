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
			<view style="position: relative;">
				<image v-if="type == 2" style="position: absolute;top: 0px;left: 0px;z-index: 2;" src="@/static/img/selectedbg.png" alt=""></image>
				<image src="@/static/img/labelBtnIcon.png" alt="" @click="selType(2)"></image>
			</view>
		</view>
		<view>
			<view class="startBtn" v-if="!starting" @click="startScan">开始扫描</view>
			<view class="startBtn" v-if="starting">进行中···</view>
			<!-- <view class="startBtn" @click="startScan">开始扫描</view>
			<view class="startBtn" @click="stop">停止</view> -->
		</view>
		<view class="tableBox">
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="19%">物料名称</u-th>
					<u-th width="16%">批次号</u-th>
					<u-th width="13%">rfid</u-th>
					<u-th width="23%">未通过原因</u-th>
					<u-th width="19%">备注</u-th>
					<u-th width="10%">操作</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in materialList">
					<u-td width="19%" class="u-tdsty">{{item.materialName}}</u-td>
					<u-td width="16%" class="u-tdsty">{{item.batchCode}}</u-td>
					<u-td width="13%" class="u-tdsty">{{item.rfid}}</u-td>
					<u-td width="23%">
						 <uni-data-select v-model="item.detectionFailType" :localdata="fileOption"></uni-data-select>
							<!-- <uni-data-select v-model="item.detectionFailType" placeholder="故障等级"
								v-for="dict in fileOption" :key="dict.dictValue" :text="dict.dictLabel"
								:value="dict.dictValue" :clear="true"></uni-data-select> -->
					</u-td>
					<u-td width="19%">
						<u-input class="input-style" v-model="item.detectionFailRemark" :border="true"  />
					</u-td>
					<u-td width="10%"><view @click.prevent="handleCancel(item,index)">删除</view></u-td>
				</u-tr>
			</u-table>
		</view>
	</view>
</template>

<script>
	import {
		getDicts
	} from "@/api/system/data";
	var jpushModuleJiance = uni.requireNativePlugin("Uhf-Plugin")
	export default {
		name: "jiance",
		props: {
			typeName: {
				type: String,
				default: "入库"
			},
		},
		data() {
			return {
				trayCode:null,
				type: null, //扫描类型
				materialList: [], //物料信息
				materialCountList: {}, //预计数量
				fileOption: [], // 未通过原因选项列表
				retult: [], //扫描结果
				starting:false,
			};
		},
		created() {
			getDicts("in_delivery_detection_fail_type").then(response => {
				this.fileOption = response.data;
				this.fileOption.map(item => {
					item.text = item.dictLabel
					item.value = item.dictValue
				})
			});

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
			close(){
				jpushModuleJiance.closeDevice()
				plus.key.removeEventListener("keydown")
			},
			selType(e) {
				this.type = e
				if(this.type ==1 && this.starting){
					return
				}
				this.starting = false
				this.$modal.msg(`当前选择的类型为${e==1?'RFID':'标签'}！`);
				this.$emit('closeOtherScan','')
				this.$nextTick(() => {
					jpushModuleJiance.openDevice()
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
			//删除扫描信息
			handleCancel(item,index){
				this.materialList.splice(index,1)
			},
			register() {
				var _this = this;
				jpushModuleJiance.scannerRegister({
					name: "com.uhf.scanner"
				}, function(result) {
					_this.scannerResult = result
					console.log(1111, result);
					let scannerdata = _this.scannerResult.scannerdata.trim()
					let resultInfo = { id: scannerdata, count: 1, len: 12, rssi: -42, embeded: "" }
					let index = _this.retult.findIndex((item) => { return item.id === resultInfo.id })
					if(index === -1){
						_this.retult.push(resultInfo)
					}
					_this.$http.postAction('/wms/api/base/material/detail/alllist', {
						tMaterialDetailDTOS: _this.retult
					}).then(res => {
						console.log(666666,res)
						let materialList = [..._this.materialList]
						let list = []
						let resultList = [..._this.materialList]
						res.data.map(ret=>{
							let materialInfo = {...ret}
							delete materialInfo.rfid
							ret.rfids.map(rej=>{
								let info = {...materialInfo,rfid: rej}
								list.push(info)
							})
						})
						list.map((item) => {
							let info = materialList.find((materialInfo) => { return materialInfo.rfid == item.rfid })
							if(!info){
								resultList.push(item)
							}
						})
						_this.materialList = resultList
						_this.$forceUpdate()
						console.log('我是遍历后的值',_this.materialList)
					})
				})
			},
			stop() {
				jpushModuleJiance.cardSearchStop()
				uni.showToast({
					icon: 'none',
					title: '停止了',
					duration: 3000
				})
			},
			async setLoggerEnable() {
				let _this = this
				jpushModuleJiance.inventoryStart();
				jpushModuleJiance.cardSearchContinue((result) => {
					console.log('我是扫出来的物料信息',result)
					_this.retult = result
					_this.$http.postAction('/wms/inout/detail/alllist', {
						tMaterialDetailDTOS: result.readValues
					}).then(res => {
						console.log(666666,res)
						let materialList = [..._this.materialList]
						let list = []
						let resultList = [..._this.materialList]
						res.data.map(ret=>{
							let materialInfo = {...ret}
							delete materialInfo.rfid
							ret.rfids.map(rej=>{
								let info = {...materialInfo,rfid: rej}
								list.push(info)
							})
						})
						list.map((item) => {
							let info = materialList.find((materialInfo) => { return materialInfo.rfid == item.rfid })
							if(!info){
								resultList.push(item)
							}
						})
						_this.materialList = resultList
						_this.starting = false
						_this.$forceUpdate()
						console.log('我是遍历后的值',_this.materialList)
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
	/deep/.uni-select{
		width: 90%;
		padding-left: 10rpx;
	}
	/deep/.uni-select__input-box{
		width: 100%;
		flex: none;
	}
	/deep/.uni-select__selector{
		width: 200rpx;
		white-space: nowrap;
	}
	/deep/.uni-select__input-text{
		width: 110rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
</style>