<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
			label-width="68px">
			<el-form-item label="设备名称" prop="name">
				<el-input v-model="queryParams.name" placeholder="请输入设备名称" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="设备编号" prop="equNo">
				<el-input v-model="queryParams.equNo" placeholder="请输入设备编号" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="资产编号" prop="assetNo">
				<el-input v-model="queryParams.assetNo" placeholder="请输入资产编号" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<!-- <el-form-item label="序列号" prop="serialNo">
				<el-input v-model="queryParams.serialNo" placeholder="请输入序列号" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item> -->
			<el-form-item label="规格型号" prop="model">
				<el-input v-model="queryParams.model" placeholder="请输入规格型号" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="功能位置" prop="functionLocation">
				<el-input v-model="queryParams.functionLocation" placeholder="请输入功能位置" clearable
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="所属部门" prop="depId">
				<treeselect v-model="queryParams.depId" :options="deptOptions" :normalizer="normalizer"
					placeholder="选择所属部门" style="width: 215px;" />
			</el-form-item>
			<el-form-item label="供应商" prop="supplier">
				<el-select v-model="queryParams.supplier" filterable placeholder="请选择供应商"
					clearable>
					<el-option v-for="item in supplierList" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="设备状态" prop="useStatus">
				<el-select v-model="queryParams.useStatus" placeholder="请选择设备状态" clearable filterable>
					<el-option v-for="dict in dict.type.equipment_type" :key="dict.value" :label="dict.label"
						:value="dict.value" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
					v-hasPermi="['base:equipment:add']">新增</el-button>
			</el-col>
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table v-loading="loading" :data="equipmentList" @selection-change="handleSelectionChange">
			<el-table-column label="序号" type="index" align="center" />
			<el-table-column label="设备编号" align="center" prop="equNo" />
			<el-table-column label="设备名称" align="center" prop="name" />
			<el-table-column label="资产编号" align="center" prop="assetNo" />
			<!-- <el-table-column label="序列号" align="center" prop="serialNo" /> -->
			<el-table-column label="规格型号" align="center" prop="model" />
			<el-table-column label="功能位置" align="center" prop="functionLocation" />
			<el-table-column label="供应商" align="center" prop="supplierName" />
			<el-table-column label="图片" align="center" prop="img">
				<template slot-scope="scope">
					<div v-if="scope.row.img&&scope.row.img.length>0">
						<img style="width: 50px;height: 50px;" v-show="index==0" v-for="(item,index) in scope.row.img.split(',')" :key="item+index" :src="item" alt="" :preview="scope.row.id">
					</div>
					<div v-else>
						<span>暂无图片</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="设备状态" align="center" prop="useStatus">
				<template slot-scope="scope">
					<dict-tag :options="dict.type.equipment_type" :value="scope.row.useStatus" />
				</template>
			</el-table-column>
			<el-table-column label="责任人" align="center" prop="person" />
			<el-table-column label="所属部门" align="center" prop="depName" />
			<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
						v-hasPermi="['base:equipment:edit']">修改</el-button>
					<el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
						v-hasPermi="['base:equipment:remove']">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
			@pagination="getList" />

		<!-- 添加或修改设备台账对话框 -->
		<el-dialog :title="title" :visible.sync="open" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="120px">
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="设备名称" prop="name">
							<el-input v-model="form.name" placeholder="请输入设备名称" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="设备编号" prop="equNo">
							<el-input v-model="form.equNo" placeholder="请输入设备编号" />
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="资产编号" prop="assetNo">
							<el-input v-model="form.assetNo" placeholder="请输入资产编号" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="序列号">
							<el-input v-model="form.serialNo" placeholder="请输入序列号" />
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="规格型号" prop="model">
							<el-input v-model="form.model" placeholder="请输入规格型号" />
						</el-form-item>
					</el-col>
					<el-col :span="12">

						<el-form-item label="功能位置" prop="functionLocation">
							<el-input v-model="form.functionLocation" placeholder="请输入功能位置" />
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="所属部门" prop="depId">
							<treeselect v-model="form.depId" :options="deptOptions" :normalizer="normalizer"
								placeholder="选择所属部门" @select="getPerson" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="供应商" prop="supplier">
							<el-select v-model="form.supplier" filterable placeholder="请选择供应商" clearable
								style="width: 100%;">
								<el-option v-for="item in supplierList" :key="item.id" :label="item.name"
									:value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="12">
						<el-form-item label="使用状态" prop="useStatus">
							<el-select v-model="form.useStatus" placeholder="请选择设备状态" clearable filterable
								style="width: 100%;">
								<el-option v-for="dict in dict.type.equipment_type" :key="dict.value"
									:label="dict.label" :value="dict.value" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="责任人" prop="person">
							<el-select v-model="form.person" filterable placeholder="请选择责任人" clearable
								style="width: 100%;">
								<el-option v-for="item in personList" :key="item.userId" :label="item.userName"
									:value="item.userId">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-form-item label="图片">
					<image-upload v-model="form.img"></image-upload>
				</el-form-item>
				<el-form-item label="备注">
					<el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		listDept
	} from "@/api/system/dept";
	import Treeselect from "@riophae/vue-treeselect";
	import "@riophae/vue-treeselect/dist/vue-treeselect.css";
	import {
		listEquipment,
		getEquipment,
		delEquipment,
		addEquipment,
		updateEquipment
	} from "@/api/equipment/equipment";
	import {
		getAction
	} from "@/api/manage"
	export default {
		name: "Equipment",
		dicts: ["equipment_type"],
		components: {
			Treeselect
		},
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
				// 设备台账表格数据
				equipmentList: [],
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					name: null,
					equNo: null,
					assetNo: null,
					serialNo: null,
					model: null,
					functionLocation: null,
					depId: null,
					depName: null,
					supplier: null,
					useStatus: null,
					person: null,
				},
				// 表单参数
				form: {},
				// 表单校验
				rules: {
					name: [{
						required: true,
						message: '请输入设备名称',
						trigger: 'blur'
					}],
					equNo: [{
						required: true,
						message: '请输入设备编号',
						trigger: 'blur'
					}],
					assetNo: [{
						required: true,
						message: '请输入资产编号',
						trigger: 'blur'
					}],
					model: [{
						required: true,
						message: '请输入规格型号',
						trigger: 'blur'
					}],
					functionLocation: [{
						required: true,
						message: '请输入功能位置',
						trigger: 'blur'
					}],
					depId: [{
						required: true,
						message: '请选择所属部门',
						trigger: 'change'
					}],
					supplier: [{
						required: true,
						message: '请选择供应商',
						trigger: 'change'
					}],
					useStatus: [{
						required: true,
						message: '请选择使用状态',
						trigger: 'change'
					}],
					person: [{
						required: true,
						message: '请选择责任人',
						trigger: 'change'
					}],
				},
				supplierList: [], //供应商列表
				deptOptions: [], //部门列表
				personList: [], //责任人列表
			};
		},
		created() {
			this.getList();
			this.getSupplier()
			this.getListDept()
		},
		methods: {
			// 获取责任人列表
			getPerson(node) {
				console.log(node)
				this.form.depName = node.deptName
				getAction('system/user/list', {
					deptId: node.deptId
				}).then(res => {
					console.log(res)
					if (res.code == 200) {
						this.personList = res.rows
					} else {
						this.$modal.msgError(res.msg);
					}
				})
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
			// 获取供应商下拉列表
			getSupplier() {
				getAction('/wms/contacts/unit/list', {}).then(res => {
					if (res.code == 200) {
						this.supplierList = res.rows
					}
				})
			},
			/** 查询设备台账列表 */
			getList() {
				this.loading = true;
				listEquipment(this.queryParams).then(response => {
					this.equipmentList = response.rows;
					this.total = response.total;
				}).finally(() => {
					this.loading = false;
				});
			},
			// 取消按钮
			cancel() {
				this.open = false;
				this.reset();
			},
			// 表单重置
			reset() {
				this.form = {
					id: null,
					name: null,
					equNo: null,
					assetNo: null,
					serialNo: null,
					model: null,
					functionLocation: null,
					depId: null,
					depName: null,
					supplier: null,
					useStatus: null,
					person: null,
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
				this.open = true;
				this.title = "添加设备台账";
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.reset();
				const id = row.id || this.ids
				getEquipment(id).then(response => {
					this.form = response.data;
					this.getPerson({
						deptId: response.data.depId
					})
					this.form.useStatus = response.data.useStatus ? response.data.useStatus.toString() : response
						.data.useStatus
					this.form.supplier = response.data.supplier ? Number(response.data.supplier) : response.data
						.supplier
					this.form.person = response.data.person ? Number(response.data.person) : response.data.person
					this.open = true;
					this.title = "修改设备台账";
				});
			},
			/** 提交按钮 */
			submitForm() {
				this.$refs["form"].validate(valid => {
					if (valid) {
						if (this.form.id != null) {
							updateEquipment(this.form).then(response => {
								this.$modal.msgSuccess("修改成功");
								this.open = false;
								this.getList();
							});
						} else {
							addEquipment(this.form).then(response => {
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
				this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
					return delEquipment(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("删除成功");
				}).catch(() => {});
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download('base/equipment/export', {
					...this.queryParams
				}, `equipment_${new Date().getTime()}.xlsx`)
			}
		}
	};
</script>