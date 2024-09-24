package com.easytrip.entity.query;

import java.util.Date;


/**
 * 参数
 */
public class PhoneCodeQuery extends BaseParam {


	/**
	 * 验证码id
	 */
	private Integer id;

	/**
	 * 手机号
	 */
	private String phonenumber;

	private String phonenumberFuzzy;

	/**
	 * 验证码
	 */
	private String code;

	private String codeFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 0:未使用 1:已使用
	 */
	private Integer status;
	private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return this.id;
	}

	public void setPhonenumber(String phonenumber){
		this.phonenumber = phonenumber;
	}

	public String getPhonenumber(){
		return this.phonenumber;
	}

	public void setPhonenumberFuzzy(String phonenumberFuzzy){
		this.phonenumberFuzzy = phonenumberFuzzy;
	}

	public String getPhonenumberFuzzy(){
		return this.phonenumberFuzzy;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}

	public void setCodeFuzzy(String codeFuzzy){
		this.codeFuzzy = codeFuzzy;
	}

	public String getCodeFuzzy(){
		return this.codeFuzzy;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return this.createTime;
	}

	public void setCreateTimeStart(String createTimeStart){
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart(){
		return this.createTimeStart;
	}
	public void setCreateTimeEnd(String createTimeEnd){
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd(){
		return this.createTimeEnd;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

}
