<template>
	<div>
		<el-dialog :title="title" :visible.sync="open" width="80%" append-to-body>
			<div v-for="(item,index) in detailInfo" :key="item.id+index" style="padding: 10px;font-size: 16px;">
				<el-row :gutter="20">
					<el-col :span="4" style="display: flex;align-items: flex-start;">
						<div style="width: 3px;height: 25px;background-color: aqua;margin-right: 10px;"></div>
						<div style="min-width: 70px;">设备名称:</div>
						<div style="font-weight: bold;padding-left: 10px;">{{item.equipmentName}}</div>
					</el-col>
					<el-col :span="6" style="display: flex;align-items: flex-start;">
						<div style="min-width: 50px;">地点:</div>
						<div style="font-weight: bold;padding-left: 10px;">{{item.equipmentRegion}}</div>
					</el-col>
					<el-col :span="4" style="display: flex;align-items: flex-start;">
						<div style="min-width: 70px;">设备状态:</div>
						<dict-tag style="font-weight: bold;padding-left: 10px;" :options="dict.type.equipment_type" :value="item.equipmentStatus" />
						<!-- <span style="font-weight: bold;padding-left: 10px;">{{item.equipmentStatus}}</span> -->
					</el-col>
					<el-col :span="4" style="display: flex;align-items: flex-start;">
						<div style="min-width: 60px;">巡检人:</div>
						<div style="font-weight: bold;padding-left: 10px;">{{item.createBy}}</div>
					</el-col>
					<el-col :span="6" style="display: flex;align-items: flex-start;">
						<div style="min-width: 70px;">巡检时间:</div>
						<div style="font-weight: bold;padding-left: 10px;">{{item.createTime}}</div>
					</el-col>
				</el-row>
				<div style="padding: 10px;background-color: #f2f2f2;margin: 20px 0;">
					<div>
						<el-row>
							<el-col :span="24" style="margin-bottom: 20px;">
								<span style="font-weight: bold;">巡检详情</span>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="24" style="display: flex;align-items: flex-start;margin-bottom: 10px;">
								<div style="min-width: 70px;text-align: right;">定位:</div>
								<div style="padding-left: 10px;">{{item.region|''}}</div>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="24" style="display: flex;align-items: flex-start;margin-bottom: 10px;">
								<div style="min-width: 70px;text-align: right;">照片:</div>
								<div v-if="item.img" style="padding-left: 10px;">
									<image-preview :src="item.img" :width="50" :height="50"></image-preview>
									<!-- <img :style="{ 'width': '50px','height': '50px','margin-left': index != 0 ? '10px' : '0px' }" v-for="(itm,index) in item.img.split(',')" :key="itm" :src="itm" alt=""> -->
								</div>
								<div v-else style="padding-left: 10px;">暂无图片</div>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="24" style="display: flex;align-items: flex-start;">
								<div style="min-width: 70px;text-align: right;">巡检说明:</div>
								<div style="padding-left: 10px;">{{item.remark|''}}</div>
							</el-col>
						</el-row>
					</div>
					<div></div>
				</div>
			</div>
		</el-dialog>
	</div>

</template>

<script>
	import ImagePreview from '@/components/ImagePreview'
	export default{
		dicts: ["equipment_type"],
		components: { ImagePreview },
		data(){
			return{
				detailInfo:[],//巡检信息
				// 弹出层标题
				title: "",
				// 是否显示弹出层
				open: false,
			}
		},
		methods:{
			getDetail(row){
				this.$http.getAction('/wms/dayInfo/list', {
					dayId: row.id
				}).then(response => {
					this.open = true;
					this.title = "巡检详情";
					this.detailInfo = response.rows
					console.log(response)
				});
			},
			// 取消按钮
			cancel() {
				this.open = false;
				this.reset();
			},
		}
	}
</script>

<style>
</style>