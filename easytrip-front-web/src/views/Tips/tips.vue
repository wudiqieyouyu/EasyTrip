<template>

      <!-- <div class="top">
          <div class="carousel">
            <Carousel :imgDetail="carouselList" height="550px" ></Carousel>
        </div>
      </div> -->
      <div class="center">
      <div class="articlle-recommend">
        <h3>旅游攻略导航</h3>
        <ul class="leader">
          <li v-for="(item,index) in leaderboardList"  class="leader-children">
            <span class="number">{{ ++index }}</span>
            <span class="name a-link" @click="toBoard(item.regionId)">{{ item.regionName }}</span>
          </li>
        </ul>
      </div>
        <div class="container-body index-body" style=" width:730px ">
        <div class="title"><h3 >攻略游记</h3></div>
        <div class="article-panel">
            <div class="top-tab">
                <div :class="['tab',orderType==0?'active':'']" @click="changeOrederType(0)">热榜</div>
                <el-divider direction="vertical"></el-divider>
                <div :class="['tab',orderType==1?'active':'']" @click="changeOrederType(1)">发布时间</div>
                <el-divider direction="vertical"></el-divider>
                <div :class="['tab',orderType==2?'active':'']" @click="changeOrederType(2)">最新</div>
            </div>
            <div class="article-list">
                <DataList :loading="loading" :dataSource="articleListInfo" @loadData="loadArticle" noDataMsg="没有发现帖子,赶紧来一个吧">
                <template #default="{data}">
                    <ArticleItem :data="data"></ArticleItem>
                </template>
                </DataList>
            </div>
        </div>
        </div>
      </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import ArticleItem from "../ArticleItem/ArticleItem.vue";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api = {
     loadCarousel: "/article/loadCarousel",
    loadArticle:"/index/loadArticle",
    loadRegion:"/region/loadRegion4Leaderboard",

}

onMounted(() => {
    loadArticle();
    loadCarousel();
});

//获取轮播图信息
const carouselList = ref();
const loadCarousel = async () => {
    let result = await proxy.Request({
        url: api.loadCarousel,
    });
    if (!result) {
        return;
    }
    carouselList.value = result.data;
}

const leaderboardList =ref();
const loadRegion4Leaderboard=async()=>{
    let result = await proxy.Request({
       url:api.loadRegion,
       params:{
        pageSize:10,
        pageNo:0
       },
    })
    if(!result){
      return;
    }
    leaderboardList.value= result.data.list
}
loadRegion4Leaderboard();


const changeOrederType = (type)=>{
    orderType.value =type;
    loadArticle();
}

const loading = ref(false);
//获取文章列表
const orderType = ref(0);
const articleListInfo =ref({});
const loadArticle = async ()=>{
    loading.value = true;
    let params = {
        pageNo:articleListInfo.value.pageNo,
        orderType:orderType.value
    };
    let result = await proxy.Request({
        url:api.loadArticle,
        params:params,
        showLoading:false,
    });

    loading.value = false;
    if (!result) {
        return;
    }
    articleListInfo.value = result.data;
};

//跳转到板块
const toBoard=(data)=>{
  router.push(`/tips/${data}`)
}
</script>

<style lang="scss"  scoped>
.top{
  .mdd_nav{
  float: left;
  margin-top: 10px;
  width: 500px;
}
.carousel{
  margin-left: auto;
}
}
.center{
  display: flex;
.articlle-recommend{
  padding-top: 5px;
  margin: 0px 0px 0px 230px;
  font-family: "Microsoft YaHei";
  font-size: 26px;
.leader{
  padding: 0px 15px 0px 10px;
  margin-top: 10px;
  list-style-type: none; 
  width: 160px;
  background: #ffff;
 .leader-children{
  padding: 15px 0;
  .number{
    font-size: 25px;
    margin-right: 15px;
    color: rgb(253, 36, 36);
  }
  .name{
    cursor: pointer;
  }
  }

  }

  
}
.index-body{
  float: right;
  margin-right: 350px;
    .article-panel{
        background:#ffff;
        .top-tab{
            display: flex;
            align-items:center;
            padding: 10px;
            font-size:15px;
            border-bottom:1px solid #ddd;
            .tab{
                cursor: pointer;
            }
            .active{
                color:var(--link);
            }
        }

}
}
}
.title{
    font-size: 26px;
    font-family: "Microsoft YaHei";
    color: #333;
}


</style>
