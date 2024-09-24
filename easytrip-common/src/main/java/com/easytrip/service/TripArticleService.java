package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.TripArticleQuery;
import com.easytrip.entity.po.TripArticle;
import com.easytrip.entity.vo.PaginationResultVO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文章信息 业务接口
 */
public interface TripArticleService {

	/**
	 * 根据条件查询列表
	 */
	List<TripArticle> findListByParam(TripArticleQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(TripArticleQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<TripArticle> findListByPage(TripArticleQuery param);

	/**
	 * 新增
	 */
	Integer add(TripArticle bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<TripArticle> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<TripArticle> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(TripArticle bean,TripArticleQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(TripArticleQuery param);

	/**
	 * 根据ArticleId查询对象
	 */
	TripArticle getTripArticleByArticleId(String articleId);


	/**
	 * 根据ArticleId修改
	 */
	Integer updateTripArticleByArticleId(TripArticle bean,String articleId);


	/**
	 * 根据ArticleId删除
	 */
	Integer deleteTripArticleByArticleId(String articleId);

	void saveArticle(TripArticle article, Boolean admin);

	TripArticle readArticle(String articleId);

	void postArticle(TripArticle article, MultipartFile cover);

	void updateArticle(TripArticle article,MultipartFile cover);
}