<template>
	<view class="app">
		<headTitle>
			<view slot="content">质检拣选</view>
		</headTitle>
		<header>
			<view class="header_tap">
				<text>预约单号</text>
				<u-search shape="square" placeholder="请输入预约单号" v-model="queryParams.code" @custom="searchBank"
					@search="searchBank"></u-search>
			</view>
		</header>
		<view class="content">
			<view :class="[indexCheck == index ? 'content_cart1' : 'content_cart']" v-for="(item,index) in dataList"
				:key="index">
				<view>
					预约单号：{{item.code}}
				</view>
				<view>
					类型：
					{{transfrom(item.type)}}
				</view>
				<view>
					部门：{{item.deptName}}
				</view>
				<view>
					状态：{{transfrom1(item.status)}}
				</view>
				<button class="button" type="primary" @click="pick(item.id)">拣选</button>
			</view>
			<u-empty text="数据为空" mode="list" v-show="!dataList || dataList.length <= 0 "></u-empty>
		</view>
		<u-modal v-model="pickShow" content="确认拣选？" show-cancel-button :show-title="false" @confirm="confirm" @cancel="pickShow=false"></u-modal>
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
				total: 0,
				statusIcon:0,
				queryParams: {
					code: null,
					status: 3,
					pageSize: 10,
					pageNum: 1,
				},
				currentId: null,
				pickShow: false,
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
		
		async onLoad() {
			this.getApi()
			await this.getDicts("in_delivery_type").then(res => {
				this.option = res.data
			})
			await this.getDicts("in_delivery_status").then(res => {
				this.option1 = res.data
			})
		},
		// onReachBottom() {
		// 	if(this.dataList.length<this.total){
		// 		this.queryParams.pageNum++
		// 		this.getApi()
		// 	}
		// },
		methods: {
			//刷新
			reFresh(){
				this.queryParams.pageNum = 1
				this.getApi('load')
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
			checked(val) {
				this.indexCheck = val
			},
			getApi(type) {
				this.$http.getAction(`/wms/api/inout/delivery/warehousingList`, this.queryParams)
					.then(res => {
						if (this.dataList.length < res.total) {
							this.dataList = [...this.dataList, ...res.rows]
						} else if (this.dataList.length > res.total || type == 'load'){
							this.dataList = res.rows
						}
						// this.total = res.total
					})
			},
			pick(id) {
				this.currentId = id
				this.pickShow = true
			},
			confirm() {
				const data = {
					originId: this.currentId
				}
				this.$http.postAction(`/wms/api/inout/delivery/quality/addPda`, data)
					.then(res => {
						this.$modal.msgSuccess(res.msg);
						this.reFresh()
					})
			},
		}
	}
</script>

<style lang="scss">
	.app {
		width: 100%;
		height: 100vh;
		position: relative;
		overflow: scroll;
		header {
			display: flex;
			align-items: center;
			width: 100%;
			height: 104rpx;
			background: #FFFFFF;

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
		}

		.content {
			width: 100%;
			height: 94%;
			padding: 30rpx 30rpx;
			background: #F5F5F5;
			box-sizing: border-box;
			overflow: scroll;

			.content_cart {
				width: 100%;
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
			width: 200rpx;
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