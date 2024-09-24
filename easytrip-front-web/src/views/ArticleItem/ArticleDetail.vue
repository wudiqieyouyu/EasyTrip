<template>
    <div class="container-body article-detail-body" :style="{ width: proxy.globalInfo.bodyWidth + 'px' }"
        v-if="Object.keys(articleDetail).length > 0">
        <!--板块导航-->
        <div class="board-info">
            <router-link :to="`/tips/${articleDetail.pBoardId}`" class="a-link">{{ articleDetail.pBoardName }}</router-link>
            <span class="iconfont icon-right"></span>
            <template v-if="articleDetail.boardId">
                <router-link :to="`/tips/${articleDetail.pBoardId}${articleDetail.boardId}`" class="a-link">
                    {{ articleDetail.boardName }}
                </router-link>
                <span class="iconfont icon-right"></span>
            </template>

            <span>{{ articleDetail.title }}</span>
        </div>
        <!--文章详情-->
        <div class="detail-container" :style="{ width: proxy.globalInfo.bodyWidth - 300 + 'px' }">
            <div class="article-detail">
                <div class="title">{{ articleDetail.title }}</div>
                <div class="user-info">
                    <Avatar :userId="articleDetail.userId" width="50"></Avatar>
                    <div class="user-info-detail">
                        <router-link class="nick-name" :to="`/user/${articleDetail.userId}`">{{ articleDetail.nickName
                        }}</router-link>
                        <div class="time-info">
                            <span>{{ articleDetail.postTime }}</span>
                            <span class="address">&nbsp;·&nbsp;{{ articleDetail.userIpAddress }}</span>
                            <span class="iconfont icon-eye-solid">
                                {{ articleDetail.readCount == 0 ? "阅读" : articleDetail.readCount }}
                            </span>

                            <router-link 
                            v-if="articleDetail.userId==currentUserInfo.userId"
                            :to="`/editPost/${articleDetail.articleId}`" class="a-link btn-edit">
                                <span class="iconfont icon-edit">编辑</span>
                            </router-link>
                        </div>
                    </div>
                </div>
                <!--文章详情-->
                <div class="detail" id="detail" v-html="articleDetail.content"></div>
            </div>
            <!--评论-->
            <div class="comment-panel" id="view-comment">
                    <CommentList
                    v-if="articleDetail.articleId" 
                    :articleId="articleDetail.articleId" 
                    :articleUserId="articleDetail.userId"
                    @updateCommentCount="updateCommentCount">
                    </CommentList>
            </div>
        </div>
        <!--目录-->
        <div class="toc-panel">
            <div class="top-containner">
                <div class="toc-title">目录</div>
                <div class="toc-list">
                    <template v-if="toc-Array.length==0">
                    <div class="no-toc">未解析到目录</div>
                    </template>
                    <template v-else>
                        <div v-for="toc in tocArray">
                        <span 
                        :class="['toc-item',toc.id==anchorId?'active':'']" 
                        @click="gotoAnchor(toc.id)"
                        :style="{'padding-left':toc.level*15+'px'}"
                        >{{ toc.title }}</span>
                        </div>
                    </template>
                </div>
            </div>
        </div>
    </div>
    <!--左侧快捷操作-->
    <div class="quick-panel" :style="{ left: quickPanelLeft + 'px' }">
        <!--点赞-->
        <el-badge :value="articleDetail.goodCount" type="info" :hidden="!articleDetail.goodCount > 0">
            <div class="quick-item" @click="doLikeHandler">
                <span :class="['iconfont icon-good',haveLike?'have-like':'']"></span>
            </div>
        </el-badge>

        <!--评论-->
        <el-badge :value="articleDetail.commentCount" type="info" :hidden="!articleDetail.commentCount > 0" @click="goToPostion('view-comment')">
            <div class="quick-item">
                <span class="iconfont icon-comment"></span>
            </div>
        </el-badge>
        <!--图片预览-->
        <ImageViewer ref="imageViewerRef" :imageList="previewImgList"></ImageViewer>
    </div>
</template>

<script setup>
import hljs from "highlight.js";
import "highlight.js/styles/atom-one-light.css";//样式
import CommentList from "./CommentList.vue"
import { ref, reactive, getCurrentInstance, onMounted, handleError, nextTick, onUnmounted,watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const store = useStore();
const api = {
    getArticleDetail: "/article/getArticleDetail",
    doLike:"/article/doLike"
}
const currentUserInfo = ref({});

onMounted(() => {
    getArticleDetail(route.params.articleId);
});

//是否点赞
const haveLike = ref(false);
const articleDetail = ref({});
const getArticleDetail = async (articleId) => {
    let params = {
        articleId: articleId
    };
    let result = await proxy.Request({
        url: api.getArticleDetail,
        params: params,
    });
    if (!result) {
        return;
    }
    articleDetail.value = result.data.tripArticleVo;
    haveLike.value=result.data.haveLike;

    //图片预览
    imagePreview();
    //代码高亮
    highLightCode();
    makeToc();
}
//点赞
const doLikeHandler =async ()=>{
    if (!store.getters.getLoginUserInfo){
        store.commit("showLogin",true)
        return
    }
    let result = await proxy.Request({
        url:api.doLike,
        params:{
            articleId:articleDetail.value.articleId
        }
    });
    if (!result) {
        return
    }
    haveLike.value=!haveLike.value
    let goodCount = 1 ;
    if (!haveLike.value) {
        goodCount = -1;
    }
    articleDetail.value.goodCount  = articleDetail.value.goodCount+goodCount;
}
//快捷操作
const quickPanelLeft = (window.innerWidth - proxy.globalInfo.bodyWidth) / 2 - 60;
const goToPostion = (domId)=>{
    document.querySelector("#"+domId).scrollIntoView();
}

//文章图片预览
const imageViewerRef = ref(null);
const previewImgList=ref([]);
const imagePreview =()=>{
    nextTick(()=>{
        const imageNodeList = document.querySelector("#detail").querySelectorAll("img");
        const imageList =[];
        imageNodeList.forEach((item,index)=>{
            const src = item.getAttribute("src");
            imageList.push(src);
            item.addEventListener("click",()=>{
                imageViewerRef.value.show(index);
            })
        })
        previewImgList.value=imageList;
    })
}

//代码高亮
const highLightCode = ()=>{
    nextTick(()=>{
        let blocks =document.querySelectorAll("pre code");
        blocks.forEach(item=>{
            hljs.highlightBlock(item);
        })
    })
}

//更新评论数量
const updateCommentCount=(totalCount)=>{
    articleDetail.value.commentCount=totalCount
}

//获取目录
const tocArray = ref([]);
const makeToc = ()=>{
    nextTick(()=>{
        const tocTags = ["H1","H2","H3","H4","H5","H6"];
        //获取所有h标签
        const contentDom =document.querySelector("#detail");
        const childrenNodes = contentDom.childNodes;
        let index = 0;
        childrenNodes.forEach((item)=>{
            let tagName = item.tagName;
            if (tagName==undefined||!tocTags.includes(tagName.toUpperCase())) {
                return true;
            }
            index++
            let id ="toc"+index;
            item.setAttribute("id",id);
            tocArray.value.push({
                id:id,
                title:item.innerText,
                level:Number.parseInt(tagName.substring(1)),
                offsetTop:item.offsetTop,
            })

        })
    });
}

const anchorId =ref(null); 
const gotoAnchor=(domId)=>{
    const dom =document.querySelector("#"+domId);
    dom.scrollIntoView({
        behavior:"smooth",
        block:"start",
    })
}

const listenerScroll =()=>{
    let currentScrollTop = getScrollTop();
    tocArray.value.some((item,index)=>{
        if (index<tocArray.value.length-1&&currentScrollTop>=tocArray.value[index].offsetTop&&currentScrollTop<tocArray.value[index+1].offsetTop||index==tocArray.value.length-1&&currentScrollTop<tocArray.value[index].offsetTop) {
            anchorId.value = item.id;
            return true; 
        }
    })
}

//获取滚动条的高度
const getScrollTop = () => {
    let scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
    return scrollTop;
};

//监听用户信息
watch(() => store.state.loginUserInfo,
(newVal, oldVal) => {
    currentUserInfo.value = newVal||{};
}, { immediate: true, deep: true });

onMounted(()=>{
    window.addEventListener("scroll",listenerScroll,false);
})

onUnmounted(()=>{
    window.removeEventListener("scroll",listenerScroll,false)

})


</script>

<style lang="scss" >
.article-detail-body {
    position: relative;
    .board-info {
        line-height: 40px;

        .icon-right {
            margin: 0px 10px;
        }
    }

    .detail-container {
        .article-detail {
            background: #fff;
            padding: 15px;

            .title {
                font-weight: bolder;
            }

            .user-info {
                margin-top: 15px;
                display: flex;
                border-bottom: 1px solid #ddd;
                padding-bottom: 10px;

                .user-info-detail {
                    margin-left: 10px;

                    .nick-name {
                        text-decoration: none;
                        color: #4e5969;
                        font-size: 15px;
                    }

                    .nick-name:hover {
                        color: var(--link);
                    }

                    .time-info {
                        margin-top: 5px;
                        font-size: 13px;
                        color: var(--text2);
                    }

                    .iconfont {
                        margin-left: 10px;
                    }

                    .iconfont::before {
                        padding-right: 3px;
                    }
                    .btn-edit{
                        .iconfont{
                            font-size: 14px;
                        }
                    }
                }
            }
            
    }
        .comment-panel {
            margin-top: 20px;
            background: #fff;
            padding: 20px;
        }

    }
                .detail {
            letter-spacing: 1px;
            line-height: 22px;
            
            a {
                text-decoration: none;
                color: var(--link);
            }

            img {
                max-width: 90%; 
                max-height: 100%; /* 设置图片最大高度为父容器高度 */
                cursor: pointer;
            }
        }
}

.quick-panel {
    position: fixed;
    width: 50px;
    top:200px;
    text-align: center;
    .el-badge__content.is-fixed{
        top:5px;
        right:15px;
    }
    .quick-item{
        display: flex;
        width: 50px;
        height: 50px;
        justify-content: center;
        align-items:center;
        border-radius:50% ;
        background:#fff ;
        margin-bottom:30px ;
        cursor: pointer;
        .iconfont{
            font-size: 22px;
            color: var(--text2);
        }
        .have-like{
            color:red;
        }
    }
}
.toc-panel{
    position: absolute;
    top:45px;
    right: 0px;
    width: 285px;
    
    .top-containner{
        width:285px;
        position: fixed;
        background: #fff;
        .toc-title{
            border-bottom: 1px solid #ddd;
            padding: 10px;
        }
        .toc-list{
            max-height: calc( 100vh - 200px );
            overflow: auto;
            padding: 5px;
            .no-toc{
                text-align: center;
                color: #5f5d5d;
                line-height: 40px;
                font-size: 13px;
            }
            .toc-item{
                cursor: pointer;
                display: block;
                line-height: 35px;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                color: #555666;
                border-radius: 3px;
                font-size: 14px;
                border-left: 2px solid #fff;
            }
            .toc-item:hover{
                background: #eeeded;
            }
            .active{
                border-left: 2px solid #6ca1f7;
                border-radius: 0px 3px 3px 0px;
                background: #eeeded;    
            }
        }
    }
}
</style>
