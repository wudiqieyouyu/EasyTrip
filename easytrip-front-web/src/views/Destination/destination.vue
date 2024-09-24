<template>
    <div class="destination-page">

    <!-- 搜索框 -->
    <div class="search">
      <el-input type="text" placeholder="搜索目的地" style="width: 80%; height: 80%;" v-model="text"></el-input>
      <el-button style=" height: 80%;" @click="search"><span class="iconfont icon-search"></span></el-button>
    </div>

      <h1>热门目的地</h1>
      <div class="destination-grid">
        <div v-for="(destination, index) in destinations" :key="index" class="destination-card">
          <img :src="destination.image" alt="Destination Image" class="destination-image">
          <div class="destination-info">
            <h2>{{ destination.name }}</h2>
            <p>{{ destination.description }}</p>
            <router-link :to="'/destination/' + destination.id" class="explore-link">探索更多</router-link>
          </div>
        </div>
      </div>

    </div>
  </template>
  
  <script setup>
import {ref,reactive, getCurrentInstance} from "vue";
import { useRouter ,useRoute} from "vue-router";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api ={
  search:"/search/searchLine",
}

const destinations= [
          {
            id: 1005,
            name: '巴黎，法国',
            image: 'https://pic4.zhimg.com/v2-1ad8e4d125bfb707ded39509b5f5e761_r.jpg',
            description: '巴黎，被称为“浪漫之都”，是法国的首都和最大的城市。它以其古老的建筑、美食、时尚和艺术而闻名于世。'
          },
          {
            id: 1002,
            name: '东京，日本',
            image: 'https://pic1.zhimg.com/80/v2-845e6ae58b7f005ac3dcdda01809f0dc_720w.webp',
            description: '东京是日本的首都和最大城市，也是世界上最繁忙、最令人兴奋的城市之一，充满着现代化的高楼大厦和传统的历史遗迹。'
          },
          {
            id: 1007,
            name: '武汉, 湖北',
            image: 'https://x0.ifengimg.com/ucms/2020_41/CDBC037AEC81F4010835A0C6386433926D76EA2E_w1080_h809.jpg',
            description: '武汉，位于中国中部，长江和汉江的交汇处。作为湖北省的省会，它是中国内陆重要的城市之一，人口众多，历史悠久。武汉以其优美的自然景观和丰富的历史文化而闻名。著名景点包括黄鹤楼、东湖风景区和汉口江滩。此外，武汉也是中国重要的科技产业基地之一，拥有光谷等知名科技园区。'
          },
          // 添加更多目的地...
          {
            id: 4,
            name: '上海，中国',
            image: 'https://tse4-mm.cn.bing.net/th/id/OIP-C.KhMl9rM0Yik6jmaHPPKzSgHaLI?rs=1&pid=ImgDetMain',
            description: '上海，位于中国东部沿海地区，是中国最大的城市之一，也是国际金融、贸易和航运中心。作为中国最繁华、多元化的城市之一，上海汇集了现代化的高楼大厦、历史悠久的建筑和丰富多彩的文化景观。'
          },
        ]
 

const text= ref();
const search= async()=>{
  let result = await proxy.Request({
     url:api.search,
     params:{
      keyword:text.value
     }
  })
  if(!result){
    return;
  }
  router.push("/destination/"+result.data);

}
  </script>
  
  <style scoped>
  .destination-page {
    background: #ffff;
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  }
  .search {
    width: 350px;
    height: 100px;
    padding:20px ;
    margin-bottom: 20px;
    background-color:rgb(0,0,0,.4);
}
  .destination-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    grid-gap: 20px;
  }
  
  .destination-card {
    border: 1px solid #ddd;
    border-radius: 5px;
    overflow: hidden;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .destination-image {
    width: 100%;
    height: 200px;
    object-fit: cover;
  }
  
  .destination-info {
    padding: 15px;
  }
  
  .explore-link {
    color: #007bff;
    text-decoration: none;
  }
  </style>
  