<template>
    <div :id="mapContainerId" :style="{ width: `${width}px`, height: `${height}px` }"></div>
  </template>
  
<script setup>
import { ref,onMounted, onUnmounted ,nextTick} from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
  

  
window._AMapSecurityConfig = {
  securityJsCode: "2377d95cbfce0a1900434745de35c4a6",//申请key时的安全密钥
};
  const props = defineProps({
    width: {
      type: Number,
      default: 500
    },
    height: {
      type: Number,
      default: 500
    },
    start:{
      type:Array,
    },
    end:{
      type:Array,
    },
    waypointArray:{
      type:Array,
    }
  });
  
  const mapContainerId = ref(`map-container-${Math.random().toString(36).substr(2, 9)}`);


  let map = null;
  let driving = null;
  
  onMounted(async() => {

    await nextTick(); // 等待组件渲染完成

    AMapLoader.load({
      key: "1bed35072fae7b2e855259c48b22f9b9",
      version: "2.0",
      plugins: ["AMap.Scale", "AMap.Driving"],
    })
      .then(AMap => {
        map = new AMap.Map(mapContainerId.value, {
          viewMode: "3D",
          zoom: 11,
          center: [116.397428, 39.90923],
        });
  
        driving = new AMap.Driving({
          policy: 0,
          map: map,
        });
        console.log("我是waypoints"+props.waypointArray)
        const startLngLat = props.start;
        const endLngLat = props.end;
      //   const waypoints = [
      //   new AMap.LngLat(116.396938, 39.919824), // 第一个途经点
      //   new AMap.LngLat(116.416794, 39.920242)  // 第二个途经点
      // ];
      const waypoints = props.waypointArray;
        driving.search(startLngLat, endLngLat,{ waypoints: waypoints }, (status, result) => {
          if (status === 'complete' && result.routes && result.routes.length) {
            const route = result.routes[0]; // 取第一条路线
            const path = route.steps.reduce((path, step) => path.concat(step.path), []);
            const polyline = new AMap.Polyline({
              path: path,
              borderWeight: 2,
              strokeColor: "#3366FF",
              lineJoin: 'round',
              lineCap: 'round',
              zIndex: 50
            });
            polyline.setMap(map); // 在地图上显示路线
            map.setFitView(polyline); // 调整地图视野以适应路线
          } else {
            console.error('Failed to search driving route:', result);
          }
        });
      })
      .catch(e => {
        console.error('Failed to load AMap:', e);
      });
  });
  
  onUnmounted(() => {
    map?.destroy();
  });
  </script>
  
  <style lang="scss" scoped>
  #container {
    width: 100%;
    height: 800px;
  }
  </style>
  