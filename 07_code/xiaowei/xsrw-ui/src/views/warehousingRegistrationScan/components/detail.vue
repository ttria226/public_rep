<template>
  <div class="app-container">
    <el-dialog title="查看登记详情" :visible.sync="open" width="60%" append-to-body>
          <el-table v-loading="loading" :data="detailForm">
			  <el-table-column label="序号" align="center" type="index"></el-table-column>
            <el-table-column label="物料编码" align="center" prop="wuliaoCode" ></el-table-column>
            <el-table-column label="物料名称" align="center" prop="name"></el-table-column>
            <el-table-column label="批次号" align="center" prop="batchCode"></el-table-column>
            <el-table-column label="RFID标签ID" align="center" prop="rfid" min-width="220"></el-table-column>
            <el-table-column label="换算数量" align="center" prop="rfidCount" ></el-table-column>
			<el-table-column label="扫描状态" align="center" prop="rfidFlag" >
				<template slot-scope="scope">
					<span>{{scope.row.rfidFlag==1?"已扫描":'未扫描'}}</span>
				</template>
			</el-table-column>
			<el-table-column label="扫描时间" align="center" prop="saomiaoShijian" min-width="220"></el-table-column>
          </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
 import {
 	postAction,
 	getAction
 } from "@/api/manage";
 import {
 	wms
 } from '@/utils/agent';

  export default {
    name: "detail",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 是否显示弹出层
        open: false,
        rukuCode: '',// 详情id
		detailForm:[]
      };
    },
    methods: {
      /** 查询详情 */
      getDetail() {
        this.loading = true;
        getAction(`${wms}/inout/detail/selectSMMaterialDetaiZilList`,{rukuCode:this.rukuCode}).then(response => {
          this.detailForm = response.rows
          this.loading = false;
        });
      },
      // 取消按钮
      detailCancel() {
        this.open = false;
        this.rukuCode = ''
      },
  }
};
</script>