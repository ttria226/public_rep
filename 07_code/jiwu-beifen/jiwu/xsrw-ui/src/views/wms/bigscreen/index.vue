<template>
  <div id="index" ref="appRef">
    <div class="bg">
      <dv-loading v-show="loading">Loading...</dv-loading>
      <div class="host-body">
        <div>
          <!-- 顶部title部分 -->
          <el-row>
            <el-col :span="6">
              <!-- <el-select class="ckselect" v-model="queryParams.warehouseId" @change="freshData">
                <el-option v-for="option in warehouseList" :key="option.id" :value="option.id" :label="option.name"/>
              </el-select> -->
              <dv-decoration-8 class="title_right" :color="['#008CFF', '#00ADDD']" />
            </el-col>
            <el-col :span="12">
              <div class="title_text">智慧化仓库管理系统</div>
              <dv-decoration-5 class="title_center" :color="['#008CFF', '#00ADDD']" />
            </el-col>
            <el-col :span="6">
              <div class="title_time">{{ dateYear + ' ' + dateDay + ' ( '+ dateWeek+' )'}}</div>
              <dv-decoration-8 :reverse="true" class="title_left" :color="['#008CFF', '#00ADDD']" />
            </el-col>
          </el-row>
          <!-- 主体部分 -->
          <el-row>
            <!-- 第一列 -->
            <el-col :span="6">
              <!-- 任务看板 -->
              <div class="left_box1">
                <dv-border-box-12>
                  <div style="margin: 0 15px;padding-top: 20px;font-size: 18px;font-weight: bold;display: flex;justify-content: space-between;align-items: center;">
                    <span>任务看板</span>
                    <div class="search-type-content">
                      <div class="type-item" :class="{ 'active': queryTaskBoardParams.timeRangeType == '日' }" @click="handleSearchTypeClick('taskBoard','日')">日</div>
                      <div class="type-item" :class="{ 'active': queryTaskBoardParams.timeRangeType == '月' }" @click="handleSearchTypeClick('taskBoard','月')">月</div>
                      <div class="type-item" :class="{ 'active': queryTaskBoardParams.timeRangeType == '年' }" @click="handleSearchTypeClick('taskBoard','年')">年</div>
                    </div>
                  </div>
                  <div class="show-list">
                    <div class="list-item one" v-for="item in taskBoardList" :key="item.title">
                      <div class="num">{{ item.count }}</div>
                      <div class="img-content">
                        <img style="width: 100%;height: 96px;" :src="ScreenIconOne" alt="">
                        <img style="width: 20px;height: 20px;position: absolute;top: 47px;left: 48px;" :src="ScreenIconTwo" alt="">
                      </div>
                      <div class="title">{{ item.title }}</div>
                    </div>
                  </div>
                </dv-border-box-12>
              </div>
              <!-- 设备运行 -->
              <div class="left_box2">
                <dv-border-box-12>
                  <div style="margin: 0 15px;padding-top: 20px;font-size: 18px;font-weight: bold;display: flex;justify-content: space-between;align-items: center;">
                    <span>设备运行</span>
                    <div class="search-type-content">
                      <div class="type-item" :class="{ 'active': queryEquipmentRunningParams.timeRangeType == '日' }" @click="handleSearchTypeClick('equipmentRunning','日')">日</div>
                      <div class="type-item" :class="{ 'active': queryEquipmentRunningParams.timeRangeType == '月' }" @click="handleSearchTypeClick('equipmentRunning','月')">月</div>
                      <div class="type-item" :class="{ 'active': queryEquipmentRunningParams.timeRangeType == '年' }" @click="handleSearchTypeClick('equipmentRunning','年')">年</div>
                    </div>
                  </div>
                  <div class="show-list">
                    <div class="list-item two" v-for="item in equipmentRunningList" :key="item.title">
                      <div class="num">{{ item.count }}</div>
                      <div class="img-content">
                        <img style="width: 100%;height: 70px;" :src="ScreenIconThree" alt="">
                      </div>
                      <div class="title">{{ item.title }}</div>
                    </div>
                  </div>
                </dv-border-box-12>
              </div>
              <!-- 轮播表格部分 -->
              <div class="left_box3">
                <dv-border-box-12>
                  <p style="margin-left: 15px;padding-top: 20px;font-size: 18px;font-weight: bold;">设备信息</p>
                  <dv-scroll-board :config="configEquipmentList" style="width:98%;height: 80%;padding-left: 2%;padding-top: 10px"/>
                </dv-border-box-12>
              </div>
            </el-col>
            <!-- 第二列 -->
            <el-col :span="10">
              <!-- 仓库使用情况 -->
              <div id="china-map">
                <div style="width:100%;height:530px;margin-bottom: 10px;">
                  <dv-border-box-12 style="padding-top:0px;height: 100%;">
                    <p style="margin-left: 15px;padding-top: 20px;font-size: 18px;font-weight: bold;">仓库使用情况</p>
                    <el-form class="queryWarehouseForm" :model="queryParams" ref="queryWarehouseForm" size="small" :inline="true" label-width="80px" style="margin-top: 20px;padding: 0 15px;">
                      <el-form-item label="区域筛选" prop="type">
                        <el-select class="ckselect" v-model="queryWarehouseParams.areaId" clearable placeholder="请选择区域" @change="handleAreaChange">
                          <el-option v-for="dict in areaList" :key="dict.id" :label="dict.name" :value="dict.id" />
                        </el-select>
                      </el-form-item>
                    </el-form>
                    <div class="list-body">
                      <el-row class="out-row" :gutter="20" v-for="(item,index) in stockList" :key="'stock-out-'+index">
                        <el-col :span="8" v-for="(info,i) in item" :key="'stock-in-'+i">
                          <el-card class="list-item-card" shadow="always">
                            <div slot="header" class="card-header">
                              <el-tooltip class="item" effect="dark" :content="info.areaName + '-' + info.reservoirName" placement="top">
                                <span class="header-text">{{ info.areaName + "-" + info.reservoirName }}</span>
                              </el-tooltip>
                              <!-- <el-button style="position: absolute;right: 20px;padding: 5px 10px;" :type="info.status == 0 ? 'danger' : 'success'" @click="handleDeleteStatus(info, info.status == 0 ? '1' : '0')">{{ info.status == 0 ? '禁用' : '启用' }}</el-button> -->
                            </div>
                            <div class="use-info">
                              <div>使用中库位: {{ info.useCount }}</div>
                              <div>使用率: {{ info.useRate }}</div>
                            </div>
                          </el-card>
                        </el-col>
                      </el-row>
                    </div>
                  </dv-border-box-12>
                </div>
                <!-- 任务执行情况的柱状图-->
                <div class="line_center">
                  <dv-border-box-12 style="padding-top:0px;height: 100%;">
                    <p style="margin-left: 15px;padding-top: 20px;font-size: 18px;font-weight: bold;margin-bottom: 10px;">任务执行情况</p>
                    <div id="task_execute_charts" style="width: 100%;height: calc(100% - 50px);color:white;"></div>
                  </dv-border-box-12>
                </div>
              </div>
            </el-col>
            <!-- 第三列 -->
            <el-col :span="8">
              <!-- 轮播表格部分 -->
              <div class="right_box1">
                <dv-border-box-12>
                  <p style="margin-left: 15px;padding-top: 20px;font-size: 18px;font-weight: bold;">任务列表</p>
                  <dv-scroll-board :config="configTaskList" style="width:98%;height: 80%;padding-left: 2%;padding-top: 10px"/>
                </dv-border-box-12>
              </div>
              <!-- 雷达图部分 -->
              <div class="right_box2">
                <dv-border-box-12 :reverse="true">
                  <p style="margin-left: 15px;padding-top: 20px;font-size: 18px;font-weight: bold;">任务状态</p>
                  <div id="task_status_charts" style="height: 80%; width: 96%;"></div>
                </dv-border-box-12>
              </div>
              <!-- 排行榜部分 -->
              <div class="right_box3">
                <dv-border-box-12 :reverse="true">
                  <p style="margin-left: 15px;padding-top: 20px;font-size: 18px;font-weight: bold;">物料库存TOP</p>
                  <div class="top-list">
                    <div class="top-item" v-for="(item, index) in materialStockList" :key="item.materialName">
                      <el-tooltip class="item" effect="dark" :content="item.materialName" placement="top">
                        <div class="item-title">
                          <div class="icon" :class="{ 'one': index === 0, 'two': index === 1, 'three': index === 2, 'normal': index > 2 }">{{ 'NO.' + (index+1) }}</div>
                          <div class="text">{{ item.materialName }}</div>
                        </div>
                      </el-tooltip>
                      <div class="item-num">{{ item.libraryCount }}</div>
                    </div>
                  </div>
                </dv-border-box-12>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import drawMixin from "@/api/bigscreen/drawMixin"; //自适应缩放
  import { formatTime, countTaskBoard, countEquipmentRunning, listEquipmentInfo, listTaskInfo, getTaskExecuteInfo, getTaskStatusInfo, getMaterialStockTopInfo, getWarehouseUseInfo } from "@/api/bigscreen/index.js"; //日期格式转换
  // import { listWarehouse } from "@/api/cims/warehouse";
  import { getVisualizationAreaSelect } from "@/api/stockLedger/visualization";
  import * as echarts from "echarts";

  import ScreenIconOne from "@/assets/screen/screen-icon-1.png"
  import ScreenIconTwo from "@/assets/screen/screen-icon-2.png"
  import ScreenIconThree from "@/assets/screen/screen-icon-3.png"

  export default {
    mixins: [drawMixin],
    data() {
      return {
        //图片1
        ScreenIconOne,
        //图片2
        ScreenIconTwo,
        //图片3
        ScreenIconThree,
        taskStatusList:[], //任务状态list
        taskTypeList:[], //任务类型list
        //定时器
        timing: null,
        //loading图
        loading: true,
        //时分秒
        dateDay: null,
        //年月日
        dateYear: null,
        //周几
        dateWeek: null,
        //仓库
        warehouseList: [],
        queryParams : {
          //当前仓库id
          warehouseId:null,
        },
        //任务总数
        taskCount:0,
        //周几
        weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
        //查询条件
        queryWarehouseParams: {
          areaId: ''
        },
        //库存信息
        stockList: [],
        //区域信息
        areaList: [],
        //设备信息配置
        configEquipmentList: {
          header: ['设备编号', '设备名称', '位置', '状态'],
          data: [],
          columnWidth:[120,160,100],
          evenRowBGC: "#020308",
          // oddRowBGC: "#382B47",
          headerBGC: "#074F7F",
          headerHeight: 40
        },
        //任务信息配置
        configTaskList: {
          header: ['载具编号', '时间', '库位', '任务类型', '状态'],
          data: [],
          columnWidth:[145,160,100,100],
          evenRowBGC: "#020308",
          // oddRowBGC: "#382B47",
          headerBGC: "#074F7F",
          headerHeight: 40
        },

        queryTaskBoardParams: {
          timeRangeType: "日"
        },//任务看板查询条件
        taskBoardList: [
          {
            title: "入库任务",
            count: 0,
          },
          {
            title: "出库任务",
            count: 0,
          },
          {
            title: "盘点任务",
            count: 0,
          },
          {
            title: "移库任务",
            count: 0,
          }
        ], //任务看板list

        queryEquipmentRunningParams: {
          timeRangeType: "日"
        },//设备运行查询条件
        equipmentRunningList: [
          {
            title: "正常运行",
            count: 0,
          },
          {
            title: "故障维修",
            count: 0,
          },
          {
            title: "巡检",
            count: 0,
          },
          {
            title: "保养中",
            count: 0,
          },
          {
            title: "设备总数",
            count: 0,
          }
        ], //设备运行list
        taskExecuteData: {},//任务执行情况数据
        taskStatusData: {},//任务状态数据

        materialStockList: [],//物料库存top
      };
    },

    async created() {
      this.getDicts("wcs_excute_status").then(response => {
        this.taskStatusList = response.data
      });
      this.getDicts("wcs_task_type").then(response => {
        this.taskTypeList = response.data
      });
      //获取区域下拉
      this.getAreaList()
      // //获取实时时间
      this.timeFn();
      // //加载loading图
      this.cancelLoading();
      //获取任务看板数量
      this.getTaskBoardCount()
      //获取设备运行数量
      this.getEquipmentRunningCount()
      //获取设备信息列表
      this.getEquipmentList()
      //获取任务信息列表
      this.getTaskList()
      //获取仓库使用情况信息
      this.getWarehouseUsingList()
      //获取物料库存top
      this.getWarehouseTopList()
      //获取任务执行情况数据
      await this.getTaskExecuteData()
      //获取任务状态数据
      await this.getTaskStatusData()
    },
    mounted() {
      //获取仓库
      // this.getWarehouseList();
    },
    beforeDestroy() {
      //离开时删除计时器
      clearInterval(this.timing);
    },

    methods: {
      //右上角当前日期时间显示：每一秒更新一次最新时间
      timeFn() {
        this.timing = setInterval(() => {
          //获取当前时分秒
          this.dateDay = formatTime(new Date(), "HH: mm: ss");
          //获取当前年月日
          this.dateYear = formatTime(new Date(), "yyyy-MM-dd");
          //获取当前周几
          this.dateWeek = this.weekday[new Date().getDay()];
        }, 1000);
      },
      //刷新数据
      freshData(value){
        this.queryParams.warehouseId = value;
        this.getTaskList();
        this.getEquipmentList();
      },
      //loading图
      cancelLoading() {
        setTimeout(() => {
          this.loading = false;
        }, 500);
      },
      //获取仓库列表
      getWarehouseList(){
        listWarehouse().then(response => {
          this.warehouseList = response.rows;
          if(response.rows.length > 0){
            for (var i = 0; i < response.rows.length; i++) {
              if(response.rows[i].type == 1){
                this.freshData(response.rows[i].id);
              }
            }
          }
        });
      },
      //查询条件点击事件
      handleSearchTypeClick(type,searchType){
        if(type == 'taskBoard'){
          this.queryTaskBoardParams.timeRangeType = searchType
          this.getTaskBoardCount()
        } else if(type == 'equipmentRunning'){
          this.queryEquipmentRunningParams.timeRangeType = searchType
          this.getEquipmentRunningCount()
        }
      },
      //获取任务看板的任务数量
      getTaskBoardCount(){
        countTaskBoard(this.queryTaskBoardParams).then(response => {
          let countInfo = response.data
          this.taskBoardList[0].count = countInfo.inTaskCount
          this.taskBoardList[1].count = countInfo.outTaskCount
          this.taskBoardList[2].count = countInfo.inventoryTaskCount
          this.taskBoardList[3].count = countInfo.moveTaskCount
        });
      },
      //获取设备运行的各种数量
      getEquipmentRunningCount(){
        countEquipmentRunning(this.queryEquipmentRunningParams).then(response => {
          let countInfo = response.data
          this.equipmentRunningList[0].count = countInfo.normalCount
          this.equipmentRunningList[1].count = countInfo.faultyCount
          this.equipmentRunningList[2].count = countInfo.inspectionCount
          this.equipmentRunningList[3].count = countInfo.maintenanceCount
          this.equipmentRunningList[4].count = countInfo.totalCount
        });
      },
      //获取设备信息列表
      getEquipmentList(){
        listEquipmentInfo({}).then(response => {
          let data = this.transListData(2,response.data);
          this.configEquipmentList.data = data;
          this.configEquipmentList = { ...this.configEquipmentList };
        });
      },
      //获取任务列表
      getTaskList(){
        listTaskInfo({}).then(response => {
          let data = this.transListData(1,response.data);
          this.configTaskList.data = data;
          this.configTaskList = { ...this.configTaskList };
        });
      },
      /** 获取区域list */
      getAreaList(){
        getVisualizationAreaSelect({}).then(res => {
          this.areaList = res.data
        })
      },
      /** 处理下拉筛选 */
      handleAreaChange(val){
        this.getWarehouseUsingList()
      },
      /** 获取仓库使用情况list */
      getWarehouseUsingList(){
        getWarehouseUseInfo({ areaId: this.queryWarehouseParams.areaId ? this.queryWarehouseParams.areaId : '0' }).then(res => {
          let list = res.data
          let insideList = []
          let resultList = []
          list.map((item,index) => {
            insideList.push(item)
            if((index + 1) % 3 === 0 || (index === list.length - 1 && insideList.length > 0)){
              let infoList = JSON.parse(JSON.stringify(insideList))
              resultList.push(infoList)
              insideList = []
            }
          })
          if(resultList.length > 3){
            this.stockList = resultList.filter((item,index) => { return index < 3 })
          } else {
            this.stockList = resultList
          }
        })
      },
      //任务执行情况数据
      async getTaskExecuteData(){
        let result = await getTaskExecuteInfo({})
        let legendInfoList = []
        let legendName = []
        let dataName = []
        legendInfoList = result.data.dataList
        dataName = result.data.xList
        result.data.dataList.map((item) => {
          legendName.push(item.name)
        })
        this.taskExecuteData = {
          legendInfoList,
          legendName,
          dataName
        }
        //任务执行情况柱状图
        this.getTaskExecuteCharts()
      },
      //任务执行情况柱状图
      getTaskExecuteCharts(){
        let mapChart = echarts.init(document.getElementById("task_execute_charts")); //图表初始化，china-map是绑定的元素
        window.onresize = mapChart.resize; //如果容器变大小，自适应从新构图
        let series = []
        this.taskExecuteData.legendInfoList.map((item) => {
          let info = {
            name: item.name,
            type: 'bar',
            itemStyle: {
              opacity: 0.8,
            },
            emphasis: {
              focus: 'series'
            },
            data: item.yData
          }
          series.push(info)
        })
        let option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            data: this.taskExecuteData.legendName,
            bottom: '10%',
            textStyle: { color: "#e3e3e3" },
            itemWidth: 10,
            itemHeight: 10,
          },
          grid: {
            top: 20,
            bottom: '30%'
          },
          xAxis: [
            {
              type: 'category',
              axisLabel: {
                color: "#e3e3e3"
              },
              axisTick: { show: false },
              data: this.taskExecuteData.dataName
            }
          ],
          yAxis: [
            {
              type: 'value',
              axisLabel: {
                color: "#e3e3e3"
              },
              splitLine: {
                lineStyle: { color: ["#e3e3e3"], type: 'dashed' }
              }
            }
          ],
          series
        }
        mapChart.setOption(option); //生成图表
      },
      //任务状态数据
      async getTaskStatusData(){
        let result = await getTaskStatusInfo({})
        let legendInfoList = []
        let legendName = []
        let dataName = []
        let maxNumAllList = []
        let maxNumAll = 0
        let indicator = new Array(6)
        dataName = result.data.xList
        result.data.dataList.map((info) => {
          info.yData.map((dataInfo) => {
            maxNumAllList.push(dataInfo)
          })
        })
        maxNumAll = this.getMaxNum(maxNumAllList)
        result.data.xList.map((item,index) => {
          let itemNumList = []
          let indicatorInfo = { name: item, max: 0, color: '#e3e3e3' }
          result.data.dataList.map((info) => {
            itemNumList.push(info.yData[index])
          })
          indicatorInfo.max = this.getMaxNum(itemNumList)
          if(index === 0){
            indicator[index] = indicatorInfo
          } else if(index === 1){
            indicator[5] = indicatorInfo
          } else {
            indicator[2] = indicatorInfo
          }
        })
        indicator[1] = { name: "", max: maxNumAll, color: '#e3e3e3' }
        indicator[3] = { name: "", max: maxNumAll, color: '#e3e3e3' }
        indicator[4] = { name: "", max: maxNumAll, color: '#e3e3e3' }
        result.data.dataList.map((item) => {
          legendName.push(item.name)
        })
        legendInfoList = result.data.dataList
        this.taskStatusData = {
          legendInfoList,
          legendName,
          dataName,
          indicator
        }
        //任务状态雷达图
        this.getTaskStatusCharts()
      },
      //任务状态雷达图
      getTaskStatusCharts(){
        console.log(this.taskStatusData)
        let mapChart = echarts.init(document.getElementById("task_status_charts")); //图表初始化，china-map是绑定的元素
        window.onresize = mapChart.resize; //如果容器变大小，自适应从新构图
        let colorList = ['#0ABAFF','#8400FF','#EC808D','#F59A23']
        let series = []
        this.taskStatusData.legendInfoList.map((item,index) => {
          let yDataList = new Array(this.taskStatusData.indicator.length)
          let legendYDataList = item.yData
          for(let i = 0; i < legendYDataList.length; i++){
            if(i === 0){
              yDataList[i] = legendYDataList[i]
            } else if(i === 1){
              yDataList[5] = legendYDataList[i]
            }else {
              yDataList[2] = legendYDataList[i]
            }
          }
          yDataList[1] = 0
          yDataList[3] = 0
          yDataList[4] = 0
          let seriesInfo = {
            value: yDataList,
            areaStyle: { color: colorList[index], opacity: 0.3 },
            name: item.name,
            itemStyle: { color: colorList[index] }
          }
          series.push(seriesInfo)
        })
        let _this = this
        let option = {
          tooltip: {
            confine: true,
            trigger: 'item',
            formatter: function (params) {
              console.log(params)
              let relVal = params.name;
              let typeList = _this.taskStatusData.indicator
              for (let i = 0; i < params.data.value.length; i++) {
                if(i == 0 || i == 2 || i == 5){
                  relVal += '<br/>' + typeList[i].name + ' : ' + params.data.value[i];
                }
              }
              return relVal;
            }
          },
          legend: {
            data: this.taskStatusData.legendName,
            bottom: '2%',
            textStyle: { color: "#FFFFFF" },
            icon: 'circle',
            itemWidth: 10,
          },
          radar: {
            // shape: 'circle',
            indicator: this.taskStatusData.indicator,
            silent: true,
            center: ['50%','45%'],
            radius: '60%'
          },
          series: [
            {
              type: 'radar',
              data: series
            }
          ]
        }
        mapChart.setOption(option); //生成图表
      },
      //物料库存top
      getWarehouseTopList(){
        getMaterialStockTopInfo({}).then(res => {
          this.materialStockList = res.data
        })
      },
      //获取最大值
      getMaxNum(list){
        let returnNum = 0
        let maxNum = Math.max(...list)
        let maxNumFirst = (maxNum+"").substring(0,1)
        let maxNumFirstAdd = (Number((maxNum+"").substring(0,1))+1)+''
        let maxNumLength = (maxNum+"").length - 1
        let maxNumReresult = maxNumFirst
        let maxNumAddReresult = maxNumFirstAdd
        for(let i = 0; i < maxNumLength; i++){
          maxNumReresult += "0"
          maxNumAddReresult += "0"
        }
        if(Number(maxNumReresult) < maxNum){
          returnNum = Number(maxNumAddReresult)
        } else {
          returnNum = Number(maxNumReresult)
        }
        return returnNum
      },
      transListData(type, list){
        const scolldata = []
        if(list==null||list.length==0){
          return
        }
        for (let i = 0; i < list.length; i++) {
          const obj = [];
          const oldObj = list[i];
          if(type == 1){
            const statusItem = this.taskStatusList.find(item => item.dictValue == oldObj.taskStatus)
            let statusstr =  statusItem && statusItem.dictLabel ? statusItem.dictLabel : '--'
            const typeItem = this.taskTypeList.find(item => item.dictValue == oldObj.taskType)
            let typestr =  typeItem && typeItem.dictLabel ? typeItem.dictLabel : '--'
            obj.push(oldObj.trayCode)
            obj.push(oldObj.createTime)
            obj.push(oldObj.locationName)
            obj.push(typestr)
            // obj.push(oldObj.materialName)
            obj.push(statusstr)
          }else if(type == 2){
            obj.push(oldObj.equNo)
            obj.push(oldObj.name)
            obj.push(oldObj.functionLocation)
            if(oldObj.useStatus == 1){
              obj.push("运行中")
            }else if(oldObj.useStatus == 2){
              obj.push("故障")
            }
          }
          scolldata.push(obj)
        }
        return scolldata;
      },
    },
  };
</script>

<style lang="scss" scoped>
  //全局样式部分！！！！
  .ckselect{
    background: transparent;
    color: white;
    border: 0px;
    width: 300px;
    height: 42px;
    // position: absolute;
    // left: 52px;
    font-size: 18px;
    font-weight: bold;
  }
  * {
    margin: 0;
    padding: 0;
    list-style-type: none;
    outline: none;
    box-sizing: border-box;
  }
  html {
    margin: 0;
    padding: 0;
  }
  body {
    font-family: Arial, Helvetica, sans-serif;
    line-height: 1.2em;
    background-color: #f1f1f1;
    margin: 0;
    padding: 0;
  }
  a {
    color: #343440;
    text-decoration: none;
  }
  .show-list{
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-top: 20px;
    .list-item{
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      position: relative;
      &.one{
        width: 156px;
        height: 145px;
      }
      &.two{
        width: 75px;
        height: 120px;
        justify-content: flex-end;
        .img-content{
          padding-top: 0px;
        }
        .num{
          font-size: 16px;
          top: 30px;
          color: #22F2FF;
        }
        .title{
          position: absolute;
          bottom: 50px;
          font-size: 16px;
          font-weight: 600;
        }
      }
      .img-content{
        width: 100%;
        padding-top: 25px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
      .num{
        position: absolute;
        top: 20px;
        font-size: 16px;
        font-weight: bold;
      }
      .title{
        font-size: 16px;
        color: #FFFFFF;
      }
    }
  }
  //--------------------------------------------

  //页面样式部分！！！！
  #index {
    color: #d3d6dd;
    width: 1920px;
    height: 1080px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    transform-origin: left top;
    overflow: hidden;
    .bg {
      //整体页面背景
      width: 100%;
      height: 100%;
      padding: 16px 16px 0 16px;
      background-image: url("../../../assets/screen/pageBg.png"); //背景图
      background-size: cover; //背景尺寸
      background-position: center center; //背景位置
    }
    //顶部右边装饰效果
    .title_left {
      width: 100%;
      height: 50px;
    }
    //顶部左边装饰效果
    .title_right {
      width: 100%;
      height: 50px;
      margin-top: 18px;
    }
    //顶部中间装饰效果
    .title_center {
      width: 100%;
      height: 50px;
    }
    //顶部中间文字数据可视化系统
    .title_text {
      text-align: center;
      font-size: 24px;
      font-weight: bold;
      margin-top: 14px;
      color: #008cff;
    }
    //时间日期
    .title_time {
      text-align: center;
    }
    //中国地图
    #china-map {
      height: 610px;
      width: 100%;
    }
    //中间折线图
    .line_center {
      width: 100%;
      height: 410px;
    }
    //左1模块
    .left_box1 {
      height: 260px;
      width: 100%;
      margin-bottom: 10px;
      position: relative;
    }
    //左2模块
    .left_box2 {
      height: 260px;
      width: 100%;
      margin-bottom: 10px;
    }
    //左3模块
    .left_box3 {
      height: 410px;
      width: 100%;
    }
    //右1模块
    .right_box1 {
      height: 260px;
      width: 100%;
      margin-bottom: 10px;
    }
    //右2模块
    .right_box2 {
      height: 260px;
      width: 100%;
      margin-bottom: 10px;
    }
    //右3模块
    .right_box3 {
      height: 410px;
      width: 100%;
    }
    ::v-deep .dv-scroll-board .rows{
      height: calc(100% - 60px) !important;
    }
  }

  ::v-deep .ckselect .el-input--suffix .el-input__inner {
    padding-right: 0px;
  }
  ::v-deep .ckselect .el-input__inner {
    background-color: #060914;
    color: #fff;
    border: 1px solid #31cae4;
  }

  .search-type-content{
    width: 150px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .type-item{
      width: 42px;
      height: 32px;
      border: 1px solid #FFFFFF;
      color: #FFFFFF;
      font-weight: bold;
      font-size: 14px;
      border-radius: 4px;
      line-height: 31px;
      text-align: center;
      cursor: pointer;
      &.active{
        background-color: #00DAFF;
      }
    }
  }
  .queryWarehouseForm{
    ::v-deep .el-form-item__label{
      color: #FFFFFF;
      font-size: 16px;
    }
  }
  .list-body{
    .out-row{
      margin-bottom: 15px;
      padding: 0 15px;
      ::v-deep .el-card__header{
        display: flex;
        justify-content: center;
        align-items: center;
        border-bottom: none;
        position: relative;
        background-color: #060914;
        color: #d3d6dd;
        // border-bottom: 1px solid #FFFFFF;
        border-bottom-color: transparent;
      }
      ::v-deep .el-card__body{
        padding-top: 5px;
        background-color: #060914;
        color: #d3d6dd;
      }
    }
    .card-header{
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      width: 100%;
      text-align: center;
      .header-text{
        font-weight: bold;
        font-size: 18px;
      }
    }
    .use-info{
      min-width: 140px;
      display: flex;
      flex-direction: column;
      align-items: center;
    }
  }
  .top-list{
    width: calc(100% - 30px);
    height: calc(100% - 20px);
    margin: 10px 15px;
    .top-item{
      width: 100%;
      height: 25px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: url("../../../assets/screen/screen-icon-4.png") no-repeat;
      background-size: 100% 100%;
      margin-bottom: 10px;
      &:last-child{
        margin-bottom: 0px;
      }
      .item-title{
        width: calc(100% - 100px);
        display: flex;
        align-items: center;
        margin-right: 20px;
        .icon{
          width: 66px;
          height: 25px;
          font-size: 16px;
          color: #FFFFFF;
          margin-left: 15px;
          margin-right: 20px;
          line-height: 25px;
          text-align: center;
          &.one{
            background: url("../../../assets/screen/screen-icon-5.png") no-repeat;
            background-size: 100% 100%;
          }
          &.two{
            background: url("../../../assets/screen/screen-icon-6.png") no-repeat;
            background-size: 100% 100%;
          }
          &.three{
            background: url("../../../assets/screen/screen-icon-7.png") no-repeat;
            background-size: 100% 100%;
          }
          &.normal{
            background: url("../../../assets/screen/screen-icon-8.png") no-repeat;
            background-size: 100% 100%;
          }
        }
        .text{
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          font-size: 18px;
          font-weight: 500;
          color: #FFFFFF;
        }
      }
      .item-num{
        width: 80px;
        font-size: 16px;
        color: #BCF0FE;
        text-align: right;
        padding-right: 10px;
      }
    }
  }

  .el-select-dropdown__item{
    padding: 0 20px !important;
  }
</style>
