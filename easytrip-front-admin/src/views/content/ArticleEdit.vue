<template>
  <div>
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="60%"
      @close="dialogConfig.show = false"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        label-width="80px"
        @submit.prevent
      >
        <el-form-item label="标题" prop="title">
          <el-input
            clearable
            placeholder="请输入标题"
            v-model.trim="formData.title"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="封面"
          prop="cover"
        >
          <CoverUpload
            v-model="formData.cover"
            :width="330"
            :height="180"
            :type="2"
          ></CoverUpload>
        </el-form-item>
        <el-form-item label="问题" prop="content">
          <Suneditor v-model="formData.content"></Suneditor>
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>
<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();

const api = {
  saveArticle: "/tripArticle/saveArticleInfo",
};

const dialogConfig = ref({
  show: false,
  title: "编辑问题",
  buttons: [
    {
      type: "primary",
      text: "确定",
      click: (e) => {
        submitForm();
      },
    },
  ],
});

const formData = ref({});
const formDataRef = ref();
const rules = {
  title: [{ required: true, message: "请输入标题" }],
  coverType: [{ required: true, message: "请选择封面类型" }],
  cover: [{ required: true, message: "请选择封面" }],
  content: [{ required: true, message: "请输入内容" }],
};

const showEditArticle = (data) => {
  dialogConfig.value.show = true;
  nextTick(() => {
    formDataRef.value.resetFields();
    formData.value = Object.assign({}, data || {});
  });
};

defineExpose({
  showEditArticle,
});

const emit = defineEmits(["reload"]);
const submitForm = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: api.saveArticle,
      params,
    });
    if (!result) {
      return;
    }
    dialogConfig.value.show = false;
    proxy.Message.success("保存成功");
    emit("reload");
  });
};
</script>
<style lang="scss" scoped>
</style>
