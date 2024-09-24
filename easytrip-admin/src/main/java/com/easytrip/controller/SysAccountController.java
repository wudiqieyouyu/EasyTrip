package com.easytrip.controller;


import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.config.AppConfig;
import com.easytrip.entity.enums.PermissionCodeEnum;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.entity.enums.UserStatusEnum;
import com.easytrip.entity.enums.VerifyRegexEnum;
import com.easytrip.entity.po.SysAccount;
import com.easytrip.entity.query.SysAccountQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.SysAccountService;
import com.easytrip.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.ws.Response;

/**
 * 账号信息 Controller
 */
@RestController("sysAccountController")
@RequestMapping("/settings")
public class SysAccountController extends ABaseController {

    @Resource
    private SysAccountService sysAccountService;

    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadAccountList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_LIST)
    public ResponseVO loadAccountList(SysAccountQuery query) {
        query.setOrderBy("create_time desc");
        query.setQueryRoles(true);
        ResponseVO<SysAccount> response = getSuccessResponseVO(sysAccountService.findListByPage(query));
        return response;
    }

    @RequestMapping("/saveAccount")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_EDIT)
    public ResponseVO saveAccount(@VerifyParam SysAccount sysAccount) {
        sysAccountService.saveSysAccount(sysAccount);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/updatePassword")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_UPDATE_PASSWORD)
    public ResponseVO updatePassword(@VerifyParam(required = true) Integer userId,
                                     @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password) {
        SysAccount updateInfo = new SysAccount();
        updateInfo.setPassword(StringTools.encodeByMD5(password));
        sysAccountService.updateSysAccountByUserId(updateInfo, userId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/updateStatus")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_OP_STATUS)
    public ResponseVO updateStatus(@VerifyParam(required = true) Integer userId,
                                   @VerifyParam(required = true) Integer status) {
        UserStatusEnum userStatusEnum = UserStatusEnum.getByStatus(status);
        if (userStatusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        SysAccount updateInfo = new SysAccount();
        updateInfo.setStatus(status);
        sysAccountService.updateSysAccountByUserId(updateInfo, userId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delAccount")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_DEL)
    public ResponseVO delAccount(@VerifyParam(required = true) Integer userId) {
        SysAccount sysAccount = this.sysAccountService.getSysAccountByUserId(userId);
        if (!StringTools.isEmpty(appConfig.getSuperAdminPhones()) && ArrayUtils.contains(appConfig.getSuperAdminPhones().split(","), sysAccount.getPhone())) {
            throw new BusinessException("系统超级管理员禁止删除");
        }
        sysAccountService.deleteSysAccountByUserId(userId);
        return getSuccessResponseVO(null);
    }


}