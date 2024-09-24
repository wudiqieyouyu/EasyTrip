package com.easytrip.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.enums.BoardTypeEnum;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.exception.BusinessException;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.TripBoardQuery;
import com.easytrip.entity.po.TripBoard;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.TripBoardMapper;
import com.easytrip.service.TripBoardService;
import com.easytrip.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 文章板块信息 业务接口实现
 */
@Service("tripBoardService")
public class TripBoardServiceImpl implements TripBoardService {

	@Resource
	private TripBoardMapper<TripBoard, TripBoardQuery> tripBoardMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<TripBoard> findListByParam(TripBoardQuery param) {
		return this.tripBoardMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(TripBoardQuery param) {
		return this.tripBoardMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<TripBoard> findListByPage(TripBoardQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<TripBoard> list = this.findListByParam(param);
		PaginationResultVO<TripBoard> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(TripBoard bean) {
		return this.tripBoardMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<TripBoard> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.tripBoardMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<TripBoard> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.tripBoardMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(TripBoard bean, TripBoardQuery param) {
		StringTools.checkParam(param);
		return this.tripBoardMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(TripBoardQuery param) {
		StringTools.checkParam(param);
		return this.tripBoardMapper.deleteByParam(param);
	}

	/**
	 * 根据BoardId获取对象
	 */
	@Override
	public TripBoard getTripBoardByBoardId(Integer boardId) {
		return this.tripBoardMapper.selectByBoardId(boardId);
	}

	/**
	 * 根据BoardId修改
	 */
	@Override
	public Integer updateTripBoardByBoardId(TripBoard bean, Integer boardId) {
		return this.tripBoardMapper.updateByBoardId(bean, boardId);
	}

	/**
	 * 根据BoardId删除
	 */
	@Override
	public Integer deleteTripBoardByBoardId(Integer boardId) {
		return this.tripBoardMapper.deleteByBoardId(boardId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveBoard(TripBoard tripBoard) {
		if (tripBoard.getBoardId() == null) {
			TripBoardQuery query = new TripBoardQuery();
			Integer count = this.tripBoardMapper.selectCount(query);
			tripBoard.setSort(count + 1);
			this.tripBoardMapper.insert(tripBoard);
		} else {
			this.tripBoardMapper.updateByBoardId(tripBoard, tripBoard.getBoardId());
		}
		TripBoardQuery boardQuery = new TripBoardQuery();
		boardQuery.setBoardName(tripBoard.getBoardName());
		boardQuery.setType(tripBoard.getType());
		Integer count = this.tripBoardMapper.selectCount(boardQuery);
		if (count > 1) {
			throw new BusinessException("分类名称重复");
		}
		if (tripBoard.getBoardName() == null) {
			return;
		}
		TripBoard dbInfo = tripBoardMapper.selectByBoardId(tripBoard.getBoardId());
		if (!dbInfo.getBoardName().equals(tripBoard.getBoardName())) {
			tripBoardMapper.updateTripBoardName(tripBoard.getBoardName(), tripBoard.getBoardId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeSort(String boardIds) {
		String[] boardIdArray = boardIds.split(",");
		Integer index = 1;
		for (String boardIdStr : boardIdArray) {
			Integer boardId = Integer.parseInt(boardIdStr);
			TripBoard tripBoard = new TripBoard();
			tripBoard.setSort(index);
			tripBoardMapper.updateByBoardId(tripBoard, boardId);
			index++;
		}
	}

	@Override
	public List<TripBoard> loadAllTripBoardByType(Integer type) {
		BoardTypeEnum boardTypeEnum = BoardTypeEnum.getByType(type);
		if (boardTypeEnum == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		TripBoardQuery tripBoardQuery = new TripBoardQuery();
		tripBoardQuery.setOrderBy("sort asc");
		tripBoardQuery.setTypes(new Integer[]{boardTypeEnum.getType(), BoardTypeEnum.ARTICLE_QUESTION.getType()});
		return this.tripBoardMapper.selectList(tripBoardQuery);
	}

	@Override
	public List<TripBoard> getBoardTree( Integer postType) {
		TripBoardQuery query = new TripBoardQuery();
		query.setOrderBy("sort asc");
		query.setPostType(postType);
		query.setType(BoardTypeEnum.LAYOUT.getType());
		List<TripBoard> tripBoardList = tripBoardMapper.selectList(query);
		return converLine2Tree(tripBoardList, Constants.ZERO);
	}

	private List<TripBoard> converLine2Tree(List<TripBoard> dataList,Integer pid){
		List<TripBoard> children = new ArrayList<>();
		for (TripBoard item : dataList){
			if (item.getpBoardId().equals(pid)){
				item.setChildren(converLine2Tree(dataList,item.getBoardId()));
				children.add(item);
			}
		}
		return children;
	}
}