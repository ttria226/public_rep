<template>
	<view>
		<uni-forms-item :label="title" name="name">
			<uni-section title="" type="line">
				<uni-file-picker v-model="fileList" mode="grid" @select="select" @delete="deletephoto" @fail="fail" ref="upload" limit="5" :disabled="disabled" /></uni-file-picker>
			</uni-section>
		</uni-forms-item>
	</view>
</template>

<script>
	import {
		getToken
	} from '@/utils/auth'
	export default {
		props: {
			title:{
				type:String,
				default: ""
			}
			//父组件传过来的数据
			// dataList: {
			// 	type: Object,
			// 	default: ""
			// },
		},
		data() {
			return {
				fileList:[],
				formData:{
					img:''
				}
			}
		},
		methods: {
			// 选择上传触发函数
			select(e) {
				// 根据所选图片的个数，多次调用上传函数
				let promises = []
				for (let i = 0; i < e.tempFilePaths.length; i++) {
					const promise = this.uploadFiles(e.tempFilePaths, i)
					promises.push(promise)
				}
				Promise.all(promises).then(() => {

				})
			},
			// 上传函数
			async uploadFiles(tempFilePaths, i) {
				let that = this
				// this.$http.uploadAction("/file/upload")
				await uni.uploadFile({
					url: "http://192.168.26.31:8080/file/upload", //后端用于处理图片并返回图片地址的接口    
					filePath: tempFilePaths[i],
					name: 'file',
					header: {
						'Authorization': 'Bearer ' + getToken(),
					},
					success: res => {
						let data = JSON.parse(res.data) //返回的是字符串，需要转成对象格式   
						if (data.code == 200) {
							that.formData.img = (JSON.stringify((data.data.url)))
							this.$emit("imgDateCopy", that.formData.img);
							if (i == tempFilePaths.length - 1)
								that.formData.img = JSON.stringify(that.formData.url);
								// this.$emit("imgDate", that.formData.img);
						}
					},
					fail: () => {
						console.log("err");
					}
				})

			},
			// 移出图片函数
			async deletephoto() {
				that.formData.fileList = ''
			},
		}
	}
</script>

<style>
</style>