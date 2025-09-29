<template>
	<view class='t-toptips' :style="{top: top,background: cubgColor}" :class="[show?'t-top-show':'']">
		<view v-if="loading" class="flex flex-sub">
			<view class="cu-progress flex-sub round striped active sm">
				<view :style="{ background: color,width: value + '%'}"></view>
			</view>
			<text class="margin-left">{{value}}%</text>
		</view>
		<block v-else>{{msg}}</block>		
	</view>
</template>

<script>
export default {
	props: {
		top: {
			type: String,
			default: 'auto'
		},
		bgColor: {
			type: String,
			default: 'rgba(49, 126, 243, 0.5)',
		},
		color: {
			type: String,
			default: '#e54d42',
		}
	},
	data() {
		return {
			cubgColor: '',
			loading: false,
			value: 5,
			show: false,
			msg: '执行完毕~',
		}
	},
	methods: {
		toast(title = '',{ duration = 2000, icon = 'none'} = {}) {
			uni.showToast({title,duration,icon});
		},
		getRequest(url) {  
			let theRequest = new Object(); 
			let index = url.indexOf("?");
			if (index != -1) {  
				let str = url.substring(index+1);  
				let strs = str.split("&");  
				for(let i = 0; i < strs.length; i ++) {  
					theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);  
				} 
			}  
			return theRequest;  
		},		
		/*
			上传说明：
			currentWebview: 当前窗口webview对象
			url：上传接口地址
			name：上传文件key值
			header: 上传接口请求头
			...：body内其他参数
		*/
		appChooseFile({currentWebview,url,name = 'file',header,...formData} = {}) {			
			let wv = plus.webview.create("","/hybrid/html/index.html",{
				'uni-app': 'none', //不加载uni-app渲染层框架，避免样式冲突
				top: 0,
				height: '100%',
				background: 'transparent'
			},{
				url,
				header,
				formData,
				key: name,
			});
			wv.loadURL("/hybrid/html/index.html")
			currentWebview.append(wv);
			wv.overrideUrlLoading({mode:'reject'},(e)=>{
				let {fileName,id} = this.getRequest(e.url);
				fileName = unescape(fileName);
				id = unescape(id);
				return this.onCommit(
				this.$emit('up-success',{fileName,data: id})
				);
			});			
		},		
		/* 
		上传
		*/
		upload(param = {}) {			
			if (!param.url) {
				this.toast('上传地址不正确');
				return;
			}			
			if (this.loading) {
				this.toast('还有个文件玩命处理中，请稍候..');
				return;
			}
			return this.appChooseFile(param);
		},		
		onCommit(resolve) {
			this.msg = '执行完毕~';
			this.loading = false;
			this.cubgColor = 'rgba(57, 181, 74, 0.5)';
			setTimeout(()=>{this.show = false;},1500);
			return resolve;
		},		
		setdefUI() {
			this.cubgColor = this.bgColor;
			this.value = 0;
			this.loading = true;
			this.show = true;
		},		
		upErr(errText) {
			this.$emit('up-error',errText);
		},		
		errorHandler(errText,reject) {
			this.msg = errText;
			this.loading = false;
			this.cubgColor = 'rgba(229, 77, 66, 0.5)';
			setTimeout(()=>{this.show = false;},1500);
			return reject(errText);
		}
	}
}
</script>

<style scoped>
	.t-toptips {
		width: 100%;
		padding: 18upx 30upx;
		box-sizing: border-box;
		position: fixed;
		z-index: 90;
		color: #fff;
		font-size: 30upx;
		left: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		word-break: break-all;
		opacity: 0;
		transform: translateZ(0) translateY(-100%);
		transition: all 0.3s ease-in-out;
	}
	
	.t-top-show {
		transform: translateZ(0) translateY(0);
		opacity: 1;
	}
	
</style>
