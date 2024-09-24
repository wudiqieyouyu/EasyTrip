package com.easytrip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.WebCarouselQuery;
import com.easytrip.entity.po.WebCarousel;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.WebCarouselMapper;
import com.easytrip.service.WebCarouselService;
import com.easytrip.utils.StringTools;


/**
 *  业务接口实现
 */
@Service("webCarouselService")
public class WebCarouselServiceImpl implements WebCarouselService {

	@Resource
	private WebCarouselMapper<WebCarousel, WebCarouselQuery> webCarouselMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<WebCarousel> findListByParam(WebCarouselQuery param) {
		return this.webCarouselMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(WebCarouselQuery param) {
		return this.webCarouselMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<WebCarousel> findListByPage(WebCarouselQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<WebCarousel> list = this.findListByParam(param);
		PaginationResultVO<WebCarousel> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(WebCarousel bean) {
		return this.webCarouselMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<WebCarousel> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.webCarouselMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<WebCarousel> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.webCarouselMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(WebCarousel bean, WebCarouselQuery param) {
		StringTools.checkParam(param);
		return this.webCarouselMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(WebCarouselQuery param) {
		StringTools.checkParam(param);
		return this.webCarouselMapper.deleteByParam(param);
	}

	/**
	 * 根据CarouselId获取对象
	 */
	@Override
	public WebCarousel getWebCarouselByCarouselId(Integer carouselId) {
		return this.webCarouselMapper.selectByCarouselId(carouselId);
	}

	/**
	 * 根据CarouselId修改
	 */
	@Override
	public Integer updateWebCarouselByCarouselId(WebCarousel bean, Integer carouselId) {
		return this.webCarouselMapper.updateByCarouselId(bean, carouselId);
	}

	/**
	 * 根据CarouselId删除
	 */
	@Override
	public Integer deleteWebCarouselByCarouselId(Integer carouselId) {
		return this.webCarouselMapper.deleteByCarouselId(carouselId);
	}

	@Override
	public void saveCarousel(WebCarousel bean) {
		//新增
		if (bean.getCarouselId() == null) {
			Integer count = webCarouselMapper.selectMaxSort();
			if (count ==null){
				count = 0;
			}
			bean.setSort(count + 1);
			this.webCarouselMapper.insert(bean);
		} else {
			this.webCarouselMapper.updateByCarouselId(bean, bean.getCarouselId());
		}
	}

	@Override
	public void changeSort(String carouselIds) {
		String[] carowselIdArray = carouselIds.split(",");
		Integer index = 1;
		for (String carowselIdStr : carowselIdArray) {
			Integer carowselId = Integer.parseInt(carowselIdStr);
			WebCarousel webCarousel = new WebCarousel();
			webCarousel.setSort(index);
			webCarouselMapper.updateByCarouselId(webCarousel, carowselId);
			index++;
		}
	}
}