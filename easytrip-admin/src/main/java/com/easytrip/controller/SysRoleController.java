package com.easytrip.controller;


import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.enums.PermissionCodeEnum;
import com.easytrip.entity.po.SysRole;
import com.easytrip.entity.query.SysRoleQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.SysRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统角色表 Controller
 */
@RestController("sysRoleController")
@RequestMapping("/settings")
public class SysRoleController extends ABaseController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadRoles")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
    public ResponseVO loadRoles(SysRoleQuery query) {
        query.setOrderBy("create_time desc");
        return getSuccessResponseVO(sysRoleService.findListByPage(query));
    }

    @RequestMapping("/loadAllRoles")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
    public ResponseVO loadAllRoles() {
        SysRoleQuery query = new SysRoleQuery();
        query.setOrderBy("create_time desc");
        return getSuccessResponseVO(sysRoleService.findListByParam(query));
    }

    /**
     * 新增
     */
    @RequestMapping("/saveRole")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_EDIT)
    public ResponseVO saveRole(@VerifyParam SysRole bean,
                               String menuIds,
                               String halfMenuIds) {
        sysRoleService.saveRole(bean, menuIds, halfMenuIds);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/saveRoleMenu")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_EDIT)
    public ResponseVO saveRoleMenu(@VerifyParam(required = true) Integer roleId,
                                   @VerifyParam(required = true) String menuIds,
                                   String halfMenuIds) {
        sysRoleService.saveRoleMenu(roleId, menuIds, halfMenuIds);
        return getSuccessResponseVO(null);
    }


    /**
     * 根据RoleId查询对象
     */
    @RequestMapping("/getRoleByRoleId")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
    public ResponseVO getRoleByRoleId(@VerifyParam(required = true) Integer roleId) {
        SysRole sysRole = sysRoleService.getSysRoleByRoleId(roleId);
        return getSuccessResponseVO(sysRole);
    }

    @RequestMapping("/delRole")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_DEL)
    public ResponseVO delRole(@VerifyParam(required = true) Integer roleId) {
        sysRoleService.deleteSysRoleByRoleId(roleId);
        return getSuccessResponseVO(null);
    }


}