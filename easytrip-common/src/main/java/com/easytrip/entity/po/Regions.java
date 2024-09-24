package com.easytrip.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;


/**
 * 
 */
public class Regions implements Serializable {


	/**
	 * 地区id
	 */
	private Integer regionId;

	/**
	 * 地区名字
	 */
	private String regionName;

	/**
	 * 父级地区id 
	 */
	private Integer parentRegionId;

	/**
	 * 0:洲 1:国 2:省/区域 3 市
	 */
	private Integer regionCategory;

	/**
	 * 官方语言
	 */
	private String language;

	/**
	 * 面积 单位是平方公里
	 */
	private Integer area;

	/**
	 * 货币种类 0:美元 1:欧元 2:人民币 3:日元 4: 英镑
5:加拿大元 6:澳大利亚元 7:印尼盾 8 :泰铢 9:卢比
10:其他
	 */
	private Integer currency;

	private List<Regions> children;

	public List<Regions> getChildren() {
		return children;
	}

	public void setChildren(List<Regions> children) {
		this.children = children;
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

	public void setParentRegionId(Integer parentRegionId){
		this.parentRegionId = parentRegionId;
	}

	public Integer getParentRegionId(){
		return this.parentRegionId;
	}

	public void setRegionCategory(Integer regionCategory){
		this.regionCategory = regionCategory;
	}

	public Integer getRegionCategory(){
		return this.regionCategory;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return this.language;
	}

	public void setArea(Integer area){
		this.area = area;
	}

	public Integer getArea(){
		return this.area;
	}

	public void setCurrency(Integer currency){
		this.currency = currency;
	}

	public Integer getCurrency(){
		return this.currency;
	}


}
