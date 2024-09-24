package com.easytrip.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import com.easytrip.entity.enums.DateTimePatternEnum;
import com.easytrip.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * 
 */
public class OrderInfo implements Serializable {


	/**
	 * 
	 */
	private String orderId;

	/**
	 * 景点id
	 */
	private Integer salesId;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 景点名字
	 */
	private String salesName;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 价格
	 */
	private BigDecimal price;


	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return this.orderId;
	}

	public void setSalesId(Integer salesId){
		this.salesId = salesId;
	}

	public Integer getSalesId(){
		return this.salesId;
	}

	public void setUserId(Integer userId){
		this.userId = userId;
	}

	public Integer getUserId(){
		return this.userId;
	}

	public void setSalesName(String salesName){
		this.salesName = salesName;
	}

	public String getSalesName(){
		return this.salesName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setPrice(BigDecimal price){
		this.price = price;
	}

	public BigDecimal getPrice(){
		return this.price;
	}

	@Override
	public String toString (){
		return "orderId:"+(orderId == null ? "空" : orderId)+"，景点id:"+(salesId == null ? "空" : salesId)+"，用户id:"+(userId == null ? "空" : userId)+"，景点名字:"+(salesName == null ? "空" : salesName)+"，用户名:"+(userName == null ? "空" : userName)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，价格:"+(price == null ? "空" : price);
	}
}
