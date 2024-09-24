<template>
  <div>
    <div class="menu-tree">
      <el-row :gutter="10">
        <el-col :span="7">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>菜单管理</span>
                <span
                  class="iconfont icon-add-solid"
                  title="新增菜单"
                  @click="showEditDialog('add', {})"
                ></span>
              </div>
            </template>
            <el-tree
              ref="refTree"
              :data="treeData"
              defaultExpandAll
              node-key="menuId"
              :expand-on-click-node="false"
              :props="treeProps"
              :highlight-current="true"
              @node-click="nodeClick"
              class="tree-panel"
            >
              <template #default="{ node, data }">
                <span class="custom-node-style">
                  <span class="node-title">{{ data.menuName }}</span>
                  <el-dropdown trigger="click">
                    <a class="ant-dropdown-link" @click.prevent>
                      <span
                        class="iconfont icon-more"
                        v-has="proxy.PermissionCode.menu.edit"
                      ></span>
                    </a>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item @click="showEditDialog('add', data)">
                          添加子菜单
                        </el-dropdown-item>
                        <el-dropdown-item
                          v-if="data.pId !== -1"
                          @click="showEditDialog('edit', data)"
                        >
                          修改
                        </el-dropdown-item>
                        <el-dropdown-item
                          v-if="data.pId !== -1"
                          @click="delMenu(data)"
                        >
                          删除
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </span>
              </template>
            </el-tree>
          </el-card>
        </el-col>
        <el-col :span="17">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>菜单详情</span>
              </div>
            </template>
            <el-form
              :model="detailData"
              label-width="100px"
              class="detail-form"
            >
              <el-form-item label="菜单ID：">
                {{ detailData.menuId }}
              </el-form-item>
              <el-form-item label="菜单名称：">
                {{ detailData.menuName }}
              </el-form-item>
              <el-form-item label="菜单层级：">
                <el-breadcrumb
                  separator-class="el-icon-arrow-right"
                  :style="{ 'line-height': '40px' }"
                >
                  <el-breadcrumb-item
                    v-for="(item, index) in detailData.menuNames"
                    :key="index"
                    >{{ item }}
                  </el-breadcrumb-item>
                </el-breadcrumb>
              </el-form-item>
              <el-form-item label="菜单类型：">
                {{ detailData.menuTypeName }}
              </el-form-item>
              <el-form-item label="请求路径：">
                {{ detailData.menuUrl ? detailData.menuUrl : "-" }}
              </el-form-item>
              <el-form-item label="权限编码：">
                {{ detailData.permissionCode }}
              </el-form-item>
              <el-form-item label="菜单图标：">
                <span
                  :class="'iconfont icon-' + detailData.icon"
                  v-if="detailData.icon"
                ></span>
                <span v-else>-</span>
              </el-form-item>
              <el-form-item label="排序号：">
                {{ detailData.sort }}
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <MenuEdit
      ref="menuEditRef"
      @reload="loadTreeData"
      :treeData="treeData"
    ></MenuEdit>
  </div>
</template>
<script setup>
import MenuEdit from "./MenuEdit.vue";
import { getCurrentInstance, nextTick, ref } from "vue";
const { proxy } = getCurrentInstance();

const api = {
  loadMenu: "settings/menuList",
  delMenu: "settings/delMenu",
};

const refTree = ref(null);

const treeProps = {
  class: "cust-tree-item",
  children: "children",
  label: "menuName",
  value: "menuId",
};
//当前的node
const currentNodeKey = ref();
const treeData = ref([]);
//获取菜单
const loadTreeData = async () => {
  let result = await proxy.Request({
    url: api.loadMenu,
  });
  if (!result) {
    return;
  }
  const data = result.data;
  treeData.value = data;

  nextTick(() => {
    let firstNodeData = data[0].children ? data[0].children[0] : data[0];
    let curKey = currentNodeKey.value;
    if (!curKey) {
      curKey = firstNodeData.menuId;
    }
    refTree.value.setCurrentKey(curKey);
    const curNode = refTree.value.getNode(curKey);
    nodeClick(curNode.data, curNode);
  });
};
loadTreeData();

//菜单详情
const detailData = ref({});
//点击菜单
const nodeClick = (data, node) => {
  let menuNames = [];
  getMenuNames(node, menuNames);
  Object.assign(detailData.value, data);
  detailData.value.menuNames = menuNames;
  currentNodeKey.value = data.menuId;
};

//递归获取完整菜单路径
const getMenuNames = (node, menuNames) => {
  if (node.data.menuName) {
    menuNames.unshift(node.data.menuName);
  }
  if (node.parent) {
    getMenuNames(node.parent, menuNames);
  }
};

const delMenu = (data) => {
  proxy.Confirm("确定要删除菜单吗？", async () => {
    let result = await proxy.Request({
      url: api.delMenu,
      params: {
        menuId: data.menuId,
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("删除成功");
    currentNodeKey.value = null;
    loadTreeData();
  });
};

const menuEditRef = ref();
const showEditDialog = (type, data) => {
  let param = null;
  if (data) {
    param = Object.assign({}, data);
  }
  menuEditRef.value.showEditDialog(type, param);
};
</script>
<style lang="scss">
.menu-tree {
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    .iconfont {
      color: #409eff;
      font-size: 20px;
      cursor: pointer;
    }
  }
  .tree-panel {
    overflow: auto;
    height: calc(100vh - 220px);
  }

  .el-tree-node__content {
    height: 40px;
  }

  .custom-node-style {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
  }
}
</style>
