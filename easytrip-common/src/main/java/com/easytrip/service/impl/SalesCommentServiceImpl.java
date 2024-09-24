package com.easytrip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.SalesCommentQuery;
import com.easytrip.entity.po.SalesComment;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.SalesCommentMapper;
import com.easytrip.service.SalesCommentService;
import com.easytrip.utils.StringTools;


/**
 *  业务接口实现
 */
@Service("salesCommentService")
public class SalesCommentServiceImpl implements SalesCommentService {

	@Resource
	private SalesCommentMapper<SalesComment, SalesCommentQuery> salesCommentMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<SalesComment> findListByParam(SalesCommentQuery param) {
		return this.salesCommentMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(SalesCommentQuery param) {
		return this.salesCommentMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<SalesComment> findListByPage(SalesCommentQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<SalesComment> list = this.findListByParam(param);
		PaginationResultVO<SalesComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(SalesComment bean) {
		return this.salesCommentMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<SalesComment> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.salesCommentMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<SalesComment> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.salesCommentMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(SalesComment bean, SalesCommentQuery param) {
		StringTools.checkParam(param);
		return this.salesCommentMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(SalesCommentQuery param) {
		StringTools.checkParam(param);
		return this.salesCommentMapper.deleteByParam(param);
	}

	/**
	 * 根据CommentId获取对象
	 */
	@Override
	public SalesComment getSalesCommentByCommentId(Integer commentId) {
		return this.salesCommentMapper.selectByCommentId(commentId);
	}

	/**
	 * 根据CommentId修改
	 */
	@Override
	public Integer updateSalesCommentByCommentId(SalesComment bean, Integer commentId) {
		return this.salesCommentMapper.updateByCommentId(bean, commentId);
	}

	/**
	 * 根据CommentId删除
	 */
	@Override
	public Integer deleteSalesCommentByCommentId(Integer commentId) {
		return this.salesCommentMapper.deleteByCommentId(commentId);
	}
}