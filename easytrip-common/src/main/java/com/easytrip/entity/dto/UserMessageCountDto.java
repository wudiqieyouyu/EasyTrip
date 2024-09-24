package com.easytrip.entity.dto;

public class UserMessageCountDto {
    private Long total = 0L;
    public Long sys = 0L;
    public Long reply = 0L;
    private Long likeComment = 0L;
    private Long likePost = 0L;


    public Long getLikePost() {
        return likePost;
    }

    public void setLikePost(Long likePost) {
        this.likePost = likePost;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getSys() {
        return sys;
    }

    public void setSys(Long sys) {
        this.sys = sys;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public Long getLikeComment() {
        return likeComment;
    }

    public void setLikeComment(Long likeComment) {
        this.likeComment = likeComment;
    }
}
