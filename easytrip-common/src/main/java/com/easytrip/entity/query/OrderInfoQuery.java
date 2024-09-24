package com.easytrip.entity.query;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 参数
 */
public class OrderInfoQuery extends BaseParam {


	/**
	 * 
	 */
	private String orderId;

	private String orderIdFuzzy;

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

	private String salesNameFuzzy;

	/**
	 * 用户名
	 */
	private String userName;

	private String userNameFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

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

	public void setOrderIdFuzzy(String orderIdFuzzy){
		this.orderIdFuzzy = orderIdFuzzy;
	}

	public String getOrderIdFuzzy(){
		return this.orderIdFuzzy;
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

	public void setSalesNameFuzzy(String salesNameFuzzy){
		this.salesNameFuzzy = salesNameFuzzy;
	}

	public String getSalesNameFuzzy(){
		return this.salesNameFuzzy;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setUserNameFuzzy(String userNameFuzzy){
		this.userNameFuzzy = userNameFuzzy;
	}

	public String getUserNameFuzzy(){
		return this.userNameFuzzy;
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

	public void setPrice(BigDecimal price){
		this.price = price;
	}

	public BigDecimal getPrice(){
		return this.price;
	}

}
