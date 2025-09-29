<template>
	<view>
		<headTitle title="盘点任务"></headTitle>
		<header>
			<view class="header_tap">
				<u-dropdown>
					<u-dropdown-item v-model="searchType" :title="searchTypes[searchType].label" :options="searchTypes" @change="searchTypeChange"></u-dropdown-item>
				</u-dropdown>
				<!-- <text>物料编码</text> -->
				<u-search v-if="searchType == 0" shape="square" placeholder="请输入物料编码" v-model="queryParams.materialCode" @custom="searchBank"
					@search="searchBank"></u-search>
				<u-search v-if="searchType == 1" shape="square" placeholder="请输入物料名称" v-model="queryParams.materialName" @custom="searchBank"
					@search="searchBank"></u-search>
			</view>
		</header>
		<view class="content">
			<view class="navTitle_All">
				<view class="navTitle" v-for="(item,index) in navList" :key="index">
					<view :class="{'active':isActive === item.index}" @click="checked(item.index)">
						{{item.title}}
					</view>
				</view>
			</view>
			<view class="nav_item_All">
				<view class="nav_item2">
					<view v-for="(item,index) in liatDate" :key="index"
						:class="[isActive1 == index &&queryParams.taskStatus!=2 ? 'nav_item_cart' : 'nav_item_cart1']" @click="checked1(item,index)"
						>
						<view class="cart_wapper">
							<view class="nav_item_cart_tap">
								<text class="nav_item_cart_tap_left"></text>
								<text class="nav_item_cart_tap_right" style="display: flex;align-items: center;">
									盘点单号：
								</text>
								<text class="nav_item_cart_tap_right"
									style="width: 450rpx;overflow: hidden;text-overflow: ellipsis;">{{item.code}}</text>
							</view>
							<view class="nav_item_cart_fontSize" v-if="item.checkType == 1">
									载具编码：{{item.trayCode}}
							</view>
							<view class="nav_item_cart_fontSize" v-else>
								盘点模式：
								<text v-if="item.checkType == 1">物料盘点</text>
								<text v-else>库区盘点</text>
							</view>
							<view class="nav_item_cart_fontSize" v-if="item.checkType == 1">
								所属库区： {{item.reservoirName}}
							</view>
							<view class="nav_item_cart_fontSize" v-else>
								区域：{{item.areaName}}
							</view>
							<view class="nav_item_cart_fontSize" v-if="item.checkType == 1">
								所在库位：{{item.locationName}}
							</view>
							<view class="nav_item_cart_fontSize" v-if="item.checkType == 1">
								所属盘点计划：{{item.planName}}
							</view>
						</view>
					</view>
					<view class="nav_item_bottom" v-show="this.queryParams.taskStatus != 2">
						<button style="width: 95%;" class="button" type="primary"
							@click="goPage('./detail?id='+selItem.id)">执行盘点</button>
					</view>
				</view>
			</view>
			<u-empty text="数据为空" mode="list" v-show="liatDate.length <= 0 "></u-empty>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				selItem:{},//选中的库存信息
				isActive: 0,
				isActive1: null,
				queryParams:{
					materialCode:null,
					materialName: null,
					taskStatus: 0,
					pageSize:10,
					pageNum:1,
				},
				liatDate: [],
				navList: [{
					index: 0,
					title: '未执行'
				},{
					index: 1,
					title: '执行中'
				}, {
					index: 2,
					title: "执行完成"
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
		onLoad() {
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
			//刷新
			reFresh(){
				this.queryParams.pageNum = 1
				this.getApi('load')
			},
			getApi(type) {
				this.$http.getAction("/wms/taskApi/checkList", this.queryParams).then(res => {
					console.log(res, "111")
					// this.liatDate = this.liatDate=[...this.liatDate,...res.rows]
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
				this.selItem=item
				console.log(this.selItem,index)
				this.isActive1 = index
			},
			goPage(url) {
				if(this.isActive1 != null){
					uni.navigateTo({
						url: url
					})
				}else{
					uni.showToast({
						icon: 'none',
						title: '请先选择数据!'
					})
				}
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
		position: relative;
		width: 100%;
		height:93vh;
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
			padding: 30rpx 30rpx;
			box-sizing: border-box;
			// position: relative;

			.nav_item {
				width: 100%;
				height: 100%;
				overflow: scroll;

				// ma: 20rpx 20rpx;
				.nav_item_cart {
					margin: 16rpx 0rpx;
					// width: 785rpx;
					height: 353rpx;
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
					height: 353rpx;
					background: #FFFFFF;
					border-radius: 15rpx;
				}
			}

			.nav_item2 {
				height: 100%;
				overflow: scroll;

				.cart_wapper {
					line-height: 70rpx;
				}

				.nav_item_cart1 {
					margin: 16rpx 0rpx;
					margin-top: 16rpx;
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
			}

			.nav_item_bottom {
				position: fixed;
				display: flex;
				align-items: center;
				width: 92%;
				height: 132rpx;
				background: #FFFFFF;
				bottom: 0;
				margin: 5rpx 0rpx;
			}
		}

	}
	/deep/ .u-empty{
		height: 80% !important;
	}
</style>