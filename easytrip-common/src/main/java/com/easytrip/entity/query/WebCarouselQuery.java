package com.easytrip.entity.query;



/**
 * 参数
 */
public class WebCarouselQuery extends BaseParam {


	/**
	 * 轮播图id
	 */
	private Integer carouselId;

	/**
	 * 图片
	 */
	private String imgPath;

	private String imgPathFuzzy;

	/**
	 * 0:分享文章 1:景点 2:酒店住宿 3 外部连接
	 */
	private Integer objectType;

	/**
	 * object_id
	 */
	private String objectId;

	private String objectIdFuzzy;

	/**
	 * 外部连接
	 */
	private String outerLink;

	private String outerLinkFuzzy;

	/**
	 * 排序
	 */
	private Integer sort;


	public void setCarouselId(Integer carouselId){
		this.carouselId = carouselId;
	}

	public Integer getCarouselId(){
		return this.carouselId;
	}

	public void setImgPath(String imgPath){
		this.imgPath = imgPath;
	}

	public String getImgPath(){
		return this.imgPath;
	}

	public void setImgPathFuzzy(String imgPathFuzzy){
		this.imgPathFuzzy = imgPathFuzzy;
	}

	public String getImgPathFuzzy(){
		return this.imgPathFuzzy;
	}

	public void setObjectType(Integer objectType){
		this.objectType = objectType;
	}

	public Integer getObjectType(){
		return this.objectType;
	}

	public void setObjectId(String objectId){
		this.objectId = objectId;
	}

	public String getObjectId(){
		return this.objectId;
	}

	public void setObjectIdFuzzy(String objectIdFuzzy){
		this.objectIdFuzzy = objectIdFuzzy;
	}

	public String getObjectIdFuzzy(){
		return this.objectIdFuzzy;
	}

	public void setOuterLink(String outerLink){
		this.outerLink = outerLink;
	}

	public String getOuterLink(){
		return this.outerLink;
	}

	public void setOuterLinkFuzzy(String outerLinkFuzzy){
		this.outerLinkFuzzy = outerLinkFuzzy;
	}

	public String getOuterLinkFuzzy(){
		return this.outerLinkFuzzy;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

}
