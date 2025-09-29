<template>
	<view class="app">
		<headTitle :isRight="true">
			<view slot="content">巡检过程填报</view>
		</headTitle>
		<view class="content" :style="{ 'margin-bottom': type == '3' ? '0rpx' : '120rpx', 'height': type == '3' ? 'calc(100vh - 160rpx)' : 'calc(100vh - 280rpx)' }">
			<tap :dataList="dataList" :state="state" style="margin-bottom: 20rpx;"></tap>
			<view class="centre">
				<view class="contre_title" @click="getLocationFun">
					<image src="../../static/images/equipment/cion.png" alt="">
					<text class="contre_title_fontSize">{{locationInfo.area||''}}</text>
				</view>
				<uni-forms label-position="top" ref="form" :modelValue="formData" :rules="rules">
					<updateCompent :title="'照片/视频'" ref="imgCom" @imgDate="imgDate" :disabled="type == '3'"></updateCompent>
					<uni-forms-item label="巡检说明" name="remark">
						<uni-easyinput type="textarea" v-model="formData.remark" :disabled="type == '3'" placeholder="请输入..." />
					</uni-forms-item>
					<uni-forms-item label="是否报修" name="status">
						<uni-data-select v-model="formData.status" :disabled="type == '3'" placeholder="是否报修" :localdata="isMaintainList" :clear="false"></uni-data-select>
					</uni-forms-item>
					<uni-forms-item label="故障描述" name="faultMessage" v-if="formData.status == 2">
						<uni-easyinput type="textarea" v-model="formData.faultMessage" :disabled="type == '3'" placeholder="请输入..." />
					</uni-forms-item>
					<uni-forms-item label="故障等级" name="faultLv" v-if="formData.status == 2">
						<uni-data-select v-model="formData.faultLv" :disabled="type == '3'" placeholder="故障等级" :localdata="faultLvOption" :clear="true"></uni-data-select>
					</uni-forms-item>
					<uni-forms-item label="设备状态" name="equipmentStatus" v-if="formData.status == 2">
						<uni-data-select v-model="formData.equipmentStatus" :disabled="type == '3'" placeholder="设备状态" :localdata="equipmentTypeOption" :clear="true"></uni-data-select>
					</uni-forms-item>
					<uni-forms-item label="是否停机" name="isShutdown" v-if="formData.status == 2">
						<uni-data-select v-model="formData.isShutdown" :disabled="type == '3'" placeholder="是否停机" :localdata="range2" @change="change" :clear="true"></uni-data-select>
					</uni-forms-item>
				</uni-forms>
			</view>
		</view>
		<view class="submit_botton" v-if="type !== '3'">
			<button @click="submit" type="primary">完成</button>
		</view>
	</view>
</template>

<script>
	import { getPatrolEduDetail } from "@/api/equipment"
	import { getDicts } from "@/api/system/data";
	import { getToken } from '@/utils/auth'
	import tap from "@/pages/equipment/subassembly/tap.vue"
	import updateCompent from "@/pages/equipment/subassembly/updateCompent.vue";
	export default {
		components: {
			tap,
			updateCompent
		},
		data() {
			return {
				range2: [{
						value: 1,
						text: "是"
					},
					{
						value: 0,
						text: "否"
					}
				],
				isMaintainList: [{
						value: 2,
						text: "是"
					},
					{
						value: 1,
						text: "否"
					}
				],
				type: 1,
				locationInfo: {}, //地址信息
				//接受的数据
				tapList: [],
				//子组件传值
				dataList: [],
				state: 1,
				value: "",
				fileList: [],
				// 表单数据
				formData: {
					status: 1,
					img: [],
					faultMessage: '',
					equipmentId: '',
					faultLv: '',
					equipmentStatus: '',
					isShutdown: '',
					address: '',
					latAndLon: ""
				},
				faultLvOption: [], //故障等级
				equipmentTypeOption: [],
				//正则
				rules: {
					// 对name字段进行必填验证
					faultMessage: {
						rules: [{
								required: true,
								errorMessage: '请输入故障描述',
							},
						]
					},
					remark: {
						rules: [{
								required: true,
								errorMessage: '请输入巡检说明',
							},
						]
					},
					faultLv: {
						rules: [{
								required: true,
								errorMessage: '请选择故障等级',
							},
						]
					},
					equipmentStatus: {
						rules: [{
								required: true,
								errorMessage: '请选择设备状态',
							},
						]
					},
					isShutdown: {
						rules: [{
								required: true,
								errorMessage: '请选择是否停机',
							},
						]
					},
					isShutdown: {
						rules: [{
								required: true,
								errorMessage: '请选择是否停机',
							},
						]
					},
				},
			}
		},
		onLoad(option) {
			if (option.info != undefined) {
				let userInfo = JSON.parse(decodeURIComponent(option.info));
				this.formData.dayId = option.dayId
				this.type = option.type
				this.dataList = userInfo
				console.log('我是信息', this.dataList, this.dayId)
				// this.formData.dayId = userInfo.dayId
				this.formData.equipmentId = userInfo.id
				if(option.type == 3){
					this.getInfo()
				}
			}
			this.getDicts("equ_fault_lv").then(response => {
				this.faultLvOption = response.data;
				this.faultLvOption.map(item => {
					item.text = item.dictLabel
					item.value = item.dictValue
				})
			});
			this.getDicts("equipment_type").then(response => {
				this.equipmentTypeOption = response.data;
				this.equipmentTypeOption.map(item => {
					item.text = item.dictLabel
					item.value = item.dictValue
				})
			});
		},
		onShow(){
			if(this.type != 3){
				this.getLocationFun()
			}
		},
		methods: {
			getDicts,
			change(e) {},
			radioChange(e) {},
			// 获取当前手持设备的位置
			getLocationFun() {
				let that = this
				uni.getLocation({
					type: 'gcj02',
					geocode: true,
					isHighAccuracy: true,
					success: function(res) {
						console.log(res)
						that.locationInfo = res
						that.locationInfo.area = res.address.province + res.address.city + res.address.district + res.address.street + res.address.poiName
						console.log(that.locationInfo)
					},
					fail: (rej) => {
						console.log('我是获取地址的错误信息', rej)
					}
				});
			},
			//获取详情
			getInfo(){
				getPatrolEduDetail({ dayId: this.formData.dayId, equipmentId: this.formData.equipmentId }).then(res => {
					this.formData = {...res.data}
					console.log("formData",res.data)
					let fileList = this.formData.img.split(",")
					let fileResultList = []
					fileList.map((item,index) => {
						let info = {
							fileID: index+1,
							url: item
						}
						fileResultList.push(info)
					})
					this.$refs.imgCom.fileList = fileResultList
					let latAndLonList = this.formData.latAndLon.split(",")
					console.log(latAndLonList)
					this.locationInfo.area = this.formData.region
					this.locationInfo.latitude = Number(latAndLonList[0])
					this.locationInfo.longitude = Number(latAndLonList[1])
					// let point = new plus.maps.Point(Number(latAndLonList[1]), Number(latAndLonList[0]));
					// plus.maps.Map.reverseGeocode(point, {},
					// 	function(event) {
					// 		console.log(event, 'event');
					// 		var address = event.address; // 转换后的地理位置
					// 		var point = event.coord; // 转换后的坐标信息
					// 		console.log(address, 'address');
					// 	},
					// 	function(e) {
					// 		console.log('error', e);
					// 	}
					// );
					this.$forceUpdate()
				})
			},
			//子组件传过来的方法
			imgDate(i) {
				console.log('我是图片地址', i)
				this.formData.img.push(i)
			},
			//提交
			submit() {
				console.log('我是表单数据', this.formData)
				if ((this.formData.img && this.formData.img.length == 0) || this.formData.remark == '' || (this.formData.status == 2 && (this.formData.faultMessage === '' || this.formData.faultLv === '' || this.formData.equipmentStatus === '' || this.formData.isShutdown === ''))) {
					uni.showToast({
						icon: 'none',
						title: '请输入数据!'
					})
				} else {
					let formData = {...this.formData}
					let imgStr = this.formData.img.toString()
					formData.img = imgStr
					formData.region = this.locationInfo.area
					formData.latAndLon = this.locationInfo.latitude + ',' + this.locationInfo.longitude
					// if(this.formData.status == 2){
					// 	formData.repairReport = {
					// 		faultMessage: this.formData.faultMessage,
					// 		faultLv: this.formData.faultLv,
					// 		equipmentStatus: this.formData.equipmentStatus,
					// 		isShutdown: this.formData.isShutdown,
					// 	}
					// }
					// delete formData.faultMessage
					// delete formData.faultLv
					// delete formData.equipmentStatus
					// delete formData.isShutdown
					this.$http.postAction("/wms/day/addDayInfo", formData).then(res => {
						uni.showToast({
							icon: 'none',
							title: '保存成功'
						});
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
				}
			}
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style lang="scss" scoped>
	.app {
		width: 100%;
		height: 100vh;
		overflow: hidden;

		.content {
			width: 100%;
			height: calc(100vh - 280rpx);
			padding: 18rpx 22rpx;
			box-sizing: border-box;
			background: #F5F5F5;
			overflow-y: auto;

			.centre {
				padding: 4rpx 26rpx 20rpx 26rpx;
				box-sizing: border-box;
				// width: 393px;
				// height: 355px;
				position: relative;
				z-index: 10;
				background: #FFFFFF;
				border-radius: 16rpx;

				.contre_title {
					padding: 40rpx 0px 46rpx 0px;
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #0B0B0B;

					image {
						width: 27rpx;
						height: 34rpx;
					}

					.contre_title_fontSize {
						margin-left: 18rpx;
					}
				}
			}

			/deep/.uni-forms-item.is-direction-top .uni-forms-item__label {
				// margin-left: 13px !important;
				width: 200rpx !important;
				font-size: 40rpx !important;
				font-family: PingFang SC !important;
				font-weight: bold !important;
				color: #333333 !important;
			}

			/deep/.uni-section .uni-section-header {
				display: none !important;
			}

			/deep/ .input-value-border {
				margin: 16rpx -14rpx;
				width: 760rpx;
				height: 108rpx;
				border: 1px solid #CFD1D2;
				border-radius: 8rpx;
			}

			/deep/ .placeholder {
				font-size: 40rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #333333;
			}
		}

		.submit_botton {
			width: 100%;
			position: absolute;
			bottom: 0px;
			height: 120rpx;
			display: flex;
			justify-content: center;
			align-items: center;
			background: #FFFFFF;
			padding: 40rpx 54rpx;
			button{
				width: 100%;
			}
		}
	}
</style>