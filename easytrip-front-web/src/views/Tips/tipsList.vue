<template>
          <div class="container-body index-body" :style="{ width: proxy.globalInfo.bodyWidth + 'px' }">
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
</template>

<script setup>
import { ref, reactive, getCurrentInstance, onMounted ,watch} from "vue";
import { useRouter, useRoute } from "vue-router";
import ArticleItem from "../ArticleItem/ArticleItem.vue";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api = {
    loadArticle:"/index/loadArticle",

}

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
        orderType:orderType.value,
        boardId:route.params.regionsId,
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
loadArticle();

watch(() => route.params.regionsId,
(newVal, oldVal) => {
    if (newVal) {
        loadArticle();
    }
}, { immediate: true, deep: true });
</script>

<style lang="scss" scoped>

.index-body{
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
.title{
    font-size: 26px;
    font-family: "Microsoft YaHei";
    color: #333;
}


</style>
