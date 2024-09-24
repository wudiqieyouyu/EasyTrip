package com.easytrip.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.SalesQuery;
import com.easytrip.entity.po.Sales;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.SalesMapper;
import com.easytrip.service.SalesService;
import com.easytrip.utils.StringTools;


/**
 *  业务接口实现
 */
@Service("salesService")
public class SalesServiceImpl implements SalesService {

	@Resource
	private SalesMapper<Sales, SalesQuery> salesMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Sales> findListByParam(SalesQuery param) {
		return this.salesMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(SalesQuery param) {
		return this.salesMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Sales> findListByPage(SalesQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Sales> list = this.findListByParam(param);
		PaginationResultVO<Sales> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Sales bean) {
		return this.salesMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Sales> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.salesMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Sales> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.salesMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Sales bean, SalesQuery param) {
		StringTools.checkParam(param);
		return this.salesMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(SalesQuery param) {
		StringTools.checkParam(param);
		return this.salesMapper.deleteByParam(param);
	}

	/**
	 * 根据SalesId获取对象
	 */
	@Override
	public Sales getSalesBySalesId(Integer salesId) {
		return this.salesMapper.selectBySalesId(salesId);
	}

	/**
	 * 根据SalesId修改
	 */
	@Override
	public Integer updateSalesBySalesId(Sales bean, Integer salesId) {
		return this.salesMapper.updateBySalesId(bean, salesId);
	}

	/**
	 * 根据SalesId删除
	 */
	@Override
	public Integer deleteSalesBySalesId(Integer salesId) {
		return this.salesMapper.deleteBySalesId(salesId);
	}

	@Override
	public void saveSales(Sales bean) {
		if (bean.getSalesId() == null) {
			bean.setCreateTime(new Date());
			this.salesMapper.insert(bean);
		} else {
			bean.setCreateTime(null);
			this.salesMapper.updateBySalesId(bean, bean.getSalesId());
		}
	}

	@Override
	public void deleteBatch(String ids) {
		String[] idArray = ids.split(",");
		SalesQuery query = new SalesQuery();
		query.setSalesIds(idArray);
		this.salesMapper.deleteByParam(query);
	}
}