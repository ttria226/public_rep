// 应用全局配置
module.exports = {
	WMS_URL:"/wms/",
  // baseUrl: 'https://vue.www.xsrw.com/prod-api',
  // baseUrl: 'http://192.168.26.31:8080',
  baseUrl: 'http://192.168.0.99:8080',
  // 应用信息
  appInfo: {
    // 应用名称
    name: "xsrw-app",
    // 应用版本
    version: "1.1.0",
    // 应用logo
    logo: "/static/logo.png",
    // 官方网站
    site_url: "http://www.xsrw.com",
    // 政策协议
    agreements: [{
        title: "隐私政策",
        url: "https://www.xsrw.com/protocol.html"
      },
      {
        title: "用户服务协议",
        url: "https://www.xsrw.com/protocol.html"
      }
    ]
  }
}
