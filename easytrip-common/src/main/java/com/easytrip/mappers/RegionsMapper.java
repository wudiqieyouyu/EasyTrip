package com.easytrip.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *  数据库操作接口
 */
public interface RegionsMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据RegionId更新
	 */
	 Integer updateByRegionId(@Param("bean") T t,@Param("regionId") Integer regionId);


	/**
	 * 根据RegionId删除
	 */
	 Integer deleteByRegionId(@Param("regionId") Integer regionId);


	/**
	 * 根据RegionId获取对象
	 */
	 T selectByRegionId(@Param("regionId") Integer regionId);


}
