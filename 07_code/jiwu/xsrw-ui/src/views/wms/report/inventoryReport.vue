<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="盘点单号" prop="code">
                <el-input v-model="queryParams.code" placeholder="请输入盘点单号" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="盘点时间" prop="dataTime">
                <el-date-picker v-model="dataTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                                format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
            <el-form-item label="物料名称" prop="materialName">
                <el-input v-model="queryParams.materialName" placeholder="请输入物料名称" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                           v-hasPermi="['check:task:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="inventoryList">
            <!-- <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column> -->
            <el-table-column label="盘点单号" align="center" prop="code" min-width="200px" :show-overflow-tooltip="true" />
            <el-table-column label="物料编码" align="center" prop="materialCode" min-width="180px" :show-overflow-tooltip="true" />
            <el-table-column label="物料名称" align="center" prop="materialName" min-width="180px" :show-overflow-tooltip="true" />
            <!-- <el-table-column label="仓库名称" align="center" prop="warehouseName" :show-overflow-tooltip="true" /> -->
            <el-table-column label="库存数量" align="center" prop="predictCount" width="100" :show-overflow-tooltip="true" />
            <el-table-column label="盘点数量" align="center" prop="actualCount" width="100" :show-overflow-tooltip="true" />
            <el-table-column label="盘差" align="center" prop="checkDifferenceCount" width="100" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span :style="{ color: scope.row.checkDifferenceCount !='0' ? 'red' : '' }">{{ scope.row.checkDifferenceCount}}</span>
                </template>
            </el-table-column>
            <el-table-column label="盘点人" align="center" prop="createBy" width="120" :show-overflow-tooltip="true" />
            <el-table-column label="盘点时间" align="center" prop="createTime" width="150" :show-overflow-tooltip="true" />
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </div>
</template>

<script>
import { listTask, } from "@/api/wms/pdtask";
import { wms } from '@/utils/agent';

export default {
    name: "MaterialHandleReport",
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
            // 盘点报表表格数据
            inventoryList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                code: null,
                materialName: null,
                // warehouseId: null,
                startTime: null,
                endTime: null,
            },
            dataTime: [] //选择时间
        };
    },
    watch: {
        '$route'(to, form) {
            if (to.name == 'inventoryReport') {
                this.getList();
            }
        }
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询出库计划列表 */
        getList() {
            this.loading = true;
            if (this.dataTime && this.dataTime.length > 0) {
                this.queryParams.startTime = this.dataTime[0] + ' 00:00:00'
                this.queryParams.endTime = this.dataTime[1] + ' 23:59:59'
            } else {
                this.queryParams.startTime = null
                this.queryParams.endTime = null
            }
            this.queryParams.taskType = 3
            this.queryParams.checkType = 1
            listTask(this.queryParams).then(response => {
                this.inventoryList = response.rows;
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
            this.resetForm("queryForm");
            this.queryParams = {
                pageNum: 1,
                pageSize: 10,
                code: null,
                materialName: null,
                startTime: null,
                endTime: null,
            }
            this.dataTime = []
            this.handleQuery();
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download(wms + '/task/export', {
                ...this.queryParams
            }, `inventoryReport_${new Date().getTime()}.xlsx`)
        },
    }
};
</script>
