package com.easytrip.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;


/**
 * 文章板块信息
 */
public class TripBoard implements Serializable {


	/**
	 * 板块ID
	 */
	private Integer boardId;

	/**
	 * 父级板块ID
	 */
	private Integer pBoardId;

	/**
	 * 板块名
	 */
	private String boardName;

	/**
	 * 封面
	 */
	private String cover;

	/**
	 * 描述
	 */
	private String boardDesc;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 0:只允许管理员发帖 1:任何人可以发帖
	 */
	private Integer postType;

	/**
	 * 0:文章分类 1:问答分类 2:文章分类和问答分类
	 */
	private Integer type;

	private String bgColor;

	private List<TripBoard> children;

	public List<TripBoard> getChildren() {
		return children;
	}

	public void setChildren(List<TripBoard> children) {
		this.children = children;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public void setBoardId(Integer boardId){
		this.boardId = boardId;
	}

	public Integer getBoardId(){
		return this.boardId;
	}

	public void setpBoardId(Integer pBoardId){
		this.pBoardId = pBoardId;
	}

	public Integer getpBoardId(){
		return this.pBoardId;
	}

	public void setBoardName(String boardName){
		this.boardName = boardName;
	}

	public String getBoardName(){
		return this.boardName;
	}

	public void setCover(String cover){
		this.cover = cover;
	}

	public String getCover(){
		return this.cover;
	}

	public void setBoardDesc(String boardDesc){
		this.boardDesc = boardDesc;
	}

	public String getBoardDesc(){
		return this.boardDesc;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	public void setPostType(Integer postType){
		this.postType = postType;
	}

	public Integer getPostType(){
		return this.postType;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Integer getType(){
		return this.type;
	}

	@Override
	public String toString (){
		return "板块ID:"+(boardId == null ? "空" : boardId)+"，父级板块ID:"+(pBoardId == null ? "空" : pBoardId)+"，板块名:"+(boardName == null ? "空" : boardName)+"，封面:"+(cover == null ? "空" : cover)+"，描述:"+(boardDesc == null ? "空" : boardDesc)+"，排序:"+(sort == null ? "空" : sort)+"，0:只允许管理员发帖 1:任何人可以发帖:"+(postType == null ? "空" : postType)+"，0:文章分类 1:问答分类 2:文章分类和问答分类:"+(type == null ? "空" : type);
	}
}