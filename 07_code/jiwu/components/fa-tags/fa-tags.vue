<template>
	<view class="fa-tags">
		<view class="">
			<u-tag
				:bg-color="lightColor"
				:border-color="faBorderColor"
				:color="theme.bgColor"
				:text="item"
				class="u-m-r-10"
				v-for="(item, index) in tags"
				:key="index"
				mode="light"
				:closeable="true"
				@close="closeTag(index)"
			/>
		</view>
		<view class="u-flex u-row-between u-p-t-20">
			<view class=""><u-input v-model="lstag" placeholder="请输入标签" :clearable="false" /></view>
			<view class="u-flex">
				<view class="">
					<u-button type="primary" throttle-time="0" :custom-style="{ backgroundColor: theme.bgColor, color: theme.color }" size="mini" @click="addTags(1)">
						添加标签
					</u-button>
				</view>
				<view class="u-m-l-10"><u-button type="error" size="mini" @click="addTags(0)">清空标签</u-button></view>
			</view>
		</view>
	</view>
</template>

<script>
import Emitter from '@/uview-ui/libs/util/emitter.js';
export default {
	name:'fa-tags',
	mixins: [Emitter],
	props:{
		value:{
			type:String,
			default:''
		},
		tagList:{
			type:[Array,String],
			default:''
		}
	},
	watch: {
		tags(newValue, oldValue) {
			this.$emit('input', newValue.join(','));
			this.$emit('change',newValue);
			setTimeout(() => {
				this.dispatch('u-form-item', 'on-form-blur', newValue.join(','));
			}, 50);
		},
		tagList:{
			immediate: true,
			handler(val) {
				if(val){
					if(this.$u.test.array(val)){
						this.tags = val;
					}else{
						this.tags = val.split(',');
					}
				}
			}
		}
	},
	data() {
		return {
			tags: [],
			lstag: '',
		};
	},
	methods:{
		//添加标签或清空标签
		addTags(type) {
			if (!type) {
				this.tags = [];
				return;
			}
			if (!this.$u.trim(this.lstag, 'all')) {
				this.$u.toast('请输入标签内容');
				return;
			}
			if (this.tags.indexOf(this.lstag) != -1) {
				this.$u.toast('标签内容重复');
				return;
			}
			if (this.tags.length >= 3) {
				this.$u.toast('标签最多可添加3个哦');
				return;
			}
			this.tags.push(this.lstag);
			this.lstag = '';
		},
		//移除标签
		closeTag(index) {
			this.tags.splice(index, 1);
		},
	}
};
</script>

<style lang="scss" scoped>
	.fa-tags{
		width: 100%;
	}
</style>
