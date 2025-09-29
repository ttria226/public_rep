<template>
	<view>
		<headTitle title="扫码检测"></headTitle>
		<!-- <VehicleList ref="vehicle"></VehicleList> -->
		<view style="width: 100%;height: 10rpx;"></view>
		<jiance ref="jiance"></jiance>
		<view class="btnBox">
			<view class="cancel" @click="cancel">取消</view>
			<view class="submit" @click="submit">提交</view>
		</view>
		<u-modal v-model="equation" content="实盘数量与系统记录库存数量不一致，确定要提交吗？" show-cancel-button :show-title="false" @confirm="confirmSub" @cancel="equation=false"></u-modal>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
	import jiance from '@/components/jiance'
	export default {
		components:{
			jiance,
		},
		data() {
			return {
				equation:false,
				pandianId:null,//盘点单ID
				queryParams: {
					id:null,
					tMaterialDetailList: [],
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
			}
		},
		onLoad(e){
			if(e.id){
				this.queryParams.id=e.id
				// this.getTaskApi()
			}
		},
		methods: {
			//获取盘点单任务详情
			getTaskApi(){
				this.$http.getAction(`/wms/taskApi/${this.queryParams.id}`).then(res=>{
					this.$refs.jiance.materialList=res.data.taskDetailVOList
					this.$forceUpdate()
					console.log('我是盘点单任务详情',res)
				})
			},
			confirmSub(){
				let materRfidList = []
				this.$refs.jiance.materialList.map((item) => {
					let info = {
						rfid: item.rfid,
						detectionFailType: item.detectionFailType,
						detectionFailRemark: item.detectionFailRemark
					}
					materRfidList.push(info)
				})
				this.queryParams.tMaterialDetailList = materRfidList
				console.log(this.queryParams)
				this.$http.postAction('/wms/api/inout/delivery/checkMaterial', this.queryParams).then(res => {
					this.$modal.msgSuccess(res.msg);
					setTimeout(()=>{
						this.$tab.navigateBack(1);
					}, 1000)
				})
			},
			submit() {
				if (this.$refs.jiance.materialList && this.$refs.jiance.materialList.length > 0) {
					let hasNull = false
					const materialList = this.$refs.jiance.materialList
					for (let i = 0; i < materialList.length; i++) {
						if (!materialList[i].detectionFailType) {
							this.$refs.uToast.show({
								title: '未通过原因不能为空',
								type: 'error',
							})
							hasNull = true
							return
						}
					}
					if (hasNull) {
						return
					}
					console.log(materialList)
					this.confirmSub()
				} else {
					this.$refs.uToast.show({
						title: '请扫描物料条码',
						type: 'error',
					})
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
</style>