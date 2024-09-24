<template>
   <div>
    <h1>购买成功！您的订单号为{{ route.params.orderId }}</h1>
    <p>即将跳转到旅游攻略页面...{{ countdown }}秒</p>
  </div>
</template>

<script setup>
import {ref,reactive, getCurrentInstance} from "vue";
import { useRouter ,useRoute} from "vue-router";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const countdown = ref(5); // 倒计时初始值为5秒

// 定义倒计时函数
const startCountdown = () => {
  // 使用 setInterval 每秒更新倒计时值
  const timer = setInterval(() => {
    countdown.value--; // 每秒减1
    if (countdown.value === 0) {
      clearInterval(timer); // 当倒计时为0时，清除定时器
      redirectToOrderPage(); // 跳转到订单页面
    }
  }, 1000); // 每1000毫秒即1秒执行一次
}

// 购买成功后的跳转函数
const redirectToOrderPage = () => {
  router.push('/tips'); 
}
startCountdown();
</script>

<style lang="scss" scoped>
</style>
