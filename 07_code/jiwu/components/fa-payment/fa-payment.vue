<template>
	<view>
		<u-popup v-model="isShow" mode="center" border-radius="5" z-index="1000">
			<view class="payment" :style="[payStyle]">
				<view class="header u-flex u-row-between u-p-l-30 u-p-r-30 u-p-t-20 u-p-b-20">
					<view class="">{{ title }}</view>
					<u-icon name="close" @click="close"></u-icon>
				</view>
				<view class="u-p-30">
					<view class="alert-warning u-p-20 u-m-b-10">
						<view class="">{{ articleTitle }}</view>
						<view class="price u-m-t-10">付费金额：￥{{ money }}</view>
					</view>

					<view class="u-border-top u-m-t-20">
						<u-radio-group v-model="paytype" class="group">
							<view class="group" v-for="(item, index) in paytypelist" :key="index">
								<u-cell-item :icon="item.icon" :icon-style="item.iconColor" :arrow="false" :title="item.name" @click="selectType(item.value)">
									<u-radio slot="right-icon" :active-color="theme.bgColor" label-size="30" :name="item.value">
										<u-icon size="30" name="arrow-right"></u-icon>
									</u-radio>
								</u-cell-item>
							</view>
						</u-radio-group>
					</view>
				</view>

				<view class="u-text-center u-p-30">
					<view class="">
						<u-button type="primary" hover-class="none" :custom-style="{ width: '80%', height: '60rpx', backgroundColor: theme.bgColor, color: theme.color }" size="mini" @click="submit">
							立即支付
						</u-button>
					</view>
					<view class="u-m-t-30" v-if="vip > 0">
						<u-button type="primary" hover-class="none" size="mini" :custom-style="{ width: '80%', height: '60rpx', backgroundColor: '#18b566', color: theme.color }" @click="goPage('/pages/my/member?vip='+vip)">
							升级VIP{{ vip }},免费阅读
						</u-button>
					</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import { loginfunc } from '@/common/fa.mixin.js'
	export default {
		mixins: [loginfunc],
		props: {
			title: {
				type: String,
				default: '付费阅读'
			},
			articleTitle: {
				type: String,
				default: ''
			},
			articleId: {
				type: [String, Number],
				default: 0
			},
			money: {
				type: [String, Number],
				default: 0
			},
			vip: {
				type: [String, Number],
				default: 0
			}
		},
		computed: {
			defaultpaytype() {
				if (this.vuex_config.defaultpaytype) {
					return this.vuex_config.defaultpaytype;
				}
				return 'balance'
			},
			paytypelist() {
				if (this.vuex_config.paytypelist) {
					this.paytype = this.vuex_config.defaultpaytype;
					let paytypelist = this.vuex_config.paytypelist.split(',');
					let list = [];
					paytypelist.map(item => {
						switch (item) {
							// #ifdef APP-PLUS || MP-WEIXIN || H5
							case 'wechat':
								list.push({
									name: '微信支付',
									value: 'wechat',
									icon: 'weixin-circle-fill',
									iconColor: { color: '#40BA49' }
								})
								break;
								// #endif
								// #ifdef APP-PLUS || H5 || MP-ALIPAY
							case 'alipay':
								if (!this.isWeixin) {
									list.push({
										name: '支付宝支付',
										value: 'alipay',
										icon: 'zhifubao-circle-fill',
										iconColor: { color: '#00AAEE' }
									})
								}
								break;
								// #endif
							case 'balance':
								list.push({
									name: '余额支付',
									value: 'balance',
									icon: 'rmb-circle-fill',
									iconColor: { color: '#f39c12' }
								})
								break;
						}
					})
					return list;
				}
				return [];
			},
			isWeixin() {
				// #ifndef MP-WEIXIN
				return this.$util.isWeiXinBrowser()
				// #endif

				// #ifdef MP-WEIXIN
				return true;
				// #endif
			},
			payStyle() {
				let style = {};
				if (this.vip > 0) {
					style.height = this.paytypelist.length == 3 ? '870rpx' : this.paytypelist.length == 2 ? '770rpx' : '670rpx';
				} else {
					style.height = this.paytypelist.length == 3 ? '770rpx' : this.paytypelist.length == 2 ? '670rpx' : '570rpx'
				}
				return style;
			}
		},
		data() {
			return {
				isShow: false,
				paytype: ''
			};
		},
		methods: {
			show() {
				this.isShow = true;
			},
			close() {
				this.isShow = false;
			},
			selectType(type) {
				this.paytype = type;
			},

			submit: async function() {
				let data = {
					id: this.articleId,
					paytype: this.paytype,
					method: 'web'
				};

				// #ifdef APP-PLUS
				data.appid = plus.runtime.appid;
				data.method = 'app';
				// #endif

				// #ifdef MP-WEIXIN
				data.method = 'miniapp';
				// #endif

				// #ifdef H5
				data.method = 'web';
				if (this.paytype == 'wechat') {
					data.method = this.$util.isWeiXinBrowser() ? 'mp' : 'web';
				}
				// #endif

				let res = await this.$api.getArchivesOrder(data);
				if (res.data == 'bind') {
					// #ifdef H5
					this.goAuth();
					// #endif

					// #ifdef MP-WEIXIN
					this.$u.route('/pages/login/wxlogin');
					// #endif

					return;
				}

				if (!res.code) {
					this.$u.toast(res.msg);
					return;
				}

				//余额支付
				if (this.paytype == 'balance') {
					this.close();
					this.$emit('success');
					this.close();
					return;
				}

				// #ifdef MP-WEIXIN
				uni.requestPayment({
					provider: 'wxpay',
					timeStamp: res.data.timeStamp,
					nonceStr: res.data.nonceStr,
					package: res.data.package,
					signType: res.data.signType,
					paySign: res.data.paySign,
					success: res => {
						this.$u.toast('支付成功！');
						this.getUserIndex();
						this.getVipIndex();
					},
					fail: err => {
						console.log('fail:' + JSON.stringify(err));
						this.$u.toast('支付失败');
					}
				});
				// #endif

				// #ifdef APP-PLUS
				uni.requestPayment({
					provider: this.paytype == 'alipay' ? 'alipay' : 'wxpay',
					orderInfo: res.data, //微信、支付宝订单数据
					success: res => {
						this.$u.toast('支付成功！');
						this.getUserIndex();
						this.getVipIndex();
					},
					fail: function(err) {
						console.log('fail:' + JSON.stringify(err));
						this.$u.toast('支付失败');
					}
				});
				// #endif

				// #ifdef H5
				if (this.$util.isWeiXinBrowser() && this.paytype == 'wechat') {
					//在微信环境，且为微信支付
					window.WeixinJSBridge.invoke(
						'getBrandWCPayRequest', {
							appId: res.data.appId,
							timeStamp: res.data.timeStamp,
							nonceStr: res.data.nonceStr,
							package: res.data.package,
							signType: res.data.signType,
							paySign: res.data.paySign
						},
						res => {
							if (res.err_msg === 'get_brand_wcpay_request:ok') {
								this.$u.toast('支付成功！');
								this.getUserIndex();
								this.getVipIndex();
							} else if (res.err_msg === 'get_brand_wcpay_request:cancel') {
								this.$u.toast('取消支付');
							} else {
								this.$u.toast('支付失败');
							}
						}
					);
				} else {
					//非微信环境的wap 支付方法，会返回orderid，再模拟表单提交
					data.returnurl = window.location.href;

					//URL地址
					if (res.data.toString().match(/^((?:[a-z]+:)?\/\/)(.*)/i)) {
						location.href = res.data;
						return;
					}

					//Form表单
					document.getElementsByTagName('body')[0].innerHTML = res.data;
					let form = document.querySelector('form');
					if (form && form.length > 0) {
						form.submit();
						return;
					}

					//Meta跳转
					let meta = document.querySelector('meta[http-equiv="refresh"]');
					if (meta && meta.length > 0) {
						setTimeout(function() {
							location.href = meta.content.split(/;/)[1];
						}, 300);
						return;
					}
				}
				// #endif
			}
		}
	}
</script>

<style lang="scss">
	.payment {
		width: 600rpx;

		.header {
			border-bottom: 1px solid #eee;
			background-color: #f8f8f8;
		}

		.alert-info {
			background-color: #d9edf7;
			border-color: #bce8f1;
			color: #3498db;
			border-radius: 6rpx;
		}

		.alert-warning {
			background-color: #fcf8e3;
			border-color: #faebcc;
			color: #f39c12;
			border-radius: 6rpx;
		}

		.price {
			color: red;
		}

		.group {
			width: 100%;
		}
	}
</style>
