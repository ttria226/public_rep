<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
			<el-form-item label="计划名称" prop="name">
				<el-input v-model="queryParams.name" placeholder="请输入计划名称" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="巡检计划状态" prop="status" label-width="110">
				<el-select v-model="queryParams.status" placeholder="请选择巡检计划状态" clearable filterable >
					<el-option v-for="dict in dict.type.maintenance_plan_status" :key="dict.value" :label="dict.label" :value="dict.value" />
				</el-select>
			</el-form-item>
			<el-form-item label="巡检员" prop="inspectorName">
				<el-input v-model="queryParams.inspectorName" placeholder="请输入巡检员" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['equipmentplan:plan:add']">添加巡检计划</el-button>
			</el-col>
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
			<el-table-column label="序号" type="index" align="center" />
			<el-table-column label="计划名称" align="center" prop="name" min-width="130" />
			<el-table-column label="计划线路" align="center" prop="inspectionLine" min-width="150" />
			<el-table-column label="巡检周期" align="center" prop="type" min-width="90">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.wms_plan_type" :value="scope.row.type" />
				</template>
			</el-table-column>
			<el-table-column label="签到方式" align="center" prop="signType" min-width="170">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.sign_type" :value="scope.row.signType" />
				</template>
			</el-table-column>
			<el-table-column label="所选日期" align="center" prop="monthOrDay" min-width="170" />
			<el-table-column label="日期范围" align="center" prop="planStartTime" min-width="200">
				<template slot-scope="scope">
					<span>{{ parseTime(scope.row.planStartTime, '{y}-{m}-{d}') }} — {{ parseTime(scope.row.planEndTime, '{y}-{m}-{d}') }}</span>
				</template>
			</el-table-column>
			<el-table-column label="时间范围" align="center" prop="inspectionStartTime" min-width="200">
				<template slot-scope="scope">
					<span>{{scope.row.inspectionStartTime}} — {{ scope.row.inspectionEndTime}}</span>
				</template>
			</el-table-column>
			<el-table-column label="巡检员" align="center" prop="inspectorName" min-width="120" />
			<el-table-column label="状态" align="center" prop="status" min-width="90">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.maintenance_plan_status" :value="scope.row.status" />
				</template>
			</el-table-column>
			<el-table-column label="操作" align="center" fixed="right" min-width="300">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status === 1" @click="handleUpdate(scope.row)" v-hasPermi="['equipmentplan:plan:edit']">编辑</el-button>
					<el-button size="mini" type="text" icon="el-icon-view" @click="handleLook(scope.row)">查看</el-button>
					<el-popconfirm title="确定启用吗？" @confirm="handleEnable(scope.row)" :key="Math.random()" v-if="scope.row.status === 1" style="margin: 0 10px;">
						<el-button slot="reference" size="mini" type="text" icon="el-icon-open" v-if="scope.row.status === 1" v-hasPermi="['equipmentplan:plan:enable']">启用</el-button>
					</el-popconfirm>
					<el-popconfirm title="确定作废吗？" @confirm="handleNoEnable(scope.row)" :key="Math.random()" v-if="scope.row.status === 2" style="margin: 0 10px;">
						<el-button slot="reference" size="mini" type="text" icon="el-icon-turn-off" v-if="scope.row.status === 2" v-hasPermi="['equipmentplan:plan:enable']">作废</el-button>
					</el-popconfirm>
					<el-button v-if="scope.row.status === 2" size="mini" type="text" icon="el-icon-document" @click="handleDetail(scope.row)" v-hasPermi="['equipmentplan:plan:detail']">任务详情</el-button>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
			@pagination="getList" />

		<!-- 添加或修改巡检计划对话框 -->
		<el-dialog :title="title" :visible.sync="open" append-to-body @close="cancelEquDisable">
			<el-form ref="form" :model="form" :rules="rules" label-width="110px" :disabled="isAddForm">
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="计划名称" prop="name">
							<el-input v-model="form.name" placeholder="请输入计划名称" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="计划巡检线路" prop="inspectionLine">
							<el-input v-model="form.inspectionLine" placeholder="请输入计划巡检线路" />
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="巡检周期" prop="type">
							<el-select v-model="form.type" placeholder="请选择巡检周期" style="width: 100%;" @change="clearCheck">
								<el-option v-for="dict in dict.type.wms_plan_type" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="签到方式" prop="signType">
							<el-select v-model="form.signType" placeholder="请选择签到方式" style="width: 100%;">
								<el-option v-for="dict in dict.type.sign_type" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="周" prop="checkList" v-if="form.type==1">
							<el-checkbox-group v-model="form.checkList" @change="changeWeek">
								<el-checkbox label="星期一"></el-checkbox>
								<el-checkbox label="星期二"></el-checkbox>
								<el-checkbox label="星期三"></el-checkbox>
								<el-checkbox label="星期四"></el-checkbox>
								<el-checkbox label="星期五"></el-checkbox>
								<el-checkbox label="星期六"></el-checkbox>
								<el-checkbox label="星期七"></el-checkbox>
							</el-checkbox-group>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="年月" prop="yearOrMonth" v-if="form.type==2">
							<el-date-picker style="width: 100%;" v-model="form.yearOrMonth" type="month" placeholder="选择月" value-format="yyyy-MM" @change="getDay">
							</el-date-picker>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="月/天" prop="checkList" v-if="form.type==2">
							<el-checkbox-group v-model="form.checkList" @change="changeWeek">
								<el-checkbox :label="item.toString()" v-for="item in selDays" :key="item"></el-checkbox>
							</el-checkbox-group>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="计划日期范围" prop="dataValue" v-if="form.type==1">
							<el-date-picker style="width: 100%;" v-model="form.dataValue" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" clearable value-format="yyyy-MM-dd" @change="playStartTime"></el-date-picker>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="巡检时间范围" prop="timeValue">
							<el-time-picker style="width: 100%;" is-range v-model="form.timeValue" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" placeholder="选择时间范围" @change="inspectionStartTime" value-format="HH:mm:ss"></el-time-picker>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="所属部门" prop="depId">
							<treeselect v-model="form.depId" :options="deptOptions" :normalizer="normalizer" placeholder="选择所属部门" @select="getPerson" :disabled="isAddForm" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="巡检员" prop="inspector">
							<el-select v-model="form.inspector" filterable placeholder="请选择巡检员" clearable style="width: 100%;">
								<el-option v-for="item in personList" :key="item.userId" :label="item.userName" :value="item.userId">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-divider content-position="center">巡检计划设备列信息</el-divider>
				<el-row :gutter="10" class="mb8">
					<el-col :span="1.5">
						<el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddWmsInspectionPlanDetail">添加</el-button>
					</el-col>

				</el-row>
				<el-table :data="wmsInspectionPlanDetailList" :row-class-name="rowWmsInspectionPlanDetailIndex" @selection-change="handleWmsInspectionPlanDetailSelectionChange" ref="wmsInspectionPlanDetail">
					<el-table-column label="排序" align="center" prop="index" width="50" />
					<el-table-column label="设备名称" prop="name"></el-table-column>
					<el-table-column label="设备编号" prop="equNo"></el-table-column>
					<el-table-column label="序列号" prop="serialNo"></el-table-column>
					<el-table-column label="安装地点" prop="functionLocation"></el-table-column>
					<el-table-column label="巡检标准" prop="inspectionItems"></el-table-column>
					<el-table-column label="操作" prop="sort">
						<template slot-scope="scope">
							<el-button size="mini" type="text" v-if="scope.$index!=0" @click="moveUp(scope.$index,scope.row)">上移</el-button>
							<el-button size="mini" type="text" v-if="scope.$index!=wmsInspectionPlanDetailList.length-1" @click="moveDown(scope.$index,scope.row)">下移</el-button>
							<el-popconfirm title="确定删除吗？" @confirm="removePlanDetailList(scope.$index,scope.row)">
								<el-button slot="reference" size="mini" type="text" icon="el-icon-delete">删除</el-button>
							</el-popconfirm>
						</template>
					</el-table-column>
				</el-table>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm" v-if="title != '查看巡检计划'">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>

		<el-dialog title="添加设备" :visible.sync="openEqu" width="30%" append-to-body>
			<el-form ref="equipmentForm" :model="formEqu" :rules="rules" label-width="110px">
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="设备" prop="equipmentId">
							<el-select v-model="formEqu.equipmentId" filterable placeholder="请选择设备" clearable style="width: 100%;">
								<el-option v-for="item in equipmentList" :key="item.id" :disabled="item.isabled" :label="item.name" :value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitEquForm">确 定</el-button>
				<el-button @click="cancelEqu">取 消</el-button>
			</div>
		</el-dialog>
		<el-dialog title="任务详情" :visible.sync="openTask" width="60%" append-to-body>
			<el-table :data="taskDetail" ref="taskDetail">
				<el-table-column label="排序" align="center" type="index" width="50" />
				<el-table-column label="巡检日期" prop="day" align="center"></el-table-column>
				<el-table-column label="时间范围" prop="day" width="150" align="center">
					<template slot-scope="scope">
						<div>{{scope.row.inspectionStartTime}}~{{scope.row.inspectionEndTime}}</div>
					</template>
				</el-table-column>
				<el-table-column label="计划巡检员" prop="inspectorName" align="center"></el-table-column>
				<el-table-column label="实际巡检员" prop="inspectorTrueName" align="center"></el-table-column>
				<el-table-column label="调班原因" prop="reason" align="center"></el-table-column>
				<el-table-column label="状态" prop="status" align="center">
					<template slot-scope="scope">
						<div>{{scope.row.status==0?'未开始':scope.row.status==2?"进行中":scope.row.status==1?"已完成":scope.row.status==4?"已作废":''}}</div>
					</template>
				</el-table-column>
				<el-table-column label="设备报修" prop="isrepair" align="center">
					<template slot-scope="scope">
						<div>{{scope.row.isrepair==0?'':'有'}}</div>
					</template>
				</el-table-column>
				<el-table-column label="操作" prop="sort">
					<template slot-scope="scope">
						<el-button size="mini" type="text" v-if="scope.row.status==0" @click="handleSwitch(scope.row)">调班</el-button>
						<el-button size="mini" type="text" v-if="scope.row.status==1 || scope.row.status==2" @click="handleLookDetail(scope.row)">巡检详情</el-button>
					</template>
				</el-table-column>
			</el-table>
			<pagination v-show="taskDetailTotal>0" :total="taskDetailTotal" :page.sync="taskDetailQueryParams.pageNum" :limit.sync="taskDetailQueryParams.pageSize" @pagination="getTaskDetailList" />
		</el-dialog>
		<!-- 巡检详情页面 -->
		<Inspection-details ref="inspectionDetail"></Inspection-details>
		<el-dialog title="巡检调班" :visible.sync="openSwitch" width="45%" append-to-body>
			<el-form ref="formSwitch" :model="formSwitch" :rules="rulesSwich" label-width="110px">
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="所属部门" prop="depId">
							<treeselect v-model="formSwitch.depId" :options="deptOptions" :normalizer="normalizer" placeholder="选择所属部门" @select="getPerson" :disabled="isAddForm" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="巡检员" prop="userid">
							<el-select v-model="formSwitch.userid" filterable placeholder="请选择巡检员" clearable style="width: 100%;">
								<el-option v-for="item in personList" :key="item.userId" :label="item.userName" :value="item.userId">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="24">
						<el-form-item label="调班原因" prop="reason">
							<el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入内容" v-model="formSwitch.reason"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitSwichForm">确 定</el-button>
				<el-button @click="openSwitch=!openSwitch">取 消</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import InspectionDetails from '@/views/equipment/components/InspectionDetails'
	import { getAction } from "@/api/manage"
	import { listPlan, getPlan, delPlan, addPlan, updatePlan } from "@/api/equipment/equipmentplan/plan";
	import Treeselect from "@riophae/vue-treeselect";
	import "@riophae/vue-treeselect/dist/vue-treeselect.css";
	import { listDept } from "@/api/system/dept";
	export default {
		name: "Plan",
		components: { Treeselect, InspectionDetails },
		dicts: ['wms_plan_type', 'sign_type', 'maintenance_plan_status'],
		data() {
			return {
				openPlanId:null,
				rulesSwich: {
					depId: [{
						required: true,
						message: '请选择巡检员所属部门',
						trigger: 'change'
					}],
					userid: [{
						required: true,
						message: '请选择巡检员',
						trigger: 'change'
					}],
					reason: [{
						required: true,
						message: '请输入调班原因',
						trigger: 'blur'
					}],
				}, //巡检调班表单校验
				formSwitch: {
					id:null,
					depId:null,
					userid:null,
					reason:''
				}, //巡检调班表单
				openSwitch: false, //打开巡检调班弹窗
				// 查询参数
				taskDetailQueryParams: {
					pageNum: 1,
					pageSize: 10,
					planId: null
				},
				taskDetailTotal: 0,
				taskDetail: [], //任务详情列表
				openTask: false, //查看任务详情
				isAddForm: false, //整个表单是否禁用
				formEqu: {
					equipmentId: null,
				},
				openEqu: false, //添加设备弹窗
				equipmentList: [], //设备列表
				checkList: [],
				selDays: "", //所选月份的天数
				personList: [], //责任人列表
				deptOptions: [], //部门列表
				// 遮罩层
				loading: true,
				// 选中数组
				ids: [],
				// 子表选中数据
				checkedWmsInspectionPlanDetail: [],
				// 非单个禁用
				single: true,
				// 非多个禁用
				multiple: true,
				// 显示搜索条件
				showSearch: true,
				// 总条数
				total: 0,
				// 巡检计划表格数据
				planList: [],
				// 巡检计划设备列表格数据
				wmsInspectionPlanDetailList: [],
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					name: null,
					status: null,
					inspector: null,
				},
				// 表单参数
				form: {
					checkList: [], //选中的周
				},
				// 表单校验
				rules: {
					name: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
					inspectionLine: [{ required: true, message: '请输入计划巡检路线', trigger: 'blur' }],
					type: [{ required: true, message: '请选择巡检周期', trigger: 'change' }],
					signType: [{ required: true, message: '请选择签到方式', trigger: 'change' }],
					dataValue: [{ required: true, message: '请选择计划日期范围', trigger: 'change' }],
					timeValue: [{ required: true, message: '请选择巡检时间范围', trigger: 'change' }],
					yearOrMonth: [{ required: true, message: '请选择年月', trigger: 'change' }],
					depId: [{ required: true, message: '请选择所属部门', trigger: 'change' }],
					checkList: [{ required: true, message: '请选择日期', trigger: 'change' }],
					inspector: [{ required: true, message: '请选择巡检员', trigger: 'change' }],
					equipmentId: [{ required: true, message: '请选择设备', trigger: 'change' }],
				}
			};
		},
		created() {
			this.getList();
			this.getListDept();
			this.getEquipmentList()
		},
		methods: {
			submitSwichForm(){
				this.$refs["formSwitch"].validate(valid => {
					if (valid) {
						this.$http.putAction('/wms/day/exchange',this.formSwitch).then(res=>{
							this.openSwitch=false
							this.$modal.msgSuccess(res.msg);
							this.handleDetail(this.openPlanId)
							console.log(res)
						})
					}
				});
			},
			handleSwitch(row){
				this.resetForm('formSwitch')
				this.formSwitch.id=row.id
				this.openSwitch=true
			},
			// 查看任务详情
			handleLookDetail(row) {
				this.$nextTick(() => {
					this.$refs.inspectionDetail.getDetail(row)
				})
			},
			clearCheck() {
				this.form.checkList = []
				this.$forceUpdate()
			},
			// 查看任务详情
			handleDetail(row) {
				this.openPlanId=row
				this.openTask = true
				this.getTaskDetailList()
			},
			//任务详情
			getTaskDetailList(){
				this.taskDetailQueryParams.planId = this.openPlanId.id
				this.$http.getAction('/wms/day/list', {...this.taskDetailQueryParams}).then(res => {
					this.taskDetail = res.rows
					this.taskDetailTotal = res.total
				})
			},
			// 查看徐建计划数据
			async handleLook(row) {
				this.isAddForm = true
				this.reset();
				const id = row.id || this.ids
				getPlan(id).then(response => {
					this.form = response.data;
					this.wmsInspectionPlanDetailList = response.data.wmsInspectionPlanDetailList;
					this.form.timeValue = [this.form.inspectionStartTime, this.form.inspectionEndTime]
					this.form.depId = response.data.deptId * 1
					if (this.form.type == 2) {
						this.getDay(this.form.yearOrMonth)
					} else {
						this.form.dataValue = [this.form.planStartTime, this.form.planEndTime]
					}
					this.form.checkList = response.data.monthOrDay.split(',')
					this.open = true;
					this.title = "查看巡检计划";
					console.log(this.form)
				});
				let ret = await getAction('system/user/list', { deptId: this.form.deptId })
				this.personList = ret.rows
				console.log('我是巡检员列表', this.personList, this.deptOptions)
			},
			// 删除某一条设备的信息
			removePlanDetailList(index, row) {
				this.wmsInspectionPlanDetailList.splice(index, 1)
				this.equipmentList.map(res => {
					if (res.id == row.id) {
						res.isabled = false
					}
				})
				console.log('我是数组', this.wmsInspectionPlanDetailList)
			},
			// 下移
			moveDown(index, row) {
				this.wmsInspectionPlanDetailList[index] = this.wmsInspectionPlanDetailList.splice(index + 1, 1, this.wmsInspectionPlanDetailList[index])[0];
			},
			// 上移
			moveUp(index, row) {
				this.wmsInspectionPlanDetailList[index] = this.wmsInspectionPlanDetailList.splice(index - 1, 1, this.wmsInspectionPlanDetailList[index])[0];
			},
			handleNoEnable(item) {
				this.$http.putAction(`/wms/plan/planEnd/${item.id}`).then(res => {
					this.$modal.msgSuccess("作废成功！");
					this.getList()
				})
			},
			// 启用、禁用事件
			handleEnable(item, type) {
				this.$http.putAction(`/wms/plan/planStart/${item.id}`).then(res => {
					this.$modal.msgSuccess("启用成功！");
					this.getList()
				})
			},
			submitEquForm() {
				this.$refs["equipmentForm"].validate(valid => {
					if (valid) {
						let row = {}
						this.equipmentList.map(res => {
							console.log(res)
							if (res.id == this.formEqu.equipmentId) {
								row = res
								res.isabled = true
							}
						})
						this.wmsInspectionPlanDetailList.push(row)
						this.openEqu = false
						this.formEqu = {}
						console.log(this.wmsInspectionPlanDetailList, this.equipmentList)
					}
				});
			},
			// 获取所选月份的天数
			getDay(e) {
				if (e && e.split('-').length == 2) {
					this.dayList = []
					let date = new Date(e.split('-')[0], e.split('-')[1], 0)
					this.selDays = date.getDate()
				}
				this.$forceUpdate()
			},
			// 选择周
			changeWeek(e) {
				this.$forceUpdate()
				console.log(e)
			},
			// 获取责任人列表
			getPerson(node) {
				console.log(node)
				this.form.depName = node.deptName
				getAction('system/user/list', { deptId: node.deptId }).then(res => {
					console.log(res)
					if (res.code == 200) {
						this.personList = res.rows
					} else {
						this.$modal.msgError(res.msg);
					}
				})
			},
			// 获取设备列表
			getEquipmentList() {
				getAction('/wms/equipment/selectList').then(res => {
					if (res.code == 200) {
						this.equipmentList = res.rows
					}
				})
			},
			// 选择时间范围
			inspectionStartTime(e) {
				this.form.inspectionStartTime = e[0]
				this.form.inspectionEndTime = e[1]
			},
			// 构造树形结构
			normalizer(node) {
				if (node.children && !node.children.length) {
					delete node.children;
				}
				return {
					id: node.deptId,
					label: node.deptName,
					children: node.children
				};
			},
			// 获取部门数据
			getListDept() {
				listDept().then(response => {
					this.deptOptions = this.handleTree(response.data, "deptId");
				})
			},
			playStartTime(e) {
				this.form.planStartTime = e[0]
				this.form.planEndTime = e[1]
				this.$forceUpdate()
			},
			/** 查询巡检计划列表 */
			getList() {
				this.loading = true;
				listPlan(this.queryParams).then(response => {
					this.planList = response.rows;
					this.total = response.total;
					this.loading = false;
				});
			},
			// 取消按钮
			cancel() {
				this.open = false;
				this.reset();
			},
			cancelEquDisable() {
				this.equipmentList.map(res => {
					res.isabled = false
				})
			},
			cancelEqu() {
				this.openEqu = false;
			},
			// 表单重置
			reset() {
				this.form = {
					checkList: [],
					id: null,
					name: null,
					inspectionLine: null,
					type: null,
					planStartTime: null,
					planEndTime: null,
					inspectionStartTime: null,
					inspectionEndTime: null,
					inspector: null,
					createBy: null,
					createTime: null,
					updateBy: null,
					updateTime: null,
					deptId: null,
					deptName: null,
					delFlag: null
				};
				this.wmsInspectionPlanDetailList = [];
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
				this.handleQuery();
			},
			// 多选框选中数据
			handleSelectionChange(selection) {
				this.ids = selection.map(item => item.id)
				this.single = selection.length !== 1
				this.multiple = !selection.length
			},
			/** 新增按钮操作 */
			handleAdd() {
				this.reset();
				this.isAddForm = false
				this.open = true;
				this.title = "添加巡检计划";
			},
			/** 修改按钮操作 */
			async handleUpdate(row) {
				this.reset();
				this.isAddForm = false
				this.reset();
				const id = row.id || this.ids
				getPlan(id).then(response => {
					this.form = response.data;
					this.wmsInspectionPlanDetailList = response.data.wmsInspectionPlanDetailList;
					this.form.timeValue = [this.form.inspectionStartTime, this.form.inspectionEndTime]
					this.form.depId = response.data.deptId * 1
					this.equipmentList.map(rej => {
						this.form.wmsInspectionPlanDetailList.map(rek => {
							rej.id == rek.equipmentId ? rej.isabled = true : ''
						})
					})
					console.log('我是设备列表', this.equipmentList)
					if (this.form.type == 2) {
						this.getDay(this.form.yearOrMonth)
					} else {
						this.form.dataValue = [this.form.planStartTime, this.form.planEndTime]
					}
					this.form.checkList = response.data.monthOrDay.split(',')
					this.open = true;
					this.title = "修改巡检计划";
					console.log(this.form)
				});
				let ret = await getAction('system/user/list', { deptId: this.form.deptId })
				this.personList = ret.rows
				console.log('我是巡检员列表', this.personList, this.deptOptions)
			},
			/** 提交按钮 */
			submitForm() {
				this.$refs["form"].validate(valid => {
					if (valid) {
						this.form.monthOrDay = this.form.checkList.toString()
						this.form.wmsInspectionPlanDetailList = this.wmsInspectionPlanDetailList;
						this.form.wmsInspectionPlanDetailList.map((res, index) => {
							res.sort = index + 1
							if (!res.equipmentId) {
								res.equipmentId = res.id
							}
						})
						if (this.form.id != null) {
							updatePlan(this.form).then(response => {
								this.$modal.msgSuccess("修改成功");
								this.open = false;
								this.getList();
							});
						} else {

							addPlan(this.form).then(response => {
								this.$modal.msgSuccess("新增成功");
								this.open = false;
								this.getList();
							});
						}
					}
				});
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				const ids = row.id || this.ids;
				this.$modal.confirm('是否确认删除' + (row.id ? '该条' : '这些') + '数据？').then(function() {
					return delPlan(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("删除成功");
				}).catch(() => {});
			},
			/** 巡检计划设备列序号 */
			rowWmsInspectionPlanDetailIndex({ row, rowIndex }) {
				row.index = rowIndex + 1;
			},
			/** 巡检计划设备列添加按钮操作 */
			handleAddWmsInspectionPlanDetail() {
				this.openEqu = true
				// let obj = {};
				// obj.equipmentId = "";
				// this.wmsInspectionPlanDetailList.push(obj);
			},
			/** 巡检计划设备列删除按钮操作 */
			handleDeleteWmsInspectionPlanDetail() {
				if (this.checkedWmsInspectionPlanDetail.length == 0) {
					this.$modal.msgError("请先选择要删除的巡检计划设备列数据");
				} else {
					const wmsInspectionPlanDetailList = this.wmsInspectionPlanDetailList;
					const checkedWmsInspectionPlanDetail = this.checkedWmsInspectionPlanDetail;
					this.wmsInspectionPlanDetailList = wmsInspectionPlanDetailList.filter(function(item) {
						return checkedWmsInspectionPlanDetail.indexOf(item.index) == -1
					});
				}
			},
			/** 复选框选中数据 */
			handleWmsInspectionPlanDetailSelectionChange(selection) {
				this.checkedWmsInspectionPlanDetail = selection.map(item => item.index)
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download('equipmentplan/plan/export', { ...this.queryParams }, `plan_${new Date().getTime()}.xlsx`)
			}
		}
	};
</script>