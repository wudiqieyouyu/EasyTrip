package com.easytrip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easytrip.entity.enums.MenuCheckTypeEnum;
import com.easytrip.entity.enums.ResponseCodeEnum;
import com.easytrip.entity.po.SysAccount;
import com.easytrip.entity.po.SysRole2Menu;
import com.easytrip.entity.query.SysAccountQuery;
import com.easytrip.entity.query.SysRole2MenuQuery;
import com.easytrip.exception.BusinessException;
import com.easytrip.mappers.SysAccountMapper;
import com.easytrip.mappers.SysRole2MenuMapper;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.SysRoleQuery;
import com.easytrip.entity.po.SysRole;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.SysRoleMapper;
import com.easytrip.service.SysRoleService;
import com.easytrip.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 系统角色表 业务接口实现
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	@Resource
	private SysRoleMapper<SysRole, SysRoleQuery> sysRoleMapper;
	@Resource
	private SysRole2MenuMapper<SysRole2Menu, SysRole2MenuQuery> sysRole2MenuMapper;

	@Resource
	private SysAccountMapper<SysAccount, SysAccountQuery> sysAccountMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<SysRole> findListByParam(SysRoleQuery param) {
		return this.sysRoleMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(SysRoleQuery param) {
		return this.sysRoleMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<SysRole> findListByPage(SysRoleQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<SysRole> list = this.findListByParam(param);
		PaginationResultVO<SysRole> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(SysRole bean) {
		return this.sysRoleMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<SysRole> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.sysRoleMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<SysRole> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.sysRoleMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(SysRole bean, SysRoleQuery param) {
		StringTools.checkParam(param);
		return this.sysRoleMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(SysRoleQuery param) {
		StringTools.checkParam(param);
		return this.sysRoleMapper.deleteByParam(param);
	}

	/**
	 * 根据RoleId获取对象
	 */
	@Override
	public SysRole getSysRoleByRoleId(Integer roleId) {
		SysRole sysRole = this.sysRoleMapper.selectByRoleId(roleId);
		List<Integer> selectMenuIds = this.sysRole2MenuMapper.selectMenuIdsByRoleIds(new String[]{String.valueOf(roleId)});
		sysRole.setMenuIds(selectMenuIds);
		return sysRole;
	}

	/**
	 * 根据RoleId修改
	 */
	@Override
	public Integer updateSysRoleByRoleId(SysRole bean, Integer roleId) {
		return this.sysRoleMapper.updateByRoleId(bean, roleId);
	}

	/**
	 * 根据RoleId删除
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteSysRoleByRoleId(Integer roleId) {
		SysAccountQuery sysAccountQuery = new SysAccountQuery();
		sysAccountQuery.setRoles(String.valueOf(roleId));
		Integer count = sysAccountMapper.selectCount(sysAccountQuery);
		if (count > 0) {
			throw new BusinessException("角色正在被使用,无法删除");
		}
		count = this.sysRoleMapper.deleteByRoleId(roleId);
		SysRole2MenuQuery sysRole2MenuQuery = new SysRole2MenuQuery();
		sysRole2MenuQuery.setRoleId(roleId);
		sysRole2MenuMapper.deleteByParam(sysRole2MenuQuery);
		return count;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveRole(SysRole sysRole, String menuIds, String halfMenuIds) {
		Integer roleId = sysRole.getRoleId();
		Boolean addMenu = false;
		if (roleId == null) {
			Date curDate = new Date();
			sysRole.setCreateTime(curDate);
			sysRole.setLastUpdateTime(curDate);
			this.sysRoleMapper.insert(sysRole);
			roleId = sysRole.getRoleId();
			addMenu = true;
		} else {
			sysRole.setCreateTime(null);
			this.sysRoleMapper.updateByRoleId(sysRole, roleId);
		}
		SysRoleQuery sysRoleQuery = new SysRoleQuery();
		sysRoleQuery.setRoleName(sysRole.getRoleName());
		Integer roleCount = sysRoleMapper.selectCount(sysRoleQuery);
		if (roleCount > 1) {
			throw new BusinessException("角色已经存在");
		}
		if (addMenu) {
			saveRoleMenu(roleId, menuIds, halfMenuIds);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveRoleMenu(Integer roleId, String menuIds, String halfMenuIds) {
		if (roleId == null || menuIds == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		List<SysRole2Menu> role2MenuList = new ArrayList<>();
		SysRole2MenuQuery role2MenuQuery = new SysRole2MenuQuery();
		role2MenuQuery.setRoleId(roleId);
		sysRole2MenuMapper.deleteByParam(role2MenuQuery);
		String[] menuIdSArray = menuIds.split(",");
		String[] halfMenuIdsArray = StringTools.isEmpty(halfMenuIds) ? new String[]{} : halfMenuIds.split(",");
		convertMenuId2RoleMenu(role2MenuList, roleId, menuIdSArray, MenuCheckTypeEnum.ALL);
		convertMenuId2RoleMenu(role2MenuList, roleId, halfMenuIdsArray, MenuCheckTypeEnum.HALF);
		if (!role2MenuList.isEmpty()) {
			sysRole2MenuMapper.insertBatch(role2MenuList);
		}
	}

	private void convertMenuId2RoleMenu(List<SysRole2Menu> role2MenuList, Integer roleId, String[] menuIdArray, MenuCheckTypeEnum checkTypeEnum) {
		for (String menuId : menuIdArray) {
			SysRole2Menu role2Menu = new SysRole2Menu();
			role2Menu.setMenuId(Integer.parseInt(menuId));
			role2Menu.setRoleId(roleId);
			role2Menu.setCheckType(checkTypeEnum.getType());
			role2MenuList.add(role2Menu);
		}

	}
}