<template>
	<view
		@tap="goAdd"
		class="fa-add"
		:class="['fa-add--mode--' + mode]"
		:style="[
			{
				bottom: bottom + 'rpx',
				right: right + 'rpx',
				borderRadius: mode == 'circle' ? '10000rpx' : '8rpx',
				zIndex: zIndex
			},
			customStyle
		]"
	>
		<view class="" v-if="!$slots.default">
			<u-icon :name="icon" :custom-style="iconStyle"></u-icon>
			<view class="fa-add__tips">{{ tips }}</view>
		</view>
		<slot v-else />
	</view>
</template>

<script>
export default {
	name: 'fa-add',
	props: {		
		// 浮动按钮形状，circle-圆形，square-方形
		mode: {
			type: String,
			default: 'circle'
		},
		// 自定义图标
		icon: {
			type: String,
			default: 'edit-pen'
		},
		// 提示文字
		tips: {
			type: String,
			default: ''
		},
		// 浮动按钮到底部的距离，单位rpx
		bottom: {
			type: [Number, String],
			default: 300
		},
		// 浮动按钮到右边的距离，单位rpx
		right: {
			type: [Number, String],
			default: 40
		},
		// 层级
		zIndex: {
			type: [Number, String],
			default: '9'
		},
		// 图标的样式，对象形式
		iconStyle: {
			type: Object,
			default() {
				return {
					color: '#909399',
					fontSize: '38rpx'
				};
			}
		},
		// 整个组件的样式
		customStyle: {
			type: Object,
			default() {
				return {};
			}
		}
	},

	data() {
		return {};
	},
	methods: {
		goAdd() {
			this.$emit('click');					
		}
	}
};
</script>

<style lang="scss" scoped>
.fa-add {
	width: 80rpx;
	height: 80rpx;
	position: fixed;
	z-index: 9;
	display: flex;
	flex-direction: column;
	justify-content: center;
	background-color: #e1e1e1;
	color: $u-content-color;
	align-items: center;
	transition: opacity 0.4s;

	&__tips {
		font-size: 24rpx;
		transform: scale(0.8);
		line-height: 1;
	}
}
</style>
