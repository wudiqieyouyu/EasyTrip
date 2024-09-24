<template>
  <div
    class="cover"
    :style="{ width: width + 'px', height: height ? height + 'px' : 'auto' }"
  >
  <el-image :style="{width:width+'px',height:width+'px'}" fit="fill" loading="lazy"
    :src="proxy.globalInfo.imageUrl+cover"
    v-if="cover">
    </el-image>
    <div class="bg-color" :style="{ background: bgColor }" v-if="bgColor">
      {{ title }}
    </div>
    <el-image
      src="../assets/default_cover.png"
      v-if="cover == '' && bgColor == ''"
      fit="fill"
    />
  </div>
</template>

<script setup>
import { getCurrentInstance, reactive } from "vue";

const props = defineProps({
  cover: {
    type: String,
  },
  bgColor: {
    type: String,
  },
  title: {
    type: String,
  },
  width: {
    type: Number,
    default: 100,
  },
  height: {
    type: Number,
  },
});
const { proxy } = getCurrentInstance();
</script>
<style lang="scss">
.cover {
  background: #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  img {
    width: 100%;
  }
  .bg-color {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
  }
}
</style>