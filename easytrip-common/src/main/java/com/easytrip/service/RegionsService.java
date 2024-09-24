package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.RegionsQuery;
import com.easytrip.entity.po.Regions;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 *  业务接口
 */
public interface RegionsService {

	/**
	 * 根据条件查询列表
	 */
	List<Regions> findListByParam(RegionsQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(RegionsQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Regions> findListByPage(RegionsQuery param);

	/**
	 * 新增
	 */
	Integer add(Regions bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Regions> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Regions> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Regions bean,RegionsQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(RegionsQuery param);

	/**
	 * 根据RegionId查询对象
	 */
	Regions getRegionsByRegionId(Integer regionId);


	/**
	 * 根据RegionId修改
	 */
	Integer updateRegionsByRegionId(Regions bean,Integer regionId);


	/**
	 * 根据RegionId删除
	 */
	Integer deleteRegionsByRegionId(Integer regionId);

}