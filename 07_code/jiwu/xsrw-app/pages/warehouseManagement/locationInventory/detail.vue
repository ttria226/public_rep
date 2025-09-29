<template>
	<view>
		<headTitle title="详情"></headTitle>
		<!-- <header>
			<view class="header_tap">
				<u-dropdown>
					<u-dropdown-item v-model="searchType" :title="searchTypes[searchType].label" :options="searchTypes" @change="searchTypeChange"></u-dropdown-item>
				</u-dropdown>
				<u-search v-if="searchType == 0" shape="square" placeholder="请输入物料编码" v-model="queryParams.materialCode" @custom="searchBank"
					@search="searchBank"></u-search>
				<u-search v-if="searchType == 1" shape="square" placeholder="请输入物料名称" v-model="queryParams.materialName" @custom="searchBank"
					@search="searchBank"></u-search>
			</view>
		</header> -->
		<view class="content">
			<!-- <view class="navTitle_All">
				<view class="navTitle" v-for="(item,index) in navList" :key="index">
					<view :class="{'active':isActive === item.index}" @click="checked(item.index)">
						{{item.title}}
					</view>
				</view>
			</view> -->
			<view class="nav_item_All">
				<view class="nav_item2">
					<view v-for="(item,index) in liatDate" :key="index" :class="[isActive1 == index ? 'nav_item_cart' : 'nav_item_cart1']" @click="checked1(item,index)">
						<view class="cart_wapper">
							<view class="nav_item_cart_tap">
								<text class="nav_item_cart_tap_left"></text>
								<text class="nav_item_cart_tap_right" style="display: flex;align-items: center;">
									任务单号：
								</text>
								<text class="nav_item_cart_tap_right"
									style="width: 450rpx;overflow: hidden;text-overflow: ellipsis;">{{item.taskCode}}</text>
							</view>
							<view class="nav_item_cart_fontSize">
								状态：{{item.taskStatusName}}
							</view>
							<view class="nav_item_cart_fontSize">
								<span>载具： {{item.trayCode}}</span>
							
							</view>
							<view class="nav_item_cart_fontSize">
								物料编码：{{item.materialCode}}
							</view>
							<view class="nav_item_cart_fontSize">
								物料名称：{{item.materialName}}
							</view>
							<view class="nav_item_cart_fontSize">
								批次号：{{item.batchNumber}}
							</view>
							<!-- <view class="nav_item_cart_fontSize">
								区域：{{item.areaName}}
							</view> -->
							<view class="nav_item_cart_fontSize">
								库区：{{item.reservoirName}}
							</view>
							<view class="nav_item_cart_fontSize">
								库位：{{item.locationName}}
							</view>
							<view class="nav_item_cart_fontSize" v-if="item.predictCount">
								库存数量：{{item.predictCount}}
							</view>
							<view class="nav_item_cart_fontSize" v-if="item.actualCount">
								盘点数量：{{item.actualCount}}
							</view>
							<view class="nav_item_cart_fontSize" v-if="item.checkDifferenceCount">
								盘差：{{item.checkDifferenceCount > 0 ? `+${item.checkDifferenceCount}` : item.checkDifferenceCount}}
							</view>
							<view class="nav_item_cart_fontSize" v-if="item.checkCount!=0">
								复盘：{{item.checkCount}}
							</view>
					<!-- 		<view class="nav_item_cart_fontSize" v-if="item.status!=2">
								<button style="width: 100%;" class="button" type="primary" @click="goPage('./storeList?id='+queryParams.taskId+'&trayCode=' + item.trayCode+'&checkType='+item.checkType+'&deliveryType='+item.deliveryType,item.status)">执行盘点</button>
							</view> -->
						</view>
					</view>
					<!-- <view class="nav_item_bottom" v-show="this.queryParams.taskStatus == 0">
						<button style="width: 95%;" class="button" type="primary"
							@click="goPage('./storeList?id='+selItem.id)">执行盘点</button>
					</view> -->
					<view class="nav_item_bottom" >
						<button style="width: 50%;margin-right:30rpx;float: right;" class="button" type="primary" v-if="liatDate[0].taskStatus!=1"
							@click="zjClick(liatDate[0])">载具出库</button>
						<button style="width: 50%;" class="button" type="primary" @click="goPage('./storeList?id='+queryParams.taskId+'&trayCode=' + liatDate[0].trayCode+'&checkType='+liatDate[0].checkType+'&deliveryType='+liatDate[0].deliveryType)">执行盘点</button>
					</view>
				</view>
			</view>
			<u-empty text="数据为空" mode="list" v-show="liatDate.length <= 0"></u-empty>
		</view>
	</view>
</template>

<script>
	import {
		showConfirm
	} from "@/utils/common.js"
	export default {
		data() {
			return {
				selItem:{},//选中的库存信息
				isActive: 0,
				isActive1: null,
				queryParams:{
					materialCode:null,
					materialName: null,
					// taskStatus: 0,
					pageSize:100,
					pageNum:1,
				},
				liatDate: [],
				navList: [{
					index: 0,
					title: '未执行'
				}, {
					index: 1,
					title: '进行中'
				}, {
					index: 2,
					title: "已完成"
				}],
				total:0,
				searchTypes: [
					{
						label: '物料编码',
						value: 0,
					},
					{
						label: '物料名称',
						value: 1,
					},
				],
				searchType: 0,
			}
		},
		onLoad(opt) {
			console.log(11113)
			this.queryParams.taskId = opt.id || ''
			this.getApi()
		},
		onReachBottom(){
			if(this.liatDate.length<this.total){
				this.queryParams.pageNum++
				this.getApi()
			}
		},
		onUnload(){
			this.queryParams.pageNum=1
			this.liatDate=[]
		},
		methods: {
			//载具出入库
			zjClick(row){
				this.showConfirm('是否确认该条数据载具出库？').then(rek=>{
					this.$http.getAction('wms/tray/takeOut/checkNew',{
						id:row.trayId,
						taskId:row.id
					}).then(res=>{
						uni.showToast({
							icon: 'none',
							title: '出库成功!'
						})
						this.reFresh()
					})
				})
			},
			//刷新
			reFresh(){
				console.log('ssss')
				this.queryParams.pageNum=1
				this.getApi('load')
			},
			getApi(type) {
				this.$http.getAction("/wms/taskApi/checkList/detail", this.queryParams).then(res => {
					console.log(res, "111")
					console.log(this.queryParams, "222")
					// this.liatDate = this.liatDate=[...this.liatDate,...res.rows]
					res.rows.map((item) => {
						let statusName = this.navList.find((navInfo) => { return navInfo.index == item.status })
						item.taskStatusName = statusName ? statusName.title : ""
					})
					if(this.liatDate.length < res.total){
						this.liatDate = [...this.liatDate,...res.rows]
					} else if(this.liatDate.length > res.total || type == 'load'){
						this.liatDate = res.rows
					}
					this.total=res.total
				})
			},
			checked(index) {
				console.log(index)
				this.isActive = index
				this.queryParams.taskStatus=index
				this.liatDate=[]
				this.queryParams.pageNum=1
				this.getApi()
			},
			checked1(item,index) {
				// this.selItem=item
				// console.log(this.selItem,index)
				// this.isActive1 = index
			},
			goPage(url,status) {
				// if(!this.selItem.id){
				// 	uni.showToast({
				// 		icon: 'none',
				// 		title: '请选择任务！'
				// 	});
				// 	return
				// }
				if(status == 2){
					uni.showToast({
						icon: 'none',
						title: '此任务已完成！'
					});
					return
				}
				uni.navigateTo({
					url: url
				})
				// if(this.isActive1 != null){
				// 	uni.navigateTo({
				// 		url: url
				// 	})
				// }else{
				// 	uni.showToast({
				// 		icon: 'none',
				// 		title: '请先选择数据!'
				// 	})
				// }
			},
			searchBank(value) {
				// this.from.materialCode = value
				this.liatDate = []
				this.total=0
				this.queryParams.pageNum=1
				this.getApi()
			},
			searchTypeChange(value) {
				this.queryParams.materialCode = null
				this.queryParams.materialName = null
				this.searchBank()
			},
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style lang="scss">
	.header_tap {
		display: flex;
		align-items: center;
		margin-left: 24rpx;
		width: 688rpx;
		height: 92rpx;
		background: #F4F6F8;
		border-radius: 4rpx;

		text {
			// width: 72rpx;
			// height: 36rpx;
			margin: 0px 20rpx;
			font-size: 36rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #0B0B0B;
		}
	}

	.content {
		padding-bottom: 140rpx;
		width: 100%;
		height:calc(100vh - 90rpx - 92rpx);
		background: #F5F5F5;

		.active {
			color: #0075FF;
		}

		.active1 {
			bbackground: #0075FF;
		}

		.navTitle_All {
			display: flex;
			align-items: center;
			justify-content: center;
			height: 115rpx;
			background: #FFFFFF;

			.navTitle {
				width: 50%;
				text-align: center;
			}
		}

		.nav_item_All {
			width: 100%;
			// height: 80vh;
			height: 100%;
			padding: 30rpx 30rpx;
			box-sizing: border-box;

			.nav_item {
				width: 100%;
				height: 100%;
				padding-bottom: 132rpx;
				position: relative;
				overflow: scroll;

				// ma: 20rpx 20rpx;
				.nav_item_cart {
					margin: 16rpx 0rpx;
					// width: 785rpx;
					min-height: 353rpx;
					background: #FFFFFF;
					border-radius: 15rpx;

					.nav_item_cart_tap {
						display: flex;
						align-items: center;
						padding-top: 40rpx;

						.nav_item_cart_tap_left {
							display: inline-block;
							margin: 0rpx 6rpx;
							width: 6rpx;
							height: 40rpx;
							background: #FFFFFF;
							border-radius: 3rpx;
						}

						.nav_item_cart_tap_right {
							padding-left: 15rpx;
							// width: 534rpx;
							// height: 35rpx;
							font-size: 37rpx;
							font-family: PingFang SC;
							font-weight: bold;
							color: #FFFFFF;
						}
					}

					.nav_item_cart_fontSize {
						margin: 10rpx 25rpx;
						// width: 729rpx;
						// height: 94rpx;
						font-size: 35rpx;
						font-family: PingFang SC;
						font-weight: 500;
						color: #FFFFFF;
						// line-height: 30rpx;
					}

					.nav_item_cart_bottom {
						margin: 0rpx 20rpx;
						display: flex;
						align-items: center;
						justify-content: space-around;
						// width: 744rpx;
						margin-top: 26rpx;
						height: 100rpx;
						background: #F4F7F8;
						opacity: 0.3;
						border-radius: 15rpx;

						view {
							// width: 606rpx;
							// height: 34rpx;
							font-size: 35rpx;
							font-family: PingFang SC;
							font-weight: 500;
							color: gray;
							// line-height: 21rpx;
						}
					}
				}

				.nav_item_cart1 {
					margin-top: 16rpx;
					// width: 785rpx;
					min-height: 353rpx;
					background: #FFFFFF;
					border-radius: 15rpx;
				}
			}

			.nav_item2 {
				padding-bottom: 132rpx;
				position: relative;
				overflow-y: auto;

				.cart_wapper {
					line-height: 70rpx;
				}

				.nav_item_cart1 {
					margin: 16rpx 0rpx;
					margin-top: 16rpx;
					padding-bottom: 20rpx;
					// width: 785rpx;
					// height: 353rpx;
					background: #FFFFFF;
					border-radius: 15rpx;

					.nav_item_cart_tap {
						display: flex;
						align-items: center;
						padding-top: 40rpx;

						.nav_item_cart_tap_left {
							display: inline-block;
							margin: 0rpx 6rpx;
							width: 6rpx;
							height: 40rpx;
							background: #1948B2;
							border-radius: 3rpx;
						}

						.nav_item_cart_tap_right {
							padding-left: 15rpx;
							// width: 534rpx;
							// height: 35rpx;
							font-size: 37rpx;
							font-family: PingFang SC;
							font-weight: bold;
							color: #1948B2;
						}
					}

					.nav_item_cart_fontSize {
						margin: 10rpx 25rpx;
						// width: 729rpx;
						// height: 94rpx;
						font-size: 35rpx;
						font-family: PingFang SC;
						font-weight: 500;
						color: #0B0B0B;
						// line-height: 30rpx;
					}

					.nav_item_cart_bottom {
						margin: 0rpx 20rpx;
						display: flex;
						align-items: center;
						justify-content: space-around;
						// width: 744rpx;
						margin-top: 26rpx;
						height: 100rpx;
						background: gray;
						opacity: 0.5;
						border-radius: 15rpx;

						view {
							// width: 606rpx;
							// height: 34rpx;
							font-size: 35rpx;
							font-family: PingFang SC;
							font-weight: 900;
							color: #0B0B0B;
							// color: #FFFFFF;
							// line-height: 21rpx;
						}

						text {
							color: #1948B2;
						}
					}
				}

				.nav_item_cart {
					margin: 16rpx 0rpx;
					margin-top: 16rpx;
					// width: 785rpx;
					// height: 353rpx;
					padding-bottom: 20rpx;
					background: #0075FF;
					color: #FFFFFF;
					border-radius: 15rpx;

					.nav_item_cart_tap {
						display: flex;
						align-items: center;
						padding-top: 40rpx;

						.nav_item_cart_tap_left {
							display: inline-block;
							margin: 0rpx 6rpx;
							width: 6rpx;
							height: 40rpx;
							background: #FFFFFF;
							border-radius: 3rpx;
						}

						.nav_item_cart_tap_right {
							padding-left: 15rpx;
							// width: 534rpx;
							// height: 35rpx;
							font-size: 37rpx;
							font-family: PingFang SC;
							font-weight: bold;
							color: #FFFFFF;
						}
					}

					.nav_item_cart_fontSize {
						margin: 10rpx 25rpx;
						// width: 729rpx;
						// height: 94rpx;
						font-size: 35rpx;
						font-family: PingFang SC;
						font-weight: 500;
						color: #FFFFFF;
						// line-height: 30rpx;
					}

					.nav_item_cart_bottom {
						margin: 0rpx 20rpx;
						display: flex;
						align-items: center;
						justify-content: space-around;
						// width: 744rpx;
						margin-top: 26rpx;
						height: 100rpx;
						background: gray;
						opacity: 0.5;
						border-radius: 15rpx;

						view {
							// width: 606rpx;
							// height: 34rpx;
							font-size: 35rpx;
							font-family: PingFang SC;
							font-weight: 900;
							color: #0B0B0B;
							// color: #FFFFFF;
							// line-height: 21rpx;
						}

						text {
							color: #1948B2;
						}
					}
				}
				.nav_item_bottom {
					position: fixed;
					display: flex;
					align-items: center;
					width: 100%;
					height: 132rpx;
					background: #FFFFFF;
					bottom: 0rpx;
					margin: 5rpx -30rpx;
					padding: 5rpx 20rpx;
				}
			}
		}
	}
	/deep/ .u-empty{
		height: 80% !important;
	}
</style>