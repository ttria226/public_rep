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
              <div class="title_text">物资动态展示</div>
              <dv-decoration-5 class="title_center" :color="['#008CFF', '#00ADDD']" />
            </el-col>
            <el-col :span="6">
              <div class="title_time">{{ dateYear + ' ' + dateDay + ' ( '+ dateWeek+' )'}}</div>
              <dv-decoration-8 :reverse="true" class="title_left" :color="['#008CFF', '#00ADDD']" />
            </el-col>
          </el-row>
          <!-- 主体部分 -->
          <el-row >
            <el-col :span="24" style="width: 100%;height:280px;margin-top: 20px;padding-top: 30px;">
              <dv-border-box-12 style="padding-top:10px;">
                <!--今日入库数量框 -->
                <div style="width: 16%;height: 230px;padding: 20px;box-sizing: border-box;display: inline-block;">
                  <dv-border-box-8 :color="['#067EFB', '#1BE3E3']" style="padding-top: 10px;">
                    <div style="font-size: 32px;font-weight: bold;padding: 20px;box-sizing: border-box;color:#FAD400;text-align: center;">今日入库数量</div>
                    <dv-decoration-10 style="width:100%;height:5px;" :color="['#00CED1', '#FAD400']"/>
                    <div style="width: 100%;text-align: center;font-weight: bold;font-size: 34px;box-sizing: border-box;position: relative;padding-top:20px;">
                      <span style="">{{dayDataCount.inNum}}</span>
<!--                      <div style="position: relative;margin: 0px;overflow: hidden;padding-top:20px;">-->
<!--                        0001-->
<!--                      </div>-->
<!--                      <div style="position: relative;margin: 0px;height:120px;overflow: hidden;">-->
<!--                        <img src="@/assets/images/border8.png" style="position: absolute;top: 0px;left: 0px;width: 106px;height: 92px;z-index: 1;"/>-->
<!--                        <span style="">5</span>-->
<!--                      </div>-->
                    </div>
                  </dv-border-box-8>
                </div>
                <!--今日出库数量框 -->
                <div style="width: 16%;height: 230px;padding: 20px;box-sizing: border-box;display: inline-block;padding-left: 0px;">
                  <dv-border-box-8 :color="['#067EFB', '#1BE3E3']" style="padding-top: 10px;">
                    <div style="font-size: 32px;font-weight: bold;padding: 20px;box-sizing: border-box;color:#FAD400;text-align: center;">今日出库数量</div>
                    <dv-decoration-10 style="width:100%;height:5px;" :color="['#00CED1', '#FAD400']"/>
                    <div style='width: 100%;height: 100%;text-align: center;padding-top:20px;font-weight: bold;font-size: 34px;'>
                      <span style="">{{dayDataCount.outNum}}</span>
                    </div>
                  </dv-border-box-8>
                </div>
                <!--其他数量框 -->
                <div style="width: 68%;height: 230px;padding: 20px;box-sizing: border-box;display: inline-block;padding-left: 0px;">
                  <dv-border-box-8 :color="['#067EFB', '#1BE3E3']" style="padding-bottom:20px;display:flex;">
                    <!-- 实时库存-->
                    <dv-border-box-1 style="padding: 25px;width: 23%;display: inline-block;text-align: center;flex:1;">
                      <div style="font-size: 32px;font-weight: bold;color:#FAD400;text-align: center;padding: 20px;margin-left:20px;margin-top:10px;">实时库存</div>
                      <div style='width: 100%;height: 100%;text-align: center;font-weight: bold;font-size: 34px;margin-left:14px;'>{{dayDataCount.realTimeStock}}</div>
                    </dv-border-box-1>
                    <!-- 实时库存-->
                    <dv-border-box-1 style="padding: 25px;width: 28%;display: inline-block;text-align: center;flex:1;">
                      <div style="font-size: 32px;font-weight: bold;color:#FAD400;text-align: center;padding: 20px;margin-left:20px;margin-top:10px;">总库位/已用库位</div>
                      <div style='width: 100%;height: 100%;text-align: center;font-weight: bold;font-size: 34px;margin-left:14px;'>{{dayDataCount.locationNumAll}}/{{dayDataCount.locationNumUse}} ~{{dayDataCount.locationRatio}}</div>
                    </dv-border-box-1>
                    <!-- 库存总额-->
                    <dv-border-box-1 style="padding: 25px;width: 24%;display: inline-block;text-align: center;flex:1;">
                      <div style="font-size: 32px;font-weight: bold;color:#FAD400;text-align: center;padding: 20px;margin-left:20px;margin-top:10px;">库存总额(元)</div>
                      <div style='width: 100%;height: 100%;text-align: center;font-weight: bold;font-size: 34px;margin-left:14px;'>{{dayDataCount.stockTotalAamount}}</div>
                    </dv-border-box-1>
                    <!-- 物资种类-->
                    <dv-border-box-1 style="padding: 25px;width: 23%;display: inline-block;text-align: center;flex:1;">
                      <div style="font-size: 32px;font-weight: bold;color:#FAD400;text-align: center;padding: 20px;margin-left:20px;margin-top:10px;">物资种类</div>
                      <div style='width: 100%;height: 100%;text-align: center;font-weight: bold;font-size: 34px;margin-left:14px;'>{{dayDataCount.materialCategoryNum}}</div>
                    </dv-border-box-1>
                  </dv-border-box-8>
                </div>
              </dv-border-box-12>

            </el-col>
            <!--圆饼图 -->
            <el-col :span="24" style="width: 100%;height:640px;margin-top: 20px;">
              <dv-border-box-12 style="display:flex;padding:10px;">
                <!--库存类别饼状图 -->
                <dv-border-box-1 style="width: 50%;display: inline-block;text-align: center;flex:1;">
                  <div id="stock_category_charts" style="width: 100%;height: 100%;color:white;padding:20px;"></div>
                </dv-border-box-1>
                <!--库存位置饼状图 -->
                <dv-border-box-1 style="width: 50%;display: inline-block;text-align: center;flex:1;">
                  <div id="stock_position_charts" style="width: 100%;height: 100%;color:white;padding:20px;"></div>
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
  import { formatTime, numStatisticDay, categoryRatioDay, locationTypeRatioDay,getNowTime } from "@/api/bigscreen/index.js"; //日期格式转换
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
              inNum:2,
              locationNumAll:3591,
              locationNumUse:92,
              locationRatio:"3%",
              materialCategoryNum:479,
              outNum:3,
              realTimeStock:351,
              stockTotalAamount:324093.14,
          },
      };
    },

    async created() {

      // //获取实时时间
      this.getTime();
      // //加载loading图
      this.cancelLoading();

    },
    mounted() {
        //库存类别统计
        this.getStockCategoryCharts();
        //库存位置统计
        this.getStockPositionCharts();
        //获取今日出入库、库存数量
        this.getDayData();
        let $this = this;
        this.timing = setInterval(() => {
            //库存类别统计
            $this.getStockCategoryCharts();
            //库存位置统计
            $this.getStockPositionCharts();
            //获取今日出入库、库存数量
            $this.getDayData();
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
        getDayData(){
            let $this = this
            numStatisticDay({}).then(response => {
                $this.dayDataCount = response.data;
               // console.log(response)
            });
        },
        //库存位置饼状图
        async getStockPositionCharts(){
            let result = await locationTypeRatioDay({})
            console.log(result);
            var chartDom = document.getElementById('stock_position_charts');
            var myChart = echarts.init(chartDom);
            var option;
            option = {
                title: {
                    text: '库存位置比例',
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
                    left: 'right',
                    textStyle:{
                        color: 'white',
                        fontSize: 18,
                    },
                },
                series: [
                    {
                        name: '库存位置比例',
                        type: 'pie',
                        radius: '85%',
                        top: 80,
                        label:{
                          normal: {
                            color: 'white',
                            fontSize:20,
                            formatter: '{b}:{d}% ( {c} )'
                          },
                        },
                        data: result.data,
                        // [
                        //     { value: 1048, name: 'Search Engine' },
                        //     { value: 735, name: 'Direct' },
                        //     { value: 580, name: 'Email' },
                        //     { value: 484, name: 'Union Ads' },
                        //     { value: 300, name: 'Video Ads' }
                        // ],
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
        //库存类别饼状图
        async getStockCategoryCharts(){
            let result = await categoryRatioDay({})
            console.log(result);
            var chartDom = document.getElementById('stock_category_charts');
            var myChart = echarts.init(chartDom);
            var option;
            option = {
                title: {
                    text: '库存类别比例',
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
                        name: '库存类别比例',
                        type: 'pie',
                        radius: '85%',
                        top: 80,
                        label:{
                            normal: {
                              color: 'white',
                              fontSize:20,
                              formatter: '{b}:{d}% ( {c} )'
                            },
                        },
                        data: result.data,
                        // [
                        //     { value: 1048, name: 'Search Engine' },
                        //     { value: 735, name: 'Direct' },
                        //     { value: 580, name: 'Email' },
                        //     { value: 484, name: 'Union Ads' },
                        //     { value: 300, name: 'Video Ads' }
                        // ],
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
