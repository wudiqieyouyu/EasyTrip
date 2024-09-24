package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.LineQuery;
import com.easytrip.entity.po.Line;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 *  业务接口
 */
public interface LineService {

	/**
	 * 根据条件查询列表
	 */
	List<Line> findListByParam(LineQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(LineQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Line> findListByPage(LineQuery param);

	/**
	 * 新增
	 */
	Integer add(Line bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Line> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Line> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Line bean,LineQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(LineQuery param);

	/**
	 * 根据LineId查询对象
	 */
	Line getLineByLineId(Integer lineId);


	/**
	 * 根据LineId修改
	 */
	Integer updateLineByLineId(Line bean,Integer lineId);


	/**
	 * 根据LineId删除
	 */
	Integer deleteLineByLineId(Integer lineId);

	void saveLine(Line line,String salesIdList,String playTime);

}