package com.easytrip.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 用户信息 数据库操作接口
 */
public interface UserInfoMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据UserIdAndPhonenumber更新
	 */
	 Integer updateByUserIdAndPhonenumber(@Param("bean") T t,@Param("userId") String userId,@Param("phonenumber") String phonenumber);


	/**
	 * 根据UserIdAndPhonenumber删除
	 */
	 Integer deleteByUserIdAndPhonenumber(@Param("userId") String userId,@Param("phonenumber") String phonenumber);


	/**
	 * 根据UserIdAndPhonenumber获取对象
	 */
	 T selectByUserIdAndPhonenumber(@Param("userId") String userId,@Param("phonenumber") String phonenumber);


	/**
	 * 根据UserId更新
	 */
	 Integer updateByUserId(@Param("bean") T t,@Param("userId") String userId);


	/**
	 * 根据UserId删除
	 */
	 Integer deleteByUserId(@Param("userId") String userId);


	/**
	 * 根据UserId获取对象
	 */
	 T selectByUserId(@Param("userId") String userId);


	/**
	 * 根据Phonenumber更新
	 */
	 Integer updateByPhonenumber(@Param("bean") T t,@Param("phonenumber") String phonenumber);


	/**
	 * 根据Phonenumber删除
	 */
	 Integer deleteByPhonenumber(@Param("phonenumber") String phonenumber);


	/**
	 * 根据Phonenumber获取对象
	 */
	 T selectByPhonenumber(@Param("phonenumber") String phonenumber);


}
