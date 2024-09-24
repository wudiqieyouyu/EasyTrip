package com.easytrip.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *  数据库操作接口
 */
public interface LineMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据LineId更新
	 */
	 Integer updateByLineId(@Param("bean") T t,@Param("lineId") Integer lineId);


	/**
	 * 根据LineId删除
	 */
	 Integer deleteByLineId(@Param("lineId") Integer lineId);


	/**
	 * 根据LineId获取对象
	 */
	 T selectByLineId(@Param("lineId") Integer lineId);


}
