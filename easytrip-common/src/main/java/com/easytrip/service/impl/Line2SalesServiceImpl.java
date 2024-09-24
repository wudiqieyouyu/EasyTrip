package com.easytrip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.easytrip.entity.po.Line;
import com.easytrip.entity.po.Sales;
import com.easytrip.entity.query.SalesQuery;
import com.easytrip.mappers.SalesMapper;
import com.easytrip.utils.DateUtil;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.Line2SalesQuery;
import com.easytrip.entity.po.Line2Sales;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.Line2SalesMapper;
import com.easytrip.service.Line2SalesService;
import com.easytrip.utils.StringTools;


/**
 *  业务接口实现
 */
@Service("line2SalesService")
public class Line2SalesServiceImpl implements Line2SalesService {

	@Resource
	private Line2SalesMapper<Line2Sales, Line2SalesQuery> line2SalesMapper;

	@Resource
	private SalesMapper<Sales, SalesQuery> salesMapper;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Line2Sales> findListByParam(Line2SalesQuery param) {
		return this.line2SalesMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(Line2SalesQuery param) {
		return this.line2SalesMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Line2Sales> findListByPage(Line2SalesQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Line2Sales> list = this.findListByParam(param);
		PaginationResultVO<Line2Sales> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Line2Sales bean) {
		return this.line2SalesMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Line2Sales> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.line2SalesMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Line2Sales> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.line2SalesMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Line2Sales bean, Line2SalesQuery param) {
		StringTools.checkParam(param);
		return this.line2SalesMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(Line2SalesQuery param) {
		StringTools.checkParam(param);
		return this.line2SalesMapper.deleteByParam(param);
	}

	/**
	 * 根据LineIdAndSalesId获取对象
	 */
	@Override
	public Line2Sales getLine2SalesByLineIdAndSalesId(Integer lineId, Integer salesId) {
		return this.line2SalesMapper.selectByLineIdAndSalesId(lineId, salesId);
	}

	/**
	 * 根据LineIdAndSalesId修改
	 */
	@Override
	public Integer updateLine2SalesByLineIdAndSalesId(Line2Sales bean, Integer lineId, Integer salesId) {
		return this.line2SalesMapper.updateByLineIdAndSalesId(bean, lineId, salesId);
	}

	/**
	 * 根据LineIdAndSalesId删除
	 */
	@Override
	public Integer deleteLine2SalesByLineIdAndSalesId(Integer lineId, Integer salesId) {
		return this.line2SalesMapper.deleteByLineIdAndSalesId(lineId, salesId);
	}

	@Override
	public List<Line> updateLines(List<Line> lineList) {
		long startTime = System.currentTimeMillis();
		List<String> lineIdList =lineList.stream().map(line -> String.valueOf(line.getLineId())).collect(Collectors.toList());
		List<Line2Sales> list =  line2SalesMapper.selectSalesIdStringListMap(lineIdList);
		Map<String, List<String>> map = list.stream()
				.collect(Collectors.groupingBy(line -> String.valueOf(line.getLineId()), Collectors.mapping(line -> String.valueOf(line.getSalesId()), Collectors.toList())));
		for (Line item:lineList){
		List<Sales>salesList = salesMapper.selectSalesListById(map.get(String.valueOf(item.getLineId())));
		for (Line2Sales l:list){
			for (Sales s:salesList){
				if (s.getSalesId().equals(l.getSalesId())&&l.getLineId().equals(item.getLineId())){
					s.setPlayTime(l.getPlayTime());
				}
			}
		}
		item.setSalesList(salesList);
		}
		long e = System.currentTimeMillis();
		System.out.println(e-startTime);
		return lineList;
	}
}