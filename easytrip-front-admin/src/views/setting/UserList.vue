<template>
  <div>
    <div class="top-panel">
      <el-card>
        <el-form :model="searchForm" label-width="70px" label-position="right">
          <el-row>
            <el-col :span="5">
              <el-form-item label="账号" label-width="40px">
                <el-input
                  class="password-input"
                  v-model="searchForm.userNameFuzzy"
                  clearable
                  placeholder="支持模糊搜索"
                  @keyup.enter.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="手机号">
                <el-input
                  class="password-input"
                  v-model="searchForm.phoneFuzzy"
                  clearable
                  placeholder="支持模糊搜索"
                  @keyup.enter.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4" :style="{ paddingLeft: '10px' }">
              <el-button type="success" @click="loadDataList()"
                >查询
              </el-button>
              <el-button
                v-has="proxy.PermissionCode.account.edit"
                type="primary"
                @click="showEdit()"
                >新增账号
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
        <template #slotStatus="{ index, row }">
          <span style="color: red" v-if="row.status == 0">禁用</span>
          <span style="color: green" v-else>启用</span>
        </template>

        <template #slotOperation="{ index, row }">
          <div class="row-op-panel">
            <a
              v-has="proxy.PermissionCode.account.edit"
              href="javascript:void(0)"
              @click="showEdit(row)"
              >修改</a
            >
            <a
              v-has="proxy.PermissionCode.account.updatePwd"
              href="javascript:void(0)"
              @click="showPasswordEdit(row)"
              >修改密码</a
            >
            <a
              v-has="proxy.PermissionCode.account.updateStatus"
              href="javascript:void(0)"
              @click="changeAccountStatus(row)"
              >{{ row.status == 0 ? "启用" : "禁用" }}</a
            >
            <a
              v-has="proxy.PermissionCode.account.del"
              href="javascript:void(0)"
              @click="delAccount(row)"
              >删除</a
            >
          </div>
        </template>
      </Table>
    </el-card>

    <!--新增修改-->
    <UserEdit ref="userEditRef" @reload="loadDataList"></UserEdit>

    <!--新增修改-->
    <UserPasswordEdit ref="userPasswordEditRef"></UserPasswordEdit>
  </div>
</template>
<script setup>
import UserEdit from "./UserEdit.vue";
import UserPasswordEdit from "./UserPasswordEdit.vue";
import { getCurrentInstance, nextTick, ref } from "vue";

const { proxy } = getCurrentInstance();

const api = {
  loadDataList: "/settings/loadAccountList",
  delAccount: "/settings/delAccount",
  updateStatus: "/settings/updateStatus",
};

const tableData = ref({});
const tableOptions = {};
const columns = [
  {
    label: "用户名",
    prop: "userName",
    width: 150,
  },
  {
    label: "手机",
    prop: "phone",
    width: 200,
  },
  {
    label: "拥有角色",
    prop: "roleNames",
  },
  {
    label: "职位",
    prop: "position",
    width: 200,
  },
  {
    label: "创建时间",
    prop: "createTime",
    width: 200,
  },
  {
    label: "状态",
    prop: "status",
    width: 100,
    scopedSlots: "slotStatus",
  },
  {
    label: "操作",
    prop: "operation",
    width: 190,
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
  let result = await proxy.Request({
    url: api.loadDataList,
    params: params,
  });
  if (!result) {
    return;
  }
  Object.assign(tableData.value, result.data);
};

//新增，修改用户
const userEditRef = ref();
const showEdit = (data = {}) => {
  userEditRef.value.showEdit(Object.assign({}, data));
};

//修改密码
const userPasswordEditRef = ref();
const showPasswordEdit = (data) => {
  userPasswordEditRef.value.showEdit(Object.assign({}, data));
};

//删除用户
const delAccount = (data) => {
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

const changeAccountStatus = (data) => {
  let status = data.status == 0 ? 1 : 0;
  let info = status == 0 ? "禁用" : "启用";
  proxy.Confirm(`确认要【${info}】【${data.userName}】吗？`, async () => {
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
