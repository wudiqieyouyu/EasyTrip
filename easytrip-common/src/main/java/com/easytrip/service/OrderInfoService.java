package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.OrderInfoQuery;
import com.easytrip.entity.po.OrderInfo;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 *  业务接口
 */
public interface OrderInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<OrderInfo> findListByParam(OrderInfoQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(OrderInfoQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<OrderInfo> findListByPage(OrderInfoQuery param);

	/**
	 * 新增
	 */
	Integer add(OrderInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<OrderInfo> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<OrderInfo> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(OrderInfo bean,OrderInfoQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(OrderInfoQuery param);

	/**
	 * 根据OrderId查询对象
	 */
	OrderInfo getOrderInfoByOrderId(Integer orderId);


	/**
	 * 根据OrderId修改
	 */
	Integer updateOrderInfoByOrderId(OrderInfo bean,Integer orderId);


	/**
	 * 根据OrderId删除
	 */
	Integer deleteOrderInfoByOrderId(Integer orderId);

}