package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.SalesCommentQuery;
import com.easytrip.entity.po.SalesComment;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 *  业务接口
 */
public interface SalesCommentService {

	/**
	 * 根据条件查询列表
	 */
	List<SalesComment> findListByParam(SalesCommentQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(SalesCommentQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<SalesComment> findListByPage(SalesCommentQuery param);

	/**
	 * 新增
	 */
	Integer add(SalesComment bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<SalesComment> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<SalesComment> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(SalesComment bean,SalesCommentQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(SalesCommentQuery param);

	/**
	 * 根据CommentId查询对象
	 */
	SalesComment getSalesCommentByCommentId(Integer commentId);


	/**
	 * 根据CommentId修改
	 */
	Integer updateSalesCommentByCommentId(SalesComment bean,Integer commentId);


	/**
	 * 根据CommentId删除
	 */
	Integer deleteSalesCommentByCommentId(Integer commentId);

}