package com.easytrip.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.easytrip.entity.po.SysMenu;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.RegionsQuery;
import com.easytrip.entity.po.Regions;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.RegionsMapper;
import com.easytrip.service.RegionsService;
import com.easytrip.utils.StringTools;


/**
 *  业务接口实现
 */
@Service("regionsService")
public class RegionsServiceImpl implements RegionsService {

	@Resource
	private RegionsMapper<Regions, RegionsQuery> regionsMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Regions> findListByParam(RegionsQuery param) {
		List<Regions> regionsList = this.regionsMapper.selectList(param);
		if (param.getChild()!=null&&param.getChild()){
			regionsList=regionsList.stream().filter(region->region.getParentRegionId()!=0).collect(Collectors.toList());
		}
		if (param.getTree()!=null&&param.getTree()){
			regionsList=convertLine2Tree4Menu(regionsList,0);
		}
		return regionsList;
	}
	private List<Regions> convertLine2Tree4Menu(List<Regions> regionList, Integer pid) {
		List<Regions> child = new ArrayList<>();
		for (Regions m :regionList ) {
			if (m.getRegionId() != null && m.getParentRegionId() != null && m.getParentRegionId().equals(pid)) {
				m.setChildren(convertLine2Tree4Menu(regionList, m.getRegionId()));
				child.add(m);
			}
		}
		return child;
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(RegionsQuery param) {
		return this.regionsMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Regions> findListByPage(RegionsQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Regions> list = this.findListByParam(param);
		PaginationResultVO<Regions> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Regions bean) {
		return this.regionsMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Regions> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.regionsMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Regions> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.regionsMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Regions bean, RegionsQuery param) {
		StringTools.checkParam(param);
		return this.regionsMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(RegionsQuery param) {
		StringTools.checkParam(param);
		return this.regionsMapper.deleteByParam(param);
	}

	/**
	 * 根据RegionId获取对象
	 */
	@Override
	public Regions getRegionsByRegionId(Integer regionId) {
		return this.regionsMapper.selectByRegionId(regionId);
	}

	/**
	 * 根据RegionId修改
	 */
	@Override
	public Integer updateRegionsByRegionId(Regions bean, Integer regionId) {
		return this.regionsMapper.updateByRegionId(bean, regionId);
	}

	/**
	 * 根据RegionId删除
	 */
	@Override
	public Integer deleteRegionsByRegionId(Integer regionId) {
		return this.regionsMapper.deleteByRegionId(regionId);
	}
}