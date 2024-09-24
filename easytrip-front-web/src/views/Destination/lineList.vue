<template>
  <div class="warp">
    <div class="card" v-for="item in lineList">
    <el-card class="map-card" >
        <h3>{{ item.lineName }}</h3>
        <MapContainer class="map"
        :start="getStartAddress(item)" 
        :end="getEndAddress(item)"
        :waypointArray="getWayPonitArray(item)"
        :width="500"
        :height="200"></MapContainer> 

        <div v-for="(name,index) in item.salesList" :key="index"  class="lines">
        <span  v-if="index !== item.salesList.length - 1" >{{ name.salesName}}({{ name.playTime }})-></span>
        <span  v-else >{{ name.salesName}}({{ name.playTime }})</span>
        </div>
    </el-card>
</div>
  </div>
</template>

<script setup>
import {ref,reactive, getCurrentInstance} from "vue";
import { useRouter ,useRoute} from "vue-router";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api = {
    loadLineList:"/line/loadLineList",
}

const start= ref([])
const end =ref([])
const waypoint = ref({})
const salesList=ref({})
const lineList = ref({})
const loadLineList = async()=>{
    let result = await proxy.Request({
       url:api.loadLineList,
       params:{
        regionId:route.params.regionId
       }
    })
    if(!result){
      return;
    }
    lineList.value= result.data;
}
loadLineList();

const getStartAddress=(item)=>{
   return item.salesList[0].salesAddress.split(",")
}
const getEndAddress=(item)=>{
    return item.salesList[item.salesList.length-1].salesAddress.split(",")
}
const getWayPonitArray=(item)=>{
    let array =  item.salesList.slice(1,-1).map(obj=>obj.salesAddress)
    array=array.map(obj =>obj.split(","))
    console.log(array)
  return  array;
}
</script>

<style lang="scss" scoped>
.warp {
  width: 1000px;
  margin: 0px auto;
    display: flex;
    .card{
    display: flex;
    padding: 15px;
    .map-card {
    width: 500px;
    margin-bottom: 20px;
  }
  .map {
    float: left;
  }
  .lines {
    display: inline-block;
    font-size: 13px;
  } 
    }

}
</style>
