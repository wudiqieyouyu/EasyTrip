package com.easytrip.entity.query;

import java.util.Date;


/**
 * 参数
 */
public class SalesCommentQuery extends BaseParam {


	/**
	 * 景区评论id
	 */
	private Integer commentId;

	/**
	 * 景区id
	 */
	private String salesId;

	private String salesIdFuzzy;

	/**
	 * 父级评论id
	 */
	private Integer pCommentId;

	/**
	 * 评论用户
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 用户昵称
	 */
	private String userName;

	private String userNameFuzzy;

	/**
	 * 用户ip地址
	 */
	private String userIpAddress;

	private String userIpAddressFuzzy;

	/**
	 * 评论
	 */
	private String cotent;

	private String cotentFuzzy;

	/**
	 * 点赞数量
	 */
	private Integer goodCount;

	/**
	 * 用户旅游交通评分
	 */
	private Integer transportsfeel;

	/**
	 * 用户玩耍感受
	 */
	private Integer playfeel;

	/**
	 * 0:未置顶 1:已置顶
	 */
	private Integer topType;

	/**
	 * 回复人用户id
	 */
	private String replyUserId;

	private String replyUserIdFuzzy;

	/**
	 * 回复人用户名
	 */
	private String replyUserName;

	private String replyUserNameFuzzy;

	/**
	 * 发布时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * -1:删除0:已审核 1:待审核
	 */
	private Integer status;


	public void setCommentId(Integer commentId){
		this.commentId = commentId;
	}

	public Integer getCommentId(){
		return this.commentId;
	}

	public void setSalesId(String salesId){
		this.salesId = salesId;
	}

	public String getSalesId(){
		return this.salesId;
	}

	public void setSalesIdFuzzy(String salesIdFuzzy){
		this.salesIdFuzzy = salesIdFuzzy;
	}

	public String getSalesIdFuzzy(){
		return this.salesIdFuzzy;
	}

	public void setpCommentId(Integer pCommentId){
		this.pCommentId = pCommentId;
	}

	public Integer getpCommentId(){
		return this.pCommentId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setUserIdFuzzy(String userIdFuzzy){
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy(){
		return this.userIdFuzzy;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setUserNameFuzzy(String userNameFuzzy){
		this.userNameFuzzy = userNameFuzzy;
	}

	public String getUserNameFuzzy(){
		return this.userNameFuzzy;
	}

	public void setUserIpAddress(String userIpAddress){
		this.userIpAddress = userIpAddress;
	}

	public String getUserIpAddress(){
		return this.userIpAddress;
	}

	public void setUserIpAddressFuzzy(String userIpAddressFuzzy){
		this.userIpAddressFuzzy = userIpAddressFuzzy;
	}

	public String getUserIpAddressFuzzy(){
		return this.userIpAddressFuzzy;
	}

	public void setCotent(String cotent){
		this.cotent = cotent;
	}

	public String getCotent(){
		return this.cotent;
	}

	public void setCotentFuzzy(String cotentFuzzy){
		this.cotentFuzzy = cotentFuzzy;
	}

	public String getCotentFuzzy(){
		return this.cotentFuzzy;
	}

	public void setGoodCount(Integer goodCount){
		this.goodCount = goodCount;
	}

	public Integer getGoodCount(){
		return this.goodCount;
	}

	public void setTransportsfeel(Integer transportsfeel){
		this.transportsfeel = transportsfeel;
	}

	public Integer getTransportsfeel(){
		return this.transportsfeel;
	}

	public void setPlayfeel(Integer playfeel){
		this.playfeel = playfeel;
	}

	public Integer getPlayfeel(){
		return this.playfeel;
	}

	public void setTopType(Integer topType){
		this.topType = topType;
	}

	public Integer getTopType(){
		return this.topType;
	}

	public void setReplyUserId(String replyUserId){
		this.replyUserId = replyUserId;
	}

	public String getReplyUserId(){
		return this.replyUserId;
	}

	public void setReplyUserIdFuzzy(String replyUserIdFuzzy){
		this.replyUserIdFuzzy = replyUserIdFuzzy;
	}

	public String getReplyUserIdFuzzy(){
		return this.replyUserIdFuzzy;
	}

	public void setReplyUserName(String replyUserName){
		this.replyUserName = replyUserName;
	}

	public String getReplyUserName(){
		return this.replyUserName;
	}

	public void setReplyUserNameFuzzy(String replyUserNameFuzzy){
		this.replyUserNameFuzzy = replyUserNameFuzzy;
	}

	public String getReplyUserNameFuzzy(){
		return this.replyUserNameFuzzy;
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

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

}
