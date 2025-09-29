<template>
	<view>
		<headTitle title="载具明细"></headTitle>
		<u-sticky>
			<view class="searchBox">
				<image src="@/static/img/vehicleDetailIcon.png" alt=""></image>
				<view style="margin-left: 22rpx;
font-size: 39rpx;
font-family: PingFang SC;
font-weight: bold;
color: #0B0B0B;">{{transfrom(type)}} <span style="margin-left: 20rpx;">{{code}}</span> </view>
			</view>
		</u-sticky>
		<view class="contentBox">
			<view class="infoCard" v-for="(item,index) in orderList">
				<view class="top">物料编码：<span>{{item.materialCode}}</span> </view>
				<view class="top">物料名称：<span>{{item.materialName}}</span></view>
				<view class="bott">
					<view style="width: 100%;">单位：<span style="color:#1948B2;">{{item.unitName}}</span></view>
					<view style="width: 50%;">批号:<span style="color:#1948B2;">{{item.batchCode}}</span></view>
					<view style="width: 50%;">数量:<span style="color:#1948B2;">{{item.count}}</span></view>
				</view>
			</view>
		</view>
		<view style="background-color: #fff;position: fixed;bottom: 30rpx;width: 100%;padding-top: 20rpx;" v-if="page == '1'">
			<view style="width: 650rpx;
				height: 83rpx;
				background: #0075FF;
				border-radius: 8rpx;
				font-size: 39rpx;
				margin-left: 50rpx;;
				font-family: PingFang SC;
				font-weight: bold;
				color: #FFFFFF;
				line-height: 83rpx;text-align: center;" @click="grounding">上架</view>
		</view>
		<view style="background-color: #fff;position: fixed;bottom: 30rpx;width: 100%;padding-top: 20rpx;" v-else-if="palletNum">
			<view v-if="palletNum" style="width: 650rpx;
				height: 83rpx;
				background: #0075FF;
				border-radius: 8rpx;
				font-size: 39rpx;
				margin-left: 50rpx;;
				font-family: PingFang SC;
				font-weight: bold;
				color: #FFFFFF;
				line-height: 83rpx;text-align: center;" @click="chukuSubmit">出库</view>
		</view>
	</view>
</template>

<script>
	import {
		getDicts
	} from "@/api/system/data";
	export default {
		data() {
			return {
				getDicts,
				code: "",
				type: '',
				page: '',
				orderList: [], //物料数据
				option: [],
				id:null,
				palletNum: '',
			}
		},
		async onLoad(e) {
			console.log(e)
			if (e.id) this.id=e.id
			if (e.id) this.getMXInfo(e.id)
			if (e.code) this.code = e.code
			if (e.type) this.type = e.type
			if (e.page) this.page = e.page
			if (e.palletNum) this.palletNum = e.palletNum
			await this.getDicts("wms_t_tray_category").then(response => {
				this.option = response.data;
			});
		},
		methods: {
			chukuSubmit(){
				this.$http.postAction('/wms/api/base/tray/outStock',{
					code:this.code
				}).then(res=>{
					this.$modal.msgSuccess(res.msg);
					setTimeout(() => {
						this.$tab.navigateBack(2);
					}, 1000)
					console.log(res)
				})
			},
			grounding() {
				this.$http.postAction('/wms/api/inout/delivery/executeIn',{
					id:this.id
				}).then(res=>{
					this.$modal.msgSuccess(res.msg);
					setTimeout(() => {
						this.$tab.navigateBack(2);
					}, 1000)
					console.log(res)
				})
			},
			transfrom(e) {
				let dictLabel = ''
				this.option.map(res => {
					if (res.dictValue == e) {
						dictLabel = res.dictLabel
						return
					}
				})
				return dictLabel
			},
			getMXInfo(e) {
				this.$http.getAction('/wms/api/base/tray/getStockById', {
					trayId: e
				}).then(res => {
					this.orderList = res.data
				})
			},
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style scoped lang="scss">
	.searchBox {
		padding: 15rpx 25rpx;
		display: flex;
		align-items: center;

		image {
			width: 76rpx;
			height: 76rpx;
		}
	}

	.contentBox {
		padding: 30rpx;

		.infoCard {
			background-color: #fff;
			border-radius: 14rpx;
			padding: 20rpx;

			.top {
				font-size: 32rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #0B0B0B;
				line-height: 70rpx;
			}

			.bott {
				margin-top: 24rpx;
				padding: 22rpx;
				background: #F4F7F8;
				border-radius: 14rpx;
				display: flex;
				justify-content: space-between;
				flex-wrap: wrap;
				font-size: 32rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #0B0B0B;
				line-height: 70rpx;
			}
		}
	}
</style>