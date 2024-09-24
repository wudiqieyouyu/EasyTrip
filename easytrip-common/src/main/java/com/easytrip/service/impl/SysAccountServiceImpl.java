package com.easytrip.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easytrip.entity.config.AppConfig;
import com.easytrip.entity.dto.SessionUserAdminDto;
import com.easytrip.entity.enums.MenuTypeEnum;
import com.easytrip.entity.enums.SysAccountStatusEnum;
import com.easytrip.entity.enums.UserStatusEnum;
import com.easytrip.entity.po.SysMenu;
import com.easytrip.entity.query.SysMenuQuery;
import com.easytrip.entity.vo.SysMenuVO;
import com.easytrip.exception.BusinessException;
import com.easytrip.service.SysMenuService;
import com.easytrip.utils.CopyTools;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.SysAccountQuery;
import com.easytrip.entity.po.SysAccount;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.SysAccountMapper;
import com.easytrip.service.SysAccountService;
import com.easytrip.utils.StringTools;


/**
 * 账号信息 业务接口实现
 */
@Service("sysAccountService")
public class SysAccountServiceImpl implements SysAccountService {

	@Resource
	private SysAccountMapper<SysAccount, SysAccountQuery> sysAccountMapper;

	@Resource
	private SysMenuService sysMenuService;
	@Resource
	private AppConfig appConfig;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<SysAccount> findListByParam(SysAccountQuery param) {
		return this.sysAccountMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(SysAccountQuery param) {
		return this.sysAccountMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<SysAccount> findListByPage(SysAccountQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<SysAccount> list = this.findListByParam(param);
		PaginationResultVO<SysAccount> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(SysAccount bean) {
		return this.sysAccountMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<SysAccount> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.sysAccountMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<SysAccount> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.sysAccountMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(SysAccount bean, SysAccountQuery param) {
		StringTools.checkParam(param);
		return this.sysAccountMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(SysAccountQuery param) {
		StringTools.checkParam(param);
		return this.sysAccountMapper.deleteByParam(param);
	}

	/**
	 * 根据UserId获取对象
	 */
	@Override
	public SysAccount getSysAccountByUserId(Integer userId) {
		return this.sysAccountMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId修改
	 */
	@Override
	public Integer updateSysAccountByUserId(SysAccount bean, Integer userId) {
		return this.sysAccountMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteSysAccountByUserId(Integer userId) {
		return this.sysAccountMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Phone获取对象
	 */
	@Override
	public SysAccount getSysAccountByPhone(String phone) {
		return this.sysAccountMapper.selectByPhone(phone);
	}

	/**
	 * 根据Phone修改
	 */
	@Override
	public Integer updateSysAccountByPhone(SysAccount bean, String phone) {
		return this.sysAccountMapper.updateByPhone(bean, phone);
	}

	/**
	 * 根据Phone删除
	 */
	@Override
	public Integer deleteSysAccountByPhone(String phone) {
		return this.sysAccountMapper.deleteByPhone(phone);
	}

	@Override
	public SessionUserAdminDto login(String phone, String password) {
		SysAccount sysAccount = this.sysAccountMapper.selectByPhone(phone);
		if (sysAccount == null) {
			throw new BusinessException("账号或者密码错误");
		}
		if (SysAccountStatusEnum.DISABLE.getStatus().equals(sysAccount.getStatus())) {
			throw new BusinessException("账号已禁用");
		}
		if (!sysAccount.getPassword().equals(password)) {
			throw new BusinessException("账号或者密码错误");
		}
		SessionUserAdminDto sessionUserAdminDto = new SessionUserAdminDto();
		sessionUserAdminDto.setUserId(sysAccount.getUserId());
		sessionUserAdminDto.setUserName(sysAccount.getUserName());
		List<SysMenu> allMenus = new ArrayList<>();
		if (!StringTools.isEmpty(appConfig.getSuperAdminPhones()) && ArrayUtils.contains(appConfig.getSuperAdminPhones().split(","), phone)) {
			sessionUserAdminDto.setSuperAdmin(true);
			SysMenuQuery query = new SysMenuQuery();
			query.setFormate2Tree(false);
			query.setOrderBy("sort asc");
			allMenus = sysMenuService.findListByParam(query);
		} else {
			sessionUserAdminDto.setSuperAdmin(false);
			allMenus = sysMenuService.getAllMenuByRoleIds(sysAccount.getRoles());
		}

		List<String> permissionCodeList = new ArrayList<>();
		List<SysMenu> menuList = new ArrayList<>();
		for (SysMenu sysMenu : allMenus) {
			if (MenuTypeEnum.MEMU.getType().equals(sysMenu.getMenuType())) {
				menuList.add(sysMenu);
			}
			permissionCodeList.add(sysMenu.getPermissionCode());
		}
		List<SysMenuVO> menuVOList = new ArrayList<>();
		menuList = sysMenuService.convertLine2Tree4Menu(menuList, 0);
		if (menuList.isEmpty()) {
			throw new BusinessException("请联系管理员分配角色");
		}
		menuList.forEach(item -> {
			SysMenuVO sysMenuVO = CopyTools.copy(item, SysMenuVO.class);
			sysMenuVO.setChildren(CopyTools.copyList(item.getChildren(), SysMenuVO.class));
			menuVOList.add(sysMenuVO);
		});
		sessionUserAdminDto.setMenuList(menuVOList);
		sessionUserAdminDto.setPermissionCodeList(permissionCodeList);
		return sessionUserAdminDto;
	}

	@Override
	public void saveSysAccount(SysAccount sysAccount) {
		SysAccount phoneDb = sysAccountMapper.selectByPhone(sysAccount.getPhone());
		if (phoneDb != null && (sysAccount.getUserId() == null || !phoneDb.getUserId().equals(sysAccount.getUserId()))) {
			throw new BusinessException("手机号已存在");
		}
		if (sysAccount.getUserId() == null) {
			sysAccount.setCreateTime(new Date());
			sysAccount.setStatus(UserStatusEnum.ENABLE.getStatus());
			sysAccount.setPassword(StringTools.encodeByMD5(sysAccount.getPassword()));
			this.sysAccountMapper.insert(sysAccount);
		} else {
			sysAccount.setPassword(null);
			sysAccount.setStatus(null);
			this.sysAccountMapper.updateByUserId(sysAccount, sysAccount.getUserId());
		}
	}
}