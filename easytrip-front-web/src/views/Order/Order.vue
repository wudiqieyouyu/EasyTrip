<template>
    <div class="order-page">
      <h1>订单页面</h1>
      <div class="order-details">
        <h2>订单详情</h2>
        <ul>
          <li>商品名称: {{ orderInfo.saleName }}</li>
          <li>价格: {{ orderInfo.price }}元</li>
          <!-- <li>订单号: {{ order.orderNumber }}</li> -->
        </ul>
      </div>
      <div class="customer-details">
        <h2>客户信息</h2>
        <ul>
          <li>姓名: {{ orderInfo.userName }}</li>
          <li>电话: {{ orderInfo.phoneNumber }}</li>
          <li>邮箱: {{ orderInfo.email }}</li>
        </ul>
      </div>
      <div class="order-actions">
        <el-button @click="cancelOrder" class="cancel-btn" type="primary">取消订单</el-button>
        <el-button @click="confirmOrder" class="confirm-btn" type="primary">确认订单</el-button>
      </div>
    </div>
  </template>
<script setup>
import { ElMessage } from "element-plus";
import {ref,reactive, getCurrentInstance} from "vue";
import { useRouter ,useRoute} from "vue-router";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api ={
    loadOrderInfo:"/order/loadOrderInfo",
    postOrder:"/order/postOrder"
}
const orderInfo = ref({})
const loadOrderInfo = async()=>{
    let result = await proxy.Request({
       url:api.loadOrderInfo,
       params:{
        saleId:route.params.saleId
       }
    })
    if(!result){
      return;
    }
    orderInfo.value = result.data;
}
loadOrderInfo();
  
      const cancelOrder = () => {
        // 在此处添加取消订单的逻辑
        router.go(-1)
      };
  
      const confirmOrder = () => {
        proxy.Confirm("确认订单即视为同意购买本产品,确定吗",async()=>{
        let result = await proxy.Request({
           url:api.postOrder,
           params:{
            salesId:route.params.saleId,
            userId:orderInfo.value.userId,
            salesName:orderInfo.value.saleName,
            userName:orderInfo.value.userName,
            price:orderInfo.value.price,
           }
        })
        if(!result){
          return;
        }
        if (result.code===200) {
            router.push(`/orderSuccess/${result.data.orderId}`);
        }
    })
      };

  </script>
  
  <style scoped>
  .order-page {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #f9f9f9;
  }
  
  .order-details, .customer-details {
    margin-bottom: 20px;
    padding: 10px;
    border: 1px solid #eee;
    border-radius: 5px;
    background-color: #fff;
  }
  
  .order-actions button {
    margin-right: 10px;
  }
  
  .cancel-btn {
    background-color: #ff6347;
    color: #fff;
  }
  
  .confirm-btn {
    background-color: #4caf50;
    color: #fff;
  }
  </style>
  