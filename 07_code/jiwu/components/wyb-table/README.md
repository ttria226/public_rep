#### 一、使用说明

#### 	如果想完美使用该组件，请一定一字一句看完整个说明文档！！！

#### 	如果想完美使用该组件，请一定一字一句看完整个说明文档！！！

#### 	如果想完美使用该组件，请一定一字一句看完整个说明文档！！！

#### 	创作不易，喜欢的朋友给个五星好评，这对我很重要鸭╰(￣▽￣)╭

####  已上传GitHub，大家可以一起开发，完善111

1. 解压下载的压缩包，将组件放在项目的components目录下

2. 引用组件

    ```html
    <wyb-table ref="table" :headers="headers" :contents="contents" height="600rpx" :url-col="urlCol" />
    ```

    ```javascript
    import wybTable from '@/components/wyb-table/wyb-table.vue'
    export default {
        components: {
            wybTable
        },
        data() {
            const theUrl = '/pages/demos/noticeBar-demo/more' // 本地的某个页面
    		const httpUrl = 'https://ext.dcloud.net.cn/plugin?id=2667' // 某个网址
            return {
                headers: [{
                    label: '姓名',
                    key: 'name'
                }, {
                    label: '年龄',
                    key: 'age'
                }, {
                    label: '学院',
                    key: 'collage'
                }, {
                    label: '成绩',
                    key: 'score'
                }, {
                    label: '路由',
                    key: 'url'
                }, {
                    label: '网址',
                    key: 'link'
                }],
                contents: [{
                    name: '刘一',
                    age: '',
                    collage: '信息学院',
                    score: '99',
                    url: ['查看更多', theUrl],
                    link: ''
                }, {
                    name: '陈二',
                    age: '18',
                    collage: '商学院',
                    score: '98',
                    url: ['我带参数', theUrl, {name: '陈二'}],
                    link: ''
                }, {
                    name: '张三',
                    age: '22',
                    collage: '统计与数学学院',
                    score: '97',
                    url: ['查看更多', theUrl],
                    link: ''
                }, {
                    name: '李四',
                    age: '19',
                    collage: '城市与环境学院',
                    score: '96',
                    url: '',
                    link: ['访问网址', httpUrl]
                }, {
                    name: '王五',
                    age: '20',
                    collage: '旅游与酒店管理学院',
                    score: '95',
                    url: ['查看更多', theUrl],
                    link: ''
                }, {
                    name: '赵六 ',
                    age: '20',
                    collage: '',
                    score: '88',
                    url: ['查看更多', theUrl],
                    link: ''
                }, {
                    name: '孙七',
                    age: '21',
                    collage: '',
                    score: '95',
                    url: '',
                    link: ['访问网址', httpUrl]
                }, {
                    name: '周八 ',
                    age: '21',
                    collage: '会计学院',
                    score: '85',
                    url: ['查看更多', theUrl],
                    link: ''
                }, {
                    name: '吴九 ',
                    age: '20',
                    collage: '',
                    score: '91',
                    url: ['查看更多', theUrl],
                    link: ''
                }, {
                    name: '郑十',
                    age: '19',
                    collage: '',
                    score: '95',
                    url: '',
                    link: ['访问网址', httpUrl]
                }],
                urlCol: [{
                    type: 'route',
                    key: 'url'
                }, {
                    type: 'http',
                    key: 'link'
                }]
            }
        }
    }
    ```

3. Demo体验地址：[http://m.wybfiles.cn:82](http://m.wybfiles.cn:82)（请用手机访问，或用桌面浏览器启动手机调试模式）
4. 如果觉得我的插件还不错，可以下载我的插件的示例集合Demo => [传送门](http://tomcat.wybfiles.cn/download/wyb-demo.zip)，里面有我全部的插件以及全部插件的示例工程

#### 二、参数说明

 1. 表头 (header)

    | params | 类型           | 说明                                        |
    | ------ | -------------- | ------------------------------------------- |
    | label  | string         | 表头显示的文字                              |
    | key    | string         | 表头的key键值，用于列内容的赋值             |
    | width  | string, number | 该列的列宽，其值可覆盖参数default-col-width |

2. 表格内容 (contents)

    　　表格内容的格式见上方**“引用组件”**部分，有一点需要注意：如果单元格的内容为链接，请使用数组形式，例如：`url: ['查看更多', theUrl, {name: '陈二'}]`，数组的第一个元素为要显示的文字，第二个元素为实际要使用的链接。如果链接是项目中的某个页面，那么第三个元素可以传入参数（对象形式）

3. 参数列表（外观型参数）

    | 参数名                     | 类型               | 默认值                                           | 说明                                                         | 可选值      |
    | -------------------------- | ------------------ | ------------------------------------------------ | ------------------------------------------------------------ | ----------- |
    | width                      | string             | \`${uni.getSystemInfoSync()<br>.screenWidth}px\` | 表格宽度，默认为屏幕宽度，格式同css                          |             |
    | height                     | string             | 'auto'                                           | 表格高度，格式同css（如果想让表格可以纵向滚动并且表头固定，这个参数必须传一个具体值） |             |
    | font-size                  | array              | [30]                                             | 表格字体大小<br>第一个元素是表头字体大小，第二个元素时表格内容字体大小<br>如果像默认值这种只传一个元素，那么表格中所有字体大小都是该元素 |             |
    | header-weight              | boolean            | true                                             | 表头是否加粗                                                 | true, false |
    | min-height                 | array              | [70]                                             | 表格最小行高，超出自动增高<br/>第一个元素是表头最小行高，第二个元素时表格内容最小行高<br/>如果像默认值这种只传一个元素，那么表格中所有行的最小行高都是该元素 |             |
    | ~~col-width~~              | ~~array~~          | ~~[165]~~                                        | 1.0.6版本该参数已弃用<br>~~表格列宽<br/>每个元素代表每一列的列宽<br/>如果像默认值这种只传一个元素，那么表格中列的列宽都是该元素~~ |             |
    | default-col-width          | string, number     | 165                                              | 表格中所有列的列宽，其值可被headers数组的元素中的width覆盖   |             |
    | header-bg-color            | hexColor, rgbColor | '#f1f1f1'                                        | 表头背景色                                                   |             |
    | content-bg-color           | hexColor, rgbColor | '#fff'                                           | 表格内容背景色                                               |             |
    | header-ft-color            | hexColor, rgbColor | '#3e3e3e'                                        | 表头文字颜色                                                 |             |
    | content-ft-color           | hexColor, rgbColor | '#3e3e3e'                                        | 表格内容文字颜色                                             |             |
    | link-color                 | hexColor, rgbColor | '#0024c8'                                        | 表格内容为链接时的颜色                                       |             |
    | first-col-bg-color         | hexColor, rgbColor | '#f1f1f1'                                        | 首列背景色                                                   |             |
    | border-color               | hexColor, rgbColor | '#e1e1e1'                                        | 表格框线的颜色                                               |             |
    | show-left-and-right-border | boolean            | false                                            | 是否显示表格两侧的边框，这个参数主要是考虑到表格的宽度是屏宽时，两侧有边框不太好看 | true, false |
    | show-vert-border           | boolean            | true                                             | 是否显示单元格的垂直方向的框线                               | true, false |
    | loader-size                | string, number     | 50                                               | 加载动画的大小（单位：rpx）                                  |             |
    | loader-color               | hexColor           | '#a3a3a3'                                        | 加载动画的颜色（必须是#a3a3a3这种16进制的形式）              |             |
    | loader-bg-color            | hexColor, rgbColor | '#f8f8f8'                                        | 加载时的表格背景色                                           |             |
    | check-col-width            | string, number     | 70                                               | checkbox列的列宽                                             |             |
    | checker-color              | hexColor, rgbColor | '#3e3e3e'                                        | checkbox里面对勾的颜色                                       |             |
    | checker-border-color       | hexColor, rgbColor | '#d3d3d3'                                        | checkbox边框的颜色                                           |             |
    | checker-bg-color           | hexColor, rgbColor | 'rgba(0, 0, 0, 0)'                               | checkbox里面对勾周围的填充颜色                               |             |
    | checker-box-bg-color       | hexColor, rgbColor | 'rgba(0, 0, 0, 0)'                               | checkbox的背景色                                             |             |
    | checker-cell-bg-color      | hexColor, rgbColor | '#f1f1f1'                                        | checkbox单元格的背景色                                       |             |

3. 参数列表（功能型参数）

    | 参数名                | 类型    | 默认值                 | 说明                                                         | 可选值                       |
    | :-------------------- | :------ | :--------------------- | :----------------------------------------------------------- | :--------------------------- |
    | empty-string          | string  | '-'                    | 单元格判空时显示的字符                                       |                              |
    | first-line-fixed      | boolean | false                  | 是否固定首列                                                 | true, false                  |
    | text-align            | string  | 'center'               | 单元格内容对齐方式                                           | center, left, right          |
    | padding               | array   | [5, 10]                | 单元格内间距<br>第一个元素是垂直间距，第二个元素是水平间距<br>只传入一个元素时，垂直与水平间距均为该元素 |                              |
    | url-col               | array   | []                     | 内容为链接的列的标注，用于告诉组件哪些列是链接               | 格式见下方说明               |
    | computed-col          | array   | []                     | 需要统计的列的标注，用于告诉组件哪些列需要统计<br>不填时不显示底部统计，填了才显示 | 格式见下方说明               |
    | bottom-computed-fixed | boolean | true                   | 是否固定底部统计                                             | true, false                  |
    | value-format          | array   | []                     | 条件格式标注，用于展示表格数据的条件格式（例如满足什么条件，文本显示什么颜色） | 格式见下方说明               |
    | format-col            | array   | []                     | 需要格式化内容的列的标注，用于告诉组件哪些列需要格式化内容   | 格式见下方说明               |
    | sort-col              | array   | []                     | 表格以哪些列为排序基准的标注，用于告诉组件哪些列可以作为排序的标准 | 格式见下方说明               |
    | sort-ways             | array   | ['none', 'asc', 'inv'] | 排序方式，默认值代表：初始化时不进行排序，点一下表头升序排列，再点一下表头降序排列，再点一下恢复无序，使用方法见下方说明 | 格式见下方说明               |
    | loading               | boolean | false                  | 控制表格是否显示加载动画，使用方法见下方说明                 | true, false                  |
| enable-check          | string  | ''                     | 控制表格是否开启选择功能                                     | multiple, single<br>或者不填 |
    
    参数格式说明：
    
    (1) url-col
    
    ```json
    // 数组中每个元素代表一列，这些列的内容为链接
    // 链接类型：route (项目页面路由), link (网址)
    [{
        type: 'route', // 链接类型
        key: 'url'     // 所在列的key值（表头的key）
    }, {
        type: 'http',
        key: 'link'
    }]
    ```
    
    (2) computed-col
    
    ```json
    // 数组中每个元素代表一列（每个元素都是表头的key）
    ['age', 'score'] // key为'age'与'score'的列需要统计
    ```
    
    (3) value-format
    
    ```json
    // 数组中每个元素代表一列，这些列实行条件格式
    /** 条件类型（type）：
     * bigger (单元格内容大于某个值实行条件格式，单元格内容必须为纯数字或数字字符串)
     * smaller (单元格内容小于某个值实行条件格式，单元格内容必须为纯数字或数字字符串)
     * range (单元格内容在某个不包括端点的范围内实行条件格式，单元格内容必须为纯数字或数字字符串)
     * equal (单元格内容等于某个值实行条件格式)
     * average-bigger (单元格内容大于这一列的平均值实行条件格式，单元格内容必须为纯数字或数字字符串)
     * average-smaller (单元格内容小于这一列的平均值实行条件格式，单元格内容必须为纯数字或数字字符串)
     * average-equal (单元格内容等于这一列的平均值实行条件格式，单元格内容必须为纯数字或数字字符串)
     */
    /** 关于range：
      * type = bigger | smaller | equal 时，range为一数字，例如下面的第一个元素
      * type = range 时，range为一数组，例如下面的第二个元素
      * type = average-* 时，range不填写，例如下面的第三个元素
      */
    /** 关于key：
      * 目前仅能实现每列一个条件格式，每列多个条件格式暂时无法实现
      * 所以，数组中如果出现多个一样的key，取索引值最小的条件格式进行渲染
      */
    /** 关于style:
      * 有两个参数：
      * color => 文本颜色
      * bgColor => 单元格背景色
      * 可一起使用，例如下面的第一个元素
      * 可单独使用，例如下面的第二、第三个元素
      */
    [{
        type: 'bigger',
        range: 20,
        key: 'level',
        style: {
            color: '#00f',
            bgColor: '#e1f0ff'
        }
    }, {
        type: 'range',
        range: [19, 25],
        key: 'age',
        style: {
            color: '#f00'
        }
    }, {
        type: 'average-smaller',
        key: 'score',
        style: {
            bgColor: '#e1f0ff'
        }
    }]
    ```
    
    (4) format-col
    
    ```json
    // 数组中每个元素代表一列，这些列需要格式化内容
    /** 关于template:
          * #key#代表原来单元格中的值，例如原来的值是19，#age#岁格式化之后是19岁
          */
    /** 关于bottomComputedFormat：
          * 底部统计是否也格式化
          */
    [{
        key: 'age',
        template: '#age#岁',
        bottomComputedFormat: true
    }, {
        key: 'score',
        template: '#score#分',
        bottomComputedFormat: false
    }]
    ```
    
    (5) sort-col
    
    ```json
    // 数组中每个元素代表一列，这些列可以作为排序的基准
    // 这些列的表头会出现排序的图标
    [{
        key: 'name',    // 所在列的key值（表头的key）
        isNumber: false // 该列数据是不是数字或者日期，日期例子：(2020-09-04 08:00) (2020/06/04) (08:00:06)
    }, {
        key: 'age',
        isNumber: true
    }]
    ```
    
    (6) sort-ways
    
    ```json
    ['none', 'asc', 'inv']
    /** 元素说明
      * none：不排序
      * asc：升序
      * inv：降序
      */
    /** 为什么是这种格式？
      * 在点击可以作为排序基准列的表头时，排序方式会在这个数组中切换
      * 第一个元素代表表格初始化时候的排序方式
      * 例如，表格刚初始化的时候是无序的，因为第一个元素是none，然后你点了可以作为排序基准列的表头，这时表格会按升序排列，因为第二个元素是asc；之后你又点这个表头，这时表格会按降序排列，因为最后一个元素是inv；如果你又点了一次表头，表格就会恢复为无序状态；再点这个表头，以此类推
      * 另外，如果在你点击过可以作为排序基准列的表头之后（无论点了几次），你点了另外一个可以作为排序基准列的表头，这时排序方式会变成数组中的第二个元素，在上面描述的情景中，排序方式会变成升序，因为第二个元素是asc
      */
    /** 我应该怎么传值？
      * 类似 ['asc', 'inv', 'none']、['inv', 'none', 'asc']都可以，只要是这三个字符串的排列组合都可以。但是不能出现其他字符串
      */
    ```
    
    (7) loading
    
    　这个参数主要是用在等待数据加载的，传入true时显示loading动画并隐藏表格，传入false时显示表格并隐藏loading动画。

5\. Event

| 事件名       | 说明                           | 返回值 |
| ------------ | ------------------------------ | ------ |
| @onCellClick | 点击单元格时触发               | 见下方 |
| @onCheck     | 选择行时触发（启用选择后有效） | 见下方 |

```json
// @onCellClick
{
  "content": "18",    // 单元格内容
  "contentIndex": 1,  // 单元格行索引
  "header": "年龄",    // 表头内容
  "headerIndex": 1,   // 单元格列索引
  "key": "age"        // 单元格所在列的key
  "lineData": {       // 这一行的所有数据
    "name": "陈二",
    "age": "18",
    "collage": "商学院",
    "score": "98",
    "url": [
      "我带参数",
      "/pages/demos/noticeBar-demo/more",
      {
        "name": "陈二"
      }
    ],
    "link": ""
  }
}
```

```json
// @onCheck
{
  "checkType": "multiple", // 启用的类型（多选 => multiple / 单选 => single）
  "data": [
    {
      "index": 0,
      "lineData": { // 这一行的数据
        "name": "刘一",
        "age": "",
        "collage": "信息学院",
        "score": 99,
        "url": [
          "查看更多",
          "/pages/demos/noticeBar-demo/more"
        ],
        "link": "",
        "checked": true
      }
    }
  ]
}
```

6\. 可使用ref调用的组件本身的方法

(1) `this.$refs.table.getAverage(key)`

​	返回某个数字列的平均值，传入该列的key

(2) `this.$refs.table.getTotal(key)`

​	返回某个数字列的总和，传入该列的key

#### 三、作者的话

1. 创作不易，喜欢的朋友给个五星好评，这对我很重要鸭

2. 有什么问题可以加我QQ：751551976，或者发邮件：wyb_goodluck@163.com

3. 因为包含了中文字符表（用于首字母排序），所以插件体积较大。如果用不到排序可以删掉插件包中的`characterToPinyin.js`文件，并将插件源码中的几个地方注释掉：

    (1) 第195行`import Pinyin from './characterToPinyin.js'`

    (2) 第711行`this.doSort(this.sortCol[0].key, this.sortWays[this.sortWay], this.sortCol[0].isNumber)`

    (3) 第715行整个`doSort()`方法注释
    
    (4) 如果对项目体积不敏感或者嫌麻烦，可以不用动
