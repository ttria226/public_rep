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
				<u-search v-if="searchType == 1" placeholder="请输入载具编码" shape="square" v-model="queryParams.code" @search="search" @custom="search"></u-search>
				<view class="search-state" v-if="searchType == 0">
					<view class="search-state-left">
						<u-icon name="search" size="30" color="#606266"></u-icon>
						<u-input v-model="statusTaskInput" type="select" @click="statusTaskShow = true" placeholder="请选择任务状态" style="margin: 0 10rpx;" placeholder-style="color: #909399" />
					</view>
					<view class="btn-search" @click="search">搜索</view>
				</view>
				<view class="search-state" v-else>
					<view class="search-state-left">
						<u-icon name="search" size="30" color="#606266"></u-icon>
						<u-input v-model="statusInput" type="select" @click="statusShow = true" placeholder="请选择载具状态" style="margin: 0 10rpx;" placeholder-style="color: #909399" />
					</view>
					<view class="btn-search" @click="search">搜索</view>
				</view>
				<u-action-sheet :style="{ 'z-index': 99 }" :list="taskStatus" v-model="statusTaskShow" @click="actionSheetTaskCallback"></u-action-sheet>
				<u-action-sheet :style="{ 'z-index': 99 }" :list="status" v-model="statusShow" @click="actionSheetCallback"></u-action-sheet>
			</view>
		</u-sticky>
		<view class="tableBox">
			<u-table border-color="#595959" v-if="orderList && orderList.length > 0">
				<u-tr>
					<u-th width="50%">载具编码</u-th>
					<u-th width="25%">载具状态</u-th>
					<u-th width="25%">操作</u-th>
				</u-tr>
				<u-tr v-for="(item,index) in orderList">
					<u-td width="50%">{{item.trayCode}}</u-td>
					<u-td width="25%">{{transfrom(item.trayStatus)}}</u-td>
					<u-td width="25%"><span style="color: #009BF4;" @click="goPage('./detail?id='+item.id+'&code='+item.trayCode+'&type='+item.trayCategory+'&page='+page)">查看明细</span></u-td>
				</u-tr>
			</u-table>
			<u-empty class="emptyClass" text="数据为空" mode="list" v-show="orderList.length <= 0 "></u-empty>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: '载具出库',
				page: '',
				queryParams: {
					pageNum: 1,
					pageSize: 30,
					code: null,
					status: null,
				}, //查询条件
				total: null, //总条数
				orderList: [], //入库单列表
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
						label: '任务状态',
						value: 0,
					},
					{
						label: '载具编码',
						value: 1,
					},
					{
						label: '载具状态',
						value: 2,
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
				taskStatus: [
					{
						text: '未执行',
						value: 	'1',
						color: '',
					},
					{
						text: '已执行',
						value: 	'3',
						color: '',
					}
				],
				statusShow: false,
				statusTaskShow:false,
				statusInput: '',
				statusTaskInput: '未执行',
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
				taskStatus: null
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
				this.$http.getAction('/wms/api/inout/delivery/getTaskList', this.queryParams).then(res => {
					// this.orderList = [...this.orderList, ...res.rows]
					if(this.orderList.length < res.total){
						this.orderList = [...this.orderList,...res.rows]
					} else if(this.orderList.length > res.total || type == 'load'){
						this.orderList = res.rows
					}
					this.total = res.total
					console.log(this.orderList)
				})
			},
			actionSheetCallback(index) {
				for (let i = 0; i < this.status.length; i++) {
					this.status[i].color = ''
				}
				this.status[index].color = 'blue'
				this.statusInput = this.status[index].text;
				this.queryParams.status = this.status[index].value
			},
			actionSheetTaskCallback(index) {
				for (let i = 0; i < this.taskStatus.length; i++) {
					this.taskStatus[i].color = ''
				}
				this.taskStatus[index].color = 'blue'
				this.statusTaskInput = this.taskStatus[index].text
				this.queryParams.taskStatus = this.taskStatus[index].value
			},
			searchTypeChange(value) {
				if (value == 0) {
					for (let i = 0; i < this.status.length; i++) {
						this.status[i].color = ''
					}
					this.statusInput = '';
					this.taskStatus = '';
					this.queryParams.status = null
				} else {
					this.queryParams.code = null
				}
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
		// position: fixed;
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
		padding: 30rpx;
		position: relative;
		height: calc(100vh - 60rpx - 104rpx - 100rpx);
		// z-index: 99;

		/deep/.u-th {
			background: #287096;
			font-size: 36rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
		}
	}
	// /deep/.u-empty{
	// 	height: 90% !important;
	// }
</style>