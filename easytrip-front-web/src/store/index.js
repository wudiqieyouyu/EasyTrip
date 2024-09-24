import {createStore} from "vuex";

export default createStore({
    state:{
        //用户信息
        loginUserInfo:null,
        //是否展示登录
        showLogin:false,
        //板块信息
        boardList:[],
        //消息数
        messageCountInfo:{},

    },
    getters:{
        getLoginUserInfo:(state)=>{
            return state.loginUserInfo;
        },
        getBoardList:(state)=>{
            return state.boardList
        },
        getSubBoardList:(state)=>(boardId)=>{
            let board = state.boardIdList.find(item=>{
                return item.boardId==boardId;
            })
            return board?board.children:[];
        },
        getMessageCountInfo:(state)=>{
            return state.messageCountInfo;
        }
    },
    mutations:{
        updateLoginUserInfo(state,value){
            state.loginUserInfo= value;
        },
        showLogin(state,value){
            state.showLogin = value;
        },
        saveBoardList(state,value){
            state.boardList = value;
        },
        //设置消息数
        updateMessageCountInfo(state,value){
            state.messageCountInfo = value;
        },
        readMessage(state,value){
            state.messageCountInfo.total=state.messageCountInfo.total-state.messageCountInfo[value]
            state.messageCountInfo[value]=0;
        }
    },
    actions:{

    },
    modules:{

    },
})