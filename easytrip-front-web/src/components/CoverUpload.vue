<template>
  <div class="cover-upload">
    <el-upload
    name="file"
    :show-file-list="false"
    accept=".png,.PNG,.jpg,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"
    :multiple="false"
    :http-request="uploadImage"
    >
    <div class="cover-upload-btn">
        <template v-if="localFile">
        <img :src="localFile" />
        </template>
        <template v-else>
            <img :src="(imageUrlPrefix?imageUrlPrefix:proxy.globalInfo.imageUrl)+modelValue.imageUrl" v-if="modelValue&&modelValue.imageUrl"/>
            <span v-else class="iconfont icon-add"></span>
        </template>
    </div>
    </el-upload>
  </div>
</template>

<script setup>
import {ref, getCurrentInstance} from "vue";
import { useRouter ,useRoute} from "vue-router";
const{proxy} = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const props=defineProps({
    imageUrlPrefix:{
        type:String,
    },
    modelValue:{
        type:Object,
        default:null,
    },

});
const localFile = ref(null);
const emit = defineEmits();
const uploadImage= async (file)=>{
    file = file.file
    let img =new FileReader();
    img.readAsDataURL(file);
    img.onload=({target})=>{
    localFile.value = target.result;
    };
    emit("update:modelValue",file);
};
</script>

<style lang="scss" >
.cover-upload{
    .cover-upload-btn{
        background: rgb(251, 251, 251);
        width: 150px;
        height: 150px;
        display: flex;
        align-items: center;
        justify-content: center;
        .iconfont{
            font-size: 50px;
            color:#ddd;
        }
        img{
            width:100%;
        }
    }
}
</style>
