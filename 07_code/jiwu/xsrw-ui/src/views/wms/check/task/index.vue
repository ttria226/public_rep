<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
			label-width="68px">
			<el-form-item label="所属盘点计划">
				<el-input v-model="queryParams.planName" placeholder="请输入盘点计划" />
			</el-form-item>
			<el-form-item label="任务号">
				<el-input v-model="queryParams.code" placeholder="请输入任务号" />
			</el-form-item>
			<el-form-item label="所属库区">
				<el-select v-model="queryParams.reservoirId" @change="changeQueryGoodShelfList()" placeholder="请选择所属库区" clearable>
					<el-option v-for="dict in kqQueryList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="状态">
				<el-select v-model="queryParams.taskStatus" clearable placeholder="请选择状态">
					<el-option v-for="dict in dict.type.task_status" :key="dict.value" :label="dict.label"
						:value="dict.value"></el-option>
				</el-select>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-odometer" size="mini" :disabled="multiple"
					@click="handleActivate" v-hasPermi="['check:task:remove']">激活任务</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-stopwatch" size="mini" :disabled="multiple"
					@click="handleStop" v-hasPermi="['check:task:remove']">终止任务</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
					@click="handleDelete" v-hasPermi="['check:task:remove']">删除</el-button>
			</el-col>
			<!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="ImportBill"
        >单据导入</el-button>
      </el-col> -->
<!-- 			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-document-checked" size="mini" @click="handleApprove"
					v-hasPermi="['wms:checkTask:approve']">确认</el-button>
			</el-col> -->
			<!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="addResult"
          v-hasPermi="['wms:checkResult:createCheckResult']"
        >生成盘点报表</el-button>
      </el-col> -->
			<!--      <el-col :span="1.5">-->
			<!--        <el-button-->
			<!--          type="warning"-->
			<!--          plain-->
			<!--          icon="el-icon-download"-->
			<!--          size="mini"-->
			<!--          @click="handleExport"-->
			<!--          v-hasPermi="['wms:task:export']"-->
			<!--        >导出</el-button>-->
			<!--      </el-col>-->
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-tabs v-model="currentTab" class="center-tabs" type="border-card" @tab-click="handleClick">
			<el-tab-pane label="一楼" name="one" >
				<div class="field-box">
					<el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange" v-show="currentTab==='one'">
						<el-table-column type="selection" width="55" align="center" />
						<el-table-column label="任务号" fixed min-width="100" align="center" prop="code" :show-overflow-tooltip="true" />
						<el-table-column label="载具编码" fixed min-width="100" align="center" prop="trayCode"  :show-overflow-tooltip="true"/>
						<el-table-column label="所属库区" fixed min-width="100" align="center" prop="areaName" :show-overflow-tooltip="true"/>
						<el-table-column label="所在库位" fixed min-width="100" align="center" prop="locationName" :show-overflow-tooltip="true"/>
						<el-table-column label="所属盘点计划" fixed min-width="100" align="center" prop="planName" :show-overflow-tooltip="true"/>
						<el-table-column label="来源" fixed min-width="100" align="center" prop="checkSource" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<span v-if="scope.row.checkSource == '1'">手工创建</span>
								<span v-if="scope.row.checkSource == '2'">ERP导入</span>
							</template>
						</el-table-column>
						<el-table-column label="任务激活状态" fixed min-width="100" align="center" prop="activateStatus" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<span v-if="scope.row.activateStatus == '0'">未激活</span>
								<span v-if="scope.row.activateStatus == '1'">已激活</span>
							</template>
						</el-table-column>
						<el-table-column label="任务执行状态" fixed min-width="100" align="center" prop="taskStatus" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<dict-tag :options="dict.type.task_status" :value="scope.row.taskStatus" />
							</template>
						</el-table-column>
						<!-- <el-table-column label="仓库名称" align="center" prop="currentWarehouse" :show-overflow-tooltip="true" /> -->
						<!-- <el-table-column label="状态" align="center" prop="taskStatus" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<dict-tag :options="dict.type.task_status" :value="scope.row.taskStatus" />
							</template>
						</el-table-column> -->
						<!-- <el-table-column label="所属组织" align="center" prop="deptName" :show-overflow-tooltip="true" /> -->
						<!-- <el-table-column label="盘点策略" align="center" prop="checkType" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<span v-if="scope.row.checkType == '1'">物料</span>
								<span v-if="scope.row.checkType == '2'">库区</span>
								<span v-if="scope.row.checkType == '4'">随机</span>
								<span v-if="scope.row.checkType == '3'">动碰</span>
								<span v-if="scope.row.checkType == '5'">空货位</span>
							</template>
						</el-table-column> -->
						<el-table-column label="任务创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" />
						<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
							<template slot-scope="scope">
								<el-button size="mini" type="text" icon="el-icon-document" @click="handleSeeUpdate(scope.row)"
									v-hasPermi="['check:taskDetail:getListByTaskId']">查看</el-button>
								<el-button v-if="scope.row.taskStatus==0" size="mini" type="text" icon="el-icon-document" @click="handleOutbound(scope.row)"
									>载具出库</el-button>
								<el-button size="mini" type="text" icon="el-icon-document" @click="handleSeeResult(scope.row)"
									v-hasPermi="['check:taskDetail:getListByTaskId']">登记盘点结果</el-button>
							</template>
						</el-table-column>
					</el-table>

					<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
						@pagination="getList" />
				</div>
			</el-tab-pane>
			<el-tab-pane label="二楼" name="two">
				<div class="field-box">
					<el-table v-loading="loading" :data="taskTwoList" @selection-change="handleSelectionChange">
						<el-table-column type="selection" width="55" align="center" />
						<el-table-column label="任务号" fixed min-width="100" align="center" prop="code" :show-overflow-tooltip="true" />
						<el-table-column label="载具编码" fixed min-width="100" align="center" prop="trayCode"  :show-overflow-tooltip="true"/>
						<el-table-column label="所属库区" fixed min-width="100" align="center" prop="areaName" :show-overflow-tooltip="true"/>
						<el-table-column label="所在库位" fixed min-width="100" align="center" prop="locationName" :show-overflow-tooltip="true"/>
						<el-table-column label="所属盘点计划" fixed min-width="100" align="center" prop="planName" :show-overflow-tooltip="true"/>
						<el-table-column label="来源" fixed min-width="100" align="center" prop="checkSource" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<span v-if="scope.row.checkSource == '1'">手工创建</span>
								<span v-if="scope.row.checkSource == '2'">ERP导入</span>
							</template>
						</el-table-column>
						<el-table-column label="任务激活状态" fixed min-width="100" align="center" prop="activateStatus" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<span v-if="scope.row.activateStatus == '0'">未激活</span>
								<span v-if="scope.row.activateStatus == '1'">已激活</span>
							</template>
						</el-table-column>
						<el-table-column label="任务执行状态" fixed min-width="100" align="center" prop="taskStatus" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<dict-tag :options="dict.type.task_status" :value="scope.row.taskStatus" />
							</template>
						</el-table-column>
						<el-table-column label="任务创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" />
						<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
							<template slot-scope="scope">
								<el-button size="mini" type="text" icon="el-icon-document" @click="handleSeeUpdate(scope.row)"
									v-hasPermi="['check:taskDetail:getListByTaskId']">查看</el-button>
	<el-button v-if="scope.row.taskStatus==0" size="mini" type="text" icon="el-icon-document" @click="handleOutbound(scope.row)"
									>载具出库</el-button>
								<el-button size="mini" type="text" icon="el-icon-document" @click="handleSeeResult(scope.row)"
									v-hasPermi="['check:taskDetail:getListByTaskId']">登记盘点结果</el-button>
							</template>
						</el-table-column>
					</el-table>

					<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
						@pagination="getList" />
				</div>
			</el-tab-pane>
			<el-tab-pane label="地堆" name="didui">
				<div class="field-box">
					<el-table v-loading="loading" :data="taskDiduiList" @selection-change="handleSelectionChange">
						<el-table-column type="selection" width="55" align="center" />
						<el-table-column label="任务号" fixed min-width="100" align="center" prop="code" :show-overflow-tooltip="true" />
						<el-table-column label="载具编码" fixed min-width="100" align="center" prop="trayCode"  :show-overflow-tooltip="true"/>
						<el-table-column label="所属库区" fixed min-width="100" align="center" prop="areaName" :show-overflow-tooltip="true"/>
						<el-table-column label="所在库位" fixed min-width="100" align="center" prop="locationName" :show-overflow-tooltip="true"/>
						<el-table-column label="所属盘点计划" fixed min-width="100" align="center" prop="planName" :show-overflow-tooltip="true"/>
						<el-table-column label="来源" fixed min-width="100" align="center" prop="checkSource" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<span v-if="scope.row.checkSource == '1'">手工创建</span>
								<span v-if="scope.row.checkSource == '2'">ERP导入</span>
							</template>
						</el-table-column>
						<el-table-column label="任务激活状态" fixed min-width="100" align="center" prop="activateStatus" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<span v-if="scope.row.activateStatus == '0'">未激活</span>
								<span v-if="scope.row.activateStatus == '1'">已激活</span>
							</template>
						</el-table-column>
						<el-table-column label="任务执行状态" fixed min-width="100" align="center" prop="taskStatus" :show-overflow-tooltip="true">
							<template slot-scope="scope">
								<dict-tag :options="dict.type.task_status" :value="scope.row.taskStatus" />
							</template>
						</el-table-column>
						<el-table-column label="任务创建时间" align="center" prop="createTime" :show-overflow-tooltip="true" />
						<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
							<template slot-scope="scope">
								<el-button size="mini" type="text" icon="el-icon-document" @click="handleSeeUpdate(scope.row)"
									v-hasPermi="['check:taskDetail:getListByTaskId']">查看</el-button>
	<el-button v-if="scope.row.taskStatus==0" size="mini" type="text" icon="el-icon-document" @click="handleOutbound(scope.row)"
									>载具出库</el-button>
								<el-button size="mini" type="text" icon="el-icon-document" @click="handleSeeResult(scope.row)"
									v-hasPermi="['check:taskDetail:getListByTaskId']">登记盘点结果</el-button>
							</template>
						</el-table-column>
					</el-table>

					<pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
						@pagination="getList" />
				</div>
			</el-tab-pane>
		</el-tabs>

		
		<!-- 添加或修改任务对话框 -->
		<el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
			<el-form ref="form" :model="form" :rules="rules" label-width="80px">
				<el-form-item label="来源标识(收货id、盘点id、回库id、移库id)" prop="sourceId">
					<el-input v-model="form.sourceId" placeholder="请输入来源标识(收货id、盘点id、回库id、移库id)" />
				</el-form-item>
				<el-form-item label="任务详情数量" prop="taskCount">
					<el-input v-model="form.taskCount" placeholder="请输入任务详情数量" />
				</el-form-item>
				<!-- <el-form-item label="仓库编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入仓库编码" />
        </el-form-item> -->
				<el-form-item label="源单单号" prop="code">
					<el-input v-model="form.code" placeholder="请输入源单单号" />
				</el-form-item>
				<el-form-item label="源单日期" prop="originDate">
					<el-date-picker clearable v-model="form.originDate" type="date" value-format="yyyy-MM-dd"
						placeholder="请选择源单日期">
					</el-date-picker>
				</el-form-item>
				<el-form-item label="往来单位的标识" prop="unitCode">
					<el-input v-model="form.unitCode" placeholder="请输入往来单位的标识" />
				</el-form-item>
				<el-form-item label="部门标识" prop="factory">
					<el-input v-model="form.factory" placeholder="请输入部门标识" />
				</el-form-item>
				<el-form-item label="订单编号" prop="current_warehouse_id">
					<el-input v-model="form.current_warehouse_id" placeholder="请输入订单编号" />
				</el-form-item>
				<el-form-item label="审核人" prop="auditor">
					<el-input v-model="form.auditor" placeholder="请输入审核人" />
				</el-form-item>
				<el-form-item label="备注" prop="remark">
					<el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
				</el-form-item>
				<el-form-item label="删除(0:未删除 1:删除)" prop="delFlag">
					<el-input v-model="form.delFlag" placeholder="请输入删除(0:未删除 1:删除)" />
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitForm">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>

		<!-- 确认对话框 -->
		<el-dialog title="审核" :visible.sync="statusOpen" width="500px" append-to-body>
			<el-form ref="formStatus" :model="formStatus" :rules="rulesStatus" label-width="80px">
				<el-form-item label="状态" prop="status">
					<el-select v-model="formStatus.status" clearable placeholder="请选择状态">
						<el-option v-for="item in formStatusList" :key="item.dictValue" :label="item.dictLabel"
							:value="item.dictValue">
						</el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="submitStatus">确 定</el-button>
				<el-button @click="handleStatus">取 消</el-button>
			</div>
		</el-dialog>

		<!-- 查看详情对话框 -->
		<el-dialog title="查看" :visible.sync="seeOpen" v-if="seeOpen" width="60%" append-to-body>
			<!-- <el-row :gutter="10" class="mb8">
				<el-col :span="1.5">
					<el-button type="primary" plain icon="el-icon-document-checked" size="mini" @click="handleStatus"
						v-hasPermi="['wms:checkTask:examine']">审核</el-button>
				</el-col>
			</el-row> -->
			<el-table :data="formSeeList" @selection-change="formSeeOnChange">
				<el-table-column type="selection" fixed width="55" align="center" />
				<el-table-column label="物料名称" fixed min-width="150" align="center" prop="materialName" :show-overflow-tooltip="true"
					v-if="formSeeList && formSeeList.length > 0 && (formSeeList[0].deliveryType != '5' || formSeeList[0].deliveryType != '6')" />
				<el-table-column label="载具" fixed min-width="130" align="center" prop="trayCode"
					:show-overflow-tooltip="true" />
				<el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true">
					<template slot-scope="scope">
						<dict-tag :options="dict.type.task_status" :value="scope.row.status" />
					</template>
				</el-table-column>
				<!-- <el-table-column label="优先级" align="center" prop="priority" :show-overflow-tooltip="true" /> -->
				<!-- <el-table-column label="仓库名称" align="center" width="160" prop="wareHouseName" :show-overflow-tooltip="true" /> -->
				<el-table-column label="批号" align="center" min-width="140" prop="batchNumber" :show-overflow-tooltip="true"
					v-if="formSeeList && formSeeList.length > 0 && (formSeeList[0].deliveryType != '5' || formSeeList[0].deliveryType != '6')" />
				<el-table-column label="RFID" align="center" width="200" prop="rfidHead" :show-overflow-tooltip="true" />
				<el-table-column label="实际盘点数量" align="center" width="120" prop="actualCount" :show-overflow-tooltip="true">
					<template slot-scope="scope">
						<span v-if="scope.row.status==2">{{scope.row.predictCount}}</span>
						<span v-if="scope.row.status < 2">{{scope.row.actualCount}}</span>
					</template>
				</el-table-column>
				<el-table-column label="盘差" align="center" prop="checkDifferenceCount" :show-overflow-tooltip="true"
					:formatter="checkFormat" />
				<el-table-column label="部门" align="center" min-width="120" prop="deptName" :show-overflow-tooltip="true" />
				<el-table-column label="库位" width="120" align="center" prop="locationName" :show-overflow-tooltip="true"
					v-if="formSeeList && formSeeList.length > 0 && (formSeeList[0].deliveryType == '5' || (formSeeList[0].deliveryType == '4' && formSeeList[0].wareHouseType == '1'))" />
				<el-table-column label="库区" align="center" width="180" prop="reservoirName"
					:show-overflow-tooltip="true"
					v-if="formSeeList && formSeeList.length > 0 && (formSeeList[0].deliveryType == '5' || (formSeeList[0].deliveryType == '4' && formSeeList[0].wareHouseType == '1'))" />
				<el-table-column label="区域" align="center" width="180" prop="areaName" :show-overflow-tooltip="true" />
			</el-table>
			<pagination v-show="totalSee>0" :total="totalSee" :page.sync="SeeParams.pageNum"
				:limit.sync="SeeParams.pageSize" @pagination="getSeeList" />
		</el-dialog>

		<!-- 上传弹框 -->
		<el-dialog title="导入单据" :visible.sync="upload.open" width="400px" append-to-body @close="cancel">
			<el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url"
				:file-list="fileListd" :disabled="upload.isUploading" :on-remove="onRemove" :on-change="onChanc"
				:on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag
				:before-upload="handleBeforeUpload">
				<i class="el-icon-upload"></i>
				<div class="el-upload__text">将文件拖到此处，或
					<em>点击上传</em>
				</div>
				<div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！最大不超过50M</div>
			</el-upload>
			<!-- <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm" :loading="upload.isUploading">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div> -->
		</el-dialog>
		<!-- 登记盘点结果 -->
		<el-dialog title="登记盘点结果" :visible.sync="registerOpen" v-if="registerOpen" width="60%" append-to-body>
			<div class="app-container" style="padding:5px">
				<el-button type="primary" plain icon="el-icon-s-promotion" size="mini" @click="updateCheckCount" v-hasPermi="['check:task:updateCheckCount']">批量修改实盘数量</el-button>
			</div>
			<el-table :data="formSeeResultList" @selection-change="formSeeOnChange">
				<el-table-column type="selection" fixed width="55" align="center" />
				<el-table-column label="库存id" align="center" prop="taskId" :show-overflow-tooltip="true" v-if="1==2" />
				<el-table-column label="物料编码" fixed min-width="150" align="center" prop="materialCode" :show-overflow-tooltip="true"
					v-if="formSeeResultList && formSeeResultList.length > 0 && (formSeeResultList[0].deliveryType != '5' || formSeeResultList[0].deliveryType != '6')" />
				<el-table-column label="物料名称" fixed min-width="150" align="center" prop="materialName" :show-overflow-tooltip="true"
					v-if="formSeeResultList && formSeeResultList.length > 0 && (formSeeResultList[0].deliveryType != '5' || formSeeResultList[0].deliveryType != '6')" />
				<el-table-column label="批号" align="center" min-width="150" prop="batchNumber" :show-overflow-tooltip="true"
					v-if="formSeeResultList && formSeeResultList.length > 0 && (formSeeResultList[0].deliveryType != '5' || formSeeResultList[0].deliveryType != '6')" />
				<el-table-column label="计量单位" fixed min-width="150" align="center" prop="unitName"
					:show-overflow-tooltip="true" />
				<el-table-column label="库存数量" align="center" prop="predictCount" :show-overflow-tooltip="true" />
				<el-table-column label="实盘数量" align="center" prop="actualCount" :show-overflow-tooltip="true" >
					<template slot-scope="scope">
						<el-input v-model="scope.row.actualCount" placeholder="请输入实盘数量" v-intNumber size="small" maxlength="6"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="备注" align="center" min-width="150" prop="remark" :show-overflow-tooltip="true" >
					<template slot-scope="scope">
						<el-input v-model="scope.row.remark" placeholder="请输入备注" maxlength="60"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="盘点结果id" align="center" prop="checkResultId" :show-overflow-tooltip="true" v-if="1==2" />
			</el-table>
			<pagination v-show="totalSee>0" :total="totalSee" :page.sync="SeeParams.pageNum"
				:limit.sync="SeeParams.pageSize" @pagination="getSeeList" />
		</el-dialog>
	</div>
</template>

<script>
	import {
		listTask,
		getTask,
		delTask,
		addTask,
		updateTask,
		getListByTaskId,
		getCheckAreaHistory,
		getCheckTaskDetail,
		approve,
		approveAreaCheck,
		approveTask,
		actTask,
		stopTask,
		addTaskResult,
		addCheckResult,
		vehicleOutbound
	} from "@/api/wms/pdtask";
	import {
		listDept
	} from "@/api/system/dept";
	import "@riophae/vue-treeselect/dist/vue-treeselect.css";
	import {
		mapGetters
	} from 'vuex';
	import {
		wms
	} from '@/utils/agent';
	import { listReservoir } from "@/api/wms/reservoir";
	export default {
		name: "CheckTask",
		dicts: ['wms_task_status', 'wms_check_task_detail_status','task_status'],
		computed: {
			...mapGetters([
				'deptId', 'token'
			]),
		},
		data() {
			let regulationTypeRules = (rule, value, callback) => {
				if (!this.formStatus.status) {
					callback(new Error('请选择状态'));
				} else {
					callback();
				}
			}
			return {
				currentTab: 'one',
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
				// 任务表格数据
				taskList: [],
				taskTwoList:[],
				taskDiduiList:[],
				kqQueryList: [], // 筛选的库区列表
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
				// 查询参数
				queryParams: {
					pageNum: 1,
					pageSize: 10,
					taskType: 3,
					trayType:1,
					code: null,
					factory: null,
					currentWarehouseId: null,
					taskStatus: null,
				},
				// 表单参数
				form: {},
				// 表单校验
				rules: {},
				statusOpen: false,
				formStatus: {
					status: null,
				},
				rulesStatus: {
					status: [{
						required: true,
						validator: regulationTypeRules,
						trigger: "change"
					}],
				},
				// 仓库列表
				currentList: [],
				// 部门列表
				department: [],
				defaultParams: {
					label: 'label',
					value: 'id',
					children: 'children',
					checkStrictly: true
				},
				seeOpen: false,
				formSeeList: [],
				formSeeResultList:[],
				upload: {
					// 是否显示弹出层（导入）
					open: false,
					// 是否禁用上传
					isUploading: false,
					// 设置上传的请求头部
					headers: {},
					// 上传的地址
					url: process.env.VUE_APP_BASE_API + "/system/user/importData"
				},
				fileListd: [],
				SeeParams: {
					pageNum: 1,
					pageSize: 10,
					taskId: null,
					taskType: 3,
				},
				totalSee: 10,
				formStatusList: [{
						dictLabel: '通过',
						dictValue: 4
					},
					{
						dictLabel: '驳回',
						dictValue: 5
					}
				],
				formSeeIds: [],
				areaTaskDetailId: null, // 平库库区盘点的子任务id
				registerOpen:false,
				SeeResultParams: {
					pageNum: 1,
					pageSize: 10,
					taskId: null,
					taskType: 3,
				},
			};
		},
		created() {
			this.upload.headers = {
				Authorization: "Bearer " + this.token
			}
			this.getList();
			// 库区
			listReservoir({pageSize:5000}).then(response => {
			this.kqQueryList = response.rows;
			});
		},
		watch: {
			$route(to, form) {
				if (to.name == 'CheckTask') {
					this.getList()
				}
			}
		},
		methods: {
			chuku(row){
				vehicleOutbound({id:row.trayId,taskId:row.id}).then(res=>{
					this.getList();
					this.$modal.msgSuccess("出库成功");
				})
			},
			handleOutbound(row) {
				let that=this
			  this.$modal.confirm('是否确认该条数据载具出库？').then(function() {
			     that.chuku(row);
			  })
			},
			// 盘差正负号处理符号
			checkFormat(row, column) {
				let courseType = '';
				if (row.checkDifferenceCount > 0) {
					courseType = '+' + row.checkDifferenceCount
				} else {
					courseType = row.checkDifferenceCount
				}
				return courseType;
			},
			/** 查询任务列表 */
			getList() {
				this.loading = true;
				this.queryParams.taskType = 3
				// this.queryParams.trayType = 1;
				if(this.currentTab == "one"){  //一楼
					this.queryParams.trayType = 1;
				}else if(this.currentTab == "two"){ //二楼 
					this.queryParams.trayType = 2;
				}else{  //地堆
					this.queryParams.trayType = 3;
				}

				listTask(this.queryParams).then(response => {
					if(this.currentTab == "one"){  //一楼
						this.taskList = response.rows;
					}else if(this.currentTab == "two"){ //二楼 
						this.taskTwoList = response.rows;
					}else{  //地堆
						this.taskDiduiList = response.rows;
					}
					
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
					sourceId: null,
					taskType: null,
					taskStatus: "0",
					taskCount: null,
					code: null,
					originDate: null,
					originType: null,
					type: null,
					status: "0",
					unitCode: null,
					factory: null,
					current_warehouse_id: null,
					auditor: null,
					createBy: null,
					createTime: null,
					updateBy: null,
					updateTime: null,
					remark: null,
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
				this.queryParams.code = null
				this.queryParams.currentWarehouseId = null
				this.queryParams.taskStatus = null
				this.queryParams.factory = null
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
				this.title = "添加任务";
			},
			/** 修改按钮操作 */
			handleUpdate(row) {
				this.reset();
				const id = row.id || this.ids
				getTask(id).then(response => {
					this.form = response.data;
					this.open = true;
					this.title = "修改任务";
				});
			},
			handleSeeUpdate(row) {
				this.SeeParams.pageNum = 1
				this.SeeParams.taskId = row.id
				this.formSeeList = []
				this.areaTaskDetailId = null
				this.getSeeList()
			},
			getSeeList() {
				getListByTaskId(this.SeeParams).then(response => {
					if (response.rows && response.rows.length > 0 && response.rows[0].deliveryType == "6") {
						this.areaTaskDetailId = response.rows[0].id
						const data = {
							taskDetailId: response.rows[0].id
						}
						getCheckAreaHistory(data).then(res => {
							this.formSeeList = res.rows;
							this.totalSee = res.total;
							this.seeOpen = true
						})
					} else {
						this.formSeeList = response.rows;
						this.totalSee = response.total;
						this.seeOpen = true
					}
					// this.formSeeList = [
					//   {
					//     material:'TASK202205012135324001',
					//     tray:'载具',
					//     status:0,
					//     priority:'优先级',
					//     currentWarehouse:'TASK202205012135324001',
					//     batchNumber:'TASK202205012135324001',
					//     actualCount:10,
					//     checkDifferenceCount:11,
					//     factoryName:'跟部門',
					//     location:'',reservoir:'',area :''
					//   }
					// ];
				});
			},
			formSeeOnChange(selection) {
				this.formSeeIds = selection
			},
			// 审核
			handleStatus() {
				if (this.formSeeIds && this.formSeeIds.length > 0) {
					this.resetForm("formStatus");
					this.formStatus.status = null
					this.statusOpen = !this.statusOpen
				} else {
					this.$modal.msgError('请选择需要审核的任务')
				}
			},
			// 审核提交
			submitStatus() {
				this.$refs["formStatus"].validate(valid => {
					if (valid) {
						let ids = this.formSeeIds.toString()
						if (this.areaTaskDetailId) {
							const data = {
								taskDetailId: this.areaTaskDetailId,
								ids: this.formSeeIds,
								status: this.formStatus.status,
							}
							approveAreaCheck(data).then(res => {
								this.$modal.msgSuccess("确认成功");
								this.statusOpen = false;
								this.getSeeList();
								this.getList()
							})
						} else {
							const formStatus = this.formStatus
							if (this.formSeeIds[0].checkType == '2' && this.formSeeIds[0].wareHouseType == '1') {
								this.$modal.confirm('所选载具下的所有物料都将' + formStatus == '4' ? '通过' : '驳回').then(
									function() {
										return approve(ids, formStatus);
									}).then(() => {
									this.getList();
									this.$modal.msgSuccess("审核成功");
									this.statusOpen = false;
									this.getSeeList();
									this.getList()
								}).catch(() => {});
							} else {
								approve(ids, this.formStatus).then(response => {
									this.$modal.msgSuccess("审核成功");
									this.statusOpen = false;
									this.getSeeList();
									this.getList()
								});
							}
						}
					}
				})
			},
			/** 提交按钮 */
			submitForm() {
				this.$refs["form"].validate(valid => {
					if (valid) {
						if (this.form.id != null) {
							updateTask(this.form).then(response => {
								this.$modal.msgSuccess("修改成功");
								this.open = false;
								this.getList();
							});
						} else {
							addTask(this.form).then(response => {
								this.$modal.msgSuccess("新增成功");
								this.open = false;
								this.getList();
							});
						}
					}
				});
			},
			/** 审核按钮操作 */
			handleApprove(row) {
				let ids = row.id || this.ids;
				if (ids.length <= 0) {
					this.$modal.msgWarning('请选择需要确认的任务')
					return;
				}
				let codes = [];
				if(this.currentTab == "one"){  //一楼
					this.taskList.forEach(item => {
						if (this.ids.indexOf(item.id) > -1) {
							codes.push(item.code)
						}
					})
				}else if(this.currentTab == "two"){ //二楼 
					this.taskTwoList.forEach(item => {
						if (this.ids.indexOf(item.id) > -1) {
							codes.push(item.code)
						}
					})
				}else{  //地堆
					this.taskDiduiList.forEach(item => {
						if (this.ids.indexOf(item.id) > -1) {
							codes.push(item.code)
						}
					})
				}

				// console.log(ids,123)
				ids = ids.toString()
				this.$modal.confirm('是否确认提交'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
					return approveTask(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("确认成功");
				}).catch(() => {});
			},
			/** 审核按钮操作 */
			addResult(row) {
				let ids = row.id || this.ids;
				if (ids.length <= 0) {
					this.$modal.msgWarning('请选择需要生成报表的任务')
					return;
				}
				// console.log(ids,123)
				ids = ids.toString()
				this.$modal.confirm('是否确认'+ (row.id ? '该条' : '这些') +'数据生成报表？').then(function() {
					return addTaskResult(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("操作成功");
				}).catch(() => {});
			},
			/** 删除按钮操作 */
			handleDelete(row) {
				let ids = row.id || this.ids;
				let codes = []
				if(this.currentTab == "one"){  //一楼
					this.taskList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}else if(this.currentTab == "two"){ //二楼 
					this.taskTwoList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}else{  //地堆
					this.taskDiduiList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}
				
				this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
					return delTask(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("删除成功");
				}).catch(() => {});
			},
			/** 导出按钮操作 */
			handleExport() {
				this.download(wms + '/task/export', {
					...this.queryParams
				}, `task_${new Date().getTime()}.xlsx`)
			},
			// 单据导入
			ImportBill() {
				this.fileListd = []
				this.upload.isUploading = false
				this.upload.open = true
			},
			onRemove() {},
			onChanc() {
				// if(this.fileNumber != 'x'){
				// 	this.fileNumber++
				// }else{
				// 	this.fileNumber = 0
				// }
			},
			// 文件上传中处理
			handleFileUploadProgress(event, file, fileList) {
				this.upload.isUploading = true
			},
			// 文件上传成功处理
			handleFileSuccess(response, file, fileList) {
				this.upload.isUploading = false
				if (response.code == 200) {
					this.upload.open = false
					this.$modal.msgSuccess(response.msg)
					this.getList();
				} else {
					this.upload.isUploading = false
					this.$modal.msgError(response.msg)
				}
			},
			// 上传前 校验大小
			handleBeforeUpload(file) {
				let fileSize = 50
				const isLt = file.size / 1024 / 1024 < fileSize
				if (!isLt) {
					this.msgWarn(`上传文件大小不能超过 ${fileSize} MB!`)
					return false
				}
			},
			// 提交上传文件
			submitFileForm() {
				if (this.fileListd.length > 0) {
					this.$refs.upload.submit()
					this.upload.isUploading = true
				} else {
					this.$modal.msgError('请选择需要上传文件')
				}
			},
			/** 激活任务 */
			handleActivate(row) {
				let ids = row.id || this.ids;
				let codes = []
				if(this.currentTab == "one"){  //一楼
					this.taskList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}else if(this.currentTab == "two"){ //二楼 
					this.taskTwoList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}else{  //地堆
					this.taskDiduiList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}
				
				this.$modal.confirm('是否确认激活'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
					console.log("ids="+ids)
					return actTask(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("激活成功");
				}).catch(() => {});
			},
			/** 终止任务 */
			handleStop(row) {
				let ids = row.id || this.ids;
				let codes = []
				if(this.currentTab == "one"){  //一楼
					this.taskList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}else if(this.currentTab == "two"){ //二楼 
					this.taskTwoList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}else{  //地堆
					this.taskDiduiList.forEach(item => {
					if (this.ids.indexOf(item.id) > -1) {
						codes.push(item.code)
					}
				})
				}
				
				this.$modal.confirm('是否确认终止'+ (row.id ? '该条' : '这些') +'数据？').then(function() {
					return stopTask(ids);
				}).then(() => {
					this.getList();
					this.$modal.msgSuccess("终止成功");
				}).catch(() => {});
			},
			/** 登记盘点结果 */
			handleSeeResult(row){
				this.registerOpen=true
				this.SeeResultParams.pageNum = 1
				this.SeeResultParams.taskId = row.id
				this.formSeeResultList = []
				this.getSeeResultList()
			},
			getSeeResultList() {
				getCheckTaskDetail(this.SeeResultParams).then(response => {
					this.formSeeResultList = response.rows;
					this.totalSee = response.total;
				});
			},
			handleClick(tab){
				this.currentTab = tab.name;
				if(this.currentTab == "one"){  //一楼
					this.queryParams.trayType = 1;
				}else if(this.currentTab == "two"){ //二楼 
					this.queryParams.trayType = 2;
				}else{  //地堆
					this.queryParams.trayType = 3;
				}
				this.queryParams.pageNum = 1;
				
				this.getList();
			},
			updateCheckCount(){
				if (this.formSeeIds && this.formSeeIds.length > 0) {
					let info = JSON.parse(JSON.stringify(this.formSeeIds))
					let datas = []
					this.formSeeIds.forEach(item => {
						const data = {
							id:item["checkResultId"],
							predictCount:item["predictCount"],
							taskId:item["taskId"],
							taskDetailId:item["id"],
							actualCount:item["actualCount"],
							materialId:item["materialId"],
							batchCode:item["batchNumber"],
							remark:item["remark"]
						}
						datas.push(data)
          			})

					addCheckResult(datas).then(response => {
						// this.SeeResultParams.pageNum = 1
						// this.SeeResultParams.taskId = info[0]["taskId"]
						this.formSeeResultList = []
						this.getSeeResultList()
					});
				} else {
					this.$modal.msgError('请选择已修改实盘数量的物料')
				}
			},
		}
	};
</script>
