<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
			label-width="98px">
			<el-form-item label="巡检日期" prop="dataValue">
				<el-date-picker v-model="queryParams.dataValue" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" clearable value-format="yyyy-MM-dd" @change="playStartTime"></el-date-picker>
			</el-form-item>
			<el-form-item label="计划巡检员" prop="inspectorName">
				<el-input v-model="queryParams.inspectorName" placeholder="请输入计划巡检员" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="实际巡检员" prop="inspectorTrueName">
				<el-input v-model="queryParams.inspectorTrueName" placeholder="请输入实际巡检员" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<!--    <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['wms:day:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['wms:day:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['wms:day:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:day:export']">导出</el-button>
      </el-col> -->
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="dayList" @selection-change="handleSelectionChange">
			<el-table-column label="序号" type="index" width="55" align="center" />
			<el-table-column label="所属巡检计划" align="center" prop="planName" />
			<el-table-column label="巡检日期" align="center" prop="day" />
			<el-table-column label="时间范围" align="center" prop="day" />
			<el-table-column label="计划巡检员" align="center" prop="inspectorName" />
			<el-table-column label="实际巡检员" align="center" prop="inspectorTrueName" />
			<el-table-column label="调班原因" align="center" prop="reason" />
			<el-table-column label="巡检方式" align="center" prop="signType">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.sign_type" :value="scope.row.signType" />
				</template>
			</el-table-column>
			<el-table-column label="状态" align="center" prop="status">
				<template slot-scope="scope">
					<div>{{scope.row.status==0?'未开始':scope.row.status==1?"已完成":scope.row.status==4?"已作废":''}}</div>
				</template>
			</el-table-column>
			<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button v-if="scope.row.status==1" size="mini" type="text" icon="el-icon-document" v-hasPermi="['wms:dayInfo:list']" @click="handleLook(scope.row)">巡检详情</el-button>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

		<!-- 添加或修改巡检记录对话框 -->
		<Inspection-details ref="inspectionDetail"></Inspection-details>
	</div>
</template>

<script>
	import { listDay, getDay, delDay, addDay, updateDay } from "@/api/equipment/equipmentday/day";
	import InspectionDetails from '@/views/equipment/components/InspectionDetails'
	export default {
		name: "Day",
		dicts: ['sign_type'],
		data() {
			return {
				detailInfo: [], //巡检信息
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
				// 巡检记录表格数据
				dayList: [],
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					inspectionStartTime: null,
					inspectionEndTime: null,
					inspectorName: null,
					inspectorTrueName: null,
					dataValue: []
				},
				// 表单参数
				form: {},
				// 表单校验
				rules: {}
			};
		},
		components: {
			InspectionDetails
		},
		created() {
			this.getList();
		},
		methods: {
			playStartTime(e) {
				this.queryParams.dayBegin = e[0]
				this.queryParams.dayEnd = e[1]
			},
			/** 查询巡检记录列表 */
			getList() {
				this.loading = true;
				listDay(this.queryParams).then(response => {
					this.dayList = response.rows;
					this.total = response.total;
					this.loading = false;
				});
			},

			// 表单重置
			reset() {
				this.form = {
					id: null,
					planId: null,
					day: null,
					inspectionStartTime: null,
					inspectionEndTime: null,
					inspector: null,
					inspectorTrue: null,
					reason: null,
					status: null,
					signType: null,
					remark: null,
					createBy: null,
					createTime: null,
					updateBy: null,
					updateTime: null,
					deptId: null,
					deptName: null,
					delFlag: null
				};
				this.resetForm("form");
			},
			/** 搜索按钮操作 */
			handleQuery() {
				this.queryParams.pageNum = 1;
				this.getList();
			},
			/** 重置按钮操作 */
			resetQuery() {
				this.resetForm("queryForm");
				console.log(33333333, this.queryParams)
				this.handleQuery();
			},
			// 多选框选中数据
			handleSelectionChange(selection) {
				this.ids = selection.map(item => item.id)
				this.single = selection.length !== 1
				this.multiple = !selection.length
			},
			/** 查看巡检详情 */
			handleLook(row) {
				this.reset();
				this.$nextTick(() => {
					this.$refs.inspectionDetail.getDetail(row)
				})
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				const ids = row.id || this.ids;
				this.$modal.confirm('是否确认删除' + (row.id ? '该条' : '这些') + '数据？').then(function() {
					return delDay(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("删除成功");
				}).catch(() => {});
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download('wms/day/export', { ...this.queryParams }, `day_${new Date().getTime()}.xlsx`)
			}
		}
	};
</script>
<style scoped lang="scss">

</style>
