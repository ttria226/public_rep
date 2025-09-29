<template>
	<view class="list">
		<view class="fa-list-item u-border-bottom u-flex" v-for="(item, index) in archivesList" :key="index"
			@click="goDetail(item)">
			<view class="fa-item-image" v-if="!item.images_list.length">
				<image :src="item.image" mode="aspectFill"></image>
			</view>
			<view class="fa-item-content" :class="{ 'u-m-l-20': !item.images_list.length }">
				<view class="u-line-2 u-font-30 u-m-b-10" :style="[cmsTitleStyle(item.style)]">{{ item.title }}</view>
				<view class="u-flex u-flex-wrap u-m-t-20" v-if="item.images_list.length">
					<view class="images" v-for="(res, key) in item.images_list" :key="key">
						<u-image width="100%" border-radius="6" height="140" :src="res"></u-image>
					</view>
				</view>
				<view class="u-tips-color u-m-b-10 u-font-23">{{ item.create_date }}</view>
				<view class="article-tag u-flex">
					<view class="">
						<u-icon name="thumb-up-fill" color="#aaa" size="20"></u-icon>
						<text class="u-m-l-5 u-m-r-5">{{ item.likes }}</text>
						点赞
					</view>
					<view class="u-m-l-30">
						<u-icon name="chat-fill" color="#aaa" size="20"></u-icon>
						<text class="u-m-l-5 u-m-r-5">{{ item.comments }}</text>
						评论
					</view>
					<view class="u-m-l-30">
						<u-icon name="eye-fill" color="#aaa" size="20"></u-icon>
						<text class="u-m-l-5 u-m-r-5">{{ item.views }}</text>
						浏览
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			archivesList: {
				type: Array,
				default: []
			}
		},
		data() {
			return {};
		},
		methods: {
			goDetail(item) {
				if (item.model_id == 2) {
					this.$u.route('/pages/product/detail', {
						id: item.id
					});
				} else {
					this.$u.route('/pages/article/detail', {
						id: item.id
					});
				}
			}
		}
	};
</script>

<style lang="scss">
	.list {
		background: #ffffff;

		.fa-list-item {
			color: #333;
			padding: 30rpx;

			.fa-item-image {
				image {
					width: 220rpx;
					flex: 0 0 120rpx;
					height: 160rpx;
					border-radius: 10rpx;
				}
			}

			.fa-item-content {
				width: 100%;

				.images {
					width: 31%;
					margin-bottom: 25rpx;
				}

				.article-tag {
					color: #aaa;
					font-size: 25rpx;
				}
			}
		}
	}

	.fa-item-content .images:nth-child(3n + 2) {
		margin: 0 3.5%;
	}
</style>
