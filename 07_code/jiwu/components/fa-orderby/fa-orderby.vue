<template>
	<view>
		<view class="u-p-l-30 u-p-r-30 u-p-b-30 u-p-t-15 u-text-center" v-if="!filterList.length">
			<scroll-view scroll-x class="nav" scroll-with-animation :scroll-left="scrollLeft">
				<view class="u-flex u-row-around">
					<view
						class="item"
						:style="[{ color: tabIndex == index ? activeColor : color }]"
						v-for="(item, index) in tabList"
						:key="index"
						@click="cutTab(index)"
					>
						<view class="u-m-r-10" v-text="item[showField]"></view>
						<view class=""><u-icon :name="orderwayIcon(item)" :color="tabIndex == index ? activeColor : color" size="30"></u-icon></view>
					</view>
				</view>
			</scroll-view>
		</view>
		<view class="" v-else-if="is_show">
			<fa-dropdown :active-color="theme.bgColor">
				<u-dropdown-item v-model="value1" title="排序" :options="options1" @change="change1"></u-dropdown-item>
				<u-dropdown-item
					v-for="(item, index) in options2"
					:key="index"
					v-model="tabVal[item.name]"
					:title="item.title"
					:options="item.option"
				></u-dropdown-item>
			</fa-dropdown>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		tabList: {
			type: Array,
			default() {
				return [];
			}
		},
		filterList: {
			type: Array,
			default() {
				return [];
			}
		},
		showField: {
			type: String,
			default: 'name'
		},
		activeColor: {
			type: String,
			default: '#000'
		},
		color: {
			type: String,
			default: '#999'
		}
	},
	computed: {
		orderwayIcon() {
			return item => {
				let orderway = this.$util.getQueryString('orderway', item.url);
				if (orderway == 'asc') {
					return 'arrow-up';
				} else {
					return 'arrow-down';
				}
			};
		}
	},
	watch: {
		tabList: {
			immediate: true,
			handler: function(newValue) {				
				if (newValue && !this.options1.length) {
					this.options1 = [];
					newValue.forEach(item => {
						this.options1.push({
							label: item.title,
							value: item.url
						});
					});
				}
			}
		},
		filterList: {
			immediate: true,
			handler: function(newValue) {				
				if (newValue.length) {
					this.options2 = [];
					let val = {};
					newValue.forEach(item => {
						let op = {
							name: item.name,
							title: item.title,
							option: []
						};
						val[item.name] = '';
						item.content.forEach(it => {
							op.option.push({
								label: it.title,
								value: it.url
							});
						});
						this.options2.push(op);
					});
					this.tabVal = val;
					setTimeout(() => {
						this.is_show = true;
					}, 0);
				}
			}
		},
		tabVal: {
			deep: true,
			handler: function(newValue) {
				this.urls = '';
				for (let i in newValue) {
					if (newValue[i] && newValue[i] != '?') {
						let arr = decodeURIComponent(newValue[i])
							.replace('?', '')
							.split('=');
						this.urls += '&' + arr[0] + '=' + arr[1];
					}
				}
				this.$emit('change', {
					orderby: this.orderby,
					orderway: this.orderway,
					url: this.urls
				});
			}
		}
	},
	data() {
		return {
			tabIndex: 0,
			scrollLeft: 0,
			tabVal: {},
			value1: '',
			options1: [],
			options2: [],
			orderby: '',
			orderway: '',
			urls: '',
			is_show: false
		};
	},
	methods: {
		change1(value){
			this.goBy(value)
		},
		cutTab(index) {
			this.tabIndex = index;
			this.scrollLeft = (index - 1) * 60;
			this.goBy(this.tabList[index].url);
		},
		goBy(url) {
			this.orderby = this.$util.getQueryString('orderby', url);
			this.orderway = this.$util.getQueryString('orderway', url);
			this.$emit('change', {
				orderby: this.orderby,
				orderway: this.orderway,
				url: this.urls
			});
		}
	}
};
</script>

<style lang="scss">
.nav {
	white-space: nowrap;
	.item {
		display: inline-flex;
		margin: 0 10upx;
		padding: 0 20upx;
	}
}
</style>
