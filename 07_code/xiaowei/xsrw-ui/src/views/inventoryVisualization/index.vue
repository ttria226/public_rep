<template>
  <div class="app-container" v-loading.fullscreen="loading">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="区域筛选" prop="type">
        <el-select v-model="queryParams.areaId" clearable placeholder="请选择区域" @change="handleAreaChange">
          <el-option v-for="dict in areaList" :key="dict.id" :label="dict.name" :value="dict.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <div class="list-body">
      <el-row class="out-row" :gutter="30" v-for="(item,index) in stockList" :key="'stock-out-'+index">
        <el-col :span="8" v-for="(info,i) in item" :key="'stock-in-'+i">
          <el-card class="list-item-card" shadow="always">
            <div slot="header" class="card-header">
              <span>{{ info.areaName + "-" + info.name }}<span :style="{ color: info.status == 1 ? '#D90011' : '' }">({{ info.status == 0 ? '启用' : '禁用' }})</span></span>
              <el-button style="position: absolute;right: 20px;padding: 5px 10px;" :type="info.status == 0 ? 'danger' : 'success'" @click="handleDeleteStatus(info, info.status == 0 ? '1' : '0')">{{ info.status == 0 ? '禁用' : '启用' }}</el-button>
            </div>
            <el-row class="in-row" type="flex" v-if="info.locationList && info.locationList.length > 0">
              <el-col :span="7" v-for="(stockItem,stockIndex) in info.locationList" :key="stockItem.locationRow+'-'+stockIndex" class="card-body" @click.native="handleDetail(info,stockItem)">
                <div>{{ stockItem.locationRow }}排</div>
                <div class="use-info">
                  <div>使用中: {{ stockItem.inUser }}</div>
                  <div>使用率: {{ stockItem.rate }}</div>
                </div>
              </el-col>
            </el-row>
            <el-row class="in-row" type="flex" v-else>
              <el-col :span="24">
                <div class="center-text">暂无数据</div>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { listVisualization, getVisualizationAreaSelect } from "@/api/stockLedger/visualization";
import { delReservoirStatus } from "@/api/wms/reservoir";
export default {
  name: 'inventoryVisualization',
  data(){
    return{
      //加载
      loading: false,
      //查询条件
      queryParams: {
        areaId: ''
      },
      //库存信息
      stockList: [],
      //区域信息
      areaList: []
    }
  },
  created(){
    this.getList()
    this.getAreaList()
  },
  methods: {
    /** 获取库存信息list */
    getList(){
      this.loading = true
      listVisualization({ areaId: this.queryParams.areaId ? this.queryParams.areaId : '0' }).then(res => {
        let list = res.data
        let insideList = []
        let resultList = []
        list.map((item,index) => {
          item.locationList.map((info) => {
            info.rate = info.inUser !== null && info.totalcount !== null ? ((Number(info.inUser) / Number(info.totalcount)) * 100).toFixed(2) + '%' : '0%'
            return info
          })
          insideList.push(item)
          if((index + 1) % 3 === 0 || (index === list.length - 1 && insideList.length > 0)){
            let infoList = JSON.parse(JSON.stringify(insideList))
            resultList.push(infoList)
            insideList = []
          }
        })
        this.stockList = resultList
      }).finally(() => {
        this.loading = false
      })
      // let list = [
      //   {
      //     id: 1,
      //     areaName: '1楼',
      //     reservoirName: 'A区',
      //     status: 0,
      //     stockLineList: [
      //       {
      //         locationName: '1排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '2排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '3排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       }
      //     ]
      //   },
      //   {
      //     id: 2,
      //     areaName: '1楼',
      //     reservoirName: 'B区',
      //     status: 0,
      //     stockLineList: [
      //       {
      //         locationName: '1排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '2排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '3排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       }
      //     ]
      //   },
      //   {
      //     id: 3,
      //     areaName: '1楼',
      //     reservoirName: 'C区',
      //     status: 0,
      //     stockLineList: [
      //       {
      //         locationName: '1排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '2排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '3排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       }
      //     ]
      //   },
      //   {
      //     id: 4,
      //     areaName: '2楼',
      //     reservoirName: 'A区',
      //     status: 0,
      //     stockLineList: [
      //       {
      //         locationName: '1排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '2排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '3排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       }
      //     ]
      //   },
      //   {
      //     id: 5,
      //     areaName: '2楼',
      //     reservoirName: 'B区',
      //     status: 0,
      //     stockLineList: [
      //       {
      //         locationName: '1排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '2排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '3排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       }
      //     ]
      //   },
      //   {
      //     id: 6,
      //     areaName: '2楼',
      //     reservoirName: 'C区',
      //     status: 0,
      //     stockLineList: [
      //       {
      //         locationName: '1排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '2排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       },
      //       {
      //         locationName: '3排',
      //         usingNum: 28,
      //         usingRate: '28%'
      //       }
      //     ]
      //   }
      // ]
    },
    /** 处理下拉筛选 */
    handleAreaChange(val){
      this.getList()
    },
    /** 禁用启用 */
    handleDeleteStatus(row,status) {
      const ids = row.id;
      let strname = '禁用'
      if(status == '1'){
        strname = '禁用'
      }else{
        strname = '启用'
      }
      const data = {
        id: ids,
        status: status,
      }
      this.$modal.confirm('是否'+strname+'此库区下的库位？').then(function() {
        return delReservoirStatus(data);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(strname+"成功");
      }).catch(() => {});
    },
    /** 获取区域list */
    getAreaList(){
      getVisualizationAreaSelect({}).then(res => {
        this.areaList = res.data
      })
    },
    //可视化详情
    handleDetail(info,stockItem){
      this.$router.push({ path: 'visualization/detail', query: { reservoirId: info.id, locationRow: stockItem.locationRow } })
    }
  }
}
</script>
<style lang="scss" scoped>
.list-body{
  .out-row{
    margin-bottom: 30px;
    ::v-deep .el-card__header{
      display: flex;
      justify-content: center;
      align-items: center;
      border-bottom: none;
      position: relative;
    }
  }
  .card-header{
    & > span{
      font-weight: bold;
      font-size: 18px;
    }
  }
  .in-row{
    min-height: 120px;
    overflow-x: auto;
    .el-col{
      margin-right: 30px;
      margin-bottom: 20px;
      cursor: pointer;
      &:nth-child(3n){
        margin-right: 0;
      }
      &.card-body{
        padding: 10px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        background-color: #e3e3e3;
        min-width: 140px;
        min-height: 100px;
        .use-info{
          display: flex;
          flex-direction: column;
          align-items: center;
        }
      }
      .center-text{
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
  }
}
</style>