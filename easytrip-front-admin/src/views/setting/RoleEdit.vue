<template>
  <Dialog
    ref="editDialog"
    :title="editDialogInfo.title"
    :buttons="editDialogInfo.buttons"
    :show="editDialogInfo.show"
    @close="editDialogInfo.show = false"
  >
    <el-form
      ref="formDataRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="角色名称" prop="roleName">
        <el-input :maxLength="20" v-model="formData.roleName" />
      </el-form-item>

      <el-form-item label="关联菜单" prop="menuIds" v-if="!formData.roleId">
        <div class="tree-panel">
          <el-tree
            ref="menuTreeRef"
            node-key="menuId"
            show-checkbox
            :data="treeData"
            default-expand-all
            :props="replaceFields"
            @check-change="handleMenuTreeChecked"
          />
        </div>
      </el-form-item>
      <el-form-item label="角色描述">
        <el-input
          type="textarea"
          :maxLength="250"
          v-model="formData.roleDesc"
        />
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script setup>
import { ref, getCurrentInstance, nextTick } from "vue";
const { proxy } = getCurrentInstance();
const api = {
  saveRole: "/settings/saveRole",
};

const props = defineProps({
  treeData: {
    type: Array,
    default: [],
  },
});

const replaceFields = ref({
  label: "menuName",
});

const formDataRef = ref();
const editDialogInfo = ref({
  show: false,
  title: "新增角色",
  buttons: [
    {
      type: "primary",
      text: "确定",
      click: (e) => {
        submitForm(e);
      },
    },
  ],
});
const formData = ref({});
const rules = ref({
  roleName: [{ required: true, message: "请输入角色名称" }],
  menuIds: [{ required: true, message: "请选择菜单", trigger: ["change"] }],
});
//编辑
const showEdit = (data) => {
  editDialogInfo.value.show = true;
  nextTick(async () => {
    data.menuIds = [];
    formDataRef.value.resetFields();
    editDialogInfo.value.title = data.roleId ? "修改角色" : "新增角色";
    formData.value = data;
  });
};

defineExpose({
  showEdit,
});

//属性菜单
const menuTreeRef = ref();
const handleMenuTreeChecked = () => {
  formData.value.menuIds = menuTreeRef.value.getCheckedKeys();
};

const emit = defineEmits(["reload"]);
//提交
const submitForm = async () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return false;
    }
    const params = Object.assign({}, formData.value);
    if (params.roleId) {
      params.menuIds = null;
    } else {
      params.menuIds = params.menuIds.join(",");
      let halfMenuIdArray = menuTreeRef.value.getHalfCheckedKeys() || [];
      params.halfMenuIds = halfMenuIdArray.join(",");
    }

    let result = await proxy.Request({
      url: api.saveRole,
      params: params,
    });
    if (!result) {
      return;
    }
    editDialogInfo.value.show = false;
    proxy.Message.success("保存成功");
    emit("reload");
  });
};
</script>

<style lang="scss" scoped>
.tree-panel {
  width: 100%;
  max-height: calc(100vh / 2);
  overflow: auto;
}
</style>
