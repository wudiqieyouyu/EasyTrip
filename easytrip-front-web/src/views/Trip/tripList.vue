<template>
    <div class='spotsContainerCls'>
       
        <!-- 列表搜索区 start-->
        <div class='listCdCls' >
             <div class='listCdCls_1'>
                    <span>风景等级:</span>
                    <span>
                        <a href="javascript:;" :class='[{active: 0== isActive2},"a-link"]' @click="setActive2(0)">不限</a>
                        <a href="javascript:;" :class='[{active: 1 == isActive2},"a-link"]' @click="setActive2(1)" style="margin-left: 10px">A</a>
                        <a href="javascript:;" :class='[{active: 2 == isActive2},"a-link"]' @click="setActive2(2)" style="margin-left: 10px">AA</a>
                        <a href="javascript:;" :class='[{active: 3 == isActive2},"a-link"]' @click="setActive2(3)" style="margin-left: 10px">AAA</a>
                        <a href="javascript:;" :class='[{active: 4 == isActive2},"a-link"]' @click="setActive2(4)" style="margin-left: 10px">AAAA</a>
                        <a href="javascript:;" :class='[{active: 5 == isActive2},"a-link"]' @click="setActive2(5)" style="margin-left: 10px">AAAAA</a>
                   </span>
            </div>

             <div class='listCdCls_1'>
                    <span>价格区间:</span>
                    <span>
                        <a href="javascript:;" :class='[{active: 0 == isActive4},"a-link"]' @click="setActive4(0)">不限</a>
                        <a href="javascript:;" :class='[{active: 100 == isActive4},"a-link"]' @click="setActive4([0,100])" style="margin-left: 10px">0-100</a>
                        <a href="javascript:;" :class='[{active: 200 == isActive4},"a-link"]' @click="setActive4([100,200])" style="margin-left: 10px">100-200</a>
                        <a href="javascript:;" :class='[{active: 500 == isActive4},"a-link"]' @click="setActive4([200,500])" style="margin-left: 10px">200-500</a>
                        <a href="javascript:;" :class='[{active: 1000 == isActive4},"a-link"]' @click="setActive4([500,1000])" style="margin-left: 10px"> 500-1000</a>
                        <a href="javascript:;" :class='[{active: -1 == isActive4},"a-link"]' @click="setActive4(1000)" style="margin-left: 10px">1000+</a>
                   </span>
            </div>

        </div>
        <!-- 列表搜索区 end-->

        <!-- 排序区域 start-->
        <div class='orderCls'>
            <span style="margin-left: 20px"><a href="javascript:;" @click="getSpotsByTime()">发布时间排序</a></span>
            <span ><a href="javascript:;" @click="getSpotsByPrice()">价格排序</a></span>
        </div>
        <!-- 排序区域 end-->
        <!-- 景区分页列表start -->
        <div class='spotsCls' v-for="spot in tripList" >
            <div class='spotsLeftCls'>
                <el-image style="padding:10px;width:300px;height:200px"
                :src ="proxy.globalInfo.imageUrl+spot.imgCover"></el-image>
            </div>
            <div class='spotsRightCls'  >
                <div style="margin-top:30px;width: 100%;display:flex;justify-content: space-between;">
                    <div style="font-size:25px">{{spot.salesName}}</div>
                    <div style="font-size:23px;margin-right:20px">门票价 <span style="color:red">¥{{spot.price}}</span> 元</div>
                </div>

                <div style="margin-top:25px;width: 100%;display:flex;justify-content: space-between;">
                    <div>
                        <div style="margin-top:10px">
                            <i class="el-icon-location" style="color:orange;font-size:20px"></i>
                            <span class='font-size:18px'>{{spot.salesAddress}}</span>
                        </div>
                        <div style="margin-top:10px">
                            <i class="el-icon-alarm-clock" style="color:orange;font-size:20px"></i>
                            <span class='font-size:18px'>{{spot.openTime?spot.openTime:"全天候"}}</span>
                        </div>
                    </div>
                    <div style='width: 170px;
                                height: 56px;
                                margin-right:20px;
                                background-color: #ff7362;
                                color: #fff;
                                font-size: 18px;
                                text-align: center;
                                cursor: pointer; 
                                line-height: 56px;' @click="toSalesDetail(spot.salesId)">立即预定</div>
                </div>

            </div>
        </div>


   </div>
</template>
<script setup>
import {ref,reactive, getCurrentInstance,onMounted} from "vue";
import { useRouter ,useRoute} from "vue-router";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const api ={
    searchSales:"/search/searchSales"
}



const queryParams = ref({
    title:route.params.query,
    level:"",
    maxPrice:"",
    minPrice:"" ,
    orderBy:"",
});  
const orderTypeByTime = ref(0);
const getSpotsByTime = ()=>{  
    orderTypeByTime.value = !orderTypeByTime.value
    if(orderTypeByTime.value){
        queryParams.value.orderBy = "create_time asc" 
    }else{
        queryParams.value.orderBy = "create_time desc"
    }
    search();
}

const orderTypeByPrice = ref(0);
const getSpotsByPrice = ()=>{  
    orderTypeByPrice.value = !orderTypeByPrice.value
    if(orderTypeByPrice.value){
        queryParams.value.orderBy = "price asc" 
    }else{
        queryParams.value.orderBy = "price desc"
    }
    search();
}

const tripList =ref({})
const search=async()=>{
    let result = await proxy.Request({
       url:api.searchSales,
       params:queryParams.value
    })
    if(!result){
      return;
    }
    tripList.value = result.data.list;
}
search();

const isActive2=ref(0)
const setActive2=(data)=>{
    isActive2.value = data
    if(isActive2.value!==0){
        queryParams.value.level = isActive2.value
    }else{
        queryParams.value.level="";
    }
    search();
}

const isActive4= ref(0)
const setActive4=(data)=>{
    console.log(data);
    if (data instanceof Array) {
        queryParams.value.minPrice = data[0]
        queryParams.value.maxPrice = data[1]
        isActive4.value = data[1]
    }else if (data ===0){
        isActive4.value = 0;
        queryParams.value.minPrice = ''
        queryParams.value.maxPrice = ''
    }else {
        queryParams.value.maxPrice = ''
        queryParams.value.minPrice = data
        isActive4.value = -1
    }
    search();
}

const toSalesDetail=(salesId)=>{
    router.push(`/trip/detail/${salesId}`)
  }


</script>
<style lang="less">
    .active{
        color:white;
        background-color: var(--link);
        padding:5px;
    }

    .spotsContainerCls{
        // margin-left:@lrwith;
        // margin-right:@lrwith;
        background-color: #fafafa;
         min-width: 1024px !important;

            /** 查询条件 */
        .listCdCls{
            background-color:white;
            margin-top:10px;
            margin-bottom:10px;
            .listCdCls_1{
                margin-top:10px;
                margin-bottom:10px;
                font-size:20px;
                line-height:38px;
                span{
                        margin-left:20px;
                        a{
                            text-decoration: none;
                        }
                        a:visited{
                            color: orange;
                        }
                }
            }


        }

        /** 排序区域 */
        .orderCls{
            line-height: 100px;
            margin-bottom: 10px;
            background-color:white;
            span{
                font-size:20px;
                height: 50px;
                line-height: 50px;
                margin-right: 20px;
                padding-right: 20px;
                border-right: 1px solid #eee;
            }
            a{
                text-decoration:none;
                color: var(--link);
            }
        }

        /** 景区分页样式 */
        .spotsCls{
            display: flex;
            height: 200px;
            margin-bottom: 10px;
            background-color:white;
            .spotsRightCls{
                margin-left:20px;
                margin-right: 10px;
                width: 100%;
            }
        }


   }
</style>
