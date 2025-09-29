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
              <div class="title_text">物资库龄分析与库存预警</div>
              <dv-decoration-5 class="title_center" :color="['#008CFF', '#00ADDD']" />
            </el-col>
            <el-col :span="6">
              <div class="title_time">{{ dateYear + ' ' + dateDay + ' ( '+ dateWeek+' )'}}</div>
              <dv-decoration-8 :reverse="true" class="title_left" :color="['#008CFF', '#00ADDD']" />
            </el-col>
          </el-row>
          <!-- 主体部分 -->
          <el-row >
            <el-col :span="24" style="width: 100%;height:400px;padding-top: 2px;">
              <!--当月出入库数量框 -->
              <div style="width: 50%;height: 100%;padding-left: 0px;float: left;">
                <dv-border-box-1 :color="['#067EFB', '#1BE3E3']" style="padding-top: 10px;">
                  <div id="wzage_charts" style="width: 100%;height: 100%;color:white;margin:20px;padding-bottom: 30px;"></div>
                </dv-border-box-1>
              </div>
              <div style="width: 50%;height: 100%;float: left;padding-left: 0px;">
                <dv-border-box-1 :color="['#067EFB', '#1BE3E3']" style="padding-top: 10px;">
                  <div style="margin-left: 15px;padding-top: 10px;font-size: 32px;font-weight: bold;color: #FAD400;text-align: center;">物资库龄情况</div>
                  <div style="width:98%;height: 96%;padding-left: 2%;padding-top: 10px;">
                    <dv-scroll-board :config="configTaskList" style=""/>
                  </div>
                </dv-border-box-1>
              </div>
            </el-col>
            <!--圆饼图 -->
            <el-col :span="24" style="width: 100%;height:540px;margin-top: 20px;">
              <dv-border-box-12 style="display:flex;padding:10px;">
                <!--库存类别饼状图 -->
                <dv-border-box-1 style="width: 50%;display: inline-block;text-align: center;flex:1;">
                  <p style="margin-left: 15px;padding-top: 10px;font-size: 32px;font-weight: bold;color: #FAD400;text-align: center;">最低库存预警</p>
                  <dv-scroll-board :config="configTaskList2" style="width:98%;height: 88%;padding-left: 2%;padding-top: 10px"/>
<!--                  <div id="stock_category_charts" style="width: 112%;height: 103%;color:white;padding-top: 20px;margin-left:-36px;"></div>-->
                </dv-border-box-1>
                <!--库存位置饼状图 -->
                <dv-border-box-1 style="width: 50%;display: inline-block;text-align: center;flex:1;">
                  <p style="margin-left: 15px;padding-top: 10px;font-size: 32px;font-weight: bold;color: #FAD400;text-align: center;">最高库存预警</p>
                  <dv-scroll-board :config="configTaskList3" style="width:98%;height: 88%;padding-left: 2%;padding-top: 10px"/>
<!--                  <div id="stock_position_charts" style="width: 112%;height: 103%;color:white;padding-top: 20px;margin-left:-36px;"></div>-->
                </dv-border-box-1>
              </dv-border-box-12>
            </el-col>

          </el-row>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
    import drawMixin from "@/api/bigscreen/drawMixin"; //自适应缩放
    import { formatTime,storageAgeInfoWarn, storageAgeWarn,maximumStock,minimumStock,getNowTime } from "@/api/bigscreen/index.js"; //日期格式转换
    import * as echarts from "echarts";

    export default {
        mixins: [drawMixin],
        data() {
            return {
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
                //周几
                weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
                stockCategoryData:{},
                dayDataCount:{
                    inMonthMoney:460663.97,
                    outMonthMoney : 136715.31,
                },
                //重点物资月出入信息配置
                configTaskList: {
                    header: ['时间类型', '物资种类数量', '库存总额'],
                    data: [],
                    columnWidth:[70,320,200,200],
                    evenRowBGC: "#020308",
                    index: true,
                    align: ['center'],
                    // oddRowBGC: "#382B47",
                    headerBGC: "#074F7F",
                    headerHeight: 40
                },
                //重点物资月出入信息配置
                configTaskList2: {
                    header: ['物资名称', '物资种类', '当前库存', '最低库存'],
                    data: [],
                    columnWidth:[70,320,220,150,150],
                    evenRowBGC: "#020308",
                    index: true,
                    align: ['center'],
                    // oddRowBGC: "#382B47",
                    headerBGC: "#074F7F",
                    headerHeight: 40
                },
                //重点物资月出入信息配置
                configTaskList3: {
                    header: ['物资名称', '物资种类', '当前库存', '最高库存'],
                    data: [],
                    columnWidth:[70,320,220,150,150],
                    evenRowBGC: "#020308",
                    index: true,
                    align: ['center'],
                    // oddRowBGC: "#382B47",
                    headerBGC: "#074F7F",
                    headerHeight: 40
                },
            };
        },

        async created() {
            // 获取系统时间
            this.getTime();
            // //加载loading图
            this.cancelLoading();

        },
        mounted() {
            // 最低库存预警
            this.getKeyPointMaterialMonth2();
            // 最高库存预警
            this.getKeyPointMaterialMonth3();
            //物资库龄情况-列表数据
            this.getKeyPointMaterialMonth();
            //获取今日出入库、库存数量
            // this.getDayData();
            //物资库龄分析
            this.getWzAgeCharts();
            let $this = this;
            this.timing = setInterval(() => {
                // 最低库存预警
                $this.getKeyPointMaterialMonth2();
                // 最高库存预警
                $this.getKeyPointMaterialMonth3();
                //物资库龄情况-列表数据
                this.getKeyPointMaterialMonth();
                //物资库龄分析
                $this.getWzAgeCharts();
            }, 60000);
        },
        beforeDestroy() {
            //离开时删除计时器
            clearInterval(this.timing);
        },
        methods: {
          //获取系统时间
          getTime(){
            getNowTime().then(response => {
              if(response.data){
                //获取实时时间
                this.timeFn(new Date(response.data));
              }
            });
          },
            //最低库存预警
            getKeyPointMaterialMonth2(){
                let $this = this
                minimumStock({}).then(response => {
                    if(response.data){
                        console.log(response.data)
                        let data = this.transListData(2,response.data);
                        $this.configTaskList2.data = data;
                        $this.configTaskList2 = { ...$this.configTaskList2 };
                    }
                });
            },
            //最高库存预警
            getKeyPointMaterialMonth3(){
                let $this = this
                maximumStock({}).then(response => {
                    if(response.data){
                        console.log(response.data)
                        let data = this.transListData(3,response.data);
                        $this.configTaskList3.data = data;
                        $this.configTaskList3 = { ...$this.configTaskList3 };
                    }
                });
            },
            //物资库龄分析-饼状图
            async getWzAgeCharts(){
                let result = await storageAgeWarn({})
                console.log(result);
                var chartDom = document.getElementById('wzage_charts');
                var myChart = echarts.init(chartDom);
                var option;
                option = {
                    title: {
                        text: '物资库龄分析',
                        subtext: '',
                        left: 'center',
                        textStyle:{
                            color: '#FAD400',
                            fontSize: 32,
                        },
                    },
                    tooltip: {
                        trigger: 'item'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        textStyle:{
                            color: 'white',
                            fontSize: 18,
                        },
                    },
                    series: [
                        {
                            name: '物资库龄分析',
                            type: 'pie',
                            radius: '85%',
                            top: 80,
                            bottom:20,
                            label:{
                              normal: {
                                color: 'white',
                                fontSize:20,
                                formatter: '{b}:{d}% ( {c} )'
                              },
                            },
                            data: result.data,
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };
                option && myChart.setOption(option);
            },
            getDayData(){
                let $this = this
                inOutMoneyMonth({}).then(response => {
                    if(response.data){
                        $this.dayDataCount = response.data;
                    }
                    // console.log(response)
                });
            },
            getKeyPointMaterialMonth(){
                let $this = this
                storageAgeInfoWarn({}).then(response => {
                    if(response.data){
                        console.log(response.data)
                        let data = this.transListData(1,response.data);
                        $this.configTaskList.data = data;
                        $this.configTaskList = { ...$this.configTaskList };
                    }
                });
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
                        obj.push(oldObj.date)
                        obj.push(oldObj.count)
                        obj.push(oldObj.amount+'元')
                    }else if(type == 2){
                        obj.push(oldObj.materialName)
                        obj.push(oldObj.categoryName)
                        obj.push(oldObj.count)
                        obj.push(oldObj.stockMin)
                    }else if(type == 3){
                        obj.push(oldObj.materialName)
                        obj.push(oldObj.categoryName)
                        obj.push(oldObj.count)
                        obj.push(oldObj.stockMax)
                    }
                    scolldata.push(obj)
                }
                return scolldata;
            },
            //右上角当前日期时间显示：每一秒更新一次最新时间
            timeFn(date) {
                this.timing = setInterval(() => {
                  let time = new Date(date.setSeconds(date.getSeconds() + 1));
                  //获取当前时分秒
                  this.dateDay = formatTime(time, "HH: mm: ss");
                  //获取当前年月日
                  this.dateYear = formatTime(time, "yyyy-MM-dd");
                  //获取当前周几
                    this.dateWeek = this.weekday[time.getDay()];
                }, 1000);
            },
            //loading图
            cancelLoading() {
                setTimeout(() => {
                    this.loading = false;
                }, 500);
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
