package com.easytrip.entity.query;

import java.util.Date;


/**
 * 参数
 */
public class SalesQuery extends BaseParam {


	/**
	 * 景区id
	 */
	private Integer salesId;

	/**
	 * 目的地Id
	 */
	private String regionId;

	private String regionIdFuzzy;

	/**
	 * 商品名称
	 */
	private String salesName;

	private String salesNameFuzzy;

	/**
	 * 价格
	 */
	private String price;

	private String priceFuzzy;

	/**
	 * 销售量
	 */
	private Integer salesNumber;

	/**
	 * 描述
	 */
	private String salesDesc;

	private String salesDescFuzzy;

	/**
	 * 图片路径
	 */
	private String imgCover;

	private String imgCoverFuzzy;

	/**
	 * 0:无1:A 2:AA 3:AAA 4:AAAA 5:AAAAA
	 */
	private Integer salesLevel;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 地址
	 */
	private String salesAddress;

	private String salesAddressFuzzy;

	/**
	 * 开放时间
	 */
	private String openTime;

	private String openTimeFuzzy;

	/**
	 * 联系电话
	 */
	private String tel;

	private String telFuzzy;

	/**
	 * 景区简介
	 */
	private String cotent;

	private String cotentFuzzy;

	/**
	 * 购买须知
	 */
	private String purchaseInfo;

	private String purchaseInfoFuzzy;

	/**
	 * 产品特色
	 */
	private String features;

	private String featuresFuzzy;

	Boolean showDetail;

	String maxPrice;
	String minPrice;
	private String[] SalesIds;

	public String[] getSalesIds() {
		return SalesIds;
	}

	public void setSalesIds(String[] salesIds) {
		SalesIds = salesIds;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public Boolean getShowDetail() {
		return showDetail;
	}

	public void setShowDetail(Boolean showDetail) {
		this.showDetail = showDetail;
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

	public void setRegionIdFuzzy(String regionIdFuzzy){
		this.regionIdFuzzy = regionIdFuzzy;
	}

	public String getRegionIdFuzzy(){
		return this.regionIdFuzzy;
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

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return this.price;
	}

	public void setPriceFuzzy(String priceFuzzy){
		this.priceFuzzy = priceFuzzy;
	}

	public String getPriceFuzzy(){
		return this.priceFuzzy;
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

	public void setSalesDescFuzzy(String salesDescFuzzy){
		this.salesDescFuzzy = salesDescFuzzy;
	}

	public String getSalesDescFuzzy(){
		return this.salesDescFuzzy;
	}

	public void setImgCover(String imgCover){
		this.imgCover = imgCover;
	}

	public String getImgCover(){
		return this.imgCover;
	}

	public void setImgCoverFuzzy(String imgCoverFuzzy){
		this.imgCoverFuzzy = imgCoverFuzzy;
	}

	public String getImgCoverFuzzy(){
		return this.imgCoverFuzzy;
	}

	public void setSalesLevel(Integer salesLevel){
		this.salesLevel = salesLevel;
	}

	public Integer getSalesLevel(){
		return this.salesLevel;
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

	public void setSalesAddress(String salesAddress){
		this.salesAddress = salesAddress;
	}

	public String getSalesAddress(){
		return this.salesAddress;
	}

	public void setSalesAddressFuzzy(String salesAddressFuzzy){
		this.salesAddressFuzzy = salesAddressFuzzy;
	}

	public String getSalesAddressFuzzy(){
		return this.salesAddressFuzzy;
	}

	public void setOpenTime(String openTime){
		this.openTime = openTime;
	}

	public String getOpenTime(){
		return this.openTime;
	}

	public void setOpenTimeFuzzy(String openTimeFuzzy){
		this.openTimeFuzzy = openTimeFuzzy;
	}

	public String getOpenTimeFuzzy(){
		return this.openTimeFuzzy;
	}

	public void setTel(String tel){
		this.tel = tel;
	}

	public String getTel(){
		return this.tel;
	}

	public void setTelFuzzy(String telFuzzy){
		this.telFuzzy = telFuzzy;
	}

	public String getTelFuzzy(){
		return this.telFuzzy;
	}

	public void setCotent(String cotent){
		this.cotent = cotent;
	}

	public String getCotent(){
		return this.cotent;
	}

	public void setCotentFuzzy(String cotentFuzzy){
		this.cotentFuzzy = cotentFuzzy;
	}

	public String getCotentFuzzy(){
		return this.cotentFuzzy;
	}

	public void setPurchaseInfo(String purchaseInfo){
		this.purchaseInfo = purchaseInfo;
	}

	public String getPurchaseInfo(){
		return this.purchaseInfo;
	}

	public void setPurchaseInfoFuzzy(String purchaseInfoFuzzy){
		this.purchaseInfoFuzzy = purchaseInfoFuzzy;
	}

	public String getPurchaseInfoFuzzy(){
		return this.purchaseInfoFuzzy;
	}

	public void setFeatures(String features){
		this.features = features;
	}

	public String getFeatures(){
		return this.features;
	}

	public void setFeaturesFuzzy(String featuresFuzzy){
		this.featuresFuzzy = featuresFuzzy;
	}

	public String getFeaturesFuzzy(){
		return this.featuresFuzzy;
	}

}
