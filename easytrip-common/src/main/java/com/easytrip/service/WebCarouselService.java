package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.WebCarouselQuery;
import com.easytrip.entity.po.WebCarousel;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 *  业务接口
 */
public interface WebCarouselService {

	/**
	 * 根据条件查询列表
	 */
	List<WebCarousel> findListByParam(WebCarouselQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(WebCarouselQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<WebCarousel> findListByPage(WebCarouselQuery param);

	/**
	 * 新增
	 */
	Integer add(WebCarousel bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<WebCarousel> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<WebCarousel> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(WebCarousel bean,WebCarouselQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(WebCarouselQuery param);

	/**
	 * 根据CarouselId查询对象
	 */
	WebCarousel getWebCarouselByCarouselId(Integer carouselId);


	/**
	 * 根据CarouselId修改
	 */
	Integer updateWebCarouselByCarouselId(WebCarousel bean,Integer carouselId);


	/**
	 * 根据CarouselId删除
	 */
	Integer deleteWebCarouselByCarouselId(Integer carouselId);


	void saveCarousel(WebCarousel bean);

	void changeSort(String carouselIds);
}