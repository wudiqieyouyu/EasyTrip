<template>
  <div>
    <el-image :url="imgUrl"></el-image>

  </div>
</template>

<script setup>
import {ref,reactive, getCurrentInstance} from "vue";
import { useRouter ,useRoute} from "vue-router";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const props =defineProps({
    imgUrl:{
        type:String,
    },
})
</script>

<style lang="scss" scoped>
</style>
