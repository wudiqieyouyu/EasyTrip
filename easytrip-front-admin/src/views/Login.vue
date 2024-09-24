<template>
  <div class="login-body">
    <div class="bg"></div>
    <div class="login-panel">
      <el-form
        class="login-register"
        :model="formData"
        :rules="rules"
        ref="formDataRef"
      >
        <div class="login-title">EasyTrip管理后台</div>
        <!--input输入-->
        <el-form-item prop="phone">
          <el-input
            size="large"
            clearable
            placeholder="请输入手机号"
            v-model.trim="formData.phone"
            maxLength="11"
          >
            <template #prefix>
              <span class="iconfont icon-phone"></span>
            </template>
          </el-input>
        </el-form-item>
        <!--登录密码-->
        <el-form-item prop="password">
          <el-input
            type="password"
            size="large"
            placeholder="请输入密码"
            v-model.trim="formData.password"
            show-password
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="checkCode">
          <div class="check-code-panel">
            <el-input
              size="large"
              placeholder="请输入验证码"
              v-model.trim="formData.checkCode"
              @keyup.enter="doSubmit"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img
              :src="checkCodeUrl"
              class="check-code"
              @click="changeCheckCode(0)"
            />
          </div>
        </el-form-item>
        <el-form-item>
          <div class="rememberme-panel">
            <el-checkbox v-model="formData.rememberMe">记住我</el-checkbox>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="op-btn"
            @click="doSubmit"
            size="large"
          >
            <span>登录</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import md5 from "js-md5";

const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const api = {
  checkCode: "/api/checkCode",
  login: "/login",
};

const checkRePassword = (rule, value, callback) => {
  if (value !== formData.value.registerPassword) {
    callback(new Error(rule.message));
  } else {
    callback();
  }
};
const formData = ref({});
const formDataRef = ref();
const rules = {
  phone: [{ required: true, message: "请输入手机号" }],
  password: [{ required: true, message: "请输入密码" }],
  checkCode: [{ required: true, message: "请输入图片验证码" }],
};
//验证码
const checkCodeUrl = ref(null);
const changeCheckCode = () => {
  checkCodeUrl.value = `${api.checkCode}?&time=${new Date().getTime()}`;
};

onMounted(() => {
  init();
});
//重置表单
const init = () => {
  nextTick(() => {
    changeCheckCode();
    formDataRef.value.resetFields();
    formData.value = {};
    const cookieLoginInfo = proxy.VueCookies.get("loginInfo");
    if (cookieLoginInfo) {
      formData.value = cookieLoginInfo;
    }
  });
};

// 登录、注册、重置密码  提交表单
const doSubmit = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    //登录
    let cookieLoginInfo = proxy.VueCookies.get("loginInfo");
    let cookiePassword =
      cookieLoginInfo == null ? null : cookieLoginInfo.password;
    if (params.password !== cookiePassword) {
      params.password = md5(params.password);
    }
    let url = api.login;
    let result = await proxy.Request({
      url: url,
      params: params,
      errorCallback: () => {
        changeCheckCode(0);
      },
    });
    if (!result) {
      return;
    }
    //登录
    if (params.rememberMe) {
      const loginInfo = {
        phone: params.phone,
        password: params.password,
        rememberMe: params.rememberMe,
      };
      proxy.VueCookies.set("loginInfo", loginInfo, "7d");
    } else {
      proxy.VueCookies.remove("loginInfo");
    }
    proxy.Message.success("登录成功");
    sessionStorage.setItem("userInfo", JSON.stringify(result.data));
    //重定向到原始页面
    let firstMenu = result.data.menuList[0];
    if (firstMenu.children.length > 0) {
      firstMenu = firstMenu.children[0];
    }
    const redirectUrl = route.query.redirectUrl || firstMenu.menuUrl;
    router.push(redirectUrl);
  });
};
</script>

<style lang="scss" scoped>
.login-body {
  height: calc(100vh);
  background-size: cover;
  background: url("../assets/login_bg.jpg");
  display: flex;
  .bg {
    flex: 1;
    background-size: cover;
    background-position: center;
    background-size: 800px;
    background-repeat: no-repeat;
    background-image: url("../assets/login_img.png");
  }
  .login-panel {
    width: 430px;
    margin-right: 15%;
    margin-top: calc((100vh - 500px) / 2);
    .login-register {
      padding: 25px;
      background: #fff;
      border-radius: 5px;
      .login-title {
        text-align: center;
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 20px;
      }
      .send-emali-panel {
        display: flex;
        width: 100%;
        justify-content: space-between;
        .send-mail-btn {
          margin-left: 5px;
        }
      }
      .rememberme-panel {
        width: 100%;
      }
      .no-account {
        width: 100%;
        display: flex;
        justify-content: space-between;
      }
      .op-btn {
        width: 100%;
      }
    }
  }

  .check-code-panel {
    width: 100%;
    display: flex;
    .check-code {
      margin-left: 5px;
      cursor: pointer;
    }
  }
}
</style>