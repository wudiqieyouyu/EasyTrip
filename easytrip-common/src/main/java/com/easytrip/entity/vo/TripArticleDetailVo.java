package com.easytrip.entity.vo;

public class TripArticleDetailVo {
    private TripArticleVo tripArticleVo;
    private Boolean haveLike = false;

    public Boolean getHaveLike() {
        return haveLike;
    }

    public void setHaveLike(Boolean haveLike) {
        this.haveLike = haveLike;
    }

    public TripArticleVo getTripArticleVo() {
        return tripArticleVo;
    }

    public void setTripArticleVo(TripArticleVo tripArticleVo) {
        this.tripArticleVo = tripArticleVo;
    }
}
