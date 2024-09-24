<template>
  <div>
    <div class="top-panel">
      <el-card>
        <el-button
          v-has="proxy.PermissionCode.app.app_carousel_edit"
          type="primary"
          @click="showEdit()"
          >新增轮播图
        </el-button>
      </el-card>
    </div>
    <el-card class="table-data-card">
      <Table
        :columns="columns"
        :fetch="loadDataList"
        :dataSource="tableData"
        :options="tableOptions"
        :showPagination="false"
      >
        <template #slotImgPath="{ index, row }">
          <Cover :cover="row.imgPath" :width="330" :height="180"></Cover>
        </template>
        <template #slotObjectType="{ index, row }">
          {{ OBJEC_TYPE_MAP[row.objectType] }}
        </template>
        <template #slotObject="{ index, row }">
          <div v-if="row.objectType == 3">{{ row.outerLink }}</div>
          <div v-else>{{ row.objectId }}</div>
        </template>
        <template #slotOperation="{ index, row }">
          <div class="row-op-panel">
            <a
              v-has="proxy.PermissionCode.app.app_carousel_edit"
              href="javascript:void(0)"
              @click="showEdit(row)"
              >修改</a
            >
            <a
              v-has="proxy.PermissionCode.app.app_carousel_edit"
              href="javascript:void(0)"
              @click="del(row)"
              >删除</a
            >
            <a
              v-has="proxy.PermissionCode.app.app_carousel_edit"
              href="javascript:void(0)"
              :class="[index == 0 ? 'not-allow' : 'a-link']"
              @click="changeSort(index, 'up')"
              >上移</a
            >
            <a
              v-has="proxy.PermissionCode.app.app_carousel_edit"
              href="javascript:void(0)"
              :class="[
                index == tableData.list.length - 1 ? 'not-allow' : 'a-link',
              ]"
              @click="changeSort(index, 'down')"
              >下移</a
            >
          </div>
        </template>
      </Table>
    </el-card>
    <CarouselEdit ref="carouselEditRef" @reload="loadDataList"></CarouselEdit>
  </div>
</template>
<script setup>
import CarouselEdit from "./CarouselEdit.vue";
import { getCurrentInstance, nextTick, ref } from "vue";

const { proxy } = getCurrentInstance();
const api = {
  loadDataList: "/webCarousel/loadDataList",
  saveCarousel: "/webCarousel/saveCarousel",
  delCarousel: "/webCarousel/delCarousel",
  changeSort: "/webCarousel/changeSort",
};
const OBJEC_TYPE_MAP = {
  0: "文章",
  1: "景点",
  2: "酒店住宿",
  3: "外部连接",
};
const searchFormData = ref({});

const tableData = ref({});
const tableOptions = {};
const columns = [
  {
    label: "轮播图",
    prop: "imgPath",
    scopedSlots: "slotImgPath",
  },
  {
    label: "类型",
    prop: "objectType",
    scopedSlots: "slotObjectType",
  },
  {
    label: "主体ID/链接",
    prop: "object",
    scopedSlots: "slotObject",
  },
  {
    label: "操作",
    prop: "operation",
    scopedSlots: "slotOperation",
  },
];

const searchForm = ref({});

const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  if (searchFormData.value.createTimeRange) {
    params.createTimeStart = searchFormData.value.createTimeRange[0];
    params.createTimeEnd = searchFormData.value.createTimeRange[1];
  }
  delete params.createTimeRange;
  Object.assign(params, searchForm.value);
  let result = await proxy.Request({
    url: api.loadDataList,
    params: params,
  });
  if (!result) {
    return;
  }
  tableData.value.list = result.data;
};

const carouselEditRef = ref();
const showEdit = (data) => {
  carouselEditRef.value.showEdit(data);
};

//删除用户
const del = (data) => {
  proxy.Confirm(`确定要删除吗？`, async () => {
    let result = await proxy.Request({
      url: api.delCarousel,
      params: {
        carouselId: data.carouselId,
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("删除成功");
    loadDataList();
  });
};

//修改排序
const changeSort = async (index, type) => {
  let dataList = tableData.value.list;
  if (
    (type === "down" && index === dataList.length - 1) ||
    (type === "up" && index === 0)
  ) {
    return;
  }
  let temp = dataList[index];
  let number = type == "down" ? 1 : -1;
  dataList.splice(index, 1);
  dataList.splice(index + number, 0, temp);

  let carouselIds = [];
  dataList.forEach((element) => {
    carouselIds.push(element.carouselId);
  });

  let result = await proxy.Request({
    url: api.changeSort,
    params: {
      carouselIds: carouselIds.join(","),
    },
  });

  if (!result) {
    return;
  }
  proxy.Message.success("重新排序成功");
  loadDataList();
};
</script>
<style lang="scss" scoped>
.check-span-item {
  float: left;
  margin-right: 10px;
  line-height: 20px;
}
</style>
