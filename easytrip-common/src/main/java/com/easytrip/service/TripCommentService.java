package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.TripCommentQuery;
import com.easytrip.entity.po.TripComment;
import com.easytrip.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;


/**
 * 评论 业务接口
 */
public interface TripCommentService {

	/**
	 * 根据条件查询列表
	 */
	List<TripComment> findListByParam(TripCommentQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(TripCommentQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<TripComment> findListByPage(TripCommentQuery param);

	/**
	 * 新增
	 */
	Integer add(TripComment bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<TripComment> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<TripComment> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(TripComment bean,TripCommentQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(TripCommentQuery param);

	/**
	 * 根据CommentId查询对象
	 */
	TripComment getTripCommentByCommentId(Integer commentId);


	/**
	 * 根据CommentId修改
	 */
	Integer updateTripCommentByCommentId(TripComment bean,Integer commentId);


	/**
	 * 根据CommentId删除
	 */
	Integer deleteTripCommentByCommentId(Integer commentId);

	void changeTopType(String userId,Integer commentId,Integer topType);

	void postComment(TripComment comment, MultipartFile image);
}