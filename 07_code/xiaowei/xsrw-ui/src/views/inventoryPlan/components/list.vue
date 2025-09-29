<template>
  <div class="plan-list">
    <el-tabs tab-position="left" v-model="planLittleType" @tab-click="handleClick">
      <el-tab-pane label="盘点策略-物料" name="1">
        <div class="container-right" v-if="planLittleType === '1'">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="盘点计划名称" prop="materialName" label-width="100px">
              <el-input v-model="queryParams.planName" placeholder="请输入盘点计划名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="制单日期" prop="dateTime">
              <el-date-picker v-model="queryParams.dateTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-s-promotion" size="mini" @click="handleInventoryTask" v-hasPermi="['check:task:addCheckTask']">生成盘点任务</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['check:checkDelivery:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['check:checkDelivery:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['check:checkDelivery:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>

          <el-table v-loading="loading" :data="inventoryPlanList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="盘点计划名称" align="center" prop="planName" :show-overflow-tooltip="true" min-width="200" />
            <el-table-column label="来源" align="center" prop="checkSource" :show-overflow-tooltip="true" min-width="80">
              <template slot-scope="scope">
                <span v-if="scope.row.checkSource == '1'">手工创建</span>
                <span v-if="scope.row.checkSource == '2'">ERP导入</span>
              </template>
            </el-table-column>
            <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="200" />
            <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="90" />
            <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
            <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" min-width="100" />
            <el-table-column label="库存数量" align="center" prop="libraryCount" :show-overflow-tooltip="true" min-width="100" />
            <el-table-column label="任务生成状态" align="center" prop="taskStatus" :show-overflow-tooltip="true" min-width="100" >
              <template slot-scope="scope">
                <span v-if="scope.row.taskStatus==null" style="color: red;">未生成</span>
                  <span v-if="scope.row.taskStatus=='0'" style="color: red;">未生成</span>
                  <span v-if="scope.row.taskStatus=='1'" style="color: green;">已生成</span>
              </template>
            </el-table-column>
            <el-table-column label="盘点状态" align="center" prop="status" :show-overflow-tooltip="true" min-width="100" >
              <template slot-scope="scope">
                <span v-if="scope.row.status==null" style="color: red;">未开始</span>
                  <span v-if="scope.row.status=='0'" style="color: red;">未开始</span>
                  <span v-if="scope.row.status=='1'" style="color: yellow;">进行中</span>
                  <span v-if="scope.row.status=='2'" style="color: green;">已完成</span>
              </template>
            </el-table-column>
            <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
            <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-tab-pane>
      <el-tab-pane label="盘点策略-库区" name="2">
        <div class="container-right" v-if="planLittleType === '2'">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="制单日期" prop="dateTime">
              <el-date-picker v-model="queryParams.dateTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-s-promotion" size="mini" @click="handleInventoryTask" v-hasPermi="['check:task:addCheckTask']">生成盘点任务</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['check:checkDelivery:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['check:checkDelivery:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['check:checkDelivery:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>

          <el-table v-loading="loading" :data="inventoryPlanList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="区域" align="center" prop="areaName" :show-overflow-tooltip="true" min-width="180" />
            <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="180" />
            <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
            <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-tab-pane>
      <el-tab-pane label="盘点策略-随机" name="4">
        <div class="container-right" v-if="planLittleType === '4'">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <!-- <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item> -->
            <el-form-item label="制单日期" prop="dateTime">
              <el-date-picker v-model="queryParams.dateTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-s-promotion" size="mini" @click="handleInventoryTask" v-hasPermi="['check:task:addCheckTask']">生成盘点任务</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['check:checkDelivery:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['check:checkDelivery:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['check:checkDelivery:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>

          <el-table v-loading="loading" :data="inventoryPlanList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <!-- <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="200" />
            <el-table-column label="物料名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
            <el-table-column label="计量单位" align="center" prop="unitName" :show-overflow-tooltip="true" min-width="120" /> -->
            <!-- <el-table-column label="库存数量" align="center" prop="libraryCount" :show-overflow-tooltip="true" min-width="120" /> -->
            <el-table-column label="盘点数量" align="center" prop="randomNum" :show-overflow-tooltip="true" min-width="120" />
            <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
            <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-tab-pane>
      <el-tab-pane label="盘点策略-动碰" name="3">
        <div class="container-right" v-if="planLittleType === '3'">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="制单日期" prop="dateTime">
              <el-date-picker v-model="queryParams.dateTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-s-promotion" size="mini" @click="handleInventoryTask" v-hasPermi="['check:task:addCheckTask']">生成盘点任务</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['check:checkDelivery:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['check:checkDelivery:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['check:checkDelivery:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>

          <el-table v-loading="loading" :data="inventoryPlanList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="时间范围" align="center" prop="timeArea" :show-overflow-tooltip="true" min-width="300">
              <template slot-scope="scope">
                <span>{{ scope.row.startTime + ' ~ ' + scope.row.endTime}}</span>
              </template>
            </el-table-column>
            <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
            <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-tab-pane>
      <el-tab-pane label="盘点策略-空货位" name="5">
        <div class="container-right" v-if="planLittleType === '5'">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="制单日期" prop="dateTime">
              <el-date-picker v-model="queryParams.dateTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-s-promotion" size="mini" @click="handleInventoryTask" v-hasPermi="['check:task:addCheckTask']">生成盘点任务</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['check:checkDelivery:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['check:checkDelivery:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['check:checkDelivery:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>

          <el-table v-loading="loading" :data="inventoryPlanList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="区域" align="center" prop="areaName" :show-overflow-tooltip="true" min-width="180" />
            <el-table-column label="库区" align="center" prop="reservoirName" :show-overflow-tooltip="true" min-width="180" />
            <el-table-column label="制单人" align="center" prop="createBy" :show-overflow-tooltip="true" min-width="100" />
            <el-table-column label="制单日期" align="center" prop="createTime" width="180" :show-overflow-tooltip="true" min-width="150">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
export default {
  name: "inventoryListCom",
  data(){
    return{
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
      // 盘点计划表格数据
      inventoryPlanList: [],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialName: null,
        dateTime: [],
      },

      planLittleType: '1', //盘点策略类型值
    }
  },
  methods: {
    /** 查询盘点计划列表 */
    getList() {
      this.$emit("getList",{checkType: this.planLittleType})
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
    /** 切换盘点维度的事件 */
    handleClick(val){
      this.planLittleType = val.name
      this.inventoryPlanList = []
      this.total = 0
      this.single = true
      this.multiple = true
      this.resetQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 生成盘点按钮操作 */
    handleInventoryTask() {
      if(this.ids && this.ids.length === 0 || !this.ids){
        this.$modal.msgError('请选择需要生成任务的计划')
        return;
      }
      const ids = this.ids;
      this.$emit("setButtonHandle",{ids,checkType: this.planLittleType,type:1})
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$emit("setButtonHandle",{queryParams:this.queryParams,checkType: this.planLittleType,type:2})
    },
    /** 删除按钮操作 */
    handleDelete() {
      if(this.ids && this.ids.length === 0 || !this.ids){
        this.$modal.msgError('请选择需要删除的计划')
        return;
      }
      const ids = this.ids;
      this.$emit("setButtonHandle",{ids,checkType: this.planLittleType,type:3})
    },
    /** 导出按钮操作 */
    handleExport() {
      let params = JSON.parse(JSON.stringify(this.queryParams))
      if (params.dateTime && params.dateTime.length > 0) {
        params.startTime = params.dateTime[0]
        params.endTime = params.dateTime[1]
      }
      delete params.dateTime
      this.$emit("setButtonHandle",{queryParams:params,checkType: this.planLittleType,type:4})
    },
  }
}
</script>
<style lang="scss" scoped>
.container-right{
  margin-left: 20px;
}
</style>
