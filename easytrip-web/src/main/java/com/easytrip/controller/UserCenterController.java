package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.enums.MessageTypeEnum;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.entity.enums.UserStatusEnum;
import com.easytrip.entity.po.OrderInfo;
import com.easytrip.entity.po.TripArticle;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.query.LikeRecordQuery;
import com.easytrip.entity.query.OrderInfoQuery;
import com.easytrip.entity.query.TripArticleQuery;
import com.easytrip.entity.query.UserMessageQuery;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.entity.vo.TripArticleVo;
import com.easytrip.entity.vo.UserInfoVo;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.*;
import com.easytrip.utils.CopyTools;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController("userCenterController")
@RequestMapping("/ucenter")
public class UserCenterController extends ABaseController{
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private TripArticleService tripArticleService;
    @Resource
    private LikeRecordService likeRecordService;

    @Resource
    private UserMessageService userMessageService;

    @Resource
    private OrderInfoService orderInfoService;

    @RequestMapping("/getUserInfo")
    @GlobalInterceptor()
    public ResponseVO  getUserInfo(@VerifyParam(required = true)String userId){
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (null==userInfo|| UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException(600,"用户不存在");
        }
        TripArticleQuery query = new TripArticleQuery();
        query.setUserId(userId);
        Integer postCount =  tripArticleService.findCountByParam(query);
        UserInfoVo userInfoVo = CopyTools.copy(userInfo,UserInfoVo.class);
        userInfoVo.setPostCount(postCount);
        LikeRecordQuery recordQuery = new LikeRecordQuery();
        recordQuery.setAuthorUserId(userId);
        Integer likeCount = likeRecordService.findCountByParam(recordQuery);
        userInfoVo.setLikeCount(likeCount);
        return getSuccessResponseVO(userInfoVo);
    }


    @RequestMapping("/loadUserArticle")
    @GlobalInterceptor()
    public ResponseVO loadUserArticle(@VerifyParam(required = true)String userId, @VerifyParam(required = true)Integer type){
        UserInfo userInfo = userInfoService.getUserInfoByUserId(userId);
        if (null==userInfo|| UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        TripArticleQuery query = new TripArticleQuery();
        query.setOrderBy("post_time desc");
        if (type==0){
            query.setUserId(userId);
        }else if (type==1){
            query.setCommentUserId(userId);
        }else if (type==2){
            query.setLikeUserId(userId);
        }
        PaginationResultVO resultVO = tripArticleService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVo(resultVO, TripArticleVo.class));
    }
    @RequestMapping("/loadOrder")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadOrder(@RequestHeader(required = false,value = "token")String token){
        UserLoginDto dto = getUserAdminFromToken(token);
        OrderInfoQuery query = new OrderInfoQuery();
        query.setUserId(Integer.parseInt(dto.getUserId()));
        return getSuccessResponseVO(orderInfoService.findListByPage(query));
    }
    @RequestMapping("/updateUserInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO updateUserInfo(@RequestHeader(required = false,value = "token")String token,
                                     Integer sex,
                                     @VerifyParam(max = 100)String personDescription,
                                     MultipartFile avatar){
        UserLoginDto dto = getUserAdminFromToken(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(dto.getUserId());
        userInfo.setSex(sex);
        userInfo.setPersonDescription(personDescription);
        userInfoService.updateUserInfo(userInfo,avatar);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/getMessageCount")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getMessageCount(@RequestHeader(required = false,value = "token")String token){
        UserLoginDto dto = getUserAdminFromToken(token);
        return getSuccessResponseVO(userMessageService.getUserMessageCount(dto.getUserId()));
    }

    @RequestMapping("/loadMessageList")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadMessageList(@RequestHeader(required = false,value = "token")String token,@VerifyParam(required = true)String code,Integer pageNo){
        UserLoginDto dto = getUserAdminFromToken(token);
        MessageTypeEnum typeEnum = MessageTypeEnum.getMessageypeByCode(code);
        if (typeEnum ==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserMessageQuery query = new UserMessageQuery();
        query.setPageNo(pageNo);
        query.setReceivedUserId(dto.getUserId());
        query.setMessageType(typeEnum.getType());
        query.setOrderBy("message_id desc");
        PaginationResultVO resultVO = userMessageService.findListByPage(query);
        if (pageNo == null||pageNo==1){
            userMessageService.readMessageByType(dto.getUserId(),typeEnum.getType());
        }
        return getSuccessResponseVO(resultVO);
    }
}
