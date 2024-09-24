<template>
  <div>
    <div class="top-panel">
      <el-card>
        <el-form :model="searchForm" label-width="70px" label-position="right">
          <el-row>
            <el-col :span="4">
              <el-form-item label="标题" label-width="40px">
                <el-input
                  class="password-input"
                  v-model="searchForm.titleFuzzy"
                  clearable
                  placeholder="支持模糊搜索"
                  @keyup.enter="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <!-- 下拉框 -->
              <el-form-item label="状态" prop="status" :style="{ width: '100%' }">
                <el-select
                  clearable
                  placeholder="请选择状态"
                  v-model="searchForm.status"
                  @change="loadDataList"
                >
                  <el-option :value="0" label="待发布"></el-option>
                  <el-option :value="1" label="已发布"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label="创建人" prop="nickName" :style="{ width: '100%' }">
                <el-input
                  class="password-input"
                  v-model="searchForm.nickNameFuzzy"
                  clearable
                  placeholder="支持模糊搜索"
                  @keyup.enter="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11" :style="{ paddingLeft: '10px' }">
              <el-button-group>
                <el-button type="success" @click="loadDataList()"
                  >查询
                </el-button>
                <!-- <el-button
                  type="primary"
                  @click="showEdit()"
                  v-has="proxy.PermissionCode.article.edit"
                  >新增文章
                </el-button> -->
                <el-button
                  type="primary"
                  :disabled="selectRowList.length == 0"
                  @click="postArticleBatch"
                  v-has="proxy.PermissionCode.article.post"
                  >批量发布</el-button
                >
                <el-button
                  type="danger"
                  :disabled="selectRowList.length == 0"
                  @click="delArticleBatch"
                  v-has="proxy.PermissionCode.article.del_batch"
                  >批量删除</el-button
                >
              </el-button-group>
            </el-col>
          </el-row>
        </el-form>
      </el-card>
    </div>
    <el-card class="table-data-card">
      <Table
        :columns="columns"
        :fetch="loadDataList"
        :dataSource="tableData"
        :options="tableOptions"
        @rowSelected="rowSelected"
        :selected="selectedHandler"
      >
      <!--用户信息-->
      <template #slotUser="{ index, row }">
        <div class="userInfo">
          <Avatar :userId="row.userId" :width="50" ></Avatar>
          <div class="name-info">
            <a href="javascript:void(0)" class="a-link"  @click="toUserDetail(row.userId)">{{ row.nickName}}</a><br>
            <span>{{ row.userIpAddress }}</span>
        </div>  
        </div>
        </template>
        <!--封面-->
        <template #slotCover="{ index, row }">
          <Cover
            :cover="row.cover"
            :width="100"
            :height="100"
          ></Cover>
        </template>
        <template #slotBoard="{ index, row }">
          <span v-if="row.pBoardName">{{ row.pBoardName }}/</span><span>{{ row.boardName }}</span>
        </template>
        <template #slotTitle="{ index, row }">
          <a
            href="javascript:void(0)"
            class="a-link"
            @click="showDetail(row)"
            >{{ row.title }}</a
          >
        </template>
        <template #slotStatus="{ index, row }">
          <Badge showType="orange" text="待发布" v-if="row.status == 0"></Badge>
          <Badge showType="green" text="已发布" v-if="row.status == 1"></Badge>
        </template>
        <template #slotOperation="{ index, row }">
          <div class="row-op-panel">
            <!-- <a
              href="javascript:void(0)"
              @click="showEdit(row)"
              v-if="
                (row.status == 0 && row.createUserId == userInfo.userId) ||
                userInfo.superAdmin
              "
              v-has="proxy.PermissionCode.article.edit"
              >修改</a
            > -->
            <a
              href="javascript:void(0)"
              @click="delArticle(row)"
              v-if="
                row.status == 0 &&
                (row.createUserId == userInfo.userId || userInfo.superAdmin)
              "
              v-has="proxy.PermissionCode.article.del"
              >删除</a
            >
            <a
              href="javascript:void(0)"
              @click="postArticle(row)"
              v-if="row.status == 0"
              v-has="proxy.PermissionCode.article.post"
              >发布</a
            >
            <a
              href="javascript:void(0)"
              @click="cancelPostArticle(row)"
              v-if="row.status == 1"
              v-has="proxy.PermissionCode.article.post"
              >取消发布</a
            >
          </div>
        </template>
      </Table>
    </el-card>
    <ArticleEdit ref="articleEditRef" @reload="loadDataList"></ArticleEdit>

    <ShowDetail ref="showDetailRef" :showType="3"></ShowDetail>
  </div>
</template>
<script setup>
import ShowDetail from "@/components/content/ShowDetail.vue";
import ArticleEdit from "./ArticleEdit.vue";
import { getCurrentInstance, nextTick, ref } from "vue";
const { proxy } = getCurrentInstance();
const api = {
  loadDataList: "/tripArticle/loadDataList",
  delArticle: "/tripArticle/delArticle",
  delArticleBatch: "/tripArticle/delArticleBatch",
  postArticle: "/tripArticle/postArticle",
  cancelPostArticle: "/tripArticle/cancelPostArticle",
};
//用户信息
const userInfo = ref(JSON.parse(sessionStorage.getItem("userInfo")) || {});

const tableData = ref({});
const tableOptions = {
  selectType: "checkbox",
};
const columns = [  
  {
    label: "用户信息",
    prop: "nickName",
    scopedSlots: "slotUser",
    width: 150,
  },
  {
    label: "封面",
    prop: "cover",
    scopedSlots: "slotCover",
    width: 220,
  },
  {
    label: "标题",
    prop: "title",
    width: 230,
    scopedSlots: "slotTitle",
  },

  {
    label: "创建时间",
    prop: "postTime",
    width: 165,
  },
  {
    label: "板块",
    prop: "pBoardName",
    scopedSlots: "slotBoard",
    width: 150,
  },
  {
    label: "发布地址",
    prop: "userIpAddress",
    width: 80,
  },
  {
    label: "发布状态",
    prop: "status",
    scopedSlots: "slotStatus",
    width: 90,
  },
  {
    label: "操作",
    width: 100,
    scopedSlots: "slotOperation",
  },
];

const searchForm = ref({});

//批量选择
const selectRowList = ref([]);
const rowSelected = (rows) => {
  selectRowList.value = rows.map((item) => {
    return item.articleId;
  });
};

const selectedHandler = (row, index) => {
  return row.status == 0;
};

const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  Object.assign(params, searchForm.value);
  let result = await proxy.Request({
    url: api.loadDataList,
    params: params,
  });
  if (!result) {
    return;
  }
  Object.assign(tableData.value, result.data);
};

const articleEditRef = ref();
const showEdit = (data) => {
  articleEditRef.value.showEditArticle(data);
};

//删除
const delArticle = (data) => {
  proxy.Confirm(`确定要删除【${data.title}】吗？`, () => {
    delArticleDone(data.articleId, api.delArticle);
  });
};

const delArticleBatch = () => {
  proxy.Confirm(`确定要删除这${selectRowList.value.length}条记录吗？`, () => {
    delArticleDone(selectRowList.value.join(","), api.delArticleBatch);
  });
};

const delArticleDone = async (articleIds, url) => {
  let result = await proxy.Request({
    url: url,
    params: {
      articleIds,
    },
  });
  if (!result) {
    return;
  }
  proxy.Message.success("删除成功");
  loadDataList();
};

//发布
const postArticle = (data) => {
  proxy.Confirm(`确定要发布【${data.title}】吗？`, () => {
    postArticleDone(data.articleId);
  });
};

const postArticleBatch = () => {
  proxy.Confirm(`确定要发布这${selectRowList.value.length}条记录吗？`, () => {
    postArticleDone(selectRowList.value.join(","));
  });
};

const postArticleDone = async (articleIds) => {
  let result = await proxy.Request({
    url: api.postArticle,
    params: {
      articleIds,
    },
  });
  if (!result) {
    return;
  }
  proxy.Message.success("发布成功");
  loadDataList();
};

//取消发布
const cancelPostArticle = (data) => {
  proxy.Confirm(`确定要取消发布【${data.title}】吗？`, async () => {
    let result = await proxy.Request({
      url: api.cancelPostArticle,
      params: {
        articleIds: data.articleId,
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("取消发布成功");
    loadDataList();
  });
};
const showDetailRef = ref();
const showDetail = (row) => {
  // showDetailRef.value.show(row.articleId, searchForm.value);
  const url= proxy.globalInfo.webUrl+"article/"+row.articleId;
  window.open(url)
};
const toUserDetail=(userId)=>{
const url=proxy.globalInfo.webUrl+"user/"+userId;
  window.open(url);
}
</script>
<style lang="scss" scoped>
.check-span-item {
  float: left;
  margin-right: 10px;
  line-height: 20px;
}
.userInfo{
  display: flex;
  align-items: center;
  .name-info{
    margin-left: 5px;
    font-size:13px;
  }
}
</style>
