<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-ui/index.scss";
	.slot-content{
		padding: 40rpx 30rpx 30rpx 30rpx;
	}
	.input-style{
		display: flex;
		justify-content: center;
		align-items: center;
		flex-wrap:wrap;
		text-align: center !important;
		word-break: break-all;
	}
</style>
<script>
	import config from './config'
	import store from '@/store'
	import {
		getToken
	} from '@/utils/auth'
	var jpushModule = uni.requireNativePlugin("Uhf-Plugin")
	export default {
		onLaunch: function() {
			this.initApp()
			console.log('App Launch')
			if (uni.getSystemInfoSync().platform == "android") {

			};
			jpushModule.initUHFService()
			uni.getSystemInfo({
				success: function(e) {
					
					// #ifdef APP-PLUS
					// 检测升级
					// appUpdate()
					// #endif
					// #ifndef MP
					Vue.prototype.StatusBar = e.statusBarHeight;
					if (e.platform == 'android') {
						Vue.prototype.CustomBar = e.statusBarHeight + 50;
					} else {
						Vue.prototype.CustomBar = e.statusBarHeight + 45;
					};
					// #endif
			
					// #ifdef MP-WEIXIN
					Vue.prototype.StatusBar = e.statusBarHeight;
					let custom = wx.getMenuButtonBoundingClientRect();
					Vue.prototype.Custom = custom;
					Vue.prototype.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
					// #endif		
			
					// #ifdef MP-ALIPAY
					Vue.prototype.StatusBar = e.statusBarHeight;
					Vue.prototype.CustomBar = e.statusBarHeight + e.titleBarHeight;
					// #endif
					
					// #ifdef APP-PLUS
					//Vue.prototype.$api.listenTranMsg()
			// 		var info = plus.push.getClientInfo();
			
			// 		/* 5+  push 消息推送 ps:使用:H5+的方式监听，实现推送*/
			// 		plus.push.addEventListener("click", function(msg) {
			// 			console.log("click:" + JSON.stringify(msg));
			// 			console.log(msg.payload);
			// 			console.log(JSON.stringify(msg));
			// 			//这里可以写跳转业务代码
			// 		}, false);
			// 		// 监听在线消息事件    
			// 		plus.push.addEventListener("receive", function(msg) {
			// 			// plus.ui.alert(2);  
			// 			//这里可以写跳转业务代码
			// 			console.log("recevice:" + JSON.stringify(msg))
			// 		}, false);
					// #endif
					
					//Vue.prototype.$api.initLogin()
					
				}
			})
		},
		methods: {
			// 初始化应用
			initApp() {
				// 初始化应用配置
				this.initConfig()
				// 检查用户登录状态
				//#ifdef H5
				this.checkLogin()
				//#endif
			},
			initConfig() {
				this.globalData.config = config
			},
			checkLogin() {
				if (!getToken()) {
					this.$tab.reLaunch('/pages/login')
				}
			}
		}
	}
</script>