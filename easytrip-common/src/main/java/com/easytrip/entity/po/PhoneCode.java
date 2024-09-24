package com.easytrip.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.easytrip.entity.enums.DateTimePatternEnum;
import com.easytrip.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * 
 */
public class PhoneCode implements Serializable {


	/**
	 * 验证码id
	 */
	private Integer id;

	/**
	 * 手机号
	 */
	private String phonenumber;

	/**
	 * 验证码
	 */
	private String code;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

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

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	@Override
	public String toString (){
		return "验证码id:"+(id == null ? "空" : id)+"，手机号:"+(phonenumber == null ? "空" : phonenumber)+"，验证码:"+(code == null ? "空" : code)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，0:未使用 1:已使用:"+(status == null ? "空" : status);
	}
}
