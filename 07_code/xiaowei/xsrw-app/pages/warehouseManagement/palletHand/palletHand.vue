<template>
	<view class="pallet-hand">
		<headTitle title="托盘搬运"></headTitle>
		<view class="cont" style="margin-top: 55px;">
			<view class="radioBox">
				<view class="title">请选择出入库类型</view>
				<view>
					<u-radio-group v-model="from.type"  @change="radioTypeChange">
						<u-radio v-for="(item, index) in types" :key="index" :name="item.name">
							{{item.label}}
						</u-radio>
					</u-radio-group>
				</view>
			</view>
			
			<view class="from-box">
				<uni-forms ref="form" :modelValue="from" :rules="rules" label-position="left" label-width='100'>
					<uni-forms-item required label="开始位置" name="kaishiweizhi">
						<u-radio-group placement="row" v-model="from.kaishiweizhi" @change="radioStartChange">
							<u-radio :customStyle="{marginBottom: '0px'}" v-for="(item, index) in kaishiweizhi" :key="index" :name="item.name">
								{{item.label}}
							</u-radio>
						</u-radio-group>
					</uni-forms-item>
					<uni-forms-item required name="mudiweizhi" label="目的位置">
						<u-radio-group v-model="from.mudiweizhi">
							<u-radio v-for="(item, index) in mudiweizhi" :key="index" :name="item.name">
								{{item.label}}
							</u-radio>
						</u-radio-group>
					</uni-forms-item>
				</uni-forms>
			</view>
			<!-- <view class="titleBox">
				<view class="line"></view>
				<view class="text">请扫描托盘标签</view>
			</view>
			<view class="abilityBtn">
				<image src="@/static/img/labelBtnIcon.png"></image>
			</view>
			<view class="tableBox">
				<span>扫描结果：</span><span></span>
			</view> -->
			<button class="button" type="primary" @click="goSubmit">搬运</button>
			<u-modal v-model="transport" :content="'确定要将该托盘从' + from.kaishiweizhi + '搬运到' + from.mudiweizhi + '吗？'" :show-title="false" show-cancel-button @confirm="confirm" @cancel="transport=false"></u-modal>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				from: {
					type: 1,
					kaishiweizhi: 'HC01',
					mudiweizhi: 'HC05',
				},
				rules: {
					kaishiweizhi: {
						rules: [{
							required: true,
							errorMessage: '请选择开始位置',
						}, ]
					},
					mudiweizhi: {
						rules: [{
							required: true,
							errorMessage: '请选择目的位置',
						}]
					}
				},
				types: [{
						name: 1,
						label: "入库",
					},
					{
						name: 2,
						label: "出库",
					},
					{
						name: 3,
						label: "其他",
					},
				],
				kaishiweizhi: [{
						name: 'HC01',
						label: 'HC01',
					},
					{
						name: 'HC02',
						label: 'HC02',
					},
					{
						name: 'HC03',
						label: 'HC03',
					},
					{
						name: 'HC04',
						label: 'HC04',
					},
				],
				mudiweizhi: [{
						name: 'HC05',
						label: 'HC05',
					},
					{
						name: 'HC06',
						label: 'HC06',
					},
					{
						name: 'HC07',
						label: 'HC07',
					},
					{
						name: 'HC08',
						label: 'HC08',
					},
					{
						name: 'HC09',
						label: 'HC09',
					},
					{
						name: 'HC10',
						label: 'HC10',
					},
				],
				transport: false,
			};
		},
		methods: {
			onLoad: function() {
				this.radioStartChange()
			},
			radioStartChange(){
				if(this.from.type == 3){
					return
				}
				let typeval = this.from.kaishiweizhi
				if(typeval == 'HC01' || typeval == 'HC02'){
					this.from.mudiweizhi = 'SSX1'
					this.mudiweizhi = [{
						name: 'SSX1',
						label: 'SSX1',//输送线口
					}]
				}else if(typeval == 'HC03' || typeval == 'HC04'){
					this.from.mudiweizhi = 'HC05'
					this.mudiweizhi = [{
						name: 'HC05',
						label: 'HC05',
					},{
						name: 'HC06',
						label: 'HC06',
					}]
				}
			},
			radioTypeChange(){
				let typeval = this.from.type
				if(typeval == 1){
					this.mudiweizhi = [{
						name: 'HC05',
						label: 'HC05',
					},{
						name: 'HC06',
						label: 'HC06',
					}]
					this.from.mudiweizhi = 'HC05'
					this.kaishiweizhi = [{
						name: 'HC01',
						label: 'HC01',
					},
					{
						name: 'HC02',
						label: 'HC02',
					},
					{
						name: 'HC03',
						label: 'HC03',
					},
					{
						name: 'HC04',
						label: 'HC04',
					},]
					this.from.kaishiweizhi = 'HC01'
				}else if(typeval == 2){
					this.mudiweizhi = [{
						name: 'HC07',
						label: 'HC07',
					},{
						name: 'HC08',
						label: 'HC08',
					},{
						name: 'HC09',
						label: 'HC09',
					},{
						name: 'HC10',
						label: 'HC10',
					}]
					this.from.mudiweizhi = 'HC07'
					this.kaishiweizhi = [{
						name: 'SSX1',
						label: 'SSX1',
					},]
					this.from.kaishiweizhi = 'SSX1'
				}else{
					this.mudiweizhi = [{
						name: 'HC01',
						label: 'HC01',
					},
					{
						name: 'HC02',
						label: 'HC02',
					},
					{
						name: 'HC03',
						label: 'HC03',
					},
					{
						name: 'HC04',
						label: 'HC04',
					},{
						name: 'HC05',
						label: 'HC05',
					},
					{
						name: 'HC06',
						label: 'HC06',
					},
					{
						name: 'HC07',
						label: 'HC07',
					},
					{
						name: 'HC08',
						label: 'HC08',
					},
					{
						name: 'HC09',
						label: 'HC09',
					},
					{
						name: 'HC10',
						label: 'HC10',
					},{
						name: 'SSX1',
						label: 'SSX1',//输送线口
					}]
					this.from.mudiweizhi = 'HC05'
					this.kaishiweizhi = [{
						name: 'HC01',
						label: 'HC01',
					},
					{
						name: 'HC02',
						label: 'HC02',
					},
					{
						name: 'HC03',
						label: 'HC03',
					},
					{
						name: 'HC04',
						label: 'HC04',
					},{
						name: 'HC05',
						label: 'HC05',
					},
					{
						name: 'HC06',
						label: 'HC06',
					},
					{
						name: 'HC07',
						label: 'HC07',
					},
					{
						name: 'HC08',
						label: 'HC08',
					},
					{
						name: 'HC09',
						label: 'HC09',
					},
					{
						name: 'HC10',
						label: 'HC10',
					},{
						name: 'SSX1',
						label: 'SSX1',//输送线口
					}]
					this.from.kaishiweizhi = 'HC01'
				}
			},
			goSubmit() {
				if (!this.from.type) {
					uni.showToast({
						icon: 'none',
						title: '请选择出入库类型'
					});
				} else {
					this.$refs.form.validate().then(res => {
						this.transport = true
					})
				}
			},
			confirm() {
				console.log(this.from)
				if (!this.from.type) {
					uni.showToast({
						icon: 'none',
						title: '请选择出入库类型'
					});
				}else if(!this.from.kaishiweizhi){
					uni.showToast({
						icon: 'none',
						title: '请选择开始位置'
					});
				}else if(!this.from.mudiweizhi){
					uni.showToast({
						icon: 'none',
						title: '请选择目的位置'
					});
				}else{
					if(this.from.type == 3 && this.from.kaishiweizhi == this.from.mudiweizhi){
						uni.showToast({
							icon: 'none',
							title: '开始位置和目的位置不能一样'
						});
						return;
					}
					this.subform = {
						type :this.from.type,
						startPoint:this.from.kaishiweizhi,
						endPoint:this.from.mudiweizhi
					}
					this.$http.postAction('/wms/wcs/agvSlim', this.subform).then(res => {
						this.$modal.msgSuccess(res.msg)
					})
				}
			},
		}
	}
</script>

<style lang="scss">
	.pallet-hand {
		background: #F5F5F5;

		.cont {
			.radioBox {
				padding: 30rpx;
				background-color: #fff;
				margin-top: 10rpx;
				display: flex;
				justify-content: space-between;

				.title {
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #0B0B0B;
				}
			}

			.from-box {
				margin-top: 30rpx;
				padding: 45rpx 20rpx;
				background: #fff;
			}

			.titleBox {
				padding: 45rpx 20rpx;
				display: flex;
				align-items: center;

				.line {
					width: 6rpx;
					height: 36rpx;
					background: #1948B2;
					border-radius: 3rpx;
				}

				.text {
					font-size: 36rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #1948B2;
					margin-left: 10rpx;
				}
			}

			.abilityBtn {
				padding-top: 45rpx;
				background: #FFFFFF;
				display: flex;
				justify-content: space-around;

				image {
					width: 255rpx;
					height: 255rpx;
				}
			}

			.tableBox {
				padding: 16rpx;
				background: #FFFFFF;

				/deep/.u-th {
					background: linear-gradient(180deg, rgba(10, 133, 213, 0.85), rgba(1, 87, 174, 0.85));
					opacity: 0.98;
					font-size: 31rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #FFFFFF;
					text-shadow: 0rpx 2rpx 5rpx rgba(0, 57, 113, 0.35);
				}

				/deep/ .u-td {
					background: #F4F6F7;
					font-size: 33rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #0B0B0B;
				}
			}

			.button {
				margin: 30rpx 30rpx;
				position: absolute;
				width: 92%;
				bottom: 10rpx;
			}
		}
	}
</style>