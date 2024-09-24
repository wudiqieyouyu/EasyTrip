package com.easytrip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.dto.FileUploadDto;
import com.easytrip.entity.enums.*;
import com.easytrip.entity.po.TripArticle;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.po.UserMessage;
import com.easytrip.entity.query.TripArticleQuery;
import com.easytrip.exception.BusinessException;
import com.easytrip.mappers.TripArticleMapper;
import com.easytrip.service.UserInfoService;
import com.easytrip.service.UserMessageService;
import com.easytrip.utils.FileUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import com.easytrip.entity.query.TripCommentQuery;
import com.easytrip.entity.po.TripComment;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.TripCommentMapper;
import com.easytrip.service.TripCommentService;
import com.easytrip.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * 评论 业务接口实现
 */
@Service("tripCommentService")
public class TripCommentServiceImpl implements TripCommentService {
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private TripCommentMapper<TripComment, TripCommentQuery> tripCommentMapper;

	@Resource
	private TripArticleMapper<TripArticle, TripArticleQuery> tripArticleMapper;

	@Resource
	private UserMessageService userMessageService;

	@Resource
	private FileUtils fileUtils;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<TripComment> findListByParam(TripCommentQuery param) {
		List<TripComment> list = this.tripCommentMapper.selectList(param);
		//获取二级评论
		if (param.getLoadChildren()!=null&&param.getLoadChildren()){
			TripCommentQuery query = new TripCommentQuery();
			query.setQueryLikeType(param.getQueryLikeType());
			query.setUserId(param.getUserId());
			query.setArticleId(param.getArticleId());
			query.setStatus(param.getStatus());
			List<Integer> pCommentIdList = list.stream().map(TripComment::getCommentId).distinct().collect(Collectors.toList());
			query.setpCommentIdList(pCommentIdList);
			List<TripComment> subCommentList = this.tripCommentMapper.selectList(query);
			Map<Integer,List<TripComment>> tempMap = subCommentList.stream().collect(Collectors.groupingBy(TripComment::getpCommentId));
			list.forEach(item->{
				item.setChildren(tempMap.get(item.getCommentId()));
			});
		}
		return list;
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(TripCommentQuery param) {
		return this.tripCommentMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<TripComment> findListByPage(TripCommentQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<TripComment> list = this.findListByParam(param);
		PaginationResultVO<TripComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(TripComment bean) {
		return this.tripCommentMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<TripComment> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.tripCommentMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<TripComment> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.tripCommentMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(TripComment bean, TripCommentQuery param) {
		StringTools.checkParam(param);
		return this.tripCommentMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(TripCommentQuery param) {
		StringTools.checkParam(param);
		return this.tripCommentMapper.deleteByParam(param);
	}

	/**
	 * 根据CommentId获取对象
	 */
	@Override
	public TripComment getTripCommentByCommentId(Integer commentId) {
		return this.tripCommentMapper.selectByCommentId(commentId);
	}

	/**
	 * 根据CommentId修改
	 */
	@Override
	public Integer updateTripCommentByCommentId(TripComment bean, Integer commentId) {
		return this.tripCommentMapper.updateByCommentId(bean, commentId);
	}

	/**
	 * 根据CommentId删除
	 */
	@Override
	public Integer deleteTripCommentByCommentId(Integer commentId) {
		return this.tripCommentMapper.deleteByCommentId(commentId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeTopType(String userId, Integer commentId, Integer topType) {
		CommentTopTypeEnum topTypeEnum = CommentTopTypeEnum.getByType(topType);
		if (null == topTypeEnum){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		TripComment comment = tripCommentMapper.selectByCommentId(commentId);
		if (null == comment){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		TripArticle article = tripArticleMapper.selectByArticleId(comment.getArticleId());
		if (article==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (!article.getUserId().equals(userId)||comment.getpCommentId()!=0){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if (comment.getTopType().equals(topType)){
			return;
		}
		if (CommentTopTypeEnum.TOP.getType().equals(topType)){
			tripCommentMapper.updateTopTypeByArticleId(article.getArticleId());
		}
		TripComment updateInfo = new TripComment();
		updateInfo.setTopType(topType);
		tripCommentMapper.updateByCommentId(updateInfo,commentId);
	}


	@Override
	public void postComment(TripComment comment, MultipartFile image) {
		TripArticle article = tripArticleMapper.selectByArticleId(comment.getArticleId());
		if (article==null|| !TripArticleStatusEnum.ENABLE.getStatus().equals(article.getStatus())){
			throw new BusinessException("评论的文章不存在");
		}
		TripComment pComment = null;
		if (comment.getpCommentId()!=0){
			pComment = tripCommentMapper.selectByCommentId(comment.getpCommentId());
			if (pComment ==null){
				throw new BusinessException("回复的评论不存在");
			}
		}
		//判断回复的用户是否存在
		if (!StringTools.isEmpty(comment.getReplyUserId())){
			UserInfo userInfo = userInfoService.getUserInfoByUserId(comment.getReplyUserId());
			if (userInfo==null){
				throw new BusinessException("回复的用户不存在");
			}
			comment.setReplyNickName(userInfo.getNickName());
		}
		comment.setPostTime(new Date());
		if (image!=null){
			FileUploadDto uploadDto=fileUtils.uploadFile2Local(image,Constants.FILE_FOLDER_IMAGE,ImageUploadTypeEnum.COMMENT_IMAGE);
			comment.setImgPath(uploadDto.getLocalPath());
		}
		this.tripCommentMapper.insert(comment);
		updateCommentInfo(comment,article,pComment);
	}

	public void updateCommentInfo(TripComment comment,TripArticle article,TripComment pComment){
		if(comment.getpCommentId()==0){
			this.tripArticleMapper.updateArticleCount(UpdateArticleCountTypeEnum.COMMENT_COUNT.getType(), Constants.ONE,comment.getArticleId());
		}
		//记录消息
		UserMessage userMessage = new UserMessage();
		userMessage.setMessageType(MessageTypeEnum.COMMENT.getType());
		userMessage.setCreateTime(new Date());
		userMessage.setArticleId(comment.getArticleId());
		userMessage.setCommentId(comment.getCommentId());
		userMessage.setSendUserId(comment.getUserId());
		userMessage.setSendNickName(comment.getNickName());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		userMessage.setArticleTitle(article.getTitle());
		if(comment.getpCommentId()==0){
			userMessage.setReceivedUserId(article.getUserId());
		}else if (comment.getpCommentId()!=0&&StringTools.isEmpty(comment.getReplyUserId())){
			userMessage.setReceivedUserId(pComment.getUserId());
		}else if (comment.getpCommentId()!=0&&!StringTools.isEmpty(comment.getReplyUserId())){
			userMessage.setReceivedUserId(comment.getReplyUserId());
		}
		if (!comment.getUserId().equals(userMessage.getReceivedUserId())){
			userMessageService.add(userMessage);
		}
	}
}