<template>
  <div class="container">
    <div class="background-mv">
      <video autoplay loop muted> 
        <source src="../../assets/maldivesVideo.mp4"/>
      </video>
      <!-- 原视频色彩比较鲜艳，不适合直接作背景，想要的效果像是用一层淡黑色的幕布蒙在上面 -->
      <div class="overlay"></div>
    </div> 

    <!-- 搜索框和选择 Radio -->
    <div class="search-container">
      <div class="search-group">
        <el-radio-group v-model="type" >
          <el-radio :label="1">目的地</el-radio>
          <el-radio :label="2">跟团游</el-radio>
          <el-radio :label="3">自驾游</el-radio>
        </el-radio-group>
      </div>
      <div class="search-panel" v-if="type==1">
        <el-input placeholder="请输入目的地/产品名称" v-model="inputValue"></el-input>
        <el-button @click="search(inputValue)"><el-icon><Search /></el-icon></el-button>
      </div>
      <div class="search-panel" v-if="type==2">
        <el-cascader
        ref="cascader"
        v-model="selectedOptions"
        :options="regionOption"
        :props="regionParams"
        :show-all-levels="false"
        size="large"
        placeholder="请选择出发点"
        clearable></el-cascader>
        <el-icon color="white"><Right /></el-icon>
        <el-input placeholder="请输入目的地/产品名称" v-model="inputValue"></el-input>
        <el-button  @click="search(inputValue)"><el-icon><Search /></el-icon></el-button>
      </div>
      <div class="search-panel" v-if="type==3">
        <el-cascader
        ref="cascader"
        v-model="selectedOptions"
        :options="regionOption"
        :props="regionParams"
        :show-all-levels="false"
        size="large"
        placeholder="请选择出发点"
        clearable></el-cascader>
        <el-icon color="white"><Right /></el-icon>
        <el-input placeholder="请输入目的地/产品名称" v-model="inputValue" ></el-input>
        <el-button @click="search(inputValue)"><el-icon><Search /></el-icon></el-button>
      </div>
    </div>
    <div class="mod-hd">
      <h1>正在热卖</h1>
      <div v-for="item in SalesList " class="list"  @click="toSalesDetail(item.salesId)">
      <HotTrip
      :img-url="item.imgCover"
      :salesName="item.salesName"
      :price ="item.price"
      :salesNumber = "item.salesNumber"
      width="240"
      :salesId="item.salesId"
      > </HotTrip>

      </div>
      
    </div>

  </div>
</template>

<script setup>
import {ref,reactive, getCurrentInstance} from "vue";
import { useRouter ,useRoute} from "vue-router";
import HotTrip from "@/views/Trip/hotTrip.vue"
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const api = {
  loadRegion:"/region/loadRegion",
  loadSales:"/sales/loadSales",
}

const inputValue = ref("");
const type = ref(1); // 默认选择第一个选项

const regionParams = ref({
  label:"regionName",
  expandTrigger: 'hover',
  value:"regionId",
  children:'children',
  emitPath:false
})
const selectedOptions = ref()
const regionOption = ref([])
const loadRegion = async()=>{
    let result = await proxy.Request({
       url:api.loadRegion
    })
    if(!result){
      return;
    }
    regionOption.value = result.data
}
loadRegion();

const SalesList = ref([])
const loadSales = async()=>{
    let result = await proxy.Request({
       url:api.loadSales
    })
    if(!result){
      return;
    }
    SalesList.value = result.data.list
    console.log(SalesList)
}
loadSales();

const toSalesDetail=(salesId)=>{
    router.push(`/trip/detail/${salesId}`)
  }


const search=(query)=>{
  router.push(`/trip/${query}`)
}
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.background-mv {
  width: 100%;
  height: 100vh;
  position: relative;
  color: #fff;
  z-index: -1;

  video {
    width: 100%;
    height: 100%;
    object-fit: cover; // 超出部分被剪掉
  }
}

.overlay {
  background-color: rgba(0, 0, 0, 0.2);
  position: absolute;
  width: 100%;
  height: 100vh;
  top: 0;
  left: 0;
}

.search-container {
  position: absolute;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex; // 使用 Flex 布局
  flex-direction: column; // 垂直布局
  width:640px;
  background-color: rgba(0, 0, 0, .6);
  .search-group {
    margin-bottom: 15px; // 设置选择 Radio 和搜索框之间的间距
    display: flex; // 使用 Flex 布局
    justify-content: center; // 水平居中对齐
  }

  .search-panel {
    width: 100%;
    max-width: 1000px; // 设置最大宽度，避免在大屏幕设备上过宽
    display: flex; // 使用 Flex 布局
    align-items: center; // 垂直居中对齐
    padding:5px 5px;

    .el-input {
    flex: 1; // 输入框自动填充剩余空间
    height: 38px;
    margin-right: 10px; // 设置输入框和按钮之间的间距
    width: 800px; // 使输入框占满父容器的宽度
    }

    .el-button {
      height: 38px;
    }
  }
}
.mod-hd{
  width: 100%;
  display: block;
  float: left;
  padding: 0px 200px;
  background-color:#ffff ;
  .list{
    background-color:#FFFFFFFF ;
  }
}
</style>
