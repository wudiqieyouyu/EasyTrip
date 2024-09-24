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
          <el-form-item label="线路名称" prop="title">
            <el-input
              clearable
              placeholder="请输入线路名称"
              v-model.trim="formData.lineName"
            ></el-input>
          </el-form-item>
 
          <!-- <el-form-item label="问题" prop="content">
            <Suneditor v-model="formData.content"></Suneditor>
          </el-form-item> -->
          <el-form-item label="线路描述" prop="description">
            <el-input
              clearable
              placeholder="请输入线路描述"
              v-model.trim="formData.description"
            ></el-input>
          </el-form-item>

            <!--input输入-->
          <el-form-item label="途经点" >
              <div
              v-if="formData.salesList"
                class="question-item"
                v-for="(item, index) in formData.salesList"
              >
                <div class="letter">{{ LETTER[index] }}、</div>
                <div class="title">
                  <el-form-item
                    label-width="0px"
                    :key="index"
                    :prop="'salesList.' + index + '.title'"
                  >
                    <el-input
                      clearable
                      placeholder="请输入景点Id"
                      v-model="item.salesId"
                    ></el-input>
                  </el-form-item>
                </div>
                <div class="op">
                  <span
                    class="iconfont icon-add"
                    v-if="index == 0"
                    style="cursor: pointer;"
                    @click="addQuestionItem"
                  ></span>
                  <span
                    class="iconfont icon-reduce"
                    v-if="index > 0"
                    style="cursor: pointer;"
                    @click="reduceSaleItem(index)"
                  ></span>
                </div>

              </div>
              <div
                class="question-item"
                v-else
              >
                <div class="title">
                  <el-form-item
                    label-width="0px"
                  >
                    <el-input
                      clearable
                      placeholder="请输入景点Id集合，例如1023,1024"
                      v-model="formData.salesString"
                    ></el-input>
                  </el-form-item>
                </div>
              </div>
              
              
            </el-form-item>

          <el-form-item label="地区" prop="regionName">
            <el-input
              clearable
              placeholder="请输入地区名字"
              v-model.trim="formData.regionName"
            ></el-input>
          </el-form-item>
        </el-form>
      </Dialog>
    </div>
  </template>
  <script setup>
  import { ref, reactive, getCurrentInstance, nextTick } from "vue";
  import { LETTER} from "@/utils/Constants.js";
  const { proxy } = getCurrentInstance();
  
  const api = {
    saveLine: "/line/saveLine",
  };
  
  const dialogConfig = ref({
    show: false,
    title: "编辑线路",
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
  
  const formData = ref({
  });
  const formDataRef = ref();
  const rules = {
    lineName: [{ required: true, message: "请输入标题" }],
    salesId: [{ required: true, message: "请输入经纬度" }],
  };
  
  const showEditLine = (data) => {
    dialogConfig.value.show = true;
    nextTick(() => {
      formDataRef.value.resetFields();
      formData.value = Object.assign({}, data || {});
    });
  };
  
  defineExpose({
    showEditLine,
  });
  
  const emit = defineEmits(["reload"]);
  const submitForm = () => {
    formDataRef.value.validate(async (valid) => {
      if (!valid) {
        return;
      }
      let params = {};
      Object.assign(params, formData.value);
      console.log(params.salesList)
      params.salesIdList = params.salesList.map(sales=>sales.salesId)
       params.salesString =params.salesIdList.sort().join(",");
       delete params.salesIdList;
       delete params.salesList;
      let result = await proxy.Request({
        url: api.saveLine,
        params
      });
      if (!result) {
        return;
      }
      dialogConfig.value.show = false;
      proxy.Message.success("保存成功");
      emit("reload");
    });
  };

const addQuestionItem = () => {
  if (formData.value.salesList.length >= 26) {
    proxy.Message.success("最多可以添加26个选项");
    return;
  }
  formData.value.salesList.push({
    title: "",
    sort: formData.value.salesList.length + 1,
  });
};
const reduceSaleItem = (index) => {
  formData.value.salesList.splice(index, 1);
};

  </script>
  <style lang="scss" >
  .question-item{
    width: 100%;
  display: flex;
       .title {
    flex: 1;
    .el-form-item {
      margin-bottom: 18px;
    }
  }
  .letter {
    width: 20px;
  }
  }
 
  </style>
  