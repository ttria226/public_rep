<template>
	<view class="app">
		<headTitle :isRight="true">
			<view slot="content">巡检保修</view>
		</headTitle>
		<view class="content">
			<tap :dataList="dataList" :state="state"></tap>
			<view class="centre">
				<view class="contre_title">
					<image src="../../static/images/equipment/cion.png" alt="">
						<text class="contre_title_fontSize">
							{{locationInfo.area||''}}
						</text>
				</view>
				<uni-forms label-position="top" ref="form" :modelValue="formData" :rules="rules">
					<updateCompent @imgDate="imgDate"></updateCompent>
					<uni-forms-item label="故障描述" name="remark">
						<uni-easyinput type="textarea" v-model="formData.remark" placeholder="请输入..." />
					</uni-forms-item>
					<uni-section title="故障等级" type="line">
						<uni-data-select v-model="formData.equ_fault_lv" placeholder="故障等级"
							v-for="dict in faultLvOption" :key="dict.dictValue" :text="dict.dictLabel"
							:value="dict.dictValue" :clear="true"></uni-data-select>
					</uni-section>
					<uni-section title="设备状态" type="line">
						<uni-data-select v-model="formData.equipment_type" placeholder="设备状态"
							v-for="dict in equipmentTypeOption" :key="dict.dictValue" :text="dict.dictLabel"
							:value="dict.dictValue" :clear="true"></uni-data-select>
					</uni-section>
					<uni-section style="padding-bottom: 20px;" title="是否停机" type="line">
						<uni-data-select v-model="formData.isShutdown" placeholder="是否停机" :localdata="range2"
							@change="change" :clear="false"></uni-data-select>
					</uni-section>
				</uni-forms>
			</view>
		</view>
		<view class="submit_botton">
			<button style="margin: 10px 26px;" @click="submit" type="primary">提交</button>
		</view>
	</view>
</template>

<script>
	import {
		getDicts
	} from "@/api/system/data";
	import {
		getToken
	} from '@/utils/auth'
	import tap from "@/pages/equipment/subassembly/tap.vue"
	import updateCompent from "@/pages/equipment/subassembly/updateCompent.vue";
	export default {
		components: {
			tap,
			updateCompent
		},
		data() {
			return {
				locationInfo: {}, //地址信息
				//子组件传值
				dataList: [],
				state: 2,
				value: 1,
				faultLvOption: [], //故障等级
				equipmentTypeOption: [],
				range2: [{
						value: 1,
						text: "是"
					},
					{
						value: 2,
						text: "否"
					}
				],
				fileList: [],
				// 表单数据
				formData: {
					imgArr: [],
					faultMessage: '',
					equipmentId: '',
					faultLv: '',
					equipmentStatus: '',
					isShutdown: '',
					address: '',
					latAndLon: ""
				},
				getDictsDate: [],
				//故障等级数据
				cityData: [{
						text: '北京',
						value: '10001',
					},
					{
						text: '上海',
						value: '10002',
					},
					{
						text: '深圳',
						value: '10004',
					},
				],
				//正则
				rules: {
					// 对name字段进行必填验证
					name: {
						rules: [{
								required: true,
								errorMessage: '请输入图片',
							},
							{
								minLength: 3,
								maxLength: 5,
								errorMessage: '姓名长度在 {minLength} 到 {maxLength} 个字符',
							}
						]
					},
				},
			}
		},
		//路由跳转接受的参数
		onLoad(option) {
			if (option.info != undefined) {
				let userInfo = JSON.parse(decodeURIComponent(option.info));
				this.dataList = userInfo
				// this.formData.dayId = userInfo.dayId
				this.formData.equipmentId = userInfo.id
			}
			this.getLocationFun()
			this.getDicts("equ_fault_lv").then(response => {
				this.faultLvOption = response.data;
			});
			this.getDicts("equipment_type").then(response => {
				this.equipmentTypeOption = response.data;
			});
		},
		created() {},
		methods: {
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
						that.locationInfo.area = res.address.province + res.address.city + res.address
							.district + res.address.street + res.address.poiName
						console.log(that.locationInfo)
					},
					fail: (rej) => {
						console.log('我是获取地址的错误信息', rej)
					}
				});
			},
			//子组件传过来的方法
			imgDate(i) {
				console.log(i)
				this.formData.imgArr.push(i)
			},
			change(e) {},
			//提交
			submit() {
				this.$refs.form.validate().then(res => {
					this.$http.postAction("/wms/day/addRepair", this.formData).then(res => {
						uni.showToast({
							icon: 'success',
							title: '保存成功'
						})
						uni.navigateBack();
					})
				}).catch(err => {
					console.log('表单错误信息：', err);
				})
			}
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style lang="scss">
	.app {
		width: 100%;
		height: 100vh;
		position: relative;

		.content {
			width: 100%;
			height: 90%;
			padding: 18rpx 22rpx;
			box-sizing: border-box;
			background: #F5F5F5;
			overflow: scroll;

			.tap {
				box-sizing: border-box;
				padding: 0px 26rpx;
				background-image: url("../../static/images/equipment/detailbackground.png");
				width: 100%;
				height: 312rpx;

				.tap_title {
					// width: 205px;
					padding-top: 28rpx;
					height: 40rpx;
					font-size: 42rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #FFFFFF;
				}

				.tap_bottom {
					padding-top: 30rpx;
					// width: 212px;
					// height: 93px;
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #FFFFFF;

					// line-height: 13px;
					view {
						margin: 16rpx 0rpx 4rpx 0rpx;
					}
				}
			}

			.centre {
				padding: 4rpx 26rpx;
				box-sizing: border-box;
				// width: 393px;
				// height: 355px;
				background: #FFFFFF;
				border-radius: 16rpx;

				.contre_title {
					padding: 40rpx 0rpx 46rpx 0rpx;
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #0B0B0B;

					image {
						width: 38rpx;
						height: 24rpx;
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

			/deep/.uni-select {
				margin: 16rpx -14rpx;
				height: 108rpx;
				border: 1px solid #CFD1D2;
				border-radius: 8rpx;
			}

			/deep/.uni-select__input-text {
				font-size: 40rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #333333;
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
			line-height: 132rpx;
			height: 132rpx;
			background: #FFFFFF;
		}
	}
</style>