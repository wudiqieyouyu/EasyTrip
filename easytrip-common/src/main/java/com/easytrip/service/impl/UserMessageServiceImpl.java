package com.easytrip.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.easytrip.entity.dto.UserMessageCountDto;
import com.easytrip.entity.enums.*;
import com.easytrip.exception.BusinessException;
import org.springframework.stereotype.Service;

import com.easytrip.entity.query.UserMessageQuery;
import com.easytrip.entity.po.UserMessage;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.UserMessageMapper;
import com.easytrip.service.UserMessageService;
import com.easytrip.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户消息 业务接口实现
 */
@Service("userMessageService")
public class UserMessageServiceImpl implements UserMessageService {

	@Resource
	private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserMessage> findListByParam(UserMessageQuery param) {
		return this.userMessageMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserMessageQuery param) {
		return this.userMessageMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserMessage> findListByPage(UserMessageQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserMessage> list = this.findListByParam(param);
		PaginationResultVO<UserMessage> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserMessage bean) {
		return this.userMessageMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserMessage> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userMessageMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserMessage> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userMessageMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserMessage bean, UserMessageQuery param) {
		StringTools.checkParam(param);
		return this.userMessageMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserMessageQuery param) {
		StringTools.checkParam(param);
		return this.userMessageMapper.deleteByParam(param);
	}

	/**
	 * 根据MessageId获取对象
	 */
	@Override
	public UserMessage getUserMessageByMessageId(Integer messageId) {
		return this.userMessageMapper.selectByMessageId(messageId);
	}

	/**
	 * 根据MessageId修改
	 */
	@Override
	public Integer updateUserMessageByMessageId(UserMessage bean, Integer messageId) {
		return this.userMessageMapper.updateByMessageId(bean, messageId);
	}

	/**
	 * 根据MessageId删除
	 */
	@Override
	public Integer deleteUserMessageByMessageId(Integer messageId) {
		return this.userMessageMapper.deleteByMessageId(messageId);
	}

	/**
	 * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType获取对象
	 */
	@Override
	public UserMessage getUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(String articleId, Integer commentId, String sendUserId, Integer messageType) {
		return this.userMessageMapper.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(articleId, commentId, sendUserId, messageType);
	}

	/**
	 * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType修改
	 */
	@Override
	public Integer updateUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(UserMessage bean, String articleId, Integer commentId, String sendUserId, Integer messageType) {
		return this.userMessageMapper.updateByArticleIdAndCommentIdAndSendUserIdAndMessageType(bean, articleId, commentId, sendUserId, messageType);
	}

	/**
	 * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType删除
	 */
	@Override
	public Integer deleteUserMessageByArticleIdAndCommentIdAndSendUserIdAndMessageType(String articleId, Integer commentId, String sendUserId, Integer messageType) {
		return this.userMessageMapper.deleteByArticleIdAndCommentIdAndSendUserIdAndMessageType(articleId, commentId, sendUserId, messageType);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void replyFeedback(UserMessage userMessage) {
		UserMessage pFeedback = userMessageMapper.selectByFeedbackId(userMessage.getpId());
		if (pFeedback == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		Date curDate = new Date();
		userMessage.setCreateTime(curDate);
		userMessage.setSendType(FeedbackSendTypeEnum.ADMIN.getStatus());
		userMessage.setMessageType(MessageTypeEnum.FEEDBACK.getType());
		userMessageMapper.insert(userMessage);
		/*
		 *更新父级反馈状态
		 * */
		UserMessage parentUpdate = new UserMessage();
		parentUpdate.setStatus(FeedbackStatusEnum.REPLY.getStatus());
		userMessageMapper.updateByMessageId(parentUpdate, userMessage.getpId());
	}


	@Override
	public UserMessageCountDto getUserMessageCount(String userId) {
		List<Map> mapList =this.userMessageMapper.selectUserMessageCount(userId);
		UserMessageCountDto dto = new UserMessageCountDto();
		Long totalCount = 0L;
		for (Map item :mapList){
			Integer type =(Integer) item.get("messageType");
			Long count =(Long)item.get("count");
			totalCount = totalCount+count;
			MessageTypeEnum messageTypeEnum = MessageTypeEnum.getMessageypeByType(type);
			switch (messageTypeEnum){
				case SYSTEM:
					dto.setSys(count);
					break;
				case COMMENT:
					dto.setReply(count);
					break;
				case ARTICLE_LIKE:
					dto.setLikePost(count);
					break;
				case COMMENT_LIKE:
					dto.setLikeComment(count);
					break;
				case FEEDBACK:
					//TODO
					break;

			}
		}
		dto.setTotal(totalCount);
		return dto;
	}

	@Override
	public void readMessageByType(String receivedUserId, Integer messageType) {
		this.userMessageMapper.updateMessageStatusBatch(receivedUserId,messageType,MessageStatusEnum.READ.getStatus());
	}
}