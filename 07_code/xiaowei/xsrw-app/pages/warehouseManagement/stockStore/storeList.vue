<template>
	<view>
		<headTitle title="执行盘点"></headTitle>
		<!-- <VehicleList ref="vehicle"></VehicleList> -->
		<view style="width: 100%;height: 10rpx;"></view>
		<paidan ref="paidan"></paidan>
		<view class="btnBox">
			<view class="cancel" @click="cancel">取消</view>
			<view class="submit" @click="submit">提交</view>
		</view>
		<u-modal v-model="equation" content="实盘数量与系统记录库存数量不一致，确定要提交吗？" show-cancel-button :show-title="false" @confirm="confirmSub" @cancel="equation=false"></u-modal>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
	import paidan from '@/components/pandian'
	export default {
		components:{
			paidan,
		},
		data() {
			return {
				equation:false,
				pandianId:null,//盘点单ID
				queryParams: {
					id:null,
					taskDetailCheckVOS: [],
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
				this.getTaskApi()
			}
		},
		methods: {
			//获取盘点单任务详情
			getTaskApi(){
				this.$http.getAction(`/wms/taskApi/${this.queryParams.id}`).then(res=>{
					this.$refs.paidan.materialList=res.data.taskDetailVOList
					this.$forceUpdate()
					console.log('我是盘点单任务详情',res)
				})
			},
			confirmSub(){
				this.queryParams.taskDetailCheckVOS = this.$refs.paidan.materialList
				console.log(this.queryParams)
				this.$http.postAction('/wms/taskApi/check', this.queryParams).then(res => {
					this.$modal.msgSuccess(res.msg);
					setTimeout(()=>{
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
			},
			submit() {
				if (this.$refs.paidan.materialList && this.$refs.paidan.materialList.length > 0) {
					let hasNull = false
					const materialList = this.$refs.paidan.materialList
					for (let i = 0; i < materialList.length; i++) {
						if (!materialList[i].realyNum) {
							this.$refs.uToast.show({
								title: '实盘数量不能为空',
								type: 'error',
							})
							hasNull = true
							return
						}
					}
					if (hasNull) {
						return
					}
					for (let i = 0; i < materialList.length; i++) {
						if(materialList[i].stockNum!=materialList[i].realyNum){
							this.equation=true
							return
						}
					}
					if (this.equation==true) {
						return
					}
					this.confirmSub()
				}
			
			},
			cancel() {
				uni.navigateBack();
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