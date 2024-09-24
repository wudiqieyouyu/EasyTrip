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
                  <el-button
                    type="primary"
                    @click="showEdit()"
                    v-has="proxy.PermissionCode.sales.edit"
                    >新增景点
                  </el-button>
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
          <!--封面-->
          <template #slotCover="{ index, row }">
            <Cover
              :cover="row.imgCover"
              :width="100"
              :height="100"
            ></Cover>
          </template>
          <template #slotTitle="{ index, row }">
            <a
              href="javascript:void(0)"
              class="a-link"
              @click="showDetail(row)"
              >{{ row.salesName }}</a
            >
          </template>
          <template #slotOperation="{ index, row }">
            <div class="row-op-panel">
              <a
                href="javascript:void(0)"
                @click="showEdit(row)"
                v-if="
                  (row.status == 0 && row.createUserId == userInfo.userId) ||
                  userInfo.superAdmin
                "
                v-has="proxy.PermissionCode.article.edit"
                >修改</a
              >
              <a
                href="javascript:void(0)"
                @click="delSale(row)"
                v-if="
                  row.status == 0 &&
                  (row.createUserId == userInfo.userId || userInfo.superAdmin)
                "
                v-has="proxy.PermissionCode.article.del"
                >删除</a>

            </div>
          </template>
        </Table>
      </el-card>
      <SalesEdit ref="saleEditRef" @reload="loadDataList"></SalesEdit>
  
    </div>
  </template>
  <script setup>
  import SalesEdit from "./SalesEdit.vue";
  import { getCurrentInstance, nextTick, ref } from "vue";
  const { proxy } = getCurrentInstance();
  const api = {
    loadDataList: "/sales/loadSalesList",
    delSale: "/sales/delSale",
    delSaleBatch: "/sales/delSaleBatch",
 
  };
  //用户信息
  const userInfo = ref(JSON.parse(sessionStorage.getItem("userInfo")) || {});
  
  const tableData = ref({});
  const tableOptions = {
    selectType: "checkbox",
  };
  const columns = [  
    {
      label: "景区编号",
      prop: "salesId",
      width: 150,
    },
    {
      label: "封面",
      prop: "imgCover",
      scopedSlots: "slotCover",
      width: 220,
    },
    {
      label: "景区名",
      prop: "salesName",
      width: 230,
      scopedSlots: "slotTitle",
    },
  
    {
      label: "创建时间",
      prop: "createTime",
      width: 165,
    },
    {
      label: "地址",
      prop: "salesAddress",
      width: 150,
    },
    {
      label: "电话",
      prop: "tel",
      width: 150,
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
      return item.salesId;
    });
  };
  
  const selectedHandler = (row, index) => {
    return true;
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
  
  const saleEditRef = ref();
  const showEdit = (data) => {
    saleEditRef.value.showEditSales(data);
  };
  
  //删除
  const delSale = (data) => {
    proxy.Confirm(`确定要删除【${data.title}】吗？`, () => {
      delSaleDone(data.saleId, api.delSale);
    });
  };
  
  const delSaleBatch = () => {
    proxy.Confirm(`确定要删除这${selectRowList.value.length}条记录吗？`, () => {
      delSaleDone(selectRowList.value.join(","), api.delSaleBatch);
    });
  };
  
  const delSaleDone = async (saleIds, url) => {
    let result = await proxy.Request({
      url: url,
      params: {
        saleIds,
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("删除成功");
    loadDataList();
  };
  


  const showDetailRef = ref();
  const showDetail = (row) => {
    // showDetailRef.value.show(row.articleId, searchForm.value);
    const url= proxy.globalInfo.webUrl+"article/"+row;
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
  