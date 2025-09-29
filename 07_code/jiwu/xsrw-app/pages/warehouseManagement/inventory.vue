<template>
	<view>
		<headTitle>
			<view slot="content">库存查询</view>
		</headTitle>
		<header>
			<view class="header_tap">
				<view class="left">
					<u-dropdown>
						<u-dropdown-item v-model="searchType" :title="searchTypes[searchType].label" :options="searchTypes" @change="searchTypeChange"></u-dropdown-item>
					</u-dropdown>
				</view>
				<!-- <text>物料编码</text> -->
				<u-search v-if="searchType == 0" shape="square" placeholder="请输入物料编码" v-model="queryParams.materialCode" @custom="searchBank"
					@search="searchBank"></u-search>
				<u-search v-if="searchType == 1" shape="square" placeholder="请输入物料名称" v-model="queryParams.materialName" @custom="searchBank"
					@search="searchBank"></u-search>
				<u-search v-if="searchType == 2" shape="square" placeholder="请输入库位" v-model="queryParams.locationName" @custom="searchBank"
					@search="searchBank"></u-search>
			</view>
		</header>
		<view class="content">
			<view class="center" v-for="(item,index) in listItem" :key="index">
				<view class="center_tap">
					<view>
						物料编码：{{item.materialCode}}
					</view>
					<view>
						物料名称：{{item.materialName}}
					</view>
				</view>
				<view class="center_bottom">
					<view style="padding: 31rpx 24rpx;">
						<view class="center_bottom_cart">
							<view>
								计量单位：<text>{{item.unitName}}</text>
							</view>
						</view>
						<view class="center_bottom_cart">
							<view>
								载具：<text>{{item.trayCode}}
								</text>
							</view>
						</view>
						<view class="center_bottom_cart">
							<view>
								区域：<text>{{item.areaName}}</text>
							</view>
							<view>
								库区：<text>{{item.reservoirName}}</text>
							</view>
						</view>
						<view class="center_bottom_cart">
							<view>
								库位：<text>{{item.locationName}}</text>
							</view>
							<view>
								库存数量：<text>{{item.count}} </text>
							</view>
						</view>
					</view>
				</view>
			</view>
			<u-empty text="数据为空" mode="list" v-show="listItem.length <= 0 "></u-empty>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				listItem: [],
				queryParams: {
					materialCode: null,
					materialName: null,
					locationName: null,
					pageSize: 10,
					pageNum: 1,
				},
				searchTypes: [
					{
						label: '物料编码',
						value: 0,
					},
					{
						label: '物料名称',
						value: 1,
					},
					{
						label: '库位',
						value: 2,
					},
				],
				searchType: 0,
			}
		},
		created() {
			this.getApi()
		},
		onReachBottom() {
			if(this.listItem.length<this.total){
				this.queryParams.pageNum++
				this.getApi()
			}
		},
		methods: {
			getApi() {
				console.log(this.queryParams)
				this.$http.getAction("/wms/api/stock/list", this.queryParams).then(res => {
					if(this.listItem.length < res.total){
						this.listItem = [...this.listItem, ...res.rows]
					} else if(this.listItem.length > res.total){
						this.listItem = res.rows
					}
					this.total = res.total
					console.log(res, "111")
				})
			},
			searchTypeChange(value) {
				this.queryParams.materialCode = null
				this.queryParams.materialName = null
				this.queryParams.locationName = null
				this.searchBank()
			},
			searchBank(value) {
				// this.queryParams.materialCode = value
				this.listItem = []
				this.total=0
				this.queryParams.pageNum=1
				this.getApi()
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
	header {
		display: flex;
		align-items: center;
		width: 100%;
		height: 104rpx;
		background: #FFFFFF;

		.header_tap {
			width: 100%;
			padding: 10rpx 20rpx;
			display: flex;
			align-items: center;
			// width: 688rpx;
			// height: 92rpx;
			// background: #F4F6F8;
			background-color: #fff;
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
			.left {
				margin-right: 20rpx;
				display: flex;
				background-color: #f2f2f2;
				padding: 13rpx 15rpx;
				font-size: 28rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #0B0B0B;
				border-radius: 10rpx;
				/deep/.u-dropdown__menu{
					height: 38rpx !important;
				}
				/deep/.u-dropdown__content{
					width: 100vw;
					left: -34rpx;
				}
				
				.line {
					margin-left: 15rpx;
					width: 6rpx;
					height: 38rpx;
					background: #CFD1D2;
				}
			}
		}
	}

	.content {
		width: 100%;
		height: calc(100vh - 40rpx - 104rpx - 100rpx);
		// height: 87vh;
		background: #F5F5F5;
		padding: 20rpx 20rpx;
		box-sizing: border-box;

		.center {
			margin: 10rpx 0rpx;
			padding: 5rpx 0rpx;
			width: 100%;
			// height: 414rpx;
			background: #FFFFFF;
			border-radius: 15rpx;

			// padding: 44rpx 0rpx 33rpx 24rpx;
			.center_tap {
				width: 629rpx;
				// height: 94rpx;
				font-size: 35rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #0B0B0B;
				padding: 44rpx 0rpx 20rpx 25rpx;

				view {
					margin: 8rpx 0rpx;
				}
			}

			.center_bottom {
				// width: 744rpx;
				// height: 289rpx;
				background: #F4F7F8;
				border-radius: 15rpx;
				margin: 0rpx 21rpx 25rpx 21rpx;

				.center_bottom_cart {
					margin: 5rpx 0rpx;
					font-size: 35rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #0B0B0B;
					// padding: 31rpx 24rpx;
					display: flex;
					justify-content: space-between;

					// view {
					// 	width: 300rpx;
					// 	overflow: hidden;
					// 	/* 第二步：让文本不会换行， 在同一行继续 */
					// 	white-space: nowrap;
					// 	/* 第三步：用省略号来代表未显示完的文本 */
					// 	text-overflow: ellipsis;
					// }

					text {
						color: #1948B2;
					}
				}
			}
		}
	}
	/deep/ .u-empty{
		height: 80% !important;
	}
</style>