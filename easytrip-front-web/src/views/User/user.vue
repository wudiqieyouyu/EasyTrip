<template>
  <div
  class="container-body ucenter"
  :style="{width:proxy.globalInfo.bodyWidth+'px'}"
  >
  <div class="user-banner">
    <router-link to="/" class="a-link">首页</router-link>
    <span class="iconfont icon-right"></span>
    <span>个人中心</span>
  </div>
  <div class="ucenter-panel">
    <div class="user-side">
      <!--头像信息-->
        <div class="avatar-panel">
            <div class="edit-btn a-link" v-if="isCurrentUser" @click="updateUserInfo">
              修改资料
            </div>
            <div class="avatar-inner">
              <Avatar :userId="userInfo.userId" :width="120"></Avatar>
            </div>

            <div class="nick-name">
              <span>{{ userInfo.nickName }}</span>
              <span v-if="userInfo.sex==0" class="iconfont icon-woman" style="color: var(--pink);"></span>
              <span v-if="userInfo.sex==1" class="iconfont icon-man"></span>
            </div>
            <div class="desc">{{ userInfo.personDescription }}</div>
        </div>
        <!--扩展信息-->
        <div class="user-extend-pannel">
          <div class="info-item">
          <div class="label iconfont icon-like">获赞</div>
          <div class="value">{{ userInfo.likeCount }}</div>  
          </div>

          <div class="info-item">
          <div class="label iconfont icon-post">发帖</div>
          <div class="value">{{ userInfo.postCount }}</div>  
          </div>

          <div class="info-item">
          <div class="label iconfont icon-register">加入</div>
          <div class="value">{{ userInfo.joinTime }}</div>  
          </div>

          <div class="info-item">
          <div class="label iconfont icon-login">最后登录</div>
          <div class="value">{{ userInfo.lastLoginTime }}</div>  
          </div>
          <div class="info-item">
          <div class="label "><el-icon color="#888888"  size="18" class="el-icon"><LocationInformation /></el-icon>
            归属地</div>
          <div class="value">{{ userInfo.lastLoginIpAddress }}</div>  
          </div>
        </div>

    </div>
    <div class="article-panel">
      <el-tabs
      :model-value="activeTabName"
      @tab-change="changeTab"
      >
    <el-tab-pane label="发帖" :name="0"></el-tab-pane>
    <el-tab-pane label="评论" :name="1"></el-tab-pane>
    <el-tab-pane label="点赞" :name="2"></el-tab-pane>
    <el-tab-pane label="订单" :name="3"></el-tab-pane>
    </el-tabs>
    <div class="article-list" v-if="activeTabName !=3">
      <DataList :loading="loading" :dataSource="articleListInfo" @loadData="loadArticle" noDataMsg="没有发现帖子">
                <template #default="{data}">
                    <ArticleItem :data="data"></ArticleItem>
                </template>
      </DataList>
    </div>

    <div class="article-list" v-else>
      <DataList :loading="loading" :dataSource="orderListInfo" @loadData="loadOrder" noDataMsg="没有发现订单">
                <template #default="{data}">
                    <OrderItem :data="data"></OrderItem>
                </template>
      </DataList>
    </div>
    </div>
  </div>
  <UcenterEditUserInfo ref="ucenterEditUserInfoRef" @resetUserInfo="resetUserInfoHandler"></UcenterEditUserInfo>
  </div>
</template>

<script setup>
import UcenterEditUserInfo from "./UcenterEditUserInfo.vue";
import ArticleItem from "@/views/ArticleItem/ArticleItem.vue";
import OrderItem from "@/views/Order/OrderItem.vue";
import {ref,reactive, getCurrentInstance, onMounted,watch} from "vue";
import { useRouter ,useRoute} from "vue-router";
import { useStore } from "vuex";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const store = useStore();

const api ={
  getUserInfo:"/ucenter/getUserInfo",
  loadUserArticle:"/ucenter/loadUserArticle",
  loadOrder:"/ucenter/loadOrder"
}

const userId = ref(null);
const userInfo = ref({})
const loadUserInfo=async()=>{
  let result = await proxy.Request({
     url:api.getUserInfo,
     params:{
      userId:userId.value
     },
     errorCallback:()=>{
      setTimeout(() => {
        router.push("/");
      }, 1500);
     }
  })
  if(!result){
    return;
  }
  userInfo.value =result.data;
}

const isCurrentUser = ref(false);
//重新设置当前用户
const resetCurrentUser =()=>{
  const loginUserInfo =store.getters.getLoginUserInfo;
  if (loginUserInfo&&loginUserInfo.userId===userId.value) {
    isCurrentUser.value=true;
  }else{
    isCurrentUser.value =false;
  }
}

watch(() => store.state.loginUserInfo,(newVal, oldVal) => {
  resetCurrentUser();
}, { immediate: true, deep: true });



onMounted(()=>{
  userId.value=route.params.userId;
  loadUserInfo();
})


//右侧文章
const activeTabName=ref(0);
const changeTab =(type)=>{    
  activeTabName.value = type
  if(type!=3){
    loadArticle();
  }else{
    console.log("1");
    loadOrder();
  }

}

const orderListInfo = ref({});
const loadOrder = async()=>{
  let result = await proxy.Request({
     url:api.loadOrder,
  })
  if(!result){
    return;
  }
  orderListInfo.value = result.data;
}

const articleListInfo =ref({});
const loading = ref(false);
const loadArticle = async ()=>{
    loading.value = true;
    let params = {
        pageNo:articleListInfo.value.pageNo,
        type:activeTabName.value,
        userId:userId.value
    };
    let result = await proxy.Request({
        url:api.loadUserArticle,
        params:params,
        showLoading:false,
    });

    loading.value = false;
    if (!result) {
        return;
    }
    articleListInfo.value = result.data;
};

watch(() =>route.params.userId,
 (newVal, oldVal) => {
  if (newVal) {
      userId.value = newVal;
      resetCurrentUser();
      loadUserInfo();
      loadArticle();
  }
 }, { immediate: true, deep: true });


const ucenterEditUserInfoRef = ref(null);
const updateUserInfo=()=>{
  ucenterEditUserInfoRef.value.showEditUserInfoDialog(userInfo.value);
}


const resetUserInfoHandler = (data)=>{
  userInfo.value =data;
}
</script>

<style lang="scss" >
.ucenter{
  .user-banner{
    color:#9ba7b9;
    line-height: 35px;
    .icon-right{
      padding:0px 5px;
      color:#616161;
    }
  }
  .ucenter-panel{
    display: flex;
    .user-side{
      width: 300px;
      margin-right: 10px;
      .avatar-panel{
        background: #fff;
        text-align: center;
        padding: 10px;
        .edit-btn{
          text-align: right;
          font-size: 14px;
          cursor: pointer;
        }
        .avatar-inner{
          display:flex;
          justify-content: center;
        }
        .nick-name{
          .iconfont{
            margin-left: 5px;
            color: var(--link);
          }
        }
        .desc{
          margin-top: 5px;
          text-align: left;
          font-size: 14px;
          color: #929393;

        }
      }


      .user-extend-pannel{
        margin-top: 10px;
        background: #fff;
        padding: 10px;
        .info-item{
          display: flex;
          justify-content: space-between;
          line-height: 30px;
          .label{
            font-size: 16px;
          }
          .label::before{
            font-size:17px;
            color: #888888;
            padding-right:5px;
          }
          .value{
            font-size: 13px;
          }
        }
      }


    }

    .article-panel{
      flex:1;
      background: #ffff;
      padding: 0px 10px 10px 10px;
    }
  }
}
.el-icon{
  position: relative;
  bottom:-3px;
}
</style>
