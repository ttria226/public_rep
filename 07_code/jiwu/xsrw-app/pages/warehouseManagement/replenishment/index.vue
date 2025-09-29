<template>
	<view class="outbound">
		<headTitle :title="title"></headTitle>
		<u-sticky>
			<view class="searchBox">
				<view class="left" style="">
					<u-dropdown>
						<u-dropdown-item v-model="searchType" :title="searchTypes[searchType].label" :options="searchTypes" @change="searchTypeChange"></u-dropdown-item>
					</u-dropdown>
					<!-- <u-icon name="arrow-down" style="padding-left:17rpx ;"></u-icon> -->
					<!-- <view class="line"></view> -->
				</view>
				<u-search v-if="searchType == 0" placeholder="请输入BOM编号" shape="square" v-model="queryParams.code" @search="search" @custom="search"></u-search>
				<u-search v-if="searchType == 1" placeholder="请输入BOM名称" shape="square" v-model="queryParams.name" @search="search" @custom="search"></u-search>
				<u-action-sheet :list="status" v-model="statusShow" @click="actionSheetCallback"></u-action-sheet>
			</view>
		</u-sticky>
		<view class="tableBox">
			<u-table border-color="#595959">
				<u-tr>
					<u-th width="50%">BOM编号</u-th>
					<u-th width="25%">BOM名称</u-th>
					<u-th width="25%">操作</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in orderList">
					<u-td width="50%">{{item.code}}</u-td>
					<u-td width="25%">{{item.name}}</u-td>
					<u-td width="25%"><span style="color: #009BF4;" @click="goPage('./detail?id='+item.id)">补料</span></u-td>
				</u-tr>
			</u-table>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: '物料清单（BOM）补料',
				page: '',
				queryParams: {
					pageNum: 1,
					pageSize: 30,
					code: null,
					name: null,
				}, //查询条件
				total: null, //总条数
				orderList: [],
				option: [{
					label:'空闲',
					value:0
				},
				{
					label:'半托',
					value:1
				},
				{
					label:'全托',
					value:2
				},
				{
					label:'标记出库',
					value:3
				},
				{
					label:'标记入库',
					value:4
				}], //字典值
				searchTypes: [
					{
						label: 'BOM编号',
						value: 0,
					},
					{
						label: 'BOM名称',
						value: 1,
					},
				],
				searchType: 0,
				status: [
					{
						text: '空闲',
						value: 	0,
						color: '',
					},
					{
						text: '半托',
						value: 	1,
						color: '',
					},
					{
						text: '全托',
						value: 	2,
						color: '',
					},
					{
						text: '标记出库',
						value: 	3,
						color: '',
					},
					{
						text: '标记入库',
						value: 	4,
						color: '',
					},
				],
				statusShow: false,
				statusInput: '',
			}
		},
		onReachBottom() {
			if (this.orderList.length < this.total) {
				this.queryParams.pageNum++
				this.getBaseList()
			}
		},
		onUnload(){
			this.orderList=[]
			this.queryParams={
				pageNum: 1,
				pageSize: 30,
				code: null,
				status: null,
			}
			this.total=null
			this.option=[]
		},
		 onLoad(opt) {
			if (opt.page && opt.page == '1') {
				this.page = opt.page
				this.title = '上架'
			}
			this.orderList = []
			// 获取载具列表
			this.getBaseList()
		},
		methods: {
			//刷新
			reFresh(){
				this.queryParams.pageNum = 1
				this.getBaseList('load')
			},
			search(){
				this.orderList = []
				this.total=0
				this.queryParams.pageNum=1
				this.getBaseList()
			},
			goPage(url){
				uni.navigateTo({
					url:url
				})
			},
			transfrom(e, index) {
				let dictLabel = ''
				this.option.map(res => {
					if (res.value == e) {
						dictLabel = res.label
						return
					}
				})
				return dictLabel
			},
			getBaseList(type) {
				this.$http.getAction('/wms/api/base/getBomList', this.queryParams).then(res => {
					// this.orderList = [...this.orderList, ...res.rows]
					if(this.orderList.length < res.total){
						this.orderList = [...this.orderList,...res.rows]
					} else if(this.orderList.length > res.total || type == 'load'){
						this.orderList = res.rows
					}
					this.total = res.total
				})
			},
			actionSheetCallback(index) {
				for (let i = 0; i < this.status.length; i++) {
					this.status[i].color = ''
				}
				this.status[index].color = 'blue'
				this.statusInput = this.status[index].text
				this.queryParams.status = this.status[index].value
			},
			searchTypeChange(value) {
				this.queryParams.code = null
				this.queryParams.name = null
				this.search()
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
	.outbound{
	}
	.searchBox {
		width: 100%;
		background-color: #fff;
		padding: 10rpx 20rpx;
		display: flex;
		align-items: center;
		position: fixed;
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
		.search-state{
			flex: 1;
			display: flex;
			justify-content: space-between;
			align-items: center;
			.search-state-left{
				padding: 0 18rpx;
				flex: 1;
				height: 63rpx;
				background-color: #f2f2f2;
				border-radius: 10rpx;
				display: flex;
				align-items: center;
			}
			.btn-search{
				margin-left: 10rpx;
				width: 84rpx;
				font-size: 28rpx;
				color: #303133;
				text-align: center;
			}
		}
	}

	.tableBox {
		padding: 94rpx 30rpx 30rpx;
		// z-index: 99;

		/deep/.u-th {
			background: #287096;
			font-size: 36rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
		}
	}
</style>