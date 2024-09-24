package com.easytrip.service;

import java.util.List;

import com.easytrip.entity.query.PhoneCodeQuery;
import com.easytrip.entity.po.PhoneCode;
import com.easytrip.entity.vo.PaginationResultVO;


/**
 *  业务接口
 */
public interface PhoneCodeService {

	/**
	 * 根据条件查询列表
	 */
	List<PhoneCode> findListByParam(PhoneCodeQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(PhoneCodeQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<PhoneCode> findListByPage(PhoneCodeQuery param);

	/**
	 * 新增
	 */
	Integer add(PhoneCode bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<PhoneCode> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<PhoneCode> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(PhoneCode bean,PhoneCodeQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(PhoneCodeQuery param);

	/**
	 * 根据Id查询对象
	 */
	PhoneCode getPhoneCodeById(Integer id);


	/**
	 * 根据Id修改
	 */
	Integer updatePhoneCodeById(PhoneCode bean,Integer id);


	/**
	 * 根据Id删除
	 */
	Integer deletePhoneCodeById(Integer id);

	String sendPhoneCode(String phoneNumber);

	void insert(PhoneCode bean);
}