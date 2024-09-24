package com.easytrip.mappers;

import com.easytrip.entity.po.Sales;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  数据库操作接口
 */
public interface SalesMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据SalesId更新
	 */
	 Integer updateBySalesId(@Param("bean") T t,@Param("salesId") Integer salesId);


	/**
	 * 根据SalesId删除
	 */
	 Integer deleteBySalesId(@Param("salesId") Integer salesId);


	/**
	 * 根据SalesId获取对象
	 */
	 T selectBySalesId(@Param("salesId") Integer salesId);

	 List<Sales> selectSalesListById(@Param("saleIdList")List<String> saleIdList);
}
