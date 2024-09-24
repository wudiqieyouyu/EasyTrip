import './assets/base.scss'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

//引入cookies
import VueCookies from 'vue-cookies'
//引入element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/icon/iconfont.css'


import store from './store'
//全局方法
import Verify from '@/utils/Verify'
import Message from "@/utils/Message"
import Request from "@/utils/Request"
import Utils from "@/utils/Utils"
import Confirm from "@/utils/Confirm"
//全局组件
import Dialog from '@/components/Dialog.vue'
import  Avatar  from "@/components/Avatar.vue";
import  Carousel  from "@/components/Carousel.vue";
import  Cover  from "@/components/Cover.vue";
import  DataList  from "@/components/DataList.vue";
import  NoData  from "@/components/NoData.vue";
import  ImageViewer  from "@/components/ImageViewer.vue";
import  EditorMarkdown  from "@/components/EditorMarkdown.vue";
import  EditorHtml  from "@/components/EditorHtml.vue";
import  CoverUpload  from "@/components/CoverUpload.vue";
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import MapContainer from "@/components/MapContainer.vue";



const app = createApp(App)

app.use(router);
app.use(ElementPlus);
app.use(store);

app.config.globalProperties.VueCookies = VueCookies;
app.config.globalProperties.globalInfo = {
    bodyWidth:1300,
    avatarUrl:"/api/account/getAvatar/",
    domain:"http://localhost:6666",
    imageUrl:"/api/file/getImage/",
}
app.config.globalProperties.Verify = Verify;
app.config.globalProperties.Message= Message;
app.config.globalProperties.Request= Request;
app.config.globalProperties.Utils= Utils;
app.config.globalProperties.Confirm= Confirm;
app.component("Dialog",Dialog);
app.component("Avatar",Avatar);
app.component("Carousel",Carousel);
app.component("Cover",Cover);
app.component("DataList",DataList);
app.component("NoData",NoData);
app.component("EditorMarkdown",EditorMarkdown);
app.component("EditorHtml",EditorHtml);
app.component("ImageViewer",ImageViewer);
app.component("CoverUpload",CoverUpload);
app.component("MapContainer",MapContainer);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    if (key=="Avatar") {
        continue
    }
    app.component(key, component)
  }




  
app.mount('#app')