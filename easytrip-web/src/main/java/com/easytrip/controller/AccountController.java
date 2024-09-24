package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.component.RedisUtils;
import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.config.AppConfig;
import com.easytrip.entity.dto.CreateImageCode;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.enums.CodeStatusEnum;
import com.easytrip.entity.enums.PhoneCodeStatusEnum;
import com.easytrip.entity.enums.RequestFrequencyTypeEnum;
import com.easytrip.entity.enums.VerifyRegexEnum;
import com.easytrip.entity.po.PhoneCode;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.query.PhoneCodeQuery;
import com.easytrip.entity.query.UserInfoQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.PhoneCodeService;
import com.easytrip.service.UserInfoService;
import com.easytrip.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@RestController("accountController")
@RequestMapping("/account")
public class AccountController extends ABaseController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private PhoneCodeService phoneCodeService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private AppConfig appConfig;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    @RequestMapping("sendPhoneCode")
    @GlobalInterceptor(frequencyType = RequestFrequencyTypeEnum.MINUTE, requestFrequencyThreshold = 1)
    public ResponseVO sendPhoneCode(@VerifyParam(required = true, regex = VerifyRegexEnum.PHONE) String phoneNumber,
                                    @VerifyParam(required = true) Integer type) {
        String code = phoneCodeService.sendPhoneCode(phoneNumber);
        PhoneCode phoneCode = new PhoneCode();
        phoneCode.setCode(code);
        phoneCode.setPhonenumber(phoneNumber);
        phoneCode.setCreateTime(new Date());
        phoneCode.setStatus(PhoneCodeStatusEnum.DISABLE.getStatus());
        this.phoneCodeService.insert(phoneCode);
        String redisKey = Constants.REDIS_KEY_CHECKCODE_PHONE + phoneNumber + type;
        redisUtils.setex(redisKey, code, 5 * 60);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/checkCode")
    @GlobalInterceptor
    public void checkCode(HttpServletResponse response,
                          @VerifyParam(required = true)Integer type,
                          @VerifyParam(required = true)String uuid) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        String redisKey = Constants.REDIS_KEY_CHECKCODE +uuid+type;
        redisUtils.setex(redisKey, code, 10 * 60);
        vCode.write(response.getOutputStream());
    }

    @RequestMapping("/register")
    @GlobalInterceptor
    public ResponseVO register(HttpServletRequest request,
                               @VerifyParam(required = true) String phoneCode,
                               @VerifyParam(required = true, regex = VerifyRegexEnum.PHONE,max = 30) String phoneNumber,
                               @VerifyParam(required = true,regex = VerifyRegexEnum.PASSWORD,max = 50,min = 8) String password,
                               @VerifyParam(required = true) String nickName) {
            Date curDate = new Date();
            String rediskey =Constants.REDIS_KEY_CHECKCODE_PHONE + phoneNumber + Constants.CHECK_CODE_TYPE_REGISTER;
            String checkCodeRedis = (String) redisUtils.get(rediskey);
            if (!phoneCode.equals(checkCodeRedis)){
                throw new BusinessException("手机验证码不正确");
            }
            PhoneCodeQuery query = new PhoneCodeQuery();
            query.setPhonenumber(phoneNumber);
            query.setCode(phoneCode);
            List<PhoneCode>phoneCodeList=phoneCodeService.findListByParam(query);
            /*
            * 将手机验证码置为使用过
            *
            * */
            for (PhoneCode item : phoneCodeList){
                if (curDate.getTime()-item.getCreateTime().getTime()<Constants.MINUTE_5){
                    PhoneCode updateCode = new PhoneCode();
                    updateCode.setStatus(CodeStatusEnum.ENABLE.getStatus());
                    phoneCodeService.updatePhoneCodeById(updateCode,item.getId());
                }
            }
                UserInfo userInfo = new UserInfo();
                userInfo.setPhonenumber(phoneNumber);
                userInfo.setPassword(password);
                userInfo.setNickName(nickName);
                userInfo.setJoinTime(curDate);
                userInfo.setLastLoginTime(curDate);
                userInfo.setLastLoginIp(getIpAddr(request));
                userInfoService.register(userInfo);
                return getSuccessResponseVO(null);
    }

    @RequestMapping("/login")
    @GlobalInterceptor(frequencyType = RequestFrequencyTypeEnum.MINUTE, requestFrequencyThreshold = 5)
    public ResponseVO login(HttpServletRequest request,
                            @VerifyParam(required = true) String phoneNumber,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String uuid,
                            @VerifyParam(required = true)String checkCode) {
        String redisKey = Constants.REDIS_KEY_CHECKCODE +uuid+Constants.CHECK_CODE_TYPE_LOGIN;
        try{
            String checkCodeRedis = (String) redisUtils.get(redisKey);
            if (!checkCode.equalsIgnoreCase(checkCodeRedis)) {
                throw new BusinessException("验证码不正确");
            }
            String token = userInfoService.login(phoneNumber, password, getIpAddr(request));
            return getSuccessResponseVO(token);
        }finally {
            redisUtils.delete(redisKey);
        }

    }

    @RequestMapping("/autoLogin")
    @GlobalInterceptor
    public ResponseVO autoLogin(HttpServletRequest request,
                                @RequestHeader(value = "token",required = false) String token) {
        if (token == null){
            return getSuccessResponseVO(null);
        }
        String newToken = userInfoService.autoLogin(token, getIpAddr(request));
        return getSuccessResponseVO(newToken);
    }

    @RequestMapping("resetPwd")
    @GlobalInterceptor
    public ResponseVO resetPwd(@VerifyParam(required = true) String phoneNumber,
                               @VerifyParam(required = true,regex = VerifyRegexEnum.PASSWORD,max = 50,min = 8)String newPassword,
                               @VerifyParam(required = true) String phoneCode) {
        Date curDate = new Date();
        String rediskey =Constants.REDIS_KEY_CHECKCODE_PHONE + phoneNumber + Constants.CHECK_CODE_TYPE_RESET;
        String checkCodeRedis = (String) redisUtils.get(rediskey);
        if (!phoneCode.equals(checkCodeRedis)){
            throw new BusinessException("手机验证码不正确");
        }
        PhoneCodeQuery query = new PhoneCodeQuery();
        query.setPhonenumber(phoneNumber);
        query.setStatus(CodeStatusEnum.DISABLE.getStatus());
        List<PhoneCode> phoneCodeList = phoneCodeService.findListByParam(query);
        /*
         * 将手机验证码置为使用过
         *
         * */
        for (PhoneCode item : phoneCodeList){
            if (curDate.getTime()-item.getCreateTime().getTime()<Constants.MINUTE_5){
                PhoneCode updateCode = new PhoneCode();
                updateCode.setStatus(CodeStatusEnum.ENABLE.getStatus());
                phoneCodeService.updatePhoneCodeById(updateCode,item.getId());
            }
        }
        UserInfo updateInfo = new UserInfo();
        updateInfo.setPassword(StringTools.encodeByMD5(newPassword));
        userInfoService.updateUserInfoByPhonenumber(updateInfo,phoneNumber);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/getAvatar/{userId}")
    @GlobalInterceptor
    public void getAvatar(HttpServletResponse response,@PathVariable("userId") String userId) {
        if (userId == null){
            return;
        }
        String avatarFolderName = Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_AVATAR_NAME;
        File folder = new File(appConfig.getProjectFolder()+avatarFolderName);
        if (!folder.exists()){
            folder.mkdirs();
        }
        String avatarPath = appConfig.getProjectFolder()+avatarFolderName+userId+Constants.AVATAR_SUFFIX;
        File file = new File(avatarPath);
        if (!file.exists()){
            if (!new File(appConfig.getProjectFolder()+avatarFolderName+Constants.AVATAR_DEFAULT).exists()){
                printNoDefaultImage(response);
            }
            avatarPath = appConfig.getProjectFolder()+avatarFolderName+Constants.AVATAR_DEFAULT;
        }
        response.setContentType("image/jpg");
        readFile(response,avatarPath);
    }
    @RequestMapping("/getUserInfo")
    @GlobalInterceptor
    public ResponseVO getUserInfo(HttpServletResponse response,@RequestHeader(value = "token",required = false)String token) {
        if (StringTools.isEmpty(token)){
            return getSuccessResponseVO(null);
        }
        UserLoginDto userLoginDto = getUserAdminFromToken(token);
        if (userLoginDto.getUserId() == null){
            throw new BusinessException("登录凭证无效");
        }
        return getSuccessResponseVO(userLoginDto);
    }

    private void printNoDefaultImage(HttpServletResponse response){
        response.setHeader(CONTENT_TYPE,CONTENT_TYPE_VALUE);
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = null;
        try{
            writer = response.getWriter();
            writer.print("请在头像目录下放置默认头像default_avatar.jpg");
            writer.close();
        }catch (Exception e){
            logger.error("输出无默认图失败",e);
        }finally {
            writer.close();
        }
    }

    @RequestMapping("/updatePassword")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO updatePassword(@RequestHeader(value = "token") String token,
                                     @VerifyParam(required = true,regex = VerifyRegexEnum.PASSWORD,max = 50,min = 8)String newPassword) {
        UserLoginDto userLoginDto = getUserAdminFromToken(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(StringTools.encodeByMD5(newPassword));
        userInfoService.updateUserInfoByUserId(userInfo,userLoginDto.getUserId());
        return getSuccessResponseVO(null);
    }
}