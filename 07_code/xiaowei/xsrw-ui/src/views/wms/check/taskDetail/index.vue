<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="所属盘点计划">
				<el-input v-model="queryParams.planName" placeholder="请输入盘点计划" />
			</el-form-item>
      <el-form-item label="任务号" prop="code">
        <el-input v-model="queryParams.code" placeholder="请输入任务号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" clearable filterable placeholder="请选择状态">
          <el-option v-for="dict in dict.type.task_status" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="物料编码" prop="checkType">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" />
      </el-form-item>
      <!-- <el-form-item label="载具">
        <el-input v-model="queryParams.trayCode" placeholder="请输入载具" maxlength="20"/>
      </el-form-item> -->
      <!-- <el-form-item label="物料">
        <el-select v-model="queryParams.materialId" clearable filterable placeholder="请选择物料号">
            <el-option v-for="item in materialList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-row :gutter="10" class="mb8">
				<el-col :span="1.5">
					<el-button type="primary" plain icon="el-icon-document-checked" size="mini" @click="handleStatus"
						v-hasPermi="['wms:checkTask:examine']">审核</el-button>
				</el-col>
			</el-row>
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['wms:taskDetail:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['wms:taskDetail:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['wms:taskDetail:remove']">删除</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" :disabled="!InvenId" @click="Inventory" v-hasPermi="['wms:taskDetail:add']">执行盘点</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
         <el-button type="warning" plain icon="el-icon-download" size="mini" @click="ImportBill">单据导入</el-button>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['wms:taskDetail:export']">导出</el-button>
      </el-col> -->
      <!-- <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> -->
    </el-row>
    <el-table v-loading="loading" :data="taskDetailList" @selection-change="formSeeOnChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column width="60px"  fixed>
        <template v-slot="scope">
          <el-radio v-model="InvenId" :label="scope.row.id" @change="handleRowChange(scope.row)">{{""}}</el-radio>
        </template>
      </el-table-column> -->
      <el-table-column label="物料编码" fixed min-width="100" align="center" prop="materialCode" :show-overflow-tooltip="true"/>
      <el-table-column label="物料名称" fixed min-width="100" align="center" prop="materialName" :show-overflow-tooltip="true"/>
      <el-table-column label="单位" fixed min-width="100" align="center" prop="unitName" :show-overflow-tooltip="true"/>
      <el-table-column label="所属盘点计划" fixed min-width="100" align="center" prop="planName" :show-overflow-tooltip="true"/>
      <el-table-column label="rfid" fixed min-width="100" align="center" prop="rfidHead" :show-overflow-tooltip="true"/>
      <!--<el-table-column label="载具编码" fixed min-width="100" align="center" prop="trayCode"  :show-overflow-tooltip="true"/>
      <el-table-column label="所属库区" fixed min-width="100" align="center" prop="areaName" :show-overflow-tooltip="true"/>
      <el-table-column label="所在库位" fixed min-width="100" align="center" prop="locationName" :show-overflow-tooltip="true"/>
			<el-table-column label="来源" fixed min-width="100" align="center" prop="checkSource" :show-overflow-tooltip="true"> 
				<template slot-scope="scope">
					<span v-if="scope.row.checkSource == '1'">手工创建</span>
			        <span v-if="scope.row.checkSource == '2'">ERP导入</span>
				</template>
			</el-table-column>-->
			<!-- <el-table-column label="任务激活状态" fixed min-width="100" align="center" prop="activateStatus" :show-overflow-tooltip="true">
				<template slot-scope="scope">
					<span v-if="scope.row.activateStatus == '0'">未激活</span>
			        <span v-if="scope.row.activateStatus == '1'">已激活</span>
				</template>
			</el-table-column> -->
			<!-- <el-table-column label="任务执行状态" fixed min-width="100" align="center" prop="taskStatus" :show-overflow-tooltip="true">
				<template slot-scope="scope">
          <span v-if="scope.row.taskStatus == '0'">未执行</span>
					<span v-if="scope.row.taskStatus == '1'">执行中</span>
			    <span v-if="scope.row.taskStatus == '2'">执行完成</span>
				</template>
			</el-table-column> -->
      <el-table-column label="状态" fixed align="center" prop="status" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.task_status" :value="scope.row.status" />
          </template>
        </el-table-column>
      <!--      <el-table-column label="仓库名称" align="center" prop="currentWarehouse" :show-overflow-tooltip="true" />
        <el-table-column label="盘点类型" min-width="120" align="center" prop="trayType" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.trayType == '1'">托盘</span>
          <span v-if="scope.row.trayType == '2'">料箱</span>
          <span v-if="scope.row.trayType == '3'">地堆</span>
        </template>
      </el-table-column> 
          <el-table-column label="状态" align="center" min-width="100" prop="taskStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.task_status" :value="scope.row.taskStatus" />
        </template>
      </el-table-column>
      <el-table-column label="盘点策略" min-width="120" align="center" prop="checkType" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.checkType == '1'">物料</span>
          <span v-if="scope.row.checkType == '2'">库区</span>
          <span v-if="scope.row.checkType == '4'">随机</span>
          <span v-if="scope.row.checkType == '3'">动碰</span>
          <span v-if="scope.row.checkType == '5'">空货位</span>
        </template>
      </el-table-column>
      <el-table-column label="执行人" min-width="120" align="center" prop="executeName" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="优先级"  align="center" prop="priority" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="批号" width="220" align="center" prop="batchNumber" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="盘点数量" align="center" prop="actualCount" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="盘差" align="center" prop="checkDifferenceCount" :show-overflow-tooltip="true" :formatter="checkFormat"/> -->
      <!-- <el-table-column label="库位号" align="center" prop="locationCode" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="区域" align="center" prop="areaName" :show-overflow-tooltip="true" /> -->
      <!-- <el-table-column label="复盘" align="center" prop="checkCount"  :show-overflow-tooltip="true"/> -->
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleSee(scope.row)" v-hasPermi="['wms:checkTaskDetail:query']">查看</el-button>
        </template>
      </el-table-column> -->
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="seeOpen" v-if="seeOpen" width="1200px" append-to-body>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" size="mini" :disabled="!InvenId" @click="Inventory" v-if="invenIdRow.taskStatus != '2' && invenIdRow.taskStatus != '3' && invenIdRow.taskStatus != '4'">执行盘点</el-button>
        </el-col>
      </el-row>

      <!-- 物料盘点 => 明细 v-if="formSeeList && formSeeList[0] && formSeeList[0].deliveryType == 4"-->
      <el-table :data="formSeeList">
        <el-table-column label="任务号" align="center" prop="taskCode" min-width="180" :show-overflow-tooltip="true" />
        <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.task_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="载具" align="center" prop="trayCode" min-width="150" :show-overflow-tooltip="true"/>
        <el-table-column label="物料编码" align="center" prop="materialCode" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column label="批次号" align="center" prop="batchNumber" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" align="center" prop="materialName" min-width="180" :show-overflow-tooltip="true" />
        <el-table-column label="区域" align="center" prop="areaName" min-width="150" :show-overflow-tooltip="true" />
        <el-table-column label="库区" align="center" prop="reservoirName" width="180" :show-overflow-tooltip="true" />
        <el-table-column label="库位" align="center" prop="locationName" width="180" :show-overflow-tooltip="true" />
        <el-table-column label="库存数量" align="center" prop="predictCount" :show-overflow-tooltip="true" />
        <el-table-column label="盘点数量" align="center" prop="actualCount" :show-overflow-tooltip="true" />
        <el-table-column label="盘差" align="center" prop="checkDifferenceCount" :show-overflow-tooltip="true" :formatter="checkFormat" />
        <el-table-column label="复盘" align="center" prop="checkCount" :show-overflow-tooltip="true" />
        <el-table-column label="WCS执行状态" align="center" prop="wcsTaskStatus" min-width="150" :show-overflow-tooltip="true"
                         v-if="invenIdRow.trayType != '3'">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wcs_excute_status" :value="scope.row.wcsTaskStatus" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" min-width="120" v-if="invenIdRow.trayType != '3'">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-sell" v-if="scope.row.wcsTaskStatus == '1'" @click="handleMoveStatus(scope.row,1)">载具出库</el-button>
            <el-button size="mini" type="text" icon="el-icon-sell" v-if="scope.row.wcsTaskStatus == '4'" @click="enforcementDeliveryOut(scope.row)">重新执行</el-button>
          <el-button size="mini" type="text" icon="el-icon-sold-out" v-if="scope.row.status == '2'" @click="handleMoveStatus(scope.row,2)">载具回库</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="totalSee > 0" :total="totalSee" :page.sync="SeeParams.pageNum" :limit.sync="SeeParams.pageSize" @pagination="getSeeList" />

      <!-- 库区盘点 => 立库 明细 -->
      <el-table :data="formSeeList" v-if="formSeeList && formSeeList[0] && formSeeList[0].deliveryType == 5">
        <el-table-column label="任务号" align="center" prop="taskCode" min-width="300" :show-overflow-tooltip="true" />
        <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.task_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="库区" align="center" prop="reservoirName" width="150" :show-overflow-tooltip="true" />
        <el-table-column label="库位" align="center" prop="locationName" width="150" :show-overflow-tooltip="true" />
        <el-table-column label="载具" align="center" prop="trayCode" width="150" :show-overflow-tooltip="true" />
        <el-table-column label="物料批号" align="center" prop="batchNumber" width="260" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" align="center" prop="materialName" width="220" :show-overflow-tooltip="true" />
        <el-table-column label="盘点数量" align="center" prop="actualCount" :show-overflow-tooltip="true" />
        <el-table-column label="盘差" align="center" prop="checkDifferenceCount" :show-overflow-tooltip="true" :formatter="checkFormat" />
        <el-table-column label="复盘" align="center" prop="checkCount" :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status == '0' || scope.row.status == '5'" @click="performTask(scope.row)">执行任务</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 库区盘点 => 平库 明细 -->
      <el-table :data="formSeeList" v-if="formSeeList && formSeeList[0] && formSeeList[0].deliveryType == 6">
        <el-table-column label="任务号" align="center" prop="taskCode" min-width="300" :show-overflow-tooltip="true" />
        <el-table-column label="状态" align="center" prop="status" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.task_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="区域" align="center" prop="areaName" width="180" :show-overflow-tooltip="true" />
        <el-table-column label="盘点数量" align="center" prop="actualCount" :show-overflow-tooltip="true" />
        <el-table-column label="盘差" align="center" prop="checkDifferenceCount" :show-overflow-tooltip="true" :formatter="checkFormat" />
        <el-table-column label="复盘" align="center" prop="checkCount" :show-overflow-tooltip="true" />
      </el-table>
    </el-dialog>

    <!-- 添加或修改任务详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="来源标识(收货详情id、盘点详情id、回库详情id、移库详情id)" prop="sourceId">
          <el-input v-model="form.sourceId" placeholder="请输入来源标识(收货详情id、盘点详情id、回库详情id、移库详情id)" />
        </el-form-item>
        <el-form-item label="实际重量(整数)" prop="actualWeight">
          <el-input v-model="form.actualWeight" placeholder="请输入实际重量(整数)" />
        </el-form-item>
        <el-form-item label="任务标识" prop="advanceDeliveryId">
          <el-input v-model="form.advanceDeliveryId" placeholder="请输入任务标识" />
        </el-form-item>
        <el-form-item label="ASN单据编号" prop="originCode">
          <el-input v-model="form.originCode" placeholder="请输入ASN单据编号" />
        </el-form-item>
        <el-form-item label="物料标识" prop="materialId">
          <el-date-picker clearable v-model="form.materialId" type="date" value-format="yyyy-MM-dd" placeholder="请选择物料标识"></el-date-picker>
        </el-form-item>
        <el-form-item label="规格型号" prop="unitId">
          <el-input v-model="form.unitId" placeholder="请输入规格型号" />
        </el-form-item>
        <el-form-item label="计划交货日期" prop="planDate">
          <el-date-picker clearable v-model="form.planDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择计划交货日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="预计交货日期" prop="predictDate">
          <el-date-picker clearable v-model="form.predictDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择预计交货日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="本次预计数量" prop="predictCount">
          <el-input v-model="form.predictCount" placeholder="请输入本次预计数量" />
        </el-form-item>
        <el-form-item label="生产日期" prop="producedDate">
          <el-date-picker clearable v-model="form.producedDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择生产日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="SN" prop="sn">
          <el-input v-model="form.sn" placeholder="请输入SN" />
        </el-form-item>
        <el-form-item label="收货数量" prop="receiveCount">
          <el-input v-model="form.receiveCount" placeholder="请输入收货数量" />
        </el-form-item>
        <el-form-item label="载具" prop="carrier">
          <el-input v-model="form.carrier" placeholder="请输入载具" />
        </el-form-item>
        <el-form-item label="库位标识" prop="locationId">
          <el-input v-model="form.locationId" placeholder="请输入库位标识" />
        </el-form-item>
        <el-form-item label="值班人员" prop="dutyPersonnel">
          <el-input v-model="form.dutyPersonnel" placeholder="请输入值班人员" />
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

    <!-- 上传弹框 -->
    <el-dialog title="导入单据" :visible.sync="upload.open" width="400px" append-to-body @close="cancel">
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :file-list="fileListd"
        :disabled="upload.isUploading"
        :on-remove="onRemove"
        :on-change="onChanc"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
        :before-upload="handleBeforeUpload"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" style="color: red" slot="tip">
          提示：仅允许导入“xls”或“xlsx”格式文件！最大不超过50M
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm" :loading="upload.isUploading">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 执行盘点弹窗 -->
    <el-dialog title="执行盘点" :visible.sync="inventoryOpen" v-if="inventoryOpen" width="1100px" append-to-body>
      <el-form ref="inventoryFrom" :model="inventoryFrom" :rules="inventoryRule" :inline="true" label-width="68px">
        <el-form-item label="载具" prop="trayCode">
          <el-select v-model="inventoryFrom.trayCode" clearable filterable @change="trayCodeInput" placeholder="请选择载具">
            <el-option v-for="item in inventoryTrayList" :key="item.code" :label="item.code" :value="item.code"></el-option>
          </el-select>
          <!-- <el-input ref="trayInput" v-model="inventoryFrom.trayCode" @input="trayCodeInput" placeholder="请扫描载具" show-word-limit clearable maxLength="" /> -->
        </el-form-item>
        <el-form-item label="物料批次号" prop="batchNumber" label-width="100px">
          <el-select v-model="inventoryFrom.batchNumber" clearable filterable @change="batchNumberInput" placeholder="请选择物料批次号">
            <el-option v-for="item in inventoryMaterialList" :key="item.batchNumber" :label="item.batchNumber" :value="item.batchNumber"></el-option>
          </el-select>
          <!-- <el-input ref="materialInput" v-model="inventoryFrom.batchNumber" style="width: 300px" @input="batchNumberInput" placeholder="请扫描物料" show-word-limit clearable maxLength="" /> -->
        </el-form-item>
      </el-form>
      <el-table :data="inventoryList" ref="inventoryTab">
        <el-table-column label="任务号" align="center" width="150" prop="taskCode" :show-overflow-tooltip="true" />
        <el-table-column label="物料编码" align="center" width="220" prop="materialCode" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" align="center" width="220" prop="materialName" :show-overflow-tooltip="true" />
        <el-table-column label="批次号" align="center" width="250" prop="batchNumber" :show-overflow-tooltip="true" />
        <el-table-column label="状态" align="center" prop="taskStatus" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.task_status" :value="scope.row.taskStatus" />
          </template>
        </el-table-column>
        <el-table-column label="库存数量" align="center" prop="predictCount" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="RFID" align="center" width="200" prop="rfidHead" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="RFID对应库存" align="center" prop="rfidHeadCount" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="实盘数量" align="center" width="200" prop="actualCount" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-input-number :precision="3" :step="0.001" :max="999999"  v-if="scope.row.taskStatus != '2'" :ref="'actualCount' + scope.$index" v-model="scope.row.actualCount" @blur="value => handleInventoryActualCountBlur(value,scope.row,scope.$index)" maxlength="9" placeholder="请输入实盘数量" size="large"></el-input-number>
            <span v-else>{{ scope.row.actualCount }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitInventory" :loading="InvenUploading">确 定</el-button>
        <el-button @click="inventoryOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 平库执行盘点弹窗 -->
    <el-dialog title="执行盘点" :visible.sync="inventoryAreaOpen" width="800px" append-to-body>
      <!-- <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
        </el-col>
      </el-row> -->
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="checkAreaAdd" v-hasPermi="['']">新增</el-button>
      <el-table :data="inventoryAreaList" ref="inventoryTab" style="margin-top: 10px">
        <el-table-column label="载具" align="center" width="150" prop="trayCode" :show-overflow-tooltip="true" />
        <el-table-column label="物料名称" align="center" width="220" prop="materialName" :show-overflow-tooltip="true" />
        <el-table-column label="物料批次号" align="center" width="260" prop="batchNumber" :show-overflow-tooltip="true" />
        <el-table-column label="实盘数量" align="center" width="150" prop="actualCount" :show-overflow-tooltip="true">
          <!-- <template slot-scope="scope">
            <el-input :ref="'actualCount' + scope.$index" v-model="scope.row.actualCount" oninput="value=value.replace(/[^\d]/g,'')" maxlength="9" placeholder="请输入实盘数量" size="small"></el-input>
          </template> -->
        </el-table-column>
        <!-- <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status == 2" @click="handleAreaDel(scope.row)" v-hasPermi="['']">删除</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" v-if="scope.row.status == 5" @click="handleModify(scope.row)" v-hasPermi="['']">修改</el-button>
          </template>
        </el-table-column> -->
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitInventoryArea">确 定</el-button>
        <el-button @click="inventoryAreaOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 平库盘点添加明细 -->
    <el-dialog :title="title" :visible.sync="areaAddOpen" width="500px" append-to-body>
      <el-form ref="areaAddForm" :model="areaAddForm" :rules="areaAddRules" label-width="80px">
        <el-form-item label="载具" prop="trayCode">
          <el-input v-if="!areaAddForm.id" v-model="areaAddForm.trayCode" placeholder="请扫描载具"/>
          <div v-else>{{ areaAddForm.trayCode }}</div>
        </el-form-item>
        <el-form-item label="物料" prop="batchNumber">
          <el-input v-if="!areaAddForm.id" v-model="areaAddForm.batchNumber" placeholder="请扫描物料标签"/>
          <div v-else>{{ areaAddForm.batchNumber }}</div>
        </el-form-item>
        <el-form-item label="实盘数量" prop="actualCount">
          <el-input type="number" v-model="areaAddForm.actualCount" max="999999999" placeholder="请输入实盘数量" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAreaForm">确 定</el-button>
        <el-button @click="areaAddOpen = false">取 消</el-button>
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
  </div>
</template>

<script>
import { listTaskDetail, performCheck, getTaskDetail, delTaskDetail, addTaskDetail, updateTaskDetail, trayDetail, checkdeliverySubmit, execute, detailList, checkAreaList, checkAreaSave, checkAreaDel, checkAreaSubmit, checkAreaDeliveryUpdate, getInventorySelectLists,getTaskdetailListData,approve  } from "@/api/wms/pdtaskDetail";

import { getListByTaskId } from "@/api/wms/pdtask";
import { takeOutTrayCheck, recycleTray } from "@/api/wms/Tray";
import { enforcementDeliveryOut } from "@/api/inoutDelivery/outDelivery";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { mapGetters } from 'vuex';
import { listMaterial } from "@/api/wms/material";
import { wms } from '@/utils/agent';
export default {
  name: "CheckTaskDetail",
  dicts: ['task_status','wcs_excute_status'],
  components: {},
  computed: {
    ...mapGetters(['deptId', 'token']),
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
      // 遮罩层
      loading: true,
      totalSee: 0,
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
      // 任务详情表格数据
      taskDetailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      statusOpen:false,
      rulesStatus: {
					status: [{
						required: true,
						validator: regulationTypeRules,
						trigger: "change"
					}],
				},
      formStatus: {
					status: null,
				},
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskType: 3,
        factory: null,
        trayCode: null,
        materialId: null,
        currentWarehouseId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      // 物料号列表
      materialList: [],
      // 仓库号列表
      warehouseList: [],
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
      InvenType: false,
      InvenUploading: false,
      Inven: {
        checkNum: null,
      },
      ruleInven: {
        checkNum: [{ required: true, message: "盘点数量不能为空", trigger: "blur" }]
      },
      statusList: [],
      InvenId: 0,
      invenIdRow: null,
      department: [],

      seeOpen: false,
      formSeeList: [],
      SeeParams: {
        pageNum: 1,
        pageSize: 10,
        taskId: null,
        taskType: null,
      },
      // 盘点弹窗
      inventoryOpen: false,
      inventoryFrom: {
        trayCode: null,
        batchNumber: null,
      },
      inventoryRule: {},
      inventoryList: [],

      // 平库盘点
      inventoryAreaOpen: false,
      inventoryAreaRule: {},
      inventoryAreaList: [],

      // 平库盘点新增明细
      areaAddOpen: false,
      areaAddForm: {
        id: '',
        taskId: null,
        trayCode: null,
        batchNumber: null,
        actualCount: null,
      },
      areaAddRules: {
        trayCode: [{ required: true, message: "请扫描载具", trigger: "blur" }],
        batchNumber: [{ required: true, message: "请扫描物料标签", trigger: "blur" }],
        actualCount: [{ required: true, message: "请输入实盘数量", trigger: "blur" }]
      },

      inventoryTrayList: [], //执行盘点的载具下拉列表
      inventoryMaterialList: [], //执行盘点的物料下拉列表
      rfidList: [], //执行盘点的RFID下拉列表
    };
  },
  created() {
    this.getDicts("task_status").then(response => {
      this.statusList = response.data
    });
    this.getList();
    this.listMaterial();
  },
  watch: {
    $route(to, form) {
      if (to.name == 'CheckTaskDetail') {
        this.getList()
      }
    }
  },
  methods: {
    // 修改
    handleModify(row) {
      // this.resetForm("areaAddForm");
      this.areaAddForm = row
      this.areaAddOpen = true
    },
    examTp(value) {
      this.Inven.checkNum = value
    },
    // 物料下拉列表
    listMaterial() {
      listMaterial({ pageNum: 1, pageSize: 1000, }).then(res => {
        this.materialList = res.rows;
      })
    },
    checkFormat(row, column) {
      let courseType = '';
      if (row.checkDifferenceCount > 0) {
        courseType = '+' + row.checkDifferenceCount
      } else {
        courseType = row.checkDifferenceCount
      }
      return courseType;
    },
    statusSeeFormat(row, column) {
      let courseType = '';
      for (var i = 0; i < this.statusList.length; i++) {
        if (this.statusList[i].dictValue == row.status) {
          courseType = this.statusList[i].dictLabel
        }
      }
      return courseType;
    },
    /** 查询任务详情列表 */
    getList() {
      this.loading = true;
      getTaskdetailListData(this.queryParams).then(response => {
        this.taskDetailList = response.rows;
        // this.taskDetailList = [
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
        //     location:'2-3',
        //     reservoir:'1库',
        //     area :'东区',
        //     id:'3'
        //   }
        // ];
        this.total = response.total;
        // this.total = 1;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancels() {
      // console.log(this.$refs.addForm,'执行')
      // this.$refs.addForm.clearValidate();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sourceId: null,
        type: null,
        actualWeight: null,
        advanceDeliveryId: null,
        originCode: null,
        materialId: null,
        unitId: null,
        originType: null,
        planDate: null,
        predictDate: null,
        predictCount: null,
        producedDate: null,
        sn: null,
        receiveCount: null,
        status: "0",
        carrier: null,
        locationId: null,
        dutyPersonnel: null,
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
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        taskType: 3,
        factory: null,
        trayCode: null,
        materialId: null,
        currentWarehouseId: null,
      }
      this.handleQuery();
    },
    handleRowChange(row) {
      this.InvenId = row.id
      this.invenIdRow = row
    },
    handleSee(row) {
      this.SeeParams = {
        pageNum: 1,
        pageSize: 10,
        taskId: row.id,
        taskType: row.taskType,
      }
      this.InvenId = row.id
      this.invenIdRow = row
      this.getSeeList()
    },
    getSeeList() {
      getListByTaskId(this.SeeParams).then(response => {
        this.seeOpen = true
        this.title = "详情"
        this.formSeeList = response.rows;
        this.totalSee = response.total;
      });
    },
    // 执行盘点
    Inventory() {
      if (this.formSeeList[0].deliveryType == '6') {
        const data = {
          taskId: this.invenIdRow.id,
          // trayCode: this.inventoryFrom.trayCode,
        }
        checkAreaList(data).then(res => {
          this.inventoryAreaList = res.data
          this.inventoryAreaOpen = true
        })
      } else {
        this.getInventorySelectList()
        this.inventoryOpen = true
        this.inventoryFrom = {
          trayCode: null,
          batchNumber: null,
        }
        this.inventoryList = []
        // this.$nextTick(() => {
        //   this.$refs.trayInput.focus()
        // })
      }
      // if(this.InvenId >0){
      //   // this.Inven.checkNum = null
      //   this.resetForm("Inven");
      //   this.InvenType = true
      // }else{
      //   this.$modal.msgError('请选择需要盘点的任务')
      // }
    },
    // 获取执行盘点
    getInventorySelectList(){
      const data = {
        taskId: this.invenIdRow.id,
      }
      getInventorySelectLists(data).then(res => {
        this.inventoryTrayList = res.data.trayList
        this.inventoryMaterialList = res.data.batchList
        this.rfidList = res.data.rfidHead
      })
    },
    // 执行任务
    performTask(row) {
      const data = {
        detailId: row.id,
      }
      this.$modal.confirm('是否确认执行此任务？').then(function () {
        return execute(data);
      }).then(() => {
        this.$modal.msgSuccess("操作成功");
        this.getSeeList()
      }).catch(() => { });
    },
    //执行盘点的载具下拉变化事件
    trayCodeInput() {
      this.getInventoryStockList()
    },
    // 获取执行盘点的库存信息列表
    getInventoryStockList(){
      if(!this.inventoryFrom.trayCode && !this.inventoryFrom.batchNumber){
        this.$modal.msgError('载具或者物料不能都为空！')
        return
      }
      const data = {
        taskId: this.invenIdRow.id,
        trayCode: this.inventoryFrom.trayCode,
        checkType: this.invenIdRow.checkType,
        batch: this.inventoryFrom.batchNumber,
      }
      trayDetail(data).then(res => {
        res.data.forEach(item => {
          item.showInput = false
        })
        this.inventoryList = res.data
      })
    },
    // 库区盘点获取列表
    getAreaList() {
      const data = {
        taskId: this.invenIdRow.id,
        trayCode: this.inventoryFrom.trayCode,
      }
      checkAreaList(data).then(res => {
        this.inventoryAreaList = res.data
        this.inventoryAreaOpen = true
      })
    },
    //执行盘点的物料下拉变化事件
    batchNumberInput() {
      this.getInventoryStockList()
    },
    //执行盘点的实际盘点数量失焦事件
    handleInventoryActualCountBlur(val,row,index){
      let info = JSON.parse(JSON.stringify(row))
      info.actualCount = val.target.value
      info.showInput = val.target.value !== null && val.target.value !== '' ? true : false
      this.$set(this.inventoryList,index,info)
    },
    // 平库盘点新增
    checkAreaAdd() {
      this.resetForm("areaAddForm");
      this.areaAddForm = {
        id: '',
        taskId: null,
        trayCode: null,
        batchNumber: null,
        actualCount: null,
      }
      this.areaAddOpen = true
    },
    // 平库盘点新增明细
    submitAreaForm() {
      this.areaAddForm.taskId = this.invenIdRow.id
      this.$refs["areaAddForm"].validate(valid => {
        if (valid) {
          if (this.areaAddForm.id) {
            checkAreaDeliveryUpdate(this.areaAddForm).then(res => {
              this.$modal.msgSuccess("修改成功");
              this.areaAddOpen = false
              this.getAreaList()
            })
          } else {
            checkAreaSave(this.areaAddForm).then(res => {
              this.$modal.msgSuccess("添加成功");
              this.areaAddOpen = false
              this.getAreaList()
            })
          }

        }
      });
    },
    // 平库盘点删除明细
    handleAreaDel(row) {
      const data = {
        id: row.id,
      }
      this.$modal.confirm('是否确认删除此明细？').then(function () {
        return checkAreaDel(data);
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
        this.getAreaList()
      }).catch(() => { });
    },
    // 盘点提交
    submitInventory() {
      if (this.inventoryList.length > 0) {
        let inventoryList = []
        this.inventoryList.map((item) => {
          if(item.taskStatus != 2){
            inventoryList.push(item)
          }
        })
        if(inventoryList.length == 0){
          this.$modal.msgError("没有可提交的数据");
          return
        }
        let data = {
          checkType: this.invenIdRow.checkType,
          deliveryType: this.formSeeList[0].deliveryType,
          historyList: inventoryList,
          // trayCode:Tray,
          // materialCode:material,
          // id:pages,
        }
        let inputZeroList = []
        let inputNullList = []
        inventoryList.forEach(item => {
          if (!item.showInput) {
            inputNullList.push(item.materialName)
            item.actualCount = 0
          } else if (!item.actualCount) {
            inputZeroList.push(item.materialName)
          }
        })
        if (inputZeroList.length > 0) {
          this.$modal.msgError("请输入" + inputZeroList.toString() + '的实盘数量');
          return
        }
        if (inputNullList.length > 0) {
          this.$modal.confirm(' 该载具有物料未盘点，是否做盘亏处理？').then(function () {
            return checkdeliverySubmit(data);
          }).then(() => {
            this.$modal.msgSuccess("操作成功");
            this.inventoryOpen = false
            this.seeOpen = false
            this.getList()
          }).catch(() => { });
        } else {
          checkdeliverySubmit(data).then(res => {
            this.$modal.msgSuccess("操作成功");
            this.inventoryOpen = false
            this.seeOpen = false
            this.getList()
          })
        }
      } else {
        this.$modal.msgError("没有可提交的数据");
      }
    },
    // 平库盘点提交
    submitInventoryArea() {
      const data = {
        taskId: this.invenIdRow.id,
      }
      checkAreaSubmit(data).then(res => {
        this.$modal.msgSuccess("操作成功");
        this.inventoryAreaOpen = false
        this.inventoryOpen = false
        this.seeOpen = false
        this.getList()
      })
    },
    /** 托盘出库/回库 */
    handleMoveStatus(row,type){
      this.$modal.confirm('是否确认将该载具'+(type == 1 ? '出库':'回库')+'？').then(function() {
        return type == 1 ? takeOutTrayCheck({ id: row.trayId,taskDetailId:row.id }) : recycleTray({ id: row.trayId });
      }).then(() => {
        this.getSeeList()
        this.$modal.msgSuccess((type == 1 ? '出库':'回库') + "成功");
      }).catch(() => {});
    },
    /** 载具出库重新执行 **/
    enforcementDeliveryOut(row){
      this.$modal.confirm('确定要重新发送该任务给WCS吗？').then(function() {
        let param = {
          id: row.wcsId
        }
        return enforcementDeliveryOut(param)
      }).then(() => {
        this.$modal.msgSuccess("重新发送任务成功");
        this.getSeeList()
      }).catch(() => {});
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
      this.title = "添加任务详情";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTaskDetail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改任务详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTaskDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTaskDetail(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除'+ (row.id ? '该条' : '这些') +'数据？').then(function () {
        return delTaskDetail(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(wms + '/taskDetail/export', {
        ...this.queryParams
      }, `taskDetail_${new Date().getTime()}.xlsx`)
    },
    // 单据导入
    ImportBill() {
      this.fileListd = []
      this.upload.isUploading = false
      this.upload.open = true
    },
    onRemove() {
    },
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
    formSeeOnChange(selection) {
				this.formSeeIds = selection.map(item => item.id)
			},
    // 审核
			handleStatus() {
				if (this.formSeeIds && this.formSeeIds.length > 0) {
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
  }
};
</script>
