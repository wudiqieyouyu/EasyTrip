package com.easytrip.controller;

import com.easytrip.annotation.GlobalInterceptor;
import com.easytrip.annotation.VerifyParam;
import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.dto.SessionUserAdminDto;
import com.easytrip.entity.enums.PermissionCodeEnum;
import com.easytrip.entity.po.UserMessage;
import com.easytrip.entity.query.UserMessageQuery;
import com.easytrip.entity.vo.ResponseVO;
import com.easytrip.service.UserMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
* 问题反馈
* */
@RestController("webFeedbackController")
@RequestMapping("/webFeedback")
public class WebFeedbackController extends ABaseController{

    @Resource
    private UserMessageService userMessageService;
    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadFeedback")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_FEEDBACK_LIST)
    public ResponseVO loadFeedback(UserMessageQuery query) {
        query.setOrderBy("message_id desc");
        query.setpId(0);
        query.setMessageType(Constants.FEEDBACK_TYPE);
        return getSuccessResponseVO(userMessageService.findListByPage(query));
    }

    @RequestMapping("/loadFeedbackReply")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_FEEDBACK_REPLY)
    public ResponseVO loadFeedbackReply(@VerifyParam(required = true) Integer pFeedbackId) {
        UserMessageQuery query = new UserMessageQuery();
        query.setOrderBy("message_id asc");
        query.setpId(pFeedbackId);
        return getSuccessResponseVO(userMessageService.findListByParam(query));
    }

    @RequestMapping("/replyFeedback")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.WEB_FEEDBACK_REPLY)
    public ResponseVO replyFeedback(HttpSession session,
                                    @VerifyParam(required = true, max = 500) String content,
                                    @VerifyParam(required = true) Integer pFeedbackId) {
        SessionUserAdminDto userAdminDto=getUserAdminFromSession(session);
        UserMessage userMessage = new UserMessage();
        userMessage.setpId(pFeedbackId);
        userMessage.setMessageContent(content);
        userMessage.setSendNickName(userAdminDto.getUserName());

        userMessageService.replyFeedback(userMessage);
        return getSuccessResponseVO(null);
    }}
