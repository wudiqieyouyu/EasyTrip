package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.TripBoardQuery;
import com.easytrip.entity.po.TripBoard;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 * 文章板块信息 业务接口
 */
public interface TripBoardService {

	/**
	 * 根据条件查询列表
	 */
	List<TripBoard> findListByParam(TripBoardQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(TripBoardQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<TripBoard> findListByPage(TripBoardQuery param);

	/**
	 * 新增
	 */
	Integer add(TripBoard bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<TripBoard> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<TripBoard> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(TripBoard bean,TripBoardQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(TripBoardQuery param);

	/**
	 * 根据BoardId查询对象
	 */
	TripBoard getTripBoardByBoardId(Integer boardId);


	/**
	 * 根据BoardId修改
	 */
	Integer updateTripBoardByBoardId(TripBoard bean,Integer boardId);


	/**
	 * 根据BoardId删除
	 */
	Integer deleteTripBoardByBoardId(Integer boardId);

	void saveBoard(TripBoard tripBoard);

	void changeSort(String boardIds);

	List<TripBoard> loadAllTripBoardByType(Integer type);

	List<TripBoard> getBoardTree(Integer postType);
}