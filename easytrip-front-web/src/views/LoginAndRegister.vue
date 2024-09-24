<template>
  <div>
  <Dialog
    :show="dialogConfig.show"
    :title="dialogConfig.title"
    :buttons = "dialogConfig.buttons"
    width = "400px"
    :showCancel = "false"
    @close="closeDialog"
  >
   <el-form
    class="login-register"
     :model="formData"
     :rules="rules"
     ref="formDataRef"
     @submit.prevent
   >
   <!--input输入-->
     <el-form-item  prop="phone" >
       <el-input size="large" clearable placeholder="请输入手机号" v-model.trim="formData.phone">
      <template #prefix>
        <span class="iconfont icon-account"></span>
      </template>
      </el-input>
     </el-form-item>
     
     <!--登录的密码,需要进行加密处理-->
     <el-form-item  prop="password" v-if="optype==1">
       <el-input :type="passwordEyeType.passwordEyeOpen?'text':'password'" size="large"  placeholder="请输入密码" v-model.trim="formData.password">
      <template #prefix>
        <span class="iconfont icon-password"></span>
      </template>
      <template #suffix>
        <div class="cursor"><span @click="eyeChange('passwordEyeOpen')" :class="['iconfont',passwordEyeType.passwordEyeOpen?'icon-eye':'icon-close-eye']"></span></div>
      </template>
      </el-input>
     </el-form-item>
     <!--注册模块-->
     <div v-if="optype==0">
      <el-form-item  prop="nickName" >
       <el-input size="large" clearable placeholder="请输入昵称" v-model.trim="formData.nickName">
      <template #prefix>
        <span class="iconfont icon-account"></span>
      </template>
      </el-input>
     </el-form-item>
     </div>
     <el-form-item  prop="registerPassword" v-if="optype == 0||optype ==2" >
       <el-input :type="passwordEyeType.registerPasswordEyeOpen?'text':'password'" size="large"  placeholder="请输入密码" v-model="formData.registerPassword" >
      <template #prefix>
        <span class="iconfont icon-password"></span>
      </template>
      <template #suffix>
        <div class="cursor"><span @click="eyeChange('registerPasswordEyeOpen')" :class="['iconfont',passwordEyeType.registerPasswordEyeOpen?'icon-eye':'icon-close-eye']"></span></div>
      </template>
      </el-input>
     </el-form-item>

      <el-form-item  prop="rePassword "  v-if="optype == 0||optype ==2">
       <el-input :type="passwordEyeType.rePasswordEyeOpen?'text':'password'" size="large"  placeholder="请再次输入密码" v-model="formData.rePassword">
      <template #prefix>
        <span class="iconfont icon-password"></span>
      </template>
      <template #suffix>
        <div class="cursor"><span @click="eyeChange('rePasswordEyeOpen')" :class="['iconfont',passwordEyeType.rePasswordEyeOpen?'icon-eye':'icon-close-eye']"></span></div>
      </template>
      </el-input>
     </el-form-item>
     <el-form-item  prop="checkCode" v-if="optype==1">
      <div class = "check-code-panel">
       <el-input size="large" clearable placeholder="请输入验证码" v-model.trim="formData.checkCode" @keyuo.enter="doSubmit">
      <template #prefix>
        <span class="iconfont icon-checkcode"></span>
      </template>
      </el-input>
      <img :src = "checkCodeUrl" class="check-code" @click="changeCheckCode(1,uuid.valueOf())">
    </div>
     </el-form-item>

     <el-form-item  prop="phoneCode" v-if="optype==0||optype==2" >
      <div class = "send-phoneCode-panel">
       <el-input size="large" clearable placeholder="请输入验证码" v-model.trim="formData.phoneCode">
      <template #prefix>
        <span class="iconfont icon-checkcode"></span>
      </template>
      </el-input>
     <el-button type="primary" size="large" class="send-phoneCode-btn" :disabled="countdown>0" @click="startCountdown(optype)">{{ countdown > 0 ? `${countdown} 秒后可用` : '获取验证码' }}</el-button>
    </div>
     </el-form-item>
     <el-form-item>
      <div class="no-account">
        <a href="javascript:void(0)" class="a-link" @click="showPanel(1)" v-if="optype==0||optype ==2">去登录</a>
        <a href="javascript:void(0)" class="a-link" @click="showPanel(2)" v-if="optype==0||optype ==1">忘记密码?</a>
        <a href="javascript:void(0)" class="a-link" @click="showPanel(0)" v-if="optype==2||optype ==1">没有账号?</a>
      </div>
     </el-form-item>
     <el-form-item >
      <el-button type ="primary" class="op-btn" @click="doSubmit">
          <span v-if="optype==0">注册</span>
          <span v-if="optype==1">登录</span>
          <span v-if="optype==2">找回密码</span>
        </el-button>
     </el-form-item>
   </el-form>
  </Dialog>
  </div>
</template>

<script setup>
import {ref,reactive, getCurrentInstance, nextTick} from "vue";
import { useRouter ,useRoute} from "vue-router";
import { v4 as uuidv4 } from 'uuid'
import md5 from "js-md5";
import { LOCAL_STORAGE_KEY } from "@/utils/Constants.js";
import { useStore } from "vuex";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const store = useStore();
const api = {
  checkCode:"/api/account/checkCode",
  sendPhoneCode:"/account/sendPhoneCode",
  register:"/account/register",
  login:"/account/login",
  resetPwd:"/account/resetPwd",
  getUserInfo:"/account/getUserInfo",
}

//生成uuid
const uuid = ref("");
uuid.value = uuidv4();;

//0: 注册 1:登录 2:找回密码
const optype =ref();
const showPanel = (type)=>{
    optype.value = type;
    resetForm();
    if (type==1) {
       changeCheckCode(1,uuid.value);
    }
}
//验证码
const checkCodeUrl = ref();
const changeCheckCode=(type,uuid)=>{
  if (type == 1) {
    checkCodeUrl.value =
      api.checkCode + "?type=" + type +"&uuid="+uuid+ "&time=" + new Date().getTime();
  }
}
defineExpose({showPanel})

//手机验证码按钮倒计时
const countdown = ref(0);

const startCountdown = (type) => {
  formDataRef.value.validateField('phone',(valid)=>{
    if(!valid){
      return;
    }
    if (countdown.value === 0) {
    countdown.value = 60; // 设置倒计时秒数
    const timer = setInterval(() => {
      countdown.value -= 1;
      if (countdown.value === 0) {
        clearInterval(timer);
      }
    }, 1000);
      sendPhoneCode(type,formData.value.phone);
  }
     
  })
};
//发送手机短信验证码
const sendPhoneCode=async(type,phoneNumber)=>{
  let result = await proxy.Request({
    url:api.sendPhoneCode,
    params:{
        type:type,
        phoneNumber:phoneNumber
    }
  });
  if (!result) {
    return;
  }
  proxy.Message.success("验证码发送成功,请注意接收短信");
};
//密码显示隐藏操作
const passwordEyeType = reactive({
  passwordEyeOpen:false,
  registerPasswordEyeOpen:false,
  rePasswordEyeOpen:false,
});
const eyeChange = (type)=>{
    passwordEyeType[type] = !passwordEyeType[type]
}

const dialogConfig = reactive({
  show: false,
  title: "标题",
});
const checkRePassword = (rule,value,callback)=>{
  if (value!==formData.value.registerPassword) {
    callback(new Error(rule.message))
  }else{
    callback();
  }
}
const formData = ref({});
const formDataRef = ref();
const rules = {
  phone: [{ required: true, message: "请输入手机号" },
  {max:30,message:"手机号过长"},
  {validator:proxy.Verify.phone,message:"请输入正确的手机号"},
],
  password:[{required:true,message:"请输入密码"},
  {max:50,message:"密码过长"},
  {validator:proxy.Verify.password,message:"请输入正确的密码"},
  ],
  phoneCode:[{required: true,message:"请输入手机验证码"}
],
  checkCode:[{required: true,message:"请输入图片验证码"}
],
  nickName:[{required: true,message:"请输入昵称"},
  {max:20,message:"昵称过长"},
],
  registerPassword:[{required:true,message:"请输入密码"},
  {max:50,message:"密码过长"},
  {validator:proxy.Verify.password,message:"密码只能是数字,字母,特殊字符,8-18位"},
  ],
  rePassword:[{required:true,message:"请再次输入密码"},
  {max:50,message:"密码过长"},
  {validator:checkRePassword,message:"两次输入密码不一致"},
  ],
};

//重置表单
const resetForm = ()=>{
  dialogConfig.show=true;
  if (optype.value==0) {
    dialogConfig.title="注册";
  }else if (optype.value==1) {
    dialogConfig.title="登录";
  }else if (optype.value==2){
    dialogConfig.title="找回密码"
  }
  nextTick(()=>{
    changeCheckCode(0,uuid.valueOf())
    formData.value = {};
    formDataRef.value.resetFields();
  })
}
//登录注册找回密码
const doSubmit=()=>{
  formDataRef.value.validate(async (valid)=>{
    if (!valid) {
      return;
    }
 
  let params={};
  let url = null;
  Object.assign(params,formData.value);
  //注册
  if (optype.value==0) {
    params.password=params.registerPassword;
    params.phoneNumber = params.phone
    delete params.registerPassword;
    delete params.rePassword;
    delete params.phone;
    url = api.register
  }
  if (optype.value==1) {
    url =api.login
    params.password = md5(params.password);
    params.phoneNumber=params.phone;
    params.uuid=uuid.value;
    delete params.phone;
  }
  if (optype.value==2) {
    url=api.resetPwd
    params.newPassword=params.registerPassword;
    params.phoneNumber = params.phone
    delete params.registerPassword;
    delete params.rePassword;
    delete params.phone;
  }
  
  let result = await proxy.Request({
    url:url,
    params:params,
    errorCallback:()=>{
      if (optype.value==1) {
        changeCheckCode(1,uuid.value);
      }
    }
  })

  if (!result) {
    return
  }
  //注册返回
  if (optype.value==0) {
    proxy.Message.success("注册成功,请登录");
    showPanel(1);
  }else if (optype.value==1) {
    proxy.Message.success("登录成功!");
    localStorage.setItem(LOCAL_STORAGE_KEY.token.key, result.data);
    let getUserResult = await proxy.Request({
    url:api.getUserInfo,
  })
    store.commit("updateLoginUserInfo",getUserResult.data);
    dialogConfig.show=false;
  }else if (optype.value==2) {
    proxy.Message.success("找回密码成功");
    showPanel(1);
  }
 });
}

const closeDialog=()=>{
  dialogConfig.show=false
  store.commit("showLogin",false);
}
</script>

<style lang="scss" scoped>
.login-register{
  .cursor{
    cursor: pointer;
  }
  .send-phoneCode-panel{
    display: flex;
    width: 100%;
    justify-content: space-between;
    .send-phoneCode-btn{
      margin-left:5px;

    }
  }
  .check-code-panel{
    display: flex;
    .check-code{
      margin-left: 5px; 
      cursor: pointer;
    }
  }
  .no-account{
    width: 100%;
    display: flex;
    justify-content: space-between;
  }
  .op-btn{
    width:100%
  }
}

</style>
