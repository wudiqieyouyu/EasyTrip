<template>
  <div>
    <div class="top-panel">
      <el-card>
        <el-form :model="searchForm" label-width="70px" label-position="right">
          <el-row :gutter="5">
            <el-col :span="5">
              <el-form-item label="加入日期" label-width="70px">
                <el-date-picker
                  v-model="searchForm.joinTimeRange"
                  type="daterange"
                  range-separator="~"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="YYYY-MM-DD"
                  @change="loadDataList"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="手机号" label-width="60px">
                <el-input
                  class="password-input"
                  v-model="searchForm.phoneNumberFuzzy"
                  clearable
                  placeholder="支持模糊搜索"
                  @keyup.enter.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="登录ip">
                <el-input
                  class="password-input"
                  v-model="searchForm.lastLoginIp"
                  clearable
                  placeholder="不支持模糊搜索"
                  @keyup.enter.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4" :style="{ paddingLeft: '10px' }">
              <el-button type="success" @click="loadDataList()"
                >查询
              </el-button>
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
        <template #slotNickName="{ index, row }">
          <span>{{ row.nickName }}</span>
          <span v-if="row.sex == 0">(女)</span>
          <span v-if="row.sex == 1">(男)</span>
        </template>
        <template #slotStatus="{ index, row }">
          <span style="color: red" v-if="row.status == 0">禁用</span>
          <span style="color: green" v-else>启用</span>
        </template>

        <template #slotOperation="{ index, row }">
          <div class="row-op-panel">
            <a
              v-has="proxy.PermissionCode.app.app_user_edit"
              href="javascript:void(0)"
              @click="changeAccountStatus(row)"
              >{{ row.status == 0 ? "启用" : "禁用" }}</a
            >
          </div>
        </template>
      </Table>
    </el-card>
  </div>
</template>
<script setup>
import { getCurrentInstance, nextTick, ref } from "vue";

const { proxy } = getCurrentInstance();

const api = {
  loadDataList: "/webUser/loadDataList",
  updateStatus: "/webUser/updateStatus",
};

const tableData = ref({});
const tableOptions = {};
const columns = [
  {
    label: "手机号",
    prop: "phonenumber",
    width: 200,
  },
  {
    label: "昵称",
    prop: "nickName",
    width: 160,
    scopedSlots: "slotNickName",
  },
  {
    label: "个人描述",
    prop: "personDescription",
    width: 200,
  },
  {
    label: "加入时间",
    prop: "joinTime",
    width: 180,
  },
  {
    label: "最后登录时间",
    prop: "lastLoginTime",
    width: 180,
  },
  {
    label: "最后登录IP",
    prop: "lastLoginIp",
    width: 150,
  },
  {
    label: "状态",
    prop: "status",
    width: 80,
    scopedSlots: "slotStatus",
  },
  {
    label: "操作",
    prop: "operation",
    width: 80,
    scopedSlots: "slotOperation",
  },
];

const searchForm = ref({});
const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  Object.assign(params, searchForm.value);
  if (searchForm.value.joinTimeRange) {
    params.joinTimeStart = searchForm.value.joinTimeRange[0];
    params.joinTimeEnd = searchForm.value.joinTimeRange[1];
  }
  delete params.createTimeRange;
  let result = await proxy.Request({
    url: api.loadDataList,
    params: params,
  });
  if (!result) {
    return;
  }
  Object.assign(tableData.value, result.data);
};

const changeAccountStatus = (data) => {
  let status = data.status == 0 ? 1 : 0;
  let info = status == 0 ? "禁用" : "启用";
  proxy.Confirm(`确认要【${info}】【${data.nickName}】吗？`, async () => {
    let result = await proxy.Request({
      url: api.updateStatus,
      params: {
        userId: data.userId,
        status: status,
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("操作成功");
    loadDataList();
  });
};
</script>
<style lang="scss" scoped>
</style>
