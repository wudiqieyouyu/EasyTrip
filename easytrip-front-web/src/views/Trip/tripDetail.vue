<template>
    <div class='spotsDetailCls'>
        <div class='spotsDetailCls-title'>
            <span>{{salesInfo.salesName}}</span>
        </div>
        <el-divider></el-divider>
         <div class='spotsDetailCls-info'>
             <div class='spotsDetailCls-info-img'>
                  <img style='width:95%;height:360px' :src="proxy.globalInfo.imageUrl+salesInfo.imgCover">
             </div>
             <div class='spotsDetailCls-info-content'>
                 <p>
                     <span style='font-size:20px'>简介<i class="el-icon-document"></i>:</span>
                   {{salesInfo.salesDesc}}
                </p>
                <el-divider></el-divider>
                 <p>
                     <span style='font-size:20px'>地址<i class="el-icon-location"></i>:</span>
                   {{salesInfo.salesAddress}}
                </p>
                <p style="margin-top:10px">
                     <span style='font-size:20px'>开放时间:<i class="el-icon-alarm-clock"></i>:</span>
                  {{salesInfo.openTime}}
                </p>
                <p style="margin-top:10px">
                     <span style='font-size:20px'>联系电话:<i class="el-icon-phone-outline"></i>:</span>
                  {{salesInfo.tel}}
                </p>
                <el-divider></el-divider>
                 <p style="margin-top:10px">
                     <span style='font-size:20px'>价格:</span>
                     <span style='font-size:20px;color:red'>¥{{salesInfo.price}}/人</span>

                     <span  >
                         <el-button style='margin-left:20px;
                         cursor: pointer;
                         background-color: #ff6e5c;
                         color: white;
                         line-height:15px;
                         padding:10px 20px;
                         display: inline-block;
                         font-size: 15px;'
                         @click="scheduleSpots(route.params.salesId)">我要预定</el-button>
                     </span>
                </p>
             </div>
        </div>

             <div class='spotsDetailCls-info-cd'>
                    <el-tabs type="border-card" v-model="selectedTab">
                        <el-tab-pane label="景区简介" name="tab1">
                        <div class="spotsDetailCls-info-cd-1">
                            <p style="white-space: normal; text-align: justify;">
                              <span style="font-size: 14px;" v-html="salesInfo.cotent">

                              </span>
                            </p>
                        </div>
                        </el-tab-pane>
                        <el-tab-pane label="购买须知" name="tab2">
                             <p v-html='salesInfo.purchaseInfo'>

                               </p>
                        </el-tab-pane>
                        <el-tab-pane label="产品特色" name="tab3">
                           <p v-html='salesInfo.features'></p>
                        </el-tab-pane>
                        <el-tab-pane label="用户点评" name="tab4">
                             <div style='display:flex;margin-bottom: 10px' v-for="(comment,index) in comments">
                                 <div style="display:flex;flex-direction:column;">
                                     <!-- <img :src='`http://localhost/upload/headimg/${comment.custorm.headImg}`' -->
                                          style='height:80px;width:80px;border-radius:50%;'/>
                                     <span style='margin-left:10px;margin-top:10px;text-align: center;width:80px'>{{comment.custorm.username}}</span>
                                 </div>
                                 <div style='margin-left:30px;'>
                                       <div>
                                           <span style='font-weight:800;margin-right:10px'>旅游交通:</span><span>{{formaterCommentStar(comment.transportsfeel)}}</span>
                                           <span style='font-weight:800;margin-left:30px;margin-right:10px'>酒店住宿:</span><span>{{formaterCommentStar(comment.hotelfeel)}}</span>
                                           <span style='font-weight:800;margin-left:30px;margin-right:10px'>是否玩爽:</span><span>{{formaterCommentStar(comment.playfeel)}}</span>
                                           <span style='margin-left:100px'>{{comment.createtime}}</span>
                                       </div>
                                       <div style='margin-top:30px;font-size:23px;color:#666'>
                                         {{comment.content}}
                                       </div>
                                 </div>
                             </div>
                             <el-divider></el-divider>

<!--                             <div style='display:flex'>-->
<!--                                 <div style="display:flex;flex-direction: column;">-->
<!--                                     <img src='../../static/img/tx3.jpg' style='height:100px;height:100px;border-radius:50%;'/>-->
<!--                                     <span style='margin-left:30px;margin-top:10px'>李四</span>-->
<!--                                 </div>-->
<!--                                 <div style='margin-left:30px;'>-->
<!--                                       <div>-->
<!--                                           <span style='font-weight:800;margin-right:10px'>旅游交通:</span><span>满意</span>-->
<!--                                           <span style='font-weight:800;margin-left:30px;margin-right:10px'>酒店住宿:</span><span>一般</span>-->
<!--                                           <span style='font-weight:800;margin-left:30px;margin-right:10px'>是否玩爽:</span><span>满意</span>-->
<!--                                           <span style='margin-left:500px'>2020-10-15</span>-->
<!--                                       </div>-->
<!--                                       <div style='margin-top:30px;font-size:23px;color:#666'>-->
<!--                                            价格实惠，酒店一般,床不是很大,热水来的很慢.-->
<!--                                       </div>-->
<!--                                 </div>-->
<!--                             </div>-->
<!--                             <el-divider></el-divider>-->

                            旅游交通:<el-rate v-model="transportsfeel" show-text></el-rate>
                            酒店住宿:<el-rate v-model="hotelfeel" show-text> </el-rate>
                            是否玩爽:<el-rate v-model="playfeel" show-text> </el-rate>

                            <el-input type="textarea" placeholder="来了就说点什么吧" v-model='content' ></el-input>
                            <el-button type="warning" @click="submitComments" >发表评论</el-button>
                        </el-tab-pane>

                    </el-tabs>
             </div>
    </div>
</template>

<script setup>
import {ref,reactive, getCurrentInstance} from "vue";
import { useRouter ,useRoute} from "vue-router";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api={
    loadSalesDetail:"/sales/loadSalesDetail"
}

const selectedTab = ref("tab1")


const salesInfo = ref({})
const loadSalesDetail =async()=>{
    let result = await proxy.Request({
       url:api.loadSalesDetail,
       params:{
        salesId:route.params.salesId
       }
    })
    if(!result){
      return;
    }
    salesInfo.value = result.data;
}
loadSalesDetail();

const scheduleSpots=(salesId)=>{
    router.push(`/order/${salesId}`)
}
</script>

<style lang="less" scoped>
 .spotsDetailCls{
    margin-left:160px;
    margin-right: 160px;
    .spotsDetailCls-title{
            margin-top:20px;
            margin-bottom:20px;
            span{
                font-size:30px
            }
        }
        .spotsDetailCls-info{
             margin-top:20px;
             margin-bottom:20px;
             display: flex;
            .spotsDetailCls-info-img{
                margin-right:20px;
                width: 50%;
                height: 400px;
            }
            .spotsDetailCls-info-content{
                 width: 50%;
                height: 400px;
            }

        }
        .spotsDetailCls-info-cd{
            margin-top:20px;
            margin-bottom:100px;
           .el-tabs__item{
               font-size:20px !important;
           }

        }

        .el-textarea__inner{
            min-height: 120px !important;
            margin-top:10px;
            margin-bottom:10px;
        }
        .el-button{
            float: right!important;
        }
    }
</style>
