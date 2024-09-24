package com.easytrip.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.easytrip.entity.Constants.Constants;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.SysMenuQuery;
import com.easytrip.entity.po.SysMenu;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.SysMenuMapper;
import com.easytrip.service.SysMenuService;
import com.easytrip.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 菜单表 业务接口实现
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {
	private static final String ROOT_MENU_NAME = "所有菜单";
	@Resource
	private SysMenuMapper<SysMenu, SysMenuQuery> sysMenuMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<SysMenu> findListByParam(SysMenuQuery param) {
		List<SysMenu> sysMenuList = this.sysMenuMapper.selectList(param);
		if (param.getFormate2Tree() != null && param.getFormate2Tree()) {
			SysMenu root = new SysMenu();
			root.setMenuId(Constants.DEFUALT_ROOT_MENU);
			root.setpId(-1);
			root.setMenuName(ROOT_MENU_NAME);
			sysMenuList.add(root);
			sysMenuList = convertLine2Tree4Menu(sysMenuList, -1);
		}
		return sysMenuList;
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(SysMenuQuery param) {
		return this.sysMenuMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<SysMenu> findListByPage(SysMenuQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<SysMenu> list = this.findListByParam(param);
		PaginationResultVO<SysMenu> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(SysMenu bean) {
		return this.sysMenuMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<SysMenu> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.sysMenuMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<SysMenu> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.sysMenuMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(SysMenu bean, SysMenuQuery param) {
		StringTools.checkParam(param);
		return this.sysMenuMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(SysMenuQuery param) {
		StringTools.checkParam(param);
		return this.sysMenuMapper.deleteByParam(param);
	}

	/**
	 * 根据MenuId获取对象
	 */
	@Override
	public SysMenu getSysMenuByMenuId(Integer menuId) {
		return this.sysMenuMapper.selectByMenuId(menuId);
	}

	/**
	 * 根据MenuId修改
	 */
	@Override
	public Integer updateSysMenuByMenuId(SysMenu bean, Integer menuId) {
		return this.sysMenuMapper.updateByMenuId(bean, menuId);
	}

	/**
	 * 根据MenuId删除
	 */
	@Override
	public Integer deleteSysMenuByMenuId(Integer menuId) {
		return this.sysMenuMapper.deleteByMenuId(menuId);
	}

	@Override
	public List<SysMenu> convertLine2Tree4Menu(List<SysMenu> sysMenuList, Integer pid) {
		List<SysMenu> child = new ArrayList<>();
		for (SysMenu m : sysMenuList) {
			if (m.getMenuId() != null && m.getpId() != null && m.getpId().equals(pid)) {
				m.setChildren(convertLine2Tree4Menu(sysMenuList, m.getMenuId()));
				child.add(m);
			}
		}
		return child;
	}

	@Override
	public List<SysMenu> getAllMenuByRoleIds(String roleIds) {
		if (StringTools.isEmpty(roleIds)) {
			return new ArrayList<>();
		}
		int[] roleIdArray = Arrays.stream(roleIds.split(",")).mapToInt(Integer::valueOf).toArray();
		return sysMenuMapper.selectAllMenuByRoleIds(roleIdArray);
	}

	@Override
	public void saveMenu(SysMenu menu) {
		if (menu.getMenuId() == null) {
			this.sysMenuMapper.insert(menu);
		} else {
			this.sysMenuMapper.updateByMenuId(menu, menu.getMenuId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteMenu(Integer menuId) {
		List<Integer> sysMenuIdList = new ArrayList<>();
		findAllChildMenu(sysMenuIdList, menuId);
		for (Integer m : sysMenuIdList) {
			this.sysMenuMapper.deleteByMenuId(m);
		}
	}

	public void findAllChildMenu(List<Integer> sysMenuIdList, Integer sysMenuId) {
		sysMenuIdList.add(sysMenuId);
		SysMenuQuery sysMenuQuery = new SysMenuQuery();
		sysMenuQuery.setpId(sysMenuId);
		List<SysMenu> sysMenuList = this.sysMenuMapper.selectList(sysMenuQuery);
		for (SysMenu s : sysMenuList) {
			findAllChildMenu(sysMenuIdList, s.getMenuId());
		}
	}
}