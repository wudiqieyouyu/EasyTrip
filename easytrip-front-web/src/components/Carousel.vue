<template>
    <div class="block text-center">
      <el-carousel
        :height="height"
        :width="width"
        :autoplay="autoplay"
        :interval="interval"
        :indicator-position="position"
        @change="handleCarouselChange"
      >
        <el-carousel-item v-for="item in imgDetail" :key="imgSrc">
          <div class="carousel-item">
            <el-image  :src="api.getImage + item.imgPath" class="picture" alt="carousel-image" @click="() => toLink(objTypeList[currentIndex], linkList[currentIndex],objectIdList[currentIndex])"></el-image>
          <!-- <div style="width:100%;height:100px;position:absolute;top:150px" >
          <span style="color:white;font-size:40px;line-height:100px">{{ item.text }}</span>
          </div> -->
          </div>

        </el-carousel-item>
      </el-carousel>
    </div>
  </template>
  
  <script setup>
  import { ref, defineProps, watch,reactive } from "vue";
  import { useRouter } from "vue-router";
  
  const router = useRouter();
  const api = {
    getImage: "/api/file/getImage/",
  };
  
  const props = defineProps({
    height: {
      type: String,
      default: "500px",
    },
    width: {
      type: String,
      default: "500px",
    },
    autoplay: {
      type: Boolean,
      default: true,
    },
    interval: {
      type: Number,
      default: 3000,
    },
    postion: {
      type: String,
      default: "none",
    },
    imgDetail: {
      type: Array,
      default: () => [],
    },
  });
  
  const imgSrcList = ref([]);
  const objTypeList = ref([]); 
  const linkList = ref([]);
  const objectIdList = ref([]);
  
  //跳转页面
  const toLink = (objType, link,objectId) => {
    console.log(objType);
    if (objType === 3) {
        window.open(link, '_blank');
    } else if (objType===0) {
        router.push(`/article/${objectId}`)
    } else{
      return;
    }
  };
  
  watch(
    () => props.imgDetail,
    (newVal) => {
      imgSrcList.value = newVal.map((item) => item.imgPath);
      objTypeList.value = newVal.map((item) => item.objectType);
      linkList.value = newVal.map((item) => item.outerLink);
      objectIdList.value  = newVal.map((item)=>item.objectId);
      console.log(objectIdList.value)
    },
    { immediate: true }
  );
  
  // 处理轮播图切换事件
  const currentIndex = ref(0);
  const handleCarouselChange = (index) => {
    currentIndex.value = index;
  };
  </script>
  
  <style lang="scss" scoped>
  .text-center {
    text-align: center;
  }
  
  .carousel-item {
    width: 100%;
    height: 100%;
    overflow: hidden;
    text-align: center;
  }
  
  .picture {
    width: 100%;
    height: 100%;
    object-fit: cover;
    cursor: pointer;
  }
  </style>
  