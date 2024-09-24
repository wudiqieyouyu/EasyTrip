package com.easytrip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easytrip.entity.po.Line2Sales;
import com.easytrip.entity.po.Regions;
import com.easytrip.entity.query.Line2SalesQuery;
import com.easytrip.entity.query.RegionsQuery;
import com.easytrip.exception.BusinessException;
import com.easytrip.mappers.Line2SalesMapper;
import com.easytrip.mappers.RegionsMapper;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.LineQuery;
import com.easytrip.entity.po.Line;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.LineMapper;
import com.easytrip.service.LineService;
import com.easytrip.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 *  业务接口实现
 */
@Service("lineService")
public class LineServiceImpl implements LineService {

	@Resource
	private LineMapper<Line, LineQuery> lineMapper;

	@Resource
	private RegionsMapper<Regions, RegionsQuery> regionsMapper;

	@Resource
	private Line2SalesMapper<Line2Sales, Line2SalesQuery> line2SalesMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Line> findListByParam(LineQuery param) {
		return this.lineMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(LineQuery param) {
		return this.lineMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Line> findListByPage(LineQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Line> list = this.findListByParam(param);
		PaginationResultVO<Line> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Line bean) {
		return this.lineMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Line> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.lineMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Line> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.lineMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Line bean, LineQuery param) {
		StringTools.checkParam(param);
		return this.lineMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(LineQuery param) {
		StringTools.checkParam(param);
		return this.lineMapper.deleteByParam(param);
	}

	/**
	 * 根据LineId获取对象
	 */
	@Override
	public Line getLineByLineId(Integer lineId) {
		return this.lineMapper.selectByLineId(lineId);
	}

	/**
	 * 根据LineId修改
	 */
	@Override
	public Integer updateLineByLineId(Line bean, Integer lineId) {
		return this.lineMapper.updateByLineId(bean, lineId);
	}

	/**
	 * 根据LineId删除
	 */
	@Override
	public Integer deleteLineByLineId(Integer lineId) {
		return this.lineMapper.deleteByLineId(lineId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveLine(Line line,String salesIdList,String playTime) {
		String[] stringArray = salesIdList.split(",");
		RegionsQuery regionsQuery = new RegionsQuery();
		regionsQuery.setRegionName(line.getRegionName());
		List<Regions> regionsList=regionsMapper.selectList(regionsQuery);
		if (regionsList.size()==1){
			line.setRegionId(regionsList.get(0).getRegionId());
		}else {
			throw new BusinessException("地区名字错误或者不存在");
		}
		List<Line2Sales> line2SalesList = new ArrayList<>();
		if (line.getLineId() == null) {
			line.setCreateTime(new Date());
			this.lineMapper.insert(line);
		} else {
			line.setCreateTime(null);
			this.lineMapper.updateByLineId(line, line.getLineId());
			Line2SalesQuery line2SalesQuery = new Line2SalesQuery();
			line2SalesQuery.setLineId(line.getLineId());
			this.line2SalesMapper.deleteByParam(line2SalesQuery);
		}
		int index = 1;
		for (String id:stringArray){
			Line2Sales sales = new Line2Sales();
			sales.setLineId(line.getLineId());
			sales.setSalesId(Integer.parseInt(id));
			sales.setSort(index++);
			sales.setPlayTime(playTime);
			line2SalesList.add(sales);
		}
		this.line2SalesMapper.insertOrUpdateBatch(line2SalesList);
	}
}