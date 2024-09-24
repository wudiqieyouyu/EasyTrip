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
          <el-form-item label="景区名" prop="title">
            <el-input
              clearable
              placeholder="请输入名称"
              v-model.trim="formData.salesName"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="封面"
            prop="cover"
          >
            <CoverUpload
              v-model="formData.imgCover"
              :width="330"
              :height="180"
              :type="1"
            ></CoverUpload>
          </el-form-item>
          <!-- <el-form-item label="问题" prop="content">
            <Suneditor v-model="formData.content"></Suneditor>
          </el-form-item> -->
          <el-form-item label="地址" prop="address">
            <el-input
              clearable
              placeholder="请输入地址（经纬度）"
              v-model.trim="formData.addressCode"
            ></el-input>
          </el-form-item>

          <el-form-item label="手机号" prop="tel">
            <el-input
              clearable
              placeholder="请输入手机号"
              v-model.trim="formData.tel"
            ></el-input>
          </el-form-item>
        </el-form>
      </Dialog>
    </div>
  </template>
  <script setup>
  import { ref, reactive, getCurrentInstance, nextTick } from "vue";
  const { proxy } = getCurrentInstance();
  
  const api = {
    saveSales: "/sales/saveSales",
  };
  
  const dialogConfig = ref({
    show: false,
    title: "编辑景点",
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
    salesName: [{ required: true, message: "请输入标题" }],
    imgCover: [{ required: true, message: "请选择封面" }],
    addressCode: [{ required: true, message: "请输入经纬度" }],
  };
  
  const showEditSales = (data) => {
    dialogConfig.value.show = true;
    nextTick(() => {
      formDataRef.value.resetFields();
      formData.value = Object.assign({}, data || {});
    });
  };
  
  defineExpose({
    showEditSales,
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
        url: api.saveSales,
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
  