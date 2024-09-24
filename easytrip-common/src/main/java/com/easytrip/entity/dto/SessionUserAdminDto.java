package com.easytrip.entity.dto;

import com.easytrip.entity.vo.SysMenuVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SessionUserAdminDto implements Serializable {

    private static final long serialVersionUID = 1690149993220674991L;
    private Integer userId;
    private String userName;
    private List<SysMenuVO> menuList;
    private List<String> permissionCodeList = new ArrayList<>();
    private Boolean superAdmin;

    public Boolean getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<SysMenuVO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SysMenuVO> menuList) {
        this.menuList = menuList;
    }

    public List<String> getPermissionCodeList() {
        return permissionCodeList;
    }

    public void setPermissionCodeList(List<String> permissionCodeList) {
        this.permissionCodeList = permissionCodeList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
