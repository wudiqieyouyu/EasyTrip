const PermissionCode = {
    home: "home",
    menu: {
        edit: "settings_menu_edit",
    },
    role: {
        edit: "settings_role_edit",
        del: "settings_role_del"
    },
    account: {
        edit: "settings_account_edit",
        del: "settings_account_del",
        updatePwd: "settings_account_update_password",
        updateStatus: "settings_account_op_status"
    },
    category: {
        edit: "board_eidt",
        del: "board_del"
    },
    question: {
        edit: "question_edit",
        import: "question_import",
        export:"question_export",
        post: "question_post",
        del: "question_del",
        del_batch: "question_del_batch"
    },
    article: {
        edit: "article_edit",
        post: "article_post",
        del: "article_del",
        del_batch: "article_del_batch",
    },
    sales: {
        edit: "sales_edit",
        del: "sales_del",
        del_batch: "sales_del_batch",
    },
    line: {
        edit: "line_edit",
        del: "line_del",
        del_batch: "line_del_batch",
    },
    app: {
        app_carousel_edit: "web_carousel_edit",
        app_user_edit: "web_user_edit"
    }
}

export default PermissionCode;