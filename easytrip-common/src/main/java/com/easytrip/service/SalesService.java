package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.SalesQuery;
import com.easytrip.entity.po.Sales;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 *  业务接口
 */
public interface SalesService {

	/**
	 * 根据条件查询列表
	 */
	List<Sales> findListByParam(SalesQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(SalesQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Sales> findListByPage(SalesQuery param);

	/**
	 * 新增
	 */
	Integer add(Sales bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Sales> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Sales> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Sales bean,SalesQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(SalesQuery param);

	/**
	 * 根据SalesId查询对象
	 */
	Sales getSalesBySalesId(Integer salesId);


	/**
	 * 根据SalesId修改
	 */
	Integer updateSalesBySalesId(Sales bean,Integer salesId);


	/**
	 * 根据SalesId删除
	 */
	Integer deleteSalesBySalesId(Integer salesId);

	void saveSales(Sales bean);

	void deleteBatch(String ids);
}