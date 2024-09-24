package com.easytrip.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;


/**
 * 
 */
public class Line2Sales implements Serializable {


	/**
	 * 
	 */
	private Integer lineId;

	/**
	 * 
	 */
	private Integer salesId;

	/**
	 * 游玩时间
	 */
	private String playTime;

	/**
	 * 排序
	 */
	private Integer sort;


	public void setLineId(Integer lineId){
		this.lineId = lineId;
	}

	public Integer getLineId(){
		return this.lineId;
	}

	public void setSalesId(Integer salesId){
		this.salesId = salesId;
	}

	public Integer getSalesId(){
		return this.salesId;
	}

	public void setPlayTime(String playTime){
		this.playTime = playTime;
	}

	public String getPlayTime(){
		return this.playTime;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	@Override
	public String toString (){
		return "lineId:"+(lineId == null ? "空" : lineId)+"，salesId:"+(salesId == null ? "空" : salesId)+"，游玩时间:"+(playTime == null ? "空" : playTime)+"，排序:"+(sort == null ? "空" : sort);
	}
}
