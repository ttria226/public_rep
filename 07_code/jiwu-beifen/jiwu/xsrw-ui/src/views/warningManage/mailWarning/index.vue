<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="预警类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择预警类型" clearable>
          <el-option
            v-for="dict in dict.type.warning_mail_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option value="0" label="启用"></el-option>
          <el-option value="1" label="禁用"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['stock:forewarning:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['stock:forewarning:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['stock:forewarning:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stock:forewarning:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="forewarningList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预警类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.warning_mail_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="接收人" align="center" prop="receiver" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '0' ">启用</span>
          <span v-if="scope.row.status == '1' ">禁用</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="receiver" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['stock:forewarning:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['stock:forewarning:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleEmail(scope.row)"
          >发送邮件</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改预警邮件配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="预警类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择预警类型">
            <el-option
              v-for="dict in dict.type.warning_mail_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接收人" prop="receiver">
          <el-select style="width: 100%;" v-model="form.receiver" filterable multiple placeholder="请选择接收人" clearable>
            <el-option v-for="item in personList" :key="item.userId" :label="item.userName" :value="item.userId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select style="width: 100%;" v-model="form.status" placeholder="请选择状态" @change="inputUpdate" class="select-input-form">
            <el-option value="0" label="启用"></el-option>
            <el-option value="1" label="禁用"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listForewarning, getForewarning, delForewarning, addForewarning, updateForewarning, sendEmail } from "@/api/wms/mailWarning";
  import { listUser } from "@/api/system/user";

  import { wms } from '@/utils/agent';

  export default {
    name: "Forewarning",
    dicts: ['warning_mail_type'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 预警邮件配置表格数据
        forewarningList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          type: null,
          receiver: null,
          status: null,
        },
        // 表单参数
        form: {
          status: '1'
        },
        // 表单校验
        rules: {
          type: [
            { required: true, message: "预警类型不能为空", trigger: "change" }
          ],
          status: [
            { required: true, message: "状态不能为空", trigger: "change" }
          ],
        },
        personList: [], //人员下拉列表
      };
    },
    created() {
      this.getList();
      this.getPerson();
    },
    methods: {
      /** 查询预警邮件配置列表 */
      getList() {
        this.loading = true;
        listForewarning(this.queryParams).then(response => {
          this.forewarningList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 更新输入框，部分输入框改变后视图不会同步更新时使用
      inputUpdate () {
        this.$forceUpdate()
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          type: null,
          receiver: null,
          status: null,
          remark: null,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          deptId: null,
          deptName: null,
          delFlag: null
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加预警邮件配置";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getForewarning(id).then(response => {
          this.form = response.data;
          let receiverList = this.form.receiver.split(",");
          let receiver = [];
          receiverList.forEach(item => {
            return receiver.push(Number(item));
          });
          this.form.receiver = receiver;
          this.open = true;
          this.title = "修改预警邮件配置";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if(this.form.receiver){
            this.form.receiver = this.form.receiver.join(",");
          }
          if (valid) {
            if (this.form.id != null) {
              updateForewarning(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addForewarning(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除预警邮件配置编号为"' + ids + '"的数据项？').then(function() {
          return delForewarning(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 发送邮件 */
      handleEmail(row){
        let param = {
          "type": row.type
        }
        sendEmail(param).then(response => {
          this.$modal.msgSuccess("发送成功");
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download(wms + '/stock/forewarning/export', {
          ...this.queryParams
        }, `forewarning_${new Date().getTime()}.xlsx`)
      },
      // 获取操作人员列表
      getPerson() {
        listUser({pageSize: 5000}).then(res => {
          if (res.code == 200) {
            this.personList = res.rows
          } else {
            this.$modal.msgError(res.msg);
          }
        })
      },
    }
  };
</script>
