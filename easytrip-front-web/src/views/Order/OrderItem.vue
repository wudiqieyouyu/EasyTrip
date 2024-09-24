<template>
    <div class="article-item">
      <div class ="article-item-inner">
          <div class = "article-body">
              <div class="user-info">
                  <Avatar :userId="data.userId" :width="30"></Avatar>
                  <router-link :to ="'/user/'+data.userId" class="link-info">{{ data.nickName }}</router-link>
                  <el-divider direction="vertical"></el-divider>
                  <div class="post-time">{{ data.createTime }}</div>
                

              </div>
              <router-link :to="`/article/${data.articleId}`" class="title">
                <span v-if="data.topType==1" class="top">置顶</span>
                <span>{{ data.salesName }}</span>
            </router-link>
              <div class="summary">订单号：{{ data.orderId }}</div>

          </div>
          <router-link :to="`/article/${data.articleId}`" class="title"><Cover :cover="data.cover" :width="100" v-if="data.cover"></Cover></router-link>
      </div>
    </div>
  </template>
  
  <script setup>
  const props = defineProps({
      data:{
          type:Object,
      },
  });
  </script>
  
  <style lang="scss" scoped>
  .article-item{
      padding: 10px 15px 0px 15px;
      .article-item-inner{
      border-bottom: 1px solid #ddd;
      padding: 10px 0px;
      display: flex;
      .article-body{
          flex: 1;
          .user-info{
              display:flex;
              align-items:center ;
              font-size: 14px;
              color:#4e5969;
              .link-info{
                  margin-left:5px;
                  color: #494949;
                  text-decoration:none;
              }
              .link-info:hover{
                  color:var(--link);
              }
              .post-time{
                  font-size: 13px;
                  color: #aaaaaa;
              }
          }
          .title{
              font-weight:bold;
              text-decoration: none;
              color: #4a4a4a;
              font-size: 16px;
              margin:10px 0px;
              display:inline-block;
              .top{
                  font-size: 12px;
                  border-radius:5px;
                  border:1px solid var(--pink);
                  color: var(--pink);
                  padding: 0px 5px;
                  margin-right: 10px;
              }
          }
          .summary{
              font-size: 14px;
              color:#86909c
          }
          .article-info{
              margin-top:10px ;
              display: flex;
              align-items:center;
              font-size: 13px;
              .iconfont{
                  color: #86909c;
                  margin-right: 25px;
                  font-size: 14px;
              }
              .iconfont:before{
                  padding-right:3px ;
              }
          }
      }
  }
  }
  .article-item:hover{
      background: #fffbfb;
  }
  </style>
  