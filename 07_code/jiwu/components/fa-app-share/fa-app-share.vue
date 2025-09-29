<template>
	<view>
		<u-popup v-model="is_show" mode="bottom">
			<u-grid :col="3" :border="false">
				<u-grid-item v-for="(item,index) in list" :key="index" @click="share(index,item)">
					<u-icon :name="item.icon" :color="item.color" :size="item.size"></u-icon>
					<view class="grid-text u-m-t-10" v-text="item.name"></view>
				</u-grid-item>
			</u-grid>
			<u-gap height="3" bg-color="rgb(244, 246, 248)"></u-gap>
			<view class="u-text-center u-p-30" @click="hide">
				取消
			</view>
		</u-popup>
	</view>
</template>

<script>
export default {
	name:'fa-app-share',
	props:{
		title:{
			type:String,
			default:''
		},
		summary:{
			type:String,
			default:''
		},
		href:{
			type:String,
			default:''
		},
		imageUrl:{
			type:String,
			default:''
		}
	},
	data() {
		return {
			is_show: false,
			list:[
				{
					name:'微信好友',
					icon:'weixin-circle-fill',
					size:60,
					color:'#44b549',
					type:0,//图文
					provider:'weixin'					
				},
				{
					name:'微信朋友圈',
					icon:'moments-circel-fill',
					size:60,
					color:'#44b549',
					type:0,//图文
					provider:'weixin'					
				},
				{
					name:'微信收藏',
					icon:'star-fill',
					size:60,
					color:'#ff9100',
					type:0,//图文
					provider:'weixin'
				},
				{
					name:'QQ好友',
					icon:'qq-circle-fill',
					size:60,
					color:'#388BFF',
					type:2,//图
					provider:'qq',
				},
				{
					name:'新浪微博',
					icon:'weibo-circle-fill',
					size:60,
					color:'#BE3E3F',
					type:0,//图文
					provider:'sinaweibo	',
				}
			]
		};
	},
	methods:{
		show(){
			this.is_show = true;
		},
		hide(){
			this.is_show = false;
		},
		share(index,item){
			let data = {
			    provider: item.provider,			   
			    type: item.type,
			    href: this.href,
			    title: this.title,
			    summary: this.summary,
			    imageUrl: this.imageUrl,
			    success: (res) => {
			        this.hide();
					this.$u.toast('分享成功！');
			    },
			    fail: (err) => {
					console.log(JSON.stringify(err))
			        this.$u.toast('分享失败！');
			    }
			};
			if(index==0){
				 data.scene = "WXSceneSession";
			}
			if(index==1){
				data.scene = "WXSenceTimeline";
			}
			if(index==2){
				data.scene = 'WXSceneFavorite';
			}
			console.log(JSON.stringify(data))
			uni.share(data);
		}
	}
};
</script>

<style></style>
