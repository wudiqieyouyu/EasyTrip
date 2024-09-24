package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.po.Line;
import com.easytrip.entity.query.Line2SalesQuery;
import com.easytrip.entity.po.Line2Sales;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 *  业务接口
 */
public interface Line2SalesService {

	/**
	 * 根据条件查询列表
	 */
	List<Line2Sales> findListByParam(Line2SalesQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(Line2SalesQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Line2Sales> findListByPage(Line2SalesQuery param);

	/**
	 * 新增
	 */
	Integer add(Line2Sales bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Line2Sales> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Line2Sales> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Line2Sales bean,Line2SalesQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(Line2SalesQuery param);

	/**
	 * 根据LineIdAndSalesId查询对象
	 */
	Line2Sales getLine2SalesByLineIdAndSalesId(Integer lineId,Integer salesId);


	/**
	 * 根据LineIdAndSalesId修改
	 */
	Integer updateLine2SalesByLineIdAndSalesId(Line2Sales bean,Integer lineId,Integer salesId);


	/**
	 * 根据LineIdAndSalesId删除
	 */
	Integer deleteLine2SalesByLineIdAndSalesId(Integer lineId,Integer salesId);

	List<Line> updateLines(List<Line> lineList);
}