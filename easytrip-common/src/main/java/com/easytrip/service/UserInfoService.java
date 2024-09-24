package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.UserInfoQuery;
import com.easytrip.entity.po.UserInfo;
import com.easytrip.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户信息 业务接口
 */
public interface UserInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<UserInfo> findListByParam(UserInfoQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(UserInfoQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param);

	/**
	 * 新增
	 */
	Integer add(UserInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(UserInfo bean,UserInfoQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(UserInfoQuery param);

	/**
	 * 根据UserIdAndPhonenumber查询对象
	 */
	UserInfo getUserInfoByUserIdAndPhonenumber(String userId,String phonenumber);


	/**
	 * 根据UserIdAndPhonenumber修改
	 */
	Integer updateUserInfoByUserIdAndPhonenumber(UserInfo bean,String userId,String phonenumber);


	/**
	 * 根据UserIdAndPhonenumber删除
	 */
	Integer deleteUserInfoByUserIdAndPhonenumber(String userId,String phonenumber);


	/**
	 * 根据UserId查询对象
	 */
	UserInfo getUserInfoByUserId(String userId);


	/**
	 * 根据UserId修改
	 */
	Integer updateUserInfoByUserId(UserInfo bean,String userId);


	/**
	 * 根据UserId删除
	 */
	Integer deleteUserInfoByUserId(String userId);


	/**
	 * 根据Phonenumber查询对象
	 */
	UserInfo getUserInfoByPhonenumber(String phonenumber);


	/**
	 * 根据Phonenumber修改
	 */
	Integer updateUserInfoByPhonenumber(UserInfo bean,String phonenumber);


	/**
	 * 根据Phonenumber删除
	 */
	Integer deleteUserInfoByPhonenumber(String phonenumber);

	 void register(UserInfo userInfo);

	String login(String phoneNumber,String password,String ip);

	String autoLogin(String token,String ip);

	void updateUserInfo(UserInfo userInfo, MultipartFile avatar);
}