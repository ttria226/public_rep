<template>
	<view>
		<uni-forms-item :label="title" name="name">
			<uni-section title="" type="line">
				<uni-file-picker v-model="fileList" mode="grid" @select="select" @delete="deletephoto" ref="upload" limit="5" file-mediatype="image" :disabled="disabled" /></uni-file-picker>
			</uni-section>
		</uni-forms-item>
	</view>
</template>

<script>
	import config from '@/config'
	import {
		getToken
	} from '@/utils/auth'
const baseUrl = config.baseUrl
	export default {
		props: {
			title: {
				type: String,
				default: "照片"
			},
			disabled: {
				type: Boolean,
				default: false
			}
			//父组件传过来的数据
			// imgList: {
			// 	type: Object,
			// 	default: ""
			// },
		},
		data() {
			return {
				fileList: [],
				formData: {
					img: ''
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
					url: baseUrl+"/file/upload", //后端用于处理图片并返回图片地址的接口    
					filePath: tempFilePaths[i],
					name: 'file',
					header: {
						'Authorization': 'Bearer ' + getToken(),
					},
					success: res => {
						let data = JSON.parse(res.data) //返回的是字符串，需要转成对象格式   
						console.log('我是图片上传后的数据',res,data)
						if (data.code == 200) {
							that.formData.img = data.data.url
							this.$emit("imgDate", that.formData.img);
							// if (i == tempFilePaths.length - 1)
							// 	that.formData.img = JSON.stringify(that.formData.url);
							// // this.$emit("imgDate", that.formData.img);
						}
					},
					fail: () => {
						console.log("err");
					}
				})

			},
			// 移出图片函数
			async deletephoto() {
				that.formData.img = ''
			},
		}
	}
</script>

<style>
</style>