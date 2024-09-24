package com.easytrip.mappers;

import com.easytrip.entity.po.Line;
import com.easytrip.entity.po.Line2Sales;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  数据库操作接口
 */
public interface Line2SalesMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据LineIdAndSalesId更新
	 */
	 Integer updateByLineIdAndSalesId(@Param("bean") T t,@Param("lineId") Integer lineId,@Param("salesId") Integer salesId);


	/**
	 * 根据LineIdAndSalesId删除
	 */
	 Integer deleteByLineIdAndSalesId(@Param("lineId") Integer lineId,@Param("salesId") Integer salesId);


	/**
	 * 根据LineIdAndSalesId获取对象
	 */
	 T selectByLineIdAndSalesId(@Param("lineId") Integer lineId,@Param("salesId") Integer salesId);


	List<Line2Sales> selectSalesIdStringListMap(@Param("lineIdList")List<String> lineIdList);
}
