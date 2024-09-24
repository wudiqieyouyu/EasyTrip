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
public class Sales implements Serializable {


	/**
	 * 景区id
	 */
	private Integer salesId;

	/**
	 * 目的地Id
	 */
	private String regionId;

	/**
	 * 商品名称
	 */
	private String salesName;

	/**
	 * 价格
	 */
	private String price;

	/**
	 * 销售量
	 */
	private Integer salesNumber;

	/**
	 * 描述
	 */
	private String salesDesc;

	/**
	 * 图片路径
	 */
	private String imgCover;

	/**
	 * 0:无1:A 2:AA 3:AAA 4:AAAA 5:AAAAA
	 */
	private Integer salesLevel;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 地址
	 */
	private String salesAddress;

	/**
	 * 开放时间
	 */
	private String openTime;

	/**
	 * 联系电话
	 */
	private String tel;

	/**
	 * 景区简介
	 */
	private String cotent;

	/**
	 * 购买须知
	 */
	private String purchaseInfo;

	/**
	 * 产品特色
	 */
	private String features;

	private String playTime;
	private String addressCode;

	public String getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	public String getPlayTime() {
		return playTime;
	}

	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

	public void setSalesId(Integer salesId){
		this.salesId = salesId;
	}

	public Integer getSalesId(){
		return this.salesId;
	}

	public void setRegionId(String regionId){
		this.regionId = regionId;
	}

	public String getRegionId(){
		return this.regionId;
	}

	public void setSalesName(String salesName){
		this.salesName = salesName;
	}

	public String getSalesName(){
		return this.salesName;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return this.price;
	}

	public void setSalesNumber(Integer salesNumber){
		this.salesNumber = salesNumber;
	}

	public Integer getSalesNumber(){
		return this.salesNumber;
	}

	public void setSalesDesc(String salesDesc){
		this.salesDesc = salesDesc;
	}

	public String getSalesDesc(){
		return this.salesDesc;
	}

	public void setImgCover(String imgCover){
		this.imgCover = imgCover;
	}

	public String getImgCover(){
		return this.imgCover;
	}

	public void setSalesLevel(Integer salesLevel){
		this.salesLevel = salesLevel;
	}

	public Integer getSalesLevel(){
		return this.salesLevel;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setSalesAddress(String salesAddress){
		this.salesAddress = salesAddress;
	}

	public String getSalesAddress(){
		return this.salesAddress;
	}

	public void setOpenTime(String openTime){
		this.openTime = openTime;
	}

	public String getOpenTime(){
		return this.openTime;
	}

	public void setTel(String tel){
		this.tel = tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setCotent(String cotent){
		this.cotent = cotent;
	}

	public String getCotent(){
		return this.cotent;
	}

	public void setPurchaseInfo(String purchaseInfo){
		this.purchaseInfo = purchaseInfo;
	}

	public String getPurchaseInfo(){
		return this.purchaseInfo;
	}

	public void setFeatures(String features){
		this.features = features;
	}

	public String getFeatures(){
		return this.features;
	}

	@Override
	public String toString (){
		return "景区id:"+(salesId == null ? "空" : salesId)+"，目的地Id:"+(regionId == null ? "空" : regionId)+"，商品名称:"+(salesName == null ? "空" : salesName)+"，价格:"+(price == null ? "空" : price)+"，销售量:"+(salesNumber == null ? "空" : salesNumber)+"，描述:"+(salesDesc == null ? "空" : salesDesc)+"，图片路径:"+(imgCover == null ? "空" : imgCover)+"，0:无1:A 2:AA 3:AAA 4:AAAA 5:AAAAA:"+(salesLevel == null ? "空" : salesLevel)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，地址:"+(salesAddress == null ? "空" : salesAddress)+"，开放时间:"+(openTime == null ? "空" : openTime)+"，联系电话:"+(tel == null ? "空" : tel)+"，景区简介:"+(cotent == null ? "空" : cotent)+"，购买须知:"+(purchaseInfo == null ? "空" : purchaseInfo)+"，产品特色:"+(features == null ? "空" : features);
	}
}
