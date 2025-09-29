<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
			<el-form-item label="标准名称" prop="name">
				<el-input v-model="queryParams.name" placeholder="请输入标准名称" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="标准编号" prop="itemNo">
				<el-input v-model="queryParams.itemNo" placeholder="请输入标准编号" clearable @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="适用设备" prop="equipmentId">
				<el-select v-model="queryParams.equipmentId" filterable placeholder="请选择适用设备" clearable>
					<el-option v-for="item in equipmentList" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</el-form-item>
			<!-- 		<el-form-item label="所属部门" prop="depId">
				<treeselect v-model="queryParams.depId" :options="deptOptions" :normalizer="normalizer"
					placeholder="选择所属部门" style="width: 215px;" />
			</el-form-item> -->
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['equipmentItems:items:add']">新增</el-button>
			</el-col>
			<!-- 			<el-col :span="1.5">
				<el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
					v-hasPermi="['equipmentItems:items:edit']">修改</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
					@click="handleDelete" v-hasPermi="['equipmentItems:items:remove']">删除</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
					v-hasPermi="['equipmentItems:items:export']">导出</el-button>
			</el-col> -->
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>
		<el-table v-loading="loading" :data="itemsList" @selection-change="handleSelectionChange">
			<el-table-column label="序号" type="index" width="55" align="center" />
			<el-table-column label="标准编号" align="center" prop="itemNo" />
			<el-table-column label="标准名称" align="center" prop="name" />
			<el-table-column label="适用设备" align="center" prop="equipmentName" />
			<el-table-column label="标准内容" align="center" prop="remark" />
			<el-table-column label="创建时间" align="center" prop="createTime" />
			<el-table-column label="状态" align="center" prop="status">
				<template slot-scope="scope">
					<div style="display: flex;justify-content: center;">
						<div :class="scope.row.status==1?'green':'red'"> {{scope.row.status==1?'启用':'作废'}}</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
				<template slot-scope="scope">
					<el-button v-if="scope.row.status==1" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['equipmentItems:items:edit']">编辑</el-button>
					<el-button size="mini" type="text" icon="el-icon-document" @click="handleLook(scope.row)" v-hasPermi="['equipmentItems:items:edit']">查看</el-button>
					<el-button v-if="scope.row.status==1" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['equipmentItems:items:remove']">作废</el-button>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
			@pagination="getList" />

		<!-- 添加或修改巡检标准对话框 -->
		<el-dialog :title="title" :visible.sync="open" width="40%" append-to-body>
			<div style="padding: 0 50px;">
				<el-form ref="form" :model="form" :rules="rules" label-width="80px">
					<el-form-item label="标准名称" prop="name">
						<el-input v-model="form.name" :disabled="dialong" placeholder="请输入标准名称" />
					</el-form-item>
					<el-form-item label="标准编号" prop="itemNo">
						<el-input v-model="form.itemNo" :disabled="dialong" placeholder="请输入巡检编号" />
					</el-form-item>
					<el-form-item label="适用设备" prop="equipmentIdArr">
						<el-select v-model="form.equipmentIdArr" @change="$forceUpdate()" :disabled="dialong" multiple filterable placeholder="请选择适用设备" clearable style="width: 100%;">
							<el-option v-for="item in equipmentList" :key="item.id" :label="item.name" :value="item.id"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="标准内容" prop="remark">
						<el-input v-model="form.remark" type="textarea" :disabled="dialong" placeholder="请输入内容" />
					</el-form-item>
				</el-form>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm" v-if="!dialong">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import { getAction } from "@/api/manage"
	import Treeselect from "@riophae/vue-treeselect";
	import "@riophae/vue-treeselect/dist/vue-treeselect.css";
	import { listDept } from "@/api/system/dept";
	import { listItems, getItems, delItems, addItems, updateItems } from "@/api/equipment/equipmentItems/items";

	export default {
		name: "Items",
		components: { Treeselect },
		data() {
			return {
				dialong: false, //是否可以编辑
				equipmentList: [], //设备列表
				deptOptions: [], //部门列表
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
				// 巡检标准表格数据
				itemsList: [],
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					name: null,
					itemNo: null,
					depId: null,
					depName: null,
					equipmentId: null
				},
				// 表单参数
				form: {
					name: '',
					itemNo: '',
					equipmentIdArr: [],
					remark: ''

				},
				// 表单校验
				rules: {
					name: [{
						required: true,
						message: '请输入标准名称',
						trigger: 'blur'
					}],
					itemNo: [{
						required: true,
						message: '请输入标准编号',
						trigger: 'blur'
					}],
					equipmentIdArr: [{
						required: true,
						message: '请选择适用设备',
						trigger: 'blur，changes'
					}],
					remark: [{
						required: true,
						message: '请输入标准内容',
						trigger: 'blur'
					}],
				}
			};
		},
		created() {
			this.getList();
			this.getListDept();
			this.getEquipmentList()
		},
		methods: {
			//  查看
			handleLook(row) {
				this.reset();
				const id = row.id || this.ids
				getItems(id).then(response => {
					this.form = response.data;
					this.form.equipmentIdArr = this.form.equipmentId.split(',').map(Number)
					this.open = true;
					this.dialong = true
					this.title = "查看巡检标准";
				});
			},
			// 获取设备下拉列表
			getEquipmentList() {
				getAction('/wms/equipment/list?pageSize=1000').then(res => {
					if (res.code == 200) {
						this.equipmentList = res.rows
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
			/** 查询巡检标准列表 */
			getList() {
				this.loading = true;
				listItems(this.queryParams).then(response => {
					this.itemsList = response.rows;
					this.total = response.total;
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
					itemNo: null,
					remark: null,
					createBy: null,
					createTime: null,
					updateBy: null,
					updateTime: null,
					deptId: null,
					deptName: null,
					delFlag: null,
					depId: null,
					depName: null,
					equipmentId: null
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
				this.dialong = false
				this.title = "添加巡检标准";
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.reset();
				const id = row.id || this.ids
				getItems(id).then(response => {
					this.form = response.data;
					this.form.equipmentIdArr = this.form.equipmentId.split(',').map(Number)
					this.open = true;
					this.dialong = false
					this.title = "修改巡检标准";
				});
			},
			/** 提交按钮 */
			submitForm() {
				let that = this
				this.$refs["form"].validate(valid => {
					if (valid) {
						that.form.equipmentId = that.form.equipmentIdArr.toString()
						if (this.form.id != null) {
							updateItems(this.form).then(response => {
								this.$modal.msgSuccess("修改成功");
								this.open = false;
								this.getList();
							});
						} else {
							addItems(this.form).then(response => {
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
				const ids = row.itemNo
				let formClone = {
					id: row.id,
					itemNo: row.itemNo,
					status: 0
				}
				this.$modal.confirm('是否确认废除该条数据？').then(function() {
					return updateItems(formClone);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("废除成功");
				}).catch(() => {});
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download('equipmentItems/items/export', { ...this.queryParams }, `items_${new Date().getTime()}.xlsx`)
			}
		}
	};
</script>
<style scoped lang="scss">
	.green {
		width: 50%;
		padding: 5px;
		color: #fff;
		background-color: #33ccb0;
		border-radius: 5px;
		border: 1px solid #000000;
	}

	.red {
		width: 50%;
		padding: 5px;
		color: #fff;
		background-color: #ee0d0d;
		border-radius: 5px;
		border: 1px solid #000000;
	}
</style>