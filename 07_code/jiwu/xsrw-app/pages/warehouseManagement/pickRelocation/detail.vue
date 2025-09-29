<template>
	<view class="app">
		<headTitle>
			<view slot="content">单据详情</view>
		</headTitle>
		<view class="content">
			<view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList"
				:key="index">
				<!-- <view>
					预约单号：{{tapList.code}}
				</view> -->
				<view>
					物料编码：{{item.materialCode}}
				</view>
				<view>
					物料名称：{{item.materialName}}
				</view>
				<view>
					区域：{{item.areaName}}
				</view>
				<view>
					批次号：{{item.batchCode}}
				</view>
				<view>
					在库数量：{{item.count}}
				</view>
				<view class="cart_wapper">
					<view>
						是否冻结：{{transfrom(item.isFreeze)}}
					</view>
					<view>
						冻结类型：{{transfrom1(item.originType)}}
					</view>
				</view>
				<button class="button" v-if="!item.isFreeze || item.isFreeze == 0" type="primary" @click="relocation(item.id, item.locationId)">移库</button>
			</view>
			<u-empty text="数据为空" mode="list" v-show="!dataList || dataList.length <= 0 "></u-empty>
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
				indexCheck: null,
				dataList: [],
				tapList:[],
				total: 0,
				statusIcon:0,
				queryParams: {
					id: "",
					materialCode: null,
					isFreeze: 0,
					pageSize: 10,
					pageNum: 1,
				},
				//弹出框
				show: false,
				content: '东临碣石，以观沧海',
				formData: {
					status: 0,
					id: ""
				},
				rules: {},
				option: [],
				option1: [],
			}
		},
		
		async onLoad(option) {
			this.getApi()
			await this.getDicts("stock_is_freeze").then(res => {
				this.option = res.data
			})
			await this.getDicts("stock_origin_type").then(res => {
				this.option1 = res.data
			})
		},
		onReachBottom() {
			if(this.dataList.length<this.total){
				this.queryParams.pageNum++
				this.getApi()
			}
		},
		methods: {
			//刷新
			reFresh(){
				this.queryParams.pageNum = 1
				this.getApi('load')
			},
			checked(val) {
				this.indexCheck = val
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
			transfrom1(e) {
				let dictLabel = ''
				this.option1.map(res => {
					if (res.dictValue == e) {
						dictLabel = res.dictLabel
						return
					}
				})
				return dictLabel
			},
			getApi(type) {
				this.$http.getAction(`/wms/api/stock/selectTStockList`, this.queryParams)
					.then(res => {
						if (this.dataList.length < res.total) {
							this.dataList = [...this.dataList, ...res.rows]
						} else if (this.dataList.length > res.total || type == 'load'){
							this.dataList = res.rows
						}
						this.total = res.total
					})
			},
			relocation(id, locationId) {
				uni.navigateTo({
					url:`./relocation?id=${id}&locationId=${locationId}`
				})
			}
		}
	}
</script>

<style lang="scss">
	.app {
		width: 100%;
		height: 100vh;
		position: relative;
		overflow: scroll;

		.content {
			width: 100%;
			height: 94%;
			padding: 30rpx 30rpx;
			background: #F5F5F5;
			box-sizing: border-box;
			overflow: scroll;

			.content_cart {
				// height: 414rpx;
				background: #FFFFFF;
				border-radius: 15rpx;
				padding: 30rpx 30rpx;
				margin: 20rpx 0rpx;

				view {
					// width: 729rpx;
					// height: 94rpx;
					margin: 10rpx 0rpx;
					font-size: 35rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #0B0B0B;
					// line-height: 30rpx;
				}

				.cart_wapper {
					display: flex;

					view {
						width: 50%;
					}
				}
			}

			.content_cart1 {
				height: 414rpx;
				background: gray;
				border-radius: 15rpx;
				padding: 30rpx 30rpx;
				margin: 20rpx 0rpx;

				view {
					// width: 729rpx;
					// height: 94rpx;
					margin: 10rpx 0rpx;
					font-size: 35rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #0B0B0B;
					// line-height: 30rpx;
				}

				.cart_wapper {
					display: flex;

					view {
						width: 50%;
					}
				}
			}
		}

		.button {
			width: 200rpx !important;
			margin: 0;
			margin-left: auto;
		}
		.btn-detection{
			margin: 30rpx 30rpx;
			position: absolute;
			width: 92%;
			bottom: 10rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			.detection{
				width: 45%;
			}
		}

		.uni-forms-item.is-direction-top {
			flex-direction: inherit;
			display: flex;
		}
	}
</style>