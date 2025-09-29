<template>
	<view class="app">
		<headTitle :isRight="true">
			<view slot="content">保养任务</view>
		</headTitle>
		<view class="content">
			<tap :dataList="dataList" :state="state" :skip="skip"></tap>
			<view class="centre">
				<view class="contre_title" @click="getLocationFun">
					<image src="../../static/images/equipment/cion.png" alt="">
					<text class="contre_title_fontSize">{{locationInfo.area||''}}</text>
				</view>
				<uni-forms label-position="top" ref="formData" :modelValue="formData" :rules="rules">
					<updateCompent :title="'保养前（照片/视频）'" ref="imgCom" @imgDate="imgDate"></updateCompent>
					<updateCompentCopy :title="'保养后（照片/视频）'" ref="imgCopyCom" @imgDateCopy="imgDateCopy"></updateCompentCopy>
					<view class="input_gai">
						<view class="input_size">
							保养费用
						</view>
						<uni-easyinput class="uni_input" type="text" v-model="formData.price" placeholder="请输入" />
					</view>
					<view class="input_gai">
						<view class="input_size">
							消耗物料
						</view>
						<uni-easyinput class="uni_input" type="text" v-model="formData.material" placeholder="请输入" />
					</view>
					<uni-forms-item label="其他" name="remark">
						<uni-easyinput style="border: 1px solid #CFD1D2;" type="textarea" v-model="formData.remark" placeholder="请输入..." />
					</uni-forms-item>
					<uni-forms-item label="是否外协" name="isExternal">
						<uni-data-select v-model="formData.isExternal" placeholder="是否外协" :localdata="range2" @change="change" :clear="true"></uni-data-select>
					</uni-forms-item>
					<view class="input_gai" v-if="formData.isExternal == '1'">
						<view class="input_size">
							外协单位
						</view>
						<uni-easyinput class="uni_input" type="text" v-model="formData.externalCompany" placeholder="请输入" />
					</view>
				</uni-forms>
			</view>
		</view>
		<view class="submit_botton">
			<button @click="submit('formData')" type="primary">完成</button>
		</view>
	</view>
</template>

<script>
	import tap from "@/pages/equipment/subassembly/maTap.vue"
	import updateCompent from "@/pages/equipment/subassembly/updateCompent.vue";
	import updateCompentCopy from "@/pages/equipment/subassembly/updateCompentCopy.vue";
	export default {
		components: {
			tap,
			updateCompent,
			updateCompentCopy
		},
		data() {
			return {
				titles: "保养前 (照片/视频)",
				//子组件传值
				dataList: [],
				state: 1,
				skip: 1,
				value: "",
				range2: [{
						value: 1,
						text: "是"
					},
					{
						value: 0,
						text: "否"
					}
				],
				locationInfo: {}, //地址信息
				// 表单数据
				formData: {
					id: '',
					beforeImg: '',
					afterImg: '',
					price: '',
					material: '',
					isExternal: 0,
					externalCompany: '',
					remark: ''
				},
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
					price: {
						required: true,
						errorMessage: '请填写保养费用',
					},

				},
			}
		},
		onLoad(option) {
			if (option.info != undefined) {
				let userInfo = JSON.parse(decodeURIComponent(option.info));
				this.dataList = userInfo
				this.formData.id = userInfo.dayid
			}
		},
		onReady() {
			// 需要在onReady中设置规则
			this.$refs.formData.setRules(this.rules)
		},
		onShow(){
			this.getLocationFun()
		},
		methods: {
			change(e) {},
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
			//子组件传过来的方法
			imgDate(i) {
				console.log(i)
				this.formData.beforeImg = i
			},
			imgDateCopy(i) {
				this.formData.afterImg = i
			},
			//提交
			submit(form) {
				this.$refs.formData.validate().then(res => {
					if ((this.formData.beforeImg && this.formData.beforeImg.length == 0) || (this.formData.afterImg && this.formData.afterImg.length == 0) || this.formData.price == '' || (this.formData.isExternal && this.formData.externalCompany == '') || this.formData.material == '' || this.formData.remark == '') {
						uni.showToast({
							icon: 'none',
							title: '请输入数据!'
						})
					} else {
						this.$http.putAction("/wms/day/startBy", this.formData).then(res => {
							console.log(res, "111")
							if (res.code == 200) {
								uni.showToast({
									icon: 'success',
									title: '保存成功'
								})
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
							} else {
								uni.showToast({
									icon: 'none',
									title: '保存失败'
								})
							}
						})
					}
				}).catch(err => {
					console.log('表单错误信息：', err);
				})
				console.log(this.formData)
				// this.$http.putAction("/wms/day/startBy", this.formData).then(res => {
				// 	console.log(res, "111")
				// 	if (res.code == 200) {
				// 		uni.showToast({
				// 			icon: 'success',
				// 			title: '保存成功'
				// 		})
				// 		uni.navigateBack();
				// 	} else {
				// 		uni.showToast({
				// 			icon: 'none',
				// 			title: '保存失败'
				// 		})
				// 	}
				// })
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
		overflow: hidden;

		.content {
			width: 100%;
			height: calc(100vh - 280rpx);
			padding: 18rpx 22rpx;
			box-sizing: border-box;
			background: #F5F5F5;
			overflow-y: auto;

			.tap {
				background-size: 100% 100%;
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
						margin: 16rpx 0px 4rpx 0px;
					}
				}
			}

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

				.input_gai {
					padding: 0px 0.4375rem;
					margin: 10rpx 0;
					// width: 380px;
					height: 108rpx;
					border: 1px solid #CFD1D2;
					border-radius: 8rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;

					.input_size {
						// width: 79px;
						// height: 19px;
						font-size: 40rpx;
						font-family: PingFang SC;
						font-weight: bold;
						color: #333333;
					}

					/deep/ .uni_input {
						width: 200rpx !important;
						display: flex !important;
						flex: 0.3 !important;
					}

					.uni-easyinput__placeholder-class {
						width: 108rpx;
						// height: 17px;
						font-size: 36rpx;
						font-family: PingFang SC;
						font-weight: 500;
						color: #909294;
					}

					/deep/.is-input-border {
						border: none !important;
					}
				}
			}

			/deep/.uni-forms-item.is-direction-top .uni-forms-item__label {
				// margin-left: 13px !important;
				width: 400rpx !important;
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
				// border: 1px solid #CFD1D2;
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