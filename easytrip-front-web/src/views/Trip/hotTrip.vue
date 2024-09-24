<template>
    <div class="sale"  
      :style="{ width: width + 'px', height: width + 'px', 'border-radius': width/2 + 'px' }"
        >
      <el-image :src="proxy.globalInfo.imageUrl + imgUrl"></el-image>
      <div class="desc">
        <h3>{{ salesName }}</h3>
      </div>
      <div class="price-number">
        <span class="price">¥{{ price }}起</span>
        <span class="number">已售{{ salesNumber }}</span>
      </div>
    </div>
  </template>
  
  <script setup>
  import {ref,reactive, getCurrentInstance} from "vue";
  import { useRouter ,useRoute} from "vue-router";
  const{proxy} = getCurrentInstance();
  const router = useRouter();
  const route = useRoute();
  
  const props = defineProps({
    salesId:{
      type:String,
    },
    imgUrl: {
      type: String,
    },
    salesName: {
      type: String,
    },
    price: {
      type: String,
    },
    salesNumber: {
      type: String,
    },
    width: {
      type: Number,
      default: 60,
    },
  });

  </script>
  
  <style lang="scss" scoped>
  .sale {
    float: left;
    height: 300px;
    display: inline-block;
    margin: 15px 30px 15px 0;
    position: relative;
    background-color: #FFFF;
    cursor: pointer;
  }
  
  .desc {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    padding: 10px;
    background-color: #f9f9f9;
    overflow: hidden;
    height: 83px;
    opacity: 0.8;
    transition: height 0.3s ease;
  }
  .price-number{
    position: absolute;
    bottom: 0;
    left: 0;
    padding: 10px;
    margin-bottom: -30px; /* 添加底部间距，防止与 .desc 重叠 */
    .price{
        display: inline-block;
        color: #ff8a00;
        font-weight:bold;
        font-size: 22px;
    }
    .number{
        float: right;
    padding-top: 8px;
    font-size: 12px;
    color: #666;
    max-width: 120px;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap
    }
  }
  
  .sale:hover .desc {
    height: auto;
    opacity: 1;
  }
  </style>
  