<template>
	<view class="">
		<view class="orderby-select" @click="show = true"><image src="../../static/image/select.png" mode="aspectFit"></image></view>
		<u-popup v-model="show" mode="right" width="88%" :customStyle="customStyle">
			<view class="fa-popup">
				<scroll-view class="fa-scroll-view" scroll-y="true">
					<view class="fa-select-list u-m-b-15">
						<view class="u-border-bottom u-p-20 u-text-weight">排序</view>
						<view class="u-flex u-flex-wrap list u-p-20">
							<view class="item u-m-b-15" :style="[orderStyle(item)]" @click="orderBy(item, index)" v-for="(item, index) in orderList" :key="index">
								<text class="u-m-r-5">{{ item.title }}</text>
								<u-icon :name="orderIcon(item, index)"></u-icon>
							</view>
						</view>
					</view>
					<view class="">
						<view class="u-flex u-bg-white u-row-between u-m-b-15">
							<view class="u-p-20 u-text-weight">筛选</view>
							<view class="" v-if="multiple">
								<u-checkbox  v-model="checked" name="1" :active-color="theme.bgColor"><text>多选模式</text></u-checkbox>
							</view>
						</view>
						<view class="fa-select-list u-m-b-15 u-border-top" v-for="(item, index) in filterList" :key="index">
							<view class="u-p-20 u-border-bottom u-text-weight">{{ item.title }}</view>
							<view class="u-flex list u-flex-wrap u-p-20">
								<view
									class="item u-m-b-15"
									:style="[itemStyle(index, key)]"
									v-for="(res, key) in item.content"
									:key="key"
									@click="select(index, key)"
								>
									{{ res.title }}
								</view>
							</view>
						</view>
					</view>
				</scroll-view>
				<view class="footer u-flex u-border-top">
					<view class="u-flex-6" @click="close">取消</view>
					<view class="u-flex-6" :style="{ backgroundColor: theme.bgColor, color: theme.color }" @click="toGo">确定</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
// 获取系统状态栏的高度
let systemInfo = uni.getSystemInfoSync();
export default {
	name: 'fa-orderby-select',
	props: {
		multiple:{
			type:Boolean,
			default:false
		},
		separator: {
			type: String,
			default: ';'
		},
		orderList: {
			type: [Array],
			default() {
				return [];
			}
		},
		filterList: {
			type: [Array],
			default() {
				return [];
			}
		}
	},
	computed: {
		// 转换字符数值为真正的数值
		navbarHeight() {
			// #ifdef H5
				return 44;
			// #endif
			// #ifdef APP-PLUS
				return 44+systemInfo.statusBarHeight;
			// #endif
			// #ifdef MP
			// 小程序特别处理，让导航栏高度 = 胶囊高度 + 两倍胶囊顶部与状态栏底部的距离之差(相当于同时获得了导航栏底部与胶囊底部的距离)			
				let height = systemInfo.platform == 'ios' ? 44 : 48;
				return height+systemInfo.statusBarHeight;
			// #endif
		},
		customStyle(){
			return {
				height:'calc(100% - '+this.navbarHeight+'px)',
				top:this.navbarHeight+'px'
			}
		},
		orderStyle() {
			return item => {
				let style = {
					backgroundColor: this.theme.bgColor,
					color: this.theme.color
				};
				if (item.name == this.orderby) {
					return style;
				}
				return {};
			};
		},
		itemStyle() {
			return (index, key) => {
				let style = {
					backgroundColor: this.theme.bgColor,
					color: this.theme.color
				};
				if (!this.checked) {
					if (this.singleList[index] == key) {
						return style;
					}
				} else {
					let arr = this.manyList[index];
					if (arr && arr.indexOf(key) != -1) {
						return style;
					}
				}
				return {};
			};
		},
		orderIcon() {
			return (item, index) => {
				if (item.name == this.orderby || this.orderway[index] != undefined) {
					if (this.orderway[index] == 'asc') {
						return 'arrow-upward';
					} else {
						return 'arrow-downward';
					}
				}
				let orderway = this.$util.getQueryString('orderway', item.url);
				if (orderway == 'asc') {
					return 'arrow-upward';
				}
				return 'arrow-downward';
			};
		}
	},
	mounted() {		
		this.init();
	},
	data() {
		return {
			show: false,
			checked: false,
			orderIndex: 0,
			singleList: [],
			manyList: [],
			orderway: [],
			orderby: ''
		};
	},
	methods: {
		init() {
			//默认排序
			this.orderList.forEach((item, index) => {
				if (item.active) {
					this.orderby = item.name;
					this.orderway[index] = this.$util.getQueryString('orderway', item.url);
				}
			});
			//默认选中
			let len = (this.filterList && this.filterList.length) || 0;
			this.singleList = [];
			this.manyList = [];
			for (let i = 0; i < len; i++) {
				this.singleList.push(0);
				this.manyList.push([0]);
			}
		},
		close() {
			this.show = false;
			this.init();
		},
		orderBy(item, index) {
			this.orderIndex = index;
			//当前已选中
			if (item.name == this.orderby) {
				if (this.orderway[index] == 'asc') {
					this.$set(this.orderway, index, 'desc');
				} else {
					this.$set(this.orderway, index, 'asc');
				}
				return;
			}
			if (this.orderway[index] == undefined) {
				this.$set(this.orderway, index, this.$util.getQueryString('orderway', item.url));
			}
			this.orderby = this.$util.getQueryString('orderby', item.url);
		},
		select(index, key) {
			if (!this.checked) {
				this.$set(this.singleList, index, key);
			} else {
				//全部，就只有全部了
				if (key == 0) {
					this.$set(this.manyList, index, [key]);
					return;
				}
				let arr = this.manyList[index];
				if (!arr) {
					this.$set(this.manyList, index, [key]);
				} else {
					//全部存在，要移除
					let all = arr.indexOf(0);
					if (all != -1) {
						arr.splice(all, 1);
					}
					let index = arr.indexOf(key);
					if (index != -1) {
						//存在，移除
						arr.splice(index, 1);
					} else {
						arr.push(key);
					}
				}
			}
		},
		//取筛选条件
		toGo() {
			let query = {};
			//单选
			if (!this.checked) {
				this.singleList.forEach((item, index) => {
					if (item && this.filterList[index]) {
						let row = this.filterList[index].content[item];
						query[this.filterList[index].name] = row.value;
					}
				});
			} else {
				//多选
				query.multiple = 1;
				this.manyList.forEach((item, index) => {
					let arr = [];
					item.forEach(res => {
						if (res && this.filterList[index]) {
							let row = this.filterList[index].content[res];
							arr.push(row.value);
						}
					});
					if (arr.length) {
						query[this.filterList[index].name] = !this.separator ? arr : arr.join(this.separator);
					}
				});
			}
			//赋值排序
			query.orderby = this.orderby;
			query.orderway = this.orderway[this.orderIndex];
			this.show = false;
			this.$emit('change', query);
		}
	}
};
</script>

<style lang="scss" scoped>
.orderby-select {
	image {
		width: 40rpx;
		height: 40rpx;
	}
}
.fa-select-list {
	background-color: #ffffff;
	.title {
	}
	.list {
		.item {
			background-color: #eeeeee;
			padding: 10rpx 30rpx;
			border-radius: 100rpx;
			margin-right: 15rpx;
			color: $u-tips-color;
		}
	}
}
.fa-popup {
	height: 100%;
	background-color: #f7f7f7;
	.fa-scroll-view {
		height: calc(100% - 100rpx);
	}
	.footer {
		background-color: #ffffff;
		height: 100rpx;
		line-height: 100rpx;
		display: flex;
		width: 100%;
		text-align: center;
	}
}
</style>
