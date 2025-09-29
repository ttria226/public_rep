<template>
  <el-dialog title="分派" :visible.sync="open" width="50%" append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" style="padding-right: 30px">
      <el-form-item label="所属部门" prop="companyId">
        <treeselect v-model="form.companyId" :options="deptOptions" :normalizer="normalizer" placeholder="选择所属部门" @select="getPerson" />
      </el-form-item>
      <el-form-item label="执行人" prop="executorId">
        <el-select v-model="form.executorId" filterable placeholder="请选择执行人" clearable style="width: 100%;" @change="handlePersonChange">
          <el-option v-for="item in personList" :key="item.userId" :label="item.userName" :value="item.userId"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">分派</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { listDept } from "@/api/system/dept";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

import { getAction } from "@/api/manage"
export default {
  name: "ChooseSendPerson",
  components: { Treeselect },
  data(){
    return{
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        companyId: [{ required: true, message: '请选择所属部门', trigger: 'change' }],
        executorId: [{ required: true, message: '请选择执行人', trigger: 'change' }],
      },

      deptOptions: [], //部门列表
      personList: [], //申请人列表
    }
  },
  watch: {
    open(val){
      if(val){
        this.reset()
        this.getListDept()
      }
    }
  },
  methods: {
    // 获取申请人列表
    getPerson(node) {
      this.form.executorId = null
      this.form.executorName = null
      this.form.companyName = node.deptName
      getAction('system/user/list', { deptId: node.deptId }).then(res => {
        console.log(res)
        if (res.code == 200) {
          this.personList = res.rows
        } else {
          this.$modal.msgError(res.msg);
          this.personList = []
        }
      })
    },
    // 构造树形结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    // 获取部门数据
    getListDept() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      })
    },
    // 处理分派人选择
    handlePersonChange(val){
      if(val){
        let info = this.personList.find(item => { return item.userId === val })
        if(info){
          this.form.executorName = info.userName
        } else {
          this.form.executorName = null
        }
      } else {
        this.form.executorId = null
        this.form.executorName = null
      }
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        companyId: null,
        companyName: null,
        executorId: null,
        executorName: null
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$emit("setSendPerson",this.form)
        }
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