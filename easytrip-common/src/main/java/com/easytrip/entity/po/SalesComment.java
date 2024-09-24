package com.easytrip.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.easytrip.entity.enums.DateTimePatternEnum;
import com.easytrip.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * 
 */
public class SalesComment implements Serializable {


	/**
	 * 景区评论id
	 */
	private Integer commentId;

	/**
	 * 景区id
	 */
	private String salesId;

	/**
	 * 父级评论id
	 */
	private Integer pCommentId;

	/**
	 * 评论用户
	 */
	private String userId;

	/**
	 * 用户昵称
	 */
	private String userName;

	/**
	 * 用户ip地址
	 */
	private String userIpAddress;

	/**
	 * 评论
	 */
	private String cotent;

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

	/**
	 * 回复人用户名
	 */
	private String replyUserName;

	/**
	 * 发布时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

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

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setUserIpAddress(String userIpAddress){
		this.userIpAddress = userIpAddress;
	}

	public String getUserIpAddress(){
		return this.userIpAddress;
	}

	public void setCotent(String cotent){
		this.cotent = cotent;
	}

	public String getCotent(){
		return this.cotent;
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

	public void setReplyUserName(String replyUserName){
		this.replyUserName = replyUserName;
	}

	public String getReplyUserName(){
		return this.replyUserName;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	@Override
	public String toString (){
		return "景区评论id:"+(commentId == null ? "空" : commentId)+"，景区id:"+(salesId == null ? "空" : salesId)+"，父级评论id:"+(pCommentId == null ? "空" : pCommentId)+"，评论用户:"+(userId == null ? "空" : userId)+"，用户昵称:"+(userName == null ? "空" : userName)+"，用户ip地址:"+(userIpAddress == null ? "空" : userIpAddress)+"，评论:"+(cotent == null ? "空" : cotent)+"，点赞数量:"+(goodCount == null ? "空" : goodCount)+"，用户旅游交通评分:"+(transportsfeel == null ? "空" : transportsfeel)+"，用户玩耍感受:"+(playfeel == null ? "空" : playfeel)+"，0:未置顶 1:已置顶:"+(topType == null ? "空" : topType)+"，回复人用户id:"+(replyUserId == null ? "空" : replyUserId)+"，回复人用户名:"+(replyUserName == null ? "空" : replyUserName)+"，发布时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，-1:删除0:已审核 1:待审核:"+(status == null ? "空" : status);
	}
}
