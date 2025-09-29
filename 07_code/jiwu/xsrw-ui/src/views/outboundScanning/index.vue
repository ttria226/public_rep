<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
			label-width="98px">
			<el-form-item label="出库单号">
				<el-input v-model="queryParams.chukuCode" placeholder="请输入入库单号" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="物料编码">
				<el-input v-model="queryParams.wuliaoCode" placeholder="请输入物料编码" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="最后扫描日期">
				<el-date-picker v-model="queryParams.startEndTime" type="daterange" range-separator="至"
					start-placeholder="开始日期" value-format="yyyy-MM-dd" end-placeholder="结束日期">
				</el-date-picker>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="validityWarningList">
			<el-table-column label="出库单号" align="center" prop="chukuCode" :show-overflow-tooltip="true"
				min-width="130px" />
			<el-table-column label="物料编码" align="center" prop="wuliaoCode" min-width="130px"
				:show-overflow-tooltip="true" />
			<el-table-column label="物料名称" align="center" prop="name" :show-overflow-tooltip="true"
				min-width="200px" />
			<el-table-column label="批次号" align="center" prop="batchCode" :show-overflow-tooltip="true"
				min-width="120px" />
			<el-table-column label="物料标签ID" align="center" prop="rfid" :show-overflow-tooltip="true"
				min-width="130px" />
			<el-table-column label="换算数量" align="center" prop="huansuanShuliang" :show-overflow-tooltip="true"
				min-width="130px" />
			<el-table-column label="最后扫描时间" align="center" prop="saomiaoShijian" :show-overflow-tooltip="true"
				min-width="150px" />
			<el-table-column label="状态" align="center" prop="chukuSaomiaoFlag" :show-overflow-tooltip="true"
				min-width="90px" >
				<template slot-scope="scope">
					<span v-if="scope.row.chukuSaomiaoFlag" style="color: red;">异常出库</span>
					<span v-if="!scope.row.chukuSaomiaoFlag" style="color: #3092fe;">正常出库</span>
				</template>
			</el-table-column>
		</el-table>
		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
			:limit.sync="queryParams.pageSize" @pagination="getList" />
	</div>
</template>

<script>
	import dayjs from 'dayjs'
	import {
		postAction,
		getAction
	} from "@/api/manage";
	import {
		wms
	} from '@/utils/agent';

	export default {
		name: "outboundScanning",
		dicts: [],
		data() {
			return {
				// 遮罩层
				loading: true,
				// 选中数组
				ids: [],
				// 非单个禁用
				single: true,
				// 非多个禁用
				multiple: true,
				// 显示搜索条件
				showSearch: true,
				// 总条数
				total: 0,
				// 有效期预警列表表格数据
				validityWarningList: [],
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					startEndTime: ['', ''],
				},

			};
		},
		created() {
			// this.getQueryWareHousList();
			this.getList();
			const today=dayjs().format('YYYY-MM-DD')
			this.queryParams.startEndTime=[today,today]
		},
		methods: {
			/** 查询仓库列表 */
			getQueryWareHousList() {
				let qps = {
					pageNum: 1,
					pageSize: 10,
				};
				qps.pageSize = 5000
			},
			/** 查询出库计划列表 */
			getList() {
				this.loading = true;
				this.queryParams.saomiaoShijianStart = this.queryParams.startEndTime[0]
				this.queryParams.saomiaoShijianEnd = this.queryParams.startEndTime[1]
				getAction(`${wms}/deliveryOut/selectChuKuList`, this.queryParams).then(response => {
					this.validityWarningList = response.rows;
					this.total = response.total;
				}).finally(() => {
					this.loading = false;
				});
			},
			/** 搜索按钮操作 */
			handleQuery() {
				this.queryParams.pageNum = 1;
				this.getList();
			},
			/** 重置按钮操作 */
			resetQuery() {
				this.queryParams = {
					pageNum: 1,
					pageSize: 10,
					startEndTime: ['', ''],
				}
				this.resetForm("queryForm");
				this.handleQuery();
			},
		}
	};
</script>