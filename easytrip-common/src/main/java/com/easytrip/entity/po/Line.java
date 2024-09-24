package com.easytrip.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.easytrip.entity.enums.DateTimePatternEnum;
import com.easytrip.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;


/**
 * 
 */
public class Line implements Serializable {


	/**
	 * 线路id
	 */
	private Integer lineId;

	/**
	 * 线路名称
	 */
	private String lineName;

	/**
	 * 线路描述
	 */
	private String description;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 上次更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;

	/**
	 * 地区id
	 */
	private Integer regionId;

	/**
	 * 地区名字
	 */
	private String regionName;

	private List<Sales> salesList;

	public List<Sales> getSalesList() {
		return salesList;
	}

	public void setSalesList(List<Sales> salesList) {
		this.salesList = salesList;
	}

	public void setLineId(Integer lineId){
		this.lineId = lineId;
	}

	public Integer getLineId(){
		return this.lineId;
	}

	public void setLineName(String lineName){
		this.lineName = lineName;
	}

	public String getLineName(){
		return this.lineName;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return this.description;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime(){
		return this.lastUpdateTime;
	}

	public void setRegionId(Integer regionId){
		this.regionId = regionId;
	}

	public Integer getRegionId(){
		return this.regionId;
	}

	public void setRegionName(String regionName){
		this.regionName = regionName;
	}

	public String getRegionName(){
		return this.regionName;
	}

	@Override
	public String toString (){
		return "线路id:"+(lineId == null ? "空" : lineId)+"，线路名称:"+(lineName == null ? "空" : lineName)+"，线路描述:"+(description == null ? "空" : description)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，上次更新时间:"+(lastUpdateTime == null ? "空" : DateUtil.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，地区id:"+(regionId == null ? "空" : regionId)+"，地区名字:"+(regionName == null ? "空" : regionName);
	}
}
