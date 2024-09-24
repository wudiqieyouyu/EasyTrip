package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.enums.PermissionCodeEnum;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.entity.enums.UserStatusEnum;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.query.UserInfoQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("webUserInfoController")
@RequestMapping("/webUser")
public class WebUserInfoController extends ABaseController{

    @Resource
    private UserInfoService userInfoService;
    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_USER_LIST)
    public ResponseVO loadDataList(UserInfoQuery query) {
        query.setOrderBy("join_time desc");
        return getSuccessResponseVO(userInfoService.findListByPage(query));
    }

    /**
     * 设备管理列表
     */
//    @RequestMapping("/loadDeviceList")
//    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_USER_DEVICE)
//    public ResponseVO loadDeviceList(AppDeviceQuery query) {
//        query.setOrderBy("create_time desc");
//        return getSuccessResponseVO(appDeviceService.findListByPage(query));
//    }

    @RequestMapping("/updateStatus")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_USER_EDIT)
    public ResponseVO updateStatus(@VerifyParam(required = true) String userId, @VerifyParam(required = true) Integer status) {
        UserStatusEnum userStatusEnum = UserStatusEnum.getByStatus(status);
        if (userStatusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserInfo webUserInfo = new UserInfo();
        webUserInfo.setStatus(status);
        userInfoService.updateUserInfoByUserId(webUserInfo, userId);
        return getSuccessResponseVO(null);
    }
}
