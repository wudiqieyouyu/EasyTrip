package com.easytrip.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *  数据库操作接口
 */
public interface OrderInfoMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据OrderId更新
	 */
	 Integer updateByOrderId(@Param("bean") T t,@Param("orderId") Integer orderId);


	/**
	 * 根据OrderId删除
	 */
	 Integer deleteByOrderId(@Param("orderId") Integer orderId);


	/**
	 * 根据OrderId获取对象
	 */
	 T selectByOrderId(@Param("orderId") Integer orderId);


}
