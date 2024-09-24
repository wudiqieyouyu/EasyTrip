<template>
    <div>
        <div class="header" v-if="showHeader">
            <div class="header-content" :style="{ width: proxy.globalInfo.bodyWidth + 'px' }">
                <router-link to="/" class="logo">
                    <span v-for="item in logoInfo" :style="{ color: item.color }" :key="item">
                        {{ item.letter }}
                    </span>
                </router-link>
                <!--板块信息-->
                <div class="menu-panel">
                    <div :class="['menu-item home',boardId===0?'active':'']"  @click="toIndex()">
                    <span class="fontsize">
                      首页
                    </span>
                    </div>
                    <template v-for="board in boardList">
                        <el-popover placement="bottom-start" :width="300" trigger="hover" v-if="board.children.length >0">
                            <template #reference>
                                <span class="menu-item " @click="boardClickHandler(board)" :class="['board-item',board.boardId==boardId?'active':'']">{{ board.boardName }}</span>
                            </template>
                            <div class="sub-board-list">
                                <span class="sub-board" v-for ="subBoard in board.children" @click="subBoardClickHandler(board,subBoard)">{{ subBoard.boardName }}</span>
                            </div>
                        </el-popover>
                        <div class="menu-item " @click="boardClickHandler(board)" :class="['board-item',board.boardId==boardId?'active':'']" v-else><span class="fontsize">{{ board.boardName }}</span></div>
                    </template>
                </div>
                <!--登录,注册用户信息-->
                <div class="user-info-panel">
                    <div class="op-btn">
                        <el-button type="primary" class="op-btn" @click="newPost">
                            发帖<span class="iconfont icon-add"></span>
                        </el-button>
                        <!-- <el-button type="primary" class="op-btn">
                            登录<span class="iconfont icon-search"></span>
                        </el-button> -->
                    </div>
                    <template v-if="userInfo.userId">
                        <div class="message-info">
                            <el-dropdown>
                                <el-badge :value="messageCountInfo.total" class="item" :hidden="messageCountInfo.total==null||messageCountInfo.total==0">
                                    <div class="iconfont icon-message"></div>
                                </el-badge>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item @click="gotoMessage('reply')" class="message-item"><span class="text">回复我的</span><span  class="count-tag" v-if="messageCountInfo.reply>0">{{ messageCountInfo.reply>99?'99+':messageCountInfo.reply }}</span></el-dropdown-item>
                                        <el-dropdown-item @click="gotoMessage('likePost')" class="message-item"><span class="text">赞了我的文章</span><span  class="count-tag" v-if="messageCountInfo.likePost>0">{{ messageCountInfo.likePost>99?'99+':messageCountInfo.likePost }}</span></el-dropdown-item>
                                        <el-dropdown-item @click="gotoMessage('likeComment')" class="message-item"><span class="text">赞了我的评论</span><span  class="count-tag" v-if="messageCountInfo.likeComment>0">{{ messageCountInfo.likeComment>99?'99+':messageCountInfo.likeComment }}</span></el-dropdown-item>
                                        <el-dropdown-item @click="gotoMessage('sys')" class="message-item"><span class="text">系统消息</span><span  class="count-tag" v-if="messageCountInfo.sys>0">{{ messageCountInfo.sys>99?'99+':messageCountInfo.sys }}</span></el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                        </div>
                        <div class="user-info">
                            <el-dropdown>
                                <Avatar :userId="userInfo.userId" :width="50" ></Avatar>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item @click="gotoUcenter(userInfo.userId)">我的主页</el-dropdown-item>
                                        <el-dropdown-item @click="logout()">退出</el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                        </div>
                    </template>
                    <el-button-group :style="{ 'margin-left': '10px' }" v-else>
                        <el-button type="primary" plain @click="loginAndRegister(1)">登录</el-button>
                        <el-button type="primary" plain @click="loginAndRegister(0)">注册</el-Button>
                    </el-button-group>
                </div>
            </div>
        </div>
        <div class="body-content">
            <router-view></router-view>
        </div>
        <div class="footer">
            <div class="footer-content"
            :style="{ width: proxy.globalInfo.bodyWidth + 'px' }">
            <el-row>
            <el-col :span="6" class="item">
                <div class="logo">
                <div class="logo-letter">
                    <span v-for="item in logoInfo" :style="{ color: item.color }" :key="item">
                        {{ item.letter }}
                    </span>
                </div>
                <div class="info">一个实用的旅游网站</div>
            </div>
            </el-col>
            <el-col :span="6" class="item">
                <div class="title">网站相关 

                    <div >
    <a target="_blank" style="color: #5c6b77" href="https://beian.miit.gov.cn/">鄂ICP备2022014799号</a>&nbsp;
</div>
                </div>
  
            </el-col>
            <el-col :span="6" class="item">
                <div class="title">友情链接 </div>
               <div><a class="a-link" href="https://www.baidu.com">百度 </a></div> 
               <div><a class="a-link" href="https://www.mafengwo.cn">马蜂窝</a></div>
            </el-col>
            <el-col :span="6" class="item">
                <div class="title">联系我 </div>
                
            </el-col>
            </el-row>
            </div>
        </div>
        <LoginAndRegister ref="loginRegisterRef"></LoginAndRegister>
        <!--回到顶部-->
        <el-backtop :right="100" :bottom="100"></el-backtop>
        <div>
        </div>
    </div>
    <!-- <div style="position:fixed;text-align:center;bottom:0;margin:0 auto;width:100%;color: #5c6b77">
    <a target="_blank" style="color: #5c6b77" href="https://beian.miit.gov.cn/">鄂ICP备2022014799号</a>&nbsp;
</div> -->
</template>

<script setup>
import LoginAndRegister from "./LoginAndRegister.vue";
import { ref, reactive, getCurrentInstance, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
import { LOCAL_STORAGE_KEY } from "@/utils/Constants.js";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const store = useStore();

const api = {
    getUserInfo: "/account/getUserInfo",
    loadBoard: "/index/loadBoard",
    autoLogin:"/account/autoLogin",
    loadMessageCount:"/ucenter/getMessageCount"
}

const logoInfo = ref([
    {
        letter: "E",
        color: "#3285FF",
    },
    {
        letter: "a",
        color: "#FB3624",
    },
    {
        letter: "s",
        color: "#FFBA02",
    },
    {
        letter: "y",
        color: "#3285FF",
    },
    {
        letter: "T",
        color: "#25B24E",
    },
    {
        letter: "r",
        color: "#FD3224",
    },
    {
        letter: "i",
        color: "#3285FF",
    },
    {
        letter: "p",
        color: "#FFBA02",
    },
])

const showHeader = ref(true);
//获取滚动条的高度
const getScrollTop = () => {
    let scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
    return scrollTop;
};
const initScroll = () => {
    let initScrollTop = getScrollTop();
    let scrollType = 0;
    window.addEventListener("scroll", () => {
        let currentScrollTop = getScrollTop();
        if (currentScrollTop > initScrollTop) {
            //往下滚动
            scrollType = 1;
        } else {
            scrollType = 0;
        }
        initScrollTop = currentScrollTop;
        if (scrollType == 1 && currentScrollTop > 120) {
            showHeader.value = false;
        } else {
            showHeader.value = true
        }
    })
}
//登录注册
const loginRegisterRef = ref();
const loginAndRegister = (type) => {
    loginRegisterRef.value.showPanel(type);
}
onMounted(() => {
    initScroll();
    loadBoard();
    autoLogin();
});
//获取用户信息
const getUserInfo = async () => {
    let result = await proxy.Request({
        url: api.getUserInfo,
    });
    if (!result) {
        return;
    }
    store.commit("updateLoginUserInfo", result.data);
};
//自动登录
const autoLogin = async () => {
    let result = await proxy.Request({
        url: api.autoLogin,
    });
    if (!result) {
        return;
    }
    if (result.data == null) {
        return;
    }
    localStorage.setItem(LOCAL_STORAGE_KEY.token.key, result.data);
    let getUserResult = await proxy.Request({
    url:api.getUserInfo,
  })    
    if (!getUserResult) {
        return;
    }
    store.commit("updateLoginUserInfo",getUserResult.data);
};
//监听用户信息
const userInfo = ref({})
watch(() => store.state.loginUserInfo,
    (newVal, oldVal) => {
        if (newVal != undefined && newVal != null) {
            userInfo.value = newVal;
            loadMessageCount();
            console.log(userInfo.value.userId)
        } else {
            userInfo.value = {};
        }
    }, { immediate: true, deep: true });

//监听是否展示登录框
watch(() => store.state.showLogin,
    (newVal, oldVal) => {
        if (newVal) {
            loginAndRegister(1);
        }
    }, { immediate: true, deep: true });


//获取板块信息
const boardList = ref([]);
const loadBoard = async () => {
    let result = await proxy.Request({
        url: api.loadBoard,
    });
    if (!result) {
        return;
    }
    boardList.value = result.data;
    store.commit("saveBoardList",result.data)
};
//首页
const toIndex = ()=>{
    boardId.value = 0;
    router.push('/')
}
//板块id
const boardId =ref(0);
//板块点击
const boardClickHandler=(board)=>{
    boardId.value = board.boardId;
    router.push(board.cover)
}
//二级板块
const subBoardClickHandler=(board,subBoard)=>{
    boardId.value = board.boardId;
    router.push(board.cover+subBoard.cover)
}

//发帖
const newPost=()=>{
    if (!store.getters.getLoginUserInfo) {
        loginAndRegister(1);
    }else{
        router.push("/newPost");
    }
}

//消息相关
const gotoMessage=(type)=>{
    router.push(`/user/message/${type}`)
}

const messageCountInfo =ref({});
const loadMessageCount = async()=>{
    let result = await proxy.Request({
       url:api.loadMessageCount,

    })
    if(!result){
      return;
    }
    messageCountInfo.value = result.data;
    store.commit("updateMessageCountInfo",result.data);
}

watch(() =>store.state.messageCountInfo,
 (newVal, oldVal) => {
    messageCountInfo.value = newVal||{};
 }, { immediate: true, deep: true });


//前往个人中心
const gotoUcenter=(userId)=>{
    router.push(`/user/${userId}`)
}
//退出
const logout =()=>{
    proxy.Confirm("确定要退出吗?",()=>{
        localStorage.removeItem('token');
        store.commit("updateLoginUserInfo");
    })
}
</script>

<style lang="scss" scoped>
.header {
    top:0px;
    width: 100%;
    height: 60px;
    position: fixed;
    box-shadow: 0 2px 6px 0 #ddd;
    z-index: 1000;
    background: #fff;

    .header-content {
        margin: 0px auto;
        align-items: center;
        height: 60px;
        display: flex;
        align-items: center;

        .logo {
            display: block;
            text-decoration: none;
            margin-right: 5px;

            span {
                font-size: 35px;
            }

        }

        .menu-panel {
            flex: 1;
            height: 60px;
            .active{
               background: #e4e4e4;
            }
            .menu-item{
                width: 80px;
                height: 100%;
                display: inline-flex;
                margin-left:50px;
                align-items: center;
                cursor: pointer;
                .fontsize{
                    margin:auto;
                }
            }
            .home{
                text-decoration: none;
                color: #000;
            }
        }

        .user-info-panel {
            width: 300px;
            display: flex;
            align-items: center;

            .op-btn {
                .iconfont {
                    margin-left: 4px;
                }

                .el-button+.el-button {
                    margin-left: 5px;
                    ;
                }
            }

            .message-info {
                margin-left: 5px;
                margin-right:25px;
                cursor: pointer;
                .icon-message {
                    font-size: 25px;
                    color: rgb(147, 147, 147);
                }


            }


        }
    }
}

.sub-board-list{
    display: flex;
    flex-wrap:wrap;
    .sub-board{
        padding: 0px 10px;
        border-radius: 20px;
        margin-right: 10px;
        background: rgb(239, 239, 239);
        border: 1px solid #ddd;
        color: rgb(135,134,134);
        margin-top:10px;
        cursor: pointer;
    }
}

.body-content{
    margin-top: 60px;
    position: relative;
    min-height: calc(100vh - 210px);
}
.menu-item:hover{
    background: #e4e4e4;
}
.message-item{
                    display:flex;
                    justify-content: space-around;
                    .text{
                        flex: 1;
                    }
                    .count-tag{
                        display: inline-block;
                        background: #f56c6c;
                        height: 15px;
                        line-height: 15px;
                        min-width: 20px;
                        border-radius: 10px;
                        font-size: 13px;
                        text-align: center;
                        color: #fff;
                        margin-left: 10px;
                    }
                }

.footer{
    background: #e9e9e9;
    height: 140px;
    margin-top: 10px;
    .footer-content{
        margin:0px auto;
        padding-top: 10px;
        .item{
            text-align: left;
            .title{
                font-size: 18px;
                margin-bottom:10px;

            }
            a{
                font-size: 14px;
                text-decoration: none;
                color:var(--text2);
                line-height: 25px;
            }
        }
        .logo{
            .logo-letter{
                font-size: 30px;
            }
            .info{
                margin-top:10px;
                color: rgb(93, 91, 91);
            }
        }
    }
}
</style>
