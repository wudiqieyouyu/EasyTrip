package com.easytrip.entity.query;



/**
 * 参数
 */
public class RegionsQuery extends BaseParam {


	/**
	 * 地区id
	 */
	private Integer regionId;

	/**
	 * 地区名字
	 */
	private String regionName;

	private String regionNameFuzzy;

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

	private String languageFuzzy;

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

	public Boolean getTree() {
		return Tree;
	}

	public void setTree(Boolean tree) {
		Tree = tree;
	}

	private Boolean
			Tree;
	private Boolean child;

	public Boolean getChild() {
		return child;
	}

	public void setChild(Boolean child) {
		this.child = child;
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

	public void setLanguageFuzzy(String languageFuzzy){
		this.languageFuzzy = languageFuzzy;
	}

	public String getLanguageFuzzy(){
		return this.languageFuzzy;
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
