package com.easytrip.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;


/**
 * 
 */
public class WebCarousel implements Serializable {


	/**
	 * 轮播图id
	 */
	private Integer carouselId;

	/**
	 * 图片
	 */
	private String imgPath;

	/**
	 * 0:分享文章 1:景点 2:酒店住宿 3 外部连接
	 */
	private Integer objectType;

	/**
	 * object_id
	 */
	private String objectId;

	/**
	 * 外部连接
	 */
	private String outerLink;

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

	public void setOuterLink(String outerLink){
		this.outerLink = outerLink;
	}

	public String getOuterLink(){
		return this.outerLink;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	@Override
	public String toString (){
		return "轮播图id:"+(carouselId == null ? "空" : carouselId)+"，图片:"+(imgPath == null ? "空" : imgPath)+"，0:分享文章 1:景点 2:酒店住宿 3 外部连接:"+(objectType == null ? "空" : objectType)+"，object_id:"+(objectId == null ? "空" : objectId)+"，外部连接:"+(outerLink == null ? "空" : outerLink)+"，排序:"+(sort == null ? "空" : sort);
	}
}
