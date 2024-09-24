<template>
  <Dialog
    :title="dialogConfig.title"
    :buttons="dialogConfig.buttons"
    :show="dialogConfig.show"
    @close="dialogConfig.show = false"
    :width="'400px'"
  >
    <el-form
      ref="formDataRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="用户名">
        {{ formData.userName }}
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          type="password"
          :maxLength="18"
          v-model="formData.password"
          show-password
        />
      </el-form-item>
      <el-form-item label="重复密码" prop="rePassword">
        <el-input
          type="password"
          :maxLength="20"
          v-model="formData.rePassword"
          show-password
        />
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();

const api = {
  updatePassword: "/settings/updatePassword",
};

const dialogConfig = ref({
  title: "修改密码",
  buttons: [
    {
      type: "primary",
      text: "确定",
      click: (e) => {
        submitForm(e);
      },
    },
  ],
  show: false,
});

const validateRePass = (rule, value, callback) => {
  if (value !== formData.value.password) {
    callback(new Error(rule.message));
  } else {
    callback();
  }
};

const formData = ref({});
const formDataRef = ref();
const rules = {
  password: [
    { required: true, message: "请输入密码" },
    {
      validator: proxy.Verify.password,
      message: "密码最少8位，只能数字字母和特殊字符",
    },
  ],
  rePassword: [
    { required: true, message: "请再次输入密码" },
    {
      validator: validateRePass,
      message: "两次输入的密码不一致",
    },
  ],
};

const showEdit = async (data) => {
  dialogConfig.value.show = true;
  nextTick(() => {
    formDataRef.value.resetFields();
    formData.value = {
      userName: data.userName,
      userId: data.userId,
      roles: [],
    };
  });
};

defineExpose({
  showEdit,
});
const submitForm = async () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return false;
    }
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: api.updatePassword,
      params: params,
    });
    if (!result) {
      return;
    }
    dialogConfig.value.show = false;
    proxy.Message.success("修改成功");
  });
};
</script>

<style lang="scss" scoped>
</style>
