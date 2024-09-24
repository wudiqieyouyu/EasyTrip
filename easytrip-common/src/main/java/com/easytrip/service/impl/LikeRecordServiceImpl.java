package com.easytrip.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.enums.*;
import com.easytrip.entity.po.TripArticle;
import com.easytrip.entity.po.TripComment;
import com.easytrip.entity.po.UserMessage;
import com.easytrip.entity.query.*;
import com.easytrip.exception.BusinessException;
import com.easytrip.mappers.TripArticleMapper;
import com.easytrip.mappers.TripCommentMapper;
import com.easytrip.mappers.UserMessageMapper;
import org.springframework.stereotype.Service;

import com.easytrip.entity.po.LikeRecord;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.mappers.LikeRecordMapper;
import com.easytrip.service.LikeRecordService;
import com.easytrip.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 点赞记录 业务接口实现
 */
@Service("likeRecordService")
public class LikeRecordServiceImpl implements LikeRecordService {

	@Resource
	private LikeRecordMapper<LikeRecord, LikeRecordQuery> likeRecordMapper;

	@Resource
	private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;

	@Resource
	private TripArticleMapper<TripArticle, TripArticleQuery> tripArticleMapper;

	@Resource
	private TripCommentMapper<TripComment, TripCommentQuery> tripCommentMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<LikeRecord> findListByParam(LikeRecordQuery param) {
		return this.likeRecordMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(LikeRecordQuery param) {
		return this.likeRecordMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<LikeRecord> findListByPage(LikeRecordQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<LikeRecord> list = this.findListByParam(param);
		PaginationResultVO<LikeRecord> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(LikeRecord bean) {
		return this.likeRecordMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<LikeRecord> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.likeRecordMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<LikeRecord> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.likeRecordMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(LikeRecord bean, LikeRecordQuery param) {
		StringTools.checkParam(param);
		return this.likeRecordMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(LikeRecordQuery param) {
		StringTools.checkParam(param);
		return this.likeRecordMapper.deleteByParam(param);
	}

	/**
	 * 根据OpId获取对象
	 */
	@Override
	public LikeRecord getLikeRecordByOpId(Integer opId) {
		return this.likeRecordMapper.selectByOpId(opId);
	}

	/**
	 * 根据OpId修改
	 */
	@Override
	public Integer updateLikeRecordByOpId(LikeRecord bean, Integer opId) {
		return this.likeRecordMapper.updateByOpId(bean, opId);
	}

	/**
	 * 根据OpId删除
	 */
	@Override
	public Integer deleteLikeRecordByOpId(Integer opId) {
		return this.likeRecordMapper.deleteByOpId(opId);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType获取对象
	 */
	@Override
	public LikeRecord getLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId, userId, opType);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType修改
	 */
	@Override
	public Integer updateLikeRecordByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.updateByObjectIdAndUserIdAndOpType(bean, objectId, userId, opType);
	}

	/**
	 * 根据ObjectIdAndUserIdAndOpType删除
	 */
	@Override
	public Integer deleteLikeRecordByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType) {
		return this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId, userId, opType);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void doLike(String objectId, String userId, String nickName, RecordOpTypeEnum opTypeEnum) {
		UserMessage userMessage = new UserMessage();
		userMessage.setCreateTime(new Date());
		LikeRecord likeRecord = new LikeRecord();
		switch(opTypeEnum){
			case ARTICLE_LIKE:
				TripArticle tripArticle = tripArticleMapper.selectByArticleId(objectId);
				if (tripArticle==null){
					throw new BusinessException("文章不存在");
				}
				likeRecord=articleLike(objectId,userId,opTypeEnum,tripArticle);
				userMessage.setArticleId(objectId);
				userMessage.setArticleTitle(tripArticle.getTitle());
				userMessage.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
				userMessage.setCommentId(Constants.ZERO);
				userMessage.setReceivedUserId(tripArticle.getUserId());

				break;
			case COMMENT_LIKE:
				TripComment comment = tripCommentMapper.selectByCommentId(Integer.parseInt(objectId));
				if (null ==comment){
					throw new BusinessException("评论不存在");
				}
				commentLike(objectId,userId,opTypeEnum,comment);
				tripArticle = tripArticleMapper.selectByArticleId(comment.getArticleId());
				userMessage.setArticleId(tripArticle.getArticleId());
				userMessage.setArticleTitle(tripArticle.getTitle());
				userMessage.setMessageType(MessageTypeEnum.COMMENT_LIKE.getType());
				userMessage.setCommentId(comment.getCommentId());
				userMessage.setReceivedUserId(comment.getUserId());
				userMessage.setMessageContent(comment.getContent());
				break;
		}
		userMessage.setSendUserId(userId);
		userMessage.setSendNickName(nickName);
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		if(!userId.equals(userMessage.getReceivedUserId())){
			UserMessage dbInfo = userMessageMapper.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(userMessage.getArticleId(),userMessage.getCommentId(),userMessage.getSendUserId(),userMessage.getMessageType());
			if (dbInfo==null){
				userMessageMapper.insert(userMessage);
			}
		}
	}
	public LikeRecord articleLike(String objId,String userId,RecordOpTypeEnum opTypeEnum,TripArticle tripArticle){
		LikeRecord record = this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objId,userId,opTypeEnum.getType());
		if(record!=null){
			this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objId,userId,opTypeEnum.getType());
			this.tripArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), -1,objId);
		}else{
			LikeRecord likeRecord = new LikeRecord();
			likeRecord.setObjectId(objId);
			likeRecord.setUserId(userId);
			likeRecord.setOpType(opTypeEnum.getType());
			likeRecord.setCreateTime(new Date());
			likeRecord.setAuthorUserId(tripArticle.getUserId());
			this.likeRecordMapper.insert(likeRecord);
			this.tripArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), 1,objId);
		}

		return record;
	}

	public void commentLike(String objectId,String userId,RecordOpTypeEnum opTypeEnum,TripComment comment){
		LikeRecord record = this.likeRecordMapper.selectByObjectIdAndUserIdAndOpType(objectId,userId,opTypeEnum.getType());
		if(record!=null){
			this.likeRecordMapper.deleteByObjectIdAndUserIdAndOpType(objectId,userId,opTypeEnum.getType());
			this.tripCommentMapper.updateCommentGoodCount(-1,Integer.parseInt(objectId));
		}else{
			LikeRecord likeRecord = new LikeRecord();
			likeRecord.setObjectId(objectId);
			likeRecord.setUserId(userId);
			likeRecord.setOpType(opTypeEnum.getType());
			likeRecord.setCreateTime(new Date());
			likeRecord.setAuthorUserId(comment.getUserId());
			this.likeRecordMapper.insert(likeRecord);
			this.tripCommentMapper.updateCommentGoodCount( 1,Integer.parseInt(objectId));
		}

	}
}