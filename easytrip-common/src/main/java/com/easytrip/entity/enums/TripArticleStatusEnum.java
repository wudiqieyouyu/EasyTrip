package com.easytrip.entity.enums;


public enum TripArticleStatusEnum {
    DEL(0,"已刪除"),
    DISABLE(0, "未审核"),
    ENABLE(1, "已审核");


    private Integer status;
    private String desc;

    TripArticleStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static TripArticleStatusEnum getByStatus(Integer status) {
        for (TripArticleStatusEnum item : TripArticleStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
