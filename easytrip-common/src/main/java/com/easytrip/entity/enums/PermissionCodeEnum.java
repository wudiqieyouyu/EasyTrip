package com.easytrip.entity.enums;

/**
 * 菜单权限编码
 */
public enum PermissionCodeEnum {
    NO_PERMISSION("no_permission", "不校验权限"),

    HOME("home", "首页"),

    SETTINGS("settings", "系统设置"),
    SETTINGS_MENU("settings_menu_list", "菜单列表"),
    SETTINGS_MENU_EDIT("settings_menu_edit", "新增/修改/删除"),

    SETTINGS_ROLE_LIST("settings_role_list", "角色列表"),
    SETTINGS_ROLE_EDIT("settings_role_edit", "新增/修改/删除"),
    SETTINGS_ROLE_DEL("settings_role_del", "删除"),

    SETTINGS_ACCOUNT_LIST("settings_account_list", "用户列表"),
    SETTINGS_ACCOUNT_EDIT("settings_account_edit", "新增/修改"),
    SETTINGS_ACCOUNT_DEL("settings_account_del", "删除"),
    SETTINGS_ACCOUNT_UPDATE_PASSWORD("settings_account_update_password", "修改密码"),
    SETTINGS_ACCOUNT_OP_STATUS("settings_account_op_status", "启用/禁用"),

    CONTENT("content", "内容管理"),

    BOARD_LIST("board_list", "分类列表"),
    BOARD_EDIT("board_eidt", "新增/修改/删除"),
    BOARD_DEL("board_del", "删除"),

    QUESTION_LIST("question_list", "问答列表"),
    QUESTION_EDIT("question_edit", "新增/修改"),
    QUESTION_IMPORT("question_import", "导入"),

    QUESTION_EXPORT("question_import", "导出"),
    QUESTION_POST("question_post", "发布/取消发布"),
    QUESTION_DEL("question_del", "删除"),
    QUESTION_DEL_BATCH("question_del_batch", "批量删除"),


    ARTICLE_LIST("article_list", "分享文章列表"),
    ARTICLE_EDIT("article_edit", "新增/修改"),
    ARTICLE_POST("article_post", "发布/取消发布"),
    ARTICLE_DEL("article_del", "删除"),
    ARTICLE_DEL_BATCH("article_del_batch", "删除"),


    WEB_CAROUSEL_LIST("web_carousel_list", "轮播图列表"),
    WEB_CAROUSEL_EDIT("web_carousel_edit", "轮播图/修改/删除"),

    WEB_FEEDBACK_LIST("web_feedback_list", "问题反馈"),
    WEB_FEEDBACK_REPLY("web_feedback_reply", "回复问题反馈"),

    WEB_USER_LIST("web_user_list", "网站用户"),
    WEB_USER_EDIT("web_user_edit", "网站用户编辑"),

    WEB_USER_Order("web_order_list", "网站订单");


    private String code;
    private String desc;


    PermissionCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
