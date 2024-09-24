package com.easytrip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.easytrip.entity.config.AppConfig;
import com.easytrip.exception.BusinessException;
import com.easytrip.utils.SmsUtil;
import org.springframework.stereotype.Service;

import com.easytrip.entity.enums.PageSize;
import com.easytrip.entity.query.PhoneCodeQuery;
import com.easytrip.entity.po.PhoneCode;
import com.easytrip.entity.vo.PaginationResultVO;
import com.easytrip.entity.query.SimplePage;
import com.easytrip.mappers.PhoneCodeMapper;
import com.easytrip.service.PhoneCodeService;
import com.easytrip.utils.StringTools;


/**
 *  业务接口实现
 */
@Service("phoneCodeService")
public class PhoneCodeServiceImpl implements PhoneCodeService {
	@Resource
	private PhoneCodeMapper<PhoneCode, PhoneCodeQuery> phoneCodeMapper;
	@Resource
	private AppConfig appConfig;
	@Resource
	private SmsUtil smsUtil;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<PhoneCode> findListByParam(PhoneCodeQuery param) {
		return this.phoneCodeMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(PhoneCodeQuery param) {
		return this.phoneCodeMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<PhoneCode> findListByPage(PhoneCodeQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<PhoneCode> list = this.findListByParam(param);
		PaginationResultVO<PhoneCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(PhoneCode bean) {
		return this.phoneCodeMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<PhoneCode> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.phoneCodeMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<PhoneCode> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.phoneCodeMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(PhoneCode bean, PhoneCodeQuery param) {
		StringTools.checkParam(param);
		return this.phoneCodeMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(PhoneCodeQuery param) {
		StringTools.checkParam(param);
		return this.phoneCodeMapper.deleteByParam(param);
	}

	/**
	 * 根据Id获取对象
	 */
	@Override
	public PhoneCode getPhoneCodeById(Integer id) {
		return this.phoneCodeMapper.selectById(id);
	}

	/**
	 * 根据Id修改
	 */
	@Override
	public Integer updatePhoneCodeById(PhoneCode bean, Integer id) {
		return this.phoneCodeMapper.updateById(bean, id);
	}

	/**
	 * 根据Id删除
	 */
	@Override
	public Integer deletePhoneCodeById(Integer id) {
		return this.phoneCodeMapper.deleteById(id);
	}

	@Override
	public String sendPhoneCode(String phoneNumber) {
		String code = StringTools.getRandomNumber(4);
		sendCode2User(phoneNumber,code);
		return code;
	}
	private  void sendCode2User(String phoneNumber,String code) {
		smsUtil.sendSms(phoneNumber,code);
	}
	@Override
	public void insert(PhoneCode bean) {
		this.phoneCodeMapper.insert(bean);
	}
}