<template>
	<view class="normal-login-container">
		<view class="logo-content align-center justify-center flex">
			<text class="title">智慧化仓库管理系统</text>
		</view>
		<view class="login-form-content">
			<view class="input-item flex align-center">
				<view class="iconfont icon-user icon"></view>
				<input v-model="loginForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" />
			</view>
			<view class="input-item flex align-center">
				<view class="iconfont icon-password icon"></view>
				<input v-model="loginForm.password" type="password" class="input" placeholder="请输入密码" maxlength="20" />
			</view>
			<view class="input-item flex align-center" style="width: 60%;margin: 0px;" v-if="captchaEnabled">
				<view class="iconfont icon-code icon"></view>
				<input v-model="loginForm.code" type="number" class="input" placeholder="请输入验证码" maxlength="4" />
				<view class="login-code">
					<image :src="codeUrl" @click="getCode" class="login-code-img"></image>
				</view>
			</view>
			<view class="action-btn">
				<button @click="handleLogin" class="login-btn cu-btn block bg-blue lg round"
					style="color: #0156C7;font-weight: bold;">登录</button>
			</view>
			<view class="xieyi text-center">
				<text class="text-grey1">登录即代表同意</text>
				<text @click="handleUserAgrement" class="text-blue">《用户协议》</text>
				<text @click="handlePrivacy" class="text-blue">《隐私协议》</text>
			</view>
		</view>
		<u-mask :show="show" @click="show = false" :mask-click-able="false">
			<view
				style="position: relative;padding: 30rpx;width: 500rpx;height: 600rpx;background-color: #FFFFFF;border-radius: 50rpx;margin: 0 auto;transform:translateY(50%);">
				<view style="text-align: center;font-size: 38rpx;font-weight: 600;margin-bottom: 20rpx;">
					发现新版本V{{versionData.appVersion}}
				</view>
				<view style="font-size: 24rpx;color: #cccccc;">更新内容</view>
				<view style="width: 100%;height: 360rpx;word-wrap:break-word;overflow-y: auto;">{{versionData.remark}}
				</view>
				<view @click="goUpdate"
					style="height: 60rpx;line-height: 60rpx;color: #FFFFFF;border-radius: 30rpx;width: 400rpx;text-align: center;position: absolute;background-color: #FA6837;bottom: 50rpx;right: 50rpx;">
					立即更新</view>
			</view>
		</u-mask>

	</view>
</template>

<script>
	import {
		getCodeImg
	} from '@/api/login'

	export default {
		data() {
			return {
					show: false, //遮罩层
				codeUrl: "",
				captchaEnabled: true,
				globalConfig: getApp().globalData.config,
				loginForm: {
					username: "",
					password: "",
					code: "",
					uuid: ''
				},
				localVersion: {
					version: "",
				}, //APP版本信息
				versionData: {
					appVersion: "",
					appName: "",
					appUrl: ""
				},
			}
		},
		created() {
			this.getCode();
			this.getVersion();
			plus.runtime.getProperty(plus.runtime.appid, (inf) => {
				this.localVersion = {
					versionCode: inf.version.replace(/\./g, ""),
					version: inf.version,
				};
			});
		},
		methods: {
			// 获取版本信息
			async getVersion() {
				this.getAppVersion()
			},
			getAppVersion() {
				this.$http.getAction('/wms/app/version',{}).then(res=>{
					console.log('res1',res)
						if (res.code == 200) {
							if (res.data) {
								this.versionData = res.data;
								if (Number(this.versionData.appVersion.replace(/\./g, "")) > Number(this.localVersion.versionCode)) {
									uni.hideTabBar()
									this.show = true
								}
							}
						}
				}).catch((e) => {
					console.log('res1',e)
				})
			},
			// 更新(当前只做了安卓端的更新)
			goUpdate() {
				//点击更新
				uni.showLoading({
					title: '下载中...'
				})
				
				console.log('appUrl',this.versionData.appUrl)
				var dtask = plus.downloader.createDownload(this.versionData.appUrl, {}, function(d, status) {
					uni.hideLoading();
					uni.showToast({
						title: '下载成功...',
						mask: false,
						duration: 1500,
					});
					console.log(d, status)
					// 下载完成    
					if (status == 200) {
						plus.runtime.install(plus.io.convertLocalFileSystemURL(d.filename), {}, {},
							function(error) {
								uni.showToast({
									title: '安装失败',
			 					icon: 'none'
								});
							})
					} else {
						uni.showToast({
							title: '下载失败，请检查您的网络',
							icon: 'none'
						});
					}
				});
				dtask.start();
			},
			// 隐私协议
			handlePrivacy() {
				let site = this.globalConfig.appInfo.agreements[0]
				this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`)
			},
			// 用户协议
			handleUserAgrement() {
				let site = this.globalConfig.appInfo.agreements[1]
				this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`)
			},
			// 获取图形验证码
			getCode() {
				getCodeImg().then(res => {
					this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
					if (this.captchaEnabled) {
						this.codeUrl = 'data:image/gif;base64,' + res.img
						this.loginForm.uuid = res.uuid
					}
				})
			},
			// 登录方法
			async handleLogin() {
				if (this.loginForm.username === "") {
					this.$modal.msgError("请输入您的账号")
				} else if (this.loginForm.password === "") {
					this.$modal.msgError("请输入您的密码")
				} else if (this.loginForm.code === "" && this.captchaEnabled) {
					this.$modal.msgError("请输入验证码")
				} else {
					this.$modal.loading("登录中，请耐心等待...")
					this.pwdLogin()
				}
			},
			// 密码登录
			async pwdLogin() {
				this.$store.dispatch('Login', this.loginForm).then(() => {
					this.$modal.closeLoading()
					this.loginSuccess()
				}).catch(() => {
					if (this.captchaEnabled) {
						this.getCode()
					}
				})
			},
			// 登录成功后，处理函数
			loginSuccess(result) {
				// 设置用户信息
				this.$store.dispatch('GetInfo').then(res => {
					this.$tab.reLaunch('/pages/inlet')
				})
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #ffffff;
		background-image: url(@/static/img/loginBg.png);
		background-size: 100% 100%;
		height: 100vh;
		background-repeat: no-repeat;
	}

	.normal-login-container {
		width: 100%;

		.logo-content {
			width: 100%;
			font-size: 21px;
			text-align: center;
			padding-top: 15%;

			image {
				border-radius: 4px;
			}

			.title {
				font-weight: 600;
				color: #ffffff;
				margin-left: 10px;
			}
		}

		.login-form-content {
			text-align: center;
			margin: 20px auto;
			margin-top: 15%;
			width: 80%;

			.input-item {
				margin: 20px auto;
				background-color: #f5f6f7;
				height: 45px;
				border-radius: 20px;
				display: flex;
				align-items: center;

				.icon {
					font-size: 38rpx;
					margin-left: 10px;
					color: #999;
				}

				.input {
					width: 100%;
					font-size: 14px;
					line-height: 20px;
					text-align: left;
					padding-left: 15px;
				}

			}

			.login-btn {
				margin-top: 40px;
				height: 45px;
			}

			.xieyi {
				color: #333;
				margin-top: 20px;
			}

			.login-code {
				height: 38px;
				float: right;

				.login-code-img {
					height: 38px;
					position: absolute;
					margin-left: 10px;
					width: 200rpx;
				}
			}
		}
	}
</style>