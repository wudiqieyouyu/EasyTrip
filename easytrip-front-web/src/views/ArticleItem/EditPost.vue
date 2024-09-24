<template>
  <div class="edit-post">
    <el-form
      :model="formData"
      :rules="rules"
      ref="formDataRef"
      class="post-panel"
      label-width="60px"
    >
    <div class="post-editor">
        <el-card :body-style="{padding:'5px'}">
            <template #header>
                <div class="post-editor-title">
                    <span>正文</span>
                <div class="change-editor-type">
                    <span class="iconfont icon-change" @click="changeEditor">
                        切换为{{ editorType==0?"markdown编辑器":"富文本编辑器" }}
                    </span>
                </div>
                </div>
            </template>
           
            <!--input输入-->
              <el-form-item prop="content" label-width="0" >
                 <EditorHtml :height="htmlEditorHeight" v-if="editorType==0" v-model="formData.content"></EditorHtml>
                 <EditorMarkdown :height="markdownHeight" v-if="editorType==1" v-model="formData.markdownContent" @htmlContent="setHtmlContent"></EditorMarkdown>
              </el-form-item>
        </el-card>

    </div>
    <div class="post-setting">
        <el-card :body-style="{padding:'5px'}">
            <template #header>
                <span>设置</span>
            </template>
            <div class="setting-inner">
            <!--input输入-->
            <el-form-item label="标题" prop="title" >
            <el-input clearable :maxlength="150" placeholder="请输入标题" v-model.trim="formData.title"></el-input>
            </el-form-item>
            <el-form-item prop="regionIds" label="地区">
                <el-cascader 
                placeholder="请选择地区"
                :options="regionList"
                :props="regionProps"
                clearable
                v-model="formData.regionIds"
                :style="{width:'100%'}">
                </el-cascader>
            </el-form-item>
            <el-form-item prop="cover" label="封面">
                <CoverUpload v-model="formData.cover"></CoverUpload>
            </el-form-item>
            <!--textarea输入-->
            <el-form-item label="摘要" prop="summary">
              <el-input
                clearable
                placeholder="提示信息"
                type="textarea"
                :rows="5"
                :maxlength="150"
                resize="none"
                show-word-limit
                v-model="formData.summary"
              ></el-input>
            </el-form-item>
            <el-form-item label="" prop="">
               <el-button type="primary" :style="{width:'100%'}" @click="postHandler">保存</el-button>
            </el-form-item>
            </div>
           
        </el-card>
    </div>
    </el-form>
    </div>
</template>

<script setup>
import {ref,reactive, getCurrentInstance,watch, nextTick} from "vue";
import { useRouter ,useRoute} from "vue-router";
import {ElMessageBox} from "element-plus";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const markdownHeight = window.innerHeight-60-69;
const htmlEditorHeight = window.innerHeight-60-152;

const api={
    loadRegionList:"/article/loadRegion4Post",
    postArticle:"/article/postArticle",
    articleDetail4Update:"/article/articleDetail4Update",
    updateArticle:"/article/updateArticle",
}

const articleId = ref(null);
const getArticleDetail=()=>{
    nextTick(async ()=>{
        formDataRef.value.resetFields();
        if(articleId.value){
            //修改
           let result = await proxy.Request({
              url:api.articleDetail4Update,
              params:{
                articleId:articleId.value,
              },  
              showError:false,
              errorCallback:(response)=>{
                ElMessageBox.alert(response.info,"错误",{
                    "show-close":false,
                    callback:(action)=>{
                        router.go(-1);
                    },
                });
              },
           });
         
           if(!result){
             return;
           }
           let articleInfo = result.data.tripArticleVo;
           //设置编辑器
           editorType.value =articleInfo.editorType;
           //设置地区信息
           articleInfo.regionIds = [];
           articleInfo.regionIds.push(articleInfo.pRegionId);
           if (articleInfo.regionId!=null&&articleInfo.regionId!=0) {
            articleInfo.regionIds.push(articleInfo.regionId);
           }
           //设置封面信息
           if (articleInfo.cover) {
            articleInfo.cover = {imageUrl:articleInfo.cover};
           }
           
           formData.value = articleInfo;
        }else{
            //新增
            formData.value={};
            editorType.value =proxy.VueCookies.get("editorType")||0;
        }
    })
}
//设置markdown编辑器的富文本信息
const setHtmlContent=(htmlContent)=>{
    formData.value.content =htmlContent
}

watch(() =>route,
 (newVal, oldVal) => {
    if (newVal.path.indexOf("/editPost")!=-1||newVal.path.indexOf("/newPost")!=-1) {
        articleId.value=  newVal.params.articleId;
        getArticleDetail();
    }
 }, { immediate: true, deep: true });

const formData = ref({});
const formDataRef = ref();
const rules = {
  title: [{ required: true, message: "请输入标题" },
  {max:150,message:"标题太长"}],
  regionIds:[{required:true,message:"请选择地区"}],
  content:[{required:true,message:"请输入正文"}],
  summary:[{max:200,message:"请输入简介"}],
};
//提交信息
const postHandler = ()=>{
    formDataRef.value.validate(async(valid)=>{
        if(!valid){
            return;
        }
        let params = {};
        Object.assign(params,formData.value);
        //设置地区
        if (params.regionIds.length==1) {
            params.pRegionId=params.regionIds[0];
        }else if(params.regionIds.length==2){
            params.pRegionId=params.regionIds[0];
            params.regionId=params.regionIds[1];
        }else{
            params.pRegionId=params.regionIds[params.regionIds.length-2];
            params.regionId=params.regionIds[params.regionIds.length-1];
        }
        delete params.regionIds;
        //设置编辑器类型
        params.editorType = editorType.value;
        //获取内容
        const contentText = params.content.replace(/<(?!img).*?>/g,"");
        if(contentText==""){
            proxy.message.warning("正文不能为空");
            return;
        }
        //封面
        if (!(params.cover instanceof File)) {
            delete params.cover;
        }
        let result = await proxy.Request({
           url:params.articleId?api.updateArticle:api.postArticle,
           params:params
        })
        if(!result){
          return;
        }
        proxy.Message.success("保存成功");
        router.push(`/article/${result.data}`);
    })
}

//板块信息
const regionProps={
    multiple:false,
    checkStrictly:true,
    value:"regionId",
    label:"regionName"

}
const regionList =ref([]); 
const loadRegionList=async()=>{
    let result = await proxy.Request({
       url:api.loadRegionList,
    })
    if(!result){
      return;
    }
    regionList.value=result.data;
}
loadRegionList();

//编辑器类型 0:是富文本 1:是markdown
const editorType = ref(null);
const changeEditor =()=>{
    proxy.Confirm("切换编辑器会清空正在编辑的内容,确定要切换吗?",()=>{
        editorType.value = editorType.value==0?1:0;
        formData.value.content="";
        formData.value.markdownContent="";
        proxy.VueCookies.set("editorType",editorType.value,-1);

    })
} 
</script>

<style lang="scss" >
.edit-post{
    .post-panel{
        display: flex;
        .el-card-header{
            padding:10px;
        }
        .post-editor{
            flex:1;
            .post-editor-title{
                display: flex;
                justify-content: space-between;
                .change-editor-type{
                    .iconfont{
                        cursor: pointer;
                        color:var(--link);
                        font-size: 14px;
                    }

                }
            }
        }
        .post-setting{
            margin-left: 10px;
            width: 450px;
            .setting-inner{
                max-height: calc(100vh - 120px);
                overflow:auto;
            }
        }
    }
}
</style>
