package com.easytrip.entity.query;



/**
 * 文章板块信息参数
 */
public class TripBoardQuery extends BaseParam {


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

	private String boardNameFuzzy;

	/**
	 * 封面
	 */
	private String cover;

	private String coverFuzzy;

	/**
	 * 描述
	 */
	private String boardDesc;

	private String boardDescFuzzy;

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
	private Integer[] types;

	public Integer[] getTypes() {
		return types;
	}

	public void setTypes(Integer[] types) {
		this.types = types;
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

	public void setBoardNameFuzzy(String boardNameFuzzy){
		this.boardNameFuzzy = boardNameFuzzy;
	}

	public String getBoardNameFuzzy(){
		return this.boardNameFuzzy;
	}

	public void setCover(String cover){
		this.cover = cover;
	}

	public String getCover(){
		return this.cover;
	}

	public void setCoverFuzzy(String coverFuzzy){
		this.coverFuzzy = coverFuzzy;
	}

	public String getCoverFuzzy(){
		return this.coverFuzzy;
	}

	public void setBoardDesc(String boardDesc){
		this.boardDesc = boardDesc;
	}

	public String getBoardDesc(){
		return this.boardDesc;
	}

	public void setBoardDescFuzzy(String boardDescFuzzy){
		this.boardDescFuzzy = boardDescFuzzy;
	}

	public String getBoardDescFuzzy(){
		return this.boardDescFuzzy;
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

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
}
