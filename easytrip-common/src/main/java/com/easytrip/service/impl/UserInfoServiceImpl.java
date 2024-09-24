package com.easytrip.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.easytrip.entity.Constants.Constants;
import com.easytrip.entity.dto.UserLoginDto;
import com.easytrip.entity.enums.ImageUploadTypeEnum;
import com.easytrip.entity.enums.UserStatusEnum;
import com.easytrip.exception.BusinessException;
import com.easytrip.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.UserInfoQuery;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.UserInfoMapper;
import com.easytrip.service.UserInfoService;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户信息 业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private JWTUtil<UserLoginDto> jwtUtil;

	@Resource
	private FileUtils fileUtils;
	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserInfo> findListByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserInfo> list = this.findListByParam(param);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserInfo bean, UserInfoQuery param) {
		StringTools.checkParam(param);
		return this.userInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserInfoQuery param) {
		StringTools.checkParam(param);
		return this.userInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据UserIdAndPhonenumber获取对象
	 */
	@Override
	public UserInfo getUserInfoByUserIdAndPhonenumber(String userId, String phonenumber) {
		return this.userInfoMapper.selectByUserIdAndPhonenumber(userId, phonenumber);
	}

	/**
	 * 根据UserIdAndPhonenumber修改
	 */
	@Override
	public Integer updateUserInfoByUserIdAndPhonenumber(UserInfo bean, String userId, String phonenumber) {
		return this.userInfoMapper.updateByUserIdAndPhonenumber(bean, userId, phonenumber);
	}

	/**
	 * 根据UserIdAndPhonenumber删除
	 */
	@Override
	public Integer deleteUserInfoByUserIdAndPhonenumber(String userId, String phonenumber) {
		return this.userInfoMapper.deleteByUserIdAndPhonenumber(userId, phonenumber);
	}

	/**
	 * 根据UserId获取对象
	 */
	@Override
	public UserInfo getUserInfoByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId修改
	 */
	@Override
	public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
		return this.userInfoMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteUserInfoByUserId(String userId) {
		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Phonenumber获取对象
	 */
	@Override
	public UserInfo getUserInfoByPhonenumber(String phonenumber) {
		return this.userInfoMapper.selectByPhonenumber(phonenumber);
	}

	/**
	 * 根据Phonenumber修改
	 */
	@Override
	public Integer updateUserInfoByPhonenumber(UserInfo bean, String phonenumber) {
		return this.userInfoMapper.updateByPhonenumber(bean, phonenumber);
	}

	/**
	 * 根据Phonenumber删除
	 */
	@Override
	public Integer deleteUserInfoByPhonenumber(String phonenumber) {
		return this.userInfoMapper.deleteByPhonenumber(phonenumber);
	}

	@Override
	public void register(UserInfo userInfo) {
		UserInfo dbUserInfo = this.userInfoMapper.selectByPhonenumber(userInfo.getPhonenumber());
		if (dbUserInfo!=null){
			throw new BusinessException("手机号已经存在");
		}
		String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
		userInfo.setUserId(userId);
		userInfo.setPassword(StringTools.encodeByMD5(userInfo.getPassword()));
		userInfo.setTotalIntegral(Constants.ZERO);
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		this.userInfoMapper.insert(userInfo);
	}

	@Override
	public String login(String phoneNumber, String password, String ip) {
		UserInfo userInfo = this.userInfoMapper.selectByPhonenumber(phoneNumber);
		if (userInfo == null){
			throw new BusinessException("账号或者密码错误");
		}
		if (!userInfo.getPassword().equals(password)){
			throw new BusinessException("账号或者密码错误");
		}
		if (!UserStatusEnum.ENABLE.getStatus().equals(userInfo.getStatus())){
			throw new BusinessException("账号已禁用");
		}
		UserInfo updateInfo = new UserInfo();
		updateInfo.setLastLoginTime(new Date());
		updateInfo.setLastLoginIp(ip);
		updateInfo.setLastLoginIpAddress(getIpAddress(ip));
		this.userInfoMapper.updateByUserId(updateInfo,userInfo.getUserId());
		UserLoginDto loginDto = new UserLoginDto();
		loginDto.setUserId(userInfo.getUserId());
		loginDto.setNickName(userInfo.getNickName());
		String token = jwtUtil.createToken(Constants.JWT_KEY_LOGIN_TOKEN, loginDto, Constants.JWT_TOKEN_EXPIRES_DAYS);
		return token;
	}

	public String getIpAddress(String ip){
		try{
			String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip="+ip;
			String reponseJson = OKHttpUtils.getRequest(url);
			if (null == reponseJson){
				return Constants.NO_ADDRESS;
			}
			Map<String,String> addressInfo = JsonUtils.convertJson2Obj(reponseJson,Map.class);
			return addressInfo.get("pro");
		}catch (Exception e){
			logger.error("获取ip地址失败",e);
		}
		return  Constants.NO_ADDRESS;
	}

	@Override
	public String autoLogin(String token, String ip) {
		UserLoginDto userLoginDto = jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN,token,UserLoginDto.class);
		if (userLoginDto == null){
			return null;
		}
		UserInfo userInfo = this.userInfoMapper.selectByUserId(userLoginDto.getUserId());
		if (!UserStatusEnum.ENABLE.getStatus().equals(userInfo.getStatus())) {
			throw new BusinessException("账号已禁用");
		}
		UserInfo updateInfo = new UserInfo();
		updateInfo.setLastLoginTime(new Date());
		updateInfo.setLastLoginIp(ip);
		updateInfo.setLastLoginIpAddress(getIpAddress(ip));
		this.userInfoMapper.updateByUserId(updateInfo,userInfo.getUserId());
		String newToken = jwtUtil.createToken(Constants.JWT_KEY_LOGIN_TOKEN, userLoginDto, Constants.JWT_TOKEN_EXPIRES_DAYS);
		return newToken;
	}

	@Override
	public void updateUserInfo(UserInfo userInfo, MultipartFile avatar) {
		userInfoMapper.updateByUserId(userInfo,userInfo.getUserId());
		if(avatar!=null){
			fileUtils.uploadFile2Local(avatar,userInfo.getUserId(),ImageUploadTypeEnum.AVATAR);
		}
	}
}