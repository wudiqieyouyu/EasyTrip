<template>
  <div>
    <div class="top-panel">
      <el-card>
        <el-form :model="searchForm" label-width="70px" label-position="right">
          <el-row :gutter="5">
            <el-col :span="4">
              <el-form-item label="日期" label-width="50px">
                <el-date-picker
                  v-model="searchFormData.createTimeRange"
                  type="daterange"
                  range-separator="~"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="YYYY-MM-DD"
                  @change="loadDataList"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <!-- 下拉框 -->
              <el-form-item label="状态" prop="" :style="{ width: '100%' }">
                <el-select
                  clearable
                  placeholder="请选择状态"
                  v-model="searchForm.status"
                  @change="loadDataList"
                >
                  <el-option :value="0" label="待回复"></el-option>
                  <el-option :value="1" label="已回复"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label="创建人" prop="" :style="{ width: '100%' }">
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
            <el-col :span="8" :style="{ paddingLeft: '10px' }">
              <el-button-group>
                <el-button type="success" @click="loadDataList()"
                  >查询
                </el-button>
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
      >
        <template #slotStatus="{ index, row }">
          <Badge showType="orange" text="待回复" v-if="row.status == 0"></Badge>
          <Badge showType="green" text="已回复" v-if="row.status == 1"></Badge>
        </template>
        <template #slotOperation="{ index, row }">
          <div class="row-op-panel">
            <a
              v-has="proxy.PermissionCode.account.edit"
              href="javascript:void(0)"
              @click="showEdit(row)"
              >回复</a
            >
          </div>
        </template>
      </Table>
    </el-card>
    <FeedbackReply
      ref="feedbackReplyRef"
      @reload="loadDataList"
    ></FeedbackReply>
  </div>
</template>
<script setup>
import FeedbackReply from "./FeedbackReply.vue";
import { getCurrentInstance, nextTick, ref } from "vue";

const { proxy } = getCurrentInstance();

const api = {
  loadDataList: "/webFeedback/loadFeedback",
  replyFeedback: "/webFeedback/replyFeedback",
};
const searchFormData = ref({});

const tableOptions = {};
const columns = [
  {
    label: "问题",
    prop: "messageContent",
  },
  {
    label: "创建者",
    prop: "sendNickName",
    width: 150,
  },
  {
    label: "回复状态",
    prop: "status",
    scopedSlots: "slotStatus",
    width: 150,
  },
  {
    label: "创建时间",
    prop: "createTime",
    width: 200,
  },
  {
    label: "操作",
    prop: "operation",
    width: 100,
    scopedSlots: "slotOperation",
  },
];

const searchForm = ref({});

const tableData = ref({});
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
  tableData.value = result.data;
};

const feedbackReplyRef = ref();
const showEdit = (data) => {
  feedbackReplyRef.value.show(data.messageId);
};

//删除用户
const del = (data) => {
  proxy.Confirm(`确定要删除【${data.userName}】吗？`, async () => {
    let result = await proxy.Request({
      url: api.delAccount,
      params: {
        userId: data.userId,
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("删除成功");
    loadDataList();
  });
};
</script>
<style lang="scss" scoped>
.check-span-item {
  float: left;
  margin-right: 10px;
  line-height: 20px;
}
</style>
