package com.easytrip.entity.query;

import java.util.Date;


/**
 * 参数
 */
public class LineQuery extends BaseParam {


	/**
	 * 线路id
	 */
	private Integer lineId;

	/**
	 * 线路名称
	 */
	private String lineName;

	private String lineNameFuzzy;

	/**
	 * 线路描述
	 */
	private String description;

	private String descriptionFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 上次更新时间
	 */
	private String lastUpdateTime;

	private String lastUpdateTimeStart;

	private String lastUpdateTimeEnd;

	/**
	 * 地区id
	 */
	private Integer regionId;

	/**
	 * 地区名字
	 */
	private String regionName;

	private String regionNameFuzzy;


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

	public void setLineNameFuzzy(String lineNameFuzzy){
		this.lineNameFuzzy = lineNameFuzzy;
	}

	public String getLineNameFuzzy(){
		return this.lineNameFuzzy;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return this.description;
	}

	public void setDescriptionFuzzy(String descriptionFuzzy){
		this.descriptionFuzzy = descriptionFuzzy;
	}

	public String getDescriptionFuzzy(){
		return this.descriptionFuzzy;
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

	public void setLastUpdateTime(String lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdateTime(){
		return this.lastUpdateTime;
	}

	public void setLastUpdateTimeStart(String lastUpdateTimeStart){
		this.lastUpdateTimeStart = lastUpdateTimeStart;
	}

	public String getLastUpdateTimeStart(){
		return this.lastUpdateTimeStart;
	}
	public void setLastUpdateTimeEnd(String lastUpdateTimeEnd){
		this.lastUpdateTimeEnd = lastUpdateTimeEnd;
	}

	public String getLastUpdateTimeEnd(){
		return this.lastUpdateTimeEnd;
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

	public void setRegionNameFuzzy(String regionNameFuzzy){
		this.regionNameFuzzy = regionNameFuzzy;
	}

	public String getRegionNameFuzzy(){
		return this.regionNameFuzzy;
	}

}
