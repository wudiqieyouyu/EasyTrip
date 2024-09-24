package com.easytrip.entity.query;



/**
 * 参数
 */
public class Line2SalesQuery extends BaseParam {


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

	private String playTimeFuzzy;

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

	public void setPlayTimeFuzzy(String playTimeFuzzy){
		this.playTimeFuzzy = playTimeFuzzy;
	}

	public String getPlayTimeFuzzy(){
		return this.playTimeFuzzy;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

}
